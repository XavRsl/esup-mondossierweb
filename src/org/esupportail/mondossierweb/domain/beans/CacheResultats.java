/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.LinkedList;
import java.util.List;

/**
 * représente le cache pour stocker les résultats déjà récupérés pour l'étudiant dont on consulte le dossier.
 * @author Charlie Dubois
 */
public class CacheResultats {

	/**
	 * liste des résultats possible pour la page 'Notes' déjà récupérés (2 possibles : vue Enseignant/vueEtudiant)
	 */
	private List<CacheResultatsVdiVet>  ResultVdiVet;
	/**
	 * liste des résultats possible pour la page 'DétailsDesNotes' déjà récupérés.Fonction de la vue et de l'étape observée.
	 */
	private List<CacheResultatsElpEpr> ResultElpEpr;
	
	public CacheResultats(){
		super();
		ResultVdiVet = new LinkedList<CacheResultatsVdiVet>();
		ResultElpEpr = new LinkedList<CacheResultatsElpEpr>();
	}

	public List<CacheResultatsVdiVet> getResultVdiVet() {
		return ResultVdiVet;
	}

	public void setResultVdiVet(List<CacheResultatsVdiVet> resultVdiVet) {
		ResultVdiVet = resultVdiVet;
	}

	public List<CacheResultatsElpEpr> getResultElpEpr() {
		return ResultElpEpr;
	}

	public void setResultElpEpr(List<CacheResultatsElpEpr> resultElpEpr) {
		ResultElpEpr = resultElpEpr;
	}
	
	
	
}
