/* ---------------------------------------------------------------
Práctica 1.
Código fuente: Dealer.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.RMI;

import com.cantero.games.poker.texasholdem.Game.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Dealer extends Remote {
    void removePlayer(Player player) throws RemoteException;

    void setOptions(TOptions values) throws RemoteException, InterruptedException;

    void StartGame() throws RemoteException, InterruptedException;

    void AddPlayer(Player player, PlayerCallback callback) throws RemoteException;

    void QuitGame() throws RemoteException, InterruptedException;
}
