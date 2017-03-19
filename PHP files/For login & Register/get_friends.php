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
$sql = "SELECT * FROM friends WHERE user_email='".$user_email."'";
$result = $conn->query($sql);

$email_array = array();//array for holding all the email values
if ($result->num_rows > 0) {
       while($row = $result->fetch_assoc()) {
       
        if($row["is_friend"] == 'true'){
         if (!in_array($row["friend_email"], $email_array)) {
            $email_array[] = $row["friend_email"]; 
        }
    }
    }
} else {
    echo "0 results";
}

$lat_long_array=array();
foreach ($email_array as &$email_value) {

    $sql1 = "select * from location where email='".$email_value."' order by updated_at DESC LIMIT 1";
    $result1 = $conn->query($sql1);

    if ($result1->num_rows > 0) {
        while($row1 = $result1->fetch_assoc()) {
            $lat_long_array[] = $row1;
        }
    } else {
        echo "0 results";
    }
}
echo json_encode($lat_long_array);
?>