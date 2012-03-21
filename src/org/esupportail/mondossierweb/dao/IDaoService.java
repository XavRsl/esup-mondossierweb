/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dao;



import gouv.education.apogee.commun.transverse.dto.etudiant.TypeHebergementDTO;
import gouv.education.apogee.commun.transverse.dto.geographie.CommuneDTO;
import gouv.education.apogee.commun.transverse.dto.geographie.PaysDTO;

import java.util.List;


import org.esupportail.mondossierweb.domain.beans.Composante;
import org.esupportail.mondossierweb.domain.beans.Diplome;
import org.esupportail.mondossierweb.domain.beans.ElementPedagogique;
import org.esupportail.mondossierweb.domain.beans.ElpDeCollection;
import org.esupportail.mondossierweb.domain.beans.Etape;
import org.esupportail.mondossierweb.domain.beans.Examen;
import org.esupportail.mondossierweb.domain.beans.Inscrit;
import org.esupportail.mondossierweb.domain.beans.Signataire;
import org.esupportail.mondossierweb.dto.ObjetRecherche;
import org.esupportail.mondossierweb.dto.ParamRequeteDTO;
/**
 * interface listant les m�thode que doit fournir 
 * le service qui va chercher les informations dans Apog�e.
 * @author Charlie Dubois
 */
public interface IDaoService {

	
	/**
	 * @return l'ann�e en cours
	 */
	String getAnneeEnCours();
	/**
	 * @return la derni�re ann�e resultat ouverte
	 */
	String getAnneeResEnCours();
	/**
	 * @return l'ann�e universitaire en cours
	 */
	String getAnneeUniversitaireEnCours();
	/**
	 * @return derni�re ann�e universitaire resultat ouverte
	 */
	String getAnneeUniversitaireResEnCours();
	/**
	 * @param login
	 * @return code individu
	 */
	String getCodEtuFromLogin(String login);

	/**
	 * @param codind
	 * @return le login
	 */
	String getLoginFromCodEtu(String codetu);
	/**
	 * @param id
	 * @return le code utilisateur
	 */
	String getCodUti(String id);
	/**
	 * 
	 * @return la liste des types d'h�bergement
	 */
	TypeHebergementDTO[] getTypesHebergement();
	
	/**
	 * @param codp
	 * @return les villes dont le codePostal commence par codp
	 */
	CommuneDTO[] getCommunes(String codp);
	
	/**
	 * @param codp
	 * @param nom
	 * @return la ville dont le codePostal et le nom sont en parametre
	 */
	String[] getCommune(String codp, String nom);
	
