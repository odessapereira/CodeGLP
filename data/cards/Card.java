package data.cards;

public class Card{

    private final int number;
    private final String color;
    private final String imagePath; // Chemin de l'image de la carte

    // Constructeur avec l'ajout d'un chemin d'image
    public Card(int valeur, String couleur, String imagePath) {
        this.number = valeur;
        this.color = couleur;
        this.imagePath = imagePath; // Le chemin de l'image associé à la carte
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
