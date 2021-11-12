package demoapp.mapping;


/**
 Implemetns the Krovak Projection.
 
 
 <p>The normal case of the Lambert Conformal conic is for the axis of the cone 
 to be coincident with the minor axis of the ellipsoid, that is the axis of the cone 
 is normal to the ellipsoid at a pole. For the Oblique Conformal Conic the axis 
 of the cone is normal to the ellipsoid at a defined location and its extension 
 cuts the minor axis at a defined angle. This projection is used in the Czech Republic 
 and Slovakia under the name "Krovak" projection.</p>
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class KrovakProjection extends MapProjection
{
	/**
	 * Maximum number of iterations for iterative computations.
	 */
	private static final int MaximumIterations = 15;

	/**
	 * When to stop the iteration.
	 */
	private static final double IterationTolerance = 1E-11;

	/**
	 * Azimuth of the centre line passing through the centre of the projection.
	 * This is equals to the co-latitude of the cone axis at point of intersection
	 * with the ellipsoid.
	 */
	private double _azimuth;

	/**
	 * Latitude of pseudo standard parallel.
	 */
	private double _pseudoStandardParallel;

	/**
	 * Useful variables calculated from parameters defined by user.
	 */
	private double _sinAzim, _cosAzim, _n, _tanS2, _alfa, _hae, _k1, _ka, _ro0, _rop;

	/**
	 * Useful constant - 45� in radians.
	 */
	private static final double S45 = 0.785398163397448;

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
	public KrovakProjection(Iterable<ProjectionParameter> parameters)
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
	protected KrovakProjection(Iterable<ProjectionParameter> parameters, KrovakProjection inverse)
	{
		super(parameters, inverse);
		setName("Krovak");

		setAuthority("EPSG");
		setAuthorityCode(9819);

		//PROJCS["S-JTSK (Ferro) / Krovak",
		//GEOGCS["S-JTSK (Ferro)",
		//    DATUM["D_S_JTSK_Ferro",
		//        SPHEROID["Bessel 1841",6377397.155,299.1528128]],
		//    PRIMEM["Ferro",-17.66666666666667],
		//    UNIT["degree",0.0174532925199433]],
		//PROJECTION["Krovak"],
		//PARAMETER["latitude_of_center",49.5],
		//PARAMETER["longitude_of_center",42.5],
		//PARAMETER["azimuth",30.28813972222222],
		//PARAMETER["pseudo_standard_parallel_1",78.5],
		//PARAMETER["scale_factor",0.9999],
		//PARAMETER["false_easting",0],
		//PARAMETER["false_northing",0],
		//UNIT["metre",1]]

		//Check for missing parameters
		_azimuth = Degrees2Radians(_Parameters.GetParameterValue("azimuth"));
		_pseudoStandardParallel = Degrees2Radians(_Parameters.GetParameterValue("pseudo_standard_parallel_1"));

		// Calculates useful constants.
		_sinAzim = Math.sin(Utils.clone(_azimuth));
		_cosAzim = Math.cos(Utils.clone(_azimuth));
		_n = Math.sin(Utils.clone(_pseudoStandardParallel));
		_tanS2 = Math.tan(Utils.clone(_pseudoStandardParallel) / 2 + S45);

		double sinLat = Math.sin(Utils.clone(lat_origin));
		double cosLat = Math.cos(Utils.clone(lat_origin));
		double cosL2 = cosLat * cosLat;
		_alfa = Math.sqrt(1 + ((Utils.clone(_es) * (cosL2 * cosL2)) / (1 - _es))); // parameter B
		_hae = Utils.clone(_alfa) * Utils.clone(_e) / 2;
		double u0 = Math.asin(sinLat / Utils.clone(_alfa));

		double esl = Utils.clone(_e) * sinLat;
		double g = Math.pow((1 - esl) / (1 + esl), (Utils.clone(_alfa) * Utils.clone(_e)) / 2);
		_k1 = Math.pow(Math.tan(Utils.clone(lat_origin) / 2 + S45), Utils.clone(_alfa)) * g / Math.tan(u0 / 2 + S45);
		_ka = Math.pow(1 / Utils.clone(_k1), -1 / Utils.clone(_alfa));

		double radius = Math.sqrt(1 - Utils.clone(_es)) / (1 - (Utils.clone(_es) * (sinLat * sinLat)));

		_ro0 = Utils.clone(scale_factor) * radius / Math.tan(Utils.clone(_pseudoStandardParallel));
		_rop = Utils.clone(_ro0) * Math.pow(Utils.clone(_tanS2), Utils.clone(_n));
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
		double lambda = lonlat[0] - central_meridian;
		double phi = lonlat[1];

		double esp = Utils.clone(_e) * Math.sin(phi);
		double gfi = Math.pow(((1.0 - esp) / (1.0 + esp)), Utils.clone(_hae));
		double u = 2 * (Math.atan(Math.pow(Math.tan(phi / 2 + S45), Utils.clone(_alfa)) / Utils.clone(_k1) * gfi) - S45);
		double deltav = -lambda * Utils.clone(_alfa);
		double cosU = Math.cos(u);
		double s = Math.asin((Utils.clone(_cosAzim) * Math.sin(u)) + (Utils.clone(_sinAzim) * cosU * Math.cos(deltav)));
		double d = Math.asin(cosU * Math.sin(deltav) / Math.cos(s));
		double eps = Utils.clone(_n) * d;
		double ro = Utils.clone(_rop) / Math.pow(Math.tan(s / 2 + S45), Utils.clone(_n));

		/* x and y are reverted  */
		double y = -(ro * Math.cos(eps)) * Utils.clone(_semiMajor);
		double x = -(ro * Math.sin(eps)) * Utils.clone(_semiMajor);

		return new double[] {x, y};
	}

	/** 
	 Converts coordinates in projected meters to decimal degrees.
	 
	 @param p Point in meters
	 @return Transformed point in decimal degrees
	*/
	@Override
	protected double[] MetersToRadians(double[] p)
	{
		double x = p[0] / Utils.clone(_semiMajor);
		double y = p[1] / Utils.clone(_semiMajor);

		// x -> southing, y -> westing
		double ro = Math.sqrt(x * x + y * y);
		double eps = Math.atan2(-x, -y);
		double d = eps / Utils.clone(_n);
		double s = 2 * (Math.atan(Math.pow(Utils.clone(_ro0) / ro, 1 / Utils.clone(_n)) * Utils.clone(_tanS2)) - S45);
		double cs = Math.cos(s);
		double u = Math.asin((Utils.clone(_cosAzim) * Math.sin(s)) - (Utils.clone(_sinAzim) * cs * Math.cos(d)));
		double kau = Utils.clone(_ka) * Math.pow(Math.tan((u / 2.0) + S45), 1 / Utils.clone(_alfa));
		double deltav = Math.asin((cs * Math.sin(d)) / Math.cos(u));
		double lambda = -deltav / Utils.clone(_alfa);
		double phi = 0d;

		// iteration calculation
		for (int i = MaximumIterations;;)
		{
			double fi1 = phi;
			double esf = Utils.clone(_e) * Math.sin(fi1);
			phi = 2.0 * (Math.atan(kau * Math.pow((1.0 + esf) / (1.0 - esf), Utils.clone(_e) / 2.0)) - S45);
			if (Math.abs(fi1 - phi) <= IterationTolerance)
			{
				break;
			}

			if (--i < 0)
			{
				break;
				//throw new ProjectionException(Errors.format(ErrorKeys.NO_CONVERGENCE));
			}
		}

		return new double[] {lambda + central_meridian, phi};
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
			_inverse = new KrovakProjection(_Parameters.ToProjectionParameter(), this);
		}

		return _inverse;
	}
}