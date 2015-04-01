<?php
ini_set('max_execution_time', 300);

// Change directory to the input data file folder
chdir("/home/hduser/LxC/data/schedules");

$userName = $_POST['Username'];

$outfile = $userName.'_hadoop.txt';
$hadoopOut = '/home/hduser/LxC/data/hadoop-output.txt';
$oldHadoopOut = '/home/hduser/LxC/data/old-hadoop-output.txt';
$exceeds = '/home/hduser/LxC/data/exceeds.txt';
unlink($outfile);
unlink($hadoopOut);

//$hadoopin= explode(",", $_POST['input']);
// Write the necessary contents
//file_put_contents($infile, "Check for android");


for ($i = 0; $i < 24; $i++)
{
	if ($i == 0)
		file_put_contents($outfile, $i.' '.$_POST[strval($i)]."\n");
	else		
		file_put_contents($outfile, $i.' '.$_POST[strval($i)]."\n", FILE_APPEND);
}

while (!file_exists($hadoopOut)) 
{
	sleep(2);
}

$cmd = escapeshellcmd('/opt/lampp/htdocs/utility1.py');
$output = shell_exec($cmd);

//$cmd= 'python utility1.py';
//exec("$cmd", $output);
echo $output;
file_put_contents($oldHadoopOut, file_get_contents($hadoopOut));
//echo "Here";
//file_put_contents($exceeds, $output[0]);
//echo file_get_contents($exceeds);
ob_flush();
flush();
?>
