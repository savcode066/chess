import java.awt.*;
import java.util.ArrayList;

public class Pieces {

    final int PAWNVAL = 100, KNIGHTVAL = 300,BISHOPVAL = 300, ROOKVAL = 500,QUEENVAL = 900, KINGVAL = 20000;

    ArrayList<Moves> pieceMoves = Moves.moves;
    int ROOK = 0, KNIGHT = 1, BISHOP = 2, QUEEN = 3, KING = 4, PAWN = 5;
    int WHITE = 100, BLACK = -100;
    int startBoardX = 50, startBoardY = 50; // one pixel short so that pieces can move to the edges
    int endBoardX = 540, endBoardY = 540;
    int []boardCoords;
    private int x, y, pieceType, col;
    private String moved;


    public int []getBoardCoords() {
        return boardCoords;
    }

    public void setBoardCoords(int[] boardCoords) {
        this.boardCoords = boardCoords;
    }

    int pieceValue;


    public int getPieceValue() {
        return pieceValue;
    }

    public void setPieceValue(int pieceValue) {
        this.pieceValue = pieceValue;
    }

    public Pieces(int x, int y, int pieceType, int col, String moved, int[] boardCoords, int pieceValue) {
        this.x = x;
        this.y = y;
        this.pieceType = pieceType;
        this.col = col;
        this.moved = moved;
        this.boardCoords = boardCoords;
        this.pieceValue = pieceValue;
    }
    public int getPieceType(){return pieceType;}
    public int getCol(){return col;}
    public String getMoved(){return moved;}
    public void setMoved(String m){moved = m;}
    public int getX(){return x;}
    public int getY(){return y;}
    public void setX(int xx){x = xx;}
    public void setY(int yy){y = yy;}
}


