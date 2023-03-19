import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  
import java.io.*;  
import java.util.*;
import com.opencsv.CSVWriter;


//Point class that contains the x and y coordinate of each point of the unistroke.
class Point{
    double X,Y;
    Point(double x, double y){
        this.X = x;
        this.Y = y;
    }
}

//Rectangle class is used for the bounding box function.
class Rectangle{
    double x,y,width,height;
    Rectangle(double x, double y, double width, double height){
	    this.x = x;
	    this.y = y;
	    this.width = width;
	    this.height = height;
    }
}

//Result class has the name of the unistoke and its score.
class Result{
    String recognisedGestureName;
    double finalScore;
    String sortedNBestList;

    Result(String name, double score){
        this.recognisedGestureName = name;
        this.finalScore = score;
    }

    Result(String name, double score, String sortedNBestList){
        this.recognisedGestureName = name;
        this.finalScore = score;
        this.sortedNBestList = sortedNBestList;

    }
    public double getScore() {
		return finalScore;
	}
}


//Unistroke class has a constructor
//The constructor resamples, rotates, scales and traslates the points.
class Unistroke{

    String name;
    ArrayList<Point> points;

    Unistroke(String name, ArrayList<Point> unistrokePoints){
        this.name = name;
        this.points = unistrokePoints;
        this.points = DollarRecognizer.Resample(points, DollarRecognizer.numPoints);
        double radians = DollarRecognizer.IndicativeAngle(this.points);
        this.points = DollarRecognizer.RotateBy(this.points, -radians);
        this.points = DollarRecognizer.ScaleTo(this.points, DollarRecognizer.squareSize);
        this.points = DollarRecognizer.TranslateTo(this.points,DollarRecognizer.origin);
    }
}


class DollarRecognizer{
    
    public static final double angleRange = deg2Rad(45.0);
    public static final double anglePrecision = deg2Rad(2.0);
    public static final double phi = 0.5 * (-1.0+Math.sqrt(5.0));
    public static final int numPoints = 64;
    public static final double squareSize = 250.0;
    public static final Point origin = new Point(0,0);
    public static final double diagonal = Math.sqrt(squareSize * squareSize + squareSize * squareSize);
    public static final double halfDiagonal = 0.5 * diagonal;

    //Function to recognize the candidate unistroke. This is called whenever the output button on the canvas is clicked.
    //We used GSS instead of protractor to perform the recognition

    public static Result Recognize(Unistroke candidate, ArrayList<Unistroke> training){
        String ans = "";
        double b =  Double.POSITIVE_INFINITY;
        ArrayList<Point> candidatePoints = candidate.points;
        ArrayList<Result> currTrainingResult = new ArrayList<>();
        for (int i=0; i<training.size(); i++){
            double d = DistanceAtBestAngle(candidatePoints, training.get(i), -angleRange, +angleRange, anglePrecision);
            double currScore = 1 - d/(0.5 * Math.sqrt(2 * squareSize * squareSize));
            currTrainingResult.add(new Result(training.get(i).name, currScore));
            if (d < b) {
				b = d;
                ans = training.get(i).name;
			}
        }
        double finalScore = 1.0 - (b/(0.5 * Math.sqrt(2 * squareSize * squareSize)));
        String nBestListString = "";
        //sorting the nBestList based on score
        Collections.sort(currTrainingResult, Comparator.comparing(Result::getScore));
        Collections.reverse(currTrainingResult);
        for (int i=0; i<currTrainingResult.size(); i++){
            if (i<50) {
                String name = currTrainingResult.get(i).recognisedGestureName;
                Double score = currTrainingResult.get(i).finalScore;
                nBestListString = nBestListString + name + ", " + score + ", ";
            }
        }
        nBestListString.substring(0, nBestListString.length() - 2);
        return new Result(ans, finalScore, nBestListString);
    }

