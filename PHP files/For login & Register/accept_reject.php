<?php

$servername = "mysql3.gear.host";
$username = "applogindb";
$password = "Di78B?!Qu5ou";
$dbname="applogindb";


$user_email =$_GET['user_email'];

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM friends where friend_email = '".$user_email."' and is_friend='false' order by friends_id desc limit 1";
$result1 = $conn->query($sql);
   if ($result1->num_rows > 0) {
        while($row1 = $result1->fetch_assoc()) {
            $lat_long_array[] = $row1;
        }
    } else {
        echo "0 results";
    }

echo json_encode($lat_long_array);
?>