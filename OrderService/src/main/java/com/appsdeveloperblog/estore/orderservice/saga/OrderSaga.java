package com.appsdeveloperblog.estore.orderservice.saga;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.orderservice.core.events.OrderCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class OrderSaga {

	@Autowired
	private CommandGateway commandGateway;
	
	public OrderSaga() {
		
	}

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {

		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
				.orderId(orderCreatedEvent.getOrderId()).productId(orderCreatedEvent.getProductId())
				.quantity(orderCreatedEvent.getQuantity()).userId(orderCreatedEvent.getUserId()).build();
		
		log.info("OrderSaga OrderCreatedEvent for order id "+orderCreatedEvent.getOrderId());
		
		commandGateway.send(reserveProductCommand);
		/*
		 * commandGateway.send(reserveProductCommand, new
		 * CommandCallback<ReserveProductCommand, Object>() {
		 * 
		 * @Override public void onResult(CommandMessage<? extends
		 * ReserveProductCommand> commandMessage, CommandResultMessage<? extends Object>
		 * commandResultMessage) {
		 * 
		 * if (commandResultMessage.isExceptional()) { // compensating transaction
		 * log.info("OrderSaga OrderCreatedEvent Exception "+commandResultMessage.
		 * exceptionResult()); }
		 * 
		 * } });
		 */
	}

	@SagaEventHandler(associationProperty = "productId")
	public void on(ProductReservedEvent productReservedEvent) {
		log.info("OrderSaga ProductReservedEvent order id "+productReservedEvent.getOrderId());
	}
}
