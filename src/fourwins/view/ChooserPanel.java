package fourwins.view;

import fourwins.model.Player;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

public class ChooserPanel extends JPanel {

    private final List<Player> players = new ArrayList<>();
    private final List<JTextField> playerNames = new ArrayList<>();
    private final List<JColorChooser> colorChoosers = new ArrayList<>();
    private int playerCount = 2;

    public ChooserPanel(FourWinsFrame parent) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel colorPanel = new JPanel(new FlowLayout());

        JTextField player1Text = new JTextField("Player 1", 5);

        JColorChooser player1Color = new JColorChooser();
        player1Color.setPreviewPanel(new JPanel());
        player1Color.setChooserPanels(new AbstractColorChooserPanel[]{new SimplifiedColorChooserPanel(16, 35)});

        JTextField player2Text = new JTextField("Player 2");

        JColorChooser player2Color = new JColorChooser();
        player2Color.setPreviewPanel(new JPanel());
        player2Color.setChooserPanels(new AbstractColorChooserPanel[]{new SimplifiedColorChooserPanel()});

        colorPanel.add(player1Text);
        colorPanel.add(player1Color);
        colorPanel.add(player2Text);
        colorPanel.add(player2Color);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> {
            String player1Name = player1Text.getText();
            String player2Name = player2Text.getText();
            String errorMessage = null;
            if ((player1Color.getColor().equals(Color.WHITE) || player2Color.getColor().equals(Color.WHITE))
                    || player1Color.getColor().equals(player2Color.getColor())) {
                errorMessage = "You have to choose different colors.";
            } else if (player1Name.equals(player2Name)) {
                errorMessage = "You have to choose different names.";
            } else {
                Player player1 = new Player(player1Text.getText(), player1Color.getColor());
                Player player2 = new Player(player2Text.getText(), player2Color.getColor());
                players.clear();
                players.add(player1);
                players.add(player2);
                parent.setPlayers(players);
                parent.initialize();
            }
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(null, errorMessage, "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(colorPanel);
        add(okButton);
    }

}
