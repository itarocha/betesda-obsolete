package br.com.itarocha.betesda.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.itarocha.betesda.model.Entidade;
import br.com.itarocha.betesda.service.EntidadeService;
import br.com.itarocha.betesda.util.validation.ItaValidator;


@CrossOrigin
@RestController
@RequestMapping("/api/entidades")
public class EntidadesController {

	@Autowired
	private EntidadeService service;
	
	@RequestMapping(value="{id}")
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
	public ResponseEntity<?> listar() {
		List<Entidade> lista = service.findAll();
		return new ResponseEntity<List<Entidade>>(lista, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/consultar/{texto}")
	public ResponseEntity<?> consultar(@PathVariable("texto") String texto) {
		List<Entidade> lista = service.consultar(texto);
		return new ResponseEntity<List<Entidade>>(lista, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> gravar(@RequestBody Entidade model) {
		ItaValidator<Entidade> v = new ItaValidator<Entidade>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			Entidade saved = null;
			saved = service.create(model);
		    return new ResponseEntity<Entidade>(saved, HttpStatus.OK);
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
