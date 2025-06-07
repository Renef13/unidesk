# Sistema de Help-Desk Universitário - Backend

## 📋 Sobre o Projeto

O backend do Sistema de Help-Desk Universitário é uma API REST desenvolvida em **Spring Boot** que gerencia a comunicação entre alunos e coordenação do curso. O sistema centraliza o atendimento de demandas acadêmicas, organiza o fluxo de trabalho e permite o acompanhamento de solicitações de forma eficiente.

## 🎯 Objetivos

- Facilitar o registro e acompanhamento de solicitações acadêmicas
- Centralizar o atendimento entre alunos e coordenação
- Melhorar o tempo de resposta das demandas
- Organizar o fluxo de trabalho da coordenação
- Fornecer base de conhecimento com soluções comuns

## 🏗️ Arquitetura

### Stack Tecnológica

**Framework Principal:**
- **Spring Boot 3.0** - Framework base da aplicação
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Camada de persistência
- **Hibernate** - ORM para mapeamento objeto-relacional

**Banco de Dados:**
- **PostgreSQL** - Banco de dados relacional principal

**Segurança:**
- **JWT** - Tokens para autenticação (em análise)
- Criptografia para comunicação segura

**Documentação:**
- **Swagger** - Documentação automática da API

## 🚀 Funcionalidades

### Core Features

- **Gestão de Tickets**: CRUD completo de solicitações
- **Sistema de Categorização**: Organização por tipo de demanda
- **Controle de Status**: Acompanhamento do ciclo de vida dos tickets
- **Sistema de Chat**: Comunicação bidirecional por ticket
- **Base de Conhecimento**: Repository de FAQs e soluções
- **Sistema de Filtros**: Busca por categoria, status, prioridade e data
- **Geração de Relatórios**: Métricas e analytics de atendimento


## 🔒 Segurança

- **Autenticação JWT** para endpoints protegidos
- **Criptografia** nas comunicações sensíveis
- **Validação de entrada** em todos os endpoints
- **Controle de acesso baseado em roles**
- **Rate limiting** para prevenção de ataques


## 📝 Glossário

- **Ticket**: Registro digital de uma solicitação do aluno
- **SLA**: Acordo de Nível de Serviço com prazos de atendimento
- **Triagem**: Análise e categorização inicial de tickets
- **Escalação**: Encaminhamento para nível superior de atendimento

## 👥 Equipe

- **Caio, Kevin & Renef** - Desenvolvedores Backend/Frontend
- **Caio, Kevin & Renef** - DevOps e Arquitetura

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

**Versão da API:** 1.0.0  
**Última atualização:** Janeiro 2025