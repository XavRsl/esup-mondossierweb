/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.services.authentification;
/**
 * interface de Security. regroupe les méthodes indispensables à la 
 * reconnaissance de l'utilisateur.
 * @author Charlie Dubois
 *
 */
public interface ISecurity {

	/**
	 * donne le type de l’utilisateur dans l’application a partir de son
	 * identifiant.
	 * @param id
	 * @return le type de l’utilisateur
	 */
	String getTypeUser(final String id);
	
	/**
	 * @param nom_appli 
	 * @param fonction 
	 * @param user_id 
	 * @param code_ctrl 
	 * @param type_ctrl 
	 * @return habilitation
	 */
	Boolean habilitationApogee(final String nom_appli, final String fonction, final String user_id, final String code_ctrl, final String type_ctrl);

	/**
	 * donne La propriete ldap du contact désignant son codetu.
	 * @return La propriete ldap du contact désignant son codetu
	 */
	String getAttributLdapCodEtu();
	
	/**
	 * donne le type ldap de l’utilisateur a partir de son identifiant.
	 * @param login
	 * @return le type ldap de l’utilisateur
	 */
	String typeLdap(final String login);
}
