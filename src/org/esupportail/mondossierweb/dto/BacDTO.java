/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;

/**
 * DTO pour récupérer le bac
 * @author chdubois
 *
 */
public class BacDTO {

	private String lib_bac;
	private String daa_obt_bac_iba;
	private String cod_mnb;
	private String cod_tpe;
	private String cod_etb;
	private String cod_dep;
	
	public BacDTO(){
		super();
	}

	public String getLib_bac() {
		return lib_bac;
	}

	public void setLib_bac(String lib_bac) {
		this.lib_bac = lib_bac;
	}

	public String getDaa_obt_bac_iba() {
		return daa_obt_bac_iba;
	}

	public void setDaa_obt_bac_iba(String daa_obt_bac_iba) {
		this.daa_obt_bac_iba = daa_obt_bac_iba;
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

	public String getCod_etb() {
		return cod_etb;
	}

	public void setCod_etb(String cod_etb) {
		this.cod_etb = cod_etb;
	}

	public String getCod_dep() {
		return cod_dep;
	}

	public void setCod_dep(String cod_dep) {
		this.cod_dep = cod_dep;
	}
	
	
}
