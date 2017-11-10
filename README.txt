***************************
Aplicativo StarWarsList
***************************
Criado por: Edson da Silva Ferreira Junior
******************************************

USO DO APLICATIVO:
-O aplicativo tem como finalidade ler um QR Code para efetuar a busca de informa��es dos personagens de StarWars.
-Para criar o QR Code, qualquer site que fa�a isto pode ser utilizado. Exemplo: http://br.qr-code-generator.com/a1/?PID=1147&kw=qr%20code%20generator&gclid=Cj0KCQiAuZXQBRDKARIsAMwpUeRhGBOk9UkuBXDY5U4Sj1oscqIHJ4ezOso9nPtOakpLAwLeTpDCICwaAtj8EALw_wcB

-A URL para puxar os personagens �: https://swapi.co/api/people/ (N�mero do personagem). Exemplo: https://swapi.co/api/people/1
-O exemplo acima � do personagem Luke SkyWalker.

-Assim que o carregamento das informa��es terminar, basta clicar no nome do personagem que ser�o exibidas todas as informa��es do personagem.
-Se clicar nos filmes ver� que abrir� a p�gina oficial do pr�prio filme.

**O aplicativo grava informa��es de localiza��o quando o GPS est� ligado. Para mostrar no momento da captura dos dados, aonde e qual hor�rio as informa��es foram capturadas pelo aparelho.

**Existe um controle de usu�rio para saber quem buscou as informa��es. N�o � necess�rio senha. S� colocar um nome de usu�rio que o aplicativo j� salvar� seu nome.

****************************
VALIDA��ES
****************************
-Se n�o tiver nome de usu�rio, n�o ir� conseguir usar o aplicativo.
-Nome de Usu�rio sem "Espa�os"
-Internet precisa estar ligada antes da leitura do QR Code
-Localiza��o de GPS n�o ser� ativada quando o pr�prio n�o estiver ativo no dispositivo.


****************************************************************

Informa��es t�cnicas

****************************************************************
-Para pegar informa��es das URLs foi feita uma conex�o que foi adaptada da API do pr�prio StarWars API, pois as URLs n�o estavam sendo lidas corretamente por alguns aparelhos.
-Foi criado um banco de dados simples para relacionar alguns elementos, gravando tamb�m no cache do aplicativo.
-O SDK m�nimo � o 25. (Android 5.0)

Depend�ncias do Aplicativo:
-BarCodeScanner.
-UniversalImageLoader
-robolectric(Testes)

-Foi feita uma classe de customiza��o para montar um cursor que tivesse imagens para aparecer as imagens dos filmes com seus respectivos nomes.
-Foram feitos alguns testes nos arquivos essenciais para o download das informa��es e em alguns campos chave.


*******************************
ESPERO QUE GOSTEM DO APLICATIVO
*******************************