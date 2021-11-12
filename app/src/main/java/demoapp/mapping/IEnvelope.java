package demoapp.mapping;


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_ICLONEABLE
//#else
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter could not confirm whether this is a namespace alias or a type alias:
//using ICloneable = GeoAPI.ICloneable;
//#endif

/** 
 Defines a rectangular region of the 2D coordinate plane.
 
 
 <p>
 It is often used to represent the bounding box of a <c>Geometry</c>,
 e.g. the minimum and maximum x and y values of the <c>Coordinate</c>s.
 </p>
 <p>
 Note that Envelopes support infinite or half-infinite regions, by using the values of
 <c>Double.PositiveInfinity</c> and <c>Double.NegativeInfinity</c>.
 </p>
 <p>
 When Envelope objects are created or initialized,
 the supplies extent values are automatically sorted into the correct order.    
 </p>
 
*/
@Deprecated
public interface IEnvelope extends Cloneable, Comparable<IEnvelope>
{
	/** 
	 Gets the area of the envelope
	*/
	double getArea();

	/** 
	 Gets the width of the envelope
	*/
	double getWidth();

	/** 
	 Gets the height of the envelope
	*/
	double getHeight();

	/** 
	 Gets the maximum x-ordinate of the envelope
	*/
	double getMaxX();

	/** 
	 Gets the maximum y-ordinate of the envelope
	*/
	double getMaxY();

	/** 
	 Gets the minimum x-ordinate of the envelope
	*/
	double getMinX();

	/** 
	 Gets the mimimum y-ordinate of the envelope
	*/
	double getMinY();

	/** 
	 Gets the <see cref="ICoordinate"/> or the center of the envelope
	*/
	ICoordinate getCentre();

	/** 
	 Returns if the point specified by <see paramref="x"/> and <see paramref="y"/> is contained by the envelope.
	 
	 @param x The x-ordinate
	 @param y The y-ordinate
	 @return True if the point is contained by the envlope
	*/
	boolean Contains(double x, double y);

	/** 
	 Returns if the point specified by <see paramref="p"/> is contained by the envelope.
	 
	 @param p The point
	 @return True if the point is contained by the envlope
	*/
	boolean Contains(ICoordinate p);

	/** 
	 Returns if the envelope specified by <see paramref="other"/> is contained by this envelope.
	 
	 @param other The envelope to test
	 @return True if the other envelope is contained by this envlope
	*/
	boolean Contains(IEnvelope other);

	/**
	 Tests if the given point lies in or on the envelope.
	
	 @param x the x-coordinate of the point which this <c>Envelope</c> is being checked for containing
	 @param y the y-coordinate of the point which this <c>Envelope</c> is being checked for containing
	 @return  <c>true</c> if <c>(x, y)</c> lies in the interior or on the boundary of this <c>Envelope</c>.
	*/
	boolean Covers(double x, double y);

	/**
	 Tests if the given point lies in or on the envelope.
	
	 @param p the point which this <c>Envelope</c> is being checked for containing
	 @return <c>true</c> if the point lies in the interior or on the boundary of this <c>Envelope</c>.
	*/
	boolean Covers(ICoordinate p);

	/**
	 Tests if the <c>Envelope other</c> lies wholely inside this <c>Envelope</c> (inclusive of the boundary).
	
	 @param other the <c>Envelope</c> to check
	 @return true if this <c>Envelope</c> covers the <c>other</c>
	*/
	boolean Covers(IEnvelope other);

	/** 
	 Computes the distance between this and another
	 <c>Envelope</c>.
	 The distance between overlapping Envelopes is 0.  Otherwise, the
	 distance is the Euclidean distance between the closest points.
	 
	 @return The distance between this and another <c>Envelope</c>.
	*/
	double Distance(IEnvelope env);

	/** 
	 Expands this envelope by a given distance in all directions.
	 Both positive and negative distances are supported.
	 
	 @param distance The distance to expand the envelope.
	*/
	void ExpandBy(double distance);

	/** 
	 Expands this envelope by a given distance in all directions.
	 Both positive and negative distances are supported.
	 
	 @param deltaX The distance to expand the envelope along the the X axis.
	 @param deltaY The distance to expand the envelope along the the Y axis.
	*/
	void ExpandBy(double deltaX, double deltaY);

	/** 
	 Enlarges this <code>Envelope</code> so that it contains
	 the given <see cref="Coordinate"/>.
	 Has no effect if the point is already on or within the envelope.
	 
	 @param p The Coordinate.
	*/
	void ExpandToInclude(ICoordinate p);

