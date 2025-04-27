package data.cards;

/**
 * Represents a playing card in the game.
 * Each card has a number, color, and an associated image path.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class Card {

    private final int number;
    private final String color;
    private final String imagePath; // Path to the image of the card

    /**
     * Constructor for the Card class.
     *
     * @param number The value of the card.
     * @param color The color (or suit) of the card.
     * @param imagePath The file path to the image representing the card.
     * Author: Nadjib-M Fariza-A Odessa-T-P
     */
    public Card(int number, String color, String imagePath) {
        this.number = number;
        this.color = color;
        this.imagePath = imagePath; // The path to the image associated with the card.
    }

    /**
     * Returns the number (value) of the card.
     *
     * @return The value of the card.
     * Author: Nadjib-M Fariza-A Odessa-T-P
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the color (suit) of the card.
     *
     * @return The color (or suit) of the card.
     * Author: Nadjib-M Fariza-A Odessa-T-P
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the image path associated with the card.
     *
     * @return The path to the card's image.
     * Author: Nadjib-M Fariza-A Odessa-T-P
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Checks if the card is a joker.
     *
     * A card is considered a joker if its number is 0 and its color is either "joker1" or "joker2".
     *
     * @return true if the card is a joker, otherwise false.
     * Author: Nadjib-M Fariza-A Odessa-T-P
     */
    public boolean isJoker() {
        return number == 0 && (color.equals("joker1") || color.equals("joker2"));
    }

    /**
     * Checks if the card is special.
     *
     * A card is considered special if its number is 20.
     *
     * @return true if the card is special, otherwise false.
     * Author: Nadjib-M Fariza-A Odessa-T-P
     */
    public boolean isSpecial() {
        return number == 20;
    }

}
