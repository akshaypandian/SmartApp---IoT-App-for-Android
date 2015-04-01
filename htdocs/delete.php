<?php

$path = "/home/hduser/LxC/data/".$_POST['Username'].".txt";
$lines = file($path, FILE_IGNORE_NEW_LINES);
$remove = $_POST['ApplianceName'];
//echo $remove;

foreach($lines as $key => $line)
  if(stristr($line, $remove)) unset($lines[$key]);

//print_r($lines);
$data = implode("\n", array_values($lines))."\n";
//echo $data;
$file = fopen($path, 'w');
fwrite($file, $data);
fclose($file);

?>
