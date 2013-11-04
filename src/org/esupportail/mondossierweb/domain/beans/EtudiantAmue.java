/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import gouv.education.apogee.commun.client.ws.administratifmetier.AdministratifMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.client.ws.etudiantmetier.EtudiantMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.client.ws.geographiemetier.GeographieMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.client.ws.pedagogiquemetier.PedagogiqueMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.servicesmetiers.AdministratifMetierServiceInterface;
import gouv.education.apogee.commun.servicesmetiers.EtudiantMetierServiceInterface;
import gouv.education.apogee.commun.servicesmetiers.GeographieMetierServiceInterface;
import gouv.education.apogee.commun.servicesmetiers.PedagogiqueMetierServiceInterface;
import gouv.education.apogee.commun.transverse.dto.administratif.CursusExterneDTO;
import gouv.education.apogee.commun.transverse.dto.administratif.CursusExternesEtTransfertsDTO;
import gouv.education.apogee.commun.transverse.dto.administratif.InsAdmEtpDTO2;
import gouv.education.apogee.commun.transverse.dto.etudiant.AdresseDTO2;
import gouv.education.apogee.commun.transverse.dto.etudiant.AdresseMajDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.CommuneDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.CommuneMajDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.CoordonneesDTO2;
import gouv.education.apogee.commun.transverse.dto.etudiant.CoordonneesMajDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.IdentifiantsEtudiantDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.IndBacDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.InfoAdmEtuDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.PaysDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.TypeHebergementCourtDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.TypeHebergementDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ContratPedagogiqueResultatElpEprDTO4;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ContratPedagogiqueResultatVdiVetDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.EpreuveElpDTO2;
import gouv.education.apogee.commun.transverse.dto.pedagogique.EtapeResVdiVetDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatElpDTO3;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatEprDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatVdiDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatVetDTO;
import gouv.education.apogee.commun.transverse.exception.WebBaseException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.esupportail.commons.services.exceptionHandling.ExceptionService;
import org.esupportail.commons.services.exceptionHandling.ExceptionServiceFactory;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.web.converters.EmailConverterInterface;
import org.esupportail.mondossierweb.web.photo.IPhoto;

/**
 * permet d'utiliser le WS de l'AMUE.
 * @author Charlie Dubois
 */
public class EtudiantAmue implements IEtudiant {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(EtudiantAmue.class);
	/**
	 * la signification du type de résultat 'COR'.
	 */
	private final String SIGNIFICATION_TYP_RESULT_COR ="Obtenu par Correspondance";
	private final String LIBELLE_WS_INSCRIPTION_PAYEE ="Paiement effectué";
	/**
	 * Le bean de configuration.
	 */
	protected Config config;
	/**
	 * le bean regroupant les appels à la BDD Apogée.
	 */
	protected IDaoService service;
	/**
	 * proxy pour faire appel aux infos sur l'étudiant WS .
	 */
	protected EtudiantMetierServiceInterface monProxyEtu;
	/**
	 * proxy pour faire appel aux infos géographique du WS .
	 */
	protected GeographieMetierServiceInterface monProxyGeo;
	/**
	 * proxy pour faire appel aux infos administratives du WS .
	 */
	protected AdministratifMetierServiceInterface monProxyAdministratif;
	/**
	 * proxy pour faire appel aux infos sur les résultats du WS .
	 */
	protected PedagogiqueMetierServiceInterface monProxyPedagogique;
	/**
	 * bean permettant l'accès aux photos.
	 */
	private IPhoto photo;
	/**
	 * bean pour convertir un login en l'email correspondant.
	 */
	protected EmailConverterInterface emailConverter;

	/**
	 * constructeur vide.
	 *
	 */
	public EtudiantAmue() {
		super();
		try {
			monProxyAdministratif =  new AdministratifMetierServiceInterfaceProxy();
			monProxyEtu = new EtudiantMetierServiceInterfaceProxy();
			monProxyGeo = new GeographieMetierServiceInterfaceProxy();
			monProxyPedagogique = new PedagogiqueMetierServiceInterfaceProxy();
		} catch(Exception e) {
			LOG.error(e);
			LOG.error("Probleme avec le WS : connexion impossible, création des proxys échouée.");
		}
	}

	/**
	 * @see org.esupportail.mondossierweb.domain.beans.IEtudiant#setCodEtu(org.esupportail.mondossierweb.domain.beans.Etudiant)
	 */
	public void setCodInd(Etudiant e) {
		try {

			IdentifiantsEtudiantDTO idetu = monProxyEtu.recupererIdentifiantsEtudiant(e.getCod_etu(),"", "", "", "", "", "", "", "","N");

			e.setCod_ind(idetu.getCodInd().toString());

		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche du codetu pour etudiant dont codetu est : " + e.getCod_etu());

			ExceptionServiceFactory exceptionServiceFactory  = 
				(ExceptionServiceFactory) BeanUtils.getBean("exceptionServiceFactory");
			ExceptionService exceptionService=exceptionServiceFactory.getExceptionService();
			exceptionService.setParameters(ex);
			exceptionService.handleException();

		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche du codetu pour etudiant dont codetu est : " + e.getCod_etu());
		}
	}

