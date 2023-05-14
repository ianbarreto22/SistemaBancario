package br.ufrn.sistemabancario.dao;

import java.util.HashMap;

import br.ufrn.sistemabancario.model.Conta;

public class ContaDAO {

	private HashMap<Long, Conta> contas;

	public ContaDAO() {
		super();
		
		this.contas = new HashMap<Long, Conta>();
	}
	
	public void cadastrarConta(long numeroDaConta) {


        Conta novaConta = new Conta(numeroDaConta);
        contas.put(numeroDaConta, novaConta);

    }
	
	 public double consultarSaldo(long numeroDaConta) {
	        Conta c = contas.get(numeroDaConta);

	        return c.getSaldo();
	    }
	
	
}
