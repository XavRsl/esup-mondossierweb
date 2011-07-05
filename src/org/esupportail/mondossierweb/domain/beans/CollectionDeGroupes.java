/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.List;

/**
 * représente une collection de groupes d'étudiants
 * @author chdubois
 *
 */
public class CollectionDeGroupes {

	/**
	 * le code de la collection
	 */
	private String codCollection;
	/**
	 * la liste des groupes de la collection
	 */
	private List<Groupe> listeGroupes;


	public CollectionDeGroupes() {
		super();
	}

	public CollectionDeGroupes(String CodCollection) {
		super();
		codCollection = CodCollection;
	}

	public String getCodCollection() {
		return codCollection;
	}

	public void setCodCollection(String CodCollection) {
		codCollection = CodCollection;
	}



	public List<Groupe> getListeGroupes() {
		return listeGroupes;
	}



	public void setListeGroupes(List<Groupe> listeGroupes) {
		this.listeGroupes = listeGroupes;
	}

	
	
	
}
