package gui;

import data.cards.Card;

import javax.swing.*;
import java.awt.*;

class CardPanel extends JPanel {
    private Image image;

    public CardPanel(String imagePath) {
        this.image = new ImageIcon(imagePath).getImage();
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
