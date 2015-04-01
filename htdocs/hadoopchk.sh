#! /bin/bash
# A script to run the Hadoop wordcount example program on all
# files stored in the /home/hduser/tmp/snip directory (*.txt)


oldTstamp=0

while true; do
newTstamp=`stat -c %Y /home/hduser/tmp/snip`

# Compare the Timestamps

if [ $newTstamp -gt $oldTstamp ]
then
	echo "New data to be processed \n"
	oldTstamp=$newTstamp

	# If new data is available start the hadoop processes
	cd /usr/local/hadoop

	# Clear all the snip directories under tmp
	bin/hadoop fs -rmr /home/hduser/tmp/snip-hdfs
	bin/hadoop fs -rmr /home/hduser/tmp/snip-output

	# Copy files into the HDFS
	echo "Copying the new data into HDFS \n"
	bin/hadoop dfs -copyFromLocal /home/hduser/tmp/snip /home/hduser/tmp/snip-hdfs
	bin/hadoop dfs -ls /home/hduser/tmp/snip-hdfs

	# Run the mapreduce job
	echo "Running the map-reduce"
	echo
	bin/hadoop jar /home/hduser/Documents/utility.jar org.myorg.Utility /home/hduser/tmp/snip-hdfs /home/hduser/tmp/snip-output
	echo
	bin/hadoop dfs -cat /home/hduser/tmp/snip-output/part-00000 > /home/hduser/tmp/hadoop-output.txt
	chmod 777 /home/hduser/tmp/hadoop-output.txt
	echo "Output processed to /home/hduser/tmp/hadoop-output"
fi
done

exit
