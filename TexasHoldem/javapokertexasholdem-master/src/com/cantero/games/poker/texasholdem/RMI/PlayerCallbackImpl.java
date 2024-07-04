/* ---------------------------------------------------------------
Práctica 1.
Código fuente: PlayerCallbavkImpl.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.RMI;

import com.cantero.games.poker.texasholdem.Game.Card;
import com.cantero.games.poker.texasholdem.Game.GameTexasHoldem;
import com.cantero.games.poker.texasholdem.Game.IPlayer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class PlayerCallbackImpl extends UnicastRemoteObject implements PlayerCallback {
    private boolean gameStarted;
    private boolean playing;
    private boolean turnBet;
    private boolean optionValidated;
    private boolean optionPass = false;

    private boolean gameStatus;

    public boolean validOption;
    private String betOption;
    private int betAmount;
    private final Object lock = new Object();

    // Callbacks per als clients
    public PlayerCallbackImpl() throws RemoteException {
        super();
    }

    @Override
    public void changeValidOption() throws RemoteException {
        synchronized (lock) {
            validOption = !validOption;
        }
    }

    @Override
    public boolean isValidOption() throws RemoteException {
        return validOption;
    }

    @Override
    public void setTrueConditions() throws RemoteException {
        synchronized (lock) {
            turnBet = true;
            validOption = true;
            lock.notify();
        }
    }

    @Override
    public void registeredClient(IPlayer player) throws RemoteException {
        System.out.println("[Servidor] Client registrat correctament al servidor " + player.getName());
        playing = true;
    }

    @Override
    public void gameStarted() throws RemoteException {
        System.out.println("[Servidor] Started Game");
        gameStarted = true;
        turnBet = false;
        validOption = false;
        gameStatus = false;
    }

    @Override
    public void receiveCards(Card[] cards) throws RemoteException {
        System.out.println("\u001B[36m\n[Servidor] Cartes rebudes:\u001B[0m\n");
        for (Card card : cards) {
            System.out.println("\u001B[36m\t\t" + card.toString() + "\u001B[0m");
        }
        System.out.println();
    }

    @Override
    public void showTableCards(List<Card> cards) throws RemoteException {
        System.out.println("\u001B[32m\n[Servidor] Cartes sobre la taula:\u001B[0m");
        for (Card card : cards) {
            System.out.println("\u001B[32m\t\t" + card.toString() + "\u001B[0m");
        }
        System.out.println();

    }

    @Override
    public void removePlayer() throws RemoteException {
        System.out.println("[Servidor] Jugador Eliminat. Final de partida.");
        playing = false;
    }

    @Override
    public boolean isGamePlaying() throws RemoteException {
        return playing;
    }

    @Override
    public void messageBet(int currentBet) throws RemoteException {
        gameStatus = false;
        synchronized (lock) {
            turnBet = true;

            if (currentBet == 0) {
                System.out.println("\u001B[97m[Servidor] Torn d'apostar:" + "\t APOSTAR (AP)" + "\t ABANDONAR (A)\u001B[0m");
            } else {
                System.out.println("\u001B[97m[Servidor] Torn d'apostar:" + "\t PASSAR TORN (PT)" + "\t PUJAR APOSTA (PA)" + "\t IGUALAR APOSTA (I)" + "\t ABANDONAR (A)\u001B[0m");
                System.out.println("Aposta Actual: [" + currentBet + "]");
            }
            lock.notify();
        }
    }


    @Override
    public boolean isTurnBet() throws RemoteException {
        return turnBet;
    }

    @Override
    public void sendBetOption(String option) throws RemoteException {
        synchronized (lock) {
            betOption = option;
            optionValidated = true;
            lock.notify();
        }
    }

    @Override
    public boolean isOptionValidated() throws RemoteException {
        return optionValidated;
    }

    @Override
    public void setOptionValidated(boolean optionValidated) throws RemoteException {
        this.optionValidated = optionValidated;
    }

    @Override
    public String getBetOption() throws RemoteException, InterruptedException {
        synchronized (lock) {
            while (betOption == null) {
                lock.wait();
            }
            String option = betOption;
            betOption = null;
            return option.toUpperCase();
        }
    }

    @Override
    public boolean isBetToDo() throws RemoteException {
        return betToDo;
    }

    // Apostes
    private final boolean betToDo = false;

    @Override
    public void messageToDoBet(int currentBet, IPlayer player) throws RemoteException {
        System.out.println("[Servidor] Si us plau, realitzeu una aposta igual o superior a " + currentBet);
        System.out.println("\u001B[33mFitxes disponibles del jugador: " + player.getChips() + "\u001B[0m");
        turnBet = true;

    }

    @Override
    public void sendBetAmount(int amount) throws RemoteException {
        synchronized (lock) {
            betAmount = amount;
            validOption = true;
            optionValidated = false;
            lock.notify();
        }
    }

    @Override
    public int getBetAmount(IPlayer player, int currentBet) throws RemoteException, InterruptedException {
        synchronized (lock) {
            while (!validOption) {
                lock.wait();
            }
            Thread.sleep(2000);

            System.out.println("\u001B[34mQuantitat apostada: " + betAmount + "\u001B[0m");
            if (player.getChips() < betAmount) {
                validOption = false;
                invalidChips();
                return getBetAmount(player, currentBet);
            }

            if (betAmount < currentBet) {
                System.out.println("\u001B[31m[Servidor] Cuantitat insertada no compleix els requisits.\u001B[0m");
                validOption = false;
                return getBetAmount(player, currentBet);
            }
            setOptionValidated(true);
            return betAmount;
        }
    }

    @Override
    public void getMessageAfterBet(int playerChips) throws RemoteException {
        System.out.println("\u001B[33m[Servidor: Actualització!] Fitxes disponibles del jugador: " + playerChips + "\u001B[0m");
        turnBet = false;
    }

    @Override
    public void finalizeBetTurn() throws RemoteException {
        turnBet = false;
        validOption = false;
        optionValidated = true;
        gameStatus = true;
    }

    @Override
    public void invalidChips() throws RemoteException {
        System.out.println("\u001B[31m[Servidor] Saldo insuficient." + " Si us plau, realitzeu una aposta igual o superior a 0.\u001B[0m");

    }

    // Passar torn
    @Override
    public void messageCorrectPass() throws RemoteException {
        System.out.println("\u001B[34m[Servidor] Torn Passat correctament.\u001B[0m");
        optionPass = true;
    }

    @Override
    public void messageIncorrectPass() throws RemoteException {
        optionPass = false;
        System.out.println("\u001B[31m[Servidor] No es pot passar el torn perquè no tens igualat la aposta." + "\nVols igualar la aposta? Si (S) / No (N)\u001B[0m");

    }

    @Override
    public boolean isPassed() throws RemoteException {
        return optionPass;
    }

    @Override
    public void messageCallBet(int currentBet) throws RemoteException {
        System.out.println("\u001B[34m[Servidor] S'ha igualat l'aposta a l'aposta màxima." + "\t\tAposta actual: " + currentBet + "\u001B[0m");

    }

    @Override
    public String getConfirmToEqualOrExit() throws RemoteException, InterruptedException {
        synchronized (lock) {
            while (betOption == null) {
                lock.wait();
            }
            String option = betOption;
            betOption = null;
            return option.toUpperCase();
        }
    }

    @Override
    public void sendWinner(List<IPlayer> players, GameTexasHoldem game) throws RemoteException {
        System.out.println("Winner:");
        for (IPlayer player : players) {
            System.out.println("\t" + player.getName());
            System.out.println("\t" + "Tables: " + game.getTableCards());

        }

    }
}
