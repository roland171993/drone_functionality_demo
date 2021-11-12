package demoapp.mapping;


import java.util.concurrent.CopyOnWriteArrayList;

//C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this class may differ from the original:
//ORIGINAL LINE: public struct utmpos
public final class utmpos
{
	public final static utmpos Zero = new utmpos();
	public double x;
	public double y;
	public int zone;
	public Object Tag;

	private static CoordinateTransformationFactory ctfac = new CoordinateTransformationFactory();
	private static IGeographicCoordinateSystem wgs84 = GeographicCoordinateSystem.getWGS84();

	public utmpos()
	{
	}

	public utmpos(double x, double y, int zone)
	{
		this.x = x;
		this.y = y;
		this.zone = zone;
		this.Tag = null;
	}

	public utmpos(utmpos pos)
	{
		this.x = pos.x;
		this.y = pos.y;
		this.zone = pos.zone;
		this.Tag = null;
	}

	public utmpos(PointLatLngAlt pos)
	{
		double[] dd = pos.ToUTM();
		this.x = dd[0];
		this.y = dd[1];
		this.zone = pos.GetUTMZone();
		this.Tag = null;
	}

//C# TO JAVA CONVERTER TODO TASK: The following operator overload is not converted by C# to Java Converter:
	public static double[] implicitoperator  (utmpos a)
	{
		return new double[] {a.x, a.y};
	}

	public double[] toDoubleArr()
	{
		return implicitoperator(this);
	}

//C# TO JAVA CONVERTER TODO TASK: The following operator overload is not converted by C# to Java Converter:
	public static PointLatLngAlt implicitoperatorPointLatLngAlt (utmpos a)
	{
		return a.ToLLA();
	}

	public PointLatLngAlt toPointLatLngAlt ()
	{
		return ToLLA();
	}


	public PointLatLngAlt ToLLA()
	{
		IProjectedCoordinateSystem utm = ProjectedCoordinateSystem.WGS84_UTM(Math.abs(zone), zone < 0 ? false : true);

		ICoordinateTransformation trans = ctfac.CreateFromCoordinateSystems(wgs84, utm);

		// get leader utm coords
		double[] pll = trans.getMathTransform().Inverse().Transform(this.clone().toDoubleArr());

		PointLatLngAlt ans = new PointLatLngAlt(pll[1], pll[0]);
		if (this.Tag != null)
		{
			ans.setTag(this.Tag.toString());
		}

		return ans;
	}

	public static CopyOnWriteArrayList<utmpos> ToList(CopyOnWriteArrayList<double[]> input, int zone)
	{
		CopyOnWriteArrayList<utmpos> data = new CopyOnWriteArrayList<utmpos>();

		input.forEach(x ->
		{
				data.add(new utmpos(x[0], x[1], zone));
		});

		return data;
	}

	public double GetDistance(utmpos b)
	{
		return Math.sqrt(Math.pow(Math.abs(x - b.x), 2) + Math.pow(Math.abs(y - b.y), 2));
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof utmpos))
		{
			return false;
		}
		return (((((utmpos)obj).x == this.x) && (((utmpos)obj).y == this.y)) && obj.getClass().equals(super.getClass()));
	}

	public static boolean opEquals(utmpos left, utmpos right)
	{
		return ((left.x == right.x) && (left.y == right.y) && (left.zone == right.zone));
	}

	public static boolean opNotEquals(utmpos left, utmpos right)
	{
		return !opEquals(left.clone(), right.clone());
	}

	@Override
	public String toString()
	{
		return "utmpos: " + x + "," + y;
	}


	public String toString2()
	{
		String valX = x+"";
		if (valX.length() > Grid.LIMIT11){
			valX = valX.substring(0,Grid.LIMIT11);
		}
		String valY = y+"";
		if (valY.length() > Grid.LIMIT11){
			valY = valY.substring(0,Grid.LIMIT11);
		}
		String res = valX+","+valY;
		return res;
	}

	@Override
	public int hashCode()
	{
		{
			int hashCode = (new Double(x)).hashCode();
			hashCode = (hashCode * 397) ^ (new Double(y)).hashCode();
			hashCode = (hashCode * 397) ^ zone;
			return hashCode;
		}
	}

	public boolean getIsZero()
	{
		if (opEquals(this.clone(), Zero.clone()))
		{
			return true;
		}
		return false;
	}

	public utmpos clone()
	{
		utmpos varCopy = new utmpos();

		varCopy.x = this.x;
		varCopy.y = this.y;
		varCopy.zone = this.zone;
		varCopy.Tag = this.Tag;

		return varCopy;
	}
}