/**
 * ESUP-Portail My Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-mondossierweb
 */
package org.esupportail.mondossierweb.domain;


import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.mondossierweb.domain.beans.User;


/**
 * The domain service interface.
 */
public interface DomainService {

	//////////////////////////////////////////////////////////////
	// User
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the User instance that corresponds to an id.
	 * @throws UserNotFoundException
	 */
	User getUser(String id) throws UserNotFoundException;



	//////////////////////////////////////////////////////////////
	// VersionManager
	//////////////////////////////////////////////////////////////
	
	
	
	//////////////////////////////////////////////////////////////
	// Authorizations
	//////////////////////////////////////////////////////////////



}
