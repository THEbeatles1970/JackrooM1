package model.card.standard;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import model.player.Player;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Five extends Standard {

    public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 5, suit, boardManager, gameManager);
    }
   
    public boolean validateMarbleColours(ArrayList<Marble> marbles){
    	
    	return true ; 
    	}
    public  void act(ArrayList<Marble> marbles) throws ActionException,InvalidMarbleException{
    	if (marbles.size() == 1 ){
    		super.act(marbles);
    	}}

}
