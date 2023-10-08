package reversi;



public class ReversiController implements IController 
{
	
	IModel model;
	IView view;
	
	int whiteScore = 0;
	int blackScore = 0;
	
	boolean key = false;
	
	
	@Override
	public void initialise(IModel model, IView view) 
	{
		this.model = model;
		this.view = view;
		
	}

	@Override
	public void startup()
	{
		
		//no chip is 0, white is 1, black is 2
		model.clear(0);
		model.setBoardContents(3, 3, 1);
		model.setBoardContents(3, 4, 2);
		model.setBoardContents(4, 3, 2);
		model.setBoardContents(4, 4, 1);
		
		model.setFinished(false);
		model.setPlayer(1);
		
		view.feedbackToUser(1, "White player - choose where to put your piece");
		view.feedbackToUser(2, "Black player - not your turn");
		
		view.refreshView();
		
		whiteScore = 0;
		blackScore = 0;
		
		key = false;
		

		
	}

	@Override
	public void update()
	{
		
		int sum = 0;
	
		view.refreshView();
		
		for(int i=0 ; i < model.getBoardHeight() ; i++)
		{
			for(int j=0 ; j < model.getBoardWidth() ; j++)
			{
				if(model.getBoardContents(i, j) == 0)
				{
				sum += ( checkScoreUp(i, j) + checkScoreDown(i, j) + checkScoreLeft(i, j) + checkScoreRight(i, j) + checkScoreUpLeft(i, j) + 
						checkScoreUpRight(i, j) + checkScoreDownLeft(i, j) + checkScoreDownRight(i, j) );
				}
			}
		}
		

		if( sum > 0)
		{
			key = false;
			model.setFinished(false);
		}
		else if( sum == 0 )
		{
			this.changePlayer();
			key = true;
			
			for(int i=0 ; i < model.getBoardHeight() ; i++)
			{
				for(int j=0 ; j < model.getBoardWidth() ; j++)
				{
					if(model.getBoardContents(i, j) == 0)
					{
					sum += ( checkScoreUp(i, j) + checkScoreDown(i, j) + checkScoreLeft(i, j) + checkScoreRight(i, j) + checkScoreUpLeft(i, j) + 
							checkScoreUpRight(i, j) + checkScoreDownLeft(i, j) + checkScoreDownRight(i, j) );
					}
				}
			}
			
			if( key == true && sum == 0)
			{
				model.setFinished(true);
			}
			else
			{
				model.setFinished(false);
			}
			
			
			
		}
		
			key = false;
			
			
	
		if( model.hasFinished() == true)
		{
			scoreCount();

			
			if(whiteScore > blackScore)
			{
				view.feedbackToUser(1, "White won. White " + whiteScore + " to Black " + blackScore + ". Reset the game to replay.");
				view.feedbackToUser(2, "White won. White " + whiteScore + " to Black " + blackScore + ". Reset the game to replay.");
			}
			else if(whiteScore < blackScore) 
			{
				view.feedbackToUser(1, "Black won. Black " + blackScore + " to White " + whiteScore + ". Reset the game to replay.");
				view.feedbackToUser(2, "Black won. Black " + blackScore + " to White " + whiteScore + ". Reset the game to replay.");
			}
			else if (whiteScore == blackScore)
			{
				view.feedbackToUser(1, "Draw. Both players ended with " + whiteScore + " pieces. Reset the game to replay.");
				view.feedbackToUser(2, "Draw. Both players ended with " + whiteScore + " pieces. Reset the game to replay.");
			}
			
			whiteScore= 0;
			blackScore = 0;
			
			
			return;
		}
		

		if ( model.getPlayer() == 1)
		{
			view.feedbackToUser(1, "White player - choose where to put your piece");
			view.feedbackToUser(2, "Black player - not your turn");
		}
		else
		{
			view.feedbackToUser(1, "White player - not your turn");
			view.feedbackToUser(2, "Black player - choose where to put your piece");
		}
		
		view.refreshView();
		
	}
		
	
	
	public void scoreCount()
	{
		for(int i=0 ; i < model.getBoardHeight() ; i++)
		{
			for(int j=0 ; j < model.getBoardWidth() ; j++)
			{
				if( model.getBoardContents(i, j) == 1 )
					whiteScore++;
				else if( model.getBoardContents(i, j) == 2 )
					blackScore++;	
			}
		}
	}

	@Override
	public void squareSelected(int player, int x, int y) 
	{
		
		
		if(model.hasFinished() == true)
		{
			return;
		}
		
		//if a player tries to play a turn when its not their turn
		if(player != model.getPlayer())
		{
			view.feedbackToUser(player, "It is not your turn!");
			return;
		}
		
		if(isValid(x, y) == false)
		{
			return;
		}
		
		if(checkSquare(x, y) == 0)
		{
			return;
		}
		
		
		
		this.changePlayer();
		
		
		
		this.update();

	}
	
	public boolean boardFull()
	{
		
		for(int i=0 ; i < model.getBoardHeight() ; i++)
		{
			for(int j=0 ; j < model.getBoardWidth() ; j++)
			{
				if( model.getBoardContents(i, j) == 0)
				{
					return false;
				}
				
			}
			
		}
		return true;
	}
	
