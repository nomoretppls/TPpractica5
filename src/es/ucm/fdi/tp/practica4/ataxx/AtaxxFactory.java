package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.basecode.bgame.control.ConsolePlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.DummyAIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.views.GenericConsoleView;

public class AtaxxFactory implements GameFactory {
	private int dim;
	private int obstacles;
	
	
	public AtaxxFactory(){
		this.dim=5;
		this.obstacles = 0;
	}
	public AtaxxFactory(int dimCustom){
		if(dimCustom < 5 ){
			throw new GameError("Dimension must be  at least 5:"+ dim);
		} else {
			this.dim = dimCustom;
			this.obstacles=0;
		}
	}
	
	public AtaxxFactory(int dim, int obstacles){
		if(dim < 5){
			throw new GameError("Dimension must be at least 5 :" + dim);
		}
		else{
			if(obstacles > (dim * dim)- 8){
				throw new GameError("Obstacles must be less than " + dim * dim);
			}
			else{
				this.dim = dim;
				this.obstacles = obstacles;
			}
		}
	}
	
	@Override
	public GameRules gameRules() {
		// TODO Auto-generated method stub
		return new AtaxxRules(dim,obstacles);
	}

	@Override
	public Player createConsolePlayer() {
		// TODO Auto-generated method stub
		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		possibleMoves.add(new AtaxxMove());
		return new ConsolePlayer(new Scanner(System.in), possibleMoves);
	}

	@Override
	public Player createRandomPlayer() {
		// TODO Auto-generated method stub
		return new AtaxxRandomPlayer();
		
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg) {
		// TODO Auto-generated method stub
		return new DummyAIPlayer(createRandomPlayer(), 1000);
	}

	@Override
	public List<Piece> createDefaultPieces() {
		// TODO Auto-generated method stub
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		return pieces;
	}

	@Override
	public void createConsoleView(Observable<GameObserver> game, Controller ctrl) {
		// TODO Auto-generated method stub
		new GenericConsoleView(game, ctrl);
	}

	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl, Piece viewPiece, Player randPlayer,
			Player aiPlayer) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("There is no swing view");
	}

}
