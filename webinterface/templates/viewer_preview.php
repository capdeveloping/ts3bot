<?php
    require_once($_SERVER["DOCUMENT_ROOT"] . '/templates/preload.php');

    if (array_key_exists("Viewer", $_SESSION["functions"])) {
        $viewerKey = $_SESSION["functions"]["Viewer"];
    }else{
        header("Refresh:0; url=/core.php");
        exit();
    }
?>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
.tsv_content {
   color: <?php echo $_SESSION["config"][$viewerKey . "_ts3_viewer_text_color"]; ?>;
   line-height: 1;
}
.tsv_suffix {
    float: right;
    position: relative;
    text-align: right;
}
.tsv_depth_0 {
   margin-left:0;
}
.tsv_depth_1 {
   margin-left:20;
}
.tsv_depth_2 {
   margin-left:40;
}
.tsv_depth_3 {
   margin-left:60;
}
.tsv_depth_4 {
   margin-left:80;
}
.channel_center {
        text-align:center;
}
channel_right {
        display:block;
        text-align:right;
}
/* Tooltip container */
.tooltip {
    position: relative;
}

/* Tooltip text */
.tooltip .tooltiptext {
    visibility: hidden;
    width: 140px;
    background-color: white;
        border: 1px solid black;
    color: black;
    text-align: left;
    padding: 5px 5;
    top: 110%;
    left: 0%;
    border-radius: 6px;
    margin-top: -1px;

    /* Position the tooltip text - see examples below! */
    position: absolute;
    z-index: 1;
}

.tooltip .tooltiptext::after {
    content: " ";
    position: absolute;
    bottom: 100%;  /* At the top of the tooltip */
    left: 10%;
    margin-left: -5px;
    border-width: 5px;
    border-style: solid;
    border-color: transparent transparent transparent;
}

/* Show the tooltip text when you mouse over the tooltip container */
.tooltip:hover .tooltiptext {
    visibility: visible;
}
button {
        position: relative;
        margin: 0em;
        padding: 0em 0em;
        width: 10.5em;
        background: #D8D8D8;
        color: white;
        border: 1px solid grey;
        border-radius: 0px;
        cursor: pointer;
    text-align: center;
}
button:hover, button:active {
        outline: none;
        background: #B9FF21;
        color: darkblue;
}
a:link{
        text-decoration: none;
        color: black;
}</style>
<body bgcolor=<?php echo '"' . $_SESSION["config"][$viewerKey . "_ts3_viewer_background_color"] . '"'; ?>>
<div class="tsv_content tsv_depth_0">
    <div>
        <p align="center" >Letzte Aktualisierung am <?php echo date("d.m.Y \u\m H:i:s")?></p>
    </div>
    <div>
        <img src="/assets/img/ts3_icons/server_open.png">
        <a class="link" href="ts3server://evil-lions.de?port=7000" style="color:<?php echo $_SESSION["config"][$viewerKey . "_ts3_viewer_text_color"]; ?>;">Preview TeamSpeak 3 Server</a>
    </div>
    <div>
        <img src="/assets/img/ts3_icons/ts3.png"> User: 4/10
    </div>
    <br>
