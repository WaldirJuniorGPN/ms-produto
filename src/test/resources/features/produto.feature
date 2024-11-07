# language: pt

  Funcionalidade: Produto

    Cenario: Cadastrar um novo produto com sucesso
      Dado que tenha os dados válidos do produto
      Quando eu envio uma requisição POST para "/produtos"
      Entao o produto deve ser criado com sucesso
      E a resposta deve conter os dados do produto criado


    Cenario: Atualizar um produto existente com sucesso
      Dado que tenha os dados a serem atualizados de um produto existente
      Quando envio uma requisição PUT para "/produtos/{id}"
      Entao o produto deve ser atualizado com sucesso
      E a resposta deve conter os dados atualizados do produto

    Cenario: Buscar um produto existente com sucesso
      Dado que tenha um ID de um produto existente
      Quando envio uma requisição GET para "/produtos/{id}"
      Entao o produto deve ser atualizado com sucesso
      E o status de resposta deve ser 200


    Cenario: Listar todos os produtos com sucesso
      Dado que existam produtos cadastrados no sistema
      Quando envio uma requisição GET para sem parametros "/produtos"
      Entao uma lista de produtos deve ser retornada


    Cenario: Deletar um produto existente com sucesso
      Dado que tenha o ID de um produto existente
      Quando envio uma requisição DELETE para "/produtos/{id}"
      Entao o produto dever ser excluido com sucesso


    Cenario: Atualizar a quantidade de um produto em estoque
      Dado que tenho o ID de um produto e a quantidade para baixar do estoque
      Quando envio de uma requisição PUT para "/produtos/atualizar/estoque/{id}/{quantidade}"
      Entao quantidade do produto com id informado dever ser subtraido pela quantidade informada na requisiçao