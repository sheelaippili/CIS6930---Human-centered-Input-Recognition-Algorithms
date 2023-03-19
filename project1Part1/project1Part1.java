import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class project1Part1 extends JFrame implements MouseMotionListener, ActionListener {
    

    //the constructor creates the canvas using JFrame class. A clear button is added to the canvas which when clicked clears the canvas.
    project1Part1(){

        addMouseMotionListener(this);
        setSize(500,500);
        setTitle("Canvas");
        setLayout(null);
        setVisible(true);
        setLocation(500,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Button b = new Button("clear");
        b.setBounds(0,0,60,30);
        b.setBackground(Color.GRAY);
        b.addActionListener(this);
        add(b);
        
    }

    //draws the line on the screen when the mouse is moved
    public void mouseDragged(MouseEvent e){
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        //the cursor is oval shaped and fills the line as the mouse is moved
        g.fillOval(e.getX(), e.getY(), 12, 12);
    }

    //clears the screen when the clear button is clicked
    public void actionPerformed(ActionEvent e){
        Graphics g = getGraphics();
        g.clearRect(0,0, getWidth(), getHeight());
    }

    //nothing is done when the mouse is moved. Overriding the predefined mouseMoved function
    @Override
    public void mouseMoved(MouseEvent e){
    }

    //constructor is called to create the canvas
    //Click on the canvas window close button to abort the run or stop the program execution.
    public static void main(String[] args){
        new project1Part1();
    }
}