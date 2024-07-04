/* ---------------------------------------------------------------
Práctica 1.
Código fuente: RankingUtil.java
Grau Informàtica
Y0638179N Raul Gabriel Filip
53399631W Marc Gàndara Sentoll
--------------------------------------------------------------- */
package com.cantero.games.poker.texasholdem.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.cantero.games.poker.texasholdem.Game.CardRankEnum.*;
import static com.cantero.games.poker.texasholdem.Game.RankingEnum.*;

/*
 * 	01) ROYAL_FLUSH,
 *	02) STRAIGHT_FLUSH,
 *	03) FOUR_OF_A_KIND,
 *	04) FULL_HOUSE,
 *	05) FLUSH,
 *	06) STRAIGHT,
 *	07) THREE_OF_A_KIND,
 *	08) TWO_PAIR,
 *	09) ONE_PAIR,
 *	10) HIGH_CARD
 */
public class RankingUtil {

    private RankingUtil() {
    }

    // Aquest mètode converteix la classificació del jugador en un enter.
    public static Integer getRankingToInt(IPlayer player) {
        return player.getRankingEnum().ordinal();
    }

    // Aquest mètode determina la classificació del jugador segons les cartes que té i les cartes de la taula.
    public static void checkRanking(IPlayer player, List<Card> tableCards) {
        if (!tableCards.isEmpty()) {
            //HIGH_CARD (Obtenim la carta més alta entre les del jugador i les de la taula)
            Card highCard = getHighCard(player, tableCards);
            player.setHighCard(highCard);

            //ROYAL_FLUSH (Comprovem si hi ha una combinació de Royal Flush)
            List<Card> rankingList = getRoyalFlush(player, tableCards);
            if (rankingList != null) {
                setRankingEnumAndList(player, ROYAL_FLUSH, rankingList);
                return;
            }
            //STRAIGHT_FLUSH (Comprovem si hi ha una combinació de Straight Flush)
            rankingList = getStraightFlush(player, tableCards);
            if (rankingList != null) {
                setRankingEnumAndList(player, STRAIGHT_FLUSH,
                        rankingList);
                return;
            }
            //FOUR_OF_A_KIND (Comprovem si hi ha una combinació de Four of a Kind)
            rankingList = getFourOfAKind(player, tableCards);
            if (rankingList != null) {
                setRankingEnumAndList(player, FOUR_OF_A_KIND,
                        rankingList);
                return;
            }
            //FULL_HOUSE (Comprovem si hi ha una combinació de Full House)
            rankingList = getFullHouse(player, tableCards);
            if (rankingList != null) {
                setRankingEnumAndList(player, FULL_HOUSE, rankingList);
                return;
            }
            //FLUSH (Comprovem si hi ha una combinació de Flush)
            rankingList = getFlush(player, tableCards);
            if (rankingList != null) {
                setRankingEnumAndList(player, FLUSH, rankingList);
                return;
            }
            //STRAIGHT (Comprovem si hi ha una combinació de Straight)
            rankingList = getStraight(player, tableCards);
            if (rankingList != null) {
                setRankingEnumAndList(player, STRAIGHT, rankingList);
                return;
            }
            //THREE_OF_A_KIND (Comprovem si hi ha una combinació de Three of a Kind)
            rankingList = getThreeOfAKind(player, tableCards);
            if (rankingList != null) {
                setRankingEnumAndList(player, THREE_OF_A_KIND,
                        rankingList);
                return;
            }
            //TWO_PAIR (Comprovem si hi ha una combinació de Two Pair)
            rankingList = getTwoPair(player, tableCards);
            if (rankingList != null) {
                setRankingEnumAndList(player, TWO_PAIR, rankingList);
                return;
            }
            //ONE_PAIR (Comprovem si hi ha una combinació de One Pair)
            rankingList = getOnePair(player, tableCards);
            if (rankingList != null) {
                setRankingEnumAndList(player, ONE_PAIR, rankingList);
                return;
            }
            //HIGH_CARD (Si no s'ha trobat cap combinació, assignem la classificació de High Card al jugador)
            player.setRankingEnum(HIGH_CARD);
            List<Card> highCardRankingList = new ArrayList<Card>();
            highCardRankingList.add(highCard);
            player.setRankingList(highCardRankingList);
        }
    }

