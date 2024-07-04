/* ---------------------------------------------------------------
Práctica 1.
Código fuente: Player.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.Game;

import java.io.Serializable;
import java.util.List;


public class Player implements IPlayer, Serializable {

    private static final long serialVersionUID = 4664480702994610549L;
    // Fitxes per apostar
    private int Chips;
    private final int MAX_CHIPS = 1000;

    // Aposta del jugador
    private int playerBet;

    // Accions que pot fer el jugador
    public String ACTION;

    // Constructor sense paràmetres
    public Player() {
    }

    // Constructor amb el nom del jugador
    public Player(String name) {
        Name = name;
        Chips = MAX_CHIPS;
        playerBet = 0;
    }

    // Mètode per obtenir el nom del jugador
    public String getName() {
        return Name;
    }

    // Obtenir fitxes jugador
    @Override
    public int getChips() {
        return Chips;
    }

    // Restar fitxes de la aposta
    @Override
    public void subtractChips(int amout) {
        Chips -= amout;

    }

    // Obtenir aposta jugador
    @Override
    public int getPlayerBet() {
        return playerBet;
    }

    @Override
    public void setPlayerBet(int betPlayer) {
        playerBet += betPlayer;
    }

    // Propietat per emmagatzemar el nom del jugador
    private String Name;

    // Array per emmagatzemar les cartes del jugador
    private Card[] cards = new Card[2];

    // Enumeració per emmagatzemar la classificació del jugador
    private RankingEnum rankingEnum = null;

    // Llista per emmagatzemar la classificació de cartes del jugador
    private List<Card> rankingList = null;

    // Carta més alta del jugador
    private Card highCard = null;

    // Mètode per obtenir la carta més alta del jugador
    public Card getHighCard() {
        return highCard;
    }

    // Mètode per establir la carta més alta del jugador
    public void setHighCard(Card highCard) {
        this.highCard = highCard;
    }

    // Mètode per obtenir la classificació del jugador com a enumeració
    public RankingEnum getRankingEnum() {
        return rankingEnum;
    }

    // Mètode per establir la classificació del jugador com a enumeració
    public void setRankingEnum(RankingEnum rankingEnum) {
        this.rankingEnum = rankingEnum;
    }

    // Mètode per obtenir la llista de classificació de cartes del jugador
    public List<Card> getRankingList() {
        return rankingList;
    }

    // Mètode per establir la llista de classificació de cartes del jugador
    public void setRankingList(List<Card> rankingList) {
        this.rankingList = rankingList;
    }

    // Mètode per obtenir les cartes del jugador
    public Card[] getCards() {
        return cards;
    }

    // Mètode per establir les cartes del jugador
    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    // Remotes
    @Override
    public void receiveCards(Card[] cards) {
        setCards(cards);
    }
}
