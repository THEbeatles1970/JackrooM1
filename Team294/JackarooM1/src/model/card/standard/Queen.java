package model.card.standard;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Queen extends Standard {

    public Queen(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 12, suit, boardManager, gameManager);
    }
    
    
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
    	int size = marbles.size() ; 
    	if (size == 1  || size == 0  ){return true ; }
    	return false ; 
    }
    public  void act(ArrayList<Marble> marbles) throws ActionException,InvalidMarbleException{
    	if (marbles.size() == 0 ) {
    		gameManager.discardCard();
        }
    	
    	if (marbles.size() == 1 ){
    		super.act(marbles);
    	}}

   
}
