package com.example.demo.Domain.Common.Mapper;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoMapperTest {
    @Autowired
    private MemoMapper memoMapper;

    @Test
    public void t1(){
        memoMapper.insert(new MemoDTO(55L,"TITLE55","a@a.com","text55", LocalDateTime.now()));
    }
    @Test
    public void t2(){
        memoMapper.update(new MemoDTO(55L,null,null,"text55!!!!", null));
    }
    @Test
    public void t3(){
        memoMapper.delete(55L);
    }

    @Test
    public void t4(){
       List<MemoDTO> list  = memoMapper.selectALL();
       list.forEach(System.out::println);
    }

    @Test
    public void t5(){
        MemoDTO dto = new MemoDTO(null,"TITLE55","a@a.com","text55", LocalDateTime.now());
        System.out.println(dto);
        memoMapper.insert(dto);
        System.out.println(dto);
    }

    @Test
    public void t6(){
        List<Map<String,Object>> list  = memoMapper.selectAllWithResultMap();
        list.forEach(System.out::println);
    }
    @Test
    public void t7(){
        List<MemoDTO> list  = memoMapper.selectAllContains("text","a");
        list.forEach(System.out::println);
    }

    @Test
    public void t8(){
        memoMapper.insertXML(new MemoDTO(null,"TITLE88","a@a.com","text55", LocalDateTime.now()));
    }
    @Test
    public void t9(){
        memoMapper.updateXML(new MemoDTO(55L,null,null,"text55!!!!", null));
    }
    @Test
    public void t10(){
        memoMapper.deleteXML(55L);
    }
    @Test
    public void t11(){
        MemoDTO memoDTO = memoMapper.selectOneXML(19L);
        System.out.println(memoDTO);
    }
    @Test
    public void t12(){
        List<MemoDTO> list  = memoMapper.selectAllXML();
        list.forEach(System.out::println);
    }
    @Test
    public void t13(){
        List< Map<String,Object> > list  = memoMapper.selectAllMapXML();
        list.forEach(System.out::println);
    }
    //
    @Test
    public void t14(){
        Map<String,Object> param = new HashMap();
        param.put("type","text");
        param.put("keyword","a");
        List< Map<String,Object> > list =  memoMapper.selectAllIfXML(param);
        System.out.println("TOTAL : " + list.size());
        list.forEach(System.out::println);
    }
    @Test
    public void t15(){
        Map<String,Object> param = new HashMap();
        param.put("field","1");
        param.put("type","writer");
        param.put("keyword","a");
        List< Map<String,Object> > list =  memoMapper.selectAllChooseXML(param);
        System.out.println("TOTAL : " + list.size());
        list.forEach(System.out::println);
    }
    @Test
    public void t16(){
        Map<String,Object> param = new HashMap();
        param.put("field","3");
        param.put("type", Arrays.asList("text","writer"));
        param.put("keyword","a");
        List< Map<String,Object> > list =  memoMapper.selectAllIfAndXML(param);
        System.out.println("TOTAL : " + list.size());
        list.forEach(System.out::println);
    }
    @Test
    public void t17(){
        Map<String,Object> param = new HashMap();
        param.put("fields", Arrays.asList("id","text","writer"));
        param.put("keyword","5");
        List< Map<String,Object> > list =  memoMapper.selectForEachAnd(param);
        System.out.println("TOTAL : " + list.size());
        list.forEach(System.out::println);
    }
}