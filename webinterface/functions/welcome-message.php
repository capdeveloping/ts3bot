<?php
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/preload.php');
    $_SESSION["nav_expanded"] = TRUE;

    if ( ! array_key_exists("WelcomeMessage", $_SESSION["functions"])) {
        header("Refresh:0; url=/core.php");
        exit();
    }

    $saved = FALSE;
    if ( isset($_POST['update']) ){
        foreach($_SESSION["functions"]["WelcomeMessage"] as $number => $key) {
            $_SESSION["config"][$key . "_welcome_message"] = $_POST['welcomeMessage-' . $key];
            if (filter_var($_POST['enablePoke-' . $key], FILTER_VALIDATE_BOOLEAN)){
                $_SESSION["config"][$key . "_welcome_poke_client"] = "true";
            }else{
                $_SESSION["config"][$key . "_welcome_poke_client"] = "false";
            }
            $_SESSION["config"][$key . "_welcome_poke_message"] = $_POST['pokeMessage-' . $key];
            if(empty($_POST['date-' . $key])){
                $_SESSION["config"][$key . "_welcome_date"] = "empty";
            }else{
                $_SESSION["config"][$key . "_welcome_date"] = $_POST['date-' . $key];
            }
            $_SESSION["config"][$key . "_welcome_repeat"] = $_POST['repeat-' . $key];
            $_SESSION["config"][$key . "_welcome_group_ids"] = $_POST['groupids-' . $key];
        }
        $saved = TRUE;
        saveConfig($_SESSION["config"], $_SESSION["configPath"]);
    }

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Function - Welcome message";
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/header.php');
    //endregion
?>
    <body class="sb-nav-fixed">
<?php require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/nav-header.php');?>
        <div id="layoutSidenav">
