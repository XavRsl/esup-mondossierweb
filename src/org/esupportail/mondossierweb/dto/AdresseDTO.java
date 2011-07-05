/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;

/**
 * DTO pour récupérer les adresses.
 * @author chdubois
 *
 */
public class AdresseDTO {
	
	private String lib_ad1;
	private String lib_ad2;
	private String lib_ad3;
	private String num_tel;
	private String lib_ade;
	private String cod_bdi;
	private String lib_ach;
	private String pays;
	private String lic_anu;
	private String num_tel_port;
	private String adr_mail;
	private String lib_thb;
	
	public AdresseDTO(){
		super();
	}

	public String getLib_ad1() {
		return lib_ad1;
	}

	public void setLib_ad1(String lib_ad1) {
		this.lib_ad1 = lib_ad1;
	}

	public String getLib_ad2() {
		return lib_ad2;
	}

	public void setLib_ad2(String lib_ad2) {
		this.lib_ad2 = lib_ad2;
	}

	public String getLib_ad3() {
		return lib_ad3;
	}

	public void setLib_ad3(String lib_ad3) {
		this.lib_ad3 = lib_ad3;
	}

	public String getNum_tel() {
		return num_tel;
	}

	public void setNum_tel(String num_tel) {
		this.num_tel = num_tel;
	}

	

	public String getLib_ade() {
		return lib_ade;
	}

	public void setLib_ade(String lib_ade) {
		this.lib_ade = lib_ade;
	}

	public String getCod_bdi() {
		return cod_bdi;
	}

	public void setCod_bdi(String cod_bdi) {
		this.cod_bdi = cod_bdi;
	}

	public String getLib_ach() {
		return lib_ach;
	}

	public void setLib_ach(String lib_ach) {
		this.lib_ach = lib_ach;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public String getLic_anu() {
		return lic_anu;
	}

	public void setLic_anu(String lic_anu) {
		this.lic_anu = lic_anu;
	}

	public String getNum_tel_port() {
		return num_tel_port;
	}

	public void setNum_tel_port(String num_tel_port) {
		this.num_tel_port = num_tel_port;
	}

	public String getAdr_mail() {
		return adr_mail;
	}

	public void setAdr_mail(String adr_mail) {
		this.adr_mail = adr_mail;
	}

	public String getLib_thb() {
		return lib_thb;
	}

	public void setLib_thb(String lib_thb) {
		this.lib_thb = lib_thb;
	}


	
}
