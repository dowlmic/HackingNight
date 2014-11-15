
package gvsu;


import robocode.*;
import java.awt.Color;
import robocode.util.Utils;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import robocode.AdvancedRobot;


public class Spam extends AdvancedRobot {
	int turnDirection = 1; // Clockwise or counterclockwise


	public void run() {
		// Set colors
		setBodyColor(Color.black);
		setGunColor(Color.red);
		setRadarColor(Color.red);
		setBulletColor(Color.red);
		setScanColor(Color.orange);
		while (true) {
			setTurnRight(10000);
			// Limit our speed to 5
			setMaxVelocity(5);
			// Start moving (and turning)
			ahead(10000);
			//turnRight(5 * turnDirection);
		}
	}


	public void onScannedRobot(ScannedRobotEvent e) {

		if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}

		turnRight(e.getBearing());
		ahead(e.getDistance() + 5);
		if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}
		turnRight(e.getBearing());
	
		// Determine a shot that won't kill the robot...
		// We want to ram him instead for bonus points
		if (e.getEnergy() > 16) {
			fire(3);
		} else if (e.getEnergy() > 10) {
			fire(2);
		} else if (e.getEnergy() > 4) {
			fire(1);
		} else if (e.getEnergy() > 2) {
			fire(.5);
		} else if (e.getEnergy() > .4) {
			fire(.1);
		}
		scan(); // Might want to move ahead again!
	}


	public void onHitRobot(HitRobotEvent e) {
		if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}
		turnRight(e.getBearing());
		if (e.isMyFault())
		{
			fire(3);
		}
		// Determine a shot that won't kill the robot...
		// We want to ram him instead for bonus points
		if (e.getEnergy() > 16) {
			fire(3);
		} else if (e.getEnergy() > 10) {
			fire(2);
		} else if (e.getEnergy() > 4) {
			fire(1);
		} else if (e.getEnergy() > 2) {
			fire(.5);
		} else if (e.getEnergy() > .4) {
			fire(.1);
		}
		//ahead(40); // Ram him again!
	}
}
