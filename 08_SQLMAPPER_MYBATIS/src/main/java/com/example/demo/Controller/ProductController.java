package com.example.demo.Controller;

import com.example.demo.Domain.Common.Daos.ProductDAO;
import com.example.demo.Domain.Common.Dtos.ProductDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================
 *  [EX] MyBatis 실습 문제 — 상품(Product) 편
 * ============================================================
 *  - 각 문제의 TODO 를 채우세요. 정답 없는 스켈레톤입니다.
 *  - 흐름 : ProductController → ProductDAO → ProductMapper(@Mapper / XML)
 *  - 화면(Thymeleaf, 완성 제공) : templates/product/list.html , templates/product/add.html
 *  - 서버 포트 : 8090
 *
 *  [선행 준비]
 *   create table tbl_product(
 *     id bigint primary key, pname varchar(100), price int, stock int
 *   );
 *
 *  [문제 안내]
 *   EX01~EX05 : ProductMapper / ProductMapper.xml 작성
 *   EX06~EX07 : ProductDAO 가 Mapper 호출하도록 작성
 *   EX08      : 아래 컨트롤러 TODO (검색 목록 / 등록)
 * ============================================================
 */
@Controller
@Slf4j
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductDAO productDAO;

    /*
     * [EX08] 목록 + 검색 (GET /product/list)
     *  - productDAO.search(keyword, minStock) 결과를 model("list") 에 담는다.
     *  - 검색어 유지를 위해 keyword/minStock 도 model 에 담는다.
     */
    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer minStock,
                       Model model) {
        log.info("GET /product/list... keyword=" + keyword + ", minStock=" + minStock);
        // TODO 1: model.addAttribute("list", productDAO.search(keyword, minStock));
        // TODO 2: model.addAttribute("keyword", keyword); model.addAttribute("minStock", minStock);
        return "product/list";
    }

    /* 등록 폼 (GET /product/add) */
    @GetMapping("/add")
    public void add() {
        log.info("GET /product/add...");
    }

    /*
     * [EX08] 등록 처리 (POST /product/add)
     *  - 유효성 검증 후 productDAO.insert(dto) 호출, 성공 메시지와 함께 목록으로 redirect
     */
    @PostMapping("/add")
    public String addPost(@Valid ProductDTO productDTO, BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes) {
        log.info("POST /product/add..." + productDTO);

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "product/add";
        }

        // TODO: productDAO.insert(productDTO);
        redirectAttributes.addFlashAttribute("message", "상품추가 성공!");
        return "redirect:/product/list";
    }
}
