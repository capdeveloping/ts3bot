<?php
    require_once($_SERVER["DOCUMENT_ROOT"] . '/_preload.php');
    $_SESSION["nav_expanded"] = TRUE;
    $saved = FALSE;
    if (array_key_exists("ChannelAutoCreate", $_SESSION["functions"])) {
        $cacKey = $_SESSION["functions"]["ChannelAutoCreate"];

        if(filesize($_SESSION["config"][$cacKey . "_channel_check_password_file_path"])) {
            $channelPasswords = fileReadContentWithSeparator($_SESSION["config"][$cacKey . "_channel_check_password_file_path"], " = ");
        }else{
            $channelPasswords = [];
        }
    }else{
        header("Refresh:0; url=/core.php");
        exit();
    }

    foreach($_POST as $key => $value){
        if( str_starts_with($key, "rmItem")){
            $number = str_replace("rmItem", "", $key);
            unset($channelPasswords[$_POST['itemKey' . $number]]);
            writeConfigFileWithSeparator($channelPasswords, $_SESSION["config"][$cacKey . "_channel_check_password_file_path"], " = ");
            $saved = TRUE;
        }
    }

    if (isset($_POST['save'])){
        $channelPasswords = [];
        foreach($_POST as $key => $value){
            if( str_starts_with($key, "newItemKey") && ! empty($value) ){
                $number = str_replace("newItemKey", "", $key);
                $channelPasswords[$value] = $_POST["newItem" . $number];
            }
            if( str_starts_with($key, "itemKey") && ! empty($value) ){
                $number = str_replace("itemKey", "", $key);
                $channelPasswords[$value] = $_POST["item" . $number];
            }
        }
        $_SESSION["config"][$cacKey . "_channel_check_subchannel"] = $_POST['channel_check_subchannel'];

        writeConfigFileWithSeparator($channelPasswords, $_SESSION["config"][$cacKey . "_channel_check_password_file_path"], " = ");

        saveConfig($_SESSION["config"], $_SESSION["configPath"]);
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
        <title>Funktion - Automatisches Channel erstellen</title>
        <link href="../css/styles.css" rel="stylesheet" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/js/all.min.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="../js/virtual-select.min.css" />
        <script src="../js/virtual-select.min.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <?php
            require_once($_SERVER["DOCUMENT_ROOT"] . '/_nav-header.php');
        ?>
        <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
                <?php
                    require_once($_SERVER["DOCUMENT_ROOT"] . '/_nav.php');
                ?>
            </div>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">Channel auto create</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Funktionen</li>
                            <li class="breadcrumb-item active">ChannelAutoCreate</li>
                        </ol>
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-chart-area mr-1"></i>
                                Settings
                            </div>
                            <br>
                            <?php if($saved) { ?>
                            <br>
                            <div class="col-md-3"></div>
                            <div id="savedDiv" class="row saved-row">
                                <label class="saved-label">Config gespeichert. Bitte den Bot neustarten! Oder '!botconfigreload' dem bot schreiben!</label>
                            </div>
                            <br>
                            <?php }?>
                            <form class="form-horizontal" data-toggle="validator" name="save" method="POST">
                                <div class="form-group row">
                                    <label class="col-sm-4 control-label" for="inputParentIDs">Eine mit Komma getrennte Liste (ohne Leerzeichen) mit Parent Channel IDs.</label>
                                    <div class="col-sm-4">
                                        <div name="channel_check_subchannel" id="multiple-select"></div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-4 control-label" for="inputChannelPasswords">Einträge mit Channel Passwörtern</label>
                                    <div class="col-sm-2">
                                        <label class="control-label" for="inputChannelPasswords">Hauptchannel ID</label>
                                    </div>
                                    <div class="col-xs-4">
                                        <label class="col-sm-12 control-label" for="inputChannelPasswords">Passwort</label>
                                    </div>
                                </div>
<?php
$counter = 1;
if( ! empty($channelPasswords) ){
foreach($channelPasswords as $key=>$value){ ?>
                                    <div class="form-group row">
                                        <div class="col-sm-4"></div>
                                        <div class="col-sm-2">
                                            <select name="itemKey<?php echo "$counter";?>" class="form-select">
<?php foreach ($_SESSION['db_channels'] as $id=>$name){
        if ( $id === $key) {?>
                                                <option selected value="<?php echo $id?>"><?php print_r("(" . $id . ") " . $name)?></option>
<?php   } else {?>
                                                <option value="<?php echo $id?>"><?php print_r("(" . $id . ") " . $name)?></option>
<?php   }
}?>
                                            </select>
                                        </div>
                                        =
                                        <div class="col-sm-3">
                                            <input class="form-control" id="item<?php echo "$counter";?>" type="text" name="item<?php echo "$counter";?>"  value='<?php echo "$value"; ?>' />
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
                                        <div class="col-sm-2">
                                            <select name="newItemKey<?php echo "$x";?>" class="form-select">
                                                <option value="">-- Channel auswählen --</option>
<?php foreach ($_SESSION['db_channels'] as $id=>$name){?>
                                                <option value="<?php echo $id?>"><?php print_r("(" . $id . ") " . $name)?></option>
<?php }?>
                                            </select>
                                        </div>
                                        =
                                        <div class="col-sm-3">
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
                        require_once($_SERVER["DOCUMENT_ROOT"] . '/_footer.php');
                    ?>
                </footer>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="../js/scripts.js"></script>
        <script>
          function getChannels() {
              var optionsData = [];

<?php foreach ($_SESSION['db_channels'] as $id=>$name){?>
              optionsData.push({ value: "<?php echo $id?>", label: "<?php print_r("(" . $id . ") " . $name)?>"});
<?php }?>
              return optionsData;
         }

          function getSelected() {
              var optionsData = [<?php echo getJSSelectOption($_SESSION['db_channels'], $_SESSION["config"][$cacKey . "_channel_check_subchannel"]);?>];
              return optionsData;
         }

         VirtualSelect.init({
            ele: '#multiple-select',
            options: getChannels(),
            multiple: true,
            selectedValue: getSelected(),
            placeholder: 'Channel auswählen',
         });
        </script>
    </body>
</html>