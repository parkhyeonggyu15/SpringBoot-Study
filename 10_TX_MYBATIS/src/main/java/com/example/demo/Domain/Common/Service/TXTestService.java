package com.example.demo.Domain.Common.Service;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Mapper.MemoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class TXTestService {

    @Autowired
    private MemoMapper memoMapper;

    public void addMemo() throws Exception{
        log.info("TxTestSerivce's addMemo() invoke!!");
        MemoDTO memo = MemoDTO.builder()
                .id(null)
                .text("addMemoTx...")
                .writer("a@a.com")
                .createAt(LocalDateTime.now())
                .build();
        memoMapper.insert(memo);
        memo.setId(null);
        memoMapper.insert(memo);
        memo.setId(null);
        memoMapper.insert(memo);
        memo.setId(null);
        throw new SQLException();


    }

    @Transactional(rollbackFor = SQLException.class,transactionManager = "dataSourceTransactionManager")
    public void addMemoTx() throws Exception{
        log.info("TxTestSerivce's addMemoTx() invoke!!");
        MemoDTO memo = MemoDTO.builder()
                .id(null)
                .text("addMemoTx...")
                .writer("a@a.com")
                .createAt(LocalDateTime.now())
                .build();
        memoMapper.insert(memo);
        memo.setId(null);
        memoMapper.insert(memo);
        memo.setId(null);
        memoMapper.insert(memo);
        memo.setId(null);
        throw new SQLException();
    }
}
