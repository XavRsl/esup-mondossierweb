/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

/**
 * représente un étudiant inscrit lors de la rechercher dans la partie enseignant.
 * @author Charlie Dubois
 */
public class Inscrit {
	/**
	 * le code individu.
	 */
	private String cod_ind;
	/**
	 * le code etudiant.
	 */
	private String cod_etu;
	/**
	 * le nom.
	 */
	private String lib_nom_pat_ind;
	/**
	 * le 1er prenom.
	 */
	private String lib_pr1_ind;
	/**
	 * la date de naissance.
	 */
	private String date_nai_ind;
	/**
	 * l'iae.
	 */
	private String iae;
	/**
	 * le login.
	 */
	private String login;
	/**
	 * la note de la session de juin.
	 */	
	private String notej;
	/**
	 * le résultat de la session de juin.
	 */
	private String resj;
	/**
	 * la note de la session de septembre.
	 */
	private String notes;
	/**
	 * le résultat de septembre.
	 */
	private String ress;
	/**
	 * le code étape où l'étudiant est incrit.
	 */
	private String cod_etp;
	/**
	 * la version de l'étape.
	 */
	private String cod_vrs_vet;
	/**
	 * le libellé de l'étape.
	 */
	private String lib_etp;
	/**
	 * l'e-mail.
	 */
	private String email;
	/**
	 * l'url  de la photo.
	 */
	private String urlphoto;
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "inscrit=  cod_etu : "+cod_etu+" nom : "+lib_nom_pat_ind+" prenom : "+lib_pr1_ind;
	}
	/**
	 * constructeur.
	 */
	public Inscrit() {
		super();
		cod_ind = "";
		cod_etu = "";
		lib_nom_pat_ind = "";
		lib_pr1_ind = "";
		date_nai_ind = "";
		iae = "";
		notej = "";
		resj = "";
		notes = "";
		ress = "";
		cod_etp = "";
		cod_vrs_vet = "";
		lib_etp = "";
		email = "";
		urlphoto = "";
	}
	public String getCod_etp() {
		return cod_etp;
	}
	public void setCod_etp(String cod_etp) {
		this.cod_etp = cod_etp;
	}
	public String getCod_vrs_vet() {
		return cod_vrs_vet;
	}
	public void setCod_vrs_vet(String cod_vrs_vet) {
		this.cod_vrs_vet = cod_vrs_vet;
	}
	public String getLib_etp() {
		return lib_etp;
	}
	public void setLib_etp(String lib_etp) {
		this.lib_etp = lib_etp;
	}
	
	public String getCod_etu() {
		return cod_etu;
	}
	public void setCod_etu(String cod_etu) {
		this.cod_etu = cod_etu;
	}
	public String getCod_ind() {
		return cod_ind;
	}
	public void setCod_ind(String cod_ind) {
		this.cod_ind = cod_ind;
	}
	public String getDate_nai_ind() {
		return date_nai_ind;
	}
	public void setDate_nai_ind(String date_nai_ind) {
		this.date_nai_ind = date_nai_ind;
	}
	public String getIae() {
		return iae;
	}
	public void setIae(String iae) {
		this.iae = iae;
	}
	public String getLib_nom_pat_ind() {
		return lib_nom_pat_ind;
	}
	public void setLib_nom_pat_ind(String lib_nom_pat_ind) {
		this.lib_nom_pat_ind = lib_nom_pat_ind;
	}
	public String getLib_pr1_ind() {
		return lib_pr1_ind;
	}
	public void setLib_pr1_ind(String lib_pr1_ind) {
		this.lib_pr1_ind = lib_pr1_ind;
	}
	public String getNotej() {
		return notej;
	}
	public void setNotej(String notej) {
		this.notej = notej;
	}
	public String getResj() {
		return resj;
	}
	public void setResj(String resj) {
		this.resj = resj;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getRess() {
		return ress;
	}
	public void setRess(String ress) {
		this.ress = ress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrlphoto() {
		return urlphoto;
	}
	public void setUrlphoto(String urlphoto) {
		this.urlphoto = urlphoto;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	
}