	/** 
	 Enlarges this <c>Envelope</c> so that it contains
	 the given <see cref="Coordinate"/>.
	 
	 Has no effect if the point is already on or within the envelope.
	 @param x The value to lower the minimum x to or to raise the maximum x to.
	 @param y The value to lower the minimum y to or to raise the maximum y to.
	*/
	void ExpandToInclude(double x, double y);

	/** 
	 Enlarges this <c>Envelope</c> so that it contains
	 the <c>other</c> Envelope.
	 Has no effect if <c>other</c> is wholly on or
	 within the envelope.
	 
	 @param other the <c>Envelope</c> to expand to include.
	*/
	void ExpandToInclude(IEnvelope other);

	/** 
	 Method to initialize the envelope. Calling this function will result in <see cref="IsNull"/> returning <value>true</value>
	*/
	void Init();

	/** 
	 Method to initialize the envelope with a <see cref="T:GeoAPI.Geometries.ICoordinate"/>. Calling this function will result in an envelope having no extent but a location.
	 
	 @param p The point
	*/
	void Init(ICoordinate p);

	/** 
	 Method to initialize the envelope. Calling this function will result in an envelope having the same extent as <paramref name="env"/>.
	 
	 @param env The envelope
	*/
	void Init(IEnvelope env);

	/** 
	 Method to initialize the envelope with two <see cref="T:GeoAPI.Geometries.ICoordinate"/>s.
	 
	 @param p1 The first point
	 @param p2 The second point
	*/
	void Init(ICoordinate p1, ICoordinate p2);

	/** 
	 Initialize an <c>Envelope</c> for a region defined by maximum and minimum values.
	 
	 @param x1 The first x-value.
	 @param x2 The second x-value.
	 @param y1 The first y-value.
	 @param y2 The second y-value.
	*/
	void Init(double x1, double x2, double y1, double y2);

	/** 
	 Computes the intersection of two <see cref="Envelope"/>s.
	 
	 @param env The envelope to intersect with
	 @return 
	 A new Envelope representing the intersection of the envelopes (this will be
	 the null envelope if either argument is null, or they do not intersect
	 
	*/
	IEnvelope Intersection(IEnvelope env);

	/** 
	 Translates this envelope by given amounts in the X and Y direction.
	 
	 @param transX The amount to translate along the X axis.
	 @param transY The amount to translate along the Y axis.
	*/
	void Translate(double transX, double transY);

	/** 
	 Check if the point <c>p</c> overlaps (lies inside) the region of this <c>Envelope</c>.
	 
	 @param p the <c>Coordinate</c> to be tested.
	 @return <c>true</c> if the point overlaps this <c>Envelope</c>.
	*/
	boolean Intersects(ICoordinate p);

	/** 
	 Check if the point <c>(x, y)</c> overlaps (lies inside) the region of this <c>Envelope</c>.
	 
	 @param x the x-ordinate of the point.
	 @param y the y-ordinate of the point.
	 @return <c>true</c> if the point overlaps this <c>Envelope</c>.
	*/
	boolean Intersects(double x, double y);

	/** 
	 Check if the region defined by <c>other</c>
	 overlaps (intersects) the region of this <c>Envelope</c>.
	 
	 @param other the <c>Envelope</c> which this <c>Envelope</c> is
	 being checked for overlapping.
	 
	 @return 
	 <c>true</c> if the <c>Envelope</c>s overlap.
	 
	*/
	boolean Intersects(IEnvelope other);

	/** 
	 Returns <c>true</c> if this <c>Envelope</c> is a "null" envelope.
	 
	 @return 
	 <c>true</c> if this <c>Envelope</c> is uninitialized
	 or is the envelope of the empty point.
	 
	*/
	boolean getIsNull();

	/** 
	 Makes this <c>Envelope</c> a "null" envelope..
	*/
	void SetToNull();

	void Zoom(double perCent);

	boolean Overlaps(IEnvelope other);

	boolean Overlaps(ICoordinate p);

	boolean Overlaps(double x, double y);

	void SetCentre(double width, double height);

	void SetCentre(IPoint centre, double width, double height);

	void SetCentre(ICoordinate centre);

	void SetCentre(IPoint centre);

	void SetCentre(ICoordinate centre, double width, double height);

	IEnvelope Union(IPoint point);

	IEnvelope Union(ICoordinate coord);

	IEnvelope Union(IEnvelope box);

}