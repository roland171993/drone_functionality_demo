package demoapp.mapping;





/** 
 Builds up complex objects from simpler objects or values.
 
 
 <p>ICoordinateSystemFactory allows applications to make coordinate systems that 
 cannot be created by a <see cref="ICoordinateSystemAuthorityFactory"/>. This factory is very 
 flexible, whereas the authority factory is easier to use.</p>
 <p>So <see cref="ICoordinateSystemAuthorityFactory"/>can be used to make 'standard' coordinate 
 systems, and <see cref="ICoordinateSystemFactory"/> can be used to make 'special' 
 coordinate systems.</p>
 <p>For example, the EPSG authority has codes for USA state plane coordinate systems 
 using the NAD83 datum, but these coordinate systems always use meters. EPSG does not 
 have codes for NAD83 state plane coordinate systems that use feet units. This factory
 lets an application create such a hybrid coordinate system.</p>
 
*/
public interface ICoordinateSystemFactory
{
//	/**
//	 Creates a <see cref="ICompoundCoordinateSystem"/>.
//
//	 @param name Name of compound coordinate system.
//	 @param head Head coordinate system
//	 @param tail Tail coordinate system
//	 @return Compound coordinate system
//	*/
//	ICompoundCoordinateSystem CreateCompoundCoordinateSystem(String name, ICoordinateSystem head, ICoordinateSystem tail);
//
//	/**
//	 Creates an <see cref="IEllipsoid"/> from radius values.
//
//	 {@link CreateFlattenedSphere}
//	 @param name Name of ellipsoid
//	 @param semiMajorAxis
//	 @param semiMinorAxis
//	 @param linearUnit
//	 @return Ellipsoid
//	*/
//	IEllipsoid CreateEllipsoid(String name, double semiMajorAxis, double semiMinorAxis, ILinearUnit linearUnit);
//
//	/**
//	 Creates a <see cref="IFittedCoordinateSystem"/>.
//
//	 The units of the axes in the fitted coordinate system will be
//	 inferred from the units of the base coordinate system. If the affine map
//	 performs a rotation, then any mixed axes must have identical units. For
//	 example, a (lat_deg,lon_deg,height_feet) system can be rotated in the
//	 (lat,lon) plane, since both affected axes are in degrees. But you
//	 should not rotate this coordinate system in any other plane.
//	 @param name Name of coordinate system
//	 @param baseCoordinateSystem Base coordinate system
//	 @param toBaseWkt
//	 @param arAxes
//	 @return Fitted coordinate system
//	*/
//	IFittedCoordinateSystem CreateFittedCoordinateSystem(String name, ICoordinateSystem baseCoordinateSystem, String toBaseWkt, ArrayList<AxisInfo> arAxes);
//
//	/**
//	 Creates an <see cref="IEllipsoid"/> from an major radius, and inverse flattening.
//
//	 {@link CreateEllipsoid}
//	 @param name Name of ellipsoid
//	 @param semiMajorAxis Semi major-axis
//	 @param inverseFlattening Inverse flattening
//	 @param linearUnit Linear unit
//	 @return Ellipsoid
//	*/
//	IEllipsoid CreateFlattenedSphere(String name, double semiMajorAxis, double inverseFlattening, ILinearUnit linearUnit);
//
//
//	/**
//	 Creates a coordinate system object from an XML string.
//
//	 @param xml XML representation for the spatial reference
//	 @return The resulting spatial reference object
//	*/
//	ICoordinateSystem CreateFromXml(String xml);
//
//	/**
//	 Creates a spatial reference object given its Well-known text representation.
//	 The output object may be either a <see cref="IGeographicCoordinateSystem"/> or
//	 a <see cref="IProjectedCoordinateSystem"/>.
//
//	 @param WKT The Well-known text representation for the spatial reference
//	 @return The resulting spatial reference object
//	*/
//	ICoordinateSystem CreateFromWkt(String WKT);
//
//	/**
//	 Creates a <see cref="IGeographicCoordinateSystem"/>, which could be Lat/Lon or Lon/Lat.
//
//	 @param name Name of geographical coordinate system
//	 @param angularUnit Angular units
//	 @param datum Horizontal datum
//	 @param primeMeridian Prime meridian
//	 @param axis0 First axis
//	 @param axis1 Second axis
//	 @return Geographic coordinate system
//	*/
//	IGeographicCoordinateSystem CreateGeographicCoordinateSystem(String name, IAngularUnit angularUnit, IHorizontalDatum datum, IPrimeMeridian primeMeridian, AxisInfo axis0, AxisInfo axis1);
//
//	/**
//	 Creates <see cref="IHorizontalDatum"/> from ellipsoid and Bursa-World parameters.
//
//
//	 Since this method contains a set of Bursa-Wolf parameters, the created
//	 datum will always have a relationship to WGS84. If you wish to create a
//	 horizontal datum that has no relationship with WGS84, then you can
//	 either specify a <see cref="DatumType">horizontalDatumType</see> of <see cref="DatumType.HD_Other"/>, or create it via WKT.
//
//	 @param name Name of ellipsoid
//	 @param datumType Type of datum
//	 @param ellipsoid Ellipsoid
//	 @param toWgs84 Wgs84 conversion parameters
//	 @return Horizontal datum
//	*/
//	IHorizontalDatum CreateHorizontalDatum(String name, DatumType datumType, IEllipsoid ellipsoid, Wgs84ConversionInfo toWgs84);
//
//	/**
//	 Creates a <see cref="ILocalCoordinateSystem">local coordinate system</see>.
//
//
//	  The dimension of the local coordinate system is determined by the size of
//	 the axis array. All the axes will have the same units. If you want to make
//	 a coordinate system with mixed units, then you can make a compound
//	 coordinate system from different local coordinate systems.
//
//	 @param name Name of local coordinate system
//	 @param datum Local datum
//	 @param unit Units
//	 @param axes Axis info
//	 @return Local coordinate system
//	*/
//	ILocalCoordinateSystem CreateLocalCoordinateSystem(String name, ILocalDatum datum, IUnit unit, ArrayList<AxisInfo> axes);
//
//	/**
//	 Creates a <see cref="ILocalDatum"/>.
//
//	 @param name Name of datum
//	 @param datumType Datum type
//	 @return
//	*/
//	ILocalDatum CreateLocalDatum(String name, DatumType datumType);
//
//	/**
//	 Creates a <see cref="IPrimeMeridian"/>, relative to Greenwich.
//
//	 @param name Name of prime meridian
//	 @param angularUnit Angular unit
//	 @param longitude Longitude
//	 @return Prime meridian
//	*/
//	IPrimeMeridian CreatePrimeMeridian(String name, IAngularUnit angularUnit, double longitude);
//
//	/**
//	 Creates a <see cref="IProjectedCoordinateSystem"/> using a projection object.
//
//	 @param name Name of projected coordinate system
//	 @param gcs Geographic coordinate system
//	 @param projection Projection
//	 @param linearUnit Linear unit
//	 @param axis0 Primary axis
//	 @param axis1 Secondary axis
//	 @return Projected coordinate system
//	*/
//	IProjectedCoordinateSystem CreateProjectedCoordinateSystem(String name, IGeographicCoordinateSystem gcs, IProjection projection, ILinearUnit linearUnit, AxisInfo axis0, AxisInfo axis1);
//
//	/**
//	 Creates a <see cref="IProjection"/>.
//
//	 @param name Name of projection
//	 @param wktProjectionClass Projection class
//	 @param Parameters Projection parameters
//	 @return Projection
//	*/
//	IProjection CreateProjection(String name, String wktProjectionClass, ArrayList<ProjectionParameter> Parameters);
//
//	/**
//	 Creates a <see cref="IVerticalCoordinateSystem"/> from a <see cref="IVerticalDatum">datum</see> and <see cref="ILinearUnit">linear units</see>.
//
//	 @param name Name of vertical coordinate system
//	 @param datum Vertical datum
//	 @param verticalUnit Unit
//	 @param axis Axis info
//	 @return Vertical coordinate system
//	*/
//	IVerticalCoordinateSystem CreateVerticalCoordinateSystem(String name, IVerticalDatum datum, ILinearUnit verticalUnit, AxisInfo axis);
//
//	/**
//	 Creates a <see cref="IVerticalDatum"/> from an enumerated type value.
//
//	 @param name Name of datum
//	 @param datumType Type of datum
//	 @return Vertical datum
//	*/
//	IVerticalDatum CreateVerticalDatum(String name, DatumType datumType);
}