package com.jpa.market.entity;

import com.jpa.market.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@ToString(exclude = "orderItems")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @Column(name = "orders_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // 양방향 매핑
    // 주인이 아닌 Order에서도 OrderItem을 조회할 수 있도록 설정
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    // java 객체를 위해 필요한 메서드
    public void addOrderItem(OrderItem orderItem) {
        // order가 연관관계로 매핑되어있는 orderItem을 알게하기 위함
        this.orderItems.add(orderItem);
        // OrderItem이 부모인 order를 알게하기 위해서 사용
        orderItem.setOrder(this);
    }

    public static Order createOrder(
            Member member,
            List<OrderItem> orderItemList
    ) {
        Order order = new Order();

        for (OrderItem orderItem: orderItemList)
            order.addOrderItem(orderItem);

        order.member = member;
        order.orderStatus = OrderStatus.ORDER;
        order.orderDate = LocalDateTime.now();

        return order;
    }

    // 전체 주문금액 계산
    public int getTotalPrice() {

        int totalPrice = 0;

        for (OrderItem orderItem: orderItems) {
            totalPrice += orderItem.getOrderPrice();
        }

        return totalPrice;
    }

    // 주문 취소
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem: orderItems) {
            orderItem.cancelOrderItem();
        }
    }
}
