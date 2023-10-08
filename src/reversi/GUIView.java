package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUIView implements IView, ActionListener
{
	
	
	IModel model;
	IController controller;
	
	JFrame guiFrameOne = new JFrame();
	JFrame guiFrameTwo = new JFrame();
	JLabel labelOne = new JLabel();
	JLabel labelTwo = new JLabel();
	JPanel panelOne = new JPanel();
	JPanel panelTwo = new JPanel();
	JPanel southPanelOne = new JPanel();
	JPanel southPanelTwo = new JPanel();
	JButton buttonOneNorth = new JButton("Greedy AI (play white)");
	JButton buttonOneSouth = new JButton("Restart");
	JButton buttonTwoNorth = new JButton("Greedy AI (play black)");
	JButton buttonTwoSouth = new JButton("Restart");
	
	
	
	
	
	
	
	@Override
	public void initialise(IModel model, IController controller)
	{
		
		this.model = model;
		this.controller = controller;
		
		//frame one
		guiFrameOne.getContentPane().setLayout(new BorderLayout());
		guiFrameOne.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrameOne.setTitle("Reversi - White Player");
		guiFrameOne.setLocationRelativeTo(null);
		labelOne.setText("White player - choose where to put your piece");
		guiFrameOne.getContentPane().add(labelOne, BorderLayout.NORTH);
		panelOne.setLayout(new GridLayout(model.getBoardHeight(), model.getBoardWidth()) );
		southPanelOne.setLayout(new GridLayout(2,1));
		buttonOneSouth.addActionListener(this);
		buttonOneNorth.addActionListener(this);
		southPanelOne.add(buttonOneNorth, BorderLayout.NORTH);
		southPanelOne.add(buttonOneSouth, BorderLayout.SOUTH);
		guiFrameOne.getContentPane().add(panelOne, BorderLayout.CENTER);
		guiFrameOne.getContentPane().add(southPanelOne, BorderLayout.SOUTH);
				
		//frame two 
		guiFrameTwo.getContentPane().setLayout(new BorderLayout());
		guiFrameTwo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrameTwo.setTitle("Reversi - Black Player");
		guiFrameTwo.setLocationRelativeTo(null);
		labelTwo.setText("Black player - not your turn");
		guiFrameTwo.getContentPane().add(labelTwo, BorderLayout.NORTH);
		panelTwo.setLayout(new GridLayout(model.getBoardHeight(), model.getBoardWidth()) );
		southPanelTwo.setLayout(new GridLayout(2,1));
		buttonTwoSouth.addActionListener(this);
		buttonTwoNorth.addActionListener(this);
		southPanelTwo.add(buttonTwoNorth, BorderLayout.NORTH);
		southPanelTwo.add(buttonTwoSouth, BorderLayout.SOUTH);
		guiFrameTwo.getContentPane().add(panelTwo, BorderLayout.CENTER);
		guiFrameTwo.getContentPane().add(southPanelTwo, BorderLayout.SOUTH);
		
		
		
		guiFrameOne.pack();
		guiFrameTwo.pack();
		guiFrameOne.setVisible(true);
		guiFrameTwo.setVisible(true);
		

	}
	
	
	
	@Override
	public void refreshView()
	{
		ColorButtons[][] arrayButtonsOne = new ColorButtons[model.getBoardHeight()][model.getBoardWidth()];
		ColorButtons[][] arrayButtonsTwo = new ColorButtons[model.getBoardHeight()][model.getBoardWidth()];
		
		panelOne.removeAll();
		panelTwo.removeAll();

		for(int i=0; i < model.getBoardHeight() ; i++)
		{
			for( int j=0; j < model.getBoardWidth() ; j++)
			{
					arrayButtonsOne[i][j] = new ColorButtons(model, controller, 50, 50, Color.green, 1, Color.black, model.getBoardContents(i, j), i, j, 1 );
					panelOne.add(arrayButtonsOne[i][j]);
			}
		}
		
		for( int i = model.getBoardHeight() - 1 ; i >= 0 ; i-- )
		{
			for( int j = model.getBoardWidth() - 1 ; j >= 0 ; j-- )
			{
					arrayButtonsTwo[i][j] = new ColorButtons(model, controller, 50, 50, Color.green, 1, Color.black, model.getBoardContents(i, j), i, j, 2);
					panelTwo.add(arrayButtonsTwo[i][j]);
			}
		}
					
		guiFrameOne.pack();
		guiFrameTwo.pack();
		guiFrameOne.setVisible(true);
		guiFrameTwo.setVisible(true);
		
	}

	@Override
	public void feedbackToUser(int player, String message) 
	{

		 
		if(player == 1)
		{
			labelOne.setText(message);
		}
		else if( player == 2 )
			labelTwo.setText(message);
		
		guiFrameOne.pack();
		guiFrameTwo.pack();
		
	}



	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		if( e.getSource() == buttonOneSouth )
			controller.startup();
		else if( e.getSource() == buttonTwoSouth)
			controller.startup();
		else if( e.getSource() == buttonOneNorth )
			controller.doAutomatedMove(1);
		else if( e.getSource() == buttonTwoNorth )
			controller.doAutomatedMove(2);
		
		
	}


}
