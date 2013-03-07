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
		drawChar(g2, (char)9786, 1, charsPerLine); /* TCOD_CHAR_SMILIE */
		drawChar(g2, (char)9787, 2, charsPerLine); /* TCOD_CHAR_SMILIE_INV */
		drawChar(g2, (char)9829, 3, charsPerLine); /* TCOD_CHAR_HEART */
		drawChar(g2, (char)9830, 4, charsPerLine); /* TCOD_CHAR_DIAMOND */
		drawChar(g2, (char)9827, 5, charsPerLine); /* TCOD_CHAR_CLUB */
		drawChar(g2, (char)9824, 6, charsPerLine); /* TCOD_CHAR_SPADE */
		drawChar(g2, (char)8226, 7, charsPerLine); /* TCOD_CHAR_BULLET */
		drawChar(g2, (char)9688, 8, charsPerLine); /* TCOD_CHAR_BULLET_INV */
		drawChar(g2, (char)9675, 9, charsPerLine); /* TCOD_CHAR_RADIO_UNSET     NOT FOUND */
		drawChar(g2, (char)8857, 10, charsPerLine); /* TCOD_CHAR_RADIO_SET 		NOT FOUND 0x1F518, (maybe also 9737, 8857) IN MOST FONTS (ALL)  */
		drawChar(g2, (char)9794, 11, charsPerLine); /* TCOD_CHAR_MALE */
		drawChar(g2, (char)9792, 12, charsPerLine); /* TCOD_CHAR_FEMALE */
		drawChar(g2, (char)9834, 13, charsPerLine); /* TCOD_CHAR_NOTE */
		drawChar(g2, (char)9835, 14, charsPerLine); /* TCOD_CHAR_NOTE_DOUBLE */
		drawChar(g2, (char)9788, 15, charsPerLine); /* TCOD_CHAR_LIGHT */
		drawChar(g2, (char)9658, 16, charsPerLine); /* TCOD_CHAR_ARROW2_E */
		drawChar(g2, (char)9668, 17, charsPerLine); /* TCOD_CHAR_ARROW2_W */
		drawChar(g2, (char)8597, 18, charsPerLine); /* TCOD_CHAR_DARROW_V */
		drawChar(g2, (char)8252, 19, charsPerLine); /* TCOD_CHAR_EXCLAM_DOUBLE */
		drawChar(g2, (char)182, 20, charsPerLine); /* TCOD_CHAR_PILCROW */
		drawChar(g2, (char)167, 21, charsPerLine); /* TCOD_CHAR_SECTION */
		drawChar(g2, (char)32, 22, charsPerLine); /* terminal - looks like thick underscore */
		drawChar(g2, (char)32, 23, charsPerLine); /* terminal 8616 - looks like TCOD_CHAR_DARROW_V with thin underscore */
		drawChar(g2, (char)8593, 24, charsPerLine); /* TCOD_CHAR_ARROW_N */
		drawChar(g2, (char)8595, 25, charsPerLine); /* TCOD_CHAR_ARROW_S */
		drawChar(g2, (char)8594, 26, charsPerLine); /* TCOD_CHAR_ARROW_E */
		drawChar(g2, (char)8592, 27, charsPerLine); /* TCOD_CHAR_ARROW_W */
		drawChar(g2, (char)32, 28, charsPerLine); /* terminal 8735 - looks like a centered lower right hand corner */
		drawChar(g2, (char)8596, 29, charsPerLine); /* TCOD_CHAR_DARROW_H */
		drawChar(g2, (char)9650, 30, charsPerLine); /* TCOD_CHAR_ARROW2_N */
		drawChar(g2, (char)9660, 31, charsPerLine); /* TCOD_CHAR_ARROW2_S */

		charIndex = 32;
		for (; charIndex < 127; charIndex++)
			drawChar(g2, (char)charIndex, charIndex, charsPerLine);

		drawChar(g2, (char)32, 127, charsPerLine); /* terminal 8962 - house shape */
		drawChar(g2, (char)32, 128, charsPerLine); /* terminal 199 */
		drawChar(g2, (char)32, 129, charsPerLine); /* terminal 252 */
		drawChar(g2, (char)32, 130, charsPerLine); /* terminal 233 */
		drawChar(g2, (char)32, 131, charsPerLine); /* terminal 226 */
		drawChar(g2, (char)32, 132, charsPerLine); /* terminal 228 */
		drawChar(g2, (char)32, 133, charsPerLine); /* terminal 224 */
		drawChar(g2, (char)32, 134, charsPerLine); /* terminal 229 */
		drawChar(g2, (char)32, 135, charsPerLine); /* terminal 231 */
		drawChar(g2, (char)32, 136, charsPerLine); /* terminal 234 */
		drawChar(g2, (char)32, 137, charsPerLine); /* terminal 235 */
		drawChar(g2, (char)32, 138, charsPerLine); /* terminal 232 */
		drawChar(g2, (char)32, 139, charsPerLine); /* terminal 239 */
		drawChar(g2, (char)32, 140, charsPerLine); /* terminal 238 */
		drawChar(g2, (char)32, 141, charsPerLine); /* terminal 236 */
		drawChar(g2, (char)32, 142, charsPerLine); /* terminal 196 */
		drawChar(g2, (char)32, 143, charsPerLine); /* terminal 197 */
		drawChar(g2, (char)32, 144, charsPerLine); /* terminal 201 - E first tone */
		drawChar(g2, (char)32, 145, charsPerLine); /* terminal 230 - ae */
		drawChar(g2, (char)32, 146, charsPerLine); /* terminal 198 - AE*/
		drawChar(g2, (char)32, 147, charsPerLine); /* terminal 244 - o hat */
		drawChar(g2, (char)32, 148, charsPerLine); /* terminal 246 - o umlaut */
		drawChar(g2, (char)32, 149, charsPerLine); /* terminal 242 - o fourth tone */
		drawChar(g2, (char)32, 150, charsPerLine); /* terminal 251 - u hat */
		drawChar(g2, (char)32, 151, charsPerLine); /* terminal 249 - u fourth tone */
		drawChar(g2, (char)32, 152, charsPerLine); /* terminal 255 - y umlaut */
		drawChar(g2, (char)32, 153, charsPerLine); /* terminal 214 - O umlaut */
		drawChar(g2, (char)32, 154, charsPerLine); /* terminal 220 - U umlaut */
		drawChar(g2, (char)32, 155, charsPerLine); /* terminal 162 - cent */
		drawChar(g2, (char)32, 156, charsPerLine); /* terminal 163 - pound */
		drawChar(g2, (char)32, 157, charsPerLine); /* terminal 165 - Y currency */
		drawChar(g2, (char)32, 158, charsPerLine); /* terminal 8359 - Pt */
		drawChar(g2, (char)32, 159, charsPerLine); /* terminal 8747 - Scientific curly f */
		drawChar(g2, (char)32, 160, charsPerLine); /* terminal 225 - a first tone */
		drawChar(g2, (char)32, 161, charsPerLine); /* terminal 237 - i first tone */
		drawChar(g2, (char)32, 162, charsPerLine); /* terminal 243 - o first tone */
		drawChar(g2, (char)32, 163, charsPerLine); /* terminal 250 - u first tone */
		drawChar(g2, (char)32, 164, charsPerLine); /* terminal 241 - spanish n*/
		drawChar(g2, (char)32, 165, charsPerLine); /* terminal 209 - spanish N */
		drawChar(g2, (char)32, 166, charsPerLine); /* terminal - high a underscore */
		drawChar(g2, (char)32, 167, charsPerLine); /* terminal - high o underscore */
		drawChar(g2, (char)32, 168, charsPerLine); /* terminal 191 - upside down ? */
		drawChar(g2, (char)32, 169, charsPerLine); /* terminal - tl corner */
		drawChar(g2, (char)32, 170, charsPerLine); /* terminal - tr corner */
		drawChar(g2, (char)189, 171, charsPerLine); /* TCOD_CHAR_HALF */
		drawChar(g2, (char)188, 172, charsPerLine); /* TCOD_CHAR_ONE_QUARTER */
		drawChar(g2, (char)32, 173, charsPerLine); /* terminal 161 - inverted exclamation mark */
		drawChar(g2, (char)32, 174, charsPerLine); /* terminal 171 - double &lt; left */
		drawChar(g2, (char)32, 175, charsPerLine); /* terminal 187 - double &gt; right */		
		drawChar(g2, (char)9617, 176, charsPerLine); /* TCOD_CHAR_BLOCK1 */
		drawChar(g2, (char)9618, 177, charsPerLine); /* TCOD_CHAR_BLOCK2 */
		drawChar(g2, (char)9619, 178, charsPerLine); /* TCOD_CHAR_BLOCK3 */
		drawChar(g2, (char)9474, 179, charsPerLine); /* TCOD_CHAR_VLINE */
		drawChar(g2, (char)9508, 180, charsPerLine); /* TCOD_CHAR_TEEW */
		drawChar(g2, (char)32, 181, charsPerLine); /* terminal 9569 */
		drawChar(g2, (char)32, 182, charsPerLine); /* terminal 9570 */    	
		drawChar(g2, (char)32, 183, charsPerLine); /* terminal 9558*/    	
		drawChar(g2, (char)169, 184, charsPerLine); /* TCOD_CHAR_COPYRIGHT */    	
		drawChar(g2, (char)9571, 185, charsPerLine); /* TCOD_CHAR_DTEEW */   	
		drawChar(g2, (char)9553, 186, charsPerLine); /* TCOD_CHAR_DVLINE */    	
		drawChar(g2, (char)9559, 187, charsPerLine); /* TCOD_CHAR_DNE */    	
		drawChar(g2, (char)9565, 188, charsPerLine); /* TCOD_CHAR_DSE */    	
		drawChar(g2, (char)162, 189, charsPerLine); /* TCOD_CHAR_CENT */    	
		drawChar(g2, (char)165, 190, charsPerLine); /* TCOD_CHAR_YEN */    	
		drawChar(g2, (char)9488, 191, charsPerLine); /* TCOD_CHAR_NE */    	
		drawChar(g2, (char)9492, 192, charsPerLine); /* TCOD_CHAR_SW */    	
		drawChar(g2, (char)9524, 193, charsPerLine); /* TCOD_CHAR_TEEN */    	
		drawChar(g2, (char)9516, 194, charsPerLine); /* TCOD_CHAR_TEES */    	
		drawChar(g2, (char)9500, 195, charsPerLine); /* TCOD_CHAR_TEEE */    	
		drawChar(g2, (char)9472, 196, charsPerLine); /* TCOD_CHAR_HLINE */    	
		drawChar(g2, (char)9532, 197, charsPerLine); /* TCOD_CHAR_CROSS */    	
		drawChar(g2, (char)32, 198, charsPerLine); /* */    	
		drawChar(g2, (char)32, 199, charsPerLine); /* */    	
		drawChar(g2, (char)9562, 200, charsPerLine); /* TCOD_CHAR_DSW */    	
		drawChar(g2, (char)9556, 201, charsPerLine); /* TCOD_CHAR_DNW */    	
		drawChar(g2, (char)9577, 202, charsPerLine); /* TCOD_CHAR_DTEEN */    	
		drawChar(g2, (char)9574, 203, charsPerLine); /* TCOD_CHAR_DTEES */    	
		drawChar(g2, (char)9568, 204, charsPerLine); /* TCOD_CHAR_DTEEE */    	
		drawChar(g2, (char)9552, 205, charsPerLine); /* TCOD_CHAR_DHLINE */    	
		drawChar(g2, (char)9580, 206, charsPerLine); /* TCOD_CHAR_DCROSS */    	
		drawChar(g2, (char)32, 207, charsPerLine); /* TCOD_CHAR_CURRENCY */    	
		drawChar(g2, (char)32, 208, charsPerLine); /* terminal 9575 */    	
		drawChar(g2, (char)32, 209, charsPerLine); /* terminal 9576 */    	
		drawChar(g2, (char)32, 210, charsPerLine); /* terminal 9572*/    	
		drawChar(g2, (char)32, 211, charsPerLine); /* terminal 9573 */    	
		drawChar(g2, (char)32, 212, charsPerLine); /* terminal 9561 */    	
		drawChar(g2, (char)32, 213, charsPerLine); /* terminal 9560 */    	
		drawChar(g2, (char)32, 214, charsPerLine); /* terminal 9554 */    	
		drawChar(g2, (char)32, 215, charsPerLine); /* terminal 9555 */    	
		drawChar(g2, (char)32, 216, charsPerLine); /* terminal 9579 */    	
		drawChar(g2, (char)9496, 217, charsPerLine); /* TCOD_CHAR_SE */    	
		drawChar(g2, (char)9484, 218, charsPerLine); /* TCOD_CHAR_NW */    	
		drawChar(g2, (char)32, 219, charsPerLine); /* terminal 9608 */    	
		drawChar(g2, (char)32, 220, charsPerLine); /* terminal 9604 */    	
		drawChar(g2, (char)32, 221, charsPerLine); /* terminal 9612 */    	
		drawChar(g2, (char)32, 222, charsPerLine); /* terminal 9600 */    	
		drawChar(g2, (char)32, 223, charsPerLine); /* terminal 9616 */    	
		drawChar(g2, (char)0x2610, 224, charsPerLine); /* TCOD_CHAR_CHECKBOX_UNSET */    	
		drawChar(g2, (char)0x2611, 225, charsPerLine); /* TCOD_CHAR_CHECKBOX_SET */    	
		drawChar(g2, (char)32, 226, charsPerLine); /* TCOD_CHAR_SUBP_NW */    	
		drawChar(g2, (char)32, 227, charsPerLine); /* TCOD_CHAR_SUBP_NE */    	
		drawChar(g2, (char)32, 228, charsPerLine); /* TCOD_CHAR_SUBP_N */    	
		drawChar(g2, (char)32, 229, charsPerLine); /* TCOD_CHAR_SUBP_SE */    	
		drawChar(g2, (char)32, 230, charsPerLine); /* TCOD_CHAR_SUBP_DIAG */    	
		drawChar(g2, (char)32, 231, charsPerLine); /* TCOD_CHAR_SUBP_E */    	
		drawChar(g2, (char)32, 232, charsPerLine); /* TCOD_CHAR_SUBP_SW */    	
		drawChar(g2, (char)32, 233, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 234, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 235, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 236, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 237, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 238, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 239, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 240, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 241, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 242, charsPerLine); /*  */    	
		drawChar(g2, (char)190, 243, charsPerLine); /* TCOD_CHAR_THREE_QUARTERS */    	
		drawChar(g2, (char)32, 244, charsPerLine); /*  */    	
		drawChar(g2, (char)32, 245, charsPerLine); /*  */    	
		drawChar(g2, (char)0xF7, 246, charsPerLine); /* TCOD_CHAR_DIVISION */    	
		drawChar(g2, (char)32, 247, charsPerLine); /*  */    	
		drawChar(g2, (char)176, 248, charsPerLine); /* TCOD_CHAR_GRADE */    	
		drawChar(g2, (char)168, 249, charsPerLine); /* TCOD_CHAR_UMLAUT */    	
		drawChar(g2, (char)32, 250, charsPerLine); /* terminal 183 */    	
		drawChar(g2, (char)185, 251, charsPerLine); /* TCOD_CHAR_POW1 */    	
		drawChar(g2, (char)179, 252, charsPerLine); /* TCOD_CHAR_POW3 */    	
		drawChar(g2, (char)178, 253, charsPerLine); /* TCOD_CHAR_POW2 */    	
		drawChar(g2, (char)9632, 254, charsPerLine); /* TCOD_CHAR_BULLET_SQUARE */    	

		return image;
    }

    void drawChar(Graphics2D g2, char c, int charIndex, int charsPerLine) {
		int x, y;
		x = (charIndex % charsPerLine) * charWidth;
		y = (charIndex / charsPerLine) * charHeight;
		g2.drawChars(new char[] { c }, 0, 1, x, y + charHeight);
    }
}
