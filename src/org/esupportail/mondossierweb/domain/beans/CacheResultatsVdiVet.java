/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.List;

/**
 * repr�sente le cache pour stocker les r�sultats aux diplomes et �tapes d�j� r�cup�r�s pour l'�tudiant dont on consulte le dossier.
 * @author Charlie Dubois
 */
public class CacheResultatsVdiVet {
	
	/**
	 * Faux si c'est les r�sultats visibles pour l'enseignant
	 * Vrai si c'est � destination de l'�tudiant.
	 */
	private boolean vueEtudiant;
	/**
	 * les diplomes avec les r�sultats obtenus.
	 */
	private List<Diplome> diplomes;
	/**
	 * les etapes avec les r�sultats obtenus.
	 */
	private List<Etape> etapes;
	
	/**
	 * public constructeur.
	 */
	public CacheResultatsVdiVet(){
		super();
	}

	

	public boolean isVueEtudiant() {
		return vueEtudiant;
	}



	public void setVueEtudiant(boolean vueEtudiant) {
		this.vueEtudiant = vueEtudiant;
	}



	public List<Diplome> getDiplomes() {
		return diplomes;
	}

	public void setDiplomes(List<Diplome> diplomes) {
		this.diplomes = diplomes;
	}

	public List<Etape> getEtapes() {
		return etapes;
	}

	public void setEtapes(List<Etape> etapes) {
		this.etapes = etapes;
	}
	
	

}
