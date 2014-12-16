<?php

require 'connectdb.php';

date_default_timezone_set('UTC');

$timeslot_id = $_GET['timeslot_id'];
$participant_id = $_GET['participant_id'];
$manage_timeslots = isset($_GET['manage_timeslots']);

$sql = "DELETE FROM signups WHERE TIMESLOT_ID = $timeslot_id AND PARTICIPANT_ID = $participant_id";
mysql_query($sql, $conn);

if($manage_timeslots)
{
	header("Location: /wordpress/managetimeslot.php?timeslot_id=$timeslot_id");	
}
else
{
	header("Location: /wordpress/viewparticipations.php?$manage_timeslots");
}