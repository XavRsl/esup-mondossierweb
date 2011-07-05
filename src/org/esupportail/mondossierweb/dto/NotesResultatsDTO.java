/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;


/**
 * DTO pour récupérer les notes et résultats
 * @author chdubois
 *
 */
public class NotesResultatsDTO {

	private String cod_ses;
	private String note;
	private String res;
	private String cod_adm;
	
	public NotesResultatsDTO(){
		super();
	}

	public String getCod_ses() {
		return cod_ses;
	}

	public void setCod_ses(String cod_ses) {
		this.cod_ses = cod_ses;
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

	public String getCod_adm() {
		return cod_adm;
	}

	public void setCod_adm(String cod_adm) {
		this.cod_adm = cod_adm;
	}
	
	
	
}
