<?php
    require_once('templates/preload.php');

    $saved = FALSE;
    $passwordMissMatch = FALSE;
    $wrongPassword = FALSE;

    if(isset($_POST['updatePW'])){
        $db = new MyDB();

        $statement = $db->prepare('SELECT password FROM users WHERE username = :username;');
        $statement->bindValue(':username', $_SESSION['user']['username']);
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
            $statement = $db->prepare('UPDATE users SET password = :password WHERE username = :username;');
            $statement->bindValue(':password', $password);
            $statement->bindValue(':username', $_SESSION['user']['username']);
            $result = $statement->execute();
            $saved = TRUE;
        }

        $db->close();
    }
?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Change password";
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
                        <h1 class="mt-4">Change Password</h1>
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
                    <!-- End of Page Content -->
<?php require_once('templates/footer.php'); ?>
                </div>
                <!-- End of Main Content -->
            </div>
            <!-- End of Content Wrapper -->
        </div>
    </body>
</html>