package demoapp.mapping;



/**
 The IEllipsoid interface defines the standard information stored with ellipsoid objects.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class Ellipsoid extends Info implements IEllipsoid
{
	/** 
	 Initializes a new instance of an Ellipsoid
	 
	 @param semiMajorAxis Semi major axis
	 @param semiMinorAxis Semi minor axis
	 @param inverseFlattening Inverse flattening
	 @param isIvfDefinitive Inverse Flattening is definitive for this ellipsoid (Semi-minor axis will be overridden)
	 @param axisUnit Axis unit
	 @param name Name
	 @param authority Authority name
	 @param code Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public Ellipsoid(double semiMajorAxis, double semiMinorAxis, double inverseFlattening, boolean isIvfDefinitive, ILinearUnit axisUnit, String name, String authority, long code, String alias, String abbreviation, String remarks)
	{
		super(name, authority, code, alias, abbreviation, remarks);
		setSemiMajorAxis(semiMajorAxis);
		setInverseFlattening(inverseFlattening);
		setAxisUnit(axisUnit);
		setIsIvfDefinitive(isIvfDefinitive);
		if (isIvfDefinitive && (inverseFlattening == 0 || Double.isInfinite(inverseFlattening)))
		{
			setSemiMinorAxis(semiMajorAxis);
		}
		else if (isIvfDefinitive)
		{
			setSemiMinorAxis((1.0 - (1.0 / getInverseFlattening())) * semiMajorAxis);
		}
		else
		{
			setSemiMinorAxis(semiMinorAxis);
		}
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Predefined ellipsoids
	/** 
	 WGS 84 ellipsoid
	 
	 
	 Inverse flattening derived from four defining parameters 
	 (semi-major axis;
	 C20 = -484.16685*10e-6;
	 earth's angular velocity w = 7292115e11 rad/sec;
	 gravitational constant GM = 3986005e8 m*m*m/s/s).
	 
	*/
	public static Ellipsoid getWGS84()
	{
		return new Ellipsoid(6378137, 0, 298.257223563, true, LinearUnit.getMetre(), "WGS 84", "EPSG", 7030, "WGS84", "", "Inverse flattening derived from four defining parameters (semi-major axis; C20 = -484.16685*10e-6; earth's angular velocity w = 7292115e11 rad/sec; gravitational constant GM = 3986005e8 m*m*m/s/s).");
	}

	/** 
	 WGS 72 Ellipsoid
	*/
	public static Ellipsoid getWGS72()
	{
		return new Ellipsoid(6378135.0, 0, 298.26, true, LinearUnit.getMetre(), "WGS 72", "EPSG", 7043, "WGS 72", "", "");
	}

	/** 
	 GRS 1980 / International 1979 ellipsoid
	 
	 
	 Adopted by IUGG 1979 Canberra.
	 Inverse flattening is derived from
	 geocentric gravitational constant GM = 3986005e8 m*m*m/s/s;
	 dynamic form factor J2 = 108263e8 and Earth's angular velocity = 7292115e-11 rad/s.")
	 
	*/
	public static Ellipsoid getGRS80()
	{
		return new Ellipsoid(6378137, 0, 298.257222101, true, LinearUnit.getMetre(), "GRS 1980", "EPSG", 7019, "International 1979", "", "Adopted by IUGG 1979 Canberra.  Inverse flattening is derived from geocentric gravitational constant GM = 3986005e8 m*m*m/s/s; dynamic form factor J2 = 108263e8 and Earth's angular velocity = 7292115e-11 rad/s.");
	}

	/** 
	 International 1924 / Hayford 1909 ellipsoid
	 
	 
	 Described as a=6378388 m. and b=6356909m. from which 1/f derived to be 296.95926. 
	 The figure was adopted as the International ellipsoid in 1924 but with 1/f taken as
	 297 exactly from which b is derived as 6356911.946m.
	 
	*/
	public static Ellipsoid getInternational1924()
	{
		return new Ellipsoid(6378388, 0, 297, true, LinearUnit.getMetre(), "International 1924", "EPSG", 7022, "Hayford 1909", "", "Described as a=6378388 m. and b=6356909 m. from which 1/f derived to be 296.95926. The figure was adopted as the International ellipsoid in 1924 but with 1/f taken as 297 exactly from which b is derived as 6356911.946m.");
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IEllipsoid Members

	/** 
	 Gets or sets the value of the semi-major axis.
	*/
	private double SemiMajorAxis;
	public final double getSemiMajorAxis()
	{
		return SemiMajorAxis;
	}
	public final void setSemiMajorAxis(double value)
	{
		SemiMajorAxis = value;
	}

	/** 
	 Gets or sets the value of the semi-minor axis.
	*/
	private double SemiMinorAxis;
	public final double getSemiMinorAxis()
	{
		return SemiMinorAxis;
	}
	public final void setSemiMinorAxis(double value)
	{
		SemiMinorAxis = value;
	}

	/** 
	 Gets or sets the value of the inverse of the flattening constant of the ellipsoid.
	*/
	private double InverseFlattening;
	public final double getInverseFlattening()
	{
		return InverseFlattening;
	}
	public final void setInverseFlattening(double value)
	{
		InverseFlattening = value;
	}

	/** 
	 Gets or sets the value of the axis unit.
	*/
	private ILinearUnit AxisUnit;
	public final ILinearUnit getAxisUnit()
	{
		return AxisUnit;
	}
	public final void setAxisUnit(ILinearUnit value)
	{
		AxisUnit = value;
	}

	/** 
	 Tells if the Inverse Flattening is definitive for this ellipsoid. Some ellipsoids use 
	 the IVF as the defining value, and calculate the polar radius whenever asked. Other
	 ellipsoids use the polar radius to calculate the IVF whenever asked. This 
	 distinction can be important to avoid floating-point rounding errors.
	*/
	private boolean IsIvfDefinitive;
	public final boolean getIsIvfDefinitive()
	{
		return IsIvfDefinitive;
	}
	public final void setIsIvfDefinitive(boolean value)
	{
		IsIvfDefinitive = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SPHEROID[\"{0}\", {1}, {2}");
		sb.append(getName());
		sb.append(getSemiMajorAxis());
		sb.append(getInverseFlattening());
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
		return String.format("<CS_Ellipsoid SemiMajorAxis=\"%1$s\" SemiMinorAxis=\"%2$s\" InverseFlattening=\"%3$s\" IvfDefinitive=\"%4$s\">%5$s%6$s</CS_Ellipsoid>", getSemiMajorAxis(), getSemiMinorAxis(), getInverseFlattening(), (getIsIvfDefinitive() ? 1 : 0), getInfoXml());
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
		if (!(obj instanceof Ellipsoid))
		{
			return false;
		}
		Ellipsoid e = obj instanceof Ellipsoid ? (Ellipsoid)obj : null;
		return (e.getInverseFlattening() == this.getInverseFlattening() && e.getIsIvfDefinitive() == this.getIsIvfDefinitive() && e.getSemiMajorAxis() == this.getSemiMajorAxis() && e.getSemiMinorAxis() == this.getSemiMinorAxis() && e.getAxisUnit().EqualParams(this.getAxisUnit()));
	}
}