
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {
		
		Scanner sc = new Scanner(System.in);
		String aString = sc.nextLine();     // input the current state 
		String fgoal = sc.nextLine(); // input the goal state 
		
		//doFB(aString,fgoal);
		//doGreedy(aString,fgoal);
		//doAstar(aString,fgoal);
		//doIDS(aString, fgoal);
		doIDAstar(aString, fgoal);
		System.out.print("-- END --");
				
	}
	
	
	private static void doIDAstar(String aString, String fgoal) {
		IDAstar sss = new IDAstar();
		
		Iterator<IDAstar.Node> it = sss.solve(new SSP(aString), new SSP(fgoal));
		
		if (it == null)
			System.out.println("Uniform cost(IDAstar), no solution found");
		else {
			while (it.hasNext()) {
				IDAstar.Node node = it.next();
				// System.out.println(node.toString()); // to print the solution
				if (!it.hasNext())
					System.out.println("Uniform cost(IDAstar), "+node.getG()); // to print the solution cost only
				  //  System.out.println(node.getG())
				
			}
		}
		
	}
	public static void doFB(String aString, String fgoal){

		BestFirst sss = new BestFirst();
		
		Iterator<BestFirst.Node> it = sss.solve(new SSP(aString), new SSP(fgoal));
		
		if (it == null)
			System.out.println("Uniform cost(Bestfirst), no solution found");
		else {
			while (it.hasNext()) {
				BestFirst.Node node = it.next();
				// System.out.println(node.toString()); // to print the solution
				if (!it.hasNext())
					//System.out.println("Uniform cost(Bestfirst), "+node.getG()); // to print the solution cost only
				  System.out.println(node.getG());
				
			}
		}
	}
	public static void doGreedy(String aString, String fgoal){
		
		Greedy sss = new Greedy();
		
		Iterator<Greedy.Node> it = sss.solve(new SSP(aString), new SSP(fgoal));
		
		if (it == null)
			System.out.println("Greedy, no solution found");
		else {
			while (it.hasNext()) {
				Greedy.Node node = it.next();
				// System.out.println(node.toString()); // to print the solution
				if (!it.hasNext())
					System.out.println("Greedy "+node.getG()); // to print the solution cost only
			//  System.out.println(node.getG())
				
			}
		}
	}
	public static void doAstar(String aString, String fgoal){
		
		Astar sss = new Astar();
		
		Iterator<Astar.Node> it = sss.solve(new SSP(aString), new SSP(fgoal));
		
		if (it == null)
			System.out.println("Astar, no solution found");
		else {
			while (it.hasNext()) {
				Astar.Node node = it.next();
				// System.out.println(node.toString()); // to print the solution
				if (!it.hasNext())
					System.out.println("Astar, "+node.getG()); // to print the solution cost only
			//  System.out.println(node.getG())
				
			}
		}	
	}
	public static void doIDS(String aString, String fgoal){
		
		IDS sss = new IDS();
		int limit=0;
		Iterator<IDS.Node> it = null;
		do{
			limit++;
			it = sss.solve(new SSP(aString), new SSP(fgoal), limit);
			
		}while (it == null && limit<100);
		
		
		if (it == null)
			System.out.println("IDS, no solution found");
		else {
			while (it.hasNext()) {
				IDS.Node node = it.next();
				// System.out.println(node.toString()); // to print the solution
				if (!it.hasNext())
					System.out.println("IDS, "+node.getG()); // to print the solution cost only
			//  System.out.println(node.getG())
				
			}
		}	
	}


	public static void main(String[] args) throws Exception {
		
		Scanner sc = new Scanner(System.in);
		String aString = sc.nextLine();     // input the current state 
		String fgoal = sc.nextLine(); // input the goal state 
		
		//doFB(aString,fgoal);
		//doGreedy(aString,fgoal);
		//doAstar(aString,fgoal);
		//doIDS(aString, fgoal);
		doIDAstar(aString, fgoal);
		System.out.print("-- END --");
				
	}
    
}

import java.util.List;




interface IState { 
	 
