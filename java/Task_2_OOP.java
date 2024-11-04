import java.util.*;
import java.io.*;

/******************************** 
 * Title: Task_2_OOP
 * Author: Daniil Sergeev
 * Date Created: 03/05/2024
 * Date Modified: 03/05/2024
 * Purpose: To Add CSV data to an object array, perform data analysis and data filteringusing an Object Oriented Solution
*/



/********************************
 * Author: Daniil Sergeev
 * Date: 03/05/2024
 * Purpose: Main class where program mostly runs
 */
public class Task_2_OOP 
{
    private static Scanner input = new Scanner(System.in); // scanner is a global variable so it doesnt have to be passed to other functions that want input


    /********************************* 
     * Name: main
     * Date 03/05/2024
     * Inport: argvs (String[]),
     * Export: N/A
     * Purpose: Entry point of program sets a few variables and proceeds to file I/O
    */
    public static void main(String[] argvs)
    {
        Team[] team = null; // Empty Team object array declared
        int linecount = 0;

        FileIO(team, linecount);

    }


    /********************************* 
     * Name: FileIO
     * Date 03/05/2024
     * Inport: team (Team[]), linecount (int)
     * Export: N/A
     * Purpose: User input is taken for filename with exception handling and appropriate error handling, total data entries are counted and objects are created
    */
    public static void FileIO(Team[] team, int linecount)
    {
        FileInputStream fileStream = null;
        InputStreamReader rdr;
        BufferedReader bufRdr;
        int lineNum;
        //int linecount = 0;
        
        String line;
        String fileName = "";

        // Top block goes through the file to find out how many lines the file had (used for Team object array initilization)
        try
        {
            System.out.println("Welcome to the F1 Task 2\n\nPlease Enter A CSV file to Analyze");

            fileName = input.nextLine();
            
            fileStream = new FileInputStream(fileName); // creates a file stream and reads user input for file name
            rdr = new InputStreamReader(fileStream); // reads characters from bytes
            bufRdr = new BufferedReader(rdr); // creates a line buffer
    
            line = bufRdr.readLine(); // reads line in buffer and stores it

            while(line != null)
            {
                linecount++;
                line = bufRdr.readLine(); // next line is recovered
            }
            fileStream.close();

            //System.out.println("Linecount = " + linecount);
        }
        catch(IOException errorDetails)
        {
            System.out.println("Error in file Processing" + errorDetails.getMessage());
        }
    


        // Uses line count from previous block to set array length and uses polymorthism to initilize objects
        team = new Team[linecount]; // initializes array with number of teams

        for(int i = 0; i < linecount; i++)
        {
            team[i] = new Team(); // initializes objects in array (through polymorthism)
        }


        // Block takes care of file reading
        try 
        {
            
            fileStream = new FileInputStream(fileName); // creates a file stream and reads user input for file name
            rdr = new InputStreamReader(fileStream); // reads characters from bytes
            bufRdr = new BufferedReader(rdr); // creates a line buffer
            lineNum = 0;
            line = bufRdr.readLine(); // reads line in buffer and stores it

            // object creation
            int objIndex = 0;


            while(line != null)
            {
                lineNum++;

                if(lineNum != 1) // if statement skips over the headers in the first line
                {
                    objIndex = ParseCSV(line, objIndex, team);
                }

                line = bufRdr.readLine(); // next line is recovered
            }
            fileStream.close();

            DisplayRaceCompletionTeams(team, linecount);
            DisplayFastestTeam(team, linecount);
        }
        catch(IOException errorDetails) // error message not needed because its done in the first file IO
        {
        }
    }


    /********************************* 
     * Name: ParseCSV
     * Date 03/05/2024
     * Inport: csvRow (String), objIndex (int), team (Team[])
     * Export: objIndex (int)
     * Purpose: To populate team array
    */
    public static int ParseCSV(String csvRow, int objIndex, Team[] team)
    {
        String[] splitLine;
        splitLine = csvRow.split(",");
        
        // Set object values
        team[objIndex].SetTeamName(splitLine[0]);
        team[objIndex].SetCarCode(splitLine[1]);
        team[objIndex].SetDriverName(splitLine[2]);
        team[objIndex].SetGrandPrix(splitLine[3]);
        team[objIndex].SetPositionFinished(Integer.parseInt(splitLine[4]));
        team[objIndex].SetFastestLapTime(Double.parseDouble(splitLine[5]));


        //System.out.println("Team Name " + objIndex + " " + team[objIndex].teamName + " " + team[objIndex].carCode);
        objIndex++; // returns the index of the NEXT object (create objects before this point)

        return objIndex;
    }


