/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;


import gouv.education.apogee.commun.client.ws.etudiantmetier.EtudiantMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.client.ws.geographiemetier.GeographieMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.servicesmetiers.OffreFormationMetierServiceInterface;
import gouv.education.apogee.commun.servicesmetiers.EtudiantMetierServiceInterface;
import gouv.education.apogee.commun.servicesmetiers.GeographieMetierServiceInterface;
import gouv.education.apogee.commun.client.ws.offreformationmetier.OffreFormationMetierServiceInterfaceProxy;
import gouv.education.apogee.commun.transverse.dto.etudiant.TypeHebergementDTO;
import gouv.education.apogee.commun.transverse.dto.geographie.CommuneDTO;
import gouv.education.apogee.commun.transverse.dto.geographie.PaysDTO;
import gouv.education.apogee.commun.transverse.dto.scolarite.CollectionDTO2;
import gouv.education.apogee.commun.transverse.dto.scolarite.ElementPedagogiDTO;
import gouv.education.apogee.commun.transverse.dto.scolarite.GroupeDTO;
import gouv.education.apogee.commun.transverse.dto.scolarite.RecupererGroupeDTO;

import java.util.LinkedList;
import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.domain.beans.CollectionDeGroupes;
import org.esupportail.mondossierweb.domain.beans.Composante;
import org.esupportail.mondossierweb.domain.beans.Diplome;
import org.esupportail.mondossierweb.domain.beans.ElementPedagogique;
import org.esupportail.mondossierweb.domain.beans.ElpDeCollection;
import org.esupportail.mondossierweb.domain.beans.Etape;
import org.esupportail.mondossierweb.domain.beans.Examen;
import org.esupportail.mondossierweb.domain.beans.Groupe;
import org.esupportail.mondossierweb.domain.beans.Inscrit;
import org.esupportail.mondossierweb.domain.beans.Signataire;
import org.esupportail.mondossierweb.dto.ObjetRecherche;
import org.esupportail.mondossierweb.dto.ParamRequeteDTO;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;



/**
 * contient toute les méthodes permettant d'aller 
 * chercher les informations utiles dans Apogée.
 * @author Charlie Dubois
 */
public class DaoServiceIBatisImpl extends SqlMapClientDaoSupport implements IDaoService {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(DaoServiceIBatisImpl.class);
	/**
	 * la DAO pour récupérer le login d'un étudiant.
	 */
	private IDaoCodeLoginEtudiant daoCodeLogin;

	/**
	 * la DAO pour récupérer le codind d'un étudiant.
	 */
	private IDaoLoginCodeEtudiant daoLoginCode;
	/**
	 * proxy pour faire appel aux infos géographique du WS .
	 */
	private GeographieMetierServiceInterface monProxyGeo;
	/**
	 * proxy pour faire appel aux infos sur l'étudiant WS .
	 */
	private EtudiantMetierServiceInterface monProxyEtu;
	/**
	 * proxy pour faire appel aux infos sur l'étudiant WS .
	 */
	private OffreFormationMetierServiceInterface monProxyOffreDeFormation;


