package Carddeck;

import java.util.Random;

public class Deck {private int MAX_CARDS = 36;

private Card[] cards;
private int nextIndex;

public Deck() {
	
	cards = new Card[MAX_CARDS];
    int c = 0;

    for (int s = 0; s < Suit.values.length; s++) {

    	for (int r = 0; r < Rank.values.length; r++) {

            cards[c] = new Card(Rank.values[r], Suit.values[s]);
            System.out.println("1-" + c + ": " + cards[c].getSuit().getName() + " " + cards[c].getRank().getName());
            c++;
        }
    }
}

// accidentally (randomly) shuffle the deck

public void shuffle() {

 	if(cards != null) {

   		int length = cards.length;

   		Random generator = new Random(System.currentTimeMillis());
   		
   		for(int i = 0; i < length; i++) {

   			int newPos = generator.nextInt(length);       			 
   			while (newPos == i) { 
   				newPos = generator.nextInt(length);        				 
   			}
   		    System.out.print(">>>" + cards[i] + "=" + cards[newPos]);
            Card currentCard = cards[i];
            cards[i] = cards[newPos];
            cards[newPos] = currentCard;
            //System.out.println("::" + cards[i] + "=" + cards[newPos]);
   		}
   		for(int k = 0; k < length; k++) System.out.println("2." + k + ": " + cards[k]);
   	}       	 
}
// Arrange the deck of cards by suit and value 
public void order() {    	 
	for (int j = 0; j < cards.length; j++) {
		for (int k = 0; k < cards.length -1; k++) {
			int resultCompare = compare(cards[k], cards[k+1]);
			//System.out.println("Compare-1: " + resultCompare + " " + cards[k] + " " + cards[k+1]);    			 
			if (resultCompare == 0) continue;
			else if (resultCompare > 0) {
				Card cardTranzit = cards[k];
				cards[k] = cards[k+1];
				cards[k+1] = cardTranzit;
				//System.out.println("Compare-2: " + resultCompare + " " + cards[k] + " " + cards[k+1]);
			}
		}
	}
}

//Returns true when cards are still available in the deck
public boolean hasNext() {

    if (nextIndex >= cards.length) {
        return false;
    } else {
        return true;
    }
}

//"Removes" one card from the deck, when all 36 cards are issued returns null
//The cards are removed from the "top" of the deck. For example, the first call will give SPADES 6 later
//SPADES 7, ..., CLUBS 6, ..., CLUBS Ace and so on to HEARTS Ace

public Card drawOne() {
    if (hasNext()) {    	
        Card card = cards[cards.length - nextIndex - 1];
        nextIndex++;
        return card;
    } else {
        return null;
    }
}

int getRankIndex(Rank rank) {
    int index = 0;
    for (int i = 0; i < Rank.values.length; i++) {
        if (rank == Rank.values[i]) {
            index = i;
            break;
        }
    }
    return index;
}

int getSuitIndex(Suit rank) {
    int index = 0;
    for (int i = 0; i < Suit.values.length; i++) {
        if (rank == Suit.values[i]) {
            index = i;
            break;
        }
    }
    return index;
}

int compare(Card card1, Card card2) {
    int suitIndex1 = getSuitIndex(card1.getSuit());
    int suitIndex2 = getSuitIndex(card2.getSuit());
    if (suitIndex1 < suitIndex2) {
        return -1;
    }

    if (suitIndex1 > suitIndex2) {
        return 1;
    }

    // Suits are equal, now compare ranks.

    int rankIndex1 = getRankIndex(card1.getRank());
    int rankIndex2 = getRankIndex(card2.getRank());
    if (rankIndex1 < rankIndex2) {
        return -1;
    }

    if (rankIndex1 > rankIndex2) {
        return 1;
    }

    // Ranks are also equal, return 0.

    return 0;
}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deck deck = new Deck();
        deck.shuffle();
        deck.order();

        while (deck.hasNext()) {
            Card card = deck.drawOne();
            System.out.println("4- " + card.getSuit().getName() + " " + card.getRank().getName());
            //System.out.println("Compare " + deck.cards[2] + " and " + deck.cards[3]);
	        //System.out.println(deck.compare(deck.cards[2], deck.cards[3]));
 	    }
	}
}
