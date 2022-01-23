            <!-- Sidebar -->
            <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

                <!-- Sidebar - Brand -->
                <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.php">
                    <div class="sidebar-brand-icon rotate-n-15">
                        <i class="fas fa-laugh-wink"></i>
                    </div>
                    <div class="sidebar-brand-text mx-3">Ts3Bot Webinterface <sup>2</sup></div>
                </a>

                <!-- Divider -->
                <hr class="sidebar-divider my-0">

                <!-- Nav Item - Dashboard -->
                <li class="nav-item nav-item-1 <?php if(basename($_SERVER['SCRIPT_NAME']) == 'index.php'){echo 'active'; }else { echo ''; } ?>">
                    <a class="nav-link" href="/index.php">
                        <i class="fas fa-fw fa-tachometer-alt"></i>
                        <span>Dashboard</span></a>
                </li>

                <!-- Divider -->
                <hr class="sidebar-divider">

                <li class="nav-item nav-item-1 <?php if(basename($_SERVER['SCRIPT_NAME']) == 'change-password.php'){echo 'active'; }else { echo ''; } ?>">
                    <a class="nav-link" href="/change-password.php">
                        <i class="fas fa-key"></i>
                        <span>Change Password</span></a>
                </li>
                <li class="nav-item nav-item-1 <?php if(basename($_SERVER['SCRIPT_NAME']) == 'modify-users.php'){echo 'active'; }else { echo ''; } ?>">
                    <a class="nav-link" href="/modify-users.php">
                        <i class="fas fa-users"></i>
                        <span>Modify Users</span></a>
                </li>

                <!-- Divider -->
                <hr class="sidebar-divider">



 <?php if (!empty($_SESSION["config"])) { ?>
                <li class="nav-item nav-item-2 <?php if(basename($_SERVER['SCRIPT_NAME']) == 'core.php'){echo 'active'; }else { echo ''; } ?>">
                    <a class="nav-link " href="/core.php">
                        <i class="fas fa-table"></i>
                        <span>Core</span></a>
                </li>
 <?php } ?>
 <?php
         $nav_main_position = 2;
         if ( $_SESSION['db_users'] != array()
                && $_SESSION['db_groups'] != array()
                && $_SESSION['db_channels'] != array()
                && $_SESSION['functions'] != array() ){
             $nav_position = 3;
             $nav_main_position++;
             if( $_SESSION['functions'] != array() ){
                $nav_position++;?>
                 <!-- Nav Item - Utilities Collapse Menu -->
<?php            if( ! $_SESSION["nav_expanded"] ){?>
                 <li class="nav-item">
                     <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseUtilities"
                         aria-expanded="false" aria-controls="collapseUtilities">
                         <i class="fas fa-fw fa-wrench"></i>
                         <span>Funktionen</span>
                     </a>

                     <div id="collapseUtilities" class="collapse" aria-labelledby="headingUtilities"
<?php            }else{ ?>
                 <li class="nav-item">
                     <a class="nav-link" href="#" data-toggle="collapse" data-target="#collapseUtilities"
                         aria-expanded="true" aria-controls="collapseUtilities">
                         <i class="fas fa-fw fa-wrench"></i>
                         <span>Funktionen</span>
                     </a>

                     <div id="collapseUtilities" class="collapse show" aria-labelledby="headingUtilities"
<?php            } ?>
                         data-parent="#accordionSidebar">
                         <div class="bg-white py-2 collapse-inner rounded">
<?php           if (array_key_exists("AcceptRules", $_SESSION["functions"])) { ?>
                            <a class="collapse-item nav-item-expanded-<?php print_r($nav_position);?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'accept-ts3-rules.php'){echo 'active'; }else { echo ''; } ?>" href="/functions/accept-ts3-rules.php">Accept Ts3 Rules</a>
<?php           $nav_position++;}
                if (array_key_exists("AutoRemove", $_SESSION["functions"])) { ?>
                            <a class="collapse-item nav-item-expanded-<?php print_r($nav_position);?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'auto-remove-group.php'){echo 'active'; }else { echo ''; } ?>" href="/functions/auto-remove-group.php">Auto Remove Group</a>
<?php           $nav_position++;}
                if (array_key_exists("ChannelAutoCreate", $_SESSION["functions"])) { ?>
                            <a class="collapse-item nav-item-expanded-<?php print_r($nav_position);?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'channelautocreate.php'){echo 'active'; }else { echo ''; } ?>" href="/functions/channelautocreate.php">ChannelAutoCreate</a>
<?php           $nav_position++;}
                if (array_key_exists("ClientAFK", $_SESSION["functions"])) { ?>
                            <a class="collapse-item nav-item-expanded-<?php print_r($nav_position);?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'client-afk-mover.php'){echo 'active'; }else { echo ''; } ?>" href="/functions/client-afk-mover.php">Client AFK Mover</a>
<?php           $nav_position++;}
                if (array_key_exists("ClientMove", $_SESSION["functions"])) { ?>
                            <a class="collapse-item nav-item-expanded-<?php print_r($nav_position);?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'client-moved-channel.php'){echo 'active'; }else { echo ''; } ?>" href="/functions/client-moved-channel.php">Client Moved</a>
<?php           $nav_position++;}
                if (array_key_exists("VersionChecker", $_SESSION["functions"])) { ?>
                            <a class="collapse-item nav-item-expanded-<?php print_r($nav_position);?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'new-version-checker.php'){echo 'active'; }else { echo ''; } ?>" href="/functions/new-version-checker.php">New Version Checker</a>
<?php           $nav_position++;}
                if (array_key_exists("Viewer", $_SESSION["functions"])) { ?>
                            <a class="collapse-item nav-item-expanded-<?php print_r($nav_position);?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'ts3-viewer.php'){echo 'active'; }else { echo ''; } ?>" href="/functions/ts3-viewer.php">Ts3 Viewer</a>
<?php           $nav_position++;}
                if (array_key_exists("Twitch", $_SESSION["functions"])) { ?>
                            <a class="collapse-item nav-item-expanded-<?php print_r($nav_position);?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'twitch-controller.php'){echo 'active'; }else { echo ''; } ?>" href="/functions/twitch-controller.php">Twitch Controller</a>
<?php           $nav_position++;}
                if (array_key_exists("WelcomeMessage", $_SESSION["functions"])) { ?>
                            <a class="collapse-item nav-item-expanded-<?php print_r($nav_position);?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'welcome-message.php'){echo 'active'; }else { echo ''; } ?>" href="/functions/welcome-message.php">Welcome Message</a>
<?php           $nav_position++;}?>
                        </div>
                    </div>
                </li>
<?php       }
         }
      if (!empty($_SESSION["config"])) { ?>
                <li class="nav-item nav-item-<?php $nav_main_position++; echo $nav_main_position?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'start-stop-bot.php'){echo 'active'; }else { echo ''; } ?>">
                    <a class="nav-link" href="/start-stop-bot.php">
                        <i class="fa fa-power-off fa-w-16"></i>
                        <span>Start / Stop Bot</span>
                    </a>
                </li>
<?php }?>
                <li class="nav-item nav-item-<?php $nav_main_position++; echo $nav_main_position?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'instances.php'){echo 'active'; }else { echo ''; } ?>">
                    <a class="nav-link" href="/instances.php">
                        <i class="fas fa-server"></i>
                        <span>Instanzen</span>
                    </a>
                </li>
                <li class="nav-item nav-item-<?php $nav_main_position++; echo $nav_main_position?> <?php if(basename($_SERVER['SCRIPT_NAME']) == 'info.php'){echo 'active'; }else { echo ''; } ?>">
                    <a class="nav-link" href="/info.php">
                        <i class="fas fa-info-circle"></i>
                        <span>Bot Informationen</span>
                    </a>
                </li>

                <!-- Divider -->
                <hr class="sidebar-divider d-none d-md-block">
                <li class="nav-item">
                    <a class="nav-link" href="https://github.com/capdeveloping/ts3bot">
                        <i class="fas fa-code-branch"></i>
                        <span>v1.1.12</span>
                    </a>
                </li>
                <!-- <div class="indicator"></div>-->

            </ul>
            <!-- End of Sidebar -->
            <script>
                const list = document.querySelectorAll('.nav-link');
                function activeLink(){
                    list.forEach((item) =>
                    item.classList.remove('active'));
                    this.classList.add('active');
                }
                list.forEach((item) =>
                item.addEventListener('click', activeLink));
            </script>
