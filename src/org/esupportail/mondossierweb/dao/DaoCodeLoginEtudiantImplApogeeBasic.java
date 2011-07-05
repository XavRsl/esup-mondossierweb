/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;


import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.web.navigation.View;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


/**
 * Classe qui sait récupérer le cod_etu depuis le login Etudiant.
 * @author Charlie Dubois
 *
 */
public class DaoCodeLoginEtudiantImplApogeeBasic extends SqlMapClientDaoSupport implements IDaoCodeLoginEtudiant {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(DaoCodeLoginEtudiantImplApogeeBasic.class);
	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapService;
	/**
	 * constructeur vide.
	 */
	public DaoCodeLoginEtudiantImplApogeeBasic() {
		super();
	}

	/** 
	 * @see org.esupportail.mondossierweb.dao.IDaoCodeLoginEtudiant#getCodIndFromLogin(java.lang.String)
	 */
	public String getCodEtuFromLogin(final String login) {

		try{
			String cod = "";

			//avec requete SQL et ibatis:
			cod = (String) getSqlMapClientTemplate().queryForObject("Mellogin.getCodEtu", login);
			if (cod != null && !cod.equals("")){
				return cod;
			}
		} catch (Exception e) {
			LOG.error(e);
		}
		
		LOG.error("probleme de récupération du cod_etu via Apogee et une requete SQL.");

		return View.ERROR;
	}

	public LdapUserService getLdapService() {
		return ldapService;
	}

	public void setLdapService(LdapUserService ldapService) {
		this.ldapService = ldapService;
	}




}
