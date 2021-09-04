<?php
    $lang = "YOUR DEFAULT LANGUAGE";
    $allowedlangs = array(
        "AVAILABLE LANGUAGES 1",
        "AVAILABLE LANGUAGESS 2"
    );
    if (!empty($_GET["lang"])) {
        $rlang = $_GET["lang"];
    } else if (!empty($_COOKIE["lang"])) {
        $rlang = $_COOKIE["lang"];
    }
    if (isset($rlang) && !empty($rlang) && in_array($rlang, $allowedlangs)) {
        $lang = $rlang;
        setcookie("lang", $lang,time()+31536000);
    }
    $langmaps = array(
        "AVAILABLE LANGUAGES 1" => "FILE PATH TO LANGUAGE 1 FILE.php",
        "AVAILABLE LANGUAGES 2" => "FILE PATH TO LANGUAGE 1 FILE.php"
    );
?>