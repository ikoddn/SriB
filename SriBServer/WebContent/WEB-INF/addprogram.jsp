<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet"
		href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">

	

	<!-- Latest compiled and minified JavaScript -->
	<script
		src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

	 <link href="signing.css" rel="stylesheet">
</head>
<body>
<div class="container">
<a href="SetSchedule">Tilbake</a>
<h1>Legg til program</h1>
<c:if test="${error == true}">
<span style="color:red">Ugyldig input</span>
</c:if>

<form action="AddProgram" method="post">
<input type="text" name="name" placeholder="Navn på program"/>
<input type="submit" value="Legg til" name="add" >
</form>

<h3>Program i lista</h3>
<c:forEach items="${programList }" var="program">
<form action="AddProgram" method="post"> 
<input type="text" name="name" value="${program.name}" />
	<input type="hidden" name="id" value="${program.id}" />
	<input type="submit" value="Endre" name="edit" />
	<input type="submit" value="Slett" name="delete" />
</form>

</c:forEach>

</div>
</body>
</html>