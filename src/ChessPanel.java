import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/*
/*
ChessPanel.java
Savio Joseph Benher
This class loads the images, displays the images, and deals with moves played by the human

 */



public class ChessPanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 700;
    Timer timer;
    Moves possibleMove;
    public static int WHITE = 100, BLACK = -100;
    final int PAWNVAL = 100, KNIGHTVAL = 300, BISHOPVAL = 300, ROOKVAL = 500, QUEENVAL = 900, KINGVAL = 20000;


    int ROOK = 0, KNIGHT = 1, BISHOP = 2, QUEEN = 3, KING = 4, PAWN = 5;
    int startBoardX = 50, startBoardY = 50;
    int endBoardX = 610, endBoardY = 610;
    int sqLen = 70;
    int sqOffset = 10;
    int mouseOffset = 30;
    int pieceWidth = 50, pieceHeight = 50;
    int sqWidth = 70, sqHeight = 70;
    int newSquare;
    int[][] pieceData =
            {{50, 50, ROOK, BLACK, ROOKVAL}, {50, 120, KNIGHT, BLACK, KNIGHTVAL}, {50, 190, BISHOP, BLACK, BISHOPVAL}, {50, 260, QUEEN, BLACK, QUEENVAL}, {50, 330, KING, BLACK, KINGVAL}, {50, 400, BISHOP, BLACK, BISHOPVAL}, {50, 470, KNIGHT, BLACK, KNIGHTVAL}, {50, 540, ROOK, BLACK, ROOKVAL},
                    {120, 50, PAWN, BLACK, PAWNVAL}, {120, 120, PAWN, BLACK, PAWNVAL}, {120, 190, PAWN, BLACK, PAWNVAL}, {120, 260, PAWN, BLACK, PAWNVAL}, {120, 330, PAWN, BLACK, PAWNVAL}, {120, 400, PAWN, BLACK, PAWNVAL}, {120, 470, PAWN, BLACK, PAWNVAL}, {120, 540, PAWN, BLACK, PAWNVAL},
                    {470, 50, PAWN, WHITE, PAWNVAL}, {470, 120, PAWN, WHITE, PAWNVAL}, {470, 190, PAWN, WHITE, PAWNVAL}, {470, 260, PAWN, WHITE, PAWNVAL}, {470, 330, PAWN, WHITE, PAWNVAL}, {470, 400, PAWN, WHITE}, {470, 470, PAWN, WHITE, PAWNVAL}, {470, 540, PAWN, WHITE, PAWNVAL},
                    {540, 50, ROOK, WHITE, ROOKVAL}, {540, 120, KNIGHT, WHITE, KNIGHTVAL}, {540, 190, BISHOP, WHITE, BISHOPVAL}, {540, 260, QUEEN, WHITE, QUEENVAL}, {540, 330, KING, WHITE, KINGVAL}, {540, 400, BISHOP, WHITE, BISHOPVAL}, {540, 470, KNIGHT, WHITE, KNIGHTVAL}, {540, 540, ROOK, WHITE, ROOKVAL}};
    Pieces touchedPiece;
    public static ArrayList<Pieces> pieceInfo = new ArrayList<Pieces>();
    int[] oldCoords = new int[2];

    public static int turn = WHITE;
    boolean start = false;
    Image whiteSquare = new ImageIcon("assets//whiteSquare.png").getImage();
    Image whiteSquareImg = whiteSquare.getScaledInstance(70, 70, Image.SCALE_SMOOTH);

    Image blackSquare = new ImageIcon("assets//blackSquare.png").getImage();
    Image blackSquareImg = blackSquare.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
    Image bg = new ImageIcon("assets//bg.png").getImage();
    Image highl = new ImageIcon("assets//highlight.png").getImage();
    Image highlight = highl.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
    Image rookW, knightW, bishopW, queenW, kingW, pawnW, rookB, knightB, bishopB, queenB, kingB, pawnB;
    Image[] imgsW = {rookW, knightW, bishopW, queenW, kingW, pawnW};
    Image[] imgsB = {rookB, knightB, bishopB, queenB, kingB, pawnB};
    String[] whiteNames = {"rookW", "knightW", "bishopW", "queenW", "kingW", "pawnW"};
    String[] blackNames = {"rookB", "knightB", "bishopB", "queenB", "kingB", "pawnB"};
    Image introbg = new ImageIcon("assets//introbg.png").getImage();
    Image backGround = introbg.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);


    int INTRO = 0, TWOPL = 1, AI = 2;
    int screen = INTRO;
    boolean firstTime = true;


    public static Board[][] board = new Board[8][8];

    int notAttacked = 0;
    int isAttacked = 1;

    //  Pieces currPromotePiece;

    Computer Ai;
    Moves m = new Moves(new int[]{-1, -1}, new int[]{-1, -1}, null, false, false, false);
    boolean wait = false;

    Menu menu;



    Rectangle whitePromoteRect = new Rectangle(620, 550, 70, 70);
    Pieces []whitePromotePieces =
           {new Pieces(-1, -1, QUEEN, WHITE, "1", new int[]{-1, -1}, QUEENVAL),
            new Pieces(-1, -1, ROOK, WHITE, "1", new int[]{-1, -1}, ROOKVAL),
            new Pieces(-1, -1, KNIGHT, WHITE, "1", new int[]{-1, -1}, KNIGHTVAL),
            new Pieces(-1, -1, BISHOP, WHITE, "1", new int[]{-1, -1}, BISHOPVAL)};
    Pieces []blackPromotePieces =
                    {new Pieces(-1, -1, QUEEN, BLACK, "1", new int[]{-1, -1}, QUEENVAL),
                    new Pieces(-1, -1, ROOK, BLACK, "1", new int[]{-1, -1}, ROOKVAL),
                    new Pieces(-1, -1, KNIGHT, BLACK, "1", new int[]{-1, -1}, KNIGHTVAL),
                    new Pieces(-1, -1, BISHOP, BLACK, "1", new int[]{-1, -1}, BISHOPVAL)};
    int whiteIndex = 0;
    int blackIndex = 0;


    Pieces whitePromotePiece = new Pieces(-1, -1, QUEEN, WHITE, "1", new int[]{-1, -1}, QUEENVAL);
    Pieces blackPromotePiece = new Pieces(-1, -1, QUEEN, BLACK, "1", new int[]{-1, -1}, QUEENVAL);
    ;
    Rectangle blackPromoteRect = new Rectangle(620, 30, 70, 70);


    public ChessPanel() {

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();

        addMouseListener(this);
        addMouseMotionListener(this);
        for (int i = 0; i < pieceData.length; i++) {
            pieceData[i][0] += sqOffset;
            pieceData[i][1] += sqOffset;
        }
        int index = 0;
        int firstHalf = 0;
        int secondHalf = 16;
        for (int row = 50; row < 610; row += 70) {
            for (int col = 50; col < 610; col += 70) {
                if (firstHalf <= 15) {
                    board[index / 8][index % 8] = (new Board(new Rectangle(row, col, sqWidth, sqHeight), new Pieces(pieceData[firstHalf][0], pieceData[firstHalf][1], pieceData[firstHalf][2], pieceData[firstHalf][3], "", new int[]{index / 8, index % 8}, pieceData[firstHalf][4]), notAttacked, notAttacked, null));
                    firstHalf++;
                } else if (secondHalf <= 31 && index > 47) {
                    board[index / 8][index % 8] = new Board(new Rectangle(row, col, sqWidth, sqHeight), new Pieces(pieceData[secondHalf][0], pieceData[secondHalf][1], pieceData[secondHalf][2], pieceData[secondHalf][3], "", new int[]{index / 8, index % 8}, pieceData[firstHalf][4]), notAttacked, notAttacked, null);
                    secondHalf++;
                } else {
                    board[index / 8][index % 8] = (new Board(new Rectangle(row, col, sqWidth, sqHeight), null, notAttacked, notAttacked, null));

                }
                index++;
            }
        }
        for (int i = 0; i < imgsW.length; i++) {
            imgsW[i] = new ImageIcon("assets//" + whiteNames[i] + ".png").getImage();
            imgsW[i] = imgsW[i].getScaledInstance(pieceWidth, pieceHeight, Image.SCALE_SMOOTH);

            imgsB[i] = new ImageIcon("assets//" + blackNames[i] + ".png").getImage();
            imgsB[i] = imgsB[i].getScaledInstance(pieceWidth, pieceHeight, Image.SCALE_SMOOTH);

        }
        Ai = new Computer(BLACK, m);
        menu = new Menu();



        timer = new Timer(20, this);
        timer.start();
    }

    public int[] collide(int x, int y) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getRect().contains(x, y)) {
                    return new int[]{j, i}; // inverted
                }
            }
        }
        return new int[]{-1, -1};
    }




    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       int mx = e.getX(), my =e.getY();
       if(whitePromoteRect.contains(mx,my)){
           whiteIndex++;
           whiteIndex = whiteIndex%4;

       }
       if(blackPromoteRect.contains(mx,my)){
           blackIndex++;
           blackIndex = blackIndex%4;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }


    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX(), my = e.getY();
        if (screen != INTRO) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j].getRect().contains(my, mx)) { // inverted
                        if (board[i][j].getPiece() != null) {
                            if (board[i][j].getPiece().getCol() == turn) {
                                start = true;
                                touchedPiece = board[i][j].getPiece();
                                oldCoords[0] = board[i][j].getPiece().getX();
                                oldCoords[1] = board[i][j].getPiece().getY();
                            }
                        }
                    }
                }
            }
        }
        else {
            if (menu.getTwoPlayerRect().contains(mx, my)) {
                screen = TWOPL;
            }
            if (menu.getAiRect().contains(mx, my)) {
                screen = AI;
            }
        }
    }




    @Override
    public void mouseReleased(MouseEvent e) {

        int mouseX = e.getX(), mouseY = e.getY();
        if (screen != INTRO) {
            if (start && touchedPiece != null) {
                if (touchedPiece.getCol() == turn) {
                    int[] newSquare = collide(mouseX, mouseY); // inverted
                    //  System.out.println
                    if (newSquare != new int[]{-1, -1}) {


                        //I try to figure out what the move is before
                        if ((newSquare[0] == 7 || newSquare[0] == 0) && touchedPiece.getPieceType() == PAWN) {
                            Board square = board[newSquare[0]][newSquare[1]];
                            if (turn == WHITE) {
                                whitePromotePiece = whitePromotePieces[whiteIndex];
                                whitePromotePiece.setX(square.getRect().x + sqOffset);
                                whitePromotePiece.setY(square.getRect().y + sqOffset);
                                whitePromotePiece.setBoardCoords(newSquare);
                                possibleMove = new Moves(touchedPiece.getBoardCoords(), newSquare, whitePromotePiece, false, false, false);
                            }
                            else {
                                if (screen != AI){
                                blackPromotePiece = blackPromotePieces[blackIndex];
                                blackPromotePiece.setX(square.getRect().x + sqOffset);
                                blackPromotePiece.setY(square.getRect().y + sqOffset);
                                blackPromotePiece.setBoardCoords(newSquare);
                                possibleMove = new Moves(touchedPiece.getBoardCoords(), newSquare, blackPromotePiece, false, false, false);
                            }
                            }
                        }
                        else if (m.isEnpassant(new Moves(touchedPiece.getBoardCoords(), newSquare, null, false, false, false)) != null) {
                            possibleMove = new Moves(touchedPiece.getBoardCoords(), newSquare, null, false, true, false);
                        }
                        else if (m.isCastling(new Moves(touchedPiece.getBoardCoords(), newSquare, null, false, false, false))) {
                            possibleMove = new Moves(touchedPiece.getBoardCoords(), newSquare, null, false, true, true);
                        }
                        else {
                            possibleMove = new Moves(touchedPiece.getBoardCoords(), newSquare, null, false, false, false);
                        }

                    }



                    if (!possibleMove.isLegal(possibleMove).equals("")) {
                        wait = true;
                        turn *= -1;
                        //  System.out.println("?");
                        if (possibleMove.isCastling()) {
                            // checking which side the player chooses to castle
                            if (board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] + 1].getPiece() != null) {
                                if (board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] + 1].getPiece().getPieceType() == ROOK) {// kingside castling
                                    Pieces rook = board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] + 1].getPiece();
                                    Board newRookSquare = board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] - 1];
                                    Board oldRookSquare = board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] + 1];

                                    newRookSquare.setPiece(rook);// rook is on other side of king hence the -1
                                    oldRookSquare.setPiece(null); // set rook's current square to null since I'm moving the rook off the square
                                    newRookSquare.getPiece().setBoardCoords(new int[]{possibleMove.getEnd()[0], possibleMove.getEnd()[1] - 1}); // set the rooks board coords to the new location

                                    touchedPiece.setX(board[newSquare[0]][newSquare[1]].getRect().x + sqOffset);
                                    touchedPiece.setY(board[newSquare[0]][newSquare[1]].getRect().y + sqOffset);
                                    newRookSquare.getPiece().setX(newRookSquare.getRect().x + sqOffset);
                                    newRookSquare.getPiece().setY(newRookSquare.getRect().y + sqOffset);

                                    board[newSquare[0]][newSquare[1]].setPiece(touchedPiece);
                                    board[touchedPiece.getBoardCoords()[0]][touchedPiece.getBoardCoords()[1]].setPiece(null);
                                    touchedPiece.setBoardCoords(newSquare);
                                }
                            }
                            else if (board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] - 2].getPiece() != null) {
                                if (board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] - 2].getPiece().getPieceType() == ROOK) { // queenside castling
                                    Pieces rook = board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] - 2].getPiece();
                                    Board newSquareRook = board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] + 1];
                                    Board oldSquareRook = board[possibleMove.getEnd()[0]][possibleMove.getEnd()[1] - 2];

                                    newSquareRook.setPiece(rook);
                                    oldSquareRook.setPiece(null);
                                    newSquareRook.getPiece().setBoardCoords(new int[]{possibleMove.getEnd()[0], possibleMove.getEnd()[1] + 1});

                                    touchedPiece.setX(board[newSquare[0]][newSquare[1]].getRect().x + sqOffset);
                                    touchedPiece.setY(board[newSquare[0]][newSquare[1]].getRect().y + sqOffset);
                                    newSquareRook.getPiece().setX(newSquareRook.getRect().x + sqOffset);
                                    newSquareRook.getPiece().setY(newSquareRook.getRect().y + sqOffset);

                                    board[newSquare[0]][newSquare[1]].setPiece(touchedPiece);
                                    board[touchedPiece.getBoardCoords()[0]][touchedPiece.getBoardCoords()[1]].setPiece(null);
                                    touchedPiece.setBoardCoords(newSquare);
                                }
                            }
                        }
                        else if (possibleMove.getPromotionPiece() != null) {
                            int x = board[newSquare[0]][newSquare[1]].getRect().x + sqOffset;
                            int y = board[newSquare[0]][newSquare[1]].getRect().y + sqOffset;
                            possibleMove.promotionPiece.setX(x);
                            possibleMove.promotionPiece.setY(y);
                            board[newSquare[0]][newSquare[1]].setPiece(possibleMove.promotionPiece);
                            board[possibleMove.getStart()[0]][possibleMove.getStart()[1]].setPiece(null);
                            possibleMove.getPromotionPiece().setBoardCoords(newSquare);
                        }
                        else {
                            if (touchedPiece.getPieceType() == PAWN) {
                                if (possibleMove.isEnpassant()) {
                                    if (touchedPiece.getCol() == WHITE) { // checks the square behind the end square as that is the pawn which should be captured
                                        board[possibleMove.getEnd()[0] + 1][possibleMove.getEnd()[1]].setPiece(null);
                                    } else {
                                        board[possibleMove.getEnd()[0] - 1][possibleMove.getEnd()[1]].setPiece(null);
                                    }

                                }
                            }
                            touchedPiece.setX(board[newSquare[0]][newSquare[1]].getRect().x + sqOffset);
                            touchedPiece.setY(board[newSquare[0]][newSquare[1]].getRect().y + sqOffset);

                            board[newSquare[0]][newSquare[1]].setPiece(touchedPiece);

                            board[touchedPiece.getBoardCoords()[0]][touchedPiece.getBoardCoords()[1]].setPiece(null);
                            touchedPiece.setBoardCoords(newSquare);

                        }


                        if (screen == AI) {
                            if (turn == Ai.getCol()) {
                                Ai.playMove();
                            }
                        }


                    }
                    else { // if the move was invalid then return the piece back to its orginal coords
                        touchedPiece.setX(oldCoords[0]);
                        touchedPiece.setY(oldCoords[1]);
                        firstTime = true;
                    }
                }
                else {
                    touchedPiece.setX(oldCoords[0]);
                    touchedPiece.setY(oldCoords[1]);
                    firstTime = true;
                }
            }
        }
        //resets touchedPiece
        touchedPiece = null;
    }



    @Override
    public void mouseDragged(MouseEvent e){
        int mx = e.getX(),my = e.getY();
        if(screen != INTRO) {
            if (touchedPiece != null) {
                if (touchedPiece.getCol() == turn) {
                    if (firstTime) {
                        oldCoords[0] = touchedPiece.getX();
                        oldCoords[1] = touchedPiece.getY();
                        firstTime = false;
                    }
                    touchedPiece.setX(my - mouseOffset); //inverted
                    touchedPiece.setY(mx - mouseOffset);
                }
            }
        }
    }
    @Override
    public void mouseMoved(MouseEvent e){}

    @Override
    public void paint(Graphics g){
        if(screen != INTRO) {
            g.drawImage(bg, 0, 0, null);
            g.setColor(Color.WHITE);
            g.fillRect(whitePromoteRect.x,whitePromoteRect.y,whitePromoteRect.width,whitePromoteRect.height);
            g.drawImage(imgsW[whitePromotePieces[whiteIndex].getPieceType()],whitePromoteRect.x + sqOffset, whitePromoteRect.y+ sqOffset,null);
            g.setColor(Color.BLACK);
            if(screen != AI) {
                g.fillRect(blackPromoteRect.x, blackPromoteRect.y, blackPromoteRect.width, blackPromoteRect.height);
                g.drawImage(imgsB[blackPromotePieces[blackIndex].getPieceType()], blackPromoteRect.x + sqOffset, blackPromoteRect.y + sqOffset, null);
            }

            for (int x = 50; x < 541; x += 70) {
                for (int y = 50; y < 541; y += 70) {
                    if (((x / 10) % 2 == 0 && (y / 10) % 2 == 0) || ((x / 10) % 2 == 1 && (y / 10) % 2 == 1)) {
                        g.drawImage(whiteSquareImg, x, y, null);
                    } else {
                        g.drawImage(blackSquareImg, x, y, null);
                    }

                }
            }


            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    g.drawString(Integer.toString(i), board[i][j].getRect().x + 15, board[i][j].getRect().y + 5);
                    if (board[i][j].getPiece() != null) {
                        if (board[i][j].getPiece().getCol() == WHITE) {
                            g.drawImage(imgsW[board[i][j].getPiece().getPieceType()], board[i][j].getPiece().getY(), board[i][j].getPiece().getX(), null); // change get.Piece().getPiece()
                            // to pieceType

                        } else {
                            g.drawImage(imgsB[board[i][j].getPiece().getPieceType()], board[i][j].getPiece().getY(), board[i][j].getPiece().getX(), null);
                        }
                    }
                }
            }
        }
        else{
            menu.drawIntro(g);
        }



    }
}
