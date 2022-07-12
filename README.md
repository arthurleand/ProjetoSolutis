# Projeto Solutis
Sistema de uma cooperativa onde cada associado possui um voto e as decisões são tomadas através de assembleias, por votação.
### 📋 Pré-requisitos
```
Java 11 ou mais atual
```
```
Docker
```
## 🔧 Instalação
Clone o repositório:
```
git clone https://github.com/arthurleand/ProjetoSolutis.git
```
Na pasta clonada abra o terminal e rode o docker compose:
```
docker compose up
```
Na IDE de preferência rode o projeto.

## ⚙️ Acessos das principais funções
Senha MySQL:
```
User: rooot
Password:arthur
```
Usuário e senha do RabbitMQ
```
User: admin
Password: admin
```
## 🖥️ Link Swagger
```
http://localhost:8080/swagger-ui/#/
```
## 📃 Tarefas bônus realizadas 
### Tarefa Bônus 1 - Integração com sistemas externos 
Integrar com um sistema externo que verifica o CPF antes de cadastrar um usuário. Caso o sistema esteja indisponível, você deve fazer mais duas tentativas (retry) no intervalo de 2 e 4 segundos respectivamente (você deve registrar as tentativas no log). Se ainda assim o serviço externo esteja indisponível, permita o cadastro do usuário, mas registre no log que o serviço não pode ser consultado. OBS: O serviço só verifica se foram passados 11 dígitos ou não, veja exemplos de requisição e retornos abaixo.
### Tarefa Bônus 2 - Contabilização automática 
A contabilização de votos de pautas encerradas (RF4), deve ser feita de forma automática pelo sistema. A rotina de contabilização deve ser executada a cada minuto. Os dados devem ser persistidos no banco de dados.
### Tarefa Bônus 3 - Mensageria e filas 
Quando cada sessão de votação for fechada, poste uma mensagem em uma mensageria (Kafka, RebbitMQ ou qualquer outra) com o resultado da votação. Forneça dockerfile ou configurações necessárias para o serviço de mensageiria utilizado.
#### Link RabbitMQ
```
http://localhost:15672/
```
### Tarefa Bônus 6 - Versionamento da API 
Sistemas evoluem constantemente logo é possível que sua API mude e seja necessário que você mantenha diferentes versões em produção (ex: 1.0, 2.0) até que seus clientes (pessoas/sistemas que consomem sua api) realizem as mudanças necessárias para consumir a nova versão da API. Escreva um texto dizendo qual estratégia você utilizaria para versionar a sua API.

#### Texto Versionamento 
Está API irá trabalhar com versionamento via URL no modelo subdomínio onde será especificada a versão logo no inicío da URL dando assim ao cliente tempo de implementar as mudanças necessárias para utilizar a nova versão da API e facilitando a troca entre versões. 
