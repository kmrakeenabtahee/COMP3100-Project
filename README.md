# How to run
### To run MyClient.java, first compile with ```javac MyClient.java``` in terminal
#### Next run the test script against the MyClient.class
#### Format: ./"Test Script Name" -n MyClient.class
#### Example: ./S1Tests-wk6.sh -n MyClient.class
#### In the above example S1Tests-wk6.sh is the name of the test script

### To run First_Capable_Modified.java, first compile with ```javac First_Capable_Modified.java``` in terminal
#### Next run the test script against the First_Capable_Modified.class
#### Format: ```./"Test Script Name" -n First_Capable_Modified.class```
#### Example: ```./S1Tests-wk6.sh -n First_Capable_Modified.class```
#### If it is to be tested with the test suite for Stage 2, first the ```First_Capable_Modified.class```
#### and the test suite must be in the same folder. Then the test suite has to be extracted with the necessary requirements of the ```.tar``` file with the OS.
#### Then to run the test suite against the Stage 2 class file, follow as below:
#### Format: ```python3 ./s2_test.py "java Name_Of_File" -n -r results/ref_results.json```
#### Example: ```python3 ./s2_test.py "java First_Capable_Modified" -n -r results/ref_results.json```

