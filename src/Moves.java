import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
/*
Moves.java
Savio Joseph Benher
This class includes lots of functions. There are functions for to determine the psuedo legal moves of all the pieces.
There are functions to deal with special moves like enpassant, promotion, and castling.
There are functions for making a move and unmaking it, these are used for the ai to calculate positions.
 */
public class Moves {

    int WHITE = 100, BLACK = -100;
    private Board[][] board = ChessPanel.board;
    int ROOK = 0, KNIGHT = 1, BISHOP = 2, QUEEN = 3, KING = 4, PAWN = 5;
    int[][] pieceOffsets = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1},}; // change name
    int[] pawnMove = {-1, -2, 1, 2};
    int[][] pawnCaptures = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    int[][] knMoves = {{2, 1}, {2, -1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}};

    int notAttacked = 0, isAttacked = 1;
    int isAttackedWhite = ChessPanel.WHITE, isAttackedBlack = ChessPanel.BLACK;
    final int PAWNVAL = 100, KNIGHTVAL = 300,BISHOPVAL = 300, ROOKVAL = 500,QUEENVAL = 900, KINGVAL = 20000;



    public static ArrayList<Moves> moves; //change moves name to allMoves

    int[] start;
    int[] end;

    Pieces promotionPiece;
    int sqOffset = 10;

    public int[] getStart() {
        return start;
    }



    public int[] getEnd() {
        return end;
    }



    public Pieces getPromotionPiece() {
        return promotionPiece;
    }
    boolean capture, castling, enpassant;


    public boolean isEnpassant() {
        return enpassant;
    }



    public boolean isCastling() {
        return castling;
    }



    public Moves(int[] start, int[] end, Pieces promotionPiece, boolean capture, boolean enpassant, boolean castling) {
        this.start = start;
        this.end = end;
        this.promotionPiece = promotionPiece;
        this.capture = capture;
        this.castling = castling;
        this.enpassant = enpassant;
    }

    public boolean isCastling(Moves move){
        Pieces piece = board[move.getStart()[0]][move.getStart()[1]].getPiece();
        if(piece!=null) {
            if (piece.getPieceType() == KING) { // king can normally move only 1 square but in caslting it moves 2
                // so, I just check if the different in its start square and end square is 2
                if (Math.abs(move.getEnd()[0] - move.getStart()[0]) == 2 || Math.abs(move.getEnd()[1] - move.getStart()[1]) == 2) {
                    return true;

                }
            }
        }
        return false;
    }



    public void resetAttacked(int col) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (col == ChessPanel.WHITE){
                    board[i][j].setAttackedWhite(notAttacked);
                }
                else if(col == ChessPanel.BLACK) {
                    board[i][j].setAttackedBlack(notAttacked);
                }
                else{
                    board[i][j].setAttackedWhite(notAttacked);
                    board[i][j].setAttackedBlack(notAttacked);
                }
            }
        }
    }

    public void setAllMoved(){
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board[i].length;j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getPieceType() == PAWN && board[i][j].getPiece().getMoved().equals("1")){ // enpassant can only happen on the move the opponents pawn went two 2 squares
                        // after the move is over, that pawn cant be taken by enpassant
                      //  System.out.println(i+" setAllMoved "+j);
                        board[i][j].getPiece().setMoved("2");
                    }
                }
            }
        }
    }



    public Pieces[] makeMove(Moves move) {
       // System.out.println(move.getStart()[0]+" "+move.getStart()[1]);
        Pieces startPiece = board[move.getStart()[0]][move.getStart()[1]].getPiece(); // starting Piece
        Pieces endPiece = null; // end is null at the beginning since I don't know if there is a piece on the end square
        Pieces castlingRook = null;

        if (move.isEnpassant()) {
           // System.out.println("nora324242ml");
         //   enpassantPiece = new Pieces(-1,-1,-1,7,"", move.getStart(),9);
                if (startPiece.getCol() == ChessPanel.WHITE){
                    endPiece = board[move.getEnd()[0]+1][move.getEnd()[1]].getPiece(); // I can find pawn that is getting captured by looking at the square above the end square for the pawn that is doing the enpassant
                    board[move.getEnd()[0] + 1][move.getEnd()[1]].setPiece(null);
                }
                else {
                    endPiece = board[move.getEnd()[0] - 1][move.getEnd()[1]].getPiece();
                    board[move.getEnd()[0] - 1][move.getEnd()[1]].setPiece(null);
                }
            }
        else{
             endPiece = board[move.getEnd()[0]][move.getEnd()[1]].getPiece(); // if its normal case then endpiece is just whatever is on the end square
            }
        if(move.isCastling()) {
           // System.out.println("3242");
            if (board[move.getEnd()[0]][move.getEnd()[1] + 1].getPiece() != null) {
                if (board[move.getEnd()[0]][move.getEnd()[1] + 1].getPiece().getPieceType() == ROOK) {// kingside castling
                    castlingRook = board[move.getEnd()[0]][move.getEnd()[1] + 1].getPiece();
                    Pieces rook = board[move.getEnd()[0]][move.getEnd()[1] + 1].getPiece();
                    Board newRookSquare = board[move.getEnd()[0]][move.getEnd()[1] - 1];
                    Board oldRookSquare = board[move.getEnd()[0]][move.getEnd()[1] + 1];

                    newRookSquare.setPiece(rook);// rook is on other side of king hence the -1
                    oldRookSquare.setPiece(null); // set rook's current square to null since I'm moving the rook off the square
                    newRookSquare.getPiece().setBoardCoords(new int[]{move.getEnd()[0], move.getEnd()[1] - 1}); // set the rooks board coords to the new location


                }
            }
            else if (board[move.getEnd()[0]][move.getEnd()[1] - 2].getPiece() != null) {
                if (board[move.getEnd()[0]][move.getEnd()[1] - 2].getPiece().getPieceType() == ROOK) { // queenside castling
                    castlingRook = board[move.getEnd()[0]][move.getEnd()[1] - 2].getPiece();
                    Pieces rook = board[move.getEnd()[0]][move.getEnd()[1] - 2].getPiece();
                    Board newSquareRook = board[move.getEnd()[0]][move.getEnd()[1] + 1];
                    Board oldSquareRook = board[move.getEnd()[0]][move.getEnd()[1] - 2];

                    newSquareRook.setPiece(rook);
                    oldSquareRook.setPiece(null);
                    newSquareRook.getPiece().setBoardCoords(new int[]{move.getEnd()[0], move.getEnd()[1] + 1});
                }
            }
        }


            if (move.getPromotionPiece() != null) {
              //  System.out.println("64");
              //  promotionPiece = board[move.getStart()[0]][move.getStart()[1]].getPiece();
                board[move.getEnd()[0]][move.getEnd()[1]].setPiece(move.getPromotionPiece()); // setting end to promote piece
                board[move.getStart()[0]][move.getStart()[1]].setPiece(null); // setting start to null as nothing should be there
               // move.getPromotionPiece().setBoardCoords(move.getEnd());
            }
            else {
             //   System.out.println("noraml");
                startPiece.setBoardCoords(move.getEnd()); // for caslting, this takes care of the king movement
                board[move.getEnd()[0]][move.getEnd()[1]].setPiece(startPiece);
                board[move.getStart()[0]][move.getStart()[1]].setPiece(null);

        }
        return new Pieces[]{startPiece, endPiece, castlingRook};

    }

    public void unMakeMove(Moves move, Pieces startPiece, Pieces endPiece, Pieces castlingRook) {
       // Pieces startPiece = board[move.getEnd()[0]][move.getEnd()[1]].getPiece();
        if(move.getPromotionPiece()!=null){
            board[move.getEnd()[0]][move.getEnd()[1]].setPiece(endPiece);
            board[move.getStart()[0]][move.getStart()[1]].setPiece(startPiece);
            startPiece.setBoardCoords(move.getStart());
        }
        else if(move.isEnpassant()){
            if (startPiece.getCol() == ChessPanel.WHITE){
                // I can find pawn that is getting captured by looking at the square above the end square for the pawn that is doing the enpassant
                board[move.getEnd()[0] + 1][move.getEnd()[1]].setPiece(endPiece);
                board[move.getStart()[0]][move.getStart()[1]].setPiece(startPiece);
                startPiece.setBoardCoords(move.getStart());
                board[move.getEnd()[0]][move.getEnd()[1]].setPiece(null);
            }
            else {
                board[move.getEnd()[0] - 1][move.getEnd()[1]].setPiece(endPiece);
                board[move.getStart()[0]][move.getStart()[1]].setPiece(startPiece);
                startPiece.setBoardCoords(move.getStart());
                board[move.getEnd()[0]][move.getEnd()[1]].setPiece(null);

            }
        }
        else if(move.isCastling()){
            board[move.getStart()[0]][move.getStart()[1]].setPiece(startPiece);
            board[move.getEnd()[0]][move.getEnd()[1]].setPiece(null); // there couldn't have been a piece there
            startPiece.setBoardCoords(move.getStart());
                if(move.getEnd()[1] == 6){
                    board[move.getEnd()[0]][move.getEnd()[1]+1].setPiece(castlingRook);// this retains its boardCoords so I don't need to change it
                    board[move.getEnd()[0]][move.getEnd()[1]-1].setPiece(null);
                }
                else if(move.getEnd()[1] == 2){ // queenside castling
                    board[move.getEnd()[0]][move.getEnd()[1]-2].setPiece(castlingRook);// this retains its boardCoords so I don't need to change it
                    board[move.getEnd()[0]][move.getEnd()[1]+1].setPiece(null);
                }
        }

        else {
            //System.out.println("normal");
            board[move.getStart()[0]][move.getStart()[1]].setPiece(startPiece);
            board[move.getEnd()[0]][move.getEnd()[1]].setPiece(endPiece);
            startPiece.setBoardCoords(move.getStart());
            if (endPiece != null) {
                endPiece.setBoardCoords(move.getEnd());
            }
        }
    }




    public boolean inCheck(ArrayList<Moves> moves, int col) {
        for(int i=0;i<moves.size();i++){
            if(board[moves.get(i).getEnd()[0]][moves.get(i).getEnd()[1]].getPiece()!=null){
                if(board[moves.get(i).getEnd()[0]][moves.get(i).getEnd()[1]].getPiece().getPieceType()==KING && board[moves.get(i).getEnd()[0]][moves.get(i).getEnd()[1]].getPiece().getCol() == col){
                    return true;
                }
            }
        }
        return false;
    }


    public ArrayList<Moves> createLegalMoves(int col) {
        ArrayList<Moves> legalMoves = new ArrayList<>();
       resetAttacked(0);// making sure past iteration doesn't affect present one
        ArrayList<Moves> oppMoves = createMoves(-col); // this allows me to generate all the squares the opponent attacks
        ArrayList<Moves> playerMoves = createMoves(col); // now, since I have all the squares I can use the attack squares to determine if I  can castle

        for (int i = 0; i < playerMoves.size(); i++) {
            Pieces[] endAndStart = makeMove(playerMoves.get(i));
            //resetAttacked(0);
            ArrayList<Moves> opponentMoves = createMoves(-col);
            if (!inCheck(opponentMoves,col)) {
                legalMoves.add(playerMoves.get(i));
            }
            unMakeMove(playerMoves.get(i), endAndStart[0], endAndStart[1], endAndStart[2]);
        }

        return legalMoves;
    }


    public String isLegal(Moves move) {
        ArrayList<Moves> playerMoves = createLegalMoves(ChessPanel.turn);

        for (int i = 0; i < playerMoves.size(); i++) {
            if (move.getStart()[0] == playerMoves.get(i).getStart()[0] && move.getStart()[1] == playerMoves.get(i).getStart()[1] && move.getEnd()[0] == playerMoves.get(i).getEnd()[0] && move.getEnd()[1] == playerMoves.get(i).getEnd()[1]) {
                    move.setAllMoved();

                    if(board[move.getStart()[0]][move.getStart()[1]].getPiece().getMoved().equals("")) { // this is for enpassant
                           board[move.getStart()[0]][move.getStart()[1]].getPiece().setMoved("1");// "" = not moved, "1" moved once, "2" moved more than once
                        //enpassant is special because if you can play enpassant on a pawn on your turn and you don't play enpassant, then
                        //you lose the ablility to take that pawn  via enpassant for the rest of the game
                   }


                return "true";
            }
        }
        return "";
    }


    public ArrayList<Moves> createMoves(int col) {
        moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getCol() == col) {
                        if (board[i][j].getPiece().getPieceType() == ROOK || board[i][j].getPiece().getPieceType() == BISHOP || board[i][j].getPiece().getPieceType() == QUEEN) {
                            rookBishQueen(new int[]{i, j}, board[i][j]);
                        }
                        if (board[i][j].getPiece().getPieceType() == PAWN) {
                            pawnMoves(new int[]{i, j});
                            pawnCapture(new int[]{i, j});
                            enpassant(new int[]{i, j});
                        }
                        if (board[i][j].getPiece().getPieceType() == KNIGHT) {
                            knightMoves(new int[]{i, j});
                        }
                        if (board[i][j].getPiece().getPieceType() == KING) {
                            kingMoves(new int[]{i, j});
                        }
                    }
                }
            }
        }

        return moves;
    }

    public boolean isSameCol(int[] ind, int[] otherInd) {
        if (ind[0] < 8 && ind[0] >= 0 && ind[1] < 8 && ind[1] >= 0 && otherInd[0] < 8 && otherInd[1] >= 0 && otherInd[1] < 8 && otherInd[0] >= 0) {
            if (board[otherInd[0]][otherInd[1]].getPiece() == null) {
                return false;
            }
            if (board[otherInd[0]][otherInd[1]].getPiece().getCol() == board[ind[0]][ind[1]].getPiece().getCol()) {
                return true;
            }
        }
        return false;
    }
    public Pieces isEnpassant(Moves move){ // if the piece is a pawn and it moves a diagonally, that means that it is making a capture
        int [] startIndex = move.getStart(); // if there is nothing on the end square, then that means the capture must be enpassant
        Pieces startPiece = board[startIndex[0]][startIndex[1]].getPiece();
        int [] endIndex = move.getEnd();
        Pieces endPiece = null;
            if(startPiece.getPieceType() == PAWN) {

                if (board[endIndex[0]][endIndex[1]].getPiece() == null) {// there should be nothing on the square adjacent to the pawn because of the rules of enpassant
                    // this also rules out the possiblity of the move being a capture since there is nothing to capture at the end


                    if (endIndex[1] != startIndex[1]) { // it must have moved diagonally


                        if (startPiece.getCol() == ChessPanel.WHITE) {
                            endPiece = board[move.getEnd()[0] + 1][move.getEnd()[1]].getPiece();
                            return endPiece;
                        }
                        else {
                            endPiece = board[move.getEnd()[0] - 1][move.getEnd()[1]].getPiece();
                            return endPiece;
                        }
                    }
                }
            }
        return null;
    }
    public boolean isDiffCol(int[] ind, int[] otherInd) {
        if (ind[0] < 8 && ind[0] >= 0 && ind[1] < 8 && ind[1] >= 0 && otherInd[0] < 8 && otherInd[0] >= 0 && otherInd[1] < 8 && otherInd[1] >= 0) {
            if (board[otherInd[0]][otherInd[1]].getPiece() == null) {
                return false;
            }
            if (board[otherInd[0]][otherInd[1]].getPiece().getCol() != board[ind[0]][ind[1]].getPiece().getCol()) {
                return true;
            }
        }
        return false;
    }


    public void rookBishQueen(int[] index, Board square) {
        int start = square.getPiece().getPieceType() == BISHOP ? 4 : 0; // index 4-7 in pieceOffsets is diagonally movement
        int stop = square.getPiece().getPieceType() == ROOK ? 4 : 8; // as index 4-7 is diagonally, the rook cannot move diagonally so I will cut off the loop before it gets to diagonal movement
        for (int i = start; i < stop; i++) {
            for (int j = 1; j < 8; j++) { // multiplying j by pieceOffsets essentially adds or subtracts 1 (depending on if pieceOffsets is negative or positive) every iteration so that I can check more squares
                if (index[0] + pieceOffsets[i][0] * j < 8 && index[0] + pieceOffsets[i][0] * j >= 0 && index[1] + pieceOffsets[i][1] * j >= 0 && index[1] + pieceOffsets[i][1] * j < 8) {// making sure that the indexes are not out of bounds
                    if (square.getPiece().getCol() == ChessPanel.WHITE) {
                        board[index[0] + pieceOffsets[i][0] * j][index[1] + pieceOffsets[i][1] * j].setAttackedWhite(board[index[0] + pieceOffsets[i][0] * j][index[1] + pieceOffsets[i][1] * j].getAttackedWhite()+1);
                    }
                    else{
                        board[index[0] + pieceOffsets[i][0] * j][index[1] + pieceOffsets[i][1] * j].setAttackedBlack(board[index[0] + pieceOffsets[i][0] * j][index[1] + pieceOffsets[i][1] * j].getAttackedBlack()+1);
                    }
                    if (isSameCol(index, new int[]{index[0] + pieceOffsets[i][0] * j, index[1] + pieceOffsets[i][1] * j})) { // you can't move to a square that has a piece of the same color, so I make sure that move doesn't get added to the move list
                        break; //also, since its the same col, the direction is now blocked off so I have to switch to a new direction (switch to a different index in pieceOffsets)
                    }

                    if (isDiffCol(index, new int[]{index[0] + pieceOffsets[i][0] * j, index[1] + pieceOffsets[i][1] * j})) { // if there is a enemy piece on the piece's path, then the piece can take the enemy piece
                        // but then the path is blocked, so I have to go to next direction
                        moves.add(new Moves(index, new int[]{index[0] + pieceOffsets[i][0] * j, index[1] + pieceOffsets[i][1] * j}, null,true, false, false));
                        break;
                    }
                    else{
                        moves.add(new Moves(index, new int[]{index[0] + pieceOffsets[i][0] * j, index[1] + pieceOffsets[i][1] * j}, null,false,false, false));
                        .
                        .
                    }

                }
            }
        }
    }

    public int enpassant(int []index){
        if(board[index[0]][index[1]].getPiece().getCol() == ChessPanel.WHITE){
            if(index[0] == 3){ // pawn must be at this rank in order of enpassant to be possible
                if(index[1]+1<8 && index[1]-1>=0) { // checking that right and left of pawn doesn't go out of bounds
                    if (board[index[0]][index[1] + 1].getPiece() != null) { // making sure I don't run into a null pointer
                        if (board[index[0]][index[1] + 1].getPiece().getPieceType() == PAWN && board[index[0]][index[1] + 1].getPiece().getCol() == ChessPanel.BLACK) {
                            if(board[index[0]][index[1] + 1].getPiece().getMoved().equals("1")){
                                moves.add(new Moves(index, new int[]{index[0]-1,index[1]+1},null, true,true, false));
                                board[index[0]][index[1]+1].setAttackedWhite(board[index[0]][index[1]+1].getAttackedWhite()+1);
                                return 9; // I'm returning something because there can only be one enpassant move per position
                                // once I find an enpassant move, I can end the function
                            }
                        }
                    }
                    if(board[index[0]][index[1]-1].getPiece() != null){
                        if (board[index[0]][index[1]-1].getPiece().getPieceType() == PAWN && board[index[0]][index[1]-1].getPiece().getCol() == ChessPanel.BLACK) {
                            if(board[index[0]][index[1]-1].getPiece().getMoved().equals("1")){
                                moves.add(new Moves(index, new int[]{index[0]-1,index[1]-1},null,true,true, false));
                                board[index[0]][index[1]-1].setAttackedWhite(board[index[0]][index[1]+1].getAttackedWhite()+1);
                                return 9;

                            }
                        }
                    }
                }
            }
        }
        else{
            if(index[0] == 4){
                if(index[1]+1<8 && index[1]-1>=0) {
                    if (board[index[0]][index[1] + 1].getPiece() != null) {
                        if (board[index[0]][index[1] + 1].getPiece().getPieceType() == PAWN && board[index[0]][index[1] + 1].getPiece().getCol() == ChessPanel.WHITE) {
                            if(board[index[0]][index[1] + 1].getPiece().getMoved().equals("1")){
                                moves.add(new Moves(index, new int[]{index[0]+1,index[1]+1},null,true,true, false));
                                board[index[0]][index[1]+1].setAttackedBlack(board[index[0]][index[1]+1].getAttackedBlack()+1);
                                return 9;

                            }
                        }
                    }
                    if(board[index[0]][index[1]-1].getPiece() != null){
                        if (board[index[0]][index[1]-1].getPiece().getPieceType() == PAWN && board[index[0]][index[1]-1].getPiece().getCol() == ChessPanel.WHITE) {
                            if(board[index[0]][index[1]-1].getPiece().getMoved().equals("1")){
                                moves.add(new Moves(index, new int[]{index[0]+1,index[1]-1},null,true,true, false));
                                board[index[0]][index[1]-1].setAttackedBlack(board[index[0]][index[1]-1].getAttackedBlack()+1);
                                return 9;

                            }
                        }
                    }
                }
            }
        }
        return 9;
    }
    public void pawnCapture(int[] index) {
        for (int i = 0; i < pawnCaptures.length; i++) {
            if (isDiffCol(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]})) { // making sure that there is an enemy piece to capture
                // board[index[0] + pawnCaptures[i][0]][index[1] + pawnCaptures[i][1]].setAttacked(isAttacked);
                int x = board[index[0] + pawnCaptures[i][0]][index[1] + pawnCaptures[i][1]].getRect().x + sqOffset;
                int y = board[index[0] + pawnCaptures[i][0]][index[1] + pawnCaptures[i][1]].getRect().y + sqOffset;
                int []boardCoords = new int []{index[0] + pawnCaptures[i][0],index[1] + pawnCaptures[i][1]};
                // don't need to check if its black because for it to reach the first rank, it has to be black
                if(board[index[0]][index[1]].getPiece().getCol() == ChessPanel.WHITE){
                    if(i<2){
                        if(index[0] + pawnCaptures[i][0] == 0){
                            Pieces knightPiece = new Pieces(x,y,KNIGHT,WHITE,"", boardCoords,KNIGHTVAL);
                            Pieces bishopPiece = new Pieces(x,y,BISHOP,WHITE,"", boardCoords,BISHOPVAL);
                            Pieces rookPiece = new Pieces(x,y,ROOK,WHITE,"1", boardCoords,ROOKVAL); // this rook cannot be used for castling and this just makes sure of that
                            Pieces queenPiece = new Pieces(x,y,QUEEN,WHITE,"", boardCoords,QUEENVAL);

                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, knightPiece,true,false, false));
                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, bishopPiece,true,false, false));
                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, rookPiece,true,false, false));
                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, queenPiece,true,false, false));

                        }
                        else {

                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, null,true,false, false));
                        }
                    }
                    board[index[0] + pawnCaptures[i][0]][index[1] + pawnCaptures[i][1]].setAttackedWhite(board[index[0] + pawnCaptures[i][0]][index[1] + pawnCaptures[i][1]].getAttackedWhite()+1);
                }
                else{
                    if(i>=2){
                        if(index[0] + pawnCaptures[i][0] == 7){

                            Pieces knightPiece = new Pieces(x,y,KNIGHT,BLACK,"", boardCoords,KNIGHTVAL);
                            Pieces bishopPiece = new Pieces(x,y,BISHOP,BLACK,"", boardCoords,BISHOPVAL);
                            Pieces rookPiece = new Pieces(x,y,ROOK,BLACK,"1", boardCoords,ROOKVAL);
                            Pieces queenPiece = new Pieces(x,y,QUEEN,BLACK,"", boardCoords,QUEENVAL);

                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, knightPiece,true,false, false));
                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, bishopPiece,true,false, false));
                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, rookPiece,true,false, false));
                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, queenPiece,true,false, false));

                        }
                        else {
                            moves.add(new Moves(index, new int[]{index[0] + pawnCaptures[i][0], index[1] + pawnCaptures[i][1]}, null,true,false, false));
                        }
                    }
                    board[index[0] + pawnCaptures[i][0]][index[1] + pawnCaptures[i][1]].setAttackedBlack(board[index[0] + pawnCaptures[i][0]][index[1] + pawnCaptures[i][1]].getAttackedBlack()+1);
                }
            }
        }

    }

    public boolean isOpen(int[] ind) {
        if (ind[0] < 8 && ind[0] >= 0 && ind[1] < 8 && ind[1] >= 0) {
            if (board[ind[0]][ind[1]].getPiece() == null) {
                return true;
            }
        }
        return false;
    }

    public void pawnMoves(int[] index) {
        for (int i = 0; i < pawnMove.length; i++) {
            if (board[index[0]][index[1]].getPiece().getCol() == WHITE) {
                if (pawnMove[i] < 0) { // need to subtract to go up if its white
                    if (pawnMove[i] % 2 == 0) { // checking if we are trying a 2 square move
                        if (board[index[0]][index[1]].getPiece().getMoved().equals("")) { // checking if the pawn has moved before, because if it has, then you cannot do the 2 square move
                            if (isOpen(new int[]{index[0] + pawnMove[i], index[1]})) { // checking if the square which is 2 squares away from the pawn is open
                                if (isOpen(new int[]{index[0] + pawnMove[0], index[1]})) { // checking if the first square in front of the pawn since a pawn cannot use the 2 move rule if there is something directly infront of it
                                    moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, null,false,false, false));
                                }
                            }
                        }
                    }
                    else { // if it is 1 square move then just check if the square infront of pawn is open
                        if (isOpen(new int[]{index[0] + pawnMove[i], index[1]})) { // i don't need to check 2 square movement since the pawn should have lost it 2 square move when it was getting to the other end of the board
                            int x = board[index[0] + pawnMove[i]][index[1]].getRect().x + sqOffset;
                            int y = board[index[0] + pawnMove[i]][index[1]].getRect().y + sqOffset;
                            int []boardCoords = new int []{index[0] + pawnMove[i],index[1]};
                          //  System.out.println(index[0] + pawnCaptures[i][0]);
                            if(index[0] + pawnCaptures[i][0] == 0){
                               // System.out.println("kjwrh");
                                Pieces knightPiece = new Pieces(x,y,KNIGHT,WHITE,"", boardCoords,KNIGHTVAL);
                                Pieces bishopPiece = new Pieces(x,y,BISHOP,WHITE,"", boardCoords,BISHOPVAL);
                                Pieces rookPiece = new Pieces(x,y,ROOK,WHITE,"", boardCoords,ROOKVAL);
                                Pieces queenPiece = new Pieces(x,y,QUEEN,WHITE,"", boardCoords,QUEENVAL);

                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, knightPiece,false,false, false));
                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, bishopPiece,false,false, false));
                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, rookPiece,false,false, false));
                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, queenPiece,false,false, false));

                            }
                            else {
                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, null,false,false, false));
                            }
                        }
                    }
                }
            } else { // same logic as code above but with the numbers being for the other side
                if (pawnMove[i] > 0) {
                    if (pawnMove[i] % 2 == 0) {
                        if (board[index[0]][index[1]].getPiece().getMoved().equals("")) {
                            if (isOpen(new int[]{index[0] + pawnMove[i], index[1]})) {
                                if (isOpen(new int[]{index[0] + pawnMove[2], index[1]})) { // there was case where pawn jumps over pieces
                                    moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, null,false,false, false));
                                }
                            }
                        }
                    }
                    else {
                        if (isOpen(new int[]{index[0] + pawnMove[i], index[1]})) {
                            int x = board[index[0] + pawnMove[i]][index[1]].getRect().x + sqOffset;
                            int y = board[index[0] + pawnMove[i]][index[1]].getRect().y + sqOffset;
                            int []boardCoords = new int []{index[0] + pawnMove[i],index[1]};
                          //  System.out.println(index[0] + pawnCaptures[i][0]);
                            if(index[0] + pawnCaptures[i][0] == 7){
                                Pieces knightPiece = new Pieces(x,y,KNIGHT,BLACK,"", boardCoords,notAttacked);
                                Pieces bishopPiece = new Pieces(x,y,BISHOP,BLACK,"", boardCoords,notAttacked);
                                Pieces rookPiece = new Pieces(x,y,ROOK,BLACK,"", boardCoords,notAttacked);
                                Pieces queenPiece = new Pieces(x,y,QUEEN,BLACK,"", boardCoords,notAttacked);

                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, knightPiece,false,false, false));
                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, bishopPiece,false,false, false));
                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, rookPiece,false,false, false));
                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, queenPiece,false,false, false));

                            }
                            else {
                                moves.add(new Moves(index, new int[]{index[0] + pawnMove[i], index[1]}, null,false,false, false));
                            }
                        }
                    }
                }
            }
        }
    }

    public void knightMoves(int[] index) {
        for (int i = 0; i < knMoves.length; i++) { //going through all possible knight moves and checking if they will land on a piece of the same color
            if (knMoves[i][0] + index[0] < 8 && knMoves[i][0] + index[0] >= 0 && knMoves[i][1] + index[1] < 8 && knMoves[i][1] + index[1] >= 0) {
                if(board[index[0]][index[1]].getPiece().getCol() == ChessPanel.WHITE){
                    board[index[0] + knMoves[i][0]][index[1] + knMoves[i][1]].setAttackedWhite(board[index[0] + knMoves[i][0]][index[1] + knMoves[i][1]].getAttackedWhite()+1);
                }
                else{
                    board[index[0] + knMoves[i][0]][index[1] + knMoves[i][1]].setAttackedBlack(board[index[0] + knMoves[i][0]][index[1] + knMoves[i][1]].getAttackedBlack()+1);
                }
                if (!isSameCol(index, new int[]{index[0] + knMoves[i][0], index[1] + knMoves[i][1]})) {

                    if (isDiffCol(index, new int[]{knMoves[i][0] + index[0], knMoves[i][1] + index[1]})){ // if there is a enemy piece on the piece's path, then the piece can take the enemy piece
                        // but then the path is blocked, so I have to go to next direction
                        moves.add(new Moves(index, new int[]{knMoves[i][0] + index[0], knMoves[i][1] + index[1]}, null,true,false, false));
                    }
                    else{
                        moves.add(new Moves(index, new int[]{knMoves[i][0] + index[0], knMoves[i][1] + index[1]}, null,false,false, false));
                    }
                }
            }
        }
    }

    public void kingMoves(int[] index) { //going through all possible king moves and checking if they will land on a piece of the same color
        for (int i = 0; i < pieceOffsets.length; i++) {
            if (pieceOffsets[i][0] + index[0] < 8 && pieceOffsets[i][0] + index[0] >= 0 && pieceOffsets[i][1] + index[1] < 8 && pieceOffsets[i][1] + index[1] >= 0) {
                if(board[index[0]][index[1]].getPiece().getCol() == ChessPanel.WHITE){
                    board[index[0] + pieceOffsets[i][0]][index[1] + pieceOffsets[i][1]].setAttackedWhite(board[index[0] + pieceOffsets[i][0]][index[1] + pieceOffsets[i][1]].getAttackedWhite()+1);
                }
                else{
                    board[index[0] + pieceOffsets[i][0]][index[1] + pieceOffsets[i][1]].setAttackedBlack(board[index[0] + pieceOffsets[i][0]][index[1] + pieceOffsets[i][1]].getAttackedBlack()+1);
                }
                if (!isSameCol(index, new int[]{index[0] + pieceOffsets[i][0], index[1] + pieceOffsets[i][1]})) {

                    if (isDiffCol(index, new int[]{knMoves[i][0] + index[0], knMoves[i][1] + index[1]})){ // if there is a enemy piece on the piece's path, then the piece can take the enemy piece
                        // but then the path is blocked, so I have to go to next direction
                        moves.add(new Moves(index, new int[]{index[0] + pieceOffsets[i][0], index[1] + pieceOffsets[i][1]}, null,true,false, false));
                    }
                    else{
                        moves.add(new Moves(index, new int[]{index[0] + pieceOffsets[i][0], index[1] + pieceOffsets[i][1]}, null,false,false, false));
                    }
                }
            }
        }
        castling(index, board[index[0]][index[1]].getPiece());

    }
    public void castling(int []index, Pieces king){
        if(index[1]+3<8 && index[1]-4>=0){
            Pieces rightRook = board[index[0]][index[1] + 3].getPiece();
            Pieces leftRook = board[index[0]][index[1] - 4].getPiece();
            boolean whichAttack = king.getCol() == ChessPanel.WHITE;
            if (king.getMoved().equals("")) {
                if (isOpen(new int[]{index[0], index[1] + 2}) && isOpen(new int[]{index[0], index[1] + 1})) { // checking for the 2 squares next to the king and making sure there open
                    if (rightRook != null) {
                        if (rightRook.getPieceType() == ROOK && rightRook.getMoved().equals("")) { // making sure the rook hasn't moved yet
                            if(whichAttack){
                                //king can't castle if its in check or the squares on its path to castling are controlled by enemy pieces
                                if(board[index[0]][index[1]+2].getAttackedBlack() != isAttacked && board[index[0]][index[1]+1].getAttackedBlack() != isAttacked && board[index[0]][index[1]].getAttackedBlack() != isAttacked)
                                    moves.add(new Moves(index, new int[]{index[0], index[1] + 2}, null, false,false, true));
                            }
                            else{
                                if(board[index[0]][index[1]+2].getAttackedWhite() != isAttacked && board[index[0]][index[1]+1].getAttackedWhite() != isAttacked && board[index[0]][index[1]].getAttackedWhite() != isAttacked ){
                                    moves.add(new Moves(index, new int[]{index[0], index[1] + 2}, null, false,false, true));
                                }

                            }
                        }
                    }
                }
                if (index[1] - 2 >= 0 && isOpen(new int[]{index[0], index[1] - 2}) && isOpen(new int[]{index[0], index[1] - 1})) {
                    if (leftRook != null) {
                        if (leftRook.getPieceType() == ROOK && leftRook.getMoved().equals("")) {
                            if (whichAttack) {
                                if (board[index[0]][index[1] - 2].getAttackedBlack() != isAttacked && board[index[0]][index[1] - 1].getAttackedBlack() != isAttacked && board[index[0]][index[1]].getAttackedBlack() != isAttacked ){
                                    moves.add(new Moves(index, new int[]{index[0], index[1] - 2}, null, false, false, true));
                                }
                            }
                            else{
                                if (board[index[0]][index[1] - 2].getAttackedWhite() != isAttacked && board[index[0]][index[1] - 1].getAttackedWhite() != isAttacked && board[index[0]][index[1]].getAttackedWhite() != isAttacked ){
                                    moves.add(new Moves(index, new int[]{index[0], index[1] - 2}, null, false, false, true));
                                }
                            }
                        }
                    }
                }

            }
        }
    }



}














