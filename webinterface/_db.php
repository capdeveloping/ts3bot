<?php
    class MyDB extends SQLite3 {
       function __construct() {
          $this->open("/data/db.sqlite3");
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
    } catch (Exception $e) {

    }
?>