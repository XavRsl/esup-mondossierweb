/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;


/**
 * interface listant les méthodes devant être implémentées pour la classe qui récupérera le 
 * login Etudiant depuis le cod_etu
 * @author Charlie Dubois
 *
 */
public interface IDaoLoginCodeEtudiant {

	/**
	 * @param codetu
	 * @return le login d'un étudiant à partir de son codetu
	 */
	String getLoginFromCodEtu(String codetu);
}
