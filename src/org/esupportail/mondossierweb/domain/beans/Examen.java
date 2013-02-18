/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.io.Serializable;

/**
 * classe qui représente un examen du calendrier.
 * @author Charlie Dubois
 */
public class Examen implements Serializable {


	private static final long serialVersionUID = -8989355093199939179L;
	/**
	 * date de l'événement.
	 */
	private String date;
	/**
	 * heure de l'événement.
	 */
	private String heure;
	/**
	 * duree de l'événement.
	 */
	private String duree;
	/**
	 * salle ou se déroule l'événement.
	 */
	private String salle;
	/**
	 * place dans la salle.
	 */
	private String place;
	/**
	 * batiment ou se déroule l'événement.
	 */
	private String batiment;
	/**
	 * épreuve concernée par l'événement.
	 */
	private String epreuve;
	/**
	 * centre incompatibilité concernée par l'événement.
	 */
	private String codCin;
		
	/**
	 * constructeur.
	 *
	 */
	public Examen() {
		super();
	}

	public String getBatiment() {
		return batiment;
	}

	public void setBatiment(String batiment) {
		this.batiment = batiment;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDuree() {
		return duree;
	}

	public void setDuree(String duree) {
		this.duree = duree;
	}

	public String getEpreuve() {
		return epreuve;
	}

	public void setEpreuve(String epreuve) {
		this.epreuve = epreuve;
	}

	public String getHeure() {
		return heure;
	}

	public void setHeure(String heure) {
		this.heure = heure;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getSalle() {
		return salle;
	}

	public void setSalle(String salle) {
		this.salle = salle;
	}

	/**
	 * @return the codCin
	 */
	public String getCodCin() {
		return codCin;
	}

	/**
	 * @param codCin the codCin to set
	 */
	public void setCodCin(String codCin) {
		this.codCin = codCin;
	}
}
