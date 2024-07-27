package servico.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;

import modelo.Cliente;
import modelo.Endereco;
import modelo.RepositorioCliente;
import modelo.RepositorioEndereco;
import servico.ClienteService;
import servico.ViaCep;

public class ClienteServiceImp implements ClienteService {

	@Autowired
	private RepositorioCliente repositorioCliente;

	@Autowired
	private RepositorioEndereco repositorioEndereco;

	@Autowired
	ViaCep viaCep;

	@Override
	public Iterable<Cliente> buscarTodos() {

		return repositorioCliente.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente> cliente = repositorioCliente.findById(id);
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {

		salvarClienteCep(cliente);

	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		Optional<Cliente> clienteDB = repositorioCliente.findById(id);
		if (clienteDB.isPresent()) {
			salvarClienteCep(cliente);

		}

	}

	private void salvarClienteCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = repositorioEndereco.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCep.consultarCep(cep);
			repositorioEndereco.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		repositorioCliente.save(cliente);

	}

	@Override
	public void deletar(Long id) {
		repositorioCliente.deleteById(id);

	}

}
