package demoapp.mapping;


import java.util.concurrent.CopyOnWriteArrayList;

/**
 A coordinate system which sits inside another coordinate system. The fitted 
 coordinate system can be rotated and shifted, or use any other math transform
 to inject itself into the base coordinate system.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class FittedCoordinateSystem extends CoordinateSystem implements IFittedCoordinateSystem
{
	/** 
	 Creates an instance of FittedCoordinateSystem using the specified parameters
	 
	 @param baseSystem Underlying coordinate system.
	 @param transform Transformation from fitted coordinate system to the base one
	 @param name Name
	 @param authority Authority name
	 @param code Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	protected FittedCoordinateSystem(ICoordinateSystem baseSystem, IMathTransform transform, String name, String authority, long code, String alias, String remarks, String abbreviation)
	{
		super(name, authority, code, alias, abbreviation, remarks);
		_BaseCoordinateSystem = baseSystem;
		_ToBaseTransform = transform;
		//get axis infos from the source
		super.setAxisInfo(new CopyOnWriteArrayList<AxisInfo>());
		for (int dim = 0; dim < baseSystem.getDimension(); dim++)
		{
			super.getAxisInfo().add(baseSystem.GetAxis(dim));
		}
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region public properties

	private IMathTransform _ToBaseTransform;

	/** 
	 Represents math transform that injects itself into the base coordinate system.
	*/
	public final IMathTransform getToBaseTransform()
	{
		return _ToBaseTransform;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion public properties

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IFittedCoordinateSystem Members

	private ICoordinateSystem _BaseCoordinateSystem;

	/** 
	 Gets underlying coordinate system.
	*/
	public final ICoordinateSystem getBaseCoordinateSystem()
	{
		return _BaseCoordinateSystem;
	}

	/** 
	 Gets Well-Known Text of a math transform to the base coordinate system. 
	 The dimension of this fitted coordinate system is determined by the source 
	 dimension of the math transform. The transform should be one-to-one within 
	 this coordinate system's domain, and the base coordinate system dimension 
	 must be at least as big as the dimension of this coordinate system.
	 
	 @return 
	*/
	public final String ToBase()
	{
		return _ToBaseTransform.getWKT();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion IFittedCoordinateSystem Members

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region ICoordinateSystem Members

	/** 
	 Returns the Well-known text for this object as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
			//<fitted cs>          = FITTED_CS["<name>", <to base>, <base cs>]

//		StringBuilder sb = new StringBuilder();
//		sb.append(String.format("FITTED_CS[\"%1$s\", %2$s, %3$s]", getName(), this._ToBaseTransform.getWKT(), this._BaseCoordinateSystem.WKT));
//		return sb.toString();
		return "ROLAND HAS COMMENT in FittedCoordinateSystem.java getWKT()";
	}

	/** 
	 Gets an XML representation of this object.
	*/
	@Override
	public String getXML()
	{
		throw new UnsupportedOperationException();
	}

	/** 
	 Checks whether the values of this instance is equal to the values of another instance.
	 Only parameters used for coordinate system are used for comparison.
	 Name, abbreviation, authority, alias and remarks are ignored in the comparison.
	 
	 @param obj
	 @return True if equal
	*/
	@Override
	public boolean EqualParams(Object obj)
	{
		IFittedCoordinateSystem fcs = obj instanceof IFittedCoordinateSystem ? (IFittedCoordinateSystem)obj : null;
		if (fcs != null)
		{
			if (fcs.getBaseCoordinateSystem().EqualParams(this.getBaseCoordinateSystem()))
			{
				String fcsToBase = fcs.ToBase();
				String thisToBase = this.ToBase();
				if (fcsToBase.equals(thisToBase))
				{
					return true;
				}
			}
		}
		return false;
	}

	/** 
	 Gets the units for the dimension within coordinate system. 
	 Each dimension in the coordinate system has corresponding units.
	*/
	@Override
	public IUnit GetUnits(int dimension)
	{
		return _BaseCoordinateSystem.GetUnits(dimension);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}