	 /** 
	 @return the children of the receiver. 
	 */ 
	 List<IState> children(); 
	 
	 /** 
	 @return true if the receiver equals the argument l; return false otherwise. 
	 */ 
	 boolean isGoal(IState l); 
	 
	/** 
	 @return the cost from the receiver to its father. 
	 */ 
	int getCost(); 
	}


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;



class BestFirst {
	public class Node {
		private IState state;
		private Node father;
		private int g; // cost from the current to the initial node

		public Node(IState l, Node n) {
			state = l;
			father = n;
			g = l.getCost();
			if (father != null)
				g = g + father.g;
			
		}

		public IState getState() {
			return state;
		}

		public String toString() {
			return "(" + state.toString() + ", " + g + ") ";
		}

		public int getG() {
			return g;
		}
	}

	private Queue<Node> abertos;
	private List<Node> fechados;
	private Node actual;
	private IState objective;

	final private List<Node> sucessores(Node n) {
		
		List<Node> sucs = new ArrayList<Node>();
		
		List<IState> the_children = n.state.children();
		
		// System.out.println("No of C="+the_children.size());
		
		for (IState e : the_children) {
			if (n.father == null || !e.equals(n.father.state)) {
				// System.out.println("there is C="+n.father);
				Node nn = new Node(e, n);
				sucs.add(nn);
			}
			// System.out.println("there is No C="+n.father);
		}
		
		return sucs;
	}

	final public Iterator<Node> solve(IState s, IState goal) {
		
		objective = goal;
		List<Node> fechados = new ArrayList<Node>();
		// Queue<Node> abertos = new PriorityQueue<Node>(10, new NodeComparator());
		Queue<Node> abertos = new PriorityQueue<Node>(30, new NodeComparator());
		abertos.add(new Node(s, null));
		// System.out.println("STATE="+s);
		// System.out.println("STATEabertos="+abertos.size());
		List<Node> sucs;
		for (;;) {
			if (abertos.isEmpty()) {
				// System.out.println("null");
				return null;
			}
			actual = abertos.poll();
			if (goal.isGoal(actual.state)) {
				class IT implements Iterator<Node> {
					private Node last;
					private Stack<Node> stack;

					public IT() {
						last = actual;
						stack = new Stack<Node>();
						while (last != null) {
							stack.push(last);
							last = last.father;
						}
					}

					public boolean hasNext() {
						return !stack.empty();
					}

					public Node next() {
						return stack.pop();
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				}
				return new IT();
			} else {
				// System.out.println("else"); 
				sucs = sucessores(actual);
				fechados.add(actual);
				boolean contains;
				for (Node e : sucs) {
					contains = false;
					for (Node f : fechados)
						if (f.state.equals(e.state))
							contains = true;
					if (!contains)
						abertos.add(e);
				}
			}
		}
	}
}

class NodeComparator implements Comparator<BestFirst.Node> {

	public int compare(BestFirst.Node s1, BestFirst.Node s2) {
		// System.out.println("gost 1 and 2 = "+s1.getG()+" "+s2.getG());
		if (s1.getG() > s2.getG())
			return 1;
		else if (s1.getG() == s2.getG())
			return 0;
		else
			return -1;
	}
}


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;



class Greedy {
	public class Node {
		private int h; // step from goal
		private IState state;
		private Node father;
		private int g; // cost from the current to the initial node

		public Node(IState l, Node n) {
			state = l;
			father = n;
			g = l.getCost();
			if (father != null)
				g += father.g;
			
		}
		
		// to set the h (the step from the goal) for current node 
		public void set_h(Node ns){
			for (int ind=0;ind<objective.toString().length();ind++){
				if ( objective.toString().charAt(ind) != ns.toString().charAt(ind) ){
					h=h+1;
				}
									
			}
						 
		}

		public IState getState() {
			return state;
		}

		public String toString() {
			return "(" + state.toString() + ", " + g + ") ";
		}

		public int getG() {
			return g;
		}
	}

	private Queue<Node> abertos;
	private List<Node> fechados;
	private Node actual;
	private IState objective;

