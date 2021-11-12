package demoapp.mapping;

/**
	 Orientation of axis. Some coordinate systems use non-standard orientations. 
	 For example, the first axis in South African grids usually points West, 
	 instead of East. This information is obviously relevant for algorithms
	 converting South African grid coordinates into Lat/Long.
	*/
	public enum AxisOrientationEnum 
	{
		/** 
		 Unknown or unspecified axis orientation. This can be used for local or fitted coordinate systems.
		*/
		Other((short)0),

		/** 
		 Increasing ordinates values go North. This is usually used for Grid Y coordinates and Latitude.
		*/
		North((short)1),

		/** 
		 Increasing ordinates values go South. This is rarely used.
		*/
		South((short)2),

		/** 
		 Increasing ordinates values go East. This is rarely used.
		*/
		East((short)3),

		/** 
		 Increasing ordinates values go West. This is usually used for Grid X coordinates and Longitude.
		*/
		West((short)4),

		/** 
		 Increasing ordinates values go up. This is used for vertical coordinate systems.
		*/
		Up((short)5),

		/** 
		 Increasing ordinates values go down. This is used for vertical coordinate systems.
		*/
		Down((short)6);

		public static final int SIZE = Short.SIZE;

		private short shortValue;
		private static java.util.HashMap<Short, AxisOrientationEnum> mappings;
		private static java.util.HashMap<Short, AxisOrientationEnum> getMappings()
		{
			if (mappings == null)
			{
				synchronized (AxisOrientationEnum.class)
				{
					if (mappings == null)
					{
						mappings = new java.util.HashMap<Short, AxisOrientationEnum>();
					}
				}
			}
			return mappings;
		}

		private AxisOrientationEnum(short value)
		{
			shortValue = value;
			getMappings().put(value, this);
		}

		public short getValue()
		{
			return shortValue;
		}

		public static AxisOrientationEnum forValue(short value)
		{
			return getMappings().get(value);
		}
	}