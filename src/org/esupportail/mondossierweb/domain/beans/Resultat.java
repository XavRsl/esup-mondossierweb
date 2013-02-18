/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.domain.beans;

import java.io.Serializable;



/**
 * classe qui réprésente le résultat d'un étudiant.
 * @author Charlie Dubois
 */
public class Resultat implements Serializable{

	
	private static final long serialVersionUID = 4809562988462399839L;
		/**
		 * la session.
		 */
		private String session;
		/**
		 * la note.
		 */
		private String note;
		/**
		 * le bareme
		 */
		private int bareme;
		/**
		 * le résultat (admis ou pas).
		 */
		private String admission;
		/**
		 * le code de la mention
		 */
		private String codMention;
		/**
		 * le code de la mention
		 */
		private String libMention;
		
		
		
		/**
		 * constructeur vide.
		 */
		public Resultat() {
			super();
		}
		
		
		
		
		/**
		 * @return admission
		 */ 
		public String getAdmission() {
			return admission;
		}
		/**
		 * @param admission
		 */
		public void setAdmission(final String admission) {
			this.admission = admission;
		}
		/**
		 * @return note
		 */ 
		public String getNote() {
			return note;
		}
		/**
		 * @param note
		 */
		public void setNote(String note) {
			this.note = note;
		}
		/**
		 * @return session
		 */
		public String getSession() {
			return session;
		}
		/**
		 * @param session
		 */
		public void setSession(final String session) {
			this.session = session;
		}




		public int getBareme() {
			return bareme;
		}




		public void setBareme(int bareme) {
			this.bareme = bareme;
		}




		public String getCodMention() {
			return codMention;
		}




		public void setCodMention(String codMention) {
			this.codMention = codMention;
		}




		public String getLibMention() {
			return libMention;
		}




		public void setLibMention(String libMention) {
			this.libMention = libMention;
		}
		
		
		
}
