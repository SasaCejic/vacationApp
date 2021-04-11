<?php

if (isset($_POST['firstName'])) {
	$fullName = $_POST['firstName'] . " " . $_POST['lastName'];
	$dateFrom = $_POST['dateFrom'];
	$dateUntil = $_POST['dateUntil'];
	$numberOfDays = $_POST['numberOfDays'];
	$year = $_POST['year'];
	$filename = 'post_data.json';
	$handle = fopen($filename, 'w');
	$values = json_encode($_POST);
	fwrite($handle, $values);
	fclose($handle);
}

$strJsonFileContents = file_get_contents("post_data.json");
$_POST = json_decode($strJsonFileContents, true);
	
$fullName = $_POST['firstName'] . " " . $_POST['lastName'];
$dateFrom = $_POST['dateFrom'];
$dateUntil = $_POST['dateUntil'];
$numberOfDays = $_POST['numberOfDays'];
$year = $_POST['year'];


if($fullName != "" && $dateFrom != "" && $dateUntil != "" && $numberOfDays != "" && $year != ""){
    echo "Full name: $fullName <br>";
    echo "Date from: $dateFrom <br>";
    echo "Date until: $dateUntil <br>";
    echo "Number of days: $numberOfDays <br>";
    echo "Year: $year <br>";
}else{
    echo "No data. Try again.";
}

?>

