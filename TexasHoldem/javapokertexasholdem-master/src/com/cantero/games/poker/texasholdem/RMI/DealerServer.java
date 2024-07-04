/* ---------------------------------------------------------------
Práctica 1.
Código fuente: DealerServer.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.RMI;

import com.cantero.games.poker.texasholdem.Game.Deck;
import com.cantero.games.poker.texasholdem.Game.GameTexasHoldem;
import com.cantero.games.poker.texasholdem.Game.IPlayer;
import com.cantero.games.poker.texasholdem.Game.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DealerServer extends UnicastRemoteObject implements Dealer {
    private static ConcurrentHashMap<IPlayer, PlayerCallback> playerCallMap;
    private static GameTexasHoldem game;
    private TOptions options;
    private int maxPlayers;
    private static double timeWait;

    public DealerServer() throws RemoteException {
        super();
        playerCallMap = new ConcurrentHashMap<IPlayer, PlayerCallback>();
        game = new GameTexasHoldem();

    }

    @Override
    public void setOptions(TOptions values) throws InterruptedException {
        if (values == null) {
            System.out.println("Els valors d'opcions no poden ser nulls.");
            return;
        }

        if (values.getMaxPlayers() <= 2) {
            System.out.println("El número mínim de jugadors ha de ser 2.");
        } else {
            maxPlayers = values.getMaxPlayers();
            System.out.println("Número màxim de jugadors: " + maxPlayers);
        }

        if (values.getTimeWait() < 0) {
            System.out.println("El temps d'espera no pot ser negatiu.");
        } else {
            timeWait = values.getTimeWait();
            System.out.println("Temps d'espera establert a: " + timeWait / 1000 + " segons.");
            Thread.sleep((long) timeWait);
        }
    }


    @Override
    public void removePlayer(Player player) throws RemoteException {
        if (playerCallMap.containsKey(player)) {
            System.out.println("Player unregistered: " + player.getName());
            playerCallMap.get(player).removePlayer();
            playerCallMap.remove(player);
        } else {
            System.out.println("El jugador no està registrat.");
        }

    }


    @Override
    public void StartGame() throws RemoteException, InterruptedException {
        if (playerCallMap.size() >= 2) {
            System.out.println("Starting game...");
            List<IPlayer> players = new ArrayList<>(playerCallMap.keySet());
            game.newGame(new Deck(), players, playerCallMap);
            game.deal();

            // Fer funció apostes a game
            game.setBets();

            //Repartir cartes taula
            game.callFlop();

            // Apostar
            game.setBets();

            // Bet Turn
            game.betTurn();

            // Apostar
            game.setBets();

            // Bet River
            game.betRiver();

            // Apostar
            game.setBets();

            //Get Winner
            System.out.println("Get Winner");
            List<IPlayer> winners = game.getWinner();
            for (PlayerCallback callback : playerCallMap.values()) {
                callback.sendWinner(winners, game);
            }

        }
    }

    // Afegir jugador al joc
    @Override
    public void AddPlayer(Player player, PlayerCallback callback) throws RemoteException {
        if (!playerCallMap.containsKey(player)) {
            System.out.println("Jugador registrat: " + player.getName());
            System.out.println("Player callback registered.");
            playerCallMap.put(player, callback);    // Afegir jugador i callback a un map, jugador (key)
            callback.registeredClient(player);  // Enviar confirmació al jugador que està registrat
        } else {
            System.out.println("El jugador " + player.getName() + " ja està registrat.");
        }

    }

    // Finalitzar jpc
    @Override
    public void QuitGame() throws RemoteException {
        // Eliminar jugadors i finalitzar partida
        Thread[] quitThread = new Thread[playerCallMap.size()];
        int playerIterator = 0;
        for (IPlayer player : playerCallMap.keySet()) {
            Thread thread = new Thread(() -> {
                try {
                    removePlayer((Player) player);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            quitThread[playerIterator] = thread;
            quitThread[playerIterator].start();
        }

        System.out.println("[FI DE LA PARTIDA]");
        System.exit(0);
    }

}
