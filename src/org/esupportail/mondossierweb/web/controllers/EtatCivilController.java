/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.mondossierweb.domain.beans.Etudiant;
import org.esupportail.mondossierweb.web.navigation.View;
import org.esupportail.mondossierweb.web.photo.IPhoto;





/**
 * le controller de l'affichage de l'état-civil de l'étudiant.
 * @author Charlie Dubois
 *
 */
public class EtatCivilController extends AbstractContextAwareController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2732605935591124734L;

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(EtatCivilController.class);
	
	/**
	 * le bean pour récupérer les photos.
	 */
	private IPhoto photo;
	
	/**
	 * L'étudiant dont on consulte le dossier.
	 */
	private Etudiant etudiant;
	
	


	/**
	 * savoir quand on est dans le dossier etudiant si on y est arrivé depuis 
	 * une liste d'étudiant ou pas.
	 * grace a vientDuneListe on peut afficher un bouton de retour dans les
	 * pages du dossierEtudiant pour l'utilisateur enseignant
	 */
	private boolean vientDuneListe;

	/**
	 * savoir quand on est dans le dossier etudiant si on y est arrivé depuis 
	 * le trombinoscope ou pas.
	 * grace a vientDuTrombi on peut afficher un bouton de retour dans les
	 * pages du dossierEtudiant pour l'utilisateur enseignant
	 */
	private boolean vientDuTrombi;

	/**
	 * savoir quand on est dans le dossier etudiant si on y est arrivé depuis 
	 * la recherche en tapant le numéro ou pas.
	 * grace a vientDuNumero on peut afficher un bouton de retour dans les
	 * pages du dossierEtudiant pour l'utilisateur enseignant
	 */
	private boolean vientDuNumero;
	
	/**
	 * savoir quand on est dans le dossier etudiant si on y est arrivé depuis 
	 * la recherche dans l'annuaire ou pas.
	 * grace a vientDeAnnuaire on peut afficher un bouton de retour dans les
	 * pages du dossierEtudiant pour l'utilisateur enseignant
	 */
	private boolean vientDeAnnuaire;
	

	/**
	 * Constructor.
	 */
	public EtatCivilController() {
		super();
	}
	
	



	/**
	 * @return true if the current user is allowed to view the page.
	 */
	public boolean isPageAuthorized() {
		return true;
	}

	/**
	 * on arrive à la vue de l'état-civil d'un étudiant 
	 * depuis la liste des inscrtis de la partie enseignant.
	 * @return la vue de l'état-civil
	 */
	public String lienEnter() {

		FacesContext context = FacesContext.getCurrentInstance();
		String id = (String) context.getExternalContext().getRequestParameterMap().get("idparam");
		etudiant.reset();
		etudiant.setCod_etu(id);
		setVientDuneListe(true);
		setVientDuTrombi(false);
		setVientDuNumero(false);
		setVientDeAnnuaire(false);
		renseigner();
		return enter();
	}

	/**
	 * on arrive à la vue de l'état-civil d'un étudiant 
	 * depuis la vue du trombinoscope de la partie enseignant.
	 * @return la vue de l'état-civil
	 */
	public String lienEnterTrombi() {
		FacesContext context = FacesContext.getCurrentInstance();
		String id = (String) context.getExternalContext().getRequestParameterMap().get("idparam");
		etudiant.reset();
		etudiant.setCod_etu(id);
		setVientDuneListe(false);
		setVientDuTrombi(true);
		setVientDuNumero(false);
		setVientDeAnnuaire(false);
		renseigner();
		return enter();
	}

	/**
	 * on arrive à la vue de l'état-civil d'un étudiant 
	 * depuis le formulaire de saisie du code individu de la partie enseignant.
	 * @param codind
	 * @return la vue de l'état-civil
	 */
	public String lienEnterNumero(final String codetu) {
		setVientDuneListe(false);
		setVientDuTrombi(false);
		setVientDeAnnuaire(false);
		setVientDuNumero(true);
		etudiant.reset();
		etudiant.setCod_etu(codetu);
		renseigner();
		return enter();
	}
	
	/**
	 * on arrive à la vue de l'état-civil d'un étudiant 
	 * depuis le formulaire de choix dans l'annuaire.
	 * @param codind
	 * @return la vue de l'état-civil
	 */
	public String lienEnterAnnuaire(final String codetu) {
		setVientDuneListe(false);
		setVientDuTrombi(false);
		setVientDuNumero(false);
		setVientDeAnnuaire(true);
		etudiant.reset();
		etudiant.setCod_etu(codetu);
		renseigner();
		return enter();
	}
	/**
	 * entrer dans la vue de l'état-civil d'un étudiant d'un étudiant.
	 * @return la page d'arrivée
	 */
	public String enter() {
		String cod_ind = etudiant.getCod_ind();
		if (cod_ind == null || cod_ind.equals("")
				|| etudiant.getCod_nne() == null || etudiant.getCod_nne().equals("")) {
			etudiant.renseigneEtatCivil();
		}
		
		/*
		 * GESTION DE LA NON RECUPERATION DU COD_IND, si le WS ne fonctionne pas.
		 */
		if (cod_ind == null ||cod_ind.equals("")){
			String message = "";
			SessionController session = (SessionController)  BeanUtils.getBean("sessionController");		
			if(session.isEnseignant()){
				message = "Un problème est survenu lors de la récupération des informations de l'étudiant. Veuillez réessayer ultérieurement.";
			}else{
				message = "Un problème est survenu lors de la récupération des informations de l'état civil. Veuillez réessayer ultérieurement.";
			}
			
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			
			//on test si l'utilisateur est un etudiant
			if(!session.isEnseignant())
				return View.INDEX_ETU;
			
			if(vientDeAnnuaire)
				return View.AFFICHE_RECHERCHE_ANNUAIRE;
			if(vientDuNumero)
				return View.CHERCHE_NUM;
			if(vientDuneListe)
				return View.INSCRITS_ARBORESCENCE;
			if (vientDuTrombi)
				return View.INSCRITS_TROMBINOSCOPE;
			
			return View.INDEX_ENS;
			
		}
		
		etudiant.setUrlphoto(photo.getUrlPhoto(etudiant.getCod_ind(),etudiant.getCod_etu()));
		
	
		
		return View.ETAT_CIVIL;
	}

	
	/**
	 * renseigne les champs de l'état-civil de l'étudiant consulté.
	 *
	 */
	public void renseigner() {
		etudiant.renseigneEtatCivil();
	}



	/**
	 * @return vientDuTrombi
	 */
	public boolean isVientDuTrombi() {
		return vientDuTrombi;
	}

	/**
	 * @param vientDuTrombi
	 */
	public void setVientDuTrombi(final boolean vientDuTrombi) {
		this.vientDuTrombi = vientDuTrombi;
	}

	/**
	 * @return vientDuNumero
	 */
	public boolean isVientDuNumero() {
		return vientDuNumero;
	}

	/**
	 * @param vientDuNumero
	 */
	public void setVientDuNumero(final boolean vientDuNumero) {
		this.vientDuNumero = vientDuNumero;
	}

	

	/**
	 * @return etudiant
	 */
	public Etudiant getEtudiant() {
		return etudiant;
	}

	/**
	 * @param etudiant 
	 */
	public void setEtudiant(final Etudiant etudiant) {
		this.etudiant = etudiant;
	}
	/**
	 * @return vientDuneListe
	 */
	public boolean isVientDuneListe() {
		return vientDuneListe;
	}

	/**
	 * @param vientDuneListe
	 */
	public void setVientDuneListe(final boolean vientDuneListe) {
		this.vientDuneListe = vientDuneListe;
	}
	
	/**
	 * @return vientDeAnnuaire
	 */
	public boolean isVientDeAnnuaire() {
		return vientDeAnnuaire;
	}
	
	/**
	 * @param vientDeAnnuaire
	 */
	public void setVientDeAnnuaire(final boolean vientDeAnnuaire) {
		this.vientDeAnnuaire = vientDeAnnuaire;
	}
	
	public IPhoto getPhoto() {
		return photo;
	}
	public void setPhoto(IPhoto photo) {
		this.photo = photo;
	}


	
	
}
