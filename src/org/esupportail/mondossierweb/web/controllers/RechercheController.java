/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;


import gouv.education.apogee.commun.transverse.exception.WebBaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.domain.beans.Composante;
import org.esupportail.mondossierweb.domain.beans.ElementArborescence;
import org.esupportail.mondossierweb.domain.beans.Etape;
import org.esupportail.mondossierweb.dto.ObjetRecherche;
import org.esupportail.mondossierweb.web.navigation.View;

/**
 * controller pour le parcourt de l'arborescence
 * à la recherche de l'étape ou de l'élément pédagogique voulu.
 * @author Charlie Dubois
 */
public class RechercheController extends AbstractContextAwareController {

	private static final long serialVersionUID = -2161066039618935231L;
	public String typeComposante = "COMPOSANTE";
	public String typeDiplome = "DIPLOME";
	public String typeEtape = "ETAPE";
	public String typeElp = "ELP";
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(RechercheController.class);
	/**
	 * Le service.
	 */
	private IDaoService service;
	/**
	 * le controller qui stocke les inscrits pour l'affichage.
	 */
	private ListeInscritsController listeInscritsController;
	/**
	 * l'annee.
	 */
	private String annee;
	/**
	 * le type de recherche (Etape ou Elément pédagogique).
	 */
	private String type;
	/**
	 * resultat session1.
	 */
	private boolean session1;
	/**
	 * resultat session2.
	 */
	private boolean session2;
	/**
	 * resultat etape appartenance.
	 */
	private boolean etape;

	/**
	 * l'objet courant.
	 */
	private ElementArborescence objcourant;
	/**
	 * l'objet composantes courant.
	 */
	private ElementArborescence composantes;
	/**
	 * l'objet diplomes courant.
	 */
	private ElementArborescence diplomes;
	/**
	 * l'objet courant.
	 */
	private ArrayList<ElementArborescence> chemin;
	/**
	 * l'objet courant.
	 */
	private boolean aucunFils;
	/**
	 * index du dernier element du chemin à afficher avec un lien de retour.
	 */
	private int finchemin;
	/**
	 * le controller qui recherche les groupes par codELP père.
	 */
	private RechercheCodeController rechercheCodeController;

	/**
	 * initialise le controller.
	 *
	 */
	public RechercheController() {
		super();
		type = "Etape ou diplôme ou groupe";
		session1 = false;
		session2 = false;
		etape = false;
		chemin = new ArrayList<ElementArborescence>();
		finchemin = 0;
	}

	public String retourNavigationArborescence(){
		//test si on a déjà effectué une recherche dans l'arborescence
		if(chemin != null){
			//test si on s'est arrêté après la sélection de la composante
			if(objcourant.getTypeFils().equals(typeDiplome))
				return View.DIPLOMES_ARBO;
			//test si on s'est arrêté après la sélection de l'étape
			if(objcourant.getTypeFils().equals(typeEtape))
				return View.ETAPES_ARBO;
			//test si on s'est arrêté après la sélection d'un elp
			if(objcourant.getTypeFils().equals(typeElp))
				return View.ELEMENTS_ARBO;
			//test si on s'est arrêté après la validation de la recherche
			if(objcourant.getTypeFils().equals(typeComposante))
				return View.COMPOSANTS_ARBO;
		}
		return View.INDEX_ENS;

	}

