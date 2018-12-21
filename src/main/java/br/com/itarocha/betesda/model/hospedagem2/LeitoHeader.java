package br.com.itarocha.betesda.model.hospedagem2;

import java.util.ArrayList;
import java.util.List;

public class LeitoHeader {
	
	private String key;
	
	private Long leitoId;
	
	private Integer leitoNumero;

	private Long quartoId;

	private Integer quartoNumero;
	
	private List<HospedagemMapa> hospedagens = new ArrayList<HospedagemMapa>();
	
	public LeitoHeader() {
		
	}
	
	public LeitoHeader(Long leitoId, Integer leitoNumero, Long quartoId, Integer quartoNumero) {
		this.leitoId = leitoId;
		this.leitoNumero = leitoNumero;
		this.quartoId = quartoId;
		this.quartoNumero = quartoNumero;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public List<HospedagemMapa> getHospedagens() {
		return hospedagens;
	}

	public void setHospedagens(List<HospedagemMapa> hospedagens) {
		this.hospedagens = hospedagens;
	}

}

