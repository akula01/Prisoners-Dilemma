/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public double [] chromo;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){

		//  Set gene values to a randum sequence of 1's and 0's
		char geneBit;
		chromo = new double [Parameters.numGenes];

		for (int i=0; i<Parameters.numGenes; i++){
			randnum = Search.r.nextDouble();
			chromo[i] = randnum;
		}

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Get Alpha Represenation of a Gene **************************************

//	public String getGeneAlpha(int geneID){
//		int start = geneID * Parameters.geneSize;
//		int end = (geneID+1) * Parameters.geneSize;
//		String geneAlpha = this.chromo.substring(start, end);
//		return (geneAlpha);
//	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment) ****

//	public int getIntGeneValue(int geneID){
//		String geneAlpha = "";
//		int geneValue;
//		char geneSign;
//		char geneBit;
//		geneValue = 0;
//		geneAlpha = getGeneAlpha(geneID);
//		for (int i=Parameters.geneSize-1; i>=1; i--){
//			geneBit = geneAlpha.charAt(i);
//			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
//		}
//		geneSign = geneAlpha.charAt(0);
//		if (geneSign == '1') geneValue = geneValue - (int)Math.pow(2.0, Parameters.geneSize-1);
//		return (geneValue);
//	}

	//  Get Integer Value of a Gene (Positive only) ****************************

//	public int getPosIntGeneValue(int geneID){
//		String geneAlpha = "";
//		int geneValue;
//		char geneBit;
//		geneValue = 0;
//		geneAlpha = getGeneAlpha(geneID);
//		for (int i=Parameters.geneSize-1; i>=0; i--){
//			geneBit = geneAlpha.charAt(i);
//			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
//		}
//		return (geneValue);
//	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		double [] mutChromo = new double[Parameters.numGenes];

		switch (Parameters.mutationType){

		case 1:     //  Replace with new random number

			// TODO: look up Gaussian mutation

			for (int j=0; j<(Parameters.numGenes); j++){
				randnum = Search.r.nextDouble();
				double change = Search.r.nextDouble() / 10;
				boolean negative = Search.r.nextBoolean();

				if (randnum < Parameters.mutationRate){
					// TODO: implement Gaussian mutation here
					if (negative) change *= -1;
					mutChromo[j] = Math.min(Math.max(chromo[j] + change, 0),1);
				} else {
					mutChromo[j] = chromo[j];
				}
			}

			this.chromo = mutChromo;
			break;

		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		double k = 0.9;
		
		double rand;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     //  Tournament Selection
			rand = Search.r.nextDouble();
			int randComp1 = Search.r.nextInt(Parameters.popSize);
			int randComp2 = Search.r.nextInt(Parameters.popSize);
			if (rand < k) {
				if (Search.member[randComp1].proFitness < Search.member[randComp2].proFitness)
					return randComp2;
				else
					return randComp1;
			} else {
				if (Search.member[randComp1].proFitness < Search.member[randComp2].proFitness)
					return randComp1;
				else
					return randComp2;
			}
		case 4:		//	 Rank Selection
			//To implement rank selection, scale type must be 3 or 4
			if (Parameters.scaleType == 0 || Parameters.scaleType == 1) {
				System.out.println("Error - to use rank selection, please change scale type to rank 2 or 3");
				return -1;
			} else {
				//Normal proportional selection
				rand = Search.r.nextDouble();
				for (j = 0; j < Parameters.popSize; j++) {
					rWheel = rWheel + Search.member[Search.memberIndex[j]].proFitness;
					if (rand < rWheel)
						return Search.memberIndex[j];
				}
			}
			break;
		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
		int xoverPoint2;

		switch (Parameters.xoverType){

		case 1:     //  Single Point Crossover

			//  Select crossover point
			// TODO: assert this is correct
			xoverPoint1 = (int)(Search.r.nextDouble() * (Parameters.numGenes));

			double [] child1Array = new double[Parameters.numGenes];
			double [] child2Array = new double[Parameters.numGenes];


			for (int i=0; i<Parameters.numGenes; i++){
				if(i < xoverPoint1){
					// left half of child1 is average of both parents
					child1Array[i] = (parent1.chromo[i] + parent2.chromo[i]) / 2;
					//child1Array[i] = parent1.chromo[i];
					// left half of child2 is the same as parent2
					child2Array[i] = parent2.chromo[i];
				} else {
					// right half of child1 is the same as parent1
					child1Array[i] = parent2.chromo[i];
					// right half of child2 is the average of both parents
					child2Array[i] = (parent1.chromo[i] + parent2.chromo[i]) / 2;
				}
			}

			//  Create child chromosome from parental material
			child1.chromo = child1Array;
			child2.chromo = child2Array;
			break;

		case 2:     //  Two Point Crossover

		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		//Modified by Yu Zou
//		targetA.chromo = sourceB.chromo;
		System.arraycopy(sourceB.chromo, 0, targetA.chromo, 0, sourceB.chromo.length);

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

}   // End of Chromo.java ******************************************************
