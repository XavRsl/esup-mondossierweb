/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.converters;

import org.esupportail.mondossierweb.domain.beans.Config;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * la classe qui permet d'obtenir l'e-mail d'un étudiant connaissant son login.
 * @author Charlie Dubois
 */
public class EmailConverterUnivLorraineImpl extends SqlMapClientDaoSupport implements EmailConverterInterface{
	/**
	 * la bean qui contient la String de complétion de l'e-mail.
	 */
	private Config config;
	
	
	/**
	 * le constructeur.
	 */
	public EmailConverterUnivLorraineImpl() {
		super();
	}
	
	/**
	 * @param login
	 * @return l'adresse mail.
	 */
	public String getMail(final String login) {
		//Gestion du cas ou le login est null ou vide
		if (login != null && !login.equals("") && config.getExtMail() != null && !config.getExtMail().equals("")) {
			String mail="";
			//aller chercher le mail dans annu_mel_login
			mail =  (String) getSqlMapClientTemplate().queryForObject("Mellogin.getMail", login);
			if(mail == null || mail.equals("null") || mail.equals("error"))
				mail = "";
			return mail;
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
