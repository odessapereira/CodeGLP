package gui;

import engine.CardsInteractions;

import javax.swing.*;
import java.awt.*;

/**
 * A panel representing a single card in the game GUI. It displays a card's image and handles
 * visual properties such as size and rotation.
 *
 * @author Nadjib-M Fariza-A Odessa-T-P
 */
public class CardPanel extends JPanel {
    private Image image;
    private CardsInteractions ci;

    /**
     * Constructs a CardPanel with a specified image path, rotation setting, and optional custom size.
     *
     * @param imagePath  The file path to the card's image.
     * @param isRotated  True if the card should be rotated 90 degrees, false otherwise.
     * @param customSize The custom size for the card panel, or null to use default dimensions.
     */
    public CardPanel(String imagePath, boolean isRotated, Dimension customSize) {
        this.image = new ImageIcon(imagePath).getImage();

        if (customSize != null) {
            setPreferredSize(customSize);
        } else if (isRotated) {
            setPreferredSize(new Dimension(GameGUI.CARD_DIMENSION.height, GameGUI.CARD_DIMENSION.width));
        } else {
            setPreferredSize(GameGUI.CARD_DIMENSION);
        }
    }

    /**
     * Paints the component, rendering the card image.
     *
     * @param g The Graphics context used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}