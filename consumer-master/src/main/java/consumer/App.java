package consumer;

import static java.lang.System.getProperty;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.StreamSupport;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class App {
    public static void main(String[] args) {
        
        System.out.println(" > Consumidor java");

        try(KafkaConsumer<String, String> consumer = 
                new KafkaConsumer<>(criarProperties())){

            // #########
            // Tópico
            consumer.subscribe(
                Arrays.asList(getProperty("topico", "topico-a")),
                new SimpleListener());

            // Poll timeout
            long pollTimeoutMillis = 
                Long.parseLong(getProperty("poll", "5000"));
            
            // Poll loop
            while(true){
                ConsumerRecords<String, String> records = 
                    consumer.poll(Duration.ofMillis(pollTimeoutMillis));

                System.out.println(" > > # registros consumidos: " 
                    + records.count());

                if(records.count() > 0){
                    // Total de bytes consumidos
                    int bytes = 
                        StreamSupport
                            .stream(records.spliterator(), false)
                            .map(ConsumerRecord::serializedValueSize)
                            .reduce(0, (total, r) -> total + r);

                    System.out.println(" > > >  bytes: " + bytes);
                    System.out.println(" > > > Kbytes: " + bytes / 1024);
                    System.out.println(" > > > Mbytes: " + bytes / 1024 / 1024);
                }
                
                // Processar os registros
                records
                    .forEach(record -> {
                        System.out.println(" > > Partição: " + record.partition() 
                            + ", Offset: " + record.offset() 
                            + ", valor: " + record.value());
                    });

                // #########
                // Commit do maior offset recebido
                consumer.commitSync();
            }
        }
    }

    /**
     * Criar configurações com base em propriedades de sistema
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

        // Configurações adicionais
        String cfgs = getProperty("cfg");
        if(null!= cfgs){
            for(String cfg : cfgs.split(",")) {
                String[] kv = cfg.trim().split("=");
                if(kv.length == 2) {
                    System.out.println(" > > cfg " + kv[0] + "=" + kv[1]);
                    props.put(kv[0], kv[1]);
                } else {
                    throw new IllegalArgumentException(
                        "Configurações incorretas: " + cfgs);
                }
            }
        }
        return props;
    }
}
