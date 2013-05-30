/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.List;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;


/**
 * bean contenant les variables de configuration.
 * @author Charlie Dubois
 */
@ManagedResource(objectName = "spring:name=configEsupMonDossierWeb", description = "la configuration du dossier etudiant")
public class Config {
	
	/**
	 * numero de version de l'application
	 */
	private String version;
	/**
	 * true pour activer la portlet. A false un message 'en maintenance' s'affiche.
	 */
	private boolean activation;
	/**
	 * true pour que les enseignants aient accès.
	 */
	private boolean partieEnseignant;
	/**
	 * true pour que les étudiants aient accès à leur dossier.
	 */
	private boolean partieEtudiant;
	/**
	 * true pour logguer les connexions  : login - typeAcces - IP  (voir log4j.properties)
	 */
	private boolean logConnexion;
	/**
	 * true pour activer le trombinoscope.
	 */
	private boolean utiliserTrombinoscope;
	/**
	 * nombre d'étudiants visibles dans chaque page (du navigateur) du trombinoscope.
	 */
	private int nbEtudiantsParPageAfficheeDuTrombinoscope;
	/**
	 * Extension des adresses mail étudiantes.
	 */
	private String extMail;

	/**
	 * Adresse du lien "nous contacter".
	 */
	private String lienContact;

	/**
	 * Vrai si le canal MailTo peut etre appelé par MonDossierWeb.
	 */
	private boolean lienContactActif;
	
	/**
	 * Message de maintenance (quand activation=false).
	 */
	private String msgMaintenance;

	/**
	 * message d'erreur de connexion.
	 */
	private String msgConnexion;

	/**
	 * message d'erreur d'accès.
	 */
	private String msgRefusAcces;
	/**
	 * le message informant que les résultats ne sont pas dénitifs. 
	 * Affiché dans le détail des notes d'une étape dont la délibération n'est pas à "T", pour terminée, dans Apogée.
	 */
	private String msgResultatsNonDefinitifs;
	/**
	 * boolean pour utilisation de CalRent.
	 */
	private boolean affInscriptions;

	/**
	 * boolean pour utilisation de CalExam.
	 */
	private boolean affCalendrier;
	/**
	 * boolean pour afficher le calendrier par etape a la place du calendrier standard.
	 */
	private boolean affCalendrierEtape;
	/**
	 * boolean pour autoriser le téléchargement du calendrier en pdf.
	 */
	private boolean calendrierPDF;

	/**
	 * boolean pour utilisation de Resultats.
	 */
	private boolean affNotes;
	/**
	 * boolean pour insertion de filigrane dans le pdf des notes.
	 */
	private boolean insertionFiligranePdfNotes;

	/**
	 * boolean pour utilisation de Resultats.
	 */
	private boolean listeScrollable;

	/**
	 * nombre d'étudiants par page lors de l'utilisation d'un dataScroller pour afficher la liste des étudiants.
	 */
	private int nbEtudiantsParPage;
	/**
	 * true pour que l'email de l'étudiant (état-civil) soit récupéré dans l'annuaire.
	 * false pour que ce soir iBATIS qui s'en charge.
	 */
	private boolean recupAnnuaire;
	
