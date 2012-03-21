Modifs plugin Rennes 1

* G�n�ral :

ajout Classe org.esupportail.mondossierweb.domain.beans.EtudiantAmueR1 : Etend EtudiantAmue

ajout propri�t� properties/specific/specific.xml : bean WS Apo-Cri-Web

N�cessaire pour :
- calendriers examens type "Rennes 1"
- calendriers de rentr�e
- utilisation des habilitation Apog�e


* Utilisation Calendriers d'examens type "Rennes 1"

modif classe org.esupportail.mondossierweb.domain.beans.Examen : ajout attributs :
	/**
	 * libell� �tape concern�e par l'�v�nement.
	 */
	private String etape;
	/**
	 * version d'�tape concern�e par l'�v�nement.
	 */
	private String vet;
	/**
	 * informations compl�mentaires sur l'�v�nement.
	 */
	private String infos;

ajout classe org.esupportail.mondossierweb.domain.beans.ExamensEtape : liste d'examens pour une �tape

	
* Utilisation Calendriers de rentr�e
	
ajout classe org.esupportail.mondossierweb.domain.beans.CalendrierRentree

ajout feuille webapp/stylesheets/etu/calendrierExamensR1.xhtml

* habilitations Apogee

ajout classe org.esupportail.mondossierweb.services.authentification.SecurityHabilitationApogee : Etend Security

* Installation :

monDossierWeb.xml : 
- D�commenter bean bas� sur EtudiantAmueR1
- D�commenter bean bas� sur SecurityHabilitationApogee

specific.xml :
- D�commenter la d�claration des beans

navigation-rules.xml :
- D�commenter navigationCalendrier et navigationRentree

		<navigation-case>
			<from-outcome>navigationCalendrier</from-outcome>
			<to-view-id>/stylesheets/etu/calendrierExamensR1.xhtml</to-view-id>
			<redirect />
		</navigation-case>
		
affichage avec css :
- copier les fichiers de webapps/stylesheets/alternate dans webapps/stylesheets



