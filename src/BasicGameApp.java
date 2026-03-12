//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv DON'T CHANGE! vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
/*
Author: Samuel Nunes
Class: CS 1
Date: 3/11/2026
Project: Game #2
Project Title: Civil War in Toylandia
*/

// Graphics Libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
public class BasicGameApp implements Runnable, MouseListener, MouseMotionListener {

























    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;


    //Variable Definition Section
    //You can set their initial values too
    // Like Mario mario = new Mario(); //
// --map variables--
    Image map;
    KeyInput keyInput;
    double cameraDX;
    double cameraDY;
    int cameraXPos;
    int cameraYPos;
    int mapXPos;
    int mapYpos;
    boolean mapVertMoving;
    boolean mapHorzMoving;
    boolean cityCreated;
    //boolean hexPurged;
// --mouse variables--
    int mouseX;
    int mouseY;
    int mousePrsX;
    int mousePrsY;
    int mouseRelX;
    int mouseRelY;
    int constMouseX;
    int constMouseY;
    int dragMouseX;
    int dragMouseY;
    boolean mousePrs;


// --troop related variables--
    Rectangle selectBox;
    boolean placeSelect;
    int placeX;
    int placeY;
    int enemyMaxTroop;
    int playerMaxTroop;
    int aiSpawnTime;
    boolean showHitboxes;
// gridding/GUI variables
    int hexaRows;

    GUI troopDepl; //creates the troop deployment menu


//--class arrays--
    ArrayList<Unit> playerUnits = new ArrayList<>();
    ArrayList<Unit> unreadyUnits = new ArrayList<>();
    ArrayList<Unit> allUnits = new ArrayList<>();
    ArrayList<Unit> enemyUnits = new ArrayList<>();
    ArrayList<GUI> unitSpawnBars = new ArrayList<>();
    ArrayList<Land> grid = new ArrayList<>();
    ArrayList<Land> neighbors = new ArrayList<>();
    ArrayList<Cities> cities = new ArrayList<>();
    ArrayList<Cities> playerCities = new ArrayList<>();
    ArrayList<Cities> enemyCities = new ArrayList<>();

    Random random = new Random(); //creates random for random integers
    Robot pixleCheck = new Robot(); //adds a robot for getting pixel colors






    // Initialize your variables and construct your program objects here.
    public BasicGameApp() throws AWTException { // BasicGameApp constructor
        setUpGraphics();
        // -- adding input listeners--
        keyInput = new KeyInput(this); //creates keyboard input class
        canvas.addMouseListener(this);     //mouse input listener
        canvas.addKeyListener(keyInput);      //creates keyboard input listener
        canvas.addMouseMotionListener(this); //creates mouse drag/motion listener




        //variable and objects
        //create (construct) the objects needed for the game
//-- creating the map grid --
        troopDepl=new GUI();
        for (double columns = 0; columns <140; columns++) { //loops an entire row for each column
            hexaRows++;
            double hexaYPos = columns*15;
            for (double rows = 0; rows < 200; rows++) { //creates hexagons for each row
                if (hexaRows%2==1) { //staggers hexagons so that they stack neatly
                    double hexaXPos = 15*rows;
                    Land land = new Land(10, hexaXPos -7.5, hexaYPos,rows,columns);
                    grid.add(land);
                    neighbors.add(land);
                }
                else {
                    double hexaXPos = 15*rows;
                    Land land = new Land(10, hexaXPos, hexaYPos,rows,columns);
                    grid.add(land);
                    neighbors.add(land);
                }
            }
        }

        //land = new Land(50,500,500);
        //land.xpos=500;
        //land.ypos=500;

        // map image
        map = Toolkit.getDefaultToolkit().getImage("map2.png");








    }
    // end BasicGameApp constructor

    public void moveThings() {
        //call the move() code for each object  -
        moveMap(); //camera movement

    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);


        g.drawImage(map, mapXPos-cameraXPos, mapYpos-cameraYPos,3000,2100,null); // renders the map w camera movement

        //--city creation--

