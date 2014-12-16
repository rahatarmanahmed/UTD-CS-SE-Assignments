<?php require('include-start.php') ?>

<script id="cohort" type="text/html">
	<div class="form-group cohort" data-cohort-id="{{id}}">
		<label class="col-xs-3 control-label" class="control-label">Cohort Name</label>
		<div class="col-xs-7">
			<input type="text" class="input-xs form-control cohort-name width100" value="{{name}}" required>
		</div>
		<div class="col-xs-2">
			<a href="#" title="Edit" class="form-control btn btn-danger btn-delete-cohort" style="width:100%;float:right">Delete</a>
		</div>
	</div>
</script>

<form id="new-experiment-form" class="form-horizontal" role="form">
	<div class="legend">
		<legend>Edit Experiment Details</legend>
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
		<legend>Edit Tests</legend>
	</div>
	<div class="form-group">
		<label for="requiredTest" class="col-xs-3 control-label">Pre-Signup Test</label>
		<div class="col-xs-9">
			<input type="text" class="form-control" id="requiredTest">
		</div>
	</div>
	<div>
		<p class="col-xs-offset-3 col-xs-9">This is an optional field. You may input a link to a Qualtrics test that you wish for all participants to take before signing up for the experiment.</p>
	</div>

	<div class="legend">
		<legend>&nbsp;</legend>
	</div>
	<div class="form-group">
		<div class="pull-right">
			<a href="home.php" class="btn btn-default">Cancel</a>
			<button id="btn-submit-experiment" type="submit" class="btn btn-primary">Save Changes</button>
		</div>
	</div>
</form>


<script>

	$(function(){
		var cohorts = [];
		window.cohortsArray = cohorts;
		function addCohort(c)
		{
			var cohort = ich.cohort(c);
			$('#cohorts').append(cohort);
			if(c.id != 'new')
				cohorts.push(c);
		}

		var id = getParameterByName('id');
		if(!id)
			window.location = "home.php";
		var editExperiment;
		for(var k=0;k<experiments.length;k++)
		{
			var experiment = experiments[k];
			if(experiment.id == id)
				editExperiment = experiment;
		}

		$('#experimentName').val(editExperiment.name);
		$('#description').val(editExperiment.description);
		$('#requirement-warning').val(editExperiment.requirementWarning);
		$('#requiredTest').val(editExperiment.signupTest);
		for(var k=0;k<editExperiment.cohorts.length;k++)
		{
			var cohort = editExperiment.cohorts[k];
			addCohort(cohort);
		}

		$('#btn-add-cohort').click(function(e){
			e.preventDefault();
			var cohort = {
				name: '',
				id:'new',
				status: 'new'
			}
			addCohort(cohort);
		});

		$('#cohorts').on('click', '.btn-delete-cohort', function(e){
			e.preventDefault();
			if($('.cohort').length == 1)
				alert("You must have at least one cohort.");
			else
			{	
				if(confirm("Are you sure you want to delete this cohort? Participants in this cohort will be moved to another cohort."))
				{
					var cohort = $(this).closest('.cohort');
					var id = cohort.data('cohort-id');
						
					for(var k=0;k<cohorts.length;k++)
					{
						if(cohorts[k].id == id)
						{
							cohorts[k].status = "delete";
							break;
						}
					}
					cohort.remove();
				}
			}
		});

		$('form').submit(function(e){
			e.preventDefault();
			var name = $('#experimentName').val();
			var description = $('#description').val();
			var requirementWarning = $('#requirement-warning').val();
			var signupTest = $('#requiredTest').val();
			if(signupTest != "" && signupTest.indexOf('http://') != 0)
				signupTest = 'http://' + signupTest;
			var good = name && description;
			$('.cohort').each(function(i,cohort){
				var name = $(cohort).find('.cohort-name').val();
				if(name == "")
					good = false;
				var id = $(cohort).data('cohort-id');
				if(id == 'new')
					cohorts.push({
						name: name,
						status: 'new'
					});
				else
					for(var k=0;k<cohorts.length;k++)
					{
						if(cohorts[k].id == id)
						{
							cohorts[k].name = name;
							cohorts[k].status = "changed";
						}
					}
			});
			if(good)
			{
				var data = JSON.stringify({
					id: id,
					name: name,
					description: description,
					requirementWarning: requirementWarning,
					cohorts: cohorts,
					signupTest: signupTest
				});
				$.ajax({
					data: data,
					url: "api/newexperiment.php",
					type: "POST",
					contentType: "application/json"
				}).done(function(){
					window.location = "home.php";
				});
			}
		});



	});
</script>


<?php require('include-end.php') ?>