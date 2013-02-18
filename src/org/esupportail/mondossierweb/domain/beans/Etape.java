/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * repr�sente une �tape.
 * @author Charlie Dubois
 */
public class Etape implements Serializable {

	private static final long serialVersionUID = -4920377897525522629L;
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
	 * code du dipl�me p�re.
	 */
	private String cod_dip;
	/**
	 * version du diplome p�re;
	 */
	private int vers_dip;
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
	 * vrai si on doit afficher le rang de l'�tudiant � l'�tape.
	 */
	private boolean afficherRang;
	
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
	public String getCod_dip() {
		return cod_dip;
	}
	public void setCod_dip(String cod_dip) {
		this.cod_dip = cod_dip;
	}
	public int getVers_dip() {
		return vers_dip;
	}
	public void setVers_dip(int vers_dip) {
		this.vers_dip = vers_dip;
	}
	public boolean isAfficherRang() {
		return afficherRang;
	}
	public void setAfficherRang(boolean afficherRang) {
		this.afficherRang = afficherRang;
	}
	
	
}
