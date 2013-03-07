import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class CharLookup extends JFrame implements ActionListener, ItemListener, ChangeListener, MouseMotionListener
{
	/* Shut up Eclipse. */
	private static final long serialVersionUID = -4471311765737904715L;

	/* How many characters widh the display table is. */
	public static final int TABLE_WIDTH = 30;
	/* How many characters high the display table is. */
	public static final int TABLE_HEIGHT = 20;
	/* The left/right margin size. */
	public static final int OFFSET_X = 5;
	/* The top/bottom margin size. */
	public static final int OFFSET_Y = 5;
	/* The margin size between characters. */
	public static final int GRID_MARGIN = 5;
	/* The initial font size. */
	public static final int DEFAULT_FONT_SIZE = 17;
	/* The colour of the underlying grid. */
	public static final Color GRID_COLOUR = new Color(0.2f, 0.5f, 0.2f);
	public static final Color GRID_SELECT_COLOUR = new Color(1.0f, 0.97f, 0.19f);

	/* The base code point for the display table. */
	public static int base = 0;
	/* How many characters are displayed per table page. */
	public static int displayed = TABLE_WIDTH * TABLE_HEIGHT;
	public static int mouseLocX = 0;
	public static int mouseLocY = 0;
	public static char cornerChar;
	public static boolean lockedOutput = false;

	/* Control panel controls. */
	public static String[][] displayArr;
	public JComboBox<String> fontDD;
	public JSpinner sizeSpinner;
	public JLabel charWidthLabel;
	public JLabel charHeightLabel;
	public JLabel codePointLabel;
	public JButton nextB;
	public JButton prevB;
	public JButton jumpB;
	public JButton exportB;
	public JTextArea locL;
	public JLabel creditL;
	public String defaultFontName = "Lucida Console";
	public String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

	/* Display panel */
	public ImageIcon imageIcon;
	public int charWidth, charHeight, charBaselineOffset;
	public JLabel imageIconLabel;
	public BufferedImage bufferedImage;
	public int fontSize;
	public String fontName;
	
	BoxLayout outerLayout;
    BoxLayout displayLayout;
    BoxLayout controlLayout;

	JPanel outerPanel;
	JPanel displayPanel;
	JPanel controlPanel;

    ExportDialog exportDialog;
	
	public CharLookup()
	{
		super();
		
		displayArr = new String[TABLE_WIDTH][TABLE_HEIGHT];
		buildFontPage();

		setTitle("TCOD Font Maker");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		outerPanel = new JPanel();
		outerLayout = new BoxLayout(outerPanel, BoxLayout.X_AXIS);
		outerPanel.setLayout(outerLayout);
		this.add(outerPanel);

		/* Set up the display panel. */
		displayPanel = new JPanel();
		displayLayout = new BoxLayout(displayPanel, BoxLayout.Y_AXIS);
		displayPanel.setLayout(displayLayout);
		displayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		displayPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		outerPanel.add(displayPanel);

		displayPanel.addMouseMotionListener(this);

		imageIcon = new ImageIcon();
		imageIconLabel = new JLabel(imageIcon);
		displayPanel.add(imageIconLabel);

		/* Set up the control panel. */
		controlPanel = new JPanel();
		controlLayout = new BoxLayout(controlPanel, BoxLayout.Y_AXIS);
		controlPanel.setLayout(controlLayout);	
		controlPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		controlPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));		
		outerPanel.add(controlPanel);

		int fontIndex = 0;
		
		fontDD = new JComboBox<String>(fontNames);
		for (fontIndex=0; fontIndex<fontNames.length; fontIndex++)
			if (fontNames[fontIndex].equals(defaultFontName))
				break;
		fontDD.setSelectedIndex(fontIndex);
		fontDD.addItemListener(this);
		fontDD.setMinimumSize(new Dimension(150, 20));
		fontDD.setAlignmentX(Component.CENTER_ALIGNMENT);
		controlPanel.add(fontDD);

		JPanel fontSizePanel = new JPanel();
		fontSizePanel.setMinimumSize(new Dimension(150, 20));
		fontSizePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		fontSizePanel.add(new JLabel("Size:"));
        SpinnerModel widthFieldModel = new SpinnerNumberModel(DEFAULT_FONT_SIZE, 8, 18, 1);
		sizeSpinner = new JSpinner(widthFieldModel);
		sizeSpinner.addChangeListener(this);
		fontSizePanel.add(Box.createHorizontalStrut(5));
		fontSizePanel.add(sizeSpinner);
		controlPanel.add(fontSizePanel);

		JPanel charSizePanel = new JPanel();
		charSizePanel.setMinimumSize(new Dimension(150, 20));
		charSizePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		charSizePanel.add(new JLabel("Pixels:"));
		charWidthLabel = new JLabel("");
		charSizePanel.add(charWidthLabel);
		charSizePanel.add(new JLabel("x"));
		charHeightLabel = new JLabel("");		
		charSizePanel.add(charHeightLabel);
		controlPanel.add(charSizePanel);

		codePointLabel = new JLabel(" ");
		codePointLabel.setMinimumSize(new Dimension(150, 20));
		codePointLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		controlPanel.add(codePointLabel);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		nextB = new JButton("Next " + displayed);
		nextB.addActionListener(this);
		nextB.setAlignmentX(Component.CENTER_ALIGNMENT);
		nextB.setMinimumSize(new Dimension(150, 20));
		controlPanel.add(nextB);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		prevB = new JButton("Previous " + displayed);
		prevB.addActionListener(this);
		prevB.setAlignmentX(Component.CENTER_ALIGNMENT);
		prevB.setMinimumSize(new Dimension(150, 20));
		controlPanel.add(prevB);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		jumpB = new JButton("Jump To");
		jumpB.addActionListener(this);
		jumpB.setAlignmentX(Component.CENTER_ALIGNMENT);
		jumpB.setMinimumSize(new Dimension(150, 20));
		controlPanel.add(jumpB);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		exportB = new JButton("Export");
		exportB.addActionListener(this);
		exportB.setAlignmentX(Component.CENTER_ALIGNMENT);
		exportB.setMinimumSize(new Dimension(150, 20));
		controlPanel.add(exportB);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		locL = new JTextArea("");
		locL.setEditable(false);
		locL.setLineWrap(true);
		locL.setWrapStyleWord(true);
		locL.setBackground(this.getBackground());
		locL.setAlignmentX(Component.CENTER_ALIGNMENT);
		locL.setMinimumSize(new Dimension(150, 100));
		controlPanel.add(locL);
		
		creditL = new JLabel((char)169 + " Michael Widler, 2012");
		creditL.setBackground(this.getBackground());
		creditL.setMinimumSize(new Dimension(150, 20));
		creditL.setAlignmentX(Component.CENTER_ALIGNMENT);
		controlPanel.add(creditL);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		fontName = (String)fontDD.getSelectedItem();
		fontSize = DEFAULT_FONT_SIZE;
		_font_changed();

		drawFontPage();
		pack();
		
		setVisible(true);
	}
	
	public void itemStateChanged(ItemEvent ie) {
		if (ie.getStateChange() == ItemEvent.SELECTED) {
			if (ie.getSource() == fontDD) {
				setNewFontName((String)fontDD.getSelectedItem());
				drawFontPage();
			}
		}
	}
	
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == sizeSpinner) {
			setNewFontSize((int)sizeSpinner.getValue());
			drawFontPage();
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == exportB)
			exportPNG();
		else {
			if (ae.getSource() == nextB)
				base += displayed;
			else if (ae.getSource() == prevB)
				base -= displayed;		
			else if (ae.getSource() == jumpB)
				getJumpDestination();
			else
				return;

			buildFontPage();
			drawFontPage();
		}
	}
	
	public void mouseDragged(MouseEvent me){}

	public void mouseMoved(MouseEvent me) {
		drawCharOutline(mouseLocX, mouseLocY, GRID_COLOUR);
		mouseLocX = (me.getX() - OFFSET_X) / (charWidth + GRID_MARGIN);
		mouseLocY = (me.getY() - OFFSET_Y) / (charHeight + GRID_MARGIN);
		drawCharOutline(mouseLocX, mouseLocY, GRID_SELECT_COLOUR);
		
		updateField();
	}
	
	public void drawCharOutline(int x, int y, Color color) {
		int cx = OFFSET_X + x * (charWidth + GRID_MARGIN);
		int cy = OFFSET_Y + y * (charHeight + GRID_MARGIN);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.setColor(color);
		g2.drawRect(cx-1, cy-1, charWidth+2, charHeight+2);		

		repaint();
	}

	public void updateField() {
		if(lockedOutput) return;
		
		int curCharIndex = mouseLocX + (mouseLocY * TABLE_WIDTH) + base;
		char curChar = (char)curCharIndex;

		if (locL != null) {
			locL.setText("Characters " + base + " through " + (base + displayed) + ".\n\n");
			this.codePointLabel.setFont(new Font(fontName, Font.PLAIN, 12));
			this.codePointLabel.setText(String.format("%d (%c)", curCharIndex, curChar));
		}
	}	
	
	public void buildFontPage() {
		cornerChar = (char)base;
		base = (int)cornerChar;
				
		for(int x = 0; x < TABLE_WIDTH; x++)
			for(int y = 0; y < TABLE_HEIGHT; y++) {
				int xVal = x;
				int yVal = y * TABLE_WIDTH;
				char c = (char)(xVal + yVal + base);
				displayArr[x][y] = "" + c;
			}
			
		updateField();
	}

	public void _font_changed() {
		BufferedImage tempImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Font font = new Font(fontName, Font.PLAIN, fontSize);
		Graphics tempGraphics = tempImage.getGraphics();
		FontMetrics fontMetrics = tempGraphics.getFontMetrics(font);
		Rectangle2D fontBounds = fontMetrics.getMaxCharBounds(tempGraphics);
		charWidth = (int)fontBounds.getWidth();
		charHeight = (int)fontBounds.getHeight();
		charBaselineOffset = charHeight - fontMetrics.getMaxDescent();
		charWidthLabel.setText(String.valueOf(charWidth));
		charHeightLabel.setText(String.valueOf(charHeight));
	}
	
	public void setNewFontName(String newFontName) {
		fontName = newFontName;
		_font_changed();
	}
	
	public void setNewFontSize(int newFontSize) {
		fontSize = newFontSize;
		_font_changed();
	}
	
	public void drawFontPage() {
		Font font = new Font(fontName, Font.PLAIN, fontSize);
		int imageWidth = (OFFSET_X * 2) + (charWidth * TABLE_WIDTH) + ((TABLE_WIDTH - 1) * GRID_MARGIN);
		int imageHeight = (OFFSET_Y * 2) + (charHeight * TABLE_HEIGHT) + ((TABLE_HEIGHT - 1) * GRID_MARGIN);
		bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2 = bufferedImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);        		
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setFont(font);
		for(int x = 0; x < TABLE_WIDTH; x++)
			for(int y = 0; y < TABLE_HEIGHT; y++) {
				int cx = OFFSET_X + x * (charWidth + GRID_MARGIN);
				int cy = OFFSET_Y + y * (charHeight + GRID_MARGIN);
				
				g2.setColor(GRID_COLOUR);
				g2.drawRect(cx-1, cy-1, charWidth+2, charHeight+2);
				
				g2.setColor(Color.white);
				g2.drawString(displayArr[x][y], cx, cy + charBaselineOffset);
			}

		imageIcon.setImage(bufferedImage);
		imageIconLabel.setSize(imageWidth, imageHeight);

		pack();
		repaint();
	}
	
	public void getJumpDestination() {
		try {
			String value = JOptionPane.showInputDialog("Enter value (0 - 65535):");
			base = Integer.parseInt(value);
		}
		catch(NumberFormatException nfEx){}
	}

	public void exportPNG() {
		String fontName = fontNames[fontDD.getSelectedIndex()];
        exportDialog = new ExportDialog(this, fontName);
        exportDialog.pack();

        exportDialog.setLocationRelativeTo(this);
		exportDialog.setVisible(true);
	}
	
	public static void main(String[] args) {
		new CharLookup();
	}
}
