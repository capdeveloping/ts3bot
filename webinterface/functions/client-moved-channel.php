<?php
    require_once($_SERVER["DOCUMENT_ROOT"] . '/_preload.php');
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
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="Capdeveloping" />
        <title>Funktion - Client joint Channel</title>
        <link href="../css/styles.css" rel="stylesheet" />
        <link href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css" rel="stylesheet" crossorigin="anonymous" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/js/all.min.js" crossorigin="anonymous"></script>
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
                                                    <label class="control-label" for=<?php echo '"clientJoindChannel-' . $key . '"'; ?>  >Client joint den channel</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name=<?php echo '"clientJoindChannel-' . $key . '"'; ?> class="form-control" id=<?php echo '"clientJoindChannel-' . $key . '"'; ?> type="text" placeholder=<?php echo "Channel ID eingeben"; ?> value=<?php echo '"' . $_SESSION["config"][$key . "_client_moved_channel"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"clientPokeClient-' . $key . '"'; ?> >Clients dieser Server Groupen sollen angestupst</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name=<?php echo '"clientPokeClient-' . $key . '"'; ?> class="form-control" id=<?php echo '"clientPokeClient-' . $key . '"'; ?> type="text" placeholder=<?php echo "liste von Gruppen ids"; ?> value=<?php echo '"' . $_SESSION["config"][$key . "_client_moved_group_notify"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"groupids-' . $key . '"'; ?> >Gruppen auf die geachtet oder die ignoriert werden sollen</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name=<?php echo '"groupids-' . $key . '"'; ?> class="form-control" id=<?php echo '"groupids-' . $key . '"'; ?> type="text" placeholder=<?php echo "Channel ids eingeben"; ?> value=<?php echo '"' . $_SESSION["config"][$key . "_client_moved_group_ids"] . '"'; ?> />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-7">
                                                    <label class="control-label" for=<?php echo '"action-' . $key . '"'; ?> >Ignoriere die oberen Gruppen oder überprüfe nur diese</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input name=<?php echo '"action-' . $key . '"'; ?> class="form-control" id=<?php echo '"action-' . $key . '"'; ?> type="text" placeholder=<?php echo "ignore/only"; ?> value=<?php echo '"' . $_SESSION["config"][$key . "_client_moved_group_action"] . '"'; ?> />
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
        <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" crossorigin="anonymous"></script>
    </body>
</html>
