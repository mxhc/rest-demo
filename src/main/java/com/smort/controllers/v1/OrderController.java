package com.smort.controllers.v1;

import com.smort.api.v1.model.OrderDTO;
import com.smort.api.v1.model.OrderListDTO;
import com.smort.services.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(description = "Orders controller")
@Validated
@RequestMapping(OrderController.BASE_URL)
@RestController
public class OrderController {

    public static final String BASE_URL = "/api/v1/orders";

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "${controller.order.get.list}", notes = "${controller.order.get.list.notes}")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderListDTO getAllOrders() {   //todo display by order status
        return new OrderListDTO(orderService.getAllOrders());
    }

    @ApiOperation(value = "${controller.order.post}")
    @PostMapping("/orders/customers/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO createNewOrder(@PathVariable Long customerId) {
        return orderService.createNewOrder(customerId);
    }

}
