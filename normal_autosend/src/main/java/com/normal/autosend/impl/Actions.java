package com.normal.autosend.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * @author: fei.he
 */
public class Actions {
    public static final Logger logger = LoggerFactory.getLogger(Actions.class);

    public static Actions instance;

    private Point currentPoint;

    private Robot robot;

    /**
     * 操作位置
     */
    private OpsXy opsXy;
    public Actions(Robot robot) {
        this.robot = robot;
    }

    public synchronized static Actions getInstance(){
        if (instance == null) {
            try {
                Robot robot = new Robot();
                robot.setAutoWaitForIdle(true);
                instance =  new Actions(robot);
            } catch (AWTException e) {
                //ignore
                logger.error(e.getMessage());
            }
        }
        return instance;
    }

    public Actions moveTo(int x, int y){
        robot.mouseMove(x, y);
        opsXy = new OpsXy(x, y);
        return this;
    }

    public Actions click(){
        checkPosition();
        moveToOpsXyIfNeed();
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        return this;
    }

    private void checkPosition() {
        if(currentPoint == null){
            throw new IllegalStateException("must invoke  moveTo  first");
        }
    }


    private void moveToOpsXyIfNeed() {
        for (; !MouseInfo.getPointerInfo().getLocation().equals(currentPoint); ) {
             moveTo(opsXy.x, opsXy.y);
             if(MouseInfo.getPointerInfo().getLocation().equals(currentPoint)){
                 break;
             }
        }
    }

    @Data
    @AllArgsConstructor
    static class OpsXy {
        private int x;
        private int y;
    }
}
