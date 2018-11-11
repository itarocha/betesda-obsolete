package br.com.itarocha.betesda.model.hospedagem;

public class HospedagemInfo {

	private Long hospedagemId;
	private Long hospedeLeitoId;
	private Long hospedeId;
	private Long pessoaId;
	private Boolean inicio;
	private Boolean durante;
	private Boolean fim;
	private Boolean ponta;
	
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
	public Boolean getPonta() {
		return this.ponta;
	}
	public void setPonta(Boolean ponta) {
		this.ponta = ponta;
	}
	public Long getHospedeLeitoId() {
		return this.hospedeLeitoId;
	}
	public void setHospedeLeitoId(Long id) {
		this.hospedeLeitoId = id;
	}
}
