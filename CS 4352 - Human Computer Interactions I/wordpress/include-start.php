<?php
session_start();

# Redirect to login if not logged in and visiting some page
 if(!isset($_SESSION['user']) && strpos($_SERVER['PHP_SELF'], "wordpress/index.php") === FALSE)
 {
 	header('Location: /wordpress/index.php');
 }

# Redirect to home page if logged in and visiting login page
 if(isset($_SESSION['user']) && strpos($_SERVER['PHP_SELF'], "wordpress/index.php") !== FALSE)
 {
 	header('Location: /wordpress/home.php');
 }

  if(isset($_SESSION['user']) && $_SESSION['user']['IS_EXPERIMENTER'] == FALSE && strpos($_SERVER['PHP_SELF'], "wordpress/home.php") !== FALSE)
 {

 	header('Location: /wordpress/participate.php');
 }

?>
<html>
<head>
	<title>CS 4352 - Experiment Manager</title>
	<link rel="stylesheet" href="/wordpress/bootstrap-3.2.0-dist/css/bootstrap.min.css">
	<link rel="stylesheet" href="/wordpress/bootstrap-3.2.0-dist/css/bootstrap-theme.min.css">
	<link rel="stylesheet" href="jquery-ui-1.11.1/jquery-ui.css">
	<link rel="stylesheet" href="jquery.datetimepicker.css">
	<link rel="stylesheet" href="font-awesome-4.2.0/css/font-awesome.min.css">
	<link rel="stylesheet" href="style.css">
	<script src="jquery-1.11.1.min.js"></script>
	<script src="jquery-ui-1.11.1/jquery-ui.min.js"></script>
	<script src="/wordpress/bootstrap-3.2.0-dist/js/bootstrap.min.js"></script>
	<script src="ICanHaz.min.js"></script>
	<script src="jquery.datetimepicker.js"></script>
	<script src="jquery.validate.min.js"></script>
	<script src="moment.js"></script>
	<script>

		function getParameterByName(name) {
		    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
		    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
		}

		function getExperimentById(id) {
			for(var k=0;k<experiments.length;k++)
				if(experiments[k].id == id)
					return experiments[k];
			return null;
		}

		function getTimeslotById(id) {
			for(var k=0;k<experiments.length;k++)
			{
				experiment = experiments[k];
				for(var j=0;j<experiment.timeslots.length;j++)
				{
					if(experiment.timeslots[j].id == id)
					{
						return experiment.timeslots[j]
					}
				}
			}
			return null;
		}

		function getParticipatorsTimeslots(participatorId)
		{
			var timeslots = [];
			for(var k=0;k<experiments.length;k++)
			{
				experiment = experiments[k];
				for(var j=0;j<experiment.timeslots.length;j++)
				{
					timeslot = experiment.timeslots[j];
					for(var i=0;i<timeslot.participants.length;i++)
					{
						var participant = timeslot.participants[i];
						if(participant.id == participatorId)
						{
							timeslots.push(timeslot)
							break;
						}
					}
				}
			}
			return timeslots;
		}
		
		var experimenter_email = "experimenter@utd.edu",
			participant_email = "participant@utd.edu";

		var experiments = <?php require('api/listexperiments.php'); ?>;
		var currentUser = <?php echo isset($_SESSION['user']) ? json_encode($_SESSION['user']) : "null" ; ?>;

		// Do some sorting on experiment data
		for(var k=0;k<experiments.length;k++)
		{
			var experiment = experiments[k];
			experiment.totalParticipants = 0;
			experiment.totalMaxParticipants = 0;
			experiment.cohorts.sort(function(a,b){
				return a.participants.length - b.participants.length;
			});
			experiment.timeslots.sort(function(a,b){
				return new Date(a.startTime) - new Date(b.startTime);
			});
			for(var j=0;j<experiment.timeslots.length;j++)
			{
				var timeslot = experiment.timeslots[j];
				// while we're here, format the dates
				timeslot.formattedStartTime = moment(timeslot.startTime).format('MMMM Do YYYY, h:mm a');
				timeslot.formattedEndTime = moment(timeslot.endTime).format('MMMM Do YYYY, h:mm a');
				timeslot.formattedTwoLineStartTime = timeslot.formattedStartTime.replace(", ", "<br>");
				timeslot.formattedTwoLineEndTime = timeslot.formattedEndTime.replace(", ", "<br>");
				timeslot.participants.sort(function(a,b){
					return a.name.localeCompare(b.name);
				})
				experiment.totalParticipants += timeslot.participants.length;
				experiment.totalMaxParticipants += parseInt(timeslot.maxParticipants);
			}
		}
		experiments.sort(function(a,b){
			return b.id - a.id;
		});
	</script>
</head>
<body>
	<?php require('header.php') ?>
	<div class="container">
		<div class="row">
			<div class="col-xs-3 sidebar-nav">
				<?php require('sidebar.php') ?>		
			</div>
			<div class="col-xs-9 main-content">
