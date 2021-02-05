<?php
include "functions.php";

$config = [];
$configPath = "";
$configFolderPath = "";
$functions = [];
$instance_name = loadCurrentInstance();
$instances = [];
$nav_expanded = FALSE;
$instance_config_path = "/data/configs/instancemanager.cfg";

$retArray = loadInstanceConfig($instance_config_path, $instance_name);
if( ! empty($retArray)){
    $instances = $retArray[0];
    $instance_name = $retArray[1];
    $configPath = $instances[$instance_name]["instance_config_pfad"];
}else{
    $instance_name = "";
}

if( ! empty($configPath)){
    $configFolderPath = dirname($configPath) . "/";
    $retValue = loadServerconfig($configPath);
    $config = $retValue[0];
    $functions = $retValue[1];
}
?>