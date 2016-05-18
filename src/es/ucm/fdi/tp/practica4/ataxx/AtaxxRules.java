package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class AtaxxRules implements GameRules {
	// This object is returned by gameOver to indicate that the game is not
	// over. Just to avoid creating it multiple times, etc.
	//
	protected final Pair<State, Piece> gameInPlayResult = new Pair<State, Piece>(State.InPlay, null);

	private int dim;
	private int obstacles;
	private final Piece obstacle = new Piece("+");

	public AtaxxRules() {
		this.dim = 5;
	}

	public AtaxxRules(int dim, int obstacles) {
		if (dim < 5) {
			throw new GameError("Dimension must be at least 5 :" + dim);
		} else {
			if (obstacles > (dim * dim) - 8) {
				throw new GameError("Obstacles must be less than " + dim * dim);
			} else {
				this.dim = dim;
				this.obstacles = obstacles;
			}
		}
	}

	public AtaxxRules(int dim) {
		if (dim < 5) {
			throw new GameError("Dimension must be at least 5 :" + dim);
		}
		this.dim = dim;
		this.obstacles = 0;
	}

	@Override
	public String gameDesc() {
		// TODO Auto-generated method stub
		return "Ataxx " + this.dim + " x " + this.dim;
	}

	@Override
	public Board createBoard(List<Piece> pieces) {
		// TODO Auto-generated method stub
		Board board=new FiniteRectBoard(dim, dim);
		Piece p1 = pieces.get(0);
		board.setPosition(0, 0, p1);
		board.setPosition(sym(0), sym(0), p1);
		board.setPieceCount(p1, 2);
		
		Piece p2 = pieces.get(1);
		board.setPosition(0, this.dim - 1, p2);
		board.setPosition(sym(0), sym(this.dim - 1), p2);
		board.setPieceCount(p2, 2);
		
		if(pieces.size() > 2){
			Piece p3 = pieces.get(2);
			board.setPosition(0, this.dim / 2, p3);
			board.setPosition(sym(0), sym(this.dim /2), p3);
			board.setPieceCount(p3, 2);
			
			if(pieces.size() > 3){
				Piece p4 = pieces.get(3);
				board.setPosition(this.dim / 2, 0, p4);
				board.setPosition(sym(this.dim / 2), sym(0), p4);
				board.setPieceCount(p4, 2);
			}
		}
		if(this.obstacles > 0)
			placeObstacles(board);
		
		return board;
		
	}

	@Override
	public Piece initialPlayer(Board board, List<Piece> pieces) {
		// TODO Auto-generated method stub
		return pieces.get(0);
	}

	@Override
	public int minPlayers() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int maxPlayers() {
		// TODO Auto-generated method stub
		return 4;
	}

	
	
	/**Metodo que coloca los obstaculos en el atributo de la clase{@link AtaxxRules},
	 * de manera simetrica.
	 * @param board tablero donde se colocan los obstaculos.
	 */
	private void placeObstacles(Board board){
		int r, c;
		int count = this.obstacles;
		if(count % 2 != 0){
			board.setPosition(this.dim/2, this.dim/2, this.obstacle);
			count--;
		}
		while(count != 0){
			r = Utils.randomInt(this.dim);
			c = Utils.randomInt(this.dim);
			if(board.getPosition(r, c) == null){
				board.setPosition(r, c, this.obstacle);
				board.setPosition(sym(r),sym(c), this.obstacle);
				count = count - 2;
			}
		}
	}
	
	/**Metodo que devuelve el valor simetrico , en el tablero ,de un valor dado.
	 * @param origin es una  componente de una coordenada de una posicion del tablero.
	 * @return valor simetrico en el tablero, respecto del origen y el centro del tablero.
	 */
	private int sym(int origin){

		int symmetrical;
		int middle = this.dim/2;
		if(origin > middle)
			symmetrical = Math.abs((origin-middle)- middle);
		else if(origin < middle)
			symmetrical =(Math.abs(origin-middle) + middle);
		else
			symmetrical = origin;
		return symmetrical;
	}

	@Override
	public Pair<State, Piece> updateState(Board board, List<Piece> pieces, Piece turn) {
		// TODO Auto-generated method stub
		
		Pair<State, Piece> res = new Pair<State, Piece>(State.InPlay, null);

		if (board.isFull()) {
			res = result(board, pieces, turn);
		} else if (this.nextPlayer(board, pieces, turn).equals(turn)) {
			if (this.playersBlocked(board, pieces))
				res = result(board, pieces, turn);
		}
		return res;
	}
	
	/**Metodo que determina si los jugadores estan bloqueados, excepto uno.
	 * @param board tablero de la partida.
	 *		
	 * @param pieces lista de las fichas de los jugadores.
	 * @return
	 */
	protected boolean playersBlocked(Board board, List<Piece> pieces){
		boolean blocked = false;
		int counter = 0;
		
		for(int i = 0; i < pieces.size(); i++){
			if(this.validMoves(board, pieces, pieces.get(i)).size() <= 0)
				counter++;
		}
		if(counter == pieces.size() - 1)
			blocked = true;
		return blocked;
	}

	/**Metodo que indica el estado final  de una partida indicando el jugador ganador y las fichas.
	 * @param board tablero del juego.
	 * @param pieces lista de tipos  fichas de los jugadores
	 * @param turn ficha del turno actual
	 * @return  Pair<State,Piece> estado final de la partida y el jugador.
	 */
	private Pair<State, Piece> result(Board board, List<Piece> pieces, Piece turn) {

		State game = State.InPlay;
		Piece piece = null;
		int higerValue = 0;
		int[] playersScore = new int[pieces.size()];

		for (int i = 0; i < pieces.size(); i++) {
			playersScore[i] = board.getPieceCount(pieces.get(i));
			if (higerValue == playersScore[i]) {
				game = State.Draw;

			} else if (higerValue < playersScore[i]) {
				higerValue = playersScore[i];
				piece = pieces.get(i);
				game = State.Won;
			}
		}
		Pair<State, Piece> result = new Pair<State, Piece>(game, piece);

		return result;
	}

	@Override
	public Piece nextPlayer(Board board, List<Piece> pieces, Piece turn) {
		// TODO Auto-generated method stub
		Piece piece;
		int numPlayers = pieces.size();
		int i = pieces.indexOf(turn);
		int quant, counter = 1;

		do {
			piece = pieces.get((i + counter) % numPlayers);
			quant = this.validMoves(board, pieces, piece).size();
			counter++;
		} while (quant <= 0 && counter <= numPlayers);

		return piece;
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn,Piece p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces, Piece turn) {
		// TODO Auto-generated method stub
		
		Piece piece = turn;
		List<GameMove> moves = new ArrayList<GameMove>();
		for (int r = 0; r < board.getRows(); r++)
			for (int c = 0; c < board.getCols(); c++) {
				if (board.getPosition(r, c) == piece) {
					moves.addAll(valMovePiece(board, turn, r, c));
				}
			}
		return moves;

	}

	/**Metodo que determina los posibles movimientos de una ficha en una posicion del tablero.
	 * @param board tablero de ljuego.
	 * @param piece ficha de la cual se quiere determinar los posibles movimientos.
	 * @param row fila de la ficha consultada.
	 * @param col columna  de la ficha consultada.
	 * @return
	 */
	protected List<GameMove> valMovePiece(Board board, Piece piece, int row, int col) {

		int rows = board.getRows();
		int cols = board.getCols();

		List<GameMove> moves = new ArrayList<GameMove>();
		for (int r = Math.max(row - 2, 0); r <= Math.min(row + 2, rows - 1); r++)
			for (int c = Math.max(col - 2, 0); c <= Math.min(col + 2, cols - 1); c++)
				if (board.getPosition(r, c) == null) {
					moves.add(new AtaxxMove(row, col, r, c, piece));
				}
		return moves;

	}

}
