import java.util.*;

public class Host implements Comparable<Host> {
	
	private String hostName;
	private ArrayList<Integer> times;
	private ArrayList<Integer> amounts; 
	private String[] split;
	private int weight;
	
	public Host(String hostName){
		this.hostName = hostName;
		split = this.hostName.split("\\.");
		for (int i = 0; i < split.length; i++)
			weight += Integer.parseInt(split[i]) * Math.pow(10.0, 3 - i);
		times = new ArrayList<Integer>();
		amounts = new ArrayList<Integer>();
	}
	
	public String getHost(){
		return hostName;
	}
	
	public Integer getTime(){
		return times.get(times.size()-1); 
	}
	
	public Integer getMaxAmount(){
		return Collections.max(amounts);
	}
	
	public ArrayList<Integer> getTimes(){
		return times;
	}
	
	public ArrayList<Integer> getAmounts(){
		return amounts;
	}

	public void appendTimeAndAmount(String time, String amount){
		int t = (int) (Double.parseDouble(time));
		int a = Integer.parseInt(amount);
		int s = 0;
		Integer i = 0;
		if (times.isEmpty()){
			times.add(t);
			amounts.add(a);
		}
		else if ( t % 2 == 1){
			s = amounts.size() - 1;
			i = amounts.get(s);
			amounts.set(s, i + a);
		}
		else if (times.contains(t)){
			s = amounts.size() - 1;
			i = amounts.get(s);
			amounts.set(s, i + a);
		}
		else {
			times.add(t);
			amounts.add(a);
		}
	}
	
	public int hashCode(){
		return weight;
	}
	
	public boolean equals(Object other){
		if ( other instanceof Host){
			Host o = (Host) other;
			return this.getHost().equals(o.getHost());
		}
		return false;
	}

	public int compareTo(Host other){
		return weight - other.weight;
	}
}