	final private List<Node> sucessores(Node n) {
		
		List<Node> sucsP = new ArrayList<Node>();
		List<Node> sucs = new ArrayList<Node>();
		
		List<IState> the_children = n.state.children();
		
		// System.out.println("No of C="+the_children.size());
		int hh=0,s_h=0;
		
		for (IState e : the_children) {
			               
			if (n.father == null || !e.equals(n.father.state)) {
				// System.out.println("there is C="+n.father);
				Node nn = new Node(e, n);
				nn.set_h(nn); //          this will set h for each child 
				sucsP.add(nn);  // all the children
				
			}
			
			
			// System.out.println("there is No C="+n.father);
		}
		s_h=smallest_h(sucsP); // find smallest h
		// System.out.println("smallest h "+s_h+"\t sucsP size "+sucsP.size());
		// make a list with only smallest h		
		for(int index=0; index<sucsP.size();index++){
			if(sucsP.get(index).h==s_h){
				Node nnn=sucsP.get(index);
				// System.out.println("\t\t\t\t "+nnn.getState());
				sucs.add(nnn);   
			}
				
		}
		
		return sucs;
	}

	
// find the smallest h from children list
	private int smallest_h(List<Node> sucsP) {

		int sh=1000;
		for (int index=0;index<sucsP.size();index++){
			// System.out.println(" hhh "+sucsP.get(index).h);
			if (sh>sucsP.get(index).h )
				sh=sucsP.get(index).h;
		}
		return sh;
	}



	final public Iterator<Node> solve(IState s, IState goal) {
		
		objective = goal;
		List<Node> fechados = new ArrayList<Node>();
		// Queue<Node> abertos = new PriorityQueue<Node>(10, new NodeComparator());
		Queue<Node> abertos = new PriorityQueue<Node>(30, new NodeComparatorG());
		abertos.add(new Node(s, null));
		// System.out.println("STATE="+s);
		// System.out.println("STATEabertos="+abertos.size());
		List<Node> sucs;
		for (;;) {
						
			if (abertos.isEmpty()) {
				 // System.out.println("null");
				return null;
			}
			actual = abertos.poll();
			if (goal.isGoal(actual.state)) {
				class IT implements Iterator<Node> {
					private Node last;
					private Stack<Node> stack;

					public IT() {
						last = actual;
						stack = new Stack<Node>();
						while (last != null) {
							stack.push(last);
							last = last.father;
						}
					}

					public boolean hasNext() {
						return !stack.empty();
					}

					public Node next() {
						return stack.pop();
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				}
				return new IT();
			} else {
				// System.out.println("else"); 
				sucs = sucessores(actual);
				fechados.add(actual);
				boolean contains;
				for (Node e : sucs) {
					contains = false;
					for (Node f : fechados)
						if (f.state.equals(e.state))
							contains = true;
					if (!contains)
						abertos.add(e);
				}
			}
		}
	}
}

class NodeComparatorG implements Comparator<Greedy.Node> {

	public int compare(Greedy.Node s1, Greedy.Node s2) {
		if (s1.getG() > s2.getG())
			return 1;
		else if (s1.getG() == s2.getG())
			return 0;
		else
			return -1;
	}
}

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;



class Astar {
	public class Node {
		private int f; // f=g+h
		private int h; // step from goal
		private IState state;
		private Node father;
		private int g; // cost from the current to the initial node

		public Node(IState l, Node n) {
			state = l;
			father = n;
			g = l.getCost();
			if (father != null)
				g += father.g;
			
		}
		
		// to set the h (the step from the goal) for current node 
		public void set_h(Node ns){
			for (int ind=0;ind<objective.toString().length();ind++){
				if ( objective.toString().charAt(ind) != ns.toString().charAt(ind) ){
					h=h+1;
				}
									
			}
						 
		}
		public IState getState() {
			return state;
		}
		public String toString() {
			return "(" + state.toString() + ", " + g + ") ";
		}
		public int getG() {
			return g;
		}
		public int getF() {
			return g+h;
		}
	}

