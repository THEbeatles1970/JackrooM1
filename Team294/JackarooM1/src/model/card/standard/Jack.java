package model.card.standard;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Jack extends Standard {

    public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 11, suit, boardManager, gameManager);
    }
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
    	int size = marbles.size() ; 
    	if (size == 1  || size == 2  ){return true ; }
    	return false ; 
    }
    public boolean validateMarbleColours(ArrayList<Marble> marbles){
    	Colour players_colour = gameManager.getActivePlayerColour(); 
    	if ( marbles.size() == 1  && players_colour ==  marbles.get(0).getColour()){return true ; }
    	if ( marbles.size() == 2  ){
    		Colour marble_1_colour = marbles.get(0).getColour() ;
    		Colour marble_2_colour = marbles.get(1).getColour() ; 
    		if ( players_colour == marble_1_colour && players_colour != marble_2_colour){return true ;}
    		if ( players_colour == marble_2_colour && players_colour != marble_1_colour){return true ;}
    				}

    	return false ; 
    	}
    public  void act(ArrayList<Marble> marbles) throws ActionException,InvalidMarbleException{
    	
		if (marbles.size() == 2 ) { 
			Marble marble_1 = marbles.get(0) ; 
			Marble marble_2 = marbles.get(1) ; 
    		boardManager.swap(marble_1, marble_2);;
        }
    	
		else {
    		super.act(marbles);
    		
    	}}


}
