<?php
    require_once('templates/preload.php');

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Dashboard - TS3 Bot";
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/header.php');
    //endregion
?>
    <body class="sb-nav-fixed">
<?php include $_SERVER["DOCUMENT_ROOT"] . "/templates/nav-header.php";?>
        <div id="layoutSidenav">
<?php include $_SERVER["DOCUMENT_ROOT"] . "/templates/nav.php"; ?>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">Dashboard</h1>
                        <hr>
                        <div class="row">
                            <div class="col-xl-3 col-md-6">
                                <div class="card mb-4">
                                    <div class="card-header">
                                        <i class="fas fa-chart-pie mr-1"></i><?php if( ! empty($_SESSION["instances"]) ){ echo "Bots active"; }else{ echo "Keine Instanzen"; }?>
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
                </main>
<?php include $_SERVER["DOCUMENT_ROOT"] . "/templates/footer.php"; ?>
            </div>
        </div>
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