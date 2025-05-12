# Desafio Backend - BTG Pactual

## 📋 Visão Geral

Este repositório contém a solução para o desafio técnico proposto pelo BTG Pactual para a posição de **Engenheiro de Software**. O objetivo do desafio foi desenvolver uma aplicação backend robusta capaz de:

* Consumir pedidos enviados via **RabbitMQ**
* Persistir os dados em um banco **MongoDB**
* Expor uma **API REST** para consultas agregadas e específicas
* Prover testes automatizados de suas funcionalidades

A solução foi desenvolvida utilizando **Java 21** com o ecossistema **Spring Boot**, aproveitando recursos como Spring Web, Spring Data MongoDB e Spring AMQP para integração com a fila RabbitMQ.

---

## 🚀 Desafio Proposto

**Objetivo:** Criar um microsserviço que receba pedidos em formato JSON, grave os dados em um banco e permita consultas REST.

### 🧾 Exemplo da mensagem (JSON) recebida via RabbitMQ:

```json
{
  "codigoPedido": 1001,
  "codigoCliente": 1,
  "itens": [
    {
      "produto": "lápis",
      "quantidade": 100,
      "preco": 1.10
    },
    {
      "produto": "caderno",
      "quantidade": 10,
      "preco": 1.00
    }
  ]
}
```

### 📌 Funcionalidades solicitadas:

* Gravar cada pedido recebido via fila RabbitMQ
* Calcular e armazenar o valor total do pedido
* Criar uma API REST que permita:

  * Consultar o valor total de um pedido
  * Listar pedidos realizados por cliente
  * Obter a quantidade total de pedidos por cliente

---

## 🛠️ Tecnologias e Ferramentas

* **Java 21**
* **Spring Boot**

  * Spring Web
  * Spring Data MongoDB
  * Spring AMQP (RabbitMQ)
* **MongoDB**
* **RabbitMQ**
* **JUnit 5 + Mockito** (para testes)
* **Docker / Docker Compose**
* **Maven**

---

## 🧱 Estrutura do Projeto

```
src/
├──/main
  ├── controller/         # Camada REST
  ├── service/            # Lógica de negócios
  ├── repository/         # MongoDB repository
  ├── model/              # Entidades e DTOs
  ├── config/             # Configurações (RabbitMQ, MongoDB)
  ├── listener/           # Lógica de consumo da fila RabbitMQ
└─tests/
  ├──testes              # Testes unitários e de serviço
```

---

## 🔍 Endpoints da API

| Método | Rota                                          | Descrição                                        |
| ------ | --------------------------------------------- | ------------------------------------------------ |
| GET    | `/pedidos/total/{codigoPedido}`               | Retorna o valor total do pedido                  |
| GET    | `/pedidos/cliente/{codigoCliente}`            | Retorna todos os pedidos de um cliente           |
| GET    | `/pedidos/cliente/{codigoCliente}/quantidade` | Retorna a quantidade total de pedidos do cliente |

---

## 🧪 Testes

* **Cobertura:** serviços, controller e consumo da fila
* **Ferramentas:** JUnit 5 + Mockito
* Para executar:

```bash
mvn test
```

---

## 🐳 Execução com Docker

Com `docker-compose.yml` incluído no projeto, basta rodar:

```bash
docker-compose up --build
```

Isso inicializa:

* RabbitMQ
* MongoDB
* Aplicação Spring Boot

A aplicação ficará acessível via: `http://localhost:8080`

---

## 📊 Plano de Trabalho

| Task | Descrição                                | Status |
| ---- | ---------------------------------------- | ------ |
| 1    | Setup inicial do projeto e dependências  | ✅      |
| 2    | Configuração do RabbitMQ + MongoDB       | ✅      |
| 3    | Implementação do consumidor de mensagens | ✅      |
| 4    | Modelagem de dados e persistência        | ✅      |
| 5    | Criação dos endpoints REST               | ✅      |
| 6    | Implementação dos testes                 | ✅      |
| 7    | Dockerização e documentação              | ✅      |
