# KnowledgeGem

API REST para recursos de IA generativa aplicados ao desenvolvimento de produtos de software. O projeto usa Spring Boot e Spring AI para integrar o Google Gemini, memória de conversas e busca semântica com Qdrant.

## Funcionalidades

- Conversas com memória por `conversationId`.
- Geração de briefs de produto a partir de uma ideia.
- Geração de brief em streaming via Server-Sent Events (SSE).
- Revisão de imagens PNG e JPEG.
- Indexação de documentos de conhecimento, divididos em chunks e armazenados no Qdrant.
- Busca semântica filtrada por projeto.
- Respostas RAG baseadas nos documentos indexados, com referências às fontes.

## Tecnologias

- Java 25
- Spring Boot 4.1.2
- Spring AI 2.0.1
- Google Gemini
- Qdrant
- Maven Wrapper

## Pré-requisitos

- JDK 25
- Docker e Docker Compose
- Uma chave de API do Google Gemini

## Configuração

Defina a variável de ambiente usada pela aplicação:

```bash
export GEMINI_API_KEY="sua-chave-do-gemini"
```

O arquivo [`application.yaml`](src/main/resources/application.yaml) configura:

- Gemini para chat: `gemini-3.5-flash-lite`
- Gemini para embeddings: `gemini-embedding-001`
- Qdrant em `localhost:6334`
- Coleção `KnowledgeGem_collection`
- Porta HTTP padrão do Spring Boot: `8080`

> Não versionar chaves de API. Para desenvolvimento local, prefira variáveis de ambiente ou um arquivo de configuração fora do controle de versão.

## Executando o Qdrant

Suba o serviço de armazenamento vetorial:

```bash
docker compose up -d qdrant
```

O Qdrant ficará disponível em:

- API REST e Web UI: <http://localhost:6333>
- gRPC: `localhost:6334`

Para parar o serviço:

```bash
docker compose down
```

## Executando a aplicação

Com o Qdrant em execução e `GEMINI_API_KEY` definida:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API estará disponível em <http://localhost:8080>.

## Testes

```bash
./mvnw test
```

## API

### Conversas

Envia uma mensagem mantendo o histórico associado ao identificador da conversa:

```bash
curl -X POST http://localhost:8080/api/conversation \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "demo-1",
    "message": "Sugira três funcionalidades para um aplicativo de estudos."
  }'
```

Resposta:

```json
{
  "answer": "..."
}
```

Limpa a memória de uma conversa:

```bash
curl -X DELETE http://localhost:8080/api/conversation/demo-1
```

### Briefs de produto

Gera um brief estruturado:

```bash
curl -X POST http://localhost:8080/api/briefs \
  -H "Content-Type: application/json" \
  -d '{
    "input": "Uma plataforma para conectar estudantes a mentores."
  }'
```

Gera o brief em streaming:

```bash
curl -N -X POST http://localhost:8080/api/briefs/pitch \
  -H "Content-Type: application/json" \
  -d '{
    "input": "Uma plataforma para conectar estudantes a mentores."
  }'
```

Revisa uma imagem PNG ou JPEG de até 5 MB:

```bash
curl -X POST http://localhost:8080/api/briefs/review-photo \
  -F "image=@./imagem.png"
```

### Conhecimento

Indexa um documento no Qdrant. O conteúdo é dividido em chunks antes da geração dos embeddings:

```bash
curl -X POST http://localhost:8080/api/knowledge \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "projeto-demo",
    "source": "docs/visao-geral.md",
    "content": "Conteúdo do documento que será usado como contexto..."
  }'
```

Resposta:

```json
{
  "chuncknized": 1
}
```

Busca os trechos mais semelhantes dentro de um projeto:

```bash
curl "http://localhost:8080/api/knowledge/search?projectId=projeto-demo&query=qual%20%C3%A9%20o%20objetivo%20do%20projeto"
```

### RAG

Responde usando os documentos indexados e retorna as fontes utilizadas:

```bash
curl -X POST http://localhost:8080/api/rag/answer \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "projeto-demo",
    "question": "Qual é o objetivo do projeto?"
  }'
```

Também está disponível uma implementação baseada no `QuestionAnswerAdvisor` do Spring AI:

```bash
curl -X POST http://localhost:8080/api/rag/answerAdvisor \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "projeto-demo",
    "question": "Quais são as principais funcionalidades?"
  }'
```

Resposta esperada:

```json
{
  "answer": "...",
  "sources": [
    {
      "documentId": "...",
      "source": "docs/visao-geral.md",
      "score": 0.87
    }
  ]
}
```

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/aichat/springaigemini/
│   │   ├── configuration/  # Beans do Gemini, memória e Qdrant
│   │   ├── controller/     # Endpoints REST
│   │   ├── model/          # DTOs de entrada e saída
│   │   └── service/        # Regras de negócio e integração com IA
│   └── resources/
│       └── application.yaml
└── test/
    └── java/               # Testes de contexto da aplicação
```

## Validações atuais

- `conversationId` e `projectId`: letras, números, `_` e `-`, com até 80 caracteres.
- Mensagens de conversa: até 10.000 caracteres.
- Conteúdo de documentos: até 20.000 caracteres.
- Perguntas RAG: até 100 caracteres.
- Imagens: somente PNG e JPEG, com limite de 5 MB.

## Próximos passos

Melhorias planejadas no estado atual do projeto incluem:

- autenticação e autorização com Spring Security;
- tratamento global e padronizado de erros;
- observabilidade e logs estruturados;
- documentação OpenAPI/Swagger;
- ampliação da cobertura de testes.

## Licença

Este projeto ainda não declara uma licença. Consulte os responsáveis antes de reutilizar ou distribuir o código.
