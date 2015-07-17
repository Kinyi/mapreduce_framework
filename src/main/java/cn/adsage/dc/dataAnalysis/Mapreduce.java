package cn.adsage.dc.dataAnalysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Mapreduce {

	//数据输入输出路径
	private static final String INPUT_PATH = "hdfs://172.16.3.151:9000/kinyi/data";
	private static final String OUTPUT_PATH = "hdfs://172.16.3.151:9000/kinyi/result";

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		try {
			FileSystem fileSystem = FileSystem.get(conf);
			if (fileSystem.exists(new Path(OUTPUT_PATH))) {
				fileSystem.delete(new Path(OUTPUT_PATH), true);
			}
			Job job = Job.getInstance(conf, Mapreduce.class.getSimpleName());
			job.setJarByClass(Mapreduce.class);
			job.setMapperClass(MyMapper.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			job.setReducerClass(MyReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(NullWritable.class);
			FileInputFormat.setInputPaths(job, INPUT_PATH);
			FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));
			job.waitForCompletion(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {
		Map<String, String> map = new HashMap<String, String>();

		@Override
		protected void map(LongWritable k1, Text v1,
				Mapper<LongWritable, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			//把每行数据转化为字符串格式
			String jsonString = v1.toString();
			String ipKey = "ip";
			String versionKey = "version";
			//获取ip属性对应的JSON对象字符串
			String ipValue = JsonUtils.parseTwoNoNestedAttr(jsonString, ipKey);
			//获取version属性对应的JSON对象字符串
			String versionValue = JsonUtils.parseTwoNoNestedAttr(jsonString, versionKey);
			//把ip和对应字符串以键值对形式添加到map中，目的是为了用context写出去
			map.put(ipKey, ipValue);
			//把version和对应字符串以键值对形式添加到map中
			map.put(versionKey, versionValue);
			//迭代map，用context写出去
			for (Entry<String, String> entry : map.entrySet()) {
				context.write(new Text(entry.getKey()), new Text(entry.getValue()));
			}
		}
	}

	public static class MyReducer extends Reducer<Text, Text, Text, NullWritable> {
		HashMap<String, String> map = new HashMap<String, String>();

		@Override
		protected void reduce(Text k2, Iterable<Text> v2s,
				Reducer<Text, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			//迭代具有相同key的字符串集合
			for (Text v2 : v2s) {
				//有可能一个字符串包含多个元素，如{"ip":{"1.1.1.1":"100","2.2.2.2":"200"}}
				String[] split = v2.toString().split(",");//切割格式查看Jsonutils.java
				for (String single : split) {
					//切分具体某个元素，数据格式为："1.1.1.1\t100"
					String[] nestedELement = single.split("\t");
					//属性 1.1.1.1
					String nestedKey = nestedELement[0];
					//值     100
					String nestedValue = nestedELement[1];
					//对数据进行处理，由此处进行算法切入
					String frequence = Float.parseFloat(nestedValue) / 10000 + "";
					//构造一个map，用于生成JSON
					map.put(nestedKey, frequence);
				}
			}
			//生成一个JSON并以字符串的格式进行输出
			String result = JsonUtils.generateJson(k2.toString(), map);
			context.write(new Text(result), NullWritable.get());
		}
	}
}