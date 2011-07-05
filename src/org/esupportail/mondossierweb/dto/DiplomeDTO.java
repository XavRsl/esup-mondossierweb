/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;

/**
 * DTO pour récupérer les diplomes
 * @author chdubois
 *
 */
public class DiplomeDTO {

	private String anu;
	private String vdi;
	private String lic_vdi;
	private String cod_anu;
	private String cod_dip;
	
	public DiplomeDTO(){
		super();
	}

	public String getAnu() {
		return anu;
	}

	public void setAnu(String anu) {
		this.anu = anu;
	}

	public String getVdi() {
		return vdi;
	}

	public void setVdi(String vdi) {
		this.vdi = vdi;
	}

	public String getLic_vdi() {
		return lic_vdi;
	}

	public void setLic_vdi(String lic_vdi) {
		this.lic_vdi = lic_vdi;
	}

	public String getCod_anu() {
		return cod_anu;
	}

	public void setCod_anu(String cod_anu) {
		this.cod_anu = cod_anu;
	}

	public String getCod_dip() {
		return cod_dip;
	}

	public void setCod_dip(String cod_dip) {
		this.cod_dip = cod_dip;
	}
	
	
}
