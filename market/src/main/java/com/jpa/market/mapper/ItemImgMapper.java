package com.jpa.market.mapper;

import com.jpa.market.dto.ItemImgDto;
import com.jpa.market.entity.ItemImg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemImgMapper {

    // Entity -> Dto 변환
    // 결과물타입 메서드이름(원본데이터)
    ItemImgDto entityToDto(ItemImg itemImg);

    @Mapping(target = "id", ignore = true)      // id 제외
    @Mapping(target = "item", ignore = true)    // 연관관계 -> service 에서 따로 설정
    ItemImg dtoToEntity(ItemImgDto itemImgDto);


}
