package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;

public class Celula {
	
	private String tipo;
	private LocalDate data;
	private Boolean inicio;
	private Boolean durante;
	private Boolean fim;
	private Long hospedagemId;
	private Long hospedeId;
	private Long pessoaId;
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public Boolean getInicio() {
		return inicio;
	}
	public void setInicio(Boolean inicio) {
		this.inicio = inicio;
	}
	public Boolean getDurante() {
		return durante;
	}
	public void setDurante(Boolean durante) {
		this.durante = durante;
	}
	public Boolean getFim() {
		return fim;
	}
	public void setFim(Boolean fim) {
		this.fim = fim;
	}
	public Long getHospedagemId() {
		return hospedagemId;
	}
	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}
	public Long getHospedeId() {
		return hospedeId;
	}
	public void setHospedeId(Long hospedeId) {
		this.hospedeId = hospedeId;
	}
	public Long getPessoaId() {
		return pessoaId;
	}
	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}
	
	
}
