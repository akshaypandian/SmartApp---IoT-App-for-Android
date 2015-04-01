import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

class Task {
	private String name;
	private int startTime;
	private int endTime;
	private int runtime;
	private int wattage;
	private double weight;
	public List<Integer> scheduledHours = new ArrayList<Integer>();

	public Task(String name, int startTime, int endTime, int runtime,
			int wattage, double weight) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.runtime = runtime;
		this.wattage = wattage;
		this.weight = weight;
	}

	public double stability(int hour) {
		if (hour < startTime) {
			return 1;
		} else if (hour >= endTime) {
			return ((hour - endTime + 1) * (weight));
		} else {
			return 0;
		}
	}

	public double getPrice(HashMap<Integer, Double> priceMap) {
		double sumPrice = 0;
		for (int i = 0; i < scheduledHours.size(); i++) {
			sumPrice += wattage * priceMap.get(scheduledHours.get(i));
		}
		return sumPrice;
	}

	public int getWattage() {
		return wattage;
	}

	public int getRuntime() {
		return runtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public List<Integer> getAssignedHours() {
		return scheduledHours;
	}

	public void setAssignedHours(List<Integer> assignedHours) {
		this.scheduledHours = assignedHours;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public void setWattage(int wattage) {
		this.wattage = wattage;
	}

}


public class simulatedAnnealing {

	public static ArrayList<Task> tasks = new ArrayList<Task>();
	static int shifted = 0;
	static double weight;
	public static HashMap<Integer, Double> priceMap = new HashMap<Integer, Double>();
	private static Random random = new Random();
	private static Integer[] rps = new Integer[24];
	private static Random randPrice = new Random();
	static double minPrice = Double.POSITIVE_INFINITY;
	static double minWattage = Double.POSITIVE_INFINITY;

	public static void main(String[] args) throws IOException {
		
		File appliances = new File(args[0]);
		File priceByHour = new File(args[1]);
		BufferedReader appliancesBuffered = new BufferedReader(
				new FileReader(appliances));
		BufferedReader priceBuffered = new BufferedReader(new FileReader(
				priceByHour));
		String line = null;
		double indWeightage;
		Random r1 = new Random();
		int startTime;
		int stopTime;
		int runTime;
		int watt;
		String appName;
		String con;
		while ((line = appliancesBuffered.readLine()) != null) {
			String[] applianceSplit = line.split(";");

			appName = applianceSplit[0];
			startTime = Integer.parseInt(applianceSplit[1]);
			stopTime = Integer.parseInt(applianceSplit[2]);
			runTime = Integer.parseInt(applianceSplit[3]);
			watt = Integer.parseInt(applianceSplit[4]);
			con = applianceSplit[5];

			if (con.equals("HC")) {
				indWeightage = 1;
			} else {
				indWeightage = 0.1 + (0.9 - 0.1) * r1.nextDouble();
			}
			tasks.add(new Task(appName, startTime, stopTime, runTime, watt,
					indWeightage));

		}

		line = null;
		int read = 0;
		while ((line = priceBuffered.readLine()) != null) {
			if (read == 1) {
				String[] priceSplit = line.split(",");
				for (int i = 0; i < 24; i++) {
					priceMap.put(i, Double.parseDouble(priceSplit[i + 1]));
				}
				read = 0;
			}
			if (line.contains("Start of Day Ahead Energy Price Data")) {
				read = 1;
			}
		}
		ArrayList<Task> historyList = new ArrayList<Task>();
		int iteration = 0;

		while (iteration < 100000) {
			assignRandom();
			double totalPrice = getTotalPrice();
			double totalWattage = getTotalWattage();
			if (totalPrice < minPrice) {
				minPrice = totalPrice;
				minWattage = totalWattage;
				historyList.clear();
				for (int i = 0; i < tasks.size(); i++) {
					historyList.add(new Task(tasks.get(i).getName(), tasks
									.get(i).getStartTime(), tasks.get(i)
									.getEndTime(), tasks.get(i)
									.getRuntime(), tasks.get(i)
									.getWattage(), tasks.get(i).getWeight()));
					for (int j = 0; j < tasks.get(i).scheduledHours.size(); j++) {
						historyList.get(i).scheduledHours.add(new Integer(
								tasks.get(i).scheduledHours.get(j)));
					}
				}
			}
			iteration++;
		}

		tasks.clear();
		for (int r = 0; r < historyList.size(); r++) {
			tasks.add(new Task(historyList.get(r).getName(),
					historyList.get(r).getStartTime(), historyList
							.get(r).getEndTime(), historyList.get(r)
							.getRuntime(), historyList.get(r)
							.getWattage(), historyList.get(r).getWeight()));
			for (int i = 0; i < historyList.get(r).scheduledHours.size(); i++) {
				tasks.get(r).scheduledHours.add(new Integer(historyList
						.get(r).scheduledHours.get(i)));
			}
		}

		for (int i = 0; i < 24; i++) {
			rps[i] = random.nextInt(70);
		}

		getScheduledPower();
	}

	public static void assignRandom() {
		double totalPenalty = 0;
		Iterator<Task> taskItr = tasks.iterator();
		while (taskItr.hasNext()) {
			Task currentTask = taskItr.next();
			currentTask.scheduledHours.clear();
			int randFound = 0;
			while (randFound < currentTask.getRuntime()) {
				int newRand = random.nextInt(24);
				if (!(currentTask.scheduledHours.contains(newRand))
						&& ((currentTask.stability(newRand) + totalPenalty) < 1)) {
					currentTask.scheduledHours.add(newRand);
					randFound++;
					totalPenalty += currentTask.stability(newRand);
				}
			}
		}
	}

	public static double getTotalPrice() {
		double sumPrice = 0;
		int i = 0;
		Task task;
		Iterator<Task> taskItr = tasks.iterator();
		while (taskItr.hasNext()) {
			i = randPrice.nextInt(50);
			task = taskItr.next();
			sumPrice += task.getPrice(priceMap);
		}
		return sumPrice;
	}

	public static double getTotalWattage() {
		double sumWattage = 0;
		Iterator<Task> taskItr = tasks.iterator();
		while (taskItr.hasNext()) {
			Task tsk = taskItr.next();
			sumWattage += tsk.getWattage() * tsk.getRuntime();
		}
		return sumWattage;
	}

	public static void getScheduledPower() throws IOException {
		
		int[] wattArray = new int[24];
		String[] apps = new String[24];
		for (int i = 0; i < 24; i++) {
			apps[i] = " ";
		}
		PrintWriter fpOut = new PrintWriter("scheduledPower.txt");
		Iterator<Task> taskItr = tasks.iterator();
		while (taskItr.hasNext()) {
			Task tmpTask = taskItr.next();
			for (int i = 0; i < tmpTask.scheduledHours.size(); i++) {
				wattArray[tmpTask.scheduledHours.get(i)] += tmpTask.getWattage();
				apps[tmpTask.scheduledHours.get(i)] += (tmpTask.getName() + " ");
			}
		}
		for (int i = 0; i < 24; i++)
			if (rps[i] < wattArray[i]) {
				fpOut.print((wattArray[i] - rps[i]) + ",");
			} else {
				fpOut.print(0 + ",");
			}
		fpOut.println();
		for (int i = 0; i < 24; i++) {
			fpOut.println(i + "\t" + apps[i]);
		}

		fpOut.close();
	}

}