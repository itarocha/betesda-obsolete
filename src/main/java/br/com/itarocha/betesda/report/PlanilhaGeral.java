package br.com.itarocha.betesda.report;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "cidadeOrigem" })
public class PlanilhaGeral {
	
	private Long pessoaId;
	private String pessoaNome;
	private String pessoaCPF;
	private String pessoaRG;
	private LocalDate pessoaDataNascimento;
	private int pessoaIdade;
	private String pessoaTelefone;
	private String pessoaEndereco;
	private String pessoaCidadeOrigem;
	private String pessoaCidadeOrigemUF;
	
	private CidadeUF cidadeOrigem;
	
	private Long hospedagemId;
	private Long encaminhadorId;
	private String encaminhadorNome;
	
	private LocalDate dataEntrada;
	private LocalDate dataSaida;
	private String tipoUtilizacao;
	private String tipoHospede;
	private Long diasPermanencia;

	public Long getPessoaId() {
		return pessoaId;
	}
	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}
	public String getPessoaNome() {
		return pessoaNome;
	}
	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}
	public String getPessoaCPF() {
		return pessoaCPF;
	}
	public void setPessoaCPF(String pessoaCPF) {
		this.pessoaCPF = pessoaCPF;
	}
	public String getPessoaRG() {
		return pessoaRG;
	}
	public void setPessoaRG(String pessoaRG) {
		this.pessoaRG = pessoaRG;
	}
	public LocalDate getPessoaDataNascimento() {
		return pessoaDataNascimento;
	}
	public void setPessoaDataNascimento(LocalDate pessoaDataNascimento) {
		this.pessoaDataNascimento = pessoaDataNascimento;
	}
	public int getPessoaIdade() {
		return pessoaIdade;
	}
	public void setPessoaIdade(int pessoaIdade) {
		this.pessoaIdade = pessoaIdade;
	}
	public String getPessoaTelefone() {
		return pessoaTelefone;
	}
	public void setPessoaTelefone(String pessoaTelefone) {
		this.pessoaTelefone = pessoaTelefone;
	}
	public String getPessoaEndereco() {
		return pessoaEndereco;
	}
	public void setPessoaEndereco(String pessoaEndereco) {
		this.pessoaEndereco = pessoaEndereco;
	}
	public String getPessoaCidadeOrigem() {
		return pessoaCidadeOrigem;
	}
	public void setPessoaCidadeOrigem(String pessoaCidadeOrigem) {
		this.pessoaCidadeOrigem = pessoaCidadeOrigem == null ?  null : pessoaCidadeOrigem.trim();
	}
	public Long getHospedagemId() {
		return hospedagemId;
	}
	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}
	public Long getEncaminhadorId() {
		return encaminhadorId;
	}
	public void setEncaminhadorId(Long encaminhadorId) {
		this.encaminhadorId = encaminhadorId;
	}
	public LocalDate getDataEntrada() {
		return dataEntrada;
	}
	public void setDataEntrada(LocalDate dataEntrada) {
		this.dataEntrada = dataEntrada;
	}
	public LocalDate getDataSaida() {
		return dataSaida;
	}
	public void setDataSaida(LocalDate dataSaida) {
		this.dataSaida = dataSaida;
	}
	public String getTipoUtilizacao() {
		return tipoUtilizacao;
	}
	public void setTipoUtilizacao(String tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}
	public String getTipoHospede() {
		return tipoHospede;
	}
	public void setTipoHospede(String tipoHospede) {
		this.tipoHospede = tipoHospede;
	}
	public Long getDiasPermanencia() {
		return diasPermanencia;
	}
	public void setDiasPermanencia(Long diasPermanencia) {
		this.diasPermanencia = diasPermanencia;
	}
	
	public void setCidadeOrigem(CidadeUF cidadeUF) {
		this.pessoaCidadeOrigem = cidadeUF.getCidade();
		this.pessoaCidadeOrigemUF = cidadeUF.getUf();
		this.cidadeOrigem = cidadeUF;
	}
	
	public CidadeUF getCidadeOrigem() {
		return this.cidadeOrigem;
	}
	
	public String getPessoaCidadeOrigemUF() {
		return pessoaCidadeOrigemUF;
	}
	public void setPessoaCidadeOrigemUF(String pessoaCidadeOrigemUF) {
		this.pessoaCidadeOrigemUF = pessoaCidadeOrigemUF;
	}
	public String getEncaminhadorNome() {
		return encaminhadorNome;
	}
	public void setEncaminhadorNome(String encaminhadorNome) {
		this.encaminhadorNome = encaminhadorNome;
	}
	
}

