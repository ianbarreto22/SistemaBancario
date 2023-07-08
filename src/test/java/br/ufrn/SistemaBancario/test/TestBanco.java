package br.ufrn.SistemaBancario.test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import br.ufrn.SistemaBancario.dao.ContaDAO;
import br.ufrn.SistemaBancario.model.*;
import br.ufrn.SistemaBancario.model.exceptions.OperacaoIlegalException;

public class TestBanco {

	private ContaDAO dao = new ContaDAO();

	// Cadastrar Conta e Consultar Conta

	@Test
	public void testCadastrarContaPadraoId() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaSimples((long) 1, 50.0);
		long id = dao.consultarConta((long) 1).getNumero();
		assertEquals((long) 1, id, 0);
	}

	@Test
	public void testCadastrarContaPadraoException() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaSimples((long) 1, 50.0);

		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.consultarConta((long) 5);
		});
		
		String mensagemEsperada = "Conta não existe!";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}

	@Test
	public void testCadastrarContaBonusId() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarConta(3, TipoConta.CONTABONUS);
		long id = dao.consultarConta((long) 3).getNumero();
		assertEquals((long) 3, id, 0);
	}

	@Test
	public void testCadastrarContaBonusPontuacao() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarConta(4, TipoConta.CONTABONUS);
		int pontuacao = ((ContaBonus) dao.consultarConta((long) 4)).getPontuacao();
		assertEquals(10, pontuacao, 0);
	}
	
	@Test
	public void testCadastrarContaBonusException() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarConta(3, TipoConta.CONTABONUS);

		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.consultarConta((long) 2);
		});
		
		String mensagemEsperada = "Conta não existe!";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}

	@Test
	public void testCadastrarContaPoupancaId() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 50.0);
		long id = dao.consultarConta((long) 5).getNumero();
		assertEquals((long) 5, id, 0);
	}
	
	@Test
	public void testCadastrarContaPoupancaException() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 12, 50.0);

		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.consultarConta((long) 4);
		});
		
		String mensagemEsperada = "Conta não existe!";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}

	// Consultar saldo

	@Test
	public void testCadastrarContaPadraoSaldo() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaSimples((long) 1, 50.0);
		double saldo = dao.consultarConta((long) 1).getSaldo();
		assertEquals(50.0, saldo, 0);
	}

	@Test
	public void testCadastrarContaBonusSaldo() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarConta(4, TipoConta.CONTABONUS);
		double saldo = ((ContaBonus) dao.consultarConta((long) 4)).getSaldo();
		assertEquals(0, saldo, 0);
	}

	@Test
	public void testCadastrarContaPoupancaSaldo() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 50.0);
		double saldo = dao.consultarConta((long) 5).getSaldo();
		assertEquals(50.0, saldo, 0);
	}
	
	// Crédito
	
	@Test
	public void testCreditoPadrao() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaSimples((long) 5, 50.0);
		dao.credito(5, 40);
		double saldo = dao.consultarConta((long) 5).getSaldo();
		assertEquals(90.0, saldo, 0);
	}
	
	@Test
	public void testCreditoValorNegativo() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 50.0);
		
		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.credito(5, -40);
		});
		
		String mensagemEsperada = "Valor não pode ser negativo";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}
	
	@Test
	public void testCreditoBonificacao() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarConta(4, TipoConta.CONTABONUS);
		dao.credito(4, 100);
		int pontuacao = ((ContaBonus) dao.consultarConta((long) 4)).getPontuacao();
		assertEquals(11, pontuacao, 0);
	}
	
	// Débito
	
	@Test
	public void testDebitoPadraoSaldoNegativo() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaSimples((long) 5, 100.0);
		dao.debito(5, 150);
		
		double saldo = dao.consultarConta((long) 5).getSaldo();
		assertEquals(-50.0, saldo, 0);
	}
	
	@Test
	public void testDebitoPadrao() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarConta(4, TipoConta.CONTABONUS);
		dao.credito(4, 100);
		dao.debito(4, 60);
		
		double saldo = dao.consultarConta((long) 4).getSaldo();
		assertEquals(40.0, saldo, 0);
	}
	
	@Test
	public void testDebitoValorNegativo() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 3, 50.0);
		
		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.debito(3, -90);
		});
		
		String mensagemEsperada = "Valor não pode ser negativo";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}
	
	@Test
	public void testDebitoAcimaLimiteNegativo() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaSimples((long) 3, 50.0);
		
		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.debito(3, 1051);
		});
		
		String mensagemEsperada = "Saldo da conta insuficiente! O limite para saldo negativo é R$ -1.000,00";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}
	
	@Test
	public void testDebitoSaldoNegativoExceção() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 50.0);
		
		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.debito(5, 90);
		});
		
		String mensagemEsperada = "Saldo da conta insuficiente!";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}
	
	// Transferência
	
	@Test
	public void testTransferirValorNegativo() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaSimples((long) 3, 50.0);
		dao.cadastrarContaPoupanca((long) 5, 50.0);
		
		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.transferir(3, 5, -40);
		});
		
		String mensagemEsperada = "Valor não pode ser negativo";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}
	
	@Test
	public void testTransferirSaldoNegativoExcecao() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 50.0);
		dao.cadastrarContaSimples((long) 3, 50.0);
		
		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.transferir(5, 3, 60);
		});
		
		String mensagemEsperada = "Saldo da conta insuficiente!";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}
	
	@Test
	public void testTransferirBonificacao() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 600.0);
		dao.cadastrarConta(4, TipoConta.CONTABONUS);
		dao.credito(4, 200);
		
		dao.transferir(5, 4, 300);
		
		int pontuacao = ((ContaBonus) dao.consultarConta((long) 4)).getPontuacao();
		assertEquals(14, pontuacao, 0);
	}
	
	@Test
	public void testSaldoDestinoAposTransferir() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 600.0);
		dao.cadastrarConta(4, TipoConta.CONTABONUS);
		dao.credito(4, 200);
		
		dao.transferir(5, 4, 300);
		
		double saldo = dao.consultarConta((long) 4).getSaldo();
		assertEquals(500.0, saldo, 0);
	}
	
	@Test
	public void testSaldoOrigemAposTransferir() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 600.0);
		dao.cadastrarConta(4, TipoConta.CONTABONUS);
		dao.credito(4, 200);
		
		dao.transferir(5, 4, 300);
		
		double saldo = dao.consultarConta((long) 5).getSaldo();
		assertEquals(300.0, saldo, 0);
	}
	
	// Render Juros
	
	@Test
	public void testRenderJuros1() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 600.0);
		dao.renderJuros(5, 5);
		
		
		double saldo = dao.consultarConta((long) 5).getSaldo();
		assertEquals(630.0, saldo, 0);
	}
	
	@Test
	public void testRenderJuros2() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 600.0);
		dao.renderJuros(5, 57);
		
		
		double saldo = dao.consultarConta((long) 5).getSaldo();
		assertEquals(942.0, saldo, 0.1);
	}
	
	@Test
	public void testRenderJurosTaxaValorNegativo() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaPoupanca((long) 5, 600.0);
		
		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.renderJuros(5, -5);
		});
		
		String mensagemEsperada = "A taxa percentual não pode ser negativa";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}
	
	@Test
	public void testRenderJurosContaNaoPoupanca() throws OperacaoIlegalException {
		dao = new ContaDAO();
		dao.cadastrarContaSimples((long) 5, 600.0);
		
		Exception exception = assertThrows(OperacaoIlegalException.class, () -> {
			dao.renderJuros(5, 10);
		});
		
		String mensagemEsperada = "Apenas contas do tipo Poupança podem realizar essa operação";
	    String mensagemGerada = exception.getMessage();
	    
	    assertTrue(mensagemGerada.contains(mensagemEsperada));
	}
}
