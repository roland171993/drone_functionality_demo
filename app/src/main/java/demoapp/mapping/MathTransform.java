package demoapp.mapping;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/** 
 Abstract class for creating multi-dimensional coordinate points transformations.
 
 
 If a client application wishes to query the source and target coordinate 
 systems of a transformation, then it should keep hold of the 
 <see cref="ICoordinateTransformation"/> interface, and use the contained 
 math transform object whenever it wishes to perform a transform.
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public abstract class MathTransform implements IMathTransform
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IMathTransform Members

	/** 
	 Gets the dimension of input points.
	*/
	public abstract int getDimSource();

	/** 
	 Gets the dimension of output points.
	*/
	public abstract int getDimTarget();

	/** 
	 Tests whether this transform does not move any points.
	 
	 @return 
	*/
	public boolean Identity()
	{
		throw new UnsupportedOperationException();
	}

	/** 
	 Gets a Well-Known text representation of this object.
	*/
	public abstract String getWKT();

	/** 
	 Gets an XML representation of this object.
	*/
	public abstract String getXML();

	/** 
	 Gets the derivative of this transform at a point. If the transform does 
	 not have a well-defined derivative at the point, then this function should 
	 fail in the usual way for the DCP. The derivative is the matrix of the 
	 non-translating portion of the approximate affine map at the point. The
	 matrix will have dimensions corresponding to the source and target 
	 coordinate systems. If the input dimension is M, and the output dimension 
	 is N, then the matrix will have size [M][N]. The elements of the matrix 
	 {elt[n][m] : n=0..(N-1)} form a vector in the output space which is 
	 parallel to the displacement caused by a small change in the m'th ordinate 
	 in the input space.
	 
	 @param point
	 @return 
	*/
	public double[][] Derivative(double[] point)
	{
		throw new UnsupportedOperationException();
	}

	/** 
	 Gets transformed convex hull.
	 
	 
	 <p>The supplied ordinates are interpreted as a sequence of points, which generates a convex
	 hull in the source space. The returned sequence of ordinates represents a convex hull in the 
	 output space. The number of output points will often be different from the number of input 
	 points. Each of the input points should be inside the valid domain (this can be checked by 
	 testing the points' domain flags individually). However, the convex hull of the input points
	 may go outside the valid domain. The returned convex hull should contain the transformed image
	 of the intersection of the source convex hull and the source domain.</p>
	 <p>A convex hull is a shape in a coordinate system, where if two positions A and B are 
	 inside the shape, then all positions in the straight line between A and B are also inside 
	 the shape. So in 3D a cube and a sphere are both convex hulls. Other less obvious examples 
	 of convex hulls are straight lines, and single points. (A single point is a convex hull, 
	 because the positions A and B must both be the same - i.e. the point itself. So the straight
	 line between A and B has zero length.)</p>
	 <p>Some examples of shapes that are NOT convex hulls are donuts, and horseshoes.</p>
	 
	 @param points
	 @return 
	*/
	public CopyOnWriteArrayList<Double> GetCodomainConvexHull(CopyOnWriteArrayList<Double> points)
	{
		throw new UnsupportedOperationException();
	}

	/** 
	 Gets flags classifying domain points within a convex hull.
	 
	 
	 The supplied ordinates are interpreted as a sequence of points, which 
	 generates a convex hull in the source space. Conceptually, each of the 
	 (usually infinite) points inside the convex hull is then tested against
	 the source domain. The flags of all these tests are then combined. In 
	 practice, implementations of different transforms will use different 
	 short-cuts to avoid doing an infinite number of tests.
	 
	 @param points
	 @return 
	*/
	public DomainFlags GetDomainFlags(CopyOnWriteArrayList<Double> points)
	{
		throw new UnsupportedOperationException();
	}

	/** 
	 Creates the inverse transform of this object.
	 
	 This method may fail if the transform is not one to one. However, all cartographic projections should succeed.
	 @return 
	*/
	public abstract IMathTransform Inverse();

	/** 
	 Transforms a coordinate point. The passed parameter point should not be modified.
	 
	 @param point
	 @return 
	*/
	public abstract double[] Transform(double[] point);

	/** 
	 Transforms a list of coordinate point ordinal values.
	 
	 
	 This method is provided for efficiently transforming many points. The supplied array 
	 of ordinal values will contain packed ordinal values. For example, if the source 
	 dimension is 3, then the ordinals will be packed in this order (x0,y0,z0,x1,y1,z1 ...).
	 The size of the passed array must be an integer multiple of DimSource. The returned 
	 ordinal values are packed in a similar way. In some DCPs. the ordinals may be 
	 transformed in-place, and the returned array may be the same as the passed array.
	 So any client code should not attempt to reuse the passed ordinal values (although
	 they can certainly reuse the passed array). If there is any problem then the server
	 implementation will throw an exception. If this happens then the client should not
	 make any assumptions about the state of the ordinal values.
	 
	 @param points
	 @return 
	*/
	public List<double[]> TransformList(List<double[]> points)
	{
		CopyOnWriteArrayList<double[]> result = new CopyOnWriteArrayList<double[]>();

		for (double[] c : points)
		{
			result.add(Transform(c));
		}

		return result;
	}

	public List<Coordinate> TransformListCoordinateList(List<Coordinate> points)
	{
		CopyOnWriteArrayList<Coordinate> result = new CopyOnWriteArrayList<Coordinate>();

		for (Coordinate c : points)
		{
			result.add(Transform(c));
		}
		return result;
	}

	@Deprecated
	public final ICoordinate Transform(ICoordinate coordinate)
	{
		double[] ret = Transform(new double[] {coordinate.getX(), coordinate.getY(), coordinate.getZ()});

		coordinate.setX(ret[0]);
		coordinate.setY(ret[1]);
		if (getDimTarget() > 2)
		{
			coordinate.setZ(ret[2]);
		}

		return coordinate;
	}

	public final Coordinate Transform(Coordinate coordinate)
	{
		double[] ordinates = getDimSource() == 2 ? new double[] {coordinate.getX(), coordinate.getY()} : new double[] {coordinate.getX(), coordinate.getY(), coordinate.getZ()};

			double[] ret = Transform(ordinates);

		return (getDimTarget() == 2) ? new Coordinate(ret[0], ret[1]) : new Coordinate(ret[0], ret[1], ret[2]);
	}

	public ICoordinateSequence Transform(ICoordinateSequence coordinateSequence)
	{
		Coordinate clone = new Coordinate();
		for (int i = 0; i < coordinateSequence.getCount(); i++)
		{
			clone.setCoordinateValue(coordinateSequence.GetCoordinate(i));
			clone = Transform(clone);
			coordinateSequence.SetOrdinate(i, Ordinate.X, clone.getX());
			coordinateSequence.SetOrdinate(i, Ordinate.Y, clone.getY());
			if (getDimTarget() > 2)
			{
				coordinateSequence.SetOrdinate(i, Ordinate.Z, clone.getZ());
			}
		}
		return coordinateSequence;
	}

	/** 
	 Reverses the transformation
	*/
	public abstract void Invert();

	/** 
	 To convert degrees to radians, multiply degrees by pi/180. 
	*/
	protected static double Degrees2Radians(double deg)
	{
		return (D2R * deg);

	}
	/** 
	 R2D
	*/
	protected static final double R2D = 180 / Math.PI;

	/** 
	 D2R
	*/
	protected static final double D2R = Math.PI / 180;

	/** 
	 
	 
	 @param rad
	 @return 
	*/
	protected static double Radians2Degrees(double rad)
	{
		return (R2D * rad);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}