    /********************************* 
     * Name: DisplayRaceCompletionTeams
     * Date 05/05/2024
     * Inport: team (Team[]), linecount (int)
     * Export: N/A
     * Purpose: Displays which team had at least 1 driver complete the race and which team had no drivers completing the race
    */
    public static void DisplayRaceCompletionTeams(Team[] team, int linecount)
    {
        Boolean CheckRepeat;
        int x = 0;
        Clear();
         
        System.out.println("Teams which had at least 1 car complete race"); 
        for(int i = 0; i < linecount - 1; i++) // block prevents printing repeat values
        {
            CheckRepeat = false;
            
            while (x < i) 
            {
                if (team[i].teamName.equals(team[x].teamName))
                {
                    CheckRepeat = true;
                }
                x++;
            }
    
            // If the team name is not repeated and the position is not -1, print it
            if (!CheckRepeat && team[i].positionFinished != -1)
            {
                System.out.println("TeamName: " + team[i].teamName);
            }
        }




        x = 0;
        //Next block displays teams with no drivers that completed race
        System.out.println("\nTeams which had NO drivers that completed race");
        for(int i = 0; i < linecount - 1; i++)
        {
            CheckRepeat = false;
            
            while (x < i) 
            {
                if (team[i].teamName.equals(team[x].teamName))
                {
                    CheckRepeat = true;
                }
                x++;
            }

            if(CheckRepeat == false && team[i].positionFinished == -1) // prints teams that had no drivers that finished the race
            {
                System.out.println("TeamName: " + team[i].teamName);
            }
        }
    }


    /********************************* 
     * Name: DisplayFastestTeam
     * Date 05/05/2024
     * Inport: team (Team[]), linecount (int)
     * Export: N/A
     * Purpose: Displays the average time of the fastest team
    */
    // ASSUMING THERE ARE 2 drivers per team
    public static void DisplayFastestTeam(Team[] team, int linecount)
    {
        int x = 0;
        int TeamCount = linecount - 1;
        Double Time = 0.0;
        Double FastestTime = 205.50;
        String FastestTeam = null;
         

        // This block calulates the average times for teams with 2 racers
        System.out.println("\n\nFastest Average Team"); 
        for(int i = 0; i < linecount - 1; i++) // block checks if teamnames have been repeated na dif they finished before printing them
        {
            team[i].SetCheckRepeat(false);
            
            while (x < i) 
            {
                if (team[i].teamName.equals(team[x].teamName)) // repeat flags added to objects
                {
                    team[i].SetCheckRepeat(true);
                    team[x].SetCheckRepeat(true);

                    Time = (team[i].fastestLapTime + team[x].fastestLapTime) / 2;

                    team[i].SetAVGTime(Time); // set average time for team
                    team[x].SetAVGTime(Time);

                    if(Time < FastestTime) // set fastest time an team of time is faster then predefined fastest time of 205.50 seconds
                    {
                        FastestTime = Time;
                        FastestTeam = team[i].teamName;
                    }

                    TeamCount--;
                }
                x++;
            }
    
        }


        
        // this block calculates the times for teams with 1 racer
        for(int i = 0; i < linecount - 1; i++)
        {
            if(team[i].checkRepeat == false)
            {
                Time = team[i].fastestLapTime;

                if(Time < FastestTime) // set fastest time an team of time is faster then predefined fastest time of 205.50 seconds
                {
                    FastestTime = Time;
                    FastestTeam = team[i].teamName;
                }

                team[i].avgTime = Time;
            }
        }
        System.out.println("TeamName: " + FastestTeam + " AVG Time " + FastestTime);




        SortTeamsDescending(team, linecount, TeamCount);
        SortDrivers(team, linecount);
   }


