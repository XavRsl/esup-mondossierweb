/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;




import gouv.education.apogee.commun.transverse.dto.etudiant.TypeHebergementDTO;
import gouv.education.apogee.commun.transverse.dto.geographie.CommuneDTO;
import gouv.education.apogee.commun.transverse.dto.geographie.PaysDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.domain.beans.Etudiant;
import org.esupportail.mondossierweb.web.navigation.View;

/**
 * le controller de l'affichage des adresses de l'étudiant.
 * @author Charlie Dubois
 *
 */
public class AdressesController extends AbstractContextAwareController {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4827749118466461085L;
	/**
	 * l'etatCivilController.
	 */
	private EtatCivilController etatcivilcontroller;
	/**
	 * la type hebergement domicile parental.
	 */
	private final String DOMCILPARENTAL="Domicile parental";
	/**
	 * Le service.
	 */
	private IDaoService service;
	/**
	 * l'étudiant en cours de modification de ses adresses.
	 */
	private Etudiant etudiant;
	/**
	 * dto pour stocker toutes les villes annuelle.
	 */
	private CommuneDTO[] cdtoan;
	/**
	 * dto pour stocker toutes les villes fixe.
	 */
	private CommuneDTO[] cdtofixe;
	/**
	 * le type d'hébergement que constitue l'adresse fixe.
	 */
	private String typehebergement;
	/**
	 * liste des types d'hébergement.
	 */
	private List<SelectItem> ltypehebergement;
	/**
	 * vrai si l'adresse annuelle est la meme que l'adresse fixe.
	 */
	private boolean adresseIdentique;
	/**
	 * liste des pays.
	 */
	private List<SelectItem> lpays;
	/**
	 * liste des villes adresse annuelle.
	 */
	private List<SelectItem> lvillesan;
	/**
	 * liste des villes adresse fixe.
	 */
	private List<SelectItem> lvillesfixe;
	/**
	 * adresse fixe 1.
	 */
	private String adfixe1;
	/**
	 * adresse fixe 2.
	 */
	private String adfixe2;
	/**
	 * adresse fixe3.
	 */
	private String adfixe3;
	/**
	 * ville etranger.
	 */
	private String adfixeEtranger;

	/**
	 * codepostal adresse fixe.
	 */
	private String adfixecp;
	/**
	 * ville de l'adresse fixe.
	 */
	private String adfixeville;
	/**
	 * pays de l'adresse fixe.
	 */
	private String adfixepays;
	/**
	 * téléphone de l'adresse fixe.
	 */
	private String adfixetel;
	/**
	 * adresse annuelle 1.
	 */
	private String adan1;
	/**
	 * adresse annuelle 2.
	 */
	private String adan2;
	/**
	 * adresse annuelle 3.
	 */
	private String adan3;
	/**
	 * ville etranger.
	 */
	private String adanEtranger;
	/**
	 * code postal de l'adresse annuelle.
	 */
	private String adancp;
	/**
	 * vile de l'adresse annuelle.
	 */
	private String adanville;
	/**
	 * pays de l'adresse annuelle.
	 */
	private String adanpays;
	/**
	 * téléphone de l'adresse annuelle.
	 */
	private String adantel;
	/**
	 * telephone portable.
	 */
	private String telPortable;
	/**
	 * email perso.
	 */
	private String emailPerso;
	/**
	 * vrai si l'adresse Annuelle est a l'étranger.
	 */
	private boolean boolAnEtranger;
	/**
	 * vrai si l'adresse Fixe est a l'étranger.
	 */
	private boolean boolFixeEtranger;

	/**
	 * le constructeur.
	 *
	 */
	public AdressesController() {
		super();
		adresseIdentique = false;
	}


	/**
	 * 
	 * @return la vue des adresses de l'étudiant.
	 */
	public String enter() {
		return View.ADRESSES;
	}


