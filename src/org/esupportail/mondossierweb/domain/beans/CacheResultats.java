/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.util.LinkedList;
import java.util.List;

/**
 * repr�sente le cache pour stocker les r�sultats d�j� r�cup�r�s pour l'�tudiant dont on consulte le dossier.
 * @author Charlie Dubois
 */
public class CacheResultats {

	/**
	 * liste des r�sultats possible pour la page 'Notes' d�j� r�cup�r�s (2 possibles : vue Enseignant/vueEtudiant)
	 */
	private List<CacheResultatsVdiVet>  ResultVdiVet;
	/**
	 * liste des r�sultats possible pour la page 'D�tailsDesNotes' d�j� r�cup�r�s.Fonction de la vue et de l'�tape observ�e.
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
