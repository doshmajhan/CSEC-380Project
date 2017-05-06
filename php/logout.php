<?php 
    require("config.php"); 
    unset($_SESSION['token']);
    header("Location: index.php"); 
    die("Redirecting to: index.php");
?>
