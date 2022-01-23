<?php
    require_once('templates/preload.php');

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
        $_SESSION['config']["language"] = $_POST['language'];
        $_SESSION['config']["ts3_server_floodrate"] = $_POST['ts3_server_floodrate'];
        $_SESSION['config']["ts3_server_ip"] = $_POST['ts3_server_ip'];
        $_SESSION['config']["ts3_server_port"] = $_POST['ts3_server_port'];
        $_SESSION['config']["ts3_server_query_port"] = $_POST['ts3_server_query_port'];
        $_SESSION['config']["ts3_server_query_login_name"] = $_POST['ts3_server_query_login_name'];
        $_SESSION['config']["ts3_server_query_login_password"] = $_POST['ts3_server_query_login_password'];
        $_SESSION['config']["ts3_bot_nickname"] = $_POST['ts3_bot_nickname'];
        $_SESSION['config']["ts3_bot_nickname2"] = $_POST['ts3_bot_nickname2'];
        $_SESSION['config']["ts3_bot_channel_id"] = $_POST['ts3_bot_channel_id'];
        $_SESSION['config']["bot_admin"] = $_POST['bot_admin'];
        $_SESSION['config']["bot_full_admin"] = $_POST['bot_full_admin'];

        if( empty($_POST["enableTwitch"]) && array_key_exists("Twitch", $_SESSION['functions'])) {
            $retValue = removeFunction($_SESSION['config'], $_SESSION['functions'], "Twitch");
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        } else if( ! array_key_exists("Twitch", $_SESSION['functions']) && $_POST["enableTwitch"]) {
            $retValue = addTwitch($_SESSION['config'], $_SESSION['functions'], $_POST["twitch_key"], $_SESSION['configFolderPath']);
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        }
        if( empty($_POST["enableViewer"]) && array_key_exists("Viewer", $_SESSION['functions'])) {
            $retValue = removeCustomFunction($_SESSION['config'], $_SESSION['functions'], "Viewer", "ts3_viewer");
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        } else if( ! array_key_exists("Viewer", $_SESSION['functions']) && $_POST["enableViewer"]) {
            $retValue = addTs3Viewer($_SESSION['config'], $_SESSION['functions'], $_POST["viewer_key"], $_SESSION['configFolderPath']);
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        }
        if( empty($_POST["enableChannelAutoCreate"]) && array_key_exists("ChannelAutoCreate", $_SESSION['functions'])) {
            $retValue = removeCustomFunction($_SESSION['config'], $_SESSION['functions'], "ChannelAutoCreate", "channel_check");
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        } else if( ! array_key_exists("ChannelAutoCreate", $_SESSION['functions']) && $_POST["enableChannelAutoCreate"]) {
            $retValue = addChannelAutoCreate($_SESSION['config'], $_SESSION['functions'], $_POST["channelautocreate_key"], $_SESSION['configFolderPath']);
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        }
        if( empty($_POST["enableVersionChecker"]) && array_key_exists("VersionChecker", $_SESSION['functions'])) {
            $retValue = removeCustomFunction($_SESSION['config'], $_SESSION['functions'], "VersionChecker", "version");
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        } else if( ! array_key_exists("VersionChecker", $_SESSION['functions']) && $_POST["enableVersionChecker"]) {
            $retValue = addVersionChecker($_SESSION['config'], $_SESSION['functions'], $_POST["versionchecker_key"]);
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        }
        if( empty($_POST["enableAutoRemove"]) && array_key_exists("AutoRemove", $_SESSION['functions'])) {
            $retValue = removeCustomFunction($_SESSION['config'], $_SESSION['functions'], "AutoRemove", "auto_remove");
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        } else if( ! array_key_exists("AutoRemove", $_SESSION['functions']) && $_POST["enableAutoRemove"]) {
            $retValue = addAutoRemove($_SESSION['config'], $_SESSION['functions'], $_POST["autoremove_key"]);
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        }
        if( empty($_POST["enableAcceptRules"]) && array_key_exists("AcceptRules", $_SESSION['functions'])) {
            $retValue = removeCustomFunction($_SESSION['config'], $_SESSION['functions'], "AcceptRules", "accept_rules");
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        } else if( ! array_key_exists("AcceptRules", $_SESSION['functions']) && $_POST["enableAcceptRules"]) {
            $retValue = addAcceptRules($_SESSION['config'], $_SESSION['functions'], $_POST["acceptrules_key"], $_SESSION['configFolderPath']);
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        }
        if( $_POST["enableClientAfk"] ) {
            $retValue = addClientAfk($_SESSION['config'], $_SESSION['functions'], $_POST["clientafk_key"]);
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        }
        if( $_POST["enableClientMove"] ) {
            $retValue = addClientMove($_SESSION['config'], $_SESSION['functions'], $_POST["clientmove_key"]);
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        }

        if( $_POST["enableWelcomeMessage"] ) {
            $retValue = addWelcomeMessage($_SESSION['config'], $_SESSION['functions'], $_POST["welcomemessage_key"], $_SESSION['configFolderPath']);
            $_SESSION['config'] = $retValue[0];
            $_SESSION['functions'] = $retValue[1];
        }

        foreach($_POST as $key => $value) {
            $tmpkey = str_replace("clientafk_key-", "", $key);
            if(str_starts_with($key, "clientafk_key-") && ! isset( $_POST["enableClientAfk-" . $tmpkey] )){
                $retValue = removeSpecialFunction($_SESSION['config'], $_SESSION['functions'], "ClientAFK", "client_afk", $tmpkey);
                $_SESSION['config'] = $retValue[0];
                $_SESSION['functions'] = $retValue[1];
            }
            $tmpkey = str_replace("clientmove_key-", "", $key);
            if(str_starts_with($key, "clientmove_key-") && ! isset( $_POST["enableClientMove-" . $tmpkey] )){
                $retValue = removeSpecialFunction($_SESSION['config'], $_SESSION['functions'], "ClientMove", "client_moved", $tmpkey);
                $_SESSION['config'] = $retValue[0];
                $_SESSION['functions'] = $retValue[1];
            }
            $tmpkey = str_replace("welcomemessage_key-", "", $key);
            if(str_starts_with($key, "welcomemessage_key-") && ! isset( $_POST["enableWelcomeMessage-" . $tmpkey] )){
                $retValue = removeSpecialFunction($_SESSION['config'], $_SESSION['functions'], "WelcomeMessage", "welcome", $tmpkey);
                $_SESSION['config'] = $retValue[0];
                $_SESSION['functions'] = $retValue[1];
            }
        }

        $_SESSION['config']["functions"] = updateFunctionString($_SESSION['functions']);
        saveConfig($_SESSION['config'], $_SESSION['configPath']);
        $saved = TRUE;
    }

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Core Settings";
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/header.php');
    //endregion
