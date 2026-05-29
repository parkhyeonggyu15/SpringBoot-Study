<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	PAGE : /memo/add
</h1>

        <form action="/memo/add" method="POST">
            <div>
                <label>아이디</label>
                <input name="id" />&nbsp;&nbsp;<span style="font-size:.8rem;">${id}</span>
            <div>
            <div>
                <label>제목</label>
                <input name="title" />&nbsp;&nbsp;<span style="font-size:.8rem;">${title}</span>
            <div>
            <div>
                <label>작성자</label>
                <input name="writer" />&nbsp;&nbsp;<span style="font-size:.8rem;">${writer}</span>
            <div>
            <div>
                <label>내용</label>
                <textarea name="text"></textarea>&nbsp;&nbsp;<span style="font-size:.8rem;">${text}</span>
            <div>
            <div>
                <label>작성일</label>
                <input type="datetime-local" name="createAt"/>
            <div>

            <div>
                <input type="submit" value="저장" />&nbsp;&nbsp;<span style="font-size:.8rem;">${createAt}</span>
            </div>

        </form>

</body>
</html>


