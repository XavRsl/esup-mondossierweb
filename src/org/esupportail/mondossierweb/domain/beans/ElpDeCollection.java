/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.List;

/**
 * représente un ELP contenant des collections de groupes
 * @author chdubois
 *
 */
public class ElpDeCollection {
	/**
	 * le codeELP
	 */
	private String codElp;
	/**
	 * le libellé de l'ELP
	 */
	private String libElp;
	/**
	 * la liste de collection de l'ELP
	 */
	private List<CollectionDeGroupes> listeCollection;
	
	
	public ElpDeCollection() {
		super();
	}


	public ElpDeCollection(String CodElp, String LibElp) {
		super();
		codElp = CodElp;
		libElp = LibElp;
	}


	public String getCodElp() {
		return codElp;
	}


	public void setCodElp(String CodElp) {
		codElp = CodElp;
	}


	public String getLibElp() {
		return libElp;
	}


	public void setLibElp(String LibElp) {
		libElp = LibElp;
	}


	public List<CollectionDeGroupes> getListeCollection() {
		return listeCollection;
	}


	public void setListeCollection(List<CollectionDeGroupes> listeCollection) {
		this.listeCollection = listeCollection;
	}
	
	
	
	
	
	
}
