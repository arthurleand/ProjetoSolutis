package com.solutis.project.config.external;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConection {
	
	private static final String EXCHANGE_NAME = "amq.direct";
	
	private AmqpAdmin amqpAdmin;
	
	public RabbitMQConection(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	} 
	
	private Queue queue(String queueName) {
		return new Queue(queueName, true, false, false);
	}
	
	private DirectExchange directExchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}
	
	private Binding binding(Queue queue, DirectExchange directExchange) {
		return new Binding(queue.getName(),
				DestinationType.QUEUE,
				directExchange.getName(), 
				queue.getName(),
				null);
	}
	
	@PostConstruct
	private void add() {
		Queue inventory = this.queue(RabbitMQConstants.INVENTORY_QUEUE);
		
		DirectExchange exchange = this.directExchange();
		
		Binding binding = this.binding(inventory, exchange);
		
		this.amqpAdmin.declareQueue(inventory);
		this.amqpAdmin.declareExchange(exchange);
		this.amqpAdmin.declareBinding(binding);
	}
}
