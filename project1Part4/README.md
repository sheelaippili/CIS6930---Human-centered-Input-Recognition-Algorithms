# Human-Centered Input Recognition Algorithms - Collecting Data from People - Project 1 Part 4

## Goals:
- The goals of part 3 of project 1 are as following:
  - update your GUI canvas code to write the gesture the user draws to a file;
  - add prompts to the user to draw 10 samples of each gesture type one at a time (and write them to files);
  - recruit 6 people to provide gesture samples for your project; and
  - submit your full dataset.

## Steps to run the project:
* Compile the project1Part4.java file\
```javac project1Part4.java```
* Run the project1Part4.java class file\
  ```java project1Part4```
* Enter user number (1 - 6) in command prompt (users 1- 6)
  
## Goals explained in detail:
### Write Gesture Files
   * Updated the GUI canvas code to write the gesture the user draws to an XML file. 
   * Used the same XML format as the Unistroke gesture logs we used in Part 3 from the $1 recognizer homepage for the gesture files

### Prompt for Specific Samples
  * Further modified the code to step through each gesture type (one at a time) from the $1 recognizer homepage (http://depts.washington.edu/acelab/proj/dollar/index.html) to save 10 samples of each type (16 total) from a user.
  * I displayed a prompt on the screen to tell the user which gesture to draw next. Click on next gesture button to move on to the next gesture.

### Recruit 6 People
  * I recruited 6 people as users to draw the gestures
  * The names would remain anonymous
      
### Save the gesture set
   * A total of 960 files would be generated for each user.
   * The dataset could be found in project1Part5 folder udner teh dataset folder
   
