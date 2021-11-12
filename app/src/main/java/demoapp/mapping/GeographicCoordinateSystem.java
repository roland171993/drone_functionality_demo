package demoapp.mapping;


import java.util.concurrent.CopyOnWriteArrayList;

/**
 A coordinate system based on latitude and longitude. 
 
 
 Some geographic coordinate systems are Lat/Lon, and some are Lon/Lat. 
 You can find out which this is by examining the axes. You should also 
 check the angular units, since not all geographic coordinate systems 
 use degrees.
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class GeographicCoordinateSystem extends HorizontalCoordinateSystem implements IGeographicCoordinateSystem
{

	/** 
	 Creates an instance of a Geographic Coordinate System
	 
	 @param angularUnit Angular units
	 @param horizontalDatum Horizontal datum
	 @param primeMeridian Prime meridian
	 @param axisInfo Axis info
	 @param name Name
	 @param authority Authority name
	 @param authorityCode Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public GeographicCoordinateSystem(IAngularUnit angularUnit, IHorizontalDatum horizontalDatum, IPrimeMeridian primeMeridian, CopyOnWriteArrayList<AxisInfo> axisInfo, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks)
	{
		super(horizontalDatum, axisInfo, name, authority, authorityCode, alias, abbreviation, remarks);
		_AngularUnit = angularUnit;
		_PrimeMeridian = primeMeridian;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Predefined geographic coordinate systems

	/** 
	 Creates a decimal degrees geographic coordinate system based on the WGS84 ellipsoid, suitable for GPS measurements
	*/
	public static IGeographicCoordinateSystem getWGS84()
	{
		CopyOnWriteArrayList<AxisInfo> axes = new CopyOnWriteArrayList<AxisInfo>();
		axes.add(new AxisInfo("Lon", AxisOrientationEnum.East));
		axes.add(new AxisInfo("Lat", AxisOrientationEnum.North));
		return new GeographicCoordinateSystem(AngularUnit.getDegrees(), HorizontalDatum.getWGS84(), PrimeMeridian.getGreenwich(), axes, "WGS 84", "EPSG", 4326, "", "", "");
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IGeographicCoordinateSystem Members

	private IAngularUnit _AngularUnit;

	/** 
	 Gets or sets the angular units of the geographic coordinate system.
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
	 Gets units for dimension within coordinate system. Each dimension in 
	 the coordinate system has corresponding units.
	 
	 @param dimension Dimension
	 @return Unit
	*/
	@Override
	public IUnit GetUnits(int dimension)
	{
		return _AngularUnit;
	}
	private IPrimeMeridian _PrimeMeridian;

	/** 
	 Gets or sets the prime meridian of the geographic coordinate system.
	*/
	public final IPrimeMeridian getPrimeMeridian()
	{
		return _PrimeMeridian;
	}
	public final void setPrimeMeridian(IPrimeMeridian value)
	{
		_PrimeMeridian = value;
	}

	/** 
	 Gets the number of available conversions to WGS84 coordinates.
	*/
	public final int getNumConversionToWGS84()
	{
		return _WGS84ConversionInfo.size();
	}

	private CopyOnWriteArrayList<Wgs84ConversionInfo> _WGS84ConversionInfo;

	public final CopyOnWriteArrayList<Wgs84ConversionInfo> getWGS84ConversionInfo()
	{
		return _WGS84ConversionInfo;
	}
	public final void setWGS84ConversionInfo(CopyOnWriteArrayList<Wgs84ConversionInfo> value)
	{
		_WGS84ConversionInfo = value;
	}

	/** 
	 Gets details on a conversion to WGS84.
	*/
	public final Wgs84ConversionInfo GetWgs84ConversionInfo(int index)
	{
		return _WGS84ConversionInfo.get(index);
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("GEOGCS[\"%1$s\", %2$s, %3$s, %4$s",getName()));
			//Skip axis info if they contain default values
		if (getAxisInfo().size() != 2 || !getAxisInfo().get(0).getName().equals("Lon") || getAxisInfo().get(0).getOrientation() != AxisOrientationEnum.East || !getAxisInfo().get(1).getName().equals("Lat") || getAxisInfo().get(1).getOrientation() != AxisOrientationEnum.North)
		{
			for (int i = 0; i < getAxisInfo().size(); i++)
			{
				sb.append(String.format(", %1$s", GetAxis(i).getWKT()));
			}
		}
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
		StringBuilder sb = new StringBuilder();
		sb.append( "<CS_CoordinateSystem Dimension=\"{0}\"><CS_GeographicCoordinateSystem>{1}");
		sb.append(this.getDimension());
		sb.append(getInfoXml());
		for (AxisInfo ai : this.getAxisInfo())
		{
			sb.append(ai.getXML());
		}
		sb.append(String.format("%1$s%2$s%3$s</CS_GeographicCoordinateSystem></CS_CoordinateSystem>"));
		return sb.toString();
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
		if (!(obj instanceof GeographicCoordinateSystem))
		{
			return false;
		}
		GeographicCoordinateSystem gcs = obj instanceof GeographicCoordinateSystem ? (GeographicCoordinateSystem)obj : null;
		if (gcs.getDimension() != this.getDimension())
		{
			return false;
		}
		if (this.getWGS84ConversionInfo() != null && gcs.getWGS84ConversionInfo() == null)
		{
			return false;
		}
		if (this.getWGS84ConversionInfo() == null && gcs.getWGS84ConversionInfo() != null)
		{
			return false;
		}
		if (this.getWGS84ConversionInfo() != null && gcs.getWGS84ConversionInfo() != null)
		{
			if (this.getWGS84ConversionInfo().size() != gcs.getWGS84ConversionInfo().size())
			{
				return false;
			}
			for (int i = 0; i < this.getWGS84ConversionInfo().size(); i++)
			{
				if (!gcs.getWGS84ConversionInfo().get(i).equals(this.getWGS84ConversionInfo().get(i)))
				{
					return false;
				}
			}
		}
		if (this.getAxisInfo().size() != gcs.getAxisInfo().size())
		{
			return false;
		}
		for (int i = 0; i < gcs.getAxisInfo().size(); i++)
		{
			if (gcs.getAxisInfo().get(i).getOrientation() != this.getAxisInfo().get(i).getOrientation())
			{
				return false;
			}
		}
		return gcs.getAngularUnit().EqualParams(this.getAngularUnit()) && gcs.getHorizontalDatum().EqualParams(this.getHorizontalDatum()) && gcs.getPrimeMeridian().EqualParams(this.getPrimeMeridian());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}