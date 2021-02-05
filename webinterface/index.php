<?php
    require_once('_preload.php');
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Dashboard - TS3 Bot</title>
        <link href="css/styles.css" rel="stylesheet" />
        <link href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css" rel="stylesheet" crossorigin="anonymous" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/js/all.min.js" crossorigin="anonymous"></script>
        <script src="assets/chart-pie-bots-active.js"></script>
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
                        <h1 class="mt-4">Dashboard</h1>
                        <hr>
                        <div class="row">
                            <div class="col-xl-3 col-md-6">
                                <div class="card mb-4">
                                    <div class="card-header">
                                        <i class="fas fa-chart-pie mr-1"></i><?php if( ! empty($instances) ){ echo "Bots active"; }else{ echo "Keine Instanzen"; }?>
                                    </div>
                                    <div id="piechart"></div>
                                </div>
                            </div>
                        </div>
                        <h4 class="mt-4">Instanzen</h4>
                        <hr>
                        <form class="form-horizontal" data-toggle="validator" name="changeConfig" method="POST">
<?php
$counter = 0;
foreach($instances as $key => $value){
 if($counter % 4 == 0 || $counter == 0){ ?>
                            <div class="row">
<?php }
$counter++;
if( filter_var($instances[$key]["instance_activ"], FILTER_VALIDATE_BOOLEAN)){ ?>
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
    if($counter % 4 == 0 || count($instances) == 1 || count($instances) == $counter){ ?>
                            </div>
<?php }
} ?>
                        </form>
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
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    </body>
</html>

<script type="text/javascript">
// Load google charts
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

// Draw the chart and set the chart values
function drawChart() {
  var data = google.visualization.arrayToDataTable([
  ['Task', ''],
  <?php if( ! empty($instances) ){ ?>
  ['Online', <?php echo intval(countActiveInstances($instances)); ?>],
  ['Offline', <?php echo intval(count($instances) - countActiveInstances($instances)); ?>],
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