package org.usfirst.frc.team87.robot.commands;

import org.usfirst.frc.team87.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class AutoGearCheck extends Command {
	private boolean gearDelivered;
	private Timer timer;
	public AutoGearCheck() {
		requires(Robot.gearsensor);
	}

	protected void execute() {

		if(!Robot.gearsensor.checkForGear()){
            if(timer.hasPeriodPassed(2)){
                gearDelivered = true;
            }else if(timer.get() == 0){
                timer.start();
            }
        }else{
            timer.reset();
            timer.stop();
            gearDelivered = false;
        }
	}

	protected boolean isFinished() {
		return gearDelivered;
	}

	protected void end() {
	}
}