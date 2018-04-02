package main;

import moves.*;

import java.util.ArrayList;
import java.util.List;

public class TreeOfGame {

    private List<TreeOfGame> trees = new ArrayList<TreeOfGame>();
    private ArrayList<Move> previousMove;
    private ArrayList<ArrayList<Move>> moves;
    private Game currentGame;
    private int wins = 0;
    private int simulations = 0;
    private double rate=100;

    
    public int getPlayouts()
    {
    	return simulations;
    }
    
    public int getMaxDeep()
    {
    	int deep=0;
    	if(!trees.isEmpty())
    	{
    		for( int i=0;i<trees.size();i++)
        	{
        		int tempDeep=trees.get(i).getMaxDeep()+1;
        		if(tempDeep>deep)
        			deep=tempDeep;
        	}
    	}
    	return deep;
    }
    
    public int getAverageDeep()
    {
    	int[] average=getAverageDeepAndNumberOfLeafs(0);
    	return average[0]/average[1];
    }
    
    private int[] getAverageDeepAndNumberOfLeafs(int currentDeep)
    {
    	int[] average=new int[2];
    	if(!trees.isEmpty())
    	{
    		int tempAverage=0;
    		for(int i=0;i<trees.size();i++)
        	{
    			int[] averageOfTree = trees.get(i).getAverageDeepAndNumberOfLeafs(currentDeep+1);
        		average[0]+=averageOfTree[0];
        		average[1]+=averageOfTree[1];
        	}
    	}
    	else
    	{
    		average[0]=currentDeep;
    		average[1]=1;
    	}
    	return average;
    }
    
    
    public ArrayList<Move> calculateBestMove(long maxTime, int player, float c_param, int simulateBestOf_param) {
        long endTime = System.currentTimeMillis() + maxTime;

        while (endTime > System.currentTimeMillis()) {
            mcts(player, c_param, simulateBestOf_param);
            rate = wins / simulations + c_param*Math.sqrt(Math.log1p(simulations) / simulations);
            //System.out.println("ocena "+rate+" "+trees.get(0).rate+" "+trees.size()+" "+getMaxDeep());
        }
        //System.out.println(trees.get(0).trees.size());
        //System.out.println("mcts: "+rate+" "+trees.get(0).rate+" "+trees.size()+" "+getMaxDeep()+" "+moves.size());
        
        return getBestMove();
    }

    private ArrayList<Move> getBestMove() {
        ArrayList<Move> bestMove = null;
        double rate = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < trees.size(); i++) {
        	//System.out.println(trees.get(i).simulations + " " + trees.get(i).wins);
            double tempRate = trees.get(i).wins / trees.get(i).simulations;
            if (tempRate > rate) {
                rate = tempRate;
                bestMove = trees.get(i).previousMove;
            }
        }
        /*System.out.println("---");
        System.out.println("MCTS"+trees.size());
        for(Move m : bestMove)
        {
        	System.out.println(m.toString());
        }
        System.out.println("---");*/
        return bestMove;
    }

    private int mcts(int player, float c_param, int simulateBestOf) {
        TreeOfGame currentTree;
        int result=0;
        if ((currentTree = selection(c_param)).equals(this)) {
        	TreeOfGame expansionedTree = expansion();
            int winner = simulate(expansionedTree.currentGame.clone(), simulateBestOf);
            //System.out.println("Wygra³em: "+result);
            result = (winner == player) ? 1 : 0;
            expansionedTree.wins+=result;
            expansionedTree.simulations++;
            //System.out.println("to ja");
            
        } else {
        	//#System.out.println("to nie ja");
            result = currentTree.mcts(player, c_param, simulateBestOf);
        }
        wins+= result;
        simulations++;
        return result;
    }

    int simulate(Game tempGame)
    {
    	return simulate(tempGame,1);
    }
    
    int simulate(Game tempGame, int bestOf) // return number of player who won
    {
        tempGame.initializeMove(false);
        ArrayList<Move> moves = MovesGenerator.getRandomMove(tempGame);
        for(int i = 1; i<bestOf; i++)
        {
        	ArrayList<Move> tempMoves = MovesGenerator.getRandomMove(tempGame);
        	if(tempMoves.size()>moves.size())
        	{
        		moves=tempMoves;
        	}
        	
        }
        tempGame.performMoves(moves);
        tempGame.nextRound();
        tempGame.endTour();
        //if(tempGame.getPlayerWin()!=-1)
        	//System.out.println("Symulacja");
        return tempGame.getPlayerWin() == -1 ? simulate(tempGame,bestOf) : tempGame.getPlayerWin();
    }

    private TreeOfGame expansion() {
        currentGame.initializeMove(true);
        if (moves == null) {
            moves = MovesGenerator.getAllMoves(currentGame);
            //System.out.println("ruchy: "+moves.size());
        }
        if(!moves.isEmpty())
        {
        	ArrayList<Move> currentMove = moves.remove(0);

            TreeOfGame newLeaf = new TreeOfGame(currentGame);
            newLeaf.previousMove = currentMove;
            newLeaf.currentGame.performMoves(currentMove);
            newLeaf.currentGame.nextRound();
            newLeaf.currentGame.endTour();
            trees.add(newLeaf);
            return newLeaf;
        }
        else
        {
        	//System.out.println("Brak ekspansji");
        	return this;
        }
    }

    private TreeOfGame selection()
    {
    	return selection(2);
    }
    private TreeOfGame selection(float c) {
        if (trees.isEmpty()) 
        {
            return this;
        } 
        else 
        {
            TreeOfGame current = null;
            double rate = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < trees.size(); i++) 
            {
                double tempRate = Double.NEGATIVE_INFINITY;
                double ri = trees.get(i).wins / trees.get(i).simulations;
                tempRate = ri + c * Math.sqrt(Math.log1p(simulations) / trees.get(i).simulations);
                trees.get(i).rate=tempRate;
                //System.out.println("temprate: "+tempRate);
                if (tempRate > rate) 
                {
                    rate = tempRate;
                    current = trees.get(i);
                }
            }
            //System.out.println("wybieram nie siebie "+rate+" "+this.rate);
            if(rate<=this.rate)
            	current=this;
            return current;
        }
    }


    public TreeOfGame(Game game) {
        currentGame = game.clone();
    }

}
