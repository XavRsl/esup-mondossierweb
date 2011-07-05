/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;


import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.web.navigation.View;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


/**
 * Classe qui sait comment récupérer le Login Etudiant depuis le cod_etu
 * @author Charlie Dubois
 *
 */
public class DaoLoginCodeEtudiantImplBasic extends SqlMapClientDaoSupport implements IDaoLoginCodeEtudiant {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(DaoLoginCodeEtudiantImplBasic.class);


	/**
	 * constructeur vide.
	 */
	public DaoLoginCodeEtudiantImplBasic() {
		super();

	}
	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoLoginCodeEtudiant#getLoginFromCodInd(java.lang.String)
	 */
	public String getLoginFromCodEtu(final String codetu) {
		//via ibatis et une requete SQL:
		try {
			
			String login =  (String) getSqlMapClientTemplate().queryForObject("Mellogin.getLogin", codetu);

			if(login != null && !login.equals("") && !login.equals("error")){
				return login;
			}
			
		} catch (Exception e) {
			LOG.error(e);
		}
		
		LOG.error("Erreur lors de la récupération du login a partir du codetu.");
		
		return View.ERROR;

	}

}

