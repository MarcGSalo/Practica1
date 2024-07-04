/* ---------------------------------------------------------------
Práctica 1.
Código fuente: Deck.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck implements IDeck, Serializable {

    // Identificador únic per a la serialització
    private static final long serialVersionUID = 2463644121163649891L;

    // Llista de cartes en el muntó
    private List<Card> cards;

    // Objecte Random per generar nombres aleatoris
    private Random random;

    // Constructor per defecte que inicialitza la llista de cartes i el generador aleatori amb un objecte Random
    public Deck() {
        this(new Random());
    }

    // Constructor que permet especificar un objecte Random per a la generació aleatòria
    public Deck(Random random) {
        this.random = random;
        createDeck(); // Crea un muntó de cartes
    }

    // Mètode privat per crear el muntó de cartes
    public void createDeck() {
        cards = new ArrayList<Card>(); // Inicialitza la llista de cartes
        // Crea una carta per a cada combinació de pal i categoria
        for (CardSuitEnum suit : CardSuitEnum.values()) {
            for (CardRankEnum rank : CardRankEnum.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    // Mètode per extreure una carta aleatòria del muntó
    public Card pop() {
        return cards.remove(random.nextInt(cards.size())); // Retorna una carta eliminada aleatòriament de la llista
    }

    public int getSize() {
        return cards.size();
    }
}