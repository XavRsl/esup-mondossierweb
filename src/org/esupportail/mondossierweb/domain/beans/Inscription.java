/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;
/**
 * représente une inscription.
 * @author Charlie Dubois
 */
public class Inscription {

	/**
	 * année de l'inscription.
	 */
	private String daa_ent_etb;
	/**
	 * libellé de l'établissement de l'inscription.
	 */
	private String lib_etb;
	/**
	 * code année.
	 */
	private String cod_anu;
	/**
	 * code étape.
	 */
	private String cod_etp;
	/**
	 * libellé de l'étape.
	 */
	private String lib_etp;
	/**
	 * code de la composante
	 */
	private String cod_comp;
	/**
	 * libellé de la composante
	 */
	private String lib_comp;
	/**
	 * code du diplome
	 */
	private String cod_dip;
	/**
	 * version du diplome
	 */
	private String vers_dip;
	/**
	 * libelle du diplome
	 */
	private String lib_dip;
	/**
	 * version de l'étape.
	 */
	private String cod_vrs_vet;
	
	/**
	 * vrai si inscription en regle (payee)
	 */
	private boolean estEnRegle;
	/**
	 * code individu
	 */
	private String cod_ind;
	/**
	 * code dac.
	 */
	private String cod_dac;
	/**
	 * libelle composante.
	 */
	private String lib_cmt_dac;
	/**
	 * résultat.
	 */
	private String res;
	
	/**
	 * constucteur.
	 *
	 */
	public Inscription() {
		super();
	}

	public String getDaa_ent_etb() {
		return daa_ent_etb;
	}

	public void setDaa_ent_etb(String daa_ent_etb) {
		this.daa_ent_etb = daa_ent_etb;
	}

	public String getLib_etb() {
		return lib_etb;
	}

	public void setLib_etb(String lib_etb) {
		this.lib_etb = lib_etb;
	}

	public String getCod_anu() {
		return cod_anu;
	}

	public void setCod_anu(String cod_anu) {
		this.cod_anu = cod_anu;
	}

	public String getCod_etp() {
		return cod_etp;
	}

	public void setCod_etp(String cod_etp) {
		this.cod_etp = cod_etp;
	}

	public String getCod_ind() {
		return cod_ind;
	}

	public void setCod_ind(String cod_ind) {
		this.cod_ind = cod_ind;
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

	public String getCod_dac() {
		return cod_dac;
	}

	public void setCod_dac(String cod_dac) {
		this.cod_dac = cod_dac;
	}

	public String getLib_cmt_dac() {
		return lib_cmt_dac;
	}

	public void setLib_cmt_dac(String lib_cmt_dac) {
		this.lib_cmt_dac = lib_cmt_dac;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}
	
	public String getCodEtpCodAnuConcat() {
		return cod_etp + cod_anu;
	}

	public boolean isEstEnRegle() {
		return estEnRegle;
	}

	public void setEstEnRegle(boolean estEnRegle) {
		this.estEnRegle = estEnRegle;
	}

	public String getCod_dip() {
		return cod_dip;
	}

	public void setCod_dip(String cod_dip) {
		this.cod_dip = cod_dip;
	}

	public String getVers_dip() {
		return vers_dip;
	}

	public void setVers_dip(String vers_dip) {
		this.vers_dip = vers_dip;
	}

	public String getLib_dip() {
		return lib_dip;
	}

	public void setLib_dip(String lib_dip) {
		this.lib_dip = lib_dip;
	}

	public String getCod_comp() {
		return cod_comp;
	}

	public void setCod_comp(String cod_comp) {
		this.cod_comp = cod_comp;
	}

	public String getLib_comp() {
		return lib_comp;
	}

	public void setLib_comp(String lib_comp) {
		this.lib_comp = lib_comp;
	}
	
	
	
}
