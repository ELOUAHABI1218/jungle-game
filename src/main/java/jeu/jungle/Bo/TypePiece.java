package jeu.jungle.Bo;

/**
 * Hiérarchie des animaux avec leurs propriétés spécifiques
 */
public enum TypePiece {
    ELEPHANT(8, "Éléphant", "E", false),
    LION(7, "Lion", "L", true),
    TIGRE(6, "Tigre", "T", true),
    PANTHERE(5, "Panthère", "P", false),
    CHIEN(4, "Chien", "Chi", false),
    LOUP(3, "Loup", "L", false),
    CHAT(2, "Chat", "cha", false),
    RAT(1, "Rat", "R", false);

    private final int rang;
    private final String nomComplet;
    private final String symbole;
    private final boolean peutSauterRiviere;

    TypePiece(int rang, String nomComplet, String symbole, boolean peutSauterRiviere) {
        this.rang = rang;
        this.nomComplet = nomComplet;
        this.symbole = symbole;
        this.peutSauterRiviere = peutSauterRiviere;
    }

    // Getters
    public int getRang() { return rang; }
    public String getNomComplet() { return nomComplet; }
    public String getSymbole() { return symbole; }
    public boolean peutSauterRiviere() { return peutSauterRiviere; }
}
