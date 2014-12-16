<?php require('include-start.php') ?>

<script id="timeslot" type="text/html">
	<table class="table">
		<tr>
			<td><strong>Start Time:</strong></td>
			<td><input type="text" class="form-control start-time width100" value="{{timeslot.formattedStartTime}}"></td>
		</tr>
		<tr>
			<td><strong>End Time:</strong></td>
			<td><input type="text" class="form-control end-time width100" value="{{timeslot.formattedEndTime}}"></td>
		</tr>
		<tr>
			<td><strong>Max Participants:</strong></td>
			<td><input type="number" class="form-control max-participants" min="1" value="{{timeslot.maxParticipants}}"></td>
		</tr>
	</table>
	<div class="pull-right">
			<button type="button" id="btn-reschedule-timeslot" class="btn btn-primary"><i class="fa fa-calendar"></i> Save</button>
			<button type="button" id="btn-delete-timeslot" class="btn btn-danger"><i class="fa fa-times-circle"></i> Delete</button>
	</div>
	<br>
	<br>
	<br>
	<div class="legend">
		<legend>Participants</legend>
	</div>
	<table class="table" id="participants-table">
<!-- 		<tr>
			<th>Participant</th>
			<th></th>
		</tr> -->
	</table>
</script>

<script id="participant" type="text/html">
	<tr class="participant-row" data-participant-id={{id}}>
		<td><a href="mailto:{{username}}"><i class="fa fa-envelope"></i> {{name}}</a></td>
		<td><button type="button" class="btn btn-danger pull-right btn-cancel-appointment"><i class="fa fa-times-circle"></i> Cancel Appointment</button></td>
	</tr>
</script>

<div class="legend">
	<legend>Edit Timeslot</legend>
</div>
<div id="timeslot-container">

</div>
<script>
$(function(){
	var timeslotId = getParameterByName("timeslot_id");
	var timeslot = getTimeslotById(timeslotId);
	var experiment = getExperimentById(timeslot.experimentId);
	experiment.timeslot = timeslot;

	var generated = ich.timeslot(experiment);
	for(var k=0;k<timeslot.participants.length;k++)
	{
		var participant = timeslot.participants[k];
		generated.siblings('#participants-table').append(ich.participant(participant));
	}

	$('#timeslot-container').append(generated);

	if(timeslot.participants.length == 0)
	{
		$('#participants-table').replaceWith("<p>There are no participants in this timeslot.</p>");
	}

	$('.btn-cancel-appointment').click(function(){
		var result = confirm("Are you sure you want to cancel this appointment?");
		if(result)
		{
		    var participantId = $(this).closest('.participant-row').data('participant-id');
		    window.location = "api/cancelappointment.php?timeslot_id="+timeslotId+"&participant_id="+participantId+"&manage_timeslots=1";
		}
	});

	$('#btn-delete-timeslot').click(function(){
		var result = confirm("Are you sure you want to delete this timeslot?");
		if(result)
		{
		    var experimentId = experiment.id;
		    window.location = "api/deletetimeslot.php?id="+timeslotId+"&experiment_id="+experimentId;
		}
	});

	Date.parseDate = function( input, format ){
	  return moment(input,format).toDate();
	};
	Date.prototype.dateFormat = function( format ){
	  return moment(this).format(format);
	};

	var format = 'MMMM Do YYYY, h:mm a';

	function lazyDTValidate(dp, $input)
	{
		$input.val(moment($input.val(),format).format(format));
		if($input.val() === "Invalid date")
			$input.val(moment().format(format));
	}

	$(".start-time").datetimepicker({
		format: format,
		formatTime: 'h:mm a',
		formatDate: 'MMMM Do YYYY',
		minDate: new Date(),
		onShow: function(ct) {
			var endTime = $(".end-time").val();
			this.setOptions({
				maxDate: endTime?endTime:false,
			})
		},
		onChangeDateTime: lazyDTValidate
	});

	$(".end-time").datetimepicker({
		format: format,
		formatTime: 'h:mm a',
		formatDate: 'MMM Do YYYY',
		minDate: new Date(),
		onShow: function(ct) {
			var startTime = $(".start-time").val();
			this.setOptions({
				minDate: startTime?startTime:new Date(),
			})
		},
		onChangeDateTime: lazyDTValidate
	});

	$('#btn-reschedule-timeslot').click(function(){
		var startTime = $('.start-time').val();
		var endTime = $('.end-time').val();
		var maxParticipants = $('.max-participants').val();
		var experimentId = experiment.id;
		if(maxParticipants < timeslot.participants.length)
		{
			alert("You cannot have less max participants than you have existing participants!");
			return;
		}
		var data = {
			id: timeslotId,
			startTime: startTime,
			endTime: endTime,
			maxParticipants: maxParticipants
		}
		$.ajax({
			data: JSON.stringify(data),
			url: "api/rescheduletimeslot.php",
			type: "POST",
			contentType: "application/json"
		}).done(function(){
			setTimeout(function(){
				window.location = "managetimeslots.php?experiment_id="+experimentId;
			},0)
		});
		
	});
});
</script>


<?php require('include-end.php') ?>