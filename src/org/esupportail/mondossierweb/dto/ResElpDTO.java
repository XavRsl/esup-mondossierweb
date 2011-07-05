/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;


/**
 * DTO pour récupérer les résultats a un ELP
 * @author chdubois
 *
 */
public class ResElpDTO {

	private String cod_ses;
	private String note;
	private String res;
	private String cod_anu;
	
	public ResElpDTO(){
		super();
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getCod_anu() {
		return cod_anu;
	}

	public void setCod_anu(String cod_anu) {
		this.cod_anu = cod_anu;
	}

	public String getCod_ses() {
		return cod_ses;
	}

	public void setCod_ses(String cod_ses) {
		this.cod_ses = cod_ses;
	}
	
	
}
