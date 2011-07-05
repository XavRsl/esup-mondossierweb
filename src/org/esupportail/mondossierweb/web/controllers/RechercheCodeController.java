/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;


import gouv.education.apogee.commun.transverse.exception.WebBaseException;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.CharSet;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.domain.beans.CollectionDeGroupes;
import org.esupportail.mondossierweb.domain.beans.ElpDeCollection;
import org.esupportail.mondossierweb.domain.beans.Etape;
import org.esupportail.mondossierweb.domain.beans.Inscrit;
import org.esupportail.mondossierweb.dto.ObjetRecherche;
import org.esupportail.mondossierweb.web.navigation.View;

/**
 * le controller de la recherche d'une étape ou d'un élément pédagogique par son code.
 * @author Charlie Dubois
 */
public class RechercheCodeController extends AbstractContextAwareController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2051626399885299540L;
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(RechercheCodeController.class);
	/**
	 * Le service.
	 */
	private IDaoService service;
	/**
	 * l'annee.
	 */
	private String annee;
	/**
	 * le type de recherche (Etape ou Elément pédagogique).
	 */
	private String type;
	/**
	 * le code.
	 */
	private String code;
	/**
	 * le code version.
	 */
	private String version;
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
	 * libelle du groupe selectionne
	 */
	private String libGpe;
	/**
	 * la liste de collection résultat d'une recherche de groupes.
	 */
	private List<ElpDeCollection> listeElpDeCollection;
	/**
	 * vrai si la liste listeElpDeCollection est non vide
	 */
	private boolean existeGroupe;
	/**
	 * le controller qui stocke les inscrits pour l'affichage.
	 */
	private ListeInscritsController listeInscritsController;
	/**
	 * vrai si le controller est appelé a partir du formulaire de recherche par code
	 * faux si le controller est appelé à partir du parcours de l'arborescence
	 */
	private boolean rechercheParCode;

	/**
	 * initialise le formulaire.
	 *
	 */
	public RechercheCodeController() {
		super();
		type = "Etape";
		session1 = false;
		session2 = false;
		etape = false;
		listeElpDeCollection = new LinkedList<ElpDeCollection>();
		libGpe = "";
		rechercheParCode = true;
	}

	/**
	 * valide le formulaire et transmet au controller qui liste les inscrits.
	 * @return la vue suivante
	 */
	public String chercher() {
		try {
			rechercheParCode = true;
			boolean erreur = false;
			String message = "";
			libGpe = "";
			if (!(annee != null && !annee.equals(""))) {
				message = "Veuillez entrer une année";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			} else {
				annee = annee.trim();
				if (!Pattern.matches("^[0-9]{4}", annee)) {
					message = "L'année doit être composée de 4 chiffres";
					FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
					FacesContext.getCurrentInstance().addMessage(null, messageX);
					erreur = true;
				}
			}

			if (!(code != null && !code.equals(""))) {
				message = "Veuillez entrer un code";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			}else{
				code=code.trim();
			}
			if (type.equals("Etape")  &&    !(version != null && !version.equals(""))) {
				message = "Veuillez entrer une version";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			}else{
				if (type.equals("Etape")){
					version = version.trim();
				}
			}
			if (type.equals("Etape")  &&    !(Pattern.matches("^[0-9]*", version))) {
				message = "Version mal renseignée";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			}

			if (!erreur) {		

				if (type.equals("Etape")) {
					//Recherche d'une Etape
					etape = false;
					Etape e = new Etape(code, version, annee);
					int check = service.checkEtape(e);
					if (check > 0) {
						renseigneController();
						return listeInscritsController.enter();
					} 
					message = "Etape inexistante";
					FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
					FacesContext.getCurrentInstance().addMessage(null, messageX);

				} else {
					if (type.equals("Groupe")) {
						//Recherche des groupes d'un ELP
						int check = service.checkElementPedagogique(code);
						if (check > 0) {

							//on cherche les groupes
							if(listeElpDeCollection != null && listeElpDeCollection.size() > 0){
								listeElpDeCollection.clear();
							}
							try{
								listeElpDeCollection = service.recupererGroupes(annee,code);

								return View.AFFICHE_RECHERCHE_GROUPES;
							}catch(WebBaseException e){
								message = "Aucun groupe récupéré pour cet ELP";
								FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
								FacesContext.getCurrentInstance().addMessage(null, messageX);

							}
						} else{
							message = "Elément pédagogique inexistant";
							FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
							FacesContext.getCurrentInstance().addMessage(null, messageX);
						}
					}else{
						//Recherche Element pédagogique
						int check = service.checkElementPedagogique(code);
						if (check > 0) {
							renseigneController();
							return listeInscritsController.enter();
						} 
						message = "Elément pédagogique inexistant";
						FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
						FacesContext.getCurrentInstance().addMessage(null, messageX);
					}
				}
			}	

			return View.CHERCHE_CODE_ARBO;
		} catch(Exception e) {
			LOG.error(e);
			return View.EXCEPTION_ERROR;
		}
	}
	
	public String enterViaArborescencePourGroupe(String anne, String cod){
		annee = anne;
		code = cod;
		type = "Groupe";
		rechercheParCode = false;
		
		int check = service.checkElementPedagogique(code);
		if (check > 0) {

			//on cherche les groupes
			if(listeElpDeCollection != null && listeElpDeCollection.size() > 0){
				listeElpDeCollection.clear();
			}
			try{
				listeElpDeCollection = service.recupererGroupes(annee,code);

				
			}catch(WebBaseException e){
				//message = "Aucun groupe récupéré pour cet ELP";
				//FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				//FacesContext.getCurrentInstance().addMessage(null, messageX);

			}
		}
		return View.AFFICHE_RECHERCHE_GROUPES;
	}

	/**
	 * @return la vue du formulaire
	 */
	public String enter() {
		try {
			annee = service.getAnneeEnCours();
		} catch(Exception e) {
			LOG.error(e);
			LOG.error("iBATIS : probleme de récupération de l'année en cours dans la base");
			return View.EXCEPTION_ERROR;
		}
		type = "Etape";
		code = "";
		version = "";
		libGpe = "";
		session1 = false;
		session2 = false;
		rechercheParCode = true;
		etape = false;
		return View.CHERCHE_CODE_ARBO;
	}

	/**
	 * @return la page d'accueil
	 */
	public String retour() {
		annee = service.getAnneeEnCours();
		code = "";
		version = "";
		libGpe = "";
		rechercheParCode = true;
		return View.INDEX_ENS;
	}

	/**
	 * 
	 * @return
	 */
	public String validGroupe(){

		//recupère les infos du groupe sélectionné dans les attributs a passer a listeInscritsController
		FacesContext context = FacesContext.getCurrentInstance();
		code = (String) context.getExternalContext().getRequestParameterMap().get("code");
		
		libGpe = service.getLibelleGroupe(code);

		renseigneController();
		return listeInscritsController.enter();
	}
	/**
	 * renseigne les attributs utiles du controller listeInscritsController.
	 */
	private void renseigneController() {
		listeInscritsController.setAnnee(annee);
		listeInscritsController.setCode(code);
		listeInscritsController.setVersion(version);
		listeInscritsController.setEtape(etape);
		listeInscritsController.setSession1(session1);
		listeInscritsController.setSession2(session2);
		listeInscritsController.setType(type);
		listeInscritsController.setLibelle(libGpe);
		listeInscritsController.setRecherchecode(true);
	}




	/**
	 * @return annee
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
	 * @return code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code
	 */
	public void setCode(final String code) {
		this.code = code;
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
	 * @return version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version
	 */
	public void setVersion(final String version) {
		this.version = version;
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
	 * @return listeElpDeCollection
	 */
	public List<ElpDeCollection> getListeElpDeCollection() {
		return listeElpDeCollection;
	}
	/**
	 * 
	 * @param listeElpDeCollection
	 */
	public void setListeElpDeCollection(List<ElpDeCollection> listeElpDeCollection) {
		this.listeElpDeCollection = listeElpDeCollection;
	}
	/**
	 * 
	 * @return libGpe
	 */
	public String getLibGpe() {
		return libGpe;
	}
	/**
	 * 
	 * @param libGpe
	 */
	public void setLibGpe(String libGpe) {
		this.libGpe = libGpe;
	}

	public boolean isExisteGroupe() {
		existeGroupe = false;
		if(listeElpDeCollection != null && listeElpDeCollection.size()>0){
			existeGroupe = true;
		}
		return existeGroupe;
	}

	public void setExisteGroupe(boolean existeGroupe) {
		this.existeGroupe = existeGroupe;
	}

	public boolean isRechercheParCode() {
		return rechercheParCode;
	}

	public void setRechercheParCode(boolean rechercheParCode) {
		this.rechercheParCode = rechercheParCode;
	}




}
