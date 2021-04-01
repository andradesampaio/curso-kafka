package consumer;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;

/**
 * @author fabiojose
 */
public class TimestampAwareListener implements ConsumerRebalanceListener {

    private final Long timestamp;
    private final KafkaConsumer<String, String> consumer;

    public TimestampAwareListener(Long timestamp,
        KafkaConsumer<String, String> consumer) {

        this.timestamp = Objects.requireNonNull(timestamp);
        this.consumer = Objects.requireNonNull(consumer);
    }
    
    /**
     * Ajustar o offset com base no carimbo data-hora (timestamp)
     */
    private static void ajustar(Long timestamp, KafkaConsumer<String, String> consumer) {

        Map<TopicPartition, Long> timestampsToSearch =
            consumer.assignment().stream()
                .map(tp -> new AbstractMap.SimpleEntry<>(tp, timestamp))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        System.out.println(" > > > Buscar offsets p/ " + timestampsToSearch);

        Map<TopicPartition, OffsetAndTimestamp> offsets = 
            consumer.offsetsForTimes(timestampsToSearch);

        consumer.assignment().forEach(particao -> {
            long offset = 
                offsets.get(particao).offset();

            System.out.println(" > > > Offset para " + particao + ": " + offset);

            // Ajustar offset para àquele baseado no timestamp
            consumer.seek(particao, offset);
        });
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

        System.out.println(" > > Rebalaceamento . . .");

    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

        System.out.println(" > > > Consumindo: " + partitions.toString());
        
        // #########
        // Iniciar consumo com base no offset do timestamp
        ajustar(timestamp, consumer);

    }

}