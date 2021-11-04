package com.asherolson.myapplication;

import java.util.ArrayList;
import java.util.Random;

public class BlackJack {
    public ArrayList<String> deck = new ArrayList<String>();
    public ArrayList<String> playerHand = new ArrayList<String>();
    public ArrayList<String> dealerHand = new ArrayList<String>();
    public ArrayList<String> splitHand1 = new ArrayList<String>();
    public ArrayList<String> splitHand2 = new ArrayList<String>();
    public boolean dealerSoft;
    public boolean playerSoft;
    //public int count;

    public BlackJack(int deckSize){
        String[] suits = {"D", "H", "S", "C"};
        String[] vals = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
        for(int i = 0; i < deckSize; i++){
            for(int j = 0; j < suits.length; j++){
                for(int k = 0; k < vals.length; k++){
                    deck.add(suits[j] + vals[k]);
                }
            }
        }
        playerSoft = false;
        dealerSoft = false;
        //count = 0;
    }

    public String hit(String hand){
        Random ran = new Random();
        int deckInd = ran.nextInt(deck.size());
        //int deckInd = 1;
        String card = deck.get(deckInd);
       // String val = ""+ card.charAt(1);
//        if(val.equals("A") || val.equals("K") || val.equals("Q") || val.equals("J") || val.equals("T")){
//            count--;
//        }
//        else if(Integer.parseInt(val) < 7){
//            count++;
//        }
        deck.remove(deckInd);
        if(hand.equals("dealer")){
            dealerHand.add(card);
        }
        else if(hand.equals("splitHand1")){
            splitHand1.add(card);
        }
        else if(hand.equals("splitHand2")){
            splitHand2.add(card);
        } else {
            playerHand.add(card);
        }
        return card;
    }

    public void split(){
        splitHand1.add(playerHand.get(0));
        splitHand2.add(playerHand.get(1));
        playerHand.clear();
    }

    public int handCheck(String player){
        ArrayList<String> hand;
        if(player.equals("player")){
            hand = playerHand;
        }
        else if(player.equals("dealer")){
            hand = dealerHand;
        }
        else if(player.equals("splitHand1")){
            hand = splitHand1;
        } else {
            hand = splitHand2;
        }
        ArrayList<String> handValsOnly = new ArrayList<String>();
        int numAces = 0;
        int totalVal = 0;
        for(int i = 0; i < hand.size(); i++){
            handValsOnly.add(""+hand.get(i).charAt(1));
        }
        for(int j = 0; j < handValsOnly.size(); j++){
            if(handValsOnly.get(j).equals("A")){
                numAces++;
            }
            else if(handValsOnly.get(j).equals("K") ||
                    handValsOnly.get(j).equals("Q") ||
                    handValsOnly.get(j).equals("J") ||
                    handValsOnly.get(j).equals("T")){
                totalVal += 10;
            } else {
                totalVal += Integer.parseInt(handValsOnly.get(j));
            }
        }
        for(int k = 0; k < numAces; k++){
            if(k != numAces -1){
                totalVal += 1;
            }
            else if(totalVal + 11 <= 21){
                totalVal += 11;
                if(player.equals("player") || player.equals("splitHand1") || player.equals("splitHand2")){
                    playerSoft = true;
                } else {
                    dealerSoft = true;
                }
            } else {
                totalVal += 1;
                if(player.equals("player") || player.equals("splitHand1") || player.equals("splitHand2")){
                    playerSoft = false;
                } else {
                    dealerSoft = false;
                }
            }
        }
        return totalVal;
    }

    public void newHand(){
        playerHand.clear();
        dealerHand.clear();
        splitHand1.clear();
        splitHand2.clear();
        dealerSoft = false;
        playerSoft = false;
    }

    public static void main(String[] args){
        BlackJack a = new BlackJack(1);
        System.out.println(a.playerHand);
        System.out.println(a.dealerHand);
        System.out.println("" + a.handCheck("dealer"));
        System.out.println("" + a.handCheck("player"));
    }
}
