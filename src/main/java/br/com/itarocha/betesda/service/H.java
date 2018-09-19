package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.Hospede;

public class H {
	public Long id;
	
	public Hospede hospede;
	
	public H() {}
	
	public H(Long id, Hospede h) {
		this.id = id;
		this.hospede = h;
	}
	/*
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
	*/
}