	/**
	 * valide le formulaire et redirige vers la vue adéquate.
	 * @return la prochaine vue.
	 */
	public String chercher() {
		String message = "";
		boolean erreur = false;
		if (!(annee != null && !annee.equals(""))) {
			message = "Veuillez entrer une annee";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			erreur = true;
		} else {
			if (!Pattern.matches("^[0-9]{4}", annee)) {
				message = "L'année doit être composée de 4 chiffres";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			}
		}

		if (!erreur) {		
			if (type.equals("Etape")) {
				etape = false;
			}

			if (chemin != null) {
				chemin.clear();
			}
			objcourant = new ElementArborescence();
			objcourant.setPosition(0);
			objcourant.setListeFils(service.getComposantes());
			objcourant.setTypeFils(typeComposante);
			composantes = new ElementArborescence();
			composantes = (ElementArborescence)objcourant.clone();
			return View.COMPOSANTS_ARBO;
		}	

		return View.CHERCHE_ARBO;
	}
	/**
	 * initialise le formulaire.
	 * @return la vue du formulaire.
	 */
	public String enter() {
		annee = service.getAnneeEnCours();
		type = "Etape ou diplôme ou groupe";
		session1 = false;
		session2 = false;
		etape = false;
		return View.CHERCHE_ARBO;
	}

	/**
	 * détermine les diplômes composants la composante sélectionnée.
	 * @return la vue sur les diplomes de la composante sélectionée.
	 */
	public String chercheDiplomes() {

		//on cherche les diplomes de la composante sélectionée
		FacesContext context = FacesContext.getCurrentInstance();
		String cod = (String) context.getExternalContext().getRequestParameterMap().get("code");


		//test si dans le cas d'un retour arrière dans le navigateur:
		if (chemin.size() > 0) {
			objcourant.getListeFils().clear();
			objcourant = (ElementArborescence) chemin.get(0).clone();
			chemin.clear();
		}

		//objcourant.setLibelle((String) context.getExternalContext().getRequestParameterMap().get("lib"));
		List<Composante> lc = composantes.getListeFils();
		for(Composante c : lc){
			if(c.getCod_cmp().equals(cod))
				objcourant.setLibelle(c.getLib_cmp());
		}
		objcourant.setCode(cod);
		objcourant.setTypeFils(typeDiplome);
		objcourant.setVersion("");
		objcourant.setPosition(0);
		ObjetRecherche objrech = new ObjetRecherche(annee, cod);
		objcourant.setListeFils(service.getDiplomes(objrech));
		chemin.clear();
		ElementArborescence objchemin = (ElementArborescence) objcourant.clone();

		chemin.add(objchemin);

		diplomes = new ElementArborescence();
		diplomes = (ElementArborescence) objcourant.clone();

		return View.DIPLOMES_ARBO;
	}


	/**
	 * cherche les éléments fils de l'élément sélectionné
	 * ou redirige vers la liste des incrits.
	 * @return la prochaine vue.
	 */
	public String chercheEtapesDepuisDiplome() {

		FacesContext context = FacesContext.getCurrentInstance();
		String cod = (String) context.getExternalContext().getRequestParameterMap().get("code");
		String vers = (String) context.getExternalContext().getRequestParameterMap().get("vers");


		/*
		 * TEST RETOUR NAVIGATEUR
		 * test si dans le cas d'un retour arrière dans le navigateur:
		 */
		String codeobjcourant = (String) context.getExternalContext().getRequestParameterMap().get("codeobjcourant");
		if (!codeobjcourant.equals(objcourant.getCode())) {
			if (chemin.size() > 1) {
				//on vérifie qu'on a pas cliqué sur un chemin n'existant plus:
				if(chemin.get(0).getCode().equals(codeobjcourant)) {
					objcourant.getListeFils().clear();
					objcourant = (ElementArborescence) chemin.get(0).clone();
					//on supprime les éléments inutiles du chemin
					for (int i = chemin.size() - 1; i > 0; i--) {
						chemin.remove(i);
					}
				} else {
					//on a reculé sur un chemin n'existant plus: erreur, on retourne a la recherche
					return erreurRetourNavigateur();
				}
			} else {
				//on a reculé sur un chemin n'existant plus: erreur, on retourne a la recherche
				return erreurRetourNavigateur();
			}
		} 
		if (chemin.size() > 1) {
			//on supprime les éléments inutiles du chemin
			for (int i = chemin.size() - 1; i > 0; i--) {
				chemin.remove(i);
			}
		}

		/*
		 * FIN TEST RETOUR NAVIGATEUR
		 */

		ObjetRecherche o = new ObjetRecherche(annee, cod, vers);

		List<Etape> letapes=service.getEtape(o);

		if (type.equals("Etape")) {
			//on cherche les inscrits

			//test si le diplome n'a qu'une étape:
			if(letapes != null && letapes.size()==1){
				Etape et = (Etape) service.getEtape(o).get(0);
				renseigneController(et.getCode(), et.getVersion(), "Etape", true,false);
				return listeInscritsController.enter();
			}

		}


		objcourant.setCode(cod);

		objcourant.setLibelle(service.getDiplome(o));
		objcourant.setVersion(vers);
		objcourant.setPosition(1);

		if(letapes != null && letapes.size()>1){

			//on cherche les etapes
			objcourant.setListeFils(letapes);
			objcourant.setTypeFils(typeEtape);
			ElementArborescence objchemin = (ElementArborescence) objcourant.clone();

			chemin.add(objchemin);
			return View.ETAPES_ARBO;
		}else{
			//on cherche les elp de la seule étape du diplome
			Etape etape=(Etape)letapes.get(0);
			objcourant.setListeFils(service.getElements(etape));
			objcourant.setTypeFils(typeElp);
			ElementArborescence objchemin = (ElementArborescence) objcourant.clone();
			chemin.add(objchemin);
			return View.ELEMENTS_ARBO;
		}


	}



