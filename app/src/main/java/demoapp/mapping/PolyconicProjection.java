package demoapp.mapping;


/**
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class PolyconicProjection extends MapProjection
{
	/** 
	 Maximum difference allowed when comparing real numbers.
	*/
	private static final double Epsilon = 1E-10;

	/** 
	 Maximum number of iterations for iterative computations.
	*/
	private static final int MaximumIterations = 20;

	/** 
	 Difference allowed in iterative computations.
	*/
	private static final double IterationTolerance = 1E-12;

	/**
	 Meridian distance at the latitude of origin.
	 Used for calculations for the ellipsoid.
	*/
	private final double _ml0;

	/**
	 Constructs a new map projection from the supplied parameters.
	
	 @param parameters The parameter values in standard units
	*/
	public PolyconicProjection(Iterable<ProjectionParameter> parameters)
	{
		this(parameters, null);
	}

	/** 
	 Constructs a new map projection from the supplied parameters.
	 
	 @param parameters The parameter values in standard units
	 @param inverse Defines if Projection is inverse
	*/
	protected PolyconicProjection(Iterable<ProjectionParameter> parameters, PolyconicProjection inverse)
	{
		super(parameters, inverse);
		_ml0 = mlfn(Utils.clone(lat_origin), Math.sin(Utils.clone(lat_origin)), Math.cos(Utils.clone(lat_origin)));
	}

	@Override
	protected double[] RadiansToMeters(double[] lonlat)
	{

		double lam = lonlat[0];
		double phi = lonlat[1];

		double delta_lam = adjust_lon(lam - central_meridian);

		double x, y;

		if (Math.abs(phi) <= Epsilon)
		{
			x = delta_lam; //lam;
			y = -_ml0;
		}
		else
		{
			double sp = Math.sin(phi);
			double cp;
			double ms = Math.abs(cp = Math.cos(phi)) > Epsilon ? msfn(sp, cp) / sp : 0.0;
			/*lam =*/
			delta_lam *= sp;
			x = ms * Math.sin(delta_lam);
			y = (mlfn(phi, sp, cp) - Utils.clone(_ml0)) + ms * (1.0 - Math.cos(delta_lam));
		}

		x = Utils.clone(scale_factor) * Utils.clone(_semiMajor) * x; // + false_easting;
		y = Utils.clone(scale_factor) * Utils.clone(_semiMajor) * y; // +false_northing;

		return new double[] {x, y};
	}

	@Override
	protected double[] MetersToRadians(double[] p)
	{

		double x = (p[0]) / (Utils.clone(_semiMajor) * Utils.clone(scale_factor));
		double y = (p[1]) / (Utils.clone(_semiMajor) * Utils.clone(scale_factor));

		double lam, phi;

		y += _ml0;
		if (Math.abs(y) <= Epsilon)
		{
			lam = x;
			phi = 0.0;
		}
		else
		{
			double r = y * y + x * x;
			phi = y;
			int i = 0;
			for (; i <= MaximumIterations; i++)
			{
				double sp = Math.sin(phi);
				double cp = Math.cos(phi);
				if (Math.abs(cp) < IterationTolerance)
				{
					throw new RuntimeException("No Convergence");
				}

				double s2ph = sp * cp;
				double mlp = Math.sqrt(1.0 - Utils.clone(_es) * sp * sp);
				double c = sp * mlp / cp;
				double ml = mlfn(phi, sp, cp);
				double mlb = ml * ml + r;
				mlp = (1.0 - Utils.clone(_es)) / (mlp * mlp * mlp);
				double dPhi = (ml + ml + c * mlb - 2.0 * y * (c * ml + 1.0)) / (Utils.clone(_es) * s2ph * (mlb - 2.0 * y * ml) / c + 2.0 * (y - ml) * (c * mlp - 1.0 / s2ph) - mlp - mlp);
				if (Math.abs(dPhi) <= IterationTolerance)
				{
					break;
				}

				phi += dPhi;
			}
			if (i > MaximumIterations)
			{
				throw new RuntimeException("No Convergence");
			}
			double c2 = Math.sin(phi);
			lam = Math.asin(x * Math.tan(phi) * Math.sqrt(1.0 - Utils.clone(_es) * c2 * c2)) / Math.sin(phi);
		}

		return new double[] {adjust_lon(lam + central_meridian), phi};
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
			_inverse = new PolyconicProjection(_Parameters.ToProjectionParameter(), this);
		}
		return _inverse;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Private helpers
	/**
	  Computes function <code>f(s,c,e�) = c/sqrt(1 - s�*e�)</code> needed for the true scale
	  latitude (Snyder 14-15), where <var>s</var> and <var>c</var> are the sine and cosine of
	  the true scale latitude, and <var>e�</var> is the eccentricity squared.
	*/
	private double msfn(double s, double c)
	{
		return c / Math.sqrt(1.0 - (s * s) * Utils.clone(_es));
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}