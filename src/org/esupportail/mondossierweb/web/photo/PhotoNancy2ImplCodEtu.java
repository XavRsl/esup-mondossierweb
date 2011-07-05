/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.photo;

import fr.univnancy2.PhotoClient.beans.Category;
import fr.univnancy2.PhotoClient.beans.PhotoClient;
import fr.univnancy2.PhotoClient.beans.TicketClient;
import fr.univnancy2.PhotoClient.exception.PhotoClientException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.mondossierweb.web.controllers.SessionController;




/**
 * classe pour la gestion des photos (notament la récupération du ticket).
 * @author Charlie Dubois
 */
public class PhotoNancy2ImplCodEtu implements IPhoto {
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(PhotoNancy2ImplCodEtu.class);
	/**
	 * vrai si l'utilisateur est un enseignant.
	 */
	private boolean utilisateurEnseignant;
	
	/**
	 * délai avant expiration du ticket.
	 */
	private static final int DELAI_TICKET_SEC = 200;
	
	/**
	 * url du serveur de photo.
	 */
	private String ressourceurl;
	/**
	 * url du serveur de photo pour le ticket.
	 */
	private String ticketurl;
	/**
	 * vrai si le ticket en cours est pour le serveur.
	 */
	private boolean forserver;
	/**
	 * le code de l'application pour le serveur de photos.
	 */
	private String codeapp;
	/**
	 *  ticket pour le serveur de photos.
	 */
	private TicketClient tc;
	
	/**
	 * client photo.
	 */
	private PhotoClient photoClient;

	/**
	 * constructeur vide.
	 */
	public PhotoNancy2ImplCodEtu() {
		super();
		forserver = false;
	}

	public String toString(){
		return "Bean PhotoNancy2CodEtu : tc="+tc.getCode();
	}
	
	/**
	 * @see org.esupportail.mondossierweb.web.photo.IPhoto#urlPhoto(java.lang.String)
	 */
	public String getUrlPhoto(final String cod_ind, String cod_etu) {
		
		checkTicket(cod_etu);
		
		String url = "";
		
		
		if (tc != null && tc.isValid(DELAI_TICKET_SEC)) {
			
			try {
				url = photoClient.computeURLforCode(Category.ETUDIANT, cod_etu, tc);
				
			} catch (PhotoClientException e) {
				LOG.error(e);
			}
		}
		
		return url;
	}

	public String getUrlPhotoTrombinoscopePdf(final String cod_ind, String cod_etu) {
		String url = "";
		
		
		//on refait le ticket pour le serveur qui genere le pdf:
		SessionController session = (SessionController)  BeanUtils.getBean("sessionController");
		utilisateurEnseignant = session.isEnseignant();
		String loginUser = session.getIduser();
		
		if (!forserver || tc == null || (tc != null && !tc.isValid(DELAI_TICKET_SEC))) {
			initForServer(loginUser);
		}
		if (tc != null && tc.isValid(DELAI_TICKET_SEC)) {
			
			try {
				url = photoClient.computeURLforCode(Category.ETUDIANT, cod_etu, tc);
				
			} catch (PhotoClientException e) {
				LOG.error(e);
			}
		}
		
		
		//on retourne la nouvelle url:
		return url;
	}
	
