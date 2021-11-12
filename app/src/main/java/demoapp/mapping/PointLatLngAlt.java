package demoapp.mapping;

import android.graphics.Color;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class PointLatLngAlt implements Comparable
{
	public static final PointLatLngAlt Zero = new PointLatLngAlt();
	private double Lat = 0;
	public final double getLat()
	{
		return Lat;
	}
	public final void setLat(double value)
	{
		Lat = value;
	}
	private double Lng = 0;
	public final double getLng()
	{
		return Lng;
	}
	public final void setLng(double value)
	{
		Lng = value;
	}
	private double Alt = 0;
	public final double getAlt()
	{
		return Alt;
	}
	public final void setAlt(double value)
	{
		Alt = value;
	}
	private String Tag = "";
	public final String getTag()
	{
		return Tag;
	}
	public final void setTag(String value)
	{
		Tag = value;
	}
	private String Tag2 = "";
	public final String getTag2()
	{
		return Tag2;
	}
	public final void setTag2(String value)
	{
		Tag2 = value;
	}
	private int color = Color.WHITE;
	public final int getColor()
	{
		return color;
	}
	public final void setColor(int value)
	{
		color = value;
	}

	private static CoordinateTransformationFactory ctfac = new CoordinateTransformationFactory();
	private static IGeographicCoordinateSystem wgs84 = GeographicCoordinateSystem.getWGS84();

	public PointLatLngAlt(double lat, double lng, double alt, String tag)
	{
		this.setLat(lat);
		this.setLng(lng);
		this.setAlt(alt);
		this.setTag(tag);
	}

	public PointLatLngAlt()
	{

	}

	public PointLatLngAlt toPointLatLngAlt (PointLatLng pll)
	{
		this.setLat(pll.getLat());
		this.setLng(pll.getLng());
		return this;
	}

	public PointLatLngAlt(double lat, double lng)
	{
		this.setLat(lat);
		this.setLng(lng);
	}

	public PointLatLngAlt(double lat, double lng, double alt)
	{
		this.setLat(lat);
		this.setLng(lng);
		this.setAlt(alt);
	}





	public final PointLatLng Point()
	{
		PointLatLng pnt = new PointLatLng(getLat(), getLng());
		return pnt.clone();
	}

	public final PointLatLngAlt Clone()
	{
		// fix Same memory Address Issue
		PointLatLngAlt point = new PointLatLngAlt(Double.parseDouble(getLat() + ""), Double.parseDouble(getLng() + ""), Double.parseDouble(getAlt() + ""), String.valueOf(getTag()));
		return point;
	}

//C# TO JAVA CONVERTER TODO TASK: The following operator overload is not converted by C# to Java Converter:
//	public static PointLatLngAlt implicitoperator (PointLatLng a)
//	{
//		return new PointLatLngAlt(a.clone());
//	}

//C# TO JAVA CONVERTER TODO TASK: The following operator overload is not converted by C# to Java Converter:
	public static PointLatLng implicitoperatorToPointLatLng (PointLatLngAlt a)
	{
		return a.Point().clone();
	}

//C# TO JAVA CONVERTER TODO TASK: The following operator overload is not converted by C# to Java Converter:
	public static double[] implicitoperatorTodouble (PointLatLngAlt a)
	{
		return new double[] {a.getLng(), a.getLat()};
	}

	public double[] toDoubleArr ()
	{
		return new double[] {this.getLng(), this.getLat()};
	}

//C# TO JAVA CONVERTER TODO TASK: The following operator overload is not converted by C# to Java Converter:
	public static PointLatLngAlt implicitoperatorToPointLatLngAlt (double[] a)
	{
		if (a.length == 3)
		{
			PointLatLngAlt tempVar = new PointLatLngAlt();
			tempVar.setLng(a[0]);
			tempVar.setLat(a[1]);
			tempVar.setAlt(a[2]);
			return tempVar;
		}
		PointLatLngAlt tempVar2 = new PointLatLngAlt();
		tempVar2.setLng(a[0]);
		tempVar2.setLat(a[1]);
		return tempVar2;
	}

	@Override
	public boolean equals(Object pllaobj)
	{
		PointLatLngAlt plla = pllaobj instanceof PointLatLngAlt ? (PointLatLngAlt)pllaobj : null;

		if (plla == null)
		{
			return false;
		}

		if (this.getLat() == plla.getLat() && this.getLng() == plla.getLng() && this.getAlt() == plla.getAlt() && (this.getColor() == plla.getColor()) && this.getTag().equals(plla.getTag()))
		{
			return true;
		}
		return false;
	}

	public static boolean opEquals(PointLatLngAlt p1, PointLatLng p2)
	{
		if (p1 == null || p2 == null)
		{
			return false;
		}

		if (p1.getLat() == p2.getLat() && p1.getLng() == p2.getLng())
		{
			return true;
		}
		return false;
	}

	public static boolean opNotEquals(PointLatLngAlt p1, PointLatLng p2)
	{
		return !opEquals(p1, p2.clone());
	}

	@Override
	public int hashCode()
	{
		return (int)(Double.doubleToLongBits(getLat()) ^ Long.parseLong(getLng()+"") ^ Long.parseLong(getAlt()+""));
	}

	@Override
	public String toString()
	{
		return getLat() + "," + getLng() + "," + getAlt() + "," + getTag();
	}

	public String toString2()
	{
		int limit7 = 7;
		String valLat = getLat()+"";
		if (valLat.length() >limit7){
			valLat = valLat.substring(0,limit7);
		}
		String valLong = getLng()+"";
		if (valLong.length() > limit7){
			valLong = valLong.substring(0,limit7);
		}
		String res = valLat+","+valLong;
		return res;
	}

	public final int GetUTMZone()
	{
		int zone = (int)((getLng() - -186.0) / 6.0);
		if (getLat() < 0)
		{
			zone *= -1;
		}

		return zone;
	}





	/** 
	 Calc Distance in M
	 
	 @param p2
	 @return Distance in M
	*/
	public final double GetDistance(PointLatLngAlt p2)
	{
		double d = getLat() * 0.017453292519943295;
		double num2 = getLng() * 0.017453292519943295;
		double num3 = p2.getLat() * 0.017453292519943295;
		double num4 = p2.getLng() * 0.017453292519943295;
		double num5 = num4 - num2;
		double num6 = num3 - d;
		double num7 = Math.pow(Math.sin(num6 / 2.0), 2.0) + ((Math.cos(d) * Math.cos(num3)) * Math.pow(Math.sin(num5 / 2.0), 2.0));
		double num8 = 2.0 * Math.atan2(Math.sqrt(num7), Math.sqrt(1.0 - num7));
		return (6371 * num8) * 1000.0; // M
	}


	public final int compareTo(Object obj)
	{
		PointLatLngAlt pnt = obj instanceof PointLatLngAlt ? (PointLatLngAlt)obj : null;

		if (pnt == null)
		{
			return 1;
		}

		double wpno = 0;
		double wpnothis = 0;

		OutObject<Double> tempOut_wpnothis = new OutObject<Double>();
		if (!TryParseHelper.tryParseDouble(this.getTag(), tempOut_wpnothis))
		{
		wpnothis = tempOut_wpnothis.argValue;
			return 0;
		}
	else
	{
		wpnothis = tempOut_wpnothis.argValue;
	}

		OutObject<Double> tempOut_wpno = new OutObject<Double>();
		if (TryParseHelper.tryParseDouble(pnt.getTag(), tempOut_wpno))
		{
		wpno = tempOut_wpno.argValue;
			if (wpno < wpnothis)
			{
				return 1;
			}
			if (wpno > wpnothis)
			{
				return -1;
			}
			return 0;
		}
		else
		{
		wpno = tempOut_wpno.argValue;
			return 0;
		}
	}

	public static CopyOnWriteArrayList<double[]> ToUTM(int utmzone, CopyOnWriteArrayList<PointLatLngAlt> list)
	{
		ICoordinateTransformation trans = TryGetTransform(utmzone, list.get(0).getLat());

		CopyOnWriteArrayList<double[]> data = new CopyOnWriteArrayList<double[]>();

		list.forEach(x ->
		{
				data.add((double[])x.toDoubleArr());
		});

		List<double[]> mList = trans.getMathTransform().TransformList(data);
		CopyOnWriteArrayList<double[]> response = new CopyOnWriteArrayList<>();
		response.addAll(mList);

		return response;
	}

	public final double[] ToUTM()
	{
		return ToUTM(GetUTMZone());
	}

	// force a zone
	public final double[] ToUTM(int utmzone)
	{
		ICoordinateTransformation trans = TryGetTransform(utmzone, getLat());

		double[] pll = {getLng(), getLat()};

		// get leader utm coords
		double[] utmxy = trans.getMathTransform().Transform(pll);

		return utmxy;
	}

	public static double[] ToUTM(int utmzone, double lat, double lng)
	{
		ICoordinateTransformation trans = TryGetTransform(utmzone, lat);

		return trans.getMathTransform().Transform(new double[] {lng, lat});
	}

	private static HashMap<Integer, ICoordinateTransformation> coordtrans = new HashMap<Integer, ICoordinateTransformation>();

	private static ICoordinateTransformation TryGetTransform(int utmzone, double lat)
	{
		if (lat < 0 && utmzone > 0)
		{
			utmzone *= -1;
		}

		synchronized (coordtrans)
		{
			if (coordtrans.containsKey(utmzone))
			{
				return coordtrans.get(utmzone);
			}
		}

		IProjectedCoordinateSystem utm = ProjectedCoordinateSystem.WGS84_UTM(Math.abs(utmzone), lat < 0 ? false : true);
		ICoordinateTransformation trans = ctfac.CreateFromCoordinateSystems(wgs84, utm);

		synchronized (coordtrans)
		{
			coordtrans.put(utmzone, trans);
		}

		synchronized (coordtrans)
		{
			return coordtrans.get(utmzone);
		}
	}
}