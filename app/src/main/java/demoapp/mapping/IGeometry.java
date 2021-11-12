package demoapp.mapping;



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_ICLONEABLE
//#else
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter could not confirm whether this is a namespace alias or a type alias:
//using ICloneable = GeoAPI.ICloneable;
//#endif

/**
 Interface for basic implementation of <c>Geometry</c>.
*/
public interface IGeometry extends Cloneable, Comparable<IGeometry>
{
	/**
	 The <see cref="IGeometryFactory"/> used to create this geometry
	*/
	IGeometryFactory getFactory();


	/**
	 Gets the spatial reference id
	*/
	int getSRID();
	void setSRID(int value);

	/**
	 Gets a <see cref="Coordinate"/> that is guaranteed to be part of the geometry, usually the first.
	 */
	Coordinate getCoordinate();

	/**
	 Gets the geometry type
	*/
	String getGeometryType();

	/**
	 Gets the OGC geometry type
	*/
	OgcGeometryType getOgcGeometryType();

	/**
	 Gets the area of this geometry if applicable, otherwise <c>0d</c>

	 A <see cref="ISurface"/> method moved in IGeometry
	*/
	double getArea();

	/**
	 Gets the length of this geometry if applicable, otherwise <c>0d</c>

	 A <see cref="ICurve"/> method moved in IGeometry
	*/
	double getLength();

	/**
	 Gets the number of geometries that make up this geometry


	 A <see cref="IGeometryCollection"/> method moved in IGeometry

	*/
	int getNumGeometries();

	/**
	 Get the number of coordinates, that make up this geometry

	 A <see cref="ILineString"/> method moved to IGeometry
	*/
	int getNumPoints();

	/**
	 Gets the boundary geometry
	*/
	IGeometry getBoundary();

	boolean getIsEmpty();

	boolean getIsRectangle();

	boolean getIsSimple();

	boolean getIsValid();

	boolean Within(IGeometry g);

	boolean Contains(IGeometry g);

	boolean IsWithinDistance(IGeometry geom, double distance);

	boolean CoveredBy(IGeometry g);

	boolean Covers(IGeometry g);

	boolean Crosses(IGeometry g);

	boolean Intersects(IGeometry g);

	boolean Overlaps(IGeometry g);

	boolean Relate(IGeometry g, String intersectionPattern);

	boolean Touches(IGeometry g);

	boolean Disjoint(IGeometry g);

	/** 
	 
	 
	 @return 
	*/
	IGeometry Reverse();

	/**   
	 Returns the minimum distance between this <c>Geometry</c>
	 and the <c>Geometry</c> g.
	 
	 @param g The <c>Geometry</c> from which to compute the distance.
	*/
	double Distance(IGeometry g);


	/** 
	 Notifies this geometry that its coordinates have been changed by an external
	 party (using a CoordinateFilter, for example). The Geometry will flush
	 and/or update any information it has cached (such as its <see cref="IEnvelope"/>).
	*/
	void GeometryChanged();

	/** 
	 Notifies this Geometry that its Coordinates have been changed by an external
	 party. When <see cref="GeometryChanged"/> is called, this method will be called for
	 this <c>Geometry</c> and its component geometries.
	*/
	void GeometryChangedAction();
}