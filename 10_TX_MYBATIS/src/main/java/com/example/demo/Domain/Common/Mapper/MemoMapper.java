package com.example.demo.Domain.Common.Mapper;


import com.example.demo.Domain.Common.Dtos.MemoDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemoMapper {

    @SelectKey(statement = "SELECT max(id) FROM testdb.memo",keyProperty = "id",before = false,resultType = Long.class)
    @Insert("insert into memo values(#{id},#{title},#{writer},#{text},#{createAt})")
    public int insert(MemoDTO memoDTO);

    @Update("update tbl_memo set text=#{text} where id=#{id}")
    public int update(MemoDTO memoDTO);

    @Delete("delete from tbl_memo where id=#{id}")
    public int delete(Long id);

    @Select("select * from tbl_memo")
    public List<MemoDTO> selectALL();

    @Select("select * from tbl_memo where ${type} like concat('%',#{keyword},'%')")
    public List<MemoDTO> selectAllContains(@Param(value = "type") String type,String keyword);


    @Results(id="MemoResultMap",value={
            @Result(property = "text",column = "text"),
            @Result(property = "writer",column = "writer")
    })
    @Select("select text,writer from tbl_memo")
    public List<Map<String,Object>> selectAllWithResultMap();

    //XML
    public int insertXML(MemoDTO memoDTO);
    public int updateXML(MemoDTO memoDTO);
    public int deleteXML(Long id);
    public MemoDTO selectOneXML(Long id);
    public List<MemoDTO> selectAllXML();
    public List< Map<String,Object> > selectAllMapXML();
    //
    public List< Map<String,Object> > selectAllIfXML(Map<String,Object> param);
    public List< Map<String,Object> > selectAllChooseXML(Map<String,Object> param);
    public List< Map<String,Object> > selectAllIfAndXML(Map<String,Object> param);
    public List< Map<String,Object> > selectForEachAnd(Map<String,Object> param);
}
