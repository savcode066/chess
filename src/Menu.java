import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
/*
Menu.java
Savio Joseph Benher
This class deals with the main menu. It includes fonts and the rectangle and images of the start screen
 */

public class Menu {
    public Rectangle twoPlayerRect = new Rectangle(50,200, 558,100);
    public Rectangle aiRect = new Rectangle(50,400, 558,100);

    Image introbg = new ImageIcon("assets/introbg.png").getImage();
    Image twoPlayerBg = new ImageIcon("assets/2pl.png").getImage();
    Image aiImg = new ImageIcon("assets/ai.png").getImage();



    public static Font fontSys=null, font2pl = null;

    public Menu(){
        fontSys = new Font("Engravers MT",Font.PLAIN,100);
        font2pl = new Font("Engravers MT",Font.PLAIN,32);
    }
    public Rectangle getTwoPlayerRect(){
        return twoPlayerRect;
    }
    public Rectangle getAiRect(){
        return aiRect;
    }

    public void drawIntro(Graphics g){
        g.setFont(fontSys);
        g.setColor(Color.BLACK);
        g.drawImage(introbg, 0,0,null);
        g.drawString("Chess", 50 ,100);
        g.drawImage(twoPlayerBg, 50,200,null);
        g.drawImage(aiImg, 50, 400 ,null);
    }

}
