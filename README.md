# Credit Card Processing Application (CCPA)

This service handles the processing of credit card files input from the user.

In its current form, it is capable to understanding the following credit card action types:
-Add
-Charge
-Credit

Each of these action types updates a fictional datastore (Just HashSets) and creates a transaction record as well.

While processing the file, a CreditCardFileProcessingReport is created that specifies the following while processing:
-fileName (name of file its processing)
-processingStartDate (when the processing began)
-processingEndDate (when the processing completed)
-transactionSummary (keeps summary of customer actions created through execution)
-status (overall status of the processing, can be in STARTED, COMPLETED, or FAILED states)
-message (error message if error occured)

# To run the program

Navigate to the root direction and run the following:

./gradlew clean build 

This will build the application

Next, run the following to run the program:
java -jar build/libs/credit_card_processor-0.0.1-SNAPSHOT.jar input.txt
or
java -jar build/libs/credit_card_processor-0.0.1-SNAPSHOT.jar < input.txt

Note: In the case of the file redirection, since the source of the redirection, might not be a file,
we persist this down to a local file and reference that later for processing.

If you don't provide any input, it will wait until you enter something or exit the program.

# To run the tests

Run the following command to run the tests:

./gradlew test

# Object Design

The way this is currently implemented, user input comes into the Application.java class.

From there, the file source is passed to the TextFileProcessingService.java.  This class is responsible for 
parsing the text file, kicking off a new CreditCardFileProcessingReport, and delegating any found action types
to the CreditCardService.java class.

CreditCardService.java will use the CreditCardDao.java class to make updates to our fictional database tables.  
Note that one of the tables is a transaction table and one is the actual credit card table.

When processing is complete, the TextFileProcessingService will complete the CreditCardFileProcessingReport.
Right now this report just contains basic information but could be expanded to hold number of each action type,
total error count, etc.

Finally, Application.java will call this report to print the required output to the System.console.

# Design Considerations (Language, Framework, Design, etc)

I chose Java as its my language of choice.  I've used Ruby and Python, but have more experience with Java and
Spring so I went with those for this exercise.  I use SpringBoot for the scaffolding and Spring for the dependency
injection.  Note that ServiceConfig.java is where I create a Bean that instantiates and specifies the actual 
credit card validator we use (LuhnCreditCardValidator).  If we wanted to switch the validator, we can just flip it
in this file.

I used Lombok to remove some of the boilerplate getters, setters, builders, and constructors where possible.

For the Dao collection representations of the tables, I choose HashSets just to prevent duplicate entries.

For the CreditCardFileProcessingReport.transactionSummary map, I went with a TreeMap to have the customers
sorted in alphabetical order (which was another requirement when producing the console output).

Some other design decisions I went with include choosing composition over inheritance.  I did create an interface
for the CreditCardValidator as this seems like something that could be swapped in and out often.

# Desired Enhancements

Things that I would have liked to add would have included:
-Full end-to-end integration tests (was time restricted so could only include unit tests)

-In memory database (H2 database could have been useful to demonstrate)

-Creating "templates" for each file type so we could quickly update things like the line delimiter, 
field location (ex: if we need to change from position 1 -> 2)

-Having alternate file types (would be cool to demonstrate parsing a json file)

If you have any questions, let me know!
