package demoapp.mapping;

/**
	 Standard ordinate index values.
	*/
	public enum Ordinate
	{
		/** 
		 X Ordinate = 0.
		*/
		X(0),

		/** 
		 Y Ordinate = 1.
		*/
		Y(1),

		/** 
		 Z Ordinate = 2.
		*/
		Z(2),

		/** 
		 M Ordinate = 3
		*/
		M(3),

		/** 
		 Ordinate at index 2
		*/
		Ordinate2(2),

		/** 
		 Ordinate at index 3
		*/
		Ordinate3(3),

		/** 
		 Ordinate at index 4
		*/
		Ordinate4(4),

		/** 
		 Ordinate at index 5
		*/
		Ordinate5(5),

		/** 
		 Ordinate at index 6
		*/
		Ordinate6(6),

		/** 
		 Ordinate at index 7
		*/
		Ordinate7(7),

		/** 
		 Ordinate at index 8
		*/
		Ordinate8(8),

		/** 
		 Ordinate at index 9
		*/
		Ordinate9(9),

		/** 
		 Ordinate at index 10
		*/
		Ordinate10(10),

		/** 
		 Ordinate at index 11
		*/
		Ordinate11(11),

		/** 
		 Ordinate at index 12
		*/
		Ordinate12(12),

		/** 
		 Ordinate at index 13
		*/
		Ordinate13(13),

		/** 
		 Ordinate at index 14
		*/
		Ordinate14(14),

		/** 
		 Ordinate at index 15
		*/
		Ordinate15(15),

		/** 
		 Ordinate at index 16
		*/
		Ordinate16(16),

		/** 
		 Ordinate at index 17
		*/
		Ordinate17(17),

		/** 
		 Ordinate at index 18
		*/
		Ordinate18(18),

		/** 
		 Ordinate at index 19
		*/
		Ordinate19(19),

		/** 
		 Ordinate at index 20
		*/
		Ordinate20(20),

		/** 
		 Ordinate at index 21
		*/
		Ordinate21(21),

		/** 
		 Ordinate at index 22
		*/
		Ordinate22(22),

		/** 
		 Ordinate at index 23
		*/
		Ordinate23(23),

		/** 
		 Ordinate at index 24
		*/
		Ordinate24(24),

		/** 
		 Ordinate at index 25
		*/
		Ordinate25(25),

		/** 
		 Ordinate at index 26
		*/
		Ordinate26(26),

		/** 
		 Ordinate at index 27
		*/
		Ordinate27(27),

		/** 
		 Ordinate at index 28
		*/
		Ordinate28(28),

		/** 
		 Ordinate at index 29
		*/
		Ordinate29(29),

		/** 
		 Ordinate at index 30
		*/
		Ordinate30(30),

		/** 
		 Ordinate at index 31
		*/
		Ordinate31(31),

		/** 
		 Ordinate at index 32
		*/
		Ordinate32(32);

		public static final int SIZE = Integer.SIZE;

		private int intValue;
		private static java.util.HashMap<Integer, Ordinate> mappings;
		private static java.util.HashMap<Integer, Ordinate> getMappings()
		{
			if (mappings == null)
			{
				synchronized (Ordinate.class)
				{
					if (mappings == null)
					{
						mappings = new java.util.HashMap<Integer, Ordinate>();
					}
				}
			}
			return mappings;
		}

		private Ordinate(int value)
		{
			intValue = value;
			getMappings().put(value, this);
		}

		public int getValue()
		{
			return intValue;
		}

		public static Ordinate forValue(int value)
		{
			return getMappings().get(value);
		}
	}