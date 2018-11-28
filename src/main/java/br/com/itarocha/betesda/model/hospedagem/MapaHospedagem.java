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
	
	Integer[] qtdTotais 			= {0,0,0,0,0,0,0};
	Integer[] qtdTotaisPendentes 	= {0,0,0,0,0,0,0};
	Integer[] qtdTotaisEncerradas 	= {0,0,0,0,0,0,0};

	// Parciais somente quando fizer o select dos sem leito
	Integer[] qtdParciais 			= {0,0,0,0,0,0,0};
	Integer[] qtdParciaisPendentes 	= {0,0,0,0,0,0,0};
	Integer[] qtdParciaisEncerradas = {0,0,0,0,0,0,0};
	
	Integer[] qtdLeitos 			= {0,0,0,0,0,0,0};
	Integer[] qtdLeitosOcupados 	= {0,0,0,0,0,0,0};
	Integer[] qtdLeitosLivres 		= {0,0,0,0,0,0,0};

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

	public Integer[] getQtdTotais() {
		return qtdTotais;
	}

	public void setQtdTotais(Integer[] qtdTotais) {
		this.qtdTotais = qtdTotais;
	}

	public Integer[] getQtdTotaisPendentes() {
		return qtdTotaisPendentes;
	}

	public void setQtdTotaisPendentes(Integer[] qtdTotaisPendentes) {
		this.qtdTotaisPendentes = qtdTotaisPendentes;
	}

	public Integer[] getQtdTotaisEncerradas() {
		return qtdTotaisEncerradas;
	}

	public void setQtdTotaisEncerradas(Integer[] qtdTotaisEncerradas) {
		this.qtdTotaisEncerradas = qtdTotaisEncerradas;
	}

	public Integer[] getQtdParciais() {
		return qtdParciais;
	}

	public void setQtdParciais(Integer[] qtdParciais) {
		this.qtdParciais = qtdParciais;
	}

	public Integer[] getQtdParciaisPendentes() {
		return qtdParciaisPendentes;
	}

	public void setQtdParciaisPendentes(Integer[] qtdParciaisPendentes) {
		this.qtdParciaisPendentes = qtdParciaisPendentes;
	}

	public Integer[] getQtdParciaisEncerradas() {
		return qtdParciaisEncerradas;
	}

	public void setQtdParciaisEncerradas(Integer[] qtdParciaisEncerradas) {
		this.qtdParciaisEncerradas = qtdParciaisEncerradas;
	}

	public Integer[] getQtdLeitos() {
		return qtdLeitos;
	}

	public void setQtdLeitos(Integer[] qtdLeitos) {
		this.qtdLeitos = qtdLeitos;
	}

	public Integer[] getQtdLeitosOcupados() {
		return qtdLeitosOcupados;
	}

	public void setQtdLeitosOcupados(Integer[] qtdLeitosOcupados) {
		this.qtdLeitosOcupados = qtdLeitosOcupados;
	}

	public Integer[] getQtdLeitosLivres() {
		return qtdLeitosLivres;
	}

	public void setQtdLeitosLivres(Integer[] qtdLeitosLivres) {
		this.qtdLeitosLivres = qtdLeitosLivres;
	}
	
}
