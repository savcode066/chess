import javax.swing.*;

public class Chess extends JFrame {
    ChessPanel chess;

    public final int WIDTH = 800;
    public final int HEIGHT = 800;
    public Chess(){
        super("Chess");
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chess = new ChessPanel();

        add(chess);


        pack();
        setResizable(false);
        setVisible(true);

    }

    public static void main(String[] args){
        Chess frame = new Chess();
    }

}