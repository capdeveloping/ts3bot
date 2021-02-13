<?php
session_start();

if( ! isset($_SESSION['userid']) ){
    header("Refresh:0; url=/login.php");
    exit();
}

include $_SERVER["DOCUMENT_ROOT"] . "/functions.php";

saveCurrentInstance($_POST, $_SESSION["instances"]);

if( ! isset($_SESSION["config"])){
    $_SESSION["config"] = [];
}
if( ! isset($_SESSION["configPath"])){
    $_SESSION["configPath"] = "";
}
if( ! isset($_SESSION["configFolderPath"])){
    $_SESSION["configFolderPath"] = "";
}
if( ! isset($_SESSION["functions"])){
    $_SESSION["functions"] = [];
}
if( ! isset($_SESSION["instances"])){
    $_SESSION["instances"] = [];
}

$_SESSION["instance_name"] = loadCurrentInstance();
$_SESSION["nav_expanded"] = FALSE;
$_SESSION["instance_config_path"] = "/data/configs/instancemanager.cfg";

$retArray = loadInstanceConfig($_SESSION["instance_config_path"], $_SESSION["instance_name"]);
if( ! empty($retArray)){
    $_SESSION["instances"]  = $retArray[0];
    $_SESSION["instance_name"] = $retArray[1];
    $_SESSION["configPath"] = $_SESSION["instances"][$_SESSION["instance_name"]]["instance_config_pfad"];
}

if( ! empty($_SESSION["configPath"])){
    $_SESSION["configFolderPath"] = dirname($_SESSION["configPath"]) . "/";
    $retValue = loadServerconfig($_SESSION["configPath"]);
    $_SESSION["config"] = $retValue[0];
    $_SESSION["functions"] = $retValue[1];
}
?>