    public static List<Card> getRoyalFlush(IPlayer player, List<Card> tableCards) {
        // Comprovem si totes les cartes, incloent-hi les de la taula, són del mateix pal
        if (!isSameSuit(player, tableCards)) {
            // Si no, no pot ser un Royal Flush
            return null;
        }

        // Convertim les cartes del jugador i les de la taula en una llista de CardRankEnum
        List<CardRankEnum> rankEnumList = toRankEnumList(player, tableCards);

        // Comprovem si la llista conté les cartes necessàries per a un Royal Flush
        if (rankEnumList.contains(CARD_10)
                && rankEnumList.contains(JACK)
                && rankEnumList.contains(QUEEN)
                && rankEnumList.contains(KING)
                && rankEnumList.contains(ACE)) {
            // Si les conté, retornem la llista de cartes del jugador i de la taula, ja que formen un Royal Flush
            return getMergedCardList(player, tableCards);
        }

        // Si no es compleixen les condicions per a un Royal Flush, retornem null
        return null;
    }

    /**
     * Comprova si el jugador té una combinació de Straight Flush, que consisteix en cinc cartes consecutives del mateix pal.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista de les cartes que formen un Straight Flush si el jugador en té un, o null si no en té cap.
     */
    public static List<Card> getStraightFlush(IPlayer player,
                                              List<Card> tableCards) {
        // Utilitzem el mètode getSequence per aconseguir un Straight Flush amb el mateix pal
        return getSequence(player, tableCards, 5, true);
    }

    /**
     * Comprova si el jugador té una combinació de Four of a Kind, que consisteix en quatre cartes amb el mateix rang.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista de les cartes que formen un Four of a Kind si el jugador en té un, o null si no en té cap.
     */
    public static List<Card> getFourOfAKind(IPlayer player,
                                            List<Card> tableCards) {
        // Obtenim una llista de totes les cartes del jugador i de la taula
        List<Card> mergedList = getMergedCardList(player, tableCards);
        // Comprovem si hi ha una parella de quatre cartes amb el mateix rang
        return checkPair(mergedList, 4);
    }

    /**
     * Comprova si el jugador té una combinació de Full House, que consisteix en tres cartes amb el mateix rang i dues cartes amb un altre rang.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista de les cartes que formen un Full House si el jugador en té un, o null si no en té cap.
     */
    public static List<Card> getFullHouse(IPlayer player, List<Card> tableCards) {
        // Obtenim una llista de totes les cartes del jugador i de la taula
        List<Card> mergedList = getMergedCardList(player, tableCards);
        // Comprovem si hi ha una parella de tres cartes amb el mateix rang
        List<Card> threeList = checkPair(mergedList, 3);
        // Si trobem una parella de tres cartes, busquem una parella de dues cartes diferent
        if (threeList != null) {
            mergedList.removeAll(threeList);
            List<Card> twoList = checkPair(mergedList, 2);
            // Si trobem una parella de dues cartes diferent, combinem-les en una llista de Full House
            if (twoList != null) {
                threeList.addAll(twoList);
                return threeList;
            }
        }
        return null;
    }


    /**
     * Comprova si el jugador té una combinació de Flush, que consisteix en cinc cartes del mateix pal.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista de les cartes que formen un Flush si el jugador en té un, o null si no en té cap.
     */
    public static List<Card> getFlush(IPlayer player, List<Card> tableCards) {
        // Obtenim una llista de totes les cartes del jugador i de la taula
        List<Card> mergedList = getMergedCardList(player, tableCards);
        // Obtenim una llista de totes les cartes del jugador i de la taula
        List<Card> flushList = new ArrayList<Card>();

        // Iterem a través de les cartes fusionades.
        for (Card card1 : mergedList) {
            for (Card card2 : mergedList) {
                // Comprovem si dues cartes tenen el mateix pal
                if (card1.getSuit().equals(card2.getSuit())) {
                    // Afegim les cartes al Flush si encara no estan presents a la llista
                    if (!flushList.contains(card1)) {
                        flushList.add(card1);
                    }
                    if (!flushList.contains(card2)) {
                        flushList.add(card2);
                    }
                }
            }
            // Si la llista de Flush conté 5 cartes, retornem la llista
            if (flushList.size() == 5) {
                return flushList;
            }
            // En cas contrari, buidem la llista de Flush per a la següent iteració
            flushList.clear();
        }
        return null;
    }

