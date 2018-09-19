package br.com.itarocha.betesda.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="hospedagem")
public class Hospedagem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="entidade_id")
	//////////////////@NotNull(message="Entidade é obrigatório") // por enquanto não
	private Entidade entidade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="encaminhador_id")
	//////////////////@NotNull(message="Encaminhador é obrigatório") // por enquanto não
	private Encaminhador encaminhador;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="destinacao_hospedagem_id")
	@NotNull(message="Destinação da Hospedagem é obrigatória")
	private DestinacaoHospedagem destinacaoHospedagem;
			
	@NotNull(message="Data de Entrada precisa ser informado")
	@Temporal(TemporalType.DATE)
	@Column(name = "data_entrada")
	private Date dataEntrada;
	
	@NotNull(message="Data Prevista de Saída precisa ser informada")
	@Temporal(TemporalType.DATE)
	@Column(name = "data_prevista_saida")
	private Date dataPrevistaSaida;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_efetiva_saida")
	private Date dataEfetivaSaida;

	@Enumerated(EnumType.STRING)
	@Column(name="tipo_utilizacao", length=1)
	@NotNull(message="Tipo de Utilização precisa ser informada")
	private TipoUtilizacaoHospedagem tipoUtilizacao;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String observacoes;
	
	@OneToMany(mappedBy = "hospedagem",fetch=FetchType.LAZY)
	private List<Hospede> hospedes = new ArrayList<Hospede>();
	
	public Hospedagem() {
		this.tipoUtilizacao = TipoUtilizacaoHospedagem.T;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Entidade getEntidade() {
		return this.entidade;
	}

	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}

	public Encaminhador getEncaminhador() {
		return this.encaminhador;
	}

	public void setEncaminhador(Encaminhador encaminhador) {
		this.encaminhador = encaminhador;
	}

	public Date getDataEntrada() {
		return this.dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Date getDataPrevistaSaida() {
		return this.dataPrevistaSaida;
	}

	public void setDataPrevistaSaida(Date dataPrevistaSaida) {
		this.dataPrevistaSaida = dataPrevistaSaida;
	}

	public Date getDataEfetivaSaida() {
		return dataEfetivaSaida;
	}

	public void setDataEfetivaSaida(Date dataEfetivaSaida) {
		this.dataEfetivaSaida = dataEfetivaSaida;
	}

	public String getObservacoes() {
		return this.observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public DestinacaoHospedagem getDestinacaoHospedagem() {
		return destinacaoHospedagem;
	}

	public void setDestinacaoHospedagem(DestinacaoHospedagem destinacaoHospedagem) {
		this.destinacaoHospedagem = destinacaoHospedagem;
	}

	public TipoUtilizacaoHospedagem getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(TipoUtilizacaoHospedagem tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public List<Hospede> getHospedes() {
		return this.hospedes;
	}

	public void setHospedes(List<Hospede> hospedes) {
		this.hospedes = hospedes;
	}
}
