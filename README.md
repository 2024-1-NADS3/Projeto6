# FECAP - Fundação de Comércio Álvares Penteado

<p align="center">
<a href= "https://www.fecap.br/"><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhZPrRa89Kma0ZZogxm0pi-tCn_TLKeHGVxywp-LXAFGR3B1DPouAJYHgKZGV0XTEf4AE&usqp=CAU" alt="FECAP - Fundação de Comércio Álvares Penteado" border="0"></a>
</p>

# Educaliza

## Integrantes: Wilson Testoni, Gabriel Italo, Matheus Adaniya, Cleiton

## Professores Orientadores: Adriano Felix Valente, Victor Bruno Alexander Rosetti de Quiroz

## Descrição

<p align="center">
<img src="./imagens/educaliza_logo.png" height="250px" alt="Logo educaliza" border="0">
</p>

O Educaliza é um aplicativo que facilita o acesso a cursos e aulas gratuitos na cidade de São Paulo, fornecendo uma plataforma onde parceiros podem publicar 
informações detalhadas sobre seus cursos, como datas de início e término, localização e mais. Com ele, você encontra o conteúdo que deseja estudar sem 
nenhum custo! Esses cursos são oferecidos por instituições parceiras, como ONGs, garantindo a diversidade e a qualidade das opções disponíveis.

## 💻 Tecnologias: <br>
### Front-end
- **Android Studio**: Um ambiente de desenvolvimento integrado (IDE) usado principalmente para desenvolver aplicativos Android. Ele fornece ferramentas para escrever código, depurar e testar aplicativos Android.

- **Java**: Uma linguagem de programação amplamente utilizada, especialmente para desenvolvimento de aplicativos Android. É uma linguagem orientada a objetos com uma ampla gama de bibliotecas e frameworks disponíveis.

### Back-end 
- **Node.js**: Ambiente de execução JavaScript, utilizado para construir aplicações escaláveis e de alta performance.
- **SQLite**: Sistema de gerenciamento de banco de dados para armazenamento de dados do aplicativo.

## 💻 Bibliotecas: <br> 
- **Android Studio**:
   <p>Picasso: Uma biblioteca para carregamento e exibição de imagens em aplicativos Android. Ele fornece uma maneira simples e fácil de carregar imagens de URLs ou do sistema de arquivos.</p>
   <p>Volley: Uma biblioteca de rede do Android que simplifica o processo de fazer solicitações de rede, como solicitações HTTP. É útil para realizar operações de rede de forma eficiente e assíncrona em aplicativos Android.</p>
  
- **Node.js**:
  <p>Nodemailer: Uma biblioteca de e-mail para Node.js que permite enviar e-mails facilmente através de servidores SMTP ou outros meios de transporte.</p>
  <p>Bcrypt: Uma biblioteca utilizada para encriptar senhas dos usuários. É comumente usada para armazenar senhas de forma segura em bancos de dados.</p>
  <p>jsonwebtoken: Uma biblioteca para Node.js que facilita a geração e verificação de tokens de autenticação. É comumente usado em sistemas de autenticação baseados em tokens, como JSON Web Tokens (JWT).</p>
  <p>Express: Um framework web para Node.js que simplifica a criação de aplicativos web e APIs. Ele fornece uma camada fina sobre o HTTP, permitindo o roteamento fácil, middleware e manipulação de requisições e respostas. </p>
  <p>dotenv: Uma biblioteca para carregar variáveis de ambiente de arquivos .env em Node.js. É útil para configurar facilmente variáveis de ambiente em diferentes ambientes de desenvolvimento.</p>
  <p>cors: Uma biblioteca utilizada para habilitar o Cross-Origin Resource Sharing (CORS) em aplicativos Node.js. Isso permite que os navegadores solicitem recursos de um servidor em um domínio diferente.</p>
  

## 🛠 Estrutura de pastas

-Raiz<br>
|-->BUILD/realease<br>
  &emsp;|-->app-realese.aab<br>
|-->documentos<br>
  &emsp;|-->backend<br>
  &emsp;|-->google-docs<br>
  &emsp;|-->Projeto_de_Extenção_Educaliza.pdf<br>
  &emsp;|-->documentação devops<br>
|-->executavéis<br>
  &emsp;|--> android<br>
|-->imagens<br>
  &emsp;|-->educaliza_logo.png<br>
|-->src<br>
  &emsp;|-->backend<br>
  &emsp;|-->frontend<br>
|readme.md<br>


## 💻 Instalação Local

### Requisitos: NodeJS, AndroidStudio

Configuração do Backend (API)

Clone o repositório e vá para o diretório backend
```sh
git clone https://github.com/2024-1-NADS3/Projeto6.git
cd src/backend
```

Abra o terminal e execute os comandos para instalar as dependências e iniciar o servidor
```sh
npm i
node index
```

Configuração do Frontend (App Android)

Clone o repositório
```sh
git clone https://github.com/2024-1-NADS3/Projeto6.git
```

Abra o Android Studio.
Selecione "Open an existing Android Studio project" e navegue até o diretório Projeto6/src/frontend.

O Android Studio deve automaticamente instalar todas as dependências necessárias ao abrir o projeto. Certifique-se de que o Gradle esteja sincronizado.
Configure o emulador ou dispositivo físico.

Configure um emulador Android ou conecte um dispositivo físico via USB para testar a aplicação.
Execute a aplicação

Clique no botão "Run".


## 🗃 Histórico de lançamentos

A cada atualização os detalhes devem ser lançados aqui.

* 0.2.1 - 25/01/2022
    * MUDANÇA: Atualização de docs (código do módulo permanece inalterado)
* 0.2.0 - 15/01/2022
    * MUDANÇA: Remove `setDefaultXYZ()`
    * ADD: Adiciona `init()`
* 0.1.1 - 11/01/2022
    * CONSERTADO: Crash quando chama `baz()` (Obrigado @NomeDoContribuidorGeneroso!)
* 0.1.0 - 10/01/2022
    * O primeiro lançamento adequado
    * MUDANÇA: Renomeia `foo()` para `bar()`
* 0.0.1 - 01/01/2022
    * Trabalho em andamento

## 📋 Licença/License


## 🎓 Referências

Aqui estão as referências usadas no projeto.

1. <https://developer.android.com/reference/org/w3c/dom/Document>
2. <https://www.npmjs.com/package/bcrypt>
3. <https://jwt.io/>
4. <https://www.nodemailer.com/>
