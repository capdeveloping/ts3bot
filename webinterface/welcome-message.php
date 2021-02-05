<?php
    require_once('_preload.php');
    $nav_expanded = TRUE;

    $saved = FALSE;
    if ( isset($_POST['update']) ){
        foreach($functions["WelcomeMessage"] as $number => $key) {
            $config[$key . "_welcome_message"] = $_POST['welcomeMessage-' . $key];
            $config[$key . "_welcome_poke_client"] = $_POST['enablePoke-' . $key];
            $config[$key . "_welcome_poke_message"] = $_POST['pokeMessage-' . $key];
            $config[$key . "_welcome_date"] = $_POST['date-' . $key];
            $config[$key . "_welcome_repeat"] = $_POST['repeat-' . $key];
            $config[$key . "_welcome_group_ids"] = $_POST['groupids-' . $key];
        }
        $saved = TRUE;
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
        <meta name="author" content="Capdeveloping" />
        <title>Funktion - Client Willkommensnachricht</title>
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
                        <h1 class="mt-4">Welcome Message für Clients</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Funktionen</li>
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
<?php foreach($functions["WelcomeMessage"] as $number=>$key){ ?>
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
                                                    <label class="control-label" for="welcomeMessage-<?php echo $key; ?>"  >Willkommensnachricht die der Client erhalten soll.</label>
                                                </div>
                                            </div>
                                            <div class="col-sm-12">
                                                <textarea name="welcomeMessage-<?php echo $key; ?>" class="form-control" id="inputWelcomeMessage-<?php echo $key; ?>" placeholder="Text eingeben die der Client erhalten soll" rows="3"><?php echo $config[$key . "_welcome_message"]; ?></textarea>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for="inputWelcomePokeClient-<?php echo $key; ?>" >Soll der Client angestupst werden?</label>
                                                </div>
                                                <div class="col-sm-1">
                                                    <label class="switch">
                                                      <input name="enablePoke-<?php echo $key; ?>" id="inputWelcomePokeClient-<?php echo $key; ?>" type="checkbox" checked >
                                                      <span class="slider round"></span>
                                                    </label>
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
                                                    <span id="charNum-<?php echo $key; ?>" class="input-group-text"></span>
                                                </div>
                                                <textarea name="pokeMessage-<?php echo $key; ?>" class="form-control" onkeyup="countChar(this, '<?php echo $key; ?>')" id="inputWelcomePokeMessage-<?php echo $key; ?>" placeholder="Text eingeben die der Client als Poke Nachricht erhalten soll" rows="1"><?php echo $config[$key . "_welcome_poke_message"]; ?></textarea>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for="inputWelcomeEndDate-<?php echo $key; ?>" >Wann ist das Enddatum?</label>
                                                </div>
                                                <div class="col-sm-4 input-group">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text"><i class="fa fa-calendar"></i></span>
                                                    </div>
                                                    <input class="form-control" name="date-<?php echo $key; ?>" onkeyup="checkDate(this, '<?php echo $key; ?>')" id="inputWelcomeEndDate-<?php echo $key; ?>" type="text" placeholder="dd.MM.yy HH:mm" value=<?php echo '"' . $config[$key . "_welcome_date"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for="inputWelcomeRepeat-<?php echo $key; ?>" >Wie hoft soll die Nachricht gesendet werden? daily/always</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name="repeat-<?php echo $key; ?>" class="form-control" id="inputWelcomeRepeat-<?php echo $key; ?>" type="text" placeholder=<?php echo "daily/always" ?> value=<?php echo '"' . $config[$key . "_welcome_repeat"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for="inputWelcomeGroupIds-<?php echo $key; ?>" >Welche Gruppe soll die Nachricht bekommen</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name="groupids-<?php echo $key; ?>" class="form-control" id="inputWelcomeGroupIds-<?php echo $key; ?>" type="text" placeholder=<?php echo "gruppen ids" ?> value=<?php echo '"' . $config[$key . "_welcome_group_ids"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
<?php if($number % 2 != 0 || count($functions["WelcomeMessage"]) == 0 ){ ?>
                            </div>
<?php }
    if ( count($functions["WelcomeMessage"]) == ($number + 1) && $number % 2 == 0){?>
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
                <footer class="py-4 bg-light mt-auto">
                    <div class="container-fluid">
                        <div class="d-flex align-items-center justify-content-between small">
                            <div class="text-muted">Copyright &copy; Capdeveloping 2021</div>
                            <div>
                                <a href="#">Privacy Policy</a>
                                &middot;
                                <a href="#">Terms &amp; Conditions</a>
                            </div>
                        </div>
                    </div>
                </footer>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="js/scripts.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.js" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
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
        </script>
    </body>
</html>
