package br.ufrn.sistemabancario.model;

public class ContaBonus extends Conta {
	
	private int pontuacao;

	public ContaBonus(long numero) {
		super(numero);
		this.pontuacao = 10;
	}
	
	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}
	
	public int getPontuacao() {
		return this.pontuacao;
	}

}
