
	
<?php


if (isset($_POST['tag']) && $_POST['tag'] != ''){
	
	$tag = $_POST['tag'];
	
	require_once 'include/DBFunctions.php';
	$db = new DBFunctions();
	
	$response = array("tag" => $tag, "error" => FALSE);
	
	if($tag == 'login'){
		
		$email = $_POST['email'];
		$password = $_POST['password'];
		
		$user = $db->getUserByEmailAndPassword($email, $password);
		if($user != false){
			$response["error"] = FALSE;
			$response["uid"] = $user["unique_id"];
			$response["user"]["name"] = $user["name"];
			$response["user"]["email"] = $user["email"];
			$response["user"]["created_at"] = $user["created_at"];
			$response["user"]["updated_at"] = $user["updated_at"];
			echo json_encode($response);
		}else{
			$response["error"] = TRUE;
			$response["error_msg"] = "Incorrect email or password";
			echo json_encode($response);
		}
	}
	else if($tag == 'register'){
		
		$name = $_POST['name'];
		$email = $_POST['email'];
		$password = $_POST['password'];
		
		if($db->isUserExisted($email)){
			$response["error"] = TRUE;
			$response["error_msg"] = "User already exist";
			echo json_encode($response);
		}else{
			
			$user = $db->storeUser($name, $email, $password);
			if($user){
				$response["error"] = FALSE;
				$response["uid"] = $user["unique_id"];
				$response["user"]["name"] = $user["name"];
				$response["user"]["email"] = $user["email"];
				$response["user"]["created_at"] = $user["created_at"];
				$response["user"]["updated_at"] = $user["updated_at"];
				echo json_encode($response);
			}
			else{
				$response["error"] = TRUE;
				$response["error_msg"] = "Error occured in Registration";
				echo json_encode($response);
			}
		}
	}
	
	
	else if($tag == 'location'){
		$email = $_POST['email'];
		$lat = $_POST['lat'];
		$lon = $_POST['lon'];
		if($db->isUserExisted($email)){
			$location = $db->storeLocationInsert($email,$lat,$lon);
			if($location){
				$response["error"] = FALSE;		
				$response["uid"] = "location insert success ";
				echo json_encode($response);
			}
			else{
				$response["error"] = TRUE;		
				$response["error_msg"] = "location insert error";
				echo json_encode($response);
			}
			
		}
		else{
			$response["error"] = TRUE;		
			$response["error_msg"] = "email doesnot exsit ";
			echo json_encode($response);

		
		}
		
	}
	
	
	else if($tag == 'invite'){
		$emailfriend = $_POST['emailfriend'];
		$emailmy = $_POST['emailmy'];
		if($db->isUserExisted($emailfriend)){
			if($db->isUserFriendExistInFriendTable($emailmy,$emailfriend)){
				$response["error"] = TRUE;		
				$response["error_msg"] = "You have already invited this friend";
				echo json_encode($response);
			}
			else{
				$friend = $db->storeFriendInsert($emailmy,$emailfriend);
				if($friend){
					$response["error"] = FALSE;
					$response["uid"] = "inserted into friend table";
					echo json_encode($response);
				}
				else{
					$response["error"] = TRUE;
					$response["error_msg"] = "freind insert error";
					echo json_encode($response);
				}
				
			}
			
		}
		else{
			$response["error"] = TRUE;
			$response["error_msg"] = "Your Friend Doesnot have this App";
			echo json_encode($response);
			}
	}
	
		
		
			
	else if($tag == 'accept'){
		$email = $_POST['email'];
		if($db->isUserExsitInFriendsTable($email)){
			$result = $db->FetchAllFriends($email);
			if($result){
				$response["error"] = FALSE;
				$response["uid"] = $result["friend_email"];				
				echo json_encode($response);
			}
			else{
				$response["error"] = TRUE;
				$response["error_msg"] = "fetching the user and friends email error";
				echo json_encode($response);
			}
		}
		else{
			$response["error"] = TRUE;
			$response["error_msg"] = "You have no request to accept";
			echo json_encode($response);
		}
	}
	
		
		
		
		
		
		
		
		
		
		
		
	
		else{
			$response["error"] = TRUE;
			$response["error_msg"] = "Unknown 'tag' value. It should be either login or register";
			echo json_encode($response);
		}
	
	}




else{
	$response["error"] = TRUE;
	$response["error_msg"] = "Required parameter 'tag' is missing";
	
	echo json_encode($response);
}

?>