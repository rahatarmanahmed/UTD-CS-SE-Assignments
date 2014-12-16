<?php require('include-start.php') ?>

<script id="contentTpl" type="text/html">
	<p>Which cohort would you like to move {{participantName}} to?</p>

	<select id="cohort-select" name="cohorts">
	</select>

	<br>

	<div class="pull-right">
		<a href="viewparticipants.php?experiment_id={{experimentId}}" class="btn btn-default">Cancel</a>
		<a href="#" id="btn-submit-change" class="btn btn-primary">Change Cohort</a>
	</div>
</script>

<div class="legend">
	<legend>Change Cohort</legend>
</div>
<div id="content">
	
</div>

<script>
$(function(){
	var experimentId = getParameterByName("experiment_id");
	var signupId = getParameterByName("signup_id");
	var experiment = getExperimentById(experimentId);
	var cohort, participant;
	for(var j=0;j<experiment.cohorts.length;j++)
	{
		var aCohort = experiment.cohorts[j];
		for(var k=0;k<aCohort.participants.length;k++)
		{
			if(aCohort.participants[k].signupId == signupId)
			{
				cohort = aCohort;
				participant = aCohort.participants[k];
			}
			
		}
	}

	$('#content').append(ich.contentTpl({
		participantName: participant.name,
		experimentId: experimentId
	}));

	for(var j=0;j<experiment.cohorts.length;j++)
	{
		var c = experiment.cohorts[j];
		if(c.id == cohort.id)
			$('select').append("<option value="+c.id+" selected>"+c.name+"</option>");
		else
			$('select').append("<option value="+c.id+">"+c.name+"</option>");
	}

	$('#btn-submit-change').click(function(e){
		e.preventDefault();
		var newCohortId = $('#cohort-select').val();
		var url = "api/changecohort.php?signup_id="+signupId+"&old_cohort_id="+cohort.id+"&new_cohort_id="+newCohortId;
		$.ajax({
			url: url,
			type: "GET"
		}).done(function(){
			setTimeout(function(){
				window.location = "viewparticipants.php?experiment_id="+experimentId;
			},0)
		});
	});
	
});
</script>


<?php require('include-end.php') ?>