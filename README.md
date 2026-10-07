# AlunosAPI

API REST desenvolvida em Java com Spring Boot para gerenciamento de alunos.

O projeto foi desenvolvido como atividade acadêmica e evoluiu de uma versão com armazenamento em memória para uma versão com persistência de dados utilizando PostgreSQL e Spring Data JPA.

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Lombok
- Postman

## Estrutura do projeto

```text
src/
└── main/
    ├── java/
    │   └── br/unipar/backend/minhaapi/
    │       ├── controller/
    │       │   └── AlunoController.java
    │       ├── model/
    │       │   └── Aluno.java
    │       ├── repository/
    │       │   └── AlunoRepository.java
    │       └── MinhaApiApplication.java
    │
    └── resources/
        └── application.properties
```

## Modelo Aluno

Cada aluno possui os seguintes atributos:

| Campo | Tipo | Descrição |
|---|---|---|
| id | Long | Identificador único gerado pelo banco |
| nome | String | Nome do aluno |
| idade | int | Idade do aluno |
| curso | String | Curso do aluno |
| cidade | String | Cidade do aluno |

O campo `id` é gerado automaticamente pelo PostgreSQL através do JPA.

## Configuração do banco de dados

O projeto utiliza PostgreSQL.

Crie um banco de dados chamado:

```text
aluno_db
```

Depois configure o arquivo:

```text
src/main/resources/application.properties
```

Exemplo:

```properties
spring.application.name=MinhaAPI

spring.datasource.url=jdbc:postgresql://localhost:5432/aluno_db
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Substitua `SUA_SENHA` pela senha do usuário PostgreSQL.

## Executando o projeto

No terminal, dentro da pasta do projeto:

```bash
./mvnw spring-boot:run
```

No Windows também pode ser utilizado:

```powershell
.\mvnw.cmd spring-boot:run
```

A API será executada em:

```text
http://localhost:8080
```

---

# Endpoints

## 1. Listar todos os alunos

### Requisição

```http
GET /alunos
```

Exemplo:

```text
GET http://localhost:8080/alunos
```

### Resposta

```json
[
    {
        "id": 1,
        "nome": "João Silva",
        "idade": 20,
        "curso": "ADS",
        "cidade": "Cascavel"
    },
    {
        "id": 2,
        "nome": "Maria Souza",
        "idade": 22,
        "curso": "Engenharia de Software",
        "cidade": "Toledo"
    }
]
```

---

## 2. Buscar aluno por ID

### Requisição

```http
GET /alunos/{id}
```

Exemplo:

```text
GET http://localhost:8080/alunos/1
```

### Resposta

```json
{
    "id": 1,
    "nome": "João Silva",
    "idade": 20,
    "curso": "ADS",
    "cidade": "Cascavel"
}
```

Caso o aluno não exista:

```text
404 Not Found
```

---

## 3. Cadastrar aluno

### Requisição

```http
POST /alunos
```

### Body

```json
{
    "nome": "João Silva",
    "idade": 20,
    "curso": "ADS",
    "cidade": "Cascavel"
}
```

> O campo `id` não deve ser enviado. O identificador é gerado automaticamente pelo banco de dados.

### Resposta

```json
{
    "id": 1,
    "nome": "João Silva",
    "idade": 20,
    "curso": "ADS",
    "cidade": "Cascavel"
}
```

Status:

```text
201 Created
```

---

## 4. Atualizar aluno

### Requisição

```http
PUT /alunos/{id}
```

Exemplo:

```text
PUT http://localhost:8080/alunos/1
```

### Body

```json
{
    "nome": "João da Silva",
    "idade": 21,
    "curso": "Engenharia de Software",
    "cidade": "Toledo"
}
```

### Resposta

```json
{
    "id": 1,
    "nome": "João da Silva",
    "idade": 21,
    "curso": "Engenharia de Software",
    "cidade": "Toledo"
}
```

Caso o aluno não exista:

```text
404 Not Found
```

---

## 5. Excluir aluno

### Requisição

```http
DELETE /alunos/{id}
```

Exemplo:

```text
DELETE http://localhost:8080/alunos/1
```

### Resposta

```text
Aluno 1 excluido com sucesso!
```

Status:

```text
200 OK
```

Caso o aluno não exista:

```text
404 Not Found
```

---

# Filtros

A API possui três filtros:

- `nome`
- `curso`
- `cidade`

Os filtros podem ser utilizados individualmente ou combinados.

## Filtrar por nome

```text
GET http://localhost:8080/alunos?nome=Joao
```

## Filtrar por curso

```text
GET http://localhost:8080/alunos?curso=ADS
```

## Filtrar por cidade

```text
GET http://localhost:8080/alunos?cidade=Cascavel
```

## Combinar filtros

Os filtros podem ser utilizados juntos.

Exemplo:

```text
GET http://localhost:8080/alunos?curso=ADS&cidade=Cascavel
```

Outro exemplo:

```text
GET http://localhost:8080/alunos?nome=Joao&curso=ADS&cidade=Cascavel
```

Os filtros não diferenciam letras maiúsculas de minúsculas e aceitam buscas sem acentuação.

Exemplo:

```text
GET http://localhost:8080/alunos?nome=Joao
```

pode encontrar:

```text
João
```

---

# Persistência

Os dados da API são armazenados no PostgreSQL através do Spring Data JPA.

A entidade `Aluno` é mapeada para uma tabela no banco de dados:

```text
aluno
```

Exemplo de consulta no PostgreSQL:

```sql
SELECT * FROM aluno;
```

Os dados permanecem armazenados mesmo após o encerramento e reinício da aplicação.

---

# Repository

A persistência é realizada através da interface:

```java
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
```

O `JpaRepository` fornece operações como:

```text
save()
findAll()
findById()
existsById()
deleteById()
```

---

# Testes

As requisições da API podem ser testadas utilizando o Postman.

Os principais testes incluem:

- Cadastro de aluno
- Listagem de alunos
- Busca por ID
- Atualização
- Exclusão
- Busca por nome
- Busca por curso
- Busca por cidade
- Combinação de filtros
- Tratamento de ID inexistente
- Persistência dos dados após reiniciar a aplicação

## Autor

Matheus Heberle

Projeto acadêmico desenvolvido para a disciplina de desenvolvimento backend.
