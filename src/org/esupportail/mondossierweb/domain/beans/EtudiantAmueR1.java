/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import fr.univ.rennes1.cri.apogee.domain.dto.Ren1CalendRentreeDTO;
import fr.univ.rennes1.cri.apogee.services.remote.ReadRennes1;
import gouv.education.apogee.commun.client.ws.administratifmetier.AdministratifMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.client.ws.etudiantmetier.EtudiantMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.client.ws.geographiemetier.GeographieMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.client.ws.pedagogiquemetier.PedagogiqueMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.transverse.dto.etudiant.AdresseDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.CoordonneesDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.IdentifiantsEtudiantDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.IndBacDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.InfoAdmEtuDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.TypeHebergementCourtDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ContratPedagogiqueResultatElpEprDTO2;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ContratPedagogiqueResultatElpEprDTO4;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ContratPedagogiqueResultatVdiVetDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.EpreuveElpDTO2;
import gouv.education.apogee.commun.transverse.dto.pedagogique.EtapeResVdiVetDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatElpDTO2;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatEprDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatVdiDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatVetDTO;
import gouv.education.apogee.commun.transverse.exception.WebBaseException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.esupportail.apogee.domain.dto.enseignement.VersionEtapeDTO;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.mondossierweb.dao.IDaoServiceDossierEtudiant;
import org.esupportail.mondossierweb.dto.ObjetRecherche;
import org.esupportail.mondossierweb.dto.ParamRequeteDTO;
import org.esupportail.mondossierweb.dto.ResExtractionR1DTO;
import org.esupportail.mondossierweb.services.authentification.ISecurity;
import org.esupportail.mondossierweb.web.controllers.SessionController;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * permet d'utiliser le WS de l'AMUE + le WS apo-cri-web + Ibatis.
 * utilise la table de publication des resultats Web
 * extend EtudiantAmue
 * @author Vincent Repain, Guillaume Martel
 */
public class EtudiantAmueR1 extends EtudiantAmue {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(EtudiantAmueR1.class);
	/**
	 * la signification du type de résultat 'COR'.
	 */
	private final String SIGNIFICATION_TYP_RESULT_COR ="Obtenu par Correspondance";

	/**
	 * Appel aux beans remote : infos R1 du WS apo-cri-web.
	 */
	private static final String REMOTE_CONFIG_FILE = "/properties/specific/specific.xml";
	/**
	 * 
	 */
	private ReadRennes1 monApoCriR1;

	/**
	 * le bean proposant les appels à la BDD Apogée pour toutes mes informations du dossier étudiant.
	 */
	private IDaoServiceDossierEtudiant serviceDossierEtudiant;

	/**
	 * constructeur vide.
	 *
	 */
	public EtudiantAmueR1() {
		super();
		try {
			monProxyAdministratif =  new AdministratifMetierServiceInterfaceProxy();
			monProxyEtu = new EtudiantMetierServiceInterfaceProxy();
			monProxyGeo = new GeographieMetierServiceInterfaceProxy();
			monProxyPedagogique = new PedagogiqueMetierServiceInterfaceProxy();
			
			ClassPathResource res = new ClassPathResource(REMOTE_CONFIG_FILE);
			BeanFactory beanFactory = new XmlBeanFactory(res);
			monApoCriR1 = (ReadRennes1) beanFactory.getBean("remoteCriApogeeRennes1");
			//securityHabilitationApogee = (SecurityHabilitationApogee) beanFactory.getBean("securityHabilitationApogee");
			
		} catch(Exception e) {
			LOG.error(e);
			LOG.error("Probleme avec le WS : connexion impossible, création des proxys échouée.");
		}
	}

	/**
	 * @param serviceDossierEtudiant
	 */
	public void setServiceDossierEtudiant(IDaoServiceDossierEtudiant serviceDossierEtudiant) {
		this.serviceDossierEtudiant = serviceDossierEtudiant;
	}

	/**
	 * @return IDaoServiceDossierEtudiant
	 */
	public IDaoServiceDossierEtudiant getServiceDossierEtudiant() {
		return serviceDossierEtudiant;
	}

	/**
	 * va chercher et renseigne les informations concernant le calendrier
	 * des examens de 
	 * l'étudiant placé en paramètre avec le WS apo-cri-web.
	 */
	@Override
	public void setCalendrier(Etudiant e) {

		try {
			e.getCalendrier().clear();
			List<ExamensEtape> listeCalendExamEtp = new ArrayList<ExamensEtape>();

			if (e.isInscriptionsRenseignees()) {
				List<Inscription> vinscr = e.getLinsciae();
				int max_annee = 0;
				// Recherche dernière année
				for (int i = 0; i < vinscr.size(); i++) {
					int cur_annee = Integer.parseInt(vinscr.get(i).getCod_anu().substring(0, 4));
					if (cur_annee > max_annee) {
						max_annee = cur_annee;
					}
				}		
				
				// Pour chaque étape de la dernière année
				for (int i = 0; i < vinscr.size(); i++) {
					int cur_annee = Integer.parseInt(vinscr.get(i).getCod_anu().substring(0, 4));
					
					if (cur_annee == max_annee) {
						ExamensEtape examEtp = new ExamensEtape();
						examEtp.setEtape(vinscr.get(i).getCod_etp());
						examEtp.setVet(vinscr.get(i).getCod_vrs_vet());
						examEtp.setLibEtape(vinscr.get(i).getLib_etp());
				
						// Récuperation des examens
						ParamRequeteDTO param = new ParamRequeteDTO();
						param.setChaine1(e.getCod_ind());
						param.setChaine2(vinscr.get(i).getCod_etp());
						param.setChaine3(vinscr.get(i).getCod_vrs_vet());
						List<Examen> listeCalendExam = new ArrayList<Examen>();
						listeCalendExam = service.getExamensEtape(param);
							
						// Affectation des examens
						if ((listeCalendExam!=null)&&(listeCalendExam.size()>0)) {
							examEtp.setCalendrierExamensEtape(listeCalendExam);
							e.setExisteExamEtape(true);
							
							//renseigner la page info
							examEtp.setInfos("");
							String codCin = listeCalendExam.get(0).getCodCin();
							if (codCin!=null) {
								String pageInfos = service.getPageInfos(codCin);
								if (pageInfos!=null) examEtp.setInfos(pageInfos);
							}

							listeCalendExamEtp.add(examEtp);
						}
					}
				}
				e.setCalendrierEtape(listeCalendExamEtp);
			}
		} catch(Exception ex) {
			LOG.error(ex);
		}
	}

