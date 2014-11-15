package gvsu;
import java.util.*;
import robocode.*;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Fuhzee - a robot by shawn42
 */
public class Fuhzee extends Robot
{
  class RobotInfo
  {
    public double x;
    public double y;
    public String name;
    public double velocity;
    public double heading;
    public RobotInfo(ScannedRobotEvent e, Robot me) {
      name = e.getName();
      velocity = e.getVelocity();
      heading = e.getHeading();

      double meX = me.getX();
      double meY = me.getY();
      double realBearing = Math.toRadians((me.getHeading() + e.getBearing()) % 360);

      double distance = e.getDistance();
      x = me.getX() + Math.sin(realBearing) * distance;
      y = me.getY() + Math.cos(realBearing) * distance;
    }

    public String toString() {
      return ""+getName()+" ["+x+","+y+"]";
    }
  }

  HashMap<String, RobotInfo> robotCache;

	public void run() {
    robotCache = new HashMap<String, RobotInfo>();
		setColors(Color.red,Color.blue,Color.green); // body,gun,radar

    int i = 0;
		while(true) {

      i++;
      if(!robotCache.isEmpty()) {
        for(Map.Entry<String, RobotInfo> entry : robotCache.entrySet()) {
          shootWhereHeWillBe(entry.getValue());
          break;
        }
      }
      /* System.out.format("%d\n", i); */
      if(i % 50 == 0) {
        /* System.out.format("HERE!\n", i); */
        int moveAmount = 600;
        ahead(Math.ceil(Math.random()*moveAmount)-moveAmount/2);
      }
      /* back(Math.ceil(Math.random()*50)); */
      turnGunRight(10);
			
		}
	}

  public void shootWhereHeWillBe(RobotInfo target) {
    double millerTime = 20;
    double leadTimeInTicks = millerTime * target.velocity;
    double aimX = target.x + Math.sin(target.heading)*leadTimeInTicks;
    double aimY = target.y + Math.cos(target.heading)*leadTimeInTicks;
    /* System.out.format("aim: [%f,%f]\n", aimX, aimY); */
    

    // get angle to aim at
    double differenceX = aimX - getX();
    double differenceY = aimY - getY();
    if(differenceX != 0) {
      /* System.out.format("diffXY: [%f,%f]\n", differenceX, differenceY); */

      double globalTargetAngle = 0;
      if(differenceX > 0)
      {
        globalTargetAngle = (Math.PI/2 - Math.atan(differenceY/differenceX)) % (2*Math.PI);
      } else{
        globalTargetAngle = (Math.PI/2 - Math.atan(differenceY/differenceX) + Math.PI) % (2*Math.PI);
      }
      /* System.out.format("globalTargetAngle: [%f] (%f)\n", globalTargetAngle, Math.toDegrees(globalTargetAngle)); */
      double gunHeadingRads = Math.toRadians(getGunHeading());

      double diff = gunHeadingRads - globalTargetAngle ;
      /* double diff = globalTargetAngle - gunHeadingRads; */
      /* System.out.format("diff: [%f]\n", diff); */
      
      double gunTurnDeg = Math.toDegrees(diff);
      /* System.out.format("turn deg: [%f]\n", gunTurnDeg); */
      turnGunRight(gunTurnDeg);
      double gunTurnDegreePerTick = 20.0;
      double ticksToTurn = gunTurnDeg/gunTurnDegreePerTick;
      double distanceToTarget = Math.sqrt(differenceX*differenceX+differenceY*differenceY);

      double shotTime = millerTime - ticksToTurn;
      double firePower = (20-(distanceToTarget/shotTime))/3;

      /* System.out.format("power: [%f]\n", firePower); */
      fire(firePower);
      robotCache.clear();
    }
  }

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
    updateRobotCache(e);
	}

  public void updateRobotCache(ScannedRobotEvent e){
    // fire(2);
    RobotInfo info = new RobotInfo(e, this);
    // System.out.println(info.toString());
    robotCache.put(e.getName(), info);
  }

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
	}	
}
