<?php
#region Instance
function loadCurrentInstance($document_root){
    if(file_exists( $document_root . "/.current")) {
        $instance_name = file_get_contents( $document_root . "/.current", "r") or die("Unable to open(read) '.current' file!");
        return $instance_name;
    }
    return NULL;
}

function loadInstanceConfig($instance_config_path, $instance_name){
    if(file_exists($instance_config_path)) {
        $myfile = fopen($instance_config_path, "r") or die("Unable to open instancemanager.cfg file!");
        // Output one line until end-of-file
        $mapping = [];
        while(!feof($myfile)) {
            $line = fgets($myfile);
            if ( str_starts_with( $line, "#") || str_starts_with( $line, "-") ) {
                continue;
            }
            $pos = strpos($line, "=") + 1 ;
            $key = trim(substr($line, 0, $pos - 1 ));
            if(empty($key)){
                continue;
            }
            $number = substr($key , 0, 1 );
            if(str_contains($key, "instance_name")){
                $mapping[$number] = trim(substr($line, $pos));
                $instances[$mapping[$number]] = [];
                continue;
            }
            $instances[$mapping[$number]][trim(substr($key, 2))] = trim(substr($line, $pos));
        }
        fclose($myfile);
        if( empty($instance_name) ){
            $instance_name = $mapping[1];
        }
        return array($instances, $instance_name);
    }
    return NULL;
}

function countActiveInstances($instances){
    $active = 0;
    foreach($instances as $key => $value){
        if($instances[$key]["instance_activ"] === "true"){
            $active++;
        }
    }
    return $active;
}

function removeCurrentInstance($document_root){
    exec("rm " . $document_root . "/.current", $resultexec);
}

function saveCurrentInstance($POST, $instances, $document_root){
    foreach($instances as $key => $value){
        if (isset($POST[$key])){
            $currentFile = fopen( $document_root . "/.current", "w+") or die("Unable to open file!");
            fwrite($currentFile, array_key_first ( $POST ));
            fclose($currentFile);
            return TRUE;
        }
    }
    return FALSE;
}

function saveInstanceConfigFile($instances, $instance_config_path){
    try{
        $instanceFile = fopen($instance_config_path, "w+") or die("Unable to open instancemanager.cfg file! => " . $instance_config_path);
        $counter = 1;
        foreach($instances as $key => $value){
            fwrite($instanceFile, $counter . ".instance_name = " . $key . "\n");
            fwrite($instanceFile, $counter . ".instance_config_pfad = " . $instances[$key]["instance_config_pfad"] . "\n");
            fwrite($instanceFile, $counter . ".instance_activ = " . $instances[$key]["instance_activ"] . "\n");
            $counter++;
        }
        fclose($instanceFile);
    } catch(Throwable $ex) { }
}

function renameInstance($instances, $newkey, $oldkey){
    $instances_new = [];
    foreach($instances as $key=>$value) {
        if($key === $oldkey){
            $instances_new[$newkey] = $instances[$oldkey];
            $instances_new[$newkey]["instance_config_pfad"] = str_replace($oldkey, $newkey, $instances_new[$newkey]["instance_config_pfad"]);
        }else{
            $instances_new[$key] = $instances[$key];
        }
    }
    return $instances_new;
}

#endregion

#region serverconfig

function loadServerconfig($configPath){
    $config = [];
    $functions = [];
    try{
        $myfile = fopen($configPath, "r") or die("Unable to open serverconfig file!" . $configPath);
        // Output one line until end-of-file
        while(!feof($myfile)) {
            $line = fgets($myfile);
            if (substr( $line, 0, 1 ) === "#" || substr( $line, 0, 1 ) === "-" || $line === NULL ) {
                continue;
            }
            $pos = strpos($line, "=") + 1 ;
            $key = trim(substr($line, 0, $pos - 1 ));
            $config[$key] = trim(substr($line, $pos));
        }
        $tempArry = explode(",", $config["functions"]);
        foreach ($tempArry as $key => $value) {
            if(empty($value)){
                continue;
            }
            $tmp = explode(":", $value);
            switch ($tmp[0]) {
                case "WelcomeMessage":
                    $functions = functionArrayPush($functions, "WelcomeMessage", $tmp[1]);
                    break;
                case "ClientMove":
                    $functions = functionArrayPush($functions, "ClientMove", $tmp[1]);
                    break;
                case "ClientAFK":
                    $functions = functionArrayPush($functions, "ClientAFK", $tmp[1]);
                    break;
                default:
                    $functions[$tmp[0]] = $tmp[1];
            }
        }
        fclose($myfile);
    } catch(Throwable $ex) { }
    return array($config, $functions);
}

