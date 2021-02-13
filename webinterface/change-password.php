<?php
    require_once('_preload.php');
    $saved = FALSE;
    $passwordMissMatch = FALSE;
    $wrongPassword = FALSE;
    include "_db.php";

    if(isset($_POST['updatePW'])){
        $statement = $db->prepare('SELECT password FROM users WHERE username = :username;');
        $statement->bindValue(':username', $_SESSION['userid']);
        $result = $statement->execute();
        $result = $result->fetchArray();
        if( empty($result) || ! password_verify($_POST["oldPw"], $result[0])){
            $wrongPassword = TRUE;
        }
        if( $_POST["newPw"] !== $_POST["newPwRepeated"]){
            $passwordMissMatch = TRUE;
        }

        if( ! $passwordMissMatch && ! $wrongPassword){
            $password = password_hash($_POST["newPw"], PASSWORD_BCRYPT, ['cost' => 12]);
            $statement = $db->prepare('UPDATE users SET password = :password WHERE username = "admin";');
            $statement->bindValue(':password', $password);
            $result = $statement->execute();
            $saved = TRUE;
        }
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
        <title>Instanzen verwalten</title>
        <link href="css/styles.css" rel="stylesheet" />
        <link href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css" rel="stylesheet" crossorigin="anonymous" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/js/all.min.js" crossorigin="anonymous"></script>
    </head>
    <body class="sb-nav-fixed">
<?php require_once('_nav-header.php'); ?>
        <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
<?php require_once('_nav.php'); ?>
            </div>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">Core</h1>
                        <form class="form-horizontal" data-toggle="validator" name="updatePW" method="POST">
                            <div class="card mb-4">
                                <br>
                                <div class="form-group row">
                                    <div class="col-sm-3"></div>
                                    <label class="col-sm-2 control-label" for="oldPw" >Altes Passwort</label>
                                    <div class="col-sm-2">
                                        <div class="row">
                                            <input class="form-control" <?php if( $wrongPassword){ echo 'style="border-color: #ff0000 ;"';}?> id="oldPw" type="password" name="oldPw" />
                                        </div>
                                        <div class="row">
                                            <div class=<?php if( $wrongPassword){ echo '"collapse show"';}else{echo "collapse";}?> id="collapse">
                                                <div>Password ist falsch!</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-sm-3"></div>
                                    <label class="col-sm-2 control-label" for="newPw" >Neues Passwort</label>
                                    <div class="col-sm-2">
                                        <div class="row">
                                            <input class="form-control" <?php if( $passwordMissMatch){ echo 'style="border-color: #ff0000 ;"';}?> id="newPw" type="password" name="newPw" />
                                        </div>
                                        <div class="row">
                                            <div class=<?php if( $passwordMissMatch){ echo '"collapse show"';}else{echo "collapse";}?> id="collapse">
                                                <div>Passwörter stimmen nicht überein!</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-sm-3"></div>
                                    <label class="col-sm-2 control-label" for="newPwRepeated">Neues Passwort wiederholen</label>
                                    <div class="col-sm-2 input-group">
                                        <input class="form-control" <?php if( $passwordMissMatch){ echo 'style="border-color: #ff0000 ;"';}?> id="newPwRepeated" type="password" name="newPwRepeated" />
                                    </div>
                                </div>
                                <br>
                                <div class="col-md-3"></div>
                                <?php if($saved) { ?>
                                <div id="savedDiv" class="row saved-row">
                                    <label class="saved-label">Password gespeichert!</label>
                                </div>
                                <?php }?>
                                <div class="row">&nbsp;</div>
                                <div class="row" style="display: block;">
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary" name="updatePW"><i class="fas fa-save"></i>&nbsp;speichern</button>
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                            </div>
                        </form>
                    </div>
                </main>
                <footer class="py-4 bg-light mt-auto">
<?php require_once('_footer.php'); ?>
                </footer>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="js/scripts.js"></script>
    </body>
</html>