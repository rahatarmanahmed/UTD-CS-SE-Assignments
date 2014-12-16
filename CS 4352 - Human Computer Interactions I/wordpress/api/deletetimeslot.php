<?php

require 'connectdb.php';

date_default_timezone_set('UTC');

if(!isset($_GET['id']))
	exit;

$id = $_GET['id'];
$experiment_id = $_GET['experiment_id'];

$sql = "DELETE FROM signups WHERE TIMESLOT_ID = $id";
mysql_query($sql, $conn);
$sql = "DELETE FROM timeslots WHERE ID = $id";
mysql_query($sql, $conn);

header("Location: /wordpress/managetimeslots.php?experiment_id=$experiment_id");