	public void changePlayer()
	{
		if(model.getPlayer() == 1)
			model.setPlayer(2);
		else
			model.setPlayer(1);
	}
		

	@Override
	public void doAutomatedMove(int player)
	{
		int highest = 0;
		int buf = 0;
		int x = 0;
		int y = 0;
		
		if( model.hasFinished() == true)
		{
			return;
		}
		
		if( model.getPlayer() != player)
		{
			view.feedbackToUser(player, "It is not your turn!");
			return;
		}
		
		for(int i=0 ; i < model.getBoardHeight() ; i++)
		{
			for(int j = 0; j < model.getBoardWidth() ; j++)
			{
				buf = 0;
				
				if( model.getBoardContents(i, j) == 0 )
				{
				buf += ( checkScoreUp(i, j) + checkScoreDown(i, j) + checkScoreLeft(i, j) + checkScoreRight(i, j) +
						+ checkScoreUpLeft(i, j) + checkScoreUpRight(i, j) + checkScoreDownLeft(i, j) + checkScoreDownRight(i, j));
				}
				
				if(buf > highest)
				{
					highest = buf;
					x = i;
					y = j;
				}
					
			}
		}
		
		squareSelected(player, x, y);
		
	}
	
	public boolean isValid(int x, int y)
	{
		if(model.getBoardContents(x, y) > 0)
			return false;
		else
			return true;
	}
	
	public int checkSquare(int x, int y)
	{
		int scoreUp = checkScoreUp(x, y);
		if(scoreUp > 0)
		{
			for(int i=0; i <= scoreUp ; i++)
			{
				model.setBoardContents(x+i, y, model.getPlayer());
			}
		}
		
		
		int scoreLeft = checkScoreLeft(x, y);
		if(scoreLeft > 0)
		{
			for(int i=0; i <= scoreLeft ; i++)
			{
				model.setBoardContents(x, y-i, model.getPlayer());
			}
		}
		
		
		int scoreDown = checkScoreDown(x, y);
		if(scoreDown > 0)
		{
			for(int i=0; i<= scoreDown ; i++)
			{
				model.setBoardContents(x-i, y, model.getPlayer());
			}
		}
		
		
		int scoreRight = checkScoreRight(x, y);
		if(scoreRight > 0)
		{
			for(int i=0; i <= scoreRight ; i++)
			{
				model.setBoardContents(x, y+i, model.getPlayer());
			}
		}
		
		
		int scoreUpLeft = checkScoreUpLeft(x, y);
		if(scoreUpLeft > 0)
		{
			for(int i=0; i<= scoreUpLeft ; i++)
			{
				model.setBoardContents(x+i, y-i, model.getPlayer());
			}
		}
		
		
		int scoreUpRight = checkScoreUpRight(x, y);
		if(scoreUpRight > 0)
		{
			for(int i = 0 ; i <= scoreUpRight ; i++)
			{
				model.setBoardContents(x+i, y+i, model.getPlayer());
			}
		}
		
		
		int scoreDownLeft = checkScoreDownLeft(x, y);
		if(scoreDownLeft > 0)
		{
			for(int i=0 ; i <= scoreDownLeft ; i++)
			{
				model.setBoardContents(x-i, y-i, model.getPlayer());
			}
		}
		
		
		int scoreDownRight = checkScoreDownRight(x, y);
		if(scoreDownRight > 0)
		{
			for(int i=0 ; i <= scoreDownRight ; i++)
			{
				model.setBoardContents(x-i, y+i, model.getPlayer());
			}
		}
		
		
		int total;
		
		total = scoreUp + scoreDown + scoreLeft + scoreRight + scoreUpLeft + scoreUpRight + scoreDownLeft + scoreDownRight;
		
		return total;
		
		
	}
	
	
	
	public int checkScoreUp(int x, int y)
	{
		int opCounter;
		int score = 0;
		
		if(model.getPlayer() == 1)
			opCounter = 2;    //player 1, white, is looking for black counters, 2
		else 
			opCounter = 1;        //player 2, black, is looking for white counters, 1
		

		
		for(int i=1 ; i < model.getBoardHeight() ; i++)
		{
			
			if( x+i > 7)
				return 0;
			
			if( x == 7)
				return 0;
			else if( model.getBoardContents(x+i, y) == 0 )
				return 0;
			else if( model.getBoardContents(x+i, y) != opCounter )
				return score;
			else if( x+i == 7 && model.getBoardContents(x+i, y) == opCounter)
				return 0;	
			else
				score++;	
				
		}
		
		return score;

	}
	
	public int checkScoreDown(int x, int y)
	{
		int opCounter;
		int score = 0;
		
		if(model.getPlayer() == 1)
			opCounter = 2;    //player 1, white, is looking for black counters, 2
		else 
			opCounter = 1; 
		
		
		for(int i=1 ; i < model.getBoardHeight() ; i++)
		{
			if(x-i < 0)
				return 0;
			
			if( x == 0)
				return 0;
			else if( model.getBoardContents(x-i, y) == 0 )
				return 0;
			else if( model.getBoardContents(x-i, y) != opCounter )
			{
				return score;
			}
			else if( x-i == 0 && model.getBoardContents(x-i, y) == opCounter)
				return 0;
			else
			{
				score++;	
			}
			
		}
		
		return score;
			
	}
	
