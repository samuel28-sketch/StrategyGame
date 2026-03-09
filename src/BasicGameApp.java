//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv DON'T CHANGE! vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// Graphics Libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
// gridding/GUI variables
    int hexaRows;

    GUI troopDepl;
    Land land;


//--class arrays--
    ArrayList<Unit> readyUnits = new ArrayList<>();
    ArrayList<Unit> unreadyUnits = new ArrayList<>();
    ArrayList<GUI> unitBars = new ArrayList<>();
    ArrayList<Land> grid = new ArrayList<>();
    ArrayList<Land> neighbors = new ArrayList<>();
    ArrayList<Cities> cities = new ArrayList<>();

    Random random = new Random(); //creates random
    Robot pixleCheck = new Robot(); //adds a robot






    // Initialize your variables and construct your program objects here.
    public BasicGameApp() throws AWTException { // BasicGameApp constructor
        setUpGraphics();
        // -- adding input listeners--
        keyInput = new KeyInput(this);
        canvas.addMouseListener(this);
        canvas.addKeyListener(keyInput);
        canvas.addMouseMotionListener(this);




        //variable and objects
        //create (construct) the objects needed for the game
//-- creating the map grid --
        troopDepl=new GUI();
        for (double columns = 0; columns <140; columns++) {//double columns =50;columns<2100;columns+= 15
            hexaRows++;
            double hexaYPos = columns*15;
            for (double rows = 0; rows < 200; rows++) {//double rows = 50; rows < 3000; rows += 15
                if (hexaRows%2==1) {
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
        if (!cityCreated) {
            for (int i = 0; i < 50; i++) {
                int randomX = random.nextInt(3000) + 380; //random.nextInt(2000) + 380;
                int randomY = random.nextInt(500) + 1200; //random.nextInt(500) + 1200
                Color color = pixleCheck.getPixelColor(randomX, randomY);
                if (!(color.getRed() > 200 && color.getRed() < 255 && color.getGreen() > 200 && color.getGreen() < 255 && color.getBlue() > 200 && color.getBlue() < 255)) { // spawns if pixle color isnt grey (238,238,238)
                    double teamChoice = Math.random();
                    if (teamChoice<.5) {
                        Cities city = new Cities(randomX, randomY, 's','p');
                        cities.add(city);
                    }
                    else {
                        Cities city = new Cities(randomX, randomY, 's','e');
                        cities.add(city);
                        System.out.println("city spawn blocked");
                    }
                } else {
                    i--;
                }
            }
            for (int i = 0; i < 30; i++) {
                int ranomX = random.nextInt(2000) + 380;
                int randomY = random.nextInt(500) + 1200;
                Color color = pixleCheck.getPixelColor(ranomX, randomY);
                if (!(color.getRed() > 200 && color.getRed() < 255 && color.getGreen() > 200 && color.getGreen() < 255 && color.getBlue() > 200 && color.getBlue() < 255)) { // spawns if pixle color isnt grey (238,238,238)
                    double teamChoice = Math.random();
                    if (teamChoice<.5) {
                        Cities city = new Cities(ranomX, randomY, 'm','p');
                        cities.add(city);
                    }
                    else {
                        Cities city = new Cities(ranomX, randomY, 'm','e');
                        cities.add(city);
                    }
                } else {
                    i--;
                }
            }
            for (int i = 0; i < 10; i++) {
                int ranomX = random.nextInt(2000) + 380;
                int randomY = random.nextInt(500) + 1200;
                Color color = pixleCheck.getPixelColor(ranomX, randomY);
                if (!(color.getRed() > 200 && color.getRed() < 255 && color.getGreen() > 200 && color.getGreen() < 255 && color.getBlue() > 200 && color.getBlue() < 255)) { // spawns if pixle color isnt grey (238,238,238)
                    double teamChoice = Math.random();
                    if (teamChoice<.5) {
                        Cities city = new Cities(ranomX, randomY, 'l','p');
                        cities.add(city);
                    }
                    else {
                        Cities city = new Cities(ranomX, randomY, 'l','e');
                        cities.add(city);
                    }
                } else {
                    i--;
                }
            }
            for (int i = 0; i < 2; i++) {
                int ranomX = random.nextInt(2000) + 380;
                int randomY = random.nextInt(500) + 1200;
                Color color = pixleCheck.getPixelColor(ranomX, randomY);
                if (!(color.getRed() > 200 && color.getRed() < 255 && color.getGreen() > 200 && color.getGreen() < 255 && color.getBlue() > 200 && color.getBlue() < 255)) { // spawns if pixle color isnt grey (238,238,238)
                    if (i == 0) {
                        Cities city = new Cities(ranomX, randomY, 'c','p');
                        cities.add(city);
                    }
                    else if (i == 1){
                        Cities city = new Cities(ranomX, randomY, 'c','e');
                        cities.add(city);
                    }
                } else {
                    i--;
                }
            }
            cityCreated = true;
        }

        //updates the map grid to world coordinates
        for (Land land:grid) {
            land.update(cameraXPos, cameraYPos);
            for (Cities city:cities) {
                if (land.tile.contains(city.xpos-cameraXPos,city.ypos-cameraYPos)&&city.team=='e') {
                    g.setColor(Color.red);
                    g.fillPolygon(land.xPoints, land.yPoints, 6);
                    g.setColor(Color.black);
                    land.team = 'e';
                }
                else if (land.tile.contains(city.xpos-cameraXPos,city.ypos-cameraYPos)&&city.team=='p'){
                    g.setColor(Color.blue);
                    g.fillPolygon(land.xPoints, land.yPoints, 6);
                    g.setColor(Color.black);
                    land.team = 'p';
                }
                /*if (land.team == 'n'){ //if i want to do the teamcount system, i need a boolean is false untill a nearby tile updates its team, and then this conditional will run and mark it fasle again
                    Land neighbor = grid.get(land.rowIndex-1);
                    if (neighbor.tile.intersects(neighbor.tile.getBounds2D())){
                        if (neighbor.team == 'p') {
                            land.teamCount++;
                        }
                    }
                }*/
                /*if (land.team == 'n') { //ADD THIS LATER, SPREADING TEAM COLOR TO ADJECTENT HEXES, CURRENTLY CRASHES THE GAME CUZ IT CHECKS TOO MANY TILES, NEED TO BASE IT OFF OF INDEX NUMB INSTEAD, CHECK THE HEXES W ADJ NUMBS
                    for (Land neighbor : neighbors) { //NOTE: MAKE A SYSTEM WHERE FOR EVERY p HEX NEIGHBORING IT ADDS 1 TO AN INT, THEN FOR EVERY e HEX IT REMOVES 1, SO THAT IF THE INT IS ABOVE, BELOW, OR EQUAL TO 0 THE HEX WILL EITHER BE BLUE RED OR NORMAL RESPECTIVELY
                          //  if (land.tile.intersects(neighbor.tile.getBounds2D())){
                          //      land.team = neighbor.team;
                          //  }
                    }
                }*/
            }
        }
        for (Cities city:cities){
            g.fillOval(city.xpos-cameraXPos,city.ypos-cameraYPos,city.width,city.height);
        }

        for(Unit unit:readyUnits){
            g.drawImage(unit.idlePic, unit.xpos-cameraXPos-20, unit.ypos-cameraYPos-50, unit.width, unit.height, null); //renders troops on the map
            //System.out.println(unit.xpos-cameraXPos);
            //System.out.println(unit.ypos-cameraYPos);
            //g.drawRect(unit.xpos-cameraXPos-10,unit.ypos-cameraYPos-27,unit.width-30,unit.height-45); hitbox visualized

        }

        if (troopDepl.unitDeplMenu){
            g.drawImage(troopDepl.deplGUI,10,10,400,100,null); //renders the troop menu

            //renders the progress bar of the timer when units are made
            for (int i = 0;i < unitBars.size()&& i<=6; i++) {
                Unit unit = unreadyUnits.get(i);
                g.drawImage(unitBars.get(i).troopBar, 10, 110 + 50 * i, 400, 50, null);
                    g.setColor(Color.yellow);
                    g.fillRect(270, 130 + 50 * i, (int) (50 * (double) (500 - unit.spawnTime) / 500), 10);
                    g.setColor(Color.black);

            }
            //int i = unitBars.size()-1;i >= 0 && i<=6; i--

        }

        //g.fillRect(270, 55,75,15);

        //g.fillRect(115,55,150,15);



        //draw the images
        // Signature: drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)

//renders the indicator for if a troop is selected
        for(Unit unit:readyUnits){
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

            updateUnits();
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
            newUnit("PLAYER"); //hotkey shortcut for spawning units for the player
        }



    }

    public void keyReleased(KeyEvent e) {
        mapVertMoving = false; //makes the map stop moving when the key is released
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

    //creates new units and adds them to unit array
public void newUnit(String team){
        Unit newUnit = new Unit(placeX, placeY,"PLAYER");
        unreadyUnits.add(newUnit);
        GUI newUnitBar = new GUI();
        unitBars.add(newUnitBar);
}

//--unit updates--
public void updateUnits(){
        //if (!unreadyUnits.isEmpty()){
    //updates the spawn time for units that aren't on the map yet
        for (int i = unreadyUnits.size()-1;i >= 0; i--) {
            Unit unit = unreadyUnits.get(i);
            unit.spawnUpdate();
            System.out.println(unit.spawnTime);
    //spawns units if the spawn time is over, and moves them to the "exists on map" array
            if (unit.isReadyToSpawn()) {
                unreadyUnits.remove(i);
                unitBars.remove(i);
                readyUnits.add(unit);
                //System.out.println("Spawning at: " + mouseX + ", " + mouseY);
            }
        }
        //updates units hitbox position into world coordinates
            for(Unit readyUnit:readyUnits){
                readyUnit.update(cameraXPos,cameraYPos);
            }


        /*for (Unit unit:readyUnits){
            unit.updateHitbox(cameraXPos,cameraYPos);
        }*/
        }

    @Override
    //--mouse controls--
    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        //troop spawn button
        System.out.println("x:" + mouseX + "Y:" + mouseY);
        if (troopDepl.spawnButton.contains(mouseX,mouseY)&& troopDepl.unitDeplMenu){
            newUnit("PLAYER");
        }
        //selecting a location for units to spawn after the spawn location button is pressed
        if (placeSelect){
            placeX=mouseX+cameraXPos;
            placeY=mouseY+cameraYPos;
            placeSelect = false;
        }
        //spawn location button
        if (troopDepl.placeButton.contains(mouseX,mouseY)&& troopDepl.unitDeplMenu){
            placeSelect = true;
        }

        //System.out.println(placeSelect);
        /*for (Unit unit:readyUnits) {
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
        for(Unit unit:readyUnits){
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
            for (Unit unit:readyUnits){
                if (selectBox.intersects(unit.hitbox)&&unit.team=="PLAYER"){
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