function fileReadContentWithSeparator($configPath, $separator){
    $dictionary = [];
    try{
        $configFile = fopen($configPath, "r") or die("Unable to open file! => " . $configPath);
        // Output one line until end-of-file
        while(!feof($configFile)) {
            $line = fgets($configFile);
            if(empty($line)){
                continue;
            }
            $line = explode($separator, $line);
            $dictionary[trim($line[0])] = trim($line[1]);
        }
        fclose($configFile);
    } catch(Throwable $ex) { }
    return $dictionary;
}

function fileReadContentWithMultipleSeparators($configPath, $separator){
    $dictionary = [];
    $counter = 1;
    try{
        $configFile = fopen($configPath, "r") or die("Unable to open file! => " . $configPath);
        // Output one line until end-of-file
        while(!feof($configFile)) {
            $line = fgets($configFile);
            if(empty($line)){
                continue;
            }
            $line = explode($separator, $line);
            $dictionary[$counter]["twitchname"] = trim($line[0]);
            $dictionary[$counter]["uid"] = trim($line[1]);
            $dictionary[$counter]["sendMessage"] = trim($line[2]);
            $counter++;
        }
        fclose($configFile);
    } catch(Throwable $ex) { }
    return $dictionary;
}

function fileReadContentWithoutSeparator($configPath){
    $dictionary = [];
    try{
        $configFile = fopen($configPath, "r") or die("Unable to open serverconfig file! => " . $configPath);
        // Output one line until end-of-file
        while(!feof($configFile)) {
            $line = trim(fgets($configFile));
            if(empty($line)){
                continue;
            }
            $dictionary[$line] = "";
        }
        fclose($configFile);
    } catch(Throwable $ex) { }
    return $dictionary;
}

function write_content_block($content, $configPath){
    try{
        $configFile = fopen($configPath, "w+") or die("Unable to open file! => " . $configPath);
        fwrite($configFile, $content . "\n");
        fclose($configFile);
    } catch(Throwable $ex) { }
}

function remove_item_and_write_file($dictionary, $item, $configFile){
    $removed = FALSE;
    foreach($_POST as $key => $value){
        if( str_starts_with( $key, "rmItem") ){
            $number = str_replace("rmItem", "", $key);
            unset($dictionary[ $_POST["item" . $number] ] );
            $removed = TRUE;
        }
    }
    if($removed){
        write_content_block($dictionary, $configFile);
    }
}

function writeConfigFileWithSeparator($content, $configPath, $separator){
    try{
        $configFile = fopen($configPath, "w+") or die("Unable to open file! => " . $configPath);
        foreach($content as $key => $value){
            if (empty($key)){
                continue;
            } else {
                fwrite($configFile, $key . $separator . $value . "\n");
            }
        }
        fclose($configFile);
    } catch(Throwable $ex) { }
}

function writeConfigFileWithMultipleSeparators($content, $configPath, $separator){
    try{
        $configFile = fopen($configPath, "w+") or die("Unable to open file! => " . $configPath);
        foreach($content as $key => $value){
            if (empty($key)){
                continue;
            } else {
                fwrite($configFile, $value["twitchname"] . $separator . $value["uid"] . $separator . $value["sendMessage"] . "\n");
            }
        }
        fclose($configFile);
    } catch(Throwable $ex) { }
}

function write_config_file($content, $configPath){
    try{
        $configFile = fopen($configPath, "w+") or die("Unable to open file! => " . $configPath);
        foreach($content as $key => $value){
            if (empty($key)){
                continue;
            } else {
                fwrite($configFile, $key . "\n");
            }
        }
        fclose($configFile);
    } catch(Throwable $ex) { }
}

function saveConfig($config, $configPath, $separator = " = "){
    try{
        $configFile = fopen($configPath, "w+") or die("Unable to write file! => " . $configPath);
        foreach($config as $key => $value){
            if (empty($key)){
                continue;
            } else {
                fwrite($configFile, $key . $separator . $value . "\n");
            }
        }
        fclose($configFile);
    } catch(Throwable $ex) { }
}

#endregion

#region configure functions

function functionArrayPush($functions, $functionName, $key){
    if(isset($functions[$functionName])){
        array_push($functions[$functionName], $key);
    }else{
        $functions[$functionName] = array($key);
    }
    return $functions;
}

