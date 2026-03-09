import java.awt.*;

public class GUI {
    //variables
    boolean unitDeplMenu;
    Image deplGUI;
    Image troopBar;
    Rectangle spawnButton;
    Rectangle placeButton;

    //constructor assigns images/buttons positions
    public GUI(){
        deplGUI = Toolkit.getDefaultToolkit().getImage("troopGUI1.png");
        troopBar = Toolkit.getDefaultToolkit().getImage("troopGUIbar.png");
        spawnButton = new Rectangle(270, 55,75,15);
        placeButton = new Rectangle(115,55,150,15);
    }


}
