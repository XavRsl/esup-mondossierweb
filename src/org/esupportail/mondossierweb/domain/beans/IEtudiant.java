/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;



/**
 * m�thodes indispensables devant �tre fournies par le bean etudiantManager.
 * @author Charlie Dubois
 *
 */
public interface IEtudiant {

	/**
	 * renseigne le cod_ind de l'�tudiant.
	 * @param e
	 */
	void setCodInd(Etudiant e);
	/**
	 * renseigne les attributs concernant l'�tat-civil.
	 * @param e
	 */
	void setEtatCivil(Etudiant e);
	/**
	 * renseigne l'url de la photo.
	 * @param e
	 */
	void setPhoto(Etudiant e);
	/**
	 * renseigne les attributs concernant les adresses.
	 * @param e
	 */
	void setAdresses(Etudiant e);
	/**
	 * @param e
	 * @return vrai si la transcation s'est bien pass�e
	 */
	boolean setMajAdresses(Etudiant e);
	/**
	 * renseigne les attributs concernant les inscriptions.
	 * @param e
	 */
	void setInscriptions(Etudiant e);
	/**
	 * renseigne les attributs concernant l'inscription p�dagogique d'une �tape choisie
	 * @param e
	 * @param et
	 */
	void setInscriptionPedagogique(Etudiant e, Etape et);
	/**
	 * renseigne les attributs concernant le calendrier des examens.
	 * @param e
	 */
	void setCalendrier(Etudiant e);
	/**
	 * renseigne les attributs concernant les notes et r�sultats obtenus.
	 * @param e
	 */
	void setNotesEtResultats(Etudiant e);
	/**
	 * renseigne les attributs concernant les notes et r�sultats obtenus 
	 * avec la vue enseignant.
	 * @param e
	 */
	void setNotesEtResultatsEnseignant(Etudiant e);
	/**
	 * renseigne les attributs concernant les r�sultats obtenus 
	 * aux �l�ments p�dagogiques d'une �tape choisie.
	 * @param e
	 * @param et
	 */
	void setNotesElpEpr(Etudiant e, Etape et);
	/**
	 * renseigne les attributs concernant les r�sultats obtenus 
	 * aux �l�ments p�dagogiques d'une �tape choisie avec la vue enseignant.
	 * @param e
	 * @param et
	 */
	void setNotesElpEprEnseignant(Etudiant e, Etape et);
	
	/**
	 * renseigne les attributs concernant le calendrier de rentr�e 
	 * @param etudiant
	 */
	void setCalendrierRentree(Etudiant etudiant);
	

	

}
