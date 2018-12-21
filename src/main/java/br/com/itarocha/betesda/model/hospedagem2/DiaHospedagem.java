package br.com.itarocha.betesda.model.hospedagem2;

import br.com.itarocha.betesda.model.hospedagem.CellAndamento;

public class DiaHospedagem {
	
	private Integer index;
	private String identificador;
	private CellAndamento andamento;
	private Boolean firstIndex;
	private Boolean atendimento;
	private Boolean possuiContinuidade;
	private Boolean continuacao;
	
	
	public DiaHospedagem(Integer index) {
		this.identificador = "0";
		this.firstIndex = false;
		this.atendimento = false;
		this.possuiContinuidade = false;
		this.continuacao = false;
		this.index = index;
	}
	
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Integer getIndex() {
		return this.index;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public CellAndamento getAndamento() {
		return this.andamento;
	}
	
	public void setAndamento(CellAndamento andamento) {
		this.andamento = andamento;
	}
	
	public Boolean getFirstIndex() {
		return this.firstIndex;
	}
	
	public void setFirstIndex(Boolean firstIndex) {
		this.firstIndex = firstIndex;
	}
	
	public Boolean getAtendimento() {
		return atendimento;
	}
	
	public void setAtendimento(Boolean atendimento) {
		this.atendimento = atendimento;
	}

	public Boolean getPossuiContinuidade() {
		return possuiContinuidade;
	}

	public void setPossuiContinuidade(Boolean possuiContinuidade) {
		this.possuiContinuidade = possuiContinuidade;
	}

	public Boolean getContinuacao() {
		return continuacao;
	}

	public void setContinuacao(Boolean continuacao) {
		this.continuacao = continuacao;
	}
	
	public String getClasse() {
		return this.possuiContinuidade ? "INDO" : (this.continuacao ? "VINDO" : this.andamento.toString());
	}
}
