/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;

import gouv.education.apogee.commun.client.ws.etudiantmetier.EtudiantMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.transverse.dto.etudiant.IdentifiantsEtudiantDTO;


import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.web.navigation.View;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * @author Charlie Dubois
 *
 */
public class DaoLoginCodeEtudiantImplRennes1 extends SqlMapClientDaoSupport implements IDaoLoginCodeEtudiant {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(DaoCodeLoginEtudiantImplRennes1.class);
	/**
	 * constructeur vide.
	 */
	public DaoLoginCodeEtudiantImplRennes1() {
		super();

	}
	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoLoginCodeEtudiant#getLoginFromCodInd(java.lang.String)
	 */
	public String getLoginFromCodEtu(final String codind) {
		//via ibatis et une requete SQL:
		try {
			String cod = "";
			cod = (String) getSqlMapClientTemplate().queryForObject("Etudiant.getCodEtuFromCodInd", codind);
			if (cod != null && !cod.equals("")){
				return cod;
			}
		} catch (Exception e) {
			LOG.error(e);
			//return View.ERROR;
		}
		LOG.error("probleme de récupération du cod_etu par Apogee (Etudiant.getCodEtuFromCodInd).");
		return View.ERROR;

	}

}
