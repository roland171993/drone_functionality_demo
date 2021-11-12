package demoapp.mapping;


import java.util.concurrent.CopyOnWriteArrayList;

/**
 A 3D coordinate system, with its origin at the center of the Earth.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class GeocentricCoordinateSystem extends CoordinateSystem implements IGeocentricCoordinateSystem
{
	public GeocentricCoordinateSystem(IHorizontalDatum datum, ILinearUnit linearUnit, IPrimeMeridian primeMeridian, CopyOnWriteArrayList<AxisInfo> axisinfo, String name, String authority, long code, String alias, String remarks, String abbreviation)
	{
		super(name, authority, code, alias, abbreviation, remarks);
		_HorizontalDatum = datum;
		_LinearUnit = linearUnit;
		_Primemeridan = primeMeridian;
		if (axisinfo.size() != 3)
		{
			throw new IllegalArgumentException("Axis info should contain three axes for geocentric coordinate systems");
		}
		super.setAxisInfo(axisinfo);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Predefined geographic coordinate systems

	/** 
	 Creates a geocentric coordinate system based on the WGS84 ellipsoid, suitable for GPS measurements
	*/
	public static IGeocentricCoordinateSystem getWGS84()
	{
		return (new CoordinateSystemFactory()).CreateGeocentricCoordinateSystem("WGS84 Geocentric", HorizontalDatum.getWGS84(), LinearUnit.getMetre(), PrimeMeridian.getGreenwich());
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IGeocentricCoordinateSystem Members

	private IHorizontalDatum _HorizontalDatum;

	/** 
	 Returns the HorizontalDatum. The horizontal datum is used to determine where
	 the centre of the Earth is considered to be. All coordinate points will be 
	 measured from the centre of the Earth, and not the surface.
	*/
	public final IHorizontalDatum getHorizontalDatum()
	{
		return _HorizontalDatum;
	}
	public final void setHorizontalDatum(IHorizontalDatum value)
	{
		_HorizontalDatum = value;
	}

	private ILinearUnit _LinearUnit;

	/** 
	 Gets the units used along all the axes.
	*/
	public final ILinearUnit getLinearUnit()
	{
		return _LinearUnit;
	}
	public final void setLinearUnit(ILinearUnit value)
	{
		_LinearUnit = value;
	}

	/** 
	 Gets units for dimension within coordinate system. Each dimension in 
	 the coordinate system has corresponding units.
	 
	 @param dimension Dimension
	 @return Unit
	*/
	@Override
	public IUnit GetUnits(int dimension)
	{
		return _LinearUnit;
	}

	private IPrimeMeridian _Primemeridan;

	/** 
	 Returns the PrimeMeridian.
	*/
	public final IPrimeMeridian getPrimeMeridian()
	{
		return _Primemeridan;
	}
	public final void setPrimeMeridian(IPrimeMeridian value)
	{
		_Primemeridan = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		return "ROLAND HAS COMMENT in GeocentricCoordinateSystem.java getWKT()";
//		StringBuilder sb = new StringBuilder();
//		sb.append(String.format("GEOCCS[\"%1$s\", %2$s, %3$s, %4$s", getName(), getHorizontalDatum().WKT, getPrimeMeridian().WKT, getLinearUnit().WKT));
//			//Skip axis info if they contain default values
//		if (getAxisInfo().size() != 3 || !getAxisInfo().get(0).getName().equals("X") || getAxisInfo().get(0).getOrientation() != AxisOrientationEnum.Other || !getAxisInfo().get(1).getName().equals("Y") || getAxisInfo().get(1).getOrientation() != AxisOrientationEnum.East || !getAxisInfo().get(2).getName().equals("Z") || getAxisInfo().get(2).getOrientation() != AxisOrientationEnum.North)
//		{
//			for (int i = 0; i < getAxisInfo().size(); i++)
//			{
//				sb.append(String.format(", %1$s", GetAxis(i).getWKT()));
//			}
//		}
//		if (!StringHelper.isNullOrEmpty(getAuthority()) && getAuthorityCode() > 0)
//		{
//			sb.append(String.format(", AUTHORITY[\"%1$s\", \"%2$s\"]", getAuthority(), getAuthorityCode()));
//		}
//		sb.append("]");
//		return sb.toString();
	}

	/** 
	 Gets an XML representation of this object
	*/
	@Override
	public String getXML()
	{
		return "ROLAND HAS COMMENT in GeocentricCoordinateSystem.java getXML()";
//		StringBuilder sb = new StringBuilder();
//		sb.AppendFormat(CultureInfo.InvariantCulture.NumberFormat, "<CS_CoordinateSystem Dimension=\"{0}\"><CS_GeocentricCoordinateSystem>{1}", this.getDimension(), getInfoXml());
//		for (AxisInfo ai : this.getAxisInfo())
//		{
//			sb.append(ai.getXML());
//		}
//		sb.append(String.format("%1$s%2$s%3$s</CS_GeocentricCoordinateSystem></CS_CoordinateSystem>", getHorizontalDatum().XML, getLinearUnit().XML, getPrimeMeridian().XML));
//		return sb.toString();
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
		if (!(obj instanceof GeocentricCoordinateSystem))
		{
			return false;
		}
		GeocentricCoordinateSystem gcc = obj instanceof GeocentricCoordinateSystem ? (GeocentricCoordinateSystem)obj : null;
		return gcc.getHorizontalDatum().EqualParams(this.getHorizontalDatum()) && gcc.getLinearUnit().EqualParams(this.getLinearUnit()) && gcc.getPrimeMeridian().EqualParams(this.getPrimeMeridian());
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}