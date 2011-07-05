/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

/**
 * Composante.
 * @author Charlie Dubois
 */
public class Composante {
	/**
	 * code de la compososante.
	 */
	private String cod_cmp;
	/**
	 * libelle de la composante.
	 */
	private String lib_cmp;
	
	/**
	 * constructeur vide.
	 *
	 */
	public Composante() {
		super();
	}
	public String getCod_cmp() {
		return cod_cmp;
	}
	public void setCod_cmp(String cod_cmp) {
		this.cod_cmp = cod_cmp;
	}
	public String getLib_cmp() {
		return lib_cmp;
	}
	public void setLib_cmp(String lib_cmp) {
		this.lib_cmp = lib_cmp;
	}
	
	
}
