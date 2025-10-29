# language: pt
Funcionalidade: Consulta de Pets
  Como usuário do sistema
  Eu quero consultar pets cadastrados
  Para visualizar e filtrar informações dos animais

  @consulta
  Cenário: Listar todos os pets
    Dado que existem os seguintes pets:
      | nome    | raca       | idade |
      | Rex     | Labrador   | 3     |
      | Zero    | Vira-lata  | 0     |
    Quando eu realizo a requisição de listagem de pets na consulta
    Então na consulta deve retornar status 200
    E na consulta deve conter ao menos 2 pets

  @consulta
  Cenário: Consultar pet por ID existente
    Dado que eu crio um pet com nome "Bidu" e raça "Pug" e idade 2
    Quando eu consulto o pet pelo ID na consulta
    Então na consulta deve retornar status 200
    E na consulta deve conter um ID
    E na consulta o nome deve ser "Bidu"
    E na consulta a raça deve ser "Pug"
    E na consulta a idade deve ser 2

  @consulta @erro
  Cenário: Consultar pet por ID inexistente
    Dado que não existe um pet com ID 999999
    Quando eu consulto o pet pelo ID 999999 na consulta
    Então na consulta deve retornar status 404

  @consulta
  Cenário: Filtrar pets por nome
    Dado que existem os seguintes pets:
      | nome    | raca     | idade |
      | Nemo    | SRD      | 4     |
      | Nino    | SRD      | 1     |
    Quando eu consulto pets com nome "Nemo" na consulta
    Então na consulta deve retornar status 200
    E na consulta deve conter um pet com nome "Nemo"
