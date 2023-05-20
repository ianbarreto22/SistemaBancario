package br.ufrn.sistemabancario.dao;

import java.util.HashMap;

import br.ufrn.sistemabancario.model.Conta;
import br.ufrn.sistemabancario.model.exceptions.OperacaoIlegalException;

public class ContaDAO {

	private HashMap<Long, Conta> contas;

	public ContaDAO() {
		super();
		
		this.contas = new HashMap<Long, Conta>();
	}
	
	public boolean entrarConta(long numeroDaConta) {
		return this.contas.containsKey(numeroDaConta);
	}
	
	public void cadastrarConta(long numeroDaConta) throws OperacaoIlegalException {
		if(this.contas.containsKey(numeroDaConta)) {
			throw new OperacaoIlegalException("Número já utilizado");
		 }

        Conta novaConta = new Conta(numeroDaConta);
        contas.put(numeroDaConta, novaConta);

    }
	
	 public double consultarSaldo(long numeroDaConta) {
	        Conta c = contas.get(numeroDaConta);

	        return c.getSaldo();
	    }
	 
	 public void credito(long numeroDaConta, double valor) throws OperacaoIlegalException {
		 	if(valor < 0 ) throw new OperacaoIlegalException("Valor inválido");
	        Conta c = contas.get(numeroDaConta);

	        double novoSaldo = c.getSaldo() + valor;
	        c.setSaldo(novoSaldo);
	    }

	 public void debito(long numeroDaConta, double valor) throws OperacaoIlegalException {
		 	Conta c = contas.get(numeroDaConta);
		 	
		 	if(c.getSaldo() < valor) {
		 		throw new OperacaoIlegalException("Saldo da conta insuficiente!");
		 	}
		 	
		 	if(valor < 0 ) throw new OperacaoIlegalException("Valor inválido");

	
	        double novoSaldo = c.getSaldo() - valor;
	        c.setSaldo(novoSaldo);
	    }
	    
	 public void transferir(long numContaOrigem, long numContaDestino, double valor) throws OperacaoIlegalException {
		 if(!this.contas.containsKey(numContaDestino)) {
			throw new OperacaoIlegalException("Conta de destino não existe");
		 }
		 
		 this.debito(numContaOrigem, valor);
		 this.credito(numContaDestino, valor);
	 }
	
	
}
