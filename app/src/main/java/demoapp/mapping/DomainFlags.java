package demoapp.mapping;

/**
	 Flags indicating parts of domain covered by a convex hull. 
	 
	 
	 These flags can be combined. For example, the value 3 
	 corresponds to a combination of <see cref="Inside"/> and <see cref="Outside"/>,
	 which means that some parts of the convex hull are inside the 
	 domain, and some parts of the convex hull are outside the domain.
	 
	*/
	public enum DomainFlags 
	{
		/** 
		 At least one point in a convex hull is inside the transform's domain.
		*/
		Inside(1),

		/** 
		 At least one point in a convex hull is outside the transform's domain.
		*/
		Outside(2),

		/** 
		 At least one point in a convex hull is not transformed continuously.
		 
		 
		 As an example, consider a "Longitude_Rotation" transform which adjusts 
		 longitude coordinates to take account of a change in Prime Meridian. If
		 the rotation is 5 degrees east, then the point (Lat=175,Lon=0) is not 
		 transformed continuously, since it is on the meridian line which will 
		 be split at +180/-180 degrees.
		 
		*/
		Discontinuous(4);

		public static final int SIZE = Integer.SIZE;

		private int intValue;
		private static java.util.HashMap<Integer, DomainFlags> mappings;
		private static java.util.HashMap<Integer, DomainFlags> getMappings()
		{
			if (mappings == null)
			{
				synchronized (DomainFlags.class)
				{
					if (mappings == null)
					{
						mappings = new java.util.HashMap<Integer, DomainFlags>();
					}
				}
			}
			return mappings;
		}

		private DomainFlags(int value)
		{
			intValue = value;
			getMappings().put(value, this);
		}

		public int getValue()
		{
			return intValue;
		}

		public static DomainFlags forValue(int value)
		{
			return getMappings().get(value);
		}
	}