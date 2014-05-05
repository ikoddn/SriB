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
		href="bootstrap.css">


	 <link href="signing.css" rel="stylesheet">
</head>
<body>
<div class="container">
<a href="SetSchedule">Tilbake</a>
<h1>Legg til program</h1>
<c:if test="${error == true}">
<span style="color:red">Ugyldig input</span>
</c:if>

<form  class="form form-horizontal" role="form" action="AddProgram" method="post">
<div class="form-group">
<div class="col-md-3	">
<input class="form-control" type="text" name="name" placeholder="Navn på program"/>
</div>
</div>
<input type="submit" value="Legg til" name="add" >
</form>

<h3>Program i lista</h3>
<c:forEach items="${programList }" var="program">
<form class="form form-horizontal" action="AddProgram" method="post"> 
<div class="form-group">
<div class="col-md-3">
<input class="form-control" type="text" name="name" value="${program.name}" />

</div>
	<input type="hidden" name="id" value="${program.id}" />
	
	<input class="btn btn-default" type="submit" value="Endre" name="edit" />
	<input class="btn btn-default" type="submit" value="Slett" name="delete" />
	
</div>
</form>

</c:forEach>

</div>
</body>
</html>