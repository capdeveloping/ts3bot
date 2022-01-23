<?php
    require_once('templates/preload.php');

    $saved = FALSE;

    if(isset($_POST['updateUser'])) { //&& $_SESSION['user']['isadmin'] === true){

        print_r("isadmin\n");
        /*$statement = $db->prepare('UPDATE users SET isadmin = :isadmin WHERE username = :username;');
        $statement->bindValue(':instances', $_POST['isadmin']);
        $statement->bindValue(':username', $_POST['lblusername']);
        $result = $statement->execute();*/
        $saved = TRUE;

        if( ! empty($_POST['instances'])){
            print_r("instances");
            /*$statement = $db->prepare('UPDATE users SET instances = :instances WHERE username = :username;');
            $statement->bindValue(':instances', $_POST['instances']);
            $statement->bindValue(':username', $_POST['lblusername']);
            $result = $statement->execute();*/
            $saved = TRUE;
        }

        if( ! empty($_POST['newPw']) ){
            print_r("newPw");
            /*$password = password_hash($_POST["newPw"], PASSWORD_BCRYPT, ['cost' => 12]);
            $statement = $db->prepare('UPDATE users SET password = :password WHERE username = :username;');
            $statement->bindValue(':password', $password);
            $statement->bindValue(':username', $_POST['lblusername']);
            $result = $statement->execute();*/
            $saved = TRUE;
        }
    }
?>
<!DOCTYPE html>
<?php
    // region import header
    $website_title = "Modify Users";
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
                        <h1 class="mt-4">Sorry this feature is under construction. :( </h1>
<!--
<?php
    if( ! $_SESSION['user']['isadmin'] ){
        header("HTTP/1.1 403 Unauthorized");
?>
                        <h1 class="mt-4">Forbbiden! You are not an admin!</h1>
<?php
    }
    else {
?>
// TODO: if $_GET['username'] exists!!!!
                        <h1 class="mt-4">Modify Users</h1>
<?php
        if( ! isset($_GET['username']) ){
?>
                        <form class="form-horizontal" data-toggle="validator" name="updateUser" method="POST">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <i class="fas fa-chart-area mr-1"></i>
                                    Welcher Benutzer soll bearbeitet werden?
                                </div>
                                <div class="card-body">
                                    <div class="form-group row">
                                        <div class="col-sm-2"></div>
                                        <div class="col-sm-2 input-group">
                                            <div class="input-group-prepend">
                                                <div name="instances" id="single-select"></div>
                                            </div>
                                        </div>
                                        <div class="col-sm-2"></div>
                                    </div>
                                </div>
                            </div>
                        </form>
<?php
        }
        else {
?>
                        <form class="form-horizontal" data-toggle="validator" name="updateUser" method="POST">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <i class="fas fa-chart-area mr-1"></i>
                                    Einstellungen
                                </div>
                                <div class="card-body">
                                    <br>
                                    <div class="form-group row">
                                        <label name="username" class="col-sm-2 control-label" for="muser">User: <b><?php print_r($_GET['username']) ?></b></label>
                                        <input type="hidden" name="lblusername" value="<?php print_r($_GET['username']) ?>">
                                    </div>
                                    <div class="form-group row">
                                        <div class="col-sm-2"></div>
                                        <label class="col-sm-2 control-label" for="isadmin">Ist es ein Admin?</label>
                                        <div class="col-sm-2">
                                            <select name="isadmin" class="form-select" aria-label="select">
<?php       if ( $_SESSION['user']["isadmin"] === "true") { ?>
                                                <option selected value="true">true</option>
                                                <option value="false">false</option>
<?php       } else if ( $_SESSION['user']["isadmin"] === "false"){?>
                                                <option value="true">true</option>
                                                <option selected value="false">false</option>
<?php       } else { ?>
                                                <option value="true">true</option>
                                                <option value="false">false</option>
<?php       } ?>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <div class="col-sm-2"></div>
                                        <label class="col-sm-2 control-label" for="instances" >Berechtigte Instanzen</label>
                                        <div class="col-sm-2 input-group">
                                            <div class="input-group-prepend">
                                                <button class="btn btn-outline-secondary" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">?</button>
                                                <div name="instances" id="multiple-select"></div>
                                            </div>
                                            <div class="collapse" id="collapseExample">
                                                <div class="card">
                                                    Wenn Admin true ist werden automatisch alle Instanzen berechtigt!
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-2">
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <div class="col-sm-2"></div>
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
                                        <button type="submit" class="btn btn-primary" name="updateUser"><i class="fas fa-save"></i>&nbsp;speichern</button>
                                    </div>
                                </div>
                                <div class="row">&nbsp;</div>
                            </div>
                        </form>
<?php   }?>
<?php }?>
-->
                    </div>
                    <!-- End of Page Content -->
<?php require_once('templates/footer.php'); ?>
                </div>
                <!-- End of Main Content -->
            </div>
            <!-- End of Content Wrapper -->
        </div>
<?php
    if( ! $_SESSION['user']['isadmin'] ){
?>
        <script>
            function getSelected() {
                var optionsData = [<?php echo getJSSelectOption($_SESSION['instances'], $_SESSION["user"]["instances"]);?>];
                return optionsData;
            }

            VirtualSelect.init({
                ele: '#multiple-select',
                options: getInstances(),
                multiple: true,
                selectedValue: getSelected(),
                placeholder: '-- Instanzen auswählen --',
            });

            VirtualSelect.init({
                ele: '#single-select',
                options: getInstances(),
                multiple: true,
                selectedValue: "",
                placeholder: '-- User auswählen --',
            });
        </script>
<?php } ?>
    </body>
</html>