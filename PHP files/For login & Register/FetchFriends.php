<?php

$servername = "mysql3.gear.host";
$username = "applogindb";
$password = "Di78B?!Qu5ou";
$dbname="applogindb";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$email = 'jaymin@gmail.com';
		
$sql = "SELECT * FROM friends WHERE user_email='".$email."'";
$result = $conn->query($sql);
while($row1 = $result->fetch_assoc()) {
$friendEmail_array[] = $row1['friend_email'];
}
echo json_encode($friendEmail_array);
?>