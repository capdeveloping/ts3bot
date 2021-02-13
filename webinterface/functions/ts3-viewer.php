<?php
    require_once($_SERVER["DOCUMENT_ROOT"] . '/_preload.php');
    $_SESSION["nav_expanded"] = TRUE;
    if (array_key_exists("Viewer", $_SESSION["functions"])) {
        $viewerKey = $_SESSION["functions"]["Viewer"];
    }else{
        header("Refresh:0; url=/core.php");
        exit();
    }
    $saved = FALSE;
    if (isset($_POST['update'])){

        $_SESSION["config"][$viewerKey . "_ts3_viewer_update_time"] = $_POST['ts3_viewer_update_time'];
        $_SESSION["config"][$viewerKey . "_ts3_viewer_file_location"] = $_POST['ts3_viewer_file_location'];
        $_SESSION["config"][$viewerKey . "_ts3_viewer_text_color"] = $_POST['ts3_viewer_text_color'];
        $_SESSION["config"][$viewerKey . "_ts3_viewer_background_color"] = $_POST['ts3_viewer_background_color'];
        $_SESSION["config"][$viewerKey . "_ts3_viewer_server_ip"] = $_POST['ts3_viewer_server_ip'];

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
        <title>Funktion - TS3 Viewer</title>
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
                        <h1 class="mt-4">Ts3 Viewer</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Funktionen</li>
                            <li class="breadcrumb-item active">Ts3 Viewer</li>
                        </ol>
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-chart-area mr-1"></i>
                                Settings
                            </div>
                            <br>
                            <form class="form-horizontal" data-toggle="validator" name="update" method="POST">
                                <div class="form-group row">
                                    <label class="col-sm-4 control-label" for="inputUpdateTime">HTML File update Zeitintervall</label>
                                    <div class="col-sm-4 input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">in Minuten</span>
                                        </div>
                                        <input class="form-control" id="inputUpdateTime" type="text" name="ts3_viewer_update_time" placeholder="1" value=<?php echo '"' . $_SESSION["config"][$viewerKey . "_ts3_viewer_update_time"] . '"' ?> required/>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-4 control-label" for="inputFileLocation">HTML File output location</label>
                                    <div class="col-sm-4">
                                        <input class="form-control" id="inputFileLocation" type="text" name="ts3_viewer_file_location" placeholder="enter output location" value=<?php if ( ! empty($_SESSION["config"][$viewerKey . "_ts3_viewer_file_location"])){ echo '"' . $_SESSION["config"][$viewerKey . "_ts3_viewer_file_location"] . '"'; } else { echo '"' . $_SESSION["configFolderPath"] . 'ts3viewer.html"';} ?> required/>
                                    </div>
                                </div>
                                <div class="form-group row" >
                                    <label class="col-sm-4 control-label" for="inputTextColor">Text Color</label>
                                    <div class="col-sm-4">
                                        <input class="form-control" id="inputTextColor" type="text" name="ts3_viewer_text_color" placeholder="enter viewer text color" value=<?php echo '"' . $_SESSION["config"][$viewerKey . "_ts3_viewer_text_color"] . '"' ?> required/>
                                    </div>
                                </div>
                                <div class="form-group row" >
                                    <label class="col-sm-4 control-label" for="inputBackgroundColor">Viewer Background Color</label>
                                    <div class="col-sm-4">
                                        <input class="form-control" id="inputBackgroundColor" type="text" name="ts3_viewer_background_color" placeholder="enter viewer background color" value=<?php echo '"' . $_SESSION["config"][$viewerKey . "_ts3_viewer_background_color"] . '"' ?> required/>
                                    </div>
                                </div>
                                <div class="form-group row" >
                                    <label class="col-sm-4 control-label" for="inputServerIp">Viewer Server IP</label>
                                    <div class="col-sm-4">
                                        <input class="form-control" id="inputServerIp" type="text" name="ts3_viewer_server_ip" placeholder="enter viewer server ip" value=<?php echo '"' . $_SESSION["config"][$viewerKey . "_ts3_viewer_server_ip"] . '"' ?> required/>
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