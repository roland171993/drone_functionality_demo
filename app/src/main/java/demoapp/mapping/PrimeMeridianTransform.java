package demoapp.mapping;


/**
 Adjusts target Prime Meridian
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class PrimeMeridianTransform extends MathTransform
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region class variables
	private boolean _isInverted = false;
	private IPrimeMeridian _source;
	private IPrimeMeridian _target;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion class variables

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region constructors & finalizers
	/** 
	 Creates instance prime meridian transform
	 
	 @param source
	 @param target
	*/
	public PrimeMeridianTransform(IPrimeMeridian source, IPrimeMeridian target)
	{
		super();
		if (!source.getAngularUnit().EqualParams(target.getAngularUnit()))
		{
			throw new UnsupportedOperationException("The method or operation is not implemented.");
		}
		_source = source;
		_target = target;
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion constructors & finalizers

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region public properties
	/** 
	 Gets a Well-Known text representation of this affine math transformation.
	 
	 <value></value>
	*/
	@Override
	public String getWKT()
	{
		throw new UnsupportedOperationException("The method or operation is not implemented.");
	}
	/** 
	 Gets an XML representation of this affine transformation.
	 
	 <value></value>
	*/
	@Override
	public String getXML()
	{
		throw new UnsupportedOperationException("The method or operation is not implemented.");
	}

	/** 
	 Gets the dimension of input points.
	*/
	@Override
	public int getDimSource()
	{
		return 3;
	}

	/** 
	 Gets the dimension of output points.
	*/
	@Override
	public int getDimTarget()
	{
		return 3;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion public properties

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region public methods
	/** 
	 Returns the inverse of this affine transformation.
	 
	 @return IMathTransform that is the reverse of the current affine transformation.
	*/
	@Override
	public IMathTransform Inverse()
	{
		return new PrimeMeridianTransform(_target, _source);
	}

	/** 
	 Transforms a coordinate point. The passed parameter point should not be modified.
	 
	 @param point
	 @return 
	*/
	@Override
	public double[] Transform(double[] point)
	{
		double[] transformed = new double[point.length];

		if (!_isInverted)
		{
			transformed[0] = point[0] + _source.getLongitude() - _target.getLongitude();
		}
		else
		{
			transformed[0] = point[0] + _target.getLongitude() - _source.getLongitude();
		}
		transformed[1] = point[1];
		if (point.length > 2)
		{
			transformed[2] = point[2];
		}
		return transformed;
	}

	/** 
	 Reverses the transformation
	*/
	@Override
	public void Invert()
	{
		this._isInverted = !this._isInverted;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion public methods
}