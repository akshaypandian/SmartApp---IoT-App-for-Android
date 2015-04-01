<?php
	$pyscript = 'homeserv.py';
	$python = 'python';
	$userName = $_POST['Username'];
	//$userName = 'xgHV6Yeqjv';
	$outfile1 = '/home/hduser/LxC/data/'.$userName.'_schedule.txt';
	$outfile = 'hourWiseDukePower.txt';	
	//$outfile = '/home/hduser/LxC/data/schedules'.'xgHV6Yeqjv'.'_schedule.txt';
	@unlink($outfile);
	$cmd= 'java simulatedAnnealing /home/hduser/LxC/data/'.$userName.'.txt 20141203-da.csv';
	//$cmd= 'python homeserv.py '.'xgHV6Yeqjv';

	exec("$cmd", $output);
	//print_r($output);
	//echo $output[0];
	while (!file_exists($outfile)) 
	{
		sleep(1);
	}
	echo file_get_contents($outfile);
	file_put_contents($outfile1, file_get_contents($outfile));
	ob_flush();
	flush();
?>
