package com.jpa.market.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@Getter
@ToString(exclude = "order") // 양방향 매핑에서 서로 호출하다 무한루프가 발생할 수 있으므로 연관 필드를 제외
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class OrderItem extends BaseEntity {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;

    private int count;

    // DB에 order에 대한 정보를 전달하기 위한 메서드
    // setter가 아닌 연관관계 설정 메서드
    public void setOrder(Order order) {
        this.order = order;
    }

    public static OrderItem createOrderItem(
            Item item,
            int count
    ) {
        if (count < 1)
            throw new IllegalArgumentException("주문 수량은 1 이상이어야 합니다.");

        OrderItem orderItem = new OrderItem();
        orderItem.item = item;
        orderItem.orderPrice = item.getPrice() * count;
        orderItem.count = count;

        item.removeStock(count);

        return orderItem;
    }

    public void cancelOrderItem(
    ) {
        // 주문 취소 시 상품 재고 복구하기
        this.getItem().addStock(count);


    }
}
