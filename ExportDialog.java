import javax.imageio.ImageIO;
import javax.swing.*;
import java.beans.*; //property change stuff
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

class ExportDialog extends JDialog implements ActionListener, PropertyChangeListener {
    private JSpinner widthField;
    private JOptionPane optionPane;
    private String btnString1 = "Enter";
    private String btnString2 = "Cancel";
    
    private String fontName;
    private int charWidth;
    private int charHeight;
    
    public ExportDialog(Frame aFrame, String fontName_) {
        super(aFrame, true);

    	fontName = fontName_;
    	Font font = new Font(fontName, Font.PLAIN, 12);
    	FontMetrics fontMetrics = this.getFontMetrics(font);
    	charWidth = fontMetrics.getMaxAdvance();
    	if (charWidth == -1)
    		charWidth = fontMetrics.charWidth(' ');
    	charHeight = fontMetrics.getHeight();

        /* Minimum width of 10 characters, maximum width of 80.  Default is 16. */
        SpinnerModel widthFieldModel = new SpinnerNumberModel(16, 10, 80, 1);
		widthField = new JSpinner(widthFieldModel);

        String msgString1 = String.format("Character size: %d x %d", charWidth, charHeight);
        Object[] array = {msgString1, widthField};
        Object[] options = {btnString1, btnString2};

        optionPane = new JOptionPane(array,
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);
        setContentPane(optionPane);

        optionPane.addPropertyChangeListener(this);
	}

    /** This method handles events for the text field. */
    public void actionPerformed(ActionEvent e) {
	}

    /** This method reacts to state changes in the option pane. */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE)
                return;
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            if (btnString1.equals(value)) {
            	int charsPerLine = (int)widthField.getValue();
            	int imageWidth = charsPerLine * charWidth;
            	int imageHeight = 16 * charHeight;
        		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        		Graphics2D g2 = image.createGraphics();
        		g2.setFont(new Font(fontName, Font.PLAIN, 12));
        		char firstChar = ' ';
        		for (char c = firstChar; c < 255; c++) {
        			int x, y;
        			x = ((c - firstChar) % charsPerLine) * charWidth;
        			y = ((c - firstChar) / charsPerLine) * charHeight;
        			g2.drawChars(new char[] { c }, 0, 1, x, y + charHeight);
        		}
        		try {
        		    File file = new File("fontx.png");
        		    ImageIO.write(image, "png", file);
        		} catch (IOException ioe) {}
    		}
			setVisible(false);
        }
	}
}
