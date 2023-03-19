
# Human-Centered Input Recognition Algorithms - Drawing on a Canvas

## Goals:
- The goals of part 1 of project 1 are as following:
  - set up the project development environment
  - instantiate a blank ‘canvas’ to the screen using GUI elements
  - listen for mouse or touch events on the canvas and draw them as the user makes them
  - allow the user to clear the canvas.
  
  
## Goals explained in detail:
### Set up a development environment
   * All the project deliverables are implemented in java.
   * Java version is jdk 17 and I used Visual Studio code to compile and execute the project.

### Instantiating a canvas
   * I created a class called project1Part1 which has a constructor. When an object is created for the class, a canvas of the size 500 * 500 with the title "canvas" is created.

### Listening for mouse or touch events
   * I used MouseMotionListener class (in the java.awt package) to implement the mouse events.
   * When the mouse is clicked somewhere on the canvas and dragged around the canvas, the mouseDragged function is called where the cursor which is in oval shape fills in the line using ovals.
   * When the mouse is just moved on the screen, the function mouseMoved is called where the line keeps filling at the position. It is used to override the mouseDragged function.
   * I also added the line setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) to close the canvas application when clicking on the close button in the menu bar.
   * Both touch and mouse events are working as expected.

### Clearing the canvas
   * I added a button called "clear" which when clicked calls the actionPerformed function linked to the button which clears the canvas.
   
## Demo
 ![ Alt text](project1Part1GIF. gif) / ! [](project1Part1GIF. gif)