        //right now makes cities randomly
        if (!cityCreated) { //makes it so cities are only created once
            for (int i = 0; i < 50; i++) { //creates multiple small cities
                int randomX = random.nextInt(3000) + 380;
                int randomY = random.nextInt(500) + 1200;
                Color color = pixleCheck.getPixelColor(randomX, randomY);
                if (!((color.getRed() == 127 || color.getRed() == 238) && (color.getGreen() ==127 || color.getGreen() ==238) && (color.getBlue() ==127 || color.getBlue() == 238))) { // (should) prevent city from spawning on grey sections, may benefit from fine tuning
                    double teamChoice = Math.random();
                    if (teamChoice<.5) { //random 50/50 chance to be assigned to player or enemy team
                        Cities city = new Cities(randomX, randomY, 's','p');
                        cities.add(city);
                        playerCities.add(city);
                    }
                    else {
                        Cities city = new Cities(randomX, randomY, 's','e');
                        cities.add(city);
                        enemyCities.add(city);
                        //System.out.println("city spawn blocked");
                    }
                } else { //repeats that instance in the loop if the city would've spawned on an invalid spot
                    i--;
                }
            }
            for (int i = 0; i < 30; i++) { //creates multiple medium cities
                int ranomX = random.nextInt(2000) + 380;
                int randomY = random.nextInt(500) + 1200;
                Color color = pixleCheck.getPixelColor(ranomX, randomY);
                if (!((color.getRed() == 127 || color.getRed() == 238) && (color.getGreen() ==127 || color.getGreen() ==238) && (color.getBlue() ==127 || color.getBlue() == 238))) { // spawns if pixle color isnt grey (127,127,127), or off-map white (238,238,238)
                    double teamChoice = Math.random();
                    if (teamChoice<.5) { //assigns to player or enemy team randomly
                        Cities city = new Cities(ranomX, randomY, 'm','p');
                        cities.add(city);
                        playerCities.add(city);
                    }
                    else {
                        Cities city = new Cities(ranomX, randomY, 'm','e');
                        cities.add(city);
                        enemyCities.add(city);
                    }
                } else { //loops back if spawn was invalid
                    i--;
                }
            }
            for (int i = 0; i < 10; i++) { //creates multiple large cities
                int ranomX = random.nextInt(2000) + 380;
                int randomY = random.nextInt(500) + 1200;
                Color color = pixleCheck.getPixelColor(ranomX, randomY);
                if (!((color.getRed() == 127 || color.getRed() == 238) && (color.getGreen() ==127 || color.getGreen() ==238) && (color.getBlue() ==127 || color.getBlue() == 238))) { // spawns if pixle color isnt grey (127,127,127), or off-map white (238,238,238)
                    double teamChoice = Math.random();
                    if (teamChoice<.5) { //randomly assigns team
                        Cities city = new Cities(ranomX, randomY, 'l','p');
                        cities.add(city);
                        playerCities.add(city);
                    }
                    else {
                        Cities city = new Cities(ranomX, randomY, 'l','e');
                        cities.add(city);
                        enemyCities.add(city);
                    }
                } else { //loops back if spawn wasnt valid
                    i--;
                }
            }
            for (int i = 0; i < 2; i++) { //creates only 2 capital cities
                int ranomX = random.nextInt(2000) + 380;
                int randomY = random.nextInt(500) + 1200;
                Color color = pixleCheck.getPixelColor(ranomX, randomY);
                if (!((color.getRed() == 127 || color.getRed() == 238) && (color.getGreen() ==127 || color.getGreen() ==238) && (color.getBlue() ==127 || color.getBlue() == 238))) { // spawns if pixle color isnt grey (127,127,127), or off-map white (238,238,238)
                    if (i == 0) { //assigns one to player, one to enemy
                        Cities city = new Cities(ranomX, randomY, 'c','p');
                        cities.add(city);
                        playerCities.add(city);
                    }
                    else if (i == 1){
                        Cities city = new Cities(ranomX, randomY, 'c','e');
                        cities.add(city);
                        enemyCities.add(city);
                    }
                } else { //loops back if spawn wasnt valid
                    i--;
                }
            }
            cityCreated = true; //makes it so cities are only created once
        }

        //updates the map grid to world coordinates
        for (Land land:grid) { //gets every hex-tile on the world grid
            land.update(cameraXPos, cameraYPos);
            // updates hex-tiles to reflect the color of the team if they contain a city
            for (Cities city:cities) { //gets every city

                land.team='n';
                if (land.tile.contains(city.xpos - cameraXPos, city.ypos - cameraYPos) && city.team == 'e') {
                    g.setColor(Color.red);
                    g.fillPolygon(land.xPoints, land.yPoints, 6);
                    g.setColor(Color.black);
                    land.team = 'e';
                } else if (land.tile.contains(city.xpos - cameraXPos, city.ypos - cameraYPos) && city.team == 'p') {
                    g.setColor(Color.blue);
                    g.fillPolygon(land.xPoints, land.yPoints, 6);
                    g.setColor(Color.black);
                    land.team = 'p';
                }

            }
        }

