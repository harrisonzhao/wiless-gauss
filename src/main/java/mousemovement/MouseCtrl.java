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

    //0 degrees is directly up, positive angle to the left and negative to the right, up until +-90 degrees
    //velocity could be negative
    public void moveAtAngleWithVel(double vel1, double vel2, double angle){


        System.out.println("old: "+x+" "+y);
        y = (int)((double)y - (Math.cos(angle*3.14/180)));
        x = (int)((double)x + (Math.sin(angle*3.14/180)));
        System.out.println("new: "+x+" "+y);
        robot.mouseMove(x,y);
    }

    public void moveMousePosition(int vx, int vy){
        y = y + angleToDistance(vy);
        x = x + angleToDistance(vx);
        if(x>1919){
            x=1919;
        }
        else if(x<0){
            x=0;
        }
        if(y>1079){
            y=1079;
        }
        else if(y<0){
            y=0;
        }
        System.out.println(x+" "+y);
        robot.mouseMove(x,y);
    }

    public void moveAtAngle(double angle){
        System.out.println("old: "+x+" "+y);
        y = (int)((double)y - (5*Math.cos(angle*3.14/180)));
        x = (int)((double)x + (5*Math.sin(angle*3.14/180)));
        System.out.println("new: "+x+" "+y);
        robot.mouseMove(x,y);
    }
    public void rightClick(){
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public void leftClick(){
        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
    }

    private int angleToDistance(int a) {
        if (a < -80) {
            return -40;
        }
        else if (a < -65) {
            return -20;
        }
        else if (a < -50) {
            return -10;
        }
        else if (a < -15) {
            return -5;
        }
        else if (a < -5) {
            return -1;
        }
        else if (a > 80) {
            return 40;
        }
        else if (a > 65) {
            return 20;
        }
        else if (a > 15) {
            return 10;
        }
        else if (a > 5) {
            return 1;
        }
        else {
            return 0;
        }
    }

}
