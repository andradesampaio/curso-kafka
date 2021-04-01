package producer;

import static java.lang.System.getProperty;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class App {

    public static void main(String[] args) throws Exception {
        
        System.out.println(" > Produtor Kafka");

        try(KafkaProducer<String, String> producer = 
                new KafkaProducer<>(criarProducerConfigs())){

            // ####
            // Tópico onde será produzido o registro
            String topicoProducer = getProperty("produzir", "topico-a");

            // ####
            // Registro que será produzido
            ProducerRecord<String, String> produce = 
                new ProducerRecord<>(topicoProducer, "Dados do evento");

            // #####
            // Produzir registro
            RecordMetadata metadata = producer.send(produce).get();

            System.out.println("\n > > Registro produzido na partição: " 
                + metadata.partition());

            System.out.println("\n > > Offset do registro............: "
                + metadata.offset());
        }
    }

    /**
     * Criar configurações com base em propriedades de sistema
     */
    private static Properties criarProducerConfigs() {
        Properties props = new Properties();

        // ####
        // Serializadores para chave e valor
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        // ####
        // Servidor Kafka
        props.put("bootstrap.servers",
            getProperty("kafka", "localhost:9092"));

        props.put("client.id", "producer");
        
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
