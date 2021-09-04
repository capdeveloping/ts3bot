<?php
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/preload.php');
    $_SESSION["nav_expanded"] = TRUE;

    if ( ! array_key_exists("ClientAFK", $_SESSION["functions"])) {
        header("Refresh:0; url=/core.php");
        exit();
    }

    $saved = FALSE;
    if ( isset($_POST['update']) ){
        foreach($_SESSION["functions"]["ClientAFK"] as $number => $key) {
            $_SESSION["config"][$key . "_client_afk_time"] = $_POST['afkTime-' . $key];
            $_SESSION["config"][$key . "_client_afk_channel"] = $_POST['afkChannel-' . $key];
            if( ! empty($_POST['afkChannelIo-' . $key]) ){
                $_SESSION["config"][$key . "_client_afk_channel_io"] = $_POST['afkChannelIo-' . $key];
            }else{
                $_SESSION["config"][$key . "_client_afk_channel_io"] = "";
            }
            if( ! empty($_POST['afkGroupIds-' . $key]) ){
                $_SESSION["config"][$key . "_client_afk_group_ids"] = $_POST['afkGroupIds-' . $key];
            }else{
                $_SESSION["config"][$key . "_client_afk_group_ids"] = "";
            }
            $_SESSION["config"][$key . "_client_afk_channel_watch"] = $_POST['afkChannelWatch-' . $key];
            $_SESSION["config"][$key . "_client_afk_group_watch"] = $_POST['afkGroupWatch-' . $key];
        }
        $saved = TRUE;
        saveConfig($_SESSION["config"], $_SESSION["configPath"]);
    }

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Function - Client afk mover";
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/header.php');
    //endregion
?>
    <body class="sb-nav-fixed">
<?php require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/nav-header.php'); ?>
        <div id="layoutSidenav">
<?php require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/nav.php'); ?>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">Client AFK</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Funktionen</li>
                            <li class="breadcrumb-item active">Client AFK</li>
                        </ol>

                        <form class="form-horizontal" data-toggle="validator" name="addFunction" method="POST">
<?php foreach($_SESSION["functions"]["ClientAFK"] as $number=>$key){ ?>
<?php if($number % 2 == 0 || $number == 0){ ?>
                            <div class="row">
<?php }?>
                                <div class="col-lg-6">
                                    <div class="card mb-4">
                                        <div class="card-header">
                                            Settings für die Funktion mit der ID: <?php echo $key; ?>
                                        </div>
                                        <br>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkTime-' . $key . '"'; ?>  >AFK Zeit eines Users</label>
                                                </div>
                                                <div class="col-sm-4 input-group">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text">in Sekunden</span>
                                                    </div>
                                                    <input name=<?php echo '"afkTime-' . $key . '"'; ?> class="form-control" id=<?php echo '"afkTime-' . $key . '"'; ?> type="text" placeholder=<?php echo "Zeit eingeben in Sekunden"; ?> value=<?php echo '"' . $_SESSION["config"][$key . "_client_afk_time"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkChannel-' . $key . '"'; ?> >AFK Channel ID</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <select name=<?php echo '"afkChannel-' . $key . '"'; ?> class="form-select">
<?php foreach ($_SESSION['db_channels'] as $id=>$name){
        if ( strval($id) === $_SESSION["config"][$key . "_client_afk_channel"]) {
?>
                                                        <option selected value="<?php echo $id?>"><?php print_r("(" . $id . ") " . $name)?></option>
<?php   } else {?>
                                                        <option value="<?php echo $id?>"><?php print_r("(" . $id . ") " . $name)?></option>
<?php   }
}?>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkChannelIo-' . $key . '"'; ?> >Channels auf die geachtet oder die ignoriert werden sollen</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div name=<?php echo '"afkChannelIo-' . $key . '"'; ?> id="multiple-select-afk-channel-<?php echo $key?>"></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkChannelWatch-' . $key . '"'; ?> >Ignoriere die oberen Channel oder überprüfe nur diese</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <select name=<?php echo '"afkChannelWatch-' . $key . '"'; ?> class="form-select" aria-label="select2">
<?php if ($_SESSION["config"][$key . "_client_afk_channel_watch"] === "ignore"){?>
                                                        <option selected value="ignore">ignore</option>
                                                        <option value="only">only</option>
<?php } else if ($_SESSION["config"][$key . "_client_afk_channel_watch"] === "only"){?>
                                                        <option value="ignore">ignore</option>
                                                        <option selected value="only">only</option>
<?php } else { ?>
                                                        <option value="ignore">ignore</option>
                                                        <option value="only">only</option>
<?php } ?>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkGroupIds-' . $key . '"'; ?> >Gruppen auf die geachtet oder die ignoriert werden sollen</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div name=<?php echo '"afkGroupIds-' . $key . '"'; ?> id="multiple-select-groups-<?php echo $key?>"></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkGroupWatch-' . $key . '"'; ?> >Ignoriere die oberen Gruppen oder überprüfe nur diese</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <select name=<?php echo '"afkGroupWatch-' . $key . '"'; ?> class="form-select" aria-label="select">
<?php if ($_SESSION["config"][$key . "_client_afk_group_watch"] === "ignore"){?>
                                                        <option selected value="ignore">ignore</option>
                                                        <option value="only">only</option>
<?php } else if ($_SESSION["config"][$key . "_client_afk_group_watch"] === "only"){?>
                                                        <option value="ignore">ignore</option>
                                                        <option selected value="only">only</option>
<?php } else { ?>
                                                        <option value="ignore">ignore</option>
                                                        <option value="only">only</option>
<?php } ?>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
<?php if($number % 2 != 0 || count($_SESSION["functions"]["ClientAFK"]) == 0 ){ ?>
                            </div>
<?php }
    if ( count($_SESSION["functions"]["ClientAFK"]) == ($number + 1) && $number % 2 == 0){?>
                            </div>
                            <div class="row">
                                 <div class="col-lg-6">
                                 </div>
                            </div>
<?php }
} ?>
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
<?php require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/footer.php'); ?>
            </div>
        </div>
        <script type="text/javascript">
          function getSelected(key) {
              var optionsData = [];
              switch(key) {
<?php foreach($_SESSION["functions"]["ClientAFK"] as $number=>$key){?>
                case "channel-<?php echo $key?>":
                    optionsData = [<?php echo getJSSelectOption($_SESSION['db_channels'], $_SESSION["config"][$key . "_client_afk_channel_io"]);?>];
                    break;
                case "groups-<?php echo $key?>":
                    optionsData = [<?php echo getJSSelectOption($_SESSION['db_groups'], $_SESSION["config"][$key . "_client_afk_group_ids"]);?>];
                    break;
<?php }?>
              }
              return optionsData;
         }

<?php foreach($_SESSION["functions"]["ClientAFK"] as $number=>$key){?>
         VirtualSelect.init({
            ele: '#multiple-select-afk-channel-<?php echo $key?>',
            options: getChannels(),
            multiple: true,
            selectedValue: getSelected("channel-<?php echo $key?>"),
            placeholder: 'Channel auswählen',
          });

         VirtualSelect.init({
            ele: '#multiple-select-groups-<?php echo $key?>',
            options: getGroups(),
            multiple: true,
            selectedValue: getSelected("groups-<?php echo $key?>"),
            placeholder: 'Servergruppen auswählen',
          });
<?php }?>
        </script>
    </body>
</html>
