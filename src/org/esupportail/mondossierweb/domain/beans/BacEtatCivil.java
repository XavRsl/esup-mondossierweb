/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

/**
 * classe qui décrit le bac obtenu par l'étudiant.
 * @author Charlie Dubois
 */
public class BacEtatCivil {
	
	/**
	 * le libellé du bac.
	 */
	private String lib_bac;
	/**
	 * l'année d'obtention du bac.
	 */
	private String daa_obt_bac_iba;
	/**
	 * le code de la mention obtenue.
	 */
	private String cod_mnb;
	/**
	 * le code du type d'établissement.
	 */
	private String cod_tpe;
	/**
	 * le code de l'établissement.
	 */
	private String cod_etb;
	/**
	 * le code du département.
	 */
	private String cod_dep;
	
	/**
	 * constructeur vide.
	 *
	 */
	public BacEtatCivil() {
		super();
	}

	public String getCod_dep() {
		return cod_dep;
	}

	public void setCod_dep(String cod_dep) {
		this.cod_dep = cod_dep;
	}

	public String getCod_etb() {
		return cod_etb;
	}

	public void setCod_etb(String cod_etb) {
		this.cod_etb = cod_etb;
	}

	public String getCod_mnb() {
		return cod_mnb;
	}

	public void setCod_mnb(String cod_mnb) {
		this.cod_mnb = cod_mnb;
	}

	public String getCod_tpe() {
		return cod_tpe;
	}

	public void setCod_tpe(String cod_tpe) {
		this.cod_tpe = cod_tpe;
	}

	public String getDaa_obt_bac_iba() {
		return daa_obt_bac_iba;
	}

	public void setDaa_obt_bac_iba(String daa_obt_bac_iba) {
		this.daa_obt_bac_iba = daa_obt_bac_iba;
	}

	public String getLib_bac() {
		return lib_bac;
	}

	public void setLib_bac(String lib_bac) {
		this.lib_bac = lib_bac;
	}
	
}
