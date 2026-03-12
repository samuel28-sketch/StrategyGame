import java.awt.*;

public class Cities {
    int xpos;
    int ypos;
    int camX;
    int camY;
    Rectangle hitbox;
    char type;
    int height;
    int width;
    char team;
    int captureTime;
    int maxCaptureTime;

    //icon, like a circle
    //label, maybe
    //team / occupier
    //a population or point value
    //booleans like isAttacked, maybe a double 0-1 on damage, developmenet level/buildings
    //buildings should prob be methods

    public Cities(int xpos, int ypos, char type, char team){
        this.xpos = xpos;
        this.ypos = ypos;
        this.type = type;
        this.team = team;
        //assigns diff attributes according to city size
        if (type == 's') {
            height = 5;
            width = 5;
            captureTime = 10;
            maxCaptureTime = 10;
            hitbox = new Rectangle(xpos - width/2, ypos - height/2, width, height);
        }
        if (type == 'm') {
            height = 8;
            width = 8;
            captureTime = 50;
            maxCaptureTime =50;
            hitbox = new Rectangle(xpos - width/2, ypos - height/2, width, height);
        }
        if (type == 'l') {
            height = 10;
            width = 10;
            hitbox = new Rectangle(xpos - width/2, ypos - height/2, width, height);
            captureTime = 100;
            maxCaptureTime =100;
        }
        if (type == 'c') {
            height = 15;
            width = 15;
            captureTime = 200;
            maxCaptureTime =200;
            hitbox = new Rectangle(xpos - width/2, ypos - height/2, width, height);
        }
    }
//updates screen cords into world cords
    public void update(int camX,int camY){
        hitbox = new Rectangle(xpos-width/2-camX,ypos-height/2-camY,width,height);
    }

}
