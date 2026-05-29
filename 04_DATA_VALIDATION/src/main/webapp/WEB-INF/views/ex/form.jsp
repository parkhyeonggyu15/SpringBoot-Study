<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>EX 회원가입 폼</title>
    <style>
        body   { font-family: sans-serif; }
        .row   { margin-bottom: 14px; }
        label  { display:inline-block; width:90px; font-weight:bold; }
        .err   { color:red; font-size:13px; margin-left:6px; }
        input  { padding:4px 6px; }
        button { padding:6px 16px; }
    </style>
</head>
<body>
    <h1>[EX06] 회원가입 폼 (유효성 검증 메시지 출력)</h1>
    <p>값을 비우거나 잘못 입력하고 [가입]을 누르면 각 항목 옆에 오류 메시지가 표시됩니다.</p>

    <form action="/ex/form" method="post">

        <div class="row">
            <label>이름</label>
            <input name="username" value="${dto.username}" />
            <span class="err">${username}</span>
        </div>

        <div class="row">
            <label>나이</label>
            <input name="age" value="${dto.age}" />
            <span class="err">${age}</span>
        </div>

        <div class="row">
            <label>이메일</label>
            <input name="email" value="${dto.email}" />
            <span class="err">${email}</span>
        </div>

        <div class="row">
            <label>비밀번호</label>
            <input type="password" name="password" value="${dto.password}" />
            <span class="err">${password}</span>
        </div>

        <div class="row">
            <button type="submit">가입</button>
        </div>

    </form>
</body>
</html>
