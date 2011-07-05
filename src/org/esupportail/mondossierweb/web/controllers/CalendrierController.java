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


import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.ActionRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.mondossierweb.domain.beans.Config;
import org.esupportail.mondossierweb.domain.beans.Etudiant;
import org.esupportail.mondossierweb.web.navigation.View;

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
 * controller de la vue du calendrier des examens de l'étudiant.
 * @author Charlie Dubois
 */
public class CalendrierController extends AbstractContextAwareController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8444253411228061206L;
	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(CalendrierController.class);
	/**
	 * outputstream size.
	 */
	private static final int OUTPUTSTREAM_SIZE = 1024;
	/**
	 * marge.
	 */
	private static final float MARGE_PDF = 1.0f;
	/**
	 * l'écartement du pied de page (libellé de la promo et date d'édition) des pdf.
	 */
	private static final int ECARTEMENT_PIED_PAGE_PDF = 120;
	/**
	 * nom de la page des diplomes (1er page de l'onglet Notes et Résultat).
	 */
	private static final String PAGE_DES_DIPLOMES="diplome";
	/**
	 * l'etatCivilController.
	 */
	private EtatCivilController etatcivilcontroller;
	/**
	 * l'étudiant dont on consulte le dossier.
	 */
	private Etudiant etudiant;
	/**
	 * la configuration de l'application.
	 */
	private Config config;
	


	/**
	 * le constructeur.
	 *
	 */
	public CalendrierController() {
		super();
	}


	/**
	 * on entre dans la vue du calendrier des examens.
	 * @return la vue du calendrier des examens.
	 */
	public String enter() {
		if(!config.isAffCalendrierEtape()){
			return View.CALENDRIER;
		}
		return View.CALENDRIER_ETAPE;
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
				PdfWriter docWriter = null;
				docWriter = PdfWriter.getInstance(document, baosPDF);
				docWriter.setStrictImageSequence(true);
				String titre = getString("PDF.TITLE.CALENDRIER")+" " + etudiant.getNom().replace('.', ' ').replace(' ', '_') + ".pdf";


				creerPdfCalendrier(document);

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
	public void creerPdfCalendrier(final Document document) {



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
		//alignement des libellés du pied de page:
		String partie1 = getString("PDF.TITLE.CALENDRIER");
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
		Phrase phra = new Phrase(partie1 + " -"+getString("PDF.PAGE"), legerita);
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
			Paragraph p = new Paragraph(getString("PDF.TITLE.CALENDRIER").toUpperCase()+"\n\n", headerbig);
			p.indentationLeft();
			document.add(p);

			if (etudiant.getNom() != null) {
				Paragraph p0 = new Paragraph(etudiant.getNom(), normal);
				p0.indentationLeft();
				document.add(p0);
			}
			if (etudiant.getCod_etu() != null) {
				Paragraph p01 = new Paragraph(getString("PDF.FOLDER")+" : " + etudiant.getCod_etu(), normal);
				p01.indentationLeft();
				document.add(p01);
			}
			if (etudiant.getCod_nne() != null) {
				Paragraph p02 = new Paragraph(getString("PDF.NNE")+" : " + etudiant.getCod_nne(), normal);
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




			//Partie Calendrier
			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(98);
			PdfPCell cell = new PdfPCell(new Paragraph(getString("PDF.SUBTITLE.CALENDRIER").toUpperCase()+" ", header));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setBackgroundColor(new Color(153, 153, 255));
			table.addCell(cell);

			if(etudiant.getCalendrier()!=null && etudiant.getCalendrier().size()>0){

				PdfPTable table2 = new PdfPTable(7);

				table2.setWidthPercentage(98);
				int [] tabWidth = {15,10,10,40,30,10,60};
				table2.setWidths(tabWidth);

				Paragraph p1 = new Paragraph(getString("PDF.DATE"),normalbig);
				Paragraph p2 = new Paragraph(getString("PDF.HEURE"),normalbig);
				Paragraph p3 = new Paragraph(getString("PDF.DUREE"),normalbig);
				Paragraph p4 = new Paragraph(getString("PDF.BATIMENT"),normalbig);
				Paragraph p5 = new Paragraph(getString("PDF.SALLE"),normalbig);
				Paragraph p6 = new Paragraph(getString("PDF.PLACE"),normalbig);
				Paragraph p7 = new Paragraph(getString("PDF.EXAMEN"),normalbig);

				PdfPCell ct1 = new PdfPCell(p1);
				PdfPCell ct2 = new PdfPCell(p2);
				PdfPCell ct3 = new PdfPCell(p3);
				PdfPCell ct4 = new PdfPCell(p4);
				PdfPCell ct5 = new PdfPCell(p5);
				PdfPCell ct6 = new PdfPCell(p6);
				PdfPCell ct7 = new PdfPCell(p7);

				ct1.setBorder(Rectangle.BOTTOM); ct1.setBorderColorBottom(Color.black);
				ct2.setBorder(Rectangle.BOTTOM); ct2.setBorderColorBottom(Color.black);
				ct3.setBorder(Rectangle.BOTTOM); ct2.setBorderColorBottom(Color.black);
				ct4.setBorder(Rectangle.BOTTOM); ct1.setBorderColorBottom(Color.black);
				ct5.setBorder(Rectangle.BOTTOM); ct2.setBorderColorBottom(Color.black);
				ct6.setBorder(Rectangle.BOTTOM); ct2.setBorderColorBottom(Color.black);
				ct7.setBorder(Rectangle.BOTTOM); ct2.setBorderColorBottom(Color.black);

				table2.addCell(ct1);
				table2.addCell(ct2);
				table2.addCell(ct3);
				table2.addCell(ct4);
				table2.addCell(ct5);
				table2.addCell(ct6);
				table2.addCell(ct7);




				for (int i = 0; i < etudiant.getCalendrier().size(); i++) {
					Paragraph pa = new Paragraph(etudiant.getCalendrier().get(i).getDate(), normal);
					PdfPCell celltext = new PdfPCell(pa);
					celltext.setBorder(Rectangle.NO_BORDER);

					Paragraph pa2 = new Paragraph(etudiant.getCalendrier().get(i).getHeure(), normal);
					PdfPCell celltext2 = new PdfPCell(pa2);
					celltext2.setBorder(Rectangle.NO_BORDER);

					Paragraph pa3 = new Paragraph(etudiant.getCalendrier().get(i).getDuree(), normal);
					PdfPCell celltext3 = new PdfPCell(pa3);
					celltext3.setBorder(Rectangle.NO_BORDER);

					Paragraph pa4 = new Paragraph(etudiant.getCalendrier().get(i).getBatiment(), normal);
					PdfPCell celltext4 = new PdfPCell(pa4);
					celltext4.setBorder(Rectangle.NO_BORDER);

					Paragraph pa5 = new Paragraph(etudiant.getCalendrier().get(i).getSalle(), normal);
					PdfPCell celltext5 = new PdfPCell(pa5);
					celltext5.setBorder(Rectangle.NO_BORDER);

					Paragraph pa6 = new Paragraph(etudiant.getCalendrier().get(i).getPlace(), normal);
					PdfPCell celltext6 = new PdfPCell(pa6);
					celltext6.setBorder(Rectangle.NO_BORDER);

					Paragraph pa7 = new Paragraph(etudiant.getCalendrier().get(i).getEpreuve(), normal);
					PdfPCell celltext7 = new PdfPCell(pa7);
					celltext7.setBorder(Rectangle.NO_BORDER);

					table2.addCell(celltext);
					table2.addCell(celltext2);
					table2.addCell(celltext3);
					table2.addCell(celltext4);
					table2.addCell(celltext5);
					table2.addCell(celltext6);
					table2.addCell(celltext7);

					/*PdfPCell celltext4 = new PdfPCell(table3);
				celltext4.setBorder(Rectangle.NO_BORDER);
				table2.addCell(celltext4);*/

				}
				document.add(table);
				document.add(table2);
				document.add(new Paragraph("\n"));
			}else{


				document.add(table);
				Paragraph pinfo = new Paragraph("      "+getString("PDF.SUBTITLE.NOCALENDRIER"), normalbig);
				pinfo.indentationLeft();
				document.add(pinfo);
				document.add(new Paragraph("\n"));
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
	 * 
	 * @return etudiant
	 */
	public Etudiant getEtudiant() {
		return etudiant;
	}

	/**
	 * 
	 * @param etudiant
	 */
	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	/**
	 * 
	 * @return config
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * 
	 * @param config
	 */
	public void setConfig(Config config) {
		this.config = config;
	}






}