	/**
	 * boolean pour que l'étudiant puisse modifier ses adresses.
	 */
	private boolean modificationAdresses;
	/**
	 * etat de délibération des notes et résultats visibles par l'étudiant (A : avant delibération, E: en cours, T : terminée). T par defaut.
	 */
	private String temoinNotesEtudiant;
	/**
	 * etat de délibération des notes et résultats visibles par les enseignants (A : avant delibération, E: en cours, T : terminée). AET par defaut.
	 */
	private String temoinNotesEnseignant;
	/**
	 * Valeur du témoin (O ou N) temoinCtlValCadEpr (Témoin modalités contrôle validées) pour laquelle on veut que les notes aux épreuves soient visibles même si l'état de délibération n'est pas dans la liste de ceux définis ci-dessus.
	 */
	private String temoinCtlValCadEpr;
	/**
	 * la liste des types d'épreuve dont on affiche la note
	 */
	private List<String> typesEpreuveAffichageNote;
	/**
	 * Si temoinFictif est renseigné, seuls les éléments dont tem_fictif est égal à témoinFictif seront affichés dans l'écran du détail des notes
	 */
	private String temoinFictif;
	/**
	 * affichage ou non du rang de l'étudiant aux diplomes , épreuves et elp sur les pages de 'Notes et Résultats' du dossier
	 */
	private boolean affRangEtudiant;
	/**
	 * la liste des codes etape dont on affiche le rang même si affRangEtudiant=false
	 */
	private List<String> codesEtapeAffichageRang;
	/**
	 * affichage ou non des informations crédits ECTS par la page du détail des notes.
	 */
	private boolean affECTSEtudiant;
	/**
	 * affichage ou non de la mention aux diplomes et étapes à la page des notes.
	 */
	private boolean affMentionEtudiant;
	/**
	 * Edition pdf des notes : true pour l'activer, false sinon.
	 */
	private boolean notesPDF;
	/**
	 * Edition pdf des certificats de scolarité : true pour l'activer, false sinon.
	 */
	private boolean certificatScolaritePDF;
	/**
	 * Autoriser l'édition des certificats de scolarité pour toutes les années et pas seulement l'année en cours.
	 */
	private boolean certificatScolariteTouteAnnee;
	/**
	 * Autoriser les personnels à imprimer les certificats de scolarité.
	 */
	private boolean certScolAutorisePersonnel;
	/**
	 * Code du signataire des certificats de scolarité.
	 */
	private String certScolCodeSignataire;
	/**
	 * Lieu d'édition des certificats de scolarité.
	 */
	private String certScolLieuEdition;
	/**
	 * Ajoute le logo de l'université dans les certificats de scolarité générés.
	 */
	private boolean certScolUtiliseLogo;
	/**
	 * Liste des codes de types de diplomes pour lesquels la generation de certificat est desactivee.
	 */
	private List<String> certScolTypDiplomeDesactive;
	/**
	 * url vers le logo de l'université inséré dans les pdf.
	 */
	private String logoUniversitePdf;
	/**
	 * vrai si on veut voir la banniere (/media/images/HC/banniere.JPG) avec l'image et le bouton de déconnexion en mode servlet.
	 */
	private boolean banniereServlet;
	/**
	 * Affichage du numéro de place dans le calendrier des examens : true pour l'activer, false sinon.
	 */
	private boolean affNumPlaceExamen;

	/**
	 * Message complémentaire concernant le calendrier des examens.
	 */
	private String cmtCalExamen;
	/**
	 * Message complémentaire concernant le calendrier de rentree.
	 */
	private String cmtCalRentree;
	/**
	 * l'identifiant (login) de l'étudiant pour lequel on souhaite se faire passer pour un test. 
	 */
	private String idEtudiantTest;
	/**
	 * l'identifiant que l'on souhaite substituer à celui d'un étudiant : 'idEtudiantTest'.
	 */
	private String idLoginTest;
	/**
	 * liste des logins qu'on exclu de l'application.
	 */
	private List<String> listeLoginExclus;
	/**
	 * la source de résultats pour les notes ex: "Apogee" ou "Apogee-extraction".
	 */
	private String sourceResultats;
	
	/**
	 * Affichage ou non du calendrier de rentrée.
	 */
	private String affCalendrierRentree;

	/**
	 * constructeur vide.
	 *
	 */
	public Config() {
		super();

	}

	@ManagedAttribute
	public boolean isActivation() {
		return activation;
	}
	
	@ManagedAttribute
	public void setActivation(boolean activation) {
		this.activation = activation;
	}
	
	@ManagedAttribute
	public String getMsgMaintenance() {
		return msgMaintenance;
	}
	
	@ManagedAttribute
	public void setMsgMaintenance(String msgMaintenance) {
		this.msgMaintenance = msgMaintenance;
	}
	
	@ManagedAttribute
	public String getLienContact() {
		return lienContact;
	}

	@ManagedAttribute
	public void setLienContact(String lienContact) {
		this.lienContact = lienContact;
	}


	@ManagedAttribute
	public String getExtMail() {
		return extMail;
	}

	@ManagedAttribute
	public void setExtMail(String extMail) {
		this.extMail = extMail;
	}

	@ManagedAttribute
	public String getMsgConnexion() {
		return msgConnexion;
	}

