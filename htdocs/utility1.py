#!/usr/bin/env python

import random	
import urllib
import datetime

hadoopOutFile = open("/home/hduser/LxC/data/hadoop-output.txt");
hadoopOutLines = hadoopOutFile.readlines()
usage = []
for i in range(0, 24):
	usage.append(0.0);
for i in range(0, 24):
	usage[int(hadoopOutLines[i].split('\t')[0])] = float(hadoopOutLines[i].split('\t')[1].split('\n')[0])
	
thresholds = []
for i in range(0, 24):
	thresholds.append(random.randint(max(usage) - 1000, max(usage)))
	
diffs = []
resched = 0
for i in range(0, 24):
	diffs.append(0.0)
	diffs[i] = usage[i] - thresholds[i];
	if (diffs[i] > 0):
		resched = 1

date = str(datetime.date.today()).replace("-","")
#pricefile = open(urllib.urlretrieve ("http://www.pjm.com/pub/account/lmpda/"+date+"-da.csv")[0])
pricefile = open("/home/hduser/Downloads/20141203-da.csv")
priceline = pricefile.readline()
priceline = pricefile.readline()
priceline = pricefile.readline()
priceline = pricefile.readline()
pricelist = priceline.split(',')[1:25]
for i in range(0, 24):
	pricelist[i] = float(pricelist[i])

extraCost = 0.0
for i in range(0, 24):
	if diffs[i] > 0:
		extraCost += (diffs[i] * pricelist[i])
print "exceeds:" + str(extraCost)
#outfile = open('/home/hduser/LxC/data/exceeds.txt','w')
#outfile.write("exceeds:" + str(extraCost))
#outfile.close()
#times = dict()

#for i in range(0, 24):
#	times[pricelist[i]] = i;
##print times

#prioritylist = []

#for watt in sorted(times):
#	prioritylist.append(times[watt])
##print prioritylist

#for i in range(0, 24):
#	if (diffs[i] > 0):
#		diff = diffs[i]
#		while (diff > 0):
			
