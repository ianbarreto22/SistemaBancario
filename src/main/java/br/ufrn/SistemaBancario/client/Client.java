package br.ufrn.SistemaBancario.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.ufrn.SistemaBancario.dao.ContaDAO;
import br.ufrn.SistemaBancario.model.exceptions.OperacaoIlegalException;
import br.ufrn.SistemaBancario.model.exceptions.RestRequestException;
import br.ufrn.SistemaBancario.utils.HttpUtils;

public class Client {
	
	public Client() {
		
	}
	
	public static String URI = "http://localhost:8080/banco/";
	
	public static int cadastrarConta(int tipoConta, double saldo) throws RestRequestException {
		String uri = URI + "conta";
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode body = objectMapper.createObjectNode();
		((ObjectNode) body).put("tipoConta", tipoConta);
		((ObjectNode) body).put("saldo", saldo);
		
		String bodyString = body.toString();
		
		String response = HttpUtils.httpPostRequest(uri, headerParams, bodyString, 200);
		JsonNode json;
		String id = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			id = json.get("id").asText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		int idParser = Integer.parseInt(id);
		return idParser;
	}
	
	public static int entrarConta(int numConta) throws RestRequestException {
		String uri = URI + "conta/" + numConta;
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();

		String response = HttpUtils.httpGetRequest(uri, headerParams);
		JsonNode json;
		String id = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			id = json.get("id").asText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		int idParser = Integer.parseInt(id);
		return idParser;
	}
	
	public static double consultarSaldo(int numConta) throws RestRequestException {
		String uri = URI + "conta/" + numConta + "/saldo";
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
	
		String response = HttpUtils.httpGetRequest(uri, headerParams);
		JsonNode json;
		String saldo = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			saldo = json.get("saldo").asText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		Double saldoParsed = Double.parseDouble(saldo);
		return saldoParsed;
	}
	
	public static double credito(int numConta, double valor) throws RestRequestException {
		String uri = URI + "conta/" + numConta + "/credito";
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode body = objectMapper.createObjectNode();
		((ObjectNode) body).put("valor", valor);
		
		String bodyString = body.toString();
	
		String response = HttpUtils.httpPutRequest(uri, headerParams, bodyString, 200);
		JsonNode json;
		String saldo = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			saldo = json.get("saldo").asText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		Double saldoParsed = Double.parseDouble(saldo);
		return saldoParsed;
	}
	
	public static double debito(int numConta, double valor) throws RestRequestException {
		String uri = URI + "conta/" + numConta + "/debito";
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode body = objectMapper.createObjectNode();
		((ObjectNode) body).put("valor", valor);
		
		String bodyString = body.toString();
	
		String response = HttpUtils.httpPutRequest(uri, headerParams, bodyString, 200);
		JsonNode json;
		String saldo = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			saldo = json.get("saldo").asText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		Double saldoParsed = Double.parseDouble(saldo);
		return saldoParsed;
	}
	
