<?php
	// Sets the proper content type for javascript
	header('Content-Type: text/javascript; charset=UTF-8');
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/preload.php');
?>
function getUsers() {
    var optionsData = [];

<?php foreach ($_SESSION['db_users'] as $key=>$value){
    $jsValue = '"' . $value["uid"] . '"';
    $online = "";
    if($value["online"]){
        $online = '<img width="15" height="15" src="/assets//img/online.gif"> ';
    }
    $jsLabel = $online . str_replace("'", "", $value["name"]) . " - (" . $value["uid"] . ")";
?>
    optionsData.push({ value: <?php echo $jsValue?>, label: '<?php echo $jsLabel;?>'});
<?php }?>
    return optionsData;
}

function getWebUsers() {
    var optionsData = [];

<?php foreach ($_SESSION['db_users'] as $key=>$value){
    $jsValue = '"' . $value["uid"] . '"';
    $online = "";
    if($value["online"]){
        $online = '<img width="15" height="15" src="/assets//img/online.gif"> ';
    }
    $jsLabel = $online . str_replace("'", "", $value["name"]) . " - (" . $value["uid"] . ")";
?>
    optionsData.push({ value: <?php echo $jsValue?>, label: '<?php echo $jsLabel;?>'});
<?php }?>
    return optionsData;
}

function getGroups() {
    var optionsData = [];

<?php foreach ($_SESSION['db_groups'] as $id=>$name){?>
    optionsData.push({ value: "<?php echo $id?>", label: "<?php print_r("(" . $id . ") " . $name)?>"});
<?php }?>
    return optionsData;
}

function getChannels() {
    var optionsData = [];

<?php foreach ($_SESSION['db_channels'] as $id=>$name){?>
    optionsData.push({ value: "<?php echo $id;?>", label: "<?php print_r("(" . $id . ") " . $name);?>"});
<?php }?>
    return optionsData;
}

function getInstances() {
    var optionsData = [];

<?php foreach ($_SESSION['instances'] as $key=>$name){?>
    optionsData.push({ value: "<?php echo $key;?>", label: "<?php print_r($key);?>"});
<?php }?>
    return optionsData;
}