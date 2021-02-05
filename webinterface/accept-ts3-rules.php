<?php
    require_once('_preload.php');
    $nav_expanded = TRUE;
    $saved = FALSE;
    if (array_key_exists("AcceptRules", $functions)) {
        $acprKey = $functions["AcceptRules"];
        if(filesize($config[$acprKey . "_accept_rules_message_file_path"])) {
            $messageContent = file_get_contents($config[$acprKey . "_accept_rules_message_file_path"], "r") or die("Unable to open(read) '" . $config[$acprKey . "_accept_rules_message_file_path"] . "' file!");
        }else{
            $messageContent = "";
        }
        if(filesize($config[$acprKey . "_accept_rules_forbidden_file_path"])) {
            $forbiddenNames = fileReadContentWithoutSeparator($config[$acprKey . "_accept_rules_forbidden_file_path"]);
        }
    }

    foreach($_POST as $key => $value){
        if( str_starts_with($key, "rmItem")){
            $number = str_replace("rmItem", "", $key);
            unset($forbiddenNames[$_POST['item' . $number]]);
            write_config_file($forbiddenNames, $config[$acprKey . "_accept_rules_forbidden_file_path"]);
            $saved = TRUE;
        }
    }

    if (isset($_POST['save'])){
        $forbiddenNames = [];
        foreach($_POST as $key => $value){
            if( ( str_starts_with($key, "newItem") || str_starts_with($key, "item") ) && ! empty($value) ){
                $forbiddenNames[$value] = "";
            }
        }
        $config[$acprKey . "_accept_rules_first_group"] = $_POST['accept_rules_first_group'];
        $config[$acprKey . "_accept_rules_accepted_group"] = $_POST['accept_rules_accepted_group'];
        $config[$acprKey . "_accept_rules_poke_message"] = $_POST['accept_rules_poke_message'];
        $config[$acprKey . "_accept_rules_name_seperators"] = $_POST['accept_rules_name_seperators'];
        $messageContent = $_POST['privateMessage'];
        write_content_block($messageContent, $config[$acprKey . "_accept_rules_message_file_path"] );
        write_config_file($forbiddenNames, $config[$acprKey . "_accept_rules_forbidden_file_path"]);
        saveConfig($config, $configPath);
        $saved = TRUE;
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
        <title>Funktion - Accept Rules</title>
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
                        <h1 class="mt-4">Accept Rules</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Funktionen</li>
                            <li class="breadcrumb-item active">Accept Rules</li>
                        </ol>
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-chart-area mr-1"></i>
                                Settings
                            </div>
                            <br>
                            <div class="col-md-3"></div>
                            <?php if($saved) { ?>
                            <div id="savedDiv" class="row saved-row">
                                <label class="saved-label">Config gespeichert. Bitte den Bot neustarten! Oder '!botconfigreload' dem bot schreiben!</label>
                            </div>
                            <?php }?>
                            <br>
                            <form class="form-horizontal" data-toggle="validator" name="update" method="POST">
                                <div class="form-group row">
                                    <label class="col-sm-4 control-label" for="inputServergroup">Gast Gruppe bei der ersten Verbindung</label>
                                    <div class="col-sm-4">
                                        <input class="form-control" id="inputServergroup" type="text" name="accept_rules_first_group" placeholder="enter first servergroup" value=<?php echo '"' . $config[$acprKey . "_accept_rules_first_group"] . '"' ?> required/>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-4 control-label" for="inputServergroup2">Gast Gruppe die nach dem Akzeptieren vergeben werden soll</label>
                                    <div class="col-sm-4">
                                        <input class="form-control" id="inputServergroup2" type="text" name="accept_rules_accepted_group" placeholder="enter second servergroup" value=<?php echo '"' . $config[$acprKey . "_accept_rules_accepted_group"] . '"' ?> required/>
                                    </div>
                                </div>
                                <div class="form-group row" >
                                    <div class="col-sm-12 input-group">
                                        <label class="col-sm-4 control-label" for="inputPokeText">Nachricht die der Client als Poke Nachricht erhalten soll.</label>
                                        <div class="input-group-prepend">
                                            <span id="charNum" class="input-group-text"><?php echo "verbleibend: " . 100 - strlen($config[$acprKey . "_accept_rules_poke_message"]); ?></span>
                                        </div>
                                        <div class="col-sm-4">
                                            <textarea name="accept_rules_poke_message" class="form-control" onkeyup="countChar(this)" id="inputPokeText" placeholder="Text eingeben die der Client als Poke Nachricht erhalten soll" rows="1"><?php echo $config[$acprKey . "_accept_rules_poke_message"]; ?></textarea>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group row" >
                                    <label class="col-sm-4 control-label" for="inputText">Nachricht die der Client als Private Nachricht erhalten soll.</label>
                                    <div class="col-sm-7" >
                                        <textarea class="col-sm-7" name="privateMessage" id="privateMessage" placeholder="Private Message eingeben" rows="3"><?php echo "$messageContent" ?></textarea>
                                    </div>
                                </div>
                                <div class="form-group row" >
                                    <label class="col-sm-4 control-label" for="inputTrenner">TeamSpeak Namen Trenner</label>
                                    <div class="col-sm-4">
                                        <input class="form-control" id="inputTrenner" type="text" name="accept_rules_name_seperators" placeholder="-,/,|" value=<?php echo '"' . $config[$acprKey . "_accept_rules_name_seperators"] . '"' ?> required/>
                                    </div>
                                </div>
                                <div class="form-group row" >
                                    <label class="col-sm-7 control-label" for="inputForbiddenNames">Verbotene Teamspeaknamen in einem regex format</label>
                                </div>
<?php
$counter = 1;
if( ! empty($forbiddenNames) ){
foreach($forbiddenNames as $key=>$value){ ?>
                                    <div class="form-group row">
                                        <div class="col-sm-4"></div>
                                        <div class="col-sm-4">
                                            <input class="form-control" id="item<?php echo "$counter";?>" type="text" name="item<?php echo "$counter";?>"  value='<?php echo "$key"; ?>' />
                                        </div>
                                        <div class="text-center">
                                            <button name="rmItem<?php echo "$counter";?>" type="submit" class="btn btn-danger" ><i class="fas fa-trash-alt"></i></button>
                                        </div>
                                    </div>
<?php $counter++; }
}
for ($x = 1; $x <= 3; $x++) { ?>

                                    <div class="form-group row">
                                        <div class="col-sm-4"></div>
                                        <div class="col-sm-4">
                                            <input class="form-control" id="newItem<?php echo "$x"?>" type="text" name="newItem<?php echo "$x";?>"/>
                                        </div>
                                    </div>
<?php } ?>
                                <div class="text-center">
                                    <button type="submit" class="btn btn-success" name="save"><i class="fas fa-plus"></i></button>
                                </div>
                                <br>
                                <div class="row">&nbsp;</div>
                                <div class="row" style="display: block;">
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary" name="save"><i class="fas fa-save"></i>&nbsp;speichern</button>
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                            </form>
                        </div>
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
        <script>
            function countChar(val) {
                var len = val.value.length;
                if (len > 100) {
                    val.value = val.value.substring(0, 100);
                } else {
                    $('#charNum').text("verbleibend: " + (100 - len).toString());
                }
            };
        </script>
    </body>
</html>