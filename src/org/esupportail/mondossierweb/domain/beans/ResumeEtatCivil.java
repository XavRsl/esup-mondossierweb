/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.io.Serializable;




/**
 * classe qui réprésente le résumé de l'état civil pour les services HttpInvoker.
 * @author Charlie Dubois
 */
public class ResumeEtatCivil implements Serializable {


	private static final long serialVersionUID = 1390880693585983066L;
	
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
	 * date de naissance.
	 */
	private String datenaissance;

	/**
	 * constructeur vide.
	 */
	public ResumeEtatCivil() {
		super();
	}

	public String getCod_ind() {
		return cod_ind;
	}

	public void setCod_ind(String cod_ind) {
		this.cod_ind = cod_ind;
	}

	public String getCod_etu() {
		return cod_etu;
	}

	public void setCod_etu(String cod_etu) {
		this.cod_etu = cod_etu;
	}

	public String getCod_nne() {
		return cod_nne;
	}

	public void setCod_nne(String cod_nne) {
		this.cod_nne = cod_nne;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDatenaissance() {
		return datenaissance;
	}

	public void setDatenaissance(String datenaissance) {
		this.datenaissance = datenaissance;
	}
	
	
	


}
