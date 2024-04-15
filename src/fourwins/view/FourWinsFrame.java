package fourwins.view;

import fourwins.model.Board;
import fourwins.model.Chip;
import fourwins.model.Player;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FourWinsFrame extends JFrame {

    private Board board;

    private Player current;

    private final JLabel currentPlayerText = new JLabel();

    private boolean locked = false;

    private List<Player> players = new ArrayList<>();

    ChooserPanel startupPanel = new ChooserPanel(this);

    private final JPanel panel = new JPanel();
    private final JPanel innerPanel = new JPanel();

    class MoveThread extends Thread {
        private final int col;
        MoveThread(int col) {
            this.col = col;
        }

        @Override
        public void run() {
            locked = true;
            getCellAt(0, col).setBorder(new LineBorder(Color.BLACK, 2));
            for (int row = 0; row < board.getHeight() ; row++) {
                Cell currentCell = getCellAt(row, col);
                if (currentCell.getForeground().equals(Color.WHITE)) {
                    int finalRow = row;
                    SwingUtilities.invokeLater(() -> {
                        currentCell.setForeground(current.color());
                        if (finalRow > 0) {
                            getCellAt(finalRow - 1, col).setForeground(Color.WHITE);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            checkIfDone(col);
            current = players.get((players.indexOf(current) + 1) % players.size());
            setCurrentPlayerText(current.name());
            locked = false;
        }
    }

    public FourWinsFrame() {
        setTitle("Four wins!");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(panel);
        setJMenuBar(initializeMenuBar());
        panel.add(startupPanel);
        currentPlayerText.setAlignmentX(CENTER_ALIGNMENT);
        pack();
        setLocationRelativeTo(null);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    private JMenuBar initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu newMenu = new JMenu("New");
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.setMnemonic('N');
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newMenu.setMnemonic('N');
        newGame.addActionListener(e -> {
            players = new ArrayList<>();
            panel.removeAll();
            panel.setLayout(new FlowLayout());
            panel.add(startupPanel);
            pack();
            setLocationRelativeTo(null);
        });
        newMenu.add(newGame);
        menuBar.add(newMenu);
        return menuBar;
    }

    public void initialize() {
        current = players.get(0);
        setCurrentPlayerText(players.get(0).name());
        board = new Board(players.size());
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(currentPlayerText);
        panel.add(Box.createVerticalGlue());
        innerPanel.removeAll();
        innerPanel.setLayout(new GridLayout(board.getHeight(), board.getWidth()));
        for (int col = 0; col < board.getWidth(); col++) {
            Cell topCell = createTopCell(col);
            innerPanel.add(topCell);
        }
        for (int i = 0; i < board.getWidth() * (board.getHeight() - 1); i++) {
            innerPanel.add(new Cell());
        }
        panel.add(innerPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private Cell createTopCell(int col) {
        Cell topCell = new Cell();
        topCell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                topCell.setBorder(new LineBorder(current.color(), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                topCell.setBorder(new LineBorder(Color.BLACK, 1));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!locked) {
                    int row = board.placeChip(new Chip(current), col);
                    if (row > -1) {
                        MoveThread moveThread = new MoveThread(col);
                        moveThread.start();
                    } else {
                        JOptionPane.showMessageDialog(null, "You can't move here",
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        return topCell;
    }

    private void setCurrentPlayerText(String name) {
        currentPlayerText.setText("It's " + name + "'s turn!");
    }

    private void checkIfDone(int lastCol) {
        int result = -1;
        if (board.isWon(lastCol)) {
            result = JOptionPane.showConfirmDialog(null,
                    current.name() + " won! \nDo you want to play again?",
                    "Congrats!", JOptionPane.YES_NO_OPTION);
        } else if (board.isFull()) {
            result = JOptionPane.showConfirmDialog(null,
                    "No one won \nDo you want to play again?", "Tie", JOptionPane.YES_NO_OPTION);
        }
        if (result == 0) {
            initialize();
        } else if (result == 1) {
            dispose();
        }
    }

    private Cell getCellAt(int row, int col) {
        int positionInGrid = board.getWidth() * row + col;
        return (Cell) innerPanel.getComponent(positionInGrid);
    }

    public static void main(String[] args) {
        FourWinsFrame frame = new FourWinsFrame();
        frame.setVisible(true);
    }

}
