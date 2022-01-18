import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;

public class Algorithm extends Thread {

	private Grid grid;
	private JPanel panel;
	private boolean solutionFound = false;

	public Algorithm(Grid grid, JPanel panel) {
		this.grid = grid;
		this.panel = panel;
	}

	@Override
	public void run() {
		if (MyUtils.solving) {
			MyUtils.breakAlgo = false;
			switch (MyUtils.algorithm) {
			case 0:
				bfs(grid.getStart());
				break;
			case 1:
				dfs(grid.getStart());
				break;
			case 2:
				best(grid.getStart());
				break;
			case 3:
				astar(grid.getStart());
				break;
			}
		}
		MyUtils.solving = false;
		if (MyUtils.breakAlgo) {
			grid.initialiseGrid();
		}
		panel.repaint();
	}

	private void astar(Node start) {
		LinkedList<Node> queue = new LinkedList<>();
		queue.add(start);
		start.setAlreadyVisited(true);

		while (MyUtils.solving && !solutionFound && !queue.isEmpty()) {
			Node current = queue.pollFirst();
			current.setType(2);
			panel.repaint();
			delay(MyUtils.delay);

			if (current.equals(grid.getFinish())) {
				MyUtils.solving = false;
				solutionFound = true;
				extractSolution(current);
				return;
			} else {
				current.setType(1);
				for (Node child : current.getNeighbors(grid)) {
					queue.add(child);
					child.setAlreadyVisited(true);
					child.setType(4);
				}
				queue.sort(new Comparator<Node>() {
					public int compare(Node n1, Node n2) {
						if (n1.equals(n2)) {
							return 0;
						}
						if (n1.getF() > n2.getF()) {
							return 1;
						} else if (n1.getF() < n2.getF()) {
							return -1;
						} else {
							return 0;
						}
					}
				});
			}

		}
	}

	private void best(Node start) {
		LinkedList<Node> queue = new LinkedList<>();
		queue.add(start);
		start.setAlreadyVisited(true);

		while (MyUtils.solving && !solutionFound && !queue.isEmpty()) {
			Node current = queue.poll();
			current.setType(2);
			panel.repaint();
			delay(MyUtils.delay);

			if (current.equals(grid.getFinish())) {
				MyUtils.solving = false;
				solutionFound = true;
				extractSolution(current);
				return;
			} else {
				current.setType(1);
				for (Node child : current.getNeighbors(grid)) {
					queue.add(child);
					child.setAlreadyVisited(true);
					child.setType(4);
				}
				queue.sort(new Comparator<Node>() {
					public int compare(Node n1, Node n2) {
						if (n1.equals(n2)) {
							return 0;
						}
						if (n1.getH() > n2.getH()) {
							return 1;
						} else if (n1.getH() < n2.getH()) {
							return -1;
						} else {
							return 0;
						}
					}
				});
			}

		}
	}

	private void dfs(Node start) {
//		dfsUntill(start);

		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addLast(start);
		start.setAlreadyVisited(true);

		while (MyUtils.solving && !solutionFound && !queue.isEmpty()) {
			Node current = queue.pollLast();
			current.setType(2);
			panel.repaint();
			delay(MyUtils.delay);

			if (current.equals(grid.getFinish())) {
				MyUtils.solving = false;
				solutionFound = true;
				extractSolution(current);
				return;
			} else {
				current.setType(1);
				for (Node child : current.getNeighbors(grid)) {
					queue.addLast(child);
					child.setAlreadyVisited(true);
					child.setType(4);
				}
			}
		}

	}

//	private void dfsUntill(Node node) {
//		if (!MyUtils.solving || solutionFound) {
//			return;
//		}
//		node.setType(2);
//		node.setAlreadyVisited(true);
//		panel.repaint();
//		delay(MyUtils.delay);
//
//		if (node.equals(grid.getFinish())) {
//			MyUtils.solving = false;
//			solutionFound = true;
//			extractSolution(node);
//			return;
//		} else {
//			node.setType(1);
//			for (Node child : node.getNeighbors(grid)) {
//				dfsUntill(child);
//			}
//
//		}
//
//	}

	private void bfs(Node startingNode) {
		Queue<Node> frontier = new LinkedList<Node>();
		Node currentNode = null;
		frontier.add(startingNode);

		while (MyUtils.solving && !frontier.isEmpty() && !solutionFound) {
			currentNode = frontier.poll();
			currentNode.setType(2);
			currentNode.setAlreadyVisited(true);
			panel.repaint();
			delay(MyUtils.delay);

			if (currentNode.equals(grid.getFinish())) {
				MyUtils.solving = false;
				extractSolution(currentNode);
				continue;
			} else {
				currentNode.setType(1);
				for (Node neighbor : currentNode.getNeighbors(grid)) {
					frontier.add(neighbor);
					neighbor.setType(4);
					neighbor.setAlreadyVisited(true);
				}
			}

		}
	}

	public void extractSolution(Node node) {
		Node parent = node.getParent();

		while (parent != null) {
			parent.setType(5);
			panel.repaint();
			delay(10);
			parent = parent.getParent();
		}
		panel.repaint();
	}

	public void delay(int delay) {
		try {
			Thread.sleep(delay);
			panel.repaint();
		} catch (InterruptedException e) {
		}
	}
}
