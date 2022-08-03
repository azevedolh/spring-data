package br.com.alura.spring.data.service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.alura.spring.data.model.Cargo;
import br.com.alura.spring.data.repository.CargoRepository;

@Service
public class CargoService {
	private final CargoRepository cargoRepository;
	private Boolean system;

	public CargoService(CargoRepository cargoRepository) {
		this.cargoRepository = cargoRepository;
	}

	public void inicial(Scanner scanner) {
		system = true;

		while (system) {
			System.out.println("Selecione a operação para a entidade CARGO:");
			System.out.println("0 - Voltar");
			System.out.println("1 - Inserir");
			System.out.println("2 - Atualizar");
			System.out.println("3 - Listar Todos");
			System.out.println("4 - Deletar");

			String opcaoString = scanner.nextLine();
			try {
				Integer opcao = Integer.valueOf(opcaoString);

				switch (opcao) {
				case 0:
					system = false;
					break;
				case 1:
					this.salvar(scanner);
					break;
				case 2:
					this.atualizar(scanner);
					break;
				case 3:
					this.listarTodos(scanner);
					break;
				case 4:
					this.deletar(scanner);
					break;
				default:
					System.out.println("Erro: Opcao inválida.");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Erro: Opcao inválida.");
			}
		}
	}
	
	public List<Cargo> recuperarCargos() {
		List<Cargo> cargoLista = this.cargoRepository.findAll();
		return cargoLista;
	}

	private void deletar(Scanner scanner) {
		System.out.println("Digite o id do Cargo que deseja deletar: ");
		String idString = scanner.nextLine();
		try {
			Integer id = Integer.valueOf(idString);
			this.cargoRepository.deleteById(id);

		} catch (NumberFormatException e) {
			System.out.println("Erro: Id inválido.");	
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Erro: Cargo não encontrado");
		}
	}

	private void listarTodos(Scanner scanner) {
		System.out.println("Cargos: ");

		List<Cargo> cargoLista = recuperarCargos();

		if (cargoLista.isEmpty()) {
			System.out.println("Nenhum Registro Encontrado!");
		} else {
			cargoLista.forEach(c -> System.out.println(c));
		}

		System.out.println(" ");
		System.out.println("Pressione ENTER para voltar ao menu");

		scanner.nextLine();
	}

	private void atualizar(Scanner scanner) {
		System.out.println("Digite o id do Cargo que deseja atualizar: ");
		String idString = scanner.nextLine();
		try {
			Integer id = Integer.valueOf(idString);
			Optional<Cargo> cargoOptional = this.cargoRepository.findById(id);

			if (!cargoOptional.isPresent()) {
				System.out.println("Erro: Cargo não encontrado");
				return;
			}

			Cargo cargo = cargoOptional.get();

			System.out.println("Digite a nova descrição para o cargo " + cargo.getDescricao() + ":");

			String novaDescricao = scanner.nextLine();

			cargo.setDescricao(novaDescricao);

			this.cargoRepository.save(cargo);
		} catch (NumberFormatException e) {
			System.out.println("Erro: Id inválido.");
		}
	}

	private void salvar(Scanner scanner) {
		System.out.println("Informar cargo a ser inserido:");

		String cargoDescricao;

		cargoDescricao = scanner.nextLine();

		if (cargoDescricao.isEmpty()) {
			System.out.println("Erro: Operação não executada, cargo não informado.");
			return;
		}

		Cargo cargo = new Cargo(cargoDescricao);
		this.cargoRepository.save(cargo);
	}

}
