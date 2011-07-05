package org.esupportail.mondossierweb.domain.beans;

/**
 * Représente un signataire.
 * 
 * @author Adrien Colson
 */
public class Signataire {

	/**
	 * Code du signataire.
	 */
	private String cod_sig;

	/**
	 * Nom du signataire.
	 */
	private String nom_sig;

	/**
	 * Qualité du signataire.
	 */
	private String qua_sig;

	/**
	 * Image de la signature digitalisée.
	 */
	private byte[] img_sig_std;

	/**
	 * Constructeur.
	 */
	public Signataire() {
		super();
	}

	/**
	 * @return the cod_sig
	 */
	public String getCod_sig() {
		return cod_sig;
	}

	/**
	 * @param cod_sig
	 *            the cod_sig to set
	 */
	public void setCod_sig(String cod_sig) {
		this.cod_sig = cod_sig;
	}

	/**
	 * @return the nom_sig
	 */
	public String getNom_sig() {
		return nom_sig;
	}

	/**
	 * @param nom_sig
	 *            the nom_sig to set
	 */
	public void setNom_sig(String nom_sig) {
		this.nom_sig = nom_sig;
	}

	/**
	 * @return the qua_sig
	 */
	public String getQua_sig() {
		return qua_sig;
	}

	/**
	 * @param qua_sig
	 *            the qua_sig to set
	 */
	public void setQua_sig(String qua_sig) {
		this.qua_sig = qua_sig;
	}

	/**
	 * @return the img_sig_std
	 */
	public byte[] getImg_sig_std() {
		return img_sig_std;
	}

	/**
	 * @param img_sig_std
	 *            the img_sig_std to set
	 */
	public void setImg_sig_std(byte[] img_sig_std) {
		this.img_sig_std = img_sig_std;
	}

}
