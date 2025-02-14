package data.cards;

public class Card{

    private final int number;
    private final String color;

    public Card (int valeur, String couleur) {
        this.number=valeur;
        this.color=couleur;
    }

    public int getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return number+"de"+color ;
    }
}
