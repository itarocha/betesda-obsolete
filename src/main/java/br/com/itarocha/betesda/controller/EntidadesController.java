package br.com.itarocha.betesda.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.model.Entidade;
import br.com.itarocha.betesda.service.EntidadeService;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import br.com.itarocha.betesda.utils.Validadores;

@RestController
@RequestMapping("/api/app/entidades")
public class EntidadesController {

	@Autowired
	private EntidadeService service;
	
	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			Optional<Entidade> model = service.find(id);
			if (model.isPresent()) {
				return new ResponseEntity<Entidade>(model.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("não encontrado", HttpStatus.NOT_FOUND);
			}
		} finally {
			//em.close();
		}
	}

	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<Entidade> lista = service.findAll();
		return new ResponseEntity<List<Entidade>>(lista, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/consultar/{texto}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> consultar(@PathVariable("texto") String texto) {
		List<Entidade> lista = service.consultar(texto);
		return new ResponseEntity<List<Entidade>>(lista, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody Entidade model) {
		
		if (model.getCnpj() != null) {
			model.setCnpj(model.getCnpj().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
		}
		if (model.getEndereco() != null && model.getEndereco().getCep() != null) {
			model.getEndereco().setCep((model.getEndereco().getCep().replaceAll("\\-", "")));
		}
		
		ItaValidator<Entidade> v = new ItaValidator<Entidade>(model);
		v.validate();
		
		if (model.getCnpj() != null && model.getCnpj() != "") {
			if (!Validadores.isValidCNPJ(model.getCnpj())) {
				v.addError("cnpj", "CNPJ inválido");
			}
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			Entidade saved = null;
			saved = service.create(model);
		    return new ResponseEntity<Entidade>(saved, HttpStatus.OK);
		} catch (ValidationException e) {
			ResponseEntity<?> re = new ResponseEntity<>(e.getRe(), HttpStatus.BAD_REQUEST); 
			return re;
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}
