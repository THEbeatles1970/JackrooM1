package model.player;
import java.util.ArrayList ;


import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import model.Colour;
import model.card.Card;
import model.player.Marble ;


public class Player {
    private final  String name ;
    private final Colour colour ;
    private  ArrayList<Card> hand ;
    private final ArrayList<Marble> marbles ;
    private  Card selectedCard ;
    private final ArrayList<Marble> selectedMarbles ;
 public Player(String name, Colour colour){
        this.name = name ;
        this.colour = colour ;
        this.hand = new ArrayList<Card>();
        this.selectedMarbles = new ArrayList<Marble>() ;
        this.marbles = new ArrayList<Marble>() ;
        this.selectedCard = null ;
        for (int i = 0 ; i < 4 ; i ++){
            this.marbles.add(new Marble(colour)) ;
        }
    }
    public String getName() {
        return name;
    }

    public Colour getColour() {
        return colour;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void regainMarble(Marble marble){
    	marbles.add(marble) ; 
    }
    
   public Marble getOneMarble(){
	   if (marbles.isEmpty()){return null ;}
	   else {return (marbles.get(0)) ; }
   }
    
    public void selectCard(Card card) throws InvalidCardException{
    	for (int i = 0 ; i < hand.size() ; i++ ){
    		if (card.equals(hand.get(i))){
    			selectedCard = card ; 
    		}}
    	if (!hand.contains(card)){
    		throw new InvalidCardException("The selected card is not in the player's hand."); 
    	}
    }
    public void selectMarble(Marble marble) throws InvalidMarbleException{
    		if (selectedMarbles.size() >= 2) {
    			        throw new InvalidMarbleException("Cannot select more than two marbles.");
    			    }
    		
    		else if (!selectedMarbles.contains(marble)){selectedMarbles.add(marble);}
    }
    
    public void deselectAll(){
    	selectedCard= null ; 
    	selectedMarbles.clear();
    }
    
    public void play() throws GameException{
    	
    	if (selectedCard == null ){
    		
    		throw new InvalidCardException("no cards are selected ");
    	}
    	
    	
    	if (selectedCard.validateMarbleSize(selectedMarbles)== false ){
    		
    		throw new InvalidMarbleException("you cannot select this card  ");}
    	
    	
    	if (selectedCard.validateMarbleColours(selectedMarbles)== false ){    		
    		
    		throw new InvalidMarbleException("you cannot select this card  ");
    	}
    	
    	
    	
    	selectedCard.act(selectedMarbles);
    	
    }
    public void removefromHomezone(Marble marble ){
    	marbles.remove(marble) ; 
    }
    public boolean is_this_marble_in_homezone(Marble marble ) {
    	for ( int i = 0 ;  i  < marbles.size() ; i++   ){
    		if (marble.equals(marbles.get(i)))
    		{return true ;}
    		
    	}
    	return false; 
    }
}