	/**
	 * va chercher et renseigne les informations concernant le calendrier
	 * de rentree de 
	 * l'étudiant placé en paramètre avec le WS apo-cri-web.
	 * @param e 
	 */
	@Override
	public void setCalendrierRentree(Etudiant e) {
		/**
		 *  copie de etudiantEsup:
		 */
		try {
			e.getCalendrierRentree().clear();

			if (e.isInscriptionsRenseignees()) {
				List<Inscription> vinscr = e.getLinsciae();
				int max_annee = 0;
				// Recherche dernière année
				for (int i = 0; i < vinscr.size(); i++) {
					int cur_annee = Integer.parseInt(vinscr.get(i).getCod_anu().substring(0, 4));
					if (cur_annee > max_annee) {
						max_annee = cur_annee;
					}
				}		
				List<VersionEtapeDTO> listeVersionEtp = new ArrayList<VersionEtapeDTO>();
				listeVersionEtp.clear();
				// Création liste versions d'étapes pour la dernière année
				for (int i = 0; i < vinscr.size(); i++) {
					int cur_annee = Integer.parseInt(vinscr.get(i).getCod_anu().substring(0, 4));
					if (cur_annee == max_annee) {
						VersionEtapeDTO versionEtp = new VersionEtapeDTO();
						versionEtp.setCodEtp(vinscr.get(i).getCod_etp());
						versionEtp.setCodVrsVet(Integer.valueOf(vinscr.get(i).getCod_vrs_vet()));
						listeVersionEtp.add(versionEtp);
					}
				}
				List<Ren1CalendRentreeDTO> listeR1CalendRentree = monApoCriR1.getRen1CalendRent(listeVersionEtp, null);
				List<CalendrierRentree> listeCalendrierRentree = new ArrayList<CalendrierRentree>();
				for (int j = 0; j < listeR1CalendRentree.size(); j++) {
					CalendrierRentree calendrierRentree = new CalendrierRentree();
					calendrierRentree.setCodEtp(listeR1CalendRentree.get(j).getCodEtp());
					calendrierRentree.setLibEtp(recupLibEtpFromInscr(vinscr,listeR1CalendRentree.get(j).getCodEtp()));
					calendrierRentree.setCodVrsVet(listeR1CalendRentree.get(j).getCodVrsVet());
					calendrierRentree.setCommentaire(listeR1CalendRentree.get(j).getCommentaire());
					calendrierRentree.setDate(listeR1CalendRentree.get(j).getDate());
					calendrierRentree.setHeure(String.format("%02d", listeR1CalendRentree.get(j).getHeure()));
					calendrierRentree.setLieu(listeR1CalendRentree.get(j).getLieu());
					calendrierRentree.setMinute(String.format("%02d", listeR1CalendRentree.get(j).getMinute()));
					calendrierRentree.setPageInfos(listeR1CalendRentree.get(j).getPageInfos());
					listeCalendrierRentree.add(calendrierRentree);
				}
				if (listeR1CalendRentree.size() > 0 ) {
					e.setExisteCalendrierRentree(true);
				}
				e.setCalendrierRentree(listeCalendrierRentree);
			}
		} catch(Exception ex) {
			LOG.error(ex);
		}
	}
	
	/**
	 * @param vinscr
	 * @param codEtp
	 * @return String
	 */
	private String recupLibEtpFromInscr(List<Inscription> vinscr, String codEtp) {
		String vlibEtp = null;
		for (int i = 0; i < vinscr.size(); i++) {
			if (vinscr.get(i).getCod_etp().compareToIgnoreCase(codEtp) == 0 ) {
				vlibEtp = vinscr.get(i).getLib_etp();
				break;
			}
		}
		return vlibEtp;
	}

	/**
	 * renseigne les attributs concernant les notes et résultats obtenus.
	 * @param e
	 * @param resultatVdiVet
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setNotesEtResultats(Etudiant e, ContratPedagogiqueResultatVdiVetDTO[] resultatVdiVet) {
		try {

			e.getDiplomes().clear();
			e.getEtapes().clear();

			// Init variables pour habilitations Apogee
			Boolean habilitationApogee = true;
			SessionController session = (SessionController)  BeanUtils.getBean("sessionController");
			ISecurity security = (ISecurity)  BeanUtils.getBean("security");
			String currentUserId = session.getIduser();

			for (int i = 0; i < resultatVdiVet.length; i++ ) {
				//information sur le diplome:
				ContratPedagogiqueResultatVdiVetDTO rdto = resultatVdiVet[i];
				
				if(rdto.getDiplome() != null) {
					Diplome d = new Diplome();
					
					d.setLib_web_vdi(rdto.getDiplome().getLibDip());
					d.setCod_dip(rdto.getDiplome().getCodDip());
					d.setCod_vrs_vdi(rdto.getDiplome().getCodVrsVdi().toString());

					int annee2 = new Integer(rdto.getAnnee()) + 1;
					
					d.setAnnee(rdto.getAnnee() + "/" + annee2);

					// Ctrl habilitation Apogee sur visu resultat diplome
					habilitationApogee = true;
					if(session.isEnseignant()) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("userId is : " + currentUserId);
							LOG.debug("code_diplome is : " + d.getCod_dip());
						}
						habilitationApogee = security.habilitationApogee("monDosWeb", "RESULT_NOTES", currentUserId, d.getCod_dip()+d.getCod_vrs_vdi(), "dip");
					}
					//information sur les résultats obtenus au diplome:
					ResultatVdiDTO[] tabres = rdto.getResultatVdi();

					if (tabres != null && tabres.length > 0) {

						for (int j = 0; j < tabres.length; j++ ) {
							Resultat r = new Resultat();
							ResultatVdiDTO res = tabres[j];
							r.setSession(res.getSession().getLibSes());
							if(res.getNatureResultat() != null && res.getNatureResultat().getCodAdm() != null && res.getNatureResultat().getCodAdm().equals("0")){
								//on est en Admissibilité à l'étape.Pas en admission.
								//on le note pour que ce soit plus clair pour l'étudiant
								r.setNote(res.getNatureResultat().getLibAdm());

							}

							String result = null;
							if (habilitationApogee) {
								// modif GMA : 12/02/2010
								if (res.getTypResultat() != null) {
									result = res.getTypResultat().getCodTre();
								}
								r.setAdmission(result);
								if (res.getNotVdi() != null) {
									r.setNote(res.getNotVdi().toString());
									//ajout pour note Jury
									if(res.getNotPntJurVdi() != null && !res.getNotPntJurVdi().equals(new BigDecimal(0))){
										r.setNote(r.getNote()+"(+"+res.getNotPntJurVdi()+")");
									}
								} else {
									if (result.equals("DEF")) {
										r.setNote("DEF");
									}
								}

								//ajout de la signification du résultat dans la map
								if (result != null && !e.getSignificationResultats().containsKey(r.getAdmission())) {
									e.getSignificationResultats().put(r.getAdmission(), res.getTypResultat().getLibTre());
								}
								if(res.getNbrRngEtuVdi() != null && !res.getNbrRngEtuVdi().equals("")){
									d.setRang(res.getNbrRngEtuVdi()+"/"+res.getNbrRngEtuVdiTot());
								}
							} else {

								result = "NC";
								r.setAdmission(result);
								r.setNote(result);
								d.setRang(result);
								if (!e.getSignificationResultats().containsKey(r.getAdmission())) {
									e.getSignificationResultats().put(r.getAdmission(), "Non communiqué");
								}
							}

							//Gestion du barème:
							if(res.getBarNotVdi() != null){
								r.setBareme(res.getBarNotVdi());
							}

							//ajout du résultat au diplome:
							d.getResultats().add(r);
						}
						//ajout du diplome si on a au moins un résultat
						e.getDiplomes().add(0, d);
					}

				}

				//information sur les etapes:
				EtapeResVdiVetDTO[] etapes = rdto.getEtapes();
				if (etapes != null && etapes.length > 0) {

					for (int j = 0; j < etapes.length; j++ ) {
						EtapeResVdiVetDTO etape = etapes[j];

						// On n'affiche pas les étapes annulées
						if ((etape.getCodEtaIae()== null) || ((etape.getCodEtaIae()!= null && !etape.getCodEtaIae().equals("A")))){
							Etape et = new Etape();
							int anneeEtape = new Integer(etape.getCodAnu());
							et.setAnnee(anneeEtape + "/" + (anneeEtape + 1));
							et.setCode(etape.getEtape().getCodEtp());
							et.setVersion(etape.getEtape().getCodVrsVet().toString());
							et.setLibelle(etape.getEtape().getLibEtp());

							// VR 23/03/09
							// Ici : Ajout recherche habilitations sur l'étape si enseignant

							// Ctrl habilitation Apogee sur visu resultat etape
							habilitationApogee = true;
							if(session.isEnseignant()) {
								if (LOG.isDebugEnabled()) {
									LOG.debug("userId is : " + currentUserId);
									LOG.debug("code_etape is : " + et.getCode());
								}
								habilitationApogee = security.habilitationApogee("monDosWeb", "RESULT_NOTES", currentUserId, et.getCode()+et.getVersion(), "etp");
							}
							//résultats de l'étape:
							ResultatVetDTO[] tabresetape = etape.getResultatVet();

							if (tabresetape != null && tabresetape.length > 0) {
								for (int k = 0; k < tabresetape.length; k++ ) {
									ResultatVetDTO ret = tabresetape[k];

									// modif GMA : 02/02/2010
									if (ret==null) {
										continue;
									}
									
									Resultat r = new Resultat();

									if(!ret.getEtatDelib().getCodEtaAvc().equals("T")) {
										et.setDeliberationTerminee(false);
									} else {
										et.setDeliberationTerminee(true);
									}

									r.setSession(ret.getSession().getLibSes());
									if(ret.getNatureResultat() != null && ret.getNatureResultat().getCodAdm()!= null && ret.getNatureResultat().getCodAdm().equals("0")){
										//on est en Admissibilité à l'étape.Pas en admission.
										//on le note pour que ce soit plus clair pour l'étudiant
										r.setNote(ret.getNatureResultat().getLibAdm());

									}

									// VR 23/03/09
									// On affiche le résultat si habilité, sinon
									// NC : Non communiqué
									String result = null;
									if (habilitationApogee) {
										// modif GMA : 02/02/2010
										if (ret.getTypResultat()!=null) {
											result = ret.getTypResultat().getCodTre();
										}
										r.setAdmission(result);

										if (ret.getNotVet() != null) {
											r.setNote(ret.getNotVet().toString());
										} else {
											if (result != null && result.equals("DEF")) {
												r.setNote("DEF");
											}
										}

										//ajout de la signification du résultat dans la map
										if (result != null && !e.getSignificationResultats().containsKey(r.getAdmission())) {
											e.getSignificationResultats().put(r.getAdmission(), ret.getTypResultat().getLibTre());
										}
										if(ret.getNbrRngEtuVet() != null && !ret.getNbrRngEtuVet().equals("")){
											et.setRang(ret.getNbrRngEtuVet()+"/"+ret.getNbrRngEtuVetTot());
										}
										
									} else {
										result = "NC";
										r.setAdmission(result);
										r.setNote(result);
										et.setRang(result);
										if (!e.getSignificationResultats().containsKey(r.getAdmission())) {
											e.getSignificationResultats().put(r.getAdmission(), "Non communiqué");
										}
									}
									//ajout du résultat
									et.getResultats().add(r);
								}
							} else {
								if (! habilitationApogee) {
									et.setRang("NC");
								}
							}

							//ajout de l'étape a la liste d'étapes de l'étudiant:
							e.getEtapes().add(0, et);

						}
					}
				}

			}
		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats pour etudiant dont codetu est : " + e.getCod_etu());
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats pour etudiant dont codetu est : " + e.getCod_etu());
		}

	}




	/**
	 * va chercher et renseigne les informations concernant les notes
	 * et résultats des éléments de l'etape choisie
	 * de l'étudiant placé en paramètre via le WS de l'Amue.
	 */
	@Override
	public void setNotesElpEpr(Etudiant e, Etape et) {

		String anneeEnCours = service.getAnneeUniversitaireResEnCours().trim();
		if (et.getAnnee().trim().equals(anneeEnCours)) {
			setNotesElpEprEnCours(e, et);
		} else {
			try {
				e.getElementsPedagogiques().clear();

				String temoin = config.getTemoinNotesEtudiant();
				if(temoin == null || temoin.equals("")){
					temoin="T";
				}
				
				ContratPedagogiqueResultatElpEprDTO4[] cpdto = monProxyPedagogique.recupererContratPedagogiqueResultatElpEpr_v5(e.getCod_etu(), et.getAnnee().substring(0, 4), et.getCode(), et.getVersion(), "Apogee", temoin, "toutes", "tous");

				setNotesElpEpr(e, et, cpdto, temoin);

			} catch (WebBaseException ex) {
				//on catch le cas ou les inscriptions aux elp de l'étape ne soient pas effectués.
				if (!ex.getNature().equals("nullretrieve") && !ex.getDomaine().equals("data") && !ex.getElement().equals("elp") && !ex.getType().equals("tecnical")){
					LOG.error(ex);
				}
				LOG.error(ex.getLastErrorMsg()+" pour etudiant dont codetu est : " + e.getCod_etu());
			} catch (Exception ex) {
				LOG.error(ex);
				LOG.error("Probleme avec le WS lors de la recherche des notes et résultats a une étape pour etudiant dont codetu est : " + e.getCod_etu());
			}
		}

	}

