package br.edu.utfpr.servico;

import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.dto.PaisDTO;
import br.edu.utfpr.excecao.NomeClienteMenor5CaracteresException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class ServicoCliente {
    private List<ClienteDTO> clientes;
    private List<PaisDTO> paises;

    public ServicoCliente() {

        paises = Stream.of(
                PaisDTO.builder().id(1).nome("Brasil").sigla("BR").codigoTelefone(55).build(),
                PaisDTO.builder().id(2).nome("Reino Unido").sigla("RU").codigoTelefone(44).build()
        ).collect(Collectors.toList());


        clientes = Stream.of(
                ClienteDTO.builder().id(1).nome("Pedro").idade(20).telefone("00000000").pais(paises.get(0)).limiteCredito(1000).build(),
                ClienteDTO.builder().id(2).nome("Felipe").idade(20).telefone("444444444").pais(paises.get(1)).limiteCredito(2000).build()
        ).collect(Collectors.toList());
    }

    @GetMapping("/servico/cliente")
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(clientes);
    }

    @GetMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> listarPorId(@PathVariable int id) {
        Optional<ClienteDTO> clienteEncontrado = clientes.stream().filter(p -> p.getId() == id).findAny();
        return ResponseEntity.of(clienteEncontrado);
    }

    @PostMapping("/servico/cliente")
    public ResponseEntity<ClienteDTO> criar (@RequestBody ClienteDTO cliente) {
        cliente.setId(paises.size() + 1);
        clientes.add(cliente);
        return ResponseEntity.status(201).body(cliente);
    }

    @DeleteMapping ("/servico/cliente/{id}")
    public ResponseEntity excluir (@PathVariable int id) {
        if (clientes.removeIf(cliente -> cliente.getId() == id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> alterar (@PathVariable int id, @RequestBody ClienteDTO cliente) {
        Optional<ClienteDTO> clienteExistente = clientes.stream().filter(p -> p.getId() == id).findAny();
        clienteExistente.ifPresent(c -> {
            try {
                c.setNome(cliente.getNome());
            } catch (NomeClienteMenor5CaracteresException e) {
                e.printStackTrace();
            }
            c.setIdade(cliente.getIdade());
            c.setLimiteCredito(cliente.getLimiteCredito());
            c.setTelefone(cliente.getTelefone());
            c.setPais(cliente.getPais());
        });

          return ResponseEntity.of(clienteExistente);
    }

}