	/**
	 * pour que l'étudiant puisse modifier ses adresses.
	 * @return la vue avec le formulaire de modification des adresses
	 */
	public String modifierAdresses() {

		adan1 = etudiant.getAdresseannuelle1();
		adan2 = etudiant.getAdresseannuelle2();
		adan3 = etudiant.getAdresseannuelle3();
		adantel = etudiant.getNumerotelannuel();
		adfixe1 = etudiant.getAdressefixe1();
		adfixe2 = etudiant.getAdressefixe2();
		adfixe3 = etudiant.getAdressefixe3();
		adfixetel = etudiant.getNumerotelfixe();
		emailPerso = etudiant.getEmailPerso();
		telPortable = etudiant.getTelPortable();

		adanEtranger = etudiant.getAdresseannuelleetranger();
		adfixeEtranger = etudiant.getAdressefixeetranger();

		if (adanEtranger.equals("")) {
			boolAnEtranger = false;
		} else {
			boolAnEtranger = true;
		}

		if (adfixeEtranger.equals("")) {
			boolFixeEtranger = false;
		} else {
			boolFixeEtranger = true;
		}

		typehebergement = etudiant.getAdresseannuelleType();
		adfixepays = etudiant.getPaysfixe();
		adanpays = etudiant.getPaysannuel();
		adancp = etudiant.getAdresseannuellecp();
		adanville = etudiant.getVilleannuelle();
		adfixecp = etudiant.getAdressefixecp();
		adfixeville = etudiant.getVillefixe();

		if (typehebergement.equals(DOMCILPARENTAL)) {
			adresseIdentique = true;
		} else {
			adresseIdentique = false;
		}

		//on remplit la liste des villes(adresse annuelle) si l'adresse est en france
		if (adanpays.equals("FRANCE")) {
			boolAnEtranger = false;
			if(adancp!= null && !adancp.equals("") && Pattern.matches("[0-9]*",adancp))
				cdtoan = service.getCommunes(adancp);

		} else {
			boolAnEtranger = true;
		}

		TypeHebergementDTO[] thvo = service.getTypesHebergement();
		PaysDTO[] pdto = service.getPays();

		ltypehebergement = new ArrayList<SelectItem>();
		for (int i = 0; i < thvo.length; i++) {
			ltypehebergement.add(new SelectItem(thvo[i].getLibTypeHebergement(), thvo[i].getLibTypeHebergement()));
		}

		lpays = new ArrayList<SelectItem>();
		for (int i = 0; i < pdto.length; i++) {
			boolean insere = false;
			int j = 0;
			while (!insere && j < lpays.size()) {
				if (lpays.get(j).getLabel().compareTo(pdto[i].getLibPay()) > 0) {
					lpays.add(j, new SelectItem(pdto[i].getLibPay(), pdto[i].getLibPay()));
					insere = true;
				}
				j++;
			}
			if (!insere) {
				lpays.add(new SelectItem(pdto[i].getLibPay(), pdto[i].getLibPay()));
			}
		}

		lvillesan = new ArrayList<SelectItem>();
		if (cdtoan != null && (cdtoan.length > 0)) {
			for (int i = 0; i < cdtoan.length; i++) {
				boolean insere = false;
				int j = 0;
				while (!insere && j < lvillesan.size()) {
					if (lvillesan.get(j).getLabel().compareTo(cdtoan[i].getLibCommune()) > 0) {
						lvillesan.add(j, new SelectItem(cdtoan[i].getLibCommune(), cdtoan[i].getLibCommune()));
						insere = true;
					}
					if (lvillesan.get(j).getLabel().equals(cdtoan[i].getLibCommune())) {
						insere = true;
					}
					j++;
				}
				if (!insere) {
					lvillesan.add(new SelectItem(cdtoan[i].getLibCommune(), cdtoan[i].getLibCommune()));
				}
			}
		}

		//on remplit la liste des villes(adresse fixe) si l'adresse est en france
		if (adfixepays.equals("FRANCE")) {
			boolFixeEtranger = false;
			if(adfixecp!= null && !adfixecp.equals("") && Pattern.matches("[0-9]*",adfixecp))
				cdtofixe = service.getCommunes(adfixecp);

		} else  {
			boolFixeEtranger = true;
		}
		lvillesfixe = new ArrayList<SelectItem>();
		if (cdtofixe != null && (cdtofixe.length > 0)) {
			for (int i = 0; i < cdtofixe.length; i++) {
				boolean insere = false;
				int j = 0;
				while (!insere && j < lvillesfixe.size()) {
					if (lvillesfixe.get(j).getLabel().compareTo(cdtofixe[i].getLibCommune()) > 0) {
						lvillesfixe.add(j, new SelectItem(cdtofixe[i].getLibCommune(), cdtofixe[i].getLibCommune()));
						insere = true;
					}
					if (lvillesfixe.get(j).getLabel().equals(cdtofixe[i].getLibCommune())) {
						insere = true;
					}
					j++;
				}
				if (!insere) {
					lvillesfixe.add(new SelectItem(cdtofixe[i].getLibCommune(), cdtofixe[i].getLibCommune()));
				}
			}
		}


		return View.MODIFIER_ADRESSES;
	}

