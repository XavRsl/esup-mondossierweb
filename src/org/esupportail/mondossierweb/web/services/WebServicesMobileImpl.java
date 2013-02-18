/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.services;


import java.util.HashMap;
import java.util.List;

import org.esupportail.mondossierweb.dao.IDaoService;
import org.esupportail.mondossierweb.domain.beans.ElementPedagogique;
import org.esupportail.mondossierweb.domain.beans.Etape;
import org.esupportail.mondossierweb.domain.beans.Etudiant;
import org.esupportail.mondossierweb.domain.beans.Examen;
import org.esupportail.mondossierweb.domain.beans.IEtudiant;
import org.esupportail.mondossierweb.domain.beans.ResumeDetailNotes;
import org.esupportail.mondossierweb.domain.beans.ResumeEtatCivil;
import org.esupportail.mondossierweb.domain.beans.ResumeNotes;


/**
 * interface des web services
 * @author Charlie Dubois
 */
public class WebServicesMobileImpl implements WebServicesMobile{


	private static final long serialVersionUID = -1354609884748079032L;
	
	/**
	 * Le service.
	 */
	private IDaoService service;
	/**
	 * L'EtudiantManager
	 */
	private IEtudiant etudiantManager;
	
	
	
	/**
	 * CONSTRUCTEUR
	 */
	
	public WebServicesMobileImpl(){
		super();
	}
	
	
	
	/**
	 * LES SERVICES
	 */
	
	/**
	 * Methode qui retourne le codetu en fonction du login
	 */
	public String getCodEtuFromLogin(String login){
		return service.getCodEtuFromLogin(login);
	}

	/**
	 * Méthode qui retourne la calendrier des examens
	 */
	public List<Examen> getCalendrierExamens(String codetu) {
		Etudiant etudiant = new Etudiant();
		etudiant.setCod_etu(codetu);
		etudiantManager.setCodInd(etudiant);
		etudiantManager.setCalendrier(etudiant);
		return etudiant.getCalendrier();
	}


	/**
	 * Méthode qui retourne les notes aux diplomes et étapes
	 */
	public ResumeNotes getNotesDiplomesEtapes(String codetu) {
		Etudiant etudiant = new Etudiant();
		etudiant.setCod_etu(codetu);
		etudiantManager.setNotesEtResultats(etudiant);
		ResumeNotes resume = new ResumeNotes();
		resume.setDiplomes(etudiant.getDiplomes());
		resume.setEtapes(etudiant.getEtapes());
		resume.setSignificationResultats(etudiant.getSignificationResultats());
		return resume;
	}
	
	/**
	 * Méthode qui retourne les notes aux ELP
	 */
	public ResumeDetailNotes getNotesElpEpr(String codetu, Etape et) {
		Etudiant etudiant = new Etudiant();
		etudiant.setCod_etu(codetu);
		etudiantManager.setNotesElpEpr(etudiant, et); 
		ResumeDetailNotes resume = new ResumeDetailNotes();
		resume.setElementsPedagogiques(etudiant.getElementsPedagogiques());
		resume.setSignificationResultats(etudiant.getSignificationResultats());
		
		//suppression de l'étape en tête de liste
		if(resume.getElementsPedagogiques() != null && resume.getElementsPedagogiques().size()>0)
			resume.getElementsPedagogiques().remove(0);
		
		
		//suppression de l'indentation
		for (ElementPedagogique elp : resume.getElementsPedagogiques()){
			elp.setLibelle(elp.getLibelle().replaceAll("&#160;", ""));
		}
		
		
		return resume;
	}
	

	/**
	 * Recupere quelques informations de l'état-civil
	 */
	public ResumeEtatCivil getEtatCivil(String codetu) {
		Etudiant etudiant = new Etudiant();
		etudiant.setEtudiantManager(etudiantManager);
		etudiant.setCod_etu(codetu);
		etudiant.setUrlphoto("OnNeVeutPasRecupererLaPhoto");
		//etudiantManager.setEtatCivil(etudiant);
		ResumeEtatCivil resume = new ResumeEtatCivil();
		resume.setNom(etudiant.getNom());
		resume.setCod_etu(etudiant.getCod_etu());
		resume.setCod_ind(etudiant.getCod_ind());
		resume.setCod_nne(etudiant.getCod_nne());
		resume.setDatenaissance(etudiant.getDatenaissance());
		resume.setEmail(etudiant.getEmail());
		return resume;
	}
	
	/**
	 * LES GETTERS ET SETTERS
	 */
	
	public IEtudiant getEtudiantManager() {
		return etudiantManager;
	}
	public void setEtudiantManager(IEtudiant etudiantManager) {
		this.etudiantManager = etudiantManager;
	}
	public IDaoService getService() {
		return service;
	}
	public void setService(IDaoService service) {
		this.service = service;
	}






	




}
