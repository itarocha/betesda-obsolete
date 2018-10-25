package br.com.itarocha.betesda.model;

import java.time.LocalDate;

//import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="hospede_leito")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quarto", "leito", "hospede"})
public class HospedeLeito {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hospede_id")
	@NotNull(message="Hóspede precisa ser informado")
	private Hospede hospede;
	
	
	//@Temporal(TemporalType.DATE)
	@NotNull(message="Data de Entrada precisa ser informado")
	@Column(name="data_entrada")
	private LocalDate dataEntrada;
	
	//@Temporal(TemporalType.DATE)
	@NotNull(message="Data de Saída precisa ser informado")
	@Column(name="data_saida")
	private LocalDate dataSaida;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="quarto_id")
	@NotNull(message="Quarto precisa ser informado")
	private Quarto quarto;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="leito_id")
	@NotNull(message="Leito precisa ser informado")
	private Leito leito;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Hospede getHospede() {
		return hospede;
	}

	public void setHospede(Hospede hospede) {
		this.hospede = hospede;
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

	public Quarto getQuarto() {
		return quarto;
	}

	public void setQuarto(Quarto quarto) {
		this.quarto = quarto;
	}

	public Leito getLeito() {
		return leito;
	}

	public void setLeito(Leito leito) {
		this.leito = leito;
	}

}