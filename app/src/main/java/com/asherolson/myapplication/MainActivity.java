//Asher Olson
//07/2020

//after taking an Android Development course on codecademy.com, I made this BlackJack app

package com.asherolson.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public BlackJack game;
    public ArrayList<ImageView> dealerCards;
    public ArrayList<ImageView> playerCards;
    public ArrayList<ImageView> splitCards1;
    public ArrayList<ImageView> splitCards2;
    public int nextDealerInd = 2;
    public int nextPlayerInd = 2;
    public int nextSplit1Ind;
    public int nextSplit2Ind;
    public int chipCount = 0;
    public int playerBet;
    public int origBet;
    public int split1Bet;
    public int split2Bet;
    public int count;
    public Button betButton;
    public EditText betEditText;
    public Button hitButton;
    public Button standButton;
    public Button splitButton;
    public Button doubleButton;
    public TextView chipCountTextView;
    public TextView playerCount;
    public TextView dealerCount;
    public TextView gameTerminal;
    public TextView displayBet;
    public Button countView;
    public boolean evaluated;
    public boolean isSplit;
    public boolean firstHand;
    public boolean countShowing;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        betButton = (Button) findViewById(R.id.betButton);
        betEditText = (EditText) findViewById(R.id.betEditText);
        chipCountTextView = (TextView) findViewById(R.id.playerChipView);
        playerCount = (TextView) findViewById(R.id.playerCount);
        dealerCount = (TextView) findViewById(R.id.dealerCount);
        countView = (Button) findViewById(R.id.showCount);
        hitButton = (Button) findViewById(R.id.hit);
        standButton = (Button) findViewById(R.id.stand);
        splitButton = (Button) findViewById(R.id.split);
        doubleButton = (Button) findViewById(R.id.doubleDown);
        displayBet = (TextView) findViewById(R.id.displayBet);
        gameTerminal = (TextView) findViewById(R.id.gameTerminal);
        isSplit = false;
        firstHand = true;



        // set dealer card ImageView array
        dealerCards = new ArrayList<ImageView>();
        dealerCards.add((ImageView) findViewById(R.id.dealerCard1));
        dealerCards.add((ImageView) findViewById(R.id.dealerCard2));
        dealerCards.add((ImageView) findViewById(R.id.dealerCard3));
        dealerCards.add((ImageView) findViewById(R.id.dealerCard4));
        dealerCards.add((ImageView) findViewById(R.id.dealerCard5));
        dealerCards.add((ImageView) findViewById(R.id.dealerCard6));
        dealerCards.add((ImageView) findViewById(R.id.dealerCard7));
        dealerCards.add((ImageView) findViewById(R.id.dealerCard8));
        dealerCards.add((ImageView) findViewById(R.id.dealerCard9));

        // set player card ImageView array
        playerCards = new ArrayList<ImageView>();
        playerCards.add((ImageView) findViewById(R.id.playerCard1));
        playerCards.add((ImageView) findViewById(R.id.playerCard2));
        playerCards.add((ImageView) findViewById(R.id.playerCard3));
        playerCards.add((ImageView) findViewById(R.id.playerCard4));
        playerCards.add((ImageView) findViewById(R.id.playerCard5));
        playerCards.add((ImageView) findViewById(R.id.playerCard6));
        playerCards.add((ImageView) findViewById(R.id.playerCard7));
        playerCards.add((ImageView) findViewById(R.id.playerCard8));
        playerCards.add((ImageView) findViewById(R.id.playerCard9));

        // set split ImageView arrays
        splitCards1 = new ArrayList<ImageView>();
        splitCards1.add((ImageView) findViewById(R.id.splitCard1Hand1));
        splitCards1.add((ImageView) findViewById(R.id.splitCard2Hand1));
        splitCards1.add((ImageView) findViewById(R.id.splitCard3Hand1));
        splitCards1.add((ImageView) findViewById(R.id.splitCard4Hand1));
        splitCards1.add((ImageView) findViewById(R.id.splitCard5Hand1));

        splitCards2 = new ArrayList<ImageView>();
        splitCards2.add((ImageView) findViewById(R.id.splitCard1Hand2));
        splitCards2.add((ImageView) findViewById(R.id.splitCard2Hand2));
        splitCards2.add((ImageView) findViewById(R.id.splitCard3Hand2));
        splitCards2.add((ImageView) findViewById(R.id.splitCard4Hand2));
        splitCards2.add((ImageView) findViewById(R.id.splitCard5Hand2));

        betButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bet = betEditText.getText().toString();
                if(bet.length() == 0){
                    // catch empty text box
                }
                else if(Integer.parseInt(bet) <= chipCount && Integer.parseInt(bet) > 0){
                    evaluated = false;
                    playerBet = Integer.parseInt(bet);
                    origBet = playerBet;
                    betEditText.setText("");
                    betButton.setAlpha((float) 0.3);
                    betButton.setClickable(false);
                    displayBet.setText("Bet: "+ bet);
                    displayBet.setVisibility(View.VISIBLE);
                    chipCount -= playerBet;
                    chipCountTextView.setText("Chips: "+ chipCount);
                    //testView.setText(""+ playerBet);
                    startNewHand();
                }
            }
        });

        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSplit){
                    if(firstHand){
                        handler.postDelayed(splitHand1Runnable, 300);
                    } else {
                        handler.postDelayed(splitHand2Runnable, 300);
                    }
                } else {
                    handler.postDelayed(playerHitRunnable, 300);
                }
            }
        });

        standButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSplit){
                    if(firstHand){
                        firstHand = false;
                        gameTerminal.setText("Option on second hand...");
                        updateHandValue("splitHand2");
                        doubleButton.setAlpha((float) 1.0);
                        doubleButton.setClickable(true);
                    } else {
                        evaluated = true;
                        evaluateResult();
                    }
                } else {
                    evaluated = true;
                    evaluateResult();
                }
            }
        });

        splitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSplit = true;
                split();
            }
        });


        doubleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doubleDown();
            }
        });

        countView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShowing){
                    countView.setText("Show count");
                    countShowing = false;
                } else {
                    if(count > 0){
                        countView.setText("+"+ count);
                    } else {
                        countView.setText(""+ count);
                    }
                    countShowing = true;
                }
            }
        });

        startNewGame(6);
    }

    private Runnable playerHitRunnable = new Runnable() {
        @Override
        public void run() {
            hit("player");
        }
    };

    private Runnable dealerHitRunnable = new Runnable() {
        @Override
        public void run() {
            hit("dealer");
        }
    };

    private Runnable splitHand1Runnable = new Runnable() {
        @Override
        public void run() {
            hit("splitHand1");
        }
    };

    private Runnable splitHand2Runnable = new Runnable() {
        @Override
        public void run() {
            hit("splitHand2");
        }
    };

    private Runnable revealDealerCard = new Runnable() {
        @Override
        public void run() {
            dealerCards.get(1).setImageResource(findDrawableId(game.dealerHand.get(1)));
        }
    };



    public void startNewGame(int deckSize){
        game = new BlackJack(deckSize);
        if(chipCount == 0){
            chipCount = 3000;
        }
        chipCountTextView.setText("Chips: "+ chipCount);
        count = 0;
        countShowing = false;
    }

    public void startNewHand(){
        gameTerminal.setText("Dealer must stand on soft 17");
        playerCount.setVisibility(View.VISIBLE);
        dealerCount.setVisibility(View.VISIBLE);
        for(int i = nextPlayerInd -1; i >= 0; i--){
            playerCards.get(i).setVisibility(View.INVISIBLE);
        }
        for(int j = nextDealerInd -1; j >= 0; j--){
            dealerCards.get(j).setVisibility(View.INVISIBLE);
        }
        if(isSplit) {
            for (int k = nextSplit1Ind - 1; k >= 0; k--) {
                splitCards1.get(k).setVisibility(View.INVISIBLE);
            }
            for (int m = nextSplit2Ind - 1; m >= 0; m--) {
                splitCards2.get(m).setVisibility(View.INVISIBLE);
            }
        }
        nextDealerInd = 0;
        nextPlayerInd = 0;
        nextSplit1Ind = 0;
        nextSplit2Ind = 0;
        isSplit = false;
        firstHand = true;
        game.newHand();
        hit("player");
        hit("dealer");
        hit("player");
        game.hit("dealer");
        dealerCards.get(1).setImageResource(R.drawable.cardback);
        dealerCards.get(1).setVisibility(View.VISIBLE);
        nextDealerInd++;
        if(game.handCheck("player") == 21){
            evaluated = true;
            evaluateResult();
        } else {
            hitButton.setAlpha((float) 1.0);
            hitButton.setClickable(true);
            standButton.setAlpha((float) 1.0);
            standButton.setClickable(true);
            if (("" + game.playerHand.get(0).charAt(1)).equals("" + game.playerHand.get(1).charAt(1))) {
                splitButton.setAlpha((float) 1.0);
                splitButton.setClickable(true);
            }
            if(playerBet <= chipCount){
                doubleButton.setAlpha((float) 1.0);
                doubleButton.setClickable(true);
            }

        }
    }

    public void hit(String player){
        String newCard = game.hit(player);
        String val = ""+ newCard.charAt(1);
        updateHandValue(player);
        setNextCard(player, newCard);
        if(val.equals("A") || val.equals("K") || val.equals("Q") || val.equals("J") || val.equals("T")){
            count--;
        }
        else if(Integer.parseInt(val) < 7){
            count++;
        }
        if(countShowing){
            if(count > 0){
                countView.setText("+"+ count);
            } else {
                countView.setText(""+ count);
            }
        }
        if(player.equals("player") && nextPlayerInd >= 2){
            splitButton.setAlpha((float) 0.3);
            splitButton.setClickable(false);
            doubleButton.setAlpha((float) 0.3);
            doubleButton.setClickable(false);
        }
        if(isSplit){
            if(firstHand){
                if(nextSplit1Ind >= 2){
                    doubleButton.setAlpha((float) 0.3);
                    doubleButton.setClickable(false);
                }
                if(game.handCheck("splitHand1") >= 22){
                    firstHand = false;
                    doubleButton.setAlpha((float) 1.0);
                    doubleButton.setClickable(true);
                    gameTerminal.setText("Option on second hand...");
                }
            } else {
                if(nextSplit2Ind >= 3){
                    doubleButton.setAlpha((float) 0.3);
                    doubleButton.setClickable(false);
                }
                if(game.handCheck("splitHand2") >= 22){
                    evaluated = true;
                    evaluateResult();
                }
            }
        }
        if(game.handCheck(player) >= 22 && player.equals("player")){
            evaluated = true;
            evaluateResult();
        }
    }

    public void doubleDown(){
        if(isSplit){
            chipCount -= origBet;
            chipCountTextView.setText("Chips: "+ chipCount);
            playerBet += origBet;
            displayBet.setText("Bet: "+ playerBet);
            if(firstHand){
                firstHand = false;
                split1Bet *= 2;
                hit("splitHand1");
                doubleButton.setAlpha((float) 1.0);
                doubleButton.setClickable(true);
            } else {
                split2Bet *= 2;
                hit("splitHand2");
                if(!evaluated){
                    evaluated = true;
                    evaluateResult();
                }
            }
        } else {
            chipCount -= playerBet;
            chipCountTextView.setText("Chips: " + chipCount);
            playerBet *= 2;
            displayBet.setText("Bet: " + playerBet);
            hit("player");
            if (!evaluated) {
                evaluated = true;
                evaluateResult();
            }
        }
    }

    public void split(){
        splitButton.setAlpha((float) 0.3);
        splitButton.setClickable(false);
        split1Bet = playerBet;
        split2Bet = playerBet;
        chipCount -= playerBet;
        chipCountTextView.setText("Chips: "+ chipCount);
        playerBet *= 2;
        displayBet.setText("Bet: "+ playerBet);
        game.split();
        playerCards.get(1).setVisibility(View.INVISIBLE);
        splitCards2.get(0).setImageResource(findDrawableId(game.splitHand2.get(0)));
        splitCards2.get(0).setVisibility(View.VISIBLE);
        nextSplit2Ind++;
        // TODO: make delay
        hit("splitHand1");
        hit("splitHand2");
        gameTerminal.setText("Option on first hand...");
        // now wait for hit
    }

    public void updateHandValue(String player){
        int handVal = game.handCheck(player);
        if(player.equals("player") || player.equals("splitHand1") || player.equals("splitHand2")){
            if(game.playerSoft){
                playerCount.setText("Soft " + handVal);
            } else {
                playerCount.setText("" + handVal);
            }
        } else {
            if(game.dealerSoft){
                dealerCount.setText("Soft " + handVal);
            } else {
                dealerCount.setText("" + handVal);
            }
        }
    }

    public void setNextCard(String player, String card){
        int id = findDrawableId(card);
        if(player.equals("player")){
            playerCards.get(nextPlayerInd).setImageResource(id);
            playerCards.get(nextPlayerInd).setVisibility(View.VISIBLE);
            nextPlayerInd++;
        }
        else if(player.equals("splitHand1")){
            splitCards1.get(nextSplit1Ind).setImageResource(id);
            splitCards1.get(nextSplit1Ind).setVisibility(View.VISIBLE);
            nextSplit1Ind++;
        }
        else if(player.equals("dealer")){
            dealerCards.get(nextDealerInd).setImageResource(id);
            dealerCards.get(nextDealerInd).setVisibility(View.VISIBLE);
            nextDealerInd++;
        } else {
            splitCards2.get(nextSplit2Ind).setImageResource(id);
            splitCards2.get(nextSplit2Ind).setVisibility(View.VISIBLE);
            nextSplit2Ind++;
        }
    }

    // returns int id of drawable to set in setNextCard()
    public int findDrawableId(String card){
        char suit = card.charAt(0);
        char val = card.charAt(1);
        int id = 0;
        switch(suit){
            case 'C':
                switch(val){
                    case 'A':
                        id = R.drawable.ca;
                        break;
                    case '2':
                        id = R.drawable.c2;
                        break;
                    case '3':
                        id = R.drawable.c3;
                        break;
                    case '4':
                        id = R.drawable.c4;
                        break;
                    case '5':
                        id = R.drawable.c5;
                        break;
                    case '6':
                        id = R.drawable.c6;
                        break;
                    case '7':
                        id = R.drawable.c7;
                        break;
                    case '8':
                        id = R.drawable.c8;
                        break;
                    case '9':
                        id = R.drawable.c9;
                        break;
                    case 'T':
                        id = R.drawable.ct;
                        break;
                    case 'J':
                        id = R.drawable.cj;
                        break;
                    case 'Q':
                        id = R.drawable.cq;
                        break;
                    case 'K':
                        id = R.drawable.ck;
                        break;
                }
                break;
            case 'D':
                switch(val){
                    case 'A':
                        id = R.drawable.da;
                        break;
                    case '2':
                        id = R.drawable.d2;
                        break;
                    case '3':
                        id = R.drawable.d3;
                        break;
                    case '4':
                        id = R.drawable.d4;
                        break;
                    case '5':
                        id = R.drawable.d5;
                        break;
                    case '6':
                        id = R.drawable.d6;
                        break;
                    case '7':
                        id = R.drawable.d7;
                        break;
                    case '8':
                        id = R.drawable.d8;
                        break;
                    case '9':
                        id = R.drawable.d9;
                        break;
                    case 'T':
                        id = R.drawable.dt;
                        break;
                    case 'J':
                        id = R.drawable.dj;
                        break;
                    case 'Q':
                        id = R.drawable.dq;
                        break;
                    case 'K':
                        id = R.drawable.dk;
                        break;
                }
                break;
            case 'H':
                switch(val){
                    case 'A':
                        id = R.drawable.ha;
                        break;
                    case '2':
                        id = R.drawable.h2;
                        break;
                    case '3':
                        id = R.drawable.h3;
                        break;
                    case '4':
                        id = R.drawable.h4;
                        break;
                    case '5':
                        id = R.drawable.h5;
                        break;
                    case '6':
                        id = R.drawable.h6;
                        break;
                    case '7':
                        id = R.drawable.h7;
                        break;
                    case '8':
                        id = R.drawable.h8;
                        break;
                    case '9':
                        id = R.drawable.h9;
                        break;
                    case 'T':
                        id = R.drawable.ht;
                        break;
                    case 'J':
                        id = R.drawable.hj;
                        break;
                    case 'Q':
                        id = R.drawable.hq;
                        break;
                    case 'K':
                        id = R.drawable.hk;
                        break;
                }
                break;
            default:
                switch(val){
                    case 'A':
                        id = R.drawable.sa;
                        break;
                    case '2':
                        id = R.drawable.s2;
                        break;
                    case '3':
                        id = R.drawable.s3;
                        break;
                    case '4':
                        id = R.drawable.s4;
                        break;
                    case '5':
                        id = R.drawable.s5;
                        break;
                    case '6':
                        id = R.drawable.s6;
                        break;
                    case '7':
                        id = R.drawable.s7;
                        break;
                    case '8':
                        id = R.drawable.s8;
                        break;
                    case '9':
                        id = R.drawable.s9;
                        break;
                    case 'T':
                        id = R.drawable.st;
                        break;
                    case 'J':
                        id = R.drawable.sj;
                        break;
                    case 'Q':
                        id = R.drawable.sq;
                        break;
                    case 'K':
                        id = R.drawable.sk;
                        break;
                }
                break;
        }
        return id;
    }

    private void hitDealer(){
        handler.postDelayed(dealerHitRunnable, 200);
    }

    public void evaluateResult(){
        // finalize player and dealer cards and find hand values
        int dealerHandCount;
        String val;
        // variables for split case:
        int splitCount;
        int currentBet;
        int winnings = 0;
        String[] splitHands;
        //*****************
        int playerHandCount = game.handCheck("player");
        if(playerHandCount == 21 && game.playerHand.size() == 2){
            gameTerminal.setText("Blackjack!");
        } else {
            handler.postDelayed(revealDealerCard, 400);
            //dealerCards.get(1).setImageResource(findDrawableId(game.dealerHand.get(1)));
            val = ""+ game.dealerHand.get(1).charAt(1);
            updateHandValue("dealer");
            if(val.equals("A") || val.equals("K") || val.equals("Q") || val.equals("J") || val.equals("T")){
                count--;
            }
            else if(Integer.parseInt(val) < 7){
                count++;
            }
            if(countShowing){
                if(count > 0){
                    countView.setText("+"+ count);
                } else {
                    countView.setText(""+ count);
                }
            }
            while(game.handCheck("dealer") < 17){
                hit("dealer");
            }
            dealerHandCount = game.handCheck("dealer");
        }

        dealerHandCount = game.handCheck("dealer");

        // fade buttons
        hitButton.setClickable(false);
        hitButton.setAlpha((float) 0.3);
        standButton.setClickable(false);
        standButton.setAlpha((float) 0.3);
        splitButton.setAlpha((float) 0.3);
        splitButton.setClickable(false);
        doubleButton.setAlpha((float) 0.3);
        doubleButton.setClickable(false);

        // evaluate bet result

        if(isSplit){
            splitHands = new String[2];
            splitHands[0] = "splitHand1";
            splitHands[1] = "splitHand2";
            for(int i = 0; i < splitHands.length; i++){
                splitCount = game.handCheck(splitHands[i]);
                if(i == 0){
                    currentBet = split1Bet;
                } else {
                    currentBet = split2Bet;
                }
                if((dealerHandCount == splitCount) && splitCount < 22){
                    chipCount += currentBet;
                }
                else if(splitCount < 22){
                    if(dealerHandCount > 21 || splitCount > dealerHandCount){
                        chipCount += (currentBet * 2);
                        winnings += (currentBet * 2);
                    } else {
                        winnings -= currentBet;
                    }
                } else {
                    winnings -= currentBet;
                }
            }
            if(winnings > 0){
                gameTerminal.setText("You made some money, nice");
                displayBet.setText("+"+ winnings);
            }
            else if(winnings == 0){
                gameTerminal.setText("You made your bet back");
                displayBet.setText("+0");
            } else {
                gameTerminal.setText("L");
                displayBet.setText(""+ winnings);
            }
        }
        else if(game.playerHand.size() == 2 && playerHandCount == 21){
            chipCount += (playerBet * 2.5);
            displayBet.setText("+"+ (playerBet * 1.5));
        }
        else if((dealerHandCount == playerHandCount) && playerHandCount < 22){
            gameTerminal.setText("Push");
            chipCount += playerBet;
            displayBet.setText("+0");
        }
        else if(playerHandCount < 22){
            if(dealerHandCount > 21 || playerHandCount > dealerHandCount){
                gameTerminal.setText("You win!");
                chipCount += (playerBet * 2);
                displayBet.setText("+"+ playerBet);
            } else {
                gameTerminal.setText(R.string.dealerWins);
                displayBet.setText("-" + playerBet);
            }
        } else {
            System.out.println("in player loss");
            gameTerminal.setText("Dealer wins, better luck next time");
            displayBet.setText("-" + playerBet);
        }


        betButton.setAlpha((float) 1.0);
        betButton.setClickable(true);
        if(chipCount == 0){
            chipCount = 3000;
            gameTerminal.setText("I just need to get even...");
        }
        if(game.deck.size() <= 10){
//            betButton.setAlpha((float) 0.3);
//            betButton.setClickable(false);
            startNewGame(3);
        }
        chipCountTextView.setText("Chips: "+ chipCount);
    }
}