package br.com.itarocha.betesda.model.hospedagem;

public class HospedagemMapa {
	
	private DiaHospedagem[] dias;
	
	public HospedagemMapa(int numeroDias) {
		this.dias = new DiaHospedagem[numeroDias];
		for (int i = 0; i < numeroDias; i++) {
			this.dias[i] = new DiaHospedagem(i);
		}
	}

	public DiaHospedagem[] getDias() {
		return dias;
	}

	public void setDias(DiaHospedagem[] dias) {
		this.dias = dias;
	}
	
}
