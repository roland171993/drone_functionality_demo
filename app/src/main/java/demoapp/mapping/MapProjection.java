package demoapp.mapping;



import java.util.concurrent.CopyOnWriteArrayList;


/**
 Projections inherit from this abstract class to get access to useful mathematical functions.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public abstract class MapProjection extends MathTransform implements IProjection
{
// ReSharper disable InconsistentNaming
	protected final double _e;
	protected final double _es;
	protected final double _semiMajor;
	protected final double _semiMinor;
	protected final double _metersPerUnit;

	protected final double scale_factor; // scale factor
	protected double central_meridian; // Center longitude (projection center)
	protected final double getLonOrigin()
	{
		return central_meridian;
	}
	protected final void setLonOrigin(double value)
	{
		central_meridian = value;
	}
	protected final double lat_origin; // center latitude
	protected final double false_northing; // y offset in meters
	protected final double false_easting; // x offset in meters

	protected final double en0, en1, en2, en3, en4;

	protected ProjectionParameterSet _Parameters;

	protected MathTransform _inverse;
	private boolean _isInverse;

	// ReSharper restore InconsistentNaming

	/** 
	 Creates an instance of this class
	 
	 @param parameters An enumeration of projection parameters
	 @param inverse Indicator if this projection is inverse
	*/
	protected MapProjection(Iterable<ProjectionParameter> parameters, MapProjection inverse)
	{
		this(parameters);
		_inverse = inverse;
		if (_inverse != null)
		{
			inverse._inverse = this;
			_isInverse = !inverse._isInverse;
		}
	}

	/** 
	 Creates an instance of this class
	 
	 @param parameters An enumeration of projection parameters
	*/
	protected MapProjection(Iterable<ProjectionParameter> parameters)
	{
		_Parameters = new ProjectionParameterSet(parameters);

		_semiMajor = _Parameters.GetParameterValue("semi_major");
		_semiMinor = _Parameters.GetParameterValue("semi_minor");

		//_es = 1.0 - (_semiMinor * _semiMinor) / (_semiMajor * _semiMajor);
		_es = EccentricySquared(Utils.clone(_semiMajor), Utils.clone(_semiMinor));
		_e = Math.sqrt(Utils.clone(_es));

		scale_factor = _Parameters.GetOptionalParameterValue("scale_factor", 1);

		central_meridian = Degrees2Radians(_Parameters.GetParameterValue("central_meridian", "longitude_of_center"));
		lat_origin = Degrees2Radians(_Parameters.GetParameterValue("latitude_of_origin", "latitude_of_center"));

		_metersPerUnit = _Parameters.GetParameterValue("unit");

		false_easting = _Parameters.GetOptionalParameterValue("false_easting", 0) * Utils.clone(_metersPerUnit);
		false_northing = _Parameters.GetOptionalParameterValue("false_northing", 0) * Utils.clone(_metersPerUnit);

		// TODO: Should really convert to the correct linear units??

		//  Compute constants for the mlfn
		double t;
		en0 = C00 - Utils.clone(_es) * (C02 + Utils.clone(_es) * (C04 + Utils.clone(_es) * (C06 + Utils.clone(_es) * C08)));
		en1 = Utils.clone(_es) * (C22 - Utils.clone(_es) * (C04 + Utils.clone(_es) * (C06 + Utils.clone(_es) * C08)));
		en2 = (t = Utils.clone(_es) * Utils.clone(_es)) * (C44 - Utils.clone(_es) * (C46 + Utils.clone(_es) * C48));
		en3 = (t *= Utils.clone(_es)) * (C66 - Utils.clone(_es) * C68);
		en4 = t * Utils.clone(_es) * C88;

	}

	/** 
	 Returns a list of projection "cloned" projection parameters
	 
	 @return 
	*/
	protected static CopyOnWriteArrayList<ProjectionParameter> CloneParametersList(Iterable<ProjectionParameter> projectionParameters)
	{
		CopyOnWriteArrayList<ProjectionParameter> res = new CopyOnWriteArrayList<ProjectionParameter>();
		for (ProjectionParameter pp : projectionParameters)
		{
			res.add(new ProjectionParameter(pp.getName(), pp.getValue()));
		}
		return res;
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Implementation of IProjection

	/** 
	 Gets the projection classification name (e.g. 'Transverse_Mercator').
	*/
	public final String getClassName()
	{
		return getName();
	}

	/** 
	 
	 
	 @param index
	 @return 
	*/
	public final ProjectionParameter GetParameter(int index)
	{
		return _Parameters.GetAtIndex(index);
	}

	/** 
	 Gets an named parameter of the projection.
	 
	 The parameter name is case insensitive
	 @param name Name of parameter
	 @return parameter or null if not found
	*/
	public final ProjectionParameter GetParameter(String name)
	{
		return _Parameters.Find(name);
	}

	/** 
	 
	*/
	public final int getNumParameters()
	{
		return _Parameters.size();
	}

	/** 
	 Gets or sets the abbreviation of the object.
	*/
	private String Abbreviation;
	public final String getAbbreviation()
	{
		return Abbreviation;
	}
	public final void setAbbreviation(String value)
	{
		Abbreviation = value;
	}

	/** 
	 Gets or sets the alias of the object.
	*/
	private String Alias;
	public final String getAlias()
	{
		return Alias;
	}
	public final void setAlias(String value)
	{
		Alias = value;
	}

	/** 
	 Gets or sets the authority name for this object, e.g., "EPSG",
	 is this is a standard object with an authority specific
	 identity code. Returns "CUSTOM" if this is a custom object.
	*/
	private String Authority;
	public final String getAuthority()
	{
		return Authority;
	}
	public final void setAuthority(String value)
	{
		Authority = value;
	}

	/** 
	 Gets or sets the authority specific identification code of the object
	*/
	private long AuthorityCode;
	public final long getAuthorityCode()
	{
		return AuthorityCode;
	}
	public final void setAuthorityCode(long value)
	{
		AuthorityCode = value;
	}

	/** 
	 Gets or sets the name of the object.
	*/
	private String Name;
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}

	/** 
	 Gets or sets the provider-supplied remarks for the object.
	*/
	private String Remarks;
	public final String getRemarks()
	{
		return Remarks;
	}
	public final void setRemarks(String value)
	{
		Remarks = value;
	}


	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		StringBuilder sb = new StringBuilder();
		if (_isInverse)
		{
			sb.append("INVERSE_MT[");
		}
		sb.append(String.format("PARAM_MT[\"%1$s\"", getName()));
		for (int i = 0; i < getNumParameters(); i++)
		{
			sb.append(String.format(", %1$s", GetParameter(i).getWKT()));
		}
			//if (!String.IsNullOrEmpty(Authority) && AuthorityCode > 0)
			//	sb.AppendFormat(", AUTHORITY[\"{0}\", \"{1}\"]", Authority, AuthorityCode);
		sb.append("]");
		if (_isInverse)
		{
			sb.append("]");
		}
		return sb.toString();
	}

	/** 
	 Gets an XML representation of this object
	*/
	@Override
	public String getXML()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<CT_MathTransform>");
		sb.append(_isInverse ? "<CT_InverseTransform Name=\"{0}\">" : "<CT_ParameterizedMathTransform Name=\"{0}\">");
		sb.append(getClassName());
		for (int i = 0; i < getNumParameters(); i++)
		{
			sb.append(GetParameter(i).getXML());
		}
		sb.append(_isInverse ? "</CT_InverseTransform>" : "</CT_ParameterizedMathTransform>");
		sb.append("</CT_MathTransform>");
		return sb.toString();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IMathTransform

	@Override
	public int getDimSource()
	{
		return 2;
	}

	@Override
	public int getDimTarget()
	{
		return 2;
	}

	/** 
	 Function to transform from meters to degrees
	 
	 @param p The ordinates of the point
	 @return The transformed ordinates
	*/
	public final double[] MetersToDegrees(double[] p)
	{
		double[] tmp = p.length == 2 ? new double[] {p[0] * Utils.clone(_metersPerUnit) - Utils.clone(false_easting), p[1] * Utils.clone(_metersPerUnit) - Utils.clone(false_northing)} : new double[] {p[0] * _metersPerUnit - false_easting, p[1] * _metersPerUnit - false_northing, p[2] * _metersPerUnit};

		double[] res = MetersToRadians(tmp);

		res[0] = Radians2Degrees(res[0]);
		res[1] = Radians2Degrees(res[1]);
		if (getDimTarget() == 3)
		{
			res[2] = Radians2Degrees(res[2]);
		}

		return res;
	}

	/** 
	 Function to transform from degrees to meters
	 
	 @param lonlat The ordinates of the point
	 @return The transformed ordinates
	*/
	public final double[] DegreesToMeters(double[] lonlat)
	{
		// Convert to radians
		double[] tmp = lonlat.length == 2 ? new double[] {Degrees2Radians(lonlat[0]), Degrees2Radians(lonlat[1])} : new double[] {Degrees2Radians(lonlat[0]), Degrees2Radians(lonlat[1]), Degrees2Radians(lonlat[2])};

		// Convert to meters
		double[] res = RadiansToMeters(tmp);

		// Add false easting and northing, convert to unit
		res[0] = (res[0] + Utils.clone(false_easting)) / Utils.clone(_metersPerUnit);
		res[1] = (res[1] + Utils.clone(false_northing)) / Utils.clone(_metersPerUnit);
		if (res.length == 3)
		{
			res[2] = res[2] / Utils.clone(_metersPerUnit);
		}

		return res;
	}

	protected abstract double[] RadiansToMeters(double[] lonlat);
	protected abstract double[] MetersToRadians(double[] p);

	/** 
	 Reverses the transformation
	*/
	@Override
	public void Invert()
	{
		_isInverse = !_isInverse;
		if (_inverse != null)
		{
			((MapProjection) _inverse).Invert(false);
		}
	}

	protected final void Invert(boolean invertInverse)
	{
		_isInverse = !_isInverse;
		if (invertInverse && _inverse != null)
		{
			((MapProjection) _inverse).Invert(false);
		}
	}

	/** 
	 Returns true if this projection is inverted.
	 Most map projections define forward projection as "from geographic to projection", and backwards
	 as "from projection to geographic". If this projection is inverted, this will be the other way around.
	*/
	protected final boolean getIsInverse()
	{
		return _isInverse;
	}

	/** 
	 Transforms the specified cp.
	 
	 @param cp The cp.
	 @return 
	*/
	@Override
	public double[] Transform(double[] cp)
	{
		//var projectedPoint = new double[] { 0, 0, 0, };
		return !_isInverse ? DegreesToMeters(cp) : MetersToDegrees(cp);
	}

	/** 
	 Checks whether the values of this instance is equal to the values of another instance.
	 Only parameters used for coordinate system are used for comparison.
	 Name, abbreviation, authority, alias and remarks are ignored in the comparison.
	 
	 @param obj
	 @return True if equal
	*/
	public final boolean EqualParams(Object obj)
	{
		if (!(obj instanceof MapProjection))
		{
			return false;
		}
		MapProjection proj = obj instanceof MapProjection ? (MapProjection)obj : null;

		if (!_Parameters.equals(proj._Parameters))
		{
			return false;
		}
		/*
		if (proj.NumParameters != NumParameters)
			return false;

		for (var i = 0; i < _Parameters.Count; i++)
		{
			var param = _Parameters.Find(par => par.Name.Equals(proj.GetParameter(i).Name, StringComparison.OrdinalIgnoreCase));
			if (param == null)
				return false;
			if (param.Value != proj.GetParameter(i).Value)
				return false;
		}
		 */
		return getIsInverse() == proj.getIsInverse();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Helper mathmatical functions

	// defines some usefull constants that are used in the projection routines
	/** 
	 PI
	*/
	protected static final double PI = Math.PI;

	/** 
	 Half of PI
	*/
	protected static final double HALF_PI = (PI * 0.5);

	/** 
	 PI * 2
	*/
	protected static final double TWO_PI = (PI * 2.0);

	/** 
	 EPSLN
	*/
	protected static final double EPSLN = 1.0e-10;

	/** 
	 S2R
	*/
	protected static final double S2R = 4.848136811095359e-6;

	/** 
	 MAX_VAL
	*/
	protected static final double MAX_VAL = 4;

	/** 
	 prjMAXLONG
	*/
	protected static final double prjMAXLONG = 2147483647;

	/** 
	 DBLLONG
	*/
	protected static final double DBLLONG = 4.61168601e18;


	/**
	Function to return the sign of an argument
	*/
	protected static double sign(double x)
	{
		if (x < 0.0)
		{
			return (-1);
		}
		else
		{
			return (1);
		}
	}

	/** 
	 
	 
	 @param x
	 @return 
	*/
	protected static double adjust_lon(double x)
	{
		long count = 0;
		for (;;)
		{
			if (Math.abs(x) <= PI)
			{
				break;
			}
			else if (((long) Math.abs(x / Math.PI)) < 2)
			{
				x = x - (sign(x) * TWO_PI);
			}
			else if (((long) Math.abs(x / TWO_PI)) < prjMAXLONG)
			{
				x = x - (((long)(x / TWO_PI)) * TWO_PI);
			}
			else if (((long) Math.abs(x / (prjMAXLONG * TWO_PI))) < prjMAXLONG)
			{
				x = x - (((long)(x / (prjMAXLONG * TWO_PI))) * (TWO_PI * prjMAXLONG));
			}
			else if (((long) Math.abs(x / (DBLLONG * TWO_PI))) < prjMAXLONG)
			{
				x = x - (((long)(x / (DBLLONG * TWO_PI))) * (TWO_PI * DBLLONG));
			}
			else
			{
				x = x - (sign(x) * TWO_PI);
			}
			count++;
			if (count > MAX_VAL)
			{
				break;
			}
		}
		return (x);
	}

	/** 
	 Function to compute the constant small m which is the radius of
	 a parallel of latitude, phi, divided by the semimajor axis.
	*/
	protected static double msfnz(double eccent, double sinphi, double cosphi)
	{
		double con;

		con = eccent * sinphi;
		return ((cosphi / (Math.sqrt(1.0 - con * con))));
	}

	/** 
	 Function to calculate the sine and cosine in one call.  Some computer
	 systems have implemented this function, resulting in a faster implementation
	 than calling each function separately.  It is provided here for those
	 computer systems which don`t implement this function
	*/
	protected static void sincos(double val, OutObject<Double> sin_val, OutObject<Double> cos_val)

	{
		sin_val.argValue = Math.sin(val);
		cos_val.argValue = Math.cos(val);
	}

	/** 
	 Function to compute the constant small t for use in the forward
	 computations in the Lambert Conformal Conic and the Polar
	 Stereographic projections.
	*/
	protected static double tsfnz(double eccent, double phi, double sinphi)
	{
		double con;
		double com;
		con = eccent * sinphi;
		com = .5 * eccent;
		con = Math.pow(((1.0 - con) / (1.0 + con)), com);
		return (Math.tan(.5 * (HALF_PI - phi)) / con);
	}

	/**
	Function to eliminate roundoff errors in asin
	*/
	protected static double asinz(double con)
	{
		if (Math.abs(con) > 1.0)
		{
			if (con > 1.0)
			{
				con = 1.0;
			}
			else
			{
				con = -1.0;
			}
		}
		return (Math.asin(con));
	}

	/** 
	 Function to compute the latitude angle, phi2, for the inverse of the
	 Lambert Conformal Conic and Polar Stereographic projections.
	 
	 @param eccent Spheroid eccentricity
	 @param ts Constant value t
	 @param flag Error flag number
	*/
	protected static double phi2z(double eccent, double ts, OutObject<Long> flag)
	{
		double con;
		double dphi;
		double sinpi;
		long i;

		flag.argValue = 0L;
		double eccnth = .5 * eccent;
		double chi = HALF_PI - 2 * Math.atan(ts);
		for (i = 0; i <= 15; i++)
		{
			sinpi = Math.sin(chi);
			con = eccent * sinpi;
			dphi = HALF_PI - 2 * Math.atan(ts * (Math.pow(((1.0 - con) / (1.0 + con)), eccnth))) - chi;
			chi += dphi;
			if (Math.abs(dphi) <= .0000000001)
			{
				return (chi);
			}
		}
		throw new IllegalArgumentException("Convergence error - phi2z-conv");
	}

	private static final double C00 = 1.0, C02 = 0.25, C04 = 0.046875, C06 = 0.01953125, C08 = 0.01068115234375, C22 = 0.75, C44 = 0.46875, C46 = 0.01302083333333333333, C48 = 0.00712076822916666666, C66 = 0.36458333333333333333, C68 = 0.00569661458333333333, C88 = 0.3076171875;


	/** 
	 Calculates the meridian distance. This is the distance along the central 
	 meridian from the equator to <paramref name="phi"/>. Accurate to &lt; 1e-5 meters 
	 when used in conjuction with typical major axis values.
	 
	 @param phi
	 @param sphi
	 @param cphi
	 @return 
	*/
	protected final double mlfn(double phi, double sphi, double cphi)
	{
		cphi *= sphi;
		sphi *= sphi;
		return Utils.clone(en0) * phi - cphi * (Utils.clone(en1) + sphi * (Utils.clone(en2) + sphi * (Utils.clone(en3) + sphi * Utils.clone(en4))));
	}

	/** 
	 Calculates the latitude (phi) from a meridian distance.
	 Determines phi to TOL (1e-11) radians, about 1e-6 seconds.
	 
	 @param arg The meridonial distance
	 @return The latitude of the meridian distance.
	*/
	protected final double inv_mlfn(double arg)
	{
		final double MLFN_TOL = 1E-11;
		final int MAXIMUM_ITERATIONS = 20;
		double s, t, phi, k = 1.0 / (1.0 - Utils.clone(_es));
		int i;
		phi = arg;
		for (i = MAXIMUM_ITERATIONS; true;)
		{
			// rarely goes over 5 iterations
			if (--i < 0)
			{
				throw new IllegalStateException("No convergence");
			}
			s = Math.sin(phi);
			t = 1.0 - Utils.clone(_es) * s * s;
			t = (mlfn(phi, s, Math.cos(phi)) - arg) * (t * Math.sqrt(t)) * k;
			phi -= t;
			if (Math.abs(t) < MLFN_TOL)
			{
				return phi;
			}
		}
	}

	/** 
	 Calculates the flattening factor, (<paramref name="equatorialRadius"/> - <paramref name="polarRadius"/>) / <paramref name="equatorialRadius"/>.
	 
	 @param equatorialRadius The radius of the equator
	 @param polarRadius The radius of a circle touching the poles
	 @return The flattening factor
	*/
	private static double FlatteningFactor(double equatorialRadius, double polarRadius)
	{
		return (equatorialRadius - polarRadius) / equatorialRadius;
	}

	/** 
	 Calculates the square of eccentricity according to es = (2f - f^2) where f is the <see cref="FlatteningFactor">flattening factor</see>.
	 
	 @param equatorialRadius The radius of the equator
	 @param polarRadius The radius of a circle touching the poles
	 @return The square of eccentricity
	*/
	private static double EccentricySquared(double equatorialRadius, double polarRadius)
	{
		double f = FlatteningFactor(equatorialRadius, polarRadius);
		return 2 * f - f * f;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Static Methods;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}