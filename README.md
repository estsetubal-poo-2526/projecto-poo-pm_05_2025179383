# Jogo de Estratégia por Turnos

Este projeto é um jogo TBS local, focado na gestão de recursos.  
Os jogadores constroem estruturas que consomem e produzem recursos, geram pontuação e podem ser melhoradas ao longo do jogo.

No final dos 30 dias, vence o jogador com maior pontuação.

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
 - Ter Liberica 25 instalado
 - Ter um IDE de Java com suporte a Maven


# Como executar o Projeto

 - Copiar o repositorio: https://github.com/estsetubal-poo-2526/projecto-poo-pm_05_2025179383.git
 - Executar o ficheiro Launcher.java


# Estrutura do Projeto

``` text
src/
├── main/
│   ├── java/
│   │   └── jogo/
│   │       ├── Main.java          # Ponto de entrada da aplicação
│   │       ├── engine/            # Lógica principal do jogo, turnos, eventos e mapa
│   │       ├── exceptions/        # Exceções personalizadas para validação e erros do jogo
│   │       ├── io/                # Gravação e carregamento do estado do jogo
│   │       ├── models/            # Entidades, recursos, jogadores e estruturas
│   │       └── screens/           # Ecrãs e componentes da interface gráfica JavaFX
│   └── resources/
│       ├── icons/                 # Imagens e ícones utilizados na interface
│       └── jogo/
│           └── style.css          # Ficheiro CSS principal da aplicação
└── test/
└── java/                      # Testes unitários JUnit da lógica do jogo

```

# Autores

    - Fabio Cruz - https://github.com/FabioCruz2005
    - Tiago Silva - https://github.com/Notosant-IPS
