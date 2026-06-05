package com.example.demo.Domain.Service;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Dtos.PageBlock;
import com.example.demo.Domain.Common.Dtos.PageDTO;
import com.example.demo.Domain.Common.Entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface MemoService {
    //메모등록
    boolean memoRegistration(MemoDTO memoDTO) throws Exception;

    //메모리스트
    public Map<String,Object> getMemoList(PageDTO pageDTO) throws Exception;


    MemoDTO getMemo(Long id) throws Exception;

    boolean updateMemo(MemoDTO dto) throws Exception;

    boolean removeMemo(Long id) throws Exception;
}
