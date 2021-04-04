package com.example.ceougnas;

import java.util.LinkedList;
import java.util.List;

public class BlackJack {
    private Deck deck;
    private Hand playerHand;
    private Hand bankHand;
    private int cagnotte = 5000;
    public boolean gameFinished;

    public BlackJack(int nbJeux) throws EmptyDeckException {
        this.deck = new Deck(nbJeux);
        this.playerHand = new Hand();
        this.bankHand = new Hand();
        this.gameFinished = false;
        this.reset();
    }

    public void reset() throws EmptyDeckException {
        this.bankHand.clear();
        this.playerHand.clear();
        this.gameFinished = false;
        this.bankHand.add(this.deck.draw());
        for (int i=0; i<2; i++){
            this.playerHand.add(deck.draw());
        }
    }

    public void resetDeck(int nbJeux){
        this.deck = new Deck(nbJeux);
    }

    public String getPlayerHandString(){
        return this.playerHand.toString();
    }

    public String getBankHandString(){
        return this.bankHand.toString();
    }

    public int getPlayerBest(){
        return this.playerHand.best();
    }

    public int getBankBest(){
        return this.bankHand.best();
    }

    public boolean isPlayerWinner(){
        if (isGameFinished())
            if (getPlayerBest()<=21 ){
                if (getBankBest()>21) {
                    return true;
                } else {
                    return Math.abs(getPlayerBest()-21) < Math.abs(getBankBest()-21);
                }
        }
        return false;
    }

    public boolean isBankWinner(){
        if (isGameFinished())
            if (getPlayerBest()>21) {
                return true;
            } else if (getBankBest()>21) {
                return false;
            } else {
                return Math.abs(getBankBest()-21) < Math.abs(getPlayerBest()-21);
            }
        return false;
    }

    public boolean isGameFinished(){
        return gameFinished;
    }

    public void playerDrawAnotherCard() throws EmptyDeckException {
        this.playerHand.add(deck.draw());
        if (getPlayerBest()>21){
            gameFinished = true;
        }
    }

    public void bankLastTurn() throws EmptyDeckException {
        if (!isGameFinished() && getPlayerBest()<=21 && getBankBest()<=21){
            while(getBankBest()<=17){ //getPlayerBest()
                this.bankHand.add(this.deck.draw());
            }
        }
        gameFinished = true;
    }

    public List getPlayerCardList(){
        LinkedList<Card> CardListCopy = new LinkedList<Card>(this.playerHand.getCardList());
        return CardListCopy;
    }

    public List getBankCardList(){
        LinkedList<Card> CardListCopy = new LinkedList<Card>(this.bankHand.getCardList());
        return CardListCopy;
    }

    public int getCagnotte(){
        return this.cagnotte;
    }

    public void setCagnotte(int montant){
        this.cagnotte = montant;
    }

    public void addToCagnotte(int mise){
        this.cagnotte += mise;
    }
}
