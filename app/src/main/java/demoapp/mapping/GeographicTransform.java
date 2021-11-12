package demoapp.mapping;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 The GeographicTransform class is implemented on geographic transformation objects and
 implements datum transformations between geographic coordinate systems.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class GeographicTransform extends MathTransform
{
	public GeographicTransform(IGeographicCoordinateSystem sourceGCS, IGeographicCoordinateSystem targetGCS)
	{
		_SourceGCS = sourceGCS;
		_TargetGCS = targetGCS;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IGeographicTransform Members

	private IGeographicCoordinateSystem _SourceGCS;

	/** 
	 Gets or sets the source geographic coordinate system for the transformation.
	*/
	public final IGeographicCoordinateSystem getSourceGCS()
	{
		return _SourceGCS;
	}
	public final void setSourceGCS(IGeographicCoordinateSystem value)
	{
		_SourceGCS = value;
	}

	private IGeographicCoordinateSystem _TargetGCS;

	/** 
	 Gets or sets the target geographic coordinate system for the transformation.
	*/
	public final IGeographicCoordinateSystem getTargetGCS()
	{
		return _TargetGCS;
	}
	public final void setTargetGCS(IGeographicCoordinateSystem value)
	{
		_TargetGCS = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification. [NOT IMPLEMENTED].
	*/
	@Override
	public String getWKT()
	{
		throw new UnsupportedOperationException();
	}

	/** 
	 Gets an XML representation of this object [NOT IMPLEMENTED].
	*/
	@Override
	public String getXML()
	{
		throw new UnsupportedOperationException();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	public int getDimSource()
	{
		return _SourceGCS.getDimension();
	}

	@Override
	public int getDimTarget()
	{
		return _TargetGCS.getDimension();
	}

	/** 
	 Creates the inverse transform of this object.
	 
	 This method may fail if the transform is not one to one. However, all cartographic projections should succeed.
	 @return 
	*/
	@Override
	public IMathTransform Inverse()
	{
		throw new UnsupportedOperationException();
	}

	/** 
	 Transforms a coordinate point. The passed parameter point should not be modified.
	 
	 @param point
	 @return 
	*/
	@Override
	public double[] Transform(double[] point)
	{
		double[] pOut = (double[]) point.clone();
		pOut[0] /= getSourceGCS().getAngularUnit().getRadiansPerUnit();
		pOut[0] -= getSourceGCS().getPrimeMeridian().getLongitude() / getSourceGCS().getPrimeMeridian().getAngularUnit().getRadiansPerUnit();
		pOut[0] += getTargetGCS().getPrimeMeridian().getLongitude() / getTargetGCS().getPrimeMeridian().getAngularUnit().getRadiansPerUnit();
		pOut[0] *= getSourceGCS().getAngularUnit().getRadiansPerUnit();
		return pOut;
	}

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
	@Override
	public List<double[]> TransformList(List<double[]> points)
	{
		CopyOnWriteArrayList<double[]> trans = new CopyOnWriteArrayList<double[]>();
		for (double[] p : points)
		{
			trans.add(Transform(p));
		}
		return trans;
	}

	@Override
	public List<Coordinate> TransformListCoordinateList(List<Coordinate> points)
	{
		CopyOnWriteArrayList<Coordinate> trans = new CopyOnWriteArrayList<Coordinate>();
		for (Coordinate coordinate : points)
		{
			trans.add(Transform(coordinate));
		}
		return trans;
	}

	/** 
	 Reverses the transformation
	*/
	@Override
	public void Invert()
	{
		throw new UnsupportedOperationException();
	}
}