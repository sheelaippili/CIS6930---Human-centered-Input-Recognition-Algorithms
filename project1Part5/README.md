# Human-Centered Input Recognition Algorithms - Collecting Data from People - Project 1 Part 5

## Goals:
- The goals of part 5 of project 1 are as following:
  - run an offline recognition test with $1 (using your code from Part 3) on your new dataset (from Part 4)
  - output the result of the recognition tests to a log file (same format as Part 3)
  - run your data through the GHOST heatmap toolkit

## Steps to run the project:
* Compile the project1Part5.java file\
```javac project1Part5.java```
* Run the project1Part5.java class file\
  ```java project1Part5```
  
## Goals explained in detail:
### Run an Offline Recognition Test
- Fed the dataset I collected in Part 4 into the offline recognition test application from Part 3.
- As before, I used the random-10* loop and repeated it once per user.

### Output the Result
- generate the log file output.csv for all the 6 users random 10 loop run

### Analyze Dataset using GHoST
- A Windows executable is available on the GHoST project homepage (http://depts.washington.edu/acelab/proj/dollar/ghost.html).
- Downloaded the ran the executable and used it to analyze your own dataset from Part 4
- Selected the dataset from the “Gesture dataset” menu (all users), confirmed the settings in the “Heatmaps settings” menu (align points chronologically, shape error), and generated the heatmaps using the “Compute” menu^.
- Saved the heatmap image (Figure 1) and feature data from the “Export” menu in GHoST (image.bmp)

## Observations
- I observed that the endpoints of the pigtail are orange and red in color because different users ended the gesture at different points.
- Star, circle and pigtail have more orange and red, and the reason may be because these shapes are more rounded and do not have straight lines(except the star)
- I also observed that the whole circle is orange in color because different users drew circles of different sizes.
