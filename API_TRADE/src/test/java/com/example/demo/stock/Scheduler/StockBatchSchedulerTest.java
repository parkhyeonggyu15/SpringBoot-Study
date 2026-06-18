package com.example.demo.stock.Scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StockBatchSchedulerTest {

    @Autowired
    private StockBatchScheduler stockBatchScheduler;
    @Test
    public void batchJob(){
        stockBatchScheduler.batchJob();
    }
}