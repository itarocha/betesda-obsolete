package br.com.itarocha.betesda.report;

public class CidadeQuantidade {
	
	private String cidade;
	private String uf;
	private Integer quantidade;
	
	public CidadeQuantidade(String cidade, String uf, Integer quantidade) {
		super();
		this.cidade = cidade;
		this.uf = uf;
		this.quantidade = quantidade;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	
}
