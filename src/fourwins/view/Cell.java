package fourwins.view;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

public class Cell extends JLabel {
    private static final int PREFERRED_CELL_SIZE = 75;

    public Cell() {
        super(new ChipIcon());
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(Color.WHITE);
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(PREFERRED_CELL_SIZE, PREFERRED_CELL_SIZE));
        setBorder(new LineBorder(Color.BLACK, 1));
        setOpaque(true);
    }

    static class ChipIcon implements Icon {

        private static final int PADDING = 2;
        private static final int SIZE = PREFERRED_CELL_SIZE - 2 * PADDING;
        private static final int INNER_PADDING = PADDING * 5;
        private static final int INNER_SIZE = SIZE - 8 * PADDING;



        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(c.getForeground());
            g.fillOval(PADDING, PADDING, SIZE, SIZE);
            g.setColor(c.getForeground().darker());
            g.fillOval(INNER_PADDING, INNER_PADDING, INNER_SIZE, INNER_SIZE);
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }
}
