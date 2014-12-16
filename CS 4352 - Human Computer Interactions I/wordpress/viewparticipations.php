<?php require('include-start.php') ?>

<script id="experiment" type="text/html">
<div class="panel panel-default">
    <div class="panel-heading accordion-toggle" data-toggle="collapse" data-toggle="#collapse{{timeslotId}}">
         <h4 class="panel-title">
            <a href="#">
                {{name}} - {{startTime}}
            </a>
        </h4>
    </div>
    <div id="collapse{{timeslotId}}" class="appointment panel-body panel-collapse collapse in" data-experiment-id="{{id}}"  data-timeslot-id="{{timeslotId}}">
        <h5><strong>Created by: </strong><a href="mailto:{{userEmail}}"><i class="fa fa-envelope"></i> {{user}}</a></h5>
        <p><strong>Description: </strong>{{description}}</p>
        <p><strong>Start Time: </strong>{{startTime}}</p>
        <p><strong>End Time: </strong>{{endTime}}</p>
        <div id="requirement-warning" class="alert alert-warning requirements-required">
            <h5><strong><i class="fa fa-warning"></i> Experiment Requirements:</strong></h5>
            <p>{{requirementWarning}}</p>
        </div>
        <button class="btn btn-danger btn-cancel-appointment"><i class="fa fa-times-circle"></i> Cancel Appointment</button>
    </div>
</div>
</script>

<div class="legend">
	<legend>Upcoming Appointments</legend>
</div>
<div class="panel-group" id="upcoming-appointments"></div>
<div class="legend">
	<legend>Past Appointments</legend>
</div>
<div class="panel-group" id="past-appointments"></div>

<script>
$(function(){
	$('.sidebar-nav-item a[href="/wordpress/viewparticipations.php"]').parent().addClass("active")
	var participantId = currentUser.ID;
	var timeslots = getParticipatorsTimeslots(participantId);
	for(var k=0;k<timeslots.length;k++)
	{
		var timeslot = timeslots[k];
		var experiment = getExperimentById(timeslot.experimentId);
		experiment.startTime = timeslot.formattedStartTime;
		experiment.endTime = timeslot.formattedEndTime;
		experiment.timeslotId = timeslot.id;
		var generated = ich.experiment(experiment)
		if(!experiment.requirementWarning)
            generated.find('#requirement-warning').hide();
		if(new Date(timeslot.endTime) < new Date())
		{
			generated.find(".btn-cancel-appointment").hide();
			$('#past-appointments').append(generated);
		}
		else
		{
			$('#upcoming-appointments').append(generated);
		}
	}

	if($('#past-appointments').find('.panel').length == 0)
		$('#past-appointments').append('<p>There are no past appointments.</p>')
	if($('#upcoming-appointments').find('.panel').length == 0)
		$('#upcoming-appointments').append('<p>There are no upcoming appointments.</p>')

	$('.btn-cancel-appointment').click(function(){
		var result = confirm("Are you sure you want to cancel this appointment?");
		if(result)
		{
		    var timeslotId = $(this).closest('.appointment').data('timeslot-id');
			var participantId = currentUser.ID;
		    window.location = "api/cancelappointment.php?timeslot_id="+timeslotId+"&participant_id="+participantId;
		}
	})
});
</script>


<?php require('include-end.php') ?>