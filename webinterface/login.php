<?php
    session_start();
    include $_SERVER["DOCUMENT_ROOT"] . "/templates/db-connections.php";

    if( isset($_SESSION['userid']) ){
        header("Refresh:0; url=index.php");
        exit();
    }

?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Login - TS3 Bot";
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/header.php');
    //endregion
?>
    <body class="bg-primary">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-5">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header"><h3 class="text-center font-weight-light my-4">Login</h3></div>
                                    <div class="card-body">
                                        <form class="form-horizontal" data-toggle="validator" name="login" method="POST">
                                            <div class="form-group">
                                                <label class="small mb-1" for="inputUsername">Username</label>
                                                <input name="username" <?php if( isset($_SESSION['login_failed']) && $_SESSION['login_failed']){echo "style=\"border-color: red;\"";}?> class="form-control py-4" id="inputUsername" type="username" placeholder="Enter username" />
                                            </div>
                                            <div class="form-group">
                                                <label class="small mb-1" for="inputPassword">Password</label>
                                                <input name="password" <?php if( isset($_SESSION['login_failed']) && $_SESSION['login_failed']){echo "style=\"border-color: red;\"";}?> class="form-control py-4" id="inputPassword" type="password" placeholder="Enter password" />
                                            </div>
                                            <div class="form-group d-flex align-items-center justify-content-between mt-4 mb-0">
<?php if( isset($_SESSION['login_failed']) && $_SESSION['login_failed']){?>
                                                <label class="small mb-1" style="color: #ef0000; font-weight: bold;">Wrong username or password!</label>
<?php } else { ?>
                                                <a class="small" href="#"></a>
<?php }?>
                                                <button name="login" type="submit" class="btn btn-primary">Login</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
            <div id="layoutAuthentication_footer">
<?php require_once('_footer.php'); ?>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </body>
</html>