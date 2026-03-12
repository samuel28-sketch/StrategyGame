import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Unit {
    //variables
    Image idlePic;
    double xpos;
    double ypos;
    int height;
    int width;
    double dx;
    double dy;
    int spawnTime;
    int maxSpawnTime;
    boolean isSelected;
    boolean readyToMove;
    Rectangle hitbox;
    int camX;
    int camY;
    char team;
    int selctX;
    int selctY;
    char type;
    Ellipse2D.Double blastZone;
//comabt variables
    double health;
    double maxHealth;
    double strength;
    double blastStrength;
    int kills;

    //unit constuctor assigns position, team allegiance, size and spawn time are constant
    public Unit(int xpos, int ypos, char team, char type){
        this.type = type;
        this.xpos = xpos;
        this.ypos = ypos;
        this.team = team;
        //troop units have more health and do more dmg
        if (type=='t') {
            health = 100;
            maxHealth = 100;
            strength = 3;
        }
        else if (type=='a'){
            health = 20;
            maxHealth = 20;
            strength = 1;
        }
        //assigns images
        if (team == 'p') {
            idlePic = Toolkit.getDefaultToolkit().getImage("unit1.png");
        }
        else if (team == 'e'){
            idlePic = Toolkit.getDefaultToolkit().getImage("enemyunit.png");
        }
        if (type=='a'){
            idlePic=Toolkit.getDefaultToolkit().getImage("unit.artillery.png");
        }
        height = 100;
        width = 50;
        //assigns different spawn times for artillery and troops (spawn time only applies to player)
        if (type=='t') {
            spawnTime = 150;
            maxSpawnTime = 150;
        }
        else if (type == 'a')
        {
        blastStrength = .25;
        spawnTime = 500;
        maxSpawnTime = 500;
        }
        hitbox = new Rectangle(xpos-camX,ypos-camY,width,height);
    }
//constructor overload for player units
    public Unit(int xpos, int ypos, char team, char type, double strength){
        this.type = type;
        this.xpos = xpos;
        this.ypos = ypos;
        this.team = team;
        if (type=='t') {
            health = 100;
            maxHealth = 100;
            this.strength = strength; // player units are just a little bit stronger
        }
        else if (type=='a'){
            health = 20;
            maxHealth = 20;
            strength = 1;
        }
        if (team == 'p') {
            idlePic = Toolkit.getDefaultToolkit().getImage("unit1.png");
        }
        else if (team == 'e'){
            idlePic = Toolkit.getDefaultToolkit().getImage("enemyunit.png");
        }
        if (type=='a'){
            idlePic=Toolkit.getDefaultToolkit().getImage("unit.artillery.png");
        }
        height = 100;
        width = 50;
        if (type=='t') { //assigns different spawn times for artillery and troops (spawn time only applies to player)
            spawnTime = 150;
            maxSpawnTime = 150;
        }
        else if (type == 'a')
        {
            blastStrength = 1;
            spawnTime = 500;
            maxSpawnTime = 500;
        }
        hitbox = new Rectangle(xpos-camX,ypos-camY,width,height);
    }


//updates/runs spawn timer
    public void spawnUpdate(){
        spawnTime--;
    }
//updates units into world coordinates
    public void update(int camX, int camY){
        hitbox = new Rectangle((int)xpos-camX-10,(int)ypos-camY-27,width-30,height-45);
        if (type == 'a'){ //creates the range for artillery, range gets buffed linearly with kills
            blastZone = new Ellipse2D.Double((int)(xpos-camX-(150+(10*kills))),(int)(ypos-camY-(150+(10*kills))),(int)(300+(20*kills)),(int)(300+(20*kills)));
        }
        xpos += dx;
        ypos += dy;

//System.out.println(readyToMove);
        //--movement logic--
if (readyToMove) {
    double distanceY = selctY - ypos; //finds the height of the triangle between the npc1 and player
    double distanceX = selctX - xpos; //finds the length (aka base) of the triangle between the npc1 and player
    double angle = Math.atan2(distanceY, distanceX); //finds the angle of where the the player is to the npc1
    if (type == 't') { //changes speed based on unit type
        dx = Math.cos(angle) * 2; //these 2 lines always move the npc1 through the hypotenuse of said triangle i.e the shortest path
        dy = Math.sin(angle) * 2;
    }
    else if (type == 'a'){
        dx = Math.cos(angle) * 1.5; //these 2 lines always move the npc1 through the hypotenuse of said triangle i.e the shortest path
        dy = Math.sin(angle) * 1.5; //artillery are a little slower
    }
    //System.out.println(xpos+"  :  "+ypos);

    //System.out.println(selctX+"  :  "+selctY);
//        npc1.move();

    //-- stops the unit if its reached its destination --
    if (Math.abs((xpos-camX)-(selctX-camX))<10 && Math.abs((ypos-camY)-(selctY-camY))<10){
        readyToMove=false;
        dx=0;
        dy=0;
        selctX = 0;
        selctY =0;
    }
//        distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);//uses pythag. thry. to find the distance between player and npc1, and uses that to update the npc1's photo if its close
    //System.out.println(distance);
}
    }
//spawns the unit after spawn timer elapses
    public boolean isReadyToSpawn(){
        return spawnTime <=0;
    }

    /*public void updateHitbox(int camX, int camY){
        hitbox = new Rectangle(xpos-camX-20,ypos-camY-50,width,height);
    }*/

}
