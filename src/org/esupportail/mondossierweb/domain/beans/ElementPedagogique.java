/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.io.Serializable;

/**
 * représente un élément pédagogique.
 * @author Charlie Dubois
 */
public class ElementPedagogique implements Serializable {
	
	private static final long serialVersionUID = 5197935378325878240L;
	/**
	 * code de l'élément pédagogique.
	 */
	private String code;
	/**
	 * année de l'élément pédagogique.
	 */
	private String annee;
	/**
	 * libellé de l'élément pédagogique.
	 */
	private String libelle;
	/**
	 * vrai si l'elp est une epreuve
	 */
	private boolean isEpreuve;
	/**
	 * note session de juin.
	 */
	private String note1;
	/**
	 * le barement pour la note1
	 */
	private int bareme1;
	/**
	 * résultat session de juin.
	 */
	private String res1;
	/**
	 * note session de septembre.
	 */
	private String note2;
	/**
	 * Le bareme pour la note2
	 */
	private int bareme2;
	/**
	 * résultat session de septembre.
	 */
	private String res2;
	/**
	 * le rang de l'étudiant pour son résultat à l'elp.
	 */
	private String rang;
	/**
	 * ects saisi dans la structure des enseignements.
	 */
	private String ects;
	/**
	 * le témoin fictif
	 */
	private String temFictif;
	/**
	 * niveau dans l'arborescence.
	 */
	private int level;
	/**
	 * elp supérieur (père).
	 */
	private String codElpSup;
	

	/**
	 * constructeur.
	 * @param code
	 * @param annee
	 */
	public ElementPedagogique(final String code, final String annee) {
		super();
		this.code = code;
		this.annee = annee;
		note1 = "";
		res1 = "";
		note2 = "";
		res2 = "";
		level = 0;
		codElpSup = "";
	}
	/**
	 * constructeur vide.
	 *
	 */
	public ElementPedagogique() {
		super();
		note1 = "";
		res1 = "";
		note2 = "";
		res2 = "";
		level = 0;
		codElpSup = "";
	}
	public String getAnnee() {
		return annee;
	}
	public void setAnnee(final String annee) {
		this.annee = annee;
	}
	public String getCode() {
		return code;
	}
	public void setCode(final String code) {
		this.code = code;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(final String libelle) {
		this.libelle = libelle;
	}
	public String getNote1() {
		return note1;
	}
	public void setNote1(final String note1) {
		this.note1 = note1;
	}
	public String getNote2() {
		return note2;
	}
	public void setNote2(final String note2) {
		this.note2 = note2;
	}
	public String getRes1() {
		return res1;
	}
	public void setRes1(final String res1) {
		this.res1 = res1;
	}
	public String getRes2() {
		return res2;
	}
	public void setRes2(final String res2) {
		this.res2 = res2;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getCodElpSup() {
		return codElpSup;
	}
	public void setCodElpSup(String codElpSup) {
		this.codElpSup = codElpSup;
	}
	public int getBareme1() {
		return bareme1;
	}
	public void setBareme1(int bareme1) {
		this.bareme1 = bareme1;
	}
	public int getBareme2() {
		return bareme2;
	}
	public void setBareme2(int bareme2) {
		this.bareme2 = bareme2;
	}
	public String getRang() {
		return rang;
	}
	public void setRang(String rang) {
		this.rang = rang;
	}
	public String getEcts() {
		return ects;
	}
	public void setEcts(String ects) {
		this.ects = ects;
	}
	public String getTemFictif() {
		return temFictif;
	}
	public void setTemFictif(String temFictif) {
		this.temFictif = temFictif;
	}
	public boolean isEpreuve() {
		return isEpreuve;
	}
	public void setEpreuve(boolean isEpreuve) {
		this.isEpreuve = isEpreuve;
	}
	
	
}
