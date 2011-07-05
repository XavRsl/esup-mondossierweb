/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;


/**
 * DTO pour récupérer une épreuve et ses notes et résultat
 * @author chdubois
 *
 */
public class EpreuveDTO {
	
	private String cod_epr;
	private String cod_anu;
	private String lib_epr;
	private String cod_ses;
	private String note;
	
	public EpreuveDTO(){
		super();
	}

	public String getCod_epr() {
		return cod_epr;
	}

	public void setCod_epr(String cod_epr) {
		this.cod_epr = cod_epr;
	}

	public String getCod_anu() {
		return cod_anu;
	}

	public void setCod_anu(String cod_anu) {
		this.cod_anu = cod_anu;
	}

	public String getLib_epr() {
		return lib_epr;
	}

	public void setLib_epr(String lib_epr) {
		this.lib_epr = lib_epr;
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
	
	

}
