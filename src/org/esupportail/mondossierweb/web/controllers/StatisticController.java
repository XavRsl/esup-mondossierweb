package org.esupportail.mondossierweb.web.controllers;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * bean contenant les statistiques.
 * @author Charlie Dubois
 */
@ManagedResource(objectName = "spring:name=statisticEsupMonDossierWeb", description = "les statistiques du dossier etudiant")
public class StatisticController {

	/**
	 * le nombre de connexion depuis le lancement du Serveur d'application.
	 */
	private int nbConnexion;
	
	/**
	 * le nombre de connexion d'étudiants depuis le lancement du Serveur d'application.
	 */
	private int nbConnexionEtudiant;
	
	/**
	 * le nombre de connexion d'enseignants depuis le lancement du Serveur d'application.
	 */
	private int nbConnexionEnseignant;

	/**
	 * le constructeur vide.
	 */
	public StatisticController() {
		super();
		nbConnexion = 0;
		nbConnexionEtudiant = 0;
		nbConnexionEnseignant = 0;
	}
	
	@ManagedAttribute
	public int getNbConnexion() {
		return nbConnexion;
	}

	public void setNbConnexion(int nbConnexion) {
		this.nbConnexion = nbConnexion;
	}
	
	@ManagedAttribute
	public int getNbConnexionEtudiant() {
		return nbConnexionEtudiant;
	}

	public void setNbConnexionEtudiant(int nbConnexionEtudiant) {
		this.nbConnexionEtudiant = nbConnexionEtudiant;
	}

	@ManagedAttribute
	public int getNbConnexionEnseignant() {
		return nbConnexionEnseignant;
	}

	public void setNbConnexionEnseignant(int nbConnexionEnseignant) {
		this.nbConnexionEnseignant = nbConnexionEnseignant;
	}

	/**
	 * incremente le nombre de connexion étudiant depuis le lancement du Serveur d'application.
	 */
	public void incNbConnexionEtudiant(){
		nbConnexionEtudiant++;
		nbConnexion++;
	}
	
	/**
	 * incremente le nombre de connexion enseignant depuis le lancement du Serveur d'application.
	 */
	public void incNbConnexionEnseignant(){
		nbConnexionEnseignant++;
		nbConnexion++;
	}
}
