package jeu.jungle.Bo;


/**
 * Plateau de jeu pour Xou Dou Qi avec gestion des règles spéciales
 */
public class Plateau {
    public static final int LARGEUR = 7;
    public static final int HAUTEUR = 9;
    private Piece[][] cases;

    public Plateau() {
        this.cases = new Piece[LARGEUR][HAUTEUR];
    }

    /**
     * Vérifie si un saut par-dessus la rivière est possible
     * @param x1 Position X de départ
     * @param y1 Position Y de départ
     * @param x2 Position X d'arrivée
     * @param y2 Position Y d'arrivée
     * @return true si le saut est valide
     */
    public boolean peutSauterRiviere(int x1, int y1, int x2, int y2) {
        Piece piece = obtenirPiece(x1, y1);

        // Seuls le Lion et le Tigre peuvent sauter
        if (piece == null || !piece.getType().peutSauterRiviere()) {
            return false;
        }

        // Saut horizontal
        if (y1 == y2 && Math.abs(x1 - x2) > 1) {
            return verifierSautHorizontal(x1, y1, x2);
        }

        // Saut vertical
        if (x1 == x2 && Math.abs(y1 - y2) > 1) {
            return verifierSautVertical(x1, y1, y2);
        }

        return false;
    }

    private boolean verifierSautHorizontal(int x1, int y, int x2) {
        int direction = x1 < x2 ? 1 : -1;
        for (int x = x1 + direction; x != x2; x += direction) {
            // Vérifie que chaque case intermédiaire est de l'eau vide
            if (!estCaseEau(x, y) || obtenirPiece(x, y) != null) {
                return false;
            }
        }
        return true;
    }

    private boolean verifierSautVertical(int x, int y1, int y2) {
        int direction = y1 < y2 ? 1 : -1;
        for (int y = y1 + direction; y != y2; y += direction) {
            if (!estCaseEau(x, y) || obtenirPiece(x, y) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si une case fait partie de la rivière
     */
    private boolean estCaseEau(int x, int y) {
        // Zones d'eau (lacs) sur le plateau
        return (x >= 3 && x <= 5) && (y == 1 || y == 2 || y == 4 || y == 5);
    }

    public Piece obtenirPiece(int x, int y) {
        if (x < 0 || x >= LARGEUR || y < 0 || y >= HAUTEUR) {
            return null;
        }
        return cases[x][y];
    }

    public void placerPiece(Piece piece, int x, int y) {
        if (x >= 0 && x < LARGEUR && y >= 0 && y < HAUTEUR) {
            cases[x][y] = piece;
            if (piece != null) {
                piece.setPosition(x, y);
            }
        }
    }

    /**
     * Initialise le plateau avec les pièces en position de départ
     */
    public void initialiser() {



        // Joueur 2
        placerPiece(new Piece(TypePiece.LION, 2), 0, 8);
        placerPiece(new Piece(TypePiece.TIGRE, 2), 6, 8);
        placerPiece(new Piece(TypePiece.CHIEN, 2), 1, 7);
        placerPiece(new Piece(TypePiece.CHAT, 2), 5, 7);
        placerPiece(new Piece(TypePiece.RAT, 2), 0, 6);
        placerPiece(new Piece(TypePiece.PANTHERE, 2), 2, 6);
        placerPiece(new Piece(TypePiece.LOUP, 2), 4, 6);
        placerPiece(new Piece(TypePiece.ELEPHANT, 2), 6, 6);
        // Joueur 1
        placerPiece(new Piece(TypePiece.TIGRE, 1), 0, 0);
        placerPiece(new Piece(TypePiece.LION, 1), 6, 0);
        placerPiece(new Piece(TypePiece.CHIEN, 1), 5, 1);
        placerPiece(new Piece(TypePiece.CHAT, 1), 1, 1);
        placerPiece(new Piece(TypePiece.RAT, 1), 6, 2);
        placerPiece(new Piece(TypePiece.PANTHERE, 1), 4, 2);
        placerPiece(new Piece(TypePiece.LOUP, 1), 2, 2);
        placerPiece(new Piece(TypePiece.ELEPHANT, 1), 0, 2);
    }


    public boolean estPartieTerminee() {
        // Vérifier si un joueur a atteint le sanctuaire adverse
        for (int x = 0; x < LARGEUR; x++) {
            // Sanctuaire du joueur 1 (en bas, ligne 0)
            Piece piece = cases[x][0];
            if (piece != null && piece.getJoueur() == 2) {
                return true; // Joueur 2 a atteint le sanctuaire du joueur 1
            }

            // Sanctuaire du joueur 2 (en haut, ligne HAUTEUR-1)
            piece = cases[x][HAUTEUR-1];
            if (piece != null && piece.getJoueur() == 1) {
                return true; // Joueur 1 a atteint le sanctuaire du joueur 2
            }
        }

        // Vérifier si un joueur n'a plus de pièces
        boolean joueur1APieces = false;
        boolean joueur2APieces = false;

        for (int x = 0; x < LARGEUR; x++) {
            for (int y = 0; y < HAUTEUR; y++) {
                Piece piece = cases[x][y];
                if (piece != null) {
                    if (piece.getJoueur() == 1) joueur1APieces = true;
                    else joueur2APieces = true;

                    // Si les deux joueurs ont encore des pièces, continuer
                    if (joueur1APieces && joueur2APieces) {
                        return false;
                    }
                }
            }
        }

        // Si on arrive ici, un joueur n'a plus de pièces
        return true;
    }
    public void deplacerPiece(int x1, int y1, int x2, int y2) {
        // Vérifier les limites
        if (x1 < 0 || x1 >= LARGEUR || y1 < 0 || y1 >= HAUTEUR ||
                x2 < 0 || x2 >= LARGEUR || y2 < 0 || y2 >= HAUTEUR) {
            return;
        }

        Piece piece = cases[x1][y1];
        if (piece != null) {
            cases[x2][y2] = piece;
            cases[x1][y1] = null;

            // Mettre à jour la position de la pièce
            piece.setX(x2);
            piece.setY(y2);
        }
    }

}
