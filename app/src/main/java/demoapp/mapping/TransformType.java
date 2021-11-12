package demoapp.mapping;

/**
	 Semantic type of transform used in coordinate transformation.
	*/
	public enum TransformType 
	{
		/** 
		 Unknown or unspecified type of transform.
		*/
		Other(0),

		/** 
		 Transform depends only on defined parameters. For example, a cartographic projection.
		*/
		Conversion(1),

		/** 
		 Transform depends only on empirically derived parameters. For example a datum transformation.
		*/
		Transformation(2),

		/** 
		 Transform depends on both defined and empirical parameters.
		*/
		ConversionAndTransformation(3);

		public static final int SIZE = Integer.SIZE;

		private int intValue;
		private static java.util.HashMap<Integer, TransformType> mappings;
		private static java.util.HashMap<Integer, TransformType> getMappings()
		{
			if (mappings == null)
			{
				synchronized (TransformType.class)
				{
					if (mappings == null)
					{
						mappings = new java.util.HashMap<Integer, TransformType>();
					}
				}
			}
			return mappings;
		}

		private TransformType(int value)
		{
			intValue = value;
			getMappings().put(value, this);
		}

		public int getValue()
		{
			return intValue;
		}

		public static TransformType forValue(int value)
		{
			return getMappings().get(value);
		}
	}