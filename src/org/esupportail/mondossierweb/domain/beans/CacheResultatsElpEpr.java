/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.List;


/**
 * repr�sente le cache pour stocker les r�sultats aux Elp et Epreuves d�j� r�cup�r�s pour l'�tudiant dont on consulte le dossier.
 * @author Charlie Dubois
 */
public class CacheResultatsElpEpr {

	/**
	 * Faux si c'est les r�sultats visibles pour l'enseignant
	 * Vrai si c'est � destination de l'�tudiant.
	 */
	private boolean vueEtudiant;
	/**
	 * l'�tape concern�e par ces r�sultats.
	 */
	private Etape etape;
	/**
	 * la liste des �l�ments p�dagogique (avec r�sultats) d'une �tape choisie.
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
