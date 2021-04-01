# Consumer Kafka - Timestamp

Consumer Kafka que processa registros com base no timestamp.

## Requerimentos

- Java (JDK) 1.8
- Kafka em `localhost:9092`

## Executar

Escolha um dos comandos abaixo para executar. Se for a primeira vez que
você faz isso, levará um tempo até que todas as dependências estejam 
disponíveis.

> Entre cada execução, altere o grupo. Assim você poderá visualizar o consumo
de cada timestamp. Varie também o timestamp e entenda como é o ajuste do offset.

__Linux__

```bash
./gradlew run \
  -Dkafka=localhost:9092 \
  -Dgrupo=kafkatrain \
  -Dtopico=lab5-timestamp \
  -Dtimestamp=<TIMESTAMP QUE VOCÊ GUARDOU>
```

__Windows__

```powershell
.\gradlew.bat run ^
-Dkafka=localhost:9092 ^
-Dgrupo=kafkatrain ^
-Dtopico=lab5-timestamp ^
-Dtimestamp=<TIMESTAMP QUE VOCÊ GUARDOU>
```