function removeFunction($config, $functions, $funcName){
    $funcStr = "";
    foreach($config as $key=>$value) {
        if( str_starts_with( $key, $functions[$funcName] . "_" . strtolower ( $funcName ) ) ){
            unset($config[$key]);
        }
        if( str_contains("file_path", $key) || str_contains("file_location", $key) || str_contains("config_name", $key) ){
            unlink($config[$key]);
        }
    }
    unset($functions[$funcName]);
    return array($config, $functions);
}

function removeCustomFunction($config, $functions, $funcName, $funcConfigName){
    $funcStr = "";
    foreach($config as $key=>$value) {
        if( str_starts_with( $key, $functions[$funcName] . "_" . strtolower( $funcConfigName ) ) ){
            unset($config[$key]);
        }
    }
    unset($functions[$funcName]);
    return array($config, $functions);
}

function removeSpecialFunction($config, $functions, $funcName, $customFuncName, $funcKey){
    $funcStr = "";
    foreach($config as $key=>$value) {
        if( str_starts_with( $key, $funcKey . "_" . strtolower( $customFuncName ) ) ){
            unset($config[$key]);
        }
    }

    foreach($functions[$funcName] as $number => $value){
        if($value === $funcKey){
            \array_splice($functions[$funcName], $number, 1);
            break;
        }
    }

    return array($config, $functions);
}

function updateFunctionString($functions){
    $str = "";
    foreach ($functions as $key=>$value) {
        if( empty($key) ){
            continue;
        }
        if( ! empty( $str) ){
            $str .= ",";
        }
        switch ($key) {
            case "WelcomeMessage":
            case "ClientMove":
            case "ClientAFK":
                foreach($value as $number=>$value2){
                    if( $number != 0){
                        $str .= "," . $key . ":" . $value2;
                    } else {
                        $str .= $key . ":" . $value2;
                    }
                }
                break;
            default:
                $str .= $key . ":" . $value;
        }
    }
    return $str;
}

## region add functions

function addClientAfk($config, $functions, $key){
    if( isset($functions["ClientAFK"]) ){
        array_push($functions["ClientAFK"], $key);
    }else{
        $functions["ClientAFK"] = array($key);
    }

    $config[$key . "_client_afk_time"] = "";
    $config[$key . "_client_afk_channel"] = "";
    $config[$key . "_client_afk_channel_io"] = "";
    $config[$key . "_client_afk_channel_watch"] = "ignore";
    $config[$key . "_client_afk_group_ids"] = "";
    $config[$key . "_client_afk_group_watch"] = "ignore";

    return array($config, $functions);
}

function addClientMove($config, $functions, $key){
    if( isset($functions["ClientMove"]) ){
        array_push($functions["ClientMove"], $key);
    }else{
        $functions["ClientMove"] = array($key);
    }

    $config[$key . "_client_moved_channel"] = "";
    $config[$key . "_client_moved_group_notify"] = "false";
    $config[$key . "_client_moved_group_ids"] = "";
    $config[$key . "_client_moved_group_action"] = "ignore";

    return array($config, $functions);
}

function addWelcomeMessage($config, $functions, $key, $configPath){
    if( isset($functions["WelcomeMessage"]) ){
        array_push($functions["WelcomeMessage"], $key);
    }else{
        $functions["WelcomeMessage"] = array($key);
    }

    $config[$key . "_welcome_message"] = "";
    $config[$key . "_welcome_poke_client"] = "false";
    $config[$key . "_welcome_poke_message"] = "";
    $config[$key . "_welcome_date"] = "";
    $config[$key . "_welcome_repeat"] = "daily";
    $config[$key . "_welcome_group_ids"] = "";

    return array($config, $functions);
}

function addTwitch($config, $functions, $key, $configFolderPath){
    $functions["Twitch"] = $key;
    $config[$key . "_twitch_api_client_id"] = "";
    $config[$key . "_twitch_api_client_oauth_token"] = "";
    $config[$key . "_twitch_server_group"] = "";
    $config[$key . "_twitch_config_name"] = $configFolderPath . 'twitch_channel_name_to_uid.cfg';
    file_put_contents($config[$key . "_twitch_config_name"], "");
    return array($config, $functions);
}

function addTs3Viewer($config, $functions, $key, $configFolderPath){
    $functions["Viewer"] = $key;
    $config["functions"] .= ",Viewer:" . $key;
    $config[$key . "_ts3_viewer_update_time"] = "";
    $config[$key . "_ts3_viewer_file_location"] = $configFolderPath . 'ts3viewer.html';
    $config[$key . "_ts3_viewer_text_color"] = "";
    $config[$key . "_ts3_viewer_background_color"] = "";
    $config[$key . "_ts3_viewer_server_ip"] = "";
    file_put_contents($config[$key . "_ts3_viewer_file_location"], "");
    return array($config, $functions);
}