	/**
	 * @see org.esupportail.mondossierweb.domain.beans.IEtudiant#setNotesElpEprEnseignant(org.esupportail.mondossierweb.domain.beans.Etudiant, org.esupportail.mondossierweb.domain.beans.Etape)
	 */
	@Override
	public void setNotesElpEprEnseignant(Etudiant e, Etape et) {
		String anneeEnCours = service.getAnneeUniversitaireResEnCours().trim();
		if (et.getAnnee().trim().equals(anneeEnCours)) {
			setNotesElpEprEnseignantEnCours(e, et);
		} else {
			try {
				e.getElementsPedagogiques().clear();

				String temoin = config.getTemoinNotesEtudiant();
				if(temoin == null || temoin.equals("")){
					temoin="AET";
				}
				
				ContratPedagogiqueResultatElpEprDTO4[] cpdto = monProxyPedagogique.recupererContratPedagogiqueResultatElpEpr_v5(e.getCod_etu(), et.getAnnee().substring(0, 4), et.getCode(), et.getVersion(), "Apogee", temoin, "toutes", "tous");

				setNotesElpEpr(e, et, cpdto, temoin);

			} catch (WebBaseException ex) {
				//on catch le cas ou les inscriptions aux elp de l'étape ne soient pas effectués.
				if (!ex.getNature().equals("nullretrieve") && !ex.getDomaine().equals("data") && !ex.getElement().equals("elp") && !ex.getType().equals("tecnical")){
					LOG.error(ex);
				}
				LOG.error(ex.getLastErrorMsg()+" pour etudiant dont codetu est : " + e.getCod_etu());
			} catch (Exception ex) {
				LOG.error(ex);
				LOG.error("Probleme avec le WS lors de la recherche des notes et résultats a une étape pour etudiant dont codetu est : " + e.getCod_etu());
			}
		}
	}

	/**
	 * va chercher et renseigne les informations concernant les notes
	 * et résultats des éléments de l'etape choisie
	 * de l'étudiant placé en paramètre via le service Ibatis.
	 * @param e Etudiant
	 * @param et Etape
	 */
	public void setNotesElpEprEnCours(Etudiant e, Etape et) {

		LOG.debug("==== enterging setNotesElpEprEnCours);");
		
		setNotesElpEprEnseignantEnCours(e, et);
	}

