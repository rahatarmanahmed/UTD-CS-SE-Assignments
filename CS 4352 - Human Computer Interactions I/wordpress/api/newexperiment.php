<?php

/*

test POST body
{
	"name": "POST test",
	"description": "This is a POST test.",
	"requirementWarning": "This experiment is working yaay.",
	"timeslots": [
		{
			"startTime": "2014-11-15T10:00:00.000Z",
			"endTime": "2014-11-15T11:00:00.000Z",
			"maxParticipants": 1
		},
		{
			"startTime": "2014-11-16T10:00:00.000Z",
			"endTime": "2014-11-16T11:00:00.000Z",
			"maxParticipants": 1
		},
		{
			"startTime": "2014-11-16T14:00:00.000Z",
			"endTime": "2014-11-16T16:00:00.000Z",
			"maxParticipants": 2
		}		
	],
	"cohorts": ["one", "two"],
	"signupTest": "http://google.com"
}
*/

require 'connectdb.php';

date_default_timezone_set('UTC');

$conn = mysql_connect($hostname, $username, $password);
$db = mysql_select_db($database);

$body = file_get_contents('php://input');
$json = json_decode($body, true);

// echo var_dump($json);
if($json == NULL)
	die("this JSON is whack, yo");


# Create the experiment
$id = $json['id'];
$name = mysql_real_escape_string($json['name']);
$description = mysql_real_escape_string($json['description']);
$requirementWarning = mysql_real_escape_string($json['requirementWarning']);
$signupTest = mysql_real_escape_string($json['signupTest']);
$user_id = 1;
$is_public = true;


if($name)
{
	if(!isset($id))
		$sql = "INSERT INTO experiments (NAME, USER_ID, DESCRIPTION, REQUIREMENT_WARNING, SIGNUP_TEST) VALUES ('$name', $user_id, '$description', '$requirementWarning','$signupTest')";
	else
		$sql = "UPDATE experiments SET NAME='$name', USER_ID=$user_id, DESCRIPTION='$description', REQUIREMENT_WARNING='$requirementWarning', SIGNUP_TEST='$signupTest' WHERE ID = $id";
	$retval = mysql_query($sql, $conn);
	if(!$retval)
	{
		// header('X-PHP-Response-Code: 500', true, 500);
		die(mysql_error());
	}
	
	# Create timeslots and cohorts
	if(!isset($id))
	{
		$id = mysql_insert_id();
		
		foreach($json['timeslots'] as $timeslot)
		{
			// $startTime = substr($timeslot['startTime'], 0, -5);
			// $startTime = date_create_from_format($JSDateFormat, $startTime);
			$startTime = date("Y-m-d H:i:s", strtotime($timeslot['startTime']));
			$endTime = date("Y-m-d H:i:s", strtotime($timeslot['endTime']));
			$maxParticipants = $timeslot['maxParticipants'];
			$sql = "INSERT INTO timeslots (EXPERIMENT_ID, START_TIME, END_TIME, MAX_PARTICIPANTS) VALUES ($id, '$startTime', '$endTime', $maxParticipants)";
			$retval = mysql_query($sql);
			if(!$retval)
			{
				// header('X-PHP-Response-Code: 500', true, 500);
				die(mysql_error());
			}
		}
		
		foreach($json['cohorts'] as $cohort)
		{
			$cohort = mysql_real_escape_string($cohort);
			$sql = "INSERT INTO cohorts (EXPERIMENT_ID, NAME) VALUES ($id, '$cohort')";
			$retval = mysql_query($sql);
			if(!$retval)
			{
				// header('X-PHP-Response-Code: 500', true, 500);
				die(mysql_error());
			}
		}
	}
	else
	{
		foreach($json['cohorts'] as $cohort)
		{
			$cohort_name = mysql_real_escape_string($cohort['name']);
			$cohort_id = $cohort['id'];
			if($cohort['status'] == 'new')
			{
				$sql = "INSERT INTO cohorts (EXPERIMENT_ID, NAME) VALUES ($id, '$cohort_name')";
				$retval = mysql_query($sql);
				if(!$retval)
					die(mysql_error());	
			}
			else if($cohort['status'] == 'delete')
			{
				$sql = "SELECT id FROM cohorts WHERE experiment_id = $id AND id != $cohort_id";
				$other_cohorts_retval = mysql_query($sql);
				$other_cohorts = array();
				while($other_cohort = mysql_fetch_assoc($other_cohorts_retval))
					$other_cohorts[] = $other_cohort;
				$sql = "SELECT id FROM signups WHERE cohort_id = $cohort_id";
				$signups_retval = mysql_query($sql);
				$signups = array();
				while($signup = mysql_fetch_assoc($signups_retval))
					$signups[] = $signup;
				$pos = 0;
				foreach($signups as $signup)
				{
					$other_cohort_id = $other_cohorts[$pos++]['id'];
					echo "Updating \n";
					echo $signup;
					echo "To $pos\n";
					echo $other_cohort_id;
					echo "\n\n";
					if($pos >= count($other_cohorts))
						$pos = 0;
					$sql = "UPDATE signups SET cohort_id = $other_cohort_id WHERE cohort_id = $cohort_id";
					$retval = mysql_query($sql);
					if(!$retval)
						die("$other_cohort_id".mysql_error());
				}
				$sql = "DELETE FROM cohorts WHERE id = $cohort_id";
				$retval = mysql_query($sql);
				if(!$retval)
					die(mysql_error());
			}
			else
			{
				$sql = "UPDATE cohorts SET name='$cohort_name' WHERE id = $cohort_id";
				$retval = mysql_query($sql);
				if(!$retval)
					die(mysql_error());	
			}
		}
	}
}