package demoapp.mapping;

/** the size of coordinates
  */
//C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this class may differ from the original:
//ORIGINAL LINE: public struct SizeLatLng
  public final class SizeLatLng
  {
	public static final SizeLatLng Empty = new SizeLatLng();
	private double heightLat;
	private double widthLng;

	public SizeLatLng()
	{
	}

	public SizeLatLng(SizeLatLng size)
	{
	  this.widthLng = size.widthLng;
	  this.heightLat = size.heightLat;
	}

	public SizeLatLng(PointLatLng pt)
	{
	  this.heightLat = pt.getLat();
	  this.widthLng = pt.getLng();
	}

	public SizeLatLng(double heightLat, double widthLng)
	{
	  this.heightLat = heightLat;
	  this.widthLng = widthLng;
	}

	public static SizeLatLng opAdd(SizeLatLng sz1, SizeLatLng sz2)
	{
	  return SizeLatLng.Add(sz1.clone(), sz2.clone());
	}

	public static SizeLatLng opSubtract(SizeLatLng sz1, SizeLatLng sz2)
	{
	  return SizeLatLng.Subtract(sz1.clone(), sz2.clone());
	}

	public static boolean opEquals(SizeLatLng sz1, SizeLatLng sz2)
	{
	  if (sz1.getWidthLng() == sz2.getWidthLng())
	  {
		return sz1.getHeightLat() == sz2.getHeightLat();
	  }
	  return false;
	}

	public static boolean opNotEquals(SizeLatLng sz1, SizeLatLng sz2)
	{
	  return !opEquals(sz1.clone(), sz2.clone());
	}

	public static PointLatLng toPointLatLng(SizeLatLng size)
	{
	  return new PointLatLng(size.getHeightLat(), size.getWidthLng());
	}

	public boolean getIsEmpty()
	{
	  if (this.widthLng == 0.0)
	  {
		return this.heightLat == 0.0;
	  }
	  return false;
	}

	public double getWidthLng()
	{
	  return this.widthLng;
	}
	public void setWidthLng(double value)
	{
	  this.widthLng = value;
	}

	public double getHeightLat()
	{
	  return this.heightLat;
	}
	public void setHeightLat(double value)
	{
	  this.heightLat = value;
	}

	public static SizeLatLng Add(SizeLatLng sz1, SizeLatLng sz2)
	{
	  return new SizeLatLng(sz1.getHeightLat() + sz2.getHeightLat(), sz1.getWidthLng() + sz2.getWidthLng());
	}

	public static SizeLatLng Subtract(SizeLatLng sz1, SizeLatLng sz2)
	{
	  return new SizeLatLng(sz1.getHeightLat() - sz2.getHeightLat(), sz1.getWidthLng() - sz2.getWidthLng());
	}

	@Override
	public boolean equals(Object obj)
	{
	  if (!(obj instanceof SizeLatLng))
	  {
		return false;
	  }
	  SizeLatLng sizeLatLng = (SizeLatLng) obj;
	  if (sizeLatLng.getWidthLng() == this.getWidthLng() && sizeLatLng.getHeightLat() == this.getHeightLat())
	  {
		return sizeLatLng.getClass().equals(this.getClass());
	  }
	  return false;
	}

	@Override
	public int hashCode()
	{
	  if (this.getIsEmpty())
	  {
		return 0;
	  }
	  return (new Double(this.getWidthLng())).hashCode() ^ (new Double(this.getHeightLat())).hashCode();
	}

	public PointLatLng ToPointLatLng()
	{
	  return toPointLatLng(this.clone());
	}

	@Override
	public String toString()
	{
	  return "{WidthLng=" + (Double.valueOf(this.widthLng)) + ", HeightLng=" + (Double.valueOf(this.heightLat))+ "}";
	}

	  public SizeLatLng clone()
	  {
		  SizeLatLng varCopy = new SizeLatLng();

		  varCopy.heightLat = this.heightLat;
		  varCopy.widthLng = this.widthLng;

		  return varCopy;
	  }
  }