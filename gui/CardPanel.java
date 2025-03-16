package gui;

import data.cards.Card;
import engine.CardsInteractions;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CardPanel extends JPanel {
    private Image image;
    private static String hiddenCard="./images/hidenCard.jpeg";
    private CardsInteractions ci;

    public CardPanel(String imagePath) {
        this.image = new ImageIcon(imagePath).getImage();
        ci= CardsInteractions.getInstance();
        setPreferredSize(GameGUI.CARD_DIMENSION);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void rotateCard(int angle) {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        g2d.rotate(Math.toRadians(angle), getWidth() / 2, getHeight() / 2);
        repaint();
    }
}
