/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;


/**
 * repr�sente un �l�ment de larborescence dans laquelle on recherche
 * l'�tape ou l'�l�ment .
 * @author Charlie Dubois
 */
public class ElementArborescence implements Cloneable {
	
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(ElementArborescence.class);
	/**
	 * Code de l'�l�ment.
	 */ 
	private String code;
	/**
	 * le type des Fils : composante, diplome, etape ou elp.
	 */
	private String typeFils;
	/**
	 * Version de l'�l�ment.
	 */
	private String version;
	/**
	 * Libell� de l'�l�ment.
	 */
	private String libelle;
	/**
	 * Position de l'�l�ment dans le chemin (0,1,...).
	 */
	private Integer position;

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\n-ElementArbo:  -code : "+code
		+" version : "+version
		+" libelle : "+libelle
		+" position :"+position;
	}
	
	/**
	 * Liste des fils de l'�l�ments.
	 */
	private List listeFils;
	
	/**
	 * constructeur.
	 *
	 */	
	public ElementArborescence() {
		this.code = "";
		this.version = "";
		this.position = new Integer(0);
		this.listeFils = null;
	}
	
	/**
	 * constructeur.
	 * @param code
	 * @param version
	 * @param libelle
	 */
	public ElementArborescence(final String code, final String version, final String libelle) {
		this.code = code;
		this.version = version;
		this.libelle = libelle;
	}
	/**
	 * constructeur.
	 * @param code
	 * @param version
	 * @param libelle
	 * @param position
	 */
	public ElementArborescence(final String code, final String version, final String libelle, final Integer position) {
		this(code, version, libelle);
		this.position = position;
	}
	
	
	
	public Object clone() {
		ElementArborescence o = null;
	    try {
	    	// On r�cup�re l'instance � renvoyer par l'appel de la 
	      	// m�thode super.clone()
	      	o = (ElementArborescence) super.clone();
	    } catch(CloneNotSupportedException cnse) {
	      	// Ne devrait jamais arriver car nous impl�mentons 
	      	// l'interface Cloneable
	      	LOG.error(cnse);
	    }
	    
	    // on renvoie le clone
	    return o;
	}

	/**
	 * @return Renvoie code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code code � d�finir.
	 */
	public void setCode(final String code) {
		this.code = code;
	}
	
	/**
	 * @return Renvoie version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version version � d�finir.
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	/**
	 * @return Renvoie libelle.
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle libelle � d�finir.
	 */
	public void setLibelle(final String libelle) {
		this.libelle = libelle;
	}

	/**
	 * @return Renvoie position.
	 */
	public Integer getPosition() {
		return position;
	}

	/**
	 * @param position position � d�finir.
	 */
	public void setPosition(final Integer position) {
		this.position = position;
	}


	/**
	 * @return Renvoie listeFils.
	 */
	public List getListeFils() {
		return listeFils;
	}

	/**
	 * @param listeFils listeFils � d�finir.
	 */
	public void setListeFils(final List listeFils) {
		this.listeFils = listeFils;
	}

	public String getTypeFils() {
		return typeFils;
	}

	public void setTypeFils(String typeFils) {
		this.typeFils = typeFils;
	}

	
	
	
	
}