	/**
	 * vérifie les champs renseignés et fait effectuer le changement dans la bdd.
	 * @return la vue des adresses
	 */
	public String enregistrerModif() {
		//1-vérification des parametres
		boolean erreur = false;
		boolean succes = false;
		String message = "";
		if (typehebergement.equals("")) {
			message = "Veuillez indiquer un type d'hébergement pour l'adresse annuelle";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			erreur = true;
		}
		if (!adresseIdentique) {
			if (adan1.equals("")) {
				message = "Veuillez remplir le premier champ de l'adresse annuelle";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			}
			if ((adanville == null || adanville.equals("")) && !boolAnEtranger) {
				message = "Veuillez sélectionner une ville pour l'adresse annuelle";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			}
			if (adanpays.equals("")) {
				message = "Veuillez sélectionner un pays pour l'adresse annuelle";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			}
			if (!adantel.equals("") && (!Pattern.matches("[0-9[.]]*", adantel))){
				message = "Veuillez indiquer un numéro de téléphone pour l'adresse annuelle";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			}
			if (adanEtranger.equals("") && boolAnEtranger) {
				message = "Veuillez indiquer une ville pour l'adresse annuelle";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
				erreur = true;
			}

		}

		if (adfixe1.equals("")) {
			message = "Veuillez remplir le premier champ de l'adresse fixe";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			erreur = true;
		}
		if ((adfixeville == null || adfixeville.equals("")) && !boolFixeEtranger) {
			message = "Veuillez sélectionner une ville pour l'adresse fixe";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			erreur = true;
		}
		if (adfixepays.equals("")) {
			message = "Veuillez sélectionner un pays pour l'adresse fixe";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			erreur = true;
		}
		if (!adfixetel.equals("") && (!Pattern.matches("[0-9[.]]*", adfixetel))){
			message = "Veuillez indiquer un numéro de téléphone pour l'adresse fixe";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			erreur = true;
		}
		if (adfixeEtranger.equals("") && boolFixeEtranger) {
			message = "Veuillez indiquer une ville pour l'adresse fixe";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			erreur = true;
		}
		if (!emailPerso.equals("") && (!Pattern.matches("[a-zA-Z[._-][\\d]]*[@][a-zA-Z[._-][\\d]]*[.][a-zA-Z[._-][d]]*", emailPerso))) {
			message = "L'email personnnel est mal renseigné";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			erreur = true;
		}
		if (!telPortable.equals("") && (!Pattern.matches("[0-9[.]]*", telPortable))) {
			message = "Le numéro de téléphone portable est mal renseigné";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
			erreur = true;
		}

		//2-maj les attribut utiles au changement d'adresse du bean etudiant
		if (!erreur) {

			etudiant.setAdressefixe1(adfixe1);
			etudiant.setAdressefixe2(adfixe2);
			etudiant.setAdressefixe3(adfixe3);
			etudiant.setNumerotelfixe(adfixetel);
			etudiant.setVillefixe(adfixeville);
			etudiant.setPaysfixe(adfixepays);
			etudiant.setAdressefixecp(getCodePostal(etudiant.getVillefixe(), cdtofixe));
			etudiant.setAdressefixeetranger(adfixeEtranger);
			etudiant.setAdresseannuelleType(typehebergement);

			etudiant.setEmailPerso(emailPerso);
			etudiant.setTelPortable(telPortable);

			if (!adresseIdentique) {
				etudiant.setAdresseannuelle1(adan1);
				etudiant.setAdresseannuelle2(adan2);
				etudiant.setAdresseannuelle3(adan3);
				etudiant.setNumerotelannuel(adantel);
				etudiant.setVilleannuelle(adanville);
				etudiant.setPaysannuel(adanpays);
				etudiant.setAdresseannuelleetranger(adanEtranger);
				etudiant.setAdresseannuellecp(getCodePostal(etudiant.getVilleannuelle(), cdtoan));
			} else {
				etudiant.setAdresseannuelle1(adfixe1);
				etudiant.setAdresseannuelle2(adfixe2);
				etudiant.setAdresseannuelle3(adfixe3);
				etudiant.setNumerotelannuel(adfixetel);
				etudiant.setVilleannuelle(adfixeville);
				etudiant.setPaysannuel(adfixepays);
				etudiant.setAdresseannuelleetranger(adfixeEtranger);
				etudiant.setAdresseannuellecp(etudiant.getAdressefixecp());		
			}



			//3:
			succes = etudiant.majAdresses();
			if (!succes) {
				message = "Un problème est survenu pendant la mise à jour. Veuillez réessayer ultérieurement";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);
			}

			etudiant.renseigneAdresses();
			return View.ADRESSES;
		} 

