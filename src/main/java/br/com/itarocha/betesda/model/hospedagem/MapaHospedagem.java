package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.itarocha.betesda.model.HospedeHospedagemVO;

public class MapaHospedagem {

	private LocalDate dataIni;

	private LocalDate dataFim;
	
	private List<HospedagemHeader> hospedagens = new ArrayList<HospedagemHeader>();
	
	private List<Celula> celulas = new ArrayList<Celula>();
	
	private List<LocalDate> dias = new ArrayList<LocalDate>();
	
	private List<HospedeHospedagemVO> hospedes = new ArrayList<HospedeHospedagemVO>(); 

	public MapaHospedagem() {
		
	}
	
	public MapaHospedagem(LocalDate dataIni, LocalDate dataFim) {
		this.dataIni = dataIni;
		this.dataFim = dataFim;
	}
	
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

	public List<Celula> getCelulas() {
		return celulas;
	}

	public void setCelulas(List<Celula> celulas) {
		this.celulas = celulas;
	}

	public List<LocalDate> getDias() {
		return dias;
	}

	public void setDias(List<LocalDate> dias) {
		this.dias = dias;
	}

	public List<HospedeHospedagemVO> getHospedes(){
		return this.hospedes;
	}
	
	public void setHospedes(List<HospedeHospedagemVO> hospedes) {
		this.hospedes = hospedes;
	}
	
	
}
