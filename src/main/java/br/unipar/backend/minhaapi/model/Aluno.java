package br.unipar.backend.minhaapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Aluno {

    private int id;
    private String nome;
    private int idade;
    private String curso;
    private String cidade;
}