	/**
	 * constructeur vide.
	 */
	public DaoServiceIBatisImpl() {
		super();
		monProxyGeo = new GeographieMetierServiceInterfaceProxy();
		monProxyEtu = new EtudiantMetierServiceInterfaceProxy();
		monProxyOffreDeFormation = new OffreFormationMetierServiceInterfaceProxy();

	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getCodInd(java.lang.String)
	 */
	public String getCodEtuFromLogin(final String login) {
		return  daoCodeLogin.getCodEtuFromLogin(login);
	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getCodIndFromCodEtu(java.lang.String)
	 */
	public String getCodIndFromCodEtu(final String id) {
		return (String) getSqlMapClientTemplate().queryForObject("Etudiant.getCodIndFromCodEtu", id);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getLogin(java.lang.String)
	 */
	public String getLoginFromCodEtu(final String codetu) {
		return daoLoginCode.getLoginFromCodEtu(codetu);
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getCodUti(java.lang.String)
	 */
	public String getCodUti(final String id) {
		String code = "";
		try {
			code = (String ) getSqlMapClientTemplate().queryForObject("Utilisateur.getCodUti", id);
		} catch(Exception e) {
			LOG.error(e);
			return "-";
		}
		if (code != null) {
			return code;
		}
		return "";
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getTypesHebergement()
	 */
	public TypeHebergementDTO[] getTypesHebergement() {
		return monProxyEtu.recupererTypeHebergement(null, null, null);
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getPays()
	 */
	public PaysDTO[] getPays() {
		return monProxyGeo.recupererPays(null, "O");
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getPay(java.lang.String)
	 */
	public String[] getPay(final String pays) {
		PaysDTO[] pdto = monProxyGeo.recupererPays(null, "O");
		int i = 0;
		while (i < pdto.length) {
			if (pdto[i].getLibPay().equals(pays)) {
				String[] pay = new String[2];
				pay[0] = pdto[i].getLibPay();
				pay[1] = pdto[i].getCodePay();
				return pay;
			}
			i++;
		}
		return null;
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getCommunes(java.lang.String)
	 */
	public CommuneDTO[] getCommunes(final String codp) {
		//return monProxyGeo.recupererCommune(null, null, null);
		return monProxyGeo.recupererCommune(codp,  "O", "T");
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getCommune(java.lang.String)
	 */
	public String[] getCommune(final String codp) {
		//return monProxyGeo.recupererCommune(null, null, null);
		CommuneDTO[]cdto = monProxyGeo.recupererCommune(codp, "O", "T");
		CommuneDTO c = cdto[0];
		String[] ville = new String[3];
		ville[0] = c.getLibCommune();
		ville[1] = c.getCodeCommune();
		ville[2] = c.getCodePostal();

		return ville;

	}

	/**
	 * méthode utile pour les villes ayant le même code postal.
	 */
	public String[] getCommune(final String codp, final String nom) {
		CommuneDTO[]cdto = monProxyGeo.recupererCommune(codp, "O", "T");
		String[] ville = new String[3];
		for (int i = 0; i < cdto.length; i++) {
			CommuneDTO c = cdto[i];
			if (c.getLibCommune().equals(nom)){
				ville[0] = c.getLibCommune();
				ville[1] = c.getCodeCommune();
				ville[2] = c.getCodePostal();
			}
		}

		return ville;

	}




	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#checkEtape(org.esupportail.mondossierweb.domain.beans.Etape)
	 */
	public Integer checkEtape(final Etape e) {
		return (Integer) getSqlMapClientTemplate().queryForObject("Etape.checkEtape", e);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#checkElementPedagogique(java.lang.String)
	 */
	public Integer checkElementPedagogique(final String id) {
		return (Integer) getSqlMapClientTemplate().queryForObject("ElementPedagogique.checkElementPedagogique", id);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsEtape(org.esupportail.mondossierweb.domain.beans.Etape)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsEtape(final Etape e) {
		return getSqlMapClientTemplate().queryForList("Etape.getInscrits", e);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getLibelleEtape(java.lang.String)
	 */
	public String getLibelleEtape(final ObjetRecherche o) {
		return (String) getSqlMapClientTemplate().queryForObject("Etape.getLibelle", o);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsEtapeJuin(org.esupportail.mondossierweb.domain.beans.Etape)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsEtapeJuin(final Etape e) {
		return getSqlMapClientTemplate().queryForList("Etape.getInscritsJuin", e);
	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsEtapeSep(org.esupportail.mondossierweb.domain.beans.Etape)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsEtapeSep(final Etape e) {
		return getSqlMapClientTemplate().queryForList("Etape.getInscritsSep", e);
	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsEtapeJuinSep(org.esupportail.mondossierweb.domain.beans.Etape)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsEtapeJuinSep(final Etape e) {
		return getSqlMapClientTemplate().queryForList("Etape.getInscritsJuinSep", e);
	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getLibelleElementPedagogique(java.lang.String)
	 */
	public String getLibelleElementPedagogique(final String id) {
		return (String) getSqlMapClientTemplate().queryForObject("ElementPedagogique.getLibelle", id);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsElementPedagogique(org.esupportail.mondossierweb.domain.beans.ElementPedagogique)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsElementPedagogique(final ElementPedagogique e) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getInscrits", e);
	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsElementPedagogiqueJuin(org.esupportail.mondossierweb.domain.beans.ElementPedagogique)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsElementPedagogiqueJuin(final ElementPedagogique e) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getInscritsJuin", e);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsElementPedagogiqueSep(org.esupportail.mondossierweb.domain.beans.ElementPedagogique)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsElementPedagogiqueSep(final ElementPedagogique e) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getInscritsSep", e);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsElementPedagogiqueEtape(org.esupportail.mondossierweb.domain.beans.ElementPedagogique)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsElementPedagogiqueEtape(final ElementPedagogique e) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getInscritsEtape", e);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsElementPedagogiqueJuinSep(org.esupportail.mondossierweb.domain.beans.ElementPedagogique)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsElementPedagogiqueJuinSep(final ElementPedagogique e) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getInscritsJuinSep", e);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsElementPedagogiqueJuinEtape(org.esupportail.mondossierweb.domain.beans.ElementPedagogique)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsElementPedagogiqueJuinEtape(final ElementPedagogique e) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getInscritsJuinEtape", e);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsElementPedagogiqueSepEtape(org.esupportail.mondossierweb.domain.beans.ElementPedagogique)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsElementPedagogiqueSepEtape(final ElementPedagogique e) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getInscritsSepEtape", e);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getInscritsElementPedagogiqueJuinSepEtape(org.esupportail.mondossierweb.domain.beans.ElementPedagogique)
	 */
	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsElementPedagogiqueJuinSepEtape(final ElementPedagogique e) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getInscritsJuinSepEtape", e);

	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getEtablissementDef()
	 */
	public String getEtablissementDef() {
		return (String) getSqlMapClientTemplate().queryForObject("Inscriptions.getEtablissementDef", null);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getExamens(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Examen> getExamens(final String id) {
		return getSqlMapClientTemplate().queryForList("Calendrier.getExamens", id);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getExamensEtape(org.esupportail.mondossierweb.dto.ParamRequeteDTO)
	 */
	@SuppressWarnings("unchecked")
	public List<Examen> getExamensEtape(ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("Calendrier.getExamensEtape", param);
	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getPageInfos(java.lang.String)
	 */
	public String getPageInfos(String codcin) {
		return (String) getSqlMapClientTemplate().queryForObject("Calendrier.getPageInfos", codcin);
	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getComposantes()
	 */
	@SuppressWarnings("unchecked")
	public List<Composante> getComposantes() {
		return getSqlMapClientTemplate().queryForList("Composantes.getComposantes", null);
	}


	public String getDiplome(final ObjetRecherche o) {
		return (String) getSqlMapClientTemplate().queryForObject("Diplomes.getDiplome", o);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getDiplomes(org.esupportail.mondossierweb.dto.ObjetRecherche)
	 */
	@SuppressWarnings("unchecked")
	public List<Diplome> getDiplomes(final ObjetRecherche o) {
		return getSqlMapClientTemplate().queryForList("Diplomes.getDiplomes", o);
	}

	public String getCodeTypeDiplome(String codDip){
		return (String) getSqlMapClientTemplate().queryForObject("Diplomes.getCodeTypeDiplome", codDip);
	}
	
	/*public List<Diplome> getDiplomeByEtape(ObjetRecherche o) {
		return getSqlMapClientTemplate().queryForList("Diplomes.getDiplomeByEtape", o);
	}*/


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getEtape(org.esupportail.mondossierweb.dto.ObjetRecherche)
	 */
	@SuppressWarnings("unchecked")
	public List<Etape> getEtape(final ObjetRecherche o) {
		return getSqlMapClientTemplate().queryForList("Etape.getEtape", o);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getElements(org.esupportail.mondossierweb.domain.beans.Etape)
	 */
	@SuppressWarnings("unchecked")
	public List<ElementPedagogique> getElements(final Etape e) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getElements", e);
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getSousElements(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<ElementPedagogique> getSousElements(final String id) {
		return getSqlMapClientTemplate().queryForList("ElementPedagogique.getSousElements", id);

	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#hasSousElements(java.lang.String)
	 */
	public boolean hasSousElements(final String id) {
		boolean b = false;
		String s = (String) getSqlMapClientTemplate().queryForObject("ElementPedagogique.hasSousElements", id);
		int compteur = new Integer(s);
		if (compteur > 0) {
			b = true;
		}
		return b;
	}

	public String getFormationEnCours(final String codind) {
		List<String> l = getSqlMapClientTemplate().queryForList("Etape.getFormationEnCours", codind);
		if (l.size() > 0) {
			return l.get(0);
		}
		return "";
	}

	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getAnneeEnCours()
	 */
	public String getAnneeEnCours() {
		return (String) getSqlMapClientTemplate().queryForObject("Annees.getAnneeEnCours", null);
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getAnneeUniversitaireEnCours()
	 */
	public String getAnneeUniversitaireEnCours() {
		return (String) getSqlMapClientTemplate().queryForObject("Annees.getAnneeUniversitaireEnCours", null);
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getAnneeResEnCours()
	 */
	public String getAnneeResEnCours() {
		return (String) getSqlMapClientTemplate().queryForObject("Annees.getAnneeResEnCours", null);
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getAnneeUniversitaireEnCours()
	 */
	public String getAnneeUniversitaireResEnCours() {
		return (String) getSqlMapClientTemplate().queryForObject("Annees.getAnneeUniversitaireResEnCours", null);
	}


	/**
	 * @return daoCodeLogin
	 */
	public IDaoCodeLoginEtudiant getDaoCodeLogin() {
		return daoCodeLogin;
	}


	/**
	 * @param daoCodeLogin
	 */
	public void setDaoCodeLogin(final IDaoCodeLoginEtudiant daoCodeLogin) {
		this.daoCodeLogin = daoCodeLogin;
	}


	/**
	 * @return daoLoginCode
	 */
	public IDaoLoginCodeEtudiant getDaoLoginCode() {
		return daoLoginCode;
	}


	/**
	 * @param daoLoginCode
	 */
	public void setDaoLoginCode(final IDaoLoginCodeEtudiant daoLoginCode) {
		this.daoLoginCode = daoLoginCode;
	}


	/**
	 * @return monProxyEtu
	 */
	public EtudiantMetierServiceInterface getMonProxyEtu() {
		return monProxyEtu;
	}


	/**
	 * @param monProxyEtu
	 */
	public void setMonProxyEtu(final EtudiantMetierServiceInterface monProxyEtu) {
		this.monProxyEtu = monProxyEtu;
	}


	/**
	 * @return monProxyGeo
	 */
	public GeographieMetierServiceInterface getMonProxyGeo() {
		return monProxyGeo;
	}


	/**
	 * @param monProxyGeo
	 */
	public void setMonProxyGeo(final GeographieMetierServiceInterface monProxyGeo) {
		this.monProxyGeo = monProxyGeo;
	}



	public OffreFormationMetierServiceInterface getMonProxyOffreDeFormation() {
		return monProxyOffreDeFormation;
	}


	public void setMonProxyOffreDeFormation(
			OffreFormationMetierServiceInterface monProxyOffreDeFormation) {
		this.monProxyOffreDeFormation = monProxyOffreDeFormation;
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoService#getSignataire(java.lang.String)
	 */
	public Signataire getSignataire(String id) {
		return (Signataire) getSqlMapClientTemplate().queryForObject("Signataire.getSignataire", id);
	}



	public List<ElpDeCollection> recupererGroupes(String annee, String codElp) {
		//appel WS Offre de foramtion 'recupererGroupe'
		List<ElpDeCollection> listeElp = new LinkedList<ElpDeCollection>();

		RecupererGroupeDTO recupererGroupeDTO = monProxyOffreDeFormation.recupererGroupe(annee, null, null, null, codElp, null);

		if (recupererGroupeDTO != null){

			//On parcourt les ELP
			for(ElementPedagogiDTO elp : recupererGroupeDTO.getListElementPedagogi()){
				ElpDeCollection el = new ElpDeCollection(elp.getCodElp(), elp.getLibElp());
				
				List<CollectionDeGroupes> listeCollection = new LinkedList<CollectionDeGroupes>();

				//On parcourt les collections de l'ELP
				for( CollectionDTO2 cd2: elp.getListCollection()){
					CollectionDeGroupes collection = new CollectionDeGroupes(cd2.getCodExtCol());

					List<Groupe> listegroupe = new LinkedList<Groupe>();
					
					//On parcourt les groupes de la collection
					for(GroupeDTO gd2 : cd2.getListGroupe()){
						//On récupère les infos sur le groupe
						Groupe groupe = new Groupe(gd2.getCodExtGpe());
						groupe.setLibGroupe(gd2.getLibGpe());
						
						//on récupère le codeGpe a la main car il est pas retourne par le WS.
						ObjetRecherche or = new ObjetRecherche();
						or.setCode(groupe.getCodGroupe());
						or.setLibelle(groupe.getLibGroupe());
						groupe.setCleGroupe(getCleGroupe(or));
						
						if(gd2.getCapaciteGpe() != null){
							if(gd2.getCapaciteGpe().getCapMaxGpe() != null){
								groupe.setCapMaxGpe(gd2.getCapaciteGpe().getCapMaxGpe());
							}else{
								groupe.setCapMaxGpe(0);
							}
							if(gd2.getCapaciteGpe().getCapIntGpe()!=null){
								groupe.setCapIntGpe(gd2.getCapaciteGpe().getCapIntGpe());
							}else{
								groupe.setCapIntGpe(0);
							}
						}else {
							groupe.setCapMaxGpe(0);
							groupe.setCapIntGpe(0);
						}
						//On ajoute le groupe à la liste de la collection
						listegroupe.add(groupe);
					}
					//on insere la liste créé dans la collection
					collection.setListeGroupes(listegroupe);
					//On ajoute la collection a la liste
					listeCollection.add(collection);
				}
				//On insere la liste créé dans l'ELP
				el.setListeCollection(listeCollection);
				//On ajoute l'ELP a la liste
				listeElp.add(el);
			}

		}

		return listeElp;
	}


	@SuppressWarnings("unchecked")
	public List<Inscrit> getInscritsGroupe(ObjetRecherche r) {
		return getSqlMapClientTemplate().queryForList("Groupes.getInscritsGroupe", r);

	}



	public String getCleGroupe(ObjetRecherche r) {
		return (String) getSqlMapClientTemplate().queryForObject("Groupes.getCleGroupe", r);
		
	}



	public String getLibelleGroupe(String codgpe) {
		return (String) getSqlMapClientTemplate().queryForObject("Groupes.getLibelleGroupe", codgpe);
		
	}


	

}

