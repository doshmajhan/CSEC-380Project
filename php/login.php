<?php 
    require("config.php");
    if(!empty($_POST)) { 
        // Ensure that the user fills out fields 
        if(empty($_POST['username'])) { 
            die("Please enter a username."); 
        } 
        if(empty($_POST['password'])) { 
            die("Please enter a password."); 
        }
        /*$password = hash('sha256', $_POST['password']); 
        $query = " 
            SELECT 
                1 
            FROM users 
            WHERE 
                username = :username
            AND
                password = :password 
        "; 
        $query_params = array( 
            ':username' => $_POST['username'], 
            ':password' => $_POST['password']
        ); 
        try { 
            $stmt = $db->prepare($query); 
            $result = $stmt->execute($query_params); 
        } 
        catch(PDOException $ex){ 
            die("Failed to run query: " . $ex->getMessage()); 
        } 
        $row = $stmt->fetch(); 
        if(!$row){ 
            die("Username or password is incorrect");
            header("Location: index.php"); 
            die("Redirecting to index.php"); 
        } 
         */

        $ch = curl_init();
        $data = '';
        $fields = array('user' => $_POST['username'], 'pass' => $_POST['password']); 
        foreach($fields as $k => $v){
            $data .= $k . '=' . $v . '&';
        }
        $data = rtrim($data, '&');

        //echo $data;
        curl_setopt($ch, CURLOPT_URL, "http://54.208.87.70:8181/login");
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-type: application/x-www-form-urlencoded'));

        $server_output = curl_exec($ch);
        if($server_ouput == "LDAP Auth Failed"){
            die("Incorrect Credentials");
        }
        //echo $server_output;
        $token = explode("=", $server_output)[1];
        $info = curl_getinfo($ch);
        //print_r($info);
        curl_close($ch);
        // Make token then redirect to home
        setcookie("skitter", $token, time() + (86400 * 30), "/");
        header("Location: home.php"); 
    } 
?>

