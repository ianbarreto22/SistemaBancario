package br.ufrn.sistemabancario.model;

public class ContaPoupanca extends Conta {

  public ContaPoupanca(long numero) {
    super(numero);
  }

  // public void renderJuros(double taxaPercentual) {
  // double saldoComJuros = this.getSaldo() * (1 + (taxaPercentual / 100));
  // this.setSaldo(saldoComJuros);
  // }
}
