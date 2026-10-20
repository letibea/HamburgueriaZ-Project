# HamburgueriaZ

Aplicativo Android nativo em Java para montar um pedido de hambúrguer e encaminhá-lo a um aplicativo de e-mail instalado no aparelho.

## Abrir no Android Studio

1. Descompacte `HamburgueriaZ.zip`.
2. No Android Studio, selecione **File > Open** e escolha a pasta `HamburgueriaZ`.
3. Aguarde a sincronização do Gradle. O Android Studio instalará os componentes necessários se forem solicitados.
4. Execute o app em um emulador ou aparelho Android.

O projeto usa `minSdk 23` (Android 6.0), compile SDK 35 e Android Gradle Plugin 8.6.1. O código Java está configurado para compatibilidade com Java 17; o JDK 20 indicado no roteiro também pode ser usado para executar o Gradle.

Para gerar o APK de depuração pelo terminal, execute `./gradlew assembleDebug` (Windows: `gradlew.bat assembleDebug`).

## O que está implementado

- Campo para o nome do cliente.
- Checkboxes de bacon, queijo e onion rings.
- Seletor de quantidade com limites para não permitir valores negativos.
- Preço atualizado conforme os adicionais e a quantidade: hambúrguer R$ 20,00; bacon R$ 2,00; queijo R$ 2,00; onion rings R$ 3,00.
- Resumo do pedido com as escolhas e o preço total.
- Envio do resumo por e-mail usando `Intent.ACTION_SENDTO` e assunto `Pedido de (nome do cliente)`.
- Faixa de logo no topo e estilo compartilhado `EstiloTexto` para os títulos e o total.

Para testar o e-mail, instale e configure um aplicativo de e-mail no emulador ou aparelho.
