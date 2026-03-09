import java.awt.*;

public class Unit {
    //variables
    Image idlePic;
    int xpos;
    int ypos;
    int height;
    int width;
    double dx;
    double dy;
    int spawnTime;
    boolean isSelected;
    boolean readyToMove;
    Rectangle hitbox;
    int camX;
    int camY;
    String team;
    int selctX;
    int selctY;
    //should add a system where units can stack up/split from stacks

    //unit constuctor assigns position, team allegiance, size and spawn time are constant
    public Unit(int xpos, int ypos, String team){
        idlePic = Toolkit.getDefaultToolkit().getImage("unit1.png");
        this.xpos = xpos;
        this.ypos = ypos;
        this.team = team;
        height = 100;
        width = 50;
        spawnTime = 500;
        hitbox = new Rectangle(xpos-camX,ypos-camY,width,height);
    }
//updates/runs spawn timer
    public void spawnUpdate(){
        spawnTime--;
    }
//updates units into world coordinates
    public void update(int camX, int camY){
        hitbox = new Rectangle(xpos-camX-10,ypos-camY-27,width-30,height-45);
        xpos += dx;
        ypos += dy;

//System.out.println(readyToMove);
        //--movement logic--
if (readyToMove&&selctX-camX!=0&&selctY-camY!=0) {
    double distanceY = selctY - ypos; //finds the height of the triangle between the npc1 and player
    double distanceX = selctX - xpos; //finds the length (aka base) of the triangle between the npc1 and player
    double angle = Math.atan2(distanceY, distanceX); //finds the angle of where the the player is to the npc1
    dx = Math.cos(angle) * 2; //these 2 lines always move the npc1 through the hypotenuse of said triangle i.e the shortest path
    dy = Math.sin(angle) * 2;
    //System.out.println(xpos+"  :  "+ypos);

    //System.out.println(selctX+"  :  "+selctY);
//        npc1.move();
    //stops the unit if its reached its destination
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
