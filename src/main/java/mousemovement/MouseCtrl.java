package mousemovement;

import java.awt.*;
import java.awt.event.InputEvent;

public class MouseCtrl {

    private int x;
    private int y;
    Robot robot;

    public void setInitialPos(int initX, int initY){
        this.x = initX;
        this.y = initY;
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void moveUp(int distance){
        y = y - distance;
        robot.mouseMove(x,y);
    }

    public void moveDown(int distance){
        y = y + distance;
        robot.mouseMove(x,y);
    }

    public void moveRight(int distance){
        x = x + distance;
        robot.mouseMove(x,y);
    }

    public void moveLeft(int distance){
        x = x - distance;
        robot.mouseMove(x,y);
    }

    public void rightClick(){
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public void leftClick(){
        robot.mousePress(InputEvent.BUTTON2_MASK);
        robot.mouseRelease(InputEvent.BUTTON2_MASK);
    }

}
