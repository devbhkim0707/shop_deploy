package com.jpa.market.repository;


import com.jpa.market.dto.ItemAdminListDto;
import com.jpa.market.dto.ItemSearchDto;
import com.jpa.market.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    // Pageable: JPA에서 제공하는 페이징과 정렬에 대한 정보가 담겨있는 인터페이스
    Page<ItemAdminListDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
