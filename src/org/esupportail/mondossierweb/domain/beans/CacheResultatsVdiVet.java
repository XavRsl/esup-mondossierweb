/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.List;

/**
 * représente le cache pour stocker les résultats aux diplomes et étapes déjà récupérés pour l'étudiant dont on consulte le dossier.
 * @author Charlie Dubois
 */
public class CacheResultatsVdiVet {
	
	/**
	 * Faux si c'est les résultats visibles pour l'enseignant
	 * Vrai si c'est à destination de l'étudiant.
	 */
	private boolean vueEtudiant;
	/**
	 * les diplomes avec les résultats obtenus.
	 */
	private List<Diplome> diplomes;
	/**
	 * les etapes avec les résultats obtenus.
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