    /**
     * Comprova si el jugador té una combinació de Straight, que consisteix en cinc cartes consecutives de valors diferents.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista de les cartes que formen un Straight si el jugador en té un, o null si no en té cap.
     */
    public static List<Card> getStraight(IPlayer player, List<Card> tableCards) {
        // Utilitzem el mètode getSequence amb compareSuit = false per comprovar un Straight
        return getSequence(player, tableCards, 5, false);
    }

    /**
     * Comprova si el jugador té una combinació de Three of a Kind, que consisteix en tres cartes del mateix valor.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista de les cartes que formen un Three of a Kind si el jugador en té un, o null si no en té cap.
     */
    public static List<Card> getThreeOfAKind(IPlayer player,
                                             List<Card> tableCards) {
        // Obtenim la llista de totes les cartes del jugador i les de la taula
        List<Card> mergedList = getMergedCardList(player, tableCards);
        // Comprovem si hi ha un grup de tres cartes del mateix valor
        return checkPair(mergedList, 3);
    }

    /**
     * Comprova si el jugador té una combinació de Two Pair, que consisteix en dos parelles de cartes del mateix valor.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista de les cartes que formen un Two Pair si el jugador en té un, o null si no en té cap.
     */
    public static List<Card> getTwoPair(IPlayer player, List<Card> tableCards) {
        // Obtenim la llista de totes les cartes del jugador i les de la taula
        List<Card> mergedList = getMergedCardList(player, tableCards);
        // Comprovem si hi ha una parella i, si és així, comprovem si hi ha una segona parella
        List<Card> twoPair1 = checkPair(mergedList, 2);
        if (twoPair1 != null) {
            mergedList.removeAll(twoPair1);
            List<Card> twoPair2 = checkPair(mergedList, 2);
            if (twoPair2 != null) {
                twoPair1.addAll(twoPair2);
                return twoPair1;
            }
        }
        return null;
    }

    /**
     * Comprova si el jugador té una combinació de One Pair, que consisteix en una parella de cartes del mateix valor.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista de les cartes que formen un One Pair si el jugador en té un, o null si no en té cap.
     */
    public static List<Card> getOnePair(IPlayer player, List<Card> tableCards) {
        // Obtenim la llista de totes les cartes del jugador i les de la taula.
        List<Card> mergedList = getMergedCardList(player, tableCards);
        // Comprovem si hi ha una parella de cartes del mateix valor.
        return checkPair(mergedList, 2);
    }

    /**
     * Obté la carta més alta entre les cartes del jugador i les de la taula.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return La carta més alta.
     */
    public static Card getHighCard(IPlayer player, List<Card> tableCards) {
        // Creem una nova llista que contingui totes les cartes, tant les del jugador com les de la taula
        List<Card> allCards = new ArrayList<Card>();
        allCards.addAll(tableCards);
        allCards.add(player.getCards()[0]);
        allCards.add(player.getCards()[1]);

        // Inicialitzem la carta més alta amb la primera carta de la llista.
        Card highCard = allCards.get(0);
        // Iterem per totes les cartes per trobar la més alta
        for (Card card : allCards) {
            if (card.getRankToInt() > highCard.getRankToInt()) {
                highCard = card;
            }
        }
        return highCard;
    }

    /**
     * Obté una seqüència de cartes consecutives, ja sigui amb o sense comparació de naipe.
     *
     * @param player       El jugador.
     * @param tableCards   Les cartes de la taula.
     * @param sequenceSize La mida de la seqüència que es busca.
     * @param compareSuit  Booleà que indica si es compara el naipe de les cartes.
     * @return Una llista de cartes que formen una seqüència consecutiva de la mida especificada, o null si no es troba cap seqüència.
     */
    private static List<Card> getSequence(IPlayer player,
                                          List<Card> tableCards, Integer sequenceSize, Boolean compareSuit) {
        // Obtenim una llista ordenada de totes les cartes del jugador i les de la taula
        List<Card> orderedList = getOrderedCardList(player, tableCards);
        // Inicialitzem una nova llista per a emmagatzemar la seqüència de cartes
        List<Card> sequenceList = new ArrayList<Card>();

        // Iterem per totes les cartes ordenades
        Card cardPrevious = null;
        for (Card card : orderedList) {
            // Comprovem si ja tenim una carta prèvia i si la diferència entre els valors de les cartes és de 1
            if (cardPrevious != null) {
                if ((card.getRankToInt() - cardPrevious.getRankToInt()) == 1) {
                    // Si no cal comparar el naipe o si els naips són iguals, afegim la carta a la seqüència
                    if (!compareSuit || cardPrevious.getSuit().equals(card.getSuit())) {
                        if (sequenceList.size() == 0) {
                            sequenceList.add(cardPrevious);
                        }
                        sequenceList.add(card);
                    }
                } else {
                    // Si la seqüència no és contínua, comprovem si la longitud de la seqüència és la desitjada
                    if (sequenceList.size() == sequenceSize) {
                        return sequenceList;
                    }
                    // Si no, buidem la seqüència per començar una nova
                    sequenceList.clear();
                }
            }
            // Actualitzem la carta prèvia amb la carta actual
            cardPrevious = card;
        }
        // Si la longitud de la seqüència és la desitjada al final de la iteració, la retornem; sinó, retornem null
        return (sequenceList.size() == sequenceSize) ? sequenceList : null;
    }


