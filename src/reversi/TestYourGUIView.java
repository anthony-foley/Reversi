package reversi;

public class TestYourGUIView extends SimpleModel implements IController 
{
	IModel model;
	IView view;
	
	java.util.Random rand = new java.util.Random();

	
	
	@Override
	public void initialise(IModel model, IView view)
	{
		this.model = model;
		this.view = view;
	}

	@Override
	public void startup()
	{
		// Initialise board
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		for ( int x = 0 ; x < width ; x++ )
			for ( int y = 0 ; y < height ; y++ )
				model.setBoardContents(x, y, 0);
		// Consider setting up any initial pieces here in your own controller
		
		// Refresh all messages and frames
		view.feedbackToUser(1, "startup() was called successfully");
		view.feedbackToUser(2, "startup() was called successfully");
		view.refreshView();
	}

	
	
	@Override
	public void squareSelected(int player, int x, int y)
	{
		model.setBoardContents(x, y, player);		
		view.feedbackToUser(player, "You last played in location " + x + "," + y);
		view.refreshView();
	}
	
	
	@Override
	public void doAutomatedMove(int player)
	{
		view.feedbackToUser(player, "Automated move was pressed successfully - setting next location" );
		
		boolean doneOne = false;
		int repeat = rand.nextInt(6) + 5;
		
		for ( int y = 0 ; y < 8 ; y++ )
			for ( int x = 0 ; x < 8 ; x++ )
				if ( model.getBoardContents(x, y) == 0 )
				{
					model.setBoardContents(x, y, player);
					view.refreshView();
					view.feedbackToUser(player, "Automated move played in location " + x + "," + y );
					if ( --repeat == 0 )
						return;
					doneOne = true;
				}
		
		// If we filled at least one then let it end now
		if ( doneOne )
			return;
		
		model.clear(0);
		view.refreshView();
		view.feedbackToUser( player, "Automated move cleared board as it had nowhere to play" );
	}



	@Override
	public void update()
	{
		// Here we will set finished based upon whether there is any space on the board or not...
		boolean finished = true;
		for ( int x = 0 ; x < model.getBoardWidth() ; x++ )
			for ( int y = 0 ; y < model.getBoardHeight() ; y++ )
				if ( model.getBoardContents(x, y) == 0 )
					finished = false; // There is an empty square
		model.setFinished(finished);
		
		// We assume that something might have changed so update the labels accordingly, then tell view to update itself
		// In this controller though we don't use the finished flag or player number, so we probably just tell the user what was set, for debug purposes
		view.feedbackToUser(1, "I just updated: finished = " + model.hasFinished() + ", current player = " + model.getPlayer() );
		view.feedbackToUser(2, "I just updated: finished = " + model.hasFinished() + ", current player = " + model.getPlayer() );
		view.refreshView();
	}
	
	
	public static void main(String[] args)
	{
		TestYourGUIView tester = new TestYourGUIView();
		
		//// Choose ONE of the models
		//model = new SimpleModel();
		IModel model = tester;

		// Need to test student's GUI view
		IView view = new GUIView();
		
		IController controller = tester;
		
		// Don't change the lines below here, which connect things together
		
		// Initialise everything...
		model.initialise(8, 8, view, controller);
		controller.initialise(model, view);
		view.initialise(model, controller);
		
		// Now start the game - set up the board
		controller.startup();
	}
}
