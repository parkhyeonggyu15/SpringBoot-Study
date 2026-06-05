package com.example.demo.Domain.Common.Dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageBlock<E> {

    private PageDTO pageDto;
    private Page<E> page ;

    //블럭정보
    private int pagePerBlock;		//블럭에 표시할 페이지개수(15건 지정)
    private int totalBlock;			//totalpage / pagePerBlock
    private int nowBlock;			//현재페이지번호 /pagePerBlock

    //표시할 페이지(블럭에표시할 시작페이지-마지막페이지)
    private int startPage;
    private int endPage;

    //NextPrev 버튼 표시여부
    private boolean prev,next;

    public PageBlock(PageDTO pageDto , Page<E> page){
        this.pageDto = pageDto;
        this.page = page;
        long totalCount = page.getTotalElements(); // memoRepository.count();
        //전체페이지 계산
        int totalpage = page.getTotalPages();//memoRepository.count() / 10 ;
        //블럭계산
        pagePerBlock=15;
        totalBlock = (int)Math.ceil( (1.0*totalpage) / pagePerBlock );
        nowBlock =  (int)Math.ceil ((1.0*(pageDto.getPageNo()+1)) / pagePerBlock);
        //Next,Prev 버튼 활성화 유무
        prev=nowBlock>1;
        next=nowBlock<totalBlock;
        
        //블럭에 표시할 페이지 번호 계산
        this.endPage = (nowBlock * pagePerBlock < totalpage) ? nowBlock * pagePerBlock : totalpage ;
        this.startPage=nowBlock * pagePerBlock - pagePerBlock + 1;
        
        // startPage가 0 부터라서 1씩 누적감소
        this.endPage+=-1;
        this.startPage+=-1;
    }
}
