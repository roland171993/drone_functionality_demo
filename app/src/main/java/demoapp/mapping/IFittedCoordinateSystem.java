package demoapp.mapping;

/**
 A coordinate system which sits inside another coordinate system. The fitted
 coordinate system can be rotated and shifted, or use any other math transform
 to inject itself into the base coordinate system.
 */
public interface IFittedCoordinateSystem extends ICoordinateSystem
{
	/**
	 Gets underlying coordinate system.
	 */
	ICoordinateSystem getBaseCoordinateSystem();
	/**
	 Gets Well-Known Text of a math transform to the base coordinate system.
	 The dimension of this fitted coordinate system is determined by the source
	 dimension of the math transform. The transform should be one-to-one within
	 this coordinate system's domain, and the base coordinate system dimension
	 must be at least as big as the dimension of this coordinate system.

	 @return
	 */
	String ToBase();
}