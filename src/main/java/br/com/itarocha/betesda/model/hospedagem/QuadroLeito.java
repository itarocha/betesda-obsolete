package br.com.itarocha.betesda.model.hospedagem;

public class QuadroLeito {
	public Long id;
	public Integer numero;
	private Integer[] dias;
	
	public QuadroLeito(Long id, Integer numero, int numeroDias) {
		this.id = id;
		this.numero = numero;

		this.dias = new Integer[numeroDias];
		for (int i = 0; i < numeroDias; i++) {
			this.dias[i] = 0;
		}
	}

	public Integer[] getDias() {
		return dias;
	}

	public void setDias(Integer[] dias) {
		this.dias = dias;
	}
	
	
}
