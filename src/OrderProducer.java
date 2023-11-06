import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class OrderProducer {
	public static void main(String[] args){
        Properties props = new Properties();
        props.setProperty("bootstrap.servers","localhost:9092");		
		props.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.setProperty("value.serializer","OrderSerializer");
		
		KafkaProducer<String, Order> producer = new KafkaProducer<String, Order>(props);
		Order order = new Order();
		order.setCustomerName("John");
		order.setProduct("IPhone");
		order.setQuantity(1);
		ProducerRecord<String, Order> record =  new ProducerRecord<>("OrderPartitionedTopic", order.getCustomerName(), order);
		
		try{
			producer.send(record);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}
	}
}