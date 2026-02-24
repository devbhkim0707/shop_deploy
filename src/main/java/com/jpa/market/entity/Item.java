package com.jpa.market.entity;

import com.jpa.market.config.exception.OutOfStockException;
import com.jpa.market.constant.ItemSellStatus;
import com.jpa.market.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@ToString(exclude = "itemImgs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob        // 대용량 텍스트 파일 -> Large Object (BLOB는 LOB의 하위 개념 - binary 한정)
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<ItemImg> itemImgs = new ArrayList<>();

    // 정적 객체 생성 메서드
    public static Item createItem(ItemFormDto itemCreateDto) {
        Item item = new Item();

        item.itemName = itemCreateDto.getItemName();
        item.price = itemCreateDto.getPrice();
        item.stockNumber = itemCreateDto.getStockNumber();
        item.itemDetail = itemCreateDto.getItemDetail();
        item.itemSellStatus = itemCreateDto.getItemSellStatus();

        return item;
    }

    // 상품 수정
    public void updateItem(ItemFormDto itemFormDto) {
        // this: JPA가 DB에서 조회해온 상품 객체를 말함
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    // 재고 감소
    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;

        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }

        this.stockNumber = restStock;

        if (this.stockNumber == 0) {
            this.itemSellStatus = ItemSellStatus.SOLD_OUT;
        }
    }

    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;

        if (this.stockNumber > 0) {
            this.itemSellStatus = ItemSellStatus.SELL;
        }
    }
}
