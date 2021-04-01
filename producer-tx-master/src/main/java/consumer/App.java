package consumer;

import static java.lang.System.getProperty;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class App {

    private static final String GROUP_ID = "kafkatrain";

    public static void main(String[] args) {
        
        System.out.println(" > Transações com Kafka");

        try(KafkaProducer<String, String> producer = 
                new KafkaProducer<>(criarProducerConfigs());
            
            KafkaConsumer<String, String> consumer = 
                new KafkaConsumer<>(criarConsumerConfigs())){
            
            // #########
            // Iniciar contexto transacional
            producer.initTransactions();

            // Tópico que será consumido
            String topicoConsumer = getProperty("consumir", "topico-a-in");

            // Tópico onde serão produzidos registros
            String topicoProducer = getProperty("produzir", "topico-a-out");

            // Consumir registros
            consumer.subscribe(Collections
                .singleton(topicoConsumer), new SimpleListener());
            
            // Poll loop
            while(true){
                ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofSeconds(5));

                if (!records.isEmpty()) {
                    System.out.println(">> Processar " + records.count() 
                        + " registro(s)");

                    try {
                        // #####
                        // Iniciar uma transação
                        producer.beginTransaction();

                        // Processar registros
                        records.forEach(r -> {
                            System.out.println(">> Processado offset: " + r.offset());

                            ProducerRecord<String, String> produce = 
                                new ProducerRecord<>(topicoProducer, r.value());

                            // #####
                            // Produzir registro transacional
                            producer.send(produce);
                        });

                        // #########
                        // Confirmar offsets processados utilizando a transação
                        producer.sendOffsetsToTransaction(
                                offsetsProcessados(records),
                                    GROUP_ID);

                        // #########
                        // Confirmar contexto transacional: 
                        //    confirmando offsets e registros produzidos
                        producer.commitTransaction();

                    }catch(ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e){
                        e.printStackTrace();

                        // ####
                        // Não é possível recuperar os erros capturados
                        System.exit(1);
                    }catch(KafkaException e) {
                        e.printStackTrace();
                        // #####
                        // Abortar transação
                        producer.abortTransaction();
                    }
                }

                producer.metrics().entrySet().stream()
                    .filter(kv -> kv.getKey().name().equals("record-send-rate"))
                    .forEach(kv -> {
                        System.out.println(kv.getKey() + "=" + kv.getValue().metricValue());
                    });
            }
        }
    }

    /**
     * Criar configurações com base em propriedades de sistema
     */
    private static Properties criarProducerConfigs() {
        Properties props = new Properties();

        props.put("acks", "all");
        props.put("enable.idempotence", "true");

        // #########
        // Identificador único e global da transação
        props.put("transactional.id", App.class.getCanonicalName() + "-1");

        // Serializadores para chave e valor
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

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

    private static Properties criarConsumerConfigs() {
        Properties props = new Properties();

        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        props.put("bootstrap.servers",
            getProperty("kafka", "localhost:9092"));

        props.put("isolation.level", "read_committed");
        props.put("enable.auto.commit", false);
        props.put("group.id", "kafkatrain");

        props.put("auto.offset.reset", "earliest");

        props.put("client.id", "consumer");

        return props;
    }

    private static Map<TopicPartition, OffsetAndMetadata> offsetsProcessados(
            ConsumerRecords<String, String> records) {

        Map<TopicPartition, OffsetAndMetadata> result = new HashMap<>();

        records.partitions().forEach(tp -> {
            List<ConsumerRecord<String, String>> pRecods = records.records(tp);
            long offset = pRecods.get(pRecods.size() -1).offset();

            result.put(tp, new OffsetAndMetadata(offset +1));
        });

        return result;
    }
}