        // --theoretically would spread team colors to neutral, colorless tiles, but currently barely works, and is very unoptimized that it usually crashes the game and prevents any rendering at all ---

        /*for (Land land:grid) {
            if (land.team == 'n') { //if i want to do the teamcount system, i need a boolean is false untill a nearby tile updates its team, and then this conditional will run and mark it fasle again
                land.teamCount = 0;
                if (land.rowIndex - 1 >= 0) { //CURRENTLY NEED TO OPTIMIZE OR SMTH CUZ ALL THIS RLY LAGS THE GAME
                    Land neighbor = grid.get(land.columnIndex * 200 + land.rowIndex - 1); //prior line ran off of if (land.tile.intersects(neighbor.tile.getBounds2D())){
                    if (neighbor.team == 'p') {
                        land.teamCount++;
                    }
                    if (neighbor.team == 'e') {
                        land.teamCount--;
                    }
                }
                if (land.rowIndex + 1 < 200) {
                    Land neighbor = grid.get(land.columnIndex * 200 + land.rowIndex + 1);
                    if (neighbor.team == 'p') {
                        land.teamCount++;
                    }
                    if (neighbor.team == 'e') {
                        land.teamCount--;
                    }
                }
                if (land.columnIndex - 1 >= 0) {
                    Land neighbor = grid.get(land.rowIndex + (land.columnIndex - 1) * 200);
                    if (neighbor.team == 'p') {
                        land.teamCount++;
                    }
                    if (neighbor.team == 'e') {
                        land.teamCount--;
                    }
                }

                if (land.columnIndex + 1 < 140) {
                    Land neighbor = grid.get(land.rowIndex + (land.columnIndex + 1) * 200);
                    if (neighbor.team == 'p') {
                        land.teamCount++;
                    }
                    if (neighbor.team == 'e') {
                        land.teamCount--;
                    }
                }


                if (land.teamCount > 0) {
                    land.team = 'p';
                } else if (land.teamCount < 0) {
                    land.team = 'e';
                } else {
                    land.team = 'n';
                }

            }


            if (!hexPurged) {
                Color color = pixleCheck.getPixelColor((int) land.xpos+7, (int) land.ypos+7);
                if (color.getRed() > 50 && color.getRed() < 255 && color.getGreen() > 50 && color.getGreen() < 255 && color.getBlue() > 50 && color.getBlue() < 255) {
                    land.onLand = false;
                }
            }

            if (land.onLand) {
                if (land.team == 'p') {
                    g.setColor(Color.blue);
                    g.fillPolygon(land.xPoints, land.yPoints, 6);
                    g.setColor(Color.black);
                }
                if (land.team == 'e') {
                    g.setColor(Color.red);
                    g.fillPolygon(land.xPoints, land.yPoints, 6);
                    g.setColor(Color.black);
                }
            }
        }
        */
                /*if (land.team == 'n') { //ADD THIS LATER, SPREADING TEAM COLOR TO ADJECTENT HEXES, CURRENTLY CRASHES THE GAME CUZ IT CHECKS TOO MANY TILES, NEED TO BASE IT OFF OF INDEX NUMB INSTEAD, CHECK THE HEXES W ADJ NUMBS
                    for (Land neighbor : neighbors) { //NOTE: MAKE A SYSTEM WHERE FOR EVERY p HEX NEIGHBORING IT ADDS 1 TO AN INT, THEN FOR EVERY e HEX IT REMOVES 1, SO THAT IF THE INT IS ABOVE, BELOW, OR EQUAL TO 0 THE HEX WILL EITHER BE BLUE RED OR NORMAL RESPECTIVELY
                          //  if (land.tile.intersects(neighbor.tile.getBounds2D())){
                          //      land.team = neighbor.team;
                          //  }
                    }
                }*/
        //hexPurged = true;

        //renders cities as circles on the world cords
        for (Cities city:cities){ //gets every city
            g.fillOval(city.xpos-cameraXPos,city.ypos-cameraYPos,city.width,city.height);
        }

