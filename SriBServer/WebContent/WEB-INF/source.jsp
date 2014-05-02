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
	<a href="Login">Tilbake</a><h1>Sett kilde</h1>
	<c:if test="${errorUrl == true}">
		<span style="color: red">Ugyldig url</span>
	</c:if>
	<form action="UpdateUrl" method="post">
	
		<h4>Hovedkilde:</h4>
		Navn:<input type="text" name="name" value='<c:out value="${url1.name}"/>' />
		URL:<input type="url" value='<c:out value="${url1.url}"/>' name="url"
			size="60">
			<input type="hidden" name="id" value="${url1.id}" />
			<input type="submit" value="Lagre" />
	</form>
	<form action="UpdateUrl" method="post">
		<h4>Sekunderkilde:</h4>
		Navn:<input type="text" name="name" value='<c:out value="${url2.name}"/>' />
		URL:<input type="url" value='<c:out value="${url2.url}"/>' name="url"
			size="60"> 
			<input type="hidden" name="id" value="${url2.id}" />
			<input type="submit" value="Lagre" />
	</form>


	<c:if test="${not empty schedule }"><h3>Skift til sekunderkilde i desse tidsromma:</h3>
	<c:if test="${errorUpdate == true}">
		<span style="color: red">Ugyldig input</span>
	</c:if>
	<c:forEach items="${schedule}" var="el">
		<form action="UpdateUrlSchedule" method="post">
			<select name="day">
				<c:forEach items="${days}" var="day" varStatus="loop">


					<c:if test="${loop.index+1 != el.day}">
						<option value="${loop.index+1}">
							<c:out value="${day}" />
						</option>
					</c:if>

					<c:if test="${loop.index+1 == el.day}">
						<option value="${loop.index+1}" selected="selected">
							<c:out value="${day}" />
						</option>
					</c:if>


				</c:forEach>
			</select> Fra:<input type="time" name="fromTime" value="${el.fromtime}">
			Til: <input type="time" name="toTime" value="${el.totime}"> <input
				type="hidden" name="id" value="${el.id}"> <input
				type="Submit" name="edit" value="Endre"> <input
				type="Submit" name="delete" value="Slett"> <br>
		</form>
	</c:forEach>
	</c:if>
	
	<c:if test="${empty schedule }">
	<h3>Ingen tidspunkt er lagt til</h3>
	</c:if>
	
	<h3>Legg til nytt tidspunkt</h3>
	<form action="AddUrlSchedule" method="post">
		<c:if test="${errorNew == true}">
			<span style="color: red">Ugyldig input</span>
		</c:if>
		Fra: <select name="day">

			<c:forEach items="${days}" var="day" varStatus="loop">
				<option value="${loop.index+1}"><c:out value="${day}" /></option>
			</c:forEach>
		</select> <input type="time" name="fromTime"> Til: <input type="time"
			name="toTime"> <input type="submit" value="Legg til">

	</form>




</div>
</body>
</html>


<!-- <div class="form-group">
    <label for="" class="control-label">Paycheck</label>
    <div class="input-group input-group-lg">
        <span class="input-group-addon">$</span> 
        <input type="text" class="form-control" id="">
    </div>
</div> -->