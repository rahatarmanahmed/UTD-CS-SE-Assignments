<?php require('include-start.php') ?>

<script id="timeslot" type="text/html">
    <tr>
        <td>
        <table style="padding-bottom:10px;padding-top:10px">
            <tr>
                <td style="padding-right:10px;text-align:right">Start Time: </td>
                <td>{{formattedStartTime}}</td>
            </tr>
            <tr>
                <td style="padding-right:10px;text-align:right">End Time: </td>
                <td>{{formattedEndTime}}</td>
            </tr>
        </table>
        </td>
        <td class="participant-cell">
        </td>
    </tr>
</script>

<script id="participant" type="text/html">
    <tr>
        <td>
        <a href="mailto:{{username}}"><i class="fa fa-envelope"></i> {{name}}</a>
        </td>
    </tr>
</script>

<script id="cohort" type="text/html">
    <h4>{{name}}</h4>
    <ul class="cohort-participants" style="margin-left:20px">
    </ul>
</script>

<script id="cohortParticipant" type="text/html">
    <li>
        <a href="mailto:{{username}}"><i class="fa fa-envelope"></i> {{name}} </a>
        <!--<a href="movecohort.php?experiment_id={{experimentId}}&signup_id={{signupId}}" class="btn btn-default"><i class="fa fa-pencil"></i> Change Cohort</a>-->
        <a href="movecohort.php?experiment_id={{experimentId}}&signup_id={{signupId}}" class="">(<i class="fa fa-pencil"></i> Move)</a>
    </li>
</script>

<div class="legend">
	<legend>Participants</legend>
</div>

<a id="email-all-link" href="newexperiment.php" class="btn btn-sm btn-primary email-participants-btn pull-right"><i class="fa fa-envelope"></i> Email All Participants</a>

<table id="participant-table" class="table">
    <tr>
        <th>Timeslot</th>
        <th>Participants</th>
    </tr>
</table>
<div class="legend">
    <legend>Cohorts</legend>
</div>
<div id="cohorts">
    
</div>
<script>
$(function(){
	var experimentId = getParameterByName('experiment_id');
    var experiment = getExperimentById(experimentId);
    var emails = [];
    for(var k=0;k<experiment.timeslots.length;k++)
    {
        var timeslot = experiment.timeslots[k];
        var generated = ich.timeslot(timeslot);
        var participantCell = generated.find('.participant-cell');
        for(var j=0;j<timeslot.participants.length;j++)
        {
            var participant = timeslot.participants[j];
            emails.push(participant.username);
            participantCell.append(ich.participant(participant));
        }
        $('#participant-table').append(generated);
    }

    for(var k=0;k<experiment.cohorts.length;k++)
    {
        var cohort = experiment.cohorts[k];
        var generated = ich.cohort(cohort);
        for(var j=0;j<cohort.participants.length;j++)
        {
            var participant = cohort.participants[j];
            participant.experimentId = experimentId;
            $(generated.get(2)).append(ich.cohortParticipant(participant));
        }
        if(cohort.participants.length == 0)
            $(generated.get(2)).append('<p> There are no participants in this cohort.');
        $('#cohorts').append(generated);
    }


    $('#email-all-link').attr('href', 'mailto:'+emails.join(','));

});
</script>


<?php require('include-end.php') ?>