package demoapp.mapping;

/**
	 Enumeration of OGC Geometry Types
	*/
	public enum OgcGeometryType
	{
		/** 
		 Point.
		*/
		Point(1),

		/** 
		 LineString.
		*/
		LineString(2),

		/** 
		 Polygon.
		*/
		Polygon(3),

		/** 
		 MultiPoint.
		*/
		MultiPoint(4),

		/** 
		 MultiLineString.
		*/
		MultiLineString(5),

		/** 
		 MultiPolygon.
		*/
		MultiPolygon(6),

		/** 
		 GeometryCollection.
		*/
		GeometryCollection(7),

		/** 
		 CircularString
		*/
		CircularString(8),

		/** 
		 CompoundCurve
		*/
		CompoundCurve(9),

		/** 
		 CurvePolygon
		*/
		CurvePolygon(10),

		/** 
		 MultiCurve
		*/
		MultiCurve(11),

		/** 
		 MultiSurface
		*/
		MultiSurface(12),

		/** 
		 Curve
		*/
		Curve(13),

		/** 
		 Surface
		*/
		Surface(14),

		/** 
		 PolyhedralSurface
		*/
		PolyhedralSurface(15),

		/** 
		 TIN
		*/
// ReSharper disable InconsistentNaming
		TIN(16);
// ReSharper restore InconsistentNaming




		/*
		/// <summary>
		/// Point with Z coordinate.
		/// </summary>
		WKBPointZ = 1001,

		/// <summary>
		/// LineString with Z coordinate.
		/// </summary>
		WKBLineStringZ = 1002,

		/// <summary>
		/// Polygon with Z coordinate.
		/// </summary>
		WKBPolygonZ = 1003,

		/// <summary>
		/// MultiPoint with Z coordinate.
		/// </summary>
		WKBMultiPointZ = 1004,

		/// <summary>
		/// MultiLineString with Z coordinate.
		/// </summary>
		WKBMultiLineStringZ = 1005,

		/// <summary>
		/// MultiPolygon with Z coordinate.
		/// </summary>
		WKBMultiPolygonZ = 1006,

		/// <summary>
		/// GeometryCollection with Z coordinate.
		/// </summary>
		WKBGeometryCollectionZ = 1007,

		/// <summary>
		/// Point with M ordinate value.
		/// </summary>
		WKBPointM = 2001,

		/// <summary>
		/// LineString with M ordinate value.
		/// </summary>
		WKBLineStringM = 2002,

		/// <summary>
		/// Polygon with M ordinate value.
		/// </summary>
		WKBPolygonM = 2003,

		/// <summary>
		/// MultiPoint with M ordinate value.
		/// </summary>
		WKBMultiPointM = 2004,

		/// <summary>
		/// MultiLineString with M ordinate value.
		/// </summary>
		WKBMultiLineStringM = 2005,

		/// <summary>
		/// MultiPolygon with M ordinate value.
		/// </summary>
		WKBMultiPolygonM = 2006,

		/// <summary>
		/// GeometryCollection with M ordinate value.
		/// </summary>
		WKBGeometryCollectionM = 2007,

		/// <summary>
		/// Point with Z coordinate and M ordinate value.
		/// </summary>
		WKBPointZM = 3001,

		/// <summary>
		/// LineString with Z coordinate and M ordinate value.
		/// </summary>
		WKBLineStringZM = 3002,

		/// <summary>
		/// Polygon with Z coordinate and M ordinate value.
		/// </summary>
		WKBPolygonZM = 3003,

		/// <summary>
		/// MultiPoint with Z coordinate and M ordinate value.
		/// </summary>
		WKBMultiPointZM = 3004,

		/// <summary>
		/// MultiLineString with Z coordinate and M ordinate value.
		/// </summary>
		WKBMultiLineStringZM = 3005,

		/// <summary>
		/// MultiPolygon with Z coordinate and M ordinate value.
		/// </summary>
		WKBMultiPolygonZM = 3006,

		/// <summary>
		/// GeometryCollection with Z coordinate and M ordinate value.
		/// </summary>
		WKBGeometryCollectionZM = 3007
		 */

		public static final int SIZE = Integer.SIZE;

		private int intValue;
		private static java.util.HashMap<Integer, OgcGeometryType> mappings;
		private static java.util.HashMap<Integer, OgcGeometryType> getMappings()
		{
			if (mappings == null)
			{
				synchronized (OgcGeometryType.class)
				{
					if (mappings == null)
					{
						mappings = new java.util.HashMap<Integer, OgcGeometryType>();
					}
				}
			}
			return mappings;
		}

		private OgcGeometryType(int value)
		{
			intValue = value;
			getMappings().put(value, this);
		}

		public int getValue()
		{
			return intValue;
		}

		public static OgcGeometryType forValue(int value)
		{
			return getMappings().get(value);
		}
	}