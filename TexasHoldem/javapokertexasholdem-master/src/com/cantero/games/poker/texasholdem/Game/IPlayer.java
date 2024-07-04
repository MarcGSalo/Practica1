/* ---------------------------------------------------------------
Práctica 1.
Código fuente: IPlayer.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.Game;

import java.rmi.Remote;
import java.util.List;


public interface IPlayer extends Remote {
    // Obté el nom del jugador
    public String getName();

    // Obtenir fitxes
    int getChips();

    // Obté les cartes del jugador
    public Card[] getCards();

    // Obtenir aposta jugador
    int getPlayerBet();

    // Obté la llista de cartes utilitzada per a la classificació del jugador
    public List<Card> getRankingList();

    // Estableix la llista de classificació de cartes del jugador
    public void setRankingList(List<Card> rankingList);

    // Obté la classificació del jugador com a enumeració
    public RankingEnum getRankingEnum();

    // Estableix la classificació del jugador com a enumeració
    public void setRankingEnum(RankingEnum rankingEnum);

    void setPlayerBet(int betPlayer);

    // Obté la carta més alta del jugador
    public Card getHighCard();

    // Estableix la carta més alta del jugador
    public void setHighCard(Card highCard);

    // Remot
    void receiveCards(Card[] cards);

    void subtractChips(int amount);
}
