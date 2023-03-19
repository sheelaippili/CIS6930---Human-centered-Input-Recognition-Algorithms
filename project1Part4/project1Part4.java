//Imported the required libraries to implement the functionalities
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//Point class to store the X and Y coordinates
class Point{
    double X,Y;
    Point(double x, double y){
        this.X = x;
        this.Y = y;
    }
}

//class to create the canvas to draw gestures in
public class project1Part4 extends JFrame implements MouseMotionListener, ActionListener {
    
    protected Button clear;
    protected Button nextGesture;
    protected JTextField displayGesture;
    Scanner sc;
    List<Point> points = new ArrayList<>();
    int numPoints = 0;
    String[] gestureTypes = {"triangle" , "x", "rectangle", "circle", "check", "caret","zig-zag","arrow","left_sq_bracket","right_sq_bracket","v","delete","left_curly_brace", "right_curly_brace", "star","pigtail" };
    int gestureTypeCounter = 0;
    int templateNumberCounter = 1;
    String gestureName = "";
    String templateName = "";
    int userNumber = 0;
    StringBuilder xmlStr = new StringBuilder();


    //the constructor creates the canvas using JFrame class. A clear button is added to the canvas.
    project1Part4(){

        addMouseMotionListener(this);
        setSize(500,500);
        setTitle("Canvas");
        setLayout(null);
        setVisible(true);
        setLocation(500,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clear = new Button("clear");
        clear.setBounds(0,0,60,30);
        clear.setBackground(Color.GRAY);
        clear.addActionListener(this);
        add(clear);
        nextGesture = new Button("nextGesture");
        nextGesture.setBounds(80,0,130,30);
        nextGesture.setBackground(Color.GRAY);
        nextGesture.addActionListener(this);
        add(nextGesture);
        displayGesture = new JTextField("Draw " + gestureTypes[0] + templateNumberCounter);
        add(displayGesture);
        displayGesture.setBounds(0,440,300,40);
        displayGesture.setBackground(Color.GRAY);
        displayGesture.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        displayGesture.setEditable(true);
        displayGesture.setVisible(true);
        sc = new Scanner(System.in);
        System.out.println("Enter user number: ");
        userNumber = sc.nextInt();
        
    }

    //draws the line on the screen when the mouse is moved
    public void mouseDragged(MouseEvent e){
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        //the cursor is oval shaped and fills the line as the mouse is moved
        g.fillOval(e.getX(), e.getY(), 12, 12);
        int x = e.getX();
        int y = e.getY();
        //stores all the points in an arraylist which are later stored in the XML file of the gesture type
        points.add(new Point(x,y));
        numPoints++;
    }

    
    public void actionPerformed(ActionEvent e){
        //clears the screen when the clear button is clicked
        if (e.getSource() == clear)
        {
            Graphics g = getGraphics();
            g.clearRect(0,0, getWidth(), getHeight());
            points = new ArrayList<>();
            numPoints = 0;
            displayGesture.setText("Draw " + gestureTypes[gestureTypeCounter] + templateNumberCounter);
        }
        //functionality to write the data into XML file and move on to the next gesture
        else if (e.getSource() == nextGesture)
        {
            //construct the XML
            if (templateNumberCounter < 10) templateName = gestureTypes[gestureTypeCounter] + "0" +  templateNumberCounter;
            else templateName = gestureTypes[gestureTypeCounter] + templateNumberCounter;
            xmlStr.setLength(0);
            xmlStr.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>" + "\n" + "<Gesture Name=");
            xmlStr.append("\"" + templateName + "\"" + " Subject=" + "\"" + userNumber + "\"" +  " Number=" + "\"" + templateNumberCounter + "\""  + " NumPts=" + "\"" + numPoints + "\" >" + "\n");
            
            for (int i=0; i<points.size(); i++){
                xmlStr.append("  <Point X=\"" + points.get(i).X + "\" Y=\"" + points.get(i).Y + "\" />" + "\n");
            }
            xmlStr.append("</Gesture>");

            //log xml information into a new file
            try {
                String userDirectoryPath = System.getProperty("user.dir");
                String filePath = userDirectoryPath + "/dataset/user" + userNumber+1 + "/" + templateName+ ".txt";
                System.out.println(filePath);
                File myObj = new File(filePath);
                if (!myObj.createNewFile()) {
                  System.out.println("File already exists.");
                }
                FileWriter myWriter = new FileWriter(filePath);
                myWriter.write(xmlStr.toString());
                myWriter.close();
                File rename = new File(userDirectoryPath + userNumber + "/" + templateName+ ".xml");
                myObj.renameTo(rename);


              } catch (IOException x) {
                System.out.println("An error occurred.");
                x.printStackTrace();
              }


            if (templateNumberCounter == 10 && gestureTypeCounter == 15){
                displayGesture.setText("All gestures input done");
            }
            if (templateNumberCounter == 10) {
                templateNumberCounter = 0;
                gestureTypeCounter++;

            }
            templateNumberCounter++;
            Graphics g = getGraphics();
            g.clearRect(0,0, getWidth(), getHeight());
            points = new ArrayList<>();
            numPoints = 0;
            if(gestureTypeCounter <16){
                displayGesture.setText("Draw " + gestureTypes[gestureTypeCounter] + templateNumberCounter);
            }
            

        }
    }

    //nothing is done when the mouse is moved. Overriding the predefined mouseMoved function
    @Override
    public void mouseMoved(MouseEvent e){
    }

    //constructor is called to create the canvas
    public static void main(String[] args){
        String userDirectoryPath = System.getProperty("user.dir");
        File directory = new File(userDirectoryPath + "/dataset");
        if (! directory.exists()) directory.mkdir();
        for (int i=1; i<=6; i++){
            directory = new File(userDirectoryPath + "/dataset" + "/user0" + i);
            if (! directory.exists()) directory.mkdir();
        }
        new project1Part4();
    }
}