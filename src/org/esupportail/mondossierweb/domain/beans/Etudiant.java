/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.web.controllers.Resettable;
import org.esupportail.mondossierweb.web.controllers.SessionController;

/**
 * classe qui représente l'étudiant dont on consulte le dossier.
 * @author Charlie Dubois
 */
public class Etudiant implements Resettable {

	/**
	 * bean qui fait le lien avec la BDD.
	 */
	private IEtudiant etudiantManager;
	/**
	 * le code individu.
	 */
	private String cod_ind;
	/**
	 * le code etudiant.
	 */
	private String cod_etu;
	/**
	 * le code NNE.
	 */
	private String cod_nne;
	/**
	 * le nom.
	 */
	private String nom;
	/**
	 * l'e-mail.
	 */
	private String email;
	/**
	 * email perso.
	 */
	private String emailPerso;
	/**
	 * telephone portable.
	 */
	private String telPortable;
	/**la nationalité.
	 * 
	 */
	private String nationalite;
	/**
	 * année de l'inscription universitaire.
	 */
	private String annee;
	/**
	 * établissement de l'inscription universitaire.
	 */
	private String etablissement;
	/**
	 * date de naissance.
	 */
	private String datenaissance;
	/**
	 * lieu de naissance.
	 */
	private String lieunaissance;
	/**
	 * département de naissance.
	 */
	private String departementnaissance;
	/**
	 * la liste des bac de l'étudiant.
	 */
	private ArrayList<BacEtatCivil> listeBac;
	/**
	 * url de la photo.
	 */
	private String urlphoto;
	/**
	 * l'année universiataire. EX : '2006-2007'.
	 */
	private String anu;
	/**
	 * type d'hébergement de l'adresse annuelle.
	 */
	private String adresseannuelleType;
	/**
	 * 1er partie de l'adresse annuelle.
	 */
	private String adresseannuelle1;
	/**
	 * 2em partie de l'adresse annuelle.
	 */
	private String adresseannuelle2;
	/**
	 * 3em partie de l'adresse annuelle.
	 */
	private String adresseannuelle3;
	/**
	 * ville etrangere.
	 */
	private String adresseannuelleetranger;
	/**
	 * code postal adresse annuelle.
	 */
	private String adresseannuellecp;
	/**
	 * numéro de téléphone de l'adresse annuelle.
	 */
	private String numerotelannuel;
	/**
	 * vile de l'adresse annuelle.
	 */
	private String villeannuelle;
	/**
	 * pays de l'adresse annuelle.
	 */
	private String paysannuel;
	/**
	 * 1er partie de l'adresse fixe.
	 */
	private String adressefixe1;
	/**
	 * 2em partie de l'adresse fixe.
	 */
	private String adressefixe2;
	/**
	 * 3em partie de l'adresse fixe.
	 */
	private String adressefixe3;
	/**
	 * ville etrangere.
	 */
	private String adressefixeetranger;
	/**
	 * numéro de téléphone de l'adresse fixe.
	 */
	private String numerotelfixe;
	/**
	 * code postal adresse fixe.
	 */
	private String adressefixecp;
	/**
	 * ville de l'adresse fixe.
	 */
	private String villefixe;
	/**
	 * pays de l'adresse fixe.
	 */
	private String paysfixe;
	/**
	 * établissement où est inscrit l'étudiant.
	 */
	private String lib_etb;
	/**
	 * année de sa première inscription.
	 */
	private String anneePremiereInscrip;
	/**
	 * établissement de sa première inscription.
	 */
	private String etbPremiereInscrip;
	/**
	 * liste des inscription dans l'établissement en cours.
	 */
	private List<Inscription> linsciae;
	/**
	 * liste des autres cursus suivis.
	 */
	private List<Inscription> linscdac;
	/**
	 * vrai si des inscriptions dans d'autres cursus existe.
	 */
	private boolean existeInsDac;
	/**
	 * liste des ELP d'une inscription pédagogique.
	 */
	private List<ElementPedagogique> elementsInscriptionPedagogique;
	/**
	 * liste des examens prévus.
	 */
	private List<Examen> calendrier;
	/**
	 * vrai si des examens sont prévus dans le calendrier.
	 */
	private boolean existeExam;
	/**
	 * les diplomes avec les résultats obtenus.
	 */
	private List<Diplome> diplomes;
	/**
	 * vrai si des diplomes sont enregistrés.
	 */
	private boolean existeDip;
	/**
	 * les etapes avec les résultats obtenus.
	 */
	private List<Etape> etapes;
	/**
	 * vrai si les résultat à l'épreuve sont définitifs.
	 */
	private boolean deliberationTerminee;
	/**
	 * vrai si des etapes sont enregistrés.
	 */
	private boolean existeEtape;
	/**
	 * la liste des éléments pédagogique (avec résultats) d'une étape choisie.
	 */
	private List<ElementPedagogique> elementsPedagogiques;
	/**
	 * le cache du résultat et des notes déjà récupérés.
	 */
	private CacheResultats cacheResultats;
	/**
	 * vrai si les adresses sont renseignées.
	 */
	private boolean adressesRenseignees;
	/**
	 * vrai si les inscriptions sont renseignées.
	 */
	private boolean inscriptionsRenseignees;
	/**
	 * vrai si l'inscription pédagogique renseignées.
	 */
	private boolean inscriptionPedagogiqueRenseignees;
	/**
	 * vrai si les infos sur l'etatCivil sont renseignées.
	 */
	private boolean etatCivilRenseigne;
	/**
	 * vrai si les infos sur les notes et résultats sont renseignées.
	 */
	private boolean notesRenseignees;
	/**
	 * vrai si les infos sur les notes de l'épreuve sélectionnée sont renseignées.
	 */
	private boolean notesEtapeRenseignees;
	/**
	 * map qui contient les couples (indice,signification) pour les résultats.
	 */
	private Map allSignificationResultats;
	/**
	 * map qui contient les couples (indice,signification) pour les résultats à afficher.
	 */
	private Map significationResultats;
	/**
	 * vrai si significationResultats n'est pas vide.
	 */
	private boolean significationResultatsUtilisee;
	/**
	 * chaine pour l'affichage de la signification des résultats.
	 */
	private String grilleSignficationResultats;

