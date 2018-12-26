package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MapaRetorno {
	
	private LocalDate dataIni;
	
	private LocalDate dataFim;
	
	private List<LeitoHeader> leitos = new ArrayList<>();
	
	private List<LocalDate> dias = new ArrayList<LocalDate>();	
	
	private List<HospedagemHeaderInfo> hospedagens = new ArrayList<HospedagemHeaderInfo>();
	
	private Integer[] qtdTotais 			= {0,0,0,0,0,0,0};
	private Integer[] qtdVencidos 			= {0,0,0,0,0,0,0};
	private Integer[] qtdPendentes 			= {0,0,0,0,0,0,0};
	private Integer[] qtdEncerrados 		= {0,0,0,0,0,0,0};

	private Integer[] qtdParciaisTotais		= {0,0,0,0,0,0,0};
	private Integer[] qtdParciaisVencidos	= {0,0,0,0,0,0,0};
	private Integer[] qtdParciaisPendentes 	= {0,0,0,0,0,0,0};
	private Integer[] qtdParciaisEncerrados = {0,0,0,0,0,0,0};
	
	private Integer[] qtdLeitosTotais		= {0,0,0,0,0,0,0};
	private Integer[] qtdLeitosOcupados 	= {0,0,0,0,0,0,0};
	private Integer[] qtdLeitosLivres 		= {0,0,0,0,0,0,0};

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

	public List<LeitoHeader> getLeitos() {
		return leitos;
	}

	public void setLeitos(List<LeitoHeader> leitos) {
		this.leitos = leitos;
	}
	
	public List<HospedagemHeaderInfo> getHospedagens() {
		return hospedagens;
	}

	public void setHospedagens(List<HospedagemHeaderInfo> hospedagens) {
		this.hospedagens = hospedagens;
	}

	public List<LocalDate> getDias() {
		return dias;
	}
	
	public void setDias(List<LocalDate> dias) {
		this.dias = dias;
	}

	public Integer[] getQtdTotais() {
		return qtdTotais;
	}

	public void setQtdTotais(Integer[] qtdTotais) {
		this.qtdTotais = qtdTotais;
	}

	public Integer[] getQtdVencidos() {
		return qtdVencidos;
	}

	public void setQtdVencidos(Integer[] qtdVencidos) {
		this.qtdVencidos = qtdVencidos;
	}

	public Integer[] getQtdPendentes() {
		return qtdPendentes;
	}

	public void setQtdPendentes(Integer[] qtdPendentes) {
		this.qtdPendentes = qtdPendentes;
	}

	public Integer[] getQtdEncerrados() {
		return qtdEncerrados;
	}

	public void setQtdEncerrados(Integer[] qtdEncerrados) {
		this.qtdEncerrados = qtdEncerrados;
	}

	public Integer[] getQtdParciaisTotais() {
		return qtdParciaisTotais;
	}

	public void setQtdParciaisTotais(Integer[] qtdParciaisTotais) {
		this.qtdParciaisTotais = qtdParciaisTotais;
	}

	public Integer[] getQtdParciaisVencidos() {
		return qtdParciaisVencidos;
	}

	public void setQtdParciaisVencidos(Integer[] qtdParciaisVencidos) {
		this.qtdParciaisVencidos = qtdParciaisVencidos;
	}

	public Integer[] getQtdParciaisPendentes() {
		return qtdParciaisPendentes;
	}

	public void setQtdParciaisPendentes(Integer[] qtdParciaisPendentes) {
		this.qtdParciaisPendentes = qtdParciaisPendentes;
	}

	public Integer[] getQtdParciaisEncerrados() {
		return qtdParciaisEncerrados;
	}

	public void setQtdParciaisEncerrados(Integer[] qtdParciaisEncerrados) {
		this.qtdParciaisEncerrados = qtdParciaisEncerrados;
	}

	public Integer[] getQtdLeitosTotais() {
		return qtdLeitosTotais;
	}

	public void setQtdLeitosTotais(Integer[] qtdLeitosTotais) {
		this.qtdLeitosTotais = qtdLeitosTotais;
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
