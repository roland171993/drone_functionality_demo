package demoapp.mapping;


/**
 A lightweight class used to store coordinates on the 2-dimensional Cartesian plane.
 <p>
 It is distinct from <see cref="IPoint"/>, which is a subclass of <see cref="IGeometry"/>.
 Unlike objects of type <see cref="IPoint"/> (which contain additional
 information such as an envelope, a precision model, and spatial reference
 system information), a <other>Coordinate</other> only contains ordinate values
 and propertied.
 </p>
 <p>
 <other>Coordinate</other>s are two-dimensional points, with an additional Z-ordinate.    
 If an Z-ordinate value is not specified or not defined,
 constructed coordinates have a Z-ordinate of <code>NaN</code>
 (which is also the value of <see cref="NullOrdinate"/>).
 </p>
 
 
 Apart from the basic accessor functions, NTS supports
 only specific operations involving the Z-ordinate.
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning disable 612,618
public class Coordinate implements ICoordinate
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning restore 612,618
{
	/**
	 The value used to indicate a null or missing ordinate value.
	 In particular, used for the value of ordinates for dimensions
	 greater than the defined dimension of a coordinate.
	*/
	public static final double NullOrdinate = Double.NaN;

	/** 
	 X coordinate.
	*/
	public double mX; // = Double.NaN;
	/** 
	 Y coordinate.
	*/
	public double mY; // = Double.NaN;
	/** 
	 Z coordinate.
	*/
	public double mZ; // = Double.NaN;

	/** 
	 Constructs a <other>Coordinate</other> at (x,y,z).
	 
	 @param x X value.
	 @param y Y value.
	 @param z Z value.
	*/
	public Coordinate(double x, double y, double z)
	{
		mX = x;
		mY = y;
		mZ = z;
	}


