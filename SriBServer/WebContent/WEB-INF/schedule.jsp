<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
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
	<a href="Login">Tilbake</a>
	<h1>Sendeplan</h1>
	<c:if test="${errorUpdate == true}">
		<span style="color: red">Ugyldig input</span>
	</c:if>

		
		<h3>Legg til i sendeplan</h3>

		<form  action="UpdateSchedule" method="post">
				
			<select name="day">
				<c:forEach items="${days}" var="day" varStatus="loop">
					<option selected="selected" value="${((loop.index+1)%7)+1}">${day}</option>
				</c:forEach>

			</select> 
			Frå:<input type="time" name="fromTime" /> 
			Til:<input type="time" name="toTime" /> 
				<select id="chooseProgram" name="program">
				<c:forEach items="${programlist}" var="program">
					<option value="${program.id}">${program.name}</option>
				</c:forEach>
			</select> 
			<input type="submit" class="btn btn-success" name="add" value="Legg til" />
		</form>
	<a href="AddProgram">LeggTil/Fjern/Endre program</a>
	<div id="showTable">
		<h2>Programmer i sendeplanen</h2>
		<c:forEach items="${dbList}" var="dayList" varStatus="loop">
			<c:if test="${not empty dayList}">
				<h4>${days[loop.index]}</h4>
				<c:forEach items="${dayList}" var="program">
					<form action="UpdateSchedule" method="post">
						<c:set var="map" value="${definitionMap}" />
						<select name="day">
							<c:forEach items="${days}" var="day" varStatus="loop2">
								<c:if test="${day == days[loop.index]}">
									<option selected="selected" value="${((loop2.index+1)%7)+1}">${day}</option>
								</c:if>
								<c:if test="${day != days[loop.index]}">
									<option value="${((loop2.index+1)%7)+1}">${day}</option>
								</c:if>
							</c:forEach>

						</select> 
						Fra:<input type="time" name="fromTime" value="${program.fromtime}" />
						Til:<input type="time" name="toTime" value="${program.totime}" /> 
							<select name="program">
							<c:forEach items="${programlist}" var="def">
								<c:if test="${program.program == def.id}">
									<option value="${def.id}" selected="selected">${def.name}</option>
								</c:if>
								<c:if test="${program.program != def.id }">
									<option value="${def.id}">${def.name}</option>
								</c:if>
							</c:forEach>
						</select> <input type="hidden" name="id" value="${program.id}" /> 
							<input type="hidden" name="program" value="${program.program}" /> 
							<input class="btn btn-success" type="submit" name="edit" value="Endre" /> 
							<input type="submit" class="btn btn-danger" name="delete" value="Slett" />

					</form>
				</c:forEach>
			</c:if>
		</c:forEach>
	
		


	</div>

</div>

</body>
</html>

