/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;



import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;



import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.domain.beans.Config;
import org.esupportail.mondossierweb.domain.beans.ElementPedagogique;
import org.esupportail.mondossierweb.domain.beans.Etape;
import org.esupportail.mondossierweb.domain.beans.Inscrit;
import org.esupportail.mondossierweb.dto.ObjetRecherche;
import org.esupportail.mondossierweb.web.converters.EmailConverterInterface;
import org.esupportail.mondossierweb.web.navigation.View;
import org.esupportail.mondossierweb.web.photo.IPhoto;


import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * controller pour l'affichage de la liste des inscrits et du trombinoscope.
 * @author Charlie Dubois
 */
public class ListeInscritsController extends AbstractContextAwareController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6240479579657217688L;
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(ListeInscritsController.class);
	/**
	 * le nombre d'inscrits par ligne dans la version pdf du trombinoscope.
	 */
	private static final int NB_INSCRITS_LIGNE_TROMBI_PDF = 7;
	/**
	 * 
	 */
	private static final int NB_LIGNE_INSEREE_TROMBI_PDF_A_LA_SUITE = 20;
	/**
	 * le nombre d'inscrit par ligne dans l'affichage du trombinoscope.
	 */
	private static final int NB_INSCRITS_LIGNE_TROMBI = 8;
	/**
	 * outputstream size.
	 */
	private static final int OUTPUTSTREAM_SIZE = 1024;
	/**
	 * marge.
	 */
	private static final float MARGE_PDF = 1.5f;
	/**
	 * l'écartement du pied de page (libellé de la promo et date d'édition) des pdf.
	 */
	private static final int ECARTEMENT_PIED_PAGE_PDF = 80;
	/**
	 * Le service.
	 */
	private IDaoService service;

	/**
	 * le bean pour la récupération des photos.
	 */
	private IPhoto photo;
	/**
	 * Le bean de complétion de l'adresse email.
	 */
	private EmailConverterInterface emailConverter;
	/**
	 * vrai si on veut interrompre la génération du pdf (car l'utilisateur en demande un autre)
	 */
	private boolean stopGeneration;
	/**
	 * Le bean de configuration.
	 */
	private Config config;
	/**
	 * l'annee.
	 */
	private String annee;
	/**
	 * le type de recherche (Etape ou Elément pédagogique).
	 */
	private String type;
	/**
	 * le code .
	 */
	private String code;
	/**
	 * le code version.
	 */
	private String version;

	/**
	 * resultat session1.
	 */
	private boolean session1;

	/**
	 * resultat session2.
	 */
	private boolean session2;
	/**
	 * resultat etape appartenance.
	 */
	private boolean etape;
	/**
	 * la liste des inscrits.
	 */
	private ArrayList<Inscrit> listeInscrits;
	/**
	 * la liste des inscrits rangés pour l'affichage du trombinoscope.
	 */
	private ArrayList<ArrayList<Inscrit>> listeInscritsTrombi;
	/**
	 * vrai si on affiche le scroller dans la page de liste des inscrits.
	 */
	private boolean afficheScroller;
	/**
	 * le libelle de l'étape ou de l'élément.
	 */
	private String libelle;
	/**
	 * vrai si on traite une etape.
	 */
	private boolean traiteEtape;
	/**
	 * retourne le nombre d'inscrits.
	 */
	private int nbInscrits;
	/**
	 * vrai si il y a des inscrits a l'étape ou l'élément pédagogique.
	 */
	private boolean existeInscrits;
	/**
	 * si vient d'une recherche par code ou en parcourant l'arbre.
	 */
	private boolean recherchecode;
	/**
	 * si vient d'une recherche de groupe par code.
	 */
	private boolean recherchecodegroupe;
	/**
	 * vrai si vient d'une recherche en parcourant l'arbre par diplômes .
	 */
	private boolean parDiplomes;
	/**
	 * vrai si vient d'une recherche en parcourant l'arbre par étapes.
	 */
	private boolean parEtapes;


	private boolean download;

	private byte[] pdfBytes;

	private boolean photosValides;

	private boolean affichageTrombiDecoupe;

	private boolean affichageTrombiDecoupePremierePage;

	private boolean affichageTrombiDecoupeDernierePage;

	private int pageVisualiseeTrombinoscope;

	private int nbPageTotalVisualiseeTrombinoscope;

	private int nbInscritsPageTrombi;

	private boolean controllerEnAction;

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Bean LISTEINSCRITSCONTROLLER : annee="+annee+" code="+code+" version="+version+" libelle="+libelle+" type="+type +"   ListeInscrits :  "+listeInscrits;
	}
	/**
	 *le contructeur.
	 */
	public ListeInscritsController() {
		super();
		listeInscrits = new ArrayList<Inscrit>();
		listeInscritsTrombi = new ArrayList<ArrayList<Inscrit>>();
		download = false;
		photosValides = false;
		affichageTrombiDecoupe = false;
		affichageTrombiDecoupePremierePage = false;
		affichageTrombiDecoupeDernierePage = false;
		pageVisualiseeTrombinoscope = 0;
		nbPageTotalVisualiseeTrombinoscope = 0;
		Config c = (Config)  BeanUtils.getBean("config");
		nbInscritsPageTrombi = c.getNbEtudiantsParPageAfficheeDuTrombinoscope();
		stopGeneration=false;
		type = "";
		libelle = "";
		controllerEnAction=false;
		recherchecodegroupe=false;
	}
	/**
	 * détermine la liste des inscrits à l'étape ou l'élément pédagogique sélectionné.
	 * @return la page de visualisation des inscrits
	 */
	public String enter() {

		while(controllerEnAction){
			//on attend que l'action en cours (methode 'enter') soit terminée.
			//Si la méthode 'enter' est déjà en cours d'exécution, c'est que l'utilisateur a du cliquer 2 fois de suite sur 
			//la page web, appelant deux fois la méthode successivement.
			//on empeche les méthodes de s'exécuter en même temps pour ne pas avoir de ConcurrentModificationException lors de setLoginInscrits().
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOG.error(e);
				LOG.info("erreur pendant thread.sleep enter()");
			}
		}
		controllerEnAction = true;

		//Si un pdf est en cours de génération on l'arrete.
		while (download) {
			stopGeneration=true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOG.error(e);
				LOG.info("erreur pendant thread.sleep2  enter()");
			}

		}
		download = false;
		stopGeneration=false;

		photosValides = false;
		listeInscrits.clear();
		listeInscritsTrombi.clear();
		recherchecodegroupe = false;

		if (type.equals("Etape")) {
			//On part d'une Etape pour établir une liste d'étudiant
			ObjetRecherche objr = new ObjetRecherche();
			objr.setCode(code);
			objr.setVersion(version);
			libelle = service.getLibelleEtape(objr);
			Etape e = new Etape(code, version, annee);
			if (!session1 && !session2) {
				listeInscrits = ( ArrayList<Inscrit>) service.getInscritsEtape(e);
			} 
			if (session1 && !session2) {
				listeInscrits = (ArrayList<Inscrit>) service.getInscritsEtapeJuin(e);	
			} 
			if (!session1 && session2) {
				listeInscrits = (ArrayList<Inscrit>) service.getInscritsEtapeSep(e);
			} 
			if (session1 && session2) {
				listeInscrits = (ArrayList<Inscrit>) service.getInscritsEtapeJuinSep(e);
			}
		} else {
			if (type.equals("Groupe")) {
				//On part d'un Groupe pour établir une liste d'étudiant
				ObjetRecherche r = new ObjetRecherche();
				r.setAnneeencours(annee);
				r.setCode(code);
				listeInscrits = (ArrayList<Inscrit>) service.getInscritsGroupe(r);
				recherchecodegroupe=true;
			}else{
				//On part d'un ELP pour établir une liste d'étudiant
				libelle = service.getLibelleElementPedagogique(code);
				ElementPedagogique e = new ElementPedagogique(code, annee);
				if (!session1 && !session2 && !etape) {
					listeInscrits = (ArrayList<Inscrit>) service.getInscritsElementPedagogique(e);
				}
				if (session1 && !session2 && !etape) {
					listeInscrits = (ArrayList<Inscrit>) service.getInscritsElementPedagogiqueJuin(e);
				}
				if (!session1 && session2 && !etape) {
					listeInscrits = (ArrayList<Inscrit>) service.getInscritsElementPedagogiqueSep(e);
				}
				if (!session1 && !session2 && etape) {
					listeInscrits = (ArrayList<Inscrit>) service.getInscritsElementPedagogiqueEtape(e);
				}
				if (session1 && session2 && !etape) {
					listeInscrits = (ArrayList<Inscrit>) service.getInscritsElementPedagogiqueJuinSep(e);
				}
				if (session1 && !session2 && etape) {
					listeInscrits = (ArrayList<Inscrit>) service.getInscritsElementPedagogiqueJuinEtape(e);
				}
				if (!session1 && session2 && etape) {
					listeInscrits = (ArrayList<Inscrit>) service.getInscritsElementPedagogiqueSepEtape(e);
				}
				if (session1 && session2 && etape) {
					listeInscrits = (ArrayList<Inscrit>) service.getInscritsElementPedagogiqueJuinSepEtape(e);
				}
			}
		}

		setLoginInscrits();
		setMailInscrits();

		//on vérifie que les photo sont récupérées pour savoir si on peut afficher le lien vers le trombinoscope:
		if(listeInscrits != null && listeInscrits.size() > 0) {
			listeInscrits.get(0).setUrlphoto(photo.getUrlPhoto(listeInscrits.get(0).getCod_ind(), listeInscrits.get(0).getCod_etu()));
			if (listeInscrits.get(0).getUrlphoto() != null && !listeInscrits.get(0).getUrlphoto().equals("")) {
				photosValides = true;
			}
		}

		controllerEnAction=false;
		return View.INSCRITS_ARBORESCENCE;
	}

	/**
	 * renseigne les logins de chaque inscrit.
	 *
	 */
	private void setLoginInscrits() {
		for (Inscrit i : listeInscrits) {
			i.setLogin(service.getLoginFromCodEtu(i.getCod_etu()));
		}
	}

	/**
	 * renseigne les emails de chaque inscrit.
	 *
	 */
	private void setMailInscrits() {
		for (Inscrit i : listeInscrits) {
			i.setEmail(emailConverter.getMail(i.getLogin()));
		}
	}


	/**
	 * renseigne l'url pour la photo de chaque inscrit.
	 *
	 */
	private void setUrlPhotos() {
		for (Inscrit i : listeInscrits) {
			i.setUrlphoto(photo.getUrlPhoto(i.getCod_ind(), i.getCod_etu()));

		}
	}


	/**
	 * vérifie le ticket pour les photos, créer la liste pour le trombinoscope.
	 * @return la vue du trombinoscope.
	 */
	public String trombinoscope() {
		//Si un pdf est en cours de génération on l'arrete.
		while (download) {
			stopGeneration=true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOG.error(e);
				LOG.info("erreur pendant thread.sleep trombinoscope(");
			}

		}
		download = false;
		stopGeneration=false;

		if (listeInscrits.size() < nbInscritsPageTrombi || nbInscritsPageTrombi == 0) {
			affichageTrombiDecoupe = false;
			affichageTrombiDecoupePremierePage = false;
			affichageTrombiDecoupeDernierePage = false;
			pageVisualiseeTrombinoscope = 0;
			nbPageTotalVisualiseeTrombinoscope = 0;
			setUrlPhotos();
			setListeTrombi();
		} else {
			affichageTrombiDecoupe = true;
			for (int i = 0; i < nbInscritsPageTrombi; i++) {
				Inscrit ins = listeInscrits.get(i);
				ins.setUrlphoto(photo.getUrlPhoto(ins.getCod_ind(), ins.getCod_etu()));
			}
			//prepare la 1er page:
			setListeTrombi(1);

			pageVisualiseeTrombinoscope = 1;
			nbPageTotalVisualiseeTrombinoscope = (listeInscrits.size() / nbInscritsPageTrombi) + 1;
			affichageTrombiDecoupePremierePage = true;
			affichageTrombiDecoupeDernierePage = false;
		}
		return View.INSCRITS_TROMBINOSCOPE;
	}




	public String naviguationTrombinoscope() {
		FacesContext context = FacesContext.getCurrentInstance();
		String action = (String) context.getExternalContext().getRequestParameterMap().get("action");
		//on récupère la page en cours au moment du clic pour gérer le cas d'un retour arrière du navigateur
		String paramPage = (String) context.getExternalContext().getRequestParameterMap().get("pageEnCours");
		
		if(paramPage != null && !paramPage.equals("")){
			pageVisualiseeTrombinoscope = new Integer(paramPage);
		}
		
		affichageTrombiDecoupe = true;

		//Si un pdf est en cours de génération on l'arrete.
		while (download) {
			stopGeneration=true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOG.error(e);
				LOG.info("erreur pendant thread.sleep navigationTrombinoscope(");
			}
		}
		download = false;
		stopGeneration=false;


		if (action.equals("suivant")) {
			//cas ou on fait 'suivant':
			for (int i = (nbInscritsPageTrombi * pageVisualiseeTrombinoscope); i < (nbInscritsPageTrombi + (nbInscritsPageTrombi * pageVisualiseeTrombinoscope)); i++) {
				if (i < listeInscrits.size()) {
					Inscrit ins = listeInscrits.get(i);
					ins.setUrlphoto(photo.getUrlPhoto(ins.getCod_ind(), ins.getCod_etu()));
				}
			}
			pageVisualiseeTrombinoscope++;
			//prepare la 1er page:
			setListeTrombi(pageVisualiseeTrombinoscope);

			affichageTrombiDecoupePremierePage = false;
		} else {
			//cas ou on fait 'précédent':
			pageVisualiseeTrombinoscope--;
			for (int i = (nbInscritsPageTrombi * pageVisualiseeTrombinoscope); i < (nbInscritsPageTrombi + (nbInscritsPageTrombi * pageVisualiseeTrombinoscope)); i++) {
				if (i < listeInscrits.size()) {
					Inscrit ins = listeInscrits.get(i);
					ins.setUrlphoto(photo.getUrlPhoto(ins.getCod_ind(), ins.getCod_etu()));
				}
			}

			//prepare la 1er page:
			setListeTrombi(pageVisualiseeTrombinoscope);

			affichageTrombiDecoupeDernierePage = false;
			if (pageVisualiseeTrombinoscope == 1) {
				affichageTrombiDecoupePremierePage = true;
			}

		}
		nbPageTotalVisualiseeTrombinoscope = (listeInscrits.size() / nbInscritsPageTrombi) + 1;
		return View.INSCRITS_TROMBINOSCOPE;
	}
	/**
	 * créer la liste de liste de NB_INSCRITS_LIGNE_TROMBI inscrits pour la page placée en parametre 
	 * pour l'affichage du trombinoscope.
	 * 
	 */
	public void setListeTrombi(final int page) {
		listeInscritsTrombi.clear();
		int nblignes = (nbInscritsPageTrombi / NB_INSCRITS_LIGNE_TROMBI) + 1;
		int compteur = nbInscritsPageTrombi * (page - 1);
		for (int i = 0; i < nblignes; i++) {
			ArrayList<Inscrit> linscrits = new ArrayList<Inscrit>();
			for (int k = 0; k < NB_INSCRITS_LIGNE_TROMBI; k++) {
				if (compteur < listeInscrits.size() && compteur < (nbInscritsPageTrombi * page) ) {
					linscrits.add(listeInscrits.get(compteur));
					compteur++;
				} else {
					if (compteur >= listeInscrits.size()) {
						//on va afficher la derniere page
						affichageTrombiDecoupeDernierePage = true;
					}
				}
			}
			listeInscritsTrombi.add(linscrits);
		}
	}

	/**
	 * créer la liste de liste de NB_INSCRITS_LIGNE_TROMBI inscrits 
	 * pour l'affichage du trombinoscope.
	 *
	 */
	public void setListeTrombi() {
		listeInscritsTrombi = new ArrayList<ArrayList<Inscrit>>();
		int nblignes = (listeInscrits.size() / NB_INSCRITS_LIGNE_TROMBI) + 1;
		int compteur = 0;
		for (int i = 0; i < nblignes; i++) {
			ArrayList<Inscrit> linscrits = new ArrayList<Inscrit>();
			for (int k = 0; k < NB_INSCRITS_LIGNE_TROMBI; k++) {
				if (compteur < listeInscrits.size()) {
					linscrits.add(listeInscrits.get(compteur));
					compteur++;
				}
			}
			listeInscritsTrombi.add(linscrits);
		}
	}

	/**
	 * exporte la liste des inscrits en format excel.
	 * @return le fichier excel dans la session en passant par la DownloadServlet
	 */
	public String exportExcel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		ExternalContext externalContext = facesContext.getExternalContext();

		//Si un pdf est en cours de génération on l'arrete.
		while (download) {
			stopGeneration=true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOG.error(e);
				LOG.info("erreur pendant thread.sleep exportExcel(");
			}

		}
		download = false;
		stopGeneration=false;


		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(OUTPUTSTREAM_SIZE);
			HSSFWorkbook wb = creerExcel();
			wb.write(baos);

			byte[] bytes = baos.toByteArray();

			String typeDownload = "application/force-download";


			long id = DownloadUtils.setDownload(bytes, libelle.replace('.', ' ').replace(' ', '_') + ".xls", typeDownload, "attachment");

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
		sheet.setColumnWidth((short) 0, (short) (4000));
		sheet.setColumnWidth((short) 1, (short) (6000));
		sheet.setColumnWidth((short) 2, (short) (5120));
		sheet.setColumnWidth((short) 3, (short) (4000));
		sheet.setColumnWidth((short) 4, (short) (8000));
		if (isTraiteEtape()) {
			sheet.setColumnWidth((short) 5 , (short) (1200));
			if (isEtape()) {
				sheet.setColumnWidth((short) 6, (short) (2000));
				sheet.setColumnWidth((short) 7, (short) (3000));
				sheet.setColumnWidth((short) 8, (short) (8000));

				sheet.setColumnWidth((short) 9, (short) (2000));
				sheet.setColumnWidth((short) 10, (short) (3000));
				sheet.setColumnWidth((short) 11, (short) (2000));
				sheet.setColumnWidth((short) 12, (short) (3000));

			} else {
				sheet.setColumnWidth((short) 6, (short) (2000));
				sheet.setColumnWidth((short) 7, (short) (3000));
				sheet.setColumnWidth((short) 8, (short) (2000));
				sheet.setColumnWidth((short) 9, (short) (3000));
			}

		} else {
			if (isEtape()) {
				sheet.setColumnWidth((short) 5, (short) (2000));
				sheet.setColumnWidth((short) 6, (short) (3000));
				sheet.setColumnWidth((short) 7, (short) (8000));

				sheet.setColumnWidth((short) 8, (short) (2000));
				sheet.setColumnWidth((short) 9, (short) (3000));
				sheet.setColumnWidth((short) 10, (short) (2000));
				sheet.setColumnWidth((short) 11, (short) (3000));

			} else {
				sheet.setColumnWidth((short) 5, (short) (2000));
				sheet.setColumnWidth((short) 6, (short) (3000));
				sheet.setColumnWidth((short) 7, (short) (2000));
				sheet.setColumnWidth((short) 8, (short) (3000));
			}
		}

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
		cellLib1.setCellValue(getString("PDF.FOLDER").toUpperCase() );
		rang_cellule++;

		HSSFCell cellLib2 = row.createCell((short) rang_cellule);
		cellLib2.setCellStyle(headerStyle);
		cellLib2.setCellValue(getString("PDF.NOM").toUpperCase() );
		rang_cellule++;

		HSSFCell cellLib3 = row.createCell((short) rang_cellule);
		cellLib3.setCellStyle(headerStyle);
		cellLib3.setCellValue(getString("PDF.PRENOM").toUpperCase() );
		rang_cellule++;

		HSSFCell cellLib4 = row.createCell((short) rang_cellule);
		cellLib4.setCellStyle(headerStyle);
		cellLib4.setCellValue(getString("PDF.NAISSANCE").toUpperCase() );
		rang_cellule++;

		HSSFCell cellLib5 = row.createCell((short) rang_cellule);
		cellLib5.setCellStyle(headerStyle);
		cellLib5.setCellValue(getString("PDF.MESSAGERIE").toUpperCase() );
		rang_cellule++;

		if (isTraiteEtape()) {
			HSSFCell cellLib6 = row.createCell((short) rang_cellule);
			cellLib6.setCellStyle(headerStyle);
			cellLib6.setCellValue(getString("PDF.IAE").toUpperCase()+"?" );
			rang_cellule++;
		}
		if (isEtape()) {
			HSSFCell cellLib7 = row.createCell((short) rang_cellule);
			cellLib7.setCellStyle(headerStyle);
			cellLib7.setCellValue(getString("PDF.CODE").toUpperCase() );
			rang_cellule++;

			HSSFCell cellLib8 = row.createCell((short) rang_cellule);
			cellLib8.setCellStyle(headerStyle);
			cellLib8.setCellValue(getString("PDF.VERSION").toUpperCase() );
			rang_cellule++;

			HSSFCell cellLib9 = row.createCell((short) rang_cellule);
			cellLib9.setCellStyle(headerStyle);
			cellLib9.setCellValue(getString("PDF.STEP").toUpperCase() );
			rang_cellule++;
		}

		if (isSession1()) {
			HSSFCell cellLib10 = row.createCell((short) rang_cellule);
			cellLib10.setCellStyle(headerStyle);
			cellLib10.setCellValue(getString("PDF.NOTE1").toUpperCase());
			rang_cellule++;

			HSSFCell cellLib11 = row.createCell((short) rang_cellule);
			cellLib11.setCellStyle(headerStyle);
			cellLib11.setCellValue(getString("PDF.RESULT1").toUpperCase());
			rang_cellule++;

		}

		if (isSession2()) {
			HSSFCell cellLib12 = row.createCell((short) rang_cellule);
			cellLib12.setCellStyle(headerStyle);
			cellLib12.setCellValue(getString("PDF.NOTE2").toUpperCase() );
			rang_cellule++;

			HSSFCell cellLib13 = row.createCell((short) rang_cellule);
			cellLib13.setCellStyle(headerStyle);
			cellLib13.setCellValue(getString("PDF.RESULT2").toUpperCase());
			rang_cellule++;

		}

		int nbrow = 1;
		for (Inscrit inscrit : listeInscrits) {
			HSSFRow rowInscrit  = sheet.createRow((short) nbrow);

			int rang_cellule_inscrit = 0;
			HSSFCell cellLibInscrit1 = rowInscrit.createCell((short) rang_cellule_inscrit);
			cellLibInscrit1.setCellValue(inscrit.getCod_etu());
			rang_cellule_inscrit++;

			HSSFCell cellLibInscrit2 = rowInscrit.createCell((short) rang_cellule_inscrit);
			cellLibInscrit2.setCellValue(inscrit.getLib_nom_pat_ind());
			rang_cellule_inscrit++;

			HSSFCell cellLibInscrit3 = rowInscrit.createCell((short) rang_cellule_inscrit);
			cellLibInscrit3.setCellValue(inscrit.getLib_pr1_ind());
			rang_cellule_inscrit++;

			HSSFCell cellLibInscrit31 = rowInscrit.createCell((short) rang_cellule_inscrit);
			cellLibInscrit31.setCellValue(inscrit.getDate_nai_ind());
			rang_cellule_inscrit++;

			HSSFCell cellLibInscrit4 = rowInscrit.createCell((short) rang_cellule_inscrit);
			cellLibInscrit4.setCellValue(inscrit.getEmail());
			rang_cellule_inscrit++;

			if (isTraiteEtape()) {
				HSSFCell cellLibInscrit5 = rowInscrit.createCell((short) rang_cellule_inscrit);
				cellLibInscrit5.setCellValue(inscrit.getIae());
				rang_cellule_inscrit++;
			}

			if (isEtape()) {
				HSSFCell cellLibInscrit6 = rowInscrit.createCell((short) rang_cellule_inscrit);
				cellLibInscrit6.setCellValue(inscrit.getCod_etp());
				rang_cellule_inscrit++;

				HSSFCell cellLibInscrit7 = rowInscrit.createCell((short) rang_cellule_inscrit);
				cellLibInscrit7.setCellValue(inscrit.getCod_vrs_vet());
				rang_cellule_inscrit++;

				HSSFCell cellLibInscrit8 = rowInscrit.createCell((short) rang_cellule_inscrit);
				cellLibInscrit8.setCellValue(inscrit.getLib_etp());
				rang_cellule_inscrit++;
			}

			if (isSession1()) {
				HSSFCell cellLibInscrit9 = rowInscrit.createCell((short) rang_cellule_inscrit);
				cellLibInscrit9.setCellValue(inscrit.getNotej());
				rang_cellule_inscrit++;

				HSSFCell cellLibInscrit10 = rowInscrit.createCell((short) rang_cellule_inscrit);
				cellLibInscrit10.setCellValue(inscrit.getResj());
				rang_cellule_inscrit++;
			}

			if (isSession2()) {
				HSSFCell cellLibInscrit11 = rowInscrit.createCell((short) rang_cellule_inscrit);
				cellLibInscrit11.setCellValue(inscrit.getNotes());
				rang_cellule_inscrit++;

				HSSFCell cellLibInscrit12 = rowInscrit.createCell((short) rang_cellule_inscrit);
				cellLibInscrit12.setCellValue(inscrit.getRess());
				rang_cellule_inscrit++;
			}

			nbrow++;
		}

		return wb;
	}

	/**
	 * créer le fichier pdf du trombinoscope de la liste des inscrits.
	 * @return le fichier pdf de la liste des inscrits.
	 */
	public String exportPdf() {


		FacesContext facesContext = FacesContext.getCurrentInstance();

		ExternalContext externalContext = facesContext.getExternalContext();

		//if (PortletUtil.isPortletRequest(context)) {
		//ExternalContext external = context.getExternalContext();
		//CONTEXT PORTLET

		//ActionRequest request = (ActionRequest) external.getRequest();
		//String browser = request.getProperty("user-agent");

		try {



			String part="";
			if (affichageTrombiDecoupe) {
				int nbparttotale = (listeInscrits.size() / nbInscritsPageTrombi) + 1;
				part = getString("PDF.TROMBONOSCOPE.PART") + pageVisualiseeTrombinoscope + "/" + nbparttotale;
			}
			String typeDownload = "application/force-download";


			long id = DownloadUtils.setDownload(pdfBytes, getString("PDF.TITLE.TROMBINOSCOPE")+"-" + libelle.replace('.', ' ').replace(' ', '_') + part + ".pdf", typeDownload, "attachment");

			String url = DownloadUtils.getDownloadUrl(id);

			LOG.info("--downloadURL : "+url);
			//System.out.println("----urlfinal---"+externalContext.getRequestContextPath());

			pdfBytes = null;
			download = false;

			externalContext.redirect(url);


		} catch (DownloadException e) {
			LOG.error(e);
			LOG.error(e.toString());
		}  catch (IOException e) {
			LOG.error(e);
			LOG.error(e.toString());
		} 
		facesContext.responseComplete();

		/*} else {

			ExternalContext external = context.getExternalContext();


			HttpServletRequest request = (HttpServletRequest) external.getRequest();
			HttpSession sessions = request.getSession();
			try {
				String part="";
				if(affichageTrombiDecoupe) {
					int nbparttotale = (listeInscrits.size() / nbInscritsPageTrombi) + 1;
					part = "part" + pageVisualiseeTrombinoscope + "/" + nbparttotale;
			}
				String typeDownload = "application/force-download";


				sessions.setAttribute("downloadData", pdfBytes);
				sessions.setAttribute("downloadContentType", typeDownload);
				sessions.setAttribute("downloadFilename", "trombinoscope-" + libelle.replace('.', ' ').replace(' ', '_')+ part + ".pdf");



				//DownloadUtils.setDownload(pdfBytes, "trombinoscope-" + libelle.replace('.', ' ').replace(' ', '_') + part + ".pdf", "application/force-download");

				pdfBytes = null;
				download = false;
				external.redirect(external.getRequestContextPath() + "/download");
			} catch (DownloadException e) {
				LOG.error(e);
				LOG.error(e.toString());
			} catch (IOException e) {
				LOG.error(e);
				LOG.error(e.toString());
			} 
			context.responseComplete();
		}*/
		pdfBytes = null;
		download = false;
		return null;

	}

	/**
	 * @return la vue trombionoscope avec le bouotn download
	 */
	public String genererPdf() {

		//Si un pdf est en cours de génération on l'arrete.
		while (download) {
			stopGeneration=true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOG.error(e);
				LOG.info("erreur pendant thread.sleep genererPdf()");
			}

		}
		stopGeneration=false;
		download=true;


		Document document = configureDocument(MARGE_PDF);


		ByteArrayOutputStream baosPDF = new ByteArrayOutputStream(OUTPUTSTREAM_SIZE);
		PdfWriter docWriter = null;
		try {
			docWriter = PdfWriter.getInstance(document, baosPDF);
		} catch (DocumentException e) {
			LOG.error(e);
			e.printStackTrace();
		}

		docWriter.setStrictImageSequence(true);


		//on change les urls pour que le serveur y accede:
		/*for (Inscrit i : listeInscrits) {
				i.setUrlphoto(photo.getUrlPhotoTrombinoscopePdf(i.getCod_ind()));
			}*/
		creerPdf(document);
		if(!stopGeneration){
			docWriter.close();
			pdfBytes = baosPDF.toByteArray();
			//le pdf est fini on remet les url des photos pour qu'elles soit visible par le client
			//setUrlPhotos();

			return exportPdf();
		}else{
			download=false;
			return null;
		}
	}

	/**
	 * créer le pdf du trombinoscope.
	 * @param document pdf
	 */
	public void creerPdf(final Document document) {



		//configuration des fonts
		Font normal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
		Font normalbig = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
		Font legerita = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC);
		Font leger = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.NORMAL);
		Font headerbig = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD);
		Font header = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);

		//pieds de pages:
		String part="";
		if(affichageTrombiDecoupe) {
			int nbparttotale = (listeInscrits.size() / nbInscritsPageTrombi) + 1;
			part = getString("PDF.TROMBONOSCOPE.PART") + pageVisualiseeTrombinoscope + "/" + nbparttotale;
		}
		Date d = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(d);
		//alignement des libellés du pied de page:
		String partie1 = libelle+" "+annee;
		String partie2 = getString("PDF.EDITION.DATE")+" : " + date;
		if (partie1.length() < ECARTEMENT_PIED_PAGE_PDF) {
			int diff = ECARTEMENT_PIED_PAGE_PDF - partie1.length();
			for (int i = 0; i < diff; i++) {
				partie1 = partie1 + " ";

			}
		} 
		if (partie2.length() < ECARTEMENT_PIED_PAGE_PDF) {
			int diff = ECARTEMENT_PIED_PAGE_PDF - partie2.length();
			for (int i = 0; i < diff; i++) {
				partie2 = " " + partie2;
			}
		}

		//création du pied de page:
		Phrase phra = new Phrase(partie1 + "-" + part +" Page", legerita);
		Phrase phra2 = new Phrase("- "+partie2, legerita);
		HeaderFooter hf = new HeaderFooter(phra, phra2);
		hf.setAlignment(HeaderFooter.ALIGN_CENTER);
		document.setFooter(hf);	 


		//ouverte du document.
		document.open();

		try {

			//ajout image test
			if (config.getLogoUniversitePdf() != null && !config.getLogoUniversitePdf().equals("")){
				Image image1 = Image.getInstance(config.getLogoUniversitePdf());
				float scaleRatio = 40 / image1.height();
				float newWidth=scaleRatio * image1.width();
				image1.scaleAbsolute(newWidth, 40);
				image1.setAbsolutePosition(800 - newWidth, 528);
				document.add(image1);
			}



			Paragraph p = new Paragraph(getString("PDF.TITLE.TROMBINOSCOPE").toUpperCase(), headerbig);
			p.indentationLeft();
			document.add(p);

			Paragraph p3 = new Paragraph(getString("PDF.PROMOTION")+" : " + libelle, normal);
			p3.indentationLeft();
			document.add(p3);

			Paragraph p2 = new Paragraph(getString("PDF.YEAR")+" : " + annee, normal);
			p2.indentationLeft();
			document.add(p2);

			Paragraph p4 = new Paragraph(getString("PDF.NBINSCRITS")+" : " + nbInscrits, normal);
			p4.indentationLeft();
			document.add(p4);


			Paragraph p03 = new Paragraph(getString("PDF.EDITION.DATE")+" : " + date + "\n\n", normal);
			p03.indentationLeft();
			document.add(p03);


			PdfPTable table = new PdfPTable(NB_INSCRITS_LIGNE_TROMBI_PDF);
			table.setWidthPercentage(100f);

			int compteur = 0;
			Rectangle border = new Rectangle(0f, 0f);
			border.setBorderColorLeft(Color.WHITE);
			border.setBorderColorBottom(Color.WHITE);
			border.setBorderColorRight(Color.WHITE);
			border.setBorderColorTop(Color.WHITE);

			String tabNom[] = new String[NB_INSCRITS_LIGNE_TROMBI_PDF];
			String tabNum[] = new String[NB_INSCRITS_LIGNE_TROMBI_PDF];
			//insertion de listeInscrits dans listeInscritstrombi si le trombinoscope n'est pas decoupé
			/*	if (listeInscritsTrombi == null || listeInscritsTrombi.size() == 0) {
				ArrayList<Inscrit> listeInscritsbis = (ArrayList<Inscrit>) listeInscrits.clone();
				listeInscritsTrombi.add(listeInscritsbis);
			}*/
			//nombre d'etudiants insérer a la suite dans le pdf:
			int nbEtudiantInsere = 0;
			boucleFor : for (ArrayList<Inscrit> li : listeInscritsTrombi) {
				for (Inscrit inscrit : li) {
					if(!stopGeneration){
						nbEtudiantInsere++;
						//on en a inséré le plus possible d'un coup (pour eviter un timeout du server 
						//de photos sur les premieres photos 
						//au moment de l'insertion dans le pdf : document.add() ):
						//on insere la table dans le pdf et on recommence une nouvelle table
						if (nbEtudiantInsere > (NB_INSCRITS_LIGNE_TROMBI_PDF * NB_LIGNE_INSEREE_TROMBI_PDF_A_LA_SUITE)) {
							document.add(table);
							document.newPage();
							table = new PdfPTable(NB_INSCRITS_LIGNE_TROMBI_PDF);
							table.setWidthPercentage(100f);
							tabNom = new String[NB_INSCRITS_LIGNE_TROMBI_PDF];
							tabNum = new String[NB_INSCRITS_LIGNE_TROMBI_PDF];
							nbEtudiantInsere = 1;
							compteur = 0;
						}


						tabNom[compteur] = "" + inscrit.getLib_nom_pat_ind() + " \n" + inscrit.getLib_pr1_ind() + "\n";
						tabNum[compteur] = "" + inscrit.getCod_etu();

						compteur++;

						inscrit.setUrlphoto(photo.getUrlPhotoTrombinoscopePdf(inscrit.getCod_ind(), inscrit.getCod_etu()));
						Image photo = Image.getInstance(inscrit.getUrlphoto());
						photo.scaleAbsolute(85, 107);


						PdfPCell cell = new PdfPCell(photo);
						cell.cloneNonPositionParameters(border);
						table.addCell(cell);

						if (compteur == NB_INSCRITS_LIGNE_TROMBI_PDF) {
							for (int i = 0; i < NB_INSCRITS_LIGNE_TROMBI_PDF; i++) {
								Phrase ph = new Phrase(tabNom[i], normalbig);
								Phrase ph2 = new Phrase(tabNum[i], leger);
								Paragraph pinscrit = new Paragraph();
								pinscrit.add(ph);
								pinscrit.add(ph2);
								PdfPCell celltext = new PdfPCell(pinscrit);
								celltext.cloneNonPositionParameters(border);
								table.addCell(celltext);
							}
							compteur = 0;
						}

					}else{
						//LOG.info("STOP generation PDF");
						break boucleFor;
					}
				}
			}
			if(!stopGeneration){
				if (compteur > 0) {
					for (int i = compteur; i < NB_INSCRITS_LIGNE_TROMBI_PDF; i++) {
						PdfPCell cell = new PdfPCell();
						cell.cloneNonPositionParameters(border);
						table.addCell(cell);
					}

					for (int i = 0; i < compteur; i++) {
						Phrase ph = new Phrase(tabNom[i], normalbig);
						Phrase ph2 = new Phrase(tabNum[i], leger);
						Paragraph pinscrit = new Paragraph();
						pinscrit.add(ph);
						pinscrit.add(ph2);
						PdfPCell celltext = new PdfPCell(pinscrit);
						celltext.cloneNonPositionParameters(border);
						table.addCell(celltext);
					}

					for (int i = compteur; i < NB_INSCRITS_LIGNE_TROMBI_PDF; i++) {
						PdfPCell cell = new PdfPCell();
						cell.cloneNonPositionParameters(border);
						table.addCell(cell);
					}

				}

				document.add(table);
			}
		} catch (BadElementException e) {
			LOG.error(e);
		} catch (MalformedURLException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		} catch (DocumentException e) {
			LOG.error(e);
		}

		// step 6: fermeture du document.
		document.close();
		//on remet les urls pour affichage dans le navigateur.
		for (ArrayList<Inscrit> li : listeInscritsTrombi) {
			for (Inscrit inscrit : li) {
				inscrit.setUrlphoto(photo.getUrlPhoto(inscrit.getCod_ind(), inscrit.getCod_etu()));
			}
		}
	}



	/**
	 * configure le pdf.
	 * @param width
	 * @param height
	 * @param margin
	 * @return le codument configuré.
	 */
	private Document configureDocument(final float margin) {

		Document document = new Document();

		document.setPageSize(PageSize.A4.rotate());
		float marginPage = (margin / 2.54f) * 72f;
		document.setMargins(marginPage, marginPage, marginPage, marginPage);

		return document;
	}



	/**
	 * @return annee
	 */
	public String getAnnee() {
		return annee;
	}

	/**
	 * @param annee
	 */
	public void setAnnee(final String annee) {
		this.annee = annee;
	}

	/**
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * @return etape
	 */
	public boolean isEtape() {
		return etape;
	}

	/**
	 * @param etape
	 */
	public void setEtape(final boolean etape) {
		this.etape = etape;
	}

	/**
	 * @return libelle
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle
	 */
	public void setLibelle(final String libelle) {
		this.libelle = libelle;
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
	public void setListeInscrits(final ArrayList<Inscrit> listeInscrits) {
		this.listeInscrits = listeInscrits;
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
	 * @return session1
	 */
	public boolean isSession1() {
		return session1;
	}

	/**
	 * @param session1
	 */
	public void setSession1(final boolean session1) {
		this.session1 = session1;
	}

	/**
	 * @return session2
	 */
	public boolean isSession2() {
		return session2;
	}

	/**
	 * @param session2
	 */
	public void setSession2(final boolean session2) {
		this.session2 = session2;
	}

	/**
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * @return version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	/**
	 * @return traiteEtape
	 */
	public boolean isTraiteEtape() {
		if (type!=null && type.equals("Etape")) {
			traiteEtape = true;
		} else {
			traiteEtape = false;
		}
		return traiteEtape;
	}

	/**
	 * @param traiteEtape
	 */
	public void setTraiteEtape(final boolean traiteEtape) {
		this.traiteEtape = traiteEtape;
	}

	/**
	 * @return nbInscrits
	 */
	public int getNbInscrits() {
		nbInscrits = listeInscrits.size();
		return nbInscrits;
	}

	/**
	 * @param nbInscrits
	 */
	public void setNbInscrits(final int nbInscrits) {
		this.nbInscrits = nbInscrits;
	}

	/**
	 * @return listeInscritsTrombi
	 */
	public ArrayList<ArrayList<Inscrit>> getListeInscritsTrombi() {
		return listeInscritsTrombi;
	}

	/**
	 * @param listeInscritsTrombi
	 */
	public void setListeInscritsTrombi(
			final ArrayList<ArrayList<Inscrit>> listeInscritsTrombi) {
		this.listeInscritsTrombi = listeInscritsTrombi;
	}

	/**
	 * @return NB_INSCRITS_LIGNE_TROMBI_PDF
	 */
	public static int getNB_INSCRITS_LIGNE_TROMBI_PDF() {
		return NB_INSCRITS_LIGNE_TROMBI_PDF;
	}


	/**
	 * @return emailConverter
	 */
	public EmailConverterInterface getEmailConverter() {
		return emailConverter;
	}

	/**
	 * @param emailConverter
	 */
	public void setEmailConverter(final EmailConverterInterface emailConverter) {
		this.emailConverter = emailConverter;
	}

	/**
	 * @return existeInscrits
	 */
	public boolean isExisteInscrits() {
		existeInscrits = false;
		if (listeInscrits.size() > 0) {
			existeInscrits = true;
		}
		return existeInscrits;
	}

	/**
	 * @param existeInscrits
	 */
	public void setExisteInscrits(final boolean existeInscrits) {
		this.existeInscrits = existeInscrits;
	}

	/**
	 * @return afficheScroller
	 */
	public boolean isAfficheScroller() {
		afficheScroller = false;
		if ((listeInscrits.size() > config.getNbEtudiantsParPage()) && config.isListeScrollable()) {
			afficheScroller = true;
		}

		return afficheScroller;
	}

	/**
	 * @param afficheScroller
	 */
	public void setAfficheScroller(final boolean afficheScroller) {
		this.afficheScroller = afficheScroller;
	}

	/**
	 * @return config
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * @param config
	 */
	public void setConfig(final Config config) {
		this.config = config;
	}

	/**
	 * @return recherchecode
	 */
	public boolean isRecherchecode() {
		return recherchecode;
	}

	/**
	 * @param recherchecode
	 */
	public void setRecherchecode(final boolean recherchecode) {
		this.recherchecode = recherchecode;
	}
	/**
	 * @return parDiplomes
	 */
	public boolean isParDiplomes() {
		return parDiplomes;
	}
	/**
	 * @param parDiplomes
	 */
	public void setParDiplomes(final boolean parDiplomes) {
		this.parDiplomes = parDiplomes;
	}
	/**
	 * 
	 * @return parEtapes
	 */
	public boolean isParEtapes() {
		return parEtapes;
	}
	/**
	 * 
	 * @param parEtapes
	 */
	public void setParEtapes(boolean parEtapes) {
		this.parEtapes = parEtapes;
	}
	public boolean isDownload() {
		return download;
	}
	public void setDownload(boolean download) {
		this.download = download;
	}
	public boolean isPhotosValides() {
		return photosValides;
	}
	public void setPhotosValides(boolean photosValides) {
		this.photosValides = photosValides;
	}
	public IPhoto getPhoto() {
		return photo;
	}
	public void setPhoto(IPhoto photo) {
		this.photo = photo;
	}
	public boolean isAffichageTrombiDecoupe() {
		return affichageTrombiDecoupe;
	}
	public void setAffichageTrombiDecoupe(boolean affichageTrombiDecoupe) {
		this.affichageTrombiDecoupe = affichageTrombiDecoupe;
	}
	public boolean isAffichageTrombiDecoupePremierePage() {
		return affichageTrombiDecoupePremierePage;
	}
	public void setAffichageTrombiDecoupePremierePage(
			boolean affichageTrombiDecoupePremierePage) {
		this.affichageTrombiDecoupePremierePage = affichageTrombiDecoupePremierePage;
	}
	public boolean isAffichageTrombiDecoupeDernierePage() {
		return affichageTrombiDecoupeDernierePage;
	}
	public void setAffichageTrombiDecoupeDernierePage(
			boolean affichageTrombiDecoupeDernierePage) {
		this.affichageTrombiDecoupeDernierePage = affichageTrombiDecoupeDernierePage;
	}
	public int getPageVisualiseeTrombinoscope() {
		return pageVisualiseeTrombinoscope;
	}
	public void setPageVisualiseeTrombinoscope(int pageVisualiseeTrombinoscope) {
		this.pageVisualiseeTrombinoscope = pageVisualiseeTrombinoscope;
	}
	public int getNbPageTotalVisualiseeTrombinoscope() {
		return nbPageTotalVisualiseeTrombinoscope;
	}
	public void setNbPageTotalVisualiseeTrombinoscope(
			int nbPageTotalVisualiseeTrombinoscope) {
		this.nbPageTotalVisualiseeTrombinoscope = nbPageTotalVisualiseeTrombinoscope;
	}
	public int getNbInscritsPageTrombi() {
		return nbInscritsPageTrombi;
	}
	public void setNbInscritsPageTrombi(int nbInscritsPageTrombi) {
		this.nbInscritsPageTrombi = nbInscritsPageTrombi;
	}
	public boolean isStopGeneration() {
		return stopGeneration;
	}
	public void setStopGeneration(boolean stopGeneration) {
		this.stopGeneration = stopGeneration;
	}
	public boolean isRecherchecodegroupe() {
		return recherchecodegroupe;
	}
	public void setRecherchecodegroupe(boolean recherchecodegroupe) {
		this.recherchecodegroupe = recherchecodegroupe;
	}
	



}
