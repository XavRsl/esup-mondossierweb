/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.converters;

import org.esupportail.mondossierweb.domain.beans.Config;

/**
 * la classe qui permet d'obtenir l'e-mail d'un étudiant connaissant son login.
 * @author Charlie Dubois
 */
public class EmailConverter {
	/**
	 * la bean qui contient la String de complétion de l'e-mail.
	 */
	private Config config;
	
	
	/**
	 * le constructeur.
	 */
	public EmailConverter() {
		super();
	}
	
	/**
	 * @param login
	 * @return l'adresse mail.
	 */
	public String getMail(final String login) {
		if (config.getExtMail() != null && !config.getExtMail().equals("")) {
			return login + config.getExtMail();
		}
		return "";	
	}
	
	
	/**
	 * @return config
	 */
	public Config getConfig() {
		return config;
	}
	
	/**
	 * @param config
	 */
	public void setConfig(final Config config) {
		this.config = config;
	}
}