</div>
<div class="tsv_content">
    <div class="tsv_depth_0">
        <img src="/assets/img/ts3_icons/channel/channel_green_subscribed.png"> Default Channel                   <img align="right" src="/assets/img/ts3_icons/channel/default.png">
    </div>
    <div class="tsv_content tooltip tsv_depth_2">
            <img src="/assets/img/ts3_icons/client/player_off.png">  User 1
            <span class="tooltiptext">
                <table>
                    <tr>
                        <td>online</td>
                        <td>10min</td>
                    </tr>
                    <tr>
                        <td>idle</td>
                        <td>0min</td>
                    </tr>
                </table>
                <button type="button">
                    <a class="link" href="ts3server://<?php echo $_SESSION["config"][$viewerKey . "_ts3_viewer_server_ip"]; ?>?port=<?php echo $_SESSION["config"]["ts3_server_port"]; ?>">zum Client verbinden</a>
                </button>
            </span>
            <img title="DE" align="right" src="/assets/img/ts3_icons/countries/de.png">
            <img align="right" src="/assets/img/ts3_icons/gruppen/582334931.png" title="Server Admin">
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_depth_0">
        <div class="channel_center">—(•·÷[ Talk Area ]÷·•)—</div>
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_content tsv_depth_1">
        <img src="/assets/img/ts3_icons/channel/channel_green_subscribed.png"> Talk | Raum I
    </div>
    <div class="tsv_content tooltip tsv_depth_2">
            <img src="/assets/img/ts3_icons/client/input_muted.png">  User 2
            <span class="tooltiptext">
                <table>
                    <tr>
                        <td>online</td>
                        <td>1h 50min</td>
                    </tr>
                    <tr>
                        <td>idle</td>
                        <td>5min</td>
                    </tr>
                </table>
                <button type="button">
                    <a class="link" href="ts3server://<?php echo $_SESSION["config"][$viewerKey . "_ts3_viewer_server_ip"]; ?>?port=<?php echo $_SESSION["config"]["ts3_server_port"]; ?>">zum Client verbinden</a>
                </button>
            </span>
            <img title="UK" align="right" src="/assets/img/ts3_icons/countries/uk.png">
    </div>
    <div class="tsv_content tooltip tsv_depth_2">
            <img src="/assets/img/ts3_icons/client/player_on.png"> User 3
            <span class="tooltiptext">
                <table>
                    <tr>
                        <td>online</td>
                        <td>3h 35min</td>
                    </tr>
                    <tr>
                        <td>idle</td>
                        <td>0min</td>
                    </tr>
                </table>
                <button type="button">
                    <a class="link" href="ts3server://<?php echo $_SESSION["config"][$viewerKey . "_ts3_viewer_server_ip"]; ?>?port=<?php echo $_SESSION["config"]["ts3_server_port"]; ?>">zum Client verbinden</a>
                </button>
            </span>
            <img title="UK" align="right" src="/assets/img/ts3_icons/countries/uk.png">
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_content tsv_depth_1">
        <img src="/assets/img/ts3_icons/channel/channel_green_subscribed.png"> Talk | Raum II
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_depth_0">
        <div class="channel_center">—(•·÷[ Game Area ]÷·•)—</div>
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_content tsv_depth_1">
        <img src="/assets/img/ts3_icons/channel/channel_green_subscribed.png"> Game | Raum I
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_depth_0">
        <div class="channel_center">_</div>
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_depth_0">
        <div class="channel_center">—(•·÷[ AFK Area ]÷·•)—</div>
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_content tsv_depth_1">
        <img src="/assets/img/ts3_icons/channel/channel_green_subscribed.png"> Kurz Afk
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_content tsv_depth_1">
        <img src="/assets/img/ts3_icons/channel/channel_green_subscribed.png"> Lang Afk
    </div>
    <div class="tsv_content tooltip tsv_depth_2">
            <img src="/assets/img/ts3_icons/client/output_muted.png"> User 4
            <span class="tooltiptext">
                <table>
                    <tr>
                        <td>online</td>
                        <td>1d 5h 10min</td>
                    </tr>
                    <tr>
                        <td>idle</td>
                        <td>5h 2min</td>
                    </tr>
                </table>
                <button type="button">
                    <a class="link" href="ts3server://<?php echo $_SESSION["config"][$viewerKey . "_ts3_viewer_server_ip"]; ?>?port=<?php echo $_SESSION["config"]["ts3_server_port"]; ?>">zum Client verbinden</a>
                </button>
            </span>
            <img title="US" align="right" src="/assets/img/ts3_icons/countries/us.png">
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_content tsv_depth_1">
        <img src="/assets/img/ts3_icons/channel/channel_green_subscribed.png"> anderer Teamspeak
    </div>
</div>
<div class="tsv_content">
    <div class="tsv_content tsv_depth_1">
        <img src="/assets/img/ts3_icons/channel/channel_green_subscribed.png"> kurz essen
    </div>
</div>
</body>