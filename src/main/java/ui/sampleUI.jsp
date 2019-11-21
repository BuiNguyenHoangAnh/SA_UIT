<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Demo Sentiment Analysis</title>
</head>
<body>
	<form action="sampleUI" method="post">
		<input type="text" name="input">
		<input type="button" name="button" value="Submit">
	</form>
	<br>
	<div>
		<span>Aspect</span>
		<div>
			<span>Training:</span>
			<span>50%</span>

			<span>Facility:</span>
			<span>50%</span>
		</div>
	</div>
	<br>
	<div>
		<span>Sentiment</span>
		<div>
			<span>Positive:</span>
			<span>50%</span>

			<span>Negative:</span>
			<span>50%</span>
		</div>
	</div>
</body>
</html>