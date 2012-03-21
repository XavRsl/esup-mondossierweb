/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.photo;


/**
 * interface de la classe photo. 
 * Liste les m�thodes indispensable � la r�cup�ration des photos.
 * @author Charlie Dubois
 */
public interface IPhoto {

	/**
	 * Retourne l'url pour la photo de l'individu dont le cod_ind est plac� en param�tre.
	 * @param cod_ind
	 * @param cod_etu
	 * @return l'url pour r�cup�rer la photo.
	 * 
	 */
	String getUrlPhoto(String cod_ind, String cod_etu);
	
	/**
	 * retourne l'url pour la photo de l'individu dont le cod_ind est plac� en param�tre.
	 * Avec une url pour le serveur et non pour l'affichage � l'�cran, pour le client.
	 * @param cod_ind
	 * @param cod_etu
	 * @return l'url pour r�cup�rer la photo
	 */
	String getUrlPhotoTrombinoscopePdf(String cod_ind, String cod_etu);

	
}
