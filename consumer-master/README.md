# Consumer Kafka Exemplo

Consumer Kafka simples.

## Requerimentos

- Java (JDK) 1.8
- Kafka

## Executar

Escolha um dos comandos abaixo para executar. Se for a primeira vez que
você faz isso, levará um tempo até que todas as dependências estejam 
disponíveis.

- `cfg`: é um argumento para configurações adicionais do consumidor.

__Linux__

```bash
./gradlew run \
  -Dkafka=localhost:9092 \
  -Dgrupo=kafkatrain \
  -Dtopico='<NOME DO TOPICO>' \
  -Dpoll='5000' \
  -Dcfg='cfg1=v1, cfg2=v2'
```

Exemplo:
```bash
./gradlew run \
  -Dkafka=localhost:9092 \
  -Dgrupo=kafkatrain \
  -Dtopico='meu-topico' \
  -Dpoll='5000' \
  -Dcfg='fetch.min.bytes=256, auto.offset.reset=latest'
```

__Windows__

```powershell
.\gradlew.bat run ^
-Dkafka=localhost:9092 ^
-Dgrupo=kafkatrain ^
-Dtopico='<NOME DO TOCIPO>' ^
-Dpoll='5000' ^
-Dcfg='cfg1=v1, cfg2=v2'
```

Exemplo:
```powershell
.\gradlew.bat run ^
-Dkafka=localhost:9092 ^
-Dgrupo=kafkatrain ^
-Dtopico='meu-topico' ^
-Dpoll='5000' ^
-Dcfg='fetch.min.bytes=256, auto.offset.reset=latest'
```