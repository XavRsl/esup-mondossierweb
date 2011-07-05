/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.web.controllers;


import org.esupportail.mondossierweb.web.navigation.View;

/**
 * controller de la vue du calendrier de rentrée de l'étudiant.
 * @author Vincent Repain
 */
public class CalendrierRentreeController extends AbstractContextAwareController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -370390637711443434L;
	/**
	* l'etatCivilController.
	*/
	private EtatCivilController etatcivilcontroller;
	

	/**
	 * le constructeur.
	 *
	 */
	public CalendrierRentreeController() {
		super();
	}

	
	/**
	 * on entre dans la vue du calendrier des examens.
	 * @return la vue du calendrier des examens.
	 */
	public String enter() {
		return View.CALENDRIER_RENTREE;
	}
	


	


	/**
	 * @return etatcivilcontroller.
	 */
	public EtatCivilController getEtatcivilcontroller() {
		return etatcivilcontroller;
	}

	/**
	 * @param etatcivilcontroller
	 */
	public void setEtatcivilcontroller(final EtatCivilController etatcivilcontroller) {
		this.etatcivilcontroller = etatcivilcontroller;
	}

	
	

}
