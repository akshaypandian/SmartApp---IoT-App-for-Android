package org.myorg;

import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class Utility
{
	public static class UtilityMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
	{
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> outputCollector, Reporter reporter) throws IOException
		{
			String line = value.toString();
			String outkey = line.split(" ")[0];
			String outval = line.split(" ")[1];
		
			outputCollector.collect(new Text(outkey), new Text(outval));
		}
	}
	
	public static class UtilityReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text>
	{
		public void reduce(Text key, Iterator<Text> vals, OutputCollector<Text, Text> outputCollector, Reporter reporter) throws IOException
		{
			double sum = 0;
			while (vals.hasNext())
			{
				sum += Double.parseDouble(vals.next().toString());
			}
			outputCollector.collect(key, new Text(Double.toString(sum)));
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		JobConf conf = new JobConf(Utility.class);
		conf.setJobName("utility");
		
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
		
		conf.setMapperClass(UtilityMapper.class);
		conf.setReducerClass(UtilityReducer.class);
		
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		
		JobClient.runJob(conf);
	}
}
