import javax.swing.*;
import java.beans.*; //property change stuff
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

class ExportDialog extends JDialog implements ActionListener, PropertyChangeListener {
    private JSpinner widthField;
    private JOptionPane optionPane;
    private String btnString1 = "Enter";
    private String btnString2 = "Cancel";

    public ExportDialog(Frame aFrame) {
        super(aFrame, true);

        SpinnerModel widthFieldModel = new SpinnerNumberModel(16, 10, 80, 1);
		widthField = new JSpinner(widthFieldModel);

        String msgString1 = "tcod font image generator?";
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
			/*
				typedText = textField.getText();
                String ucText = typedText.toUpperCase();
                if (magicWord.equals(ucText)) {
                    //we're done; clear and dismiss the dialog
                    clearAndHide();
                } else {
                    //text was invalid
                    textField.selectAll();
                    JOptionPane.showMessageDialog(
                                    CustomDialog.this,
                                    "Sorry, \"" + typedText + "\" "
                                    + "isn't a valid response.\n"
                                    + "Please enter "
                                    + magicWord + ".",
                                    "Try again",
                                    JOptionPane.ERROR_MESSAGE);
                    typedText = null;
                    textField.requestFocusInWindow();
                }  */
				setVisible(false);
            } else {
				setVisible(false);
            }
        }
	}
}

		/*BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();*/
