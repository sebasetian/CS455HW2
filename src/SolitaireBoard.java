// Name: SHIH-YAO LIN
// USC NetID: shihyaol
// CSCI455 PA2
// Spring 2018

import java.util.ArrayList;
import java.util.Random;

/**
   class SolitaireBoard
   The board for Bulgarian Solitaire.  You can change what the total number of cards is for the game
   by changing NUM_FINAL_PILES, below.  Don't change CARD_TOTAL directly, because there are only some values
   for CARD_TOTAL that result in a game that terminates.

    SolitaireBoard class has the following named constant:
           NUM_FINAL_PILES : number of piles in a final configuration
           CARD_TOTAL : the total of the cards in the game.
           invariant : CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2,
                        as bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.

    SolitaireBoard class has the following instance variable:
           cardPiles : the current configuration of the game
           numOfPiles : the current number of piles

    SolitaireBoard class has a one-args constructor and a no-args constructor:
       one-args constructor let user set up the initial configuration
       no-args constructor Randomly divides cards into some number of piles of random size

    SolitaireBoard class supports the following methods:
        public method:
            void playRound();
            boolean isDone();
            String configString();
        private method:
            boolean isValidSolitaireBoard(); // used for assertion
 */

public class SolitaireBoard {
   
   public static final int NUM_FINAL_PILES = 9;
   // number of piles in a final configuration
   // (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)
   
   public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
   // bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
   // the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES

    /**
     Representation invariant:

     1.the number of piles is numOfPiles

     2. 0 < numOfPiles < cardPiles.length

     3.number of cards in each pile is stored from cardPiles[0] to cardPiles[numOfPiles - 1])

     4.number of cards in each pile should be a positive integer

     5.elements stored from cardPiles[numOfPiles] to cardPiles[cardPiles.length - 1]
        should be zero

     6.the sum of each element in cardPiles array = CARD_TOTAL

     */
   private int[] cardPiles;
   private int numOfPiles;

    /**
     Creates a solitaire board with the configuration specified in piles.
     PRE: piles contains a sequence of positive numbers that sum to SolitaireBoard.CARD_TOTAL
     @param piles : Save the initial configuration, i.e.,
                    the number of cards in the first pile,
                    then the number of cards in the second pile, etc.
     */
   public SolitaireBoard(ArrayList<Integer> piles) {
        cardPiles = new int[CARD_TOTAL + 1];

        for(int i = 0; i < piles.size(); i++){
            cardPiles[i] = piles.get(i);
        }
        numOfPiles = piles.size();
        assert isValidSolitaireBoard();

   }

   /**
      Creates a solitaire board with a random initial configuration.
   */
   public SolitaireBoard() {
        int remainedCard = CARD_TOTAL;

        Random RandomCard = new Random();
        cardPiles = new int[CARD_TOTAL + 1];

        while(remainedCard > 0) {
            int numOfCard = RandomCard.nextInt(remainedCard) + 1;
            cardPiles[numOfPiles++] = numOfCard;
            remainedCard -= numOfCard;
        }
        assert isValidSolitaireBoard();
   }

