                <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                    <div class="sb-sidenav-menu">
                        <div class="nav">
                            <div class="sb-sidenav-menu-heading">Statistics</div>
                            <a class="nav-link" href="/index.php">
                                <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                                Dashboard
                            </a>
<?php if (!empty($_SESSION["config"])) { ?>
                            <div class="sb-sidenav-menu-heading">Settings</div>
                            <a class="nav-link" href="/core.php">
                                <div class="sb-nav-link-icon"><i class="fas fa-table"></i></div>
                                Core
                            </a>
<?php if( ! $_SESSION["nav_expanded"] ){ ?>
                            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseLayouts" aria-expanded="false" aria-controls="collapseLayouts">
<?php } else {?>
                            <a class="nav-link" href="#" data-toggle="collapse" data-target="#collapseLayouts" aria-expanded="true" aria-controls="collapseLayouts">
<?php }?>
                                <div class="sb-nav-link-icon"><i class="fas fa-columns"></i></div>
                                Funktionen
                                <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                            </a>
<?php if( ! $_SESSION["nav_expanded"] ){ ?>
                            <div class="collapse" id="collapseLayouts" aria-labelledby="headingOne" data-parent="#sidenavAccordion">
<?php } else {?>
                            <div class="collapse show" id="collapseLayouts" aria-labelledby="headingOne" data-parent="#sidenavAccordion">
<?php }?>
                                <nav class="sb-sidenav-menu-nested nav">
<?php if (array_key_exists("Twitch", $_SESSION["functions"])) { ?>
                                    <a class="nav-link" href="/functions/twitch-controller.php">Twitch Controller</a>
<?php }
if (array_key_exists("ClientAFK", $_SESSION["functions"])) { ?>
                                    <a class="nav-link" href="/functions/client-afk-mover.php">Client AFK Mover</a>
<?php }
if (array_key_exists("ClientMove", $_SESSION["functions"])) { ?>
                                    <a class="nav-link" href="/functions/client-moved-channel.php">Client Moved</a>
<?php }
if (array_key_exists("WelcomeMessage", $_SESSION["functions"])) { ?>
                                    <a class="nav-link" href="/functions/welcome-message.php">Welcome Message</a>
<?php }
if (array_key_exists("AutoRemove", $_SESSION["functions"])) { ?>
                                    <a class="nav-link" href="/functions/auto-remove-group.php">Auto Remove Group</a>
<?php }
if (array_key_exists("Viewer", $_SESSION["functions"])) { ?>
                                    <a class="nav-link" href="/functions/ts3-viewer.php">Ts3 Viewer</a>
<?php }
if (array_key_exists("VersionChecker", $_SESSION["functions"])) { ?>
                                    <a class="nav-link" href="/functions/new-version-checker.php">New Version Checker</a>
<?php }
if (array_key_exists("AcceptRules", $_SESSION["functions"])) { ?>
                                    <a class="nav-link" href="/functions/accept-ts3-rules.php">Accept Ts3 Rules</a>
<?php }
if (array_key_exists("ChannelAutoCreate", $_SESSION["functions"])) { ?>
                                    <a class="nav-link" href="/functions/channelautocreate.php">ChannelAutoCreate</a>
<?php }?>
                                </nav>
                            </div>
<?php }
 if (!empty($_SESSION["config"])) { ?>
                            <a class="nav-link" href="/start-stop-bot.php">
                                <div class="sb-nav-link-icon"><i class="fa fa-power-off fa-w-16"></i></div>
                                Start / Stop Bot
                            </a>
<?php }?>
                            <a class="nav-link" href="/instances.php">
                                <div class="sb-nav-link-icon"><i class="fas fa-server"></i></div>
                                Instanzen
                            </a>
                            <a class="nav-link" href="/info.php">
                                <div class="sb-nav-link-icon"><i class="fas fa-info-circle"></i></div>
                                Bot Informationen
                            </a>
                        </div>
                    </div>
                </nav>
