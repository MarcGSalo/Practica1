/* ---------------------------------------------------------------
Práctica 1.
Código fuente: PlayerCallback.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.RMI;

import com.cantero.games.poker.texasholdem.Game.Card;
import com.cantero.games.poker.texasholdem.Game.GameTexasHoldem;
import com.cantero.games.poker.texasholdem.Game.IPlayer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PlayerCallback extends Remote {
    void registeredClient(IPlayer player) throws RemoteException;

    void gameStarted() throws RemoteException;

    void receiveCards(Card[] cards) throws RemoteException;

    void showTableCards(List<Card> cards) throws RemoteException;

    void removePlayer() throws RemoteException;

    boolean isGamePlaying() throws RemoteException;

    void messageBet(int currentBet) throws RemoteException;   // Notificar aposta al jugador

    boolean isTurnBet() throws RemoteException;

    void sendBetOption(String option) throws RemoteException;

    boolean isOptionValidated() throws RemoteException;

    void setOptionValidated(boolean optionValidated) throws RemoteException;

    String getBetOption() throws RemoteException, InterruptedException;

    boolean isBetToDo() throws RemoteException;

    void messageToDoBet(int currentBet, IPlayer player) throws RemoteException;

    void sendBetAmount(int amount) throws RemoteException;

    public int getBetAmount(IPlayer player, int currentBet) throws RemoteException, InterruptedException;

    void getMessageAfterBet(int playerChips) throws RemoteException;

    void finalizeBetTurn() throws RemoteException, InterruptedException;

    public void invalidChips() throws RemoteException;

    void changeValidOption() throws RemoteException;

    boolean isValidOption() throws RemoteException;

    void setTrueConditions() throws RemoteException;

    void messageCorrectPass() throws RemoteException;

    void messageIncorrectPass() throws RemoteException;

    boolean isPassed() throws RemoteException;

    void messageCallBet(int currentBet) throws RemoteException;

    String getConfirmToEqualOrExit() throws RemoteException, InterruptedException;

    void sendWinner(List<IPlayer> winners, GameTexasHoldem game) throws RemoteException;
}