        //renders all units on the map
        for(Unit unit: allUnits){ //gets every unit on the map
            for (Cities city: cities) { //gets every city on the map
                g.drawImage(unit.idlePic, (int) unit.xpos - cameraXPos - 20, (int) unit.ypos - cameraYPos - 50, unit.width, unit.height, null); //renders troops on the map
                if (showHitboxes) { //shows the hitbox of every unit if Q is pressed
                    if (unit.type == 'a') {
                        g.drawOval((int) (unit.xpos - cameraXPos - (150 + (10 * unit.kills))), (int) (unit.ypos - cameraYPos - (150 + (10 * unit.kills))), (int) (300 + (20 * unit.kills)), (int) (300 + (20 * unit.kills)));
                    }
                    g.draw(city.hitbox);
                    g.draw(unit.hitbox);
                }
            }

            //System.out.println(unit.xpos-cameraXPos);
            //System.out.println(unit.ypos-cameraYPos);
            //g.drawRect(unit.xpos-cameraXPos-10,unit.ypos-cameraYPos-27,unit.width-30,unit.height-45); hitbox visualized

        }
        if (troopDepl.unitDeplMenu) { //renders if r is pressed
            g.drawImage(troopDepl.deplGUI, 10, 10, 400, 100, null); //renders the troop menu

            //renders the progress bar of the timer for when units are made
            for (int i = 0; i < unitSpawnBars.size() && i <= 6; i++) { //limits the amount of spawn bars that are rendered to 6
                Unit unit = unreadyUnits.get(i);
                g.drawImage(unitSpawnBars.get(i).troopBar, 10, 110 + 50 * i, 400, 50, null);
                g.setColor(Color.yellow);
                g.fillRect(270, 130 + 50 * i, (int) (50 * (double) (unit.maxSpawnTime - unit.spawnTime) / unit.maxSpawnTime), 10);
                g.setColor(Color.black);
            }
            //renders a health bar for units that have taken damage
            for (Unit unit : allUnits) { //gets every unit on map
                if (unit.health < unit.maxHealth) {
                    g.setColor(Color.green);
                    g.fillRect((int) unit.xpos - cameraXPos - 10, (int) unit.ypos - cameraYPos - 50, (int) (50 * (unit.health) / unit.maxHealth), 10);
                    g.drawRect((int) unit.xpos - cameraXPos - 10, (int) unit.ypos - cameraYPos - 50, 50, 10);
                    g.setColor(Color.black);
                }
            }
        }
        //renders a capture bar for cities that are being captured
            for (Cities city:cities){//gets every city
                if (city.captureTime<city.maxCaptureTime) {
                    g.setColor(Color.orange);
                    g.fillRect(city.xpos - cameraXPos - 10, city.ypos - cameraYPos - 50, (int) (50 * (city.maxCaptureTime - city.captureTime) / city.maxCaptureTime), 10);
                    g.drawRect(city.xpos - cameraXPos - 10, city.ypos - cameraYPos - 50, 50, 10);
                    g.setColor(Color.black);
                }
            }
            //int i = unitSpawnBars.size()-1;i >= 0 && i<=6; i--



        //g.fillRect(270, 55,75,15);

        //g.fillRect(115,55,150,15);



        //draw the images
        // Signature: drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)

//renders the indicator for if a troop is selected
        for(Unit unit: playerUnits){ //gets every unit in playerunits
            if (unit.isSelected){
                g.fillRect(600,600,50,50);
            }
        }
        //renders the selection box when the mouse is dragged
        if (mousePrs) {
            g.drawRect(Math.min(mousePrsX,dragMouseX), Math.min(mousePrsY,dragMouseY), Math.abs(dragMouseX - mousePrsX), Math.abs(dragMouseY - mousePrsY)); // screen position but it needs to be map pos
        }

