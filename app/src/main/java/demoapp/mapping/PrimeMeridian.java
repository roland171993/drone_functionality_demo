package demoapp.mapping;


/**
 A meridian used to take longitude measurements from.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class PrimeMeridian extends Info implements IPrimeMeridian
{
	/** 
	 Initializes a new instance of a prime meridian
	 
	 @param longitude Longitude of prime meridian
	 @param angularUnit Angular unit
	 @param name Name
	 @param authority Authority name
	 @param authorityCode Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public PrimeMeridian(double longitude, IAngularUnit angularUnit, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks)
	{
		super(name, authority, authorityCode, alias, abbreviation, remarks);
		_Longitude = longitude;
		_AngularUnit = angularUnit;
	}

	/** 
	 Greenwich prime meridian
	*/
	public static PrimeMeridian getGreenwich()
	{
		return new PrimeMeridian(0.0, AngularUnit.getDegrees(), "Greenwich", "EPSG", 8901, "", "", "");
	}




//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IPrimeMeridian Members

	private double _Longitude;

	/** 
	 Gets or sets the longitude of the prime meridian (relative to the Greenwich prime meridian).
	*/
	public final double getLongitude()
	{
		return _Longitude;
	}
	public final void setLongitude(double value)
	{
		_Longitude = value;
	}

	private IAngularUnit _AngularUnit;

	/** 
	 Gets or sets the AngularUnits.
	*/
	public final IAngularUnit getAngularUnit()
	{
		return _AngularUnit;
	}
	public final void setAngularUnit(IAngularUnit value)
	{
		_AngularUnit = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("PRIMEM[\"{0}\", {1}");
		sb.append(getName());
		sb.append(getLongitude());
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
		return String.format("<CS_PrimeMeridian Longitude=\"%1$s\" >%2$s%3$s</CS_PrimeMeridian>", getLongitude(), getInfoXml());
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
		if (!(obj instanceof PrimeMeridian))
		{
			return false;
		}
		PrimeMeridian prime = obj instanceof PrimeMeridian ? (PrimeMeridian)obj : null;
		return prime.getAngularUnit().EqualParams(this.getAngularUnit()) && prime.getLongitude() == this.getLongitude();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}