	@ManagedAttribute
	public void setMsgConnexion(String msgConnexion) {
		this.msgConnexion = msgConnexion;
	}

	@ManagedAttribute
	public String getMsgRefusAcces() {
		return msgRefusAcces;
	}

	@ManagedAttribute
	public void setMsgRefusAcces(String msgRefusAcces) {
		this.msgRefusAcces = msgRefusAcces;
	}

	
	@ManagedAttribute
	public boolean isAffCalendrier() {
		return affCalendrier;
	}

	@ManagedAttribute
	public void setAffCalendrier(boolean affCalendrier) {
		this.affCalendrier = affCalendrier;
	}

	@ManagedAttribute
	public boolean isAffInscriptions() {
		return affInscriptions;
	}

	@ManagedAttribute
	public void setAffInscriptions(boolean affInscriptions) {
		this.affInscriptions = affInscriptions;
	}

	@ManagedAttribute
	public boolean isAffNotes() {
		return affNotes;
	}

	@ManagedAttribute
	public void setAffNotes(boolean affNotes) {
		this.affNotes = affNotes;
	}


	
	@ManagedAttribute
	public boolean isLienContactActif() {
		return lienContactActif;
	}

	@ManagedAttribute
	public void setLienContactActif(boolean lienContactActif) {
		this.lienContactActif = lienContactActif;
	}

	@ManagedAttribute
	public boolean isListeScrollable() {
		return listeScrollable;
	}

	@ManagedAttribute
	public void setListeScrollable(boolean listeScrollable) {
		this.listeScrollable = listeScrollable;
	}

	@ManagedAttribute
	public int getNbEtudiantsParPage() {
		return nbEtudiantsParPage;
	}

	@ManagedAttribute
	public void setNbEtudiantsParPage(int nbEtudiantsParPage) {
		this.nbEtudiantsParPage = nbEtudiantsParPage;
	}
	
	@ManagedAttribute
	public boolean isAffNumPlaceExamen() {
		return affNumPlaceExamen;
	}
	
	@ManagedAttribute
	public void setAffNumPlaceExamen(boolean affNumPlaceExamen) {
		this.affNumPlaceExamen = affNumPlaceExamen;
	}


	@ManagedAttribute
	public String getCmtCalExamen() {
		return cmtCalExamen;
	}

	@ManagedAttribute
	public void setCmtCalExamen(String cmtCalExamen) {
		this.cmtCalExamen = cmtCalExamen;
	}

	@ManagedAttribute
	public boolean isModificationAdresses() {
		return modificationAdresses;
	}

	@ManagedAttribute
	public void setModificationAdresses(boolean modificationAdresses) {
		this.modificationAdresses = modificationAdresses;
	}

	@ManagedAttribute
	public boolean isRecupAnnuaire() {
		return recupAnnuaire;
	}

	@ManagedAttribute
	public void setRecupAnnuaire(boolean recupAnnuaire) {
		this.recupAnnuaire = recupAnnuaire;
	}

	@ManagedAttribute
	public boolean isPartieEnseignant() {
		return partieEnseignant;
	}

	@ManagedAttribute
	public void setPartieEnseignant(boolean partieEnseignant) {
		this.partieEnseignant = partieEnseignant;
	}

	@ManagedAttribute
	public boolean isPartieEtudiant() {
		return partieEtudiant;
	}

	@ManagedAttribute
	public void setPartieEtudiant(boolean partieEtudiant) {
		this.partieEtudiant = partieEtudiant;
	}

	@ManagedAttribute
	public boolean isUtiliserTrombinoscope() {
		return utiliserTrombinoscope;
	}

	@ManagedAttribute
	public void setUtiliserTrombinoscope(boolean utiliserTrombinoscope) {
		this.utiliserTrombinoscope = utiliserTrombinoscope;
	}

	@ManagedAttribute
	public String getMsgResultatsNonDefinitifs() {
		return msgResultatsNonDefinitifs;
	}

	@ManagedAttribute
	public void setMsgResultatsNonDefinitifs(String msgResultatsNonDefinitifs) {
		this.msgResultatsNonDefinitifs = msgResultatsNonDefinitifs;
	}

	@ManagedAttribute
	public String getIdEtudiantTest() {
		return idEtudiantTest;
	}

