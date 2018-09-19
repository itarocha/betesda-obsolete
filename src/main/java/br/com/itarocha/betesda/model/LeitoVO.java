package br.com.itarocha.betesda.model;

//import com.google.gson.annotations.SerializedName;

public class LeitoVO {
	
	//@SerializedName("id")
	private Long id;
	
	//@SerializedName("quartoId")
	private Long quarto_id;
	
	//@SerializedName("numero")
	private Integer numero;
	
	//@SerializedName("tipoLeitoId")
	private Long tipo_leito_id;

	//@SerializedName("tipoLeitoDescricao")
	private String tipo_leito_descricao;
	
	//@SerializedName("situacaoLeitoId")
	private Long situacao_leito_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuarto_id() {
		return quarto_id;
	}

	public void setQuarto_id(Long quarto_id) {
		this.quarto_id = quarto_id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Long getTipo_leito_id() {
		return tipo_leito_id;
	}

	public void setTipo_leito_id(Long tipo_leito_id) {
		this.tipo_leito_id = tipo_leito_id;
	}

	public String getTipo_leito_descricao() {
		return tipo_leito_descricao;
	}

	public void setTipo_leito_descricao(String tipo_leito_descricao) {
		this.tipo_leito_descricao = tipo_leito_descricao;
	}

	public Long getSituacao_leito_id() {
		return situacao_leito_id;
	}

	public void setSituacao_leito_id(Long situacao_leito_id) {
		this.situacao_leito_id = situacao_leito_id;
	}
	
}
