<?php
    require_once('templates/preload.php');
    require_once('templates/progress_functions.php');

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Dashboard - TS3 Bot";
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
                        <h1 class="mt-4">Dashboard</h1>
                        <hr>
                        <div class="row">
                            <!-- Tasks Card Example -->
                            <div class="col-xl-3 col-md-6">
                                <div class="card border-left-info shadow h-100 py-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2 text-xs font-weight-bold text-info text-uppercase mb-1">
                                                <?php if( ! empty($_SESSION["instances"]) ){ echo "Bots active"; }else{ echo "Keine Instanzen"; }?>
                                            </div>
                                            <div id="piechart"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Progress Small -->
                            <div class="col-xl-3 col-md-6">
                                <div class="card border-left-info shadow h-100 py-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2 text-xs font-weight-bold text-info text-uppercase mb-1">
                                                Erstellte Channel
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="mb-1 small text-center"><?php echo $_SESSION['status']['channel_create_count'];?></div>
                                        </div>
                                        <div class="progress mb-4">
                                            <div class="progress-bar" role="progressbar" style="width: <?php print_r(getProgressWidth($_SESSION['status']['channel_create_count']));?>%" aria-valuemin="<?php print_r(getProgressMin($_SESSION['status']['channel_create_count']));?>" aria-valuemax="<?php print_r(getProgressMax($_SESSION['status']['channel_create_count']));?>"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="card border-left-info shadow h-100 py-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2 text-xs font-weight-bold text-info text-uppercase mb-1">
                                                Gelöschte Channel
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="mb-1 small text-center"><?php echo $_SESSION['status']['channel_delete_count'];?></div>
                                        </div>
                                        <div class="progress mb-4">
                                            <div class="progress-bar" role="progressbar" style="width: <?php print_r(getProgressWidth($_SESSION['status']['channel_delete_count']));?>%" aria-valuemin="<?php print_r(getProgressMin($_SESSION['status']['channel_delete_count']));?>" aria-valuemax="<?php print_r(getProgressMax($_SESSION['status']['channel_delete_count']));?>"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Progress Small -->
                            <div class="col-xl-3 col-md-6">
                                <div class="card border-left-info shadow h-100 py-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2 text-xs font-weight-bold text-info text-uppercase mb-1">
                                                Gesendete Willkommensnachrichten
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="mb-1 small text-center"><?php echo $_SESSION['status']['welcome_message_count'];?></div>
                                        </div>
                                        <div class="progress mb-4">
                                            <div class="progress-bar" role="progressbar" style="width: <?php print_r(getProgressWidth($_SESSION['status']['welcome_message_count']));?>%" aria-valuemin="<?php print_r(getProgressMin($_SESSION['status']['welcome_message_count']));?>" aria-valuemax="<?php print_r(getProgressMax($_SESSION['status']['welcome_message_count']));?>"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="card border-left-info shadow h-100 py-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2 text-xs font-weight-bold text-info text-uppercase mb-1">
                                                Verschoben Clients
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="mb-1 small text-center"><?php echo $_SESSION['status']['client_moved_count'];?></div>
                                        </div>
                                        <div class="progress mb-4">
                                            <div class="progress-bar" role="progressbar" style="width: <?php print_r(getProgressWidth($_SESSION['status']['client_moved_count']));?>%" aria-valuemin="<?php print_r(getProgressMin($_SESSION['status']['client_moved_count']));?>" aria-valuemax="<?php print_r(getProgressMax($_SESSION['status']['client_moved_count']));?>"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Progress Small -->
                            <div class="col-xl-3 col-md-6">
                                <div class="card border-left-info shadow h-100 py-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2 text-xs font-weight-bold text-info text-uppercase mb-1">
                                                Live gegangene Clients
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="mb-1 small text-center"><?php echo $_SESSION['status']['twitch_live_count'];?></div>
                                        </div>
                                        <div class="progress mb-4">
                                            <div class="progress-bar" role="progressbar" style="width: <?php print_r(getProgressWidth($_SESSION['status']['twitch_live_count']));?>%" aria-valuemin="<?php print_r(getProgressMin($_SESSION['status']['twitch_live_count']));?>" aria-valuemax="<?php print_r(getProgressMax($_SESSION['status']['twitch_live_count']));?>"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <h4 class="mt-4">Instanzen</h4>
                        <hr>
                        <form class="form-horizontal" data-toggle="validator" name="changeConfig" method="POST">
<?php
$counter = 0;
foreach($_SESSION["instances"] as $key => $value){
 if($counter % 4 == 0 || $counter == 0){ ?>
                            <div class="row">
<?php }
$counter++;
if( filter_var($_SESSION["instances"][$key]["instance_activ"], FILTER_VALIDATE_BOOLEAN)){ ?>
                                <div class="col-xl-3 col-md-6">
                                    <div class="card bg-success text-white mb-4">
                                        <div class="card-body text-center"><?php echo $key; ?></div>
                                        <div class="card-footer d-flex align-items-center justify-content-between">
                                            <a class="small text-white stretched-link" href="core.php?id=<?php echo $key;?>">Wechsel zur Configübersicht</a>
                                            <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                                        </div>
                                    </div>
                                </div>
<?php } else {?>
                                <div class="col-xl-3 col-md-6">
                                    <div class="card bg-danger text-white mb-4">
                                        <div class="card-body text-center"><?php echo $key; ?></div>
                                        <div class="card-footer d-flex align-items-center justify-content-between">
                                            <a class="small text-white stretched-link" href="core.php?id=<?php echo $key;?>">Wechsel zur Configübersicht</a>
                                            <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                                        </div>
                                    </div>
                                </div>
<?php }
    if($counter % 4 == 0 || count($_SESSION["instances"]) == 1 || count($_SESSION["instances"]) == $counter){ ?>
                            </div>
<?php }
} ?>
                        </form>
                    </div>
                    <!-- End of Page Content -->
                </div>
                <!-- End of Main Content -->
<?php require_once('templates/footer.php'); ?>
            </div>
            <!-- End of Content Wrapper -->
        </div>
        <!-- End of Page Wrapper -->

        <script type="text/javascript">
        // Load google charts
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        // Draw the chart and set the chart values
        function drawChart() {
          var data = google.visualization.arrayToDataTable([
          ['Task', ''],
          <?php if( ! empty($_SESSION["instances"]) ){ ?>
          ['Online', <?php echo intval(countActiveInstances($_SESSION["instances"])); ?>],
          ['Offline', <?php echo intval(count($_SESSION["instances"]) - countActiveInstances($_SESSION["instances"])); ?>],
          <?php }else{ ?>
          ['Keine Instanzen', <?php echo intval("1");?>],
          <?php } ?>
        ]);

          // Optional; add a title and set the width and height of the chart
          var options = {
            chartArea:{
                left:10,
                top:10,
                bottom:10,
                width:"100%",
                height:"100%"
            },
            legend: 'none',
            is3D: true,
            colors: ['#1cdc00', '#ff3600']
          };

          // Display the chart inside the <div> element with id="piechart"
          var chart = new google.visualization.PieChart(document.getElementById('piechart'));
          chart.draw(data, options);
        }
        </script>
    </body>
</html>