	/**
	 * @see org.esupportail.mondossierweb.web.photo.IPhoto#ticketBon(int)
	 */
	public void checkTicket(final String cod_etu) {
		
			if (forserver || tc == null || (tc != null && !tc.isValid(DELAI_TICKET_SEC))) {
				
				SessionController session = (SessionController)  BeanUtils.getBean("sessionController");
				utilisateurEnseignant = session.isEnseignant();
				String loginUser = session.getIduser();
				
				if (!utilisateurEnseignant) {
					init(cod_etu, loginUser);
				} else {
					init(loginUser);
				}
				
			}
	
	}
	
	
	public void initForServer(String loginUser) {
		
		String hostadress = "";
		try {
			hostadress = InetAddress.getLocalHost().getHostAddress();
			
			tc = photoClient.getTicket(PhotoClient.MODE_NORMAL,
					hostadress, "ID", "NONE", loginUser);
			

		} catch (UnknownHostException e1) {
			LOG.error(e1);
		} catch (PhotoClientException e) {
			LOG.error(e);
			LOG.error("Erreur serveur de photo : PhotoClientException ");
			tc = null;
			
		}
		forserver = true;
	}
	/**
	 * initialise un ticket pour la photo d'un étudiant.
	 * @param cod_etu
	 */
	public void init(final String cod_etu, String loginUser) {


		String remoteadress = getRemoteAdresse();
		// Paramètres du client photos
		if (photoClient == null) {
			photoClient = new PhotoClient();
			photoClient.setTicketURLPattern(ticketurl);
			photoClient.setRessourceURLPattern(ressourceurl);
			photoClient.setApplicationCode(codeapp);

		}
		// Demande d'un ticket au serveur de photos
		if (forserver || tc == null || (tc != null && !tc.isValid(DELAI_TICKET_SEC))) {
			try {
				tc = photoClient.getTicket(PhotoClient.MODE_NORMAL,
						remoteadress, "ID", cod_etu, loginUser);
				

			} catch (PhotoClientException e) {
				LOG.error(e);
				LOG.error("Erreur serveur de photo : PhotoClientException ");
				tc = null;
				
			}
		}
		forserver = false;

	}

	
	/**
	 * initialise un ticket pour avoir accès à toutes les photos.
	 */
	public void init(String loginUser) {

		String remoteadress = getRemoteAdresse();

		// Paramètres du client photos
		if (photoClient == null) {
			photoClient = new PhotoClient();
			photoClient.setTicketURLPattern(ticketurl);
			photoClient.setRessourceURLPattern(ressourceurl);
			photoClient.setApplicationCode(codeapp);
		}
		// Demande d'un ticket au serveur de photos
		if (forserver || tc == null || (tc != null && !tc.isValid(DELAI_TICKET_SEC))) {
			try {
				tc = photoClient.getTicket(PhotoClient.MODE_NORMAL,
						remoteadress, "ID", "NONE", loginUser);
				

			} catch (PhotoClientException e) {
				LOG.error(e);
				LOG.error("Erreur serveur de photo : PhotoClientException ");
				tc = null;
				
			}
		}
		forserver = false;

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
	 * getter pour l'url de la ressource renseigné dans domain.xml.
	 * @return l'url de la ressource.
	 */
	public String getRessourceurl() {
		return ressourceurl;
	}

	/**
	 * setter pour l'url de la ressource renseigné dans domain.xml.
	 * @param ressourceurl
	 */
	public void setRessourceurl(final String ressourceurl) {
		this.ressourceurl = ressourceurl;
	}

	/**
	 * getter pour l'url de demande de ticket renseigné dans domain.xml.
	 * @return l'url pour récupérer le ticket.
	 */
	public String getTicketurl() {
		return ticketurl;
	}

	/**
	 * setter pour l'url de demande de ticket renseigné dans domain.xml.
	 * @param ticketurl
	 */
	public void setTicketurl(final String ticketurl) {
		this.ticketurl = ticketurl;
	}

	/**
	 * @return photoClient
	 */
	public PhotoClient getPhotoClient() {
		return photoClient;
	}

	/**
	 * @param photoClient
	 */
	public void setPhotoClient(final PhotoClient photoClient) {
		this.photoClient = photoClient;
	}

	public boolean isUtilisateurEnseignant() {
		return utilisateurEnseignant;
	}

	public void setUtilisateurEnseignant(boolean utilisateurEnseignant) {
		this.utilisateurEnseignant = utilisateurEnseignant;
	}

	public String getCodeapp() {
		return codeapp;
	}

	public void setCodeapp(String codeapp) {
		this.codeapp = codeapp;
	}

	


}