	public int checkScoreLeft(int x, int y)
	{
		
		int opCounter;
		int score = 0;
		
		if(model.getPlayer() == 1)
			opCounter = 2;    //player 1, white, is looking for black counters, 2
		else 
			opCounter = 1; 
		
		for(int i=1 ; i < model.getBoardWidth() ; i++)
		{
			
			if(y-i < 0)
				return 0;
			
			if( y == 0)
				return 0;
			else if( model.getBoardContents(x, y-i) == 0 )
				return 0;
			else if( model.getBoardContents(x, y-i) != opCounter )
			{
				return score;
			}
			else if( y-i == 0 && model.getBoardContents(x, y-i) == opCounter)
				return 0;
			else
			{
				score++;	
			}
			
		}
		
		return score;
		
	}
	
	public int checkScoreRight(int x, int y)
	{
		int opCounter;
		int score = 0;
		
		if(model.getPlayer() == 1)
			opCounter = 2;    //player 1, white, is looking for black counters, 2
		else 
			opCounter = 1;
		
		
		for(int i=1 ; i < model.getBoardWidth() ; i++)
		{
			if(y+i > 7)
				return 0;
			
			if( y == 7)
				return 0;
			else if( model.getBoardContents(x, y+i) == 0 )
				return 0;
			else if( model.getBoardContents(x, y+i) != opCounter )
			{
				return score;
			}
			else if( y+i == 7 && model.getBoardContents(x, y+i) == opCounter)
				return 0;
			else
			{
				score++;	
			}
			
		}
		
		return score;
	}
	
	public int checkScoreUpLeft(int x, int y)
	{
		int opCounter;
		int score = 0;
		
		if(model.getPlayer() == 1)
			opCounter = 2;    //player 1, white, is looking for black counters, 2
		else 
			opCounter = 1;
		
		
		for(int i=1 ; i < model.getBoardHeight() ; i++)
		{
			
			if((x+i > 7) || (y-i < 0))
				return 0;
			
			if( x == 7 || y == 0)
				return 0;
			else if( model.getBoardContents(x+i, y-i) == 0)
				return 0;
			else if( model.getBoardContents(x+i, y-i) != opCounter )
				return score;
			else if( (x+i==7 || y-i==0) && model.getBoardContents(x+i, y-i) == opCounter)
				return 0;
			else
			{
				score++;
			}
			
		}
		
		return score;
	}
	
	public int checkScoreUpRight(int x, int y)
	{
		int opCounter;
		int score = 0;
		
		if(model.getPlayer() == 1)
			opCounter = 2;    //player 1, white, is looking for black counters, 2
		else 
			opCounter = 1;
		
		for(int i=1 ; i < model.getBoardHeight() ; i++)
		{
			if((x+i > 7) || (y+i > 7))
				return 0;
			
			if( x == 7 || y == 7)
				return 0;
			else if( model.getBoardContents(x+i, y+i) == 0)
				return 0;
			else if( model.getBoardContents(x+i, y+i) != opCounter )
				return score;
			else if( (x+i==7 || y+i==7) && model.getBoardContents(x+i, y+i) == opCounter )
				return 0;
			else
			{
				score++;
			}
			
		}
		
		return score;
	}
	
	public int checkScoreDownLeft(int x, int y)
	{
		int opCounter;
		int score = 0;
		
		if(model.getPlayer() == 1)
			opCounter = 2;    //player 1, white, is looking for black counters, 2
		else 
			opCounter = 1;
		
		for(int i=1 ; i < model.getBoardHeight() ; i++)
		{
			if((x-i < 0 ) || (y-i < 0))
				return 0;
			
			if( x == 0 || y == 0)
				return 0;
			else if( model.getBoardContents(x-i, y-i) == 0)
				return 0;
			else if( model.getBoardContents(x-i, y-i) != opCounter )
				return score;
			else if( (x-i==0 || y-i==0) && model.getBoardContents(x-i, y-i) == opCounter)
				return 0;
			else
			{
				score++;
			}
		
		}
		
		return score;
	}
	
	public int checkScoreDownRight(int x, int y)
	{
		int opCounter;
		int score = 0;
		
		if(model.getPlayer() == 1)
			opCounter = 2;    //player 1, white, is looking for black counters, 2
		else 
			opCounter = 1;
		
	
		
		for(int i=1 ; i < model.getBoardHeight() ; i++)
		{
			
			if((x-i < 0) || (y+i > 7) )
				return 0;
			
			if( x == 0 || y == 7)
				return 0;
			else if( model.getBoardContents(x-i, y+i) == 0)
				return 0;
			else if( model.getBoardContents(x-i, y+i) != opCounter )
				return score;
			else if( (x-i == 0 || y+i == 0) && model.getBoardContents(x-i, y+i) == opCounter)
				return 0;
			else
			{
				score++;
			}
			
		}
		
		return score;
	}
	
	

}
