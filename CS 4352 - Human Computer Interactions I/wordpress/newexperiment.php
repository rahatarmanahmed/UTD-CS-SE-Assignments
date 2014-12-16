<?php require('include-start.php') ?>

<script id="timeslot" type="text/html">
	<div class="form-group timeslot">
		<div class="col-xs-4">
			<label class="control-label">Start</label>
			<input type="text" class="input-xs form-control start-time width100" required>
		</div>
		<div class="col-xs-4">
			<label class="control-label">End</label>
			<input type="text" class="input-xs form-control end-time width100" required>
		</div>
		<div class="col-xs-2">
			<label class="control-label" style="white-space:nowrap"># Participants</label>
			<input type="number" class="form-control max-participants" required min="1" value="1">
		</div>
		<div class="col-xs-2">
			<label class="control-label width100">&zwnj;</label>
			<a href="#" title="Delete" class="form-control btn btn-default btn-delete-timeslot" style="width:100%;float:right">Delete</a>
		</div>
	</div>
</script>

<script id="cohort" type="text/html">
	<div class="form-group cohort">
		<label class="col-xs-3 control-label" class="control-label">Cohort Name</label>
		<div class="col-xs-7">
			<input type="text" class="input-xs form-control cohort-name width100" required>
		</div>
		<div class="col-xs-2">
			<a href="#" title="Delete" class="form-control btn btn-default btn-delete-cohort" style="width:100%;float:right">Delete</a>
		</div>
	</div>
</script>

<form id="new-experiment-form" class="form-horizontal" role="form" action="#">
	<div class="legend">
		<legend>New Experiment Details</legend>
	</div>
	<div class="form-group">
		<label for="experimentName" class="col-xs-3 control-label">Name*</label>
		<div class="col-xs-9">
			<input type="text" class="form-control" id="experimentName" required>
		</div>
	</div>
	<div class="form-group">
		<label for="description" class="col-xs-3 control-label">Description*</label>
		<div class="col-xs-9">
			<textarea class="form-control" id="description" required></textarea>
		</div>
	</div>
	<div class="form-group">
		<label for="requirement-warning" class="col-xs-3 control-label">Requirement Warning</label>
		<div class="col-xs-9">
			<textarea class="form-control" id="requirement-warning"></textarea>
		</div>
	</div>
	<div>
		<p>This is an optional field. Include any warning about experiment requirements to show about this experiment.</p>
	</div>
	<div class="legend">
		<legend>Timeslots</legend>
	</div>
	<div id="timeslots">
		
	</div>
	<div class="col-xs-2">
		<a id="btn-add-timeslot" href="#" class="btn btn-default"><i class="fa fa-plus"></i> New Timeslot</a>
	</div>
	<br>
	<br>

	<div class="legend">
		<legend>Cohorts</legend>
	</div>
	<div id="cohorts">
		
	</div>
	<div class="col-xs-2">
		<a id="btn-add-cohort" href="#" class="btn btn-default"><i class="fa fa-plus"></i> New Cohort</a>
	</div>
	<br>
	<br>

	<div class="legend">
		<legend>Tests</legend>
	</div>
	<div class="form-group">
		<label for="requiredTest" class="col-xs-3 control-label">Pre-Signup Test</label>
		<div class="col-xs-9">
			<input type="text" class="form-control" id="requiredTest">
		</div>
	</div>
	<div>
		<p class="col-xs-offset-3 col-xs-9">This is an optional field. You may input a link to any test (like Qualtrics) that you wish for all participants to take before signing up for the experiment.</p>
	</div>

	<div class="legend">
		<legend>&nbsp;</legend>
	</div>
	<div class="form-group">
		<div class="pull-right">
			<a href="home.php" class="btn btn-default">Cancel</a>
			<button type="submit" id="btn-submit-experiment" class="btn btn-primary">Create Experiment</button>
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

	var format = 'MMM Do YYYY, h:mm a';

	function lazyDTValidate(dp, $input)
	{
		$input.val(moment($input.val(),format).format(format));
		if($input.val() === "Invalid date")
			$input.val(moment().format(format));
	}

	function addTimeSlot()
	{
		var timeslot = ich.timeslot();
		$('#timeslots').append(timeslot);
		timeslot.find(".start-time").datetimepicker({
			format: format,
			formatTime: 'h:mm a',
			formatDate: 'MMM Do YYYY',
			minDate: new Date(),
			onShow: function(ct) {
				var endTime = timeslot.find(".end-time").val();
				this.setOptions({
					maxDate: endTime?endTime:false,
				})
			},
			onChangeDateTime: lazyDTValidate
		});

		timeslot.find(".end-time").datetimepicker({
			format: format,
			formatTime: 'h:mm a',
			formatDate: 'MMM Do YYYY',
			minDate: new Date(),
			onShow: function(ct) {
				var startTime = timeslot.find(".start-time").val();
				this.setOptions({
					minDate: startTime?startTime:new Date(),
				})
			},
			onChangeDateTime: lazyDTValidate
		});
	}

	function addCohort()
	{
		var cohort = ich.cohort();
		$('#cohorts').append(cohort);
	}

	$(function(){
		addTimeSlot();
		addCohort();

		$('#btn-add-timeslot').click(function(e){
			e.preventDefault();
			addTimeSlot();
		});

		$('#btn-add-cohort').click(function(e){
			e.preventDefault();
			addCohort();
		});

		$('#cohorts').on('click', '.btn-delete-cohort', function(e){
			e.preventDefault();
			if($('.cohort').length == 1)
				alert("You must have at least one cohort.");
			else
				$(this).closest('.cohort').remove();
		});

		$('#timeslots').on('click', '.btn-delete-timeslot', function(e) {
			e.preventDefault();
			if($('.timeslot').length == 1)
				alert("You must have at least one timeslot.");
			else
				$(this).closest('.timeslot').remove();
		});

		$('form').submit(function(e){
			e.preventDefault();
			var name = $('#experimentName').val();
			var description = $('#description').val();
			var requirementWarning = $('#requirement-warning').val();
			var timeslots = [];
			var cohorts = [];
			$('.timeslot').each(function(){
				timeslots.push({
					startTime: $(this).find('.start-time').val(),
					endTime: $(this).find('.end-time').val(),
					maxParticipants: $(this).find('.max-participants').val()
				});
			});
			$('.cohort-name').each(function(){
				cohorts.push($(this).val());
			})
			var signupTest = $('#requiredTest').val();
			if(signupTest != "" && signupTest.indexOf('http://') != 0)
				signupTest = 'http://' + signupTest;
			if(name && description)
			{
				var good = true;
				for(var k=0;k<timeslots.length;k++)
				{
					timeslot = timeslots[k];
					if(!timeslot.startTime || !timeslot.endTime || !timeslot.maxParticipants)
					{
						good = false;
						break;
					}
				}
				for(var k=0;k<cohorts.length;k++)
				{
					if(!cohorts[k])
					{
						good = false;
						break;
					}
				}

				if(good)
				{
					var data = JSON.stringify({
						name: name,
						description: description,
						requirementWarning: requirementWarning,
						timeslots: timeslots,
						cohorts: cohorts,
						signupTest: signupTest
					});
					$.ajax({
						data: data,
						url: "api/newexperiment.php",
						type: "POST",
						contentType: "application/json"
					}).done(function(){
						setTimeout(function(){
							window.location = "home.php";
						},0)
					});
				}
			}
		});



	});
</script>


<?php require('include-end.php') ?>