	@ManagedAttribute
	public void setIdEtudiantTest(String idEtudiantTest) {
		this.idEtudiantTest = idEtudiantTest;
	}

	@ManagedAttribute
	public int getNbEtudiantsParPageAfficheeDuTrombinoscope() {
		return nbEtudiantsParPageAfficheeDuTrombinoscope;
	}

	@ManagedAttribute
	public void setNbEtudiantsParPageAfficheeDuTrombinoscope(
			int nbEtudiantsParPageAfficheeDuTrombinoscope) {
		this.nbEtudiantsParPageAfficheeDuTrombinoscope = nbEtudiantsParPageAfficheeDuTrombinoscope;
	}

	@ManagedAttribute
	public boolean isNotesPDF() {
		return notesPDF;
	}

	@ManagedAttribute
	public void setNotesPDF(boolean notesPDF) {
		this.notesPDF = notesPDF;
	}

	@ManagedAttribute
	public boolean isCertificatScolaritePDF() {
		return certificatScolaritePDF;
	}

	@ManagedAttribute
	public void setCertificatScolaritePDF(boolean certificatScolaritePDF) {
		this.certificatScolaritePDF = certificatScolaritePDF;
	}

	@ManagedAttribute
	public boolean isCertScolAutorisePersonnel() {
		return certScolAutorisePersonnel;
	}

	@ManagedAttribute
	public void setCertScolAutorisePersonnel(boolean certScolAutorisePersonnel) {
		this.certScolAutorisePersonnel = certScolAutorisePersonnel;
	}

	@ManagedAttribute
	public String getCertScolCodeSignataire() {
		return certScolCodeSignataire;
	}

	@ManagedAttribute
	public void setCertScolCodeSignataire(String certScolCodeSignataire) {
		this.certScolCodeSignataire = certScolCodeSignataire;
	}

	@ManagedAttribute
	public String getCertScolLieuEdition() {
		return certScolLieuEdition;
	}

	@ManagedAttribute
	public void setCertScolLieuEdition(String certScolLieuEdition) {
		this.certScolLieuEdition = certScolLieuEdition;
	}

	@ManagedAttribute
	public boolean isCertScolUtiliseLogo() {
		return certScolUtiliseLogo;
	}

	@ManagedAttribute
	public void setCertScolUtiliseLogo(boolean certScolUtiliseLogo) {
		this.certScolUtiliseLogo = certScolUtiliseLogo;
	}

	@ManagedAttribute
	public List<String> getCertScolTypDiplomeDesactive() {
		return certScolTypDiplomeDesactive;
	}

	@ManagedAttribute
	public void setCertScolTypDiplomeDesactive(
			List<String> certScolTypDiplomeDesactive) {
		this.certScolTypDiplomeDesactive = certScolTypDiplomeDesactive;
	}

	@ManagedAttribute
	public String getIdLoginTest() {
		return idLoginTest;
	}

	@ManagedAttribute
	public void setIdLoginTest(String idLoginTest) {
		this.idLoginTest = idLoginTest;
	}

	@ManagedAttribute
	public String getLogoUniversitePdf() {
		return logoUniversitePdf;
	}
	@ManagedAttribute
	public void setLogoUniversitePdf(String logoUniversitePdf) {
		this.logoUniversitePdf = logoUniversitePdf;
	}
	@ManagedAttribute
	public boolean isBanniereServlet() {
		return banniereServlet;
	}
	@ManagedAttribute
	public void setBanniereServlet(boolean banniereServlet) {
		this.banniereServlet = banniereServlet;
	}
	@ManagedAttribute
	public String getTemoinNotesEtudiant() {
		return temoinNotesEtudiant;
	}
	@ManagedAttribute
	public void setTemoinNotesEtudiant(String temoinNotesEtudiant) {
		this.temoinNotesEtudiant = temoinNotesEtudiant;
	}
	@ManagedAttribute
	public boolean isAffRangEtudiant() {
		return affRangEtudiant;
	}
	@ManagedAttribute
	public void setAffRangEtudiant(boolean affRangEtudiant) {
		this.affRangEtudiant = affRangEtudiant;
	}
	@ManagedAttribute
	public String getTemoinNotesEnseignant() {
		return temoinNotesEnseignant;
	}
	@ManagedAttribute
	public void setTemoinNotesEnseignant(String temoinNotesEnseignant) {
		this.temoinNotesEnseignant = temoinNotesEnseignant;
	}

