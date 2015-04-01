<?php
ini_set('max_execution_time', 300);

// Change directory to the input data file folder
chdir("/home/hduser/LxC/data");

$userName = $_POST['Username'];
$infile = $userName.'.txt';

$applianceName = $_POST['ApplianceName'].';';
$startTime = $_POST['StartTime'].';';
$stopTime = $_POST['StopTime'].';';
$runTime = $_POST['RunTime'].';';
$watt = $_POST['Watt'].';';
$constraints = $_POST['Constraints'];
// Write the necessary contents
//file_put_contents($infile, "Check for android and hadoop Veola");
//if ( 0 != filesize( $infile ) )
//{
	//file_put_contents($infile, "\n", FILE_APPEND);
//}
file_put_contents($infile, $applianceName, FILE_APPEND);
file_put_contents($infile, $startTime, FILE_APPEND);
file_put_contents($infile, $stopTime, FILE_APPEND);
file_put_contents($infile, $runTime, FILE_APPEND);
file_put_contents($infile, $watt, FILE_APPEND);
file_put_contents($infile, $constraints, FILE_APPEND);
file_put_contents($infile, "\n", FILE_APPEND);

//echo 'data rxd';
// Wait for the output file

//while (!file_exists($outfile)) 
	//{
		//sleep(2);
	//}

//sleep(4);
//echo file_get_contents($outfile);
ob_flush();
flush();
?>

