/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.web.navigation.View;
/**
 * controller lors de la recherche d'un étudiant par son code_etu.
 * @author Charlie Dubois
 */
public class RechercheNumeroController extends AbstractContextAwareController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 580610422227417756L;
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(RechercheNumeroController.class);
	/**
	 * Le service.
	 */
	private IDaoService service;
	/**
	 * le controller EtatCivil.
	 */
	private EtatCivilController etatcivilcontroller;

	/**
	 * le code de l'etudiant recherché.
	 */
	private String codetu;


	/**
	 * le constructeur.
	 */
	public RechercheNumeroController() {
		super();
	}


	/**
	 * valide le formulaire et transmet au dossierWeb via le controller etatcivil.
	 * @return la prochaine vue.
	 */
	public String chercher() {

		String message = "";
		if(codetu != null && !codetu.equals("")){
			codetu = codetu.trim();
		}
		if (codetu != null && !codetu.equals("")) {
			if (!Pattern.matches("^[0-9]*",codetu)) {
				message = "Le code etudiant est mal renseigné";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
			} else {
				try{	
					String codindetu = service.getCodIndFromCodEtu(codetu);
					if (codindetu != null) {
						String codetubis=codetu;
						codetu = "";
						return etatcivilcontroller.lienEnterNumero(codetubis);
					} 
					message = "Etudiant " + codetu + " inexistant";
					FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
					FacesContext.getCurrentInstance().addMessage(null, messageX);

				} catch(Exception e) {
					LOG.error(e);
					message = "Il y a eu un problème lors de la récupération des données. Veuillez réessayer ultérieurement";
					FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
					FacesContext.getCurrentInstance().addMessage(null, messageX);
				}
			}
		} else {
			message = "Veuillez rentrer un identifiant.";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);

		}
		return View.CHERCHE_NUM;


	}

	/**
	 * initialise le formulaire.
	 * @return la vue du formulaire.
	 */
	public String enter() {
		codetu = "";
		return View.CHERCHE_NUM;
	}

	/**
	 * initialise le formulaire.
	 * @return la page d'acccueil
	 */
	public String retour() {
		codetu = "";
		return View.INDEX_ENS;
	}

	/**
	 * @return etatcivilcontroller
	 */
	public EtatCivilController getEtatcivilcontroller() {
		return etatcivilcontroller;
	}

	/**
	 * @param etatcivilcontroller
	 */
	public void setEtatcivilcontroller(final EtatCivilController etatcivilcontroller) {
		this.etatcivilcontroller = etatcivilcontroller;
	}

	/**
	 * @param codetu
	 */
	public void setCodetu(final String codetu) {
		this.codetu = codetu;
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
	 * @return codetu
	 */
	public String getCodetu() {
		return codetu;
	}


}