	/**
	 * liste des examens prévus (organisés et affichés par étape).
	 */
	private List<ExamensEtape> calendrierEtape;

	/**
	 * vrai si des examens (organisés et affichés par étape) sont prévus dans le calendrier.
	 */
	private boolean existeExamEtape;

	/**
	 * liste des calendriers de rentrée prévus.
	 */
	private List<CalendrierRentree> calendrierRentree;

	/**
	 * vrai si un calendrier de rentrée existe.
	 */
	private boolean existeCalendrierRentree;
	/**
	 * consctructeur.
	 *
	 */
	public Etudiant() {
		super();
		linsciae = new ArrayList<Inscription>();
		linscdac = new ArrayList<Inscription>();
		calendrier =  new ArrayList<Examen>();
		calendrierEtape =  new ArrayList<ExamensEtape>();
		calendrierRentree =  new ArrayList<CalendrierRentree>();
		diplomes = new ArrayList<Diplome>();
		etapes = new ArrayList<Etape>();
		elementsPedagogiques = new ArrayList<ElementPedagogique>();
		elementsInscriptionPedagogique = new ArrayList<ElementPedagogique>();
		listeBac = new ArrayList<BacEtatCivil>();
		adressesRenseignees = true;
		inscriptionsRenseignees = true;
		etatCivilRenseigne = true;
		existeExam = true;
		existeExamEtape = true;
		existeCalendrierRentree = true;
		notesRenseignees = true;
		allSignificationResultats = new HashMap<String, String>();
		significationResultats = new HashMap<String, String>();
		significationResultatsUtilisee = false;
		grilleSignficationResultats = "";
		deliberationTerminee = false;
		cacheResultats = new CacheResultats();
	}

	/**
	 * initialise l'étudiant car on va en 'charger' un nouveau.
	 * on ne fais pas new Etudiant() car on perdrait l'etudiantManager
	 * renseigné dans le fichier xml de config.
	 *
	 */
	public void reset() {
		linsciae.clear();
		linscdac.clear();
		calendrier.clear();
		calendrierEtape.clear();
		calendrierRentree.clear();
		diplomes.clear();
		etapes.clear();
		listeBac.clear();
		elementsPedagogiques.clear();
		elementsInscriptionPedagogique.clear();
		cod_etu = "";
		telPortable = "";
		emailPerso = "";
		email = "";
		cod_nne = "";
		nom = "";
		adresseannuelle1 = "";
		adresseannuelle2 = "";
		adresseannuelle3 = "";
		adresseannuellecp = "";
		adresseannuelleetranger = "";
		adresseannuelleType = "";
		adressefixe1 = "";
		adressefixe2 = "";
		adressefixe3 = "";
		adressefixecp = "";
		adressefixeetranger = "";
		numerotelannuel = "";
		numerotelfixe = "";
		paysannuel = "";
		paysfixe = "";
		villeannuelle = "";
		villefixe = "";
		lib_etb = "";
		adressesRenseignees = true;
		inscriptionsRenseignees = true;
		etatCivilRenseigne = true;
		existeExam = true;
		existeExamEtape = true;
		existeCalendrierRentree = true;
		notesRenseignees = true;
		significationResultats.clear();
		significationResultatsUtilisee = false;
		grilleSignficationResultats = "";
		urlphoto = "";
		if(cacheResultats != null){
			if(cacheResultats.getResultVdiVet()!=null){
				cacheResultats.getResultVdiVet().clear();
			}
			if(cacheResultats.getResultElpEpr()!=null){
				cacheResultats.getResultElpEpr().clear();
			}
		}else{
			cacheResultats = new CacheResultats();
		}
	}
	public void renseigneCodInd() {
		etudiantManager.setCodInd(this);
	}

