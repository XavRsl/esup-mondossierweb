/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.ActionRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.portlet.PortletUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.domain.beans.IEtudiant;
import org.esupportail.mondossierweb.domain.beans.Inscrit;
import org.esupportail.mondossierweb.services.authentification.Security;
import org.esupportail.mondossierweb.web.converters.EmailConverter;
import org.esupportail.mondossierweb.web.navigation.View;

/**
 * controller lors de la recherche d'un étudiant par son code_etu.
 * @author Charlie Dubois
 */
public class RechercheAnnuaireController extends AbstractContextAwareController {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7739816475437233931L;
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(RechercheAnnuaireController.class);
	/**
	 * outputstream size.
	 */
	private static final int OUTPUTSTREAM_SIZE = 1024;
	/**
	 * Le service.
	 */
	private IDaoService service;
	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapService;
	/**
	 * bean qui fait le lien avec la BDD.
	 */
	private IEtudiant etudiantManager;

	/**
	 * Le bean de complétion de l'adresse email.
	 */
	private EmailConverter emailConverter;

	/**
	 * le controller EtatCivil.
	 */
	private EtatCivilController etatcivilcontroller;

	/**
	 * le code de l'etudiant recherché.
	 */
	private String nom;
	/**
	 * la liste des inscrits.
	 */
	private ArrayList<Inscrit> listeInscrits;

	private boolean stopRecherche;

	private boolean recherche;


	/**
	 * le constructeur.
	 */
	public RechercheAnnuaireController() {
		super();
		listeInscrits = new ArrayList<Inscrit>();
		recherche=false;
		stopRecherche=false;
	}


	/**
	 * valide le formulaire et transmet au dossierWeb via le controller etatcivil.
	 * @return la prochaine vue.
	 */
	public String chercher() {

		String message = "";

		//Si une recherche est deja en cours.
		while (recherche) {
			stopRecherche=true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOG.error(e);
				LOG.info("erreur pendant thread.sleep  chercher()");
			}

		}
		recherche = false;
		stopRecherche=false;

