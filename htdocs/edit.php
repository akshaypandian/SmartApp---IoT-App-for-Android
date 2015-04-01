<?php

$path = "/home/hduser/LxC/data/".$_POST['Username'].".txt";
$lines = file($path, FILE_IGNORE_NEW_LINES);
$remove = $_POST['ApplianceName'];
foreach($lines as $key => $line)
  if(stristr($line, $remove))
  {
  	unset($lines[$key]);
  	$applianceName = $_POST['ApplianceName'].';';
	$startTime = $_POST['StartTime'].';';
	$stopTime = $_POST['StopTime'].';';
	$runTime = $_POST['RunTime'].';';
	$watt = $_POST['Watt'].';';
	$constraints = $_POST['Constraints'];
  	$lines[$key] = $applianceName.$startTime.$stopTime.$runTime.$watt.$constraints;
	
  }

$data = implode("\n", array_values($lines))."\n";

$file = fopen($path, 'w');
//fwrite($file, $data);
file_put_contents($path, $data);
fclose($file);

?>
