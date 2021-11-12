package demoapp.mapping;


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_ICLONEABLE
//#else
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter could not confirm whether this is a namespace alias or a type alias:
//using ICloneable = GeoAPI.ICloneable;
//#endif


/**
 Interface for lightweight classes used to store coordinates on the 2-dimensional Cartesian plane.
*/
@Deprecated
public interface ICoordinate extends Cloneable, Comparable<ICoordinate>
{
	/** 
	 The x-ordinate value
	*/
	double getX();
	void setX(double value);

	/** 
	 The y-ordinate value
	*/
	double getY();
	void setY(double value);

	/** 
	 The z-ordinate value
	*/
	double getZ();
	void setZ(double value);

	/** 
	 The measure value
	*/
	double getM();
	void setM(double value);

	/** 
	 Gets or sets all ordinate values
	*/
	ICoordinate getCoordinateValue();
	void setCoordinateValue(ICoordinate value);

	/** 
	 Gets or sets the <see cref="Ordinate"/> value of this <see cref="ICoordinate"/>
	 
	 @param index The <see cref="Ordinate"/> index
	*/
	double get(Ordinate index);
	void set(Ordinate index, double value);

	/** 
	 Computes the 2-dimensional distance to the <paramref name="other"/> coordiante.
	 
	 @param other The other coordinate
	 @return The 2-dimensional distance to other
	*/
	double Distance(ICoordinate other);

	/** 
	 Compares equality for x- and y-ordinates
	 
	 @param other The other coordinate
	 @return <c>true</c> if x- and y-ordinates of this coordinate and <see paramref="other"/> coordiante are equal.
	*/
	boolean Equals2D(ICoordinate other);

	/** 
	 Compares equality for x-, y- and z-ordinates
	 
	 @param other The other coordinate
	 @return <c>true</c> if x-, y- and z-ordinates of this coordinate and <see paramref="other"/> coordiante are equal.
	*/
	boolean Equals3D(ICoordinate other);
}