	public static double transferir(int numConta, Long contaDestino, double valor) throws RestRequestException, OperacaoIlegalException {
		String uri = URI + "conta/" + "/transferencia";
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode body = objectMapper.createObjectNode();
		((ObjectNode) body).put("valor", valor);
		((ObjectNode) body).put("destino", contaDestino);
		((ObjectNode) body).put("origem", numConta);
		
		String bodyString = body.toString();
	
		String response = HttpUtils.httpPutRequest(uri, headerParams, bodyString, 200);
		JsonNode json;
		String saldo = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			if (json.has("error")) {
			    throw new OperacaoIlegalException(json.get("error").asText());
			} else {
				saldo = json.get("saldoOrigem").asText();
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		Double saldoParsed = Double.parseDouble(saldo);
		return saldoParsed;
	}
	
	public static double renderJuros(int numConta, double taxaPercentual) throws RestRequestException, OperacaoIlegalException {
		String uri = URI + "conta/" + "rendimento";
		
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode body = objectMapper.createObjectNode();
		((ObjectNode) body).put("id", numConta);
		((ObjectNode) body).put("taxaPercentual", taxaPercentual);
		
		String bodyString = body.toString();
	
		String response = HttpUtils.httpPutRequest(uri, headerParams, bodyString, 200);
		JsonNode json;
		String saldo = "";
		
		try {
			json = objectMapper.readTree(response);
			System.out.println(json);
			if (json.has("error")) {
			    throw new OperacaoIlegalException(json.get("error").asText());
			} else {
				saldo = json.get("saldo").asText();
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		Double saldoParsed = Double.parseDouble(saldo);
		return saldoParsed;
	}
	
	public static void main(String[] args) throws RestRequestException {
		ContaDAO dao = new ContaDAO();
		Scanner sc = new Scanner(System.in);

		String opcoesIniciais = "\n 1 - Entrar na sua Conta \n 2 - Cadastrar uma nova conta \n";

		for (;;) {
			System.out.println("Digite a operação desejada:");
			System.out.println(opcoesIniciais);
			int opcao = sc.nextInt();

			if (opcao == 1) {
				System.out.println("Digite o número da sua conta: \n ");
				int numConta = sc.nextInt();
				if (entrarConta(numConta) > -1) {
					menuPrincipal(numConta, dao);
				} else {
					System.out.println("Valor inválido! Tente novamente!\n");
				}
			} else if (opcao == 2) {

				try {
					System.out.println("Escolha um tipo de conta: \n");
					opcoesConta();

					int tipoConta = sc.nextInt();
					int id;
					double saldoInicial = 0;

					switch (tipoConta) {
						case 1:
							
							System.out.println("Digite um saldo inicial para sua conta: ");
							saldoInicial = sc.nextDouble();
							
							id = cadastrarConta(tipoConta - 1, saldoInicial);
							System.out.println("Id criado: " + id);
							menuPrincipal(id, dao);
							break;
						case 2:
							id = cadastrarConta(tipoConta - 1, saldoInicial);
							System.out.println("Id criado: " + id);
							menuPrincipal(id, dao);
							break;
						case 3:
							
							System.out.println("Digite um saldo inicial para sua conta:");
							saldoInicial = sc.nextDouble();
							
							id = cadastrarConta(tipoConta - 1, saldoInicial);
							System.out.println("Id criado: " + id);
							menuPrincipal(id, dao);
						default:
							System.out.println("Um erro ocorreu: Tipo de conta inválida \n");
							break;
					}

				} catch (Exception e) {
					System.out.println("Um erro ocorreu: " + e.getMessage() + "\n");
				}
			} else
				System.out.println("Operação inválida\n");

		}

	}

	private static void opcoesConta() {
		System.out.println("1 - Conta padrão;");
		System.out.println("2 - Conta Bônus;");
		System.out.println("3 - Conta Poupanca;");
		System.out.println();
	}

	private static void menuPrincipal(int numConta, ContaDAO dao) throws RestRequestException {

		for (;;) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Digite a operação desejada: \n");
			String opcoesConta = " 1 - Obter saldo\n 2 - Crédito\n 3 - Débito\n 4 - Transferir\n 5 - Render juros \n 6 - Sair \n";
			System.out.println(opcoesConta);
			int opcao = sc.nextInt();
			double saldo;
			switch (opcao) {
				case 1:
					saldo = consultarSaldo(numConta);
					System.out.println("Saldo atual: " + saldo);
					break;
				case 2:
					System.out.println("Digite o valor a ser creditado: \n");
					double valor1 = sc.nextDouble();
					double saldoResponse = credito(numConta, valor1);
					System.out.println("Saldo atual: " + saldoResponse + "\n\n");
					break;
				case 3:
					System.out.println("Digite o valor a ser debitado: \n");
					double valor2 = sc.nextDouble();
					saldo = debito(numConta, valor2);
					System.out.println("Saldo atual: " + saldo + "\n\n");
					break;
				case 4:
					try {
						System.out.println("Digite o valor a ser transferido: \n");
						double valor = sc.nextDouble();
						System.out.println("Digite o numéro da conta destino: \n");
						Long contaDestino = sc.nextLong();
						saldo = transferir(numConta, contaDestino, valor);
						System.out.println("Transferência feita com sucesso! \n");
						System.out.println("Saldo atual: " + saldo + "\n\n");
					} catch (OperacaoIlegalException e) {
						System.out.println("Um erro ocorreu: " + e.getMessage() + "\n");
					}
					break;
				case 5:
					try {
						System.out.println("Digite o valor da taxa percentual: \n");
						double taxaPercentual = sc.nextDouble();
						saldo = renderJuros(numConta, taxaPercentual);
						System.out.println("Operação realizada com sucesso! \n");
						String formattedNumber = String.format("%.2f", saldo);
						System.out.println("Saldo atual: " + formattedNumber + "\n\n");
					} catch (OperacaoIlegalException e) {
						System.out.println("Um erro ocorreu: " + e.getMessage() + "\n");
					}
					break;
				case 6:
					System.out.println("Saindo da conta..." + "\n\n");
					return;
				default:
					System.out.println("Operação inválida" + "\n\n");
					break;

			}

		}

	}
	
}
