import java.util.LinkedList;

public class EvolvingStrategy extends Strategy {

	/* Encoding for a strategy. */

	// 0 = defect, 1 = cooperate
	
	private LinkedList<Integer> queue = new LinkedList<Integer>(); 
	private int len_history;
	private Chromo strategy;
	
	public EvolvingStrategy(int len_history, Chromo strategy) {
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
	} 
	
	private int get_choice() {
		assert this.queue.size() == this.len_history;
		int choice = 0;
		for(int i=0; i<len_history; i++) {
			choice += queue.get(i) * Math.pow(2, i);
		}
		return choice;		
	}
		

	public int nextMove() {
		int choice = this.get_choice();
		int nextMove = 0;
		double probability = this.strategy.chromo[choice];

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
