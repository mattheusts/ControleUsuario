# Integrantes

- Matheus Teixeira dos Santos - 2020205272
- Lucas Torres Sobral -

# Sistema de Gestão e Autorização de Usuários

Este projeto consiste em uma aplicação desktop desenvolvida em Java 17 com Swing,
cujo objetivo é realizar a gestão de usuários, controle de autenticação, autorização,
envio e leitura de notificações e geração de logs, conforme especificado na atividade
avaliativa da disciplina de Projeto de Sistemas de Software.

## Tecnologias utilizadas

- Java 17
- Java Swing
- Maven
- SQLite
- Arquitetura MVP (Passive View)
- JitPack (dependências externas)

## Arquitetura

A aplicação foi desenvolvida utilizando o padrão Model-View-Presenter (MVP) na abordagem
Passive View. As Views são responsáveis apenas pela interface gráfica, enquanto toda a
lógica de apresentação e orquestração das operações é realizada pelos Presenters.

## Estrutura de pacotes

- model: classes de domínio
- view: interfaces gráficas (Swing)
- presenter: lógica de apresentação
- repository: acesso a dados (SQLite)
- service: regras de negócio
- config: configurações do sistema

## Funcionalidades

- Cadastro do primeiro usuário administrador
- Autenticação de usuários autorizados
- Cadastro e autorização de usuários
- Promoção e rebaixamento de perfis
- Exclusão de usuários
- Envio e leitura de notificações
- Alteração de senha
- Configuração do formato de logs (CSV e JSONL)
- Restauração completa do sistema

## Como executar

1. Clonar o repositório:
   git clone [https://github.com/mattheusts/ControleUsuario.git](https://github.com/mattheusts/ControleUsuario.git)
2. Abrir o projeto em uma IDE compatível (NetBeans ou IntelliJ)

3. Executar a classe principal da aplicação

Observação: o banco de dados SQLite (.db) será criado automaticamente na raiz do projeto.

## Logs

O sistema registra operações relevantes em arquivos de log nos formatos CSV ou JSON,
conforme configuração realizada pelo administrador. Os logs são gerados por meio de
uma biblioteca externa disponibilizada via JitPack.

## Requisitos não implementados

[Requisitos não implementados](https://docs.google.com/document/d/1RTXJpkyc-eVIA2UQbV0sxCfKcsBwCcjOz_RAkwXQP78/edit?usp=sharing)
