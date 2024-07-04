/* ---------------------------------------------------------------
Práctica 1.
Código fuente: TOptions.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.RMI;

import java.io.Serializable;

public class TOptions implements Serializable {
    private int maxPlayers;
    private double timeWait;

    public TOptions(int maxPlayers, double timeWait) {
        this.maxPlayers = maxPlayers;
        this.timeWait = timeWait;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public double getTimeWait() {
        return timeWait;
    }

    public void setTimeWait(double timeWait) {
        this.timeWait = timeWait;
    }
}
