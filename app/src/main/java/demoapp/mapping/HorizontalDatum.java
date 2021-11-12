package demoapp.mapping;


/**
 Horizontal datum defining the standard datum information.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class HorizontalDatum extends Datum implements IHorizontalDatum
{
	/** 
	 Initializes a new instance of a horizontal datum
	 
	 @param ellipsoid Ellipsoid
	 @param toWgs84 Parameters for a Bursa Wolf transformation into WGS84
	 @param type Datum type
	 @param name Name
	 @param authority Authority name
	 @param code Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public HorizontalDatum(IEllipsoid ellipsoid, Wgs84ConversionInfo toWgs84, DatumType type, String name, String authority, long code, String alias, String remarks, String abbreviation)
	{
		super(type, name, authority, code, alias, remarks, abbreviation);
		_Ellipsoid = ellipsoid;
		_Wgs84ConversionInfo = toWgs84;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Predefined datums
	/** 
	 EPSG's WGS 84 datum has been the then current realisation. No distinction is made between the original WGS 84 
	 frame, WGS 84 (G730), WGS 84 (G873) and WGS 84 (G1150). Since 1997, WGS 84 has been maintained within 10cm of 
	 the then current ITRF.
	 
	 
	 <p>Area of use: World</p>
	 <p>Origin description: Defined through a consistent set of station coordinates. These have changed with time: by 0.7m 
	 on 29/6/1994 [WGS 84 (G730)], a further 0.2m on 29/1/1997 [WGS 84 (G873)] and a further 0.06m on 
	 20/1/2002 [WGS 84 (G1150)].</p>
	 
	*/
	public static HorizontalDatum getWGS84()
	{
		return new HorizontalDatum(Ellipsoid.getWGS84(),
				null,
				DatumType.HD_Geocentric,
				"World Geodetic System 1984", "EPSG", 6326, "", "EPSG's WGS 84 datum has been the then current realisation. No distinction is made between the original WGS 84 frame, WGS 84 (G730), WGS 84 (G873) and WGS 84 (G1150). Since 1997, WGS 84 has been maintained within 10cm of the then current ITRF.", "");
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IHorizontalDatum Members

	private IEllipsoid _Ellipsoid;

	/** 
	 Gets or sets the ellipsoid of the datum
	*/
	public final IEllipsoid getEllipsoid()
	{
		return _Ellipsoid;
	}
	public final void setEllipsoid(IEllipsoid value)
	{
		_Ellipsoid = value;
	}

	private Wgs84ConversionInfo _Wgs84ConversionInfo;
	/** 
	 Gets preferred parameters for a Bursa Wolf transformation into WGS84
	*/
	public final Wgs84ConversionInfo getWgs84Parameters()
	{
		return _Wgs84ConversionInfo;
	}
	public final void setWgs84Parameters(Wgs84ConversionInfo value)
	{
		_Wgs84ConversionInfo = value;
	}


	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("DATUM[\"%1$s\", %2$s", getName()));
		if (_Wgs84ConversionInfo != null)
		{
			sb.append(String.format(", %1$s", _Wgs84ConversionInfo.getWKT()));
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
		return String.format("<CS_HorizontalDatum DatumType=\"%1$s\">%2$s%3$s%4$s</CS_HorizontalDatum>", (getWgs84Parameters() == null ? "" : getWgs84Parameters().getXML()));
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
		if (!(obj instanceof HorizontalDatum))
		{
			return false;
		}
		HorizontalDatum datum = obj instanceof HorizontalDatum ? (HorizontalDatum)obj : null;
		if (datum.getWgs84Parameters() == null && this.getWgs84Parameters() != null)
		{
			return false;
		}
		if (datum.getWgs84Parameters() != null && !datum.getWgs84Parameters().equals(this.getWgs84Parameters()))
		{
			return false;
		}
		return (datum != null && this.getEllipsoid() != null && datum.getEllipsoid().EqualParams(this.getEllipsoid()) || datum == null && this.getEllipsoid() == null) && this.getDatumType() == datum.getDatumType();
	}
}