/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;


import java.util.List;
import java.util.Map;



import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.services.authentification.ISecurity;
import org.esupportail.mondossierweb.web.navigation.View;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


/**
 * Classe qui sait récupérer le cod_etu depuis le login Etudiant.
 * @author Charlie Dubois
 *
 */
public class DaoCodeLoginEtudiantImplLdapBasic extends SqlMapClientDaoSupport implements IDaoCodeLoginEtudiant {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(DaoCodeLoginEtudiantImplLdapBasic.class);
	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapService;
	/**
	 * Bean security
	 */
	private ISecurity security;
	/**
	 * constructeur vide.
	 */
	public DaoCodeLoginEtudiantImplLdapBasic() {
		super();
	}

	/** 
	 * @see org.esupportail.mondossierweb.dao.IDaoCodeLoginEtudiant#getCodIndFromLogin(java.lang.String)
	 */
	public String getCodEtuFromLogin(final String login) {

		//avec LDAP :
		try {
			LdapUser ldapuser = ldapService.getLdapUser(login);
			Map mattributs = ldapuser.getAttributes();
			
			LOG.info("Recuperation CODETU DEPUIS LOGIN "+ login+" - "+ldapuser.getId()+"-"+ldapuser.getAttributeNames());
			
			//ISecurity security = (ISecurity)  BeanUtils.getBean("security");
			
			String attrCodEtu = security.getAttributLdapCodEtu();
			LOG.info(attrCodEtu);
			if (attrCodEtu != null && !attrCodEtu.equals("")){
				List<String> lcodetu = (List<String>) mattributs.get(attrCodEtu);
				LOG.info("-"+lcodetu);
				LOG.info("=="+lcodetu.get(0));
				if (lcodetu != null && lcodetu.get(0) != null ) {
					LOG.info("=>"+lcodetu.get(0));
					return lcodetu.get(0);
				}
			}
		} catch (Exception e) {
			LOG.error(e);
			
		}
		LOG.error("probleme de récupération du cod_etu depuis le login via le ldap. ");

		return View.ERROR;
	}

	public LdapUserService getLdapService() {
		return ldapService;
	}

	public void setLdapService(LdapUserService ldapService) {
		this.ldapService = ldapService;
	}

	public ISecurity getSecurity() {
		return security;
	}

	public void setSecurity(ISecurity security) {
		this.security = security;
	}




}
