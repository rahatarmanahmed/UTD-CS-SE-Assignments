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
        <table>
            <tr>
                <td style="padding-right:10px;text-align:right">Max Participants: </td>
                <td>{{maxParticipants}}</td>
            </tr>
        </table>
        </td>
        <td class="participant-cell">
        </td>
        <td>
            <a class="btn btn-default" href="managetimeslot.php?timeslot_id={{id}}"><i class="fa fa-pencil"></i> Edit</a>
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

<div class="legend">
	<legend>Manage Timeslots</legend>
</div>

<a id="btn-add-timeslot" href="addtimeslot.php" class="btn btn-sm btn-primary email-participants-btn pull-right"><i class="fa fa-plus"></i> Add Timeslot</a>

<table id="participant-table" class="table">
    <tr>
        <th>Timeslot</th>
        <th>Participants</th>
        <th> </th>
    </tr>
</table>
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

    $('#email-all-link').attr('href', 'mailto:'+emails.join(','));

    $('#btn-add-timeslot').attr('href','addtimeslot.php?experiment_id='+experimentId);

});
</script>


<?php require('include-end.php') ?>