    /********************************* 
     * Name: SortTeamsDescending
     * Date 08/05/2024
     * Inport: team (Team[]), linecount (int), TeamCount (int)
     * Export: N/A
     * Purpose: Displays sorted team average times in descending order (and not printing duplicates)
    */
   // Methods sorts team times in descending order
   public static void SortTeamsDescending(Team[] team, int linecount, int TeamCount)
   {
        // Bubble sort is used in this method
        // I dont care about performance its simple to implement
        // assuming descending means from slowest time to fastest time

        Double temp = 0.0;
        String moveString = "";
        String lastTeamName = "";
        
        Double[] timesSorted = new Double[linecount - 1]; // arrays for bubble sort
        String[] moveTeamNames = new String[linecount - 1];

        for(int i = 0; i < linecount - 1; i++) // arrays filled from original values
        {
            timesSorted[i] = team[i].avgTime;
            moveTeamNames[i] = team[i].teamName;

        }

        for(int i = 0; i < linecount - 1; i++) // reverse order bubble sort also reassanges team names
        {
            for(int j = 0; j < linecount - i - 2; j++)
            {
                if(timesSorted[j] < timesSorted[j + 1])
                {
                    temp = timesSorted[j];
                    moveString = moveTeamNames[j];

                    timesSorted[j] = timesSorted[j + 1];
                    moveTeamNames[j] = moveTeamNames[j + 1];

                    timesSorted[j + 1] = temp;
                    moveTeamNames[j + 1] = moveString;
                    
                }
            }
        }


        // block prints sorted array
        lastTeamName = "";
        System.out.println("\n\nTeam average speed sorted in descending order (slowest-->fastest)");
        for(int i = 0; i < linecount - 1; i++)
        {
            if(!moveTeamNames[i].equals(lastTeamName))
            {
                System.out.println("TeamName --> " + moveTeamNames[i] + "  Average Time --> " + timesSorted[i]);
                lastTeamName = moveTeamNames[i]; // lastTeamName is used to compare to filter duplicates
            }
        }
   }




    /********************************* 
     * Name: SortDrivers
     * Date 08/05/2024
     * Inport: team (Team[]), linecount (int)
     * Export: N/A
     * Purpose: Displays sorted team driver fastest lap times in ascending and descending order
    */
   // This method displays sorted driver times (descendint then ascending)
   public static void SortDrivers(Team[] team, int linecount)
   {
        Double temp = 0.0;
        String moveString = "";

        Double[] timesSorted = new Double[linecount - 1]; // arrays for bubble sort
        String[] moveRacerNames = new String[linecount - 1];

        for(int i = 0; i < linecount - 1; i++) // arrays are filled
        {
            timesSorted[i] = team[i].fastestLapTime;
            moveRacerNames[i] = team[i].driverName;
        }


        for(int i = 0; i < linecount - 1; i++) // reverse order bubble sort also reassanges team names
        {
            for(int j = 0; j < linecount - i - 2; j++)
            {
                if(timesSorted[j] < timesSorted[j + 1])
                {
                    temp = timesSorted[j];
                    moveString = moveRacerNames[j];

                    timesSorted[j] = timesSorted[j + 1];
                    moveRacerNames[j] = moveRacerNames[j + 1];

                    timesSorted[j + 1] = temp;
                    moveRacerNames[j + 1] = moveString;
                    
                }
            }
        }


        // for loop sorts drivers in descending order
        System.out.println("\n\nDriver fastest time average speed sorted in descending order (slowest-->fastest)");
        for(int i = 0; i < linecount - 1; i++)
        {
            System.out.println("DriverName --> " + moveRacerNames[i] + "  FastestTime --> " + timesSorted[i]);
        }


        // for loop sorts drivers in ascending order
        System.out.println("\n\nDriver fastest time average speed sorted in ascending order (fastest-->slowest)");
        for(int i = linecount - 2; i >= 0; i--)
        {
            System.out.println("DriverName --> " + moveRacerNames[i] + "  FastestTime --> " + timesSorted[i]);
        }


        TeamAnalysis(team, linecount);
   }