	/**
	 * @see org.esupportail.mondossierweb.domain.beans.IEtudiant#setPhoto(org.esupportail.mondossierweb.domain.beans.Etudiant)
	 */
	public void setPhoto(Etudiant e){
		e.setUrlphoto(photo.getUrlPhoto(e.getCod_ind(),e.getCod_etu()));
	}
	/**
	 * va chercher et renseigne les informations concernant l'état-civil de 
	 * l'étudiant placé en paramètre via le WS de l'Amue.
	 * @param e 
	 */
	public void setEtatCivil(Etudiant e) {
		try {
			//informations générales :
			IdentifiantsEtudiantDTO idetu;

			if (!config.isRecupAnnuaire()) {
				idetu = monProxyEtu.recupererIdentifiantsEtudiant(e.getCod_etu(), null, null, null, null, null, null, null, null, "N");
			} else {
				idetu = monProxyEtu.recupererIdentifiantsEtudiant(e.getCod_etu(), null, null, null, null, null, null, null, null, "O");
			}

			e.setCod_ind(idetu.getCodInd().toString());

			//Modification 20/04/2012 gestion des codine null
			if(idetu.getNumeroINE() != null && idetu.getCleINE() != null ){
				e.setCod_nne(idetu.getNumeroINE() + idetu.getCleINE());
			}else{
				e.setCod_nne("");
			}

			//Modification 21/02/2012. Pour ne renseigner la photo que si elle n'est pas renseignée.
			//setPhoto(e);
			e.getUrlphoto();


			if (!config.isRecupAnnuaire()) {
				// on passe par iBATIS pour récupérer l'e-mail.
				e.setEmail(emailConverter.getMail(service.getLoginFromCodEtu(e.getCod_etu())));
			} else {
				//on récupérer l'e-mail grâce au WS.
				e.setEmail(idetu.getEmailAnnuaire());
			}

			InfoAdmEtuDTO iaetu = monProxyEtu.recupererInfosAdmEtu(e.getCod_etu());

			//MODIF POUR UTILISER LE NOM USUEL SI RENSEIGNE 19/09/2012
			if (iaetu.getNomUsuel() != null && !iaetu.getNomUsuel().equals("")){
				e.setNom(iaetu.getNomUsuel()+ " " + iaetu.getPrenom1());
			}else{
				e.setNom( iaetu.getNomPatronymique() + " " + iaetu.getPrenom1());
			}


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

	/**
	 * va chercher et renseigne les informations concernant les adresses de 
	 * l'étudiant placé en paramètre via le WS de l'Amue.
	 * @param e 
	 */
	public void setAdresses(Etudiant e) {
		/**
		 *  ++++++++++++++++++++++++++++++++++++++++++++++++++
		 *  vérifier récupération derniere année inscrption!!!
		 *  ++++++++++++++++++++++++++++++++++++++++++++++++++
		 */
		try {

			String[] annees =  monProxyAdministratif.recupererAnneesIa(e.getCod_etu(), null);

			String annee = annees[annees.length - 1];
			//récupération des coordonnées :

			CoordonneesDTO2 cdto = monProxyEtu.recupererAdressesEtudiant_v2(e.getCod_etu(), annee, "N");

			//récupération des adresses, annuelle et fixe :
			annee = cdto.getAnnee();
			e.setEmailPerso(cdto.getEmail());
			e.setTelPortable(cdto.getNumTelPortable());
			AdresseDTO2 ada = cdto.getAdresseAnnuelle();
			AdresseDTO2 adf = cdto.getAdresseFixe();

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
		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des adresses pour etudiant dont codetu est : " + e.getCod_etu());
		}catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des adresses pour etudiant dont codetu est : " + e.getCod_etu());
		}
	}


	/**
	 * @see org.esupportail.mondossierweb.domain.beans.IEtudiant#setMajAdresses(org.esupportail.mondossierweb.domain.beans.Etudiant)
	 */
	public boolean setMajAdresses(Etudiant e) {
		boolean ok = false;

		try {
			//recup de l'ancienne et modif dessus:
			String[] annees =  monProxyAdministratif.recupererAnneesIa(e.getCod_etu(), null);
			String annee = annees[annees.length - 1];
			CoordonneesDTO2 cdto = monProxyEtu.recupererAdressesEtudiant_v2(e.getCod_etu(), annee , "N");

			//AdresseDTO ad1 = cdto.getAdresseAnnuelle();
			//AdresseDTO ad2 = cdto.getAdresseFixe();

			AdresseMajDTO adanmaj = new AdresseMajDTO();
			AdresseMajDTO adfixmaj = new AdresseMajDTO();

			adanmaj.setLibAd1(e.getAdresseannuelle1());
			adanmaj.setLibAd2(e.getAdresseannuelle2());
			adanmaj.setLibAd3(e.getAdresseannuelle3());
			adanmaj.setNumTel(e.getNumerotelannuel());
			PaysDTO pdtoan = getPays(e.getPaysannuel());
			adanmaj.setCodPays(pdtoan.getCodPay());
			String codePaysFrance = getPays("FRANCE").getCodPay();
			if (pdtoan.getCodPay().equals(codePaysFrance) ) {
				CommuneDTO comdto = getVille(e.getAdresseannuellecp(), e.getVilleannuelle());
				CommuneMajDTO comanmaj = new CommuneMajDTO();
				comanmaj.setCodeInsee(comdto.getCodeInsee());
				comanmaj.setCodePostal(comdto.getCodePostal());
				adanmaj.setCommune(comanmaj);
				adanmaj.setLibAde(null);
			} else {
				adanmaj.setCommune(null);
				adanmaj.setLibAde(e.getAdresseannuelleetranger());
			}



			adfixmaj.setLibAd1(e.getAdressefixe1());
			adfixmaj.setLibAd2(e.getAdressefixe2());
			adfixmaj.setLibAd3(e.getAdressefixe3());
			adfixmaj.setNumTel(e.getNumerotelfixe());
			PaysDTO pdtof = getPays(e.getPaysfixe());
			adfixmaj.setCodPays(pdtof.getCodPay());
			if (pdtof.getCodPay().equals(codePaysFrance) ) {
				CommuneDTO comdto = getVille(e.getAdressefixecp(), e.getVillefixe());
				CommuneMajDTO comfixmaj = new CommuneMajDTO();
				comfixmaj.setCodeInsee(comdto.getCodeInsee());
				comfixmaj.setCodePostal(comdto.getCodePostal());
				adfixmaj.setCommune(comfixmaj);
				adfixmaj.setLibAde(null);
			} else {
				adfixmaj.setCommune(null);
				adfixmaj.setLibAde(e.getAdressefixeetranger());
			}


			CoordonneesMajDTO cdtomaj = new CoordonneesMajDTO();
			cdtomaj.setAnnee(cdto.getAnnee());
			cdtomaj.setTypeHebergement(getTypeHebergement(e.getAdresseannuelleType()).getCodTypeHebergement());
			cdtomaj.setEmail(e.getEmailPerso());
			cdtomaj.setNumTelPortable(e.getTelPortable());
			cdtomaj.setAdresseAnnuelle(adanmaj);
			cdtomaj.setAdresseFixe(adfixmaj);

			monProxyEtu.mettreAJourAdressesEtudiant(cdtomaj, e.getCod_etu());

			ok = true;
		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la maj des adresses de l'etudiant dont codetu est : " + e.getCod_etu());
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la maj des adresses de l'etudiant dont codetu est : " + e.getCod_etu());
		}
		return ok;
	}

	/**
	 * 
	 * @param codepostal
	 * @param nom
	 * @return la communeDTO de la ville dont code et nom sont en parametres
	 */
	private CommuneDTO getVille(final String codepostal, final String nom) {
		String[] ville = service.getCommune(codepostal, nom);

		CommuneDTO cdto = new CommuneDTO(ville[0], ville[1], ville[2]);

		return cdto;

	}

	/**
	 * @param pays
	 * @return la paysDTO correspondant au pays placé en parametre
	 */
	private PaysDTO getPays(final String pays) {
		try{
			String[] pay = service.getPay(pays);
			PaysDTO pdto = new PaysDTO(pay[0], pay[1]);

			return pdto;
		}catch(Exception e){
			LOG.error(e);
			return null;
		}
	}



	/**
	 * @param type
	 * @return la TypeHebergementVO du TypeHebergement placé en parametre
	 */
	private TypeHebergementCourtDTO getTypeHebergement(final String type) {
		TypeHebergementCourtDTO thcdto = new TypeHebergementCourtDTO();

		TypeHebergementDTO[] thvo = monProxyEtu.recupererTypeHebergement(null, null, null);
		int i = 0;
		while (i < thvo.length) {
			if (thvo[i].getLibTypeHebergement().equals(type)) {
				thcdto.setCodTypeHebergement(thvo[i].getCodTypeHebergement());
				thcdto.setLibTypeHebergement(thvo[i].getLibTypeHebergement());
				thcdto.setLibWebTypeHebergement(thvo[i].getLibWebTypeHebergement());

				return thcdto;
			}
			i++;
		}
		return null;
	}


	/**
	 * va chercher et renseigne les informations concernant les inscriptions de 
	 * l'étudiant placé en paramètre via le WS de l'Amue.
	 * @param e 
	 */
	public void setInscriptions(Etudiant e) {
		try {
			e.getLinsciae().clear();
			e.getLinscdac().clear();

			e.setLib_etb(service.getEtablissementDef());

			//cursus au sein de l'université:

			InsAdmEtpDTO2[] insdtotab = monProxyAdministratif.recupererIAEtapes_v2(e.getCod_etu(), "toutes", "ARE", "ARE");

			for (int i = 0; i < insdtotab.length; i++) {
				Inscription insc = new Inscription();
				InsAdmEtpDTO2 insdto = insdtotab[i];

				//on test si l'inscription n'est pas annulée:
				if (insdto.getEtatIae()!=null && insdto.getEtatIae().getCodeEtatIAE()!=null && insdto.getEtatIae().getCodeEtatIAE().equals("E")){

					//récupération de l'année
					int annee = new Integer(insdto.getAnneeIAE());
					int annee2 = annee + 1;
					insc.setCod_anu(annee + "/" + annee2);

					//récupération des informations sur l'étape
					insc.setCod_etp(insdto.getEtape().getCodeEtp());
					insc.setCod_vrs_vet(insdto.getEtape().getVersionEtp());
					insc.setLib_etp(insdto.getEtape().getLibWebVet());

					//récupération des informations sur le diplôme
					insc.setCod_dip(insdto.getDiplome().getCodeDiplome());
					insc.setVers_dip(insdto.getDiplome().getVersionDiplome());
					insc.setLib_dip(insdto.getDiplome().getLibWebVdi());

					//récupération des informations sur la composante
					insc.setCod_comp(insdto.getComposante().getCodComposante());
					insc.setLib_comp(insdto.getComposante().getLibComposante());

					//récupération de l'état en règle de l'inscription
					if(insdto.getInscriptionPayee().equals(LIBELLE_WS_INSCRIPTION_PAYEE)){
						insc.setEstEnRegle(true);
					}else{
						insc.setEstEnRegle(false);
					}

					//ajout de l'inscription à la liste
					e.getLinsciae().add(0, insc);
				}
			}


			//Autres cursus : 

			CursusExternesEtTransfertsDTO ctdto = monProxyAdministratif.recupererCursusExterne(e.getCod_etu());

			if (ctdto != null) {
				CursusExterneDTO[] listeCursusExt = ctdto.getListeCursusExternes();
				for (int i = 0; i < listeCursusExt.length; i++) {

					Inscription insc = new Inscription();

					CursusExterneDTO cext = listeCursusExt[i];

					int annee = new Integer(cext.getAnnee());
					int annee2 = annee + 1;
					insc.setCod_anu(annee + "/" + annee2);

					if (cext.getEtablissement() != null && cext.getTypeAutreDiplome() != null) {
						insc.setLib_etb(cext.getEtablissement().getLibEtb());
						// 24/04/2012 utilisation du libTypeDiplome a la place du CodeTypeDiplome
						insc.setCod_dac(cext.getTypeAutreDiplome().getLibTypeDiplome());
						insc.setLib_cmt_dac(cext.getCommentaire());
						if (cext.getTemObtentionDip() != null && cext.getTemObtentionDip().equals("N") ) {
							insc.setRes("AJOURNE");
						} else {
							insc.setRes("OBTENU");
						}

						e.getLinscdac().add(0, insc);
					}
				}
			}


			//première inscription universitaire : 
			InfoAdmEtuDTO iaetu = monProxyEtu.recupererInfosAdmEtu(e.getCod_etu());
			if (iaetu != null) {
				e.setAnneePremiereInscrip(iaetu.getAnneePremiereInscUniv());
				e.setEtbPremiereInscrip(iaetu.getEtbPremiereInscUniv().getLibEtb());
			}


		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des inscriptions pour etudiant dont codetu est : " + e.getCod_etu());
			//pour que la page des inscriptions affiche une imposibilité de récupérer les informations.
			e.setLib_etb("");
		} catch(Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des inscriptions pour etudiant dont codetu est : " + e.getCod_etu());
			//pour que la page des inscriptions affiche une imposibilité de récupérer les informations.
			e.setLib_etb("");
		}
	}


	/**
	 * renseigne les attributs concernant l'inscription pédagogique d'une étape choisie
	 * 
	 */
	public void setInscriptionPedagogique(Etudiant e, Etape et) {
		/*
		 * TEST si on a déjà les informations suite a une recherche de notes
		 * sinon on lance la méthode de recherche de notes puis on récupère 
		 * les éléments du résultat pour faire une arborescence de l'inscription pédagogique
		 * 
		 */

		//On regarde si on a pas déjà les infos dans le cache:
		int rang=0;
		boolean enCache=false;

		//on parcourt le résultatElpEpr pour voir si on a ce qu'on cherche:
		for(CacheResultatsElpEpr cree : e.getCacheResultats().getResultElpEpr()){
			if(!enCache){
				//test si on a déjà les infos:
				if(cree.getEtape().getAnnee().equals(et.getAnnee())
						&& cree.getEtape().getCode().equals(et.getCode())
						&& cree.getEtape().getVersion().equals(et.getVersion())){
					enCache=true;
				}else{
					//on a pas trouvé, on incrémente le rang pour se placer sur le rang suivant
					rang++;
				}
			}
		}

		//si on a pas les infos en cache:
		if(!enCache){
			e.getEtudiantManager().setNotesElpEpr(e, et);
			//AJOUT DES INFOS recupérées dans le cache. true car on est en vue Etudiant
			e.ajouterCacheResultatElpEpr(et, true);	
			//on charge les infos récupérèes dans la liste représentant lIP:
			e.recupererCacheIP(e.getCacheResultats().getResultElpEpr().size()-1);
		}else{
			//on récupére les infos du cache grace au rang :
			e.recupererCacheIP(rang);
		}

	}


	/**
	 * va chercher et renseigne les informations concernant le calendrier
	 * des examens de 
	 * l'étudiant placé en paramètre sans le WS de l'Amue.
	 */
	public void setCalendrier(Etudiant e) {
		/**
		 *  copie de etudiantEsup:
		 */
		try {
			e.getCalendrier().clear();
			e.setCalendrier(service.getExamens(e.getCod_ind()));
		} catch(Exception ex) {
			LOG.error(ex);
		}
	}

	/**
	 * va chercher et renseigne les informations concernant les notes
	 * et résultats de l'étudiant placé en paramètre via le WS de l'Amue.
	 */
	public void setNotesEtResultats(Etudiant e) {
		try {
			e.getDiplomes().clear();
			e.getEtapes().clear();

			String temoin = config.getTemoinNotesEtudiant();
			if(temoin == null || temoin.equals("")){
				temoin="T";
			}

			String sourceResultat = config.getSourceResultats();
			if(sourceResultat == null || sourceResultat.equals("")){
				sourceResultat="Apogee";
			}
			// VR 09/11/2009 : Verif annee de recherche si sourceResultat = apogee-extraction :
			// Si different annee en cours => sourceResultat = Apogee
			if(sourceResultat.compareTo("Apogee-extraction")==0){
				// On recupere les resultats dans cpdto avec sourceResultat=Apogee
				sourceResultat="Apogee";
				ContratPedagogiqueResultatVdiVetDTO[] cpdtoResult = monProxyPedagogique.recupererContratPedagogiqueResultatVdiVet(e.getCod_etu(), "toutes", sourceResultat, temoin, "toutes", "tous");

				// Puis dans cpdtoExtract avec sourceResultat=Apogee-extraction pour l'année en cours
				temoin=null;
				sourceResultat="Apogee-extraction";
				String annee = service.getAnneeResEnCours();
				ContratPedagogiqueResultatVdiVetDTO[] cpdtoExtract;
				try {
					cpdtoExtract = monProxyPedagogique.recupererContratPedagogiqueResultatVdiVet(e.getCod_etu(), annee, sourceResultat, temoin, "toutes", "tous");
				} catch (Exception ex) {
					cpdtoExtract = null;
				}

				// Et on fusionne cpdtoResult et cpdtoExtract
				ArrayList<ContratPedagogiqueResultatVdiVetDTO> cpdtoAl = new ArrayList<ContratPedagogiqueResultatVdiVetDTO>();
				for (int i = 0; i < cpdtoResult.length; i++ ) {
					if (cpdtoResult[i].getAnnee() != null) {
						if (cpdtoResult[i].getAnnee().compareTo(annee)!=0) {
							cpdtoAl.add(cpdtoResult[i]);
						}
					}
				}
				if (cpdtoExtract!=null) {
					for (int i = 0; i < cpdtoExtract.length; i++ ) {
						cpdtoAl.add(cpdtoExtract[i]);
					}
				}
				ContratPedagogiqueResultatVdiVetDTO[] cpdto = cpdtoAl.toArray(new ContratPedagogiqueResultatVdiVetDTO[ cpdtoAl.size() ]);
				setNotesEtResultats(e, cpdto);

			} else {

				ContratPedagogiqueResultatVdiVetDTO[] cpdto = monProxyPedagogique.recupererContratPedagogiqueResultatVdiVet(e.getCod_etu(), "toutes", sourceResultat, temoin, "toutes", "tous");
				setNotesEtResultats(e, cpdto);
			}


		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats pour etudiant dont codetu est : " + e.getCod_etu());
			LOG.error(ex.getLastErrorMsg());
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats pour etudiant dont codetu est : " + e.getCod_etu());
		}

	}

	/**
	 * @see org.esupportail.mondossierweb.domain.beans.IEtudiant#setNotesEtResultatsEnseignant(org.esupportail.mondossierweb.domain.beans.Etudiant)
	 */
	public void setNotesEtResultatsEnseignant(Etudiant e) {
		try {
			e.getDiplomes().clear();
			e.getEtapes().clear();

			String temoin = config.getTemoinNotesEnseignant();
			if(temoin == null || temoin.equals("")){
				temoin="AET";
			}

			String sourceResultat = config.getSourceResultats();
			if(sourceResultat == null || sourceResultat.equals("")){
				sourceResultat="Apogee";
			}

			// VR 09/11/2009 : Verif annee de recherche si sourceResultat = apogee-extraction :
			// Si different annee en cours => sourceResultat = Apogee
			if(sourceResultat.compareTo("Apogee-extraction")==0){
				// On recupere les resultats dans cpdto avec sourceResultat=Apogee
				sourceResultat="Apogee";
				ContratPedagogiqueResultatVdiVetDTO[] cpdtoResult = monProxyPedagogique.recupererContratPedagogiqueResultatVdiVet(e.getCod_etu(), "toutes", sourceResultat, temoin, "toutes", "tous");
				// Puis dans cpdtoExtract avec sourceResultat=Apogee-extraction pour l'année en cours
				temoin=null;
				sourceResultat="Apogee-extraction";
				String annee = service.getAnneeResEnCours();
				ContratPedagogiqueResultatVdiVetDTO[] cpdtoExtract;
				try {
					cpdtoExtract = monProxyPedagogique.recupererContratPedagogiqueResultatVdiVet(e.getCod_etu(), annee, sourceResultat, temoin, "toutes", "tous");
				} catch (Exception ex) {
					cpdtoExtract = null;
				}

				// Et on fusionne cpdtoResult et cpdtoExtract
				ArrayList<ContratPedagogiqueResultatVdiVetDTO> cpdtoAl = new ArrayList<ContratPedagogiqueResultatVdiVetDTO>();
				for (int i = 0; i < cpdtoResult.length; i++ ) {
					if (cpdtoResult[i].getAnnee() != null) {
						if (cpdtoResult[i].getAnnee().compareTo(annee)!=0) {
							cpdtoAl.add(cpdtoResult[i]);
						}
					}
				}
				if (cpdtoExtract!=null) {
					for (int i = 0; i < cpdtoExtract.length; i++ ) {
						cpdtoAl.add(cpdtoExtract[i]);
					}
				}
				ContratPedagogiqueResultatVdiVetDTO[] cpdto = cpdtoAl.toArray(new ContratPedagogiqueResultatVdiVetDTO[ cpdtoAl.size() ]);
				setNotesEtResultats(e, cpdto);

			} else {

				ContratPedagogiqueResultatVdiVetDTO[] cpdto = monProxyPedagogique.recupererContratPedagogiqueResultatVdiVet(e.getCod_etu(), "toutes", sourceResultat, temoin, "toutes", "tous");
				setNotesEtResultats(e, cpdto);
			}

		} catch (WebBaseException ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats pour etudiant dont codetu est : " + e.getCod_etu());
			LOG.error(ex.getLastErrorMsg());
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats pour etudiant dont codetu est : " + e.getCod_etu());
		}

	}

	/**
	 * renseigne les attributs concernant les notes et résultats obtenus.
	 * @param e
	 * @param cpdto
	 */
	public void setNotesEtResultats(Etudiant e, ContratPedagogiqueResultatVdiVetDTO[] resultatVdiVet) {
		try {


			e.getDiplomes().clear();
			e.getEtapes().clear();
			//Si on a configure pour toujours afficher le rang, on affichera les rangs de l'étudiant.
			e.setAfficherRang(config.isAffRangEtudiant());

			for (int i = 0; i < resultatVdiVet.length; i++ ) {
				//information sur le diplome:
				ContratPedagogiqueResultatVdiVetDTO rdto = resultatVdiVet[i];

				if(rdto.getDiplome() != null){
					Diplome d = new Diplome();

					d.setLib_web_vdi(rdto.getDiplome().getLibWebVdi());
					d.setCod_dip(rdto.getDiplome().getCodDip());
					d.setCod_vrs_vdi(rdto.getDiplome().getCodVrsVdi().toString());
					//System.out.println("coddip : "+d.getCod_dip() + " " + d.getLib_web_vdi());
					//System.out.println("vrsdip : "+d.getCod_vrs_vdi());

					int annee2 = new Integer(rdto.getAnnee()) + 1;


					d.setAnnee(rdto.getAnnee() + "/" + annee2);
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

							//recuperation de la mention
							if(res.getMention() != null){
								r.setCodMention(res.getMention().getCodMen());
								r.setLibMention(res.getMention().getLibMen());
							}

							String result="";
							if( res.getTypResultat()!=null){
								result= res.getTypResultat().getCodTre();
								r.setAdmission(result);
							}
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

							//Gestion du barème:
							if(res.getBarNotVdi() != null){
								r.setBareme(res.getBarNotVdi());
							}

							//ajout de la signification du résultat dans la map
							if ((result != null && !result.equals("")) && !e.getSignificationResultats().containsKey(r.getAdmission())) {
								e.getSignificationResultats().put(r.getAdmission(), res.getTypResultat().getLibTre());
							}

							//ajout du résultat au diplome:
							d.getResultats().add(r);
							if(res.getNbrRngEtuVdi() != null && !res.getNbrRngEtuVdi().equals("")){
								d.setRang(res.getNbrRngEtuVdi()+"/"+res.getNbrRngEtuVdiTot());
								//On indique si on affiche le rang du diplome.
								d.setAfficherRang(config.isAffRangEtudiant());

							}
						}
						//ajout du diplome si on a au moins un résultat
						//e.getDiplomes().add(0, d);
					}
					e.getDiplomes().add(0, d);
				}
				//information sur les etapes:
				EtapeResVdiVetDTO[] etapes = rdto.getEtapes();
				if (etapes != null && etapes.length > 0) {

					for (int j = 0; j < etapes.length; j++ ) {
						EtapeResVdiVetDTO etape = etapes[j];

						//29/01/10
						//on rejete les etapes annulée. MAJ sur proposition de Rennes1
						if((etape.getCodEtaIae()== null) || (etape.getCodEtaIae()!= null && !etape.getCodEtaIae().equals("A"))){

							Etape et = new Etape();
							int anneeEtape = new Integer(etape.getCodAnu());
							et.setAnnee(anneeEtape + "/" + (anneeEtape + 1));
							et.setCode(etape.getEtape().getCodEtp());
							et.setVersion(etape.getEtape().getCodVrsVet().toString());
							et.setLibelle(etape.getEtape().getLibWebVet());

							//ajout 16/02/2012 pour WS exposés pour la version mobile en HttpInvoker
							if(rdto.getDiplome()!= null){
								et.setCod_dip(rdto.getDiplome().getCodDip());
								et.setVers_dip(rdto.getDiplome().getCodVrsVdi());
							}

							//résultats de l'étape:
							ResultatVetDTO[] tabresetape = etape.getResultatVet();
							if (tabresetape != null && tabresetape.length > 0) {
								for (int k = 0; k < tabresetape.length; k++ ) {
									ResultatVetDTO ret = tabresetape[k];
									Resultat r = new Resultat();
									//System.out.println("credit etape : "+ret.getNbrCrdVet());
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
									//recuperation de la mention
									if(ret.getMention() != null){
										r.setCodMention(ret.getMention().getCodMen());
										r.setLibMention(ret.getMention().getLibMen());
									}

									//System.out.println("session : " + ret.getSession().getLibSes()+" "+	ret.getSession().getCodSes());
									String result="";
									if(ret.getTypResultat() != null){
										result = ret.getTypResultat().getCodTre();
										r.setAdmission(result);
									}
									if (ret.getNotVet() != null) {
										r.setNote(ret.getNotVet().toString());
										//ajout note jury
										if(ret.getNotPntJurVet() != null && !ret.getNotPntJurVet().equals(new BigDecimal(0))){
											r.setNote(r.getNote()+"(+"+ret.getNotPntJurVet()+")");
										}

									} else {
										if (result.equals("DEF")) {
											r.setNote("DEF");
										}
									}

									//Gestion du barème:
									if(ret.getBarNotVet() != null){
										r.setBareme(ret.getBarNotVet());
									}

									//ajout de la signification du résultat dans la map
									if (result != null && !result.equals("") && !e.getSignificationResultats().containsKey(r.getAdmission())) {
										e.getSignificationResultats().put(r.getAdmission(), ret.getTypResultat().getLibTre());
									}

									//si le resultat n'est pas deja ajouté on l'ajoute:
									/*	boolean ajoutRes = true;
									for(Resultat resu : et.getResultats()){
										if(resu.getSession().equals(r.getSession())
												&& resu.getAdmission().equals(r.getAdmission())){
											if(resu.getNote() == null
													&& r.getNote() == null){
												ajoutRes=false;
											}else{
												if(resu.getNote() != null
														&& r.getNote() != null){
													if(resu.getNote().equals(r.getNote())) {
														ajoutRes=false;
													}
												}
											}
										}
									}*/

									//ajout du résultat
									et.getResultats().add(r);

									//ajout du rang
									if(ret.getNbrRngEtuVet() != null && !ret.getNbrRngEtuVet().equals("")){
										et.setRang(ret.getNbrRngEtuVet()+"/"+ret.getNbrRngEtuVetTot());
										//On calcule si on affiche ou non le rang.
										boolean cetteEtapeDoitEtreAffiche=false;
										for(String codetape : config.getCodesEtapeAffichageRang()){
											if(codetape.equals(et.getCode())){
												cetteEtapeDoitEtreAffiche=true;
											}
										}
										if(config.isAffRangEtudiant() || cetteEtapeDoitEtreAffiche){
											//On affichera le rang de l'étape.
											et.setAfficherRang(true);
											//On remonte au niveau de l'étudiant qu'on affiche le rang
											e.setAfficherRang(true);
										}
									}

								}
							}

							//ajout de l'étape a la liste d'étapes de l'étudiant:
							//e.getEtapes().add(0, et);
							//en attendant la maj du WS :
							insererEtapeDansListeTriee(e, et);

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


	private void insererEtapeDansListeTriee(Etudiant e, Etape et){

		boolean insere = false;
		int rang = 0;
		int anneeEtape = new Integer(et.getAnnee().substring(0, 4));
		//LOG.info("anneeEtape : "+anneeEtape);
		while(!insere && rang < e.getEtapes().size()){

			int anneeEtapeEnCours = new Integer(e.getEtapes().get(rang).getAnnee().substring(0, 4));
			//LOG.info("anneeEtapeEnCours : "+anneeEtapeEnCours);
			if(anneeEtape > anneeEtapeEnCours){
				e.getEtapes().add(rang, et);
				insere = true;
				//	LOG.info("etape inseree");
			}
			rang++;
		} 
		if(!insere){
			e.getEtapes().add(et);
			//LOG.info("etape inseree en fin");
		}
	}



	/**
	 * va chercher et renseigne les informations concernant les notes
	 * et résultats des éléments de l'etape choisie
	 * de l'étudiant placé en paramètre via le WS de l'Amue.
	 */
	public void setNotesElpEpr(Etudiant e, Etape et) {

		try {
			e.getElementsPedagogiques().clear();

			String temoin = config.getTemoinNotesEtudiant();
			if(temoin == null || temoin.equals("")){
				temoin="T";
			}
			String sourceResultat = config.getSourceResultats();
			if(sourceResultat == null || sourceResultat.equals("")){
				sourceResultat="Apogee";
			}

			// VR 09/11/2009 : Verif annee de recherche si sourceResultat = apogee-extraction :
			// Si different annee en cours => sourceResultat = Apogee
			if(sourceResultat.compareTo("Apogee-extraction")==0){
				String annee = service.getAnneeEnCours();
				if (et.getAnnee().substring(0, 4).compareTo(annee)==0) {
					sourceResultat="Apogee-extraction";
					temoin=null;
				} else {
					sourceResultat="Apogee";
				}
			}
			//07/09/10
			if(sourceResultat.compareTo("Apogee-extraction")==0){
				//07/09/10
				//on prend le témoin pour Apogee-extraction
				ContratPedagogiqueResultatElpEprDTO4[] cpdto = monProxyPedagogique.recupererContratPedagogiqueResultatElpEpr_v5(e.getCod_etu(), et.getAnnee().substring(0, 4), et.getCode(), et.getVersion(), sourceResultat, temoin, "toutes", "tous");
				//29/01/10
				//on est dans le cas d'une extraction apogée
				setNotesElpEpr(e, et, cpdto,"AET");
			}else{
				//29/01/10
				//On récupère pour tout les états de délibération et on fera le trie après
				ContratPedagogiqueResultatElpEprDTO4[] cpdto = monProxyPedagogique.recupererContratPedagogiqueResultatElpEpr_v5(e.getCod_etu(), et.getAnnee().substring(0, 4), et.getCode(), et.getVersion(), sourceResultat, "AET", "toutes", "tous");
				setNotesElpEpr(e, et, cpdto,temoin);
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
	}

	/**
	 * @see org.esupportail.mondossierweb.domain.beans.IEtudiant#setNotesElpEprEnseignant(org.esupportail.mondossierweb.domain.beans.Etudiant, org.esupportail.mondossierweb.domain.beans.Etape)
	 */
	public void setNotesElpEprEnseignant(Etudiant e, Etape et) {
		try {

			e.getElementsPedagogiques().clear();

			String temoin = config.getTemoinNotesEnseignant();
			if(temoin == null || temoin.equals("")){
				temoin="AET";
			}
			String sourceResultat = config.getSourceResultats();
			if(sourceResultat == null || sourceResultat.equals("")){
				sourceResultat="Apogee";
			}

			// VR 09/11/2009 : Verif annee de recherche si sourceResultat = apogee-extraction :
			// Si different annee en cours => sourceResultat = Apogee
			if(sourceResultat.compareTo("Apogee-extraction")==0){
				String annee = service.getAnneeEnCours();
				if (et.getAnnee().substring(0, 4).compareTo(annee)==0) {
					sourceResultat="Apogee-extraction";
					temoin=null;
				} else {
					sourceResultat="Apogee";
				}
			}

			// 07/12/11 récupération du fonctionnement identique à la récupéraition des notes pour les étudiants.
			if(sourceResultat.compareTo("Apogee-extraction")==0){
				ContratPedagogiqueResultatElpEprDTO4[] cpdto = monProxyPedagogique.recupererContratPedagogiqueResultatElpEpr_v5(e.getCod_etu(), et.getAnnee().substring(0, 4), et.getCode(), et.getVersion(), sourceResultat, temoin, "toutes", "tous");
				setNotesElpEpr(e, et, cpdto,"AET");
			}else{
				ContratPedagogiqueResultatElpEprDTO4[] cpdto = monProxyPedagogique.recupererContratPedagogiqueResultatElpEpr_v5(e.getCod_etu(), et.getAnnee().substring(0, 4), et.getCode(), et.getVersion(), sourceResultat, "AET", "toutes", "tous");
				setNotesElpEpr(e, et, cpdto,temoin);
			}



		} catch (WebBaseException ex) {
			//on catch le cas ou les inscriptions aux elp de l'étape ne soient pas effectués.
			if (!ex.getNature().equals("nullretrieve") && !ex.getDomaine().equals("data") && !ex.getElement().equals("elp") && !ex.getType().equals("tecnical")){
				LOG.error(ex);
			}
			LOG.error(ex.getLastErrorMsg()+" pour etudiant dont codind est : " + e.getCod_etu());
		}catch (Exception ex) {
			LOG.error(ex);
			LOG.error("Probleme avec le WS lors de la recherche des notes et résultats a une étape pour etudiant dont codind est : " + e.getCod_etu());
		}
	}



	public void setNotesElpEpr(Etudiant e, Etape et, ContratPedagogiqueResultatElpEprDTO4[] reedto,String temoinEtatDelib) {
		try {

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
					elp.setEpreuve(false);


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
					elp.setTemFictif(reedto[i].getElp().getNatureElp().getTemFictif());

					//vrai si l'ELP est il dans un etat de delib qui nous convient en session1:
					boolean elpEtatDelibS1OK=false;

					//vrai si l'ELP est il dans un etat de delib qui nous convient en session2:
					boolean elpEtatDelibS2OK=false;

					//On s'occupe des résultats :
					ResultatElpDTO3[] relpdto = reedto[i].getResultatsElp();
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
									}



									//CAS DE NON OBTENTION PAR CORRESPONDANCE.
									if(relpdto[j].getLcc() == null) {

										//ajout de la signification du résultat dans la map
										if (result != null && !result.equals("") && !e.getSignificationResultats().containsKey(result)) {
											e.getSignificationResultats().put(result, relpdto[j].getTypResultat().getLibTre());
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

							//Modif 20/02/2012 pour les WS HttpInvoker
							//elp2.setAnnee("epreuve");
							elp2.setAnnee("");
							elp2.setEpreuve(true);

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
									int codsession = new Integer(repdto[k].getSession().getCodSes());
									//09/01/13
									//On recupere la note si :
									//  On a reseigné une liste de type épreuve à afficher et le type de l'épreuve en fait partie
									//  OU SI :
									//      le témoin d'avc fait partie de la liste des témoins paramétrés 
									//      OU si le témoin d'avc de  l'elp pere fait partie de la liste des témoins paramétrés 
									//      OU si le témoin TemCtlValCadEpr est égal au parametre TemoinCtlValCadEpr de monDossierWeb.xml.
									boolean recuperationNote = false;

									if(config.getTypesEpreuveAffichageNote() != null && config.getTypesEpreuveAffichageNote().size()>0){
										//On a renseigné une liste de type épreuve à afficher
										for(String typeEpreuve : config.getTypesEpreuveAffichageNote()){
											if(typeEpreuve.equals(epreuve.getEpreuve().getTypEpreuve().getCodTep())){
												recuperationNote = true;
											}
										}
									}
									if(!recuperationNote){
										//On n'a pas renseigné de liste de type épreuve à afficher ou celui ci n'était pas dans la liste
										if (codsession < 2) {
											if(temoinEtatDelib.contains(repdto[k].getEtatDelib().getCodEtaAvc()) || elpEtatDelibS1OK || TemCtlValCadEpr.equals(config.getTemoinCtlValCadEpr()))
												recuperationNote = true;
										}else{
											if(temoinEtatDelib.contains(repdto[k].getEtatDelib().getCodEtaAvc()) || elpEtatDelibS2OK || TemCtlValCadEpr.equals(config.getTemoinCtlValCadEpr()))
												recuperationNote = true;
										}
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
			//ajout des éléments dans la liste de l'étudiant en commençant par la ou les racine
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
					if (elp.isEpreuve()) {
						ElementPedagogique elp0 = e.getElementsPedagogiques().get(i - 1);
						if (i < (e.getElementsPedagogiques().size() - 1)) {
							ElementPedagogique elp1 = e.getElementsPedagogiques().get(i + 1);
							if (!elp0.isEpreuve() && !elp1.isEpreuve()) {
								if (elp0.getNote1().equals(elp.getNote1()) && elp0.getNote2().equals(elp.getNote2())) {
									//on supprime l'element i
									e.getElementsPedagogiques().remove(i);
									suppr = true;
								}
							}
						} else {
							if (!elp0.isEpreuve() && elp0.getNote1().equals(elp.getNote1()) && elp0.getNote2().equals(elp.getNote2())) {
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
                                        el.setMobLibelle(lib); // ajout P1 libelle pour la vue mobile (sans indentation)
					el.setLibelle(el.getLibelle() + lib);
				}
			}

			//Gestion des temoins fictif si temoinFictif est renseigné dans monDossierWeb.xml
			if(config.getTemoinFictif()!=null && !config.getTemoinFictif().equals("")){
				if (e.getElementsPedagogiques().size() > 0) {
					List<Integer> listeRangAsupprimer=new LinkedList<Integer>();
					int rang = 0;
					//on note les rangs des éléments à supprimer
					for (ElementPedagogique el : e.getElementsPedagogiques()) {
						if(el.getTemFictif()!= null && !el.getTemFictif().equals("") && !el.getTemFictif().equals(config.getTemoinFictif())){
							//on supprime l'élément de la liste
							listeRangAsupprimer.add(rang);
						}
						rang++;
					}
					//on supprime les éléments de la liste
					int NbElementSupprimes = 0;
					for(Integer rg:listeRangAsupprimer){
						e.getElementsPedagogiques().remove(rg - NbElementSupprimes);
						NbElementSupprimes++;
					}
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
	 * ajoute les éléments dans la liste d'éléments de l'étudiant en corrigeant les levels (rangs).
	 * @param elp
	 * @param liste1
	 * @param e
	 * @param niveauDuPere 
	 */
	protected void insererElmtPedagoFilsDansListe(ElementPedagogique elp, List<ElementPedagogique> liste1, Etudiant e, int niveauDuPere) {
		for (ElementPedagogique el : liste1) {
			if (el.getCodElpSup() != null && !el.getCodElpSup().equals("")) {
				if (el.getCodElpSup().equals(elp.getCode()) && !el.getCode().equals(elp.getCode())) {
					//on affiche pas les FICM :
					if (!el.getAnnee().equals("FICM")) {
						el.setLevel(niveauDuPere + 1);
						e.getElementsPedagogiques().add(el);
					}
					//On test si on est pas sur une epreuve pour eviter les boucle infini dans le cas ou codEpr=CodElpPere
					if(!el.getAnnee().equals("epreuve"))
						insererElmtPedagoFilsDansListe(el, liste1, e, niveauDuPere + 1);
				}
			}
		}
	}






	public IDaoService getService() {
		return service;
	}

	public void setService(IDaoService service) {
		this.service = service;
	}

	public EmailConverterInterface getEmailConverter() {
		return emailConverter;
	}

	public void setEmailConverter(EmailConverterInterface emailConverter) {
		this.emailConverter = emailConverter;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public IPhoto getPhoto() {
		return photo;
	}

	public void setPhoto(IPhoto photo) {
		this.photo = photo;
	}

	public void setCalendrierRentree(Etudiant etudiant) {
		// TODO Auto-generated method stub

	}







}
