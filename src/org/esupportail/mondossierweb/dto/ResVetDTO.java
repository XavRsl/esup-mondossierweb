/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;


/**
 * dto pour récupérer les resultat a une VET
 * @author chdubois
 *
 */
public class ResVetDTO {

	private String note;
	private String res;
	private String eta_avc_vet;
	
	public ResVetDTO(){
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

	public String getEta_avc_vet() {
		return eta_avc_vet;
	}

	public void setEta_avc_vet(String eta_avc_vet) {
		this.eta_avc_vet = eta_avc_vet;
	}
	
	
}
