package com.example.demo.Domain.Common.Daos;

import com.example.demo.Domain.Common.Dtos.ProductDTO;
import com.example.demo.Domain.Common.Mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 *  [EX] DAO 실습 (스켈레톤) — 상품(Product) 편
 * ============================================================
 *  ★ 이 DAO 는 Mapper(ProductMapper) 를 "감싸는" 계층이다.
 *     흐름 :  ProductController → ProductDAO → ProductMapper(@Mapper / XML)
 *  - 참고 : 같은 패키지의 MemoDAO.java
 *
 *  [작성 안내]
 *   EX06 각 메서드에서 productMapper 의 대응 메서드를 호출하도록 채운다.
 *   EX07 search() : keyword/minStock 를 Map 으로 묶어 searchXML 호출 (동적 SQL)
 * ============================================================
 */
@Repository
@Slf4j
public class ProductDAO {

    @Autowired
    private ProductMapper productMapper;   // [EX] DAO 가 Mapper 를 감싼다

    /* [EX06] 등록  [HINT] return productMapper.insert(dto); */
    public int insert(ProductDTO dto) {
        log.info("ProductDAO.insert..." + dto);
        // TODO: productMapper.insert 호출
        return 0;
    }

    /* [EX06] 전체 조회  [HINT] return productMapper.selectAllXML(); */
    public List<ProductDTO> selectAll() {
        log.info("ProductDAO.selectAll...");
        // TODO: productMapper.selectAllXML (또는 selectAll) 호출
        return List.of();
    }

    /* [EX06] 단건 조회  [HINT] return productMapper.selectOne(id); */
    public ProductDTO selectOne(Long id) {
        log.info("ProductDAO.selectOne... id=" + id);
        // TODO: productMapper.selectOne 호출
        return null;
    }

    /* [EX06] 수정  [HINT] return productMapper.update(dto); */
    public int update(ProductDTO dto) {
        log.info("ProductDAO.update..." + dto);
        // TODO: productMapper.update 호출
        return 0;
    }

    /* [EX06] 삭제  [HINT] return productMapper.delete(id); */
    public int delete(Long id) {
        log.info("ProductDAO.delete... id=" + id);
        // TODO: productMapper.delete 호출
        return 0;
    }

    /*
     * [EX07] 검색 (동적 SQL)
     *  - keyword, minStock 를 Map 으로 묶어 productMapper.searchXML 호출
     *  [HINT]
     *    Map<String,Object> param = new HashMap<>();
     *    param.put("keyword", keyword);
     *    param.put("minStock", minStock);
     *    return productMapper.searchXML(param);
     */
    public List<Map<String, Object>> search(String keyword, Integer minStock) {
        log.info("ProductDAO.search... keyword=" + keyword + ", minStock=" + minStock);
        // TODO: param Map 구성 후 searchXML 호출
        return List.of();
    }
}
