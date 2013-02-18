/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * classe qui r�pr�sente le r�sum� des notes au diplomes et �tapes pour les services HttpInvoker.
 * @author Charlie Dubois
 */
public class ResumeNotes implements Serializable {


	private static final long serialVersionUID = -4109276375019604880L;

	/**
	 * les diplomes avec les r�sultats obtenus.
	 */
	private List<Diplome> diplomes;

	/**
	 * les etapes avec les r�sultats obtenus.
	 */
	private List<Etape> etapes;
	
	/**
	 * map qui contient les couples (indice,signification) pour les r�sultats � afficher.
	 */
	private Map significationResultats;
	
	
	
	/**
	 * constructeur vide.
	 */
	public ResumeNotes() {
		super();
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


	public Map getSignificationResultats() {
		return significationResultats;
	}


	public void setSignificationResultats(Map significationResultats) {
		this.significationResultats = significationResultats;
	}


	
	


}
