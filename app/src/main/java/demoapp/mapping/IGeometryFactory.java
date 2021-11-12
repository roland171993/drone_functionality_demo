package demoapp.mapping;


import java.util.Collection;


/** 
 Supplies a set of utility methods for building Geometry objects 
 from lists of Coordinates.
*/
public interface IGeometryFactory
{

	/** 
	 Gets the spatial reference id to assign when creating geometries
	*/
	int getSRID();


	/**   
	 Build an appropriate <c>Geometry</c>, <c>MultiGeometry</c>, or
	 <c>GeometryCollection</c> to contain the <c>Geometry</c>s in
	 it.
	 
	 
	  If <c>geomList</c> contains a single <c>Polygon</c>,
	 the <c>Polygon</c> is returned.
	  If <c>geomList</c> contains several <c>Polygon</c>s, a
	 <c>MultiPolygon</c> is returned.
	  If <c>geomList</c> contains some <c>Polygon</c>s and
	 some <c>LineString</c>s, a <c>GeometryCollection</c> is
	 returned.
	  If <c>geomList</c> is empty, an empty <c>GeometryCollection</c>
	 is returned.
	 
	 @param geomList The <c>Geometry</c> to combine.
	 @return 
	 A <c>Geometry</c> of the "smallest", "most type-specific" 
	 class that can contain the elements of <c>geomList</c>.
	 
	*/
	IGeometry BuildGeometry(Collection<IGeometry> geomList);

	/** 
	 Returns a clone of g based on a CoordinateSequence created by this
	 GeometryFactory's CoordinateSequenceFactory.        
	*/
	IGeometry CreateGeometry(IGeometry g);

	/** 
	 Creates an empty Point
	 
	 @return An empty Point
	*/
	IPoint CreatePoint();

	/** 
	 Creates a Point using the given Coordinate; a null Coordinate will create
	 an empty Geometry.
	 
	 @param coordinate The coordinate
	 @return A Point
	*/
	IPoint CreatePoint(Coordinate coordinate);

	/** 
	 Creates a <c>Point</c> using the given <c>CoordinateSequence</c>; a null or empty
	 CoordinateSequence will create an empty Point.
	 
	 @param coordinates The coordinate sequence.
	 @return A Point
	*/
	IPoint CreatePoint(ICoordinateSequence coordinates);




	/** 
	 Creates a <see cref="IGeometry"/> with the same extent as the given envelope.
	*/
	IGeometry ToGeometry(Envelope envelopeInternal);
}