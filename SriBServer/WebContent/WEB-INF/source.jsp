<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Kilde til app</title>
</head>
<body>
	<h1>Sett kilde</h1>
	<c:if test="${errorUrl == true}">
	<span style="color:red">Ugyldig url</span></c:if>
	<form action="UpdateUrl" method="post"> 
		Hovedkilde:<input type="url" value="${url1}" name="mainSource"
			size="60"><br> Sekunderkilde:<input type="url"
			value="${url2}" name="secondSource" size="60"><br>
			<input type="submit" value="Lagre" />
			</form>
		<h3>Skift til sekunderkilde i desse tidsromma:</h3>
		
		<c:if test="${errorUpdate == true}">
		<span style="color:red">Ugyldig input</span>
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
						<option value="${loop.index+1}" selected="selected" >
					<c:out value="${day}" />
					</option>
					</c:if>


				</c:forEach>
			</select>
			Fra:<input type="time" name="fromTime" value="${el.fromtime}"> Til: <input
				type="time" name="toTime" value="${el.totime}">
				<input type="hidden" name="id" value="${el.id}">
			<input type="Submit" name="edit" value="Endre">
			<input type="Submit" name="delete" value="Slett">
			<br>
		</form>
		</c:forEach>
		
		<h3>Legg til nytt tidspunkt</h3>
		<form action="AddUrlSchedule" method="post">
		<c:if test="${errorNew == true}">
		<span style="color:red">Ugyldig input</span>
		</c:if>
		Fra: <select name="day">
		
			<c:forEach items="${days}" var="day" varStatus="loop">
				<option value="${loop.index+1}"><c:out value="${day}" /></option>
			</c:forEach>
		</select> <input type="time" name="fromTime"> Til: <input type="time" name="toTime">
		<input type="submit" value="Legg til">

	</form>





</body>
</html>


<!-- 


<select>
<option>Mandag</option>
<option>Tirsdag</option>
<option>Onsdag</option>
<option>Torsdag</option>
<option>Fredag</option>
<option>Lørdag</option>
<option>Søndag</option>
</select>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

<script type="text/javascript">
var dayArray = ["Mandag","Tirsdag","Onsdag","Torsdag","Fredag","LÃ¸rdag","SÃ¸ndag"];
var count = 0;

$( "input[name*='addMore']" ).click(function() {
	
	addTimeRow(count);
	  
	  $.each(dayArray, function(key, value) {   
		     $("select[name*='"+count +"']").append($("<option></option>")
	         .attr("value",key).text(value)); 
	});
	  
	  
	  count++;
	});
	


function addTimeRow(count){
	
	$("#sourceForm").append("<br><Select name=" +count +" class='"+count+"'></Select>Fra:<input type='time' class='"+count+"' name='fromTime'> Til: <input type='time' class='"+count+"'>"
			+ "<input type='button' class='"+count+"' id="+ count +" value='Slett' name='deleteButton'>  ");


$("button").click(function(){
	//alert(this.id);
	removeRow(this.id);
	
});

}


function removeRow(id){

}

</script> -->