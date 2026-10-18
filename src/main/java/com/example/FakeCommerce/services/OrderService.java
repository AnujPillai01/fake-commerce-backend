package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.OrderAdapter;
import com.example.FakeCommerce.dtos.*;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.OrderItemRepository;
import com.example.FakeCommerce.repositories.OrderRepository;
import com.example.FakeCommerce.repositories.ProductRepository;
import com.example.FakeCommerce.schema.Order;
import com.example.FakeCommerce.schema.OrderItem;
import com.example.FakeCommerce.schema.OrderStatus;
import com.example.FakeCommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderAdapter orderAdapter;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public List<GetOrderResponseDto> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        return orderAdapter.mapToGetOrderResponseDtoList(orders);
    }

    public GetOrderResponseDto getOrderById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found"));

        return orderAdapter.mapToGetOrderResponseDto(order);
    }

    public void deleteOrder(Long id){
        orderRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found"));
        orderRepository.deleteById(id);
    }

    @Transactional
    public GetOrderResponseDto createOrder(CreateOrderRequestDto requestDto){
      Order order = Order.builder().status(OrderStatus.PENDING).build();
      orderRepository.save(order);

      if(requestDto.getItems() != null){
          List<Long> productIds = requestDto.getItems().stream()
                  .map(CreateOrderItemRequestDto::getProductId).toList();

          List<Product> products = productRepository.findAllById(productIds);

          Map<Long, Product> productMap = products.stream()
                  .collect(Collectors.toMap(Product::getId, Function.identity()));

          for(Long id: productIds){
              if(!productMap.containsKey(id)){
                  throw new ResourceNotFoundException("Product not found with id: " + id);
              }
          }

          List<OrderItem> orderItems = new ArrayList<>();

          for(var item: requestDto.getItems()){
              orderItems.add(OrderItem.builder()
                      .order(order)
                      .product(productMap.get(item.getProductId()))
                      .quantity(item.getQuantity() != null ?  item.getQuantity() : 1)
                      .build());
          }

          orderItemRepository.saveAll(orderItems);
      }
        return orderAdapter.mapToGetOrderResponseDto(order);
    }

    @Transactional
    public GetOrderResponseDto updateOrder(Long id, UpdateOrderRequestDto requestDto){
       Order order = orderRepository.findById(id)
               .orElseThrow(()-> new ResourceNotFoundException("Order not found with id: " + id));

       if(requestDto.getOrderStatus()!=null){
           order.setStatus(requestDto.getOrderStatus());
           orderRepository.save(order);
       }

       if(requestDto.getOrderItemActions()!=null){
           List<Long> productIds = requestDto.getOrderItemActions().stream()
                   .map(OrderItemActionDto::getProductId).toList();

           List<Product> products = productRepository.findAllById(productIds);

           Map<Long, Product> productMap = products.stream()
                   .collect(Collectors.toMap(Product::getId, Function.identity()));

           for(Long pid: productIds){
               if(!productMap.containsKey(pid)){
                   throw new ResourceNotFoundException("Product not found with id: " + pid);
               }
           }

           List<OrderItem> toSave = new ArrayList<>();
           List<OrderItem> toDelete = new ArrayList<>();

           Map<Long, OrderItem> existingItems = orderItemRepository.findByOrderWithProduct(order)
                   .stream().collect(Collectors.toMap(item->item.getProduct().getId(), Function.identity()));

           for(OrderItemActionDto itemAction:  requestDto.getOrderItemActions()){
               Product product = productMap.get(itemAction.getProductId());
               OrderItem existingItem = existingItems.get(product.getId());

               switch(itemAction.getAction()){
                   case ADD -> {
                       if (existingItem != null) {
                           int addQty = (itemAction.getQuantity() != null ? itemAction.getQuantity() : 1);
                           existingItem.setQuantity(existingItem.getQuantity() + addQty);
                           toSave.add(existingItem);
                       } else {
                           OrderItem newItem = OrderItem
                                   .builder()
                                   .order(order)
                                   .product(product)
                                   .quantity(itemAction.getQuantity() != null ? itemAction.getQuantity() : 1)
                                   .build();

                           existingItems.put(product.getId(), newItem);
                           toSave.add(newItem);
                       }
                   }

                   case REMOVE -> {
                       if(existingItem == null) {
                           throw new ResourceNotFoundException("Product not found with id: " + itemAction.getProductId());
                       }else {
                           toDelete.add(existingItem);
                           existingItems.remove(product.getId());
                       }
                   }
                   case INCREMENT -> {
                       if(existingItem == null) {
                           throw new ResourceNotFoundException("Product not found with id: " + itemAction.getProductId());
                       }
                       existingItem.setQuantity(existingItem.getQuantity() + 1);
                       toSave.add(existingItem);
                   }
                   case DECREMENT -> {
                       if(existingItem == null) {
                           throw new ResourceNotFoundException("Product not found with id: " + itemAction.getProductId());
                       }

                       if(existingItem.getQuantity() <= 1) {
                           toDelete.add(existingItem);
                           existingItems.remove(product.getId());
                       }else {
                           existingItem.setQuantity(existingItem.getQuantity() - 1);
                           toSave.add(existingItem);
                       }
                   }
               }

           }

           if(!toSave.isEmpty()){
               orderItemRepository.saveAll(toSave);
           }

           if(!toDelete.isEmpty()){
               orderItemRepository.deleteAll(toDelete);
           }
       }
       return orderAdapter.mapToGetOrderResponseDto(order);
    }

    public GetOrderSummaryResponseDto getOrderSummary(Long id){
      Order order = orderRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

      List<OrderItem> orderItems = orderItemRepository.findByOrderWithProduct(order);

      List<OrderItemResponseDto> responseDto = orderAdapter.mapToOrderItemResponseDto(orderItems);

      int totalItems = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();

      BigDecimal totalPrice = orderItems.stream().map(op->op.getProduct().getPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
              .reduce(BigDecimal.ZERO,BigDecimal::add);

      return GetOrderSummaryResponseDto
              .builder()
              .id(order.getId())
              .items(responseDto)
              .status(order.getStatus())
              .totalPrice(totalPrice)
              .totalItems(totalItems)
              .createdAt(order.getCreatedAt())
              .updatedAt(order.getUpdatedAt())
              .build();
    }
}


