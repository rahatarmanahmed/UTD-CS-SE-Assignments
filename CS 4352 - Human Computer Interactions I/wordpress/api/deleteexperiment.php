<?php

require 'connectdb.php';

date_default_timezone_set('UTC');

if(!isset($_GET['id']))
	exit;

$id = $_GET['id'];

$sql = "DELETE s.* FROM signups s INNER JOIN timeslots t WHERE t.EXPERIMENT_ID = $id AND s.TIMESLOT_ID = t.ID";
mysql_query($sql, $conn);
$sql = "DELETE FROM timeslots WHERE EXPERIMENT_ID = $id";
mysql_query($sql, $conn);
$sql = "DELETE FROM experiments WHERE ID = $id";
mysql_query($sql, $conn);
$sql = "DELETE FROM cohorts WHERE EXPERIMENT_ID = $id";
mysql_query($sql, $conn);

header('Location: /wordpress/home.php');