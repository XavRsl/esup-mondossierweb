package org.esupportail.mondossierweb.domain.beans;

import java.sql.Date;

public class CalendrierRentree {
	
	/*
	 ******************* PROPERTIES ******************* */
	

	

	/**
	 * Code Centre de Gestion (CGE).
	 */
	private String codCge;

	/**
	 * Code Etape. 
	 */
	private String codEtp;
	
	/**
	 * Libellé Etape. 
	 */
	private String libEtp;
	/**
	 * Code Version Etape. 
	 */
	private Integer codVrsVet;
	
	/**
	 * Date de rentrée.
	 */
	private Date date;
	
	/**
	 * Heure de rentrée.
	 */
	private String heure;
	
	/**
	 * Minute de Rentrée.
	 */
	private String minute;
	
	/**
	 * Lieu de rentrée.
	 */
	private String lieu;
	
	/**
	 * Commentaire.
	 */
	private String commentaire;
	
	/**
	 * Page d'infos
	 */
	private String pageInfos;
	

	/**
	 * @return the codCge
	 */
	public String getCodCge() {
		return codCge;
	}

	/**
	 * @param codCge the codCge to set
	 */
	public void setCodCge(String codCge) {
		this.codCge = codCge;
	}

	/**
	 * @return the codEtp
	 */
	public String getCodEtp() {
		return codEtp;
	}

	/**
	 * @param codEtp the codEtp to set
	 */
	public void setCodEtp(String codEtp) {
		this.codEtp = codEtp;
	}

	/**
	 * @return the codVrsVet
	 */
	public Integer getCodVrsVet() {
		return codVrsVet;
	}

	/**
	 * @param codVrsVet the codVrsVet to set
	 */
	public void setCodVrsVet(Integer codVrsVet) {
		this.codVrsVet = codVrsVet;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the heure
	 */
	public String getHeure() {
		return heure;
	}

	/**
	 * @param heure the heure to set
	 */
	public void setHeure(String heure) {
		this.heure = heure;
	}

	/**
	 * @return the minute
	 */
	public String getMinute() {
		return minute;
	}

	/**
	 * @param minute the minute to set
	 */
	public void setMinute(String minute) {
		this.minute = minute;
	}

	/**
	 * @return the lieu
	 */
	public String getLieu() {
		return lieu;
	}

	/**
	 * @param lieu the lieu to set
	 */
	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	/**
	 * @return the commentaire
	 */
	public String getCommentaire() {
		return commentaire;
	}

	/**
	 * @param commentaire the commentaire to set
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	/**
	 * @return the pageInfos
	 */
	public String getPageInfos() {
		return pageInfos;
	}

	/**
	 * @param pageInfos the pageInfos to set
	 */
	public void setPageInfos(String pageInfos) {
		this.pageInfos = pageInfos;
	}

	/**
	 * @param libEtp
	 */
	public void setLibEtp(String libEtp) {
		this.libEtp = libEtp;
	}

	/**
	 * @return
	 */
	public String getLibEtp() {
		return libEtp;
	}


}
