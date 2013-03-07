import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class CharLookup extends JFrame implements ActionListener, ItemListener, MouseMotionListener
{
	public static final int TABLE_WIDTH = 31;
	public static final int TABLE_HEIGHT = 21;
	public static final int OFFSET_X = 10;
	public static final int OFFSET_Y = 40;
	public static final int SIZE = 14;
	public static final int SPACING = SIZE + 5;
	
	public static int base = 0;
	public static int displayed = (TABLE_WIDTH - 1) * (TABLE_HEIGHT - 1);
	public static int mouseLocX = 0;
	public static int mouseLocY = 0;
	public static char cornerChar;
	public static boolean lockedOutput = false;
	
	public static String[][] displayArr;
	public JComboBox fontDD;
	public JButton nextB;
	public JButton prevB;
	public JButton jumpB;
	public JButton exportB;
	public JTextArea locL;
	public JLabel creditL;
	public String defaultFontName = "Lucida Console";
	public String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    ExportDialog exportDialog;
	
	public CharLookup()
	{
		super();

        exportDialog = new ExportDialog(this);
        exportDialog.pack();

		int fontIndex = 0;
		setSize((TABLE_WIDTH * SPACING) + 190,
				(TABLE_HEIGHT * SPACING) + 40);
		setTitle("CharLookup");
		setResizable(false);
		setLocation(20, 20);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		displayArr = new String[TABLE_WIDTH][TABLE_HEIGHT];
		
		fontDD = new JComboBox(fontNames);
		// Initial font is Arial.
		for (fontIndex=0; fontIndex<fontNames.length; fontIndex++)
			if (fontNames[fontIndex].equals(defaultFontName))
				break;
		fontDD.setSelectedIndex(fontIndex);
		fontDD.setSize(150, 20);
		fontDD.setLocation(getWidth() - 170, 10);
		fontDD.addItemListener(this);
		this.add(fontDD);
		
		nextB = new JButton("Next " + displayed);
		nextB.setSize(150, 20);
		nextB.setLocation(getWidth() - 170, 10 + 30 + 30 + 20);
		nextB.addActionListener(this);
		this.add(nextB);
		
		prevB = new JButton("Previous " + displayed);
		prevB.setSize(150, 20);
		prevB.setLocation(getWidth() - 170, 10 + 30 + 30 + 30 + 20);
		prevB.addActionListener(this);
		this.add(prevB);
		
		jumpB = new JButton("Jump To");
		jumpB.setSize(150, 20);
		jumpB.setLocation(getWidth() - 170, 10 + 30 + 30 + 30 + 30 + 20);
		jumpB.addActionListener(this);
		this.add(jumpB);

		exportB = new JButton("Export");
		exportB.setSize(150, 20);
		exportB.setLocation(getWidth() - 170, 10 + 30 + 30 + 30 + 30 + 30 + 20);
		exportB.addActionListener(this);
		this.add(exportB);
		
		locL = new JTextArea("");
		locL.setSize(150, 100);
		locL.setLocation(getWidth() - 170, 10 + 30 + 30 + 30 + 30 + 30 + 30+ 20);
		locL.setEditable(false);
		locL.setLineWrap(true);
		locL.setWrapStyleWord(true);
		locL.setBackground(this.getBackground());
		this.add(locL);
		
		creditL = new JLabel((char)169 + " Michael Widler, 2012");
		creditL.setSize(150, 20);
		creditL.setLocation(getWidth() - 150, getHeight() - 60);
		creditL.setBackground(this.getBackground());
		this.add(creditL);
		
		addMouseMotionListener(this);
		update();
		
		setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == nextB)
		{
			base += displayed;
		}
		
		if(ae.getSource() == prevB)
		{
			base -= displayed;
		}
		
		if(ae.getSource() == jumpB)
		{
			getJumpDestination();
		}

		if(ae.getSource() == exportB)
		{
			exportPNG();
		}
		
		update();
	}
	
	public void itemStateChanged(ItemEvent ie)
	{
		update();
	}
	
	public void mouseDragged(MouseEvent me){}
	
	
	public void mouseMoved(MouseEvent me)
	{
		mouseLocX = (me.getX() - OFFSET_X) / SPACING;
		mouseLocY = (me.getY() - OFFSET_Y) / SPACING;
		
		mouseLocX--;
		updateField();
	}
	
	public void updateField()
	{
		if(lockedOutput) return;
		
		int curCharIndex = mouseLocX + (mouseLocY * (TABLE_WIDTH - 1)) + base;
		char curChar = (char)curCharIndex;
		
		locL.setText("Characters " + base + " through " + (base + displayed) + ".\n\n");
		locL.append(curChar + " = (char)" + curCharIndex);
	}
	
	
	public void update()
	{
		cornerChar = (char)base;
		base = (int)cornerChar;
		
		for(int x = 1; x < TABLE_WIDTH; x++)
			displayArr[x][0] = "";
		
		for(int y = 1; y < TABLE_HEIGHT; y++)
			displayArr[0][y] = "";
		
		for(int x = 0; x < TABLE_WIDTH - 1; x++)
		for(int y = 0; y < TABLE_HEIGHT- 1; y++)
		{
			int xVal = x;
			int yVal = y * (TABLE_WIDTH - 1);
			char c = (char)(xVal + yVal + base);
			displayArr[x+1][y+1] = "" + c;
		}
		
		updateField();
		
		this.repaint();
	}
	
	
	public void getJumpDestination()
	{
		try
		{
			String value = JOptionPane.showInputDialog("Enter value (0 - 65535):");
			base = Integer.parseInt(value);
		}
		catch(NumberFormatException nfEx){}
	}

	public void exportPNG()
	{
		exportDialog.setLocationRelativeTo(this);
		exportDialog.setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		lockedOutput = false;
		String fontName = fontNames[fontDD.getSelectedIndex()];
			
		g.setFont(new Font(fontName, Font.PLAIN, SIZE));
		locL.setFont(new Font(fontName, Font.PLAIN, 12));
		
		for(int x = 1; x < TABLE_WIDTH; x++)
		for(int y = 1; y < TABLE_HEIGHT; y++)
		{
			g.drawString(displayArr[x][y], OFFSET_X + (x * SPACING), 
													 OFFSET_Y + (y * SPACING));
		}
	}
	
	
	public static void main(String[] args)
	{
		CharLookup cl = new CharLookup();
	}
}
