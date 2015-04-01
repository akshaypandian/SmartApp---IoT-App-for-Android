<?php
ini_set('max_execution_time', 300);

// Change directory to the input data file folder
chdir("/home/hduser/tmp");

$infile = 'newtimestamp.txt';
$outfile = 'hadoop-output.txt';
unlink($outfile);

$hadoopin=$_POST['input'];
// Write the necessary contents
//file_put_contents($infile, "Check for android");
file_put_contents($infile, $hadoopin);

$intime = filectime($infile);

// Wait for the output file

while (!file_exists($outfile)) 
	{
		sleep(2);
	}

sleep(4);
//echo "Here";
echo file_get_contents($outfile);
ob_flush();
flush();
?>
