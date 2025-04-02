Explanation:
1. The User will be presented with a command line interface where he has to choose the option and enter the daily expenses.
2. Calculate the Total Cost of all the coffee and randonly selects a person from the list to pay.
3. Calculates each person expense by comparing the person's amount with the amount being paidby the payer.
4. This is achieved by using Hashmap with the member and the respective amount.
5. The Calculate method returns a hashmap which says how much each one has to owe.
6. The method for selecting the user can be changed to roundrobin or random.

Usage:
1. Run the TrackCoffeeCharges.exe from the command prompt and will be presented with the menu option
2. Enter the daily expense and can calulate the total amount owed which can be tracked in the program.
3. The application needs to be running through out so that on a daily basis the amount can be entered and the calculation will adjust accordingly.

Assumptions and Improvements:
1. The program will be running through out and the person can enter the data on a daily basis.
2. The program can be run daily, but then if we want to track the previous expenses then it will not be possible.
3. Can improve the same by hard committing the final expense details on to a database or file system.
4. Similarly the user details are hard coded in the code, the same can be maintained in config file as well
5. More better approach would be to write these functions as REST API and have a javascript UI which takes input on a daily basis and persists the final details in database.
   
