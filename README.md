Explanation:
1. The User will be presented with a command line interface where he has to choose the option and enter the daily expenses.
2. Calculate the Total Cost of all the coffee and randonly selects a person from the list to pay.
3. Calculates each person expense by comparing the person's amount with the amount being paidby the payer.
4. This is achieved by using Hashmap with the member and the respective amount.
5. The Calculate method returns a hashmap which says how much each one has to owe.
6. The method for selecting the user can be changed to roundrobin or random.

Usage:
1. Clone the repository on to the local drive and open the project in any IDE (IntelliJ or VSCode)
2. Compile the code by running javac TrackCoffeeCharges.java
3. This will create a class file and the same needs to be executed by "java TrackCoffeeCharges"
4. Once the program start running it will present you the menu option to add the daily orders
5. Enter the daily expense and choose option 2 and option 3 to calulate and display the the total amount owed by each person.


Assumptions:
1. The system is currenty hard coded with person names (5 persons).
2. One person pay the whole amount for the single day and the others amount will be added to that person's credit. 
3. The program can be run daily, but then if we want to track the previous expenses then it will not be possible.

Improvements:
1. Can improve the same by hard committing the final expense details on to a database or file system.
2. Similarly the user details are hard coded in the code, the same can be maintained in config file as well
3. More better approach would be to write these functions as REST API and have a javascript UI which takes input on a daily basis and persists the final details in database.
   
