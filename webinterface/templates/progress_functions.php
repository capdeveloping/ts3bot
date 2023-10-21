<?php
function getProgressMin($counter){
    $minvalue = 0;
    if($counter >= 10000000){
        $minvalue = 10000000;
    }else if($counter >= 5000000){
        $minvalue = 5000000;
    }else if($counter >= 2000000){
        $minvalue = 2000000;
    }else if($counter >= 1000000){
        $minvalue = 1000000;
    }else if($counter >= 500000){
        $minvalue = 500000;
    }else if($counter >= 200000){
        $minvalue = 200000;
    }else if($counter >= 100000){
        $minvalue = 100000;
    }else if($counter >= 50000){
        $minvalue = 50000;
    }else if($counter >= 20000){
        $minvalue = 20000;
    }else if($counter >= 10000){
        $minvalue = 10000;
    }else if($counter >= 5000){
        $minvalue = 5000;
    }else if($counter >= 2000){
        $minvalue = 2000;
    }else if($counter >= 1000){
        $minvalue = 1000;
    }else if($counter >= 500){
        $minvalue = 500;
    }else if($counter >= 200){
        $minvalue = 200;
    }else if($counter >= 100){
        $minvalue = 100;
    }else if($counter >= 0){
        $minvalue = 0;
    }
    return $minvalue;
}

function getProgressMax($counter){
    $maxvalue = 0;
    if($counter < 100){
        $maxvalue = 100;
    }else if($counter < 200){
        $maxvalue = 200;
    }else if($counter < 500){
        $maxvalue = 500;
    }else if($counter < 1000){
        $maxvalue = 1000;
    }else if($counter < 2000){
        $maxvalue = 2000;
    }else if($counter < 5000){
        $maxvalue = 5000;
    }else if($counter < 10000){
        $maxvalue = 10000;
    }else if($counter < 20000){
        $maxvalue = 20000;
    }else if($counter < 50000){
        $maxvalue = 50000;
    }else if($counter < 100000){
        $maxvalue = 100000;
    }else if($counter < 200000){
        $maxvalue = 200000;
    }else if($counter < 500000){
        $maxvalue = 500000;
    }else if($counter < 1000000){
        $maxvalue = 1000000;
    }else if($counter < 2000000){
        $maxvalue = 2000000;
    }else if($counter < 5000000){
        $maxvalue = 5000000;
    }else if($counter < 10000000){
        $maxvalue = 10000000;
    }
    return $maxvalue;
}

function getProgressWidth($counter){
    $width = 0;
    $progressMin = $counter - getProgressMin($counter);
    $progressMax = getProgressMax($counter) - $counter;
    $width = round(($progressMin/$progressMax*100), 1);
    return $width;
}
?>