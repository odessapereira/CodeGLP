package gui;

import engine.CardsInteractions;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {
    private Image image;
    private CardsInteractions ci;

    public CardPanel(String imagePath, boolean isRotated) {
        this.image = new ImageIcon(imagePath).getImage();

        // Changer la taille du panel selon l'orientation
        if (isRotated) {
            setPreferredSize(new Dimension(GameGUI.CARD_DIMENSION.height, GameGUI.CARD_DIMENSION.width));
        } else {
            setPreferredSize(GameGUI.CARD_DIMENSION);
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

}