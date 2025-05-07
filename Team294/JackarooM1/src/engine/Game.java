package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.text.StyledEditorKit.BoldAction;

import engine.board.Board;
import engine.board.BoardManager;
import engine.board.SafeZone;
import exception.CannotDiscardException;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import exception.InvalidSelectionException;
import exception.SplitOutOfRangeException;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.*;

@SuppressWarnings("unused")
public class Game implements GameManager {
    private final Board board;
    private final ArrayList<Player> players;
	private int currentPlayerIndex;
    private final ArrayList<Card> firePit;
    private int turn;

    public Game(String playerName) throws IOException {
        turn = 0;
        currentPlayerIndex = 0;
        firePit = new ArrayList<>();

        ArrayList<Colour> colourOrder = new ArrayList<>();
        
        colourOrder.addAll(Arrays.asList(Colour.values()));
        
        Collections.shuffle(colourOrder);
        
        this.board = new Board(colourOrder, this);
        
        Deck.loadCardPool(this.board, (GameManager)this);
        
        this.players = new ArrayList<>();
        this.players.add(new Player(playerName, colourOrder.get(0)));
        
        for (int i = 1; i < 4; i++) 
            this.players.add(new CPU("CPU " + i, colourOrder.get(i), this.board));
        
        for (int i = 0; i < 4; i++) 
            this.players.get(i).setHand(Deck.drawCards());
        
    }
    
    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Card> getFirePit() {
        return firePit;
    }
   public void selectCard(Card card) throws InvalidCardException{
	   Player Current_player = players.get(currentPlayerIndex); 
	   Current_player.selectCard(card); 
   }
    public void selectMarble(Marble marble) throws InvalidMarbleException{
    	Player Current_player = players.get(currentPlayerIndex); 
 	   	Current_player.selectMarble(marble); 
    }
    public void deselectAll(){
    	Player Current_player = players.get(currentPlayerIndex); 
    	Current_player.deselectAll();
    }
    public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException{
    	
    	if (splitDistance>=1 && splitDistance<= 6 ){board.setSplitDistance(splitDistance);}
    	else{     		throw new SplitOutOfRangeException("Split distance must be between 1 and 6.") ; }

    	 
    
    }
    public boolean canPlayTurn(){
    	Player Current_player = players.get(currentPlayerIndex); 
    	ArrayList<Card> HAND_OF_PLAYER = Current_player.getHand();
    	if (HAND_OF_PLAYER.size() + turn == 4  ){return true ;}
    	else {return false;}
    }
    public void playPlayerTurn() throws GameException {
        Player currentPlayer = players.get(currentPlayerIndex);
        ArrayList<Marble> playermarble = currentPlayer.getMarbles() ; 
        if (canPlayTurn()) {currentPlayer.play();}

    }

    	
    
    public void endPlayerTurn(){
    	Player Current_player = players.get(currentPlayerIndex); 
    	Card SelectedCard = Current_player.getSelectedCard(); 
    	Marble selectedMarble  =Current_player.getOneMarble();
    	ArrayList<Card> HAND_OF_PLAYER = Current_player.getHand();
    	HAND_OF_PLAYER.remove(SelectedCard) ; 
    	firePit.add(SelectedCard);
    	deselectAll();
    	currentPlayerIndex =(currentPlayerIndex + 1) % players.size();
    	 
    	if (currentPlayerIndex == 0  ){
    		turn++ ; }
    	if (turn == 4 ){
    		turn = 0 ; 
	    	for (int i = 0 ; i   <  players.size() ; i++ ){
	    		if (Deck.getPoolSize()  < 4 ){
		    		Deck.refillPool(firePit);
	                firePit.clear();
		    		}  
	    		players.get(i).setHand(Deck.drawCards());}
	    	
	    	}
    	
    	}
    	


    


    public Colour checkWin() {
        for (Player player : players) {
            Colour colour = player.getColour();
            ArrayList<SafeZone> Safezoness = board.getSafeZones() ; 
            for (int i = 0 ; i < Safezoness.size() ; i++ ){
            if (player.getColour().equals(Safezoness.get(i).getColour() )){
            	SafeZone playerssafezone = Safezoness.get(i) ;
            	 if (playerssafezone.isFull()) {
                     return colour;
                 }
            }
            }
           
        }
        return null;}
    public void sendHome(Marble marble){
    	for (int i = 0 ; i  <players.size() ;  i ++  ){
    		Player tmp_player =  players.get(i) ; 
    		if (tmp_player.getColour()== marble.getColour()) {
    			tmp_player.regainMarble(marble);
    			break;
    		}
    	}
    	
    }
    public void fieldMarble() throws CannotFieldException, IllegalDestroyException {
    	Player Current_player = players.get(currentPlayerIndex); 
    	Marble Marble_Of_current_player =  Current_player.getOneMarble();
    	
    	if (Marble_Of_current_player == null ){
    		
    		throw new CannotFieldException("No marbles available to field from Home Zone. ") ; 
    	}
    	else {
    		board.sendToBase(Marble_Of_current_player);
    	
    		Current_player.getMarbles().remove(Marble_Of_current_player);
    		
    	}
    	
    }
    
    
    public void discardCard(Colour colour) throws CannotDiscardException{
    	for (int i = 0; i< players.size() ; i++){
    		Player Current_player = players.get(i);
	    	if (Current_player.getColour() .equals(colour)){
	    		 ArrayList<Card> playerHand = Current_player.getHand() ;
	    		 if (playerHand.isEmpty()){
	    			 throw new CannotDiscardException("the hand is empty ") ; 
	    		 }
	        	 int randomNum = (int)(Math.random()*playerHand.size()) ;
	        	 Card RandomCard = playerHand.get(randomNum) ; 
	        	 playerHand.remove(RandomCard);
	        	 firePit.add(RandomCard);
	        	 break ; 
		        	 
		    	 }
	    }
	}

    public void discardCard() throws CannotDiscardException{
    	Player Current_player = players.get(currentPlayerIndex) ; 
    	int random ;
    	do {
    		random = (int)(Math.random()*players.size());
    	}while (random == currentPlayerIndex) ; 
    	discardCard(players.get(random).getColour());
    }
    public Colour getActivePlayerColour(){
    	return players.get(currentPlayerIndex).getColour() ; 
    }
    public Colour getNextPlayerColour(){    	return players.get((currentPlayerIndex +1 )% players.size() ).getColour() ; 
}
 


}