        // Keep the code below at the end of render()
        //g.fillOval(500,500,15,15);
        g.dispose();
        bufferStrategy.show();
    }














    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv DON'T CHANGE! vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    // PSVM: This is the code that runs first and automatically
    public static void main(String[] args) throws AWTException {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }

    //
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever.
        while (true) {
            moveThings();  //move all the game objects
            render();  // paint the graphics
            pause(10); // sleep for 10 ms

            battles(); // deals with battle logic
            updateUnits(); //updates unit fundamentals e.g world coordinates
            updateCities(); //updates city fundamentals e.g world coordinates
            updateAI(); //deals with AI logic
        }
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time ) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private Image getImage(String filename){
        return Toolkit.getDefaultToolkit().getImage(filename);
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //if (key==KeyEvent.VK_R){
        //    newUnit();
        //}
        if (key==KeyEvent.VK_Q){ //enables/disables show hitboxes if Q is pressed
            if (!showHitboxes) {
                showHitboxes = true;
                return;
            }
            if (showHitboxes){
                showHitboxes = false;
            }
        }
        if (key==KeyEvent.VK_R&&!troopDepl.unitDeplMenu){ //activates the unit deployment menu - rendering the image and allowing for troop creation
            troopDepl.unitDeplMenu = true;
            return;
           // System.out.println();
        }
        if (key==KeyEvent.VK_R&&troopDepl.unitDeplMenu){
            troopDepl.unitDeplMenu = false; //deactivates the menu if "r" is pressed again while its active
        }
        if (key==KeyEvent.VK_T&& troopDepl.unitDeplMenu){
            placeSelect = true; //hotkey shortcut for selecting where the next units will spawn
        }
        if (key==KeyEvent.VK_G&& troopDepl.unitDeplMenu){
            newUnit('t'); //hotkey shortcut for spawning troop units for the player
        }
        if (key==KeyEvent.VK_H&& troopDepl.unitDeplMenu){
            newUnit('a'); //hotkey shortcut for spawning artillery units for the player
        }



    }

    public void keyReleased(KeyEvent e) {
        mapVertMoving = false; //applies friction to the map when movement stops
        mapHorzMoving = false;
    }

    //-- map movement --
    //adds to the map/camera velocity when the according key (WASD) is pressed
    public void moveMap() {
        if (keyInput.isKeyDown(KeyEvent.VK_W) && (Math.abs(cameraDY) < 30)) {
            cameraDY -= 1;
            mapVertMoving = true;
        }
        if (keyInput.isKeyDown(KeyEvent.VK_S) && (Math.abs(cameraDY) < 30)) {
            cameraDY += 1;
            mapVertMoving = true;

        }
        if (keyInput.isKeyDown(KeyEvent.VK_A) && (Math.abs(cameraDX) < 30)) {
            cameraDX -= 1;
            mapHorzMoving = true;

        }
        if (keyInput.isKeyDown(KeyEvent.VK_D) && (Math.abs(cameraDX) < 30)) {
            cameraDX += 1;
            mapHorzMoving = true;

        }
        cameraXPos += cameraDX; //applies the velocity to camera position
        cameraYPos += cameraDY;
        // -- friction --
        // applies friction separately according to whether the camera isnt moving vertically or horizontally
        if (!mapHorzMoving) {
            if (Math.abs(cameraDX) == 1) cameraDX = 0; //resets camera velocity if it gets stuck at 1
            if (cameraDX > 0) {
                cameraDX -= 2;
            }
            if (cameraDX < 0) {
                cameraDX += 2;
            }
        }
        if (!mapVertMoving) {
            if (Math.abs(cameraDY) == 1) cameraDY = 0;

            if (cameraDY > 0) {
                cameraDY -= 2;
            }
            if (cameraDY < 0) {
                cameraDY += 2;
            }
        }

    }

    //creates new player units and adds them to unit arrays
public void newUnit(char type){
        playerMaxTroop = (int)(playerCities.size()*1.25); //sets the maximum limit at ~1.25x the amount of controlled cities for the player
        if (playerUnits.size()<playerMaxTroop) { //prevents troops from being spawed if the player is above the maximum limit
            Unit newUnit = new Unit(placeX, placeY, 'p', type);
            unreadyUnits.add(newUnit);
            GUI newUnitBar = new GUI();
            unitSpawnBars.add(newUnitBar);
        }
}

