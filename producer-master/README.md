# Producer Kafka

Producer Kafka

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
  -Dproduzir='<NOME DO TOPICO ONDE SERÃO PRODUZIDOS>' \
  -Dcfg='cfg1=v1, cfg2=v2'
```

Exemplo:
```bash
./gradlew run \
  -Dkafka=localhost:9092 \
  -Dproduzir='lab4-producer'
```

__Windows__

```powershell
.\gradlew.bat run ^
-Dkafka=localhost:9092 ^
-Dproduzir='<NOME DO TOPICO ONDE SERÃO PRODUZIDOS>' ^
-Dcfg='cfg1=v1, cfg2=v2'
```

Exemplo:
```powershell
.\gradlew.bat run ^
-Dkafka=localhost:9092 ^
-Dproduzir='lab4-producer'
```