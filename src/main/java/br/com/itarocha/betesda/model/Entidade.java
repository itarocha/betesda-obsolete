package br.com.itarocha.betesda.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="entidade")
public class Entidade {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message="Nome é obrigatório")
	@Size(min = 3, max = 64, message="Nome deve ter entre 3 a 64 caracteres")
	private String nome;

	@NotNull(message="CNPJ é obrigatório")
	@Size(min = 14, max = 14, message="CNPJ deve ter 14 caracteres")
	private String cnpj;
	
	@Valid
	@ManyToOne()
	@NotNull(message="Endereço é obrigatório")
	private Endereco endereco;
	
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;
	
	@Pattern(regexp="^$|[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	             message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;

	@OneToMany(mappedBy = "entidade",fetch=FetchType.LAZY)
	private List<Encaminhador> encaminhadores;
	
	public Entidade(){
		this.endereco = new Endereco();
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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
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

	public List<Encaminhador> getEncaminhadores() {
		return encaminhadores;
	}

	public void setEncaminhadores(List<Encaminhador> encaminhadores) {
		this.encaminhadores = encaminhadores;
	}

	@Override
    public String toString() {
        return String.format("EntidadeEncaminhadora[id=%d, nome='%s']",id, nome);
    }	

}