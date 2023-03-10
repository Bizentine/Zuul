/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Item currentItem;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    private void createItem()
    {
        Item theOneRing;
        
        theOneRing = new Item("The ring has awoken. It's master calls. \n The ring would like to sleep for like 15 more minutes please.", 1);
    }
    
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room HobShire, deadend, pompeii, rainbowroad, mtTabor, northernwastes, mtDoom, mtPleasantafternoon, mtVesuvius;
        // create the rooms
        HobShire = new Room("in HobShire, \n a scenic and legally distinct shire", "");
        deadend = new Room("at a dead end, \n Theres nothing here but a red fish of some kind", "The One Ring");
        pompeii = new Room("in a pleasant city called \"pompeii\",\n but it gives you a feeling of impending doom", "");
        rainbowroad = new Room("on the Rainbow Road!\n You fall off a lot and its very stressful, and yet its your favorite for some reason?", "");
        mtTabor = new Room("on Mt Tabor.\n The caldera has an archery range", "");
        northernwastes = new Room("in the Northern Wastes.\n This place gives you the distict feeling that you know nothing", "");
        mtDoom = new Room("on Mt Doom.\n You get the feeling you are being watched", "");
        mtPleasantafternoon = new Room("on Mt Pleasant Afternoon.\n Must be like greenland/iceland because its midday and hailing", "");
        mtVesuvius = new Room("on Mt Vesuvius.\n It sounds angry", "");
        
        // initialise room exits
        HobShire.setExit("north", northernwastes);
        HobShire.setExit("east", deadend);
        HobShire.setExit("south", rainbowroad);
        HobShire.setExit("west", pompeii);
        deadend.setExit("west", HobShire);
        pompeii.setExit("east", HobShire); 
        pompeii.setExit("south", mtVesuvius);
        rainbowroad.setExit("north", HobShire); 
        rainbowroad.setExit("east", mtTabor);
        rainbowroad.setExit("west", mtVesuvius);
        mtTabor.setExit("west", rainbowroad);
        northernwastes.setExit("east", mtPleasantafternoon); 
        northernwastes.setExit("west", mtDoom);
        mtDoom.setExit("east", northernwastes);
        mtPleasantafternoon.setExit("west", northernwastes);
        mtVesuvius.setExit("north", pompeii);
        mtVesuvius.setExit("east", rainbowroad);

        currentRoom = HobShire;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to what is definitely not a LotR ripoff!");
        System.out.println("World of NaLotRR is a brand new concept entirely.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Not all those who wander are lost...");
        System.out.println("But you sure seem to be.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.showCommands());
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExits(direction);
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }    
        }
        
    private void printLocationInfo()
    {
        System.out.println(currentRoom.getLongDescription());
        System.out.print(currentRoom.getExitString());
        System.out.print(currentItem.getItemDescription());
    }
    

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
