package com.example.demo.Domain.Common.Service;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Dtos.PageDTO;

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

