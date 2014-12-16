<?php require('include-start.php') ?>

<!-- <div id="login-box">
	<input id="email" type="text" placeholder="Email address"/>
	<input type="password" placeholder="Password"/>
	<a id="login-btn" href="home.php" class="btn">Login</a>
</div> -->

	<div class="panel panel-default">
	  <div class="panel-heading"><h3 class="panel-title"><strong>Login</strong></h3></div>
	  <div class="panel-body">
	   <form role="form">
	  <div class="form-group">
	    <label for="exampleInputEmail1">Email Address</label>
	    <input type="email" class="form-control" id="email" placeholder="Email Address">
	  </div>
	  <div class="form-group">
	    <label for="exampleInputPassword1">Password</label>
	    <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
	  </div>
	  <button id="login-btn" class="btn btn-primary">Login</button>
	</form>
	  </div>
	</div>

	<div class="legend">
		<legend>Prototype Accounts</legend>
		<ul style="margin-left: 25px">
			<li><a id="account-experimenter" href="#">Experimenter</a></li>
			<li><a id="account-participant" href="#">Participant</a></li>
		</ul>
	</div>

<script>
	$('.navbar-right, .sidebar-nav ul').hide();
	$('#login-btn').click(function(e){
		e.preventDefault();
		var email = $('#email').val().toLowerCase();
		if(email == experimenter_email)
		{
			window.location = "api/login.php?user_id=1"
		}
		else if(email == participant_email)
		{
			window.location = "api/login.php?user_id=2"
		}
		else
		{
			alert("The login emails used for this prototype are:\n"+
					experimenter_email+"\n"+
					participant_email+"\n\nAny password will work.");
		}
	});

	function fillLogin(email)
	{
		$('#email').val(email);
		$('#exampleInputPassword1').val("password");
	}

	$('#account-experimenter').click(fillLogin.bind(null, 'experimenter@utd.edu'));
	$('#account-participant').click(fillLogin.bind(null, 'participant@utd.edu'));
</script>

<?php require('include-end.php') ?>