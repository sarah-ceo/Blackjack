package com.example.ceougnas;

import java.util.LinkedList;
import java.util.List;

public class Hand {
    private LinkedList<Card> cardList = new LinkedList<Card>();

    public Hand(){

    }
    public String toString(){
        /*String cards = "[";
        //String points = "[";
        for (int i=0; i<cardList.size(); i++){
            cards += cardList.get(i).toString();
            //points += cardList.get(i).getPoints();
            if (i<cardList.size()-1){
                cards+= ", ";
                //points+= ", ";
            }
        }
        cards+="]";
        //points+="]";
        String hand = cards + " : " + count() +"\n The best score is : "+ best();*/
        String hand = "The best score is : "+ best();
        return hand;
    }

    public void add(Card card){
        cardList.add(card);
    }

    public void clear(){
        cardList.clear();
    }

    public List<Integer> count(){
        List<Integer> scores = new LinkedList<Integer>();
        int nbAces = 0;
        for (int i=0; i<cardList.size(); i++){
            if(cardList.get(i).getValueSymbole() == "A"){
                nbAces += 1;
            }
        }
        for (int i=0; i<=nbAces; i++){
            int score = 0;
            for (int j=0; j<cardList.size(); j++){
                score += cardList.get(j).getPoints();
            }
            score += i*10;
            scores.add(score);
        }

        return scores;
    }

    public int best(){
        int bestScore = -1;
        List<Integer> scores = count();
        if (scores.size()>1){
            List<Integer> scoresUnder21 = new LinkedList<Integer>();
            for (int i=0; i<scores.size(); i++){
                if (scores.get(i)<=21){
                    scoresUnder21.add(scores.get(i));
                }
            }
            if (!scoresUnder21.isEmpty()){
                for (int i=0; i<scoresUnder21.size(); i++) {
                    if (scoresUnder21.get(i) > bestScore) {
                        bestScore = scoresUnder21.get(i);
                    }
                }
            }else{
                for (int i=0; i<scores.size(); i++) {
                    if (scores.get(i) > bestScore) {
                        bestScore = scores.get(i);
                    }
                }
            }
        }else{
            bestScore = scores.get(0);
        }
        return bestScore;
    }

    public List getCardList(){
        return this.cardList;
    }
}
