
## 1. Source Code.

**Order.java**

```java
public class Order {
    private String customerName;
    private String product;
    private int quantity;

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product){
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
```

**OrderSerializer.java**

```java
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderSerializer implements Serializer<Order> {

    @Override
    public byte[] serialize(String topic, Order order) {
        byte[] response = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.writeValueAsString(order).getBytes();  
        } catch (JsonProcessingException e) {
            e.printStackTrace();  
        }
        return response;
    }
}
```

**OrderProducer.java**

```java
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
```

## 2. How to compile and run the application.

### 2.1. Run the following command to execute zookeeper

```
zookeeper-server-start C:\kafka_2.13-3.6.0\config\zookeeper.properties
```

### 2.1. Run the following command to start kafka-server

```
kafka-server-start C:\kafka_2.13-3.6.0\config\server.properties
```

### 2.2. Compiling the application

```
javac -cp ".;lib/*" src/Order.java src/OrderProducer.java src/OrderSerializer.java
```

### 2.3. Run the command for executing the application

```
java -cp ".;lib/*;src" OrderProducer
```
