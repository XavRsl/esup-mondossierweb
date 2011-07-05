/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.services.authentification;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.univ.rennes1.cri.apogee.services.remote.ReadRennes1;
/**
 * fournies les méthodes nécessaires à la bonnes reconnaissance de l'utilisateur.
 * @author Charlie Dubois
 *
 */
public class SecurityHabilitationApogee extends Security implements ISecurity {
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(SecurityHabilitationApogee.class);
	/**
	 * le constructeur.
	 *
	 */
	/**
	 * Appel aux beans remote : infos R1 du WS apo-cri-web.
	 */
	private static final String REMOTE_CONFIG_FILE = "/properties/specific/specific.xml";
	private ReadRennes1 monApoCriR1;

	public SecurityHabilitationApogee() {
		super();
		ClassPathResource res = new ClassPathResource(REMOTE_CONFIG_FILE);
		BeanFactory beanFactory = new XmlBeanFactory(res);
		monApoCriR1 = (ReadRennes1) beanFactory.getBean("remoteCriApogeeRennes1");
	}



	/**
	 * 
	 * @param nom_appli : "monDosWeb"
	 * @param fonction : 
	 * @param user_id : login de l'utilisateur
	 * @param code_ctrl : code controle (diplome, etp, code_etu
	 * @param type_ctrl : type de controle ("dip", "etp", "etu")
	 * @return true / false
	 */
	public Boolean habilitationApogee(String nom_appli, String fonction, String user_id, String code_ctrl, String type_ctrl) {
		try {
			// 1. getUtilisateur
			//	==> type_utilisateur
			//	==> list composantes
			//	==> code_cge
			// 2. getEtape
			//	==> getEtpGererCge ==> set etpGererCge
			//		==> code_cge
			//		==> centre gestion
			//		==> composante
			// 3. Comp. 1 et 2 => au moins une correspondance
			//	
			Boolean ret = false;
			ret = monApoCriR1.controlDroitProfilAppliApo(nom_appli, fonction, user_id.toUpperCase(), code_ctrl, type_ctrl);
			return ret;
		} catch (Exception e) {
			LOG.error(e);
			LOG.error("probleme a la recuperation de l habilitation utilisateur : " + user_id + " pour fonction " + fonction + " et code dip/etp/etu " + code_ctrl + " dans le WS");
			return false;
		}
	}





}
