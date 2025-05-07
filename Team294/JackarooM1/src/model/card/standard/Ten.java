package model.card.standard;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Ten extends Standard {

    public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 10, suit, boardManager, gameManager);
    }
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
    	int size = marbles.size() ; 
    	if (size == 1  || size == 0  ){return true ; }
    	return false ; 
    }
    public boolean validateMarbleColours(ArrayList<Marble> marbles){
    	Colour players_colour = gameManager.getActivePlayerColour(); 
    	if ( players_colour ==  marbles.get(0).getColour()){return true ; }
    	return false ; 
    	}
    public  void act(ArrayList<Marble> marbles) throws ActionException,InvalidMarbleException{
    	if (marbles.size()== 0 ){
    		Colour colour  = gameManager.getNextPlayerColour() ;
    		gameManager.discardCard(colour);
		}
    	
    	if (marbles.size() == 1 ){
    		super.act(marbles);
    	}}
    
}
