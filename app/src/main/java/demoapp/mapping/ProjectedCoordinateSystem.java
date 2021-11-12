package demoapp.mapping;


import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;


/** 
 A 2D cartographic coordinate system.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class ProjectedCoordinateSystem extends HorizontalCoordinateSystem implements IProjectedCoordinateSystem
{
	/** 
	 Initializes a new instance of a projected coordinate system
	 
	 @param datum Horizontal datum
	 @param geographicCoordinateSystem Geographic coordinate system
	 @param linearUnit Linear unit
	 @param projection Projection
	 @param axisInfo Axis info
	 @param name Name
	 @param authority Authority name
	 @param code Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public ProjectedCoordinateSystem(IHorizontalDatum datum, IGeographicCoordinateSystem geographicCoordinateSystem, ILinearUnit linearUnit, IProjection projection, CopyOnWriteArrayList<AxisInfo> axisInfo, String name, String authority, long code, String alias, String remarks, String abbreviation)
	{
		super(datum, axisInfo, name, authority, code, alias, abbreviation, remarks);
		_GeographicCoordinateSystem = geographicCoordinateSystem;
		_LinearUnit = linearUnit;
		_Projection = projection;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Predefined projected coordinate systems

	/** 
	 Universal Transverse Mercator - WGS84
	 
	 @param zone UTM zone
	 @param zoneIsNorth true of Northern hemisphere, false if southern
	 @return UTM/WGS84 coordsys
	*/
	public static IProjectedCoordinateSystem WGS84_UTM(int zone, boolean zoneIsNorth)
	{
		CopyOnWriteArrayList<ProjectionParameter> pInfo = new CopyOnWriteArrayList<ProjectionParameter>();
		pInfo.add(new ProjectionParameter("latitude_of_origin", 0));
		pInfo.add(new ProjectionParameter("central_meridian", zone * 6 - 183));
		pInfo.add(new ProjectionParameter("scale_factor", 0.9996));
		pInfo.add(new ProjectionParameter("false_easting", 500000));
		pInfo.add(new ProjectionParameter("false_northing", zoneIsNorth ? 0 : 10000000));
		//IProjection projection = cFac.CreateProjection("UTM" + Zone.ToString() + (ZoneIsNorth ? "N" : "S"), "Transverse_Mercator", parameters);
		Projection proj = new Projection("Transverse_Mercator", pInfo, "UTM" + (new Integer(zone)).toString() + (zoneIsNorth ? "N" : "S"), "EPSG", 32600 + zone + (zoneIsNorth ? 0 : 100), "", "", "");
		CopyOnWriteArrayList<AxisInfo> axes = new CopyOnWriteArrayList<AxisInfo>(Arrays.asList(new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North)));
		return new ProjectedCoordinateSystem(HorizontalDatum.getWGS84(), GeographicCoordinateSystem.getWGS84(), LinearUnit.getMetre(), proj, axes, "WGS 84 / UTM zone " + (new Integer(zone)).toString() + (zoneIsNorth ? "N" : "S"), "EPSG", 32600 + zone + (zoneIsNorth ? 0 : 100), "", "Large and medium scale topographic mapping and engineering survey.", "");
	}

	/** 
	 Gets a WebMercator coordinate reference system
	*/
	public static IProjectedCoordinateSystem getWebMercator()
	{
		CopyOnWriteArrayList<ProjectionParameter> pInfo = new CopyOnWriteArrayList<ProjectionParameter>(Arrays.asList(new ProjectionParameter("latitude_of_origin", 0.0), new ProjectionParameter("central_meridian", 0.0), new ProjectionParameter("false_easting", 0.0), new ProjectionParameter("false_northing", 0.0)));

		Projection proj = new Projection("Popular Visualisation Pseudo-Mercator", pInfo, "Popular Visualisation Pseudo-Mercator", "EPSG", 3856, "Pseudo-Mercator", "", "");

		CopyOnWriteArrayList<AxisInfo> axes = new CopyOnWriteArrayList<AxisInfo>(Arrays.asList(new AxisInfo("East", AxisOrientationEnum.East), new AxisInfo("North", AxisOrientationEnum.North)));

		return new ProjectedCoordinateSystem(HorizontalDatum.getWGS84(), GeographicCoordinateSystem.getWGS84(), LinearUnit.getMetre(), proj, axes, "WGS 84 / Pseudo-Mercator", "EPSG", 3857, "WGS 84 / Popular Visualisation Pseudo-Mercator", "Certain Web mapping and visualisation applications." + "Uses spherical development of ellipsoidal coordinates. Relative to an ellipsoidal development errors of up to 800 metres in position and 0.7 percent in scale may arise. It is not a recognised geodetic system: see WGS 84 / World Mercator (CRS code 3395).", "WebMercator") { };
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IProjectedCoordinateSystem Members

	private IGeographicCoordinateSystem _GeographicCoordinateSystem;

	/** 
	 Gets or sets the GeographicCoordinateSystem.
	*/
	public final IGeographicCoordinateSystem getGeographicCoordinateSystem()
	{
		return _GeographicCoordinateSystem;
	}
	public final void setGeographicCoordinateSystem(IGeographicCoordinateSystem value)
	{
		_GeographicCoordinateSystem = value;
	}

	private ILinearUnit _LinearUnit;

	/** 
	 Gets or sets the <see cref="LinearUnit">LinearUnits</see>. The linear unit must be the same as the <see cref="CoordinateSystem"/> units.
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

	private IProjection _Projection;

	/** 
	 Gets or sets the projection
	*/
	public final IProjection getProjection()
	{
		return _Projection;
	}
	public final void setProjection(IProjection value)
	{
		_Projection = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		return "getWKT DISABLE BY ROLAND";
//		StringBuilder sb = new StringBuilder();
//		sb.append(String.format("PROJCS[\"%1$s\", %2$s, %3$s, %4$s", getName(), getGeographicCoordinateSystem().WKT, getLinearUnit().WKT, getProjection().WKT));
//		for (int i = 0;i < getProjection().NumParameters;i++)
//		{
//			sb.AppendFormat(CultureInfo.InvariantCulture.NumberFormat, ", {0}", getProjection().GetParameter(i).WKT);
//		}
//			//sb.AppendFormat(", {0}", LinearUnit.WKT);
//			//Skip authority and code if not defined
//		if (!tangible.StringHelper.isNullOrEmpty(getAuthority()) && getAuthorityCode() > 0)
//		{
//			sb.append(String.format(", AUTHORITY[\"%1$s\", \"%2$s\"]", getAuthority(), getAuthorityCode()));
//		}
//			//Skip axis info if they contain default values
//		if (getAxisInfo().size() != 2 || !getAxisInfo().get(0).getName().equals("X") || getAxisInfo().get(0).getOrientation() != AxisOrientationEnum.East || !getAxisInfo().get(1).getName().equals("Y") || getAxisInfo().get(1).getOrientation() != AxisOrientationEnum.North)
//		{
//			for (int i = 0; i < getAxisInfo().size(); i++)
//			{
//				sb.append(String.format(", %1$s", GetAxis(i).getWKT()));
//			}
//		}
//			//if (!String.IsNullOrEmpty(Authority) && AuthorityCode > 0)
//			//    sb.AppendFormat(", AUTHORITY[\"{0}\", \"{1}\"]", Authority, AuthorityCode);
//		sb.append("]");
//		return sb.toString();
	}

	/** 
	 Gets an XML representation of this object.
	*/
	@Override
	public String getXML()
	{
		return "getXML DISABLE BY ROLAND";
//		StringBuilder sb = new StringBuilder();
//		sb.AppendFormat(CultureInfo.InvariantCulture.NumberFormat, "<CS_CoordinateSystem Dimension=\"{0}\"><CS_ProjectedCoordinateSystem>{1}", this.getDimension(), getInfoXml());
//		for (AxisInfo ai : this.getAxisInfo())
//		{
//			sb.append(ai.getXML());
//		}
//
//		sb.append(String.format("%1$s%2$s%3$s</CS_ProjectedCoordinateSystem></CS_CoordinateSystem>", getGeographicCoordinateSystem().XML, getLinearUnit().XML, getProjection().XML));
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
		if (!(obj instanceof ProjectedCoordinateSystem))
		{
			return false;
		}
		ProjectedCoordinateSystem pcs = obj instanceof ProjectedCoordinateSystem ? (ProjectedCoordinateSystem)obj : null;
		if (pcs.getDimension() != this.getDimension())
		{
			return false;
		}
		for (int i = 0; i < pcs.getDimension(); i++)
		{
			if (pcs.GetAxis(i).getOrientation() != this.GetAxis(i).getOrientation())
			{
				return false;
			}
			if (!pcs.GetUnits(i).EqualParams(this.GetUnits(i)))
			{
				return false;
			}
		}

		return pcs.getGeographicCoordinateSystem().EqualParams(this.getGeographicCoordinateSystem()) && pcs.getHorizontalDatum().EqualParams(this.getHorizontalDatum()) && pcs.getLinearUnit().EqualParams(this.getLinearUnit()) && pcs.getProjection().EqualParams(this.getProjection());
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}