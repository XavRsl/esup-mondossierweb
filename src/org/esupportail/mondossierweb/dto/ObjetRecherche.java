/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;
/**
 * objet utile pour renseigner les champs des requetes ibatis lors d'une recherche.
 * @author Charlie Dubois
 */
public class ObjetRecherche {
	/**
	 * l'ann�e donn�e.
	 */
	private String anneeencours;
	/**
	 * le code donn�.
	 */
	private String code;
	/**
	 * la version donn�e.
	 */
	private String version;
	/**
	 * le libelle donn�
	 */
	private String libelle;
	
	/**
	 * 
	 * @param anneeencours
	 * @param code
	 */
	public ObjetRecherche(final String anneeencours, final String code) {
		super();
		this.anneeencours = anneeencours;
		this.code = code;
	}
	/**
	 * 
	 * @param anneeencours
	 * @param code
	 * @param version
	 */
	public ObjetRecherche(final String anneeencours, final String code, final String version) {
		super();
		this.anneeencours = anneeencours;
		this.code = code;
		this.version = version;
	}
	
	public ObjetRecherche() {
		super();
	}
	public String getAnneeencours() {
		return anneeencours;
	}
	public void setAnneeencours(String anneeencours) {
		this.anneeencours = anneeencours;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
}
