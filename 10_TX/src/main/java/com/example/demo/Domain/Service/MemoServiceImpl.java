package com.example.demo.Domain.Service;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemoService {
    @Autowired
    private MemoRepository memoRepository;

    //메모등록
    public boolean memoRegistration(MemoDTO memoDTO){

        memoRepository.save();
        return false;
    }

    //DTOtoEntity

    //EntitytoDTO
}
