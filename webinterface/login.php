<?php
    session_start();
    $_SESSION = array();
    include $_SERVER["DOCUMENT_ROOT"] . "/templates/db-connections.php";
    if( isset($_SESSION['user']['username']) ){
        header("Refresh:0; url=index.php");
        exit();
    }
    // TODO: Session not getting destroyed as it should :(
?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Login - TS3 Bot";
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/header.php');
    //endregion
?>
<body class="bg-gradient-primary">

    <div class="container">

        <!-- Outer Row -->
        <div class="row justify-content-center">

            <div class="col-xl-10 col-lg-12 col-md-9">

                <div class="card o-hidden border-0 shadow-lg my-5">
                    <div class="card-body p-0">
                        <!-- Nested Row within Card Body -->
                        <div class="row">

                            <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
                            <div class="col-lg-6">
                                <div class="p-5">
                                    <div class="text-center">
                                        <h1 class="h4 text-gray-900 mb-4">Welcome Back!</h1>
                                    </div>
                                    <form class="form-horizontal" data-toggle="validator" name="login" method="POST">
                                        <div class="form-group">
                                            <input name="username"
                                                <?php if( isset($_SESSION['login_failed']) && $_SESSION['login_failed']){echo "style=\"border-color: red;\"";}?>
                                                class="form-control form-control-user" id="inputUsername" type="username" placeholder="Enter username" />
                                        </div>
                                        <div class="form-group">
                                            <input name="password"
                                                <?php if( isset($_SESSION['login_failed']) && $_SESSION['login_failed']){echo "style=\"border-color: red;\"";}?>
                                                class="form-control form-control-user" id="inputPassword" type="password" placeholder="Password" >
                                        </div>
                                        <div class="form-group">
                                            <div class="custom-control custom-checkbox small">
                                                <input type="checkbox" class="custom-control-input" id="customCheck">
                                                <label class="custom-control-label" for="customCheck">Remember
                                                    Me</label>
                                            </div>
                                        </div>
                                        <button name="login" type="submit" class="btn btn-primary btn-user btn-block">Login</button>
                                        <hr>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>

</body>

</html>