    //Function to resample the points.
    public static ArrayList<Point> Resample(ArrayList<Point> points, int n) {
        Point[] p = points.toArray(new Point[points.size()]);
        double I = PathLength(List.of(p)) / (double) (n - 1);
        double D = 0.0;
        ArrayList<Point> newPoints = new ArrayList<>();
        newPoints.add(points.get(0));
        for (int i = 1; i < points.size(); i++) {
            Point p2 = points.get(i);
            Point p1 = points.get(i - 1);
            double d = Distance(p1, p2);
            if (D + d >= I) {
                double qx = p1.X + ((I - D) / d) * (p2.X - p1.X);
                double qy = p1.Y + ((I - D) / d) * (p2.Y - p1.Y);
                Point point = new Point(qx, qy);
                newPoints.add(point);
                points.add(i, point);
                D = 0.0;
            } else {
                D += d;
            }
        }
        if (newPoints.size() == n-1) {
                newPoints.add(new Point(points.get(points.size()-1).X, points.get(points.size()-1).Y));
        }
        return newPoints;
    }

    //Helper function for resample
    public static double PathLength(List<Point> p){
        double d = 0.0;
        for (int i=1; i<p.size(); i++){
            d += Distance(p.get(i-1), p.get(i));
        }
        return d;
    }

    //helper function to calculate the distance between two points
    public static double Distance(Point p1, Point p2){
        double dx = p2.X - p1.X;
        double dy = p2.Y - p1.Y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    //helper function to find out the centroid
    public static double IndicativeAngle(List<Point> p){
        Point d= Centroid(p);
        return Math.atan2(d.X - p.get(0).X,d.X - p.get(0).X);
    }

    //Rotates the points along the centroid.
    public static ArrayList<Point> RotateBy(ArrayList<Point> p, double radians){
        Point d = Centroid(p);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        ArrayList<Point> newp = new ArrayList<>();
        for (int i=0; i<p.size(); i++){
            double qx = (p.get(i).X - d.X) * cos - (p.get(i).Y - d.Y) * sin + d.X;
            double qy = (p.get(i).X - d.X) * sin + (p.get(i).Y - d.Y) * cos + d.Y;
            newp.add(new Point((int)qx,(int)qy));
        }
        return newp;
    }

    //helper function to find out the centroid
    public static Point Centroid(List<Point> p){
        double x = 0.0;
        double y = 0.0;
        for (int i=0; i<p.size(); i++){
            x += p.get(i).X;
            y += p.get(i).Y;
        }
        x /= p.size();
        y /= p.size();
        return (new Point((int)x,(int)y));
    }

    //helper function to find the bounding box
    public static Rectangle BoundingBox(List<Point> p){
        double minX =  Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY, minY =  Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < p.size() ; i++) {
            minX = Math.min(minX, p.get(i).X);
            minY = Math.min(minY, p.get(i).Y);
            maxX = Math.max(maxX, p.get(i).X);
            maxY = Math.max(maxY, p.get(i).Y);
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);	
    }
    
    //Function to scale the unistroke
    public static ArrayList<Point> ScaleTo(ArrayList<Point> p, double squareSize) {
        Rectangle b = BoundingBox(p);
        ArrayList<Point> newP = new ArrayList<>();
        for (int i = 0; i < p.size(); i++) {
            double qx = p.get(i).X * (squareSize / b.width);
            double qy = p.get(i).Y * (squareSize / b.height);
            newP.add(new Point((int)qx, (int)qy));
        }
        return newP;
    }
    
    //function to translate all the points of teh unistroke
    public static ArrayList<Point> TranslateTo(ArrayList<Point> p, Point pt) {
        Point c = Centroid(p);
        ArrayList<Point> newP = new ArrayList<>();
        for (int i = 0; i < p.size(); i++) {
            double qx = (double)p.get(i).X + pt.X - c.X;
            double qy = (double)p.get(i).Y + pt.Y - c.Y;
            newP.add(new Point((int)qx, (int)qy));
        }
        return newP;
    }

    //helper function 
    public static double deg2Rad(double angle) {
        return angle * Math.PI / 180;
    }

