/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;


/**
 * interface listant les m�thodes devant �tre impl�ment�es pour la classe qui r�cup�rera le 
 * login Etudiant depuis le cod_etu
 * @author Charlie Dubois
 *
 */
public interface IDaoLoginCodeEtudiant {

	/**
	 * @param codetu
	 * @return le login d'un �tudiant � partir de son codetu
	 */
	String getLoginFromCodEtu(String codetu);
}
