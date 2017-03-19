<?php

$servername = "mysql3.gear.host";
$username = "applogindb";
$password = "Di78B?!Qu5ou";
$dbname="applogindb";


$user_email =$_GET['user_email'];
$friend_email = $_GET['friend_email'];


$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "DELETE FROM friends WHERE friend_email = '".$user_email."' and user_email=  '".$friend_email."'";

if ($conn->query($sql) === TRUE) {
   // echo "Record updated successfully";
}
?>