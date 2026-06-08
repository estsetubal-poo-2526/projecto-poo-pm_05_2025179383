Jogo

É um jogo TBS local, focado em gestão de recursos, que consiste em construir estruturas que custam e produzem recursos e dão pontuaçoes. Ao final dos 30 dias, o jogador com mais pontuação ganha


# Funcionalidades Pricipais

 - Mecânica de Turnos onde cada ação conta para vencer
 - Interface Gráfica Intuitiva desenvolvida com o JavaFX
 - Persistencia de Dados com suporte a gravação e carregamento de estados de jogo
 - Criação de Estruturas Dinamicas, onde cada estrutura produz um recurso diferente
 - Melhoramento das estruturas, onde cada estrutura produz mais e custa mais para ser mantida
 - Sistema de eventos aleatorios que faz com que os dias no jogo não sejam sempre iguais
 - Sistema de busca de recursos para evitar o soft-lock
 - Testes uniátios implementados para testar todas as possibílidades

# Tecnologias Utilizadas
 - Linguagens: Java
 - Estilização: CSS
 - GUI: JavafX
 - Ferramenta de Construção: Maven


# Pre-Requesitos

 - JDK 17+ Instalado
 - Ter um IDE de Java com suporte a Maven


# Como executar o Projeto

 - Copiar o repositorio: https://github.com/estsetubal-poo-2526/projecto-poo-pm_05_2025179383.git
 - Executar o ficheiro Main.java


# Estrutura do Projeto

```text
src/
├── main/
│   ├── java/
│   │   └── jogo/
│   │       ├── Main.java          # Ponto de entrada da aplicação
│   │       ├── engine/            # Motores e controladores do ciclo de jogo
│   │       ├── exceptions/        # Exceções personalizadas para controlo de erros
│   │       ├── io/                # Lógica de salvamento e carregamento de ficheiros
│   │       ├── models/            # Lógica base, entidades e regras do jogo
│   │       └── screens/           # Janelas, controladores e componentes da interface gráfica
│   └── resources/                 # Assets globais do projeto
│       ├── icons/                 # Imagens e ícones utilizados na interface
│       └── style/                 # Ficheiros CSS para estilização da UI
└── test/                          # Pacote contendo os testes unitários da lógica do jogo
```

   #Autores

   Fabio Cruz - https://github.com/FabioCruz2005
   Tiago Silva - https://github.com/Notosant-IPS
