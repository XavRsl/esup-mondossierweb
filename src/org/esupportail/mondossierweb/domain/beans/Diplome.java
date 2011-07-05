/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Diplôme.
 * @author Charlie Dubois
 */
public class Diplome {
	/**
	 * code de la composante a laquelle appartient le diplôme.
	 */
	private String cod_cmp;
	/**
	 * libelle de la composante a laquelle appartient le diplôme.
	 */
	private String lib_cmp;
	/**
	 * code du diplôme.
	 */
	private String cod_dip;
	/**
	 * code type diplôme établissement.
	 */
	private String cod_tpd_etb;
	/**
	 * version du diplôme.
	 */
	private String cod_vrs_vdi;
	/**
	 * libelle du diplôme.
	 */
	private String lib_web_vdi;
	/**
	 * année universitaire d'obtention.
	 */
	private String annee;
	/**
	 * le rang de l'étudiant pour son résultat au diplome.
	 */
	private String rang;
	/**
	 * les résultats.
	 */
	private List<Resultat> resultats;
	
	/**
	 * constructeur vide.
	 *
	 */
	public Diplome() {
		super();
		resultats = new ArrayList<Resultat>();
	}
	public String getCod_cmp() {
		return cod_cmp;
	}
	public void setCod_cmp(String cod_cmp) {
		this.cod_cmp = cod_cmp;
	}
	public String getCod_dip() {
		return cod_dip;
	}
	public void setCod_dip(String cod_dip) {
		this.cod_dip = cod_dip;
	}
	public String getCod_tpd_etb() {
		return cod_tpd_etb;
	}
	public void setCod_tpd_etb(String cod_tpd_etb) {
		this.cod_tpd_etb = cod_tpd_etb;
	}
	public String getCod_vrs_vdi() {
		return cod_vrs_vdi;
	}
	public void setCod_vrs_vdi(String cod_vrs_vdi) {
		this.cod_vrs_vdi = cod_vrs_vdi;
	}
	public String getLib_cmp() {
		return lib_cmp;
	}
	public void setLib_cmp(String lib_cmp) {
		this.lib_cmp = lib_cmp;
	}
	public String getLib_web_vdi() {
		return lib_web_vdi;
	}
	public void setLib_web_vdi(String lib_web_vdi) {
		this.lib_web_vdi = lib_web_vdi;
	}
	public String getAnnee() {
		return annee;
	}
	public void setAnnee(final String annee) {
		this.annee = annee;
	}
	public List<Resultat> getResultats() {
		return resultats;
	}
	public void setResultats(List<Resultat> resultats) {
		this.resultats = resultats;
	}
	public String getRang() {
		return rang;
	}
	public void setRang(String rang) {
		this.rang = rang;
	}
	
	
}
