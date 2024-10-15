# Microsserviço de Produto

## Descrição

O Microsserviço de Produto faz parte de um sistema de gerenciamento de pedidos construído utilizando a arquitetura de microsserviços e o ecossistema Spring. Este serviço gerencia o catálogo de produtos, permitindo operações CRUD (criação, leitura, atualização e exclusão) sobre os produtos, bem como o controle de estoque.

Além disso, o serviço oferece uma funcionalidade de carga de produtos, permitindo a importação em massa de informações de produtos a partir de fontes externas, como arquivos CSV. Essa funcionalidade pode ser agendada ou iniciada manualmente, garantindo que o catálogo de produtos esteja sempre atualizado.

## Funcionalidades

- **CRUD de Produtos**: Operações de criação, leitura, atualização e exclusão de registros de produtos no sistema.
- **Gerenciamento de Estoque**: Controle da quantidade de produtos disponíveis em estoque.
- **Carga de Produtos em Massa**: Importação de produtos em massa a partir de arquivos CSV.
- **Persistência de Dados**: Utilização de Spring Data JPA para interação com banco de dados relacional.

## Tecnologias Utilizadas

- **Spring Boot**: Framework utilizado para o desenvolvimento e configuração do microsserviço.
- **Spring Data JPA**: Facilita o gerenciamento de dados e operações CRUD no banco de dados relacional.
- **Spring Batch**: Implementa a funcionalidade de carga de produtos, permitindo a automação do processo de importação em massa.
  
## Como Executar o Projeto

### Pré-requisitos

- Java 17+
- Docker (opcional, se preferir usar contêineres para o banco de dados)

### Passos

1. Clone o repositório:

   ```bash
   git clone git@github.com:WaldirJuniorGPN/ms-produto.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd ms-produto
   ```

3. Execute o projeto com o Maven:

   ```bash
   mvn spring-boot:run
   ```

4. Se preferir, utilize Docker para subir o banco de dados relacional (MySQL, por exemplo).

```

## Testes

O microsserviço possui testes unitários e de integração para validar o comportamento de suas principais funcionalidades.

### Executar os Testes

```bash
mvn test
```
