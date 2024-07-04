/* ---------------------------------------------------------------
Práctica 1.
Código fuente: DealerServerMain.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

public class DealerServerMain {
    private static int MaxPLayers = 10;
    private static double TimeWait = 10000;

    public static void main(String[] args) {
        try {
            // Crea una instancia del servidor Dealer
            DealerServer dealerServer = new DealerServer();

            // Exporta el objetcte remot
            Dealer dealerStub;
            try {
                dealerStub = (Dealer) UnicastRemoteObject.exportObject(dealerServer, 0);
            } catch (ExportException e) {
                dealerStub = dealerServer;
            }

            // Obtenir registre RMI
            Registry registry = LocateRegistry.createRegistry(1099);

            // Vincular objecte a "Dealer"
            registry.rebind("Dealer", dealerStub);
            System.out.println("DealerServer Llest.");
            dealerStub.setOptions(new TOptions(MaxPLayers, TimeWait));

            dealerStub.StartGame();

            dealerStub.QuitGame();

        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}