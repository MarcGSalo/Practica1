/* ---------------------------------------------------------------
Práctica 1.
Código fuente: GameTexasHoldem.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.Game;

import com.cantero.games.poker.texasholdem.RMI.PlayerCallback;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GameTexasHoldem implements Serializable {
    public GameTexasHoldem() {
        // Inicializar la lista de jugadores y la baraja
        this.players = new ArrayList<>();
        this.currentBet = 0;
        this.totalBet = 0;
    }

    private int currentBet; // Aposta màxima
    private int totalBet;   //Total apostat

    // Identificador únic per a la serialització
    private static final long serialVersionUID = 967261359515323981L;

    // Baralla de cartes utilitzada en el joc
    private Deck deck;

    // Llista de jugadors participants en el joc
    private List<IPlayer> players;
    private ConcurrentHashMap<IPlayer, PlayerCallback> playerCallMap;

    // Llista de cartes a la taula
    private List<Card> tableCards;

    // Variable per a mostrar missatges verbosos
    private boolean verbose = false;

    // Inicia un nou joc amb la baralla especificada i els jugadors especificats
    public void newGame(Deck deck, List<IPlayer> players, ConcurrentHashMap<IPlayer, PlayerCallback> playerCallMap) throws RemoteException {
        this.deck = deck;
        tableCards = new ArrayList<>();
        this.players = players;
        this.playerCallMap = playerCallMap;
        System.out.println("Creada nova partida");
        for (PlayerCallback callback : playerCallMap.values()) {
            callback.gameStarted();
        }
        // Posar apostes dels jugadors a 0
        for (IPlayer player : playerCallMap.keySet()) {
            playerMapBets.put(player, 0);
        }
    }

    // Elimina un jugador del joc
    public void removePlayer(IPlayer player) throws RemoteException {
        System.out.println("Player unregistered: " + player.getName());
        playerCallMap.get(player).removePlayer();
        playerCallMap.remove(player);

    }

    // Reparteix les cartes als jugadors
    public void deal() throws RemoteException {
        for (IPlayer player : players) {
            Card[] cards = new Card[2];
            cards[0] = deck.pop();
            cards[1] = deck.pop();

            // Notificar al cliente sobre les cartas rebudes
            try {
                // Enviar callbacks de cartes repartides al jugador
                PlayerCallback callback = playerCallMap.get(player);
                player.receiveCards(cards);
                callback.receiveCards(cards);
            } catch (RemoteException e) {
                System.err.println("Error al enviar les cartes al jugador: " + e.getMessage());
            }
        }

        checkPlayersRanking();
        showPlayersCards();
    }


    // Mostra les cartes dels jugadors
    public void showPlayersCards() {
        for (IPlayer player : players) {
            System.out.println("\t\tPlayer " + player.getName() + " cards: " + player.getCards()[0] + " & " + player.getCards()[1]);
        }
    }

    // Mostra les classificacions dels jugadors
    public void showPlayersRankings() {
        if (!verbose)
            return;

        for (IPlayer player : players) {
            System.out.println("\t\tPlayer " + player.getName() + " cards: " + player.getCards()[0] + " & " + player.getCards()[1] + " ==> " + player.getRankingEnum().toString());
        }
    }

    // Mostra les cartes a la taula
    public void showTableCards() {
        System.out.print("\t\tTable Cards: ");
        for (Card card : tableCards) {
            System.out.print("\t" + card);
        }
        System.out.println(".");

        for (IPlayer player : players) {
            try {
                // Enviar callbacks de cartes repartides al jugador
                PlayerCallback callback = playerCallMap.get(player);
                callback.showTableCards(tableCards);

            } catch (RemoteException e) {
                System.err.println("Error al enviar les cartes al jugador: " + e.getMessage());
            }
        }
    }


    /**
     * Acció de l'aposta inicial
     * doble initial bet
     */
    public void callFlop() throws RemoteException {
        System.out.println(deck.getSize());
        //deck.pop();
        tableCards.add(deck.pop());
        tableCards.add(deck.pop());
        tableCards.add(deck.pop());
        System.out.println("Table: " + getTableCards());
        checkPlayersRanking();
        showTableCards();
        showPlayersRankings();
    }

    // Acció de l'aposta al torn
    public void betTurn() throws RemoteException {
        //deck.pop();
        tableCards.add(deck.pop());
        checkPlayersRanking();
        showTableCards();
        showPlayersRankings();
    }

    // Acció de l'aposta al riu
    public void betRiver() throws RemoteException {
        //deck.pop();
        tableCards.add(deck.pop());
        checkPlayersRanking();
        showTableCards();
        showPlayersRankings();
    }

    // Obté el(s) guanyador(s) del joc
    public List<IPlayer> getWinner() throws RemoteException {
        checkPlayersRanking();
        List<IPlayer> winnerList = new ArrayList<IPlayer>();
        IPlayer winner = players.get(0);
        Integer winnerRank = RankingUtil.getRankingToInt(winner);
        winnerList.add(winner);
        for (int i = 1; i < players.size(); i++) {
            IPlayer player = players.get(i);
            Integer playerRank = RankingUtil.getRankingToInt(player);
            //Draw game
            if (winnerRank == playerRank) {
                IPlayer highHandPlayer = checkHighSequence(winner, player);
                //Draw checkHighSequence
                if (highHandPlayer == null) {
                    highHandPlayer = checkHighCardWinner(winner, player);
                }
                //Not draw in checkHighSequence or checkHighCardWinner
                if (highHandPlayer != null && !winner.equals(highHandPlayer)) {
                    winner = highHandPlayer;
                    winnerList.clear();
                    winnerList.add(winner);
                } else if (highHandPlayer == null) {
                    //Draw in checkHighSequence and checkHighCardWinner
                    winnerList.add(winner);
                }
            } else if (winnerRank < playerRank) {
                winner = player;
                winnerList.clear();
                winnerList.add(winner);
            }
            winnerRank = RankingUtil.getRankingToInt(winner);
        }
        return winnerList;
    }

    // Comprova quin jugador té la seqüència més alta
    private IPlayer checkHighSequence(IPlayer player1, IPlayer player2) {
        Integer player1Rank = sumRankingList(player1);
        Integer player2Rank = sumRankingList(player2);
        if (player1Rank > player2Rank) {
            return player1;
        } else if (player1Rank < player2Rank) {
            return player2;
        }
        return null;
    }

    // Comprova quin jugador té la carta alta més gran
    @SuppressWarnings("unchecked")
    private IPlayer checkHighCardWinner(IPlayer player1, IPlayer player2) {
        IPlayer winner = compareHighCard(player1, player1.getHighCard(),
                player2, player2.getHighCard());
        if (winner == null) {
            Card player1Card = RankingUtil.getHighCard(player1,
                    Collections.EMPTY_LIST);
            Card player2Card = RankingUtil.getHighCard(player2,
                    Collections.EMPTY_LIST);
            winner = compareHighCard(player1, player1Card, player2, player2Card);
            if (winner != null) {
                player1.setHighCard(player1Card);
                player2.setHighCard(player2Card);
            } else if (winner == null) {
                player1Card = getSecondHighCard(player1, player1Card);
                player2Card = getSecondHighCard(player2, player2Card);
                winner = compareHighCard(player1, player1Card, player2,
                        player2Card);
                if (winner != null) {
                    player1.setHighCard(player1Card);
                    player2.setHighCard(player2Card);
                }
            }
        }
        return winner;
    }

    // Compara les cartes altes entre dos jugadors
    private IPlayer compareHighCard(IPlayer player1, Card player1HighCard,
                                    IPlayer player2, Card player2HighCard) {
        if (player1HighCard.getRankToInt() > player2HighCard.getRankToInt()) {
            return player1;
        } else if (player1HighCard.getRankToInt() < player2HighCard
                .getRankToInt()) {
            return player2;
        }
        return null;
    }

    /*
     *	Obté la segona carta més alta d'un jugador
     * TODO This method must be moved to RankingUtil
     */
    private Card getSecondHighCard(IPlayer player, Card card) {
        if (player.getCards()[0].equals(card)) {
            return player.getCards()[1];
        }
        return player.getCards()[0];
    }

    public List<Card> getTableCards() {
        return tableCards;
    }

    /*
     * Suma els valors de les cartes del ranking d'un jugador
     * TODO This method must be moved to RankingUtil
     */
    private Integer sumRankingList(IPlayer player) {
        Integer sum = 0;
        for (Card card : player.getRankingList()) {
            sum += card.getRankToInt();
        }
        return sum;
    }

    // Comprova el ranking de tots els jugadors
    private void checkPlayersRanking() throws RemoteException {
        System.out.println("Ranking:");
        for (IPlayer player : players) {
            System.out.println("\t" + player.getName());
            System.out.println("\t" + "Tables: " + getTableCards());
            RankingUtil.checkRanking(player, tableCards);

        }
    }


    // Funció apostar
    public void setBets() throws RemoteException, InterruptedException {
        for (IPlayer player : players) {
            PlayerCallback callback = playerCallMap.get(player);
            callback.messageBet(currentBet);
            callback.setTrueConditions();
            String playerOption = callback.getBetOption();
            System.out.println("Opció del jugador: " + playerOption);
            //Thread.sleep(1000);
            validateOption(playerOption, player, callback);
        }
    }

    private ConcurrentHashMap<IPlayer, Integer> playerMapBets = new ConcurrentHashMap<>();

    private void validateOption(String playerOption, IPlayer player, PlayerCallback callback) throws RemoteException, InterruptedException {
        switch (playerOption) {
            case "AP", "PA":
                callback.setOptionValidated(true);
                callback.changeValidOption();
                bet(player);
                break;
            case "PT":
                callback.setOptionValidated(true);
                callback.changeValidOption();
                passTurn(player);
                break;
            case "I":
                callback.setOptionValidated(true);
                callback.changeValidOption();
                callBet(player);
                break;
            case "A":
                System.out.println("Opció del jugador: " + player.getName() + " d'abandonar partida.");
                callback.setOptionValidated(true);
                callback.changeValidOption();
                removePlayer(player);
                break;
            default:
                break;
        }
    }


    // Funcions de les apostes
    // APOSTAR i PUJAR APOSTA
    public void bet(IPlayer player) throws RemoteException, InterruptedException {
        PlayerCallback callback = playerCallMap.get(player);
        if (player.getChips() > currentBet) {
            callback.messageToDoBet(currentBet, player);
            int playerBet = callback.getBetAmount(player, currentBet);
            System.out.println("Aposta rebuda del jugador " + player.getName() + ": " + playerBet);
            player.subtractChips(playerBet);
            currentBet += playerBet;
            sumToBet(playerBet);
            player.setPlayerBet(playerBet);
            playerMapBets.put(player, currentBet);
            System.out.println("\u001B[33mJugador " + player.getName() + " amb " + player.getChips() + " fitxes." + "\u001B[0m");
            callback.finalizeBetTurn();
            callback.getMessageAfterBet(player.getChips());
        }
    }

    private void sumToBet(int playerBet) {
        totalBet += playerBet;
    }

    // PASAR TORN
    public void passTurn(IPlayer player) throws RemoteException, InterruptedException {
        PlayerCallback callback = playerCallMap.get(player);
        if (playerMapBets.get(player) == currentBet) {
            callback.messageCorrectPass();
            callback.finalizeBetTurn();
        } else {
            callback.messageIncorrectPass();
            String playerOption = callback.getConfirmToEqualOrExit();
            Thread.sleep(1000);
            if (playerOption.equals("S")) {
                callBet(player);
            } else if (playerOption.equals("N")) {
                fold(player);
            }
        }
    }

    // IGUALAR APOSTA
    public void callBet(IPlayer player) throws RemoteException {
        PlayerCallback callback = playerCallMap.get(player);
        if (player.getChips() >= currentBet && playerMapBets.get(player) < currentBet) {
            int rest = currentBet - playerMapBets.get(player);
            sumToBet(rest);
            player.subtractChips(rest);
            player.setPlayerBet(rest);
            callback.messageCallBet(currentBet);
            int apostaTotalJugador = rest + playerMapBets.get(player);
            playerMapBets.put(player, apostaTotalJugador);

            System.out.println("Total del jugador: " + playerMapBets.get(player));
            System.out.println("Apostat total: " + totalBet + "\tAposta màxima: " + currentBet);
        }
    }

    // ABANDONAR
    public void fold(IPlayer player) throws RemoteException {
        removePlayer(player);
    }
}
