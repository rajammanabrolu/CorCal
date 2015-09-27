========= Cor-Cal =========
Created by: Prithiraj Ammanabrolu, Nikola Istvanic, Mason Liu, Luke Senseney
GitHub: 


===== Description =====
Cor-Cal is a Graphical User Interface-implemented Collaborative Calendar 
program which allows for the creation and sharing of user calendars. Upon request,
the program can synchronize Calendar data with that from a user's Google Calendar.
Two users can send to each other's calendars time intervals during which
they would like to arrange a meeting; the program will find (if one exists)
a period when both individuals are free.

Events can be added manually by the user to the calendar through the use of drop-down
menus and text fields. Different views of the calendar (weekly or monthly) offer more 
specific or more general information on events in the user's calendar. Upon exiting
the java GUI, all event data is saved.


===== Instructions =====
This requires gradle 2.7 to run. Go to gradle.org/gradle-download/ for directions.
Run "gradle -q run" in the CorCal directory to execute.

In either the monthly or weekly view, click on an event to edit, remove, or view details.

In order to find periods when all parties are free:
1. one user must click Request -> Create to create a meeting request. 
2. They must then fill out the formthat pops up. They will then be asked to save it.
3. They should email that file to all other parties.
4. All parties, including the creator, must click Request -> Respond.
5. They will then be asked to load a request file, and must select the file that was emailed.
6. After that they will be asked to save their response to a file. They must then email their response to the creator.
7. The creator must then click Request -> Process. He will then be directed to load any number of responses and one request.
8. He must upload the original request and the response of all parties, including his own.
9. It will then tell when all parties are free, it at any time.

In order to use the sync -> Google feature, the user must first follow the following directions:
1.  Go to https://console.developers.google.com/start/api?id=calendar
2.  Login and the click the Go to credentials button.
3.  At the top of the page, select the OAuth consent screen tab. 
4.  Select an Email address, enter a Product name if not already set, and 
    click the Save button.
5.  Back on the Credentials tab, click the Add credentials button and select
    OAuth 2.0 client ID.
6.  Select the application type Other and click the Create button.
7.  Click OK to dismiss the resulting dialog.
8.  Click the (Download JSON [little downward arrow]) button to the right of
    the client ID. 
9.  Take the downloaded project and move it (just copy and paste) to C:\ProgramFiles
10. Take the downloaded JSON file and move it to C:\ProgramFiles\_-_-_\src\main\resources
11. Rename the file as Client_Secrets.json
12. You are ready to run the project!