	/**
	 * va chercher et renseigne les informations vue enseignant concernant les notes
	 * et résultats des éléments de l'etape choisie
	 * de l'étudiant placé en paramètre via le service Ibatis.
	 * @param e Etudiant
	 * @param et Etape
	 */
	@SuppressWarnings("unchecked")
	public void setNotesElpEprEnseignantEnCours(Etudiant e, Etape et) {

		LOG.debug("==== enterging setNotesElpEprEnseignantEnCours);");
		
		e.getElementsPedagogiques().clear();

		// Init variables pour habilitations Apogee
		Boolean habilitationApogee = true;
		SessionController session = (SessionController)  BeanUtils.getBean("sessionController");
		ISecurity security = (ISecurity)  BeanUtils.getBean("security");
		String currentUserId = session.getIduser();
		
		String codInd = e.getCod_ind();
		String codAnu = service.getAnneeResEnCours().trim();
		int annee2;
		
		String result;
		String libResult;

		// Récuperation des résultats niveaux supérieurs
		ParamRequeteDTO param = new ParamRequeteDTO();
		param.setChaine1(codInd);
		param.setChaine2(codAnu);
		List<ResExtractionR1DTO> lressup = serviceDossierEtudiant.getResultats(param);
		
		if (lressup != null && !lressup.isEmpty()) {
			// On parcourt les résultats supérieurs (ie sur l'élément racine du relevé de note)
			for(ResExtractionR1DTO res : lressup) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("====== setNotesElpEprEnseignantEnCours : res.codObjMnp=" + res.getCodObjMnp());
					LOG.debug("====== setNotesElpEprEnseignantEnCours : res.codRvn=" + res.getCodRvn());
				}
				
				// on vérifie que l'étape du RVN correspond à l'étape sélectionnée
				// ou que l'élément du RVN est un élément de l'étape sélectionnée
				if (!((res.getTypObjMnp().equalsIgnoreCase("VET"))
						&&(et.getCode().equalsIgnoreCase(res.getCodObjMnp())
						&&(et.getVersion().equalsIgnoreCase(res.getCodVrsObjMnp())) ) ) ) {
					if (!isObjMnpEtape(e, res, et)) {
						continue;
					}
				}
				
				// Récupération de l'habilitation à voir les résultats et notes
				habilitationApogee = true;
				if(session.isEnseignant()) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("userId is : " + currentUserId);
						LOG.debug("code_elp is : " + res.getCodObjMnp());
					}
					if (res.getTypObjMnp().equalsIgnoreCase("VET")) {
						habilitationApogee = security.habilitationApogee("monDosWeb", "RESULT_NOTES", currentUserId, res.getCodObjMnp() + res.getCodVrsObjMnp(), "etp");
					} else {
						habilitationApogee = security.habilitationApogee("monDosWeb", "RESULT_NOTES", currentUserId, res.getCodObjMnp(), "elp");
					}
				}
				
				ElementPedagogique eltRvn = new ElementPedagogique();
				annee2 = new Integer(res.getCodAnuObjMnp()) + 1;
				eltRvn.setAnnee(res.getCodAnuObjMnp()+"/"+annee2);
				
				eltRvn.setCode(res.getCodObjMnp());
				eltRvn.setEcts(res.getNbrCrdObjMnp());
				
				// Si élément avec rang de classement, on précise le rang
				eltRvn.setLibelle(res.getLibCmtTrv());
				if (StringUtils.nullIfEmpty(res.getNbrRngEtuTrv())!=null) {
					eltRvn.setRang(res.getNbrRngEtuTrv() + "/" + res.getNbrTotRngTrv());
				}
				
				eltRvn.setCodElpSup(""); // GMA - Je ne sais pas si ça sert ?
				eltRvn.setLevel(1);
				
				// Ctrl habilitation Apogee sur visu notes element racine
				eltRvn.setNote1("");
				eltRvn.setBareme1(0);
				eltRvn.setRes1("");
				eltRvn.setNote2("");
				eltRvn.setBareme2(0);
				eltRvn.setRes2("");
				eltRvn.setEcts("");
				if(!res.getCodSesObjMnp().equals("2")){
					if (habilitationApogee) {
						result = res.getCodTre();
						libResult = res.getLicTre();
						if (StringUtils.nullIfEmpty(res.getNotTrv())!=null) {
							if (res.getNotPntJurTrv()!=null&&!res.getNotPntJurTrv().trim().isEmpty()) {
								eltRvn.setNote1(res.getNotTrv() + " (+" + res.getNotPntJurTrv().trim() + ")");
							} else {
								eltRvn.setNote1(res.getNotTrv());
							}
						}
						if (StringUtils.nullIfEmpty(res.getBarNotTrv())!=null) { 
							eltRvn.setBareme1(new Integer(res.getBarNotTrv()));
						}
					} else {
						result = "NC";
						libResult = "Non communiqué";
						eltRvn.setNote1("NC");
						eltRvn.setBareme1(0);
					}

					// Si élément avec mention, on précise la mention
					if (StringUtils.nullIfEmpty(res.getLibMen())!=null) {
						eltRvn.setRes1(result
								+ " mention " + res.getLibMen());
					} else {
						eltRvn.setRes1(result);
					}
	
				} else {
					if (habilitationApogee) {
						result = res.getCodTre();
						libResult = res.getLicTre();
						if (StringUtils.nullIfEmpty(res.getNotTrv())!=null) {
							if (res.getNotPntJurTrv()!=null&&!res.getNotPntJurTrv().trim().isEmpty()) {
								eltRvn.setNote2(res.getNotTrv() + " (+" + res.getNotPntJurTrv().trim() + ")");
							} else {
								eltRvn.setNote2(res.getNotTrv());
							}
						}
						if (StringUtils.nullIfEmpty(res.getBarNotTrv())!=null) { 
							eltRvn.setBareme2(new Integer(res.getBarNotTrv()));
						}
					} else {
						result = "NC";
						libResult = "Non communiqué";
						eltRvn.setNote2("NC");
						eltRvn.setBareme2(0);
					}
					// Si élément avec mention, on précise la mention
					if (StringUtils.nullIfEmpty(res.getLibMen())!=null) {
						eltRvn.setRes2(result
								+ " mention " + res.getLibMen());
					} else {
						eltRvn.setRes2(result);
					}

				}

				// On ajoute la légende du résultat si ce n'est déjà fait
				if (!e.getSignificationResultats().containsKey(result)) {
					e.getSignificationResultats().put(result, libResult);
				}
				
				// On ajoute l'élément à liste des éléments à afficher
				e.getElementsPedagogiques().add(eltRvn);
				
				// Pour chaque élément racine, on cherche le détail des résultats (ie sur les éléments du relevé de note)
				String codRvn = res.getCodRvn();
				param.setChaine1(codInd);
				param.setChaine2(codAnu);
				param.setChaine3(codRvn);
				List<ResExtractionR1DTO> lresdetail = serviceDossierEtudiant.getResultatsDetail(param);
				
				if (lresdetail != null && !lresdetail.isEmpty()) {
					for(ResExtractionR1DTO resdetail : lresdetail ){
		
						LOG.debug("======== setNotesElpEprEnseignantEnCours Detail : resdetail.codObjMnp=" + resdetail.getCodObjMnp());
						ElementPedagogique eltEnCours = new ElementPedagogique();
						if (resdetail.getTypObjMnp().equalsIgnoreCase("EPR")) {
							eltEnCours.setCode("");
							//eltEnCours.setAnnee("epreuve");
							eltEnCours.setAnnee("epreuve");
						} else {
							eltEnCours.setCode(resdetail.getCodObjMnp());
							eltEnCours.setEcts(resdetail.getNbrCrdObjMnp());
							
							annee2 = new Integer(resdetail.getCodAnuObjMnp()) + 1;
							eltEnCours.setAnnee(resdetail.getCodAnuObjMnp()+"/"+annee2);
						}
						eltEnCours.setCodElpSup(""); // GMA - Je ne sais pas si ça sert ?
						if (!codAnu.equals(resdetail.getCodAnuObjMnp().trim())) {
							// Si élément acquis année antérieure, on précise l'année
							eltEnCours.setLibelle(resdetail.getLibCmtTrv() + " (" + resdetail.getCodAnuObjMnp()+"/"+annee2 + ")");
						} else {
							eltEnCours.setLibelle(resdetail.getLibCmtTrv());
							// Si élément avec rang de classement, on précise le rang
							if (StringUtils.nullIfEmpty(resdetail.getNbrRngEtuTrv())!=null) {
								eltEnCours.setRang(resdetail.getNbrRngEtuTrv() + "/" + resdetail.getNbrTotRngTrv());
							}
						}
						eltEnCours.setLevel(new Integer(resdetail.getDecObjMnp()).intValue() + 1);
						
						// Ctrl habilitation Apogee sur visu notes element pedagogique détail
						// On vérifie l'habilitation uniquement s'il s'agit d'un elp
						// Pour les épreuves, on utilise l'habilitation de l'élément supérieur
						if(session.isEnseignant() && resdetail.getTypObjMnp().equalsIgnoreCase("ELP") ) {
							if (LOG.isDebugEnabled()) {
								LOG.debug("userId is : " + currentUserId);
								LOG.debug("code_elp is : " + resdetail.getCodObjMnp());
							}
							habilitationApogee = security.habilitationApogee("monDosWeb", "RESULT_NOTES", currentUserId, resdetail.getCodObjMnp(), "elp");
						}
						eltEnCours.setNote1("");
						eltEnCours.setBareme1(0);
						eltEnCours.setRes1("");
						eltEnCours.setNote2("");
						eltEnCours.setBareme2(0);
						eltEnCours.setRes2("");
						
						if(!resdetail.getCodSesObjMnp().equals("2")){
							if (habilitationApogee) {
								result = resdetail.getCodTre();
								libResult = resdetail.getLicTre();
								if (StringUtils.nullIfEmpty(resdetail.getNotTrv())!=null) {
									if (resdetail.getNotPntJurTrv()!=null&&!resdetail.getNotPntJurTrv().trim().isEmpty()) {
										eltEnCours.setNote1(resdetail.getNotTrv() + " (+" + resdetail.getNotPntJurTrv().trim() + ")");
									} else {
										eltEnCours.setNote1(resdetail.getNotTrv());
									}
								}
								if (StringUtils.nullIfEmpty(resdetail.getBarNotTrv())!=null) { 
									eltEnCours.setBareme1(new Integer(resdetail.getBarNotTrv()));
								}
							} else {
								result = "NC";
								libResult = "Non communiqué";
								eltEnCours.setNote1("NC");
								eltEnCours.setBareme1(0);
							}
							eltEnCours.setRes1(result);
		
						} else {
							if (habilitationApogee) {
								result = resdetail.getCodTre();
								libResult = resdetail.getLicTre();
								eltEnCours.setNote2(resdetail.getNotTrv());
								if (StringUtils.nullIfEmpty(resdetail.getNotTrv())!=null) {
									if (resdetail.getNotPntJurTrv()!=null&&!resdetail.getNotPntJurTrv().trim().isEmpty()) {
										eltEnCours.setNote2(resdetail.getNotTrv() + " (+" + resdetail.getNotPntJurTrv().trim() + ")");
									} else {
										eltEnCours.setNote2(resdetail.getNotTrv());
									}
								}
								if (StringUtils.nullIfEmpty(resdetail.getBarNotTrv())!=null) { 
									eltEnCours.setBareme2(new Integer(resdetail.getBarNotTrv()));
								}
							} else {
								result = "NC";
								libResult = "Non communiqué";
								eltEnCours.setNote2("NC");
								eltEnCours.setBareme2(0);
							}
							eltEnCours.setRes2(result);
						}
						
						// On ajoute la légende du résultat si ce n'est déjà fait
						if (!e.getSignificationResultats().containsKey(result)) {
							e.getSignificationResultats().put(result, libResult);
						}
						
						// On ajoute l'élément à liste des éléments à afficher
						e.getElementsPedagogiques().add(eltEnCours);
					
					}
				}
			}
			
			//indentation des libelles dans la liste:
			if (e.getElementsPedagogiques().size() > 0) {
				for (ElementPedagogique el : e.getElementsPedagogiques()) {
					int rg = new Integer(el.getLevel());
	
					String lib = new String(el.getLibelle());
					el.setLibelle("");
					for (int j = 2; j <= rg; j++) {
						el.setLibelle(el.getLibelle() + "&#160;&#160;&#160;&#160;&#160;");
					}
	
					el.setLibelle(el.getLibelle() + lib);
				}
			}
		}

	}


	/**
	 * Vérifie que l'objet res fait bien partie du contrat pédagogique de l'étape et 
	 * @param e
	 * @param res
	 * @param et
	 * @return boolean
	 */
	private boolean isObjMnpEtape(Etudiant e, ResExtractionR1DTO res, Etape et) {
		
		try {
			ContratPedagogiqueResultatElpEprDTO4[] cpdto = monProxyPedagogique.recupererContratPedagogiqueResultatElpEpr_v5(e.getCod_etu(), et.getAnnee().substring(0, 4), et.getCode(), et.getVersion(), "Aucun", "", "", "");
		
			if (cpdto != null && cpdto.length > 0) {
				for (int i = 0; i < cpdto.length; i++ ) {
					if (cpdto[i].getElp().getCodElp().equalsIgnoreCase(res.getCodObjMnp())) {
						return true;
					}
				}
			}
				
		} catch (WebBaseException ex) {
			//on catch le cas ou les inscriptions aux elp de l'étape ne soient pas effectués.
			if (!ex.getNature().equals("nullretrieve") && !ex.getDomaine().equals("data") && !ex.getElement().equals("elp") && !ex.getType().equals("tecnical")){
				LOG.error(ex);
			}
			LOG.error(ex.getLastErrorMsg()+" pour etudiant dont codetu est : " + e.getCod_etu());
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats a une étape pour etudiant dont codetu est : " + e.getCod_etu());
		}
		return false;
	}

	/**
	 * @param e
	 * @param et
	 * @param reedto
	 */
	@SuppressWarnings("unchecked")
	public void setNotesElpEpr(Etudiant e, Etape et, ContratPedagogiqueResultatElpEprDTO2[] reedto,String temoinEtatDelib) {
		try {

			// Init variables pour habilitations Apogee
			Boolean habilitationApogee = true;
			SessionController session = (SessionController)  BeanUtils.getBean("sessionController");
			ISecurity security = (ISecurity)  BeanUtils.getBean("security");
			String currentUserId = session.getIduser();
			
			e.getElementsPedagogiques().clear();
			//liste intermédiaire pour trié les éléments pédagogiques:
			List<ElementPedagogique> liste1 = new ArrayList<ElementPedagogique>();


			if (reedto != null && reedto.length > 0) {
				//On parcourt les ELP:
				for (int i = 0; i < reedto.length; i++ ) {

					ElementPedagogique elp = new ElementPedagogique();

					elp.setCode(reedto[i].getElp().getCodElp());
					elp.setLevel(reedto[i].getRngElp());
					elp.setCodElpSup(reedto[i].getCodElpSup());
					elp.setLibelle(reedto[i].getElp().getLibElp());
					elp.setAnnee("");


					if (reedto[i].getElp().getNatureElp().getCodNel().equals("FICM")) {
						//utile pour ne pas afficher les FICM par la suite
						elp.setAnnee("FICM");
					}


					elp.setNote1("");
					elp.setBareme1(0);
					elp.setRes1("");
					elp.setNote2("");
					elp.setBareme2(0);
					elp.setRes2("");
					elp.setEcts("");

					// Ctrl habilitation Apogee sur visu notes element pedagogique
					habilitationApogee = true;
					if(session.isEnseignant()) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("userId is : " + currentUserId);
							LOG.debug("code_elp is : " + reedto[i].getElp().getCodElp());
							LOG.debug("code_elp_sup is : " + reedto[i].getCodElpSup());
						}
						habilitationApogee = security.habilitationApogee("monDosWeb", "RESULT_NOTES", currentUserId, reedto[i].getElp().getCodElp(), "elp");
					}

					//vrai si l'ELP est il dans un etat de delib qui nous convient en session1:
					boolean elpEtatDelibS1OK=false;

					//vrai si l'ELP est il dans un etat de delib qui nous convient en session2:
					boolean elpEtatDelibS2OK=false;

					//On s'occupe des résultats :
					ResultatElpDTO2[] relpdto = reedto[i].getResultatsElp();
					if (relpdto != null && relpdto.length > 0) {
						//on parcourt les résultats pour l'ELP:
						for (int j = 0; j < relpdto.length; j++ ) {

							//on affiche pas les résultats d'admissibilité
							if(relpdto[j].getNatureResultat()==null || relpdto[j].getNatureResultat().getCodAdm()== null || !relpdto[j].getNatureResultat().getCodAdm().equals("0")){
								//29/01/10
								//On récupère les notes si l'ELP est dans un état de delibération compris dans la liste des témoins paramétrés.
								if(relpdto[j].getEtatDelib()==null ||  temoinEtatDelib.contains(relpdto[j].getEtatDelib().getCodEtaAvc())){

									//System.out.println("credit : "+relpdto[j].getNbrCrdElp()+" anneeResultat ELP : "+relpdto[j].getCodAnu());


									int codsession = 0;
									if(relpdto[j].getSession() != null){
										codsession = new Integer(relpdto[j].getSession().getCodSes());
									}else{
										//Pour info, on arrive ici car on peut etre en VAC: validation d'acquis
									}

									String result = null;
									if (habilitationApogee) {
										// Habilité à consulter les résultats

										//le résultat:
										if (relpdto[j].getTypResultat() != null ) {
											result = relpdto[j].getTypResultat().getCodTre();
										}

										//Test sur la session traitée
										if (codsession < 2) {
											//l'elp est dans un état de delibération compris dans la liste des témoins paramétrés.
											elpEtatDelibS1OK=true;

											//1er session  : juin
											if (relpdto[j].getNotElp() != null && !relpdto[j].getNotElp().equals("null")) {
												elp.setNote1(relpdto[j].getNotElp().toString());
												if(relpdto[j].getNotPntJurElp()!= null && !relpdto[j].getNotPntJurElp().equals(new BigDecimal(0))){
													elp.setNote1(elp.getNote1()+"(+"+relpdto[j].getNotPntJurElp()+")");
												}

											} 
											if ((elp.getNote1() == null || (elp.getNote1() != null && elp.getNote1().equals(""))) && result != null && result.equals("DEF")) {
												elp.setNote1("DEF");
											}

											//Gestion du barème:
											if(relpdto[j].getBarNotElp() != null){
												elp.setBareme1(relpdto[j].getBarNotElp());
											}

											//ajout du rang si pas déjà renseigné via la session de juin.
											if(relpdto[j].getNbrRngEtuElp() != null && !relpdto[j].getNbrRngEtuElp().equals("")
													&& (elp.getRang()==null || elp.getRang().equals(""))){
												elp.setRang(relpdto[j].getNbrRngEtuElp()+"/"+relpdto[j].getNbrRngEtuElpTot());
											}

											//on récupère l'année car si année!=null c'est un PRC  si pas déjà renseigné via la session de juin.
											if(relpdto[j].getCodAnu()!=null && !relpdto[j].getCodAnu().equals("")
													&& (elp.getAnnee()==null || elp.getAnnee().equals(""))){
												elp.setAnnee(relpdto[j].getCodAnu());
											}

											//on recupere les crédits ECTS si pas déjà renseigné via la session de juin.
											if(relpdto[j].getNbrCrdElp()!= null && relpdto[j].getNbrCrdElp().toString()!=null && !relpdto[j].getNbrCrdElp().toString().equals("")
													&& (elp.getEcts()==null || elp.getEcts().equals(""))){
												elp.setEcts(relpdto[j].getNbrCrdElp().toString());
											}

											elp.setRes1(result);
										} else {
											//2em session  : septembre
											//l'elp est dans un état de delibération compris dans la liste des témoins paramétrés.
											elpEtatDelibS2OK=true;

											if (relpdto[j].getNotElp() != null && !relpdto[j].getNotElp().equals("null")) {
												elp.setNote2(relpdto[j].getNotElp().toString());
												if(relpdto[j].getNotPntJurElp()!= null && !relpdto[j].getNotPntJurElp().equals(new BigDecimal(0))){
													elp.setNote2(elp.getNote2()+"(+"+relpdto[j].getNotPntJurElp()+")");
												}
											}
											if ((elp.getNote2() == null || (elp.getNote2() != null && elp.getNote2().equals(""))) && result != null && result.equals("DEF")) {
												elp.setNote2("DEF");
											}

											//Gestion du barème:
											if(relpdto[j].getBarNotElp()!= null){
												elp.setBareme2(relpdto[j].getBarNotElp());
											}

											//ajout du rang
											if(relpdto[j].getNbrRngEtuElp() != null && !relpdto[j].getNbrRngEtuElp().equals("")){
												elp.setRang(relpdto[j].getNbrRngEtuElp()+"/"+relpdto[j].getNbrRngEtuElpTot());
											}
											//on récupère l'année car si getCodAnu()!=null c'est un PRC
											if(relpdto[j].getCodAnu()!=null && !relpdto[j].getCodAnu().equals("")){
												elp.setAnnee(relpdto[j].getCodAnu());
											}
											//on recupere les crédits ECTS 
											if(relpdto[j].getNbrCrdElp()!= null && relpdto[j].getNbrCrdElp().toString()!=null && !relpdto[j].getNbrCrdElp().toString().equals("")){
												elp.setEcts(relpdto[j].getNbrCrdElp().toString());
											}
											elp.setRes2(result);

											//CAS DE NON OBTENTION PAR CORRESPONDANCE.
											if(relpdto[j].getLcc() == null) {

												//ajout de la signification du résultat dans la map
												if (result != null && !result.equals("") && !e.getSignificationResultats().containsKey(result)) {
													e.getSignificationResultats().put(result, relpdto[j].getTypResultat().getLibTre());
												}

											}
										}
									} else {
										// Non habilité à consulter les résultats
										result = "NC";
										elp.setNote1(result);
										elp.setRes1(result);
										if (!e.getSignificationResultats().containsKey(result)) {
											e.getSignificationResultats().put(result, "Non communiqué");
										}
									}
									
								}

							}
							//On affiche la correspondance meme si l'état de délibération n'est pas compris dans la liste des témoins paramétrés.
							if(relpdto[j].getLcc() != null) {
								//les notes ont été obtenues par correspondance a session 1.
								elp.setNote1("COR");
								//ajout de la signification du résultat dans la map
								if ( !e.getSignificationResultats().containsKey("COR")) {
									e.getSignificationResultats().put("COR",SIGNIFICATION_TYP_RESULT_COR);
								}
							}

						}
					}


					//ajout de l'élément dans la liste par ordre alphabétique:
					//liste1.add(elp);
					if (liste1.size() == 0) {
						liste1.add(elp);
					} else {
						int rang = 0;
						boolean insere = false;
						while (rang < liste1.size() && !insere) {

							if (liste1.get(rang).getCode().compareTo(elp.getCode()) > 0) {
								liste1.add(rang, elp);
								insere = true;
							}

							if (!insere) {
								rang++;
							}
						}
						if (!insere) {
							liste1.add(elp);
						}
					}



					//les epreuves de l'élément (si il y en a )
					EpreuveElpDTO2[] epelpdto = reedto[i].getEpreuvesElp();

					if (epelpdto != null && epelpdto.length > 0) {

						for (int j = 0; j < epelpdto.length; j++ ) {
							EpreuveElpDTO2 epreuve = epelpdto[j];
							boolean EprNotee = false;  //vrai si l'épreuve est notée
							ElementPedagogique elp2 = new ElementPedagogique();
							elp2.setLibelle(epreuve.getEpreuve().getLibEpr());
							elp2.setCode(epreuve.getEpreuve().getCodEpr());
							elp2.setLevel(elp.getLevel() + 1);
							//elp2.setAnnee("epreuve");
							elp2.setAnnee("epreuve");
							elp2.setCodElpSup(elp.getCode());
							elp2.setNote1("");
							elp2.setBareme1(0);
							elp2.setRes1("");
							elp2.setNote2("");
							elp2.setBareme2(0);
							elp2.setRes2("");
							ResultatEprDTO[] repdto = epreuve.getResultatEpr();
							//29/01/10
							//On récupère le témoin TemCtlValCadEpr de l'épreuve
							String TemCtlValCadEpr = epreuve.getEpreuve().getTemCtlValCadEpr();

							if (repdto != null && repdto.length > 0) {
								for (int k = 0; k < repdto.length; k++ ) {

									//29/01/10
									//On recupere la note si le témoin d'avc fait partie de la liste des témoins paramétrés 
									//OU si le témoin d'avc de  l'elp pere fait partie de la liste des témoins paramétrés 
									//OU si le témoin TemCtlValCadEpr est à "N".
									boolean recuperationNote = false;
									int codsession = new Integer(repdto[k].getSession().getCodSes());
									if (habilitationApogee) {
										// Habilité à voir les résultats

										if (codsession < 2) {

											if(temoinEtatDelib.contains(repdto[k].getEtatDelib().getCodEtaAvc()) || elpEtatDelibS1OK || TemCtlValCadEpr.equals("N"))
												recuperationNote = true;

										}else{

											if(temoinEtatDelib.contains(repdto[k].getEtatDelib().getCodEtaAvc()) || elpEtatDelibS2OK || TemCtlValCadEpr.equals("N"))
												recuperationNote = true;
										}

										//test si on recupere la note ou pas
										if(recuperationNote){


											if (codsession < 2) {
												//1er session  : juin
												if (repdto[k].getNotEpr() != null) {
													elp2.setNote1(repdto[k].getNotEpr().replaceAll(",", "."));

													//Gestion du barème:
													if(repdto[k].getBarNotEpr() != null){
														elp2.setBareme1(repdto[k].getBarNotEpr());
													}
												}
												if (elp2.getNote1() != null && !elp2.getNote1().equals("")) {
													EprNotee = true;
												}


											} else {
												//2er session  : septembre
												if (repdto[k].getNotEpr() != null) {
													elp2.setNote2(repdto[k].getNotEpr().replaceAll(",", "."));

													//Gestion du barème:
													if(repdto[k].getBarNotEpr() != null){
														elp2.setBareme2(repdto[k].getBarNotEpr());
													}
												}
												if (elp2.getNote2() != null && !elp2.getNote2().equals("")) {
													EprNotee = true;
												}
											}
										}
									} else {
										// Non habilité à voir les résultats
										String result = "NC";
										elp2.setNote1(result);
										elp2.setRes1(result);
										elp2.setNote2(result);
										elp2.setRes2(result);
									}
								}
							}
							//ajout de l'épreuve dans la liste en tant qu'élément si elle a une note
							if (EprNotee) {
								liste1.add(elp2);
							}
						}
					}
				}
			}
			//ajout des éléments dans la liste de l'étudiant en commençant par la ou les racines
			int niveauRacine = 1;
			if (liste1.size() > 0) {
				int i = 0;
				while (i < liste1.size()) {
					ElementPedagogique el = liste1.get(i);
					if (el.getCodElpSup() == null || el.getCodElpSup().equals("")) {
						//on a une racine:
						if (!el.getAnnee().equals("FICM")) {
							e.getElementsPedagogiques().add(el);
						}

						insererElmtPedagoFilsDansListe(el, liste1, e, niveauRacine);
					}
					i++;
				}
			}


			//suppression des épreuve seules et quand elles ont les mêmes notes que l'element pere:
			if (e.getElementsPedagogiques().size() > 0) {
				int i = 1;
				boolean suppr = false;
				while (i < e.getElementsPedagogiques().size()) {
					suppr = false;
					ElementPedagogique elp = e.getElementsPedagogiques().get(i);
					if (elp.getAnnee().equals("epreuve")) {
						ElementPedagogique elp0 = e.getElementsPedagogiques().get(i - 1);
						if (i < (e.getElementsPedagogiques().size() - 1)) {
							ElementPedagogique elp1 = e.getElementsPedagogiques().get(i + 1);
							if (!elp0.getAnnee().equals("epreuve") && !elp1.getAnnee().equals("epreuve")) {
								if (elp0.getNote1().equals(elp.getNote1()) && elp0.getNote2().equals(elp.getNote2())) {
									//on supprime l'element i
									e.getElementsPedagogiques().remove(i);
									suppr = true;
								}
							}
						} else {
							if (!elp0.getAnnee().equals("epreuve") && elp0.getNote1().equals(elp.getNote1()) && elp0.getNote2().equals(elp.getNote2())) {
								//on supprime l'element i
								e.getElementsPedagogiques().remove(i);
								suppr = true;
							}
						}
					}
					if (!suppr) {
						i++;
					}
				}
			}

			//indentation des libelles dans la liste:
			if (e.getElementsPedagogiques().size() > 0) {
				for (ElementPedagogique el : e.getElementsPedagogiques()) {
					int rg = new Integer(el.getLevel());

					String lib = new String(el.getLibelle());
					el.setLibelle("");
					for (int j = 2; j <= rg; j++) {
						el.setLibelle(el.getLibelle() + "&#160;&#160;&#160;&#160;&#160;");
					}

					el.setLibelle(el.getLibelle() + lib);
				}
			}
			//ajout de l'étape sélectionnée en début de liste:
			ElementPedagogique ep = new ElementPedagogique();
			//LOG.info("etape Annee : "+et.getAnnee());
			ep.setAnnee(et.getAnnee());
			ep.setCode(et.getCode());
			ep.setLevel(1);
			ep.setLibelle(et.getLibelle());
			e.setDeliberationTerminee(et.isDeliberationTerminee());
			if (et.getResultats().size() > 0) {
				if (et.getResultats().get(0).getNote() != null)
					ep.setNote1(et.getResultats().get(0).getNote().toString());
				if (et.getResultats().get(0).getAdmission() != null)
					ep.setRes1(et.getResultats().get(0).getAdmission());
			}
			if (et.getResultats().size() > 1) {
				if (et.getResultats().get(1).getNote() != null)
					ep.setNote2(et.getResultats().get(1).getNote().toString());
				if (et.getResultats().get(1).getAdmission() != null)
					ep.setRes2(et.getResultats().get(1).getAdmission());
			}
			e.getElementsPedagogiques().add(0, ep);

		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats a une étape pour etudiant dont codetu est : " + e.getCod_etu());
		}catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats a une étape pour etudiant dont codetu est : " + e.getCod_etu());
		}
	}

	/**
	 * va chercher et renseigne les informations concernant les adresses de 
	 * l'étudiant placé en paramètre via le WS de l'Amue.
	 * @param e 
	 */
	@Override
	public void setAdresses(Etudiant e) {
		/**
		 *  ++++++++++++++++++++++++++++++++++++++++++++++++++
		 *  vérifier récupération derniere année inscrption!!!
		 *  ++++++++++++++++++++++++++++++++++++++++++++++++++
		 */
		try {
			
			// Init variables pour habilitations Apogee
			Boolean habilitationApogee = true;
			SessionController session = (SessionController)  BeanUtils.getBean("sessionController");
			ISecurity security = (ISecurity)  BeanUtils.getBean("security");
			String currentUserId = session.getIduser();
			// Ctrl habilitation Apogee sur visu adresses etudiant
			habilitationApogee = true;
			if(session.isEnseignant()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("userId is : " + currentUserId);
					LOG.debug("code_etu is : " + e.getCod_etu());
				}
				habilitationApogee = security.habilitationApogee("monDosWeb", "AFF_ADR", currentUserId, e.getCod_ind(), "etu");
			}

			if(habilitationApogee) {
	
				String[] annees =  monProxyAdministratif.recupererAnneesIa(e.getCod_etu(), null);
		
				String annee = annees[annees.length - 1];
				//récupération des coordonnées :
		
				CoordonneesDTO cdto = monProxyEtu.recupererAdressesEtudiant(e.getCod_etu(), annee, "N");
		
				//récupération des adresses, annuelle et fixe :
				annee = cdto.getAnnee();
				e.setEmailPerso(cdto.getEmail());
				e.setTelPortable(cdto.getNumTelPortable());
				AdresseDTO ada = cdto.getAdresseAnnuelle();
				AdresseDTO adf = cdto.getAdresseFixe();
		
				if (ada != null) {
		
					//e.setAnu(ada.getAnnee() + "-" + (new Integer(ada.getAnnee()) + 1));
					e.setAnu(annee + "-" + (new Integer(annee) + 1));
					//informations sur l'adresse annuelle :
					if (ada.getLibAde() != null) {
						e.setAdresseannuelleetranger(ada.getLibAde());
						e.setAdresseannuellecp("");
						e.setVilleannuelle("");
					} else {
						e.setAdresseannuelleetranger("");
						if (ada.getCommune() != null) {
							e.setAdresseannuellecp(ada.getCommune().getCodePostal());
							e.setVilleannuelle(ada.getCommune().getNomCommune());
						} else {
							e.setAdresseannuellecp("");
							e.setVilleannuelle("");
						}
					}
		
					//TypeHebergementCourtDTO th = ada.getTypeHebergement();
					TypeHebergementCourtDTO th = cdto.getTypeHebergement();
					if (th != null) {
						e.setAdresseannuelleType(th.getLibTypeHebergement());
					} else {
						e.setAdresseannuelleType("");
					}
					e.setAdresseannuelle1(ada.getLibAd1());
					e.setAdresseannuelle2(ada.getLibAd2());
					e.setAdresseannuelle3(ada.getLibAd3());
					e.setNumerotelannuel(ada.getNumTel());
					if (ada.getPays() != null) {
						e.setPaysannuel(ada.getPays().getLibPay());
					} else {
						e.setPaysannuel("");
					}
				}
				if (adf != null) {
		
					//informations sur l'adresse fixe :
					e.setAdressefixe1(adf.getLibAd1());
					e.setAdressefixe2(adf.getLibAd2());
					e.setAdressefixe3(adf.getLibAd3());
					e.setNumerotelfixe(adf.getNumTel());
		
					if (adf.getLibAde() != null) {
						e.setAdressefixeetranger(adf.getLibAde());
						e.setAdressefixecp("");
						e.setVillefixe("");
					} else {
						e.setAdressefixeetranger("");
						if (adf.getCommune() != null ) {
							e.setAdressefixecp(adf.getCommune().getCodePostal());
							e.setVillefixe(adf.getCommune().getNomCommune());
						} else {
							e.setAdressefixecp("");
							e.setVillefixe("");
						}
					}
					if (adf.getPays() != null) {
						e.setPaysfixe(adf.getPays().getLibPay());
					} else {
						e.setPaysfixe("");
					}
				}
			} else {
				String result = "Non communiquée";
				e.setAdresseannuelle1(result);
				// Renseigner lignes adresse avec "NC" ?
			}
		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des adresses pour etudiant dont codetu est : " + e.getCod_etu());
		}catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des adresses pour etudiant dont codetu est : " + e.getCod_etu());
		}
	}

	/**
	 * va chercher et renseigne les informations concernant l'état-civil de 
	 * l'étudiant placé en paramètre via le WS de l'Amue.
	 * @param e 
	 */
	@Override
	public void setEtatCivil(Etudiant e) {
		try {
			// Init variables pour habilitations Apogee
			Boolean habilitationApogee = true;
			SessionController session = (SessionController)  BeanUtils.getBean("sessionController");
			ISecurity security = (ISecurity)  BeanUtils.getBean("security");
			String currentUserId = session.getIduser();
			// Ctrl habilitation Apogee sur visu état-civil etudiant
			habilitationApogee = true;
			if(session.isEnseignant()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("userId is : " + currentUserId);
					LOG.debug("code_etu is : " + e.getCod_etu());
				}
				habilitationApogee = security.habilitationApogee("monDosWeb", "SYNTH_IA", currentUserId, e.getCod_ind(), "etu");
			}
			
			//informations générales :
			IdentifiantsEtudiantDTO idetu;
	
			if (!config.isRecupAnnuaire()) {
				idetu = monProxyEtu.recupererIdentifiantsEtudiant(e.getCod_etu(), null, null, null, null, null, null, null, null, "N");
			} else {
				idetu = monProxyEtu.recupererIdentifiantsEtudiant(e.getCod_etu(), null, null, null, null, null, null, null, null, "O");
			}
	
			e.setCod_ind(idetu.getCodInd().toString());
	
			e.setCod_nne(idetu.getNumeroINE() + idetu.getCleINE());
	
			setPhoto(e);
	
			if (!config.isRecupAnnuaire()) {
				// on passe par iBATIS pour récupérer l'e-mail.
				e.setEmail(emailConverter.getMail(service.getLoginFromCodEtu(e.getCod_etu())));
			} else {
				//on récupérer l'e-mail grâce au WS.
				e.setEmail(idetu.getEmailAnnuaire());
			}
	
			InfoAdmEtuDTO iaetu = monProxyEtu.recupererInfosAdmEtu(e.getCod_etu());
	
			e.setNom(iaetu.getNomPatronymique() + " " + iaetu.getPrenom1());
	
			//informations sur la naissance :
			//la nationalité:
			if (iaetu.getNationaliteDTO() != null) {
				e.setNationalite(iaetu.getNationaliteDTO().getLibNationalite());
			} else {
				e.setNationalite("");
			}
			//la date de naissance:
			if (iaetu.getDateNaissance() != null) {
				Date d = iaetu.getDateNaissance();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String date = dateFormat.format(d);
				e.setDatenaissance(date);
				//e.setDatenaissance("" + iaetu.getDateNaissance().getDay() + " / " + iaetu.getDateNaissance().getMonth() + " / " + iaetu.getDateNaissance().getYear());
			} else {
				e.setDatenaissance("");
			}
			//la ville de naissance:
			e.setLieunaissance(iaetu.getLibVilleNaissance());
	
			//récupération du département ou du pays de naissance:
			if (iaetu.getDepartementNaissance() != null ) {
				e.setDepartementnaissance(iaetu.getDepartementNaissance().getLibDept());
			} else {
				if (iaetu.getPaysNaissance() != null) {
					e.setDepartementnaissance(iaetu.getPaysNaissance().getLibPay());
				} else {
					e.setDepartementnaissance("");
				}
			}
			
			if(!habilitationApogee) {
				String result = "Non communiqué";
				e.setNationalite(result);
				e.setDatenaissance(result);
				e.setLieunaissance(result);
				e.setDepartementnaissance(result);
			}
			
			//informations sur l'inscription universitaire :
			e.setAnnee(iaetu.getAnneePremiereInscEnsSup());
	
			if (iaetu.getEtbPremiereInscUniv() != null) {
				e.setEtablissement(iaetu.getEtbPremiereInscUniv().getLibEtb());
			} else {
				e.setEtablissement("");
			}
	
	
			//informations sur le(s) bac(s) :
			if (e.getListeBac() != null && e.getListeBac().size() > 0) {
				e.getListeBac().clear();
			} else {
				e.setListeBac(new ArrayList<BacEtatCivil>());
			}
	
	
			IndBacDTO[] bacvo = iaetu.getListeBacs();
			if (bacvo != null) {
				for (int i = 0; i < bacvo.length; i++) {
					IndBacDTO bac = bacvo[i];
					if (bac != null) {
						BacEtatCivil bec = new BacEtatCivil();
	
						bec.setLib_bac(bac.getLibelleBac());
						bec.setDaa_obt_bac_iba(bac.getAnneeObtentionBac());
	
						if (bac.getDepartementBac() != null ) {
							bec.setCod_dep(bac.getDepartementBac().getLibDept());
						} else {
							bec.setCod_dep("");
						}
						if (bac.getMentionBac() != null) {
							bec.setCod_mnb(bac.getMentionBac().getLibMention());
						} else {
							bec.setCod_mnb("");
						}
						if (bac.getTypeEtbBac() != null) {
							bec.setCod_tpe(bac.getTypeEtbBac().getLibLongTpe());
						} else { 
							bec.setCod_tpe("");
						}
						if (bac.getEtbBac() != null) {
							bec.setCod_etb(bac.getEtbBac().getLibEtb());
						} else {
							bec.setCod_etb("");
						}
						e.getListeBac().add(bec);
					}
				}
			} else {
				LOG.error("Probleme avec le WS: AUCUN BAC RETOURNE, lors de la recherche de l'état-civil pour etudiant dont codetu est : " + e.getCod_etu());
				BacEtatCivil bec = new BacEtatCivil();
				bec.setLib_bac("/");
				e.getListeBac().add(bec);
			}
	
		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche de l'état-civil pour etudiant dont codetu est : " + e.getCod_etu());
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche de l'état-civil pour etudiant dont codetu est : " + e.getCod_etu());
		}
	}






}
