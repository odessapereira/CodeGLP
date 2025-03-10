package gui;

import data.cards.Card;
import engine.CardsInteractions;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

class CardPanel extends JPanel {
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
}