<?php require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/nav.php'); ?>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">Welcome Message for Clients</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Functions</li>
                            <li class="breadcrumb-item active">Welcome Message</li>
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
<?php foreach($_SESSION["functions"]["WelcomeMessage"] as $number=>$key){ ?>
<?php if($number % 2 == 0 || $number == 0){ ?>
                            <div class="row">
<?php }?>
                                <div class="col-lg-6">
                                    <div class="card mb-4">
                                        <div class="card-header">
                                            Settings für die Funktion mit der ID: <?php echo $key; ?></div>
                                        <br>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for="welcomeMessage-<?php echo $key; ?>" >Willkommensnachricht die der Client erhalten soll.</label>
                                                </div>
                                            </div>
                                            <div class="col-sm-12">
                                                <textarea name="welcomeMessage-<?php echo $key; ?>" class="form-control" id="inputWelcomeMessage-<?php echo $key; ?>" placeholder="Text eingeben die der Client erhalten soll" rows="3"><?php echo $_SESSION["config"][$key . "_welcome_message"]; ?></textarea>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-8">
                                                    <label class="control-label" for="inputWelcomePokeClient-<?php echo $key; ?>" >Soll der Client angestupst werden?</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <select name=<?php echo '"enablePoke-' . $key . '"'; ?> class="form-select" aria-label="select">
<?php if ($_SESSION["config"][$key . "_welcome_poke_client"] === "true"){?>
                                                        <option selected value="true">anstupsen</option>
                                                        <option value="false">nicht anstupsen</option>
<?php } else if ( $_SESSION["config"][$key . "_welcome_poke_client"] === "false"){?>
                                                        <option value="true">anstupsen</option>
                                                        <option selected value="false">nicht anstupsen</option>
<?php } else { ?>
                                                        <option value="true">anstupsen</option>
                                                        <option value="false">nicht anstupsen</option>
<?php } ?>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-8">
                                                    <label class="control-label" for="pokeMessage-<?php echo $key; ?>" >Willkommensnachricht die der Client als Poke Nachricht erhalten soll.</label>
                                                </div>
                                            </div>
                                            <div class="col-sm-12 input-group">
                                                <div class="input-group-prepend">
                                                    <span id="charNum-<?php echo $key; ?>" class="input-group-text"><?php echo "verbleibend: " . 100 - strlen($_SESSION["config"][$key . "_welcome_poke_message"]); ?></span>
                                                </div>
                                                <textarea name="pokeMessage-<?php echo $key; ?>" class="form-control" onkeyup="countChar(this, '<?php echo $key; ?>')" id="inputWelcomePokeMessage-<?php echo $key; ?>" placeholder="Text eingeben die der Client als Poke Nachricht erhalten soll" rows="1"><?php echo $_SESSION["config"][$key . "_welcome_poke_message"]; ?></textarea>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-8">
                                                    <label class="control-label" for="inputWelcomeEndDate-<?php echo $key; ?>" >Wann ist das Enddatum? (leer lassen, falls es niemals ablaufen soll)</label>
                                                </div>
                                                <div class="col-sm-4 input-group">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text"><i class="fa fa-calendar"></i></span>
                                                    </div>
                                                    <input class="form-control" name="date-<?php echo $key; ?>" onkeyup="checkDate(this, '<?php echo $key; ?>')" id="inputWelcomeEndDate-<?php echo $key; ?>" type="text" placeholder="dd.MM.yy HH:mm" value=<?php echo '"' . $_SESSION["config"][$key . "_welcome_date"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-8">
                                                    <label class="control-label" for="inputWelcomeRepeat-<?php echo $key; ?>" >Wie hoft soll die Nachricht gesendet werden?</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <select name=<?php echo '"repeat-' . $key . '"'; ?> class="form-select" aria-label="select">
<?php if ($_SESSION["config"][$key . "_welcome_repeat"] === "daily"){?>
                                                        <option selected value="daily">täglich</option>
                                                        <option value="always">jedesmal</option>
<?php } else if ( $_SESSION["config"][$key . "_welcome_repeat"] === "always"){?>
                                                        <option value="daily">täglich</option>
                                                        <option selected value="always">jedesmal</option>
<?php } else { ?>
                                                        <option value="daily">täglich</option>
                                                        <option value="always">jedesmal</option>
<?php } ?>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-8">
                                                    <label class="control-label" for="inputWelcomeGroupIds-<?php echo $key; ?>" >Welche Gruppe(id) soll die Nachricht bekommen?</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div name=<?php echo '"groupids-' . $key . '"'; ?> id="multiple-select-<?php echo $key?>"></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
<?php if($number % 2 != 0 || count($_SESSION["functions"]["WelcomeMessage"]) == 0 ){ ?>
                            </div>
<?php }
    if ( count($_SESSION["functions"]["WelcomeMessage"]) == ($number + 1) && $number % 2 == 0){?>
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
            function countChar(val, key) {
                var len = val.value.length;
                if (len > 100) {
                    val.value = val.value.substring(0, 100);
                } else {
                    $('#charNum-' + key).text("verbleibend: " + (100 - len).toString());
                }
            };
            function checkDate(val, key) {
                var momentDate = moment(val.value, 'dd.MM.yy HH:mm');

                if ( ! momentDate.isValid() || val.value.length != 14) {
                    document.getElementById("inputWelcomeEndDate-" + key).style.color = "red";
                } else {
                    document.getElementById("inputWelcomeEndDate-" + key).style.color = "green";
                }
            };

          function getSelected(key) {
              var optionsData = [];
              switch(key) {
<?php foreach($_SESSION["functions"]["WelcomeMessage"] as $number=>$key){?>
                case "groups-<?php echo $key?>":
                    optionsData = [<?php echo getJSSelectOption($_SESSION['db_groups'], $_SESSION["config"][$key . "_welcome_group_ids"]);?>];
                    break;
<?php }?>
              }
              return optionsData;
         }

<?php foreach($_SESSION["functions"]["WelcomeMessage"] as $number=>$key){?>

         VirtualSelect.init({
            ele: '#multiple-select-<?php echo $key?>',
            options: getGroups(),
            multiple: true,
            selectedValue: getSelected("groups-<?php echo $key?>"),
            placeholder: 'Servergruppen auswählen',
          });
<?php }?>
        </script>
    </body>
</html>