	/**
	 * cherche les éléments fils de l'élément sélectionné
	 * ou redirige vers la liste des incrits.
	 * @return la prochaine vue.
	 */
	public String chercheElementsDepuisEtape() {

		FacesContext context = FacesContext.getCurrentInstance();
		String cod = (String) context.getExternalContext().getRequestParameterMap().get("code");
		String vers = (String) context.getExternalContext().getRequestParameterMap().get("vers");


		/*
		 * TEST RETOUR NAVIGATEUR
		 * test si dans le cas d'un retour arrière dans le navigateur:
		 */
		String codeobjcourant = (String) context.getExternalContext().getRequestParameterMap().get("codeobjcourant");
		if (!codeobjcourant.equals(objcourant.getCode())) {
			if (chemin.size() > 2) {
				//on vérifie qu'on a pas cliqué sur un chemin n'existant plus:
				if(chemin.get(1).getCode().equals(codeobjcourant)) {
					objcourant.getListeFils().clear();
					objcourant = (ElementArborescence) chemin.get(1).clone();
					//on supprime les éléments inutiles du chemin
					for (int i = chemin.size() - 1; i > 1; i--) {
						chemin.remove(i);
					}
				} else{
					//on a reculé sur un chemin n'existant plus: erreur, on retourne a la recherche
					return erreurRetourNavigateur();
				}

			} else {
				//on a reculé sur un chemin n'existant plus: erreur, on retourne a la recherche
				return erreurRetourNavigateur();
			}
		} 
		if (chemin.size() > 2) {
			//on supprime les éléments inutiles du chemin
			for (int i = chemin.size() - 1; i > 1; i--) {
				chemin.remove(i);
			}
		}
		/*
		 * FIN TEST RETOUR NAVIGATEUR
		 */

		if (type.equals("Etape")) {
			//on cherche les inscrits
			renseigneController(cod, vers, "Etape", false, true);
			return listeInscritsController.enter();
		}

		//on cherche les éléments pédagogiques ou les groupes
		objcourant.setCode(cod);
		ObjetRecherche objr = new ObjetRecherche();
		objr.setCode(cod);
		objr.setVersion(vers);
		objcourant.setLibelle(service.getLibelleEtape(objr));
		objcourant.setVersion(vers);
		objcourant.setPosition(2);
		Etape et = new Etape();
		et.setAnnee(annee);
		et.setCode(cod);
		et.setVersion(vers);
		et.setLibelle(objcourant.getLibelle());
		objcourant.setListeFils(service.getElements(et));
		objcourant.setTypeFils(typeElp);
		ElementArborescence objchemin = (ElementArborescence) objcourant.clone();

		chemin.add(objchemin);
		return View.ELEMENTS_ARBO;



	}
	/**
	 * cherche les éléments fils de l'élément sélectionné
	 * ou redirige vers la liste des incrits.
	 * @return la prochaine vue.
	 */
	public String chercheElementsDepuisElement() {

		FacesContext context = FacesContext.getCurrentInstance();
		String cod = (String) context.getExternalContext().getRequestParameterMap().get("code");
		//String vers = (String) context.getExternalContext().getRequestParameterMap().get("vers");




		/*
		 * TEST RETOUR NAVIGATEUR
		 * on va tester si dans le cas d'un retour arriere du navigateur:
		 */
		String l = (String) context.getExternalContext().getRequestParameterMap().get("longueurchemin");
		int longueur = Integer.parseInt(l) + 1;
		String codeancienobjcourant = (String) context.getExternalContext().getRequestParameterMap().get("codeobjcourant");
		String versancienobjcourant = (String) context.getExternalContext().getRequestParameterMap().get("versobjcourant");
		String actuelobjcourantid = objcourant.getCode();

		if (!versancienobjcourant.equals("element")) {
			//on est à la suite de la sélection d'une diplome, on doit ajouter la version l'id
			actuelobjcourantid = actuelobjcourantid + objcourant.getVersion();
			codeancienobjcourant = codeancienobjcourant + versancienobjcourant;
		}
		if (!codeancienobjcourant.equals(actuelobjcourantid)) {
			if (longueur < chemin.size()) {
				String idelementduchemin = chemin.get(longueur - 1).getCode();

				if (!versancienobjcourant.equals("element")) {
					//on est à la suite de la sélection d'une diplome, on doit prendre en compate la version dans l'id
					idelementduchemin = idelementduchemin + chemin.get(longueur - 1).getVersion();
				}

				if (codeancienobjcourant.equals(idelementduchemin)) {
					//on a reculé sur un élément toujours dans le chemin.
					objcourant.getListeFils().clear();
					objcourant = (ElementArborescence) chemin.get(longueur - 1).clone();
					//on supprime les éléments inutiles du chemin
					for (int i = chemin.size() - 1; i > (longueur - 1); i--) {
						chemin.remove(i);
					}

				} else {
					//on a reculé sur un chemin n'existant plus: erreur, on retourne a la recherche
					return erreurRetourNavigateur();
				}
			} else {
				//on a reculé sur un chemin n'existant plus: erreur, on retourne a la recherche
				return erreurRetourNavigateur();
			}
		}
		/*
		 * FIN TEST RETOUR NAVIGATEUR.
		 */

		//on doit tester si on affiche les inscrits 
		//ou si on cherche les sous-éléments
		String select = (String) context.getExternalContext().getRequestParameterMap().get("select");



		if (select.equals("false") && service.hasSousElements(cod)) {

			objcourant.setCode(cod);
			objcourant.setLibelle(service.getLibelleElementPedagogique(cod));
			objcourant.setPosition(objcourant.getPosition() + 1);
			objcourant.setListeFils(service.getSousElements(cod));
			objcourant.setTypeFils(typeElp);
			objcourant.setVersion("element");
			ElementArborescence objchemin = (ElementArborescence) objcourant.clone();
			chemin.add(objchemin);
			return View.ELEMENTS_ARBO;
		}
		//Si on cherche les ELP
		if(!type.equals("Groupe")){
			//on renvoi au listeInscritController:
			renseigneController(cod, "", "Elément Pédagogique", false,false);
			return listeInscritsController.enter();
		}

		//Si on cherche les groupes
		//Recherche des groupes d'un ELP
		return rechercheCodeController.enterViaArborescencePourGroupe(annee, cod);


	}