    //helper function to recognize the unistroke using GSS
    public static double DistanceAtBestAngle(ArrayList<Point> p, Unistroke t, double f, double theta, double dTheta) {
        double x1 = phi * f + (1-phi)*theta;
        double f1 = DistanceAtAngle(p, t, x1);
        double x2 = (1-phi)*f + phi*theta;
        double f2 = DistanceAtAngle(p, t, x2);

        while((theta-f) > dTheta){
            if(f1 < f2){
                theta = x2;
                x2 = x1;
                f2 = f1;
                x1 = phi*f + (1-phi)*theta;
                f1 = DistanceAtAngle(p, t, x1);
            } else {
                f = x1;
                x1 = x2;
                f1 = f2;
                x2 = (1-phi)*f + phi*theta;
                f2 = DistanceAtAngle(p, t, x2);
            }
        }
        return Math.min(f1, f2);
    }

    //helper function to recognize the unistroke using GSS
    public static double DistanceAtAngle(ArrayList<Point> p,Unistroke t, double radians) {
        ArrayList<Point> newP = RotateBy(p, radians);
        double d = pathDistance(newP,t.points);
        return d;
    }

    //helper function to recognize the unistroke using GSS
    public static double pathDistance(List<Point> pts1,List<Point> pts2)
    {
        double d = 0.0;
        for(int i=0; i<pts1.size();i++){
            d += Distance(pts1.get(i),pts2.get(i));
        }
        return d/pts1.size();
    }

}    


