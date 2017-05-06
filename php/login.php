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
        $password = hash('sha256', $_POST['password']); 
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

        // Make token then redirect to home
        header("Location: home.php"); 
    } 
?>

