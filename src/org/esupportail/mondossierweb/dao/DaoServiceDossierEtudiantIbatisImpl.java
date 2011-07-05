/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;

import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.mondossierweb.dto.AdresseDTO;
import org.esupportail.mondossierweb.dto.BacDTO;
import org.esupportail.mondossierweb.dto.DiplomeDTO;
import org.esupportail.mondossierweb.dto.EpreuveDTO;
import org.esupportail.mondossierweb.dto.EtapeDTO;
import org.esupportail.mondossierweb.dto.GeneralitesEtatCivilDTO;
import org.esupportail.mondossierweb.dto.InscriptionDTO;
import org.esupportail.mondossierweb.dto.IpDTO;
import org.esupportail.mondossierweb.dto.NotesResultatsDTO;
import org.esupportail.mondossierweb.dto.ParamRequeteDTO;
import org.esupportail.mondossierweb.dto.ResElpDTO;
import org.esupportail.mondossierweb.dto.ResExtractionR1DTO;
import org.esupportail.mondossierweb.dto.ResVetDTO;
import org.esupportail.mondossierweb.dto.SignificationResultatDTO;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * Classe qui doit récupérer via Ibatis toutes les informations
 * du dossier Etudiant.
 * @author chdubois
 *
 */
public class DaoServiceDossierEtudiantIbatisImpl extends SqlMapClientDaoSupport implements
		IDaoServiceDossierEtudiant {

	/**
	 * Un logger.
	 */
	private static final Logger LOG = new LoggerImpl(DaoServiceDossierEtudiantIbatisImpl.class);
	
	
	public GeneralitesEtatCivilDTO getGeneralitesEtatCivil(String codind) {
		return (GeneralitesEtatCivilDTO) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getGeneralitesEtatCivil", codind);

	}


	public String getNationalite(String codpays) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getNationalite", codpays);
	}


	public String getDepartementNaissance(String codDepNai) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getDepartement", codDepNai);
		
	}


	public String getPaysNaissance(String paysNai) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getPays", paysNai);
		
	}


	public String getEtablissement(String codEtb) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getEtb", codEtb);
		
	}

	public List<BacDTO> getBacs(String cod_ind) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getBacs", cod_ind);
	}


	public String getMentionBac(String codmention) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getMentionBac", codmention);
		
	}


	public String getTypeEtb(String cod_typ_etb) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getTypeEtb", cod_typ_etb);
		
	}


	public AdresseDTO getAdresseAnnuelle(ParamRequeteDTO param) {
		return (AdresseDTO) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getAdresseAnnuelle", param);
		
	}


	public AdresseDTO getAdresseFixe(String cod_ind) {
		return (AdresseDTO) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getAdresseFixe", cod_ind);
		
	}


	public String getAnneeDerniereInscription(String cod_ind) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getAnneeDerniereInscription", cod_ind);
		
	}


	public List<InscriptionDTO> getInscriptionsDAC(String cod_ind) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getInscriptionDAC", cod_ind);
		
	}


	public List<InscriptionDTO> getInscriptionsIAE(String cod_ind) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getInscriptionIAE", cod_ind);
	}


	public InscriptionDTO getPremiereInscription(String cod_ind) {
		return (InscriptionDTO) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getPremiereInscription", cod_ind);
		
	}


	public List<DiplomeDTO> getDiplomes(String codind) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getDiplomes", codind);
		
	}


	public List<EtapeDTO> getEtapes(String codind) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getEtapes", codind);
		
	}


	public List<NotesResultatsDTO> getNotesResultatsDiplome(
			ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getNotesResultatsDiplome", param);
		
	}


	public List<NotesResultatsDTO> getNotesResultatsEtape(ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getNotesResultatsEtape", param);
		
	}


	public 	List<SignificationResultatDTO>  getSignificationCodeResultat() {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getSignificationCodeResultat");
		
	}


	public EtapeDTO getEtapeIP(ParamRequeteDTO param) {
		return (EtapeDTO) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getEtapeIP", param);
		
	}


	public List<IpDTO> getIPvet(ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getIPvet", param);
		
	}


	public String getElpName(String codelp) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getElpName", codelp);
		
	}


	public String getCodAnuMax(ParamRequeteDTO param) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getCodAnuMax", param);
		
	}


	public String getCodAnuDispenseElp(ParamRequeteDTO param) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getCodAnuDispenseElp", param);
		
	}


	public String getLibEpr(String codepr) {
		return (String) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getLibEpr", codepr);
		
	}


	public List<EpreuveDTO> getNoteResEpr(ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getNoteResEpr", param);
		
	}


	public List<ResElpDTO> getResElp(ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getResElp", param);
		
	}


	public ResVetDTO getResVet(ParamRequeteDTO param) {
		return (ResVetDTO) getSqlMapClientTemplate().queryForObject("DossierEtudiant.getResVet", param);
		
	}


	public List<ResElpDTO> getResDispenseElp1(ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getResDispenseElp1", param);
		
	}


	public List<ResElpDTO> getResDispenseElp2(ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getResDispenseElp2", param);
		
	}
	
	
	
	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoServiceDossierEtudiant#getResultats(org.esupportail.mondossierweb.dto.ParamRequeteDTO)
	 */
	public List<ResExtractionR1DTO> getResultats(ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getResExtr", param);
	}


	/**
	 * @see org.esupportail.mondossierweb.dao.IDaoServiceDossierEtudiant#getResultatsDetail(org.esupportail.mondossierweb.dto.ParamRequeteDTO)
	 */
	public List<ResExtractionR1DTO> getResultatsDetail(ParamRequeteDTO param) {
		return getSqlMapClientTemplate().queryForList("DossierEtudiant.getResExtrDetail", param);
	}
	
	

}
