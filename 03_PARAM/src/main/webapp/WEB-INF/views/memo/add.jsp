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
            <label>제목</label>
            <input name="title" />
        </div>
        <div>
             <label>내용</label>
             <textarea name="text"></textarea>
        </div>
        <div>
            <input type="submit" value="저장" />
        </div>
    </form>


</body>
</html>