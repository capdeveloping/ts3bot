<?php
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/preload.php');
    $_SESSION["nav_expanded"] = TRUE;
    if (array_key_exists("AutoRemove", $_SESSION["functions"])) {
        $autormKey = $_SESSION["functions"]["AutoRemove"];
    }else{
        header("Refresh:0; url=/core.php");
        exit();
    }
    $saved = FALSE;
    if (isset($_POST['update'])){

        $_SESSION["config"][$autormKey . "_auto_remove_group_ids"] = $_POST['auto_remove_group_ids'];

        saveConfig($_SESSION["config"], $_SESSION["configPath"]);
        $saved = TRUE;
    }

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Function - Auto Remove Server Group";
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/header.php');
    //endregion
?>
    <body id="page-top">
        <!-- Page Wrapper -->
        <div id="wrapper">
<?php require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/nav.php'); ?>
            <!-- Content Wrapper -->
            <div id="content-wrapper" class="d-flex flex-column">
                <!-- Main Content -->
                <div id="content">
<?php require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/nav-header.php'); ?>
                    <!-- Begin Page Content -->
                    <div class="container-fluid">
                        <h1 class="mt-4">AutoRemove Servergroup</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item">Settings</a></li>
                            <li class="breadcrumb-item">Funktionen</li>
                            <li class="breadcrumb-item active">AutoRemove</li>
                        </ol>
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-chart-area mr-1"></i>
                                Settings
                            </div>
                            <br>
                            <div class="card-body">
                                <form class="form-horizontal" data-toggle="validator" name="update" method="POST">
                                    <div class="form-group row">
                                        <label class="col-sm-4 control-label" for="inputServergroup">AutoRemove Servergroup</label>
                                        <div class="col-sm-4">
                                            <div name="auto_remove_group_ids" id="single-select"></div>
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
                            <!-- End of card-body -->
                        </div>
                    </div>
                    <!-- End of Page Content -->
                </div>
                <!-- End of Main Content -->
<?php require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/footer.php'); ?>
            </div>
            <!-- End of Content Wrapper -->
        </div>
        <!-- End of Page Wrapper -->
        <script>
            VirtualSelect.init({
                ele: '#single-select',
                options: getGroups(),
                multiple: false,
                search: true,
                selectedValue: ["<?php echo $_SESSION["config"][$autormKey . "_auto_remove_group_ids"];?>"],
                placeholder: '-- Servergruppe auswählen --',
            });
        </script>
    </body>
</html>