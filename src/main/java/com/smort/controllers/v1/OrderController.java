package com.smort.controllers.v1;

import com.smort.api.v1.model.OrderDTO;
import com.smort.api.v1.model.OrderItemDTO;
import com.smort.api.v1.model.OrderItemListDTO;
import com.smort.api.v1.model.OrderListDTO;
import com.smort.domain.OrderStatus;
import com.smort.services.OrderService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public OrderListDTO getAllOrders(@RequestParam(required = false) @ApiParam(value = "${controller.order.get.list.param.state}") OrderStatus state) {
        return new OrderListDTO(orderService.getAllOrders(state));
    }

    @ApiOperation(value = "${controller.order.post}")
    @PostMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO createNewOrder(@PathVariable Long userId) {
        return orderService.createNewOrder(userId);
    }

    @ApiOperation(value = "${controller.order.customer.get}", notes = "${controller.order.customer.get.notes}")
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderListDTO getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    @ApiOperation(value = "${controller.order.delete}")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @ApiOperation(value = "${controller.order.get}", notes = "${controller.order.get.notes}")
    @GetMapping("/{orderId}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @ApiOperation(value = "${controller.order.post.item}", notes = "${controller.order.post.item.notes}")
    @PostMapping("/{orderId}/items")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public OrderItemDTO addItemToOrder(@PathVariable Long orderId, @RequestBody @Valid OrderItemDTO orderItemDTO) {
        return orderService.addItemToOrder(orderId, orderItemDTO);
    }

    @ApiOperation(value = "${controller.order.get.items}", notes = "${controller.order.get.items.notes}")
    @GetMapping("/{orderId}/items")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public OrderItemListDTO getListOfItems(@PathVariable Long orderId) {
        return orderService.getListOfItems(orderId);
    }

    @ApiOperation(value = "${controller.order.action.purchase}")
    @PostMapping("/{orderId}/actions/purchase")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO purchase(@PathVariable Long orderId) {
        return orderService.purchaseAction(orderId);
    }

    @ApiOperation(value = "${controller.order.action.cancel}")
    @PostMapping("/{orderId}/actions/cancel")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO cancel(@PathVariable Long orderId) {
        return orderService.cancelAction(orderId);
    }

    @ApiOperation(value = "${controller.order.action.deliver}")
    @PostMapping("/{orderId}/actions/deliver")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO deliver(@PathVariable Long orderId) {
        return orderService.deliverAction(orderId);
    }

    @ApiOperation(value = "${controller.order.get.order.item}")
    @GetMapping("/{oid}/items/{iid}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public OrderItemDTO getItemFromOrder(@PathVariable Long oid, @PathVariable Long iid) {
        return orderService.getItemFromOrder(oid, iid);
    }

    @ApiOperation(value = "${controller.order.delete.order.item}")
    @DeleteMapping("/{oid}/items/{iid}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteItemFromOrder(@PathVariable Long oid, @PathVariable Long iid) {
        orderService.deleteItemFromOrder(oid, iid);
    }
}
