<?php
    class MyDB extends SQLite3 {
       function __construct() {
          $this->open("/data/db.sqlite3");
       }
    }
    $db = new MyDB();
?>