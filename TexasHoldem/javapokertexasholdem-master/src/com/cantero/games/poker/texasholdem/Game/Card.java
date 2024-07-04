/* ---------------------------------------------------------------
Práctica 1.
Código fuente: Card.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.Game;

import java.io.Serializable;


public class Card implements Serializable {

    // Identificador únic per a la serialització
    private static final long serialVersionUID = 8343990871159439035L;

    // Atributs de la carta
    private CardSuitEnum suit; // El pal de la carta
    private CardRankEnum rank; // La categoria de la carta

    // Constructor de la classe Card que accepta un pal i una categoria
    public Card(CardSuitEnum suit, CardRankEnum rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // Mètode per obtenir el pal de la carta
    public CardSuitEnum getSuit() {
        return suit;
    }

    // Mètode per obtenir la categoria de la carta
    public CardRankEnum getRank() {
        return rank;
    }

    // Mètode per obtenir la categoria de la carta com a enter
    public Integer getRankToInt() {
        return rank.ordinal(); // Retorna l'índex ordinal de la categoria
    }

    // Mètode per comparar si dues cartes són iguals
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Card)) {
            return false;
        } else {
            Card card2 = (Card) obj;
            return rank.equals(card2.getRank()) && suit.equals(card2.getSuit());
        }
    }

    // Mètode per calcular el codi hash de la carta
    @Override
    public int hashCode() {
        // Retorna el codi hash basat en l'ordinal de la categoria i el pal
        return Integer.valueOf(String.valueOf(rank.ordinal()) + String.valueOf(suit.ordinal()));
    }

    // Mètode per obtenir una representació en cadena de la carta
    @Override
    public String toString() {
        // Retorna una cadena amb el pal i la categoria de la carta
        return "Suit: " + suit.toString() + ", Rank :" + rank.toString();
    }
}
