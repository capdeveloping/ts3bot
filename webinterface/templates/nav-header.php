<!--
        <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
            <a class="navbar-brand" href="/index.php">Ts3 Bot Webinterface</a>
            <button class="btn btn-link btn-sm order-1 order-lg-0" id="sidebarToggle" href="#"><i class="fas fa-bars"></i></button>
             Navbar
            <div class="col-md-6">
                <a href="?lang=eng"><img src="/assets/img/us.png" title="English" alt="English"></a>
                <a href="?lang=ger"><img src="/assets/img/de.png" title="Deutsch" alt="Deutsch"></a>
            </div>
            <div class="dropdown d-none d-md-inline-block form-inline ml-auto mr-0 mr-md-3 my-2 my-md-0">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="header-dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><?php if ( empty($_SESSION["instance_name"]) ){ ?>create instance<?php }else{ echo $_SESSION["instance_name"];} ?></button>
                <div class="dropdown-menu" aria-labelledby="header-dropdown">
                    <form class="form-horizontal" data-toggle="validator" name="changeInstance" method="POST">
<?php if ( empty($_SESSION["instances"]) ){ ?>
                        <a class="dropdown-item" href="/instances.php">Go to Start Bot</a>
<?php
} else {
    foreach($_SESSION["instances"] as $key => $value){
        if($_SESSION["instance_name"] === $key ){ ?>
                        <button name=<?php echo '"' . $key . '"'; ?> class="dropdown-item active" type="submit"><i class="fas fa-angle-right" style="margin-right: 10px;"></i><?php echo $key; ?></button>
<?php   } else { ?>
                        <button name=<?php echo '"' . $key . '"'; ?> class="dropdown-item" type="submit"><?php echo $key; ?></button>
<?php   }
    }
} ?>
                    </form>
                </div>
            </div>
            <ul class="navbar-nav ml-auto ml-md-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="userDropdown" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="fas fa-user fa-fw"></i><?php echo $_SESSION["userid"]; ?>
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
                    <a class="dropdown-item" href="/change-password.php">Password aendern</a>
                    <div class="dropdown-divider"></div>
                        <form class="form-horizontal" data-toggle="validator" name="logout" method="POST">
                            <div style="text-align: center;">
                                <button name="logout" type="submit" class="btn btn-danger">Logout</button>
                            </div>
                        </form>
                    </div>
                </li>
            </ul>
        </nav>
-->
                <!-- Topbar -->
                <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                    <!-- Sidebar Toggle (Topbar) -->
                    <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                        <i class="fa fa-bars"></i>
                    </button>

                    <!-- Topbar Search -->
                    <!--<form
                        class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
                        <div class="input-group">
                            <input type="text" class="form-control bg-light border-0 small" placeholder="Search for..."
                                aria-label="Search" aria-describedby="basic-addon2">
                            <div class="input-group-append">
                                <button class="btn btn-primary" type="button">
                                    <i class="fas fa-search fa-sm"></i>
                                </button>
                            </div>
                        </div>
                    </form>
                    -->

                    <!-- Topbar Navbar -->
                    <ul class="navbar-nav ml-auto">

                        <!-- Nav Item - Search Dropdown (Visible Only XS) -->
                        <li class="nav-item dropdown no-arrow d-sm-none">
                            <a class="nav-link dropdown-toggle" href="#" id="searchDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-search fa-fw"></i>
                            </a>
                            <!-- Dropdown - Messages -->
                            <div class="dropdown-menu dropdown-menu-right p-3 shadow animated--grow-in"
                                aria-labelledby="searchDropdown">
                                <form class="form-inline mr-auto w-100 navbar-search">
                                    <div class="input-group">
                                        <input type="text" class="form-control bg-light border-0 small"
                                            placeholder="Search for..." aria-label="Search"
                                            aria-describedby="basic-addon2">
                                        <div class="input-group-append">
                                            <button class="btn btn-primary" type="button">
                                                <i class="fas fa-search fa-sm"></i>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </li>

                        <!-- Nav Item - Messages -->
                        <li class="nav-item dropdown no-arrow mx-1">
                            <a class="nav-link dropdown-toggle" href="#" id="messagesDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-server"></i><div class="row">&nbsp;&nbsp;&nbsp; Instanzmenue</div>
                                <!-- Counter - Messages -->
                            </a>
                            <!-- Dropdown - Messages -->
                            <div class="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in"
                                aria-labelledby="messagesDropdown">
                                <h6 class="dropdown-header">
                                    Instanzen
                                </h6>
                                <form class="form-horizontal" data-toggle="validator" name="changeInstance" method="POST">
<?php if ( empty($_SESSION["instances"]) ){ ?>
                                    <a class="dropdown-item d-flex align-items-center" href="/instances.php">
                                        <div class="dropdown-list-image mr-3">
                                            <img class="rounded-circle" src="assets/img/undraw_profile.svg"
                                                alt="...">
                                            <div class="status-indicator bg-success"></div>
                                        </div>
                                        <div class="font-weight-bold">
                                            Go to Start Bot
                                        </div>
                                    </a>
<?php
} else {
    foreach($_SESSION["instances"] as $key => $value){
        if($_SESSION["instance_name"] === $key ){ ?>
                                    <button class="dropdown-item d-flex font-weight-bold" name=<?php echo '"' . $key . '"'; ?> class="dropdown-item" type="submit"><?php echo $key; ?>
                                        <div class="dropdown-list-image mr-3">
                                            <div class="status-indicator bg-success"></div>
                                        </div>
                                    </button>
<?php   } else { ?>
                                    <button class="dropdown-item d-flex align-items-center font-weight-bold" name=<?php echo '"' . $key . '"'; ?> class="dropdown-item" type="submit"><?php echo $key; ?>
                                    </button>
<?php   }
    }
} ?>
                                </form>
                            </div>
                        </li>

                        <div class="topbar-divider d-none d-sm-block"></div>

                        <!-- Nav Item - User Information -->
                        <li class="nav-item dropdown no-arrow">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span class="mr-2 d-none d-lg-inline text-gray-600 small"><?php echo $_SESSION['user']['username']; ?></span>
                                <img class="img-profile rounded-circle"
<?php if( $_SESSION["nav_expanded"] ){ ?>
                                    src="../assets/img/undraw_profile.svg">
<?php } else { ?>
                                    src="assets/img/undraw_profile.svg">
<?php }?>
                            </a>
                            <!-- Dropdown - User Information -->
                            <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                                aria-labelledby="userDropdown">
                                <a class="dropdown-item" href="#">
                                    <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Profile
                                </a>
                                <div class="dropdown-divider"></div>
                                <form class="form-horizontal" data-toggle="validator" name="logout" method="POST">
                                    <button class="dropdown-item d-flex align-items-center font-weight-bold" name=logout class="dropdown-item" type="submit">
                                        <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                        Logout
                                    </button>
                                </form>
                            </div>
                        </li>

                    </ul>

                </nav>
                <!-- End of Topbar -->
