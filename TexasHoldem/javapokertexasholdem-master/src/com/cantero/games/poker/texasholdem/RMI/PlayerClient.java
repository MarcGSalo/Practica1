/* ---------------------------------------------------------------
Práctica 1.
Código fuente: PlayerClient.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.RMI;

import com.cantero.games.poker.texasholdem.Game.Player;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.Scanner;

public class PlayerClient {
    private static PlayerCallback callback;
    private static Scanner reader;
    private static String option = "";

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Dealer dealer = (Dealer) registry.lookup("Dealer");

            // Crear jugador
            Random random = new Random();
            String name = "Player" + (random.nextInt(100) + 1); // Genera un número aleatorio entre 1 y 100
            Player player = new Player(name);

            // Callback per comunicar dealer-jugador
            callback = new PlayerCallbackImpl();
            // Registrar jugador al servidor (player, callback jugador)
            dealer.AddPlayer(player, callback);
            waitFinalGame();    // Funció per finalitzar joc

            // Accions de joc
            while (true) {
                if (callback.isTurnBet() && callback.isGamePlaying()) {
                    doBet();

                    if (optionToDoBet()) {
                        bet();
                    } else if (passarTorn()) {
                        //Thread.sleep(1000);
                        if (!callback.isPassed()) {
                            sendConfirmationToDo();
                        }
                    } else if (igualarAposta()) {
                        Thread.sleep(1000);
                    }

                } else if (!callback.isGamePlaying()) {
                    break;
                }
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void doBet() throws RemoteException, InterruptedException {

        while (!callback.isTurnBet()) {
            Thread.sleep(1000);
        }
        option = "";
        while (!checkOption(option)) {
            reader = new Scanner(System.in);
            option = reader.nextLine().toUpperCase();
            if (checkOption(option)) {
                break;
            } else {
                System.out.println("\u001B[31mIntrodueix una opció vàlida.\u001B[0m");
            }
        }
        callback.sendBetOption(option.toUpperCase());
        System.out.println("Opció escollida: " + option);

        while (!callback.isOptionValidated()) {
            Thread.sleep(1000);
        }
    }

    public static boolean checkOption(String option) {
        return (option.equals("AP") || option.equals("A") || option.equals("PT") || option.equals("PA") || option.equals("I"));
    }

    public static boolean optionToDoBet() {
        return (option.equals("AP") || option.equals("PA"));
    }

    public static boolean igualarAposta() {
        return option.equals("I");
    }

    public static boolean passarTorn() {
        return option.equals("PT");
    }

    private static void bet() throws RemoteException {
        while (!callback.isBetToDo()) {
            try {
                int amount = Integer.parseInt(reader.nextLine());
                callback.sendBetAmount(amount);

            } catch (NumberFormatException e) {
                if (!callback.isBetToDo() && !callback.isOptionValidated()) {
                    System.out.println("\u001B[31mIntrodueix un número.\u001B[0m");
                } else {
                    break;
                }
            }
            if (callback.isBetToDo()) {
                break;
            }
        }
    }

    public static String sendConfirmationToDo() throws RemoteException, InterruptedException {
        reader = new Scanner(System.in);
        option = reader.nextLine().toUpperCase();
        System.out.println("OPTION : " + option);
        if (!option.equals("S") || !option.equals("N")) {
            System.out.println("\u001B[31mIntrodueix una opció vàlida.\u001B[0m");
            return sendConfirmationToDo().toUpperCase();
        }
        return option.toUpperCase();

    }

    // Funció per finalitzar joc
    private static void waitFinalGame() {
        Thread waitingThread = new Thread(() -> {
            while (true) {
                try {
                    if (!callback.isGamePlaying()) break;
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
        });
        waitingThread.start();
    }
}