	@ManagedAttribute
	public void setSourceResultats(String sourceResultats) {
		this.sourceResultats = sourceResultats;
	}

	@ManagedAttribute
	public String getSourceResultats() {
		return sourceResultats;
	}

	@ManagedAttribute
	public String getAffCalendrierRentree() {
		return affCalendrierRentree;
	}

	@ManagedAttribute
	public void setAffCalendrierRentree(String affCalendrierRentree) {
		this.affCalendrierRentree = affCalendrierRentree;
	}

	@ManagedAttribute
	public String getCmtCalRentree() {
		return cmtCalRentree;
	}

	@ManagedAttribute
	public void setCmtCalRentree(String cmtCalRentree) {
		this.cmtCalRentree = cmtCalRentree;
	}

	@ManagedAttribute
	public boolean isLogConnexion() {
		return logConnexion;
	}

	@ManagedAttribute
	public void setLogConnexion(boolean logConnexion) {
		this.logConnexion = logConnexion;
	}

	public List<String> getListeLoginExclus() {
		return listeLoginExclus;
	}

	public void setListeLoginExclus(List<String> listeLoginExclus) {
		this.listeLoginExclus = listeLoginExclus;
	}

	public boolean isCalendrierPDF() {
		return calendrierPDF;
	}

	public void setCalendrierPDF(boolean calendrierPDF) {
		this.calendrierPDF = calendrierPDF;
	}

	public boolean isAffCalendrierEtape() {
		return affCalendrierEtape;
	}

	public void setAffCalendrierEtape(boolean affCalendrierEtape) {
		this.affCalendrierEtape = affCalendrierEtape;
	}

	public boolean isAffECTSEtudiant() {
		return affECTSEtudiant;
	}

	public void setAffECTSEtudiant(boolean affECTSEtudiant) {
		this.affECTSEtudiant = affECTSEtudiant;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isInsertionFiligranePdfNotes() {
		return insertionFiligranePdfNotes;
	}
	@ManagedAttribute
	public void setInsertionFiligranePdfNotes(boolean insertionFiligranePdfNotes) {
		this.insertionFiligranePdfNotes = insertionFiligranePdfNotes;
	}

	@ManagedAttribute
	public String getTemoinFictif() {
		return temoinFictif;
	}

	@ManagedAttribute
	public void setTemoinFictif(String temoinFictif) {
		this.temoinFictif = temoinFictif;
	}

	@ManagedAttribute
	public boolean isAffMentionEtudiant() {
		return affMentionEtudiant;
	}

	@ManagedAttribute
	public void setAffMentionEtudiant(boolean affMentionEtudiant) {
		this.affMentionEtudiant = affMentionEtudiant;
	}

	@ManagedAttribute
	public String getTemoinCtlValCadEpr() {
		return temoinCtlValCadEpr;
	}

	@ManagedAttribute
	public void setTemoinCtlValCadEpr(String temoinCtlValCadEpr) {
		this.temoinCtlValCadEpr = temoinCtlValCadEpr;
	}

	@ManagedAttribute
	public boolean isCertificatScolariteTouteAnnee() {
		return certificatScolariteTouteAnnee;
	}

	@ManagedAttribute
	public void setCertificatScolariteTouteAnnee(
			boolean certificatScolariteTouteAnnee) {
		this.certificatScolariteTouteAnnee = certificatScolariteTouteAnnee;
	}

	@ManagedAttribute
	public List<String> getCodesEtapeAffichageRang() {
		return codesEtapeAffichageRang;
	}

	@ManagedAttribute
	public void setCodesEtapeAffichageRang(List<String> codesEtapeAffichageRang) {
		this.codesEtapeAffichageRang = codesEtapeAffichageRang;
	}

	public List<String> getTypesEpreuveAffichageNote() {
		return typesEpreuveAffichageNote;
	}

	public void setTypesEpreuveAffichageNote(List<String> typesEpreuveAffichageNote) {
		this.typesEpreuveAffichageNote = typesEpreuveAffichageNote;
	}
	
	




}
