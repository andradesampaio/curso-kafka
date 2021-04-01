# Producer Kafka c/ Transação

Producer Kafka que demonstra o uso de transações, inclusive em conjunto
com o Consumer. Isso garante um ciclo atômico de read-process-write 
operando com garantia de entrega _exactly-once_.

## Requerimentos

- Java (JDK) 1.8
- Kafka

## Executar

Escolha um dos comandos abaixo para executar. Se for a primeira vez que
você faz isso, levará um tempo até que todas as dependências estejam 
disponíveis.

- `cfg`: é um argumento para configurações adicionais do produtor.

__Linux__

```bash
./gradlew run \
  -Dkafka=localhost:9092 \
  -Dconsumir='<NOME DO TOPICO QUE SERÁ CONSUMIDO>' \
  -Dproduzir='<NOME DO TOPICO ONDE SERÃO PRODUZIDOS>' \
  -Dcfg='cfg1=v1, cfg2=v2'
```

Exemplo:
```bash
./gradlew run \
  -Dkafka=localhost:9092 \
  -Dconsumir='meu-topico-in' \
  -Dproduzir='meu-topico-out'
```

__Windows__

```powershell
.\gradlew.bat run ^
-Dkafka=localhost:9092 ^
-Dconsumir='<NOME DO TOPICO QUE SERÁ CONSUMIDO>' ^
-Dproduzir='<NOME DO TOPICO ONDE SERÃO PRODUZIDOS>' ^
-Dcfg='cfg1=v1, cfg2=v2'
```

Exemplo:
```powershell
.\gradlew.bat run ^
-Dkafka=localhost:9092 ^
-Dconsumir='meu-topico-in' ^
-Dproduzir='meu-topico-out'
```