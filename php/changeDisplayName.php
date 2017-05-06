<?php
    require("config.php");
    if(!empty($_POST)){
        if(empty($_POST['display'])){
            die("Please enter a display name");
        }
        
        // Get username here
        $username = ""

        $query = "
            SELECT
                1
            FROM users
            WHERE
                username = :username
        ";
        
        $query_params = array(
            ':username' => $username
        );
        try {
            $stmt = $db->prepare($query);
            $result = $stmt->execute($query_params);
        }
        catch(PDOException $ex){
            die("Failed to run query: " . $ex->getMessage());
        }
        $row = $stmt->fetch(PDO::FETCH_OBJ);
        $id = $row->id;

        $query = "
            UPDATE
                displayname = :displayname
            WHERE
                id = :id
        ";

        $query_params = array(
            ':displayname' => $_POST['display'],
            ':id' => $id
        );
        try{
            $stmt = $db->prepare($query);
            $result = $stmt->execute($query_params);
        }
        catch(PDOException $ex){
            die("Failed to run query: " . $ex->getMessage());
        }
        header('Location: home.php');
    }
?>
