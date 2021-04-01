package consumer;

import static java.lang.System.getProperty;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class App {
    public static void main(String[] args) {
        
        System.out.println(" > > Consumer java");

        // Ser timestamp for um número inválido, será lançado um erro
        Long timestamp = Long.valueOf(getProperty("timestamp"));

        try(KafkaConsumer<String, String> consumer = 
                new KafkaConsumer<>(criarProperties())){

            System.out.println(" > > > Timestamp " + timestamp);

            // #########
            // Tópico
            consumer.subscribe(
                Arrays.asList(getProperty("topico", "um-topico")),
                new TimestampAwareListener(timestamp, consumer));

            while(true){
                // #########
                // Consumir . . . 
                ConsumerRecords<String, String> records = 
                    consumer.poll(Duration.ofSeconds(5));

                if(records.count() > 0){
                    System.out.println(" > > > > tamanho do lote consumido: " 
                        + records.count());
                }
                
                // Processar os registros
                records
                    .forEach(record -> {
                        System.out.println(" > > > > Partição: " + record.partition() 
                            + ", Offset: " + record.offset() 
                            + ", Timestamp: " + record.timestamp());
                    });

                // #########
                // Commit síncrono do maior offset recebido
                consumer.commitSync();
            }
        }
    }

    /**
     * Criar propriedades com base em propriedades de sistema
     */
    private static Properties criarProperties() {
        Properties props = new Properties();

        // #########
        // Deserializadores para chave e valor
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        // #########
        // Servidor Kafka
        props.put("bootstrap.servers",
            getProperty("kafka", "localhost:9092"));
        
        // #########
        // Desligar commit automático
        props.put("enable.auto.commit", false);

        // #########
        // Grupo de consumo
        props.put("group.id",
            getProperty("grupo", "kafkatrain"));
        
        // #########
        // Consumir desde o inicio, caso não exista offset p/ o Grupo de consumo
        props.put("auto.offset.reset", "earliest");

        return props;
    }
}
