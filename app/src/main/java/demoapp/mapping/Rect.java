package demoapp.mapping;

//C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this class may differ from the original:
//ORIGINAL LINE: public struct Rect
public final class Rect
{
	public double Top;
	public double Bottom;
	public double Left;
	public double Right;

	public double getWidth()
	{
		return Right - Left;
	}
	public double getHeight()
	{
		return Top - Bottom;
	}

	public double getMidWidth()
	{
		return ((Right - Left) / 2) + Left;
	}
	public double getMidHeight()
	{
		return ((Top - Bottom) / 2) + Bottom;
	}

	public Rect()
	{
	}

	public Rect(double Left, double Top, double Width, double Height)
	{
		this.Left = Left;
		this.Top = Top;
		this.Right = Left + Width;
		this.Bottom = Top + Height;
	}

	public double DiagDistance()
	{
		// pythagarus
		return Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
	}


	public Rect clone()
	{
		Rect varCopy = new Rect();

		varCopy.Top = this.Top;
		varCopy.Bottom = this.Bottom;
		varCopy.Left = this.Left;
		varCopy.Right = this.Right;

		return varCopy;
	}
}