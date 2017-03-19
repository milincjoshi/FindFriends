<?php

class DBFunctions{
	
	private $db;
	
	function __construct(){
		require_once 'DBConnect.php';
		
		$this->db = new DBConnect();
		$this->db->connect();
	}
	
	function __destruct(){
		
	}
	
	
	public function getUserUID($email){
		$result = mysql_query("SELECT * FROM users WHERE email = '$email' ") or die(mysql_error());
		$no_of_rows = mysql_num_rows($result);
		if($no_of_rows > 0){
			$result = mysql_fetch_array($result);
			$uid = $result['unique_id'];
			return $uid;
		}
		else
		{
			return false;
		}		
	
	}
	
	public function isLocationExsited($uid){
		$result = mysql_query("SELECT * from location WHERE uid = '$uid'");
		$no_of_rows = mysql_num_rows($result);
		if($no_of_rows > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public function storeLocationInsert($email, $lat, $lon){
		$result = mysql_query("INSERT INTO location(email, lat, lon, updated_at) VALUES('$email', '$lat', '$lon', NOW())");
		if($result){
			$locationId = mysql_insert_id();
			$result = mysql_query("SELECT * FROM location WHERE location_id = $locationId");
			return mysql_fetch_array($result);
		}else{
			return false;
		}
	}
	

	public function storeFriendInsert($emailmy,$emailfriend){
		$result = mysql_query("INSERT INTO friends(user_email, friend_email, is_friend) VALUES('$emailmy', '$emailfriend', 'false')");
		if($result){
			$friendsId = mysql_insert_id();
			$result = mysql_query("SELECT * FROM friends WHERE friends_id = $friendsId");
			return mysql_fetch_array($result);
		}else{
			return false;
		}
	
	}
	
	
	public function isUserFriendExistInFriendTable($emailmy,$emailfriend){
		$result = mysql_query("SELECT * FROM friends WHERE user_email = '$emailmy' AND  friend_email='$emailfriend'");
		$no_of_rows = mysql_num_rows($result);
		if($no_of_rows > 0){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
	public function storeUser($name, $email, $password){
		$uuid = uniqid('', true);
		$hash = $this->hashSSHA($password);
		$encrypted_password = $hash["encrypted"];
		$salt = $hash["salt"];
		$result = mysql_query("INSERT INTO users(unique_id, name, email, encrpted_password, salt, created_at) VALUES('$uuid', '$name', '$email', '$password', '$salt', NOW())");
		
		if($result){
			$uid = mysql_insert_id();
			$result = mysql_query("SELECT * FROM users WHERE uid = $uid");
			
			return mysql_fetch_array($result);
		}else{
			return false;
		}
		
	}
	
	public function getUserByEmailAndPassword($email, $password){
		$result = mysql_query("SELECT * FROM users WHERE email = '$email' ") or die(mysql_error());
		$no_of_rows = mysql_num_rows($result);
		if($no_of_rows > 0){
			$result = mysql_fetch_array($result);
			$salt = $result['salt'];
			$encrypted_password = $result['encrpted_password'];
			$hash = $this->checkhashSSHA($salt, $password);
			
			//if($encrypted_password == $hash['encrypted']){
			if($encrypted_password == $password){
				return $result;
			}
			else{
				return false;
			}
		}		
	}
	
	
	public function isUserExisted($email){
		$result = mysql_query("SELECT email from users WHERE email = '$email' ");
		$no_of_rows = mysql_num_rows($result);
		if($no_of_rows > 0){
			return true;
		}else{
			return false;
		}
	}
	
	
	public function hashSSHA($password){
		$salt = sha1(rand());
		$salt = substr($salt, 0, 10);
		$encrypted = base64_encode(sha1($password . $salt, true) . $salt);
		$hash = array("salt" => $salt, "encrypted" => $encrypted);
		
		return $hash;
	}
	
	public function checkhashSSHA($salt, $password){
		
		$encrypted = base64_encode(sha1($password . $salt, true) . $salt);
		$hash = array("salt" => $salt, "encrypted" => $encrypted);
		return $hash;
	}
	
}
?>