    /**
     * Combina les cartes del jugador amb les cartes de la taula per formar una única llista.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista que conté totes les cartes del jugador i les de la taula.
     */
    private static List<Card> getMergedCardList(IPlayer player,
                                                List<Card> tableCards) {
        List<Card> merged = new ArrayList<Card>();
        merged.addAll(tableCards);
        merged.add(player.getCards()[0]);
        merged.add(player.getCards()[1]);
        return merged;
    }

    /**
     * Ordena una llista de cartes segons el seu valor.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista de cartes ordenada segons el seu valor.
     */
    private static List<Card> getOrderedCardList(IPlayer player,
                                                 List<Card> tableCards) {
        List<Card> ordered = getMergedCardList(player, tableCards);
        Collections.sort(ordered, new Comparator<Card>() {

            public int compare(Card c1, Card c2) {
                return c1.getRankToInt() < c2.getRankToInt() ? -1 : 1;
            }

        });
        return ordered;
    }

    /**
     * Comprova si hi ha un parell de cartes en una llista.
     *
     * @param mergedList La llista de cartes en la qual es comprova si hi ha un parell.
     * @param pairSize   La mida del parell que es vol buscar.
     * @return Una llista que conté les cartes del parell si n'hi ha, o null si no se n'hi troba cap.
     */
    private static List<Card> checkPair(List<Card> mergedList, Integer pairSize) {
        List<Card> checkedPair = new ArrayList<Card>();
        for (Card card1 : mergedList) {
            checkedPair.add(card1);
            for (Card card2 : mergedList) {
                if (!card1.equals(card2)
                        && card1.getRank().equals(card2.getRank())) {
                    checkedPair.add(card2);
                }
            }
            if (checkedPair.size() == pairSize) {
                return checkedPair;
            }
            checkedPair.clear();
        }
        return null;
    }

    /**
     * Comprova si les cartes del jugador i les de la taula són del mateix naipe.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Cert si totes les cartes són del mateix naipe, sinó fals.
     */
    private static Boolean isSameSuit(IPlayer player, List<Card> tableCards) {
        CardSuitEnum suit = player.getCards()[0].getSuit();

        if (!suit.equals(player.getCards()[1].getSuit())) {
            return false;
        }

        for (Card card : tableCards) {
            if (!card.getSuit().equals(suit)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converteix les cartes del jugador i les de la taula a una llista d'enumeracions de rang de cartes.
     *
     * @param player     El jugador.
     * @param tableCards Les cartes de la taula.
     * @return Una llista d'enumeracions de rang de cartes.
     */
    private static List<CardRankEnum> toRankEnumList(IPlayer player,
                                                     List<Card> tableCards) {
        List<CardRankEnum> rankEnumList = new ArrayList<CardRankEnum>();

        for (Card card : tableCards) {
            rankEnumList.add(card.getRank());
        }

        rankEnumList.add(player.getCards()[0].getRank());
        rankEnumList.add(player.getCards()[1].getRank());

        return rankEnumList;
    }

    /**
     * Estableix el tipus de classificació i la llista de classificació d'un jugador.
     *
     * @param player      El jugador.
     * @param rankingEnum El tipus de classificació.
     * @param rankingList La llista de classificació.
     */
    private static void setRankingEnumAndList(IPlayer player,
                                              RankingEnum rankingEnum, List<Card> rankingList) {
        player.setRankingEnum(rankingEnum);
        player.setRankingList(rankingList);
    }
}
