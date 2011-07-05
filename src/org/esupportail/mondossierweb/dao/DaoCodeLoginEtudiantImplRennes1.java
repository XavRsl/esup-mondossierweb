/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;

import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


/**
 * @author Charlie Dubois
 * Modif VR pour Rennes 1 070708
 * Modif GM pour Rennes 1 170910
 */
public class DaoCodeLoginEtudiantImplRennes1 extends SqlMapClientDaoSupport implements IDaoCodeLoginEtudiant {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(DaoCodeLoginEtudiantImplRennes1.class);
	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapService;
	/**
	 * constructeur vide.
	 */
	public DaoCodeLoginEtudiantImplRennes1() {
		super();
	}

	/** 
	 * @param login 
	 * @return le code étudiant
	 * @see org.esupportail.mondossierweb.dao.IDaoCodeLoginEtudiant#getCodEtuFromLogin(java.lang.String)
	 */
	public String getCodEtuFromLogin(final String login) {
		//avec requete SQL et ibatis:
		/*
		try {
			String cod = "";
			cod = (String) getSqlMapClientTemplate().queryForObject("Etudiant.getCodIndFromCodEtu", login);
			if (cod != null && !cod.equals("")){
				return cod;
			}
		} catch (Exception e) {
			LOG.error(e);
			//return View.ERROR;
		}
		LOG.error("probleme de récupération du cod_ind par Apogee (Etudiant.getCodIndFromCodEtu).");
		return View.ERROR;
		*/
		
		// A Rennes 1, le login est le code étudiant
		return login;
	}

	public LdapUserService getLdapService() {
		return ldapService;
	}

	public void setLdapService(LdapUserService ldapService) {
		this.ldapService = ldapService;
	}




}