function addChannelAutoCreate($config, $functions, $key, $configFolderPath){
    $functions["ChannelAutoCreate"] = $key;
    $config["functions"] .= ",ChannelAutoCreate:" . $key;
    $config[$key . "_channel_check_subchannel"] = "";
    $config[$key . "_channel_check_password_file_path"] = $configFolderPath . 'channel_passwords.cfg';
    file_put_contents($config[$key . "_channel_check_password_file_path"], "");
    return array($config, $functions);
}

function addAutoRemove($config, $functions, $key){
    $functions["AutoRemove"] = $key;
    $config["functions"] .= ",AutoRemove:" . $key;
    $config[$key . "_auto_remove_group_ids"] = "";
    return array($config, $functions);
}

function addAcceptRules($config, $functions, $key, $configFolderPath){
    $functions["AcceptRules"] = $key;
    $config["functions"] .= ",AcceptRules:" . $key;
    $config[$key . "_accept_rules_first_group"] = "";
    $config[$key . "_accept_rules_accepted_group"] = "";
    $config[$key . "_accept_rules_poke_message"] = "";
    $config[$key . "_accept_rules_message_file_path"] = $configFolderPath . 'accept_rules_private_message.cfg';
    $config[$key . "_accept_rules_name_seperators"] = "-,/,|";
    $config[$key . "_accept_rules_forbidden_file_path"] = $configFolderPath . 'forbidden_names.cfg';
    file_put_contents($config[$key . "_accept_rules_message_file_path"], "");
    file_put_contents($config[$key . "_accept_rules_forbidden_file_path"], "");
    return array($config, $functions);
}

function addVersionChecker($config, $functions, $key){
    $functions["VersionChecker"] = $key;
    $config["functions"] .= ",VersionChecker:" . $key;
    $config[$key . "_version_check_time"] = "";
    return array($config, $functions);
}

##endregion

#endregion

function generateRandomString($length = 10) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyz';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}

function startScript($action){
    $output = "";
    exec("/bin/bash /control-bot.sh " . escapeshellarg($action) . " 2>&1", $resultexec);
    foreach($resultexec as $line) {
        $output .= $line.'<br>';
    }
    $err_msg = '<br><br>Result of ts3bot.jar:<br><pre>'.$output.'</pre>';
    $err_lvl = 1;
    usleep(80000);
}

function startScriptWithParams($action, $param1, $param2 = NULL){
    $output = "";
    if($param2 === NULL){
        exec("/bin/bash /control-bot.sh " . escapeshellarg($action) . " " . escapeshellarg($param1) . " 2>&1", $resultexec);
    }else{
        exec("/bin/bash /control-bot.sh " . escapeshellarg($action) . " " . escapeshellarg($param1) . " " . escapeshellarg($param2) . " 2>&1", $resultexec);
    }
    foreach($resultexec as $line) {
        $output .= $line.'<br>';
    }
    print_r($output);
}

function getstartlog() {
	$lines=array();
	if( file_exists("/data/logs/start.log") ) {
		$fp = fopen("/data/logs/start.log", "r");
		$buffer=array();
		while($line = fgets($fp, 4096)) {
			array_push($buffer, $line);
		}
		fclose($fp);
		foreach($buffer as $line) {
            array_push($lines, $line);
		}
	}
	return $lines;
}

function getlog($number_lines) {
	$lines=array();
	if( file_exists("/data/logs/bot.log") ) {
		$fp = fopen("/data/logs/bot.log", "r");
		$buffer=array();
		while($line = fgets($fp, 4096)) {
			array_push($buffer, $line);
		}
		fclose($fp);
		$buffer = array_reverse($buffer);
		foreach($buffer as $line) {
            array_push($lines, $line);
            if (count($lines)>$number_lines) {
                break;
            }
            continue;
		}
		$lines = array_reverse($lines);
	} else {
		$lines[] = "No log entry found...\n";
		$lines[] = "The logfile will be created with next startup.\n";
	}
	return $lines;
}
function getJSSelectOption($db_array, $commaStr){
    $arrStr = "";
    if ( empty($commaStr) ){
        return $arrStr;
    }
    $tmpArray = explode(",", $commaStr);
    foreach ($tmpArray as $key=>$value){
        $arrStr = $arrStr . "," . '"' . $value . '"';
    }
    return ltrim($arrStr, ',');
}
?>