<?php
    require_once('_preload.php');
    $nav_expanded = TRUE;

    $saved = FALSE;
    if ( isset($_POST['update']) ){
        print_r($_POST);
        foreach($functions["ClientAFK"] as $number => $key) {
            $config[$key . "_client_afk_time"] = $_POST['afkTime-' . $key];
            $config[$key . "_client_afk_channel"] = $_POST['afkChannel-' . $key];
            if( ! empty($_POST['afkChannelIo-' . $key]) ){
                $config[$key . "_client_afk_channel_io"] = $_POST['afkChannelIo-' . $key];
            }else{
                $config[$key . "_client_afk_channel_io"] = "";
            }
            if( ! empty($_POST['afkGroupIds-' . $key]) ){
                $config[$key . "_client_afk_group_ids"] = $_POST['afkGroupIds-' . $key];
            }else{
                $config[$key . "_client_afk_group_ids"] = "";
            }
            $config[$key . "_client_afk_channel_watch"] = $_POST['afkChannelWatch-' . $key];
            $config[$key . "_client_afk_group_watch"] = $_POST['afkGroupWatch-' . $key];
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
        <title>Funktion - Client Mute Mover</title>
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
                        <h1 class="mt-4">Client AFK</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Funktionen</li>
                            <li class="breadcrumb-item active">Client AFK</li>
                        </ol>

                        <form class="form-horizontal" data-toggle="validator" name="addFunction" method="POST">
<?php foreach($functions["ClientAFK"] as $number=>$key){ ?>
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
                                                    <input name=<?php echo '"afkTime-' . $key . '"'; ?> class="form-control" id=<?php echo '"afkTime-' . $key . '"'; ?> type="text" placeholder=<?php echo "Zeit eingeben in Sekunden"; ?> value=<?php echo '"' . $config[$key . "_client_afk_time"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkChannel-' . $key . '"'; ?> >AFK Channel ID</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name=<?php echo '"afkChannel-' . $key . '"'; ?> class="form-control" id=<?php echo '"afkChannel-' . $key . '"'; ?> type="text" placeholder=<?php echo "Channel ID"; ?> value=<?php echo '"' . $config[$key . "_client_afk_channel"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkChannelIo-' . $key . '"'; ?> >Channels auf die geachtet oder die ignoriert werden sollen</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name=<?php echo '"afkChannelIo-' . $key . '"'; ?> class="form-control" id=<?php echo '"afkChannelIo-' . $key . '"'; ?> type="text" placeholder=<?php echo "Channel ids eingeben"; ?> value=<?php echo '"' . $config[$key . "_client_afk_channel_io"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkChannelWatch-' . $key . '"'; ?> >Ignoriere die oberen Channel oder überprüfe nur diese</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name=<?php echo '"afkChannelWatch-' . $key . '"'; ?> class="form-control" id=<?php echo '"afkChannelWatch-' . $key . '"'; ?> type="text" placeholder=<?php echo "ignore/only"; ?> value=<?php echo '"' . $config[$key . "_client_afk_channel_watch"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkGroupIds-' . $key . '"'; ?> >Gruppen auf die geachtet oder die ignoriert werden sollen</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name=<?php echo '"afkGroupIds-' . $key . '"'; ?> class="form-control" id=<?php echo '"afkGroupIds-' . $key . '"'; ?> type="text" placeholder=<?php echo "gruppen ids" ?> value=<?php echo '"' . $config[$key . "_client_afk_group_ids"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"afkGroupWatch-' . $key . '"'; ?> >Ignoriere die oberen Gruppen oder überprüfe nur diese</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name=<?php echo '"afkGroupWatch-' . $key . '"'; ?> class="form-control" id=<?php echo '"afkGroupWatch-' . $key . '"'; ?> type="text" placeholder=<?php echo "ignore/only" ?> value=<?php echo '"' . $config[$key . "_client_afk_group_watch"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
<?php if($number % 2 != 0 || count($functions["ClientAFK"]) == 0 ){ ?>
                            </div>
<?php }
    if ( count($functions["ClientAFK"]) == ($number + 1) && $number % 2 == 0){?>
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
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="js/scripts.js"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" crossorigin="anonymous"></script>
        <script src="assets/demo/datatables-demo.js"></script>
    </body>
</html>
