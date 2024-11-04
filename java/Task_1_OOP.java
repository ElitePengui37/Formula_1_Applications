import java.io.*;
import java.util.*;

/******************************** 
 * Title: Task_1_OOP
 * Author: Daniil Sergeev
 * Date Created: 03/05/2024
 * Date Modified: 03/05/2024
 * Purpose: To take user input on F1 and output it as a CSV file using an Object Oriented Solution
*/



/********************************
 * Author: Daniil Sergeev
 * Date: 03/05/2024
 * Purpose: Main class where program mostly runs
 */
public class Task_1_OOP
{

    private static Scanner input = new Scanner(System.in); // scanner is a global variable so it doesnt have to be passed to other functions that want input

    /********************************* 
     * Name: main
     * Date 03/05/2024
     * Import: argv String[] (Command Line Arguements)
     * Export: N/A
     * Purpose: Main entry Point of program [Asks User for total number of racers with exeption handling, Calls other methods]
    */
    public static void main(String[] argvs)
    {
        int racerCount = 0; // Max 10 racers per grid

        System.out.println("Welcome to The F1 Task 1\n");

        while(racerCount < 2 || racerCount > 10) // if Racercount is in an invalid range the user keeps getting prompted until they enter a valid value
        {
            try
            {
                System.out.println("Enter Total number of racers");
                racerCount = input.nextInt();

                if(racerCount < 2 || racerCount > 10)
                {
                    Clear();
                    System.out.println("Input out of range: Choose between 2-10");
                }
            }
            catch(InputMismatchException e)
            {
                Clear();
                System.out.println("Only integer values are accepted");
                input.nextLine();
            }
        }

        EnterInput(racerCount);

        input.close(); // close input this must be last line
    }