//	public final double get(Ordinate ordinateIndex)
//	{
//		switch (ordinateIndex)
//		{
//			case X:
//				return mX;
//			case Y:
//				return mY;
//			case Z:
//				return mZ;
//		}
//		throw new IndexOutOfBoundsException("ordinateIndex");
//	}
//	public final void set(Ordinate ordinateIndex, double value)
//	{
//		switch (ordinateIndex)
//		{
//			case X:
//				mX = value;
//				return;
//			case Y:
//				mY = value;
//				return;
//			case Z:
//				mZ = value;
//				return;
//		}
//		throw new IndexOutOfBoundsException("ordinateIndex");
//	}

	/** 
	  Constructs a <other>Coordinate</other> at (0,0,NaN).
	*/
	public Coordinate()
	{
		this(0.0, 0.0, NullOrdinate);
	}

	/** 
	 Constructs a <other>Coordinate</other> having the same (x,y,z) values as
	 <other>other</other>.
	 
	 @param c <other>Coordinate</other> to copy.
	*/
	@Deprecated
	public Coordinate(ICoordinate c)
	{
		this(c.getX(), c.getY(), c.getZ());
	}

	/** 
	 Constructs a <other>Coordinate</other> having the same (x,y,z) values as
	 <other>other</other>.
	 
	 @param c <other>Coordinate</other> to copy.
	*/
	public Coordinate(Coordinate c)
	{
		this(c.mX, c.mY, c.mZ);
	}

	/** 
	 Constructs a <other>Coordinate</other> at (x,y,NaN).
	 
	 @param x X value.
	 @param y Y value.
	*/
	public Coordinate(double x, double y)
	{
		this(x, y, NullOrdinate);
	}

	/** 
	 Gets/Sets <other>Coordinate</other>s (x,y,z) values.
	*/
	public final Coordinate getCoordinateValue()
	{
		return this;
	}
	public final void setCoordinateValue(Coordinate value)
	{
		mX = value.mX;
		mY = value.mY;
		mZ = value.mZ;
	}

	/** 
	 Returns whether the planar projections of the two <other>Coordinate</other>s are equal.
	
	 @param other <other>Coordinate</other> with which to do the 2D comparison.
	 @return 
	 <other>true</other> if the x- and y-coordinates are equal;
	 the Z coordinates do not have to be equal.
	 
	*/
	public final boolean Equals2D(Coordinate other)
	{
		return mX == other.mX && mY == other.mY;
	}

	/** 
	 Tests if another coordinate has the same value for X and Y, within a tolerance.
	 
	 @param c A <see cref="Coordinate"/>.
	 @param tolerance The tolerance value.
	 @return <c>true</c> if the X and Y ordinates are within the given tolerance.
	 The Z ordinate is ignored.
	*/
	public final boolean Equals2D(Coordinate c, double tolerance)
	{
		if (!EqualsWithTolerance(mX, c.mX, tolerance))
		{
			return false;
		}
		if (!EqualsWithTolerance(mY, c.mY, tolerance))
		{
			return false;
		}
		return true;
	}

	private static boolean EqualsWithTolerance(double x1, double x2, double tolerance)
	{
		return Math.abs(x1 - x2) <= tolerance;
	}

	/** 
	 Returns <other>true</other> if <other>other</other> has the same values for the x and y ordinates.
	 Since Coordinates are 2.5D, this routine ignores the z value when making the comparison.
	 
	 @param other <other>Coordinate</other> with which to do the comparison.
	 @return <other>true</other> if <other>other</other> is a <other>Coordinate</other> with the same values for the x and y ordinates.
	*/
	@Override
	public boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		Coordinate otherC = other instanceof Coordinate ? (Coordinate)other : null;
		if (otherC != null)
		{
			return equals(otherC);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning disable 612,618
		if (!(other instanceof ICoordinate))
		{
			return false;
		}
		return ((ICoordinate)this).equals((ICoordinate)other);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning restore 612,618
	}

	/** 
	
	 
	 @param other
	 @return 
	*/
	public final boolean equals(Coordinate other)
	{
		return Equals2D(other);
	}

	///// <summary>
	/////
	///// </summary>
	///// <param name="obj1"></param>
	///// <param name="obj2"></param>
	///// <returns></returns>
	//public static bool operator ==(Coordinate obj1, ICoordinate obj2)
	//{
	//    return Equals(obj1, obj2);
	//}

	///// <summary>
	/////
	///// </summary>
	///// <param name="obj1"></param>
	///// <param name="obj2"></param>
	///// <returns></returns>
	//public static bool operator !=(Coordinate obj1, ICoordinate obj2)
	//{
	//    return !(obj1 == obj2);
	//}

	/** 
	 Compares this object with the specified object for order.
	 Since Coordinates are 2.5D, this routine ignores the z value when making the comparison.
	 Returns
	   -1  : this.x lowerthan other.x || ((this.x == other.x) AND (this.y lowerthan other.y))
		0  : this.x == other.x AND this.y = other.y
		1  : this.x greaterthan other.x || ((this.x == other.x) AND (this.y greaterthan other.y))
	 
	 @param o <other>Coordinate</other> with which this <other>Coordinate</other> is being compared.
	 @return 
	 A negative integer, zero, or a positive integer as this <other>Coordinate</other>
			 is less than, equal to, or greater than the specified <other>Coordinate</other>.
	 
	*/

	/** 
	 Compares this object with the specified object for order.
	 Since Coordinates are 2.5D, this routine ignores the z value when making the comparison.
	 Returns
	   -1  : this.x lowerthan other.x || ((this.x == other.x) AND (this.y lowerthan other.y))
		0  : this.x == other.x AND this.y = other.y
		1  : this.x greaterthan other.x || ((this.x == other.x) AND (this.y greaterthan other.y))
	 
	 @param other <other>Coordinate</other> with which this <other>Coordinate</other> is being compared.
	 @return 
	 A negative integer, zero, or a positive integer as this <other>Coordinate</other>
			 is less than, equal to, or greater than the specified <other>Coordinate</other>.
	 
	*/

	public final int compareTo(Coordinate other)
	{
		if (mX < other.mX)
		{
			return -1;
		}
		if (mX > other.mX)
		{
			return 1;
		}
		if (mY < other.mY)
		{
			return -1;
		}
		return mY > other.mY ? 1 : 0;
	}

	/** 
	 Returns <c>true</c> if <paramref name="other"/> 
	 has the same values for X, Y and Z.
	 
	 @param other A <see cref="Coordinate"/> with which to do the 3D comparison.
	 @return 
	 <c>true</c> if <paramref name="other"/> is a <see cref="Coordinate"/> 
	 with the same values for X, Y and Z.
	 
	*/
	public final boolean Equals3D(Coordinate other)
	{
		return (mX == other.mX) && (mY == other.mY) && ((mZ == other.mZ) || (Double.isNaN(mZ) && Double.isNaN(other.mZ)));
	}

	/** 
	 Tests if another coordinate has the same value for Z, within a tolerance.
	 
	 @param c A <see cref="Coordinate"/>.
	 @param tolerance The tolerance value.
	 @return <c>true</c> if the Z ordinates are within the given tolerance.
	*/
	public final boolean EqualInZ(Coordinate c, double tolerance)
	{
		return EqualsWithTolerance(this.mZ, c.mZ, tolerance);
	}

	/** 
	 Returns a <other>string</other> of the form <I>(x,y,z)</I> .
	 
	 @return <other>string</other> of the form <I>(x,y,z)</I>
	*/
	@Override
	public String toString()
	{
		return "("+(mX)+", "+ mY +", "+(mZ)+")";
	}

	/** 
	 Create a new object as copy of this instance.
	 
	 @return 
	*/
	public Coordinate Copy()
	{
		return new Coordinate(mX, mY, mZ);
	}

	/** 
	 Create a new object as copy of this instance.
	 
	 @return 
	*/
	@Deprecated
	public final Object Clone() throws CloneNotSupportedException {
		return clone();
	}

	/** 
	 Computes the 2-dimensional Euclidean distance to another location.
	 
	 @param c A <see cref="Coordinate"/> with which to do the distance comparison.
	 @return the 2-dimensional Euclidean distance between the locations.
	 The Z-ordinate is ignored.
	*/
	public final double Distance(Coordinate c)
	{
		double dx = mX - c.mX;
		double dy = mY - c.mY;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/** 
	 Computes the 3-dimensional Euclidean distance to another location.
	 
	 @param c A <see cref="Coordinate"/> with which to do the distance comparison.
	 @return the 3-dimensional Euclidean distance between the locations.
	*/
	public final double Distance3D(Coordinate c)
	{
		double dx = mX - c.mX;
		double dy = mY - c.mY;
		double dz = mZ - c.mZ;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/** 
	 Gets a hashcode for this coordinate.
	 
	 @return A hashcode for this coordinate.
	*/
	@Override
	public int hashCode()
	{
		int result = 17;
		// ReSharper disable NonReadonlyFieldInGetHashCode
		result = 37 * result + GetHashCode(mX);
		result = 37 * result + GetHashCode(mY);
		// ReSharper restore NonReadonlyFieldInGetHashCode
		return result;
	}

	/** 
	 Computes a hash code for a double value, using the algorithm from
	 Joshua Bloch's book <i>Effective Java"</i>
	 
	 @param value A hashcode for the double value
	*/
	public static int GetHashCode(double value)
	{
		return (new Double(value)).hashCode();

		// This was implemented as follows, but that's actually equivalent:
		/*
		var f = BitConverter.DoubleToInt64Bits(value);
		return (int)(f ^ (f >> 32));
		*/
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region ICoordinate

	/** 
	 X coordinate.
	*/
	@Deprecated
	public final double getX()
	{
		return mX;
	}
	@Deprecated
	public final void setX(double value)
	{
		mX = value;
	}

	/** 
	 Y coordinate.
	*/
	@Deprecated
	public final double getY()
	{
		return mY;
	}
	@Deprecated
	public final void setY(double value)
	{
		mY = value;
	}

	/** 
	 Z coordinate.
	*/
	@Deprecated
	public final double getZ()
	{
		return mZ;
	}
	@Deprecated
	public final void setZ(double value)
	{
		mZ = value;
	}

	/** 
	 The measure value
	*/
	@Deprecated
	public final double getM()
	{
		return NullOrdinate;
	}
	@Deprecated
	public final void setM(double value)
	{
	}


	@Deprecated
	public final void setCoordinateValue(ICoordinate value)
	{
		mX = value.getX();
		mY = value.getY();
		mZ = value.getZ();
	}

	/** 
	 Gets/Sets the ordinate value for a given index
	 
	 @param index The index of the ordinate
	 @return The ordinate value
	*/
	@Deprecated
	public final double get(Ordinate index)
	{
		switch (index)
		{
			case X:
				return mX;
			case Y:
				return mY;
			case Z:
				return mZ;
			default:
				return NullOrdinate;
		}
	}
	@Deprecated
	public final void set(Ordinate index, double value)
	{
		switch (index)
		{
			case X:
				mX = value;
				break;
			case Y:
				mY = value;
				break;
			case Z:
				mZ = value;
				break;
		}
	}

	/** 
	 Returns whether the planar projections of the two <other>Coordinate</other>s are equal.
	
	 @param other <other>Coordinate</other> with which to do the 2D comparison.
	 @return 
	 <other>true</other> if the x- and y-coordinates are equal;
	 the Z coordinates do not have to be equal.
	 
	*/
	@Deprecated
	public final boolean Equals2D(ICoordinate other)
	{
		return mX == other.getX() && mY == other.getY();
	}

	/** 
	 Compares this object with the specified object for order.
	 Since Coordinates are 2.5D, this routine ignores the z value when making the comparison.
	 Returns
	   -1  : this.x lowerthan other.x || ((this.x == other.x) AND (this.y lowerthan other.y))
		0  : this.x == other.x AND this.y = other.y
		1  : this.x greaterthan other.x || ((this.x == other.x) AND (this.y greaterthan other.y))
	 
	 @param other <other>Coordinate</other> with which this <other>Coordinate</other> is being compared.
	 @return 
	 A negative integer, zero, or a positive integer as this <other>Coordinate</other>
			 is less than, equal to, or greater than the specified <other>Coordinate</other>.

	*/


	public  int compareTo(ICoordinate other)
	{
		if (mX < other.getX())
		{
			return -1;
		}
		if (mX > other.getX())
		{
			return 1;
		}
		if (mY < other.getY())
		{
			return -1;
		}
		return mY > other.getY() ? 1 : 0;
	}

	/** 
	 Compares this object with the specified object for order.
	 Since Coordinates are 2.5D, this routine ignores the z value when making the comparison.
	 Returns
	   -1  : this.x lowerthan other.x || ((this.x == other.x) AND (this.y lowerthan other.y))
		0  : this.x == other.x AND this.y = other.y
		1  : this.x greaterthan other.x || ((this.x == other.x) AND (this.y greaterthan other.y))
	 
	 @param o <other>Coordinate</other> with which this <other>Coordinate</other> is being compared.
	 @return 
	 A negative integer, zero, or a positive integer as this <other>Coordinate</other>
			 is less than, equal to, or greater than the specified <other>Coordinate</other>.
	 
	*/
//	public final int compareTo(Object o)
//	{
//		Coordinate other = (Coordinate)o;
//		return compareTo(other);
//	}

	/** 
	 Returns <other>true</other> if <other>other</other> has the same values for x, y and z.
	 
	 @param other <other>Coordinate</other> with which to do the 3D comparison.
	 @return <other>true</other> if <other>other</other> is a <other>Coordinate</other> with the same values for x, y and z.
	*/
	@Deprecated
	public final boolean Equals3D(ICoordinate other)
	{
		return (mX == other.getX()) && (mY == other.getY()) && ((mZ == other.getZ()) || (Double.isNaN(mZ) && Double.isNaN(other.getZ())));
	}

	/** 
	 Computes the 2-dimensional Euclidean distance to another location.
	 The Z-ordinate is ignored.
	 
	 @param p <other>Coordinate</other> with which to do the distance comparison.
	 @return the 2-dimensional Euclidean distance between the locations
	*/
	@Deprecated
	public final double Distance(ICoordinate p)
	{
		double dx = mX - p.getX();
		double dy = mY - p.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion ICoordinate

	/* BEGIN ADDED BY MPAUL42: monoGIS team */

	///// <summary>
	///// Overloaded + operator.
	///// </summary>
	//public static Coordinate operator +(Coordinate coord1, Coordinate coord2)
	//{
	//    return new Coordinate(coord1.X + coord2.X, coord1.Y + coord2.Y, coord1.Z + coord2.Z);
	//}

	///// <summary>
	///// Overloaded + operator.
	///// </summary>
	//public static Coordinate operator +(Coordinate coord1, double d)
	//{
	//    return new Coordinate(coord1.X + d, coord1.Y + d, coord1.Z + d);
	//}

	///// <summary>
	///// Overloaded + operator.
	///// </summary>
	//public static Coordinate operator +(double d, Coordinate coord1)
	//{
	//    return coord1 + d;
	//}

	///// <summary>
	///// Overloaded * operator.
	///// </summary>
	//public static Coordinate operator *(Coordinate coord1, Coordinate coord2)
	//{
	//    return new Coordinate(coord1.X * coord2.X, coord1.Y * coord2.Y, coord1.Z * coord2.Z);
	//}

	///// <summary>
	///// Overloaded * operator.
	///// </summary>
	//public static Coordinate operator *(Coordinate coord1, double d)
	//{
	//    return new Coordinate(coord1.X * d, coord1.Y * d, coord1.Z * d);
	//}

	///// <summary>
	///// Overloaded * operator.
	///// </summary>
	//public static Coordinate operator *(double d, Coordinate coord1)
	//{
	//    return coord1 * d;
	//}

	///// <summary>
	///// Overloaded - operator.
	///// </summary>
	//public static Coordinate operator -(Coordinate coord1, Coordinate coord2)
	//{
	//    return new Coordinate(coord1.X - coord2.X, coord1.Y - coord2.Y, coord1.Z - coord2.Z);
	//}

	///// <summary>
	///// Overloaded - operator.
	///// </summary>
	//public static Coordinate operator -(Coordinate coord1, double d)
	//{
	//    return new Coordinate(coord1.X - d, coord1.Y - d, coord1.Z - d);
	//}

	///// <summary>
	///// Overloaded - operator.
	///// </summary>
	//public static Coordinate operator -(double d, Coordinate coord1)
	//{
	//    return coord1 - d;
	//}

	///// <summary>
	///// Overloaded / operator.
	///// </summary>
	//public static Coordinate operator /(Coordinate coord1, Coordinate coord2)
	//{
	//    return new Coordinate(coord1.X / coord2.X, coord1.Y / coord2.Y, coord1.Z / coord2.Z);
	//}

	///// <summary>
	///// Overloaded / operator.
	///// </summary>
	//public static Coordinate operator /(Coordinate coord1, double d)
	//{
	//    return new Coordinate(coord1.X / d, coord1.Y / d, coord1.Z / d);
	//}

	///// <summary>
	///// Overloaded / operator.
	///// </summary>
	//public static Coordinate operator /(double d, Coordinate coord1)
	//{
	//    return coord1 / d;
	//}

	/* END ADDED BY MPAUL42: monoGIS team */
}