package br.com.itarocha.betesda.report;

public class PessoaEstatistica {
	private Long id;
	private String nome;
	private String faixaEtaria;
	private String cidadeOrigem;
	private String cidadeOrigemUf;
	private String encaminhadorNome;
	private String tipoUtilizacaoDescricao;
	private String tipoHospede;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getFaixaEtaria() {
		return faixaEtaria;
	}
	public void setFaixaEtaria(String faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}
	public String getCidadeOrigem() {
		return cidadeOrigem;
	}
	public void setCidadeOrigem(String cidadeOrigem) {
		this.cidadeOrigem = cidadeOrigem;
	}
	public String getCidadeOrigemUf() {
		return cidadeOrigemUf;
	}
	public void setCidadeOrigemUf(String cidadeOrigemUf) {
		this.cidadeOrigemUf = cidadeOrigemUf;
	}
	public String getEncaminhadorNome() {
		return encaminhadorNome;
	}
	public void setEncaminhadorNome(String encaminhadorNome) {
		this.encaminhadorNome = encaminhadorNome;
	}
	public String getTipoUtilizacaoDescricao() {
		return tipoUtilizacaoDescricao;
	}
	public void setTipoUtilizacaoDescricao(String tipoUtilizacaoDescricao) {
		this.tipoUtilizacaoDescricao = tipoUtilizacaoDescricao;
	}
	public String getTipoHospede() {
		return tipoHospede;
	}
	public void setTipoHospede(String tipoHospede) {
		this.tipoHospede = tipoHospede;
	}
	
	
} 
