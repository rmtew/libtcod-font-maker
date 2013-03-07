import javax.imageio.ImageIO;
import javax.swing.*;
import java.beans.*; //property change stuff
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

class ExportDialog extends JDialog implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 8117331584698770152L;

	private JPanel panel;
	private JSpinner widthField;
    private JOptionPane optionPane;
    private String btnString1 = "Enter";
    private String btnString2 = "Cancel";
    
    private String fontName;
    private int charWidth;
    private int charHeight;
    private int CHARS_PER_LINE_DEFAULT = 32;
    
    public ExportDialog(Frame aFrame, String fontName_) {
        super(aFrame, true);
        
    	fontName = fontName_;
    	Font font = new Font(fontName, Font.PLAIN, 12);
    	FontMetrics fontMetrics = this.getFontMetrics(font);
    	charWidth = fontMetrics.getMaxAdvance();
    	if (charWidth == -1)
    		charWidth = fontMetrics.charWidth(' ');
    	charHeight = fontMetrics.getHeight();

    	BufferedImage image = getFontImage(CHARS_PER_LINE_DEFAULT);

    	ImageIcon imageIcon = new ImageIcon();
    	imageIcon.setImage(image);
    	
        /* Minimum width of 10 characters, maximum width of 80.  Default is 16. */
        SpinnerModel widthFieldModel = new SpinnerNumberModel(CHARS_PER_LINE_DEFAULT, 10, 80, 1);
		widthField = new JSpinner(widthFieldModel);

        String msgString1 = String.format("Character size: %d x %d", charWidth, charHeight);
        Object[] array = {msgString1, widthField, imageIcon};
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
            	BufferedImage image = getFontImage(charsPerLine);
        		try {
        		    File file = new File("fontx.png");
        		    ImageIO.write(image, "png", file);
        		} catch (IOException ioe) {}
    		}
			setVisible(false);
        }
	}

    BufferedImage getFontImage(int charsPerLine) {
    	int imageWidth = charsPerLine * charWidth;
    	int imageHeight = 16 * charHeight;

		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);        		
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2.setFont(new Font(fontName, Font.PLAIN, 12));

		int charIndex = 0;
		drawChar(g2, ' ', 0, charsPerLine);
		drawChar(g2, (char)9786, 1, charsPerLine); /* 1: TCOD_CHAR_SMILIE */
		drawChar(g2, (char)9787, 2, charsPerLine); /* 2: TCOD_CHAR_SMILIE_INV */
		drawChar(g2, (char)9829, 3, charsPerLine); /* 3: TCOD_CHAR_HEART */
		drawChar(g2, (char)9830, 4, charsPerLine); /* 4: TCOD_CHAR_DIAMOND */
		drawChar(g2, (char)9827, 5, charsPerLine); /* 5: TCOD_CHAR_CLUB */
		drawChar(g2, (char)9824, 6, charsPerLine); /* 6: TCOD_CHAR_SPADE */
		drawChar(g2, (char)9702, 7, charsPerLine); /* 7: TCOD_CHAR_BULLET */
		drawChar(g2, (char)9688, 8, charsPerLine); /* 8: TCOD_CHAR_BULLET_INV */
		drawChar(g2, (char)9675, 9, charsPerLine); /* 9: TCOD_CHAR_RADIO_UNSET */
		drawChar(g2, (char)9689, 10, charsPerLine); /* 10: TCOD_CHAR_RADIO_SET */
		drawChar(g2, (char)9794, 11, charsPerLine); /* 11: TCOD_CHAR_MALE */
		drawChar(g2, (char)9792, 12, charsPerLine); /* 12: TCOD_CHAR_FEMALE */
		drawChar(g2, (char)9834, 13, charsPerLine); /* 13: TCOD_CHAR_NOTE */
		drawChar(g2, (char)9835, 14, charsPerLine); /* 14: TCOD_CHAR_NOTE_DOUBLE */
		drawChar(g2, (char)9788, 15, charsPerLine); /* 15: TCOD_CHAR_LIGHT */
		drawChar(g2, (char)9658, 16, charsPerLine); /* 16: TCOD_CHAR_ARROW2_E */
		drawChar(g2, (char)9668, 17, charsPerLine); /* 17: TCOD_CHAR_ARROW2_W */
		drawChar(g2, (char)8597, 18, charsPerLine); /* 18: TCOD_CHAR_DARROW_V */
		drawChar(g2, (char)8252, 19, charsPerLine); /* 19: TCOD_CHAR_EXCLAM_DOUBLE */
		drawChar(g2, (char)182, 20, charsPerLine); /* 20: TCOD_CHAR_PILCROW */
		drawChar(g2, (char)167, 21, charsPerLine); /* 21: TCOD_CHAR_SECTION */
		drawChar(g2, ' ', 22, charsPerLine); /* 22:  */
		drawChar(g2, (char)8616, 23, charsPerLine); /* 23:  */
		drawChar(g2, (char)8593, 24, charsPerLine); /* 24: TCOD_CHAR_ARROW_N */
		drawChar(g2, (char)8595, 25, charsPerLine); /* 25: TCOD_CHAR_ARROW_S */
		drawChar(g2, (char)8594, 26, charsPerLine); /* 26: TCOD_CHAR_ARROW_E */
		drawChar(g2, (char)8592, 27, charsPerLine); /* 27: TCOD_CHAR_ARROW_W */
		drawChar(g2, ' ', 28, charsPerLine); /* 28:  */
		drawChar(g2, (char)8596, 29, charsPerLine); /* 29: TCOD_CHAR_DARROW_H */
		drawChar(g2, (char)9650, 30, charsPerLine); /* 30: TCOD_CHAR_ARROW2_N */
		drawChar(g2, (char)9660, 31, charsPerLine); /* 31: TCOD_CHAR_ARROW2_S */
		charIndex = 32;
		for (; charIndex < 127; charIndex++)
			drawChar(g2, (char)charIndex, charIndex, charsPerLine);
		drawChar(g2, (char)9617, 166, charsPerLine);
		drawChar(g2, (char)9618, 167, charsPerLine);
		drawChar(g2, (char)9619, 168, charsPerLine);    	

		return image;
    }

    void drawChar(Graphics2D g2, char c, int charIndex, int charsPerLine) {
		int x, y;
		x = (charIndex % charsPerLine) * charWidth;
		y = (charIndex / charsPerLine) * charHeight;
		g2.drawChars(new char[] { c }, 0, 1, x, y + charHeight);
    }
}
