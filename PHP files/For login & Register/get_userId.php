
<?php

$servername = "mysql3.gear.host";
$username = "applogindb";
$password = "Di78B?!Qu5ou";
$dbname="applogindb";


$user_email ="milincjoshi@gmail.com";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM friends WHERE user_email='".$user_email;
$result = $conn->query($sql);





?>