/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.mondossierweb.dao.DaoServiceIBatisImpl;
import org.esupportail.mondossierweb.domain.beans.Config;
import org.esupportail.mondossierweb.domain.beans.Etape;
import org.esupportail.mondossierweb.domain.beans.Etudiant;
import org.esupportail.mondossierweb.domain.beans.Inscription;
import org.esupportail.mondossierweb.domain.beans.Signataire;
import org.esupportail.mondossierweb.web.navigation.View;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
/**
 * controller pour laffichage des incriptions d'un étudiant.
 * @author Charlie Dubois
 */
public class InscriptionsController extends AbstractContextAwareController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8942210586770125004L;

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(InscriptionsController.class);

	/**
	 * l'etatCivilController.
	 */
	private EtatCivilController etatcivilcontroller;


	/**
	 * l'inscription selectionnee
	 */
	private Inscription inscription;
	/**
	 * l'etudiant courant
	 */
	private Etudiant etudiant;

	/**
	 * l'index de l'etape selectionnee.
	 */
	private int selectedRow;
	/**
	 * annee universitaire en cours
	 */
	private String anneeEnCours;
	/**
	 * map des autorisations des certificats
	 */
	private Map<String, Boolean> autorisationCertificats;
	/**
	 * identifiant de l'étudiant auquel correspond la Map autorisationCertificats
	 */
	private String codetuMapAutorisationsCert;
	/**
	 * map des autorisations de consultation des IP
	 */
	private Map<String, Boolean> autorisationIP;
	/**
	 * identifiant de l'étudiant auquel correspond la Map autorisationIP
	 */
	private String codetuMapAutorisationsIP;
	/**
	 * marges.
	 */
	private static final float MARGIN_TOP = 5.0f;
	private static final float MARGIN_RIGHT = 2.0f;
	private static final float MARGIN_BOTTOM = 4.0f;
	private static final float MARGIN_LEFT = 3.0f;

	/**
	 * outputstream size.
	 */
	private static final int OUTPUTSTREAM_SIZE = 1024;

	/**
	 * le contructeur.
	 *
	 */
	public InscriptionsController() {
		super();
	}

	/**
	 * on enter dans la partie des inscrptions.
	 * @return la vue des inscriptions.
	 */
	public String enter() {
		etudiant = etatcivilcontroller.getEtudiant();
		return View.INSCRIPTIONS;
	}

	public String retour() {
		return View.INSCRIPTIONS;
	}
	public Map<String, Boolean> getCertScolAutorisations() {
		if(!etudiant.getCod_etu().equals(codetuMapAutorisationsCert)){
			//on reinitialise pour le cas d'un user Enseignant qui peut consulter plusieurs dossiers à la suite
			autorisationCertificats=null;
		}
		if (autorisationCertificats == null){
			Config config = (Config)  BeanUtils.getBean("config");
			DaoServiceIBatisImpl daoService = (DaoServiceIBatisImpl) BeanUtils.getBean("daoService");
			if (anneeEnCours == null){
				anneeEnCours = daoService.getAnneeUniversitaireEnCours();
			}
			autorisationCertificats = new HashMap<String, Boolean>();
			Etudiant etu = getEtatcivilcontroller().getEtudiant();

			for (Inscription ins : etu.getLinsciae()) {
				boolean mapUpdated = false;
				// autoriser ou non la generation de certificats de scolarite.
				if (!config.isCertificatScolaritePDF()) {
					autorisationCertificats.put(ins.getCodEtpCodAnuConcat(), false);
					mapUpdated=true;
				}
				// autoriser ou non les personnels à imprimer les certificats.
				if (!mapUpdated && !config.isCertScolAutorisePersonnel() && getSessionController().isEnseignant()) {
					autorisationCertificats.put(ins.getCodEtpCodAnuConcat(), false);
					mapUpdated=true;
				}
				// autorise l'édition de certificat de scolarité uniquement pour l'année en cours.
				if (!mapUpdated && !config.isCertificatScolariteTouteAnnee() && !ins.getCod_anu().equals(anneeEnCours)) {
					autorisationCertificats.put(ins.getCodEtpCodAnuConcat(), false);
					mapUpdated=true;
				}
				if (!mapUpdated && !config.getCertScolTypDiplomeDesactive().isEmpty()) {
					// interdit les certificats pour certains types de diplomes
					String codeTypeDiplome = daoService.getCodeTypeDiplome(ins.getCod_dip());

					if (config.getCertScolTypDiplomeDesactive().contains(codeTypeDiplome)) {
						autorisationCertificats.put(ins.getCodEtpCodAnuConcat(), false);
						mapUpdated=true;
					}
				}
				//interdit l'edition de certificat pour les étudiants si l'inscription n'est pas payée
				if (!mapUpdated && !ins.isEstEnRegle() && !getSessionController().isEnseignant()){
					autorisationCertificats.put(ins.getCodEtpCodAnuConcat(), false);
					mapUpdated=true;
				}
				if(!mapUpdated)
					autorisationCertificats.put(ins.getCodEtpCodAnuConcat(), true);
			}

		}
		codetuMapAutorisationsCert = etudiant.getCod_etu();
		return autorisationCertificats;
	}

	public Map<String, Boolean> getIpAutorisations() {
		if(!etudiant.getCod_etu().equals(codetuMapAutorisationsIP)){
			//on reinitialise pour le cas d'un user Enseignant qui peut consulter plusieurs dossiers à la suite
			autorisationIP=null;
		}
		if (autorisationIP == null){
			if (anneeEnCours == null){
				DaoServiceIBatisImpl daoService = (DaoServiceIBatisImpl) BeanUtils.getBean("daoService");
				anneeEnCours = daoService.getAnneeUniversitaireEnCours();
			}
			autorisationIP = new HashMap<String, Boolean>();
			for (Inscription ins : etudiant.getLinsciae()) {
				// autoriser ou non la visualisation du détail de l'IP.
				if (ins.getCod_anu().equals(anneeEnCours)) {
					autorisationIP.put(ins.getCodEtpCodAnuConcat(), true);

				}else{
					autorisationIP.put(ins.getCodEtpCodAnuConcat(), false);
				}
			}
		}
		codetuMapAutorisationsIP = etudiant.getCod_etu();
		return autorisationIP;
	}

	public String preciserEtapeInscription() {
		FacesContext context = FacesContext.getCurrentInstance();
		String row = (String) context.getExternalContext().getRequestParameterMap().get("row");

		if(row != null && !row.equals("")){
			selectedRow = new Integer(row);

			//etudiant = etatcivilcontroller.getEtudiant();

			Etape selectedEtape = new Etape(etudiant.getLinsciae().get(selectedRow).getCod_etp(),
					etudiant.getLinsciae().get(selectedRow).getCod_vrs_vet(),
					etudiant.getLinsciae().get(selectedRow).getCod_anu());
			selectedEtape.setLibelle(etudiant.getLinsciae().get(selectedRow).getLib_etp());

			etudiant.getEtudiantManager().setInscriptionPedagogique(etudiant, selectedEtape);


			return View.INSCRIPTION_PEDAGOGIQUE;
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

		etudiant = etatcivilcontroller.getEtudiant();

		String row = (String) facesContext.getExternalContext().getRequestParameterMap().get("row");
		int selectedRow=0;
		if(row != null && !row.equals("")){
			selectedRow = new Integer(row);
		}else{
			return null;
		}
		inscription = etudiant.getLinsciae().get(selectedRow);

		// verifie les autorisations
		if (!getCertScolAutorisations().get(inscription.getCodEtpCodAnuConcat())) {
			return null;
		}

		Document document = configureDocument();
		try {

			ByteArrayOutputStream baosPDF = new ByteArrayOutputStream(OUTPUTSTREAM_SIZE);
			PdfWriter docWriter = null;
			docWriter = PdfWriter.getInstance(document, baosPDF);
			docWriter.setEncryption(null, null, PdfWriter.AllowPrinting, PdfWriter.ENCRYPTION_AES_128);
			docWriter.setStrictImageSequence(true);
			creerPdfCertificatScolarite(document);
			docWriter.close();

			byte[] bytes = baosPDF.toByteArray();
			String nomFichier = getString("PDF.TITLE.CERTIFICAT")+"_" + inscription.getCod_etp() + "_" + inscription.getCod_anu().replace('/', '-') + "_" + etatcivilcontroller.getEtudiant().getNom().replace('.', ' ').replace(' ', '_') + ".pdf";

			long id = DownloadUtils.setDownload(bytes, nomFichier, "application/force-download", "attachment");

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
	public void creerPdfCertificatScolarite(final Document document) {

		//configuration des fonts
		Font normal = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);
		Font normalBig = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
		Font header = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);

		//date
		Date d = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String date = dateFormat.format(d);

		document.open();
		try {
			Config config = (Config)  BeanUtils.getBean("config");
			DaoServiceIBatisImpl daoService = (DaoServiceIBatisImpl) BeanUtils.getBean("daoService");
		
			Signataire signataire = daoService.getSignataire(config.getCertScolCodeSignataire());

			//ajout image test
			if (config.isCertScolUtiliseLogo() && config.getLogoUniversitePdf() != null && !config.getLogoUniversitePdf().equals("")){
				Image imageLogo = Image.getInstance(config.getLogoUniversitePdf());
				/*
				imageLogo.scalePercent(72f/(float)config.getImagesDPI()*100f);
				imageLogo.setAbsolutePosition((MARGIN_LEFT / 2.54f) * 72f, document.getPageSize().height() - ((MARGIN_TOP / 2.54f) * 72f));
				 */
				float scaleRatio = 40 / imageLogo.height(); 
				float newWidth=scaleRatio * imageLogo.width();
				imageLogo.scaleAbsolute(newWidth, 40);
				imageLogo.setAbsolutePosition(100, 750);
				document.add(imageLogo);
			}

			Paragraph pTitre = new Paragraph("\n\n"+getString("PDF.TITLE.CERTIFICAT").toUpperCase(), header);
			pTitre.setAlignment(Element.ALIGN_CENTER);
			document.add(pTitre);

			Paragraph pCertifie = new Paragraph("\n\n\n\n" + signataire.getQua_sig() + " "+getString("PDF.CERTIFICAT.CERTIFIE")+"\n\n", normal);
			pCertifie.setAlignment(Element.ALIGN_LEFT);
			document.add(pCertifie);

			if (etudiant.getNom() != null) {
				Paragraph pNom = new Paragraph(etudiant.getNom(), normalBig);
				pNom.indentationLeft();
				document.add(pNom);
			}
			if (etudiant.getCod_nne() != null) {
				Paragraph pNNE = new Paragraph("\n"+getString("PDF.CERTIFICAT.ID")+" : " + etudiant.getCod_nne().toLowerCase(), normal);
				pNNE.indentationLeft();
				document.add(pNNE);
			}
			if (etudiant.getCod_etu() != null) {
				Paragraph p01 = new Paragraph(getString("PDF.CERTIFICAT.NUMETUDIANT")+" : " + etudiant.getCod_etu(), normal);
				p01.setAlignment(Element.ALIGN_LEFT);
				document.add(p01);
			}
			if (etudiant.getDatenaissance() != null) {
				Paragraph pDateNaissance = new Paragraph(getString("PDF.CERTIFICAT.NAISSANCE1")+" " + etudiant.getDatenaissance(), normal);
				pDateNaissance.setAlignment(Element.ALIGN_LEFT);
				document.add(pDateNaissance);
			}
			if ((etudiant.getLieunaissance() != null) && (etudiant.getDepartementnaissance() != null)) {
				Paragraph pLieuNaissance = new Paragraph(getString("PDF.CERTIFICAT.NAISSANCE2")+" " + etudiant.getLieunaissance() + " (" + etudiant.getDepartementnaissance() + ")", normal);
				pLieuNaissance.setAlignment(Element.ALIGN_LEFT);
				document.add(pLieuNaissance);
			}

			Paragraph pEstInscrit = new Paragraph("\n"+getString("PDF.CERTIFICAT.INSCRIT")+" " + inscription.getCod_anu() + "\n ", normal);
			pEstInscrit.setAlignment(Element.ALIGN_LEFT);
			document.add(pEstInscrit);

			float[] widths = {1.5f, 7.5f};
			PdfPTable table = new PdfPTable(widths);
			table.setWidthPercentage(100f);
			table.addCell(makeCell(getString("PDF.DIPLOME")+" :", normal));
			table.addCell(makeCell(inscription.getLib_dip(), normal));
			table.addCell(makeCell(getString("PDF.YEAR")+" :", normal));
			table.addCell(makeCell(inscription.getLib_etp(), normal));
			table.addCell(makeCell(getString("PDF.COMPOSANTE")+" :", normal));
			table.addCell(makeCell(inscription.getLib_comp(), normal));
			document.add(table);

			document.add(new Paragraph(" "));

			float[] widthsSignataire = {2f, 1.3f};
			PdfPTable tableSignataire = new PdfPTable(widthsSignataire);
			tableSignataire.setWidthPercentage(100f);
			tableSignataire.addCell(makeCellSignataire("", normal));
			tableSignataire.addCell(makeCellSignataire(getString("PDF.CERTIFICAT.FAIT1")+" " + config.getCertScolLieuEdition() + getString("PDF.CERTIFICAT.FAIT2")+" " + date, normal));
			tableSignataire.addCell(makeCellSignataire("", normal));
			tableSignataire.addCell(makeCellSignataire(signataire.getNom_sig(), normal));
			//ajout signature
			if (signataire.getImg_sig_std() != null && signataire.getImg_sig_std().length > 0){ //MODIF 09/10/2012
				tableSignataire.addCell(makeCellSignataire("", normal));
				LOG.debug(signataire.getImg_sig_std().toString());
				Image imageSignature = Image.getInstance(signataire.getImg_sig_std());

				float scaleRatio = 40 / imageSignature.height(); 
				float newWidth=scaleRatio * imageSignature.width();
				imageSignature.scaleAbsolute(newWidth, 40);
				imageSignature.setAbsolutePosition(100, 750);

				PdfPCell cellSignature = new PdfPCell();
				cellSignature.setBorder(0);
				cellSignature.setImage(imageSignature);
				//cellSignature.setFixedHeight(72f/(float)config.getImagesDPI() * imageSignature.height());
				cellSignature.setHorizontalAlignment(Element.ALIGN_CENTER);
				tableSignataire.addCell(cellSignature);
			}
			document.add(tableSignataire);

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

	private PdfPCell makeCell(String str, Font font) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setPhrase(new Phrase(str, font));
		return cell;
	}

	private PdfPCell makeCellSignataire(String str, Font font) {
		PdfPCell cell = makeCell(str, font);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return cell;
	}

	/**
	 * configure le document pdf.
	 * @param width
	 * @param height
	 * @param margin
	 * @return doc
	 */
	private Document configureDocument() {

		Document document = new Document();

		document.setPageSize(PageSize.A4);
		float marginTop = (MARGIN_TOP / 2.54f) * 72f;
		float marginRight = (MARGIN_RIGHT / 2.54f) * 72f;
		float marginBottom = (MARGIN_BOTTOM / 2.54f) * 72f;
		float marginLeft = (MARGIN_LEFT / 2.54f) * 72f;
		document.setMargins(marginLeft, marginRight, marginTop, marginBottom);

		return document;
	}




	/**
	 * getter de l'attribut EtatCivilController.
	 * @return l'etatcivilController
	 */
	public EtatCivilController getEtatcivilcontroller() {
		return etatcivilcontroller;
	}

	/**
	 * setter de l'attribut EtatCivilController.
	 * @param etatcivilcontroller
	 */
	public void setEtatcivilcontroller(final EtatCivilController etatcivilcontroller) {
		this.etatcivilcontroller = etatcivilcontroller;
	}




	public int getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}



}
