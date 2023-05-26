package br.ufrn.sistemabancario.dao;

import java.util.HashMap;

import br.ufrn.sistemabancario.model.Conta;
import br.ufrn.sistemabancario.model.ContaBonus;
import br.ufrn.sistemabancario.model.ContaPoupanca;
import br.ufrn.sistemabancario.model.TipoConta;
import br.ufrn.sistemabancario.model.exceptions.OperacaoIlegalException;

public class ContaDAO {

	private HashMap<Long, Conta> contas;
	
	private static int BONUS_TRANSFERENCIA = 150;
	private static int BONUS_CREDITO = 100;
	

	public ContaDAO() {
		super();

		this.contas = new HashMap<Long, Conta>();
	}

	public boolean entrarConta(long numeroDaConta) {
		return this.contas.containsKey(numeroDaConta);
	}

	public void cadastrarConta(long numeroDaConta, TipoConta tipo) throws OperacaoIlegalException {
		if (this.contas.containsKey(numeroDaConta)) {
			throw new OperacaoIlegalException("Número já utilizado");
		}

		switch (tipo) {
			case CONTA:
				Conta novaConta = new Conta(numeroDaConta);
				contas.put(numeroDaConta, novaConta);
				break;
			case CONTABONUS:
				ContaBonus novaContaBonus = new ContaBonus(numeroDaConta);
				contas.put(numeroDaConta, novaContaBonus);
				break;
			default:
				break;
		}
	}
	
	public void cadastrarContaPoupanca(long numeroDaConta, double saldoInicial) throws OperacaoIlegalException {
		if (this.contas.containsKey(numeroDaConta)) {
			throw new OperacaoIlegalException("Número já utilizado");
		}
		
		ContaPoupanca novaConta = new ContaPoupanca(numeroDaConta, saldoInicial);
		contas.put(numeroDaConta, novaConta);
	}
	
	public void cadastrarContaSimples(long numeroDaConta, double saldo) {
		Conta novaConta = new Conta(numeroDaConta, saldo);
		contas.put(numeroDaConta, novaConta);
	}

	public double consultarSaldo(long numeroDaConta) {
		Conta c = contas.get(numeroDaConta);

		return c.getSaldo();
	}

	public void credito(long numeroDaConta, double valor) throws OperacaoIlegalException {
		if (valor < 0)
			throw new OperacaoIlegalException("Valor não pode ser negativo");
		Conta c = contas.get(numeroDaConta);

		double novoSaldo = c.getSaldo() + valor;
		c.setSaldo(novoSaldo);

		if (c instanceof ContaBonus) {
			int pontos = (int) valor / BONUS_CREDITO;
			int novaPontuacao = ((ContaBonus) c).getPontuacao() + pontos;
			((ContaBonus) c).setPontuacao(novaPontuacao);
			System.out.println("PONTUAÇÃO: " + novaPontuacao);
		}
	}

	public void debito(long numeroDaConta, double valor) throws OperacaoIlegalException {
		Conta c = contas.get(numeroDaConta);

		if (c.getSaldo() < valor) {
			throw new OperacaoIlegalException("Saldo da conta insuficiente!");
		}

		if (valor < 0)
			throw new OperacaoIlegalException("Valor não pode ser negativo");

		double novoSaldo = c.getSaldo() - valor;
		c.setSaldo(novoSaldo);

	}

	public void renderJuros(long numeroDaConta, double taxaPercentual) throws OperacaoIlegalException {
		Conta conta = contas.get(numeroDaConta);
		if (conta instanceof ContaPoupanca) {
			if (taxaPercentual < 0) {
				throw new OperacaoIlegalException("A taxa percentual não pode ser negativa");
			}
			double saldoComJuros = conta.getSaldo() * (1 + (taxaPercentual / 100));
			conta.setSaldo(saldoComJuros);
		} else {
			throw new OperacaoIlegalException("Apenas contas do tipo Poupança podem realizar essa operação");
		}
	}

	public void transferir(long numContaOrigem, long numContaDestino, double valor) throws OperacaoIlegalException {

		if (!this.contas.containsKey(numContaDestino)) {
			throw new OperacaoIlegalException("Conta de destino não existe");
		}

		this.debito(numContaOrigem, valor);

		Conta destino = contas.get(numContaDestino);
		double saldoDestino = destino.getSaldo() + valor;
		destino.setSaldo(saldoDestino);

		if (destino instanceof ContaBonus) {
			int pontos = (int) valor / BONUS_TRANSFERENCIA;
			int novaPontuacao = ((ContaBonus) destino).getPontuacao() + pontos;
			((ContaBonus) destino).setPontuacao(novaPontuacao);
			System.out.println("PONTUAÇÃO DESTINO: " + novaPontuacao);
		}

	}

}
