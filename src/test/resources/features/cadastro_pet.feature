# language: pt
Funcionalidade: Cadastro de Pets
  Como usuário do sistema
  Eu quero cadastrar pets
  Para manter um controle dos animais

  Regras:
  - Nome é obrigatório
  - Raça é obrigatória
  - Idade é obrigatória

  @cadastro
  Cenário: Cadastrar pet com todos os dados obrigatórios
    Dado que eu crio um pet com nome "Rex"
    E com raça "Labrador"
    E com idade 3
    Quando eu cadastro o pet
    Então deve retornar status 200
    E deve conter um ID
    E o nome deve ser "Rex"
    E a raça deve ser "Labrador"
    E a idade deve ser 3

  @cadastro
  Cenário: Cadastrar pet com idade zero
    Dado que eu crio um pet com nome "Zero"
    E com raça "Vira-lata"
    E com idade 0
    Quando eu cadastro o pet
    Então deve retornar status 200
    E a idade deve ser 0

  @cadastro @erro
  Cenário: Tentar cadastrar pet sem nome
    Dado que eu crio um pet sem nome
    E com raça "Poodle"
    E com idade 2
    Quando eu cadastro o pet
    Então deve retornar status 500

  @cadastro @erro
  Cenário: Tentar cadastrar pet sem raça
    Dado que eu crio um pet com nome "Sem Raça"
    E sem raça
    E com idade 1
    Quando eu cadastro o pet
    Então deve retornar status 500

  @cadastro @erro
  Cenário: Tentar cadastrar pet sem idade
    Dado que eu crio um pet com nome "Sem Idade"
    E com raça "Golden"
    E sem idade
    Quando eu cadastro o pet
    Então deve retornar status 500

  @cadastro @erro
  Cenário: Tentar cadastrar pet com todos os campos vazios
    Dado que eu crio um pet sem nome
    E sem raça
    E sem idade
    Quando eu cadastro o pet
    Então deve retornar status 500