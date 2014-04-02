<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Schedule</title>
</head>
<body>
<h1>Sendeplan</h1>
  <div id="addProgram">
  Velg program:
  <select id="chooseProgram" ><c:forEach items="${programlist}" var="program">
        <option id="${program.defnr}">${program.name}</option>
    </c:forEach>
	</select>
	</div>
	<div id="showTable">
	<table id="planTable">
	<tr>
	<th>Mandag</th>
	<th>Tirsdag</th>
	<th>Onsdag</th>
	<th>Torsdag</th>
	<th>Fredag</th>
	<th>Lørdag</th>
	<th>Søndag</th>
	</tr>
	</table>
	</div>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript">

$("#chooseProgram").one("change",function(){
	addDaySelector();

});
var dayArray = ["Mandag","Tirsdag","Onsdag","Torsdag","Fredag","Lørdag","Søndag"];

function addDaySelector(){
	var select = $("<select>", {id: "chooseDay", class: "a"});
	var inputTimeFrom = $("<input>",{type: "time", id:"timeFrom"});
	var inputTimeTo = $("<input>",{type: "time", id:"timeTo"});
	var br = $("<br>");
	var button = $("<button>",{value: "Legg inn"});
	
	select.one("change",function(){alert("yolo");});
	
	 $.each(dayArray, function(key, value) {   
	     $(select).append($("<option></option>")
         .attr("value",((key+1)%7)+1).text(value)); 
});
	
	 $("#addProgram").append(br);
	$("#addProgram").append("Velg dag:");
	$("#addProgram").append(select);

	$("#addProgram").append("Tidspunkt fra: ");
	$("#addProgram").append(inputTimeFrom);
	$("#addProgram").append("til: ");
	$("#addProgram").append(inputTimeTo);	
	$("#addProgram").append(button);	
	

}

</script>


</body>
</html>