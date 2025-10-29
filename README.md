# API de Cadastro de Pets 🐾

Uma API RESTful desenvolvida em Spring Boot para gerenciamento de cadastro de pets, criada como exemplo para ensino de testes unitários e de integração.

## 📋 Funcionalidades

- ✅ Cadastro de novos pets
- ✅ Listagem de todos os pets cadastrados
- ✅ Busca de pet por ID
- ✅ Exclusão de pets
- ✅ Validações básicas de dados

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Web MVC**
- **Spring JDBC**
- **H2 Database** (banco em memória)
- **JUnit 5** para testes
- **Mockito** para testes unitários
- **Gradle** para gerenciamento de dependências

## 📦 Estrutura do Projeto

```
src/
├── main/
│ ├── java/
│ │ └── app/
│ │     └── qualidade/
│ │         └── apicadastro/
│ │             ├── model/
│ │             │   └── Pet.java
│ │             ├── repository/
│ │             │   └── PetRepository.java
│ │             ├── controller/
│ │             │   └── PetController.java
│ │             └── Application.java
│ ├── resources/
│ │     └── application.properties
├── test/
│ ├── java/
│ │ └── app/
│ │     └── qualidade/
│ │         └── apicadastro/
│ │             ├── bdd/
│ │             │    └── runner/
│ │             │       └── CucumberTest.java
│ │             ├── model/
│ │             │   └── PetTest.java
│ │             ├── repository/
│ │             │   └── PetRepositoryTest.java
│ │             ├── controller/
│ │             │   └── PetControllerTest.java
│ │             └── integration/
│ │                 └── PetIntegrationTest.java
│ ├── resources/
│ │ ├── features/
│ │ │   │── cadastro_pet.features
│ │ │   │── consulta_pet.features
│ │ │   └── exclusao_pet.features
│ │ │── schema.sql
│ │ └── application-test.properties
```

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Gradle 7.0+

### Executando a Aplicação

```bash
# Clone o repositório
git clone https://github.com/wellmsan/anima-api-cadastro.git

# Navegue até o diretório
cd anima-api-cadastro

# Execute a aplicação
./gradlew bootRun
```

A aplicação estará disponível em: http://localhost:8080

### Executando os Testes

```
# Executar todos os testes (incluindo BDD)
./gradlew test

# Executar apenas testes BDD Cucumber
./gradlew cucumber

# Executar com relatório detalhado
./gradlew cucumber --info

# Limpar e executar
./gradlew clean test

# Executar com cobertura de código
./gradlew test jacocoTestReport
```

## 📡 Endpoints da API

### POST /pets

Cria um novo pet.

Request:

```
{
    "nome": "Rex",
    "raca": "Labrador",
    "idade": 3
}
```

Response:

```
{
    "id": 1,
    "nome": "Rex",
    "raca": "Labrador",
    "idade": 3
}
```

### GET /pets

Retorna todos os pets cadastrados.

Response:

```
[
    {
        "id": 1,
        "nome": "Rex",
        "raca": "Labrador",
        "idade": 3
    }
]
```

### GET /pets/{id}

Retorna um pet específico pelo ID.

Response:

```
{
    "id": 1,
    "nome": "Rex",
    "raca": "Labrador",
    "idade": 3
}
```

### DELETE /pets/{id}

Remove um pet pelo ID.

Response: 200 OK (se encontrado) ou 404 Not Found

## 🧪 Estratégia de Testes

### Testes Unitários

- PetTest: Testes do modelo de dados
- PetRepositoryTest: Testes do repositório com Mock do JdbcTemplate
- PetControllerTest: Testes do controller com Mock do Repository

### Testes de Integração

PetIntegrationTest: Testes completos com banco H2 em memória

### Cobertura de Testes

O projeto busca alta cobertura de testes com:

- ✅ Testes de caminhos felizes (happy paths)
- ✅ Testes de cenários de erro
- ✅ Testes de validação de dados
- ✅ Testes de integração entre componentes

## ⚙️ Configuração

### Banco de Dados

O projeto utiliza H2 Database em memória para desenvolvimento e testes. Para acessar o console do H2 durante o desenvolvimento:

Execute a aplicação

- Acesse: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:api-cadastro
- Usuário: sa
- Senha: (vazio)

### Configurações de Teste

O arquivo application-test.properties configura o ambiente de testes com Banco H2 em memória (api-cadastro-testes)

## 📊 Exemplos de Uso Educacional

Este projeto é ideal para ensinar:

- Testes Unitários: Como mockar dependências e testar componentes isolados
- Testes de Integração: Como testar a interação entre componentes
- TDD: Desenvolvimento guiado por testes
- Boas Práticas: Organização de testes e padrões de nomenclatura

## 👨‍💻 Autor

Desenvolvido por [Welber Macedo](https://github.com/wellmsan) - [wellmsan](https://github.com/wellmsan).

<hr style="height:1px; border:none; color:#ccc; background-color:#ccc;">

**Nota**: Este projeto foi desenvolvido para fins educacionais como exemplo de implementação de testes em aplicações Spring Boot.
