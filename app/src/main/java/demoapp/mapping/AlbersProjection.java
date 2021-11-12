package demoapp.mapping;


/**
		Implements the Albers projection.
 
 
 	<p>Implements the Albers projection. The Albers projection is most commonly
 	used to project the United States of America. It gives the northern
 	border with Canada a curved appearance.</p>
 	
		<p>The <a href="http://www.geog.mcgill.ca/courses/geo201/mapproj/naaeana.gif">Albers Equal Area</a>
		projection has the property that the area bounded
		by any pair of parallels and meridians is exactly reproduced between the 
		image of those parallels and meridians in the projected domain, that is,
		the projection preserves the correct area of the earth though distorts
		direction, distance and shape somewhat.</p>
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class AlbersProjection extends MapProjection
{
	private final double _c; //constant c
	private final double _ro0;
	private final double _n;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Constructors

	/** 
	 Creates an instance of an Albers projection object.
	 
	 @param parameters List of parameters to initialize the projection.
	 
	 <p>The parameters this projection expects are listed below.</p>
	 <list type="table">
	 <listheader><term>Items</term><description>Descriptions</description></listheader>
	 <item><term>latitude_of_false_origin</term><description>The latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
	 <item><term>longitude_of_false_origin</term><description>The longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
	 <item><term>latitude_of_1st_standard_parallel</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.</description></item>
	 <item><term>latitude_of_2nd_standard_parallel</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.</description></item>
	 <item><term>easting_at_false_origin</term><description>The easting value assigned to the false origin.</description></item>
	 <item><term>northing_at_false_origin</term><description>The northing value assigned to the false origin.</description></item>
	 </list>
	 
	*/
	public AlbersProjection(Iterable<ProjectionParameter> parameters)
	{
		this(parameters, null);
	}

	/** 
	 Creates an instance of an Albers projection object.
	 
	 
	 <p>The parameters this projection expects are listed below.</p>
	 <list type="table">
	 <listheader><term>Items</term><description>Descriptions</description></listheader>
	 <item><term>latitude_of_center</term><description>The latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
	 <item><term>longitude_of_center</term><description>The longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
	 <item><term>standard_parallel_1</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.</description></item>
	 <item><term>standard_parallel_2</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.</description></item>
	 <item><term>false_easting</term><description>The easting value assigned to the false origin.</description></item>
	 <item><term>false_northing</term><description>The northing value assigned to the false origin.</description></item>
	 </list>
	 
	 @param parameters List of parameters to initialize the projection.
	 @param inverse Indicates whether the projection forward (meters to degrees or degrees to meters).
	*/
	protected AlbersProjection(Iterable<ProjectionParameter> parameters, AlbersProjection inverse)
	{
		super(parameters, inverse);
		setName("Albers_Conic_Equal_Area");

		double lat0 = Utils.clone(lat_origin);
		double lat1 = Degrees2Radians(_Parameters.GetParameterValue("standard_parallel_1"));
		double lat2 = Degrees2Radians(_Parameters.GetParameterValue("standard_parallel_2"));

		if (Math.abs(lat1 + lat2) < Double.MIN_VALUE)
		{
			throw new IllegalArgumentException("Equal latitudes for standard parallels on opposite sides of Equator.");
		}

		double alpha1 = alpha(lat1);
		double alpha2 = alpha(lat2);

		double m1 = Math.cos(lat1) / Math.sqrt(1 - Utils.clone(_es) * Math.pow(Math.sin(lat1), 2));
		double m2 = Math.cos(lat2) / Math.sqrt(1 - Utils.clone(_es) * Math.pow(Math.sin(lat2), 2));

		_n = (Math.pow(m1, 2) - Math.pow(m2, 2)) / (alpha2 - alpha1);
		_c = Math.pow(m1, 2) + (Utils.clone(_n) * alpha1);

		_ro0 = Ro(alpha(lat0));
		/*
		double sin_p0 = Math.Sin(lat0);
		double cos_p0 = Math.Cos(lat0);
		double q0 = qsfnz(e, sin_p0, cos_p0);

		double sin_p1 = Math.Sin(lat1);
		double cos_p1 = Math.Cos(lat1);
		double m1 = msfnz(e,sin_p1,cos_p1);
		double q1 = qsfnz(e,sin_p1,cos_p1);


		double sin_p2 = Math.Sin(lat2);
		double cos_p2 = Math.Cos(lat2);
		double m2 = msfnz(e,sin_p2,cos_p2);
		double q2 = qsfnz(e,sin_p2,cos_p2);

		if (Math.Abs(lat1 - lat2) > EPSLN)
			ns0 = (m1 * m1 - m2 * m2)/ (q2 - q1);
		else
			ns0 = sin_p1;
		C = m1 * m1 + ns0 * q1;
		rh = this._semiMajor * Math.Sqrt(C - ns0 * q0)/ns0;
		*/
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Public methods

	/** 
	 Converts coordinates in decimal degrees to projected meters.
	 
	 @param lonlat The point in decimal degrees.
	 @return Point in projected meters
	*/
	@Override
	protected double[] RadiansToMeters(double[] lonlat)
	{
		double dLongitude = lonlat[0];
		double dLatitude = lonlat[1];

		double a = alpha(dLatitude);
		double ro = Ro(a);
		double theta = Utils.clone(_n) * (dLongitude - central_meridian);
		dLongitude = ro * Math.sin(theta);
		dLatitude = Double.parseDouble(_ro0+"") - (ro * Math.cos(theta));

		return lonlat.length == 2 ? new double[] {dLongitude, dLatitude} : new double[] {dLongitude, dLatitude, lonlat[2]};
	}

	/** 
	 Converts coordinates in projected meters to decimal degrees.
	 
	 @param p Point in meters
	 @return Transformed point in decimal degrees
	*/
	@Override
	protected double[] MetersToRadians(double[] p)
	{
		double theta = Math.atan((p[0]) / (Double.parseDouble(_ro0+"") - (p[1])));
		double ro = Math.sqrt(Math.pow(p[0], 2) + Math.pow(Double.parseDouble(_ro0+"") - (p[1]), 2));
		double q = (Double.parseDouble(_c+"") - Math.pow(ro, 2) * Math.pow(Utils.clone(_n), 2) / Math.pow(Utils.clone(this._semiMajor), 2)) / _n;
		double b = Math.sin(q / (1 - ((1 - Utils.clone(_es)) / (2 * Utils.clone(_e))) * Math.log((1 - _e) / (1 + _e))));

		double lat = Math.asin(q * 0.5);
		double preLat = Double.MAX_VALUE;
		int iterationCounter = 0;
		while (Math.abs(lat - preLat) > 0.000001)
		{
			preLat = lat;
			double sin = Math.sin(lat);
			double e2sin2 = Utils.clone(_es) * Math.pow(sin, 2);
			lat += (Math.pow(1 - e2sin2, 2) / (2 * Math.cos(lat))) * ((q / (1 - Utils.clone(_es))) - sin / (1 - e2sin2) + 1 / (2 * Utils.clone(_e)) * Math.log((1 - Utils.clone(_e) * sin) / (1 + Utils.clone(_e) * sin)));
			iterationCounter++;
			if (iterationCounter > 25)
			{
				throw new IllegalArgumentException("Transformation failed to converge in Albers backwards transformation");
			}
		}
		double lon = central_meridian + (theta / Utils.clone(_n));
		if (p.length == 2)
		{
			return new double[] {lon, lat};
		}
		return new double[] {lon, lat, p[2]};
	}

	/** 
	 Returns the inverse of this projection.
	 
	 @return IMathTransform that is the reverse of the current projection.
	*/
	@Override
	public IMathTransform Inverse()
	{
		if (_inverse == null)
		{
			_inverse = new AlbersProjection(_Parameters.ToProjectionParameter(), this);
		}
		return _inverse;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Math helper functions

	//private double ToAuthalic(double lat)
	//{
	//    return Math.Atan(Q(lat) / Q(Math.PI * 0.5));
	//}
	//private double Q(double angle)
	//{
	//    double sin = Math.Sin(angle);
	//    double esin = e * sin;
	//    return Math.Abs(sin / (1 - Math.Pow(esin, 2)) - 0.5 * e) * Math.Log((1 - esin) / (1 + esin)));
	//}
	private double alpha(double lat)
	{
		double sin = Math.sin(lat);
		double sinsq = Math.pow(sin,2);
		return (1 - Utils.clone(_es)) * (((sin / (1 - Utils.clone(_es) * sinsq)) - 1 / (2 * Utils.clone(_e)) * Math.log((1 - Utils.clone(_e) * sin) / (1 + Utils.clone(_e) * sin))));
	}

	private double Ro(double a)
	{
		return Utils.clone(_semiMajor) * Math.sqrt((Double.parseDouble(_c+"") - Utils.clone(_n) * a)) / Utils.clone(_n);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}