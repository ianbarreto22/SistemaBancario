import java.util.Scanner;

import br.ufrn.sistemabancario.dao.ContaDAO;
import br.ufrn.sistemabancario.model.exceptions.OperacaoIlegalException;


public class Main {

	public static void main(String[] args) {
		ContaDAO dao = new ContaDAO();
		Scanner sc = new Scanner(System.in);
		
		String opcoesIniciais = "\n 1 - Entrar na sua Conta \n 2 - Cadastrar uma nova conta \n";
		
		for(;;) {
			System.out.println("Digite a operação desejada:");
			System.out.println(opcoesIniciais);
			int opcao = sc.nextInt();
			
			if (opcao == 1) {
				System.out.println("Digite o número da sua conta: \n ");
				int numConta = sc.nextInt();
				if(dao.entrarConta(numConta)) {
					menuPrincipal(numConta, dao);
				}
				else {
					System.out.println("Valor inválido! Tente novamente!\n");						
				}
			}
			else if(opcao == 2) {
						
					System.out.println("Digite um número para a sua conta entre 1 e 8 caracteres: \n");
					int novoNumConta = sc.nextInt();
					try {
						dao.cadastrarConta(novoNumConta);
						menuPrincipal(novoNumConta, dao);
					} catch(OperacaoIlegalException e) {
						System.out.println("Um erro ocorreu: " + e.getMessage() + "\n");
					}
			}
			else System.out.println("Operação inválida\n");
			
		}
		

	}

	private static void menuPrincipal(int numConta, ContaDAO dao) {
		
		for(;;) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Digite a operação desejada: \n");
			String opcoesConta = " 1 - Obter saldo\n 2 - Crédito\n 3 - Débito\n 4 - Transferir\n 5 - Sair \n";
			System.out.println(opcoesConta);
			int opcao = sc.nextInt();
			switch(opcao) {
				case 1:
					double saldo = dao.consultarSaldo(numConta);
					System.out.println("Saldo atual: " + saldo);
					break;
				case 2:
					try {
						System.out.println("Digite o valor a ser creditado: \n");
						double valor = sc.nextDouble();
						dao.credito(numConta, valor);
						System.out.println("Saldo atual: " + dao.consultarSaldo(numConta) + "\n\n");
					} catch (OperacaoIlegalException e) {
						System.out.println("Um erro ocorreu: " + e.getMessage() + "\n");
					}
					break;
				case 3:
					try {
						System.out.println("Digite o valor a ser debitado: \n");
						double valor = sc.nextDouble();
						dao.debito(numConta, valor);
						System.out.println("Saldo atual: " + dao.consultarSaldo(numConta) + "\n\n");
					} catch (OperacaoIlegalException e) {
						System.out.println("Um erro ocorreu: " + e.getMessage() + "\n");
					}
					break;
				case 4:
					try {
						System.out.println("Digite o valor a ser transferido: \n");
						double valor = sc.nextDouble();
						System.out.println("Digite o numéro da conta destino: \n");
						Long contaDestino = sc.nextLong();
						dao.transferir(numConta, contaDestino, valor);
						System.out.println("Transferência feita com sucesso! \n");
						System.out.println("Saldo atual: " + dao.consultarSaldo(numConta) + "\n\n");
					} catch (OperacaoIlegalException e) {
						System.out.println("Um erro ocorreu: " + e.getMessage() + "\n");
					}
					break;
				case 5:
					System.out.println("Saindo da conta..." + "\n\n");
					return;
				default:
					System.out.println("Operação inválida" + "\n\n");
					break;
					
			}
			
		}
		
	}

}