	private Queue<Node> abertos;
	private List<Node> fechados;
	private Node actual;
	private IState objective;

	final private List<Node> sucessores(Node n) {
		
		List<Node> sucsP = new ArrayList<Node>();
		List<Node> sucs = new ArrayList<Node>();
		
		List<IState> the_children = n.state.children();
		
		// System.out.println("No of C="+the_children.size());
				
		for (IState e : the_children) {
			               
			if (n.father == null || !e.equals(n.father.state)) {
				// System.out.println("there is C="+n.father);
				Node nn = new Node(e, n);
				nn.set_h(nn); //          this will set h for each child 
				sucsP.add(nn);  // all the children
				
			}
			
			
			// System.out.println("there is No C="+n.father);
		}
				
		return sucsP;
	}


	final public Iterator<Node> solve(IState s, IState goal) {
		
		objective = goal;
		List<Node> fechados = new ArrayList<Node>();
		// Queue<Node> abertos = new PriorityQueue<Node>(10, new NodeComparator());
		Queue<Node> abertos = new PriorityQueue<Node>(30, new NodeComparatorA());
		abertos.add(new Node(s, null));
		// System.out.println("STATE="+s);
		// System.out.println("STATEabertos="+abertos.size());
		List<Node> sucs;
		for (;;) {
						
			if (abertos.isEmpty()) {
				 // System.out.println("null");
				return null;
			}
			actual = abertos.poll();
			if (goal.isGoal(actual.state)) {
				class IT implements Iterator<Node> {
					private Node last;
					private Stack<Node> stack;

					public IT() {
						last = actual;
						stack = new Stack<Node>();
						while (last != null) {
							stack.push(last);
							last = last.father;
						}
					}

					public boolean hasNext() {
						return !stack.empty();
					}

					public Node next() {
						return stack.pop();
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				}
				return new IT();
			} else {
				// System.out.println("else"); 
				sucs = sucessores(actual);
				fechados.add(actual);
				boolean contains;
				for (Node e : sucs) {
					contains = false;
					for (Node f : fechados)
						if (f.state.equals(e.state))
							contains = true;
					if (!contains)
						abertos.add(e);
				}
			}
		}
	}
}

class NodeComparatorA implements Comparator<Astar.Node> {

	public int compare(Astar.Node s1, Astar.Node s2) {
		if (s1.getF() > s2.getF())
			return 1;
		else if (s1.getF() == s2.getF())
			return 0;
		else
			return -1;
	}
}


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;



class IDS {
	public class Node {
		private IState state;
		private Node father;
		private int g; // cost from the current to the initial node

		public Node(IState l, Node n) {
			state = l;
			father = n;
			g = l.getCost();
			if (father != null)
				g = g + father.g;
			
		}

		public IState getState() {
			return state;
		}

		public String toString() {
			return "(" + state.toString() + ", " + g + ") ";
		}

		public int getG() {
			return g;
		}
	}

	private Queue<Node> abertos;
	private List<Node> fechados;
	private Node actual;
	private IState objective;

	final private List<Node> sucessores(Node n) {
		
		List<Node> sucs = new ArrayList<Node>();
		
		List<IState> the_children = n.state.children();
		
		// System.out.println("No of C="+the_children.size());
		
		for (IState e : the_children) {
			if (n.father == null || !e.equals(n.father.state)) {
				// System.out.println("there is C="+n.father);
				Node nn = new Node(e, n);
				sucs.add(nn);
			}
			// System.out.println("there is No C="+n.father);
		}
		
		return sucs;
	}

