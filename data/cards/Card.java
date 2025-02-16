package data.cards;

public class Card{

    private final int number;
    private final String color;
    private final String imagePath; // Chemin de l'image de la carte

    // Constructeur avec l'ajout d'un chemin d'image
     public Card(int number, String color) {
        this.number = number;
        this.color = color.toLowerCase(); // Convertir en minuscules pour correspondre à l'image
        this.imagePath = generateImagePath(); // Générer le chemin de l'image
    }

    // Méthode pour générer le chemin de l'image basé sur la carte
    private String generateImagePath() {
        // Le format de l'image est supposé être "5h.gif" (5 de cœur)
        return this.number + this.color.charAt(0) + ".gif";

    public int getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }
    
    public String getImagePath() {
    return imagePath;
}


    @Override
    public String toString() {
        return number+"de"+color ;
    }
}
