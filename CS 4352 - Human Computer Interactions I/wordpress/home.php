<?php require('include-start.php') ?>

<script id="experiment" type="text/html">
<div class="panel panel-default">
    <div class="panel-heading accordion-toggle" data-toggle="collapse" data-target="#collapse{{id}}">
         <h4 class="panel-title">
            <a href="#">
                {{name}}
            </a>
        </h4>
    </div>
    <div id="collapse{{id}}" class="experiment panel-body panel-collapse collapse in" data-experiment-id="{{id}}">
        <h5><strong>Created by: </strong><a href="mailto:{{userEmail}}"><i class="fa fa-envelope"></i> {{user}}</a></h5>
        <p><strong>Description: </strong>{{description}}</p>
        <p><strong># Participants: </strong>{{totalParticipants}} / {{totalMaxParticipants}} participants</p>
        <div id="requirement-warning" class="alert alert-warning requirements-required">
            <h5><strong><i class="fa fa-warning"></i> Experiment Requirements:</strong></h5>
            <p>{{requirementWarning}}</p>
        </div>
        <a href="viewparticipants.php?experiment_id={{id}}" class="btn btn-default"><i class="fa fa-users"></i> Participants</a>
        <a href="editexperiment.php?id={{id}}" class="btn btn-default btn-edit-experiment"><i class="fa fa-pencil"></i> Edit</a>
        <a href="managetimeslots.php?experiment_id={{id}}" class="btn btn-default"><i class="fa fa-clock-o"></i> Manage Timeslots</a>
        <a href="#" class="btn btn-danger btn-delete-experiment"><i class="fa fa-times-circle"></i> Delete</a>
    </div>
</div>
</script>


<div class="legend">
	<legend>Experiments</legend>
</div>

<a href="newexperiment.php" class="btn btn-sm btn-primary new-experiment-btn pull-right"><i class="fa fa-plus"></i> Create New Experiment</a>
<div class="panel-group weird-button-margin" id="accordion">
	
</div>
<script>
$(function(){
    $('.sidebar-nav-item a[href="/wordpress/home.php"]').parent().addClass("active")
	for(var k=0; k<experiments.length; k++)
	{
		var experiment = experiments[k];
		if(experiment.user !== currentUser.NAME)
			break;
		var generated = ich.experiment(experiment);
        if(!experiment.requirementWarning)
            generated.find('#requirement-warning').hide();
        generated.find('.btn-delete-experiment').click(function(){
            var result = confirm("Are you sure you want to delete this experiment?");
            if(result)
            {
                var id = $(this).closest('.experiment').data('experiment-id');
                window.location = "api/deleteexperiment.php?id="+id;
            }
        });
		$('#accordion').append(generated);
	}
});
</script>


<?php require('include-end.php') ?>