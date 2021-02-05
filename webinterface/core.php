<?php
    if( isset($_GET['id']) ){
        $currentFile = fopen(".current", "w+") or die("Unable to open file!");
        fwrite($currentFile, $_GET['id']);
        fclose($currentFile);
    }
    require_once('_preload.php');


    $saved = FALSE;
    if (isset($_POST['addClientAfk'])){
        $addClientAfk = TRUE;
    }
    if (isset($_POST['addClientMove'])){
        $addClientMove = TRUE;
    }
    if (isset($_POST['addWelcomeMessage'])){
        $addWelcomeMessage = TRUE;
    }

    if (isset($_POST['update'])){
        $config["language"] = $_POST['language'];
        $config["ts3_server_floodrate"] = $_POST['ts3_server_floodrate'];
        $config["ts3_server_ip"] = $_POST['ts3_server_ip'];
        $config["ts3_server_port"] = $_POST['ts3_server_port'];
        $config["ts3_server_query_port"] = $_POST['ts3_server_query_port'];
        $config["ts3_server_query_login_name"] = $_POST['ts3_server_query_login_name'];
        $config["ts3_server_query_login_password"] = $_POST['ts3_server_query_login_password'];
        $config["ts3_bot_nickname"] = $_POST['ts3_bot_nickname'];
        $config["ts3_bot_nickname2"] = $_POST['ts3_bot_nickname2'];
        $config["ts3_bot_channel_id"] = $_POST['ts3_bot_channel_id'];
        $config["bot_admin"] = $_POST['bot_admin'];
        $config["bot_full_admin"] = $_POST['bot_full_admin'];

        if( empty($_POST["enableTwitch"]) && array_key_exists("Twitch", $functions)) {
            $retValue = removeFunction($config, $functions, "Twitch");
            $config = $retValue[0];
            $functions = $retValue[1];
        } else if( ! array_key_exists("Twitch", $functions) && $_POST["enableTwitch"]) {
            $retValue = addTwitch($config, $functions, $_POST["twitch_key"], $configFolderPath);
            $config = $retValue[0];
            $functions = $retValue[1];
        }
        if( empty($_POST["enableViewer"]) && array_key_exists("Viewer", $functions)) {
            $retValue = removeCustomFunction($config, $functions, "Viewer", "ts3_viewer");
            $config = $retValue[0];
            $functions = $retValue[1];
        } else if( ! array_key_exists("Viewer", $functions) && $_POST["enableViewer"]) {
            $retValue = addTs3Viewer($config, $functions, $_POST["viewer_key"], $configFolderPath);
            $config = $retValue[0];
            $functions = $retValue[1];
        }
        if( empty($_POST["enableChannelAutoCreate"]) && array_key_exists("ChannelAutoCreate", $functions)) {
            $retValue = removeCustomFunction($config, $functions, "ChannelAutoCreate", "channel_check");
            $config = $retValue[0];
            $functions = $retValue[1];
        } else if( ! array_key_exists("ChannelAutoCreate", $functions) && $_POST["enableChannelAutoCreate"]) {
            $retValue = addChannelAutoCreate($config, $functions, $_POST["channelautocreate_key"], $configFolderPath);
            $config = $retValue[0];
            $functions = $retValue[1];
        }
        if( empty($_POST["enableVersionChecker"]) && array_key_exists("VersionChecker", $functions)) {
            $retValue = removeCustomFunction($config, $functions, "VersionChecker", "version");
            $config = $retValue[0];
            $functions = $retValue[1];
        } else if( ! array_key_exists("VersionChecker", $functions) && $_POST["enableVersionChecker"]) {
            $retValue = addVersionChecker($config, $functions, $_POST["versionchecker_key"]);
            $config = $retValue[0];
            $functions = $retValue[1];
        }
        if( empty($_POST["enableAutoRemove"]) && array_key_exists("AutoRemove", $functions)) {
            $retValue = removeCustomFunction($config, $functions, "AutoRemove", "auto_remove");
            $config = $retValue[0];
            $functions = $retValue[1];
        } else if( ! array_key_exists("AutoRemove", $functions) && $_POST["enableAutoRemove"]) {
            $retValue = addAutoRemove($config, $functions, $_POST["autoremove_key"]);
            $config = $retValue[0];
            $functions = $retValue[1];
        }
        if( empty($_POST["enableAcceptRules"]) && array_key_exists("AcceptRules", $functions)) {
            $retValue = removeCustomFunction($config, $functions, "AcceptRules", "accept_rules");
            $config = $retValue[0];
            $functions = $retValue[1];
        } else if( ! array_key_exists("AcceptRules", $functions) && $_POST["enableAcceptRules"]) {
            $retValue = addAcceptRules($config, $functions, $_POST["acceptrules_key"], $configFolderPath);
            $config = $retValue[0];
            $functions = $retValue[1];
        }
        if( $_POST["enableClientAfk"] ) {
            $retValue = addClientAfk($config, $functions, $_POST["clientafk_key"]);
            $config = $retValue[0];
            $functions = $retValue[1];
        }
        if( $_POST["enableClientMove"] ) {
            $retValue = addClientMove($config, $functions, $_POST["clientmove_key"]);
            $config = $retValue[0];
            $functions = $retValue[1];
        }

        if( $_POST["enableWelcomeMessage"] ) {
            $retValue = addWelcomeMessage($config, $functions, $_POST["welcomemessage_key"], $configFolderPath);
            $config = $retValue[0];
            $functions = $retValue[1];
        }

        foreach($_POST as $key => $value) {
            $tmpkey = str_replace("clientafk_key-", "", $key);
            if(str_starts_with($key, "clientafk_key-") && ! isset( $_POST["enableClientAfk-" . $tmpkey] )){
                $retValue = removeSpecialFunction($config, $functions, "ClientAFK", "client_afk", $tmpkey);
                $config = $retValue[0];
                $functions = $retValue[1];
            }
            $tmpkey = str_replace("clientmove_key-", "", $key);
            if(str_starts_with($key, "clientmove_key-") && ! isset( $_POST["enableClientMove-" . $tmpkey] )){
                $retValue = removeSpecialFunction($config, $functions, "ClientMove", "client_moved", $tmpkey);
                $config = $retValue[0];
                $functions = $retValue[1];
            }
            $tmpkey = str_replace("welcomemessage_key-", "", $key);
            if(str_starts_with($key, "welcomemessage_key-") && ! isset( $_POST["enableWelcomeMessage-" . $tmpkey] )){
                $retValue = removeSpecialFunction($config, $functions, "WelcomeMessage", "welcome", $tmpkey);
                $config = $retValue[0];
                $functions = $retValue[1];
            }
        }

        $config["functions"] = updateFunctionString($functions);
        saveConfig($config, $configPath);
    }
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Funktion - Basis Einstellungen</title>
        <link href="css/styles.css" rel="stylesheet" />
        <link href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css" rel="stylesheet" crossorigin="anonymous" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/js/all.min.js" crossorigin="anonymous"></script>
    </head>
    <body class="sb-nav-fixed">
        <?php
            require_once('_nav-header.php');
        ?>
        <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
                <?php
                    require_once('_nav.php');
                ?>
            </div>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">Core</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item active">Core</li>
                        </ol>
                        <form class="form-horizontal" data-toggle="validator" name="addFunction" method="POST">
                            <div class="row">
                                <div class="col-xl-6">
                                    <div class="card mb-4">
                                        <div class="card-header">
                                            <i class="fas fa-chart-area mr-1"></i>
                                            Basis Einstellungen
                                        </div>
                                        <br>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="language">Sprache</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="language" type="text" name="language" placeholder="Enter new language" value=<?php if(array_key_exists("language", $config)){ echo '"' . $config["language"] . '"' . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="ts3_server_floodrate">Floodrate</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="ts3_server_floodrate" type="text" name="ts3_server_floodrate" placeholder="Enter new ts3_server_floodrate" value=<?php if(array_key_exists("ts3_server_floodrate", $config)){ echo '"' . $config["ts3_server_floodrate"] . '"' . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="ts3_server_ip">Server IP</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="ts3_server_ip" type="text" name="ts3_server_ip" placeholder="Enter new ts3_server_ip" value=<?php if(array_key_exists("ts3_server_ip", $config)){ echo '"' . $config["ts3_server_ip"] . '"' . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="ts3_server_port">Server Port</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="ts3_server_port" type="text" name="ts3_server_port" placeholder="Enter new ts3_server_port" value=<?php if(array_key_exists("ts3_server_port", $config)){ echo '"' . $config["ts3_server_port"] . '"' . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="ts3_server_query_port">Query Port</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="ts3_server_query_port" type="text" name="ts3_server_query_port" placeholder="Enter new ts3_server_query_port" value=<?php if(array_key_exists("ts3_server_query_port", $config)){ echo '"' . $config["ts3_server_query_port"] . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="ts3_server_query_login_name">Query Login Name</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="ts3_server_query_login_name" type="text" name="ts3_server_query_login_name" placeholder="Enter new ts3_server_query_login_name" value=<?php if(array_key_exists("ts3_server_query_login_name", $config)){ echo '"' . $config["ts3_server_query_login_name"] . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="ts3_server_query_login_password">Query Login Password</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="ts3_server_query_login_password" type="text" name="ts3_server_query_login_password" placeholder="Enter new ts3_server_query_login_password" value=<?php if(array_key_exists("ts3_server_query_login_password", $config)){ echo '"' . $config["ts3_server_query_login_password"] . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="ts3_bot_nickname">Bot Nickname</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="ts3_bot_nickname" type="text" name="ts3_bot_nickname" placeholder="Enter new ts3_bot_nickname" value=<?php if(array_key_exists("ts3_bot_nickname", $config)){ echo '"' . $config["ts3_bot_nickname"] . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="ts3_bot_nickname2">Bot Nickname 2</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="ts3_bot_nickname2" type="text" name="ts3_bot_nickname2" placeholder="Enter new ts3_bot_nickname2" value=<?php if(array_key_exists("ts3_bot_nickname2", $config)){ echo '"' . $config["ts3_bot_nickname2"] . '"' . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="ts3_bot_channel_id">Default Channel ID</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="ts3_bot_channel_id" type="text" name="ts3_bot_channel_id" placeholder="Enter new ts3_bot_channel_id" value=<?php if(array_key_exists("ts3_bot_channel_id", $config)){ echo '"' . $config["ts3_bot_channel_id"] . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="bot_admin">Bot Admins (als Komma Liste)</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="bot_admin" type="text" name="bot_admin" placeholder="Enter new bot_admin" value=<?php if(array_key_exists("bot_admin", $config)){ echo '"' . $config["bot_admin"] . '"';}else{ echo '""';} ?> />
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-5 control-label" for="bot_full_admin">Bot Full Admins (als Komma Liste)</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="bot_full_admin" type="text" name="bot_full_admin" placeholder="Enter new bot_full_admin" value=<?php if(array_key_exists("bot_full_admin", $config)){ echo '"' . $config["bot_full_admin"] . '"';}else{ echo '""';} ?> required/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xl-6">
                                    <div class="card mb-4">
                                        <div class="card-header">
                                            <i class="fas fa-chart-bar mr-1"></i>
                                            Funktionen
                                        </div>
                                        <br>
                                        <label class="col-sm-5 control-label" >Können nur einmal vewendet werden</label>
                                        <br>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name="enableTwitch" id="switchTwitch" type="checkbox" <?php if(array_key_exists("Twitch", $functions)){ echo "checked";} ?> >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for="switchTwitch">Twitch</label>
                                            <div class="col-sm-4">
                                                <input <?php if(array_key_exists("Twitch", $functions)){ echo 'readonly';} ?> class="form-control" id="inputTwitchKey" type="text" name="twitch_key" value=<?php if(array_key_exists("Twitch", $functions)){ echo '"' . $functions["Twitch"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name="enableViewer" id="switchViewer" type="checkbox" <?php if(array_key_exists("Viewer", $functions)){ echo "checked";} ?> >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for="switchViewer">Ts3 Viewer</label>
                                            <div class="col-sm-4">
                                                <input <?php if(array_key_exists("Viewer", $functions)){ echo 'readonly';} ?> class="form-control" id="inputViewerKey" type="text" name="viewer_key" value=<?php if(array_key_exists("Viewer", $functions)){ echo '"' . $functions["Viewer"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name="enableChannelAutoCreate" id="switchChannelAutoCreate" type="checkbox" <?php if(array_key_exists("ChannelAutoCreate", $functions)){ echo "checked";} ?> >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for="switchChannelAutoCreate">ChannelAutoCreate</label>
                                            <div class="col-sm-4">
                                                <input <?php if(array_key_exists("ChannelAutoCreate", $functions)){ echo 'readonly';} ?> class="form-control" id="inputChannelAutoCreateKey" type="text" name="channelautocreate_key" value=<?php if(array_key_exists("ChannelAutoCreate", $functions)){ echo '"' . $functions["ChannelAutoCreate"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name="enableVersionChecker" id="switchVersionChecker" type="checkbox" <?php if(array_key_exists("VersionChecker", $functions)){ echo "checked";} ?> >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for="switchVersionChecker">VersionChecker</label>
                                            <div class="col-sm-4">
                                                <input <?php if(array_key_exists("VersionChecker", $functions)){ echo 'readonly';} ?> class="form-control" id="inputVersionCheckerKey" type="text" name="versionchecker_key" value=<?php if(array_key_exists("VersionChecker", $functions)){ echo '"' . $functions["VersionChecker"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name="enableAutoRemove" id="switchAutoRemove" type="checkbox" <?php if(array_key_exists("AutoRemove", $functions)){ echo "checked";} ?> >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for="switchAutoRemove">AutoRemove</label>
                                            <div class="col-sm-4">
                                                <input <?php if(array_key_exists("AutoRemove", $functions)){ echo 'readonly';} ?> class="form-control" id="inputAutoRemoveKey" type="text" name="autoremove_key" value=<?php if(array_key_exists("AutoRemove", $functions)){ echo '"' . $functions["AutoRemove"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name="enableAcceptRules" id="switchAcceptRules" type="checkbox" <?php if(array_key_exists("AcceptRules", $functions)){ echo "checked";} ?> >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for="switchAcceptRules">AcceptRules</label>
                                            <div class="col-sm-4">
                                                <input <?php if(array_key_exists("AcceptRules", $functions)){ echo 'readonly';} ?> class="form-control" id="inputAcceptRulesKey" type="text" name="acceptrules_key" value=<?php if(array_key_exists("AcceptRules", $functions)){ echo '"' . $functions["AcceptRules"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                            </div>
                                        </div>
                                        <br>
                                        <label class="col-sm-5 control-label">Können mehrmals vewendet werden</label>
                                        <br>
<?php
if( isset($functions["ClientAFK"])){
    foreach($functions["ClientAFK"] as $number=>$key){ ?>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name=<?php echo '"enableClientAfk-' . $key . '"'; ?> id=<?php echo '"switchClientAfk-' . $key . '"'; ?> type="checkbox" checked >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for=<?php echo '"switchClientAfk-' . $key . '"';?> >ClientAfk</label>
                                            <div class="col-sm-4">
                                                <input readonly class="form-control" id=<?php echo '"inputClientAfk-' . $key . '"'; ?> type="text" name=<?php echo '"clientafk_key-' . $key . '"' ?>  value=<?php echo '"' . $key . '"'; ?> />
                                            </div>
<?php if( count($functions["ClientAFK"]) != $number + 1 ){ ?>
                                        </div>
<?php   }
    }
}
if ( ( isset($addClientAfk) && $addClientAfk ) || ( isset($invalidClientAfk) && $invalidClientAfk ) || ! isset($functions["ClientAFK"]) || (isset($functions["ClientAFK"]) && count($functions["ClientAFK"]) == 0) ) {
    if(isset($functions["ClientAFK"]) && count($functions["ClientAFK"]) != 0){?>
                                        </div>
<?php } ?>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name="enableClientAfk"id="switchClientAfk" type="checkbox" "checked" >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for="switchClientAfk" >ClientAfk</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="inputClientAfk"type="text" name="clientafk_key" value=<?php echo '"' . generateRandomString(7) . '"'; ?> />
                                            </div>
                                            <div class="text-center">
                                                <button type="submit" class="btn btn-success" name="addClientAfk"><i class="fas fa-plus"></i></button>
                                            </div>
                                        </div>
<?php } else { ?>
                                            <div class="text-center">
                                                <button type="submit" class="btn btn-success" name="addClientAfk"><i class="fas fa-plus"></i></button>
                                            </div>
                                        </div>
<?php } ?>

<?php
if( isset($functions["ClientMove"])){
    foreach($functions["ClientMove"] as $number=>$key){ ?>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name=<?php echo '"enableClientMove-' . $key . '"'; ?> id=<?php echo '"switchClientMove-' . $key . '"'; ?> type="checkbox" checked >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for=<?php echo '"switchClientMove-' . $key . '"';?> >ClientMove</label>
                                            <div class="col-sm-4">
                                                <input readonly class="form-control" id=<?php echo '"inputClientMove-' . $key . '"'; ?> type="text" name=<?php echo '"clientmove_key-' . $key . '"' ?>  value=<?php echo '"' . $key . '"'; ?> />
                                            </div>
<?php if( count($functions["ClientMove"]) != $number + 1 ){ ?>
                                        </div>
<?php   }
    }
}
if ( ( isset($addClientMove) && $addClientMove ) || ( isset($invalidClientMove) && $invalidClientMove ) || ! isset($functions["ClientMove"]) || (isset($functions["ClientMove"]) && count($functions["ClientMove"]) == 0) ) {
    if(isset($functions["ClientMove"]) && count($functions["ClientMove"]) != 0){?>
                                        </div>
<?php } ?>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name="enableClientMove"id="switchClientMove" type="checkbox" "checked" >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for="switchClientMove" >ClientMove</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="inputClientMove"type="text" name="clientmove_key" value=<?php echo '"' . generateRandomString(7) . '"'; ?> />
                                            </div>
                                            <div class="text-center">
                                                <button type="submit" class="btn btn-success" name="addClientMove"><i class="fas fa-plus"></i></button>
                                            </div>
                                        </div>
<?php } else { ?>
                                            <div class="text-center">
                                                <button type="submit" class="btn btn-success" name="addClientMove"><i class="fas fa-plus"></i></button>
                                            </div>
                                        </div>
<?php } ?>


<?php
if( isset($functions["WelcomeMessage"])){
    foreach($functions["WelcomeMessage"] as $number=>$key){ ?>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name=<?php echo '"enableWelcomeMessage-' . $key . '"'; ?> id=<?php echo '"switchWelcomeMessage-' . $key . '"'; ?> type="checkbox" checked >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for=<?php echo '"switchWelcomeMessage-' . $key . '"';?> >WelcomeMessage</label>
                                            <div class="col-sm-4">
                                                <input readonly class="form-control" id=<?php echo '"inputWelcomeMessage-' . $key . '"'; ?> type="text" name=<?php echo '"welcomemessage_key-' . $key . '"' ?>  value=<?php echo '"' . $key . '"'; ?> />
                                            </div>
<?php if( count($functions["WelcomeMessage"]) != $number + 1 ){ ?>
                                        </div>
<?php   }
    }
}
if ( ( isset($addWelcomeMessage) && $addWelcomeMessage ) || ( isset($invalidWelcomeMessage) && $invalidWelcomeMessage ) || ! isset($functions["WelcomeMessage"]) || (isset($functions["WelcomeMessage"]) && count($functions["WelcomeMessage"]) == 0) ) {
    if(isset($functions["WelcomeMessage"]) && count($functions["WelcomeMessage"]) != 0){?>
                                        </div>
<?php } ?>
                                        <div class="form-group row">
                                            <div class="col-sm-1">
                                                <label class="switch">
                                                  <input name="enableWelcomeMessage"id="switchWelcomeMessage" type="checkbox" "checked" >
                                                  <span class="slider round"></span>
                                                </label>
                                            </div>
                                            <label class="col-sm-5 control-label" for="switchWelcomeMessage" >WelcomeMessage</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" id="inputWelcomeMessage"type="text" name="welcomemessage_key" value=<?php echo '"' . generateRandomString(7) . '"'; ?> />
                                            </div>
                                            <div class="text-center">
                                                <button type="submit" class="btn btn-success" name="addWelcomeMessage"><i class="fas fa-plus"></i></button>
                                            </div>
                                        </div>
<?php } else { ?>
                                            <div class="text-center">
                                                <button type="submit" class="btn btn-success" name="addWelcomeMessage"><i class="fas fa-plus"></i></button>
                                            </div>
                                        </div>
<?php } ?>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-3"></div>
                            <?php if($saved) { ?>
                            <div id="savedDiv" class="row saved-row">
                                <label class="saved-label">Config gespeichert. Bitte den Bot neustarten!</label>
                            </div>
                            <?php }?>
                            <div class="row">&nbsp;</div>
                            <div class="row" style="display: block;">
                                <div class="text-center">
                                    <button type="submit" class="btn btn-primary" name="update"><i class="fas fa-save"></i>&nbsp;speichern</button>
                                </div>
                            </div>
                            <div class="row">&nbsp;</div>
                        </form>
                    </div>
                </main>
                <footer class="py-4 bg-light mt-auto">
                    <?php
                        require_once('_footer.php');
                    ?>
                </footer>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="js/scripts.js"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" crossorigin="anonymous"></script>
        <script src="assets/demo/datatables-demo.js"></script>
    </body>
</html>
