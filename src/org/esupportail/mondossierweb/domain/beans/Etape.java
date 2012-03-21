/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * repr�sente une �tape.
 * @author Charlie Dubois
 */
public class Etape {

	/**
	 * code de l'�tape.
	 */
	private String code;
	/**
	 * version de l'�tape.
	 */
	private String version;
	/**
	 * ann�e de l'�tape.
	 */
	private String annee;
	/**
	 * libell� de l'�tape.
	 */
	private String libelle;
	/**
	 * le rang de l'�tudiant pour son r�sultat a l'�tape.
	 */
	private String rang;
	/**
	 * les r�sultats.
	 */
	private List<Resultat> resultats;
	/**
	 * vrai si les r�sultat � l'�preuve sont d�finitifs.
	 */
	private boolean deliberationTerminee;
	
	/**
	 * constructeur.
	 * @param code
	 * @param version
	 * @param annee
	 */
	public Etape(String code, String version, String annee) {
		super();
		this.code = code;
		this.version = version;
		this.annee = annee;
		resultats = new ArrayList<Resultat>();
		deliberationTerminee = false;
	}
	/**
	 * constructeur vide.
	 *
	 */
	public Etape() {
		super();
		resultats = new ArrayList<Resultat>();
	}
	public String getAnnee() {
		return annee;
	}
	public void setAnnee(String annee) {
		this.annee = annee;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public List<Resultat> getResultats() {
		return resultats;
	}
	public void setResultats(List<Resultat> resultats) {
		this.resultats = resultats;
	}
	public boolean isDeliberationTerminee() {
		return deliberationTerminee;
	}
	public void setDeliberationTerminee(boolean deliberationTerminee) {
		this.deliberationTerminee = deliberationTerminee;
	}
	public String getRang() {
		return rang;
	}
	public void setRang(String rang) {
		this.rang = rang;
	}
	
}
