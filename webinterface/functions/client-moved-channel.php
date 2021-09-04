<?php
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/preload.php');
    $_SESSION["nav_expanded"] = TRUE;

    if ( ! array_key_exists("ClientMove", $_SESSION["functions"])) {
        header("Refresh:0; url=/core.php");
        exit();
    }

    $saved = FALSE;
    if ( isset($_POST['update']) ){
        print_r($_POST);
        foreach($_SESSION["functions"]["ClientMove"] as $number => $key) {
            $_SESSION["config"][$key . "_client_moved_channel"] = $_POST['clientJoindChannel-' . $key];
            $_SESSION["config"][$key . "_client_moved_group_notify"] = $_POST['clientPokeClient-' . $key];
            $_SESSION["config"][$key . "_client_moved_group_ids"] = $_POST['groupids-' . $key];
            $_SESSION["config"][$key . "_client_moved_group_action"] = $_POST['action-' . $key];
        }
        $saved = TRUE;
        saveConfig($_SESSION["config"], $_SESSION["configPath"]);
    }

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Function - Client moved to channel";
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
                        <h1 class="mt-4">Client moved to channel</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Funktionen</li>
                            <li class="breadcrumb-item active">Client Moved</li>
                        </ol>
                        <?php if($saved) { ?>
                        <div class="col-md-3"></div>
                        <br>
                        <div id="savedDiv" class="row saved-row">
                            <label class="saved-label">Config gespeichert. Bitte den Bot neustarten! Oder '!botconfigreload' dem bot schreiben!</label>
                        </div>
                        <br>
                        <?php }?>
                        <form class="form-horizontal" data-toggle="validator" name="addFunction" method="POST">
<?php foreach($_SESSION["functions"]["ClientMove"] as $number=>$key){ ?>
<?php   if($number % 2 == 0 || $number == 0){ ?>
                            <div class="row">
<?php   }?>
                                <div class="col-lg-6">
                                    <div class="card mb-4">
                                        <div class="card-header">
                                            Settings für die Funktion mit der ID: <?php echo $key; ?>
                                        </div>
                                        <br>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"clientJoindChannel-' . $key . '"'; ?>  >Client joint den channel</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <select name=<?php echo '"clientJoindChannel-' . $key . '"'; ?> class="form-select" aria-label="select" placeholder=" -- Channel auswählen -- ">
<?php   foreach ($_SESSION['db_channels'] as $id=>$name){
            if ( strval($id) === $_SESSION["config"][$key . "_client_moved_channel"]) {
?>
                                                        <option selected value="<?php echo $id?>"><?php print_r("(" . $id . ") " . $name)?></option>
<?php       } else {?>
                                                        <option value="<?php echo $id?>"><?php print_r("(" . $id . ") " . $name)?></option>
<?php       }
        }?>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"clientPokeClient-' . $key . '"'; ?> >Clients dieser Server Groupen sollen angestupst</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div name=<?php echo '"clientPokeClient-' . $key . '"'; ?> id="multiple-select-<?php echo $key?>"></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"groupids-' . $key . '"'; ?> >Gruppen auf die geachtet oder die ignoriert werden sollen</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div name=<?php echo '"groupids-' . $key . '"'; ?> id="multiple-select-groupids-<?php echo $key?>"></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"action-' . $key . '"'; ?> >Ignoriere die oberen Gruppen oder überprüfe nur diese</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <select name=<?php echo '"action-' . $key . '"'; ?> class="form-select" id="basic">
<?php if ($_SESSION["config"][$key . "_client_moved_group_action"] === "ignore"){?>
                                                        <option selected value="ignore">ignore</option>
                                                        <option value="only">only</option>
<?php } else if ($_SESSION["config"][$key . "_client_moved_group_action"] === "only"){?>
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
<?php if($number % 2 != 0 || count($_SESSION["functions"]["ClientMove"]) == 0 ){ ?>
                        </div>
<?php }
    if ( count($_SESSION["functions"]["ClientMove"]) == ($number + 1) && $number % 2 == 0){?>
                            </div>
                            <div class="row">
                                 <div class="col-lg-6">
                                 </div>
                            </div>
<?php }
} ?>
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
        <script>
          function getSelected(key) {
              var optionsData = [];
              switch(key) {
<?php foreach($_SESSION["functions"]["ClientMove"] as $number=>$key){?>
                case "poke-<?php echo $key?>":
                    optionsData = [<?php echo getJSSelectOption($_SESSION['db_groups'], $_SESSION["config"][$key . "_client_moved_group_notify"]);?>];
                    break;
                case "groupids-<?php echo $key?>":
                    optionsData = [<?php echo getJSSelectOption($_SESSION['db_groups'], $_SESSION["config"][$key . "_client_moved_group_ids"]);?>];
                    break;
<?php }?>
              }
              return optionsData;
         }

<?php foreach($_SESSION["functions"]["ClientMove"] as $number=>$key){?>
         VirtualSelect.init({
            ele: '#multiple-select-<?php echo $key?>',
            options: getGroups(),
            multiple: true,
            selectedValue: getSelected("poke-<?php echo $key?>"),
            placeholder: 'Servergruppen auswählen',
          });

         VirtualSelect.init({
            ele: '#multiple-select-groupids-<?php echo $key?>',
            options: getGroups(),
            multiple: true,
            selectedValue: getSelected("groupids-<?php echo $key?>"),
            placeholder: 'Servergruppen auswählen',
          });
<?php }?>
        </script>
    </body>
</html>