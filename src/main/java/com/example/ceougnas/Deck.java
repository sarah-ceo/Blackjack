package com.example.ceougnas;

import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;

public class Deck {
    private LinkedList<Card> cardList = new LinkedList<Card>();

    public Deck(int nbBox){
        for(int i=0; i<nbBox; i++){
            for (int j=0; j<Value.values().length; j++){
                for (int k=0; k<Color.values().length; k++){
                    cardList.add(new Card(Value.values()[j],Color.values()[k]));
                }
            }
        }
        Collections.shuffle(cardList);
    }

    public String toString(){
        String deck = "";
        for (int i=0; i<cardList.size(); i++){
            deck += cardList.get(i).toString() + ", ";
        }
        return deck;
    }

    public Card draw() throws EmptyDeckException {
        Card firstCard = null;
        if (cardList.size()>0){
            firstCard=cardList.pollFirst();
        } else{
            throw new EmptyDeckException("The deck is empty !");
        }
        return firstCard;
    }
}
