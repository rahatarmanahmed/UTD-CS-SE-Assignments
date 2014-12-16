<?php require('include-start.php') ?>

<form id="new-timeslot-form" class="form-horizontal" role="form" action="http://google.com">
	<div class="legend">
		<legend>New Timeslot</legend>
	</div>
	<div class="form-group">
		<label for="start-time" class="col-xs-3 control-label">Start Time</label>
		<div class="col-xs-9">
			<input type="text" class="input-xs form-control start-time width100" required>
		</div>
	</div>
	<div class="form-group">
		<label for="end-time" class="col-xs-3 control-label">End Time</label>
		<div class="col-xs-9">
			<input type="text" class="input-xs form-control end-time width100" required>
		</div>
	</div>
	<div class="form-group">
		<label for="max-participants" class="col-xs-3 control-label"># Participants</label>
		<div class="col-xs-2">
			<input type="number" class="form-control max-participants" required min="1" value="1">
		</div>
	</div>

	<div class="form-group">
		<div class="pull-right">
			<a href="home.php" class="btn btn-default btn-cancel">Cancel</a>
			<button type="submit" id="btn-submit-timeslot" class="btn btn-primary">Create Timeslot</button>
		</div>
	</div>

</form>


<script>
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

	$(function(){

		var experimentId = getParameterByName('experiment_id');
		var experiment = getExperimentById(experimentId);

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
			formatDate: 'MMMM Do YYYY',
			minDate: new Date(),
			onShow: function(ct) {
				var startTime = $(".start-time").val();
				this.setOptions({
					minDate: startTime?startTime:new Date(),
				})
			},
			onChangeDateTime: lazyDTValidate
		});

		$('.btn-cancel').attr('href','managetimeslots.php?experiment_id='+experimentId);

		$('form').submit(function(e){
			e.preventDefault();
			var startTime = $('.start-time').val();
			var endTime = $('.end-time').val();
			var maxParticipants = $('.max-participants').val();
			var data = {
				experimentId: experimentId,
				startTime: startTime,
				endTime: endTime,
				maxParticipants: maxParticipants
			}
			$.ajax({
				data: JSON.stringify(data),
				url: "api/newtimeslot.php",
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