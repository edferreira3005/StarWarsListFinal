***************************
Aplicativo StarWarsList
***************************
Criado por: Edson da Silva Ferreira Junior
******************************************

USO DO APLICATIVO:
-O aplicativo tem como finalidade ler um QR Code para efetuar a busca de informações dos personagens de StarWars.
-Para criar o QR Code, qualquer site que faça isto pode ser utilizado. Exemplo: http://br.qr-code-generator.com/a1/?PID=1147&kw=qr%20code%20generator&gclid=Cj0KCQiAuZXQBRDKARIsAMwpUeRhGBOk9UkuBXDY5U4Sj1oscqIHJ4ezOso9nPtOakpLAwLeTpDCICwaAtj8EALw_wcB

-A URL para puxar os personagens é: https://swapi.co/api/people/ (Número do personagem). Exemplo: https://swapi.co/api/people/1
-O exemplo acima é do personagem Luke SkyWalker.

-Assim que o carregamento das informações terminar, basta clicar no nome do personagem que serão exibidas todas as informações do personagem.
-Se clicar nos filmes verá que abrirá a página oficial do próprio filme.

**O aplicativo grava informações de localização quando o GPS está ligado. Para mostrar no momento da captura dos dados, aonde e qual horário as informações foram capturadas pelo aparelho.

**Existe um controle de usuário para saber quem buscou as informações. Não é necessário senha. Só colocar um nome de usuário que o aplicativo já salvará seu nome.

****************************
VALIDAÇÕES
****************************
-Se não tiver nome de usuário, não irá conseguir usar o aplicativo.
-Nome de Usuário sem "Espaços"
-Internet precisa estar ligada antes da leitura do QR Code
-Localização de GPS não será ativada quando o próprio não estiver ativo no dispositivo.


****************************************************************

Informações técnicas

****************************************************************
-Para pegar informações das URLs foi feita uma conexão que foi adaptada da API do próprio StarWars API, pois as URLs não estavam sendo lidas corretamente por alguns aparelhos.
-Foi criado um banco de dados simples para relacionar alguns elementos, gravando também no cache do aplicativo.
-O SDK mínimo é o 25. (Android 5.0)

Dependências do Aplicativo:
-BarCodeScanner.
-UniversalImageLoader
-robolectric(Testes)

-Foi feita uma classe de customização para montar um cursor que tivesse imagens para aparecer as imagens dos filmes com seus respectivos nomes.
-Foram feitos alguns testes nos arquivos essenciais para o download das informações e em alguns campos chave.


*******************************
ESPERO QUE GOSTEM DO APLICATIVO
*******************************