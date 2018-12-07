package br.com.itarocha.betesda.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="pessoa")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pessoa {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message="Nome é obrigatório")
	@Size(min = 3, max = 64, message="Nome deve ter entre 2 a 64 caracteres")
	private String nome;

	@NotNull(message="Data de Nascimento é obrigatório")
	//@Temporal(TemporalType.DATE)
	private LocalDate dataNascimento;

	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private EstadoCivil estadoCivil;
	
	@Size(min = 11, max = 11, message="CPF deve ter 11 caracteres")
	private String cpf;
	
	@Size(max = 32, message="RG deve ter até 32 caracteres")
	private String rg;

	@Valid
	@ManyToOne()
	@NotNull(message="Endereço deve ser preenchido")
	private Endereco endereco;
	
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;
	
	@Pattern(regexp="^$|[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	             message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String observacoes;

	public Pessoa(){
		this.endereco = new Endereco();
		this.sexo = Sexo.F;
	}

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

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	@Override
    public String toString() {
        return String.format("Pessoa[id=%d, nome='%s']",id, nome);
    }	

}
