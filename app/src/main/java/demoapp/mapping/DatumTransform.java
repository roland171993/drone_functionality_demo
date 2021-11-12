package demoapp.mapping;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 Transformation for applying 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class DatumTransform extends MathTransform
{
	protected IMathTransform _inverse;
	private final Wgs84ConversionInfo _toWgs94;
	private double[] v;

	private boolean _isInverse;

	/** 
	 Initializes a new instance of the <see cref="DatumTransform"/> class.
	 
	 @param towgs84
	*/
	public DatumTransform(Wgs84ConversionInfo towgs84)
	{
		this(towgs84, false);
	}

	private DatumTransform(final Wgs84ConversionInfo towgs84, boolean isInverse)
	{
		_toWgs94 = towgs84;
		v = _toWgs94.GetAffineTransform();
		_isInverse = isInverse;
	}
	/** 
	 Gets a Well-Known text representation of this object.
	 
	 <value></value>
	*/
	@Override
	public String getWKT()
	{
		throw new UnsupportedOperationException();
	}

	/** 
	 Gets an XML representation of this object.
	 
	 <value></value>
	*/
	@Override
	public String getXML()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getDimSource()
	{
		return 3;
	}

	@Override
	public int getDimTarget()
	{
		return 3;
	}

	/** 
	 Creates the inverse transform of this object.
	 
	 @return 
	 This method may fail if the transform is not one to one. However, all cartographic projections should succeed.
	*/
	@Override
	public IMathTransform Inverse()
	{
		if (_inverse == null)
		{
			_inverse = new DatumTransform(_toWgs94,!_isInverse);
		}
		return _inverse;
	}

	/** 
	 Transforms a coordinate point.
	 
	 @param p
	 @return 
	 <seealso href="http://en.wikipedia.org/wiki/Helmert_transformation"/>
	*/
	private double[] Apply(double[] p)
	{
		return new double[] {v[0] * (p[0] - v[3] * p[1] + v[2] * p[2]) + v[4], v[0] * (v[3] * p[0] + p[1] - v[1] * p[2]) + v[5], v[0] * (-v[2] * p[0] + v[1] * p[1] + p[2]) + v[6]};
	}

	/** 
	 For the reverse transformation, each element is multiplied by -1.
	 
	 @param p
	 @return 
	 <seealso href="http://en.wikipedia.org/wiki/Helmert_transformation"/>
	*/
	private double[] ApplyInverted(double[] p)
	{

		return new double[] {(1 - (v[0] - 1)) * (p[0] + v[3] * p[1] - v[2] * p[2]) - v[4], (1 - (v[0] - 1)) * (-v[3] * p[0] + p[1] + v[1] * p[2]) - v[5], (1 - (v[0] - 1)) * (v[2] * p[0] - v[1] * p[1] + p[2]) - v[6]};
	}

	/** 
	 Transforms a coordinate point. The passed parameter point should not be modified.
	 
	 @param point
	 @return 
	*/
	@Override
	public double[] Transform(double[] point)
	{
		if (!_isInverse)
		{
			 return Apply(point);
		}
		else
		{
			return ApplyInverted(point);
		}
	}

	/** 
	 Transforms a list of coordinate point ordinal values.
	 
	 @param points
	 @return 
	 
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
	 
	*/
	@Override
	public List<double[]> TransformList(List<double[]> points)
	{
		CopyOnWriteArrayList<double[]> pnts = new CopyOnWriteArrayList<double[]>();
		for (double[] p : points)
		{
			pnts.add(Transform(p));
		}
		return pnts;
	}

	@Override
	public List<Coordinate> TransformListCoordinateList(List<Coordinate> points)
	{
		CopyOnWriteArrayList<Coordinate> pnts = new CopyOnWriteArrayList<Coordinate>();
		for (Coordinate p : points)
		{
			pnts.add(Transform(p));
		}
		return pnts;
	}

	/** 
	 Reverses the transformation
	*/
	@Override
	public void Invert()
	{
		_isInverse = !_isInverse;
	}
}