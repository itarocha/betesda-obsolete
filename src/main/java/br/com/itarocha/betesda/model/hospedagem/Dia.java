package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;

public class Dia {
	
	private LocalDate data;
	
	private HospedagemOut hospedagem;
	
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}

	public HospedagemOut getHospedagem() {
		return hospedagem;
	}
	public void setHospedagem(HospedagemOut hospedagem) {
		this.hospedagem = hospedagem;
	}
	
}
