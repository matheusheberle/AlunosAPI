package br.unipar.backend.minhaapi.controller;

import br.unipar.backend.minhaapi.model.Aluno;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final List<Aluno> alunos = new ArrayList<>();

    private int proximoId = 1;

    @GetMapping
    public ResponseEntity<List<Aluno>> listarAlunos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String cidade) {
        List<Aluno> resultado = new ArrayList<>();

        for (Aluno aluno : alunos) {
            // O aluno precisa atender a todos os filtros informados.
            if (corresponde(aluno.getNome(), nome)
                    && corresponde(aluno.getCurso(), curso)
                    && corresponde(aluno.getCidade(), cidade)) {
                resultado.add(aluno);
            }
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarAluno(@PathVariable int id) {

        for (Aluno aluno : alunos) {
            if (aluno.getId() == id) {
                return ResponseEntity.ok(aluno);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Aluno> cadastrarAluno(@RequestBody Aluno aluno) {
        // O ID vem do contador, mesmo que o cliente envie um ID no JSON.
        aluno.setId(proximoId);
        proximoId++;
        alunos.add(aluno);

        return ResponseEntity.status(HttpStatus.CREATED).body(aluno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable int id,
                                               @RequestBody Aluno novosDados) {
        for (Aluno aluno : alunos) {
            if (aluno.getId() == id) {
                aluno.setNome(novosDados.getNome());
                aluno.setIdade(novosDados.getIdade());
                aluno.setCurso(novosDados.getCurso());
                aluno.setCidade(novosDados.getCidade());
                return ResponseEntity.ok(aluno);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirAluno(@PathVariable int id) {
        for (int i = 0; i < alunos.size(); i++) {
            // A busca usa o ID; a posicao serve apenas para remover da lista.
            if (alunos.get(i).getId() == id) {
                alunos.remove(i);
                return ResponseEntity.ok("Aluno " + id + " excluido com sucesso!");
            }
        }

        return ResponseEntity.notFound().build();
    }

    private boolean corresponde(String valor, String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return true;
        }
        if (valor == null) {
            return false;
        }
        return normalizar(valor).contains(normalizar(filtro));
    }

    private String normalizar(String texto) {
        // Separa e remove os acentos para que "Joao" encontre "João".
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }
}
