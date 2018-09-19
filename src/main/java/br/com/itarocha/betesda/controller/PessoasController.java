package br.com.itarocha.betesda.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.itarocha.betesda.model.Pessoa;
import br.com.itarocha.betesda.service.PessoaService;
import br.com.itarocha.betesda.util.validation.ItaValidator;


@CrossOrigin
@RestController
@RequestMapping("/api/pessoas")
public class PessoasController {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private PessoaService service;
	
	@RequestMapping(value="{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			Optional<Pessoa> model = service.find(id);
			if (model.isPresent()) {
				return new ResponseEntity<Pessoa>(model.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("não encontrado", HttpStatus.NOT_FOUND);
			}
		} finally {
			em.close();
		}
	}

	@RequestMapping
	public ResponseEntity<?> listar() {
		List<Pessoa> lista = service.findAll();
		return new ResponseEntity<List<Pessoa>>(lista, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/consultar/{texto}")
	public ResponseEntity<?> consultar(@PathVariable("texto") String texto) {
		List<Pessoa> lista = service.consultar(texto);
		return new ResponseEntity<List<Pessoa>>(lista, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> gravar(@RequestBody Pessoa model) {
		ItaValidator<Pessoa> v = new ItaValidator<Pessoa>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			Pessoa saved = null;
			if (model.getId() == null) {
				saved = service.create(model);
			} else {
				saved = service.update(model);
			}
		    return new ResponseEntity<Pessoa>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}