	public void renseignePhoto() {
		etudiantManager.setPhoto(this);
	}
	public void renseigneEtatCivil() {
		etudiantManager.setEtatCivil(this);
	}
	public void renseigneAdresses() {
		etudiantManager.setAdresses(this);
	}
	public boolean majAdresses() {
		return etudiantManager.setMajAdresses(this);
	}
	public void renseigneInscriptions() {
		etudiantManager.setInscriptions(this);
	}
	public void renseigneCalendrier() {
		etudiantManager.setCalendrier(this);
	}
	public void renseigneCalendrierEtape() {
		etudiantManager.setCalendrier(this);
	}
	public void renseigneCalendrierRentree() {
		etudiantManager.setCalendrierRentree(this);
	}
	/**
	 * 
	 * @param vueEtudiant
	 * @return le rang dans la liste des Notes et Résultat (aux diplomes et étapes) en cache pour la vueEtudiant
	 */
	private String getRangNotesEtResultatsEnCache(boolean vueEtudiant){
		int rang=0;
		boolean enCache=false;

		//on parcourt le résultatVdiVet pour voir si on a ce qu'on cherche:
		for(CacheResultatsVdiVet crvv : cacheResultats.getResultVdiVet()){
			if(!enCache){
				//si on a déjà les infos:
				if(crvv.isVueEtudiant() == vueEtudiant){
					enCache=true;
				}else{
					//on a pas trouvé, on incrémente le rang pour se placer sur le rang suivant
					rang++;
				}
			}
		}

		//si on a pas les infos en cache:
		if(!enCache){
			return null;
		}

		return ""+rang;

	}

	/**
	 * récupère les résultat aux diplomes et etapes dans le cache (en s'indexant sur le rang)
	 * @param rang
	 */
	private void recupererCacheResultatVdiVet(int rang){
		//1-on vide les listes existantes
		if(diplomes!=null){
			diplomes.clear();
		}
		if(etapes!=null){
			etapes.clear();
		}
		//2-on récupère les infos du cache.
		diplomes = new LinkedList<Diplome>(cacheResultats.getResultVdiVet().get(rang).getDiplomes());
		etapes = new LinkedList<Etape>(cacheResultats.getResultVdiVet().get(rang).getEtapes());
		
		
	}

	/**
	 * On complète les infos du cache pour les Résultats aux diplomes et étapes.
	 * @param vueEtudiant
	 */
	public void ajouterCacheResultatVdiVet(boolean vueEtudiant){
		CacheResultatsVdiVet crvv = new CacheResultatsVdiVet();
		crvv.setVueEtudiant(vueEtudiant);
		crvv.setDiplomes(new LinkedList<Diplome>(diplomes));
		crvv.setEtapes(new LinkedList<Etape>(etapes));
		cacheResultats.getResultVdiVet().add(crvv);
	}

	public void renseigneNotesEtResultats() {
		//On regarde si on a pas déjà les infos dans le cache:
		String rang = getRangNotesEtResultatsEnCache(true);

		if(rang == null){
			etudiantManager.setNotesEtResultats(this);
			//AJOUT DES INFOS recupérées dans le cache. true car on est en vue Etudiant
			ajouterCacheResultatVdiVet(true);
		}else{
			//on récupére les infos du cache grace au rang :
			recupererCacheResultatVdiVet(new Integer(rang));
		}
	}


	public void renseigneNotesEtResultatsVueEnseignant() {
		//On regarde si on a pas déjà les infos dans le cache:
		String rang = getRangNotesEtResultatsEnCache(false);
		if(rang == null){
			etudiantManager.setNotesEtResultatsEnseignant(this);
			//AJOUT DES INFOS recupérées dans le cache. true car on est en vue Etudiant
			ajouterCacheResultatVdiVet(false);
		}else{
			//on récupére les infos du cache grace au rang :
			recupererCacheResultatVdiVet(new Integer(rang));
		}
	}
	
	
	
