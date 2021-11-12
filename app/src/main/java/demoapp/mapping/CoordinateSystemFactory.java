package demoapp.mapping;

import android.util.Xml;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 Builds up complex objects from simpler objects or values.
 
 
 <p>ICoordinateSystemFactory allows applications to make coordinate systems that 
 cannot be created by a <see cref="ICoordinateSystemAuthorityFactory"/>. This factory is very 
 flexible, whereas the authority factory is easier to use.</p>
 <p>So <see cref="ICoordinateSystemAuthorityFactory"/>can be used to make 'standard' coordinate 
 systems, and <see cref="CoordinateSystemFactory"/> can be used to make 'special' 
 coordinate systems.</p>
 <p>For example, the EPSG authority has codes for USA state plane coordinate systems 
 using the NAD83 datum, but these coordinate systems always use meters. EPSG does not 
 have codes for NAD83 state plane coordinate systems that use feet units. This factory
 lets an application create such a hybrid coordinate system.</p>
 
*/
public class CoordinateSystemFactory implements ICoordinateSystemFactory
{
	private Xml.Encoding Encoding;
	@Deprecated
	public final Xml.Encoding getEncoding()
	{
		return Encoding;
	}
	@Deprecated
	private void setEncoding(Xml.Encoding value)
	{
		Encoding = value;
	}



	/** 
	 Creates a <see cref="CreateGeocentricCoordinateSystem"/> from a <see cref="IHorizontalDatum">datum</see>, 
	 <see cref="ILinearUnit">linear unit</see> and <see cref="IPrimeMeridian"/>.
	 
	 @param name Name of geocentric coordinate system
	 @param datum Horizontal datum
	 @param linearUnit Linear unit
	 @param primeMeridian Prime meridian
	 @return Geocentric Coordinate System
	*/
	public final IGeocentricCoordinateSystem CreateGeocentricCoordinateSystem(String name, IHorizontalDatum datum, ILinearUnit linearUnit, IPrimeMeridian primeMeridian)
	{
		if (StringHelper.isNullOrEmpty(name))
		{
			throw new IllegalArgumentException("Invalid name");
		}

		CopyOnWriteArrayList<AxisInfo> info = new CopyOnWriteArrayList<AxisInfo>();
		info.add(new AxisInfo("X", AxisOrientationEnum.Other));
		info.add(new AxisInfo("Y", AxisOrientationEnum.Other));
		info.add(new AxisInfo("Z", AxisOrientationEnum.Other));
		return new GeocentricCoordinateSystem(datum, linearUnit, primeMeridian, info, name, "", -1, "", "", "");
	}
}