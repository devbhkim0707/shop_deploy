//package com.jpa.market;
//
//import com.jpa.market.constant.ItemSellStatus;
//import com.jpa.market.dto.ItemFormDto;
//import com.jpa.market.dto.MemberJoinDto;
//import com.jpa.market.entity.Item;
//import com.jpa.market.entity.Member;
//import com.jpa.market.entity.Order;
//import com.jpa.market.entity.OrderItem;
//import com.jpa.market.repository.ItemRepository;
//import com.jpa.market.repository.MemberRepository;
//import com.jpa.market.repository.OrderItemRepository;
//import com.jpa.market.repository.OrderRepository;
//import com.jpa.market.service.MemberService;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.persistence.PersistenceContext;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
//public class OrderTest {
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Autowired
//    OrderItemRepository orderItemRepository;
//
//    @Autowired
//    ItemRepository itemRepository;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @PersistenceContext
//    EntityManager em;
//
//    public Item createItem() {
//        ItemFormDto dto = new ItemFormDto();
//
//        dto.setItemName("테스트 상품");
//        dto.setPrice(10000);
//        dto.setStockNumber(10);
//        dto.setItemDetail("상품 상세 설명");
//        dto.setItemSellStatus(ItemSellStatus.SELL);
//
//        return Item.createItem(dto);
//    }
//
//    public MemberJoinDto createMember() {
//        MemberJoinDto dto = new MemberJoinDto();
//
//        dto.setLoginId("java12");
//        dto.setPassword("1234");
//        dto.setName("김자바");
//        dto.setEmail("java@naver.com");
//        dto.setAddress("부산시 연제구");
//
//        return dto;
//    }
//
//    public Member saveMember() {
//        MemberJoinDto dto = createMember();
//
//        Member member = Member.createMember(dto, passwordEncoder);
//
//        return memberRepository.save(member);
//    }
//
//    public Order createOrder() {
//        Member member = this.saveMember();
//
//        Order order = Order.createOrder(member);
//
//        for (int i = 0; i < 3; i++) {
//            Item item = this.createItem();
//            itemRepository.save(item);
//
//            OrderItem orderItem = OrderItem.createOrderItem(item, 10);
//
////            order.getOrderItems().add(orderItem);
//            order.addOrderItem(orderItem);
//        }
//
//        return orderRepository.save(order);
//    }
//
//    @Test
//    public void orphanRemovalTest() {
//        Order order = this.createOrder();
//
//        order.getOrderItems().remove(0);
//        em.flush();
//
//    }
//
//    @Test
//    public void cascadeTest() {
//        Order order = Order.createOrder(null);
//
//        for (int i = 0; i < 3; i++) {
//            Item item = this.createItem();
//            itemRepository.save(item);
//
//            OrderItem orderItem = OrderItem.createOrderItem(item, 10);
//
////            order.getOrderItems().add(orderItem);
//            order.addOrderItem(orderItem);
//        }
//
////        orderRepository.save(order);
////        em.flush();
//        orderRepository.saveAndFlush(order);
//
//        em.clear();
//
//        Order savedOrder = orderRepository.findById(order.getId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        assertThat(3).isEqualTo(savedOrder.getOrderItems().size());
//    }
//
//    @Test
//    public void lazyLoadingTest() {
//        Order order = this.createOrder();
//        Long orderItemId = order.getOrderItems().get(0).getId();
//        em.flush();
//        em.clear();
//
//        OrderItem orderItem = orderItemRepository.findById(orderItemId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        System.out.println("order class: " + orderItem.getOrder().getClass());
//        System.out.println("------------");
//
//        orderItem.getOrder().getOrderDate();
//        System.out.println("order class: " + orderItem.getOrder().getClass());
//        System.out.println("order date: " + orderItem.getOrder().getOrderDate());
//    }
//
//
//
//}
