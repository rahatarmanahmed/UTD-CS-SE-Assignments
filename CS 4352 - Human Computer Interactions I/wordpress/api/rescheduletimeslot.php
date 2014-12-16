<?php

require 'connectdb.php';

$body = file_get_contents('php://input');
$json = json_decode($body, true);

$timeslot_id = $json["id"];

$startTime = date("Y-m-d H:i:s", strtotime($json['startTime']));
$endTime = date("Y-m-d H:i:s", strtotime($json['endTime']));
$maxParticipants = $json['maxParticipants'];

$sql = "UPDATE timeslots SET start_time = '$startTime', end_time = '$endTime', max_participants = $maxParticipants WHERE id = $timeslot_id";
$retval = mysql_query($sql, $conn);
if(!$retval)
	die(mysql_error());