	/**
	 * renseigne le controller qui liste les inscrits.
	 * @param code
	 * @param version
	 * @param typ
	 */
	private void renseigneController(final String code, final String version, final String typ, final boolean pardiplomes, final boolean paretapes) {
		listeInscritsController.setAnnee(annee);
		listeInscritsController.setCode(code);
		listeInscritsController.setVersion(version);
		listeInscritsController.setEtape(etape);
		listeInscritsController.setSession1(session1);
		listeInscritsController.setSession2(session2);
		listeInscritsController.setType(typ);
		listeInscritsController.setRecherchecode(false);
		listeInscritsController.setParDiplomes(pardiplomes);
		listeInscritsController.setParEtapes(paretapes);

	}

	/**
	 * retourne à l'objet du chemin sélectionné.
	 * @return la vue adéquate
	 */
	public String chercherRetour() {

		FacesContext context = FacesContext.getCurrentInstance();
		
		String paramRang = (String) context.getExternalContext().getRequestParameterMap().get("rang");
		int rang = 0;
		
		if(paramRang != null && !paramRang.equals("")){
			rang = new Integer(paramRang);
		}else{
			return null;
		}
		
		String code = (String) context.getExternalContext().getRequestParameterMap().get("code");
		String version = (String) context.getExternalContext().getRequestParameterMap().get("version");
	
		
		/*
		 * TEST du cas de retour du navigateur:
		 */
		if ((chemin.size() - 1) < rang) {
			//on a reculé sur un chemin n'existant plus: erreur, on retourne a la recherche
			return erreurRetourNavigateur();
		}
		if (!(chemin.get(rang).getCode().equals(code) && chemin.get(rang).getVersion().equals(version))) {
			//on a reculé sur un chemin n'existant plus: erreur, on retourne a la recherche
			return erreurRetourNavigateur();
		}
		/*
		 * FIN TEST du cas de retour du navigateur
		 */


		objcourant.getListeFils().clear();
		objcourant = (ElementArborescence) chemin.get(rang).clone();


		//on supprime les éléments inutiles du chemin
		for (int i = chemin.size() - 1; i > rang; i--) {
			chemin.remove(i);
		}

		if(chemin.get(rang).getTypeFils().equals(typeElp))
			return View.ELEMENTS_ARBO;

		if(chemin.get(rang).getTypeFils().equals(typeDiplome))
			return View.DIPLOMES_ARBO;

		if(chemin.get(rang).getTypeFils().equals(typeEtape))
			return View.ETAPES_ARBO;

		if(chemin.get(rang).getTypeFils().equals(typeComposante))
			return View.COMPOSANTS_ARBO;

		return View.CHERCHE_ARBO;
	}

