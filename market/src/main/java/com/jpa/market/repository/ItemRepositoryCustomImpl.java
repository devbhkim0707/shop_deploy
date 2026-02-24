package com.jpa.market.repository;

import com.jpa.market.constant.ItemSellStatus;
import com.jpa.market.dto.*;
import com.jpa.market.entity.QItem;
import com.jpa.market.entity.QItemImg;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

// customRepository를 구현한 클래스를 사용할때는
// 클래스명이 반드시 Impl 로 끝나야 함
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq (ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDatesAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (Objects.equals("all", searchDateType) || searchDateType == null)
            return null;
        else if (Objects.equals("1d", searchDateType))
            dateTime = dateTime.minusDays(1);
        else if (Objects.equals("1w", searchDateType))
            dateTime = dateTime.minusWeeks(1);
        else if (Objects.equals("1m", searchDateType))
            dateTime = dateTime.minusMonths(1);
        else if (Objects.equals("6m", searchDateType))
            dateTime = dateTime.minusMonths(6);

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty())
            return null;

        if (Objects.equals("itemName", searchBy))
            return QItem.item.itemName.contains(searchQuery);
        else if (Objects.equals("createdBy", searchBy))
            return QItem.item.createdBy.contains(searchQuery);

        return null;
    }

    @Override
    public Page<ItemAdminListDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        // QueryDsl 시작부분
        List<ItemAdminListDto> content = queryFactory
                // 필요한 자료만 조회하도록 엔티티가 아니라 Dto로 조회
                .select(new QItemAdminListDto(
                        QItem.item.id,
                        QItem.item.itemName,
                        QItem.item.itemSellStatus,
                        QItem.item.createdBy,
                        QItem.item.regTime
                ))
                .from(QItem.item)
                .where(
                        regDatesAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
                )
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(QItem.item.count())
                .from(QItem.item)
                .where(
                        regDatesAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(new QMainItemDto(
                        item.id,
                        item.itemName,
                        item.itemDetail,
                        itemImg.imgUrl,
                        item.price
                ))
                .from(itemImg)    // 더 강력한 조건을 가지고있는 엔티티를 작성
                .join(itemImg.item, item)                // 연관관계가 있으면 ON 조건은 자동으로 생성
                .where(itemImg.repImgYn.eq("Y"))    // .join (연관관계 필드, 조인될 엔티티 별칭)
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(item.count())
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNameLike(String searchQuery) {

        if (searchQuery == null || searchQuery.isEmpty())
            return null;

        return QItem.item.itemName.contains(searchQuery);
    }
}