//--unit updates--
public void updateUnits(){
        //if (!unreadyUnits.isEmpty()){
    //updates the spawn time for units that aren't on the map yet
        for (int i = unreadyUnits.size()-1;i >= 0; i--) { //goes thru all the training units and updates their spawn times
            Unit unit = unreadyUnits.get(i);
            unit.spawnUpdate();
            System.out.println(unit.spawnTime);
    //spawns units if the spawn time is over, and moves them to the "exists on map" array
            if (unit.isReadyToSpawn()) {
                unreadyUnits.remove(i);
                unitSpawnBars.remove(i);
                playerUnits.add(unit);
                allUnits.add(unit);
                //System.out.println("Spawning at: " + mouseX + ", " + mouseY);
            }
        }
        //updates units hitbox position into world coordinates
            for(Unit units: allUnits){ //gets every unit on the map
                units.update(cameraXPos,cameraYPos);
            }



        for (Unit unit: allUnits){ //gets all the units
            for (Cities city:cities) {//gets all the cities
//makes units capture cities if they are touching them while not moving
                if (!unit.readyToMove&&city.team!=unit.team&&unit.hitbox.intersects(city.hitbox)){
                    city.captureTime--;
                    if (city.captureTime<=0){ //captures the city/sets the city team to the troop team when capture timer is up
                        city.team=unit.team;
                        if (enemyCities.contains(city)){
                          enemyCities.remove(city);
                          playerCities.add(city);
                        }
                        else if (playerCities.contains(city))
                        {
                          playerCities.remove(city);
                          enemyCities.add(city);
                        }
                        city.captureTime=city.maxCaptureTime;
                    }
                }
                else if (!unit.readyToMove&&city.team==unit.team&&unit.hitbox.intersects(city.hitbox)){ //heals units that stop in friendly cities
                    unit.health+=.25;
                    if (unit.health>unit.maxHealth){
                        unit.health=unit.maxHealth;
                    }
                }
                }
            }

        }

        public void battles(){
            for (int i = playerUnits.size() - 1; i >= 0; i--) { //goes thru every player unit starting from the end of the list
                Unit playerUnit = playerUnits.get(i);
                for (int j = enemyUnits.size() - 1; j >= 0; j--) { ////goes thru every enemy unit starting from the end of the list
                    Unit enemyUnit = enemyUnits.get(j);

                    if (playerUnit.blastZone!=null&&playerUnit.blastZone.intersects(enemyUnit.hitbox)&&!playerUnit.readyToMove){//only for artillery, if an enemy unit is in artillery range it takes damage
                        enemyUnit.health-=playerUnit.blastStrength*Math.pow(1.5, playerUnit.kills); //gains an exponetial damage buff w more kills
                        if (enemyUnit.health<=0){
                            enemyUnits.remove(enemyUnit);
                            allUnits.remove(enemyUnit);
                            if (playerUnit.kills<5) { //if artillery kills a unit, it adds a counter which gives it a buff, stacking up to 5 kills
                                playerUnit.kills++;
                            }
                        }
                    }
////if units fight directly hitbox to hitbox, it stops them from moving and each unit begins to take damage from the other
                if (playerUnit.hitbox.intersects(enemyUnit.hitbox)){
                    playerUnit.readyToMove=false;
                    playerUnit.dx=0;
                    playerUnit.dy=0;
                    enemyUnit.readyToMove=false;
                    enemyUnit.dx=0;
                    enemyUnit.dy=0;
                    playerUnit.health-=enemyUnit.strength*Math.pow(1.1, enemyUnit.kills);
                    enemyUnit.health-=playerUnit.strength*Math.pow(1.1, playerUnit.kills); //troop units gain an exponential buff w more kills
                    //System.out.println(playerUnit.health);

                    if (playerUnit.health<=0){
                        playerUnits.remove(playerUnit);
                        allUnits.remove(playerUnit);
                        if (enemyUnit.kills<5) {
                            enemyUnit.kills++; //if a troop kills a unit, it adds a counter which gives it a buff, stacking up to 5 kills
                        }
                    }
                    if (enemyUnit.health<=0){
                        enemyUnits.remove(enemyUnit);
                        allUnits.remove(enemyUnit);
                        if (playerUnit.kills<5) {
                            playerUnit.kills++;
                        }
                    }
                }
            }
        }
        }
