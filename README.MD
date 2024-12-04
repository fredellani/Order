# Order Application

## Descrição

Este é um projeto de exemplo de uma aplicação de pedidos construída com Java, Spring Boot e Maven. A aplicação utiliza um banco de dados MariaDB para armazenar os dados dos pedidos.

## Estrutura do Projeto

- **Java**: Linguagem de programação utilizada.
- **Spring Boot**: Framework utilizado para construir a aplicação.
- **Maven**: Ferramenta de gerenciamento de dependências e build.
- **Docker**: Utilizado para containerizar a aplicação e o banco de dados.
- **Docker Compose**: Utilizado para orquestrar os containers da aplicação e do banco de dados.

## Pré-requisitos

- Java 17
- Maven 3.6+
- Docker
- Docker Compose

## Configuração

### Banco de Dados

A aplicação utiliza MariaDB como banco de dados. As configurações do banco de dados estão definidas no arquivo `compose.yaml`.

### Variáveis de Ambiente

As seguintes variáveis de ambiente são utilizadas na aplicação:

- `SPRING_DATASOURCE_URL`: URL de conexão com o banco de dados.
- `SPRING_DATASOURCE_USERNAME`: Nome de usuário do banco de dados.
- `SPRING_DATASOURCE_PASSWORD`: Senha do banco de dados.
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Configuração do Hibernate para criação do esquema do banco de dados.
- `SPRING_JPA_SHOW_SQL`: Exibir SQL gerado pelo Hibernate.
- `SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT`: Dialeto do Hibernate para MariaDB.

## Como Executar

### Localmente

1. Clone o repositório:
    ```sh
    git clone https://github.com/seu-usuario/order-app.git
    cd order-app
    ```

2. Compile e empacote a aplicação:
    ```sh
    mvn clean package
    ```

3. Execute a aplicação:
    ```sh
    java -jar target/order-0.0.1-SNAPSHOT.jar
    ```

### Usando Docker Compose

1. Construa e inicie os containers(não se esqueça de ter o projeto buildado com o Maven previamente):
    ```sh
    docker-compose up --build
    ```

2. A aplicação estará disponível em `http://localhost:8080`.

## Endpoints da API

- `POST /orders`: Cria um novo pedido.

## Entendendo o projeto
A escolha do banco de dados MariaDB se deve a sua capacidade de conexões concorrentes, além da possibilidade de utilização de um driver não bloqueante para conexão com o banco de dados.
Ao executar a aplicação e enviar um POST, no projeto está configura o serviço B(Webhook) em uma url pública de um serviço web de webhooks, acesse https://webhook.site/#!/view/f730a43a-70e7-4589-b72d-e20239a6838c/ para visualizar os pedidos processados.
