package consumer;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

/**
 * @author fabiojose
 */
public class SimpleListener implements ConsumerRebalanceListener {

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

        System.out.println(" > > Rebalaceamento . . .");

    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

        System.out.println(" > > > Consumindo: " + partitions.toString());

    }

}