		try {
			//on vérifie que le champ est bien remplie
			if (nom != null && !nom.equals("")) {

				//on vérifie que le champ est remplie avec des éléments correctes
				if (!Pattern.matches("^[a-zA-Z][ a-zA-Z-]*", nom.trim())
						|| nom.length()<3) {
					if(nom.length()<3){
						message = "Veuillez entrer au moins 3 lettres";
					}else{
						message = "Seuls les lettres, les tirets et les espaces sont autorisés.";
					}
					FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
					FacesContext.getCurrentInstance().addMessage(null, messageX);

				} else {	

					boolean existeEtu = false;

					recherche = true;

					listeInscrits.clear();

					//Recherche LDAP:
					List<LdapUser> listeuser = ldapService.getLdapUsersFromToken(nom);

					int nb = 0;

					Security security = (Security)  BeanUtils.getBean("security");

					//pour chaque utilisateur du ldap retenu:
					for (LdapUser lu : listeuser) {

						//test si on a lance une autre recherche entre temps.
						if(!stopRecherche){
							Map mattributs = lu.getAttributes();

							List<String> ltype = (List<String>) mattributs.get(security.getAttributLdapEtudiant());

							//si l'utilisateur est un étudiant:
							if (ltype != null && ltype.get(0) != null && ltype.get(0).equals(security.getTypeEtudiantLdap())) {


								List<String> llogin2 = (List<String>) mattributs.get("uid");
								List<String> lcn = (List<String>) mattributs.get("cn");

								Inscrit ins = new Inscrit();
								ins.setLogin(llogin2.get(0));
								if(!stopRecherche){
									ins.setCod_etu(service.getCodEtuFromLogin(ins.getLogin()));
									if(!stopRecherche && ins.getCod_etu()!= null && Pattern.matches("^[0-9]*", ins.getCod_etu())){
										ins.setCod_ind(service.getCodIndFromCodEtu(ins.getCod_etu()));

										//si le code individu reçu est syntaxiquement correcte:
										if (ins.getCod_ind() != null && !stopRecherche && Pattern.matches("^[0-9]*", ins.getCod_ind())) {
											ins.setLib_nom_pat_ind(lcn.get(0));
											ins.setEmail(emailConverter.getMail(ins.getLogin()));
											ins.setLib_etp(service.getFormationEnCours(ins.getCod_ind()));

											if(!stopRecherche && ins.getLib_etp()!=null){
												//test si aucune erreur grave empéchant par la suite d'aller dans le dossier étudiant:
												if (!ins.getLib_etp().equals("error")) {
													if(!ins.getLib_etp().equals("")){
														listeInscrits.add(ins);
														nb++;
													}
												}
											}
										}
									}else{
										LOG.error("Un probleme est survenu lors de la récupération du codetu de la personne : "+ins.getLogin()+" du ldap");
									}
								}
							}
						}else{
							listeInscrits.clear();
							recherche=false;
							return null;
						}
					}


					if (nb > 0) {
						//on trie le liste par nom
						trierLaListe();
						existeEtu = true;
					}


					if (existeEtu) {
						recherche = false;
						return View.AFFICHE_RECHERCHE_ANNUAIRE;
					} 


					message = "Etudiant en " + nom + " inexistant";
					FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
					FacesContext.getCurrentInstance().addMessage(null, messageX);

				} 
			} else {
				
				message = "Veuillez rentrer au moins 3 caractères.";
				FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
				FacesContext.getCurrentInstance().addMessage(null, messageX);

			}

		} catch(Exception e) {
			recherche = false;
			LOG.error(e);
			message = "Il y a eu un problème lors de la récupération des données. Veuillez réessayer ultérieurement";
			FacesMessage messageX = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
			FacesContext.getCurrentInstance().addMessage(null, messageX);
		}
		recherche = false;
		return View.CHERCHE_ANNUAIRE;

	}

	/**
	 * pour trier la liste par nom.
	 *
	 */
	private void trierLaListe() {

		for (int i = 1; i < listeInscrits.size(); i++) {
			Inscrit ins1 = listeInscrits.get(i);
			boolean insere = false;

			for (int k = 0; k < i; k++) {
				if (!insere) {
					Inscrit ins2 = listeInscrits.get(k);
					if (ins2.getLib_nom_pat_ind().compareTo(ins1.getLib_nom_pat_ind()) > 0) {
						listeInscrits.add(k, ins1);
						listeInscrits.remove(i + 1);
						insere = true;
					}
				}
			}
		}
	}
	/**
	 * @return la vue etat-civil de l'étudiant choisi.
	 */
	public String choisir() {
		FacesContext context = FacesContext.getCurrentInstance();
		String row = (String) context.getExternalContext().getRequestParameterMap().get("row");
		if(row != null && !row.equals("")){
			String codetu = listeInscrits.get(new Integer(row)).getCod_etu();

			return etatcivilcontroller.lienEnterAnnuaire(codetu);
		}
		return null;
	}

	/**
	 * initialise le formulaire.
	 * @return la vue du formulaire.
	 */
	public String enter() {
		nom = "";
		return View.CHERCHE_ANNUAIRE;
	}

	/**
	 * initialise le formulaire.
	 * @return la page d'acccueil
	 */
	public String retour() {
		nom = "";
		return View.INDEX_ENS;
	}

	/**
	 * 
	 * @return le fichier excel
	 */
	public String exportExcel() {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		ExternalContext externalContext = facesContext.getExternalContext();



		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(OUTPUTSTREAM_SIZE);
			HSSFWorkbook wb = creerExcel();
			wb.write(baos);

			byte[] bytes = baos.toByteArray();

			String typeDownload = "application/force-download";

			long id = DownloadUtils.setDownload(bytes, getString("PDF.LISTE")+" " + nom + ".xls", typeDownload, "attachment");
			String url = DownloadUtils.getDownloadUrl(id);

			externalContext.redirect(url);


		} catch (DownloadException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
		facesContext.responseComplete();


		return null;

	}



	/**
	 * créer le fichier excel à partir de la liste des inscrits.
	 * @return le fichier excel de la liste des inscrits.
	 */
	@SuppressWarnings("deprecation")
	public HSSFWorkbook creerExcel() {
		//	creation du fichier excel
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("page1");

		//formatage de la taille des colonne
		sheet.setColumnWidth((short) 0, (short) (3500));
		sheet.setColumnWidth((short) 1, (short) (9000));
		sheet.setColumnWidth((short) 2, (short) (6000));
		sheet.setColumnWidth((short) 3, (short) (15000));


		// Creation des lignes
		HSSFRow row = sheet.createRow((short) 0);

		//CREATION DES STYLES:
		//STYLE1:
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
		headerStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		font.setBoldweight((short) 10);
		headerStyle.setFont(font);
		//bordure style1
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setRightBorderColor(HSSFColor.BLACK.index);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setTopBorderColor(HSSFColor.BLACK.index);



		int rang_cellule = 0;
		HSSFCell cellLib1 = row.createCell((short) rang_cellule);
		cellLib1.setCellStyle(headerStyle);
		cellLib1.setCellValue(getString("PDF.LOGIN").toUpperCase());
		rang_cellule++;

		HSSFCell cellLib2 = row.createCell((short) rang_cellule);
		cellLib2.setCellStyle(headerStyle);
		cellLib2.setCellValue(getString("PDF.NOM").toUpperCase() );
		rang_cellule++;

		HSSFCell cellLib3 = row.createCell((short) rang_cellule);
		cellLib3.setCellStyle(headerStyle);
		cellLib3.setCellValue(getString("PDF.MESSAGERIE").toUpperCase() );
		rang_cellule++;

		HSSFCell cellLib4 = row.createCell((short) rang_cellule);
		cellLib4.setCellStyle(headerStyle);
		cellLib4.setCellValue(getString("PDF.FORMATION").toUpperCase() );
		rang_cellule++;



		int nbrow = 1;
		for (Inscrit inscrit : listeInscrits) {
			HSSFRow rowInscrit  = sheet.createRow((short) nbrow);

			int rang_cellule_inscrit = 0;
			HSSFCell cellLibInscrit1 = rowInscrit.createCell((short) rang_cellule_inscrit);
			cellLibInscrit1.setCellValue(inscrit.getLogin());
			rang_cellule_inscrit++;

			HSSFCell cellLibInscrit2 = rowInscrit.createCell((short) rang_cellule_inscrit);
			cellLibInscrit2.setCellValue(inscrit.getLib_nom_pat_ind());
			rang_cellule_inscrit++;

			HSSFCell cellLibInscrit3 = rowInscrit.createCell((short) rang_cellule_inscrit);
			cellLibInscrit3.setCellValue(inscrit.getEmail());
			rang_cellule_inscrit++;

			HSSFCell cellLibInscrit31 = rowInscrit.createCell((short) rang_cellule_inscrit);
			cellLibInscrit31.setCellValue(inscrit.getLib_etp());
			rang_cellule_inscrit++;

			nbrow++;
		}

		return wb;
	}


	/**
	 * @return etatcivilcontroller
	 */
	public EtatCivilController getEtatcivilcontroller() {
		return etatcivilcontroller;
	}

	/**
	 * @param etatcivilcontroller
	 */
	public void setEtatcivilcontroller(final EtatCivilController etatcivilcontroller) {
		this.etatcivilcontroller = etatcivilcontroller;
	}

	/**
	 * @param codetu
	 */
	public void setCodetu(final String codetu) {
		this.nom = codetu;
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
	 * @return codetu
	 */
	public String getCodetu() {
		return nom;
	}


	/**
	 * @return nom
	 */
	public String getNom() {
		return nom;
	}


	/**
	 * @param nom
	 */
	public void setNom(final String nom) {
		this.nom = nom;
	}





	public boolean isStopRecherche() {
		return stopRecherche;
	}


	public void setStopRecherche(boolean stopRecherche) {
		this.stopRecherche = stopRecherche;
	}


	public boolean isRecherche() {
		return recherche;
	}


	public void setRecherche(boolean recherche) {
		this.recherche = recherche;
	}


	public LdapUserService getLdapService() {
		return ldapService;
	}


	public void setLdapService(LdapUserService ldapService) {
		this.ldapService = ldapService;
	}


	/**
	 * @return emailConverter
	 */
	public EmailConverter getEmailConverter() {
		return emailConverter;
	}


	/**
	 * @param emailConverter
	 */
	public void setEmailConverter(final EmailConverter emailConverter) {
		this.emailConverter = emailConverter;
	}


	/**
	 * @return listeInscrits
	 */
	public ArrayList<Inscrit> getListeInscrits() {
		return listeInscrits;
	}


	/**
	 * @param listeInscrits
	 */
	public void setListeInscrits(ArrayList<Inscrit> listeInscrits) {
		this.listeInscrits = listeInscrits;
	}


	/**
	 * @return etudiantManager
	 */
	public IEtudiant getEtudiantManager() {
		return etudiantManager;
	}






	/**
	 * @param etudiantManager
	 */
	public void setEtudiantManager(IEtudiant etudiantManager) {
		this.etudiantManager = etudiantManager;
	}


}