	/**
	 * retourne en affichant une erreur.
	 * @return au 1er formulaire de recherche
	 */
	public String erreurRetourNavigateur() {
		String message = "Vous utilisez trop le retour arriere du navigateur. Essayez d'utiliser le chemin affiché au dessus de la liste quand vous souhaitez revenir en arrière";
		FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
		FacesContext.getCurrentInstance().addMessage(null, messageX);

		return View.CHERCHE_ARBO;
	}

	/**
	 * reinitialise le formulaire.
	 * @return la page d'accueil.
	 */
	public String retour() {
		annee = service.getAnneeEnCours();
		return View.INDEX_ENS;
	}

	/**
	 * détermine le rang du dernier élément du chemin.
	 * Car il doit être affiché différemment des autres.
	 * @return le rang du dernier élément du chemin.
	 */
	public int getFinchemin() {
		finchemin = chemin.size() - 2;
		return finchemin;
	}

	/**
	 * @return anne
	 */
	public String getAnnee() {
		return annee;
	}
	/**
	 * @param annee
	 */
	public void setAnnee(final String annee) {
		this.annee = annee;
	}
	/**
	 * @return service
	 */
	public IDaoService getService() {
		return service;
	}
	/**
	 * @param service
	 */
	public void setService(final IDaoService service) {
		this.service = service;
	}

