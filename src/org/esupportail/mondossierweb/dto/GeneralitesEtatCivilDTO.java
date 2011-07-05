/**
 * ESUP-Portail MONDOSSIERWEB - Copyright (c) 2007 ESUP-Portail consortium
 */
package org.esupportail.mondossierweb.dto;

/**
 * DTO pour récupérer les généralités de l'état civil
 * @author Charlie Dubois
 */
public class GeneralitesEtatCivilDTO {
	
	private String cod_etu;
	private String cod_nne_ind;
	private String cod_cle_nne_ind;
	private String lib_nom_pat_ind;
	private String lib_nom_usu_ind;
	private String lib_pr1_ind;
	private String lib_pr2_ind;
	private String lib_pr3_ind;
	private String date_nai_ind;
	private String lib_vil_nai_etu;
	private String cod_dep_pay_nai;
	private String daa_ent_etb;
	private String cod_typ_dep_pay_nai;
	private String cod_pay_nat;
	private String cod_etb;
	
	public GeneralitesEtatCivilDTO(){
		super();
	}

	public String getCod_etu() {
		return cod_etu;
	}

	public void setCod_etu(String cod_etu) {
		this.cod_etu = cod_etu;
	}

	public String getCod_nne_ind() {
		return cod_nne_ind;
	}

	public void setCod_nne_ind(String cod_nne_ind) {
		this.cod_nne_ind = cod_nne_ind;
	}

	public String getCod_cle_nne_ind() {
		return cod_cle_nne_ind;
	}

	public void setCod_cle_nne_ind(String cod_cle_nne_ind) {
		this.cod_cle_nne_ind = cod_cle_nne_ind;
	}

	public String getLib_nom_pat_ind() {
		return lib_nom_pat_ind;
	}

	public void setLib_nom_pat_ind(String lib_nom_pat_ind) {
		this.lib_nom_pat_ind = lib_nom_pat_ind;
	}

	public String getLib_nom_usu_ind() {
		return lib_nom_usu_ind;
	}

	public void setLib_nom_usu_ind(String lib_nom_usu_ind) {
		this.lib_nom_usu_ind = lib_nom_usu_ind;
	}

	public String getLib_pr1_ind() {
		return lib_pr1_ind;
	}

	public void setLib_pr1_ind(String lib_pr1_ind) {
		this.lib_pr1_ind = lib_pr1_ind;
	}

	public String getLib_pr2_ind() {
		return lib_pr2_ind;
	}

	public void setLib_pr2_ind(String lib_pr2_ind) {
		this.lib_pr2_ind = lib_pr2_ind;
	}

	public String getLib_pr3_ind() {
		return lib_pr3_ind;
	}

	public void setLib_pr3_ind(String lib_pr3_ind) {
		this.lib_pr3_ind = lib_pr3_ind;
	}

	public String getDate_nai_ind() {
		return date_nai_ind;
	}

	public void setDate_nai_ind(String date_nai_ind) {
		this.date_nai_ind = date_nai_ind;
	}

	public String getLib_vil_nai_etu() {
		return lib_vil_nai_etu;
	}

	public void setLib_vil_nai_etu(String lib_vil_nai_etu) {
		this.lib_vil_nai_etu = lib_vil_nai_etu;
	}

	public String getCod_dep_pay_nai() {
		return cod_dep_pay_nai;
	}

	public void setCod_dep_pay_nai(String cod_dep_pay_nai) {
		this.cod_dep_pay_nai = cod_dep_pay_nai;
	}

	public String getDaa_ent_etb() {
		return daa_ent_etb;
	}

	public void setDaa_ent_etb(String daa_ent_etb) {
		this.daa_ent_etb = daa_ent_etb;
	}

	public String getCod_typ_dep_pay_nai() {
		return cod_typ_dep_pay_nai;
	}

	public void setCod_typ_dep_pay_nai(String cod_typ_dep_pay_nai) {
		this.cod_typ_dep_pay_nai = cod_typ_dep_pay_nai;
	}

	public String getCod_pay_nat() {
		return cod_pay_nat;
	}

	public void setCod_pay_nat(String cod_pay_nat) {
		this.cod_pay_nat = cod_pay_nat;
	}

	public String getCod_etb() {
		return cod_etb;
	}

	public void setCod_etb(String cod_etb) {
		this.cod_etb = cod_etb;
	}
	
}
