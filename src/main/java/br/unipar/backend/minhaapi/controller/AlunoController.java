package br.unipar.backend.minhaapi.controller;

import br.unipar.backend.minhaapi.model.Aluno;
import br.unipar.backend.minhaapi.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> listarAlunos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String cidade) {

        List<Aluno> resultado = alunoRepository.findAll()
                .stream()
                .filter(aluno -> corresponde(aluno.getNome(), nome))
                .filter(aluno -> corresponde(aluno.getCurso(), curso))
                .filter(aluno -> corresponde(aluno.getCidade(), cidade))
                .toList();

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarAluno(@PathVariable Long id) {

        return alunoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aluno> cadastrarAluno(@RequestBody Aluno aluno) {

        // O ID será gerado automaticamente pelo banco.
        Aluno alunoSalvo = alunoRepository.save(aluno);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alunoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(
            @PathVariable Long id,
            @RequestBody Aluno novosDados) {

        return alunoRepository.findById(id)
                .map(aluno -> {

                    aluno.setNome(novosDados.getNome());
                    aluno.setIdade(novosDados.getIdade());
                    aluno.setCurso(novosDados.getCurso());
                    aluno.setCidade(novosDados.getCidade());

                    Aluno alunoAtualizado = alunoRepository.save(aluno);

                    return ResponseEntity.ok(alunoAtualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirAluno(@PathVariable Long id) {

        if (!alunoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        alunoRepository.deleteById(id);

        return ResponseEntity.ok(
                "Aluno " + id + " excluido com sucesso!"
        );
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

        // Remove acentos e transforma em letras minúsculas.
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }
}