package com.example.demo.Domain.Common.Repository.ex;

import com.example.demo.Domain.Common.Entity.ex.ExBoard;
import com.example.demo.Domain.Common.Entity.ex.ExReply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExReplyRepositoryTest {

    @Autowired
    private ExReplyRepository exReplyRepository;
    @Autowired
    private ExBoardRepository exBoardRepository;
    @Test
    public void t1(){
        // ExBoard id=1 인 게시물 Reply
        ExBoard board1 = exBoardRepository.findById(1L).get();

        exReplyRepository.save(new ExReply(null,"board id 1 의 댓글1",board1));
        exReplyRepository.save(new ExReply(null,"board id 1 의 댓글2",board1));
        exReplyRepository.save(new ExReply(null,"board id 1 의 댓글3",board1));

        // ExBoard id=2 인 게시물 Reply
        ExBoard board2 = exBoardRepository.findById(2L).get();

        exReplyRepository.save(new ExReply(null,"board id 2 의 댓글1",board2));
        exReplyRepository.save(new ExReply(null,"board id 2 의 댓글2",board2));
        exReplyRepository.save(new ExReply(null,"board id 2 의 댓글3",board2));
    }
}