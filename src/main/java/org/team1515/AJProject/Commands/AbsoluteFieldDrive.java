// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.AJProject.Commands;

import java.util.function.DoubleSupplier;

import org.team1515.AJProject.Subsystems.Drivetrain;

import com.team3841.SwerveLib.swervelib.SwerveController;
import com.team3841.SwerveLib.swervelib.SwerveDrive;
import com.team3841.SwerveLib.swervelib.telemetry.SwerveDriveTelemetry;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * An example command that uses an example subsystem.
 */
public class AbsoluteFieldDrive extends CommandBase
{

  private final Drivetrain swerve;
  private final DoubleSupplier  vX, vY, heading;
  private final boolean isOpenLoop;

  /**
   * Used to drive a swerve robot in full field-centric mode.  vX and vY supply translation inputs, where x is
   * torwards/away from alliance wall and y is left/right. headingHorzontal and headingVertical are the Cartesian
   * coordinates from which the robot's angle will be derived— they will be converted to a polar angle, which the robot
   * will rotate to.
   *
   * @param swerve  The swerve drivebase subsystem.
   * @param vX      DoubleSupplier that supplies the x-translation joystick input.  Should be in the range -1 to 1 with
   *                deadband already accounted for.  Positive X is away from the alliance wall.
   * @param vY      DoubleSupplier that supplies the y-translation joystick input.  Should be in the range -1 to 1 with
   *                deadband already accounted for.  Positive Y is towards the left wall when looking through the driver
   *                station glass.
   * @param heading DoubleSupplier that supplies the robot's heading angle.
   */

  public AbsoluteFieldDrive(Drivetrain swerve, DoubleSupplier vX, DoubleSupplier vY,
                            DoubleSupplier heading, boolean isOpenLoop)
  {
    this.swerve = swerve;
    this.vX = vX;
    this.vY = vY;
    this.heading = heading;
    this.isOpenLoop = isOpenLoop;

    addRequirements(swerve);
  }

  @Override
  public void initialize()
  {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {

    // Get the desired chassis speeds based on a 2 joystick module.
    System.out.println("Vx: " + vX.getAsDouble() + " Vy: " + vY.getAsDouble() + " Heading: " + heading.getAsDouble() * Math.PI);
    ChassisSpeeds desiredSpeeds = swerve.getTargetSpeeds(vX.getAsDouble(), vY.getAsDouble(),
                                                         new Rotation2d(heading.getAsDouble() * Math.PI));

    // Limit velocity to prevent tippy
    // Translation2d translation = SwerveController.getTranslation2d(desiredSpeeds);
    // translation = SwerveMath.limitVelocity(translation, swerve.getFieldVelocity(), swerve.getPose(),
    //                                        RobotMap.LOOP_TIME, RobotMap.ROBOT_MASS, List.of(RobotMap.CHASSIS),
    //                                        swerve.getSwerveDriveConfiguration());
    // SmartDashboard.putNumber("LimitedTranslation", translation.getX());
    // SmartDashboard.putString("Translation", translation.toString());

    // Make the robot move
    System.out.println(heading.getAsDouble() * Math.PI * SwerveDriveTelemetry.maxAngularVelocity);
    swerve.drive(new Translation2d(vX.getAsDouble(), vY.getAsDouble()), 1, true, isOpenLoop);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
    return false;
  }


}
