# E-Commerce API 🚀

Uma API RESTful robusta e completa desenvolvida em **Java 21 com Spring Boot** para gerenciar as operações de um sistema de comércio eletrônico, incluindo clientes, produtos e o controle transacional de pedidos.

---

## 🛠️ Tecnologias e Padrões Aplicados

Este projeto foi construído visando um design robusto, fácil manutenção e aderência às melhores práticas de Engenharia de Software.

- **Java 21 LTS** & **Spring Boot 3**
- **Maven Wrapper** (Não necessita que o Maven seja pré-instalado externamente)
- **Spring Data JPA & Hibernate** integrados ao banco de dados em memória **H2** para testes dinâmicos e rápidos.
- **Bean Validation (Jakarta Validation)** para consistência e sanitização em todas as requisições de entrada.

### 🌟 Boas Práticas e Arquitetura

1. **Design Orientado ao Domínio Puro**: Ao invés do tradicional e cômodo uso de `Lombok`, **todos os getters, setters e construtores desta aplicação foram implementados manualmente**, garantindo transparência impecável nos objetos.
2. **Separação de Preocupações (SoC)**:
   - **Controllers**: Lidam estritamente com as requisições e respostas HTTP.
   - **Services**: Contêm todas as regras de negócio sensíveis. Anotados com `@Transactional` de forma inteligente nas mutações de estado e garantindo segurança entre queries complexas (ex: abater estoque e criar pedido).
   - **Model**: Estruturas restritas ao banco de dados.
   - **Data Transfer Objects (DTOs)**: Implementados bidirecionalmente, garantindo o limite e não-vazamento de entidades internas diretamente para o cliente, resolvendo problemas de serialização e circularidade.
3. **Injeção de Dependência por Construtor**: Fortalecendo componentes mais seguros em vez da injeção arbitrária baseada em `@Autowired` por campo.
4. **Resiliência e Tratamento Global de Erros**: O `GlobalExceptionHandler` (`@ControllerAdvice`) captura não somente as propagações do `@Valid`, mas também centraliza as `RegraNegocioException`s, traduzindo exceções Java estritas em pacotes JSON legíveis no padrão HTTP adequado (`400 Bad Request`, `404 Not Found`).

---

## 🔒 Regras de Negócio Importantes Implementadas

- **Clientes**: Restrição de chaves únicas no banco, anulando e alertando amigavelmente cadastros de múltiplos e-mails iguais.
- **Estoque & Precificação**: A API do usuário não aceita totalizações manuais. O sistema subtrai o estoque isoladamente em *runtime* e calcula a multiplicação do preço no banco no momento do processamento.
- **Fluxo do Pedido**: Implementação de barreira condicional em enum sobre o ciclo de vida. (`CRIADO` → `PAGO` → `ENVIADO`). Tentar alterar um pedido ENVIADO retorna falha. 
- **Cancelamento**: O bloqueio garante reabastecimento logístico; não é possível cancelar a menos que o *status* do produto esteja `CRIADO`.

---

## 🏃 Como rodar a Aplicação

### Pré-Requisitos
Ter apenas o `Java 21` em sua máquina.

### Execução Simples
1. Abra um terminal na pasta raiz do projeto.
2. Rode o comando nativo que fará o bootstrap e inicialização completa:
   - (No Windows): `./mvnw.cmd spring-boot:run`
   - (No Linux/Mac): `./mvnw spring-boot:run`
3. A aplicação estará ativa em: `http://localhost:8080/`

### Console do Banco de Dados
A API gera e recria os esquemas automaticamente na memória através do H2. Para consultar o banco visualmente, use o navegador:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:ecommerce_db`
- **Username**: `sa`
- **Password**: `password`

### Ambientes IDE
- **Eclipse**: O projeto já contém arquivos `.project` e `.classpath`. Escolha "Import Existing Projects into Workspace".
- **IntelliJ/VSCode**: Importe reconhecendo a pom.xml.

---

## 🧪 Realizando Testes na API

Em anexo, você poderá ver o arquivo `payloads.json` isolado que acompanha este repositório, mas aqui estão algumas requisições cruciais pra testar a validação e fluxo. Recomenda-se utilizar o [Postman](https://www.postman.com/) ou requisições `curl`.

### 1. Criar um Cliente (POST `/clientes`)
```json
{
  "nome": "João da Silva",
  "email": "joao.silva@teste.com.br",
  "enderecos": [
    {
      "rua": "Av Central, 100",
      "cidade": "São Paulo",
      "cep": "00000-000"
    }
  ]
}
```

### 2. Cadastrar um Produto (POST `/produtos`)
```json
{
  "nome": "Notebook Avante",
  "preco": 3500.00,
  "estoque": 5
}
```

### 3. Orquestrar a Compra (POST `/pedidos`)
Observe que você não precisa calcular o preço; ele será resolvido transacionalmente baseado no banco de dados.
```json
{
  "clienteId": 1,
  "enderecoEntregaId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2
    }
  ]
}
```

### 4. Testando Segurança e Validação Negada:
Tente enviar a compra de um usuário passando o campo `quantidade: 99` (para esgotar o estoque testado na base), ou criar no POST `/clientes` um usuário com mesmo e-mail, e note que o sistema devolverá com estrutura legível em JSON a falha correspondente!
