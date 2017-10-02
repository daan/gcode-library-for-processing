public class GCodes {

// M280 set servo position
public static String servo(int servoIndex, int angle)
{
  return  "M280 p" + servoIndex + " s" + angle;
}

// G90, G91 set absolute or relative coordinates
public static String absoluteCoordinates() 
{
  return "G90";
}
public static String relativeCoordinates() 
{
    return "G91";
}

public static String rapidMove(float x, float y)
{
  return "G0 x" + x + " y" + y;
}

public static String rapidMove(float x, float y, float z)
{
  return "G0 x" + x + " y" + y + " z" + z;
}

public static String move(float x, float y, float f)
{
  return "G1 x" + x + " y" + y + " f" +f;
}

public static String move(float x, float y, float z, float f)
{
  return "G1 x" + x + " y" + y + " z" + z + " f" + f;
}

public static String home()
{
   return "G28";
}

public static String homeXY()
{
   return "G28 x y";
}

}