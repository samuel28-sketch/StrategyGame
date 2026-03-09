import java.awt.*;
import java.util.ArrayList;

public class Land {
    //--variables--
    char team;
    int teamCount;
    int rowIndex;
    int columnIndex;
    double xpos;
    double ypos;
    double size;
    Polygon tile;
    ArrayList<Cities> cities; //add smth like if a city is contained in a tile itll shift the color based on %ownership e.g if red has 2 cities in a tile but bluie has 3, the tile will be 3/5 blue and 2/5 red, that way i can also color only the tiles on land by just adding cities in them instead of having to draw a polygon around europe
                                //nvm cuz what abt if i dont want a tile to have a city or smth, same system can work tho, maybe if it doesn thave a tile i can copy the color of an adjacent tile w a city
    boolean playerControlled; //will be needed in the future, i could prob attribute city stats to the player through tiles, but that wouldnt work as well if the player is occuping a city in a multi city tile, but this will need to be used for only allowing the player to spawn troops on tiles they control
    //for tiles:grid if (tiles.playercontrol && tiles.polygon.containes(placeSelectX&Y)) {spawn player unit}

//array for hexagon points
int[] xPoints = new int[6];
int[] yPoints = new int[6];

//constructor assigning size and position
public Land(int size,double xpos, double ypos, double rowIndex, double columnIndex){
    this.size = size;
    this.xpos = xpos;
    this.ypos = ypos;
    team = 'n';
    tile = new Polygon(xPoints,yPoints,6);
    this.rowIndex=(int)rowIndex; //--- hold the index of where it is in the griid list so that i can find its neighbors?
    this.columnIndex=(int)columnIndex;
}
//updates tile position into world coordinates
public void update(int camX,int camY){
    for (int i =0;i<6;i++){
        double angle = Math.toRadians(60 * i - 30);
        this.xPoints[i] = (int)(xpos-camX + size * Math.cos(angle));
        this.yPoints[i] = (int)(ypos-camY + size * Math.sin(angle));
        tile = new Polygon(xPoints,yPoints,6);
    }
}


}