	final public Iterator<Node> solve(IState s, IState goal, int limit) {
		
		objective = goal;

			
			List<Node> fechados = new ArrayList<Node>();
			// Queue<Node> abertos = new PriorityQueue<Node>(10, new NodeComparator());
			Stack<Node> abertos = new Stack<Node>();
			abertos.push(new Node(s, null));
			// System.out.println("STATE="+s);
			// System.out.println("STATEabertos="+abertos.size());
			List<Node> sucs;
			for (;;) {
				if (abertos.empty()) {
					// System.out.println("null");
					return null;
				}
				actual = abertos.pop();
				if (goal.isGoal(actual.state)) {
					class IT implements Iterator<Node> {
					private Node last;
					private Stack<Node> stack;

					public IT() {
						last = actual;
						stack = new Stack<Node>();
						while (last != null) {
							stack.push(last);
							last = last.father;
						}
					}

					public boolean hasNext() {
						return !stack.empty();
					}

					public Node next() {
						return stack.pop();
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				}
					return new IT();
				} else if (actual.getG() < limit){
					// System.out.println("else"); 
					sucs = sucessores(actual);
					fechados.add(actual);
					boolean contains;
					for (Node e : sucs) {
						contains = false;
						for (Node f : fechados)
							if (f.state.equals(e.state))
								contains = true;
						if (!contains)
							abertos.push(e);
					}
				}
			}
		}
		
}



import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;





import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;




class IDAstar {
	public class Node {
		private IState state;
		private Node father;
		private int g; // cost from the current to the initial node
		private int f;
		private int h;
		
		
		public Node(IState l, Node n) {
			state = l;
			father = n;
			g = l.getCost();
			if (father != null)
				g = g + father.g;
			
		}

		public void set_h(Node ns){
			for (int ind=0;ind<objective.toString().length();ind++){
				if ( objective.toString().charAt(ind) != ns.toString().charAt(ind) ){
					h=h+1;
				}
									
			}
						 
		}
		
		public IState getState() {
			return state;
		}

		public String toString() {
			return "(" + state.toString() + ", " + g + ") ";
		}

		public int getG() {
			return g;
		}
		public int getF(){
			return g+h;
		}
	}

	private Queue<Node> abertos;
	private List<Node> fechados;
	private Node actual;
	private IState objective;
	

	final private List<Node> sucessores(Node n) {
		
		List<Node> sucs = new ArrayList<Node>();
		
		List<IState> the_children = n.state.children();
		
		// System.out.println("No of C="+the_children.size());
		
		for (IState e : the_children) {
			if (n.father == null || !e.equals(n.father.state)) {
				// System.out.println("there is C="+n.father);
				Node nn = new Node(e, n);
				nn.set_h(nn);
				sucs.add(nn);
			}
			// System.out.println("there is No C="+n.father);
		}
		
		return sucs;
	}

	final public Iterator<Node> solve(IState s, IState goal) {
		
		objective = goal;
		Node limitNode = new Node(s,null);
		int limit = limitNode.getF();
		
			
			List<Node> fechados = new ArrayList<Node>();
			// Queue<Node> abertos = new PriorityQueue<Node>(10, new NodeComparator());
			Stack<Node> abertos = new Stack<Node>();
			abertos.push(new Node(s, null));
			// System.out.println("STATE="+s);
			// System.out.println("STATEabertos="+abertos.size());
						
			List<Node> sucs;
			for (;;) {
				if (abertos.empty()) {
					// System.out.println("null");
					return null;
				}
				actual = abertos.pop();
				if (goal.isGoal(actual.state)) {
					class IT implements Iterator<Node> {
					private Node last;
					private Stack<Node> stack;

					public IT() {
						last = actual;
						stack = new Stack<Node>();
						while (last != null) {
							stack.push(last);
							last = last.father;
						}
					}

					public boolean hasNext() {
						return !stack.empty();
					}

					public Node next() {
						return stack.pop();
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				}
					return new IT();
				} else if (actual.getF() <= limit){
					// System.out.println("else"); 
					sucs = sucessores(actual);
					fechados.add(actual);
					boolean contains;
					for (Node e : sucs) {
						contains = false;
						for (Node f : fechados)
							if (f.state.equals(e.state))
								contains = true;
						if (!contains)
							abertos.push(e);
					}
				}
			}
			
			
			
		
	}

	public int smallest_f(Stack<Node> abertos2) {
		int sf=1000;
		for (int index=0;index<abertos2.size();index++){
			
			if (sf>abertos2.get(index).f)
				sf=abertos2.get(index).f;
		}
		return sf;
		
	}
		
}


