package gui;

import java.awt.*;
import javax.swing.*;
import java.util.List;

import data.Card;
import data.Hand;
import process.SimulationUtility;  // Classe qui permet de lire les images

/**
 * This class represents the game table where the hand of the player is displayed
 * along with the green background representing the game table.
 * 
 * @author Odessa
 */
public class GameTablePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final int START_X = 50;  // Position de départ pour l'affichage des cartes
    private static final int START_Y = 300; // Position de départ de la main (cartes du joueur)

    private Hand hand;
    private Image tableImage;  // Image de fond pour la table de jeu

    public GameTablePanel(Hand hand) {
        this.hand = hand;
        setBackground(Color.GREEN);  // La table de jeu est en vert
    }
