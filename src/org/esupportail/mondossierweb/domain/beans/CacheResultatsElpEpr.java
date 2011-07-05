/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.List;


/**
 * représente le cache pour stocker les résultats aux Elp et Epreuves déjà récupérés pour l'étudiant dont on consulte le dossier.
 * @author Charlie Dubois
 */
public class CacheResultatsElpEpr {

	/**
	 * Faux si c'est les résultats visibles pour l'enseignant
	 * Vrai si c'est à destination de l'étudiant.
	 */
	private boolean vueEtudiant;
	/**
	 * l'étape concernée par ces résultats.
	 */
	private Etape etape;
	/**
	 * la liste des éléments pédagogique (avec résultats) d'une étape choisie.
	 */
	private List<ElementPedagogique> elementsPedagogiques;
	
	public CacheResultatsElpEpr(){
		super();
	}

	

	public boolean isVueEtudiant() {
		return vueEtudiant;
	}



	public void setVueEtudiant(boolean vueEtudiant) {
		this.vueEtudiant = vueEtudiant;
	}



	public Etape getEtape() {
		return etape;
	}

	public void setEtape(Etape etape) {
		this.etape = etape;
	}

	public List<ElementPedagogique> getElementsPedagogiques() {
		return elementsPedagogiques;
	}

	public void setElementsPedagogiques(
			List<ElementPedagogique> elementsPedagogiques) {
		this.elementsPedagogiques = elementsPedagogiques;
	}
	
	
}
