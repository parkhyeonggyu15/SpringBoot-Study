package com.example.demo.Domain.Common.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [EX] 상품(Product) DTO — 완성 제공 (수정 불필요)
 *  테이블 tbl_product(id, pname, price, stock) 과 1:1 매핑된다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;

    @NotBlank(message = "상품명은 필수 항목입니다.")
    private String pname;

    @NotNull(message = "가격은 필수 항목입니다.")
    private Integer price;

    @NotNull(message = "재고는 필수 항목입니다.")
    private Integer stock;
}