?>
    <body id="page-top">
        <!-- Page Wrapper -->
        <div id="wrapper">
<?php require_once('templates/nav.php'); ?>
            <!-- Content Wrapper -->
            <div id="content-wrapper" class="d-flex flex-column">
                <!-- Main Content -->
                <div id="content">
<?php require_once('templates/nav-header.php'); ?>
                    <!-- Begin Page Content -->
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
                                        <div class="card-body">
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="language">Bot Kommunikationssprache</label>
                                                <div class="col-sm-4">
                                                    <select name="language" class="form-select" aria-label="select">
<?php if ( $_SESSION['config']["language"] === "german") { ?>
                                                        <option selected value="german">deutsch</option>
                                                        <option value="english">englisch</option>
<?php } else if ( $_SESSION['config']["language"] === "english"){?>
                                                        <option value="german">deutsch</option>
                                                        <option selected value="english">englisch</option>
<?php } else { ?>
                                                        <option value="german">deutsch</option>
                                                        <option value="english">englisch</option>
<?php } ?>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="ts3_server_floodrate">Floodrate</label>
                                                <div class="col-sm-4">
                                                    <select name="ts3_server_floodrate" class="form-select" aria-label="select">
<?php if ( $_SESSION['config']["ts3_server_floodrate"] === "unlimited") { ?>
                                                        <option selected value="unlimited">unlimited</option>
                                                        <option value="default">default</option>
<?php } else if ( $_SESSION['config']["ts3_server_floodrate"] === "default"){?>
                                                        <option value="unlimited">unlimited</option>
                                                        <option selected value="default">default</option>
<?php } else { ?>
                                                        <option value="unlimited">unlimited</option>
                                                        <option value="default">default</option>
<?php } ?>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="ts3_server_ip">Server IP</label>
                                                <div class="col-sm-4">
                                                    <input class="form-control" id="ts3_server_ip" type="text" name="ts3_server_ip" placeholder="Enter new ts3_server_ip" value=<?php if(array_key_exists("ts3_server_ip", $_SESSION['config'])){ echo '"' . $_SESSION['config']["ts3_server_ip"] . '"' . '"';}else{ echo '""';} ?> required/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="ts3_server_port">Server Port</label>
                                                <div class="col-sm-4">
                                                    <input class="form-control" id="ts3_server_port" type="text" name="ts3_server_port" placeholder="Enter new ts3_server_port" value=<?php if(array_key_exists("ts3_server_port", $_SESSION['config'])){ echo '"' . $_SESSION['config']["ts3_server_port"] . '"' . '"';}else{ echo '""';} ?> required/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="ts3_server_query_port">Query Port</label>
                                                <div class="col-sm-4">
                                                    <input class="form-control" id="ts3_server_query_port" type="text" name="ts3_server_query_port" placeholder="Enter new ts3_server_query_port" value=<?php if(array_key_exists("ts3_server_query_port", $_SESSION['config'])){ echo '"' . $_SESSION['config']["ts3_server_query_port"] . '"';}else{ echo '"10011"';} ?> required/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="ts3_server_query_login_name">Query Login Name</label>
                                                <div class="col-sm-4">
                                                    <input class="form-control" id="ts3_server_query_login_name" type="text" name="ts3_server_query_login_name" placeholder="Enter new ts3_server_query_login_name" value=<?php if(array_key_exists("ts3_server_query_login_name", $_SESSION['config'])){ echo '"' . $_SESSION['config']["ts3_server_query_login_name"] . '"';}else{ echo '""';} ?> required/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="ts3_server_query_login_password">Query Login Password</label>
                                                <div class="col-sm-4">
                                                    <input class="form-control" id="ts3_server_query_login_password" type="text" name="ts3_server_query_login_password" placeholder="Enter new ts3_server_query_login_password" value=<?php if(array_key_exists("ts3_server_query_login_password", $_SESSION['config'])){ echo '"' . $_SESSION['config']["ts3_server_query_login_password"] . '"';}else{ echo '""';} ?> required/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="ts3_bot_nickname">Bot Nickname</label>
                                                <div class="col-sm-4">
                                                    <input class="form-control" id="ts3_bot_nickname" type="text" name="ts3_bot_nickname" placeholder="Enter new ts3_bot_nickname" value=<?php if(array_key_exists("ts3_bot_nickname", $_SESSION['config'])){ echo '"' . $_SESSION['config']["ts3_bot_nickname"] . '"';}else{ echo '""';} ?> required/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="ts3_bot_nickname2">Bot Nickname 2</label>
                                                <div class="col-sm-4">
                                                    <input class="form-control" id="ts3_bot_nickname2" type="text" name="ts3_bot_nickname2" placeholder="Enter new ts3_bot_nickname2" value=<?php if(array_key_exists("ts3_bot_nickname2", $_SESSION['config'])){ echo '"' . $_SESSION['config']["ts3_bot_nickname2"] . '"' . '"';}else{ echo '""';} ?> required/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="ts3_bot_channel_id">Default Channel ID</label>
                                                <div class="col-sm-4">
                                                    <input class="form-control" id="ts3_bot_channel_id" type="text" name="ts3_bot_channel_id" placeholder="Enter new ts3_bot_channel_id" value=<?php if(array_key_exists("ts3_bot_channel_id", $_SESSION['config'])){ echo '"' . $_SESSION['config']["ts3_bot_channel_id"] . '"';}else{ echo '""';} ?> required/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="bot_admin">Bot Admins</label>
                                                <div class="col-sm-4">
                                                    <div name="bot_admin" id="multiple-select-admin"></div>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="bot_full_admin">Bot Full Admins</label>
                                                <div class="col-sm-4">
                                                    <div name="bot_full_admin" id="multiple-select-full-admin"></div>
                                                </div>
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
<?php   if ( $_SESSION['db_users'] == array() && $_SESSION['db_groups'] == array() && $_SESSION['db_channels'] == array()){?>
                                        <label class="col-sm-10 control-label" >Erste Verbindung ausstehend. Anschließend können Funktionen aktiviert werden!</label>
                                        <br>
<?php   } else {?>

                                        <label class="col-sm-4 control-label" >Können nur einmal vewendet werden</label>
                                        <div class="card-body">
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="switchTwitch">Twitch</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enableTwitch" id="switchTwitch" type="checkbox" <?php if(array_key_exists("Twitch", $_SESSION['functions'])){ echo "checked";} ?> >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="hidden" <?php if(array_key_exists("Twitch", $_SESSION['functions'])){ echo 'readonly';} ?> class="form-control" id="inputTwitchKey" type="text" name="twitch_key" value=<?php if(array_key_exists("Twitch", $_SESSION['functions'])){ echo '"' . $_SESSION['functions']["Twitch"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="switchViewer">Ts3 Viewer</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enableViewer" id="switchViewer" type="checkbox" <?php if(array_key_exists("Viewer", $_SESSION['functions'])){ echo "checked";} ?> >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="hidden" <?php if(array_key_exists("Viewer", $_SESSION['functions'])){ echo 'readonly';} ?> class="form-control" id="inputViewerKey" type="text" name="viewer_key" value=<?php if(array_key_exists("Viewer", $_SESSION['functions'])){ echo '"' . $_SESSION['functions']["Viewer"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="switchChannelAutoCreate">ChannelAutoCreate</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enableChannelAutoCreate" id="switchChannelAutoCreate" type="checkbox" <?php if(array_key_exists("ChannelAutoCreate", $_SESSION['functions'])){ echo "checked";} ?> >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="hidden" <?php if(array_key_exists("ChannelAutoCreate", $_SESSION['functions'])){ echo 'readonly';} ?> class="form-control" id="inputChannelAutoCreateKey" type="text" name="channelautocreate_key" value=<?php if(array_key_exists("ChannelAutoCreate", $_SESSION['functions'])){ echo '"' . $_SESSION['functions']["ChannelAutoCreate"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="switchVersionChecker">VersionChecker</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enableVersionChecker" id="switchVersionChecker" type="checkbox" <?php if(array_key_exists("VersionChecker", $_SESSION['functions'])){ echo "checked";} ?> >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="hidden" <?php if(array_key_exists("VersionChecker", $_SESSION['functions'])){ echo 'readonly';} ?> class="form-control" id="inputVersionCheckerKey" type="text" name="versionchecker_key" value=<?php if(array_key_exists("VersionChecker", $_SESSION['functions'])){ echo '"' . $_SESSION['functions']["VersionChecker"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="switchAutoRemove">AutoRemove</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enableAutoRemove" id="switchAutoRemove" type="checkbox" <?php if(array_key_exists("AutoRemove", $_SESSION['functions'])){ echo "checked";} ?> >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="hidden" <?php if(array_key_exists("AutoRemove", $_SESSION['functions'])){ echo 'readonly';} ?> class="form-control" id="inputAutoRemoveKey" type="text" name="autoremove_key" value=<?php if( array_key_exists("AutoRemove", $_SESSION['functions']) ){ echo '"' . $_SESSION['functions']["AutoRemove"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="switchAcceptRules">AcceptRules</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enableAcceptRules" id="switchAcceptRules" type="checkbox" <?php if(array_key_exists("AcceptRules", $_SESSION['functions'])){ echo "checked";} ?> >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="hidden" <?php if(array_key_exists("AcceptRules", $_SESSION['functions'])){ echo 'readonly';} ?> class="form-control" id="inputAcceptRulesKey" type="text" name="acceptrules_key" value=<?php if(array_key_exists("AcceptRules", $_SESSION['functions'])){ echo '"' . $_SESSION['functions']["AcceptRules"] . '"';}else{ echo '"' . generateRandomString(7) . '"';} ?>/>
                                                </div>
                                            </div>
                                            <hr class="sidebar-divider">
                                            <label class="col-sm-6 control-label">Können mehrmals vewendet werden</label>
                                            <br>
<?php
if( isset($_SESSION['functions']["ClientAFK"])){
    foreach($_SESSION['functions']["ClientAFK"] as $number=>$key){ ?>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for=<?php echo '"switchClientAfk-' . $key . '"';?> >ClientAfk</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name=<?php echo '"enableClientAfk-' . $key . '"'; ?> id=<?php echo '"switchClientAfk-' . $key . '"'; ?> type="checkbox" checked >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
<?php   if( count($_SESSION['functions']["ClientAFK"]) == $number + 1  && ( ! isset($addClientAfk) || ! $addClientAfk )){ ?>
                                                <div class="col-sm-2 text-center">
                                                    <button type="submit" class="btn btn-success" name="addClientAfk"><i class="fas fa-plus"></i></button>
                                                </div>
<?php       }?>
                                                <div class="col-sm-4">
                                                    <input type="hidden" readonly class="form-control" id=<?php echo '"inputClientAfk-' . $key . '"'; ?> type="text" name=<?php echo '"clientafk_key-' . $key . '"' ?>  value=<?php echo '"' . $key . '"'; ?> />
                                                </div>
<?php   if( count($_SESSION['functions']["ClientAFK"]) != $number + 1 ){ ?>
                                            </div>
<?php       }
    }
}
if ( ( isset($addClientAfk) && $addClientAfk ) || ( isset($invalidClientAfk) && $invalidClientAfk ) || ! isset($_SESSION['functions']["ClientAFK"]) || (isset($_SESSION['functions']["ClientAFK"]) && count($_SESSION['functions']["ClientAFK"]) == 0) ) {
    if(isset($_SESSION['functions']["ClientAFK"]) && count($_SESSION['functions']["ClientAFK"]) != 0){?>
                                            </div>
<?php   } ?>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="switchClientAfk" >ClientAfk</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enableClientAfk"id="switchClientAfk" type="checkbox" "checked" >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
                                                <div class="col-sm-2 text-center">
                                                    <button type="submit" class="btn btn-success" name="addClientAfk"><i class="fas fa-plus"></i></button>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="hidden" class="form-control" id="inputClientAfk"type="text" name="clientafk_key" value=<?php echo '"' . generateRandomString(7) . '"'; ?> />
                                                </div>
                                            </div>
<?php   } else { ?>
                                            </div>
<?php   } ?>

<?php
if( isset($_SESSION['functions']["ClientMove"])){
    foreach($_SESSION['functions']["ClientMove"] as $number=>$key){ ?>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for=<?php echo '"switchClientMove-' . $key . '"';?> >ClientMove</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name=<?php echo '"enableClientMove-' . $key . '"'; ?> id=<?php echo '"switchClientMove-' . $key . '"'; ?> type="checkbox" checked >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
<?php   if( count($_SESSION['functions']["ClientMove"]) == $number + 1  && ( ! isset($addClientMove) || ! $addClientMove )){ ?>
                                                <div class="col-sm-2 text-center">
                                                    <button type="submit" class="btn btn-success" name="addClientMove"><i class="fas fa-plus"></i></button>
                                                </div>
<?php       }?>
                                                <div class="col-sm-4">
                                                    <input type="hidden" readonly class="form-control" id=<?php echo '"inputClientMove-' . $key . '"'; ?> type="text" name=<?php echo '"clientmove_key-' . $key . '"' ?>  value=<?php echo '"' . $key . '"'; ?> />
                                                </div>
<?php   if( count($_SESSION['functions']["ClientMove"]) != $number + 1 ){ ?>
                                            </div>
<?php       }
    }
}
if ( ( isset($addClientMove) && $addClientMove ) || ( isset($invalidClientMove) && $invalidClientMove ) || ! isset($_SESSION['functions']["ClientMove"]) || (isset($_SESSION['functions']["ClientMove"]) && count($_SESSION['functions']["ClientMove"]) == 0) ) {
    if(isset($_SESSION['functions']["ClientMove"]) && count($_SESSION['functions']["ClientMove"]) != 0){?>
                                            </div>
<?php   } ?>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="switchClientMove" >ClientMove</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enableClientMove"id="switchClientMove" type="checkbox" "checked" >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
                                                <div class="col-sm-2 text-center">
                                                    <button type="submit" class="btn btn-success" name="addClientMove"><i class="fas fa-plus"></i></button>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="hidden" class="form-control" id="inputClientMove"type="text" name="clientmove_key" value=<?php echo '"' . generateRandomString(7) . '"'; ?> />
                                                </div>
                                            </div>
<?php   } else { ?>
                                            </div>
<?php   } ?>


<?php
if( isset($_SESSION['functions']["WelcomeMessage"])){
    foreach($_SESSION['functions']["WelcomeMessage"] as $number=>$key){ ?>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for=<?php echo '"switchWelcomeMessage-' . $key . '"';?> >WelcomeMessage</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name=<?php echo '"enableWelcomeMessage-' . $key . '"'; ?> id=<?php echo '"switchWelcomeMessage-' . $key . '"'; ?> type="checkbox" checked >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
<?php   if( count($_SESSION['functions']["WelcomeMessage"]) == $number + 1  && ( ! isset($addWelcomeMessage) || ! $addWelcomeMessage )){ ?>
                                                <div class="col-sm-2 text-center">
                                                    <button type="submit" class="btn btn-success" name="addWelcomeMessage"><i class="fas fa-plus"></i></button>
                                                </div>
<?php       }?>
                                                <div class="col-sm-4">
                                                    <input type="hidden" readonly class="form-control" id=<?php echo '"inputWelcomeMessage-' . $key . '"'; ?> type="text" name=<?php echo '"welcomemessage_key-' . $key . '"' ?>  value=<?php echo '"' . $key . '"'; ?> />
                                                </div>
<?php   if( count($_SESSION['functions']["WelcomeMessage"]) != $number + 1 ){ ?>
                                         </div>
<?php       }
    }
}
if ( ( isset($addWelcomeMessage) && $addWelcomeMessage ) || ( isset($invalidWelcomeMessage) && $invalidWelcomeMessage ) || ! isset($_SESSION['functions']["WelcomeMessage"]) || (isset($_SESSION['functions']["WelcomeMessage"]) && count($_SESSION['functions']["WelcomeMessage"]) == 0) ) {
    if(isset($_SESSION['functions']["WelcomeMessage"]) && count($_SESSION['functions']["WelcomeMessage"]) != 0){?>
                                          </div>
<?php   } ?>
                                            <div class="form-group row">
                                                <label class="col-sm-4 control-label" for="switchWelcomeMessage" >WelcomeMessage</label>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enableWelcomeMessage"id="switchWelcomeMessage" type="checkbox" "checked" >
                                                      <span class="slider round"></span>
                                                    </label>
                                                </div>
                                                <div class="col-sm-2 text-center">
                                                    <button type="submit" class="btn btn-success" name="addWelcomeMessage"><i class="fas fa-plus"></i></button>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="hidden" class="form-control" id="inputWelcomeMessage"type="text" name="welcomemessage_key" value=<?php echo '"' . generateRandomString(7) . '"'; ?> />
                                                </div>
                                            </div>
<?php   } else { ?>
                                            </div>
<?php   } ?>
<?php } ?>
                                        </div>
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
                    <!-- End of Page Content -->
<?php require_once('templates/footer.php'); ?>
                </div>
                <!-- End of Main Content -->
            </div>
            <!-- End of Content Wrapper -->
        </div>
        <!-- End of Page Wrapper -->
        <script type="text/javascript">
          function getSelectedAdmin() {
              var optionsData = [<?php echo getJSSelectOption($_SESSION['db_users'], $_SESSION["config"]["bot_admin"]);?>];
              return optionsData;
         }

          function getSelectedFullAdmin() {
              var optionsData = [<?php echo getJSSelectOption($_SESSION['db_users'], $_SESSION["config"]["bot_full_admin"]);?>];
              return optionsData;
         }

         VirtualSelect.init({
            ele: '#multiple-select-full-admin',
            options: getUsers(),
            multiple: true,
            selectedValue: getSelectedFullAdmin(),
            placeholder: 'User auswählen',
          });

         VirtualSelect.init({
            ele: '#multiple-select-admin',
            options: getUsers(),
            multiple: true,
            selectedValue: getSelectedAdmin(),
            placeholder: 'User auswählen',
          });
        </script>
    </body>
</html>
