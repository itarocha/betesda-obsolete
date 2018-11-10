package br.com.itarocha.betesda.model;

public class HospedeHospedagemVO {
	
	private Hospede hospede;
	private Hospedagem hospedagem;
	
	public HospedeHospedagemVO(Hospede hospede, Hospedagem hospedagem) {
		this.hospede = hospede;
		this.hospedagem = hospedagem;
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
}
