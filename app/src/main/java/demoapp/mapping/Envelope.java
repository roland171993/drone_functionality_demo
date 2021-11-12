package demoapp.mapping;



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_ICLONEABLE
//#else
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter could not confirm whether this is a namespace alias or a type alias:
//using ICloneable = GeoAPI.ICloneable;
//#endif

/**
 Defines a rectangular region of the 2D coordinate plane.
 It is often used to represent the bounding box of a <c>Geometry</c>,
 e.g. the minimum and maximum x and y values of the <c>Coordinate</c>s.
 Note that Envelopes support infinite or half-infinite regions, by using the values of
 <c>Double.PositiveInfinity</c> and <c>Double.NegativeInfinity</c>.
 When Envelope objects are created or initialized,
 the supplies extent values are automatically sorted into the correct order.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning disable 612,618
public class Envelope implements IEnvelope, IIntersectable<Envelope>
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning restore 612,618
{
	/** 
	 Test the point q to see whether it intersects the Envelope
	 defined by p1-p2.
	 
	 @param p1 One extremal point of the envelope.
	 @param p2 Another extremal point of the envelope.
	 @param q Point to test for intersection.
	 @return <c>true</c> if q intersects the envelope p1-p2.
	*/
	public static boolean Intersects(Coordinate p1, Coordinate p2, Coordinate q)
	{
		return ((q.mX >= (p1.mX < p2.mX ? p1.mX : p2.mX)) && (q.mX <= (p1.mX > p2.mX ? p1.mX : p2.mX))) && ((q.mY >= (p1.mY < p2.mY ? p1.mY : p2.mY)) && (q.mY <= (p1.mY > p2.mY ? p1.mY : p2.mY)));
	}

	/** 
	 Tests whether the envelope defined by p1-p2
	 and the envelope defined by q1-q2
	 intersect.
	 
	 @param p1 One extremal point of the envelope Point.
	 @param p2 Another extremal point of the envelope Point.
	 @param q1 One extremal point of the envelope Q.
	 @param q2 Another extremal point of the envelope Q.
	 @return <c>true</c> if Q intersects Point
	*/
	public static boolean Intersects(Coordinate p1, Coordinate p2, Coordinate q1, Coordinate q2)
	{
		double minp = Math.min(p1.mX, p2.mX);
		double maxq = Math.max(q1.mX, q2.mX);
		if (minp > maxq)
		{
			return false;
		}

		double minq = Math.min(q1.mX, q2.mX);
		double maxp = Math.max(p1.mX, p2.mX);
		if (maxp < minq)
		{
			return false;
		}

		minp = Math.min(p1.mY, p2.mY);
		maxq = Math.max(q1.mY, q2.mY);
		if (minp > maxq)
		{
			return false;
		}

		minq = Math.min(q1.mY, q2.mY);
		maxp = Math.max(p1.mY, p2.mY);
		if (maxp < minq)
		{
			return false;
		}

		return true;
	}

	/*
	*  the minimum x-coordinate
	*/
	private double _minx;

	/*
	*  the maximum x-coordinate
	*/
	private double _maxx;

	/*
	* the minimum y-coordinate
	*/
	private double _miny;

	/*
	*  the maximum y-coordinate
	*/
	private double _maxy;

	/** 
	 Creates a null <c>Envelope</c>.
	*/
	public Envelope()
	{
		InitSetToNull();
	}

	/** 
	 Creates an <c>Envelope</c> for a region defined by maximum and minimum values.
	 
	 @param x1 The first x-value.
	 @param x2 The second x-value.
	 @param y1 The first y-value.
	 @param y2 The second y-value.
	*/
	public Envelope(double x1, double x2, double y1, double y2)
	{
		Init(x1, x2, y1, y2);
	}





	/** 
	 Initialize to a null <c>Envelope</c>.
	*/
	public final void InitSetToNull()
	{
		SetToNull();
	}

	/** 
	 Initialize an <c>Envelope</c> for a region defined by maximum and minimum values.
	 
	 @param x1 The first x-value.
	 @param x2 The second x-value.
	 @param y1 The first y-value.
	 @param y2 The second y-value.
	*/
	public final void Init(double x1, double x2, double y1, double y2)
	{
		if (x1 < x2)
		{
			_minx = x1;
			_maxx = x2;
		}
		else
		{
			_minx = x2;
			_maxx = x1;
		}

		if (y1 < y2)
		{
			_miny = y1;
			_maxy = y2;
		}
		else
		{
			_miny = y2;
			_maxy = y1;
		}
	}



	/** 
	 Initialize an <c>Envelope</c> from an existing Envelope.
	 
	 @param env The Envelope to initialize from.
	*/
	public final void Init(Envelope env)
	{
		_minx = env.getMinX();
		_maxx = env.getMaxX();
		_miny = env.getMinY();
		_maxy = env.getMaxY();
	}

	/** 
	 Makes this <c>Envelope</c> a "null" envelope..
	*/
	public final void SetToNull()
	{
		_minx = 0;
		_maxx = -1;
		_miny = 0;
		_maxy = -1;
	}

	/** 
	 Returns <c>true</c> if this <c>Envelope</c> is a "null" envelope.
	 
	 @return 
	 <c>true</c> if this <c>Envelope</c> is uninitialized
	 or is the envelope of the empty point.
	 
	*/
	public final boolean getIsNull()
	{
		return _maxx < _minx;
	}

	/** 
	 Returns the difference between the maximum and minimum x values.
	 
	 @return max x - min x, or 0 if this is a null <c>Envelope</c>.
	*/
	public final double getWidth()
	{
		if (getIsNull())
		{
			return 0;
		}
		return _maxx - _minx;
	}

	/** 
	 Returns the difference between the maximum and minimum y values.
	 
	 @return max y - min y, or 0 if this is a null <c>Envelope</c>.
	*/
	public final double getHeight()
	{
		if (getIsNull())
		{
			return 0;
		}
		return _maxy - _miny;
	}

	/** 
	 Returns the <c>Envelope</c>s minimum x-value. min x > max x
	 indicates that this is a null <c>Envelope</c>.
	 
	 @return The minimum x-coordinate.
	*/
	public final double getMinX()
	{
		return _minx;
	}

	/** 
	 Returns the <c>Envelope</c>s maximum x-value. min x > max x
	 indicates that this is a null <c>Envelope</c>.
	 
	 @return The maximum x-coordinate.
	*/
	public final double getMaxX()
	{
		return _maxx;
	}

	/** 
	 Returns the <c>Envelope</c>s minimum y-value. min y > max y
	 indicates that this is a null <c>Envelope</c>.
	 
	 @return The minimum y-coordinate.
	*/
	public final double getMinY()
	{
		return _miny;
	}

	/** 
	 Returns the <c>Envelope</c>s maximum y-value. min y > max y
	 indicates that this is a null <c>Envelope</c>.
	 
	 @return The maximum y-coordinate.
	*/
	public final double getMaxY()
	{
		return _maxy;
	}

	/** 
	 Gets the area of this envelope.
	 
	 @return The area of the envelope, or 0.0 if envelope is null
	*/
	public final double getArea()
	{
		return getWidth() * getHeight();
	}

	/** 
	 Expands this envelope by a given distance in all directions.
	 Both positive and negative distances are supported.
	 
	 @param distance The distance to expand the envelope.
	*/
	public final void ExpandBy(double distance)
	{
		ExpandBy(distance, distance);
	}

	/** 
	 Expands this envelope by a given distance in all directions.
	 Both positive and negative distances are supported.
	 
	 @param deltaX The distance to expand the envelope along the the X axis.
	 @param deltaY The distance to expand the envelope along the the Y axis.
	*/
	public final void ExpandBy(double deltaX, double deltaY)
	{
		if (getIsNull())
		{
			return;
		}

		_minx -= deltaX;
		_maxx += deltaX;
		_miny -= deltaY;
		_maxy += deltaY;

		// check for envelope disappearing
		if (_minx > _maxx || _miny > _maxy)
		{
			SetToNull();
		}
	}

	/** 
	 Gets the minimum extent of this envelope across both dimensions.
	 
	 @return 
	*/
	public final double getMinExtent()
	{
		if (getIsNull())
		{
			return 0.0;
		}
		double w = getWidth();
		double h = getHeight();
		if (w < h)
		{
			return w;
		}
		return h;
	}

	/** 
	 Gets the maximum extent of this envelope across both dimensions.
	 
	 @return 
	*/
	public final double getMaxExtent()
	{
		if (getIsNull())
		{
			return 0.0;
		}
		double w = getWidth();
		double h = getHeight();
		if (w > h)
		{
			return w;
		}
		return h;
	}

	/** 
	 Enlarges this <code>Envelope</code> so that it contains
	 the given <see cref="Coordinate"/>.
	 Has no effect if the point is already on or within the envelope.
	 
	 @param p The Coordinate.
	*/
	public final void ExpandToInclude(Coordinate p)
	{
		ExpandToInclude(p.mX, p.mY);
	}

	/** 
	 Enlarges this <c>Envelope</c> so that it contains
	 the given <see cref="Coordinate"/>.
	 
	 Has no effect if the point is already on or within the envelope.
	 @param x The value to lower the minimum x to or to raise the maximum x to.
	 @param y The value to lower the minimum y to or to raise the maximum y to.
	*/
	public final void ExpandToInclude(double x, double y)
	{
		if (getIsNull())
		{
			_minx = x;
			_maxx = x;
			_miny = y;
			_maxy = y;
		}
		else
		{
			if (x < _minx)
			{
				_minx = x;
			}
			if (x > _maxx)
			{
				_maxx = x;
			}
			if (y < _miny)
			{
				_miny = y;
			}
			if (y > _maxy)
			{
				_maxy = y;
			}
		}
	}

	/** 
	 Enlarges this <c>Envelope</c> so that it contains
	 the <c>other</c> Envelope.
	 Has no effect if <c>other</c> is wholly on or
	 within the envelope.
	 
	 @param other the <c>Envelope</c> to expand to include.
	*/
	public final void ExpandToInclude(Envelope other)
	{
		if (other.getIsNull())
		{
			return;
		}
		if (getIsNull())
		{
			_minx = other.getMinX();
			_maxx = other.getMaxX();
			_miny = other.getMinY();
			_maxy = other.getMaxY();
		}
		else
		{
			if (other.getMinX() < _minx)
			{
				_minx = other.getMinX();
			}
			if (other.getMaxX() > _maxx)
			{
				_maxx = other.getMaxX();
			}
			if (other.getMinY() < _miny)
			{
				_miny = other.getMinY();
			}
			if (other.getMaxY() > _maxy)
			{
				_maxy = other.getMaxY();
			}
		}
	}

	/** 
	 Enlarges this <c>Envelope</c> so that it contains
	 the <c>other</c> Envelope.
	 Has no effect if <c>other</c> is wholly on or
	 within the envelope.
	 
	 @param other the <c>Envelope</c> to expand to include.
	*/
	public final Envelope ExpandedBy(Envelope other)
	{
		if (other.getIsNull())
		{
			return this;
		}
		if (getIsNull())
		{
			return other;
		}

		double minx = (other._minx < _minx) ? other._minx : _minx;
		double maxx = (other._maxx > _maxx) ? other._maxx : _maxx;
		double miny = (other._miny < _miny) ? other._miny : _miny;
		double maxy = (other._maxy > _maxy) ? other._maxy : _maxy;
		return new Envelope(minx, maxx, miny, maxy);
	}
	/** 
	 Translates this envelope by given amounts in the X and Y direction.
	 
	 @param transX The amount to translate along the X axis.
	 @param transY The amount to translate along the Y axis.
	*/
	public final void Translate(double transX, double transY)
	{
		if (getIsNull())
		{
			return;
		}
		Init(getMinX() + transX, getMaxX() + transX, getMinY() + transY, getMaxY() + transY);
	}

	/** 
	 Computes the coordinate of the centre of this envelope (as long as it is non-null).
	 
	 @return 
	 The centre coordinate of this envelope,
	 or <c>null</c> if the envelope is null.
	 .
	*/
	public final Coordinate getCentreObj()
	{
		return getIsNull() ? null : new Coordinate((getMinX() + getMaxX()) / 2.0, (getMinY() + getMaxY()) / 2.0);
	}

	/** 
	 Computes the intersection of two <see cref="Envelope"/>s.
	 
	 @param env The envelope to intersect with
	 @return 
	 A new Envelope representing the intersection of the envelopes (this will be
	 the null envelope if either argument is null, or they do not intersect
	 
	*/
	public final Envelope Intersection(Envelope env)
	{
		if (getIsNull() || env.getIsNull() || !Intersects(env))
		{
			return new Envelope();
		}

		return new Envelope(Math.max(getMinX(), env.getMinX()), Math.min(getMaxX(), env.getMaxX()), Math.max(getMinY(), env.getMinY()), Math.min(getMaxY(), env.getMaxY()));
	}

	/** 
	 Check if the region defined by <c>other</c>
	 intersects the region of this <c>Envelope</c>.
	 
	 @param other The <c>Envelope</c> which this <c>Envelope</c> is
	 being checked for intersecting.
	 
	 @return 
	 <c>true</c> if the <c>Envelope</c>s intersect.
	 
	*/
	public final boolean Intersects(Envelope other)
	{
		if (getIsNull() || other.getIsNull())
		{
			return false;
		}
		return !(other.getMinX() > _maxx || other.getMaxX() < _minx || other.getMinY() > _maxy || other.getMaxY() < _miny);
	}

	/** 
	 Use Intersects instead. In the future, Overlaps may be
	 changed to be a true overlap check; that is, whether the intersection is
	 two-dimensional.
	 
	 @param other
	 @return 
	*/
	@Deprecated
	public final boolean Overlaps(Envelope other)
	{
		return Intersects(other);
	}

	/** 
	 Use Intersects instead.
	 
	 @param p
	 @return 
	*/
	@Deprecated
	public final boolean Overlaps(Coordinate p)
	{
		return Intersects(p);
	}

	/** 
	 Use Intersects instead.
	 
	 @param x
	 @param y
	 @return 
	*/
	@Deprecated
	public final boolean Overlaps(double x, double y)
	{
		return Intersects(x, y);
	}

	/** 
	 Check if the point <c>p</c> overlaps (lies inside) the region of this <c>Envelope</c>.
	 
	 @param p the <c>Coordinate</c> to be tested.
	 @return <c>true</c> if the point overlaps this <c>Envelope</c>.
	*/
	public final boolean Intersects(Coordinate p)
	{
		return Intersects(p.mX, p.mY);
	}

	/** 
	 Check if the point <c>(x, y)</c> overlaps (lies inside) the region of this <c>Envelope</c>.
	 
	 @param x the x-ordinate of the point.
	 @param y the y-ordinate of the point.
	 @return <c>true</c> if the point overlaps this <c>Envelope</c>.
	*/
	public final boolean Intersects(double x, double y)
	{
		return !(x > _maxx || x < _minx || y > _maxy || y < _miny);
	}

	/** 
	 Check if the extent defined by two extremal points
	 intersects the extent of this <code>Envelope</code>.
	 
	 @param a A point
	 @param b Another point
	 @return <c>true</c> if the extents intersect
	*/
	public final boolean Intersects(Coordinate a, Coordinate b)
	{
		if (getIsNull())
		{
			return false;
		}

		double envminx = (a.mX < b.mX) ? a.mX : b.mX;
		if (envminx > _maxx)
		{
			return false;
		}

		double envmaxx = (a.mX > b.mX) ? a.mX : b.mX;
		if (envmaxx < _minx)
		{
			return false;
		}

		double envminy = (a.mY < b.mY) ? a.mY : b.mY;
		if (envminy > _maxy)
		{
			return false;
		}

		double envmaxy = (a.mY > b.mY) ? a.mY : b.mY;
		if (envmaxy < _miny)
		{
			return false;
		}

		return true;
	}

	/**
	 Tests if the <c>Envelope other</c> lies wholely inside this <c>Envelope</c> (inclusive of the boundary).
	
	 
	 Note that this is <b>not</b> the same definition as the SFS <i>contains</i>,
	 which would exclude the envelope boundary.
	 
	 <p>The <c>Envelope</c> to check</p>
	 @return true if <c>other</c> is contained in this <c>Envelope</c>
	 <see cref="Covers(Envelope)"/>
	*/
	public final boolean Contains(Envelope other)
	{
		return Covers(other);
	}

	/**
	 Tests if the given point lies in or on the envelope.
	
	 
	 Note that this is <b>not</b> the same definition as the SFS <i>contains</i>,
	 which would exclude the envelope boundary.
	 
	 @param p the point which this <c>Envelope</c> is being checked for containing
	 @return <c>true</c> if the point lies in the interior or on the boundary of this <c>Envelope</c>. 
	 <see cref="Covers(Coordinate)"/>
	*/
	public final boolean Contains(Coordinate p)
	{
		return Covers(p);
	}

	/**
	 Tests if the given point lies in or on the envelope.
	
	 
	 Note that this is <b>not</b> the same definition as the SFS <i>contains</i>, which would exclude the envelope boundary.
	 
	 @param x the x-coordinate of the point which this <c>Envelope</c> is being checked for containing
	 @param y the y-coordinate of the point which this <c>Envelope</c> is being checked for containing
	 @return 
	 <c>true</c> if <c>(x, y)</c> lies in the interior or on the boundary of this <c>Envelope</c>.
	 
	 <see cref="Covers(double, double)"/>
	*/
	public final boolean Contains(double x, double y)
	{
		return Covers(x, y);
	}

	/**
	 Tests if the given point lies in or on the envelope.
	
	 @param x the x-coordinate of the point which this <c>Envelope</c> is being checked for containing
	 @param y the y-coordinate of the point which this <c>Envelope</c> is being checked for containing
	 @return  <c>true</c> if <c>(x, y)</c> lies in the interior or on the boundary of this <c>Envelope</c>.
	*/
	public final boolean Covers(double x, double y)
	{
		if (getIsNull())
		{
			return false;
		}
		return x >= _minx && x <= _maxx && y >= _miny && y <= _maxy;
	}

	/**
	 Tests if the given point lies in or on the envelope.
	
	 @param p the point which this <c>Envelope</c> is being checked for containing
	 @return <c>true</c> if the point lies in the interior or on the boundary of this <c>Envelope</c>.
	*/
	public final boolean Covers(Coordinate p)
	{
		return Covers(p.mX, p.mY);
	}

	/**
	 Tests if the <c>Envelope other</c> lies wholely inside this <c>Envelope</c> (inclusive of the boundary).
	
	 @param other the <c>Envelope</c> to check
	 @return true if this <c>Envelope</c> covers the <c>other</c>
	*/
	public final boolean Covers(Envelope other)
	{
		if (getIsNull() || other.getIsNull())
		{
			return false;
		}
		return other.getMinX() >= _minx && other.getMaxX() <= _maxx && other.getMinY() >= _miny && other.getMaxY() <= _maxy;
	}

	/** 
	 Computes the distance between this and another
	 <c>Envelope</c>.
	 The distance between overlapping Envelopes is 0.  Otherwise, the
	 distance is the Euclidean distance between the closest points.
	 
	 @return The distance between this and another <c>Envelope</c>.
	*/
	public final double Distance(Envelope env)
	{
		if (Intersects(env))
		{
			return 0;
		}

		double dx = 0.0;

		if (_maxx < env.getMinX())
		{
			dx = env.getMinX() - _maxx;
		}
		else if (_minx > env.getMaxX())
		{
			dx = _minx - env.getMaxX();
		}

		double dy = 0.0;

		if (_maxy < env.getMinY())
		{
			dy = env.getMinY() - _maxy;
		}
		else if (_miny > env.getMaxY())
		{
			dy = _miny - env.getMaxY();
		}

		// if either is zero, the envelopes overlap either vertically or horizontally
		if (dx == 0.0)
		{
			return dy;
		}
		if (dy == 0.0)
		{
			return dx;
		}

		return Math.sqrt(dx * dx + dy * dy);
	}

	/** <inheritdoc/>
	*/
	@Override
	public boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}

		Envelope otherE = other instanceof Envelope ? (Envelope)other : null;
		if (otherE != null)
		{
			return equals(otherE);
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning disable 612,618
		if (!(other instanceof IEnvelope))
		{
			return false;
		}

		return ((IEnvelope)this).equals((IEnvelope)other);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning restore 612,618
	}

	/** <inheritdoc/>
	*/
	public final boolean equals(Envelope other)
	{
		if (getIsNull())
		{
			return other.getIsNull();
		}

		return _maxx == other.getMaxX() && _maxy == other.getMaxY() && _minx == other.getMinX() && _miny == other.getMinY();
	}

	/**
	 Compares two envelopes using lexicographic ordering.
	 The ordering comparison is based on the usual numerical
	 comparison between the sequence of ordinates.
	 Null envelopes are less than all non-null envelopes.

	 @param other An envelope
	*/
