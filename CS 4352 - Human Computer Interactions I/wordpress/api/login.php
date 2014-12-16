<?php

session_start();

$user_id = $_GET["user_id"];

require 'connectdb.php';

$sql = <<<SQL
SELECT * FROM users WHERE ID = $user_id
SQL;
$retval = mysql_query($sql);
if(!$retval)
{
	header('Location: ../index.php');
	http_response_code(500);
}
$_SESSION['user'] = mysql_fetch_assoc($retval);
header('Location: ../home.php');
?>