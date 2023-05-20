package br.ufrn.sistemabancario.model;

public class Conta {
	protected long numero;
	protected double saldo;
	
	
	public Conta(long numero) {
		super();
		this.numero = numero;
		this.saldo = 0;
	}
	public long getNumero() {
		return numero;
	}
	public void setNumero(long numero) {
		this.numero = numero;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	
}
