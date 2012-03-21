package org.esupportail.mondossierweb.domain.beans;


import java.util.List;


public class ExamensEtape {

	/**
	 * code �tape concern�e par l'�v�nement.
	 */
	private String etape;
	/**
	 * libell� �tape concern�e par l'�v�nement.
	 */
	private String libEtape;
	/**
	 * version d'�tape concern�e par l'�v�nement.
	 */
	private String vet;
	/**
	 * informations compl�mentaires sur l'�v�nement.
	 */
	private String infos;

	/**
	 * informations compl�mentaires sur l'�v�nement.
	 */
	private List<Examen> calendrierExamensEtape;

	/**
	 * constructeur.
	 *
	 */
	public ExamensEtape() {
		super();
	}

	/**
	 * @return the etape
	 */
	public String getEtape() {
		return etape;
	}

	/**
	 * @param etape the etape to set
	 */
	public void setEtape(String etape) {
		this.etape = etape;
	}

	/**
	 * @return the vet
	 */
	public String getVet() {
		return vet;
	}

	/**
	 * @param vet the vet to set
	 */
	public void setVet(String vet) {
		this.vet = vet;
	}

	/**
	 * @return the infos
	 */
	public String getInfos() {
		return infos;
	}

	/**
	 * @param infos the infos to set
	 */
	public void setInfos(String infos) {
		this.infos = infos;
	}

	/**
	 * @return the calendrierExamensEtape
	 */
	public List<Examen> getCalendrierExamensEtape() {
		return calendrierExamensEtape;
	}

	/**
	 * @param calendrierExamensEtape the calendrierExamensEtape to set
	 */
	public void setCalendrierExamensEtape(List<Examen> calendrierExamensEtape) {
		this.calendrierExamensEtape = calendrierExamensEtape;
	}

	/**
	 * @param libEtape
	 */
	public void setLibEtape(String libEtape) {
		this.libEtape = libEtape;
	}

	/**
	 * @return
	 */
	public String getLibEtape() {
		return libEtape;
	}

}
