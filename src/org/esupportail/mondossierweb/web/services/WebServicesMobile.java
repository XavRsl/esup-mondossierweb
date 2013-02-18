/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.services;

import java.io.Serializable;
import java.util.List;

import org.esupportail.mondossierweb.domain.beans.Etape;
import org.esupportail.mondossierweb.domain.beans.Examen;
import org.esupportail.mondossierweb.domain.beans.ResumeDetailNotes;
import org.esupportail.mondossierweb.domain.beans.ResumeEtatCivil;
import org.esupportail.mondossierweb.domain.beans.ResumeNotes;


/**
 * interface des web services
 * @author Charlie Dubois
 */
public interface WebServicesMobile extends Serializable{

	
	
	public String getCodEtuFromLogin(String login);
	public ResumeEtatCivil getEtatCivil(String codetu);
	public List<Examen> getCalendrierExamens(String codetu);
	public ResumeNotes getNotesDiplomesEtapes (String codetu);
	public ResumeDetailNotes getNotesElpEpr (String codetu, Etape e);
	
	
	
}
