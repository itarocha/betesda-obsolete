package br.com.itarocha.betesda.model;

//import java.time.LocalDate;

//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIgnoreProperties({"hospedexxxx", "hospedeLeitoxxxx"})
public class HospedeLeitoVO {

	private String tipoUtilizacao;
	private Hospedagem hospedagem;
	private Hospede hospede;
	private HospedeLeito hospedeLeito;
	
	public HospedeLeitoVO() {}

	public HospedeLeitoVO(String tipoUtilizacao, Hospedagem hospedagem, Hospede hospede) {
		this.tipoUtilizacao = tipoUtilizacao;
		this.hospedagem = hospedagem;
		this.hospede = hospede;
	}

	public HospedeLeitoVO(String tipoUtilizacao, Hospedagem hospedagem, Hospede hospede, HospedeLeito hospedeLeito) {
		this(tipoUtilizacao, hospedagem, hospede);
		this.hospedeLeito = hospedeLeito;
	}
	

	public String getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(String tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public Hospede getHospede() {
		return hospede;
	}

	public void setHospede(Hospede hospede) {
		this.hospede = hospede;
	}

	public Hospedagem getHospedagem() {
		return hospedagem;
	}

	public void setHospedagem(Hospedagem hospedagem) {
		this.hospedagem = hospedagem;
	}

	public HospedeLeito getHospedeLeito() {
		return hospedeLeito;
	}

	public void setHospedeLeito(HospedeLeito hospedeLeito) {
		this.hospedeLeito = hospedeLeito;
	}

}
