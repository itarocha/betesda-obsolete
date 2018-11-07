package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;

public class HospedagemHeader {
	
	private Long hospedagemId;
	private LocalDate dataEntrada;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	//private String status;
	
	public Long getHospedagemId() {
		return hospedagemId;
	}
	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}
	public LocalDate getDataEntrada() {
		return dataEntrada;
	}
	public void setDataEntrada(LocalDate dataEntrada) {
		this.dataEntrada = dataEntrada;
	}
	public LocalDate getDataPrevistaSaida() {
		return dataPrevistaSaida;
	}
	public void setDataPrevistaSaida(LocalDate dataPrevistaSaida) {
		this.dataPrevistaSaida = dataPrevistaSaida;
	}
	public LocalDate getDataEfetivaSaida() {
		return dataEfetivaSaida;
	}
	public void setDataEfetivaSaida(LocalDate dataEfetivaSaida) {
		this.dataEfetivaSaida = dataEfetivaSaida;
	}
	public String getStatus() {
		LocalDate hoje = LocalDate.now();
		String status = "aberta";
		if (this.getDataEfetivaSaida() != null) {
			status = "encerrada";
		} else if (this.getDataPrevistaSaida().isBefore(hoje) ) {
			status = "vencida";
		} else {
			status = "aberta";
		}
		return status;
	}
	/*
	public void setStatus(String status) {
		this.status = status;
	}
	*/

}
