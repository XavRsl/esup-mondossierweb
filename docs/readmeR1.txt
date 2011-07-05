Modifs plugin Rennes 1

* Général :

ajout Classe org.esupportail.mondossierweb.domain.beans.EtudiantAmueR1 : Etend EtudiantAmue

ajout propriété properties/specific/specific.xml : bean WS Apo-Cri-Web

Nécessaire pour :
- calendriers examens type "Rennes 1"
- calendriers de rentrée
- utilisation des habilitation Apogée


* Utilisation Calendriers d'examens type "Rennes 1"

modif classe org.esupportail.mondossierweb.domain.beans.Examen : ajout attributs :
	/**
	 * libellé étape concernée par l'événement.
	 */
	private String etape;
	/**
	 * version d'étape concernée par l'événement.
	 */
	private String vet;
	/**
	 * informations complémentaires sur l'événement.
	 */
	private String infos;

ajout classe org.esupportail.mondossierweb.domain.beans.ExamensEtape : liste d'examens pour une étape

	
* Utilisation Calendriers de rentrée
	
ajout classe org.esupportail.mondossierweb.domain.beans.CalendrierRentree

ajout feuille webapp/stylesheets/etu/calendrierExamensR1.xhtml

* habilitations Apogee

ajout classe org.esupportail.mondossierweb.services.authentification.SecurityHabilitationApogee : Etend Security

* Installation :

monDossierWeb.xml : 
- Décommenter bean basé sur EtudiantAmueR1
- Décommenter bean basé sur SecurityHabilitationApogee

specific.xml :
- Décommenter la déclaration des beans

navigation-rules.xml :
- Décommenter navigationCalendrier et navigationRentree

		<navigation-case>
			<from-outcome>navigationCalendrier</from-outcome>
			<to-view-id>/stylesheets/etu/calendrierExamensR1.xhtml</to-view-id>
			<redirect />
		</navigation-case>
		
affichage avec css :
- copier les fichiers de webapps/stylesheets/alternate dans webapps/stylesheets