		return View.MODIFIER_ADRESSES;

	}

	/**
	 * 
	 * @return la vue avec le formulaire de modification des adresses
	 */
	public String actualiserVilleAnnuelle() {
		if (Pattern.matches("^[0-9]{2}[0-9]*", adancp)) { 
			cdtoan = service.getCommunes(adancp);
			String ville1 = "";
			lvillesan.clear();
			if(cdtoan!=null){
				for (int i = 0; i < cdtoan.length; i++) {

					boolean insere = false;
					int j = 0;
					while (!insere && j < lvillesan.size()) {
						if (lvillesan.get(j).getLabel().compareTo(cdtoan[i].getLibCommune()) > 0) {
							lvillesan.add(j, new SelectItem(cdtoan[i].getLibCommune(), cdtoan[i].getLibCommune()));
							insere = true;
						}
						if (lvillesan.get(j).getLabel().equals(cdtoan[i].getLibCommune())) {
							insere = true;
						}
						if (!insere) {
							j++;
						}
					}
					if (!insere) {
						lvillesan.add(new SelectItem(cdtoan[i].getLibCommune(), cdtoan[i].getLibCommune()));
					}
					//Pour afficher le CP de la premiere ville directement
					if (j == 0 && !cdtoan[i].getLibCommune().equals(ville1)) {
						adancp = cdtoan[i].getCodePostal();
						ville1 = cdtoan[i].getLibCommune();
					}
				}
				adanville = lvillesan.get(0).getLabel();
			}



		} else {
			String message = "Le code être composé d'au moins 2 chiffres";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
		}
		return View.MODIFIER_ADRESSES;
	}




	/**
	 * @return la vue avec le codePostal de la ville choisie affiché
	 */
	public String majCpVilleAnnuelle() {
		boolean trouve = false;
		int i = 0;
		if(cdtoan!=null){
			while (i < cdtoan.length && !trouve) {

				if (cdtoan[i].getLibCommune().equals(adanville)) {
					adancp = cdtoan[i].getCodePostal();
					trouve = true;
				}
				i++;
			}
		}
		return View.MODIFIER_ADRESSES;
	}

	/**
	 * 
	 * @return la vue avec le formulaire de modification des adresses
	 */
	public String actualiserVilleFixe() {
		if (Pattern.matches("^[0-9]{2}[0-9]*", adfixecp)) { 
			cdtofixe = service.getCommunes(adfixecp);
			String ville1 = "";
			lvillesfixe.clear();
			if(cdtofixe!=null){
				for (int i = 0; i < cdtofixe.length; i++) {
					boolean insere = false;
					int j = 0;
					while (!insere && j < lvillesfixe.size()) {
						if (lvillesfixe.get(j).getLabel().compareTo(cdtofixe[i].getLibCommune()) > 0) {
							lvillesfixe.add(j, new SelectItem(cdtofixe[i].getLibCommune(), cdtofixe[i].getLibCommune()));
							insere = true;
						}
						if (lvillesfixe.get(j).getLabel().equals(cdtofixe[i].getLibCommune())) {
							insere = true;
						}
						if (!insere) {
							j++;
						}
					}
					if (!insere) {
						lvillesfixe.add(new SelectItem(cdtofixe[i].getLibCommune(), cdtofixe[i].getLibCommune()));
					}
					// Pour afficher le CP de la premiere ville directement
					if (j == 0 && !cdtofixe[i].getLibCommune().equals(ville1)) {
						adfixecp = cdtofixe[i].getCodePostal();
						ville1 = cdtofixe[i].getLibCommune();
					}
				}

				adfixeville = lvillesfixe.get(0).getLabel();
			}
		} else {
			String message = "Le code être composé d'au moins 2 chiffres";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
		}
		return View.MODIFIER_ADRESSES;

	}


	/**
	 * @return la vue avec le formulaire de modification des adresses
	 */
	public String majCpVilleFixe() {
		boolean trouve = false;
		int i = 0;
		if(cdtofixe!=null){
			while (i < cdtofixe.length && !trouve) {

				if (cdtofixe[i].getLibCommune().equals(adfixeville)) {
					adfixecp = cdtofixe[i].getCodePostal();
					trouve = true;
				}
				i++;
			}
		}
		return View.MODIFIER_ADRESSES;
	}


	/**
	 * @param ville
	 * @return le codePostal de la ville
	 */
	private String getCodePostal(final String ville, CommuneDTO[] cdt) {
		String cp = "";
		boolean trouve = false;
		int i = 0;
		if(cdt != null){ //Pays Etranger
			while (!trouve && i < cdt.length) {
				if (cdt[i].getLibCommune().equals(ville)) {
					cp = cdt[i].getCodePostal();
					trouve = true;
				}
				i++;
			}
		}
		return cp;
	}



	/**
	 * 
	 * @return le formualaire de modification
	 */
	public String changementPaysAnnuel() {

		if (adanpays.equals("FRANCE")) {
			boolAnEtranger = false;
		} else {
			boolAnEtranger = true;
		}
		return View.MODIFIER_ADRESSES;
	}

	/**
	 * 
	 * @return le formualaire de modification
	 */
	public String changementPaysFixe() {

		if (adfixepays.equals("FRANCE")) {
			boolFixeEtranger = false;
		} else {
			boolFixeEtranger = true;
		}
		return View.MODIFIER_ADRESSES;
	}
	/**
	 * 
	 * @return le formualaire de modification
	 */
	public String reload() {
		return View.MODIFIER_ADRESSES;
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


	public String getAdan1() {
		return adan1;
	}


	public void setAdan1(String adan1) {
		this.adan1 = adan1;
	}


	public String getAdan2() {
		return adan2;
	}


	public void setAdan2(String adan2) {
		this.adan2 = adan2;
	}


	public String getAdan3() {
		return adan3;
	}


	public void setAdan3(String adan3) {
		this.adan3 = adan3;
	}


	public String getAdancp() {
		return adancp;
	}


	public void setAdancp(String adancp) {
		this.adancp = adancp;
	}


	public String getAdanpays() {
		return adanpays;
	}


	public void setAdanpays(String adanpays) {
		this.adanpays = adanpays;
	}


	public String getAdantel() {
		return adantel;
	}


	public void setAdantel(String adantel) {
		this.adantel = adantel;
	}


	public String getAdanville() {
		return adanville;
	}


	public void setAdanville(String adanville) {
		this.adanville = adanville;
	}


	public String getAdfixe1() {
		return adfixe1;
	}


	public void setAdfixe1(String adfixe1) {
		this.adfixe1 = adfixe1;
	}


	public String getAdfixe2() {
		return adfixe2;
	}


	public void setAdfixe2(String adfixe2) {
		this.adfixe2 = adfixe2;
	}


	public String getAdfixe3() {
		return adfixe3;
	}


	public void setAdfixe3(String adfixe3) {
		this.adfixe3 = adfixe3;
	}


	public String getAdfixecp() {
		return adfixecp;
	}


	public void setAdfixecp(String adfixecp) {
		this.adfixecp = adfixecp;
	}


	public String getAdfixepays() {
		return adfixepays;
	}


	public void setAdfixepays(String adfixepays) {
		this.adfixepays = adfixepays;
	}


	public String getAdfixetel() {
		return adfixetel;
	}


	public void setAdfixetel(String adfixetel) {
		this.adfixetel = adfixetel;
	}


	public String getAdfixeville() {
		return adfixeville;
	}


	public void setAdfixeville(String adfixeville) {
		this.adfixeville = adfixeville;
	}


	public Etudiant getEtudiant() {
		return etudiant;
	}


	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}


	public String getTypehebergement() {
		return typehebergement;
	}


	public void setTypehebergement(String typehebergement) {
		this.typehebergement = typehebergement;
	}



	public List<SelectItem> getLtypehebergement() {
		return ltypehebergement;
	}


	public void setLtypehebergement(List<SelectItem> ltypehebergement) {
		this.ltypehebergement = ltypehebergement;
	}





	public List<SelectItem> getLpays() {
		return lpays;
	}


	public void setLpays(List<SelectItem> lpays) {
		this.lpays = lpays;
	}


	public List<SelectItem> getLvillesan() {
		return lvillesan;
	}


	public void setLvillesan(List<SelectItem> lvillesan) {
		this.lvillesan = lvillesan;
	}


	public List<SelectItem> getLvillesfixe() {
		return lvillesfixe;
	}


	public void setLvillesfixe(List<SelectItem> lvillesfixe) {
		this.lvillesfixe = lvillesfixe;
	}


	public IDaoService getService() {
		return service;
	}


	public void setService(IDaoService service) {
		this.service = service;
	}


	public boolean isAdresseIdentique() {
		return adresseIdentique;
	}


	public void setAdresseIdentique(boolean adresseIdentique) {
		this.adresseIdentique = adresseIdentique;
	}


	public CommuneDTO[] getCdtoan() {
		return cdtoan;
	}


	public void setCdtoan(CommuneDTO[] cdtoan) {
		this.cdtoan = cdtoan;
	}


	public CommuneDTO[] getCdtofixe() {
		return cdtofixe;
	}


	public void setCdtofixe(CommuneDTO[] cdtofixe) {
		this.cdtofixe = cdtofixe;
	}


	public String getAdanEtranger() {
		return adanEtranger;
	}


	public void setAdanEtranger(String adanEtranger) {
		this.adanEtranger = adanEtranger;
	}


	public String getAdfixeEtranger() {
		return adfixeEtranger;
	}


	public void setAdfixeEtranger(String adfixeEtranger) {
		this.adfixeEtranger = adfixeEtranger;
	}


	public boolean isBoolAnEtranger() {
		return boolAnEtranger;
	}


	public void setBoolAnEtranger(boolean boolAnEtranger) {
		this.boolAnEtranger = boolAnEtranger;
	}


	public boolean isBoolFixeEtranger() {
		return boolFixeEtranger;
	}


	public void setBoolFixeEtranger(boolean boolFixeEtranger) {
		this.boolFixeEtranger = boolFixeEtranger;
	}


	public String getTelPortable() {
		return telPortable;
	}


	public void setTelPortable(String telPortable) {
		this.telPortable = telPortable;
	}


	public String getEmailPerso() {
		return emailPerso;
	}


	public void setEmailPerso(String emailPerso) {
		this.emailPerso = emailPerso;
	}





}
