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
        if( isset($_SESSION['user']['username']) && ! isset($_POST['login']) ){
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
            $results = $db->query('SELECT uid, name, online FROM "' . $tablename . '" ;');

            $_SESSION['db_users'] = [];
            $counter=1;
            while ($row = $results->fetchArray()) {
                $_SESSION['db_users'][$counter]["name"] = $row["name"];
                $_SESSION['db_users'][$counter]["uid"] = $row["uid"];
                $_SESSION['db_users'][$counter]["online"] = $row["online"];
                $counter++;
            }
        }
    } catch (Throwable $e) {
        echo 'Exception abgefangen: ',  $e->getMessage(), "\n";
    } catch (Exception $e) {
        echo 'Exception abgefangen 2: ',  $e->getMessage(), "\n";
    }

    try{
        if( isset($_SESSION['user']['username']) && ! isset($_POST['login']) ){
            $tablename = 'status';
            $results = $db->query('SELECT * FROM "' . $tablename . '" ;');
            $_SESSION['status'] = [];
            while ($row = $results->fetchArray()) {
                $_SESSION['status']["channel_create_count"] = $row["channel_create_count"];
                $_SESSION['status']["channel_delete_count"] = $row["channel_delete_count"];
                $_SESSION['status']["client_moved_count"] = $row["client_moved_count"];
                $_SESSION['status']["welcome_message_count"] = $row["welcome_message_count"];
                $_SESSION['status']["twitch_live_count"] = $row["twitch_live_count"];
            }
        }
    } catch (Throwable $e) {
        echo 'Exception abgefangen: ',  $e->getMessage(), "\n";
    } catch (Exception $e) {
        echo 'Exception abgefangen 2: ',  $e->getMessage(), "\n";
    }

    if (isset($_POST['login'])){
        $statement = $db->prepare('SELECT * FROM users where username = :username;');
        $statement->bindValue(':username', $_POST["username"]);
        $result = $statement->execute();
        $result = $result->fetchArray();
        if( ! empty($result['password']) && password_verify($_POST["password"], $result['password'])){
            $_SESSION['user']['username'] = $_POST["username"];
            $_SESSION['user']['isadmin'] = $result['isadmin'];
            $_SESSION['user']['instances'] = $result['instances'];
            unset($_SESSION['login_failed']);
        }else{
            $_SESSION['login_failed'] = TRUE;
        }
    }

    $db->close();
?>