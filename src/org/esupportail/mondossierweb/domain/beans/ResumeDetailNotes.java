/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * classe qui réprésente le résumé des notes aux ELPs pour les services HttpInvoker.
 * @author Charlie Dubois
 */
public class ResumeDetailNotes implements Serializable {


	private static final long serialVersionUID = -6920901651235285550L;

	/**
	 * les elements pédagogique.
	 */
	private List<ElementPedagogique> elementsPedagogiques;
	
	/**
	 * map qui contient les couples (indice,signification) pour les résultats à afficher.
	 */
	private Map significationResultats;
	
	
	
	/**
	 * constructeur vide.
	 */
	public ResumeDetailNotes() {
		super();
	}

	

	public List<ElementPedagogique> getElementsPedagogiques() {
		return elementsPedagogiques;
	}



	public void setElementsPedagogiques(
			List<ElementPedagogique> elementsPedagogiques) {
		this.elementsPedagogiques = elementsPedagogiques;
	}



	public Map getSignificationResultats() {
		return significationResultats;
	}


	public void setSignificationResultats(Map significationResultats) {
		this.significationResultats = significationResultats;
	}


	
	


}
