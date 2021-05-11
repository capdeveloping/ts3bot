<?php
    require_once('_preload.php');
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
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Start / Stop Bot</title>
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
                     include '_nav.php';
                ?>
            </div>
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
                <footer class="py-4 bg-light mt-auto">
                    <?php
                        require_once('_footer.php');
                    ?>
                </footer>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="js/scripts.js"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" crossorigin="anonymous"></script>
    </body>
</html>