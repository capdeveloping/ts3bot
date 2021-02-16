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
        <meta name="author" content="capdeveloping" />
        <title>Informationen - TS3 Bot</title>
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
                    require_once('_nav.php');
                ?>
            </div>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">Bot Informationen</h1>
                        <br>
                        <div class="mb-4">
                            <div class="row">
                                <div class="col-lg-12">
                                    <h4><strong><span class="text-info">Was macht der ts3bot?</span></strong></h4>
                                    <p>Der Ts3Bot kann vieles automatisieren auf dem Ts3 Server. Angefangen bei automatischen Channel erstellen, sobald einer belegt ist. Bis hinzu einem Ts3 Viewer erstellen. Eine HTML Datei mit allen Informationen. Aber noch vieles mehr.</p>
                                    <br>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-12">
                                    <h4><strong><span class="text-success">Wer hat dem ts3bot erstellt?</span></strong></h4>
                                    <p>Der ts3bot wurde von <strong>capdeveloping</strong> erstellt. Copyright &copy; 2015-2021 angetrieben von <a href="//evil-lions.de/" target="_blank" rel="noopener noreferrer">evil-lions.de</a></p>
                                    <br>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-12">
                                    <h4><strong><span class="text-warning">Wann wurde der ts3bot erstellt?</span></strong></h4>
                                    <p>Aller erste Version: im Jahre 2015, genaue Datum ist leider unbekannt.</p>
                                    <p>Erste öffentliche Version: Juli 2017.</p>
                                    <p>Stable seit: Anfang 2018.</p>
                                    <p>Die neuste Version kannst du auf <a href="//github.com/capdeveloping/ts3bot" target="_blank">GitHub</a> und <a href="//hub.docker.com/repository/docker/capdeveloping/ts3bot" target="_blank">Docker</a> finden.</p>
                                    <br>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-12">
                                    <h4><strong><span class="text-danger">Wie wurde der ts3bot erstellt?</span></strong></h4>
                                    <p>Der ts3bot basiert auf</p>
                                    <p><a href="//php.net/" target="_blank" rel="noopener noreferrer">PHP</a> - Copyright &copy; 2001-2019 the <a href="//secure.php.net/credits.php" target="_blank" rel="noopener noreferrer">PHP Group</a></p>
                                    <p><a href="//openjdk.java.net/" target="_blank" rel="noopener noreferrer">openjdk</a> - Copyright &copy; 2021 the <a href="//openjdk.java.net" target="_blank" rel="noopener noreferrer">openjdk java</a></p><br>
                                    <p>Es nutzt weiterhin die folgenden Programmbibliotheken:</p>
                                    <p><a href="//fontawesome.com" target="_blank" rel="noopener noreferrer">Font Awesome 5.15.1</a> - Copyright &copy; Fonticons, Inc.</p>
                                    <p><a href="//startbootstrap.com" target="_blank" rel="noopener noreferrer">SB Admin Bootstrap Admin Template</a> - Copyright &copy; 2013-2016 Blackrock Digital LLC.</p>
                                    <br>
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
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    </body>
</html>