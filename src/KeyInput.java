import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyInput extends KeyAdapter {
    BasicGameApp game;
    Set<Integer> pressedKeys;

    public KeyInput(BasicGameApp game){
        this.game = game;
        pressedKeys = new HashSet<>();
    }

    public void keyPressed(KeyEvent e){ //method for when a key is pressed using keyadapter stuff to identify what key is pressed (numerical ID)
        pressedKeys.add(e.getKeyCode()); //adds the pressed key into the set
        game.keyPressed(e); //basically runs the keyPressed method in basicGameApp instead of here in the KeyInput class
    }

    public void keyReleased(KeyEvent e){//same thing as KeyPressed but for when the key is released
        game.keyReleased(e);//runs keyreleased in the basicgame class
        pressedKeys.remove((Integer)e.getKeyCode());//removes the ID of the key that was pressed from the set, when its released

        //       upKeys.add(e.getKeyCode());
    }

    public boolean isKeyDown (int keyCode){ //googled, makes a boolean that returns a 'true' when you give it the int ID of a key thats in the pressedkeys set, tl;dr checks whether that key is being pressed, just a more complicated version of the moving boolean in basicgame
        return pressedKeys.contains(keyCode);
    }


}
