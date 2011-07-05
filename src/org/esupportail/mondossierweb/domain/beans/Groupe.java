/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

/**
 * Représente un groupe d'étudiants
 * @author chdubois
 *
 */
public class Groupe {

	private String cleGroupe;
	/**
	 * le code du groupe
	 */
	private String codGroupe;
	/**
	 * le libelle du groupe
	 */
	private String libGroupe;
	/**
	 * la capacite max du groupe
	 */
	private int capMaxGpe;
	/**
	 * la capacite intermédiaire du groupe
	 */
	private int capIntGpe;
	/**
	 * vrai si capInt superieure a zero, donc on l'affiche
	 */
	private boolean affCapIntGpe;
	
	
	public Groupe() {
		super();
	}
	
	
	
	public Groupe(String codGroupe,String libGroupe, int capMaxGpe, int capIntGpe) {
		super();
		this.capIntGpe = capIntGpe;
		this.capMaxGpe = capMaxGpe;
		this.codGroupe = codGroupe;
		this.libGroupe = libGroupe;
	}



	public Groupe(String codGroupe) {
		super();
		this.codGroupe = codGroupe;
	}



	public String getCodGroupe() {
		return codGroupe;
	}
	public void setCodGroupe(String codGroupe) {
		this.codGroupe = codGroupe;
	}
	public String getLibGroupe() {
		return libGroupe;
	}
	public void setLibGroupe(String libGroupe) {
		this.libGroupe = libGroupe;
	}
	public int getCapMaxGpe() {
		return capMaxGpe;
	}
	public void setCapMaxGpe(int capMaxGpe) {
		this.capMaxGpe = capMaxGpe;
	}
	public int getCapIntGpe() {
		return capIntGpe;
	}
	public void setCapIntGpe(int capIntGpe) {
		this.capIntGpe = capIntGpe;
	}



	public String getCleGroupe() {
		return cleGroupe;
	}



	public void setCleGroupe(String cleGroupe) {
		this.cleGroupe = cleGroupe;
	}



	public boolean isAffCapIntGpe() {
		affCapIntGpe = false;
		if(capIntGpe > 0){
			affCapIntGpe = true;
		}
		return affCapIntGpe;
	}



	public void setAffCapIntGpe(boolean affCapIntGpe) {
		this.affCapIntGpe = affCapIntGpe;
	}
	
	
	
}
