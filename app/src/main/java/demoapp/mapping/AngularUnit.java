package demoapp.mapping;


/**
 Definition of angular units.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class AngularUnit extends Info implements IAngularUnit
{
	/** 
	 Equality tolerance value. Values with a difference less than this are considered equal.
	*/
	private static final double EqualityTolerance = 2.0e-17;


	/** 
	 Initializes a new instance of a angular unit
	 
	 @param radiansPerUnit Radians per unit
	 @param name Name
	 @param authority Authority name
	 @param authorityCode Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public AngularUnit(double radiansPerUnit, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks)
	{
		super(name, authority, authorityCode, alias, abbreviation, remarks);
		_RadiansPerUnit = radiansPerUnit;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Predifined units

	/** 
	 The angular degrees are PI/180 = 0.017453292519943295769236907684886 radians
	*/
	public static AngularUnit getDegrees()
	{
		return new AngularUnit(0.017453292519943295769236907684886, "degree", "EPSG", 9102, "deg", "", "=pi/180 radians");
	}



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IAngularUnit Members

	private double _RadiansPerUnit;

	/** 
	 Gets or sets the number of radians per <see cref="AngularUnit"/>.
	*/
	public final double getRadiansPerUnit()
	{
		return _RadiansPerUnit;
	}
	public final void setRadiansPerUnit(double value)
	{
		_RadiansPerUnit = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("UNIT[\"{0}\", {1}").append(getName()).append(getRadiansPerUnit());
		if (!StringHelper.isNullOrEmpty(getAuthority()) && getAuthorityCode() > 0)
		{
			sb.append(String.format(", AUTHORITY[\"%1$s\", \"%2$s\"]", getAuthority(), getAuthorityCode()));
		}
		sb.append("]");
		return sb.toString();
	}

	/** 
	 Gets an XML representation of this object.
	*/
	@Override
	public String getXML()
	{
		return String.format("<CS_AngularUnit RadiansPerUnit=\"%1$s\">%2$s</CS_AngularUnit>", getRadiansPerUnit(), getInfoXml());
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

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
		if (!(obj instanceof AngularUnit))
		{
			return false;
		}
		return Math.abs(((AngularUnit)obj).getRadiansPerUnit() - this.getRadiansPerUnit()) < EqualityTolerance;
	}
}