package demoapp.mapping;



/** 
 Flags for Ordinate values
*/
public class Ordinates
{
	/** 
	 No ordinates
	*/
	public static final Ordinates None = new Ordinates(0);

	/** 
	 Flag for the x-ordinate 
	*/
	public static final Ordinates X = new Ordinates(1);

	/** 
	 Flag for the y-ordinate 
	*/
	public static final Ordinates Y = new Ordinates(2);

	/** 
	 Flag for both x- and y-ordinate
	*/
	public static final Ordinates XY = new Ordinates(1 | 2);

	/** 
	 Flag for the z-ordinate 
	*/
	public static final Ordinates Z = new Ordinates(4);

	/** 
	 Flag for x-, y- and z-ordinate
	*/
	public static final Ordinates XYZ = new Ordinates(1 | 2 | 4);

	/** 
	 Flag for the m-ordinate 
	*/
	public static final Ordinates M = new Ordinates(8);

	/** 
	 Flag for x-, y- and m-ordinate
	*/
	public static final Ordinates XYM = new Ordinates(1 | 2 | 8);

	/** 
	 Flag for x-, y-, z- and m-ordinate
	*/
	public static final Ordinates XYZM = new Ordinates(1 | 2 | 4 | 8);

//	/**
//	 Flag for ordinate at index 2
//	*/
//	public static final Ordinates Ordinate2 = new Ordinates(1 << Ordinate.Ordinate2);
//	/**
//	 Flag for ordinate at index 2
//	*/
//	public static final Ordinates Ordinate3 = new Ordinates(1 << Ordinate.Ordinate3);
//	/**
//	 Flag for ordinate at index 2
//	*/
//	public static final Ordinates Ordinate4 = new Ordinates(1 << Ordinate.Ordinate4);
//	/**
//	 Flag for ordinate at index 2
//	*/
//	public static final Ordinates Ordinate5 = new Ordinates(1 << Ordinate.Ordinate5);
//	/**
//	 Flag for ordinate at index 2
//	*/
//	public static final Ordinates Ordinate6 = new Ordinates(1 << Ordinate.Ordinate6);
//	/**
//	 Flag for ordinate at index 2
//	*/
//	public static final Ordinates Ordinate7 = new Ordinates(1 << Ordinate.Ordinate7);
//	/**
//	 Flag for ordinate at index 2
//	*/
//	public static final Ordinates Ordinate8 = new Ordinates(1 << Ordinate.Ordinate8);
//	/**
//	 Flag for ordinate at index 2
//	*/
//	public static final Ordinates Ordinate9 = new Ordinates(1 << Ordinate.Ordinate9);
//	/**
//	 Flag for ordinate at index 10
//	*/
//	public static final Ordinates Ordinate10 = new Ordinates(1 << Ordinate.Ordinate10);
//	/**
//	 Flag for ordinate at index 11
//	*/
//	public static final Ordinates Ordinate11 = new Ordinates(1 << Ordinate.Ordinate11);
//	/**
//	 Flag for ordinate at index 12
//	*/
//	public static final Ordinates Ordinate12 = new Ordinates(1 << Ordinate.Ordinate12);
//	/**
//	 Flag for ordinate at index 13
//	*/
//	public static final Ordinates Ordinate13 = new Ordinates(1 << Ordinate.Ordinate13);
//	/**
//	 Flag for ordinate at index 14
//	*/
//	public static final Ordinates Ordinate14 = new Ordinates(1 << Ordinate.Ordinate14);
//	/**
//	 Flag for ordinate at index 15
//	*/
//	public static final Ordinates Ordinate15 = new Ordinates(1 << Ordinate.Ordinate15);
//	/**
//	 Flag for ordinate at index 16
//	*/
//	public static final Ordinates Ordinate16 = new Ordinates(1 << Ordinate.Ordinate16);
//	/**
//	 Flag for ordinate at index 17
//	*/
//	public static final Ordinates Ordinate17 = new Ordinates(1 << Ordinate.Ordinate17);
//	/**
//	 Flag for ordinate at index 18
//	*/
//	public static final Ordinates Ordinate18 = new Ordinates(1 << Ordinate.Ordinate18);
//	/**
//	 Flag for ordinate at index 19
//	*/
//	public static final Ordinates Ordinate19 = new Ordinates(1 << Ordinate.Ordinate19);
//	/**
//	 Flag for ordinate at index 20
//	*/
//	public static final Ordinates Ordinate20 = new Ordinates(1 << Ordinate.Ordinate20);
//	/**
//	 Flag for ordinate at index 21
//	*/
//	public static final Ordinates Ordinate21 = new Ordinates(1 << Ordinate.Ordinate21);
//	/**
//	 Flag for ordinate at index 22
//	*/
//	public static final Ordinates Ordinate22 = new Ordinates(1 << Ordinate.Ordinate22);
//	/**
//	 Flag for ordinate at index 23
//	*/
//	public static final Ordinates Ordinate23 = new Ordinates(1 << Ordinate.Ordinate23);
//	/**
//	 Flag for ordinate at index 24
//	*/
//	public static final Ordinates Ordinate24 = new Ordinates(1 << Ordinate.Ordinate24);
//	/**
//	 Flag for ordinate at index 25
//	*/
//	public static final Ordinates Ordinate25 = new Ordinates(1 << Ordinate.Ordinate25);
//	/**
//	 Flag for ordinate at index 26
//	*/
//	public static final Ordinates Ordinate26 = new Ordinates(1 << Ordinate.Ordinate26);
//	/**
//	 Flag for ordinate at index 27
//	*/
//	public static final Ordinates Ordinate27 = new Ordinates(1 << Ordinate.Ordinate27);
//	/**
//	 Flag for ordinate at index 28
//	*/
//	public static final Ordinates Ordinate28 = new Ordinates(1 << Ordinate.Ordinate28);
//	/**
//	 Flag for ordinate at index 29
//	*/
//	public static final Ordinates Ordinate29 = new Ordinates(1 << Ordinate.Ordinate29);
//	/**
//	 Flag for ordinate at index 30
//	*/
//	public static final Ordinates Ordinate30 = new Ordinates(1 << Ordinate.Ordinate30);
//	/**
//	 Flag for ordinate at index 31
//	*/
//	public static final Ordinates Ordinate31 = new Ordinates(1 << Ordinate.Ordinate31);
//	/**
//	 Flag for ordinate at index 32
//	*/
//	public static final Ordinates Ordinate32 = new Ordinates(1 << Ordinate.Ordinate32);

	public static final int SIZE = Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, Ordinates> mappings;
	private static java.util.HashMap<Integer, Ordinates> getMappings()
	{
		if (mappings == null)
		{
			synchronized (Ordinates.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, Ordinates>();
				}
			}
		}
		return mappings;
	}

	private Ordinates(int value)
	{
		intValue = value;
		synchronized (Ordinates.class)
		{
			getMappings().put(value, this);
		}
	}

	public int getValue()
	{
		return intValue;
	}

	public static Ordinates forValue(int value)
	{
		synchronized (Ordinates.class)
		{
			Ordinates enumObj = getMappings().get(value);
			if (enumObj == null)
			{
				return new Ordinates(value);
			}
			else
			{
				return enumObj;
			}
		}
	}
}