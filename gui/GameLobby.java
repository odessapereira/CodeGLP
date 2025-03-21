package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLobby {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Select Players");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(11, 167, 53));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 10, 10));
        panel.setOpaque(false);

        JButton button3 = new JButton("3 Players");
        JButton button4 = new JButton("4 Players");
        JButton button5 = new JButton("5 Players");

        ActionListener startGameAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new GameGUI();
            }
        };
        button3.addActionListener(startGameAction);
        button4.addActionListener(startGameAction);
        button5.addActionListener(startGameAction);

        panel.add(button3);
        panel.add(button4);
        panel.add(button5);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

