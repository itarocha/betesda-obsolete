package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MapaHospedagem {

	private LocalDate dataIni;

	private LocalDate dataFim;
	
	private List<HospedagemHeader> hospedagens = new ArrayList<HospedagemHeader>();
	
	private List<CelulaOut> celulas = new ArrayList<CelulaOut>();
	
	private List<LocalDate> dias = new ArrayList<LocalDate>();

	public LocalDate getDataIni() {
		return dataIni;
	}

	public void setDataIni(LocalDate dataIni) {
		this.dataIni = dataIni;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public List<HospedagemHeader> getHospedagens() {
		return hospedagens;
	}

	public void setHospedagens(List<HospedagemHeader> hospedagens) {
		this.hospedagens = hospedagens;
	}

	public List<CelulaOut> getCelulas() {
		return celulas;
	}

	public void setCelulas(List<CelulaOut> celulas) {
		this.celulas = celulas;
	}

	public List<LocalDate> getDias() {
		return dias;
	}

	public void setDias(List<LocalDate> dias) {
		this.dias = dias;
	}
	
	
}
