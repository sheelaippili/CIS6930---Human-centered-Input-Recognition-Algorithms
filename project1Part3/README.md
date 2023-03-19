# Human-Centered Input Recognition Algorithms - Offline/Test Recognition - Project 1 Part 3

## Goals:
- The goals of part 3 of project 1 are as following:
  - read in a gesture dataset from files to use for templates and candidates
  - connect to your existing $1 pre-processing and recognition methods
  - loop over the gesture dataset to systematically configure your recognizer and test it
  - output the result of the recognition tests to a log file

## Steps to run the project:
* Compile the project1Part3.java file\
```javac project1Part3.java```
* Run the project1Part3.java class file\
  ```java project1Part3```
  
## Goals explained in detail:
### read in a gesture dataset from files to use for templates and candidates
   * The $1 recognizer homepage (http://depts.washington.edu/acelab/proj/dollar/index.html) has a set of unistroke gesture samples (Unistroke gesture logs) in XML format. Download the dataset from the website or find them under xml_logs
   * The xml logs have gesture templates of 16 gesture types, 10 templates per gesture type for 10 users (s02 - s11)
   * Added a method to the project to read the XML files and parse them into an internal data structure
   * A collection of 1600 templates in total are stored, each of which have the raw points and gesture label stored

### connect to the existing $1 pre-processing and recognition methods
  * I used the preprocessing and recognize calls with the $1 algorithm from part 2.
  * For computational efficiency, I preprocessed all gestures and store the preprocessed points prior to starting the offline recognition loop.

### loop over the gesture dataset to systematically configure your recognizer and test it
  * I wrote my own version of the random-10 loop
  * The loop is repeated once per user
  * For each user, I randomly choose a stratified sample of E gestures (that is, balanced per gesture type) to use as templates, and chose 1 gesture per type to use as candidates, per iteration. I repeated these steps 10 times to ensure representative results
  * The pseudo code of the random 10 loop is as follows
    * Random 10 loop
    ```for each user U = 1 to 10
      for each example E = 1 to 9
        for i = 1 to 10
          for each gesture type G
          choose E templates from U,G set
          choose 1 candidate from U,G set
        for each candidate T from 1 to G
          recognize T w/ E,G chosen templates
          if reco correct
            reco score for each U,G += 1
    reco score for each U,G /= 100
report final average per-user accuracy```
      
   
### output the result of the recognition tests to a log file
   * After each recognition call has returned a result, check whether the recognition is correct (e.g., the returned label matches the expected label). Keep track of the results and output them to a file, including the following details per call: (a) the user U, (b) gesture type G of the candidate, (c) number of templates/training examples E, (d) complete template/training set size and contents, (e) recognition result (including score), and (f) full N-Best list with scores. 
   * Finally, output the total average accuracy across all recognition calls at the end of the output.csv file.
   
