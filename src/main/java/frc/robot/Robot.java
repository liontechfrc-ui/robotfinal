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

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  XboxController pad = new XboxController(0);
  Timer timer = new Timer();
  
  public class drive{
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
  
  public class system{
    public final SparkMax intakeMotor = new SparkMax(14, MotorType.kBrushed);
    public final SparkMax shooterMotor = new SparkMax(15, MotorType.kBrushed);

    public final SparkMaxConfig intakeConfig = new SparkMaxConfig();
    public final SparkMaxConfig shooterConfig = new SparkMaxConfig();
  }

  

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
  */

  drive drive;
  system system;
  public Robot() {
    drive = new drive();
    system = new system();

    drive.rightFrontConfig.inverted(true);
    
    drive.leftRearConfig.follow(13);
    drive.rightRearConfig.follow(11);

    drive.leftFrontConfig.idleMode(IdleMode.kBrake);
    drive.rightFrontConfig.idleMode(IdleMode.kBrake);
    drive.leftRearConfig.idleMode(IdleMode.kBrake);
    drive.rightRearConfig.idleMode(IdleMode.kBrake);

    drive.leftFrontMotorESC.configure(drive.leftFrontConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    drive.rightFrontMotorESC.configure(drive.rightFrontConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    drive.leftRearMotorESC.configure(drive.leftRearConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    drive.rightRearMotorESC.configure(drive.rightRearConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

    system.intakeMotor.configure(system.intakeConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    system.shooterMotor.configure(system.shooterConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);    
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    drive.drive.arcadeDrive(pad.getLeftY(), pad.getRightX());

    if(pad.getLeftBumperButton() == true){
      system.intakeMotor.set(1);
    }
    
    else if (pad.getRightBumperButton() == true){
      timer.start();
      system.shooterMotor.set(1);
      
      if(timer.get() > 2){
        system.intakeMotor.set(1);
      }
      
      else{
        system.intakeMotor.stopMotor();
      }
    }
    
    else{
      system.intakeMotor.stopMotor();
      system.shooterMotor.stopMotor();
      timer.reset();
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}