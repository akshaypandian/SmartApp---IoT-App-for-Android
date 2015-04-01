<?php
	$pyscript = 'homeserv.py';
	$python = 'python';
	$userName = $_POST['Username'];
	//$userName = 'xgHV6Yeqjv';
	$outfile = '/home/hduser/LxC/data/'.$userName.'_schedule.txt';	
	//$outfile = '/home/hduser/LxC/data/schedules'.'xgHV6Yeqjv'.'_schedule.txt';
	@unlink($outfile);
	$cmd= 'python homeserv.py '.$userName;
	//$cmd= 'python homeserv.py '.'xgHV6Yeqjv';

	exec("$cmd", $output);
	//print_r($output);
	//echo $output[0];
	while (!file_exists($outfile)) 
	{
		sleep(1);
	}
	echo file_get_contents($outfile);
	ob_flush();
	flush();
?>
