package reversi;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




public class ColorButtons extends JButton implements ActionListener
{
	Color drawColor;
	Color borderColor;
	int borderSize;
	IModel model;
	IController controller;
	int boardValue;
	int i;
	int j;
	int player;
	
	
	public ColorButtons (IModel mod, IController control, int width, int height, Color color, 
			int borderWidth, Color borderCol, int oval, int IValue, int JValue, int play)
	{
		borderSize = borderWidth;
		drawColor = color;
		borderColor = borderCol;
		setMinimumSize( new Dimension(width, height) );
		setPreferredSize( new Dimension(width, height) );
		boardValue = oval;
		model = mod;
		controller = control;
		i = IValue;
		j = JValue;
		player = play;
		this.addActionListener(this);
	}
	
	public ColorButtons( IModel mod, IController control, int width, int height, Color color)
	{
		this( mod, control, width, height, color, 1, Color.black, 1, 100, 100, 1);
	}
	
	public Color getDrawColor()
	{
		return drawColor;
	}

	public void setDrawColor(Color drawColor)
	{
		this.drawColor = drawColor;
	}

	public Color getBorderColor()
	{
		return borderColor;
	}

	public void setBorderColor(Color borderColor)
	{
		this.borderColor = borderColor;
	}

	public int getBorderSize()
	{
		return borderSize;
	}

	public void setBorderSize(int borderSize)
	{
		this.borderSize = borderSize;
	}

	protected void paintComponent(Graphics g)
	{
		//super.paintComponent(arg0);
		if ( borderColor != null )
		{
			g.setColor(borderColor);
			g.fillRect(0, 0, getWidth(), getHeight());
		} 
		if ( drawColor != null )
		{
			g.setColor(drawColor);
			g.fillRect(borderSize, borderSize, getWidth()-borderSize*2, getHeight()-borderSize*2);
		}
		if ( boardValue == 1 )
		{
			g.setColor(Color.white);
			g.fillOval(borderSize,borderSize, 44, 44);
			g.setColor(Color.black);
			g.drawOval(0, 0, 46, 46 );
		}
		if ( boardValue == 2 )
		{
			g.setColor(Color.black);
			g.fillOval(borderSize, borderSize, 44, 44);
			g.setColor(Color.white);
			g.drawOval(0, 0, 46, 46);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		controller.squareSelected(player, i, j);
	}
	
	
	
	
}
