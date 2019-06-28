package br.com.itarocha.betesda.report;

public class CidadeQuantidade {
	
	private String cidade;
	private String uf;
	private Long quantidade;
	
	public CidadeQuantidade(String cidade, String uf, Long quantidade) {
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

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	
	
}
