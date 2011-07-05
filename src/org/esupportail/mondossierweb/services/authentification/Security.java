/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.services.authentification;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;


import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.web.controllers.SessionController;
import org.esupportail.portal.ws.client.PortalGroup;
import org.esupportail.portal.ws.client.PortalService;
import org.esupportail.portal.ws.client.PortalUser;
/**
 * fournies les méthodes nécessaires à la bonnes reconnaissance de l'utilisateur.
 * @author Charlie Dubois
 *
 */
public class Security implements ISecurity {
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(Security.class);
	/**
	 * Les groupes uportal autorisés.
	 */
	private List<String> listeGroupesPortal;
	/**
	 * Utilisation de la table Utilisateur d'Apogee pour l'acces au canal
	 */
	private boolean loginApogee;
	/**
	 * Le service.
	 */
	private IDaoService service;
	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapService;
	/**
	 * portalService.
	 */
	private PortalService portalService;
	/**
	 * le type Ldap etudiant.
	 */
	private String typeEtudiantLdap;
	/**
	 * le nom de l'attribut du contact LDAP a comparer avec typeEtudiantLdap pour savoir si l'utilisateur est étudiant.
	 */
	private String attributLdapEtudiant;
	/**
	 * La propriete ldap du contact désignant son codind
	 */
	private String attributLdapCodEtu;

	/**
	 * le constructeur.
	 *
	 */
	public Security() {
		super();
	}



	/**
	 * @see org.esupportail.mondossierweb.services.authentification.ISecurity#getTypeUser(java.lang.String)
	 */
	public String getTypeUser(final String id) {
		String typeuser = "";
		String type = typeLdap(id);

		if(type.equals("errorLdap")){
			typeuser = SessionController.ERROR_LDAP;
		}else{

			if (type.equalsIgnoreCase(getTypeEtudiantLdap())) { 
				typeuser = SessionController.STUDENT_USER;
			} else {

				//on cherche a savoir si l'employé a acces (ex: c'est un enseignant)
				//si il est autorisé type=enseignant, sinon type=non-autorise

				boolean useruportal = false;
				try {
					//on reucupère la liste de groupes mis dans le bean security
					List<String> listegroupes = getListeGroupesPortal();

					//on test si on est en portlet
					if (listegroupes != null && listegroupes.size()>0) {
						
					//recupère l'utilisateur uportal
					PortalUser portaluser = portalService.getUser(id);

					//on cherche si il appartient a un groupe
					useruportal = false;

					

					//on regarde si il appartient a un des groupes
					for (String nomgroupe : listegroupes) {
						//si on est pas déjà sur qu'il appartient a un groupe:
						if(!useruportal) {
							//on cherche le groupe
							PortalGroup pgroup = portalService.getGroupByName(nomgroupe);
							if (pgroup != null) {
								//on regarde si l'utilisateur appartient a ce groupe
								if (portalService.isUserMemberOfGroup(portaluser, pgroup)) {
									//c'est un utilisateur uportal
									useruportal = true;
								}
							} 
						}
					}
					}
				} catch (Exception e) {
					//Test présence dans la table utilisateur de Apogee
					LOG.error("PROBLEME DE CONNEXION AUX GROUPES UPORTAL");
				}

				if (useruportal) {
					//c'est un utilisateur uportal il est donc autorisé en tant qu'enseignant
					typeuser = SessionController.TEACHER_USER;
					
				} else {
					
					//On test si on doit chercher l'utilisateur dans Apogee
					if(loginApogee){
						//Test de la présence dans la table utilisateur d'Apogee
						//on regarde si il est dans la table utilisateur 
						try {
							String coduti = service.getCodUti(id);
							
							if (coduti != null  && !coduti.equals("") && !coduti.equals("-")) {
								typeuser = SessionController.TEACHER_USER;
							} else {
								if (coduti != null && coduti.equals("-")) {
									typeuser = SessionController.ERROR_BDD;
									LOG.error("probleme lors de la vérification de l'existence de l'utilisateur"+id+" dans la table Utilisateur de Apogee");
								}else{
									typeuser = SessionController.UNAUTHORIZED_USER;
									LOG.info("utilisateur "+id+" n' est pas dans le LDAP en tant qu' etudiant, n'appartient à aucun groupe uportal, et n'est pas dans la table utilisateur d'APOGEE -> UTILISATEUR NON AUTORISE !");
								}
							}
						} catch (Exception ex) {
							LOG.error(ex);
							LOG.error("probleme lors de la vérification de l'existence de l'utilisateur dans la table Utilisateur de Apogee");
						}
					}else{
						typeuser = SessionController.UNAUTHORIZED_USER;
						LOG.info("utilisateur "+id+" n' est pas dans le LDAP en tant qu' etudiant, n'appartient à aucun groupe uportal -> UTILISATEUR NON AUTORISE !");
					}
				}
			}
		}
		return typeuser;
	}

