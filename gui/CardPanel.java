package gui;

import cards.Card;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {
    private Image image;
    private String imagePath;
    private int cardNumber;  // Numéro de la carte
    private String cardColor; // Couleur de la carte

    public CardPanel(Card card) {
        this.cardNumber = card.getNumber();
        this.cardColor = card.getColor();
        this.imagePath = card.getImagePath();
        this.image = new ImageIcon(imagePath).getImage();
        setPreferredSize(GameGUI.CARD_DIMENSION);  // Taille de la carte
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public String getCardColor() {
        return cardColor;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
