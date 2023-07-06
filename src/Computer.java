import java.util.ArrayList;
import java.util.*;
/*
Computer.java
Savio Joseph Benher
This is the ai. The ai can calculate 3 moves ahead. The ai understands good general piece placement.
The ai evalutes positions based on piece placement and the material for both sides of the board.
 */

public class Computer {
    Board[][] board = ChessPanel.board;
    int ROOK = 0, KNIGHT = 1, BISHOP = 2, QUEEN = 3, KING = 4, PAWN = 5;
    int WHITE = 100, BLACK = -100;
    final int PAWNVAL = 100, KNIGHTVAL = 300, BISHOPVAL = 300, ROOKVAL = 500, QUEENVAL = 900, KINGVAL = 10000;


    int sqOffset = 10;
    int col;
    Moves move;
    int inf = 2147483647;
    int whichTurn = ChessPanel.turn;
    Moves moveToPlay;


    public int[][] bestRookWhiteSquares = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {25, 25, 25, 25, 25, 25, 25, 25},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0,0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 5, 5, 0, 0, 0}};

    public int[][] bestRookBlackSquares = { // where the rook is placed doesn't really matter but if its well known that a rook is good on the 7th or 2nd rank
            {0, 0, 0, 0, 0, 0, 0, 0},
            {25, 25, 25, 25, 25, 25, 25, 25},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {5, 5, 5, 5, 5, 5, 5, 5},
            {0, 0, 0, 0, 0, 0, 0, 0}};


    public int[][] bestQueenSquares = {

            {-30, -15, -15, -15, -15, -15, -15, -30},
            {-20, 0, 0, 0, 0, 0, 0, -20},
            {-10, 0, 20, 20, 20, 20, 0, -10},
            {-5, 0, 40, 40, 40, 40, 0, -5},
            {0, 0, 40, 40, 40, 40, 0, 0},
            {-10, 20, 20, 20, 20, 20, 20, -10},
            {-20, 0, 0, 0, 0, 0, 0, -20},
            {-30, -15, -15, -15, -15, -15, -15, -30}};

    public int[][] bestBishopSquares = {
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-10, 20, 20, 20, 20, 20, 20, -10},
            {-10, 25, 25, 30, 30, 25, 25, -10},
            {-10, 25, 25, 30, 30, 10, 25, -10},
            {-10, 20, 20, 20, 20, 20, 20, -10},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-20, -10, -10, -10, -10, -10, -10, -20}};

    public int[][] bestKnightSquares = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20, 0, 0, 0, 0, -20, -40},
            {-30, 0, 10, 15, 15, 10, 0, -30},
            {-30, 5, 15, 20, 20, 15, 5, -30},
            {-30, 0, 15, 20, 20, 15, 0, -30},
            {-30, 5, 10, 15, 15, 10, 5, -30},
            {-40, -20, 0, 5, 5, 0, -20, -40},
            {-50, -40, -30, -30, -30, -30, -40, -50}};
    public int[][] bestPawnSquaresWhite = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5, 5, 10, 25, 25, 10, 5, 5},
            {0, 0, 0, 20, 20, 0, 0, 0},
            {5, -5, -10, 0, 0, -10, -5, 5},
            {5, 10, 10, -20, -20, 10, 10, 5},
            {0, 0, 0, 0, 0, 0, 0, 0}};
    public int[][] bestKingSquaresWhite = {
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-20, -40, -40, -40, -40, -40, -40, -20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            {20, 20, 0, 0, 0, 0, 20, 20},
            {20, 30, 10, 0, 0, 10, 30, 20}};
    public int[][] bestPawnSquaresBlack = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {5, 10, 10, -20, -20, 10, 10, 5},
            {5, -5, -10, 0, 0, -10, -5, 5},
            {0, 0, 0, 20, 20, 0, 0, 0},
            {5, 5, 10, 25, 25, 10, 5, 5},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {0, 0, 0, 0, 0, 0, 0, 0}};
    public int[][] bestKingSquaresBlack = {
            {20, 30, 10, 0, 0, 10, 30, 20},
            {20, 20, 0, 0, 0, 0, 20, 20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-30, -40, -50, -50, -50, -40, -40, -30},
            {-30, -40, -50, -50, -50, -40, -40, -30},
            {-30, -40, -50, -50, -50, -40, -40, -30},
            {-30, -40, -50, -50, -50, -40, -40, -30},
            {-30, -40, -50, -50, -50, -40, -40, -30}};



    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Moves getMove() {
        return move;
    }

    public void setMove(Moves move) {
        this.move = move;
    }

    public Computer(int col, Moves move) {
        this.move = move;
        this.col = col;
    }

    public int countMaterial(int col) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getCol() == col) {
                        if (board[i][j].getPiece().getPieceType() == PAWN) {
                            count += PAWNVAL;
                        }
                        if (board[i][j].getPiece().getPieceType() == KNIGHT) {
                            count += KNIGHTVAL;
                        }
                        if (board[i][j].getPiece().getPieceType() == BISHOP) {
                            count += BISHOPVAL;
                        }
                        if (board[i][j].getPiece().getPieceType() == ROOK) {
                            count += ROOKVAL;
                        }
                        if (board[i][j].getPiece().getPieceType() == ROOK) {
                            count += ROOKVAL;
                        }
                        if (board[i][j].getPiece().getPieceType() == QUEEN) {
                            count += QUEENVAL;
                        }
                        if (board[i][j].getPiece().getPieceType() == KING) {

                            count += KINGVAL;
                        }
                    }
                }
            }
        }
        return count;
    }




    public Moves negaMax() {
        ArrayList<Moves> playerMoves = move.createLegalMoves(ChessPanel.turn);


        int max = -inf;
        moveToPlay = null;
        for (int i = 0; i < playerMoves.size(); i++) {
            Pieces[] endAndStartPiece = move.makeMove(playerMoves.get(i)); // make the move on the board which changes the result of negamax function
            int score = -negaMax(3, -inf, inf);
            if (score > max) {
                moveToPlay = playerMoves.get(i);
                max = score;
            }

            move.unMakeMove(playerMoves.get(i), endAndStartPiece[0], endAndStartPiece[1], endAndStartPiece[2]);
        }
        return moveToPlay;
    }

    public int negaMax(int depth, int alpha, int beta) { // I got negamax from https://www.chessprogramming.org/Negamax
        // I got alpha beta pruning from https://www.chessprogramming.org/Alpha-Beta
        if (depth == 0) {
           whichTurn = ChessPanel.turn;
           return evaluate();
        }
        ArrayList<Moves> moves = move.createLegalMoves(whichTurn);
        whichTurn *= -1;
        if (moves.size() == 0) { // no moves
            ArrayList<Moves> otherMoves = move.createLegalMoves(whichTurn);
            if (move.inCheck(otherMoves,whichTurn)) { // king is in check
                return -inf; // having no moves and a king in check is the definition of checkmate
                //its infinity because checkmate is the worst possible result
            }

            return 0; //having no moves and a king that is not in check is the definition of stalemate
            // its 0 since stalemate is a draw
        }
        for (int i = 0; i < moves.size(); i++) {
            Pieces[] endAndStart = move.makeMove(moves.get(i));
            int score = -negaMax(depth - 1, -beta, -alpha);
            move.unMakeMove(moves.get(i), endAndStart[0], endAndStart[1], endAndStart[2]);
            if (score >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, score);
        }
        return alpha;
    }





    public int evaluate() { //I got the idea for this function from https://www.chessprogramming.org/Negamax
        int sumEval = 0;
        int evalPiecesW = 0, evalPiecesB = 0;
        int whiteMaterial = countMaterial(WHITE);
        int blackMaterial = countMaterial(BLACK);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getPiece() != null) {
                    if (board[i][j].getPiece().getCol() == WHITE) {
                        if (board[i][j].getPiece().getPieceType() == KNIGHT) {
                            evalPiecesW += bestKnightSquares[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == PAWN) {
                            evalPiecesW += bestPawnSquaresWhite[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == KING) {
                                evalPiecesW += bestKingSquaresWhite[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == BISHOP) {
                            evalPiecesW += bestBishopSquares[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == ROOK) {
                            evalPiecesW += bestRookWhiteSquares[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == QUEEN) {
                            evalPiecesW += bestQueenSquares[i][j];
                        }
                    }
                    else {
                        if (board[i][j].getPiece().getPieceType() == KNIGHT) {
                            evalPiecesB += bestKnightSquares[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == PAWN) {
                            evalPiecesB += bestPawnSquaresBlack[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == KING) {
                                evalPiecesB += bestKingSquaresBlack[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == BISHOP) {
                            evalPiecesB += bestBishopSquares[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == ROOK) {
                            evalPiecesB += bestRookBlackSquares[i][j];
                        }
                        if (board[i][j].getPiece().getPieceType() == QUEEN) {
                            evalPiecesB += bestQueenSquares[i][j];
                        }

                    }
                }
            }
        }
        sumEval = ((evalPiecesW + whiteMaterial ) - (evalPiecesB + blackMaterial));


        return sumEval * (ChessPanel.turn / 100);


    }

    public void makeMove(Pieces pieceToMove, Moves move) {
        int[] newSquare = move.getEnd();
        move.setAllMoved();
        if (board[move.getStart()[0]][move.getStart()[1]].getPiece().getMoved().equals("")) {
            pieceToMove.setMoved("1");
        }
        if (pieceToMove.getPieceType() == PAWN) {
            if (move.isEnpassant()) {
                if (pieceToMove.getCol() == ChessPanel.WHITE) {
                    board[move.getEnd()[0] + 1][move.getEnd()[1]].setPiece(null);
                } else {
                    board[move.getEnd()[0] - 1][move.getEnd()[1]].setPiece(null);
                }
            }
        }
        if (move.promotionPiece != null) {
            move.getPromotionPiece().setX(board[newSquare[0]][newSquare[1]].getRect().x + sqOffset); // setting the promotion piece's coords on the board
            move.getPromotionPiece().setY(board[newSquare[0]][newSquare[1]].getRect().y + sqOffset);
            board[newSquare[0]][newSquare[1]].setPiece(move.getPromotionPiece()); // putting the piece on the end square
            board[move.getStart()[0]][move.getStart()[1]].setPiece(null); // getting rid of the piece on the start square
            move.getPromotionPiece().setBoardCoords(newSquare); // setting boords coords
        }
        else if(move.isCastling()){
            if(board[move.getEnd()[0]][move.getEnd()[1]+1].getPiece()!=null) {
                if (board[move.getEnd()[0]][move.getEnd()[1] + 1].getPiece().getPieceType() == ROOK) {// kingside castling
                    Pieces rook = board[move.getEnd()[0]][move.getEnd()[1] + 1].getPiece();
                    Board newRookSquare = board[move.getEnd()[0]][move.getEnd()[1] - 1];
                    Board oldRookSquare = board[move.getEnd()[0]][move.getEnd()[1] + 1];

                    newRookSquare.setPiece(rook);// rook is on other side of king hence the -1
                    oldRookSquare.setPiece(null); // set rook's current square to null since I'm moving the rook off the square
                    newRookSquare.getPiece().setBoardCoords(new int[]{move.getEnd()[0], move.getEnd()[1] - 1}); // set the rooks board coords to the new location

                    pieceToMove.setX(board[newSquare[0]][newSquare[1]].getRect().x + sqOffset);
                    pieceToMove.setY(board[newSquare[0]][newSquare[1]].getRect().y + sqOffset);
                    newRookSquare.getPiece().setX(newRookSquare.getRect().x + sqOffset);
                    newRookSquare.getPiece().setY(newRookSquare.getRect().y + sqOffset);

                    board[newSquare[0]][newSquare[1]].setPiece(pieceToMove);
                    board[pieceToMove.getBoardCoords()[0]][pieceToMove.getBoardCoords()[1]].setPiece(null);
                    pieceToMove.setBoardCoords(newSquare);
                }
            }
            else if(board[move.getEnd()[0]][move.getEnd()[1]-2].getPiece()!=null) {
                if(board[move.getEnd()[0]][move.getEnd()[1] - 2].getPiece().getPieceType() == ROOK) { // queenside castling
                    Pieces rook = board[move.getEnd()[0]][move.getEnd()[1] - 2].getPiece();
                    Board newSquareRook = board[move.getEnd()[0]][move.getEnd()[1] + 1];
                    Board oldSquareRook = board[move.getEnd()[0]][move.getEnd()[1] - 2];

                    newSquareRook.setPiece(rook);
                    oldSquareRook.setPiece(null);
                    newSquareRook.getPiece().setBoardCoords(new int[]{move.getEnd()[0], move.getEnd()[1] + 1});

                    pieceToMove.setX(board[newSquare[0]][newSquare[1]].getRect().x + sqOffset);
                    pieceToMove.setY(board[newSquare[0]][newSquare[1]].getRect().y + sqOffset);
                    newSquareRook.getPiece().setX(newSquareRook.getRect().x + sqOffset);
                    newSquareRook.getPiece().setY(newSquareRook.getRect().y + sqOffset);

                    board[newSquare[0]][newSquare[1]].setPiece(pieceToMove);
                    board[pieceToMove.getBoardCoords()[0]][pieceToMove.getBoardCoords()[1]].setPiece(null);
                    pieceToMove.setBoardCoords(newSquare);
                }
            }
        }

        else {
            pieceToMove.setX(board[newSquare[0]][newSquare[1]].getRect().x + sqOffset); // setting the promotion piece's coords on the board
            pieceToMove.setY(board[newSquare[0]][newSquare[1]].getRect().y + sqOffset);
            // I don't need to check if the move is a capture because setting the square to the piece works if square is null or enemy piece
            board[newSquare[0]][newSquare[1]].setPiece(pieceToMove); // putting the piece on the end square
            board[pieceToMove.getBoardCoords()[0]][pieceToMove.getBoardCoords()[1]].setPiece(null); // getting rid of the piece on the start square
            pieceToMove.setBoardCoords(newSquare); // setting boords coords
        }

    }


    public Moves playMove(){
      Moves moveToPlay = negaMax();
      Pieces startPiece = board[moveToPlay.getStart()[0]][moveToPlay.getStart()[1]].getPiece();
      makeMove(startPiece, moveToPlay);
      ChessPanel.turn*=-1;
      return moveToPlay;

    }

}
