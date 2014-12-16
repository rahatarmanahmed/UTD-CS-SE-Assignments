<?php

error_reporting(E_ALL);

$hostname = "localhost";
$username = "root";
$password = "";
$database = "wordpress";

$conn = mysql_connect($hostname, $username, $password);
$db = mysql_select_db($database);