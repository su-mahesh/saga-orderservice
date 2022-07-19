/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.estore.orderservice.rest;

import java.util.UUID;
import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.estore.orderservice.command.commands.CreateOrderCommand;
import com.appsdeveloperblog.estore.orderservice.core.model.OrderStatus;

@RestController
@RequestMapping("/orders")
public class OrdersCommandController {

	private final CommandGateway commandGateway;

	// @Autowired
	public OrdersCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public String createOrder(@Valid @RequestBody OrderCreateRest order) {

		String userId = UUID.randomUUID().toString();

		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
				.addressId(order.getAddressId())
				.productId(order.getProductId())
				.userId(userId).quantity(order.getQuantity())
				.orderId(UUID.randomUUID().toString())
				.orderStatus(OrderStatus.CREATED).build();

		String result =  commandGateway.sendAndWait(createOrderCommand);
		return result;
	}

}
