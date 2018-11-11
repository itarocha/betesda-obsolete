package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;

public class HospedagemHeader {
	
	private Long hospedagemId;
	private Long pessoaId;
	private String pessoaNome;
	private LocalDate dataEntrada;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	
	public HospedagemHeader(Long id, Long pessoaId, String pessoaNome, LocalDate dataEntrada, LocalDate dataPrevistaSaida, LocalDate dataEfetivaSaida) {
		this.hospedagemId = id;
		this.pessoaId = pessoaId;
		this.pessoaNome = pessoaNome;
		this.dataEntrada = dataEntrada;
		this.dataPrevistaSaida = dataPrevistaSaida;
		this.dataEfetivaSaida = dataEfetivaSaida;
	}
	public Long getHospedagemId() {
		return hospedagemId;
	}
	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}
	public Long getPessoaId() {
		return pessoaId;
	}
	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}
	public String getPessoaNome() {
		return pessoaNome;
	}
	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
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
