package br.ufrn.SistemaBancario.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.ufrn.SistemaBancario.dao.ContaDAO;
import br.ufrn.SistemaBancario.model.Conta;
import br.ufrn.SistemaBancario.model.ContaBonus;
import br.ufrn.SistemaBancario.model.TipoConta;
import br.ufrn.SistemaBancario.model.exceptions.OperacaoIlegalException;

@RestController
@RequestMapping("/banco/conta")
public class ContaController {
	
	private ContaDAO dao = new ContaDAO();
	private int counter = 0;
	
	@PostMapping
	public ResponseEntity<JsonNode> cadastrarConta(@RequestBody JsonNode body) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode response = objectMapper.createObjectNode();
		
		TipoConta tipoConta = TipoConta.values()[body.get("tipoConta").asInt(0)];
		long numConta = counter; 
		
		switch(tipoConta) {
		case CONTAPOUPANCA:
			try {
				Double saldoInicial = body.get("saldo").asDouble(0);
				dao.cadastrarContaPoupanca(numConta, saldoInicial);
			} catch (OperacaoIlegalException e) {
				e.printStackTrace();
				response.put("error", e.getMessage());
			}
			break;
		case CONTA:
			Double saldoInicial = body.get("saldo").asDouble(0);
			dao.cadastrarContaSimples(numConta, saldoInicial);
			break;
		case CONTABONUS:
			try {
				dao.cadastrarConta(numConta, TipoConta.CONTABONUS);
			} catch (OperacaoIlegalException e) {
				e.printStackTrace();
				response.put("error", e.getMessage());
			}
			break;
		default:
			response.put("error", "Não foi possível criar a conta");
		}
		
		if(!response.has("error")) {
			response.put("id", numConta);
			counter++;
		}
		

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<JsonNode> consultarConta(@PathVariable("id") long id){
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode response = objectMapper.createObjectNode();
		
		try {
			Conta c = dao.consultarConta(id);
			response.put("id", c.getNumero());
			response.put("saldo", c.getSaldo());
			
			if(c instanceof ContaBonus) {
				response.put("pontuacao", ((ContaBonus) c).getPontuacao());
			}
		} catch (OperacaoIlegalException e) {
			e.printStackTrace();
			response.put("error", e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}/saldo")
	public ResponseEntity<JsonNode> consultarSaldo(@PathVariable("id") long id){
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode response = objectMapper.createObjectNode();
		
		Double saldo = dao.consultarSaldo(id);
		
		response.put("saldo", saldo);
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}/credito")
	public ResponseEntity<JsonNode> credito(@PathVariable("id") long id, @RequestBody JsonNode body){
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode response = objectMapper.createObjectNode();
		
		if(body.has("valor")) {
			Double valor = body.get("valor").asDouble(0);
			try {
				dao.credito(id, valor);
				response.put("saldo", dao.consultarSaldo(id));
			} catch (OperacaoIlegalException e) {
				e.printStackTrace();
				response.put("error", e.getMessage());
			}
		} else {
			response.put("error", "Valor vazio");
		}
		
		return ResponseEntity.ok(response);
		
	}
	
	@PutMapping("/{id}/debito")
	public ResponseEntity<JsonNode> debito(@PathVariable("id") long id, @RequestBody JsonNode body){
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode response = objectMapper.createObjectNode();
		
		if(body.has("valor")) {
			Double valor = body.get("valor").asDouble(0);
			try {
				dao.debito(id, valor);
				response.put("saldo", dao.consultarSaldo(id));
			} catch (OperacaoIlegalException e) {
				e.printStackTrace();
				response.put("error", e.getMessage());
			}
		} else {
			response.put("error", "Valor vazio");
		}
		
		return ResponseEntity.ok(response);
		
	}
	
	@PutMapping("/transferencia")
	public ResponseEntity<JsonNode> transferencia(@RequestBody JsonNode body){
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode response = objectMapper.createObjectNode();
		
		if(body.has("valor") && body.has("origem") && body.has("destino")) {
			Double valor = body.get("valor").asDouble(0);
			long origem = body.get("origem").asLong(-1);
			long destino = body.get("destino").asLong(-1);
			try {
				dao.transferir(origem, destino, valor);
				response.put("saldoOrigem", dao.consultarSaldo(origem));
				response.put("saldoDestino", dao.consultarSaldo(destino));
			} catch (OperacaoIlegalException e) {
				e.printStackTrace();
				response.put("error", e.getMessage());
			}
		} else {
			response.put("error", "Valor, conta de origem e conta de destino não podem ser vazios");
		}
		
		return ResponseEntity.ok(response);
		
	}
	
	@PutMapping("/rendimento")
	public ResponseEntity<JsonNode> renderJuros(@RequestBody JsonNode body){
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode response = objectMapper.createObjectNode();
		
		if(body.has("id") && body.has("taxaPercentual")) {
			long id = body.get("id").asLong(-1);
			double taxaPercentual = body.get("taxaPercentual").asDouble(0);
			
			try {
				dao.renderJuros(id, taxaPercentual);
				response.put("saldo", dao.consultarSaldo(id));
			} catch (OperacaoIlegalException e) {
				e.printStackTrace();
				response.put("error", e.getMessage());
			}
		} else {
			response.put("error", "Id e taxa percentual não podem ser vazios");
		}
		
		return ResponseEntity.ok(response);
	}
	
}
