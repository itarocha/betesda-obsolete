package br.com.itarocha.betesda.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="encaminhador")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Encaminhador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="entidade_id")
	@NotNull(message="Entidade é obrigatória")
	private Entidade entidade;
	
	@NotNull(message="Nome é obrigatório")
	@Size(min = 2, max = 64, message="Nome deve ter entre 2 a 64 caracteres")
	private String nome;
	
	@NotNull(message="Cargo é obrigatório")
	@Size(min = 2, max = 64, message="Cargo deve ter entre 2 a 64 caracteres")
	private String cargo;

	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;
	
	@Pattern(regexp="^$|[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	             message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Entidade getEntidade() {
		return entidade;
	}

	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
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
	
	
}
