// Authors: Stuart Reges and Marty Stepp

// Class CritterPanel displays a grid of critters
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CritterPanel extends JPanel {
    private CritterModel myModel;
    private Font myFont;
    private static boolean created;
    private int myFontSize = 12;

    public CritterPanel(CritterModel model, int fontSize) {
        // this prevents someone from trying to create their own copy of
        // the GUI components
        if (created)
            throw new RuntimeException("Only one world allowed");
        created = true;

        myFontSize = fontSize;

        myModel = model;
        // construct font and compute char width once in constructor
        // for efficiency
        myFont = new Font("Monospaced", Font.BOLD, myFontSize + 4);
        setBackground(Color.CYAN);
        setPreferredSize(new Dimension(myFontSize * model.getWidth() + 20,
                                       myFontSize * model.getHeight() + 20));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(myFont);
        Iterator<Critter> i = myModel.iterator();
        while (i.hasNext()) {
            Critter next = i.next();
            Point p = myModel.getPoint(next);
            String appearance = myModel.getAppearance(next);
            g.setColor(Color.BLACK);
            g.drawString("" + appearance, p.x * myFontSize + 11,
                         p.y * myFontSize + 21);
            g.setColor(myModel.getColor(next));
            g.drawString("" + appearance, p.x * myFontSize + 10,
                         p.y * myFontSize + 20);
        }
    }
}
