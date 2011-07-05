/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;

import gouv.education.apogee.commun.client.ws.etudiantmetier.EtudiantMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.servicesmetiers.EtudiantMetierServiceInterface;
import gouv.education.apogee.commun.transverse.dto.etudiant.IdentifiantsEtudiantDTO;

import java.util.List;
import java.util.Map;


import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.mondossierweb.services.authentification.ISecurity;
import org.esupportail.mondossierweb.web.navigation.View;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


/**
 * Classe qui sait récupérer le cod_etu depuis le login Etudiant.
 * @author Charlie Dubois
 *
 */
public class DaoCodeLoginEtudiantImplNancy2 extends SqlMapClientDaoSupport implements IDaoCodeLoginEtudiant {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(DaoCodeLoginEtudiantImplNancy2.class);
	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapService;
	/**
	 * proxy pour faire appel aux infos sur l'étudiant WS .
	 */
	protected EtudiantMetierServiceInterface monProxyEtu;
	
	/**
	 * constructeur vide.
	 */
	public DaoCodeLoginEtudiantImplNancy2() {
		super();
		try {
			monProxyEtu = new EtudiantMetierServiceInterfaceProxy();
		} catch(Exception e) {
			LOG.error(e);
			LOG.error("Probleme avec le WS dans DaoCodeLoginEtudiantImplNancy2: connexion impossible, création des proxys échouée.");
		}

	}

	/** 
	 * @see org.esupportail.mondossierweb.dao.IDaoCodeLoginEtudiant#getCodIndFromLogin(java.lang.String)
	 */
	public String getCodEtuFromLogin(final String login) {

		//avec LDAP :
		try {
			LdapUser ldapuser = ldapService.getLdapUser(login);
			Map mattributs = ldapuser.getAttributes();
			ISecurity security = (ISecurity)  BeanUtils.getBean("security");
			//A nancy2, on a le codind et pas le codetu dans le ldap :
			String attrCodInd = security.getAttributLdapCodEtu();
			if (attrCodInd != null && !attrCodInd.equals("")){
				
				List<String> llogin = (List<String>) mattributs.get(attrCodInd);
				
				if (llogin != null && llogin.get(0) != null ) {
					
					String codind =  llogin.get(0);
					
					//on va cherche le codetu a partir du codind grace aux WS:

					IdentifiantsEtudiantDTO idetu = monProxyEtu.recupererIdentifiantsEtudiant("",codind, "", "", "", "", "", "", "","N");
					
					//LOG.info("Implnancy2 retourne codetu : "+idetu.getCodEtu().toString()+" pour login : "+login);
					
					return idetu.getCodEtu().toString();
					
				}
			}
		} catch (Exception e) {
			LOG.error(e);

		}
		LOG.error("probleme de récupération du cod_etu depuis le login via le ldap. \n->Essai via Apogee est une requete SQL.");

		String cod = "";

		//Si ça n'a pas marché : avec requete SQL et ibatis:
		cod = (String) getSqlMapClientTemplate().queryForObject("Mellogin.getCodEtu", login);
		if (cod != null && !cod.equals("")){
			return cod;
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
