/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;

/**
 * interface listant les m�thodes devant �tre impl�ment�es par la classe 
 * qui r�cup�rera le cod_etu depuis le login Etudiant.
 * @author Charlie Dubois
 *
 */
public interface IDaoCodeLoginEtudiant {

	/**
	 * @param login
	 * @return le codind d'un �tudiant � partir de son login
	 */
	String getCodEtuFromLogin(String login);
	
	
}
