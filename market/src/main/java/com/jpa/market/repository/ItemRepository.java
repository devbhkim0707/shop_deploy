package com.jpa.market.repository;

import com.jpa.market.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// JpaRepository: jpql, 쿼리 메서드만 지원함
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    // 상품 이름 검색
    List<Item> findByItemName(String itemName);

    // 상품명 또는 상품상세설명으로 검색
    List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);

    @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC")
    List<Item> findByItemDetail(String itemDetail);

}