   /**
      Plays one round of Bulgarian solitaire.  Updates the configuration according to the rules
      of Bulgarian solitaire: Takes one card from each pile, and puts them all together in a new pile.
      The old piles that are left will be in the same relative order as before, 
      and the new pile will be at the end.
      How it works : two-pass algorithm, which takes O(n) time.
                     1.subtract 1 from every element in the cardPiles,
                        and add the new pile in the tail cardPiles
                     2.traversal for the head of cardPiles,
                        eliminate "zero" piles by swapping the first encountered zero pile
                        with the first encountered non-zero pile and so on.
                     the Time complexity is O(2n) = O(n)
                     the Space complexity is O(1)
    */
   public void playRound() {
        final int NOT_INITIALIZED = -1;
        assert isValidSolitaireBoard();
        int indexOfZero = 0;
        int indexOfElement = NOT_INITIALIZED;
        for(int i = 0; i < numOfPiles; i++){
            cardPiles[i]--;
        }
        cardPiles[numOfPiles] = numOfPiles;
        numOfPiles = 0;
        while(cardPiles[indexOfZero] != 0){
            indexOfZero++;
            numOfPiles ++;
        }
        for(int i = indexOfZero + 1; i < cardPiles.length;i++){
            if(cardPiles[i] != 0){
                indexOfElement = i;
                numOfPiles ++;
            }

            if(indexOfZero < indexOfElement){
                cardPiles[indexOfZero] = cardPiles[indexOfElement];
                cardPiles[indexOfElement] = 0;
                indexOfZero++;
                indexOfElement = NOT_INITIALIZED;
            }
        }
        assert isValidSolitaireBoard();
   }
   
   /**
      Returns true iff the current board is at the end of the game.  That is, there are NUM_FINAL_PILES
      piles that are of sizes 1, 2, 3, . . . , NUM_FINAL_PILES, in any order.
      How it works : bucket sort algorithm, which takes O(n) time and O(n) space, where n is the number of piles.
                     1. check if numOfPiles != NUM_FINAL_PILES, if so return false
                     2. make a new boolean array checkAllPiles, and the size is equal to NUM_FINAL_PILES
                     3. traversal for the head of cardPiles, turn every checkAllPiles[cardPiles[i] - 1] to true
                     4. check if any element in checkAllPiles is false, if so return false, because
                        piles are not of sizes 1, 2, 3, . . . , NUM_FINAL_PILES
                        else return true, and the game is ended
    */
   public boolean isDone() {
        assert isValidSolitaireBoard();
        if(numOfPiles != NUM_FINAL_PILES){
              return false;
        }
        boolean[] checkAllPiles = new boolean[NUM_FINAL_PILES];
        for(int i = 0; i < numOfPiles; i++){
            if(cardPiles[i] <= NUM_FINAL_PILES) {
                checkAllPiles[cardPiles[i] - 1] = true;
            }else{
                return false;
            }
        }
        for (boolean checkPile : checkAllPiles) {
            if (!checkPile) {
                return false;
            }
        }
        return true;
   }

   /**
      Returns current board configuration as a string with the format of
      a space-separated list of numbers with no leading or trailing spaces.
      The numbers represent the number of cards in each non-empty pile.
    */
   public String configString() {
        assert isValidSolitaireBoard();
        StringBuilder pileString = new StringBuilder("");
        for(int i = 0; i < numOfPiles;i++){
            pileString.append(String.valueOf(cardPiles[i])).append(" ");
        }
        pileString.deleteCharAt(pileString.length() - 1);
        return pileString.toString();
   }

   /**
      Returns true iff the solitaire board data is in a valid state, that is, check every representation invariant
      Representation invariant:
        1.the number of piles is numOfPiles
        2. 0 < numOfPiles < cardPiles.length = TOTAL_CARD + 1
        3.number of cards in each pile is stored from cardPiles[0] to cardPiles[numOfPiles - 1])
        4.number of cards in each pile should be a positive integer
        5.elements stored from cardPiles[numOfPiles] to cardPiles[cardPiles.length - 1]
            should be zero
        6.the sum of each element in cardPiles array = CARD_TOTAL
    */
   private boolean isValidSolitaireBoard() {
        int sumOfCard = 0;

        if(numOfPiles < 0 || numOfPiles >= cardPiles.length){
            return false;
        }
        for(int i = 0; i < numOfPiles;i++){
            if(cardPiles[i] <= 0){
                return false;
            }
            sumOfCard += cardPiles[i];
        }
        for(int i = numOfPiles; i < cardPiles.length; i++){
            if(cardPiles[i] != 0){
                return false;
            }
        }
        return sumOfCard == CARD_TOTAL;

   }

}
