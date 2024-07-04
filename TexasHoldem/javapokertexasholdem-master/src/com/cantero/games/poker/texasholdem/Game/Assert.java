/* ---------------------------------------------------------------
Práctica 1.
Código fuente: Assert.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */

package com.cantero.games.poker.texasholdem.Game;

public class Assert {
    // Mètode que comprova si una condició és veritat. Si no ho és, llança una excepció amb el missatge especificat.
    public static void assertTrue(Boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    // Mètode sobrecarregat que comprova si una condició és veritat. Utilitza un missatge per defecte si no es proporciona un missatge personalitzat.
    public static void assertTrue(Boolean condition) {
        assertTrue(condition, "The condition is not true");
    }

}