//	public final int compareTo(Object other)
//	{
//		return compareTo((Envelope)other);
//	}

	/** 
	 Compares two envelopes using lexicographic ordering.
	 The ordering comparison is based on the usual numerical
	 comparison between the sequence of ordinates.
	 Null envelopes are less than all non-null envelopes.
	 
	 @param env An envelope
	*/
//	public final int compareTo(Envelope env)
//	{
//		env = env != null ? env : new Envelope();
//
//		// compare nulls if present
//		if (getIsNull())
//		{
//			if (env.getIsNull())
//			{
//				return 0;
//			}
//			return -1;
//		}
//		else
//		{
//			if (env.getIsNull())
//			{
//				return 1;
//			}
//		}
//
//		// compare based on numerical ordering of ordinates
//		if (getMinX() < env.getMinX())
//		{
//			return -1;
//		}
//		if (getMinX() > env.getMinX())
//		{
//			return 1;
//		}
//		if (getMinY() < env.getMinY())
//		{
//			return -1;
//		}
//		if (getMinY() > env.getMinY())
//		{
//			return 1;
//		}
//		if (getMaxX() < env.getMaxX())
//		{
//			return -1;
//		}
//		if (getMaxX() > env.getMaxX())
//		{
//			return 1;
//		}
//		if (getMaxY() < env.getMaxY())
//		{
//			return -1;
//		}
//		if (getMaxY() > env.getMaxY())
//		{
//			return 1;
//		}
//		return 0;
//	}

	/** <inheritdoc/>
	*/
	@Override
	public int hashCode()
	{
		int result = 17;
		// ReSharper disable NonReadonlyFieldInGetHashCode
		result = 37 * result + GetHashCode(_minx);
		result = 37 * result + GetHashCode(_maxx);
		result = 37 * result + GetHashCode(_miny);
		result = 37 * result + GetHashCode(_maxy);
		// ReSharper restore NonReadonlyFieldInGetHashCode
		return result;
	}

	private static int GetHashCode(double value)
	{
		return (new Double(value)).hashCode();

		// This was implemented as follows, but that's actually equivalent:
		/*
		var f = BitConverter.DoubleToInt64Bits(value);
		return (int)(f ^ (f >> 32));
		*/
	}

	//public static bool operator ==(Envelope obj1, Envelope obj2)
	//{
	//    return Equals(obj1, obj2);
	//}

	//public static bool operator !=(Envelope obj1, Envelope obj2)
	//{
	//    return !(obj1 == obj2);
	//}

		/** 
		 Function to get a textual representation of this envelope
		 
		 @return A textual representation of this envelope
		*/
	@Override
	public String toString()
	{
		return "Env["+_minx+" : "+_maxx+", "+_miny+" : "+_maxy+"]";

		//return "Env[" + _minx + " : " + _maxx + ", " + _miny + " : " + _maxy + "]";
	}

	/** 
	 Creates a new object that is a copy of the current instance.
	 
	 @return A new object that is a copy of this instance.
	*/
	public final Object Clone() throws CloneNotSupportedException {
		return clone();
	}

	/** 
	 Creates a deep copy of the current envelope.
	 
	 @return 
	*/
	public final Envelope Copy()
	{
		if (getIsNull())
		{
			// #179: This will create a new 'NULL' envelope
			return new Envelope();
		}
		return new Envelope(_minx, _maxx, _miny, _maxy);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region BEGIN ADDED BY MPAUL42: monoGIS team

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning disable 612,618

//	/**
//	 Creates a deep copy of the current envelope.
//
//	 @return
//	*/
//	@Deprecated
//	public final Envelope Clone()
//	{
//		return Copy();
//	}

	/** 
	 Calculates the union of the current box and the given point.
	*/
	public final IEnvelope Union(IPoint point)
	{
		return ((IEnvelope)this).Union(point.getCoordinate());
	}

	/** 
	 Calculates the union of the current box and the given coordinate.
	*/
	public final IEnvelope Union(ICoordinate coord)
	{
		IEnvelope env = Copy();
		env.ExpandToInclude(coord);
		return env;
	}

	/** 
	 Calculates the union of the current box and the given box.
	*/
	public final IEnvelope Union(IEnvelope box)
	{
		if (box.getIsNull())
		{
			return this;
		}
		if (getIsNull())
		{
			return box;
		}

		return new Envelope(Math.min(_minx, box.getMinX()), Math.max(_maxx, box.getMaxX()), Math.min(_miny, box.getMinY()), Math.max(_maxy, box.getMaxY()));
	}

	/** 
	 Moves the envelope to the indicated coordinate.
	 
	 @param centre The new centre coordinate.
	*/
	public final void SetCentre(ICoordinate centre)
	{
		((IEnvelope)this).SetCentre(centre, getWidth(), getHeight());
	}

	/** 
	 Moves the envelope to the indicated point.
	 
	 @param centre The new centre point.
	*/
	public final void SetCentre(IPoint centre)
	{
		((IEnvelope)this).SetCentre(centre.getCoordinate(), getWidth(), getHeight());
	}

	/** 
	 Resizes the envelope to the indicated point.
	 
	 @param width The new width.
	 @param height The new height.
	*/
	public final void SetCentre(double width, double height)
	{
		((IEnvelope)this).SetCentre(getCentreObj(), width, height);
	}

	/** 
	 Moves and resizes the current envelope.
	 
	 @param centre The new centre point.
	 @param width The new width.
	 @param height The new height.
	*/
	public final void SetCentre(IPoint centre, double width, double height)
	{
		((IEnvelope)this).SetCentre(centre, width, height);
	}

	/** 
	 Moves and resizes the current envelope.
	 
	 @param centre The new centre coordinate.
	 @param width The new width.
	 @param height The new height.
	*/
	public final void SetCentre(ICoordinate centre, double width, double height)
	{
		_minx = centre.getX() - (width / 2);
		_maxx = centre.getX() + (width / 2);
		_miny = centre.getY() - (height / 2);
		_maxy = centre.getY() + (height / 2);
	}

	/** 
	 Zoom the box.
	 Possible values are e.g. 50 (to zoom in a 50%) or -50 (to zoom out a 50%).
	 
	 @param perCent
	 Negative do Envelope smaller.
	 Positive do Envelope bigger.
	 
	 <example>
	  perCent = -50 compact the envelope a 50% (make it smaller).
	  perCent = 200 enlarge envelope by 2.
	 </example>
	*/
	public final void Zoom(double perCent)
	{
		double w = (getWidth() * perCent / 100);
		double h = (getHeight() * perCent / 100);
		((IEnvelope)this).SetCentre(w, h);
	}

	/* END ADDED BY MPAUL42: monoGIS team */

	/** 
	 Initialize to a null <c>Envelope</c>.
	*/
	public final void Init()
	{
		SetToNull();
	}

	/** 
	 Initialize an <c>Envelope</c> for a region defined by two Coordinates.
	 
	 @param p1 The first Coordinate.
	 @param p2 The second Coordinate.
	*/
	public final void Init(ICoordinate p1, ICoordinate p2)
	{
		Init(p1.getX(), p2.getX(), p1.getY(), p2.getY());
	}

	/** 
	 Initialize an <c>Envelope</c> for a region defined by a single Coordinate.
	 
	 @param p The Coordinate.
	*/
	public final void Init(ICoordinate p)
	{
		Init(p.getX(), p.getX(), p.getY(), p.getY());
	}

	/** 
	 Initialize an <c>Envelope</c> from an existing Envelope.
	 
	 @param env The Envelope to initialize from.
	*/
	public final void Init(IEnvelope env)
	{
		_minx = env.getMinX();
		_maxx = env.getMaxX();
		_miny = env.getMinY();
		_maxy = env.getMaxY();
	}

	/** 
	 Enlarges this <code>Envelope</code> so that it contains
	 the given <see cref="ICoordinate"/>.
	 Has no effect if the point is already on or within the envelope.
	 
	 @param p The Coordinate.
	*/
	public final void ExpandToInclude(ICoordinate p)
	{
		ExpandToInclude(p.getX(), p.getY());
	}

	/** 
	 Enlarges this <c>Envelope</c> so that it contains
	 the <c>other</c> Envelope.
	 Has no effect if <c>other</c> is wholly on or
	 within the envelope.
	 
	 @param other the <c>Envelope</c> to expand to include.
	*/
	public final void ExpandToInclude(IEnvelope other)
	{
		if (other.getIsNull())
		{
			return;
		}
		if (getIsNull())
		{
			_minx = other.getMinX();
			_maxx = other.getMaxX();
			_miny = other.getMinY();
			_maxy = other.getMaxY();
		}
		else
		{
			if (other.getMinX() < _minx)
			{
				_minx = other.getMinX();
			}
			if (other.getMaxX() > _maxx)
			{
				_maxx = other.getMaxX();
			}
			if (other.getMinY() < _miny)
			{
				_miny = other.getMinY();
			}
			if (other.getMaxY() > _maxy)
			{
				_maxy = other.getMaxY();
			}
		}
	}

	/** 
	 Computes the coordinate of the centre of this envelope (as long as it is non-null).
	 
	 @return 
	 The centre coordinate of this envelope,
	 or <c>null</c> if the envelope is null.
	 .
	*/
	public final ICoordinate getCentre()
	{
		if (getIsNull())
		{
			return null;
		}
		return new Coordinate((getMinX() + getMaxX()) / 2.0, (getMinY() + getMaxY()) / 2.0);
	}

	/** 
	 Computes the intersection of two <see cref="IEnvelope"/>s.
	 
	 @param env The envelope to intersect with
	 @return 
	 A new Envelope representing the intersection of the envelopes (this will be
	 the null envelope if either argument is null, or they do not intersect
	 
	*/
	public final IEnvelope Intersection(IEnvelope env)
	{
		if (getIsNull() || env.getIsNull() || !((IEnvelope)this).Intersects(env))
		{
			return new Envelope();
		}

		return new Envelope(Math.max(getMinX(), env.getMinX()), Math.min(getMaxX(), env.getMaxX()), Math.max(getMinY(), env.getMinY()), Math.min(getMaxY(), env.getMaxY()));
	}

	/** 
	 Check if the region defined by <c>other</c>
	 overlaps (intersects) the region of this <c>Envelope</c>.
	 
	 @param other the <c>Envelope</c> which this <c>Envelope</c> is
	 being checked for overlapping.
	 
	 @return 
	 <c>true</c> if the <c>Envelope</c>s overlap.
	 
	*/
	public final boolean Intersects(IEnvelope other)
	{
		if (getIsNull() || other.getIsNull())
		{
			return false;
		}
		return !(other.getMinX() > _maxx || other.getMaxX() < _minx || other.getMinY() > _maxy || other.getMaxY() < _miny);
	}

	/** 
	 Use Intersects instead. In the future, Overlaps may be
	 changed to be a true overlap check; that is, whether the intersection is
	 two-dimensional.
	 
	 @param other
	 @return 
	*/
	@Deprecated
	public final boolean Overlaps(IEnvelope other)
	{
		return ((IEnvelope)this).Intersects(other);
	}

	/** 
	 Use Intersects instead.
	 
	 @param p
	 @return 
	*/
	@Deprecated
	public final boolean Overlaps(ICoordinate p)
	{
		return ((IEnvelope)this).Intersects(p);
	}

	/** 
	 Check if the point <c>p</c> overlaps (lies inside) the region of this <c>Envelope</c>.
	 
	 @param p the <c>Coordinate</c> to be tested.
	 @return <c>true</c> if the point overlaps this <c>Envelope</c>.
	*/
	public final boolean Intersects(ICoordinate p)
	{
		return Intersects(p.getX(), p.getY());
	}

	/**
	 Tests if the <c>Envelope other</c> lies wholely inside this <c>Envelope</c> (inclusive of the boundary).
	
	 
	 Note that this is <b>not</b> the same definition as the SFS <i>contains</i>,
	 which would exclude the envelope boundary.
	 
	 <p>The <c>Envelope</c> to check</p>
	 @return true if <c>other</c> is contained in this <c>Envelope</c>
	 <see cref="IEnvelope.Covers(IEnvelope)"/>
	*/
	public final boolean Contains(IEnvelope other)
	{
		return ((IEnvelope)this).Covers(other);
	}

	/**
	 Tests if the given point lies in or on the envelope.
	
	 
	 Note that this is <b>not</b> the same definition as the SFS <i>contains</i>,
	 which would exclude the envelope boundary.
	 
	 @param p the point which this <c>Envelope</c> is being checked for containing
	 @return <c>true</c> if the point lies in the interior or on the boundary of this <c>Envelope</c>. 
	 <see cref="IEnvelope.Covers(ICoordinate)"/>
	*/
	public final boolean Contains(ICoordinate p)
	{
		return ((IEnvelope)this).Covers(p);
	}

	/**
	 Tests if the given point lies in or on the envelope.
	
	 @param p the point which this <c>Envelope</c> is being checked for containing
	 @return <c>true</c> if the point lies in the interior or on the boundary of this <c>Envelope</c>.
	*/
	public final boolean Covers(ICoordinate p)
	{
		return Covers(p.getX(), p.getY());
	}

	/**
	 Tests if the <c>Envelope other</c> lies wholely inside this <c>Envelope</c> (inclusive of the boundary).
	
	 @param other the <c>Envelope</c> to check
	 @return true if this <c>Envelope</c> covers the <c>other</c>
	*/
	public final boolean Covers(IEnvelope other)
	{
		if (getIsNull() || other.getIsNull())
		{
			return false;
		}
		return other.getMinX() >= _minx && other.getMaxX() <= _maxx && other.getMinY() >= _miny && other.getMaxY() <= _maxy;
	}

	/** 
	 Computes the distance between this and another
	 <c>Envelope</c>.
	 The distance between overlapping Envelopes is 0.  Otherwise, the
	 distance is the Euclidean distance between the closest points.
	 
	 @return The distance between this and another <c>Envelope</c>.
	*/
	public final double Distance(IEnvelope env)
	{
		if (((IEnvelope)this).Intersects(env))
		{
			return 0;
		}

		double dx = 0.0;

		if (_maxx < env.getMinX())
		{
			dx = env.getMinX() - _maxx;
		}
		else if (_minx > env.getMaxX())
		{
			dx = _minx - env.getMaxX();
		}

		double dy = 0.0;

		if (_maxy < env.getMinY())
		{
			dy = env.getMinY() - _maxy;
		}
		else if (_miny > env.getMaxY())
		{
			dy = _miny - env.getMaxY();
		}

		// if either is zero, the envelopes overlap either vertically or horizontally
		if (dx == 0.0)
		{
			return dy;
		}
		if (dy == 0.0)
		{
			return dx;
		}

		return Math.sqrt(dx * dx + dy * dy);
	}

	public final int compareTo(IEnvelope other)
	{
		if (getIsNull() && other.getIsNull())
		{
			return 0;
		}
		if (!getIsNull() && other.getIsNull())
		{
			return 1;
		}
		if (getIsNull() && !other.getIsNull())
		{
			return -1;
		}

		if (getArea() > other.getArea())
		{
			return 1;
		}
		if (getArea() < other.getArea())
		{
			return -1;
		}
		return 0;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#pragma warning restore 612,618

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion BEGIN ADDED BY MPAUL42: monoGIS team

	/** 
	 Method to parse an envelope from its <see cref="Envelope.ToString"/> value
	 
	 @param envelope The envelope string
	 @return The envelope
	*/
	public static Envelope Parse(String envelope)
	{
		if (StringHelper.isNullOrEmpty(envelope))
		{
			throw new NullPointerException("envelope");
		}
		if (!(envelope.startsWith("Env[") && envelope.endsWith("]")))
		{
			throw new IllegalArgumentException("Not a valid envelope string envelope");
		}

		// test for null
		envelope = envelope.substring(4, 4 + envelope.length() - 5);
		if (envelope.equals("Null"))
		{
			return new Envelope();
		}

		// Parse values
		double[] ordinatesValues = new double[4];
		String[] ordinateLabel = new String[] {"x", "y"};
		int j = 0;

		// split into ranges
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		String[] parts = envelope.split("[,]", -1);
		if (parts.length != 2)
		{
			throw new IllegalArgumentException("Does not provide two ranges envelope");
		}

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		for (String part : parts)
		{
			// Split int min/max
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			String[] ordinates = part.split("[:]", -1);
			if (ordinates.length != 2)
			{
				throw new IllegalArgumentException("Does not provide just min and max values"+"envelope");
			}

			OutObject<Double> tempOut_Object = new OutObject<Double>();
			if (!ValueParser.TryParse(ordinates[0].trim(), tempOut_Object))
			{
				throw new IllegalArgumentException(String.format("Could not parse min %1$s-Ordinate"+ordinateLabel[j]) +"envelope");
			}
			OutObject<Double> tempOut_Object2 = new OutObject<Double>();
			if (!ValueParser.TryParse(ordinates[1].trim(), tempOut_Object2))
			{
				throw new IllegalArgumentException(String.format("Could not parse max %1$s-Ordinate", ordinateLabel[j])+"envelope");
			}
			j++;
		}

		return new Envelope(ordinatesValues[0], ordinatesValues[1], ordinatesValues[2], ordinatesValues[3]);
	}
}