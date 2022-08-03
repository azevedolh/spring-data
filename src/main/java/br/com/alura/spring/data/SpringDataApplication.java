package br.com.alura.spring.data;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.spring.data.service.CargoService;
import br.com.alura.spring.data.service.FuncionarioService;
import br.com.alura.spring.data.service.RelatorioService;
import br.com.alura.spring.data.service.UnidadeService;

@SpringBootApplication
public class SpringDataApplication implements CommandLineRunner {

	private Boolean system = true;
	private final CargoService cargoService;
	private final FuncionarioService funcionarioService;
	private final UnidadeService unidadeService;
	private final RelatorioService relatorioService;

	public SpringDataApplication(CargoService cargoService, FuncionarioService funcionarioService,
			UnidadeService unidadeService, RelatorioService relatorioService) {
		this.cargoService = cargoService;
		this.funcionarioService = funcionarioService;
		this.unidadeService = unidadeService;
		this.relatorioService = relatorioService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringDataApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Scanner scanner = new Scanner(System.in);

		while (system) {
			System.out.println("Digite a opção desejada:");
			System.out.println("0 - Sair");
			System.out.println("1 - Cargo");
			System.out.println("2 - Funcionario");
			System.out.println("3 - Unidade");
			System.out.println("4 - Relatórios");

			String opcaoString = scanner.nextLine();

			try {
				Integer opcao = Integer.valueOf(opcaoString);

				if (opcao.equals(1)) {
					cargoService.inicial(scanner);
				} else if (opcao.equals(2)) {
					funcionarioService.inicial(scanner);
				} else if (opcao.equals(3)) {
					unidadeService.inicial(scanner);
				} else if (opcao.equals(4)) {
					relatorioService.inicial(scanner);
				} else if (opcao.equals(0)) {
					system = false;
				} else {
					System.out.println("Erro: Opção inválida, por favor digite uma opção válida.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Erro: Opção inválida, por favor digite uma opção válida.");
			}
		}
	}

}
