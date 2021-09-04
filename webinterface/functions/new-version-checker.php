<?php
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/preload.php');
    $_SESSION["nav_expanded"] = TRUE;
    if (array_key_exists("VersionChecker", $_SESSION["functions"])) {
        $newVersionKey = $_SESSION["functions"]["VersionChecker"];
    } else {
        header("Refresh:0; url=/core.php");
        exit();
    }

    $saved = FALSE;
    if (isset($_POST['save'])){

        $_SESSION["config"][$newVersionKey . "_version_check_time"] = $_POST['version_check_time'];
        saveConfig($_SESSION["config"], $_SESSION["configPath"]);
        $saved = TRUE;
    }

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Function - New version checker";
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
                        <h1 class="mt-4">New Version Checker</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Funktionen</li>
                            <li class="breadcrumb-item active">New Version Checker</li>
                        </ol>
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-chart-area mr-1"></i>
                                Settings
                            </div>
                            <br>
                            <form class="form-horizontal" data-toggle="validator" name="save" method="POST">
                                <div class="form-group row">
                                    <label class="col-sm-4 control-label" for="inputTime">Checkintervall für neue Version</label>
                                    <div class="col-sm-4 input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">in Stunden</span>
                                        </div>
                                        <input class="form-control" id="inputTime" type="text" name="version_check_time" placeholder="enter update intervall" value='<?php echo $_SESSION["config"][$newVersionKey . "_version_check_time"] ?>' required/>
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
                                        <button type="submit" class="btn btn-primary" name="save"><i class="fas fa-save"></i>&nbsp;speichern</button>
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                            </form>
                        </div>
                    </div>
                </main>
<?php require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/footer.php'); ?>
            </div>
        </div>
    </body>
</html>