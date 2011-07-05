/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;

/**
 * DTP pour récupérer les inscriptions pédagogique pour une VET donnée.
 * @author chdubois
 *
 */
public class IpDTO {

	private String level;
	private String cod_elp;
	private String cod_anu;
	private String tem_prc_ice;
	private String cod_lcc_ice;
	
	public IpDTO(){
		super();
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCod_elp() {
		return cod_elp;
	}

	public void setCod_elp(String cod_elp) {
		this.cod_elp = cod_elp;
	}

	public String getCod_anu() {
		return cod_anu;
	}

	public void setCod_anu(String cod_anu) {
		this.cod_anu = cod_anu;
	}

	public String getTem_prc_ice() {
		return tem_prc_ice;
	}

	public void setTem_prc_ice(String tem_prc_ice) {
		this.tem_prc_ice = tem_prc_ice;
	}

	public String getCod_lcc_ice() {
		return cod_lcc_ice;
	}

	public void setCod_lcc_ice(String cod_lcc_ice) {
		this.cod_lcc_ice = cod_lcc_ice;
	}
	
	
}
