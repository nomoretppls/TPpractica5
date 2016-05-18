package es.ucm.fdi.tp.practica4.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;

public class AtaxxMove extends GameMove {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The row where to place the piece return by {@link GameMove#getPiece()}.
	 * <p>
	 * Fila en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int row;

	/**
	 * The column where to place the piece return by {@link GameMove#getPiece()}
	 * .
	 * <p>
	 * Columna en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int col;
	
	protected int destRow;
	protected int destCol;

	/**
	 * This constructor should be used ONLY to get an instance of
	 * {@link AtaxxMove} to generate game moves from strings by calling
	 * {@link #fromString(String)}
	 * 
	 * <p>
	 * Solo se debe usar este constructor para obtener objetos de
	 * {@link AtaxxMove} para generar movimientos a partir de strings usando
	 * el metodo {@link #fromString(String)}
	 * 
	 */
	
	

	public AtaxxMove() {
	}

	/**
	 * Constructs a move for placing a piece of the type referenced by {@code p}
	 * at position ({@code row},{@code col}).
	 * 
	 * <p>
	 * Construye un movimiento para colocar una ficha del tipo referenciado por
	 * {@code p} en la posicion ({@code row},{@code col}) a la posicion ({@code destRow},{@code destCol}).
	 * 
	 * @param row
	 *            Number of row.
	 *            <p>
	 *            Numero de fila.
	 * @param col
	 *            Number of column.
	 *            <p>
	 *            Numero de columna.
	 *  @param destRow
	 *            Number of  destiny row.
	 *            <p>
	 *            Numero de fila destino.
	 * @param destCol
	 *            Number of destiny column.
	 *            <p>
	 *            Numero de columna destino.
	 * @param p
	 *            A piece to be place at ({@code row},{@code col}).
	 *            <p>
	 *            Ficha a colocar en ({@code row},{@code col}).
	 */
	public AtaxxMove(int row, int col,int destRow,int destCol, Piece p) {
		super(p);
		this.row = row;
		this.col = col;
		this.destRow=destRow;
		this.destCol=destCol;
	}
	@Override
	public void execute(Board board, List<Piece> pieces) {
		// TODO Auto-generated method stub
		Piece piece = getPiece();		
		int dist = Math.max(Math.abs(this.row - this.destRow), Math.abs(this.col - this.destCol));
		
		if (board.getPosition(this.row, this.col) == null) {
			throw new GameError("position (" + this.row + "," + this.col + ") is void!");
		} 
		else if (board.getPosition(this.destRow, this.destCol ) != null) {
			throw new GameError("position (" + this.destRow + "," + this.destCol + ")  occupied!");
		}
		else if(board.getPosition(this.row, this.col) != piece){
			throw new GameError("the piece(" + this.row + "," + this.col + ") is another player");
		}
		else if(dist > 2){
			throw new GameError("the position (" + this.destRow + "," + this.destCol + ")is greater than 2");
		}
		else if(board.getPieceCount(piece) <= 0){
			throw new GameError("The piece  of type  " + piece + "is not valid");
		}
		
		if(dist == 1){
			board.setPosition(this.destRow, this.destCol, piece);
			board.setPieceCount(piece, board.getPieceCount(piece)+ 1);
		}
		else if(dist == 2){
			board.setPosition(this.destRow, this.destCol, piece);
			board.setPosition(this.row, this.col, null);
		}
		this.convertPiecesArr(board, pieces, piece);
	}
	
/**Este metodo convierte las fichas alrededor  de la ficha que se ha movido.
 * @param board tablero del juego.
 * @param pieces fichas de los jugadores.
 * @param piece ficha del jugador.
 */
private void convertPiecesArr(Board board, List<Piece> pieces, Piece piece){
		
		int rows = board.getRows();
		int cols = board.getCols();
			 
		for(int r = Math.max(this.destRow - 1, 0); r <= Math.min(this.destRow + 1, rows - 1); r++) 
			for (int c = Math.max(this.destCol - 1, 0); c <= Math.min(this.destCol + 1, cols - 1); c++) 
				if(board.getPosition(r, c) != null  && pieces.contains(board.getPosition(r, c))){
					if(piece.getId() != board.getPosition(r, c).getId() ){
						board.setPieceCount(board.getPosition(r, c), board.getPieceCount(board.getPosition(r, c)) - 1);
						board.setPieceCount(piece, board.getPieceCount(piece) + 1);
						board.setPosition(r, c, piece);
						}
					}
	}

	/**
	 * This move can be constructed from a string of the form "row SPACE col"
	 * where row and col are integers representing a position.
	 * 
	 * <p>
	 * Se puede construir un movimiento desde un string de la forma
	 * "row SPACE col" donde row y col son enteros que representan una casilla.
	 */
	@Override
	public GameMove fromString(Piece p, String str) {
		// TODO Auto-generated method stub
		String[] words = str.split(" ");
		if (words.length != 4) {
			return null;
		}

		try {
			int row, col, destRow, destCol;
			row = Integer.parseInt(words[0]);
			col = Integer.parseInt(words[1]);
			destRow = Integer.parseInt(words[2]);
			destCol = Integer.parseInt(words[3]);
			return createMove(row, col, destRow, destCol, p);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return "Row and column for origin and for destination, separated by spaces (four numbers).";
	}
	
	/**
	 * Creates a move that is called from {@link #fromString(Piece, String)}.
	 * Separating it from that method allows us to use this class for other
	 * similar games by overriding this method.
	 * 
	 * <p>
	 * Crea un nuevo movimiento con la misma ficha utilizada en el movimiento
	 * actual. Llamado desde {@link #fromString(Piece, String)}; se separa este
	 * metodo del anterior para permitir utilizar esta clase para otros juegos
	 * similares sobrescribiendo este metodo.
	 * 
	 * @param row
	 *            Row of the move being created.
	 *            <p>
	 *            Fila del nuevo movimiento.
	 * 
	 * @param col
	 *            Column of the move being created.
	 *            <p>
	 *            Columna del nuevo movimiento.
	 */
	protected GameMove createMove(int row, int col,int destRow,int destCol, Piece p) {
		return new AtaxxMove(row, col, destRow, destCol, p);
	}
	//modificar
	@Override
	public String toString() {
		if (getPiece() == null) {
			return help();
		} else {
			return "Place a piece '" + getPiece() + "' at (" + row + "," + col + ")";
		}
	}
	

}
