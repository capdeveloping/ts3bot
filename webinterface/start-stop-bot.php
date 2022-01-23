<?php
    require_once('templates/preload.php');

    if(isset($_SESSION['logfilter2'])) {
		$filter2 = $_SESSION['logfilter2'];
	} else {
		$filter2 = '';
	}
    $filters = '';
    $inactivefilter = '';
    if(isset($_POST['logfilter']) && in_array('critical', $_POST['logfilter'])) {
        $filters .= "CRITICAL,";
    } elseif(isset($_POST['logfilter'])) {
        $inactivefilter .= "CRITICAL,";
    }
    if(isset($_POST['logfilter']) && in_array('error', $_POST['logfilter'])) {
        $filters .= "ERROR,";
    } elseif(isset($_POST['logfilter'])) {
        $inactivefilter .= "ERROR,";
    }
    if(isset($_POST['logfilter']) && in_array('warning', $_POST['logfilter'])) {
        $filters .= "WARNING,";
    } elseif(isset($_POST['logfilter'])) {
        $inactivefilter .= "WARNING,";
    }
    if(isset($_POST['logfilter']) && in_array('notice', $_POST['logfilter'])) {
        $filters .= "NOTICE,";
    } elseif(isset($_POST['logfilter'])) {
        $inactivefilter .= "NOTICE,";
    }
    if(isset($_POST['logfilter']) && in_array('info', $_POST['logfilter'])) {
        $filters .= "INFO,";
    } elseif(isset($_POST['logfilter'])) {
        $inactivefilter .= "INFO,";
    }
    if(isset($_POST['logfilter']) && in_array('debug', $_POST['logfilter'])) {
        $filters .= "DEBUG,";
    } elseif(isset($_POST['logfilter'])) {
        $inactivefilter .= "DEBUG,";
    }

    if($filters != '') {
        $_SESSION['logfilter'] = $filters;
    }

    if($inactivefilter != '') {
        $_SESSION['inactivefilter'] = $inactivefilter;
    }
    if(isset($_SESSION['inactivefilter']) && $_SESSION['inactivefilter'] != NULL) {
        $inactivefilter = explode(',', $_SESSION['inactivefilter']);
    }

    if (!isset($_SESSION['logfilter'])) {
        $_SESSION['logfilter'] = "CRITICAL,ERROR,WARNING,NOTICE,INFO,DEBUG,";
    }

    $number_lines = 20;
    if (isset($_POST['number'])){
        $number_lines = $_POST['number'];
    }

    $filters = explode(',', ($_SESSION['logfilter'].'NONE'));
	$logoutput = getlog($number_lines,$filters,$filter2,$inactivefilter);

    if (isset($_POST['start'])){
        $logoutput = startScript("start");
        header("Refresh:5");
    }
    if (isset($_POST['stop'])){
        startScript("stop");
    }
    if (isset($_POST['restart'])){
        startScript("restart");
        header("Refresh:5");
    }

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Start / Stop Bot";
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/header.php');
    //endregion
?>
    <body id="page-top">
        <!-- Page Wrapper -->
        <div id="wrapper">
<?php require_once('templates/nav.php'); ?>
            <!-- Content Wrapper -->
            <div id="content-wrapper" class="d-flex flex-column">
                <!-- Main Content -->
                <div id="content">
