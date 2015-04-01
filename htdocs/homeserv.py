import sys
import random
import datetime

username = sys.argv[1]
appfile = open('/home/hduser/LxC/data/' + username + '.txt')
pricefile = open('/home/hduser/LxC/data/pricelist.txt')
outfile = open('/home/hduser/LxC/data/' + username + '_schedule.txt', 'w')

applines = appfile.readlines()
pricelines = pricefile.readlines()

pricelist = pricelines[1].split('\n')
pricelist = pricelist[0].split(',')
#print pricelist

apps = []

for line in applines:
	fields = line.split(';')
	app = {'ApplianceName':'', 'StartTime':'', 'StopTime':'', 'RunTime':'', 'Watt':'', 'Constraints':''}
	app['ApplianceName'] = fields[0]
	app['StartTime'] = int(fields[1])
	app['StopTime'] = int(fields[2])
	app['RunTime'] = int(fields[3])
	app['Watt'] = int(fields[4])
	app['Constraints'] = fields[5]
	apps.append(app)
	
times = dict()

for i in range(0, 24):
	times[pricelist[i]] = i;
#print times

prioritylist = []

for watt in sorted(times):
	prioritylist.append(times[watt])
#print prioritylist

schedule = []

for i in range(0, 24):
	schedule.append([])

for app in apps:
	runtime = app['RunTime']
	if app['Constraints'] == 'HC\n':
		#print app
		for timeslot in prioritylist:
			if (timeslot >= app['StartTime']) and (timeslot < app['StopTime']):			
				#print runtime				
				schedule[timeslot].append(app)
				runtime = runtime - 1
				if runtime < 1:
					break
	else:
		#print app
		for timeslot in prioritylist:
			if (timeslot >= app['StartTime']) and (timeslot < 24):			
				#print runtime				
				schedule[timeslot].append(app)
				runtime = runtime - 1
				if runtime < 1:
					break
					
scheduledWattage = []
for i in range(0, 24):
	scheduledWattage.append(0)

renewable = []
for i in range(0, 24):
	renewable.append(random.randint(10, 75))

					
for i in range(0, 24):
	if len(schedule[i]) > 0:
		for app in schedule[i]:
			scheduledWattage[i] = scheduledWattage[i] + app['Watt']
		scheduledWattage[i] = scheduledWattage[i] - renewable[i]
print scheduledWattage
outfile.write(str(scheduledWattage)+'\n')
for i in range(0, 24):
	outfile.write(str(i)+' ')
	for app in schedule[i]:
		outfile.write(app['ApplianceName'].replace(' ','-')+' ')
	outfile.write('\n')
appfile.close()
pricefile.close()
outfile.close()
