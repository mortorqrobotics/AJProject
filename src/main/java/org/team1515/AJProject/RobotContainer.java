// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.AJProject;

import java.io.File;

import org.team1515.AJProject.Commands.AbsoluteDrive;
import org.team1515.AJProject.Commands.AbsoluteFieldDrive;
import org.team1515.AJProject.Commands.Intake;
import org.team1515.AJProject.Commands.Outtake;
import org.team1515.AJProject.Subsystems.Claw;
import org.team1515.AJProject.Subsystems.Drivetrain;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj.RobotBase;

public class RobotContainer {

  public Claw claw;
  private Drivetrain drivebase;
  public static XboxController controller;

  public RobotContainer() {

    claw = new Claw();
    controller = new XboxController(0);
    drivebase = new Drivetrain(new File(Filesystem.getDeployDirectory(), "swerve"));

    configureBindings();

    AbsoluteDrive closedAbsoluteDrive = new AbsoluteDrive(drivebase,
      // Applies deadbands and inverts controls because joysticks
      // are back-right positive while robot
      // controls are front-left positive
      () -> MathUtil.applyDeadband(controller.getLeftY(), 0.01),
      () -> MathUtil.applyDeadband(controller.getLeftX(), 0.01),
      () -> -controller.getRightX(),
      () -> -controller.getRightY(),
      false);

    AbsoluteFieldDrive closedFieldAbsoluteDrive = new AbsoluteFieldDrive(drivebase,
      () ->
          MathUtil.applyDeadband(controller.getLeftY(), 0.01),
      () -> MathUtil.applyDeadband(controller.getLeftX(), 0.01),
      () -> controller.getRawAxis(2), false);

      drivebase.setDefaultCommand(!RobotBase.isSimulation() ? closedFieldAbsoluteDrive : closedFieldAbsoluteDrive);

  }

  private void configureBindings() {

    Controls.INTAKE.whileTrue(new Intake(claw));
    Controls.OUTTAKE.whileTrue(new Outtake(claw));

  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
