<?php require('include-start.php') ?>

<script id="experiment" type="text/html">
<div class="panel panel-default">
    <div class="panel-heading accordion-toggle" data-toggle="collapse" data-toggle="#collapse{{id}}">
         <h4 class="panel-title">
            <a href="#">
                {{name}}
            </a>
        </h4>
    </div>
    <div id="collapse{{id}}" class="panel-body panel-collapse collapse in">
        <h5><strong>Created by: </strong><a href="mailto:{{userEmail}}"><i class="fa fa-envelope"></i> {{user}}</a></h5>
        <p><strong>Description: </strong>{{description}}</p>
        <div id="requirement-warning" class="alert alert-warning requirements-required">
            <h5><strong><i class="fa fa-warning"></i> Experiment Requirements:</strong></h5>
            <p>{{requirementWarning}}</p>
        </div>
        <h4>Available Appointments</h4>
        <table class="table signup-table">
        	<tr>
        		<th>Start</th>
        		<th>End</th>
        		<th></th>
        	</tr>
        </table>
    </div>
</div>
</script>

<script id="timeslot" type="text/html">
<tr>
	<td>{{{formattedTwoLineStartTime}}}</td>
	<td>{{{formattedTwoLineEndTime}}}</td>
	<td><a href="confirmsignup.php?experiment_id={{experimentId}}&timeslot_id={{id}}" class="btn btn-primary">Signup</a></td>
</tr>
</script>


<div class="legend">
	<legend>Available Experiments</legend>
</div>
<div class="panel-group" id="accordion">
	
</div>
<script>
$(function(){
	$('.sidebar-nav-item a[href="/wordpress/participate.php"]').parent().addClass("active")
	for(var k=0; k<experiments.length; k++)
	{
		var experiment = experiments[k];
		var generated = ich.experiment(experiment);
		if(!experiment.requirementWarning)
            generated.find('#requirement-warning').hide();
		var timeslotAvailable = false;
		for(var j=0; j<experiment.timeslots.length; j++)
		{
			var timeslot = experiment.timeslots[j]
			// check if they're already in the timeslot, don't let them sign up twice
			var alreadySignedUp = false;
			for(var i=0;i<timeslot.participants.length;i++)
			{
				var participant = timeslot.participants[i];
				if(participant.id == currentUser.ID)
				{
					alreadySignedUp = true;
					break;
				}
			}
			if(alreadySignedUp)
				break;
			if(timeslot.participants.length < timeslot.maxParticipants)
			{
				$(generated).find('.signup-table').append(ich.timeslot(timeslot));
				timeslotAvailable = true;
			}
		}
		if(timeslotAvailable)
			$('#accordion').append(generated);
	}
});
</script>


<?php require('include-end.php') ?>