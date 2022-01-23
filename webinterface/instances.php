<?php
    require_once('templates/preload.php');
    $saved = FALSE;
    $oldkey = "";
    $addNewInstance = "";
    $invalidInstance = "";
    if (isset($_POST['addNewInstance'])){
        $addNewInstance = TRUE;
    }

    foreach($_POST as $key => $value){
        if( str_starts_with( $key, "delete-") ){
            $removeKey = str_replace("delete-", "", $key);
            unset($_SESSION["instances"][$removeKey]);
            saveInstanceConfigFile($_SESSION["instances"], $_SESSION["instance_config_path"]);
            startScriptWithParams("removeInstance" , $removeKey);
            if ( empty($_SESSION["instances"]) ){
                removeCurrentInstance();
                $_SESSION["configFolderPath"] = "";
                $_SESSION["config"] = "";
            }
            if($_SESSION["instance_name"] === $removeKey){
                $firstKey = array_key_first( $_SESSION["instances"] );
                $_SESSION["configPath"] = $_SESSION["instances"][$firstKey]["instance_config_pfad"];
                $_SESSION["instance_name"] = $firstKey;
                saveCurrentInstance(array($_SESSION["instance_name"] => ""), $_SESSION["instances"], $_SERVER["DOCUMENT_ROOT"]);
            }

            startScript("restart");
            continue;
        }
        if ( str_starts_with( $key, "rename-") ){
            $oldkey = str_replace("rename-", "", $key);
            $newkey = $_POST["instancename-" . $oldkey];
            if( ! preg_match("/^[0-9a-zA-Z-]*$/", $newkey) ) {
                $renameInvalid = TRUE;
                continue;
            }
            if($newkey === $oldkey){
                continue;
            }
            $_SESSION["instances"] = renameInstance($_SESSION["instances"], $newkey, $oldkey);
            saveInstanceConfigFile($_SESSION["instances"], $_SESSION["instance_config_path"]);
            startScriptWithParams("renameInstance" , $oldkey, $newkey);
            if($_SESSION["instance_name"] === $oldkey){
                $_SESSION["configPath"] = $_SESSION["instances"][$newkey]["instance_config_pfad"];
                $_SESSION["instance_name"] = $newkey;
                saveCurrentInstance(array($_SESSION["instance_name"] => ""), $_SESSION["instances"], $_SERVER["DOCUMENT_ROOT"]);
            }

            startScript("restart");
            continue;
        }
    }
    if (isset($_POST['update'])){
        $invalidInstance = FALSE;
        foreach($_SESSION["instances"] as $key => $value){
            if (isset($_POST['enable-' . $key]) && filter_var($_POST['enable-' . $key], FILTER_VALIDATE_BOOLEAN)){
                $_SESSION["instances"][$key]["instance_activ"] = "true";
            }else{
                $_SESSION["instances"][$key]["instance_activ"] = "false";
            }
        }
        if(isset($_POST['newinstancename']) && empty($_POST['newinstancename'])){
            $invalidInstance = TRUE;
        }
        if (isset($_POST['newinstancename']) && ! empty($_POST['newinstancename']) ){
            if( preg_match("/^[0-9a-zA-Z-]*$/", $_POST['newinstancename']) ) {
                $newinstance = $_POST['newinstancename'];
                if ( empty($_SESSION["instances"])){
                    $firstInstance = TRUE;
                }
                $_SESSION["instances"][$newinstance] = [];
                if (isset($_POST['enableNewInstance'])){
                    $_SESSION["instances"][$newinstance]["instance_activ"] = boolval(filter_var($_POST['enableNewInstance'], FILTER_VALIDATE_BOOLEAN));
                } else {
                    $_SESSION["instances"][$newinstance]["instance_activ"] = "false";
                }
                $_SESSION["instances"][$newinstance]["instance_config_pfad"] = "/data/configs/" . $newinstance . "/serverconfig.cfg";
                startScriptWithParams("createInstance" , $newinstance);
                if($firstInstance){
                    $_SESSION["instance_name"] = $newinstance;
                    saveCurrentInstance(array($_SESSION["instance_name"] => ""), $_SESSION["instances"]);
                    header("Refresh:0");
                }
            }else{
                $invalidInstance = TRUE;
            }
        }
        if( ! $invalidInstance ){
            $saved = TRUE;
            saveInstanceConfigFile($_SESSION["instances"], $_SESSION["instance_config_path"]);
        }
    }

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Instance Settings";
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
                            <li class="breadcrumb-item active">Instanzen</li>
                        </ol>
                        <form class="form-horizontal" data-toggle="validator" name="addFunction" method="POST">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <i class="fas fa-chart-area mr-1"></i>
                                    Settings
                                </div>
                                <br>
                                <div class="card-body">
