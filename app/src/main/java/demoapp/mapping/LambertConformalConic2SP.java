package demoapp.mapping;


/**
 Implemetns the Lambert Conformal Conic 2SP Projection.
 
 
 <p>The Lambert Conformal Conic projection is a standard projection for presenting maps
 of land areas whose East-West extent is large compared with their North-South extent.
 This projection is "conformal" in the sense that lines of latitude and longitude, 
 which are perpendicular to one another on the earth's surface, are also perpendicular
 to one another in the projected domain.</p>
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class LambertConformalConic2SP extends MapProjection
{

	//double _falseEasting;
	//double _falseNorthing;

	//private double es=0;              /* eccentricity squared         */
	//private double e=0;               /* eccentricity                 */
	//private double center_lon=0;      /* center longituted            */
	//private double center_lat=0;      /* cetner latitude              */
	private double ns = 0; // ratio of angle between meridian
	private  double f0 = 0; // flattening of ellipsoid
	private  double rh = 0; // height above ellipsoid

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Constructors

	/** 
	 Creates an instance of an LambertConformalConic2SPProjection projection object.
	 
	 
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
	 
	 @param parameters List of parameters to initialize the projection.
	*/
	public LambertConformalConic2SP(Iterable<ProjectionParameter> parameters)
	{
		this(parameters, null);
	}

	/** 
	 Creates an instance of an Albers projection object.
	 
	 
	 <p>The parameters this projection expects are listed below.</p>
	 <list type="table">
	 <listheader><term>Parameter</term><description>Description</description></listheader>
	 <item><term>latitude_of_origin</term><description>The latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
	 <item><term>central_meridian</term><description>The longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.</description></item>
	 <item><term>standard_parallel_1</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.</description></item>
	 <item><term>standard_parallel_2</term><description>For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.</description></item>
	 <item><term>false_easting</term><description>The easting value assigned to the false origin.</description></item>
	 <item><term>false_northing</term><description>The northing value assigned to the false origin.</description></item>
	 </list>
	 
	 @param parameters List of parameters to initialize the projection.
	 @param inverse Indicates whether the projection forward (meters to degrees or degrees to meters).
	*/
	protected LambertConformalConic2SP(Iterable<ProjectionParameter> parameters, LambertConformalConic2SP inverse)
	{
		super(parameters, inverse);
		setName("Lambert_Conformal_Conic_2SP");
		setAuthority("EPSG");
		setAuthorityCode(9802);

		//Check for missing parameters
		double lat1 = Degrees2Radians(_Parameters.GetParameterValue("standard_parallel_1"));
		double lat2 = Degrees2Radians(_Parameters.GetParameterValue("standard_parallel_2"));

		double sin_po; // sin value
		double cos_po; // cos value
		double con; // temporary variable
		double ms1; // small m 1
		double ms2; // small m 2
		double ts0; // small t 0
		double ts1; // small t 1
		double ts2; // small t 2



		/* Standard Parallels cannot be equal and on opposite sides of the equator
		------------------------------------------------------------------------*/
		if (Math.abs(lat1 + lat2) < EPSLN)
		{
			//Debug.Assert(true,"LambertConformalConic:LambertConformalConic() - Equal Latitiudes for St. Parallels on opposite sides of equator");
			throw new IllegalArgumentException("Equal latitudes for St. Parallels on opposite sides of equator.");
		}

		OutObject<Double> tempOut_sin_po = new OutObject<Double>();
		OutObject<Double> tempOut_cos_po = new OutObject<Double>();
		sincos(lat1, tempOut_sin_po, tempOut_cos_po);
	cos_po = tempOut_cos_po.argValue;
	sin_po = tempOut_sin_po.argValue;
		con = sin_po;
		ms1 = msfnz(Utils.clone(_e), sin_po, cos_po);
		ts1 = tsfnz(Utils.clone(_e), lat1, sin_po);
		OutObject<Double> tempOut_sin_po2 = new OutObject<Double>();
		OutObject<Double> tempOut_cos_po2 = new OutObject<Double>();
		sincos(lat2, tempOut_sin_po2, tempOut_cos_po2);
	cos_po = tempOut_cos_po2.argValue;
	sin_po = tempOut_sin_po2.argValue;
		ms2 = msfnz(Utils.clone(_e), sin_po, cos_po);
		ts2 = tsfnz(Utils.clone(_e), lat2, sin_po);
		sin_po = Math.sin(Utils.clone(lat_origin));
		ts0 = tsfnz(Utils.clone(_e), Utils.clone(lat_origin), sin_po);

		if (Math.abs(lat1 - lat2) > EPSLN)
		{
			ns = Math.log(ms1 / ms2) / Math.log(ts1 / ts2);
		}
		else
		{
			ns = con;
		}
		f0 = ms1 / (Utils.clone(ns) * Math.pow(ts1,Utils.clone(ns)));
		rh = Utils.clone(_semiMajor) * Utils.clone(f0) * Math.pow(ts0,Utils.clone(ns));
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

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

		double con; // temporary angle variable
		double rh1; // height above ellipsoid
		double sinphi; // sin value
		double theta; // angle
		double ts; // small value t


		con = Math.abs(Math.abs(dLatitude) - HALF_PI);
		if (con > EPSLN)
		{
			sinphi = Math.sin(dLatitude);
			ts = tsfnz(Utils.clone(_e), dLatitude, sinphi);
			rh1 = Utils.clone(_semiMajor) * Utils.clone(f0) * Math.pow(ts,Utils.clone(ns));
		}
		else
		{
			con = dLatitude * Utils.clone(ns);
			if (con <= 0)
			{
				throw new IllegalArgumentException();
			}
			rh1 = 0;
		}
		theta = Utils.clone(ns) * adjust_lon(dLongitude - central_meridian);
		dLongitude = rh1 * Math.sin(theta);
		dLatitude = Utils.clone(rh) - rh1 * Math.cos(theta);

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
		double dLongitude = Double.NaN;
		double dLatitude = Double.NaN;

		double rh1; // height above ellipsoid
		double con; // sign variable
		double ts; // small t
		double theta; // angle
		long flag; // error flag

		flag = 0;
		double dX = p[0];
		double dY = Utils.clone(rh) - p[1];
		if (Utils.clone(ns) > 0)
		{
			rh1 = Math.sqrt(dX * dX + dY * dY);
			con = 1.0;
		}
		else
		{
			rh1 = -Math.sqrt(dX * dX + dY * dY);
			con = -1.0;
		}
		theta = 0.0;
		if (rh1 != 0)
		{
			theta = Math.atan2((con * dX),(con * dY));
		}
		if ((rh1 != 0) || (Utils.clone(ns) > 0.0))
		{
			con = 1.0 / Utils.clone(ns);
			ts = Math.pow((rh1 / (Utils.clone(_semiMajor) * Utils.clone(f0))),con);
			OutObject<Long> tempOut_flag = new OutObject<Long>();
			dLatitude = phi2z(Utils.clone(_e), ts, tempOut_flag);
		flag = tempOut_flag.argValue;
			if (flag != 0)
			{
				throw new IllegalArgumentException();
			}
		}
		else
		{
			dLatitude = -HALF_PI;
		}

		dLongitude = adjust_lon(theta / Utils.clone(ns) + central_meridian);
		return p.length == 2 ? new double[] {dLongitude, dLatitude} : new double[] {dLongitude, dLatitude, p[2]};
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
			_inverse = new LambertConformalConic2SP(_Parameters.ToProjectionParameter(), this);
		}
		return _inverse;
	}
}