	/**
	 * 
	 * @param etape
	 * @param vueEtudiant
	 * @return  le rang dans la liste des Notes et Résultat (aux elp et epr) en cache pour la vueEtudiant
	 */
	private String getRangNotesEtResultatsEnCache(Etape etape, boolean vueEtudiant){
		int rang=0;
		boolean enCache=false;

		//on parcourt le résultatElpEpr pour voir si on a ce qu'on cherche:
		for(CacheResultatsElpEpr cree : cacheResultats.getResultElpEpr()){
			if(!enCache){
				//si on a déjà les infos:
				if(cree.getEtape().getAnnee().equals(etape.getAnnee())
						&& cree.getEtape().getCode().equals(etape.getCode())
						&& cree.getEtape().getVersion().equals(etape.getVersion())
						&& cree.isVueEtudiant() == vueEtudiant){
					enCache=true;
				}else{
					//on a pas trouvé, on incrémente le rang pour se placer sur le rang suivant
					rang++;
				}
			}
		}

		//si on a pas les infos en cache:
		if(!enCache){
			return null;
		}
		
		return ""+rang;

	}
	
	
	/**
	 * On complète les infos du cache pour les Résultats aux elp et epr.
	 * @param vueEtudiant
	 */
	public void ajouterCacheResultatElpEpr(Etape etape, boolean vueEtudiant){
		CacheResultatsElpEpr cree = new CacheResultatsElpEpr();
		cree.setVueEtudiant(vueEtudiant);
		cree.setEtape(etape);
		cree.setElementsPedagogiques(new LinkedList<ElementPedagogique>(elementsPedagogiques));
		cacheResultats.getResultElpEpr().add(cree);
		
	}
	
	
	/**
	 * récupère les résultat aux Elp et Epr dans le cache (en s'indexant sur le rang)
	 * @param rang
	 */
	private void recupererCacheResultatElpEpr(int rang){
		//1-on vide la liste existante
		if(elementsPedagogiques!=null){
			elementsPedagogiques.clear();
		}
		
		
		//2-on récupère les infos du cache.
		elementsPedagogiques = new LinkedList<ElementPedagogique>(cacheResultats.getResultElpEpr().get(rang).getElementsPedagogiques());
		
		
	}
	
	
	public void renseigneNotesElpEpr(int index) {
		
		//On regarde si on a pas déjà les infos dans le cache:
		String rang = getRangNotesEtResultatsEnCache(etapes.get(index),true);

		if(rang == null){
			//On regarde si les notes globales sont en cache pour récupérer l'etape qui va avec la vue.
			String rangNoteGlobal = getRangNotesEtResultatsEnCache(true);
			if(rangNoteGlobal == null){
				//on recupere les info des notes Globales et on les met dans le cache
				etudiantManager.setNotesEtResultats(this);
				ajouterCacheResultatVdiVet(true);
			}else{
				recupererCacheResultatVdiVet(new Integer(rangNoteGlobal));
			}
			
			etudiantManager.setNotesElpEpr(this, etapes.get(index));
			//AJOUT DES INFOS recupérées dans le cache. true car on est en vue Etudiant
			ajouterCacheResultatElpEpr(etapes.get(index), true);
		}else{
			//on récupére les infos du cache grace au rang :
			recupererCacheResultatElpEpr(new Integer(rang));
		}
	
	}
	
	public void renseigneNotesElpEprVueEnseignant(int index) {
		//On regarde si on a pas déjà les infos dans le cache:
		String rang = getRangNotesEtResultatsEnCache(etapes.get(index),false);

		if(rang == null){
			//On regarde si les notes globales sont en cache pour récupérer l'etape qui va avec la vue.
			String rangNoteGlobal = getRangNotesEtResultatsEnCache(false);
			if(rangNoteGlobal == null){
				//on recupere les info des notes Globales et on les met dans le cache
				etudiantManager.setNotesEtResultatsEnseignant(this);
				ajouterCacheResultatVdiVet(false);
			}else{
				recupererCacheResultatVdiVet(new Integer(rangNoteGlobal));
			}
			
			etudiantManager.setNotesElpEprEnseignant(this, etapes.get(index));
			//AJOUT DES INFOS recupérées dans le cache. false car on est en vue Enseignant
			ajouterCacheResultatElpEpr(etapes.get(index), false);
		}else{
			//on récupére les infos du cache grace au rang :
			recupererCacheResultatElpEpr(new Integer(rang));
		}
		
	
	}

	public void recupererCacheIP(int rang){
		//1-on vide la liste existante
		if(elementsInscriptionPedagogique!=null){
			elementsInscriptionPedagogique.clear();
		}
		
		
		//2-on récupère les infos du cache.
		elementsInscriptionPedagogique = new LinkedList<ElementPedagogique>(cacheResultats.getResultElpEpr().get(rang).getElementsPedagogiques());
		
		
	}
	
	
	/**
	 * @return cod_ind
	 */
	public String getCod_ind() {
		if (cod_ind == null || cod_ind.equals("")) {
			renseigneCodInd();
		}
		return cod_ind;
	}

