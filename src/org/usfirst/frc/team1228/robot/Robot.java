package org.usfirst.frc.team1228.robot;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
public class Robot extends IterativeRobot {
	//<GLOBAL VARIABALS>
	
	//the main 4 motor drive train controller
	RobotDrive myDrive;
	
	//defining the 4 motors for the drive train
	Talon frontLeft;
	Talon frontRight;
	Talon rearLeft;
	Talon rearRight;
	Talon climb;

	//defining the Joystick
	Joystick driveStick;
	
	Command autonomousCommand;
	
	//</GLOBAL VARIABALS>
	
	//main method for defining things to be referred to by other methods 
	@Override public void robotInit() {
		
		//assigning the port values for the drive train motors to the Victors themselves 
		frontLeft = new Talon(RobotMap.leftFront);
    	frontRight = new Talon(RobotMap.rightFront);
    	rearLeft = new Talon(RobotMap.leftRear);
    	rearRight = new Talon(RobotMap.rightRear);
    	
    	climb = new Talon(RobotMap.climbPort);
    	
    	//assigning the Victor motor objects to the drive train so they can work together simultaneously
    	myDrive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
    	
    	//set the motors to inverted type true to work with each other without harm
    	myDrive.setInvertedMotor(MotorType.kFrontLeft, false);
    	myDrive.setInvertedMotor(MotorType.kRearLeft, false);
    	myDrive.setInvertedMotor(MotorType.kFrontRight, true);
    	myDrive.setInvertedMotor(MotorType.kRearRight, true);
    	
    	//set the port value of the USB Joystick
    	driveStick = new Joystick(OI.joystickPort);
    	
    	//solenoid1 = new DoubleSolenoid(1, 2);
    	//solenoid1.set(DoubleSolenoid.Value.kOff);
	}
	
	//used for operator control
	//is called every ~20ms{due to network latency}
	@Override public void teleopPeriodic() {
		
		if (autonomousCommand != null){
			autonomousCommand.cancel();
		}
		
		turnOff();
		
		//while loop to check if teleop is selected
		//and to check that it is enabled
		while (isOperatorControl() && isEnabled()) {
			
			//defining the drive train to have mecanum drive
			//(one joystick controlling robotic translational movements)	
    		myDrive.mecanumDrive_Cartesian(driveStick.getX(), driveStick.getY(), driveStick.getThrottle() - .005, 0);
    	
    		//checks to see if one of joystick buttons was pressed
    		//index starts from [1,2,...]
    		if(driveStick.getRawButton(4)){
    			climb.set(0.50);
    			Timer.delay(0.1);
    			climb.set(0.0);
    		}else if(driveStick.getRawButton(6)){
    			climb.set(-0.50);
    			Timer.delay(0.1);
    			climb.set(0.0);
    		}
    		
    	}
	}
	
	//used for autonomous control
	//is called every ~20ms{due to network latency}
	@Override public void autonomousPeriodic() {
		
		myDrive.setSafetyEnabled(false);
		
		//while loop to check if autonomous is selected
		//and to check that it is enabled
		while(isAutonomous() && isEnabled()){}
		
		myDrive.setSafetyEnabled(true);
	}
	
	public void turnOff(){
		frontLeft.set(0.0);
		frontRight.set(0.0);
		rearLeft.set(0.0);
		rearRight.set(0.0);
	}
}