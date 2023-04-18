package org.team1515.AJProject;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Controls {

    public static final Trigger INTAKE = new Trigger(Controls::getRightTrigger);
    public static final Trigger OUTTAKE = new Trigger(Controls::getLeftTrigger);

    public static boolean getRightTrigger(){
        return RobotContainer.controller.getRightTriggerAxis() > .25;
    }

    public static boolean getLeftTrigger(){
        return RobotContainer.controller.getLeftTriggerAxis() > .25;
    }
}
