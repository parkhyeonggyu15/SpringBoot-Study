<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<html>
<head>
    <title>가입 완료</title>
    <style> body { font-family: sans-serif; } </style>
</head>
<body>
    <h1>가입이 완료되었습니다 ✅</h1>
    <p>입력값이 모든 유효성 검증을 통과했습니다.</p>
    <ul>
        <li>이름 : ${dto.username}</li>
        <li>나이 : ${dto.age}</li>
        <li>이메일 : ${dto.email}</li>
    </ul>
    <a href="/ex/form">다시 입력하기</a>
</body>
</html>
