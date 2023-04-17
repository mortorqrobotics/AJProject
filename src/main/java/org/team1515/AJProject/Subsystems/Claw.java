package org.team1515.AJProject.Subsystems;

import org.team1515.AJProject.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Claw extends SubsystemBase {

    private CANSparkMax rightMotor;
    private CANSparkMax leftMotor;
    private double speed = 0.2;

    public Claw(){

        rightMotor = new CANSparkMax(RobotMap.RIGHT_CLAW_ID, MotorType.kBrushless);
        leftMotor = new CANSparkMax(RobotMap.LEFT_CLAW_ID, MotorType.kBrushless);

    }

    public void intake(){

        rightMotor.set(speed);
        leftMotor.set(speed);

    }

    public void outtake(){

        rightMotor.set(-speed);
        leftMotor.set(-speed);

    }

    public void end(){

        rightMotor.set(0);
        leftMotor.set(0);

    }
    
}
