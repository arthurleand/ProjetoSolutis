package com.solutis.project.config.external;

import org.springframework.context.annotation.Profile;

@Profile("dev")
public class RabbitMQConstants {
	public static final String INVENTORY_QUEUE ="CLOSESESSIONS";
}