	/**
	 * 
	 * @return la liste des pays
	 */
	PaysDTO[] getPays();
	
	
	/**
	 * @param pays
	 * @return tableau avec le nom du pays, et son code
	 */
	String[] getPay(final String pays);
	/**
	 * @param codp
	 * @return tableau avec le nom de la commune , son code, et son codepostal
	 */
	String[] getCommune(final String codp);
	/**
	 * @param id
	 * @return le code individu a partir du code etudiant
	 */
	String getCodIndFromCodEtu(String id);
	/**
	 * @param e
	 * @return le nombre etape correspondant
	 */
	Integer checkEtape(Etape e);
	/**
	 * @param id
	 * @return le nombre d'�l�ment p�dagogique correspondant
	 */
	Integer checkElementPedagogique(String id);
	/**
	 * @param e
	 * @return la liste des inscrits � une �tape
	 */
	List<Inscrit> getInscritsEtape(Etape e);
	/**
	 * @param e
	 * @return la liste des inscrits � une �tape avec les r�sultats de juin
	 */
	List<Inscrit> getInscritsEtapeJuin(Etape e);
	/**
	 * 
	 * @param e
	 * @return la liste des inscrits � une �tape avec les r�sultats de septembre
	 */
	List<Inscrit> getInscritsEtapeSep(Etape e);
	/**
	 * 
	 * @param e
	 * @return la liste des inscrits � une �tape avec les r�sultats de juin et septembre
	 */
	List<Inscrit> getInscritsEtapeJuinSep(Etape e);
	/**
	 * @param id
	 * @return le libelle de l'�tape
	 */
	String getLibelleEtape(ObjetRecherche o);
	/**
	 * @param id
	 * @return le libell� de l'�l�ment p�dagogique
	 */
	String getLibelleElementPedagogique(String id);
	/**
	 * @param e
	 * @return la liste des inscrits � l'�l�ment p�dagogique
	 */
	List<Inscrit> getInscritsElementPedagogique(ElementPedagogique e);
	/**
	 * @param e
	 * @return la liste des inscrits � l'�l�ment p�dagogique avec les r�sultats de juin
	 */
	List<Inscrit> getInscritsElementPedagogiqueJuin(ElementPedagogique e);
	/**
	 * 
	 * @param e
	 * @return la liste des inscrits � l'�l�ment p�dagogique avec les r�sultats de septembre
	 */
	List<Inscrit> getInscritsElementPedagogiqueSep(ElementPedagogique e);
	/**
	 * 
	 * @param e
	 * @return la liste des inscrits � l'�l�ment p�dagogique avec l'�tape correspondant
	 */
	List<Inscrit> getInscritsElementPedagogiqueEtape(ElementPedagogique e);
	/**
	 * @param e
	 * @return la liste des inscrits � l'�l�ment p�dagogique avec les r�sultats de juin et septembre
	 */
	List<Inscrit> getInscritsElementPedagogiqueJuinSep(ElementPedagogique e);
	/**
	 * 
	 * @param e
	 * @return la liste des inscrits � l'�l�ment p�dagogique avec les r�sultats de juin et l'�tape correspondant
	 */
	List<Inscrit> getInscritsElementPedagogiqueJuinEtape(ElementPedagogique e);
	/**
	 * 
	 * @param e
	 * @return la liste des inscrits � l'�l�ment p�dagogique avec les r�sultats de septembre et l'�tape
	 */
	List<Inscrit> getInscritsElementPedagogiqueSepEtape(ElementPedagogique e);
	/**
	 * 
	 * @param e
	 * @return la liste des inscrits � l'�l�ment p�dagogique avec tous les r�sultats et l'�tape
	 */
	List<Inscrit> getInscritsElementPedagogiqueJuinSepEtape(ElementPedagogique e);
	/**
	 * 
	 * @return l'�tablissement ou l'application est install�e
	 */
	String getEtablissementDef();
	/**
	 * @param codind
	 * @return la libell� de la formation en cours
	 */
	String getFormationEnCours(String codind);
	/**
	 * 
	 * @param id
	 * @return les examens pr�vus d'un �tudiant
	 */
	List<Examen> getExamens(String id);
	/**
	 * 
	 * @param param
	 * @return les examens pr�vus d'un �tudiant (version UR1)
	 */
	List<Examen> getExamensEtape(ParamRequeteDTO param);
	/**
	 * 
	 * @param codcin
	 * @return la page infos examen associ� au centre d'incompatiilit�
	 */
	String getPageInfos(String codcin);
	/**
	 * 
	 * @return toutes les composantes
	 */
	List<Composante> getComposantes();
	/**
	 * 
	 * @param o
	 * @return le libelle du diplome dont code et version sont stock�s dans l'objetRecherche
	 */
	String getDiplome(ObjetRecherche o);
	/**
	 * 
	 * @param o
	 * @return tous les diplomes d'une composante
	 */
	List<Diplome> getDiplomes(ObjetRecherche o);
	/*
	List<Diplome> getDiplomeByEtape(ObjetRecherche o);*/
	/**
	 * 
	 * @param codDip
	 * @result le code type diplome
	 */
	String getCodeTypeDiplome(String codDip);
	/**
	 * 
	 * @param o
	 * @return les �tapes d'un diplome
	 */
	List<Etape> getEtape(ObjetRecherche o);
	/**
	 * 
	 * @param e
	 * @return les �l�ments p�dagogiques d'une �tape.
	 */
	List<ElementPedagogique> getElements(Etape e);
	/**
	 * 
	 * @param id
	 * @return les sous �l�ments d'un �l�ment
	 */
	List<ElementPedagogique> getSousElements(String id);
	/**
	 * 
	 * @param id
	 * @return vrai si l'�l�ment a des sous-�l�ments
	 */
	boolean hasSousElements(String id);
	/**
	 * @param id
	 * @return un signataire
	 */
	Signataire getSignataire(String id);
	/**
	 * 
	 * @param annee
	 * @param codElp
	 * @return
	 */
	List<ElpDeCollection> recupererGroupes(String annee, String codElp);
	/**
	 * @param e
	 * @return la liste des inscrits � une �tape
	 */
	List<Inscrit> getInscritsGroupe(ObjetRecherche r);
	/**
	 * 
	 * @param r
	 * @return le codGpe du groupe
	 */
	String getCleGroupe(ObjetRecherche r);
	/**
	 * 
	 * @param r
	 * @return le libelle du groupe
	 */
	String getLibelleGroupe(String codgpe);
	
	
	
}