	/**
	 * @return type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type
	 */
	public void setType(final String type) {
		this.type = type;
	}
	/**
	 * @return etape
	 */
	public boolean isEtape() {
		return etape;
	}
	/**
	 * @param etape
	 */
	public void setEtape(final boolean etape) {
		this.etape = etape;
	}
	/**
	 * @return session1
	 */
	public boolean isSession1() {
		return session1;
	}
	/**
	 * @param session1
	 */
	public void setSession1(final boolean session1) {
		this.session1 = session1;
	}
	/**
	 * @return session2
	 */
	public boolean isSession2() {
		return session2;
	}
	/**
	 * @param session2
	 */
	public void setSession2(final boolean session2) {
		this.session2 = session2;
	}
	/**
	 * @return objcourant
	 */
	public ElementArborescence getObjcourant() {
		return objcourant;
	}
	/**
	 * @param objcourant
	 */
	public void setObjcourant(final ElementArborescence objcourant) {
		this.objcourant = objcourant;
	}

	/**
	 * @return listeInscritsController
	 */
	public ListeInscritsController getListeInscritsController() {
		return listeInscritsController;
	}
	/**
	 * @param listeInscritsController
	 */
	public void setListeInscritsController(
			final ListeInscritsController listeInscritsController) {
		this.listeInscritsController = listeInscritsController;
	}
	/**
	 * 
	 * @return vrai si l'objet courant n'a aucun fils.
	 */
	public boolean isAucunFils() {
		aucunFils = true;
		if (objcourant != null && objcourant.getListeFils() != null && objcourant.getListeFils().size() > 0) {
			aucunFils = false;
		}
		return aucunFils;
	}

	/**
	 * @param aucunFils
	 */
	public void setAucunFils(final boolean aucunFils) {
		this.aucunFils = aucunFils;
	}
	/**
	 * @return chemin
	 */
	public ArrayList<ElementArborescence> getChemin() {
		return chemin;
	}
	/**
	 * @param chemin
	 */
	public void setChemin(final ArrayList<ElementArborescence> chemin) {
		this.chemin = chemin;
	}


	/**
	 * @param finchemin
	 */
	public void setFinchemin(final int finchemin) {
		this.finchemin = finchemin;
	}

	/**
	 * 
	 * @return composantes.
	 */
	public ElementArborescence getComposantes() {
		return composantes;
	}
	/**
	 * 
	 * @param composantes
	 */
	public void setComposantes(final ElementArborescence composantes) {
		this.composantes = composantes;
	}
	/**
	 * 
	 * @return diplomes
	 */
	public ElementArborescence getDiplomes() {
		return diplomes;
	}
	/**
	 * 
	 * @param diplomes
	 */
	public void setDiplomes(final ElementArborescence diplomes) {
		this.diplomes = diplomes;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RechercheController:  -annee : "+annee
		+" type : "+type
		+" sesion1 : "+session1
		+" session2 :"+session2
		+" etape :"+etape
		+" objcourant :"+objcourant
		+" chemin"+chemin
		+" listeInscrits : "+listeInscritsController;
	}

	public String getTypeComposante() {
		return typeComposante;
	}

	public void setTypeComposante(String typeComposante) {
		this.typeComposante = typeComposante;
	}

	public String getTypeDiplome() {
		return typeDiplome;
	}

	public void setTypeDiplome(String typeDiplome) {
		this.typeDiplome = typeDiplome;
	}

	public String getTypeEtape() {
		return typeEtape;
	}

	public void setTypeEtape(String typeEtape) {
		this.typeEtape = typeEtape;
	}

	public String getTypeElp() {
		return typeElp;
	}

	public void setTypeElp(String typeElp) {
		this.typeElp = typeElp;
	}

	public RechercheCodeController getRechercheCodeController() {
		return rechercheCodeController;
	}

	public void setRechercheCodeController(
			RechercheCodeController rechercheCodeController) {
		this.rechercheCodeController = rechercheCodeController;
	}




}