    /********************************* 
     * Name: EnterInput
     * Date 03/05/2024
     * Import: racerCount (int)
     * Export: N/A
     * Purpose: Create Team Objects using polymorthism, Call other functions to assign team things and Display stuff
    */
    public static void EnterInput(int racerCount)
    {
        Team[] team = new Team[racerCount]; // creates an array of length of the racer count
        String GrandPrix = null; //Assuming the same Grand Prix for all Racers
        int position = 0;
        Double FastsestLapTime = -100.0;

        // Polymorthism for loop to create x number of Team objects
        for(int i = 0; i < racerCount; i++)
        {
            team[i] = new Team();
        }
        
        
        input.nextLine(); // flush input buffer to prevent \n from int input to stop Team0 name getting skipped
        

        //User Input For Loop
        for(int i = 0; i < racerCount; i++)
        {
            TeamNameInput(i, team);

            CarCodeInput(i, team);

            DriverNameInput(i, team);

            PositionInput(i, team, racerCount, position);

            FastestTimeInput(i, team, FastsestLapTime);
        }

        GrandPrixInput(team, GrandPrix, racerCount);

        DisplayInput(team, racerCount);
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

    /********************************* 
     * Name: TeamNameInput
     * Date 03/05/2024
     * Import: i (int), team (Team[])
     * Export: N/A
     * Purpose: Ask for user Input Regarding Team Names in all objects (with exception handling)
    */
    public static void TeamNameInput(int i, Team[] team) // function adds team names to objcts
    {
        Clear(); // Screen is cleared for readability
        do // do while loop is looped if string is empty
        {
            System.out.println("Enter Teamname");
            team[i].SetTeamName(input.nextLine());

            if(team[i].teamName.isEmpty())
            {
                Clear();
                System.out.println("Empty Strings are Illegal Please Try Again");
            }
        } while(team[i].teamName.isEmpty());
    }

    /********************************* 
     * Name: CarCodeInput
     * Date 03/05/2024
     * Import: i (int), team (Team[])
     * Export: N/A
     * Purpose: Ask for user Input Regarding Car Codes in all objects (with exception handling)
    */
    public static void CarCodeInput(int i, Team[] team) // function adds Car Codes into objects
    {
        Clear();
        do // Do while loop used for error handling
        {
            System.out.println("Enter a Car Code");
            team[i].SetCarCode(input.nextLine());

            if(team[i].carCode.isEmpty())
            {
                Clear();
                System.out.println("Empty Strings are Illegal Please Try Again");
            }
        } while(team[i].carCode.isEmpty());
    }


    /********************************* 
     * Name: DriverNameInput
     * Date 03/05/2024
     * Import: i (int), team (Team[])
     * Export: N/A
     * Purpose: Ask for user Input Regarding Driver Names in all objects (with exception handling)
    */
    public static void DriverNameInput(int i, Team[] team) // function adds Driver Names into objects
    {
        Clear();
        do // Do while loop for error handling
        {
            System.out.println("Enter Driver Name");
            team[i].SetDriverName(input.nextLine());

            if(team[i].driverName.isEmpty())
            {
                Clear();
                System.out.println("Empty Strings are Illegal Please Try Again");
            }
        } while(team[i].driverName.isEmpty());
    }


    /********************************* 
     * Name: PositionInput
     * Date 03/05/2024
     * Import: i (int), team (Team[]), racerCount (int), position(int)
     * Export: N/A
     * Purpose: Ask for user Input regarding finishing placement in all objects (with exception handling) and error checking against the same placement getting taken
    */
    public static void PositionInput(int i, Team[] team, int racerCount, int position) // function adds position values into objects
    {
        Clear();
        // User Input for position finished -1 for DNF
        do
        {
            try
            {
                System.out.println("Enter Driver Finish Place (-1 for DNF)");
                position = input.nextInt();
                
                if((position < 1 || position > racerCount) && position != -1) // out of bounds check
                {
                    Clear();
                    System.out.println("Invalid Position Entered Please Retry (1-Total People racing) or -1 for DNF");
                }


                for(int j = 0; j <= i; j++) // for loop checks if someone is already in this position and asks for reprompts
                {
                    while(team[j].positionFinished == position && team[j].positionFinished != -1)
                    {
                        Clear();
                        System.out.println("Someone Else Has Already finished in this position Please Try Again");
                        position = input.nextInt();
                    }
                }
            }
            catch(InputMismatchException e)
            {
                Clear();
                System.out.println("Invalid Input Please Only Enter Whole Integer Numbers In this Field");
                input.nextLine();
            }

            team[i].SetPositionFinished(position);

        } while((position < 1 || position > racerCount) && position != -1);
        input.nextLine(); //input buffer is cleared for next String inputs
    }


    /********************************* 
     * Name: FastestTimeInput
     * Date 03/05/2024
     * Import: i (int), team (Team[]), FastestLapTime (Double)
     * Export: N/A
     * Purpose: Ask for user Input Fastest Lap Time in all objects (with exception handling) and error checking against negative time values
    */
    public static void FastestTimeInput(int i, Team[] team, Double FastsestLapTime) // function adds fastest lap time values into objects
    {
        Clear();
        // User Input for fastest lap time
        do
        {
            try
            {
                System.out.println("Enter Fastest Lap Time"); //DNF not reuqired for this one
                FastsestLapTime = input.nextDouble();

                if(FastsestLapTime < 0.0) // only allow positive time
                {
                    Clear();
                    System.out.println("Time Out Of Range Please Try Again");
                }
            }
            catch(InputMismatchException e)
            {
                Clear();
                System.out.println("Invalid Time Entered Please Try Again");
                input.nextLine();
            }

            team[i].SetFastestLapTime(FastsestLapTime);
        } while(FastsestLapTime < 0.0);
        input.nextLine(); //input buffer is cleared for next string inputs
    }

    /********************************* 
     * Name: GrandPrixInput
     * Date 03/05/2024
     * Import: team (Team[]), GrandPrix (String), racerCount (int)
     * Export: N/A
     * Purpose: Ask for user Input regarding Grand Prix location (with exception handling) (same location for all racers)
    */
    public static void GrandPrixInput(Team[] team, String GrandPrix, int racerCount) // function adds Grand Prix location into objects
    {
        // Assuming Granx Prix is in the same place for all racers
        Clear();
        //User Input for Grand Prix
        do // Do while loop for error handling
        {
            System.out.println("Enter Grand Prix (Assuming the same Grand Prix for all Racers)");
    
            GrandPrix = input.nextLine();
    
            if(GrandPrix.isEmpty()) // prevents empty strings
            {
                Clear();
                System.out.println("Empty Strings are Illegal Please Try Again");
            }
    
            for(int i = 0; i < racerCount; i++) // Grand Prix filled for all teams
            {
                team[i].SetGrandPrix(GrandPrix);
            }
    
        } while(GrandPrix.isEmpty());
    }

    /********************************* 
     * Name: DisplayInput
     * Date 03/05/2024
     * Import: team (Team[]), racerCount (int)
     * Export: N/A
     * Purpose: Neatly display data in CSV ready format
    */
    public static void DisplayInput(Team[] team, int racerCount) // Function Displays Data of Objects
    {
        Clear();
        
        System.out.println("Team Name, Car Code, Driver Name, Grand Prix, Position Finished, Fastest Lap (Seconds)");

        for(int i = 0; i < racerCount; i++) // for loop displays all data
        {
            System.out.println(team[i].teamName + ", " + team[i].carCode + ", " + team[i].driverName + ", " + team[i].grandPrix + ", " + team[i].positionFinished + ", " + team[i].fastestLapTime);
        }
     

        FileIO(team, racerCount);
    }


    /********************************* 
     * Name: FileIO
     * Date 03/05/2024
     * Import: team (Team[]), racerCount (int)
     * Export: N/A
     * Purpose: Ask user weather they want to export data into CSV file (with exception handling)
    */
    public static void FileIO(Team[] team, int racerCount) // This Function puts the data into a CSV file
    {
        String Choice = "";

        // while choice is empty or invalid
        while(Choice.isEmpty() || (!Choice.equals("Y") && !Choice.equals("y") && !Choice.equals("Yes") && !Choice.equals("yes") && !Choice.equals("N") && !Choice.equals("n") && !Choice.equals("No") && !Choice.equals("no")))
        {
            System.out.println("\n\n Would You Like to Save this Data to a CSV File (Y, N)");
            Choice = input.nextLine();

            if(Choice.equals("Y") || Choice.equals("y") || Choice.equals("Yes") || Choice.equals("yes")) // if yes
            {
                WriteFile(team, racerCount);
            }
            else if (Choice.equals("N") || Choice.equals("n") || Choice.equals("No") || Choice.equals("no")) // if no
            {
                System.out.println("Goodbye");
            }
            else // error value
            {
                Clear();
                System.out.println("Invalid Value Please Retry");
            }
        }

    }


    /********************************* 
     * Name: WriteFile
     * Date 03/05/2024
     * Inport: team (Team[]), racerCount int
     * Export: N/A
     * Purpose: Asks user for file name, adds CSV Extension Writes Data to file
    */
    public static void WriteFile(Team[] team, int racerCount) // FileIO part 2
    {
        String Filename = "";
        String Extension = ".csv";
        String File;
        
        FileOutputStream fileStrm = null; // declares filestream 
        PrintWriter pw; // file stuff

        do // user enters their file name here
        {
            System.out.println("Enter File Name to Create");
            Filename = input.nextLine();

            if(Filename.isEmpty()) // prevents empty file name
            {
                System.out.println("Cannot have blank file name");
            }

        } while(Filename.isEmpty());

        File = Filename + Extension; // adds CSV extension to file

        
        // exception handling for writing to file
        try
        {
            fileStrm = new FileOutputStream(File);
            pw = new PrintWriter(fileStrm);

            pw.println("Team Name" + "," + "Car Code" + "," + "Driver Name" + "," + "Grand Prix" + "," + "Position Finished" + "," + "Fastest Lap (Seconds)"); // CSV Header

            for(int i = 0; i < racerCount; i++) // data add loop
            {
                pw.println(team[i].teamName + "," + team[i].carCode + "," + team[i].driverName + "," + team[i].grandPrix + "," + team[i].positionFinished + "," + team[i].fastestLapTime);
            }

            pw.close();
        }
        catch (IOException e)
        {
            System.out.println("Error in Writing to file: " + e.getMessage());
        }
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
        double fastestLapTime;

        // Constructor not needed as user manually sets paramaters

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
}