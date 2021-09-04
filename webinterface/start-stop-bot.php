<?php
    require_once('templates/preload.php');

    $number_lines = 20;
    if (isset($_POST['number'])){
        $number_lines = $_POST['number'];
    }
    $logoutput = getlog($number_lines);
    $startlogoutput = getstartlog();
    if (isset($_POST['start'])){
        $logoutput = startScript("start");
        header("Refresh:5");
    }
    if (isset($_POST['stop'])){
        startScript("stop");
        $logoutput = getlog($number_lines);
    }
    if (isset($_POST['restart'])){
        startScript("restart");
        $logoutput = getlog($number_lines);
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
    <body class="sb-nav-fixed">
<?php require_once('templates/nav-header.php'); ?>
        <div id="layoutSidenav">
<?php require_once('templates/nav.php'); ?>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">Start / Stop Bot</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active">Start / Stop Bot</li>
                        </ol>
                        <div class="card mb-4">
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
                                    <div class="col-lg-8"></div>
                                    <div class="col-lg-2">
                                        <div class="text-center">
                                            <button type="submit" class="btn btn-primary" name="reloadLog"><i class="fa fa-power-off fa-w-16"></i>&nbsp;Log neuladen</button>
                                        </div>
                                    </div>
                                    <div class="col-lg-2">
                                        <div class="col-sm-8">
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
                                </div>
                            </form>
                            <h4 class="col-sm-2" for="inputInstanceName">Bot log</h4>
                            <div class="card mb-4" style="margin-left: 20px;margin-right: 20px;margin-top: 10px">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <pre><?PHP if(isset($err_msg)){
                                            echo $err_msg;
                                        }else{
                                            foreach ($logoutput as $line) { echo $line; }
                                        }
                                        ?>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
<?php require_once('templates/footer.php'); ?>
            </div>
        </div>
    </body>
</html>