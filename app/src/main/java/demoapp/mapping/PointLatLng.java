package demoapp.mapping;

import java.io.Serializable;


/** the point of coordinates
  */
//C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this class may differ from the original:
//ORIGINAL LINE: [Serializable] public struct PointLatLng
  public final class PointLatLng implements Serializable
  {
	public static PointLatLng Empty = new PointLatLng();
	private double lat;
	private double lng;
	private boolean NotEmpty;

	public PointLatLng()
	{
	}

	public PointLatLng(double lat, double lng)
	{
	  this.lat = lat;
	  this.lng = lng;
	  this.NotEmpty = true;
	}

	/** returns true if coordinates wasn't assigned
	*/
	public boolean getIsEmpty()
	{
	  return !this.NotEmpty;
	}

	public double getLat()
	{
	  return this.lat;
	}
	public void setLat(double value)
	{
	  this.lat = value;
	  this.NotEmpty = true;
	}

	public double getLng()
	{
	  return this.lng;
	}
	public void setLng(double value)
	{
	  this.lng = value;
	  this.NotEmpty = true;
	}

	public static PointLatLng opAdd(PointLatLng pt, SizeLatLng sz)
	{
	  return PointLatLng.Add(pt.clone(), sz.clone());
	}

	public static PointLatLng opSubtract(PointLatLng pt, SizeLatLng sz)
	{
	  return PointLatLng.Subtract(pt.clone(), sz.clone());
	}

	public static SizeLatLng opSubtract(PointLatLng pt1, PointLatLng pt2)
	{
	  return new SizeLatLng(pt1.getLat() - pt2.getLat(), pt2.getLng() - pt1.getLng());
	}

	public static boolean opEquals(PointLatLng left, PointLatLng right)
	{
	  if (left.getLng() == right.getLng())
	  {
		return left.getLat() == right.getLat();
	  }
	  return false;
	}

	public static boolean opNotEquals(PointLatLng left, PointLatLng right)
	{
	  return !PointLatLng.opEquals(left.clone(), right.clone());
	}

	public static PointLatLng Add(PointLatLng pt, SizeLatLng sz)
	{
	  return new PointLatLng(pt.getLat() - sz.getHeightLat(), pt.getLng() + sz.getWidthLng());
	}

	public static PointLatLng Subtract(PointLatLng pt, SizeLatLng sz)
	{
	  return new PointLatLng(pt.getLat() + sz.getHeightLat(), pt.getLng() - sz.getWidthLng());
	}

	@Override
	public boolean equals(Object obj)
	{
	  if (!(obj instanceof PointLatLng))
	  {
		return false;
	  }
	  PointLatLng pointLatLng = (PointLatLng) obj;
	  if (pointLatLng.getLng() == this.getLng() && pointLatLng.getLat() == this.getLat())
	  {
		return pointLatLng.getClass().equals(this.getClass());
	  }
	  return false;
	}

	public void Offset(PointLatLng pos)
	{
	  this.Offset(pos.getLat(), pos.getLng());
	}

	public void Offset(double lat, double lng)
	{
	  this.setLng(this.getLng() + lng);
	  this.setLat(this.getLat() - lat);
	}

	@Override
	public int hashCode()
	{
	  double num = this.getLng();
	  int hashCode1 = (new Double(num)).hashCode();
	  num = this.getLat();
	  int hashCode2 = (new Double(num)).hashCode();
	  return hashCode1 ^ hashCode2;
	}

	@Override
	public String toString()
	{
		return "{Lat="+((Double)this.getLat())+", Lng="+(Double)this.getLng()+"}";
	}

	  public PointLatLng clone()
	  {
		  PointLatLng varCopy = new PointLatLng();

		  varCopy.lat = this.lat;
		  varCopy.lng = this.lng;
		  varCopy.NotEmpty = this.NotEmpty;

		  return varCopy;
	  }
  }