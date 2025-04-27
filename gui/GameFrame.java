package gui;

import data.cards.Card;
import data.players.Player;
import engine.CombinaisonProcess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * A frame displaying the end-of-game statistics, including the winner, game duration, number of rounds,
 * and payments for losing players. Provides options to restart the game or quit.
 *
 * @author Nadjib-M Fariza-A Odessa-T-P
 */
public class GameFrame extends JFrame {
    private JLabel winnerLabel;
    private JPanel statsPanel;
    private int totalPayement = 0;
    private static final Font font = new Font("Arial", Font.BOLD, 16);

    /**
     * Constructs a GameFrame with game results and player statistics.
     *
     * @param winner       The name of the winning player.
     * @param gameTimer    The total duration of the game in seconds.
     * @param nbTours      The number of rounds played.
     * @param lostPlayers  The list of players who lost, with their remaining cards.
     */
    public GameFrame(String winner, int gameTimer, int nbTours, ArrayList<Player> lostPlayers) {
        // Configure the frame
        setTitle("Menu du Jeu");
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 10));
        getContentPane().setBackground(new Color(34, 45, 50));

        // Create and configure the winner label
        winnerLabel = new JLabel("Le joueur gagnant : " + winner, SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        winnerLabel.setForeground(new Color(255, 215, 0));
        winnerLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        winnerLabel.setOpaque(true);
        winnerLabel.setBackground(new Color(50, 60, 65));
        add(winnerLabel, BorderLayout.NORTH);

        // Create the central panel for statistics and buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(34, 45, 50));
        centerPanel.setLayout(new BorderLayout(0, 20));

        // Create the statistics panel
        statsPanel = new JPanel();
        statsPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Statistiques de la partie :");
        titleLabel.setForeground(Color.red);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        statsPanel.add(titleLabel, BorderLayout.NORTH);

        statsPanel.setBackground(new Color(50, 60, 65));
        statsPanel.setLayout(new GridLayout(0, 1, 0, 10));
        statsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Add game duration statistic
        int minutes = gameTimer / 60;
        int seconds = gameTimer % 60;
        String timeFormatted = String.format("%d min %d sec", minutes, seconds);
        JLabel timeLabel = new JLabel("Temps de la partie : " + timeFormatted);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timeLabel.setForeground(new Color(255, 215, 0));
        timeLabel.setOpaque(true);
        timeLabel.setBackground(new Color(50, 60, 65));
        statsPanel.add(timeLabel);

        // Add number of rounds statistic
        JLabel nbTour = new JLabel("Nombres de tours : " + nbTours);
        nbTour.setFont(new Font("Arial", Font.BOLD, 20));
        nbTour.setForeground(new Color(255, 215, 0));
        nbTour.setOpaque(true);
        nbTour.setBackground(new Color(50, 60, 65));
        statsPanel.add(nbTour);

        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // Define size for remaining cards display
        Dimension smallCardSize = new Dimension(40, 60);

        // Add statistics for each losing player
        for (Player player : lostPlayers) {
            JPanel playerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            playerRow.setBackground(new Color(50, 60, 65));

            JLabel playerLabel = new JLabel(player.getName() + " : ");
            playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerLabel.setForeground(Color.WHITE);
            playerLabel.setOpaque(true);
            playerLabel.setBackground(new Color(50, 60, 65));
            playerRow.add(playerLabel);

            // Display remaining cards
            for (Card card : player.getHand().getCards()) {
                String imagePath = card.getImagePath();
                CardPanel cardPanel = new CardPanel(imagePath, false, smallCardSize);
                playerRow.add(cardPanel);
            }

            // Calculate and display payment for the player
            CombinaisonProcess cp = new CombinaisonProcess(player.getHand());
            int payementPlayer = cp.calculateHandValue();
            JLabel payement = new JLabel("doit payer : " + payementPlayer + " euro");
            payement.setFont(font);
            payement.setForeground(Color.WHITE);
            playerRow.add(payement);

            totalPayement += payementPlayer;
            statsPanel.add(playerRow);
        }

        // Display total jackpot won by the winner
        JLabel jackPot = new JLabel(winner + " a gagné : " + totalPayement + " euro");
        jackPot.setForeground(Color.WHITE);
        jackPot.setFont(font);
        statsPanel.add(jackPot);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(34, 45, 50));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

        // Create buttons
        JButton newGameButton = new JButton("Rejouer ?");
        JButton quitButton = new JButton("Quitter");

        // Style buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        newGameButton.setFont(buttonFont);
        quitButton.setFont(buttonFont);
        newGameButton.setPreferredSize(new Dimension(150, 50));
        quitButton.setPreferredSize(new Dimension(150, 50));
        newGameButton.setBackground(new Color(0, 150, 0));
        quitButton.setBackground(new Color(0, 150, 0));
        newGameButton.setForeground(Color.WHITE);
        quitButton.setForeground(Color.WHITE);
        newGameButton.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        quitButton.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        newGameButton.setFocusPainted(false);
        quitButton.setFocusPainted(false);

        // Add button actions
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add buttons to the panel
        buttonPanel.add(newGameButton);
        buttonPanel.add(quitButton);

        // Add button panel to the central panel
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add central panel to the frame
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Restarts the game by closing the current frame and opening a new GameGUI instance.
     */
    private void restartGame() {
        dispose();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameGUI newGameGUI = new GameGUI();
                newGameGUI.setVisible(true);
                new Thread(newGameGUI).start();
            }
        });
    }

    /**
     * Main method to launch the GameFrame for testing purposes.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameFrame gameFrame = new GameFrame("", 1200, 15, new ArrayList<>());
                gameFrame.setVisible(true);
            }
        });
    }
}