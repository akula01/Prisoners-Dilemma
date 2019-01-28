import java.util.LinkedList;

public class Strategy4History extends Strategy {

	/* Encoding for a strategy. */

	// 0 = defect, 1 = cooperate
	
	private LinkedList<Integer> queue = new LinkedList<Integer>(); 
	private int len_history;
	//private Chromo strategy;
	private double[] decision = {0.43, 0.43, 0.46, 0.40,
			0.49, 0.35, 0.44, 0.37, 0.45, 0.48, 0.23,
			0.56, 0.41, 0.30, 0.36, 0};
	
	/*public Strategy(int len_history, Chromo strategy) {
		name = "Evolving Strategy";
		this.len_history = len_history;
		this.strategy = strategy;
		for(int i=0; i < len_history; i++) {
			if(Math.random() < 0.5) {
				queue.add(0);
			}else {
				queue.add(1);
			}	
		}
	}*/

	public Strategy4History() {
		name = "Monitor 4 histories";

		for (int i = 0; i < 4; i++) {
			if (Math.random() < 0.5)
				queue.add(0);
			else
				queue.add(1);
		}
	}
	
	private int get_choice() {
		int choice = 0;
		for(int i=0; i<len_history; i++) {
			choice += queue.get(i) * Math.pow(2, i);
		}
		return choice;		
	}
		

	public int nextMove() {
		int choice = this.get_choice();
		int nextMove = 0;
		double probability = this.decision[choice];

		if(Math.random() > probability) {
			nextMove = 1;
		}
		
		//Uncommented by Yu Zou - 2018.03.19
//		// Update history with the next move
//		this.queue.remove();
//		this.queue.add(nextMove);
		
		return nextMove;
	}
	
	//Added by Yu Zou - 2018.03.19
	public void saveOpponentMove(int move) {
		this.opponentLastMove = move;
		//Update history with opponent's last move
		this.queue.remove();
		this.queue.add(this.opponentLastMove);
	}
}
