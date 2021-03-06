<?php
    class MyDB extends SQLite3 {
        function __construct() {
            $this->open("/data/db.sqlite3");
            $this->enableExceptions();
            $this->busyTimeout(2000);
        }
    }
    $db = new MyDB();
    try{
        if( isset($_SESSION['userid']) ){
            $tablename = $_SESSION["instance_name"] . '_groups';
            $results = $db->query('SELECT * FROM "' . $tablename . '" ;');

            $_SESSION['db_groups'] = [];
            while ($row = $results->fetchArray()) {
                $_SESSION['db_groups'][$row["id"]] = $row["name"];
            }
            $tablename = $_SESSION["instance_name"] . '_channels';
            $results = $db->query('SELECT * FROM "' . $tablename . '" ;');

            $_SESSION['db_channels'] = [];
            while ($row = $results->fetchArray()) {
                $_SESSION['db_channels'][$row["id"]] = $row["name"];
            }
            $tablename = $_SESSION["instance_name"] . '_users';
            $results = $db->query('SELECT uid, name FROM "' . $tablename . '" ;');

            $_SESSION['db_users'] = [];
            while ($row = $results->fetchArray()) {
                $_SESSION['db_users'][$row["uid"]] = $row["name"];
            }
        }
    } catch (Throwable $e) {
        echo 'Exception abgefangen: ',  $e->getMessage(), "\n";
    } catch (Exception $e) {
        echo 'Exception abgefangen 2: ',  $e->getMessage(), "\n";
    }

    if (isset($_POST['login'])){
        $statement = $db->prepare('SELECT password FROM users where username = :username;');
        $statement->bindValue(':username', $_POST["username"]);
        $result = $statement->execute();
        $result = $result->fetchArray();
        if( ! empty($result) && password_verify($_POST["password"], $result[0])){
            $_SESSION['userid'] = $_POST["username"];
        }
    }
    $db->close();
?>