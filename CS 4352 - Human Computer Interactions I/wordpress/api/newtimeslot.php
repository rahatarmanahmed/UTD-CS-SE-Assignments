<?php

require 'connectdb.php';

$body = file_get_contents('php://input');
$json = json_decode($body, true);

$timeslot_id = $json["id"];

$startTime = date("Y-m-d H:i:s", strtotime($json['startTime']));
$endTime = date("Y-m-d H:i:s", strtotime($json['endTime']));
$maxParticipants = $json['maxParticipants'];
$id = $json['experimentId'];

$sql = "INSERT INTO timeslots (EXPERIMENT_ID, START_TIME, END_TIME, MAX_PARTICIPANTS) VALUES ($id, '$startTime', '$endTime', $maxParticipants)";
	$retval = mysql_query($sql);
if(!$retval)
	die(mysql_error());
