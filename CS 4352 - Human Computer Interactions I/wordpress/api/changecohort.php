<?php

require 'connectdb.php';

$signup_id = $_GET["signup_id"];
$old_cohort_id = $_GET["old_cohort_id"];
$new_cohort_id = $_GET["new_cohort_id"];

$sql = "UPDATE signups SET cohort_id = $new_cohort_id WHERE cohort_id = $old_cohort_id AND id = $signup_id";
$retval = mysql_query($sql, $conn);
if(!$retval)
	die(mysql_error());
