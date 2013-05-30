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
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;



import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.mondossierweb.domain.beans.Config;
import org.esupportail.mondossierweb.domain.beans.Etudiant;
import org.esupportail.mondossierweb.domain.beans.Resultat;
import org.esupportail.mondossierweb.web.navigation.View;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * controller de la vue du calendrier des examens de l'�tudiant.
 * @author Charlie Dubois
 */
public class NotesController extends AbstractContextAwareController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2609593235683113294L;

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(NotesController.class);

	/**
	 * marge.
	 */
	private static final float MARGE_PDF = 1.0f;

	/**
	 * outputstream size.
	 */
	private static final int OUTPUTSTREAM_SIZE = 1024;
	/**
	 * l'�cartement du pied de page (libell� de la promo et date d'�dition) des pdf.
	 */
	private static final int ECARTEMENT_PIED_PAGE_PDF = 120;
	/**
	 * nom de la page des diplomes (1er page de l'onglet Notes et R�sultat).
	 */
	private static final String PAGE_DES_DIPLOMES="diplome";
	/**
	 * l'etatCivilController.
	 */
	private EtatCivilController etatcivilcontroller;
	/**
	 * l'�tudiant dont on consulte le dossier.
	 */
	private Etudiant etudiant;
	/**
	 * vrai si on est en vue etudiant sur la page des notes aux diplomes quand on est sur la 
	 * page du d�tail des notes. Permet de g�rer le changement de vue dans la page des d�tails
	 */
	private boolean vueEtudiantNotesDiplomes;
	/**
	 * vrai si on est en vue etudiant. 
	 */
	private boolean vueEtudiant;
	/**
	 * la page en cours de visualisation (diplome ou detailEtape 
	 * correspondant respectivement � NOTES ou DETAIL_NOTES).
	 */
	private String page;
	/**
	 * l'index de l'�tape s�lectionn�e.
	 */
	private int selectedRow;


	/**
	 * le constructeur.
	 *
	 */
	public NotesController() {
		super();
		vueEtudiant = true;
		vueEtudiantNotesDiplomes=true;
		page = "";
		selectedRow = 0;
	}

	public String toString(){
		return "bean NotesController : codetu= "+etudiant.getCod_etu();
	}

	/**
	 * on entre dans la vue des notes aux diplomes et �preuves.
	 * @return la vue des notes aux diplomes et �preuves des examens.
	 */
	public String enter() {

		selectedRow = 0;

		//si c'est la 1er fois qu'on consulte les notes:
		if (etudiant.getDiplomes().size() < 1 && etudiant.getEtapes().size() < 1) {
			//c'est le 1er affichage des notes pour cet etudiant

			//si l'utilisateur est un enseignant, on se met en vue enseignant par d�faut
			if (etatcivilcontroller.getSessionController().isEnseignant()) {
				vueEtudiant = false;
				vueEtudiantNotesDiplomes=false;
			} else {
				vueEtudiant = true;
				vueEtudiantNotesDiplomes=true;
			}


		}else {
			//on avait deja visualise les notes aux diplomes
			//c'est comme si on avait clique sur retour
			return retour();

		}
		page = PAGE_DES_DIPLOMES;
		return View.NOTES;
	}

	/**
	 * on entre dans la vue des notes aux diplomes et Etapes.
	 * @return la vue des notes aux diplomes et Etapes.
	 */
	public String retour() {
		page = PAGE_DES_DIPLOMES;

		//on r�cup�re le type de l'utilisateur:
		SessionController session = (SessionController)  BeanUtils.getBean("sessionController");
		boolean utilisateurEnseignant = session.isEnseignant();

		//si l'utilisateur est un enseignant on doit recharger les notes aux diplomes car il peut avoir changer de 'vue'
		//si c'est un �tudiant on raffiche directement la page.
		if (utilisateurEnseignant) {
			if (vueEtudiant) {
				if (!vueEtudiantNotesDiplomes) {
					//on est pass� en vue Etudiant sur la page du d�tail des notes
					//il faut mettre � jour la page des notes aux diplomes.
					etudiant.renseigneNotesEtResultats();
				}
			} else {
				if (vueEtudiantNotesDiplomes) {
					//on est pass� en vue Enseignant sur la page du d�tail des notes
					//il faut mettre � jour la page des notes aux diplomes.
					etudiant.renseigneNotesEtResultatsVueEnseignant();
				}
			}
		} 

		return View.NOTES;
	}

	/**
	 * @return la page des notes et r�sultats dans la nouvelle vue.
	 */
	public String changerVue() {
		if(etudiant.getElementsPedagogiques()!=null)
			etudiant.getElementsPedagogiques().clear();

		//on change la vue:
		if (vueEtudiant) {
			vueEtudiant = false;
		} else {
			vueEtudiant = true;
		}
		//si on est sur la page du detail des notes:
		if (page.equals("detailEtape")) {
			if (vueEtudiant) {
				etudiant.renseigneNotesElpEpr(selectedRow);
			} else {
				etudiant.renseigneNotesElpEprVueEnseignant(selectedRow);
			}
			return View.NOTES_ETAPE;
		} 

		//on est dans la page des notes aux dipl�mes:
		if (vueEtudiant) {
			vueEtudiantNotesDiplomes = true;
			etudiant.renseigneNotesEtResultats();
		} else {
			vueEtudiantNotesDiplomes = false;
			etudiant.renseigneNotesEtResultatsVueEnseignant();
		}


		return View.NOTES;
	}

	/**
	 * @return la vue des notes compl�tes pour l'�tape s�lectionn�e
	 */
	public String preciserEtape() {

		FacesContext context = FacesContext.getCurrentInstance();
		String row = (String) context.getExternalContext().getRequestParameterMap().get("row");
		page = "detailEtape";
		if(row != null && !row.equals("")){
			selectedRow = new Integer(row);
			if (vueEtudiant) {
				etudiant.renseigneNotesElpEpr(selectedRow);
			} else {
				etudiant.renseigneNotesElpEprVueEnseignant(selectedRow);
			}

			return View.NOTES_ETAPE;
		}
		return null;
	}





	/**
	 * 
	 * @return le fichier pdf.
	 */
	public String exportPdf() {


		FacesContext facesContext = FacesContext.getCurrentInstance();

		ExternalContext externalContext = facesContext.getExternalContext();



		Document document = configureDocument( MARGE_PDF);

		try {

			ByteArrayOutputStream baosPDF = new ByteArrayOutputStream(OUTPUTSTREAM_SIZE);
			PdfWriter docWriter = PdfWriter.getInstance(document, baosPDF);
			docWriter.setStrictImageSequence(true);
			
			// ajout du filigrane
			Config config = (Config)  BeanUtils.getBean("config");
			if(config.isInsertionFiligranePdfNotes()){
				docWriter.setPageEvent(new Watermark());
			}
			String titre = "";
			if (page.equals("detailEtape")) {
				creerPdfDetail(document);
				titre = getString("PDF.TITLE.DETAIL") + " " + etudiant.getNom().replace('.', ' ').replace(' ', '_') + ".pdf";
			} else {
				creerPdfGlobal(document);
				titre = getString("PDF.TITLE.NOTES") + " " + etudiant.getNom().replace('.', ' ').replace(' ', '_') + ".pdf";
			}
			docWriter.close();
			byte[] bytes = baosPDF.toByteArray();

			String typeDownload = "application/force-download";

			long id = DownloadUtils.setDownload(bytes, titre, typeDownload, "attachment");

			String url = DownloadUtils.getDownloadUrl(id);

			externalContext.redirect(url);

			bytes = null;

		} catch (DownloadException e) {
			LOG.error(e);
		} catch (DocumentException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
		facesContext.responseComplete();


		return null;

	}

	/**
	 * 
	 * @param document pdf
	 */
	public void creerPdfDetail(final Document document) {



		//configuration des fonts
		Font normal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
		Font normalbig = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD);
		Font legerita = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC);
		Font headerbig = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD);
		Font header = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);

		//pieds de pages:
		Date d = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(d);
		//alignement des libell�s du pied de page:
		String partie1 = getString("PDF.NOTES.DETAIL"); 
		String partie2 = getString("PDF.EDITION.DATE")+ " : " + date;
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

		//creation du pied de page:
		Phrase phra = new Phrase(partie1 + " -" + getString("PDF.PAGE"), legerita);
		Phrase phra2 = new Phrase("- "+partie2, legerita);
		HeaderFooter hf = new HeaderFooter(phra, phra2);
		hf.setAlignment(HeaderFooter.ALIGN_CENTER);
		document.setFooter(hf);	 
		document.setFooter(hf);

		//ouverte du document.
		document.open();
		try {
			//ajout image test
			Config config = (Config)  BeanUtils.getBean("config");
			if (config.getLogoUniversitePdf() != null && !config.getLogoUniversitePdf().equals("")){
				Image image1 = Image.getInstance(config.getLogoUniversitePdf());
				float scaleRatio = 40 / image1.height();
				float newWidth=scaleRatio * image1.width();
				image1.scaleAbsolute(newWidth, 40);
				image1.setAbsolutePosition(800 - newWidth, 528);
				document.add(image1);
			}



			//nouveau paragraphe
			Paragraph p = new Paragraph(getString("PDF.TITLE.NOTES").toUpperCase(getLocale()) + "\n\n", headerbig);
			p.indentationLeft();
			document.add(p);

			if (etudiant.getNom() != null) {
				Paragraph p0 = new Paragraph(etudiant.getNom(), normal);
				p0.indentationLeft();
				document.add(p0);
			}
			if (etudiant.getCod_etu() != null) {
				Paragraph p01 = new Paragraph(getString("PDF.FOLDER")+ " : " + etudiant.getCod_etu(), normal);
				p01.indentationLeft();
				document.add(p01);
			}
			if (etudiant.getCod_nne() != null) {
				Paragraph p02 = new Paragraph(getString("PDF.NNE")+ " : " + etudiant.getCod_nne(), normal);
				p02.indentationLeft();
				document.add(p02);
			}
			if (etudiant.getEmail() != null) {
				Paragraph p03 = new Paragraph(getString("PDF.MAIL") + " : " + etudiant.getEmail(), normal);
				p03.indentationLeft();
				document.add(p03);
			}
		
			Paragraph p03 = new Paragraph(getString("PDF.EDITION.DATE") + " : " + date, normal);
			p03.indentationLeft();
			document.add(p03);
			document.add(new Paragraph("\n"));

			//Partie des notes
			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(98);
			PdfPCell cell = new PdfPCell(new Paragraph(getString("PDF.ELEMENTS.EPREUVES").toUpperCase(getLocale()) + " ", header));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setBackgroundColor(new Color(153, 153, 255));
			table.addCell(cell);

			PdfPTable table2; 
			
			
			
			
			if((!config.isAffRangEtudiant() && !etudiant.isAfficherRangElpEpr())&& !config.isAffECTSEtudiant()){
				//NI isAffRangEtudiant  NI isAffECTSEtudiant
				table2= new PdfPTable(7);
				table2.setWidthPercentage(98);
				int [] tabWidth = {26,35,110,25,25,25,25};
				table2.setWidths(tabWidth);
			}else{
				if(((config.isAffRangEtudiant() || etudiant.isAfficherRangElpEpr()) && !config.isAffECTSEtudiant()) ||
						((!config.isAffRangEtudiant()&& !etudiant.isAfficherRangElpEpr()) && config.isAffECTSEtudiant())){
					//isAffRangEtudiant  OU isAffECTSEtudiant
					table2= new PdfPTable(8);
					table2.setWidthPercentage(98);
					int [] tabWidth = {26,33,110,22,22,22,22,15};
					table2.setWidths(tabWidth);
				}else{
					//isAffRangEtudiant  ET isAffECTSEtudiant
					table2= new PdfPTable(9);
					table2.setWidthPercentage(98);
					int [] tabWidth = {26,33,110,22,22,22,22,15,15};
					table2.setWidths(tabWidth);
				}
			}
			

			Paragraph p1 = new Paragraph(getString("PDF.YEAR"),normalbig);
			Paragraph p2 = new Paragraph(getString("PDF.CODE"),normalbig);
			Paragraph p3 = new Paragraph(getString("PDF.LABEL"),normalbig);
			Paragraph parRang = new Paragraph(getString("PDF.RANK"),normalbig);
			Paragraph parEcts = new Paragraph(getString("PDF.ECTS"),normalbig);

			PdfPCell ct4 = new PdfPCell(new Paragraph(getString("PDF.SESSION") + " 1", normalbig));
			PdfPCell ct5 = new PdfPCell(new Paragraph(getString("PDF.RESULT"), normalbig));
			PdfPCell ct6 = new PdfPCell(new Paragraph(getString("PDF.SESSION") + " 2", normalbig));
			PdfPCell ct7 = new PdfPCell(new Paragraph(getString("PDF.RESULT"), normalbig));

			PdfPCell ct1 = new PdfPCell(p1);
			PdfPCell ct2 = new PdfPCell(p2);
			PdfPCell ct3 = new PdfPCell(p3);
			PdfPCell cellRang = new PdfPCell(parRang);
			PdfPCell cellEcts = new PdfPCell(parEcts);

			ct1.setBorder(Rectangle.BOTTOM); ct1.setBorderColorBottom(Color.black);
			ct2.setBorder(Rectangle.BOTTOM); ct2.setBorderColorBottom(Color.black);
			ct3.setBorder(Rectangle.BOTTOM); ct3.setBorderColorBottom(Color.black);
			ct4.setBorder(Rectangle.BOTTOM); ct4.setBorderColorBottom(Color.black);
			ct5.setBorder(Rectangle.BOTTOM); ct5.setBorderColorBottom(Color.black);
			ct6.setBorder(Rectangle.BOTTOM); ct6.setBorderColorBottom(Color.black);
			ct7.setBorder(Rectangle.BOTTOM); ct7.setBorderColorBottom(Color.black);
			cellRang.setBorder(Rectangle.BOTTOM); cellRang.setBorderColorBottom(Color.black);
			cellEcts.setBorder(Rectangle.BOTTOM); cellEcts.setBorderColorBottom(Color.black);


			table2.addCell(ct1);
			table2.addCell(ct2);
			table2.addCell(ct3);
			table2.addCell(ct4);
			table2.addCell(ct5);
			table2.addCell(ct6);
			table2.addCell(ct7);
			if((config.isAffRangEtudiant() || etudiant.isAfficherRangElpEpr())){
				table2.addCell(cellRang);
			}
			if(config.isAffECTSEtudiant()){
				table2.addCell(cellEcts);
			}


			for (int i = 0; i < etudiant.getElementsPedagogiques().size(); i++) {
				String annee = etudiant.getElementsPedagogiques().get(i).getAnnee().replaceAll(getString("PDF.REPLACE.FICM"), "");
				//Inutile a partir de la modification du 20/02/2012 pour les WS httpInvoker
				//annee = annee.replaceAll(getString("PDF.REPLACE.EPREUVE"), "");
				Paragraph pa = new Paragraph(annee, normal);
				PdfPCell celltext = new PdfPCell(pa);
				celltext.setBorder(Rectangle.NO_BORDER);
				table2.addCell(celltext);

				Paragraph pa2 = new Paragraph(etudiant.getElementsPedagogiques().get(i).getCode(), normal);
				PdfPCell celltext2 = new PdfPCell(pa2);
				celltext2.setBorder(Rectangle.NO_BORDER);
				table2.addCell(celltext2);

				Paragraph pa3 = new Paragraph(etudiant.getElementsPedagogiques().get(i).getLibelle().replaceAll(getString("PDF.REPLACE.NBSP"), " "), normal);
				PdfPCell celltext3 = new PdfPCell(pa3);
				celltext3.setBorder(Rectangle.NO_BORDER);
				table2.addCell(celltext3);


				Paragraph pa5 = new Paragraph(etudiant.getElementsPedagogiques().get(i).getNote1(), normal);
				PdfPCell celltext5 = new PdfPCell(pa5);
				celltext5.setBorder(Rectangle.NO_BORDER);
				table2.addCell(celltext5);


				Paragraph pa6 = new Paragraph(etudiant.getElementsPedagogiques().get(i).getRes1(), normal);
				PdfPCell celltext6 = new PdfPCell(pa6);
				celltext6.setBorder(Rectangle.NO_BORDER);
				table2.addCell(celltext6);


				Paragraph pa7 = new Paragraph(etudiant.getElementsPedagogiques().get(i).getNote2(), normal);
				PdfPCell celltext7 = new PdfPCell(pa7);
				celltext7.setBorder(Rectangle.NO_BORDER);
				table2.addCell(celltext7);

				Paragraph pa8 = new Paragraph(etudiant.getElementsPedagogiques().get(i).getRes2(), normal);
				PdfPCell celltext8 = new PdfPCell(pa8);
				celltext8.setBorder(Rectangle.NO_BORDER);
				table2.addCell(celltext8);

				
				if((config.isAffRangEtudiant() || etudiant.isAfficherRangElpEpr())){
					Paragraph parRang2 = new Paragraph(etudiant.getElementsPedagogiques().get(i).getRang(), normal);
					PdfPCell cellRang2 = new PdfPCell(parRang2);
					cellRang2.setBorder(Rectangle.NO_BORDER);
					table2.addCell(cellRang2);
				}
				
				if(config.isAffECTSEtudiant()){
					Paragraph parEcts2 = new Paragraph(etudiant.getElementsPedagogiques().get(i).getEcts(), normal);
					PdfPCell cellEcts2 = new PdfPCell(parEcts2);
					cellEcts2.setBorder(Rectangle.NO_BORDER);
					table2.addCell(cellEcts2);
				}

			}


			document.add(table);
			document.add(table2);
			document.add(new Paragraph("\n"));


			//Partie QUESTIONS
			if(etudiant.isSignificationResultatsUtilisee()) {
				PdfPTable tablequestions = new PdfPTable(1);
				tablequestions.setWidthPercentage(98);
				PdfPCell cellquestions = new PdfPCell(new Paragraph(getString("PDF.QUESTIONS").toUpperCase(getLocale()) + " ", header));
				cellquestions.setBorder(Rectangle.NO_BORDER);
				cellquestions.setBackgroundColor(new Color(153, 153, 255));
				tablequestions.addCell(cellquestions);

				PdfPTable tablequestions2 = new PdfPTable(1);
				tablequestions2.setWidthPercentage(98);
				PdfPCell cellquestions2 = new PdfPCell(new Paragraph(getString("PDF.CODE.RESULT.MEANING") + " : \n" + etudiant.getGrilleSignficationResultatsPdf(), normal));
				cellquestions2.setBorder(Rectangle.NO_BORDER);
				tablequestions2.addCell(cellquestions2);

				document.add(tablequestions);
				document.add(tablequestions2);



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




	}

	/**
	 * 
	 * @param document pdf
	 */
	public void creerPdfGlobal(final Document document) {



		//configuration des fonts
		Font normal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
		Font normalbig = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD);
		Font legerita = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC);
		Font headerbig = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD);
		Font header = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);

		//pieds de pages:
		Date d = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(d);
		//alignement des libell�s du pied de page:
		String partie1 = getString("PDF.TITLE.NOTES"); 
		String partie2 = getString("PDF.EDITION.DATE")+ " : " + date;
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

		//creation du pied de page:
		Phrase phra = new Phrase(partie1 + " -" + getString("PDF.PAGE"), legerita);
		Phrase phra2 = new Phrase("- "+partie2, legerita);
		HeaderFooter hf = new HeaderFooter(phra, phra2);
		hf.setAlignment(HeaderFooter.ALIGN_CENTER);
		document.setFooter(hf);	 


		//ouverte du document.
		document.open();
		try {
			//ajout image test
			Config config = (Config)  BeanUtils.getBean("config");
			if (config.getLogoUniversitePdf() != null && !config.getLogoUniversitePdf().equals("")){
				Image image1 = Image.getInstance(config.getLogoUniversitePdf());
				float scaleRatio = 40 / image1.height();
				float newWidth=scaleRatio * image1.width();
				image1.scaleAbsolute(newWidth, 40);
				image1.setAbsolutePosition(800 - newWidth, 528);
				document.add(image1);
			}



			//nouveau paragraphe
			Paragraph p = new Paragraph(getString("PDF.TITLE.NOTES").toUpperCase(getLocale()) + "\n\n", headerbig);
			p.indentationLeft();
			document.add(p);

			if (etudiant.getNom() != null) {
				Paragraph p0 = new Paragraph(etudiant.getNom(), normal);
				p0.indentationLeft();
				document.add(p0);
			}
			if (etudiant.getCod_etu() != null) {
				Paragraph p01 = new Paragraph(getString("PDF.FOLDER") + " : " + etudiant.getCod_etu(), normal);
				p01.indentationLeft();
				document.add(p01);
			}
			if (etudiant.getCod_nne() != null) {
				Paragraph p02 = new Paragraph(getString("PDF.NNE") + " : " + etudiant.getCod_nne(), normal);
				p02.indentationLeft();
				document.add(p02);
			}
			if (etudiant.getEmail() != null) {
				Paragraph p03 = new Paragraph(getString("PDF.MAIL") +" : " + etudiant.getEmail(), normal);
				p03.indentationLeft();
				document.add(p03);
			}
		
			Paragraph p03 = new Paragraph(getString("PDF.EDITION.DATE") + " : " + date, normal);
			p03.indentationLeft();
			document.add(p03);
			document.add(new Paragraph("\n"));

			//Partie DIPLOMES
			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(98);
			PdfPCell cell = new PdfPCell(new Paragraph(getString("PDF.DIPLOMES").toUpperCase(getLocale())+ " ", header));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setBackgroundColor(new Color(153, 153, 255));
			table.addCell(cell);

			PdfPTable table2;
			
			
			//if(!config.isAffRangEtudiant()){
			if(!etudiant.isAfficherRang()){
				table2= new PdfPTable(4);
			}else{
				table2 = new PdfPTable(5);
			}

			table2.setWidthPercentage(98);

			int tailleColonneLib = 110;
			if(config.isAffMentionEtudiant())
				tailleColonneLib = 90;
			
			//if(!config.isAffRangEtudiant()){
			if(!etudiant.isAfficherRang()){
					int [] tabWidth = {26,35,tailleColonneLib,70};
					table2.setWidths(tabWidth);
			}else{
					int [] tabWidth = {26,35,tailleColonneLib - 5,70,15};
					table2.setWidths(tabWidth);
			}



			Paragraph p1 = new Paragraph(getString("PDF.YEAR"),normalbig);
			Paragraph p2 = new Paragraph(getString("PDF.CODE.VERS"),normalbig);
			Paragraph p3 = new Paragraph(getString("PDF.DIPLOME"),normalbig);
	

			PdfPCell ct1 = new PdfPCell(p1);
			PdfPCell ct2 = new PdfPCell(p2);
			PdfPCell ct3 = new PdfPCell(p3);

			ct1.setBorder(Rectangle.BOTTOM); ct1.setBorderColorBottom(Color.black);
			ct2.setBorder(Rectangle.BOTTOM); ct2.setBorderColorBottom(Color.black);
			ct3.setBorder(Rectangle.BOTTOM); ct3.setBorderColorBottom(Color.black);
			


			table2.addCell(ct1);
			table2.addCell(ct2);
			table2.addCell(ct3);

			PdfPTable table21;
			if(!config.isAffMentionEtudiant()){
				table21 = new PdfPTable(3);
				int [] tabWidth21 = {25,20,25};
				table21.setWidths(tabWidth21);
			}else{
				table21 = new PdfPTable(4);
				int [] tabWidth21 = {25,20,25,20};
				table21.setWidths(tabWidth21);
			}

			PdfPCell ct4 = new PdfPCell(new Paragraph(getString("PDF.SESSION"), normalbig));
			PdfPCell ct5 = new PdfPCell(new Paragraph(getString("PDF.NOTE"), normalbig));
			PdfPCell ct6 = new PdfPCell(new Paragraph(getString("PDF.RESULT"), normalbig));
			PdfPCell ctmention = new PdfPCell(new Paragraph(getString("PDF.MENTION"), normalbig));
			
			
			
			ct4.setBorder(Rectangle.BOTTOM); ct4.setBorderColorBottom(Color.black);
			ct5.setBorder(Rectangle.BOTTOM); ct5.setBorderColorBottom(Color.black);
			ct6.setBorder(Rectangle.BOTTOM); ct6.setBorderColorBottom(Color.black);
			ctmention.setBorder(Rectangle.BOTTOM); ctmention.setBorderColorBottom(Color.black);
			
			

			table21.addCell(ct4);
			table21.addCell(ct5);
			table21.addCell(ct6);
			if(config.isAffMentionEtudiant()){
				table21.addCell(ctmention);
			}

			PdfPCell ct7 = new PdfPCell(table21);
			ct7.setBorder(Rectangle.BOTTOM); 
			table2.addCell(ct7);
			
			PdfPCell ctrang  = new PdfPCell(new Paragraph(getString("PDF.RANK"),normalbig));
			ctrang.setBorder(Rectangle.BOTTOM); ctrang.setBorderColorBottom(Color.black);

			//if(config.isAffRangEtudiant()){
			if(etudiant.isAfficherRang()){
				table2.addCell(ctrang);
			}


			for (int i = 0; i < etudiant.getDiplomes().size(); i++) {
				Paragraph pa = new Paragraph(etudiant.getDiplomes().get(i).getAnnee(), normal);
				PdfPCell celltext = new PdfPCell(pa);
				celltext.setBorder(Rectangle.NO_BORDER);

				Paragraph pa2 = new Paragraph(etudiant.getDiplomes().get(i).getCod_dip()+ "/" + etudiant.getDiplomes().get(i).getCod_vrs_vdi(), normal);
				PdfPCell celltext2 = new PdfPCell(pa2);
				celltext2.setBorder(Rectangle.NO_BORDER);

				Paragraph pa3 = new Paragraph(etudiant.getDiplomes().get(i).getLib_web_vdi(), normal);
				PdfPCell celltext3 = new PdfPCell(pa3);
				celltext3.setBorder(Rectangle.NO_BORDER);

				Paragraph parang = new Paragraph(etudiant.getDiplomes().get(i).getRang(), normal);
				PdfPCell cellrang = new PdfPCell(parang);
				cellrang.setBorder(Rectangle.NO_BORDER);
				
				PdfPCell cellvide = new PdfPCell();
				cellvide.setBorder(Rectangle.NO_BORDER);

				table2.addCell(celltext);
				table2.addCell(celltext2);
				table2.addCell(celltext3);

				PdfPTable table3;
				if(!config.isAffMentionEtudiant()){
					table3 = new PdfPTable(3);
					int [] tabWidth2 = {25,20,25};
					table3.setWidths(tabWidth2);
				}else{
					table3 = new PdfPTable(4);
					int [] tabWidth2 = {25,20,25,8};
					table3.setWidths(tabWidth2);
				}
				
				int j = 0;
				List<Resultat> lres = etudiant.getDiplomes().get(i).getResultats();
				while (j < lres.size()) {

					Paragraph pa5 = new Paragraph(lres.get(j).getSession(), normal);
					PdfPCell celltext5 = new PdfPCell(pa5);
					celltext5.setBorder(Rectangle.NO_BORDER);
					table3.addCell(celltext5);

					if (lres.get(j).getNote() != null) {
						Paragraph pa6 = new Paragraph(lres.get(j).getNote().toString(), normal);
						PdfPCell celltext6 = new PdfPCell(pa6);
						celltext6.setBorder(Rectangle.NO_BORDER);
						table3.addCell(celltext6);
					} else {
						Paragraph pa6 = new Paragraph("", normal);
						PdfPCell celltext6 = new PdfPCell(pa6);
						celltext6.setBorder(Rectangle.NO_BORDER);
						table3.addCell(celltext6);
					}

					Paragraph pa7 = new Paragraph(lres.get(j).getAdmission(), normal);
					PdfPCell celltext7 = new PdfPCell(pa7);
					celltext7.setBorder(Rectangle.NO_BORDER);
					table3.addCell(celltext7);
					
					if(config.isAffMentionEtudiant()){
						Paragraph pa8 = new Paragraph(lres.get(j).getCodMention(), normal);
						PdfPCell celltext8 = new PdfPCell(pa8);
						celltext8.setBorder(Rectangle.NO_BORDER);
						table3.addCell(celltext8);
					}
					

					j++;
				}
				
				PdfPCell celltext4 = new PdfPCell(table3);
				celltext4.setBorder(Rectangle.NO_BORDER);
				table2.addCell(celltext4);
				
				//if(config.isAffRangEtudiant()){
				if(etudiant.getDiplomes().get(i).isAfficherRang()){
					table2.addCell(cellrang);
				}else{
					//On insere une cellule vide si on affiche pas ce rang, alors que la colonne rang fait partie de la table
					if(etudiant.isAfficherRang()){
						table2.addCell(cellvide);
					}
				}
				

			}



			document.add(table);
			document.add(table2);
			document.add(new Paragraph("\n"));



			//Partie ETAPES
			PdfPTable tabletape = new PdfPTable(1);
			tabletape.setWidthPercentage(98);
			PdfPCell celletape = new PdfPCell(new Paragraph(getString("PDF.STEPS").toUpperCase(getLocale()), header));
			celletape.setBorder(Rectangle.NO_BORDER);
			celletape.setBackgroundColor(new Color(153, 153, 255));
			tabletape.addCell(celletape);

			PdfPTable tabletape2;
			
			//if(!config.isAffRangEtudiant()){
			if(!etudiant.isAfficherRang()){
				tabletape2= new PdfPTable(4);
				tabletape2.setWidthPercentage(98);
				int [] tabWidthetape = {26,35,tailleColonneLib,70};
				tabletape2.setWidths(tabWidthetape);
			}else{
				tabletape2= new PdfPTable(5);
				tabletape2.setWidthPercentage(98);
				int [] tabWidthetape = {26,35,tailleColonneLib - 5 ,70,15};
				tabletape2.setWidths(tabWidthetape);
			}


			PdfPCell ct3etape = new PdfPCell(new Paragraph(getString("PDF.STEP"),normalbig));
			ct3etape.setBorder(Rectangle.BOTTOM); ct3etape.setBorderColorBottom(Color.black);

			tabletape2.addCell(ct1);
			tabletape2.addCell(ct2);
			tabletape2.addCell(ct3etape);

			tabletape2.addCell(ct7);

			//if(!config.isAffRangEtudiant()){
			if(etudiant.isAfficherRang()){
				tabletape2.addCell(ctrang);
			}

			for (int i = 0; i < etudiant.getEtapes().size(); i++) {
				Paragraph pa = new Paragraph(etudiant.getEtapes().get(i).getAnnee(), normal);
				PdfPCell celltext = new PdfPCell(pa);
				celltext.setBorder(Rectangle.NO_BORDER);
				tabletape2.addCell(celltext);

				Paragraph pa2 = new Paragraph(etudiant.getEtapes().get(i).getCode()+ "/" + etudiant.getEtapes().get(i).getVersion(), normal);
				PdfPCell celltext2 = new PdfPCell(pa2);
				celltext2.setBorder(Rectangle.NO_BORDER);
				tabletape2.addCell(celltext2);

				Paragraph pa3 = new Paragraph(etudiant.getEtapes().get(i).getLibelle(), normal);
				PdfPCell celltext3 = new PdfPCell(pa3);
				celltext3.setBorder(Rectangle.NO_BORDER);
				tabletape2.addCell(celltext3);

				Paragraph parEtapeRang = new Paragraph(etudiant.getEtapes().get(i).getRang(), normal);
				PdfPCell cellEtapeRang = new PdfPCell(parEtapeRang);
				cellEtapeRang.setBorder(Rectangle.NO_BORDER);

				PdfPCell cellvide = new PdfPCell();
				cellvide.setBorder(Rectangle.NO_BORDER);

				PdfPTable table3; 
				
				if(!config.isAffMentionEtudiant()){
					table3= new PdfPTable(3);
					int [] tabWidth2 = {25,20,25};
					table3.setWidths(tabWidth2);
				}else{
					table3= new PdfPTable(4);
					int [] tabWidth2 = {25,20,25,8};
					table3.setWidths(tabWidth2);
				}
				
				int j = 0;
				List<Resultat> lres = etudiant.getEtapes().get(i).getResultats();
				while (j < lres.size()) {

					Paragraph pa5 = new Paragraph(lres.get(j).getSession(), normal);
					PdfPCell celltext5 = new PdfPCell(pa5);
					celltext5.setBorder(Rectangle.NO_BORDER);
					table3.addCell(celltext5);

					if (lres.get(j).getNote() != null) {
						Paragraph pa6 = new Paragraph(lres.get(j).getNote().toString(), normal);
						PdfPCell celltext6 = new PdfPCell(pa6);
						celltext6.setBorder(Rectangle.NO_BORDER);
						table3.addCell(celltext6);
					} else {
						Paragraph pa6 = new Paragraph("", normal);
						PdfPCell celltext6 = new PdfPCell(pa6);
						celltext6.setBorder(Rectangle.NO_BORDER);
						table3.addCell(celltext6);
					}

					Paragraph pa7 = new Paragraph(lres.get(j).getAdmission(), normal);
					PdfPCell celltext7 = new PdfPCell(pa7);
					celltext7.setBorder(Rectangle.NO_BORDER);
					table3.addCell(celltext7);
					
					if(config.isAffMentionEtudiant()){
						Paragraph pa8 = new Paragraph(lres.get(j).getCodMention(), normal);
						PdfPCell celltext8 = new PdfPCell(pa8);
						celltext8.setBorder(Rectangle.NO_BORDER);
						table3.addCell(celltext8);
					}

					j++;
				}
				PdfPCell celltext4 = new PdfPCell(table3);
				celltext4.setBorder(Rectangle.NO_BORDER);
				tabletape2.addCell(celltext4);

				//if(config.isAffRangEtudiant()){
				if(etudiant.getEtapes().get(i).isAfficherRang()){
					tabletape2.addCell(cellEtapeRang);
				}else{
					if(etudiant.isAfficherRang()){
						tabletape2.addCell(cellvide);
					}
				}

			}


			document.add(tabletape);
			document.add(tabletape2);
			document.add(new Paragraph("\n"));


			//Partie QUESTIONS
			if (etudiant.isSignificationResultatsUtilisee()) {
				PdfPTable tablequestions = new PdfPTable(1);
				tablequestions.setWidthPercentage(98);
				PdfPCell cellquestions = new PdfPCell(new Paragraph(getString("PDF.QUESTIONS")+ " ", header));
				cellquestions.setBorder(Rectangle.NO_BORDER);
				cellquestions.setBackgroundColor(new Color(153, 153, 255));
				tablequestions.addCell(cellquestions);

				PdfPTable tablequestions2 = new PdfPTable(1);
				tablequestions2.setWidthPercentage(98);
				PdfPCell cellquestions2 = new PdfPCell(new Paragraph(getString("PDF.CODE.RESULT.MEANING") + " : \n" + etudiant.getGrilleSignficationResultatsPdf(), normal));
				cellquestions2.setBorder(Rectangle.NO_BORDER);
				tablequestions2.addCell(cellquestions2);

				document.add(tablequestions);
				document.add(tablequestions2);
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




	}





	/**
	 * configure le document pdf.
	 * @param width
	 * @param height
	 * @param margin
	 * @return doc
	 */
	private Document configureDocument(final float margin) {

		Document document = new Document();

		document.setPageSize(PageSize.A4.rotate());
		float marginPage = (margin / 2.54f) * 72f;
		document.setMargins(marginPage, marginPage, marginPage, marginPage);

		return document;
	}


	/**
	 * @return etatcivilcontroller.
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


	/**
	 * @return vueEtudiant
	 */
	public boolean isVueEtudiant() {
		return vueEtudiant;
	}


	/**
	 * @param vueEtudiant
	 */
	public void setVueEtudiant(final boolean vueEtudiant) {
		this.vueEtudiant = vueEtudiant;
	}


	/**
	 * @return page
	 */
	public String getPage() {
		return page;
	}


	/**
	 * @param page
	 */
	public void setPage(final String page) {
		this.page = page;
	}


	/**
	 * @return selectedRow
	 */
	public int getSelectedRow() {
		return selectedRow;
	}


	/**
	 * @param selectedRow
	 */
	public void setSelectedRow(final int selectedRow) {
		this.selectedRow = selectedRow;
	}



		/**
		 * Inner class to add a watermark to every page.
		 */
		class Watermark extends PdfPageEventHelper {
			
			/** Default watermark font */
			Font FONT = new Font(5, 52, Font.BOLD, new GrayColor(0.75f));
			@Override
			public void onEndPage(PdfWriter writer, Document document) {
				ColumnText.showTextAligned(
						writer.getDirectContentUnder(),
						Element.ALIGN_CENTER,
						new Phrase(getString("PDF.FILIGRANE").toUpperCase(), FONT),
						421, 297.5f,
						writer.getPageNumber() % 2 == 1 ? 45 : -45);
			}
		}



}
