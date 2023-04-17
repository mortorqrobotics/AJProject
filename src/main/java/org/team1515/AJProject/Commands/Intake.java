package org.team1515.AJProject.Commands;

import org.team1515.AJProject.Subsystems.Claw;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Intake extends CommandBase{

    private Claw claw;

    public Intake (Claw claw){

        this.claw = claw;
        addRequirements(claw);

    }

    @Override
    public void execute(){

        claw.intake();

    }

    @Override
    public void end(boolean interrupted){

        claw.end();

    }
    
}
