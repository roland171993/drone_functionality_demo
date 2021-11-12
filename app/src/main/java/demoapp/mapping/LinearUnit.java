package demoapp.mapping;


/**
 Definition of linear units.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class LinearUnit extends Info implements ILinearUnit
{
	/** 
	 Creates an instance of a linear unit
	 
	 @param metersPerUnit Number of meters per <see cref="LinearUnit" />
	 @param name Name
	 @param authority Authority name
	 @param authorityCode Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public LinearUnit(double metersPerUnit, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks)
	{
		super(name, authority, authorityCode, alias, abbreviation, remarks);
		_MetersPerUnit = metersPerUnit;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Predefined units
	/** 
	 Returns the meters linear unit.
	 Also known as International metre. SI standard unit.
	*/
	public static ILinearUnit getMetre()
	{
		return new LinearUnit(1.0, "metre", "EPSG", 9001, "m", "", "Also known as International metre. SI standard unit.");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region ILinearUnit Members

	private double _MetersPerUnit;

	/** 
	 Gets or sets the number of meters per <see cref="LinearUnit"/>.
	*/
	public final double getMetersPerUnit()
	{
		return _MetersPerUnit;
	}
	public final void setMetersPerUnit(double value)
	{
		_MetersPerUnit = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "UNIT[\"{0}\", {1}");
		sb.append(getName());
		sb.append(getMetersPerUnit());
		if (!StringHelper.isNullOrEmpty(getAuthority()) && getAuthorityCode() > 0)
		{
			sb.append(String.format(", AUTHORITY[\"%1$s\", \"%2$s\"]", getAuthority(), getAuthorityCode()));
		}
		sb.append("]");
		return sb.toString();
	}

	/** 
	 Gets an XML representation of this object
	*/
	@Override
	public String getXML()
	{
		return String.format("<CS_LinearUnit MetersPerUnit=\"%1$s\">%2$s</CS_LinearUnit>", getMetersPerUnit(), getInfoXml());
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
		if (!(obj instanceof LinearUnit))
		{
			return false;
		}
		return (obj instanceof LinearUnit ? (LinearUnit)obj : null).getMetersPerUnit() == this.getMetersPerUnit();
	}
}