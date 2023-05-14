package br.ufrn.sistemabancario.model.exceptions;

public class OperacaoIlegalException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public OperacaoIlegalException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
}
