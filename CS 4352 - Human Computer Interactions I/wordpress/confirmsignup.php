<?php require('include-start.php') ?>

<script id="experiment" type="text/html">
	<div class="require-test alert alert-warning">
		<p><strong><i class="fa fa-warning"></i> This experiment requires that you pass a qualification test before you signup. Please <a href="{{signupTest}}" target="_blank" id="test-link">click here</a> to open the test in a new window.</strong></p>
		<p>Once you have completed the exam, you may return to this window and press Confirm to confirm your signup.</p>
		
	</div>
	<p>You are signing up for:</p>
	<h4>{{name}}</h4>
	<h5><strong>Created by: </strong><a href="mailto:{{userEmail}}"><i class="fa fa-envelope"></i> {{user}}</a></h5>
	<p><strong>Description: </strong>{{description}}</p>
	<br>
	<h4>Appointment:</h4>
	<table class="table">
		<tr>
			<td><strong>Start Time:</strong></td>
			<td>{{startTime}}</td>
		</tr>
		<tr>
			<td><strong>End Time:</strong></td>
			<td>{{endTime}}</td>
		</tr>
	</table>
	<div id="test-warning" class="pull-right text-warning">
		<p><i class="fa fa-warning"></i> You must open the test linked above before you may signup.</p>
	</div>
	<div class="pull-right">
			<a href="participate.php" class="btn btn-default">Cancel</a>
			<button type="button" id="btn-submit-signup" class="btn btn-primary">Confirm Signup</button>
		</div>
</script>


<div class="legend">
	<legend>Confirm Signup</legend>
</div>
<div id="confirm-dialog">

</div>
<script>
$(function(){
	var experimentId = getParameterByName("experiment_id");
	var timeslotId = getParameterByName("timeslot_id");
	var experiment = getExperimentById(experimentId);
	var timeslot = getTimeslotById(timeslotId);

	experiment.startTime = timeslot.formattedStartTime;
	experiment.endTime = timeslot.formattedEndTime;

	$('#confirm-dialog').append(ich.experiment(experiment));

	if(experiment.signupTest == "")
	{
		$('.require-test, #test-warning').hide();
	}
	else
	{
		$('#btn-submit-signup').prop('disabled', true);
	}

	$('#test-link').click(function(){
		$('#btn-submit-signup').prop('disabled', false);
		$('#test-warning').hide();
	});

	$('#btn-submit-signup').click(function(e){
		e.preventDefault();
		var cohortId = getExperimentById(getTimeslotById(timeslotId).experimentId).cohorts[0].id;
		var url = "api/signup.php?timeslot_id="+timeslotId+"&participant_id="+currentUser.ID+"&cohort_id="+cohortId;
		$.ajax({
			url: url,
			type: "GET"
		}).done(function(){
			setTimeout(function(){
				window.location = "finishsignup.php";
			},0)
		});
	});
});
</script>


<?php require('include-end.php') ?>