//spawns enemy units in random time increments, increasing exponentially with more units the AI has
//and path finds for Ai units, bringing them to the closest player city to capture
        public void updateAI(){
        aiSpawnTime--;
        enemyMaxTroop=(int)(enemyCities.size()*1.5); //maximum troop amount of ~1.5 per city for AI
        //System.out.println(enemyMaxTroop);
        if (aiSpawnTime<=0&&enemyUnits.size()<enemyMaxTroop){ //spawns troops in random time increments, as long as the Ai doesnt have mroe troops then its max
            //System.out.println("spawned enemy unit");
            Cities spawnCity = enemyCities.get(random.nextInt(enemyCities.size()));
            Unit newUnit = new Unit(spawnCity.xpos, spawnCity.ypos,'e','t',3.1); //Ai can only spawn troop units
            enemyUnits.add(newUnit);
            allUnits.add(newUnit);
            aiSpawnTime = random.nextInt(100+(int)Math.pow(1.15,enemyUnits.size())); //increases the random increments of time exponentially according to how many troops the Ai already has
        }

        //path finds for AI units
        for (Unit unit:enemyUnits) { // goes thru all the ai units

            Cities closestCity = null;
            double closestDistance = 9999999;
//finds the closest player city
            for (Cities city : playerCities) { // goes thru all the player cities
                double dx = city.xpos - unit.xpos;
                double dy = city.ypos - unit.ypos;
                double distance = Math.sqrt(dx*dx + dy*dy);

                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestCity = city;
                }
            }
            boolean healing = false;
            for (Cities city:enemyCities) { // goes thru all the enemy cities
                if(unit.health<80&&unit.hitbox.intersects(city.hitbox)){  // makes it so that if Ai unti is under 80 health it will stop to heal in an AI city
                    unit.readyToMove = false;
                    healing=true;
                    break;
                }
                //brings the Ai troop to the closest city
                if (closestCity != null && closestDistance > 5 && !unit.readyToMove &&!healing) {
                    unit.selctX = closestCity.xpos;
                    unit.selctY = closestCity.ypos;
                    unit.readyToMove = true;
                    //System.out.println("moving to closest city");
                }
            }
        }
        }

        //updates cities into world cords
        public void updateCities(){
        for (Cities city:cities) // goes thru al cities and updates from screen into world cords
        {city.update(cameraXPos,cameraYPos);}
        }

    @Override
    //--mouse controls--
    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        //troop spawn button
        System.out.println("x:" + mouseX + "Y:" + mouseY);
        System.out.println(pixleCheck.getPixelColor(mouseX,mouseY));
        if (troopDepl.spawnButton.contains(mouseX,mouseY)&& troopDepl.unitDeplMenu){
            newUnit('t');
        }
        //selecting a location for units to spawn after the spawn location button is pressed
        if (placeSelect){
            placeX=mouseX+cameraXPos;
            placeY=mouseY+cameraYPos;
            placeSelect = false;
        }

        //selects units to be moved
        for(Unit unit: playerUnits){ //gets every unit in playerunits
            if (!unit.isSelected&&unit.hitbox.contains(mouseX,mouseY))
                {unit.isSelected=true; break;}
        }
        //spawn location button
        if (troopDepl.placeButton.contains(mouseX,mouseY)&& troopDepl.unitDeplMenu){
            placeSelect = true;
        }

        //System.out.println(placeSelect);
        /*for (Unit unit:playerUnits) {
            if (unit.hitbox.contains(mouseX, mouseY)){
                System.out.println("Clicked on unit");
            }
        }*/
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePrsX = e.getX();
        mousePrsY = e.getY();
        dragMouseX=e.getX();
        dragMouseY=e.getY();
        //selects units/activates selection box when control is held
        if (keyInput.isKeyDown(KeyEvent.VK_CONTROL)) {
            mousePrs = true;
        }
        //moves units to the pressed location after they're selected
        for(Unit unit: playerUnits){ //gets every unit in playerunits
            if (unit.isSelected){
                unit.selctX = e.getX() + cameraXPos;
                unit.selctY = e.getY() + cameraYPos;
                unit.readyToMove=true;
                unit.isSelected = false;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseRelX = e.getX();
        mouseRelY = e.getY();
        //creates/finalizes selection box
        if (mousePrs){
            selectBox = new Rectangle(Math.min(mousePrsX,mouseRelX),Math.min(mousePrsY,mouseRelY),Math.abs(mouseRelX-mousePrsX),Math.abs(mouseRelY-mousePrsY));
            for (Unit unit: playerUnits){ ////gets every unit in playerunits
                if (selectBox.intersects(unit.hitbox)&&unit.team=='p'){
                    unit.isSelected = true;
                    //System.out.println("selected");
                }
            }
            mousePrs=false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }





    @Override
    public void mouseDragged(MouseEvent e) {
        dragMouseX = e.getX();
        dragMouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        constMouseX = e.getX();
        constMouseY = e.getY();
    }


//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
}