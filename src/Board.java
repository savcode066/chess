import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
/*
Board.java
Savio Joseph Benher
This class is the backbone of the program. Board allows me to access where the pieces are and the rectangles they are in.
This allows me to easily make moves with the pieces and blit there moves on the screen.
 */
public class Board {
    Rectangle rect;
    Pieces piece;
    int attackedWhite;
    int attackedBlack;

    public int getAttackedWhite() {
        return attackedWhite;
    }

    public void setAttackedWhite(int attackedWhite) {
        this.attackedWhite = attackedWhite;
    }
    public int getAttackedBlack() {
        return attackedBlack;
    }

    public void setAttackedBlack(int attackedBlack) {
        this.attackedBlack = attackedBlack;
    }
    int [] enpassantIndex;

    public int[] getEnpassantIndex() {
        return enpassantIndex;
    }

    public void setEnpassantIndex(int[] enpassantIndex) {
        this.enpassantIndex = enpassantIndex;
    }

    public Board(Rectangle rect, Pieces piece, int attackedWhite, int attackedBlack, int []enpassantIndex){
        this.rect = rect;
        this.piece = piece;
        this.attackedWhite = attackedWhite;
        this.attackedBlack = attackedBlack;
        this.enpassantIndex = enpassantIndex;

    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }
    public Pieces getPiece() {
        return piece;
    }

    public void setPiece(Pieces piece) {
        this.piece = piece;
    }






}


