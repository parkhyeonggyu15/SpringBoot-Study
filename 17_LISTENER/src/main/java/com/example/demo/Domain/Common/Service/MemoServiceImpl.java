package com.example.demo.Domain.Common.Service;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Dtos.PageBlock;
import com.example.demo.Domain.Common.Dtos.PageDTO;
import com.example.demo.Domain.Common.Entity.Memo;
import com.example.demo.Domain.Common.Repository.MemoRepository;
import com.example.demo.Listener.MemoAddEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MemoServiceImpl implements MemoService {
    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    //메모등록
    @Override
    @Transactional(rollbackFor = SQLException.class,transactionManager = "jpaTransactionManager")
    public boolean memoRegistration(MemoDTO memoDTO) throws Exception{
        memoDTO.setCreateAt(LocalDateTime.now());
        memoRepository.save(memoDTO.toEntity());
        applicationEventPublisher.publishEvent(new MemoAddEvent(this,memoDTO));
        return true;
    }

    @Transactional(rollbackFor = SQLException.class,transactionManager = "jpaTransactionManager")
    public Map<String,Object> getMemoList(PageDTO pageDTO) throws Exception{

        Map<String,Object> returnValue = new HashMap<>();

        int pageNo = 0;     //현재 pageNo
        int amount = 10;    //한페이지 표시할 게시물 건수
        if(pageDTO.getPageNo()!=null)
            pageNo = pageDTO.getPageNo();
        else
            pageDTO.setPageNo(0);

        if(pageDTO.getAmount()!=null)
            amount = pageDTO.getAmount();
        else
            pageDTO.setAmount(10);

        Pageable pageable = PageRequest.of(pageNo,amount, Sort.by("id").descending());
        Page<Memo> page =  memoRepository.findAll(pageable);
        PageBlock pageBlock = new PageBlock(pageDTO,page);
        List<Memo> list = page.getContent();

        returnValue.put("page",page);
        returnValue.put("pageBlock",pageBlock);
        returnValue.put("list",list);

        return returnValue;
    }



    @Override
    @Transactional(rollbackFor = SQLException.class,transactionManager = "jpaTransactionManager")
    public MemoDTO getMemo(Long id) throws Exception {
        Optional<Memo> memoOptional = memoRepository.findById(id);
        if(memoOptional.isPresent()){
            return MemoDTO.from(memoOptional.get());
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = SQLException.class,transactionManager = "jpaTransactionManager")
    public boolean updateMemo(MemoDTO dto) throws Exception {

        memoRepository.save(dto.toEntity());

        return dto.getId()>0;
    }

    @Override
    @Transactional(rollbackFor = SQLException.class,transactionManager = "jpaTransactionManager")
    public boolean removeMemo(Long id) throws Exception {
         memoRepository.deleteById(id);

        return true;
    }


}
