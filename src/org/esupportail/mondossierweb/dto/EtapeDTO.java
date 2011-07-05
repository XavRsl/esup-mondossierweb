/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;

/**
 * DTO pour récupérer les étapes.
 * @author chdubois
 *
 */
public class EtapeDTO {
	
	private String anu;
	private String vet;
	private String lib_etp;
	private String cod_anu;
	private String cod_etp;

	public EtapeDTO(){
		super();
	}

	public String getAnu() {
		return anu;
	}

	public void setAnu(String anu) {
		this.anu = anu;
	}

	public String getVet() {
		return vet;
	}

	public void setVet(String vet) {
		this.vet = vet;
	}

	public String getLib_etp() {
		return lib_etp;
	}

	public void setLib_etp(String lib_etp) {
		this.lib_etp = lib_etp;
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
	
	
}
