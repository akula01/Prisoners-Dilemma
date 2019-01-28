
/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class PrisonersDilemmaProblem extends FitnessFunction {

	/*******************************************************************************
	 * INSTANCE VARIABLES *
	 *******************************************************************************/

	/*******************************************************************************
	 * STATIC VARIABLES *
	 *******************************************************************************/

	/*******************************************************************************
	 * CONSTRUCTORS *
	 *******************************************************************************/

	public PrisonersDilemmaProblem() {
		name = "Prisoners Dilemma Problem";
	}

	/*******************************************************************************
	 * MEMBER METHODS *
	 *******************************************************************************/

	// COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitnessTest(Chromo X, Chromo[] Y, int maxSteps) {
		EvolvingStrategy player1 = new EvolvingStrategy(4, X);
		int fitness = 0;
		for(Chromo y : Y) {
			StrategyTitForTat player2 = new StrategyTitForTat();
			IteratedPD ipd = new IteratedPD(player1, player2);
			ipd.runSteps(maxSteps);
			fitness += ipd.player1Score();	
		}
		X.rawFitness = fitness;
	}

	// PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE
	// *********************************

//	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException {
//
//		for (int i = 0; i < Parameters.numGenes; i++) {
//			Hwrite.right(X.getGeneAlpha(i), 11, output);
//		}
//		output.write("   RawFitness");
//		output.write("\n        ");
//		for (int i = 0; i < Parameters.numGenes; i++) {
//			Hwrite.right(X.getPosIntGeneValue(i), 11, output);
//		}
//		Hwrite.right((int) X.rawFitness, 13, output);
//		output.write("\n\n");
//		return;
//	}

	/*******************************************************************************
	 * STATIC METHODS *
	 *******************************************************************************/

} // End of OneMax.java ******************************************************