<?php
$counter = 1;
foreach($_SESSION["instances"] as $key => $value){
?>
                                    <div class="form-group row">
                                        <div class="col-sm-1"></div>
                                        <label class="col-sm-2 control-label" for=<?php echo '"inputKey-' . $key . '"';?> >Instanz <?php echo $counter; ?></label>
                                        <div class="col-sm-2 input-group">
                                            <div class="input-group-prepend">
                                                <button class="btn btn-outline-secondary" type="button" data-toggle="collapse" data-target=<?php echo '"#collapse-' . $key . '"';?> aria-expanded="false" aria-controls=<?php echo '"collapse-' . $key . '"';?> >?</button>
                                            </div>
                                            <input class="form-control" <?php if( strcmp($oldkey, $key) == 0 && $renameInvalid){ echo 'style="border-color: #ff0000 ;"';}?> id=<?php echo '"inputKey-' . $key . '"';?> type="text" name=<?php echo '"instancename-' . $key . '"';?> value=<?php if( strcmp($oldkey, $key) == 0 && $renameInvalid){ echo '"' . $newkey . '"';}else{echo '"' . $key . '"';}?> />
                                            <div class=<?php if( strcmp($oldkey, $key) == 0 && $renameInvalid){ echo '"collapse show"';}else{echo "collapse";}?> id=<?php echo '"collapse-' . $key . '"';?>>
                                                <div class="card">Instanzname darf nur aus Zahlen, Buchstaben und einem Minus bestehen!</div>
                                            </div>
                                        </div>
                                        <div class="col-sm-1">
                                            <label class="switch">
                                              <input name=<?php echo '"enable-' . $key . '"';?> id=<?php echo '"switch-' . $key . '"';?> type="checkbox" <?php if( filter_var($_SESSION["instances"][$key]["instance_activ"], FILTER_VALIDATE_BOOLEAN)){ echo "checked";} ?> />
                                              <span class="slider round"></span>
                                            </label>
                                        </div>
                                        <div class="col-sm-2 text-center">
                                            <button type="submit" class="btn btn-danger" name=<?php echo '"delete-' . $key . '"';?>><i class="fas fa-trash-alt"></i></button>
                                        </div>
                                        &nbsp;&nbsp;
                                        <div class="col-sm-2 text-center">
                                            <button type="submit" class="btn btn-warning" name=<?php echo '"rename-' . $key . '"'; ?> ></i>Umbennen</button>
                                        </div>
                                    </div>
<?php
$counter++;
} ?>
<?php if ($addNewInstance || $invalidInstance) { ?>
                                    <div class="form-group row">
                                        <div class="col-sm-1"></div>
                                        <label class="col-sm-2 control-label" for="inputInstanceName">Instanz <?php echo $counter; ?></label>
                                        <div class="col-sm-2 input-group">
                                            <div class="input-group-prepend">
                                                <button class="btn btn-outline-secondary" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">?</button>
                                            </div>
                                            <input class="form-control" <?php if($invalidInstance){ echo 'style="border-color: #ff0000 ;"';}?> id="inputInstanceName" type="text" name="newinstancename" placeholder="enter instance name" value=""/>
                                            <div class=<?php if($invalidInstance){ echo '"collapse show"';}else{echo "collapse";}?> id="collapseExample">
                                                <div class="card">
                                                    Instanzname darf nur aus Zahlen, Buchstaben und einem Minus bestehen!
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-1">
                                            <label class="switch">
                                              <input name="enableNewInstance" id="switchNewInstance" type="checkbox" >
                                              <span class="slider round"></span>
                                            </label>
                                        </div>
                                        <div class="col-sm-1 text-center">
                                            <button type="submit" class="btn btn-danger" name="remove"><i class="fas fa-trash-alt"></i></button>
                                        </div>
                                    </div>
<?php } ?>
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-success" name="addNewInstance"><i class="fas fa-plus"></i></button>
                                    </div>

                                    <br>
                                    <div class="col-md-1"></div>
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
                                </div>
                                <!-- End of card-body -->
                            </div>
                        </form>
                    </div>
                    <!-- End of Page Content -->
                </div>
                <!-- End of Main Content -->
<?php require_once('templates/footer.php'); ?>
            </div>
            <!-- End of Content Wrapper -->
        </div>
        <!-- End of Page Wrapper -->
    </body>
</html>