	public void setCod_etu(String cod_etu) {
		this.cod_etu = cod_etu;
	}
	public String getNom() {
		if (nom == null || nom.equals("")) {
			renseigneEtatCivil();
		}
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getCod_nne() {
		if (cod_nne == null || cod_nne.equals("")) {
			renseigneEtatCivil();
		}
		return cod_nne;
	}
	public void setCod_nne(String cod_nne) {
		this.cod_nne = cod_nne;
	}
	public String getCod_etu() {
		return cod_etu;
	}
	public void setCod_ind(String cod_ind) {
		this.cod_ind = cod_ind;
	}
	public IEtudiant getEtudiantManager() {
		return etudiantManager;
	}
	public void setEtudiantManager(IEtudiant etudiantManager) {
		this.etudiantManager = etudiantManager;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAnnee() {
		return annee;
	}

	public void setAnnee(String annee) {
		this.annee = annee;
	}


	public String getDatenaissance() {
		return datenaissance;
	}

	public void setDatenaissance(String datenaissance) {
		this.datenaissance = datenaissance;
	}


	public String getDepartementnaissance() {
		return departementnaissance;
	}

	public void setDepartementnaissance(String departementnaissance) {
		this.departementnaissance = departementnaissance;
	}

	public String getEtablissement() {
		return etablissement;
	}

	public void setEtablissement(String etablissement) {
		this.etablissement = etablissement;
	}






	public ArrayList<BacEtatCivil> getListeBac() {
		return listeBac;
	}

	public void setListeBac(ArrayList<BacEtatCivil> listeBac) {
		this.listeBac = listeBac;
	}

	public String getLieunaissance() {
		return lieunaissance;
	}

	public void setLieunaissance(String lieunaissance) {
		this.lieunaissance = lieunaissance;
	}


	public String getNationalite() {
		return nationalite;
	}

	public void setNationalite(String nationalite) {
		this.nationalite = nationalite;
	}


	public String getUrlphoto() {
		if(urlphoto == null || urlphoto.equals("")) {
			renseignePhoto();
		}
		return urlphoto;
	}

	public void setUrlphoto(String urlphoto) {
		this.urlphoto = urlphoto;
	}




	/**
	 * @return adresseannuelleType.
	 */
	public String getAdresseannuelleType() {
		return adresseannuelleType;
	}

	/**
	 * @param adresseannuelleType
	 */
	public void setAdresseannuelleType(String adresseannuelleType) {
		this.adresseannuelleType = adresseannuelleType;
	}

	/**
	 * @return la 1er partie de l'adresse annuelle.
	 */
	public String getAdresseannuelle1() {
		return adresseannuelle1;
	}

	public void setAdresseannuelle1(String adresseannuelle1) {
		this.adresseannuelle1 = adresseannuelle1;
	}

	public String getAdresseannuelle2() {
		return adresseannuelle2;
	}

	public void setAdresseannuelle2(String adresseannuelle2) {
		this.adresseannuelle2 = adresseannuelle2;
	}

	public String getAdresseannuelle3() {
		return adresseannuelle3;
	}

	public void setAdresseannuelle3(String adresseannuelle3) {
		this.adresseannuelle3 = adresseannuelle3;
	}

	public String getAdressefixe1() {
		return adressefixe1;
	}

	public void setAdressefixe1(String adressefixe1) {
		this.adressefixe1 = adressefixe1;
	}

	public String getAdressefixe2() {
		return adressefixe2;
	}

	public void setAdressefixe2(String adressefixe2) {
		this.adressefixe2 = adressefixe2;
	}

	public String getAdressefixe3() {
		return adressefixe3;
	}

	public void setAdressefixe3(String adressefixe3) {
		this.adressefixe3 = adressefixe3;
	}

	public String getNumerotelannuel() {
		return numerotelannuel;
	}

	public void setNumerotelannuel(String numerotelannuel) {
		this.numerotelannuel = numerotelannuel;
	}

	public String getNumerotelfixe() {
		return numerotelfixe;
	}

	public void setNumerotelfixe(String numerotelfixe) {
		this.numerotelfixe = numerotelfixe;
	}

	public String getPaysannuel() {
		return paysannuel;
	}

	public void setPaysannuel(String paysannuel) {
		this.paysannuel = paysannuel;
	}

	public String getPaysfixe() {
		return paysfixe;
	}

	public void setPaysfixe(String paysfixe) {
		this.paysfixe = paysfixe;
	}


	public String getAdresseannuellecp() {
		return adresseannuellecp;
	}

	public void setAdresseannuellecp(String adresseannuellecp) {
		this.adresseannuellecp = adresseannuellecp;
	}

	public String getAdressefixecp() {
		return adressefixecp;
	}

	public void setAdressefixecp(String adressefixecp) {
		this.adressefixecp = adressefixecp;
	}

	public String getVilleannuelle() {
		return villeannuelle;
	}

	public void setVilleannuelle(String villeannuelle) {
		this.villeannuelle = villeannuelle;
	}

	public String getVillefixe() {
		return villefixe;
	}

	public void setVillefixe(String villefixe) {
		this.villefixe = villefixe;
	}

	public String getAnneePremiereInscrip() {
		return anneePremiereInscrip;
	}

	public void setAnneePremiereInscrip(String anneePremiereInscrip) {
		this.anneePremiereInscrip = anneePremiereInscrip;
	}

	public String getEtbPremiereInscrip() {
		return etbPremiereInscrip;
	}

	public void setEtbPremiereInscrip(String etbPremiereInscrip) {
		this.etbPremiereInscrip = etbPremiereInscrip;
	}


	/**
	 * @return l'établissement actuel de l'étudiant.
	 */
	public String getLib_etb() {
		return lib_etb;
	}

	public void setLib_etb(String lib_etb) {
		this.lib_etb = lib_etb;
	}

	public List<Inscription> getLinscdac() {
		return linscdac;
	}

	public void setLinscdac(List<Inscription> linscdac) {
		this.linscdac = linscdac;
	}

	public List<Inscription> getLinsciae() {
		return linsciae;
	}

	public void setLinsciae(List<Inscription> linsciae) {
		this.linsciae = linsciae;
	}

	public List<Examen> getCalendrier() {
		return calendrier;
	}

	public void setCalendrier(List<Examen> calendrier) {
		this.calendrier = calendrier;
	}

	/**
	 * @return vrai si des examens sont programmés dans le calendrier.
	 */
	public boolean isExisteExam() {
		if (existeExam) {
			//on vérifie que le calendrier est effectivement renseigné:
			if (calendrier.size() < 1) {
				renseigneCalendrier();
				//c'est la premiere fois qu'on veut accéder au calendrier
				if (calendrier.size() < 1) {
					//on arrive pas a récupérer les infos
					existeExam = false;
				}
			}

		}
		return existeExam;
	}

	/**
	 * @return vrai si des examens sont programmés dans le calendrier Rennes 1.
	 */
	public boolean isExisteExamEtape() {
		if (existeExamEtape) {
			//on vérifie que le calendrier est effectivement renseigné:
			if (calendrierEtape.size() < 1) {
				renseigneCalendrierEtape();
				//c'est la premiere fois qu'on veut accéder au calendrier
				if (calendrierEtape.size() < 1) {
					//on arrive pas a récupérer les infos
					existeExamEtape = false;
				}
			}

		}
		return existeExamEtape;
	}

	/**
	 * @return vrai si des calendriers de rentrée sont saisis dans le calendrier Rennes 1.
	 */
	public boolean isExisteCalendrierRentree() {
		if (existeCalendrierRentree) {
			//on vérifie que le calendrier est effectivement renseigné:
			if (calendrierRentree.size() < 1) {
				renseigneCalendrierRentree();
				//c'est la premiere fois qu'on veut accéder au calendrier
				if (calendrierRentree.size() < 1) {
					//on arrive pas a récupérer les infos
					existeCalendrierRentree = false;
				}
			}

		}
		return existeCalendrierRentree;
	}

	public void setExisteExam(boolean existeExam) {
		this.existeExam = existeExam;
	}

	public String getAnu() {
		return anu;
	}

	public void setAnu(String anu) {
		this.anu = anu;
	}

	public List<Diplome> getDiplomes() {
		return diplomes;
	}

	public void setDiplomes(List<Diplome> diplomes) {
		this.diplomes = diplomes;
	}

	public List<Etape> getEtapes() {
		return etapes;
	}

	public void setEtapes(List<Etape> etapes) {
		this.etapes = etapes;
	}

	public boolean isExisteDip() {
		existeDip = true;
		if (diplomes == null || diplomes.size() < 1) {
			existeDip = false;
		}
		return existeDip;
	}

	public void setExisteDip(boolean existeDip) {
		this.existeDip = existeDip;
	}

	public boolean isExisteEtape() {
		existeEtape = true;
		if (etapes.size() < 1) {
			existeEtape = false;
		}
		return existeEtape;
	}

	public void setExisteEtape(boolean existeEtape) {
		this.existeEtape = existeEtape;
	}

	public boolean isExisteInsDac() {
		existeInsDac = false;
		if (linscdac.size() > 0) {
			existeInsDac = true;
		}
		return existeInsDac;
	}

	public void setExisteInsDac(boolean existeInsDac) {
		this.existeInsDac = existeInsDac;
	}

	public List<ElementPedagogique> getElementsPedagogiques() {
		return elementsPedagogiques;
	}

	public void setElementsPedagogiques(
			List<ElementPedagogique> elementsPedagogiques) {
		this.elementsPedagogiques = elementsPedagogiques;
	}

	public boolean isAdressesRenseignees() {
		if (adressesRenseignees) {
			//on vérifie que anu est effectivement renseigné:
			if (adresseannuelle1 == null || adresseannuelle1.equals("")) {
				//c'est la premiere fois qu'on veut accéder aux adresses
				renseigneAdresses();
				if (adresseannuelle1 == null || adresseannuelle1.equals("")) {
					//on arrive pas a récupérer les infos
					adressesRenseignees = false;
				}
			}
		}
		return adressesRenseignees;
	}

	public void setAdressesRenseignees(boolean adressesRenseignees) {
		this.adressesRenseignees = adressesRenseignees;
	}

	
	public boolean isInscriptionsRenseignees() {
		if (inscriptionsRenseignees) {
			//on vérifie que le lib_etb est effectivement renseigné:
			if (lib_etb == null || lib_etb.equals("")) {
				//c'est la premiere fois qu'on veut accéder aux inscriptions
				renseigneInscriptions();
				if (lib_etb == null || lib_etb.equals("")) {
					//on arrive pas a récupérer les infos
					inscriptionsRenseignees = false;
				}
			}
		}
		return inscriptionsRenseignees;
	}

	public void setInscriptionsRenseignees(boolean inscriptionsRenseignees) {
		this.inscriptionsRenseignees = inscriptionsRenseignees;
	}

	public boolean isEtatCivilRenseigne() {
		if (etatCivilRenseigne) {
			//on vérifie que le cod_ind et le cod_nne sont effectivement renseignés:
			if (cod_ind == null || cod_ind.equals("") || cod_nne == null || cod_nne.equals("")) {
				//c'est la premiere fois qu'on veut accéder à l'état-civil
				renseigneEtatCivil();
				if (cod_ind == null || cod_ind.equals("")) {
					//on arrive pas a récupérer les infos
					etatCivilRenseigne = false;
				}
			}
		}
		return etatCivilRenseigne;
	}

	public void setEtatCivilRenseigne(boolean etatCivilRenseigne) {
		this.etatCivilRenseigne = etatCivilRenseigne;
	}

	public boolean isNotesRenseignees() {
		//notesRenseignees = false si on arrive pas a récupérer les notes.
		//si on ne fait pas ce test , l'algo boucle.
		if (notesRenseignees) {
			//on vérifie que les listes sont effectivement renseignées:
			if (diplomes.size() < 1 && etapes.size() < 1) {
				//c'est la premiere fois qu'on veut accéder aux notes

				//on test si l'utilisateur est un enseignant
				SessionController session = (SessionController)  BeanUtils.getBean("sessionController");
				if(session.isEnseignant()) {
					//on récupère tous les résultats
					//renseigneNotesEtResultats();//pour vue etudiant
					renseigneNotesEtResultatsVueEnseignant();// pour vue enseignant
				} else {
					//on récupère les résultats dont la délibération est terminée
					renseigneNotesEtResultats();
				}
				if (diplomes.size() < 1 && etapes.size() < 1) {
					//on arrive pas a récupérer les infos
					notesRenseignees = false;
				}
			}
		}
		return notesRenseignees;
	}

	public void setNotesRenseignees(boolean notesRenseignees) {
		this.notesRenseignees = notesRenseignees;
	}

	public Map getSignificationResultats() {
		return significationResultats;
	}

	public void setSignificationResultats(Map significationResultats) {
		this.significationResultats = significationResultats;
	}

	public boolean isNotesEtapeRenseignees() {
		notesEtapeRenseignees = true;
		if (elementsPedagogiques.size() < 1) {
			notesEtapeRenseignees = false;
		}
		return notesEtapeRenseignees;
	}

	public void setNotesEtapeRenseignees(boolean notesEtapeRenseignees) {
		this.notesEtapeRenseignees = notesEtapeRenseignees;
	}

	public boolean isSignificationResultatsUtilisee() {
		significationResultatsUtilisee = true;
		if (significationResultats.isEmpty()) {
			significationResultatsUtilisee = false;
		}
		return significationResultatsUtilisee;
	}

	public void setSignificationResultatsUtilisee(
			boolean significationResultatsUtilisee) {
		this.significationResultatsUtilisee = significationResultatsUtilisee;
	}

	public String getGrilleSignficationResultats() {
		grilleSignficationResultats = "";
		//grilleSignficationResultats = significationResultats.toString().substring(1,significationResultats.toString().length()-1);
		Set<String> ss = significationResultats.keySet();
		for(String k : ss){
			if(k != null && !k.equals("") && !k.equals(" ")){
				grilleSignficationResultats = grilleSignficationResultats + "<b>"+k+"</b>&#160;:&#160;"+ significationResultats.get(k);
				grilleSignficationResultats = grilleSignficationResultats + "&#160;&#160;&#160;";
			}
		}

		return grilleSignficationResultats;
	}

	public String getGrilleSignficationResultatsPdf() {
		grilleSignficationResultats = "";
		//grilleSignficationResultats = significationResultats.toString().substring(1,significationResultats.toString().length()-1);
		Set<String> ss = significationResultats.keySet();
		for(String k : ss){
			if(k != null && !k.equals("") && !k.equals(" ")){
				grilleSignficationResultats = grilleSignficationResultats + k+" : "+ significationResultats.get(k);
				grilleSignficationResultats = grilleSignficationResultats + "   ";
			}
		}

		return grilleSignficationResultats;
	}

	public void setGrilleSignficationResultats(String grilleSignficationResultats) {
		this.grilleSignficationResultats = grilleSignficationResultats;
	}

	public String getAdresseannuelleetranger() {
		return adresseannuelleetranger;
	}

	public void setAdresseannuelleetranger(String adresseannuelleetranger) {
		this.adresseannuelleetranger = adresseannuelleetranger;
	}

	public String getAdressefixeetranger() {
		return adressefixeetranger;
	}

	public void setAdressefixeetranger(String adressefixeetranger) {
		this.adressefixeetranger = adressefixeetranger;
	}

	public boolean isDeliberationTerminee() {
		return deliberationTerminee;
	}

	public void setDeliberationTerminee(boolean deliberationTerminee) {
		this.deliberationTerminee = deliberationTerminee;
	}

	public String getEmailPerso() {
		return emailPerso;
	}

	public void setEmailPerso(String emailPerso) {
		this.emailPerso = emailPerso;
	}

	public String getTelPortable() {
		return telPortable;
	}

	public void setTelPortable(String telPortable) {
		this.telPortable = telPortable;
	}

	public Map getAllSignificationResultats() {
		return allSignificationResultats;
	}

	public void setAllSignificationResultats(Map allSignificationResultats) {
		this.allSignificationResultats = allSignificationResultats;
	}

	public void setCalendrierRentree(List<CalendrierRentree> calendrierRentree) {
		this.calendrierRentree = calendrierRentree;
	}

	public List<CalendrierRentree> getCalendrierRentree() {
		return calendrierRentree;
	}

	/**
	 * @param calendrierExamenR1 the calendrierExamenR1 to set
	 */
	public void setCalendrierEtape(List<ExamensEtape> calendrierEtape) {
		this.calendrierEtape = calendrierEtape;
	}

	/**
	 * @return the calendrierExamenR1
	 */
	public List<ExamensEtape> getCalendrierEtape() {
		return calendrierEtape;
	}

	/**
	 * @param existeExamR1 the existeExamR1 to set
	 */
	public void setExisteExamEtape(boolean existeExamEtape) {
		this.existeExamEtape = existeExamEtape;
	}

	public void setExisteCalendrierRentree(boolean existeCalendrierRentree) {
		this.existeCalendrierRentree = existeCalendrierRentree;

	}

	public CacheResultats getCacheResultats() {
		return cacheResultats;
	}

	public void setCacheResultats(CacheResultats cacheResultats) {
		this.cacheResultats = cacheResultats;
	}

	public String toString(){
		return "Bean ETUDIANT :  -codind="+cod_ind+" -codetu="+cod_etu+" -nom="+nom+" -dateNaissance="+datenaissance;
	}

	public List<ElementPedagogique> getElementsInscriptionPedagogique() {
		return elementsInscriptionPedagogique;
	}

	public void setElementsInscriptionPedagogique(
			List<ElementPedagogique> elementsInscriptionPedagogique) {
		this.elementsInscriptionPedagogique = elementsInscriptionPedagogique;
	}

	public boolean isInscriptionPedagogiqueRenseignees() {
		inscriptionPedagogiqueRenseignees = true;
		if(elementsInscriptionPedagogique == null || elementsInscriptionPedagogique.size()<1){
			inscriptionPedagogiqueRenseignees = false;
		}
		return inscriptionPedagogiqueRenseignees;
	}

	public void setInscriptionPedagogiqueRenseignees(
			boolean inscriptionPedagogiqueRenseignees) {
		this.inscriptionPedagogiqueRenseignees = inscriptionPedagogiqueRenseignees;
	}
	


}
