/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;


import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


/**
 * Classe qui ne recupere pas le login
 * @author Charlie Dubois
 *
 */
public class DaoLoginCodeEtudiantImplEmpty extends SqlMapClientDaoSupport implements IDaoLoginCodeEtudiant {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(DaoLoginCodeEtudiantImplEmpty.class);


	/**
	 * constructeur vide.
	 */
	public DaoLoginCodeEtudiantImplEmpty() {
		super();

	}
	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoLoginCodeEtudiant#getLoginFromCodInd(java.lang.String)
	 */
	public String getLoginFromCodEtu(final String codetu) {
		
		return "";

	}

}

