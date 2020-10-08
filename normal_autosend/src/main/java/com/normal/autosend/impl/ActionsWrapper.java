package com.normal.autosend.impl;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author: fei.he
 */
public class ActionsWrapper {
    public static final Logger logger = LoggerFactory.getLogger(ActionsWrapper.class);

    private Robot robot;

    private Actions actions;

    private EffectRange effectRange;

    private Function<Void, Void> relocateAction;

    public ActionsWrapper(int lx, int rx, int ty, int by, Actions actions, Function<Void, Void> relocateAction) {
        try {
            this.effectRange = new EffectRange(lx, rx, ty, by);
            this.robot = new Robot();
            robot.setAutoWaitForIdle(true);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

    }

    private void ensureInRightLocation() {
        for (int i = 0; ; i++) {
            if (effectRange.inEffectRange()) {
                break;
            }
            if (i > 3) {
                throw new RuntimeException("最多尝试3次,未在规定作用域");
            }
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            } catch (InterruptedException e) {
                //ignore
                logger.error(e.getMessage());
            }
            if (relocateAction != null) {
                relocateAction.apply(null);
            }
        }
    }

    public ActionsWrapper sendKeys(CharSequence... keys) {
        ensureInRightLocation();
        actions.sendKeys(keys);
        return this;
    }

    public ActionsWrapper sendKeys(WebElement target, CharSequence... keys) {
        ensureInRightLocation();
        actions.sendKeys(target, keys);
        return this;
    }

    public ActionsWrapper keyUp(CharSequence key) {
        ensureInRightLocation();
        actions.keyUp(key);
        return this;
    }

    public ActionsWrapper keyUp(WebElement target, CharSequence key) {
        ensureInRightLocation();
        actions.keyUp(target, key);
        return this;
    }

    public ActionsWrapper keyDown(CharSequence key) {
        ensureInRightLocation();
        actions.keyDown(key);
        return this;
    }

    public ActionsWrapper keyDown(WebElement target, CharSequence key) {
        ensureInRightLocation();
        actions.keyDown(target, key);
        return this;
    }

    public ActionsWrapper perform() {
        ensureInRightLocation();
        actions.perform();
        return this;
    }

    /**
     * 有效范围
     */
    static class EffectRange {
        int lx;
        int rx;
        int ty;
        int by;

        public EffectRange(int lx, int rx, int ty, int by) {
            this.lx = lx;
            this.rx = rx;
            this.ty = ty;
            this.by = by;
        }

        public boolean inEffectRange() {
            Point location = MouseInfo.getPointerInfo().getLocation();
            logger.info("location: {}", location);
            return location.x >= lx && location.x <= rx && location.y >= ty && location.y <= by;
        }

    }
}
