/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.portlet.PortletUtil;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.authentication.DelegatingAuthenticationService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.controllers.ExceptionController;

import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.domain.beans.Config;
import org.esupportail.mondossierweb.domain.beans.Etudiant;
import org.esupportail.mondossierweb.domain.beans.User;
import org.esupportail.mondossierweb.services.authentification.Security;
import org.esupportail.mondossierweb.web.navigation.View;

import edu.yale.its.tp.cas.client.filter.CASFilter;






/**
 * bean pour mémoriser le contexte de l'application.
 * @author Charlie Dubois
 */
public class SessionController extends AbstractDomainAwareBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5538104870330464173L;
	/**
	 * type utilisateur étudiant.
	 */
	public static final String STUDENT_USER = "student";
	/**
	 * type correspondant à un utilisateur dont le login doit être exclu de l'application.
	 */
	public static final String LOGIN_EXCLU = "exclu";

	/**
	 * type utilisateur enseignant.
	 */
	public static final String TEACHER_USER = "teacher";

	/**
	 * type utilisateur non-autorisé.
	 */
	public static final String UNAUTHORIZED_USER = "unauthorized";

	/**
	 * erreur de connexion a la bdd
	 */
	public static final String ERROR_BDD = "errorBDD";
	/**
	 * erreur de connexion au ldap
	 */
	public static final String ERROR_LDAP = "errorLDAP";
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(SessionController.class);

	/**
	 * The name of the parameter that gives the logout URL.
	 */
	private static final String LOGOUT_URL_PARAM = "edu.yale.its.tp.cas.client.logoutUrl";

	/**
	 * The name of the request attribute that holds the current user.
	 */
	private static final String CURRENT_USER_ATTRIBUTE = SessionController.class.getName() + ".currentUser";

	/**
	 * The exception controller (called when logging in/out).
	 */
	private ExceptionController exceptionController;

	/**
	 * The authentication service.
	 */
	private DelegatingAuthenticationService authenticationService;
	/**
	 * Le service.
	 */
	private IDaoService service;

	/**
	 * Le bean qui gere l'acces a l'application.
	 */
	private Security security;

	/**
	 * le login de l'utilisateur.
	 */
	private String iduser;

	/**
	 * Le type de l'utilisateur.
	 */
	private String typeuser;

	/**
	 * l'etudiant dont on consulte le dossier.
	 */
	private Etudiant etudiant;
	/**
	 * la configuration de l'application.
	 */
	private Config config;
	/**
	 * le controller qui s'occupe des statistiques.
	 */
	private StatisticController statisticController;

	/**
	 * Constructor.
	 */
	public SessionController() {
		super();

	}


	/**
	 * @see org.esupportail.mondossierweb.web.controllers.AbstractDomainAwareBean#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		Assert.notNull(this.exceptionController, "property exceptionController of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.authenticationService, "property authenticationService of class "
				+ this.getClass().getName() + " can not be null");
	}

	/**
	 * @return the current user, or null if guest.
	 */
	@Override
	public User getCurrentUser() {
		
		if (ContextUtils.getRequestAttribute(CURRENT_USER_ATTRIBUTE) == null) {
			String currentUserId = "";
			if( authenticationService.getAuthInfo() != null){
				currentUserId = authenticationService.getAuthInfo().getId();
			}
		
			if (currentUserId == null || currentUserId.equals("")) {
				return null;
			}
			User user = getDomainService().getUser(currentUserId);
			ContextUtils.setRequestAttribute(CURRENT_USER_ATTRIBUTE, user);
		}
		return (User) ContextUtils.getRequestAttribute(CURRENT_USER_ATTRIBUTE);
	}

	/**
	 * @return true if running as a portlet.
	 */
	public static boolean isPortlet() {
		return PortletUtil.isPortletRequest(FacesContext.getCurrentInstance());
	}

	/**
	 * @return true if running as a servlet.
	 */
	public boolean isServlet() {
		return !isPortlet();
	}

	/**
	 * @return true if the login button should be printed.
	 */
	public boolean isPrintLogin() {
		return isServlet() && getCurrentUser() == null;
	}

	/**
	 * @return true if the logout button should be printed.
	 */
	public boolean isPrintLogout() {
		return isServlet() && getCurrentUser() != null;
	}

	/**
	 * @return a debug String.
	 */
	public String getDebug() {
		return toString();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SessionController:   -iduser=" + iduser+ " -typeuser="+typeuser;
	}

	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(final DelegatingAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 * @throws IOException
	 */
	public String logout() throws IOException {
		if (isPortlet()) {
			throw new UnsupportedOperationException("logout() should not be called in portlet mode.");
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String logoutUrl = externalContext.getInitParameter(LOGOUT_URL_PARAM);
		if (logoutUrl == null) {
			throw new ConfigException("context parameter '" + LOGOUT_URL_PARAM + "' not found");
		}
		String returnUrl = request.getRequestURL().toString().replaceFirst("/styleshe.*", "/stylesheets/login.faces");

		String forwardUrl = String.format(logoutUrl, StringUtils.utf8UrlEncode(returnUrl));
		// note: the session beans will be kept event when invalidating
		// the session so they have to be reset (by the exception controller).
		// We invalidate the session however for the other attributes.
		request.getSession().invalidate();
		request.getSession(true);
		// calling this method will reset all the beans of the application
		exceptionController.restart();
		externalContext.redirect(forwardUrl);
		facesContext.responseComplete();
		return null;
		//return "navigationLogin";
	}

	/**
	 * @param exceptionController the exceptionController to set
	 */
	public void setExceptionController(final ExceptionController exceptionController) {
		this.exceptionController = exceptionController;
	}

	/**
	 * @return la page où on va allé après avoir voulu accéder à une page de ens/
	 */
	public String isEns() {
		if	(!config.isActivation()) {
			return View.MAINTENANCE;
		}
		if (typeuser.equals(TEACHER_USER)) {
			return null;
			//return View.ERROR_CONNEXION;
		} else if (typeuser.equals(STUDENT_USER)) {
			return View.ERROR;
		} else {
			return View.ERROR_CONNEXION;
		}
	}

	/**
	 * @return la page où on va allé après avoir voulu accéder à une page de etu/
	 */
	public String isLoggue() {
		if	(!config.isActivation()) {
			return View.MAINTENANCE;
		}
		if (typeuser.equals(TEACHER_USER) || typeuser.equals(STUDENT_USER) ) {
			return null;
		}
		return View.ERROR_CONNEXION;

	}


	/**
	 * Détermine si l'utilisateur est un enseignant.
	 * @return vrai si l'utilisateur est un enseignant
	 */
	public boolean isEnseignant() {
		boolean b = false;

		if (typeuser != null && typeuser.equals(TEACHER_USER) ) {
			b = true;
		}
		return b;
	}

	/**
	 * Détermine le type de l'utilisateur.
	 */
	public void typeUser() {

		typeuser = "";
		etudiant.reset();
		User user = getCurrentUser();
		if (user == null) {
			//utitisateur non loggué	
			typeuser = "nonloggue";
		} else {
			//on test si l'utilisateur fait parti des login à exclure
			boolean exclure=false;
			if(config.getListeLoginExclus() != null && config.getListeLoginExclus().size()>0){
				for(String login : config.getListeLoginExclus()){
					if (user.getId() != null && login!=null && user.getId().equals(login)){
						//on exclu l'utilisateur
						exclure = true;
					}
				}
			}

			if(!exclure){
				//on test si l'utilisateur est la personne a substituer à un étudiant:
				if (user.getId() != null && user.getId().equals(config.getIdLoginTest()) && config.getIdEtudiantTest() != null && !config.getIdEtudiantTest().equals("")) {
					setIduser(config.getIdEtudiantTest());
				} else {
					setIduser(user.getId());
				}
				typeuser = security.getTypeUser(getIduser());

				if (typeuser.equals(STUDENT_USER)) { 
					etudiant.setCod_etu(service.getCodEtuFromLogin(getIduser()));
					if(etudiant.getCod_etu() == null || etudiant.getCod_etu().equals("error")){
						//L'utilisateur est 'student' dans le ldap mais on a pas réussi a récupérer ses identifiants.
						//le message ERROR_LDAP est celui qui convient le mieux
						typeuser = ERROR_LDAP;
					}
				}
			}else{
				typeuser=LOGIN_EXCLU;
			}
		}

	}




	/**
	 * @return la page d'accueil selon le type de l'utilisateur
	 */
	public String accueil() {
		//détermine le type de l'utilisateur stocké dans typeuser
		typeUser();

		//on loggue les connexions si demandé.
		if(config.isLogConnexion()){
			if (typeuser.equals(STUDENT_USER)) { 
				LOG.info("etudiant "+ iduser +" connecte depuis "+getRemoteAdresse());
			}else{
				if(typeuser.equals(LOGIN_EXCLU)){
					LOG.info("utilisateur "+ iduser +" EXCLU depuis "+getRemoteAdresse());
				}else{
					if(typeuser.equals(TEACHER_USER)){
						LOG.info("enseignant "+ iduser +" connecte depuis "+getRemoteAdresse());
					}
				}
			}
		}

		if	(!config.isActivation()) {
			return View.MAINTENANCE;
		} else {
			if (typeuser.equals(ERROR_LDAP)) {
				return View.ERROR_LDAP;
			}
			
			if(typeuser.equals(LOGIN_EXCLU)){
				return View.ERROR;
			}
			
			if (typeuser.equals(ERROR_BDD)) {
				return View.ERROR_BDD;
			}

			if (typeuser.equals(STUDENT_USER)) {
				if	(config.isPartieEtudiant()) {
					statisticController.incNbConnexionEtudiant();
					return View.INDEX_ETU;
				} 
				return View.ERROR;
			} else if (typeuser.equals(TEACHER_USER)) {
				if	(config.isPartieEnseignant()) {
					statisticController.incNbConnexionEnseignant();
					return View.INDEX_ENS;
				} 
				return View.ERROR;
			} else if (typeuser.equals(UNAUTHORIZED_USER)) {
				return View.ERROR;
			} else {
				if (!isPortlet()) {
					return View.LOGIN;
					//return View.ERROR_CONNEXION;
				} 
				return View.ERROR;
			}
		}
	}


	/**
	 * @return iduser
	 */
	public String getIduser() {
		return iduser;
	}

	/**
	 * @param iduser
	 */
	public void setIduser(final String iduser) {
		this.iduser = iduser;
	}

	/**
	 * @return l'adresse ip de l'utilisateur
	 */
	public String getRemoteAdresse() {
		String ra = "";

		try {

			String address = HttpUtils.getClient().toString();
			StringTokenizer s = new StringTokenizer(address, "/");
			while (s.hasMoreTokens()) {
				ra = s.nextToken();
			}
		} catch (UnknownHostException e) {
			LOG.error(e);
		}



		return ra;
	}
	/**
	 * @return typeuser
	 */
	public String getTypeuser() {
		return typeuser;
	}

	/**
	 * @param typeuser
	 */
	public void setTypeuser(final String typeuser) {
		this.typeuser = typeuser;
	}



	/**
	 * @return security
	 */
	public Security getSecurity() {
		return security;
	}

	/**
	 * @param security
	 */
	public void setSecurity(final Security security) {
		this.security = security;
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
	 * @return etudiant
	 */
	public Etudiant getEtudiant() {
		return etudiant;
	}

	/**
	 * @param etudiant
	 */
	public void setEtudiant(final Etudiant etudiant) {
		this.etudiant = etudiant;
	}


	public Config getConfig() {
		return config;
	}


	public void setConfig(Config config) {
		this.config = config;
	}


	public StatisticController getStatisticController() {
		return statisticController;
	}


	public void setStatisticController(StatisticController statisticController) {
		this.statisticController = statisticController;
	}





}
