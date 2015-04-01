#! /bin/bash

oldTstamp=0

while true; do
newTstamp=`stat -c %Y ~/LxC/data/schedules`

# Compare the Timestamps

if [ $newTstamp -gt $oldTstamp ]
then
	echo "New data to be processed"
	oldTstamp=$newTstamp

	echo 
	scp -r ~/LxC/data/schedules ubuntu@10.0.3.10:data/snip/
	scp /home/hduser/Documents/utility.jar ubuntu@10.0.3.10:/home/ubuntu/data/

	#nn_configuration.expect is used to set up automatic log-in. 
	./nn_runhadoop.expect 
	
	scp ubuntu@10.0.3.10:/home/ubuntu/data/snip-output/hadoop-output.txt /home/hduser/LxC/data/hadoop-output.txt

	echo 
	chmod 777 /home/hduser/LxC/data/hadoop-output.txt

	echo "Output available - /home/hduser/LxC/data/hadoop-output.txt"
fi
done

exit