    /********************************* 
     * Name: TeamAnalysis
     * Date 10/05/2024
     * Inport: team (Team[]), linecount (int)
     * Export: N/A
     * Purpose: Takes User input to filter by car codes (with exception handling and and checking against invalid codes) and displays driver and time for that team
    */
   // function handles user input and allows for user to filter through car codes and see who drives the car (fastest driver listed first)
   public static void TeamAnalysis(Team[] team, int linecount)
   {
        String usrInput = "";
        Boolean codeInvalid = true;
        String stopRepeat = "";

        Double time1 = 205.50;
        Double time2 = 205.50;
        String driver1 = "";
        String driver2 = "";
        int stage = 1;


        do
        {
            // Display car codes
            System.out.println("\n\nEnter CarCode to Filter Drivers");
            System.out.print("CarCodes list -->");
            for(int i = 0; i < linecount - 1; i++)
            {
               System.out.print(team[i].carCode + " ");
            }


            // initial user input
            System.out.println("\n\nEnter CarCode");
            usrInput = input.nextLine();

            // while loop reprompts for empty input
            while(usrInput.isEmpty())
            {
                System.out.println("Invalid value please enter valid value");
                usrInput = input.nextLine();
            }

            // statement checks if the entered value exists in the table and accepts data if it is
            if(!usrInput.isEmpty())
            {
                for(int i = 0; i < linecount - 1; i++)
                {
                    if(usrInput.equals(team[i].carCode))
                    {
                        codeInvalid = false;
                        System.out.println("\nCarCode " + team[i].carCode + " Accepted!\n");
                    }
                }
            }


        } while(usrInput.isEmpty() || codeInvalid == true);


        // for loop displays driver name and fastest lap time if there is only 1 carcode of this name
        for(int i = 0; i < linecount - 1; i++)
        {
            if(usrInput.equals(team[i].carCode) && team[i].checkRepeat == false)
            {
                System.out.println("\n\nThere is only 1 driver on this team driving this car " + team[i].driverName + " fastest time = " + team[i].fastestLapTime);
            }
            else if(usrInput.equals(team[i].carCode) && team[i].checkRepeat == true) // block adds drivers into 2 values if there are 2 drivers in a team/car
            {
                if(!stopRepeat.equals(team[i].driverName)) // adds 2 drivers in the same car code into the driver and time array (length 2)
                {
                    
                    if(stage == 1) // statement adds to filter
                    {
                        driver1 = team[i].driverName;
                        time1 = team[i].fastestLapTime;
                        stage++;
                    }
                    else if(stage == 2)
                    {
                        driver2 = team[i].driverName;
                        time2 = team[i].fastestLapTime;
                    }
                    stopRepeat = team[i].driverName;
                }
            }

        }


        // display drivers and times if here 2 cars
        if(time1 != 205.50)
        {
            // display filter
            if(time1 < time2)
            {
                System.out.println("DriverName " + driver1 + " --> FastestTime " + time1 + "\nDriverName " + driver2 + " --> FastestTime " + time2);
            }
            else
            {
                System.out.println("DriverName " + driver2 + " --> FastestTime " + time2+ "\nDriverName " + driver1 + " --> FastestTime " + time1);
            }
        }
   }


    /********************************* 
     * Name: Clear
     * Date 03/05/2024
     * Import: N/A
     * Export: N/A
     * Purpose: Clear Terminal using ASCII excape sequence
    */
    public static void Clear() // simple function uses ASCII escape sequence to clear terminal for improved readability
    {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }
}




/****************************
 * Author: Daniil Sergeev
 * Date: 03/05/2024
 * Purpose: Contains All the Object stuff
 */
class Team // Class creates 1 instance of an F1 team
{
        // Class Fields
        String teamName;
        String carCode;
        String driverName;
        String grandPrix;
        int positionFinished;
        Double fastestLapTime;

        // Other fields for data analysis
        Boolean checkRepeat;
        Double avgTime;

        // Constructor not needed CSV file sets

        //Setters
        public void SetTeamName(String pTeamName)
        {
            teamName = pTeamName;
        }

        public void SetCarCode(String pCarCode)
        {
            carCode = pCarCode;
        }

        public void SetDriverName(String pDriverName)
        {
            driverName = pDriverName;
        }

        public void SetGrandPrix(String pGrandPrix)
        {
            grandPrix = pGrandPrix;
        }

        public void SetPositionFinished(int pPositionFinished)
        {
            positionFinished = pPositionFinished;
        }

        public void SetFastestLapTime(Double pFastesLapTime)
        {
            fastestLapTime = pFastesLapTime;
        }

        public void SetCheckRepeat(Boolean pCheckRepeat)
        {
            checkRepeat = pCheckRepeat;
        }

        public void SetAVGTime (Double pAvgTime)
        {
            avgTime = pAvgTime;
        }
}
