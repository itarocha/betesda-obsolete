package br.com.itarocha.betesda.report;

import java.time.LocalDate;

public class HospedePermanencia {
	
	private Long pessoaId;
	private Long hospedagemId;
	private Long encaminhadorId;
	private LocalDate dataEntrada;
	private LocalDate dataSaida;
	private String tipoUtilizacao;
	private Long diasPermanencia;
	
	public HospedePermanencia(Long pessoaId, Long hospedagemId, Long encaminhadorId, LocalDate dataEntrada,
			LocalDate dataSaida, String tipoUtilizacao) {
		this.pessoaId = pessoaId;
		this.hospedagemId = hospedagemId;
		this.encaminhadorId = encaminhadorId;
		this.dataEntrada = dataEntrada;
		this.dataSaida = dataSaida;
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public Long getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}

	public Long getHospedagemId() {
		return hospedagemId;
	}

	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}

	public Long getEncaminhadorId() {
		return encaminhadorId;
	}

	public void setEncaminhadorId(Long encaminhadorId) {
		this.encaminhadorId = encaminhadorId;
	}

	public LocalDate getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(LocalDate dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public LocalDate getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(LocalDate dataSaida) {
		this.dataSaida = dataSaida;
	}

	public String getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(String tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public Long getDiasPermanencia() {
		return diasPermanencia;
	}

	public void setDiasPermanencia(Long diasPermanencia) {
		this.diasPermanencia = diasPermanencia;
	}

}
