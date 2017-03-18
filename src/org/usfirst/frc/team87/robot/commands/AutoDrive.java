package org.usfirst.frc.team87.robot.commands;

import org.usfirst.frc.team87.robot.Robot;
import org.usfirst.frc.team87.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class AutoDrive extends Command {
	double angleModifier;
	double leftSpeed;
	double rightSpeed;
	private PIDController angleController = new PIDController(0.60606, 15, 2, Robot.drivebase.getSourceGyro(), angleModifier -> this.angleModifier = angleModifier);
	private PIDController leftDriveController = new PIDController(0.60606, 15, 2, Robot.drivebase.getSourceEncoderLeft(), leftSpeed -> this.leftSpeed = leftSpeed);
	private PIDController rightDriveController = new PIDController(0.60606, 15, 2, Robot.drivebase.getSourceEncoderRight(), rightSpeed -> this.rightSpeed = rightSpeed);

	public AutoDrive(double distance) {
		requires(Robot.drivebase);
		Robot.drivebase.resetGyro();
		angleController.setAbsoluteTolerance(RobotMap.ANGLETOLERANCE);
		angleController.setOutputRange(0, 1);
		angleController.setSetpoint(0);
		leftDriveController.setAbsoluteTolerance(RobotMap.DISTANCETOLERANCE);
		leftDriveController.setOutputRange(-1, 1);
		leftDriveController.setSetpoint(distance * RobotMap.INCH_TO_ENC);
		rightDriveController.setAbsoluteTolerance(RobotMap.DISTANCETOLERANCE);
		rightDriveController.setOutputRange(-1, 1);
		rightDriveController.setSetpoint(distance * RobotMap.INCH_TO_ENC);
	}

	protected void initialize() {
		Timer.delay(0.5);
		angleController.enable();
		leftDriveController.enable();
		rightDriveController.enable();
	}

	protected void execute() {
		if (((leftSpeed + rightSpeed) / 2) * Robot.drivebase.getGyro() > 0) {
			leftSpeed *= angleModifier;
		} else if (((leftSpeed + rightSpeed) / 2) * Robot.drivebase.getGyro() < 0) {
			rightSpeed *= angleModifier;
		}
		if (leftDriveController.onTarget()) {
			leftSpeed = 0;
		}
		if (rightDriveController.onTarget()) {
			rightSpeed = 0;
		}
		Robot.drivebase.drive(leftSpeed, rightSpeed);
	}

	protected boolean isFinished() {
		return leftDriveController.onTarget()&&rightDriveController.onTarget();
	}

	protected void end() {
		Robot.drivebase.drive(0, 0);
		angleController.disable();
		leftDriveController.disable();
		rightDriveController.disable();
	}
}