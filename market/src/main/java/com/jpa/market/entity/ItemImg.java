package com.jpa.market.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString(exclude = "item")
@Table(name = "item_img")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder        // 객체 생성용 패턴
@AllArgsConstructor
public class ItemImg extends BaseEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    @Column(length = 1)
    private String repImgYn;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    // ItemImg에는 create static 메서드를 사용하지 않음
    // 1. Item에 종속된 엔티티
    // 2. 엔티티를 생성할 때 검증되어야 하는 규칙이 없음
    
    public void updateItemImg(String imgName, String oriImgName, String imgUrl, String repImgYn) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repImgYn = repImgYn;

    }

}
