package br.com.itarocha.betesda.model.hospedagem;

public class Cell {
	private String id;
	private Long hospedagemId;
	private CellUtilizacao utilizacao;
	private CellAndamento andamento;
	private CellStatusHospedagem status;
	private boolean firstIndex;
	
	public Cell() {
		this.id = "0";
		hospedagemId = 0L;
		this.utilizacao = CellUtilizacao.VAZIO;
		this.andamento = CellAndamento.VAZIO;
		this.status = CellStatusHospedagem.VAZIO;
		this.firstIndex = false;
	}
	
	/*
	public Cell(String id, CellUtilizacao utilizacao, CellAndamento andamento, CellStatusHospedagem status) {
		this.id = id;
		this.utilizacao = utilizacao;
		this.andamento = andamento;
		this.status = status;
	}
	*/
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getHospedagemId() {
		return hospedagemId;
	}

	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}

	public CellUtilizacao getUtilizacao() {
		return utilizacao;
	}

	public void setUtilizacao(CellUtilizacao utilizacao) {
		this.utilizacao = utilizacao;
	}

	public CellAndamento getAndamento() {
		return andamento;
	}

	public void setAndamento(CellAndamento andamento) {
		this.andamento = andamento;
	}

	public CellStatusHospedagem getStatus() {
		return status;
	}

	public void setStatus(CellStatusHospedagem status) {
		this.status = status;
	}

	public boolean isFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(boolean firstIndex) {
		this.firstIndex = firstIndex;
	}
	
}
