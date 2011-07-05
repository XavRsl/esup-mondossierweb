/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;


/**
 * DTO pour la signification des codes résultats
 * @author chdubois
 *
 */
public class SignificationResultatDTO {
	
	private String cod_tre;
	private String lib_tre;
	
	public SignificationResultatDTO(){
		super();
	}

	public String getCod_tre() {
		return cod_tre;
	}

	public void setCod_tre(String cod_tre) {
		this.cod_tre = cod_tre;
	}

	public String getLib_tre() {
		return lib_tre;
	}

	public void setLib_tre(String lib_tre) {
		this.lib_tre = lib_tre;
	}

	
}