public class project1Part5
{  

//function to choose random candidate template and random training templaes for a gesture
public static ArrayList<Integer> choosingTemplates(int e, ArrayList<Unistroke> templates, int startIDX, int endIDX){
    Random random = new Random();
    Set<Integer> s = new HashSet<>(e);
    while(s.size() != e){
        int idx = random.nextInt(endIDX + 1 - startIDX) + startIDX;
        s.add(idx);
    }
    ArrayList<Integer> templateIndices = new ArrayList<>(s);
    return templateIndices;
}

public static void main(String argv[])   
{  
try   
{  
    //creating a constructor of file class and parsing an XML file  
    //reads all templates of speed medium into an array templates of type unistroke
    String userDirectoryPath = System.getProperty("user.dir");
    ArrayList<Unistroke> templates = new ArrayList<>();
    File folder = new File(userDirectoryPath + "/dataset");
    File[] files = folder.listFiles((dir, name) -> (!(name.equals(".DS_Store"))));
    Arrays.sort(files);
    if (files != null) {
        for (File user : files) {
            File[] gestureFile = user.listFiles((dir, name) -> (!name.equals(".DS_Store")));
            Arrays.sort(gestureFile);
            for (File eachGestureFile : gestureFile){  
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();   
                DocumentBuilder db = dbf.newDocumentBuilder();  
                Document doc = db.parse(eachGestureFile);  
                doc.getDocumentElement().normalize();  
                String name = doc.getElementsByTagName("Gesture").item(0).getAttributes().getNamedItem("Name").getNodeValue();
                NodeList nodeList = doc.getElementsByTagName("Point");
                ArrayList<Point> templatePoints = new ArrayList<>();  
                // nodeList is not iterable, so we are using for loop 
                for (int itr = 0; itr < nodeList.getLength(); itr++)   
                {  
                    Node node = nodeList.item(itr);  
                    if (node.getNodeType() == Node.ELEMENT_NODE)   
                    {  
                        Element eElement = (Element) node;  
                        double X = Double.parseDouble(eElement.getAttributes().getNamedItem("X").getNodeValue());
                        double Y = Double.parseDouble(eElement.getAttributes().getNamedItem("Y").getNodeValue());
                        Point tempPoint = new Point(X, Y);
                        templatePoints.add(tempPoint);
                    }  
                }
                Unistroke tempUnistroke = new Unistroke(name, templatePoints);
                templates.add(tempUnistroke);
            }
        }
    }


  List<String[]> outputData = new ArrayList<>();
  int totalCount = 0;
  double avgScore = 0;

  //random 100 loop
  ArrayList<Integer> templateIndices = new ArrayList<>();
  ArrayList<Unistroke> trainingChosenTemplates =  new ArrayList<>();
  ArrayList<Integer> trainingIndices;
  int candidateIDX = 0;
  int startIDX = 0;
  int gen = 1;
  int totalCorrectCount = 0;
  int rec_score = 0;

  outputData.add(new String[] { "Recognition Log: Sheela Ippili and Varsha Priya Panguluri // $1 algorithm // medium dataset for all users in xml_logs// USER-DEPENDENT RANDOM 10"," "," "," "," "," "," "," "," "," "," "," "," " });
  outputData.add(new String[] { "User[all-users]", "GestureType[all-gestures-types]", "RandomIteration[1to100]", "#ofTrainingExamples[E]", "TotalSizeOfTrainingSet[count]", "TrainingSetContents[specific-gesture-instances]", "Candidate[specific-instance]", "RecoResultGestureType[what-was-recognized]", "CorrectIncorrect[1or0]", "RecoResultScore", "RecoResultBestMatch[specific-instance]", "RecoResultNBestSorted[instance-and-score]" });

//random 10 loop execution
  for (int u = 1; u<=6; u++){
    for (int e = 1; e<=9; e++){
        rec_score = 0;
        for (int i=0; i<10; i++){
            gen = 1;
            trainingChosenTemplates =  new ArrayList<>();
            ArrayList<Unistroke> candidateTemplates = new ArrayList<>();
            for (int g = 0; g<16; g++){
                //choose candidate set and training set randomely
                startIDX = ((u-1) * 160) + ((gen - 1)* 10);
                int endIDX = startIDX + 9;
                gen = gen + 1;
                int E = e+1;
                templateIndices = choosingTemplates(E, templates, startIDX, endIDX);
                candidateIDX = templateIndices.get(0);
                templateIndices.remove(0);
                trainingIndices = templateIndices;
                Unistroke candidateChosenTemplate = templates.get(candidateIDX);
                candidateTemplates.add(candidateChosenTemplate);
                for (int k=0; k<trainingIndices.size(); k++) trainingChosenTemplates.add(templates.get(trainingIndices.get(k)));
            }
            String trainingSetList = "";
            for (int l=0; l<trainingChosenTemplates.size(); l++)  trainingSetList = trainingSetList + trainingChosenTemplates.get(l).name + ", ";
            trainingSetList.substring(0, trainingSetList.length() - 2);
            int correctRecognitions = 0;
            for (Unistroke eachCandidate : candidateTemplates){
                //calling reconizer for each candidate template for 16*e training set
                Result res = DollarRecognizer.Recognize(eachCandidate, trainingChosenTemplates);
                totalCount++;
                String candidateName = eachCandidate.name.trim();
                String finalCandidatename = candidateName;
                candidateName = candidateName.substring(0, candidateName.length()-1);
                if ((candidateName.charAt(candidateName.length()-1)) == '1') candidateName = candidateName.substring(0, candidateName.length()-1);
                String BestScorename = res.recognisedGestureName.trim();
                int isCorrect = 0;
                String matchedGesture = BestScorename.substring(0, BestScorename.length()-1);
                if ((matchedGesture.charAt(matchedGesture.length()-1)) == '1') matchedGesture = matchedGesture.substring(0, matchedGesture.length()-1);
                if (matchedGesture.equalsIgnoreCase(candidateName)){
                    totalCorrectCount++;
                    correctRecognitions++;
                    isCorrect = 1;
                }
                outputData.add(new String[] { String.valueOf(u), candidateName, String.valueOf(i+1), String.valueOf(e), String.valueOf(16*e), trainingSetList, finalCandidatename, matchedGesture, String.valueOf(isCorrect), String.format("%.2f", (double)res.finalScore), BestScorename, (String)res.sortedNBestList });
            }
            rec_score = rec_score + (correctRecognitions/ candidateTemplates.size());

        }
        rec_score = rec_score / 10;
    }
  }

  //Calculating the final recognition score
  avgScore = (double)totalCorrectCount/(double)totalCount;
  String avgScoreResult = "Total Average Accuracy: " + avgScore; 
  outputData.add(new String[]{avgScoreResult," "," " ," "," "," "," "," "," "," "," "," "," "});

  //Writing output to a CSV file
  File file = new File(userDirectoryPath + "/output.csv");
  FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile);
        writer.writeAll(outputData);
        writer.close();

}   
catch (Exception e)  
{  
e.printStackTrace();  
}  
}  
}

