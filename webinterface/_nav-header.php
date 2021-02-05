<?php
    if(saveCurrentInstance($_POST, $instances) ){
        header("refresh:0");
    }
?>
        <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
            <a class="navbar-brand" href="index.php">Ts3 Bot Webinterface</a>
            <button class="btn btn-link btn-sm order-1 order-lg-0" id="sidebarToggle" href="#"><i class="fas fa-bars"></i></button>
            <!-- Navbar-->
            <div class="dropdown d-none d-md-inline-block form-inline ml-auto mr-0 mr-md-3 my-2 my-md-0">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="header-dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><?php if ( empty($instance_name) ){ ?>create instance<?php }else{ echo $instance_name;} ?></button>
                <div class="dropdown-menu" aria-labelledby="header-dropdown">
                    <form class="form-horizontal" data-toggle="validator" name="changeInstance" method="POST">
<?php if ( empty($instances) ){ ?>
                        <a class="dropdown-item" href="instances.php">Go to Start Bot</a>
<?php
} else {
    foreach($instances as $key => $value){
        if($instance_name === $key ){ ?>
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
                    <a class="nav-link dropdown-toggle" id="userDropdown" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fas fa-user fa-fw"></i></a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
                        <a class="dropdown-item" href="#">Settings</a>
                        <a class="dropdown-item" href="#">Activity Log</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="login.html">Logout</a>
                    </div>
                </li>
            </ul>
        </nav>
