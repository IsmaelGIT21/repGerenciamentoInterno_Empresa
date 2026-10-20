# Sistema de Gestão de Empresas

## Sobre o projeto

O Sistema de Gestão de Empresas é uma aplicação desktop desenvolvida com o objetivo de centralizar e organizar informações de diferentes empresas em um único lugar.

Atualmente, informações empresariais podem estar distribuídas em diversas planilhas Excel, dificultando a consulta, a organização e a manutenção dos dados.

A proposta deste projeto é oferecer uma solução que facilite o gerenciamento dessas informações, permitindo cadastrar empresas, consultar seus dados e importar informações existentes em planilhas.

## Objetivos

- Centralizar informações de diferentes empresas.
- Facilitar a pesquisa de empresas pelo nome ou CNPJ.
- Permitir o cadastro e a edição de informações empresariais.
- Organizar os dados em uma estrutura centralizada.
- Possibilitar a importação de informações de planilhas Excel.
- Reduzir a dependência de múltiplos arquivos para consultar informações.
- Criar uma base para futuras melhorias e possíveis automatizações.

## Funcionalidades previstas

### Cadastro de empresas
- Cadastrar novas empresas.
- Consultar informações cadastrais.
- Editar dados existentes.
- Gerenciar os cadastros.

### Pesquisa de empresas
- Pesquisar empresas pelo nome.
- Pesquisar empresas pelo CNPJ.
- Consultar as informações associadas a cada empresa.

### Centralização de informações
- Armazenar informações empresariais em um único sistema.
- Organizar os registros de acordo com a empresa correspondente.
- Facilitar a consulta e a atualização dos dados.

### Importação de planilhas
- Importar informações de arquivos Excel.
- Validar os dados antes de armazená-los.
- Identificar possíveis inconsistências e duplicidades.
- Associar os registros às empresas correspondentes.

## Tecnologias propostas

As tecnologias abaixo são propostas para a implementação inicial do sistema e poderão ser ajustadas durante o desenvolvimento.

| Tecnologia | Finalidade |
|---|---|
| Java | Linguagem de programação |
| JavaFX | Desenvolvimento da interface gráfica |
| SQLite | Armazenamento local dos dados |
| Apache POI | Leitura e processamento de planilhas Excel |
| Maven | Gerenciamento de dependências e construção do projeto |
| Git | Controle de versão |
| GitHub | Hospedagem do repositório e gerenciamento do código-fonte |

## Arquitetura prevista

O projeto será organizado em camadas para facilitar a manutenção e a separação de responsabilidades.

- **Model:** representação das entidades do sistema.
- **DAO:** operações de acesso ao banco de dados.
- **Service:** implementação das regras de negócio.
- **Controller:** comunicação entre a interface e a lógica da aplicação.
- **Database:** gerenciamento da conexão com o banco de dados.
- **Views:** componentes visuais da aplicação.

## Requisitos iniciais

Para executar o projeto durante o desenvolvimento, serão necessários:

- Java Development Kit (JDK).
- Apache Maven.
- Uma IDE compatível com Java, como IntelliJ IDEA ou Eclipse.
- Git para controle de versão.

As versões e configurações específicas serão documentadas conforme a implementação avançar.

## Desenvolvimento

O desenvolvimento será realizado em etapas:

1. Levantamento e análise das informações existentes nas planilhas.
2. Modelagem do banco de dados.
3. Implementação do cadastro de empresas.
4. Desenvolvimento da pesquisa e consulta de informações.
5. Implementação da importação de planilhas Excel.
6. Validação dos dados importados.
7. Testes e melhorias da aplicação.

## Melhorias futuras

Após a implementação das funcionalidades iniciais, poderão ser avaliadas melhorias como:

- Relatórios de informações empresariais.
- Filtros adicionais de pesquisa.
- Histórico de alterações.
- Recursos adicionais de organização e gerenciamento.
- Possíveis automatizações de tarefas, conforme as necessidades identificadas.

## Status do projeto

Em fase de planejamento e definição da estrutura inicial.

As funcionalidades descritas neste documento representam os objetivos do projeto e serão implementadas progressivamente.

## Segurança e privacidade

O sistema poderá armazenar informações empresariais que exigem cuidados de segurança e confidencialidade.

Durante o desenvolvimento, deverão ser consideradas medidas para proteger os dados armazenados, controlar o acesso às informações e manter cópias de segurança.

Dados reais e informações confidenciais não devem ser publicados no repositório.

---

Desenvolvido para centralizar e facilitar o gerenciamento de informações empresariais.
