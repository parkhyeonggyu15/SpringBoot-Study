package com.example.demo.Domain.Common.Mapper;

import com.example.demo.Domain.Common.Dtos.ProductDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * ============================================================
 *  [EX] MyBatis Mapper 실습 (스켈레톤) — 상품(Product) 편
 * ============================================================
 *  - DB : MySQL testdb, 테이블 tbl_product(id, pname, price, stock)
 *  - 참고 : 같은 패키지의 MemoMapper.java (완성 예시)
 *  - XML 매퍼 위치 : src/main/resources/mapper/ProductMapper.xml
 *
 *  [작성 안내]
 *   EX01 @Insert         : 어노테이션 SQL 로 INSERT
 *   EX02 @Select         : 전체 조회 / 단건 조회(#{id})
 *   EX03 @Update/@Delete : 수정 / 삭제
 *   EX04 XML 매퍼        : selectAllXML 를 XML 에 작성
 *   EX05 동적 SQL        : <if>/<where> 로 검색 (XML) - 상품명 LIKE + 재고 이상
 * ============================================================
 */
@Mapper
public interface ProductMapper {

    // [EX01] INSERT (어노테이션)
    //  [HINT] @Insert("insert into tbl_product values(#{id},#{pname},#{price},#{stock})")
    // TODO: @Insert(...) 작성
    int insert(ProductDTO dto);

    // [EX02] 전체 조회 (어노테이션)
    //  [HINT] @Select("select * from tbl_product")
    // TODO: @Select(...) 작성
    List<ProductDTO> selectAll();

    // [EX02] 단건 조회 (어노테이션)
    //  [HINT] @Select("select * from tbl_product where id=#{id}")
    // TODO: @Select(...) 작성
    ProductDTO selectOne(Long id);

    // [EX03] 수정 (어노테이션)
    //  [HINT] @Update("update tbl_product set pname=#{pname},price=#{price},stock=#{stock} where id=#{id}")
    // TODO: @Update(...) 작성
    int update(ProductDTO dto);

    // [EX03] 삭제 (어노테이션)
    //  [HINT] @Delete("delete from tbl_product where id=#{id}")
    // TODO: @Delete(...) 작성
    int delete(Long id);


    // ===================== XML 매퍼 =====================

    // [EX04] XML 로 전체 조회 (ProductMapper.xml 에 <select id="selectAllXML">)
    List<ProductDTO> selectAllXML();

    // [EX05] 동적 SQL 검색 (XML)
    //  - param(Map) keys : keyword(상품명 LIKE), minStock(재고 이상)
    //  - 조건이 없으면 전체조회가 되도록 <where>/<if> 사용
    List<Map<String, Object>> searchXML(Map<String, Object> param);
}
