package com.jpa.market.dto;

import com.jpa.market.constant.ItemSellStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemAdminListDto {

    private Long id;
    private String itemName;
    private ItemSellStatus itemSellStatus;
    private String createdBy;
    private LocalDateTime regTime;

    @QueryProjection
    public ItemAdminListDto(
            Long id,
            String itemName,
            ItemSellStatus itemSellStatus,
            String createdBy,
            LocalDateTime regTime
    ) {
        this.id = id;
        this.itemName = itemName;
        this.itemSellStatus = itemSellStatus;
        this.createdBy = createdBy;
        this.regTime = regTime;
    }
}
