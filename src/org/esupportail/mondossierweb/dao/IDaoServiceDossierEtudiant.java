/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;

import java.util.List;

import org.esupportail.mondossierweb.domain.beans.Inscrit;
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

/**
 * Interface contenant la liste des méthodes devant être proposées par le DaoService 
 * qui permettra de récupérer toutes les informations du dossier Etudiant a la place du
 * WS de l'Amue
 * @author chdubois
 *
 */
public interface IDaoServiceDossierEtudiant {

	GeneralitesEtatCivilDTO getGeneralitesEtatCivil(String codind);
	String getNationalite(String codpays);
	String getDepartementNaissance(String codDepNai);
	String getPaysNaissance(String paysNai);
	String getEtablissement(String codEtb);
	List<BacDTO> getBacs(String cod_ind);
	String getMentionBac(String codmention);
	String getTypeEtb(String cod_typ_etb);
	AdresseDTO getAdresseAnnuelle(ParamRequeteDTO param);
	AdresseDTO getAdresseFixe(String cod_ind);
	String getAnneeDerniereInscription(String cod_ind);
	InscriptionDTO getPremiereInscription(String cod_ind);
	List<InscriptionDTO> getInscriptionsIAE(String cod_ind);
	List<InscriptionDTO> getInscriptionsDAC(String cod_ind);
	List<DiplomeDTO> getDiplomes(String codind);
	List<EtapeDTO> getEtapes(String codind);
	List<NotesResultatsDTO> getNotesResultatsDiplome(ParamRequeteDTO param);
	List<NotesResultatsDTO> getNotesResultatsEtape(ParamRequeteDTO param);
	List<SignificationResultatDTO> getSignificationCodeResultat();
	EtapeDTO getEtapeIP(ParamRequeteDTO param);
	List<IpDTO> getIPvet(ParamRequeteDTO param);
	String getElpName(String codelp);
	String getCodAnuMax(ParamRequeteDTO param);
	String getCodAnuDispenseElp(ParamRequeteDTO param);
	List<EpreuveDTO >getNoteResEpr(ParamRequeteDTO param);
	String getLibEpr(String codepr);
	ResVetDTO getResVet(ParamRequeteDTO param);
	List<ResElpDTO> getResElp(ParamRequeteDTO param);
	List<ResElpDTO> getResDispenseElp1(ParamRequeteDTO param);
	List<ResElpDTO> getResDispenseElp2(ParamRequeteDTO param);
	
	List<ResExtractionR1DTO> getResultats(ParamRequeteDTO param);
	List<ResExtractionR1DTO> getResultatsDetail(ParamRequeteDTO param);
	
}