	/**
	 * 
	 * @param login de l'utilisateur
	 * @return le type retourné par ldap.
	 */
	public String typeLdap(final String login) {
		try {
			LdapUser ldapuser = ldapService.getLdapUser(login);
			Map mattributs = ldapuser.getAttributes();
			//List<String> ltype = (List<String>) mattributs.get("edupersonprimaryaffiliation");
			List<String> ltype = (List<String>) mattributs.get(getAttributLdapEtudiant());
			return ltype.get(0);
		} catch (Exception e) {
			LOG.error(e);
			LOG.error("probleme a la recuperation de l utilisateur : "+login+" dans le LDAP");
			return "errorLdap";
		}
	}

	/**
	 * @return typeEtudiantLdap
	 */
	public String getTypeEtudiantLdap() {
		return typeEtudiantLdap;
	}


	/**
	 * @param typeEtudiantLdap
	 */
	public void setTypeEtudiantLdap(final String typeEtudiantLdap) {
		this.typeEtudiantLdap = typeEtudiantLdap;
	}


	/**
	 * @return listeGroupesPortal
	 */
	public List<String> getListeGroupesPortal() {
		return listeGroupesPortal;
	}


	/**
	 * @param listeGroupesPortal
	 */
	public void setListeGroupesPortal(final List<String> listeGroupesPortal) {
		this.listeGroupesPortal = listeGroupesPortal;
	}



	public LdapUserService getLdapService() {
		return ldapService;
	}



	public void setLdapService(LdapUserService ldapService) {
		this.ldapService = ldapService;
	}



	/**
	 * @return portalService
	 */
	public PortalService getPortalService() {
		return portalService;
	}

	/**
	 * @param portalService
	 */
	public void setPortalService(final PortalService portalService) {
		this.portalService = portalService;
	}

	/**
	 * @return service
	 */
	public IDaoService getService() {
		return service;
	}

	/**
	 * @param service
	 */
	public void setService(final IDaoService service) {
		this.service = service;
	}


	/**
	 * @return attributLdapEtudiant
	 */
	public String getAttributLdapEtudiant() {
		return attributLdapEtudiant;
	}


	/**
	 * @param attributLdapEtudiant
	 */
	public void setAttributLdapEtudiant(String attributLdapEtudiant) {
		this.attributLdapEtudiant = attributLdapEtudiant;
	}


	/**
	 * @return attributLdapCodInd
	 */
	public String getAttributLdapCodEtu() {
		return attributLdapCodEtu;
	}


	/**
	 * @param attributLdapCodInd
	 */
	public void setAttributLdapCodEtu(String attributLdapCodEtu) {
		this.attributLdapCodEtu = attributLdapCodEtu;
	}


	/**
	 * @param nom_appli 
	 * @param fonction 
	 * @param user_id 
	 * @param code_strl 
	 * @param type_ctrl 
	 * @return true/false
	 */
	public Boolean habilitationApogee(String nom_appli, String fonction, String user_id, String code_strl, String type_ctrl) {
		// TODO Auto-generated method stub
		return true;
	}


	/**
	 * 
	 * @return loginApogee
	 */
	public boolean isLoginApogee() {
		return loginApogee;
	}


	/**
	 * 
	 * @param loginApogee
	 */
	public void setLoginApogee(boolean loginApogee) {
		this.loginApogee = loginApogee;
	}





}
