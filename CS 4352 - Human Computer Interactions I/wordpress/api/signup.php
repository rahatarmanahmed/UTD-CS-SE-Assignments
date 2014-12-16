<?php

require 'connectdb.php';

$participant_id = $_GET["participant_id"];
$timeslot_id = $_GET["timeslot_id"];
$cohort_id = $_GET["cohort_id"];
// echo "$participant_id $timeslot_id";

$sql = "INSERT INTO signups (PARTICIPANT_ID, TIMESLOT_ID, COHORT_ID) VALUES ($participant_id, $timeslot_id, $cohort_id)";
$retval = mysql_query($sql, $conn);
if(!$retval)
{
	// header('X-PHP-Response-Code: 500', true, 500);
	die(mysql_error());
}
