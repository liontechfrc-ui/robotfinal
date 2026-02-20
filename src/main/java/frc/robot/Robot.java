// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {
  XboxController pad = new XboxController(0);
  Timer timer = new Timer();

  public class drive {
    public final SparkMax leftFrontMotorESC = new SparkMax(13, MotorType.kBrushed);
    public final SparkMax rightFrontMotorESC = new SparkMax(11, MotorType.kBrushed);
    public final SparkMax leftRearMotorESC = new SparkMax(12, MotorType.kBrushed);
    public final SparkMax rightRearMotorESC = new SparkMax(10, MotorType.kBrushed);

    public final SparkMaxConfig leftFrontConfig = new SparkMaxConfig();
    public final SparkMaxConfig rightFrontConfig = new SparkMaxConfig();
    public final SparkMaxConfig leftRearConfig = new SparkMaxConfig();
    public final SparkMaxConfig rightRearConfig = new SparkMaxConfig();

    public final DifferentialDrive drive = new DifferentialDrive(leftFrontMotorESC, rightFrontMotorESC);
  }

  public class system {
    public final SparkMax intakeMotor = new SparkMax(14, MotorType.kBrushed);
    public final SparkMax shooterMotor = new SparkMax(15, MotorType.kBrushed);

    public final SparkMaxConfig intakeConfig = new SparkMaxConfig();
    public final SparkMaxConfig shooterConfig = new SparkMaxConfig();
  }

  drive drive;
  system system;

  public Robot() {
    drive = new drive();
    system = new system();

    // Sol tarafı invert (ileri-geri tersliğini düzeltmek için)
    drive.leftFrontConfig.inverted(true);
    drive.leftRearConfig.inverted(true);

    // Sağ tarafı invert (sağ/sol dönüş doğru)
    drive.rightFrontConfig.inverted(true);
    drive.rightRearConfig.inverted(true);

    // Rear motorlar front motorları takip ediyor
    drive.leftRearConfig.follow(13);
    drive.rightRearConfig.follow(11);

    // Fren modu
    drive.leftFrontConfig.idleMode(IdleMode.kBrake);
    drive.rightFrontConfig.idleMode(IdleMode.kBrake);
    drive.leftRearConfig.idleMode(IdleMode.kBrake);
    drive.rightRearConfig.idleMode(IdleMode.kBrake);

    // Config uygula
    drive.leftFrontMotorESC.configure(
        drive.leftFrontConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    drive.rightFrontMotorESC.configure(
        drive.rightFrontConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    drive.leftRearMotorESC.configure(
        drive.leftRearConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    drive.rightRearMotorESC.configure(
        drive.rightRearConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

    system.intakeMotor.configure(
        system.intakeConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    system.shooterMotor.configure(
        system.shooterConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    timer.stop();
    timer.reset();
  }

  @Override
  public void teleopPeriodic() {
    // Tetikler: Sağ tetik ileri, Sol tetik geri (terslik düzeltildi)
    double forward = pad.getLeftTriggerAxis() - pad.getRightTriggerAxis();
    // Direksiyon: Sol joystick X ekseni
    double turn = pad.getLeftX();

    drive.drive.arcadeDrive(forward, turn);

    if (pad.getLeftBumperButton()) {
      system.intakeMotor.set(1);
      system.shooterMotor.stopMotor();
      timer.stop();
      timer.reset();
    } else if (pad.getRightBumperButton()) {
      if (timer.get() == 0) { // ilk girişte başlat gibi davranır
        timer.reset();
        timer.start();
      }

      system.shooterMotor.set(1);

      if (timer.get() > 2) {
        system.intakeMotor.set(1);
      } else {
        system.intakeMotor.stopMotor();
      }
    } else {
      system.intakeMotor.stopMotor();
      system.shooterMotor.stopMotor();
      timer.stop();
      timer.reset();
    }
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}