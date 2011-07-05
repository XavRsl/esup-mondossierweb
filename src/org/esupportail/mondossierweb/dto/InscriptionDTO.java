/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;

/**
 * Dto pour récupérer une inscription.
 * @author chdubois
 *
 */
public class InscriptionDTO {

	
	private String cod_anu;
	private String lib_etb;
	private String cod_etp;
	private String cod_vrs_vet;
	private String lib_etp;
	private String res;
	private String lib_cmt_dac;
	private String cod_dac;
	
	public InscriptionDTO(){
		super();
	}

	public String getCod_anu() {
		return cod_anu;
	}

	public void setCod_anu(String cod_anu) {
		this.cod_anu = cod_anu;
	}

	public String getLib_etb() {
		return lib_etb;
	}

	public void setLib_etb(String lib_etb) {
		this.lib_etb = lib_etb;
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

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getLib_cmt_dac() {
		return lib_cmt_dac;
	}

	public void setLib_cmt_dac(String lib_cmt_dac) {
		this.lib_cmt_dac = lib_cmt_dac;
	}

	public String getCod_dac() {
		return cod_dac;
	}

	public void setCod_dac(String cod_dac) {
		this.cod_dac = cod_dac;
	}

	
}
