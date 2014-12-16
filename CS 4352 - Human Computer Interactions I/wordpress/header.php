<div class="navbar navbar-default">
	<div class="container">
		<div class="navbar-header">
			<a href="/wordpress/home.php" class="navbar-brand" style="vertical-align:top"><i class="fa fa-flask" style="font-size:35px; margin-top: -5px;"></i> Experiment Manager</a>
		</div>
		<ul class="nav navbar-nav navbar-right">
			<li><p class="navbar-text">Welcome, <?php echo $_SESSION['user']['NAME'] ?></p></li>
			<li><a href="api/logout.php">Logout</a></li>
		</ul>
	</div>
</div>

<!-- <header>
	<h1 id="title"><a href="/wordpress/home.php">Experiment Manager</a></h1>
	<nav>
		<ul id="header-nav">
			<li class="header-nav-item">Welcome, <?php echo $_SESSION['user']['NAME'] ?></li>
			<li class="header-nav-item"><a href="api/logout.php">Logout</a></li>
		</ul>
	</nav>
</header> -->