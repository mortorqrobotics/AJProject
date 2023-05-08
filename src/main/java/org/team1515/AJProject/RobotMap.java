package org.team1515.AJProject;

import com.team3841.SwerveLib.swervelib.math.Matter;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class RobotMap {
    
    //CLAW
    public static final int RIGHT_CLAW_ID = 17;
    public static final int LEFT_CLAW_ID = 16;

    //DRIVETRAIN
    public static final double ROBOT_MASS = 50.3488;
    public static final double LOOP_TIME  = 0.13; //s, 20ms + 110ms sprk max velocity lag, TODO: find falcon velocity lag
    public static final Matter CHASSIS    = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);

}
