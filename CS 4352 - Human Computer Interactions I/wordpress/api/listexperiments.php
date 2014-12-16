<?php

require 'connectdb.php';
date_default_timezone_set('UTC');

function osort(&$array, $prop)
{
    usort($array, function($a, $b) use ($prop) {
    	return $a[$prop] > $b[$prop] ? 1 : -1;
    });	
}

$sql = <<<SQL
SELECT e.id, e.name, e.description, e.requirement_warning requirementWarning, e.signup_test signupTest, u.name user, u.username userEmail
FROM experiments e
INNER JOIN users u ON u.id = e.user_id
SQL;
$retval = mysql_query($sql);
if(!$retval)
{
	trigger_error(mysql_error());
}

$response = array();
while($row = mysql_fetch_assoc($retval))
{
	$experiment_id = $row['id'];
	$sql = <<<SQL
SELECT id, name FROM cohorts
WHERE experiment_id = $experiment_id
SQL;
	$cohorts_retval = mysql_query($sql);
	$cohorts = array();
	while($cohort = mysql_fetch_assoc($cohorts_retval))
	{
		$cohort_id = $cohort['id'];
		$sql = <<<SQL
SELECT u.id, u.username, u.password, u.name, u.is_experimenter isExperimenter, s.id signupId, s.cohort_id cohortId, c.name cohortName
FROM signups s
INNER JOIN users u, cohorts c
WHERE s.participant_id = u.id AND s.cohort_id = $cohort_id AND c.id = $cohort_id
SQL;
		$cohort_participants_retval = mysql_query($sql);
		$cohort_participants = array();
		while($cohort_participant = mysql_fetch_assoc($cohort_participants_retval))
		{
			$cohort_participants[] = $cohort_participant;
		}
		$cohort['participants'] = $cohort_participants;
		$cohorts[] = $cohort;
	}
	$row['cohorts'] = $cohorts;
	$sql = <<<SQL
SELECT id, experiment_id experimentId, start_time startTime, end_time endTime, max_participants maxParticipants
FROM timeslots
WHERE experiment_id = $experiment_id
SQL;
	$timeslot_retval = mysql_query($sql);
	if(!$timeslot_retval)
	{
		trigger_error(mysql_error());
	}

	$timeslots = array();
	while($timeslot = mysql_fetch_assoc($timeslot_retval))
	{
		$timeslot_id = $timeslot['id'];


		$sql = <<<SQL
SELECT u.id, u.username, u.password, u.name, u.is_experimenter isExperimenter, s.id signupId, s.cohort_id cohortId, c.name cohortName
FROM signups s
INNER JOIN users u, cohorts c
WHERE s.timeslot_id = $timeslot_id AND s.participant_id = u.id AND s.cohort_id = c.id
SQL;
		$participant_retval = mysql_query($sql);
		if(!$participant_retval)
		{
			trigger_error(mysql_error());
		}
		$participants = array();
		while($participant = mysql_fetch_assoc($participant_retval))
		{
			$participant['timeslotId'] = $timeslot_id;
			$participants[] = $participant;
		}
		$timeslot['participants'] = $participants;
		$timeslots[] = $timeslot;
	}

	$row['timeslots'] = $timeslots;
	$response[] = $row;
}

osort($response, "id");
echo json_encode($response);