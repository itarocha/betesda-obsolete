package br.com.itarocha.betesda.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.itarocha.betesda.model.HospedagemVO;
import br.com.itarocha.betesda.model.hospedagem.MapaHospedagem;
import br.com.itarocha.betesda.service.HospedagemService;
import br.com.itarocha.betesda.util.validation.ItaValidator;

@CrossOrigin
@RestController
@RequestMapping("/api/hospedagem")
public class HospedagemController {

	@Autowired
	private HospedagemService service;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> gravar(@RequestBody HospedagemVO model) {
		ItaValidator<HospedagemVO> v = new ItaValidator<HospedagemVO>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			service.create(model);
		    return new ResponseEntity<HospedagemVO>(model, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/mapa", method = RequestMethod.POST)
	public MapaHospedagem mapa(@RequestBody MapaHospedagemRequest model)
	{
		MapaHospedagem listagem = new MapaHospedagem();
		try {
			listagem  = service.getHospedagens(model.data);
		} catch(Exception e) {
			throw e;
		} 
		
		return listagem;
	}
	
	@RequestMapping(value="/mapa/hospedagem_info", method = RequestMethod.POST)
	public String getHospedagemInfo(@RequestBody HospdeagemInfoRequest model)
	{
		System.out.println("Recebido: "+model.hospedeLeitoId);
		
		return "Ok";
	}

	@RequestMapping(value="/mapa/testes", method = RequestMethod.POST)
	public String testes(@RequestBody OperacoesRequest model)
	{
		System.out.println("Recebido: "+model.hospedagemId + " e "+ model.data);
		service.encerrarHospedagem(model.hospedagemId, model.data);
		
		return "Ok";
	}

	private static class MapaHospedagemRequest{
		public LocalDate data;
	}

	private static class OperacoesRequest{
		public LocalDate data;
		public Long hospedagemId;
	}

	private static class HospdeagemInfoRequest{
		public Long hospedeLeitoId;
	}
}
