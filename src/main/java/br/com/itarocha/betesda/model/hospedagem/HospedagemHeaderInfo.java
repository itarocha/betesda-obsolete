package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;

public class HospedagemHeaderInfo {

	private Long id; // Pode ser hospedagemId para Parcial ou hospedeLeitoId se Total
	private String identificador;
	private Long hospedagemId;
	private Long hospedeLeitoId;
	private Long hospedeId;
	
	private Long destinacaoHospedagemId;
	private String destinacaoHospedagemDescricao;
	
	
	private Long pessoaId;
	private String pessoaNome;
	
	private LocalDate dataEntrada;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	
	private LocalDate leitoDataEntrada;
	private LocalDate leitoDataSaida; 
	
	private CellStatusHospedagem statusHospedagem; // PENDENTE / VENCIDA / ENCERRADA
	private CellUtilizacao utilizacao; // TOTAL / PARCIAL
	
	private Long leitoId;
	private Integer leitoNumero;
    private Long quartoId;
    private Integer quartoNumero;	
	
	private Boolean baixado;
	
	public HospedagemHeaderInfo() {
		this.baixado = false;
		this.leitoId = 0L;
		this.leitoNumero = 0;
		this.quartoId = 0L;
		this.quartoNumero = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Long getHospedagemId() {
		return hospedagemId;
	}

	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}

	public Long getHospedeLeitoId() {
		return hospedeLeitoId;
	}

	public void setHospedeLeitoId(Long hospedeLeitoId) {
		this.hospedeLeitoId = hospedeLeitoId;
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

	public LocalDate getDataSaida() {
		return this.dataEfetivaSaida == null ? this.dataPrevistaSaida : this.dataEfetivaSaida;
	}

	public LocalDate getLeitoDataEntrada() {
		return leitoDataEntrada;
	}

	public void setLeitoDataEntrada(LocalDate leitoDataEntrada) {
		this.leitoDataEntrada = leitoDataEntrada;
	}

	public Long getLeitoId() {
		return leitoId;
	}

	public void setLeitoId(Long leitoId) {
		this.leitoId = leitoId;
	}

	public Integer getLeitoNumero() {
		return leitoNumero;
	}

	public void setLeitoNumero(Integer leitoNumero) {
		this.leitoNumero = leitoNumero;
	}

	public Long getQuartoId() {
		return quartoId;
	}

	public void setQuartoId(Long quartoId) {
		this.quartoId = quartoId;
	}

	public Integer getQuartoNumero() {
		return quartoNumero;
	}

	public void setQuartoNumero(Integer quartoNumero) {
		this.quartoNumero = quartoNumero;
	}

	public LocalDate getLeitoDataSaida() {
		return leitoDataSaida;
	}

	public void setLeitoDataSaida(LocalDate leitoDataSaida) {
		this.leitoDataSaida = leitoDataSaida;
	}

	public CellStatusHospedagem getStatusHospedagem() {
		return statusHospedagem;
	}

	public void setStatusHospedagem(CellStatusHospedagem statusHospedagem) {
		this.statusHospedagem = statusHospedagem;
	}

	public CellUtilizacao getUtilizacao() {
		return utilizacao;
	}

	public void setUtilizacao(CellUtilizacao utilizacao) {
		this.utilizacao = utilizacao;
	}

	public Long getDestinacaoHospedagemId() {
		return destinacaoHospedagemId;
	}

	public void setDestinacaoHospedagemId(Long destinacaoHospedagemId) {
		this.destinacaoHospedagemId = destinacaoHospedagemId;
	}

	public String getDestinacaoHospedagemDescricao() {
		return destinacaoHospedagemDescricao;
	}

	public void setDestinacaoHospedagemDescricao(String destinacaoHospedagemDescricao) {
		this.destinacaoHospedagemDescricao = destinacaoHospedagemDescricao;
	}

	public Boolean getPossuiContinuidade() {
		return //(!this.baixado) && 
				(this.leitoDataSaida != null) &&
				(this.getDataSaida() != null) &&
				(this.getDataSaida().isAfter(this.leitoDataSaida));    
	}

	public Boolean getContinuacao() {
		return //(!this.baixado) && 
				(this.dataEntrada != null) &&
				(this.getDataSaida() != null) && 
				(this.leitoDataEntrada.isAfter(this.dataEntrada));    
	}
	
	
	public Boolean getBaixado() {
		return baixado;
	}

	public void setBaixado(Boolean baixado) {
		this.baixado = baixado;
	}
	
}
