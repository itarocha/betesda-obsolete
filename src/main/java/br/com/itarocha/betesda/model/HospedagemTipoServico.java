package br.com.itarocha.betesda.model;

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
@Table(name="hospedagem_tipo_servico")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // "leitos", "hospedagem" 
public class HospedagemTipoServico {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hospedagem_id")
	@NotNull(message="Hospedagem precisa ser informado")
	private Hospedagem hospedagem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tipo_servico_id")
	@NotNull(message="Tipo de Serviço precisa ser informado")
	private TipoServico tipoServico;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Hospedagem getHospedagem() {
		return this.hospedagem;
	}

	public void setHospedagem(Hospedagem hospedagem) {
		this.hospedagem = hospedagem;
	}

	public TipoServico getTipoServico() {
		return this.tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

}