<?php require_once('templates/nav-header.php'); ?>
                    <!-- Begin Page Content -->
                    <div class="container-fluid">
                        <h1 class="mt-4">Start / Stop Bot</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active">Start / Stop Bot</li>
                        </ol>
                        <div class="card card-body mb-4">
                            <br>
                            <form class="form-horizontal" data-toggle="validator" name="update" method="POST">
                                <div class="col-md-3"></div>
                                <div class="row">&nbsp;</div>
                                <div class="row" style="display: block;">
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary" name="start"><i class="fa fa-power-off fa-w-16"></i>&nbsp;Bot starten</button>
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                                <div class="row" style="display: block;">
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary" name="stop"><i class="fa fa-times fa-w-11"></i>&nbsp;Bot stoppen</button>
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                                <div class="row" style="display: block;">
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary" name="restart"><i class="fa fa-sync fa-w-16"></i>&nbsp;Bot neustarten</button>
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                                <div class="row">
                                    <h4 class="col-md-2" for="inputInstanceName">Bot log</h4>
                                    <div class="col-lg-1">
                                        <div class="checkbox">
                                            <label><input class="switch-create-destroy" type="checkbox" name="logfilter[]" value="info" data-switch-no-init onchange="this.form.submit();"
                                            <?PHP if(in_array('INFO', $filters)) { echo "checked"; } ?>
                                            >Info</label>
                                        </div>
                                    </div>
                                    <div class="col-lg-1">
                                        <div class="checkbox">
                                            <label><input class="switch-create-destroy" type="checkbox" name="logfilter[]" value="notice" data-switch-no-init onchange="this.form.submit();"
                                            <?PHP if(in_array('NOTICE', $filters)) { echo "checked"; } ?>
                                            >Notice</label>
                                        </div>
                                    </div>
                                    <div class="col-lg-1">
                                        <div class="checkbox">
                                            <label><input class="switch-create-destroy" type="checkbox" name="logfilter[]" value="warning" data-switch-no-init onchange="this.form.submit();"
                                            <?PHP if(in_array('WARNING', $filters)) { echo "checked"; } ?>
                                            >Warning</label>
                                        </div>
                                    </div>
                                    <div class="col-lg-1">
                                        <div class="checkbox">
                                            <label><input class="switch-create-destroy" type="checkbox" name="logfilter[]" value="critical" data-switch-no-init onchange="this.form.submit();"
                                            <?PHP if(in_array('CRITICAL', $filters)) { echo "checked"; } ?>
                                            >Critical</label>
                                        </div>
                                    </div>
                                    <div class="col-lg-1">
                                        <div class="checkbox">
                                            <label><input class="switch-create-destroy" type="checkbox" name="logfilter[]" value="error" data-switch-no-init onchange="this.form.submit();"
                                            <?PHP if(in_array('ERROR', $filters)) { echo "checked"; } ?>
                                            >Error</label>
                                        </div>
                                    </div>
                                    <div class="col-lg-1">
                                        <div class="checkbox">
                                            <label><input class="switch-create-destroy" type="checkbox" name="logfilter[]" value="debug" data-switch-no-init onchange="this.form.submit();"
                                            <?PHP if(in_array('DEBUG', $filters)) { echo "checked"; } ?>
                                            >Debug</label>
                                        </div>
                                    </div>
                                    <div class="col-md-2">
                                            <select class="selectpicker show-tick form-control" id="number" name="number" onchange="this.form.submit();">
                                            <?PHP
                                            echo '<option value="20"'; if($number_lines=="20") echo " selected=selected"; echo '>20</option>';
                                            echo '<option value="50"'; if($number_lines=="50") echo " selected=selected"; echo '>50</option>';
                                            echo '<option value="100"'; if($number_lines=="100") echo " selected=selected"; echo '>100</option>';
                                            echo '<option value="200"'; if($number_lines=="200") echo " selected=selected"; echo '>200</option>';
                                            echo '<option value="500"'; if($number_lines=="500") echo " selected=selected"; echo '>500</option>';
                                            echo '<option value="2000"'; if($number_lines=="2000") echo " selected=selected"; echo '>2000</option>';
                                            echo '<option value="9999"'; if($number_lines=="9999") echo " selected=selected"; echo '>9999</option>';
                                            ?>
                                            </select>
                                    </div>
                                </div>
                                <div class="card mb-4" style="margin-left: 20px;margin-right: 20px;margin-top: 10px">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <pre><?PHP if(isset($err_msg)){
                                                echo $err_msg;
                                            }else{
                                                foreach ($logoutput as $line) {
                                                    echo $line;
                                                }
                                            }
                                            ?>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <!-- End of Page Content -->
                </div>
                <!-- End of Main Content -->
<?php require_once('templates/footer.php'); ?>
            </div>
            <!-- End of Content Wrapper -->
        </div>
        <!-- End of Page Wrapper -->
    </body>
</html>