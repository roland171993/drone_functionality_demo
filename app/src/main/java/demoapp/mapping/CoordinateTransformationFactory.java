package demoapp.mapping;


import java.util.concurrent.CopyOnWriteArrayList;


/**
 Creates coordinate transformations.
*/
public class CoordinateTransformationFactory implements ICoordinateTransformationFactory
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region ICoordinateTransformationFactory Members

	/**
	 Creates a transformation between two coordinate systems.


	 This method will examine the coordinate systems in order to construct
	 a transformation between them. This method may fail if no path between
	 the coordinate systems is found, using the normal failing behavior of
	 the DCP (e.g. throwing an exception).
	 @param sourceCS Source coordinate system
	 @param targetCS Target coordinate system
	 @return
	*/
	public final ICoordinateTransformation CreateFromCoordinateSystems(ICoordinateSystem sourceCS, ICoordinateSystem targetCS)
	{
		ICoordinateTransformation trans;
		if (sourceCS instanceof IProjectedCoordinateSystem && targetCS instanceof IGeographicCoordinateSystem) //Projected -> Geographic
		{
			trans = Proj2Geog((IProjectedCoordinateSystem)sourceCS, (IGeographicCoordinateSystem)targetCS);
		}
		else if (sourceCS instanceof IGeographicCoordinateSystem && targetCS instanceof IProjectedCoordinateSystem) //Geographic -> Projected
		{
			trans = Geog2Proj((IGeographicCoordinateSystem)sourceCS, (IProjectedCoordinateSystem)targetCS);
		}

		else if (sourceCS instanceof IGeographicCoordinateSystem && targetCS instanceof IGeocentricCoordinateSystem) //Geocentric -> Geographic
		{
			trans = Geog2Geoc((IGeographicCoordinateSystem)sourceCS, (IGeocentricCoordinateSystem)targetCS);
		}

		else if (sourceCS instanceof IGeocentricCoordinateSystem && targetCS instanceof IGeographicCoordinateSystem) //Geocentric -> Geographic
		{
			trans = Geoc2Geog((IGeocentricCoordinateSystem)sourceCS, (IGeographicCoordinateSystem)targetCS);
		}

		else if (sourceCS instanceof IProjectedCoordinateSystem && targetCS instanceof IProjectedCoordinateSystem) //Projected -> Projected
		{
			trans = Proj2Proj((sourceCS instanceof IProjectedCoordinateSystem ? (IProjectedCoordinateSystem)sourceCS : null), (targetCS instanceof IProjectedCoordinateSystem ? (IProjectedCoordinateSystem)targetCS : null));
		}

		else if (sourceCS instanceof IGeocentricCoordinateSystem && targetCS instanceof IGeocentricCoordinateSystem) //Geocentric -> Geocentric
		{
			trans = CreateGeoc2Geoc((IGeocentricCoordinateSystem)sourceCS, (IGeocentricCoordinateSystem)targetCS);
		}

		else if (sourceCS instanceof IGeographicCoordinateSystem && targetCS instanceof IGeographicCoordinateSystem) //Geographic -> Geographic
		{
			trans = CreateGeog2Geog(sourceCS instanceof IGeographicCoordinateSystem ? (IGeographicCoordinateSystem)sourceCS : null, targetCS instanceof IGeographicCoordinateSystem ? (IGeographicCoordinateSystem)targetCS : null);
		}
		else if (sourceCS instanceof IFittedCoordinateSystem) //Fitted -> Any
		{
			trans = Fitt2Any((IFittedCoordinateSystem)sourceCS, targetCS);
		}
		else if (targetCS instanceof IFittedCoordinateSystem) //Any -> Fitted
		{
			trans = Any2Fitt(sourceCS, (IFittedCoordinateSystem)targetCS);
		}
		else
		{
			throw new UnsupportedOperationException("No support for transforming between the two specified coordinate systems");
		}

		//if (trans.MathTransform is ConcatenatedTransform) {
		//    List<ICoordinateTransformation> MTs = new List<ICoordinateTransformation>();
		//    SimplifyTrans(trans.MathTransform as ConcatenatedTransform, ref MTs);
		//    return new CoordinateTransformation(sourceCS,
		//        targetCS, TransformType.Transformation, new ConcatenatedTransform(MTs),
		//        String.Empty, String.Empty, -1, String.Empty, String.Empty);
		//}
		return trans;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	private static void SimplifyTrans(ConcatenatedTransform mtrans, RefObject<CopyOnWriteArrayList<ICoordinateTransformation>> MTs)
	{
		for (ICoordinateTransformation t : mtrans.getCoordinateTransformationList())
		{
			if (t instanceof ConcatenatedTransform)
			{
				SimplifyTrans(t instanceof ConcatenatedTransform ? (ConcatenatedTransform)t : null, MTs);
			}
			else
			{
				MTs.argValue.add(t);
			}
		}
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Methods for converting between specific systems





	private static ICoordinateTransformation Geog2Proj(IGeographicCoordinateSystem source, IProjectedCoordinateSystem target)
	{
		if (source.EqualParams(target.getGeographicCoordinateSystem()))
		{
			IMathTransform mathTransform = CreateCoordinateOperation(target.getProjection(), target.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid(), target.getLinearUnit());
			return new CoordinateTransformation(source, target, TransformType.Transformation, mathTransform, "", "", -1, "", "");
		}
		else
		{
			// Geographic coordinatesystems differ - Create concatenated transform
			ConcatenatedTransform ct = new ConcatenatedTransform();
			CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
			ct.getCoordinateTransformationList().add(ctFac.CreateFromCoordinateSystems(source, target.getGeographicCoordinateSystem()));
			ct.getCoordinateTransformationList().add(ctFac.CreateFromCoordinateSystems(target.getGeographicCoordinateSystem(), target));
			return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
		}
	}

	private static ICoordinateTransformation Proj2Geog(IProjectedCoordinateSystem source, IGeographicCoordinateSystem target)
	{
		if (source.getGeographicCoordinateSystem().EqualParams(target))
		{
			IMathTransform mathTransform = CreateCoordinateOperation(source.getProjection(), source.getGeographicCoordinateSystem().getHorizontalDatum().getEllipsoid(), source.getLinearUnit()).Inverse();
			return new CoordinateTransformation(source, target, TransformType.Transformation, mathTransform, "", "", -1, "", "");
		}
		else
		{ // Geographic coordinatesystems differ - Create concatenated transform
			ConcatenatedTransform ct = new ConcatenatedTransform();
			CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
			ct.getCoordinateTransformationList().add(ctFac.CreateFromCoordinateSystems(source, source.getGeographicCoordinateSystem()));
			ct.getCoordinateTransformationList().add(ctFac.CreateFromCoordinateSystems(source.getGeographicCoordinateSystem(), target));
			return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
		}
	}






	private static IMathTransform CreateCoordinateOperation(IProjection projection, IEllipsoid ellipsoid, ILinearUnit unit)
	{
		CopyOnWriteArrayList<ProjectionParameter> parameterList = new CopyOnWriteArrayList<ProjectionParameter>();
		for (int i = 0; i < projection.getNumParameters(); i++)
		{
			parameterList.add(projection.GetParameter(i));
		}

		//var toMeter = 1d/ellipsoid.AxisUnit.MetersPerUnit;
		if (ListHelper.find(parameterList, (p) -> p.getName().toLowerCase().replace(' ', '_').equals("semi_major")) == null)
		{
			parameterList.add(new ProjectionParameter("semi_major", ellipsoid.getSemiMajorAxis()));
		}
		if (ListHelper.find(parameterList, (p) -> p.getName().toLowerCase().replace(' ', '_').equals("semi_minor")) == null)
		{
			parameterList.add(new ProjectionParameter("semi_minor", ellipsoid.getSemiMinorAxis()));
		}
		if (ListHelper.find(parameterList, (p) -> p.getName().toLowerCase().replace(' ', '_').equals("unit")) == null)
		{
			parameterList.add(new ProjectionParameter("unit", unit.getMetersPerUnit()));
		}

//		IMathTransform operation = ProjectionsRegistry.CreateProjection(projection.getClassName(), parameterList);
//		/*
//		var mpOperation = operation as MapProjection;
//		if (mpOperation != null && projection.AuthorityCode !=-1)
//		{
//			mpOperation.Authority = projection.Authority;
//			mpOperation.AuthorityCode = projection.AuthorityCode;
//		}
//		 */

		// return operation;

		IMathTransform operation;
		switch (projection.getClassName().toLowerCase().replace(' ', '_'))
		{
			case "mercator":
			case "mercator_1sp":
			case "mercator_2sp":
				//1SP
				operation = new Mercator(parameterList);
				break;
			case "transverse_mercator":
				operation = new TransverseMercator(parameterList);
				break;
			case "albers":
			case "albers_conic_equal_area":
				operation = new AlbersProjection(parameterList);
				break;
			case "krovak":
				operation = new KrovakProjection(parameterList);
				break;
			case "polyconic":
				operation = new PolyconicProjection(parameterList);
				break;
			case "lambert_conformal_conic":
			case "lambert_conformal_conic_2sp":
			case "lambert_conic_conformal_(2sp)":
				operation = new LambertConformalConic2SP(parameterList);
				break;
			default:
				throw new UnsupportedOperationException("Projection {0} is not supported." + projection.getClassName());
		}
		return operation;

	}

	private static ICoordinateTransformation Geog2Geoc(IGeographicCoordinateSystem source, IGeocentricCoordinateSystem target)
	{
		IMathTransform geocMathTransform = CreateCoordinateOperation(target);
		if (source.getPrimeMeridian().EqualParams(target.getPrimeMeridian()))
		{
			return new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", "");
		}
		else
		{
			ConcatenatedTransform ct = new ConcatenatedTransform();
			ct.getCoordinateTransformationList().add(new CoordinateTransformation(source, target, TransformType.Transformation, new PrimeMeridianTransform(source.getPrimeMeridian(), target.getPrimeMeridian()), "", "", -1, "", ""));
			ct.getCoordinateTransformationList().add(new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", ""));
			return new CoordinateTransformation(source, target, TransformType.Conversion, ct, "", "", -1, "", "");
		}
	}

	private static IMathTransform CreateCoordinateOperation(IGeocentricCoordinateSystem geo)
	{
		CopyOnWriteArrayList<ProjectionParameter> parameterList = new CopyOnWriteArrayList<ProjectionParameter>();

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		IEllipsoid ellipsoid = geo.getHorizontalDatum().getEllipsoid();
		//var toMeter = ellipsoid.AxisUnit.MetersPerUnit;
		if (ListHelper.find(parameterList, (p) -> p.getName().toLowerCase().replace(' ', '_').equals("semi_major")) == null)
		{
			parameterList.add(new ProjectionParameter("semi_major", ellipsoid.getSemiMajorAxis()));
		}
		if (ListHelper.find(parameterList, (p) -> p.getName().toLowerCase().replace(' ', '_').equals("semi_minor")) == null)
		{
			parameterList.add(new ProjectionParameter("semi_minor", ellipsoid.getSemiMinorAxis()));
		}

		return new GeocentricTransform(parameterList);
	}

	private static ICoordinateTransformation Geoc2Geog(IGeocentricCoordinateSystem source, IGeographicCoordinateSystem target)
	{
		IMathTransform geocMathTransform = CreateCoordinateOperation(source).Inverse();
		if (source.getPrimeMeridian().EqualParams(target.getPrimeMeridian()))
		{
			return new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", "");
		}
		else
		{
			ConcatenatedTransform ct = new ConcatenatedTransform();
			ct.getCoordinateTransformationList().add(new CoordinateTransformation(source, target, TransformType.Conversion, geocMathTransform, "", "", -1, "", ""));
			ct.getCoordinateTransformationList().add(new CoordinateTransformation(source, target, TransformType.Transformation, new PrimeMeridianTransform(source.getPrimeMeridian(), target.getPrimeMeridian()), "", "", -1, "", ""));
			return new CoordinateTransformation(source, target, TransformType.Conversion, ct, "", "", -1, "", "");
		}
	}

	private static ICoordinateTransformation Proj2Proj(IProjectedCoordinateSystem source, IProjectedCoordinateSystem target)
	{
		ConcatenatedTransform ct = new ConcatenatedTransform();
		CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
		//First transform from projection to geographic
		ct.getCoordinateTransformationList().add(ctFac.CreateFromCoordinateSystems(source, source.getGeographicCoordinateSystem()));
		//Transform geographic to geographic:
		ICoordinateTransformation geogToGeog = ctFac.CreateFromCoordinateSystems(source.getGeographicCoordinateSystem(), target.getGeographicCoordinateSystem());
		if (geogToGeog != null)
		{
			ct.getCoordinateTransformationList().add(geogToGeog);
		}
		//Transform to new projection
		ct.getCoordinateTransformationList().add(ctFac.CreateFromCoordinateSystems(target.getGeographicCoordinateSystem(), target));

		return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
	}

	private static ICoordinateTransformation CreateGeoc2Geoc(IGeocentricCoordinateSystem source, IGeocentricCoordinateSystem target)
	{
		ConcatenatedTransform ct = new ConcatenatedTransform();

		//Does source has a datum different from WGS84 and is there a shift specified?
		if (source.getHorizontalDatum().getWgs84Parameters() != null && !source.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly())
		{
			ct.getCoordinateTransformationList().add(new CoordinateTransformation(((target.getHorizontalDatum().getWgs84Parameters() == null || target.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly()) ? target : GeocentricCoordinateSystem.getWGS84()), source, TransformType.Transformation, new DatumTransform(source.getHorizontalDatum().getWgs84Parameters()), "", "", -1, "", ""));
		}

		//Does target has a datum different from WGS84 and is there a shift specified?
		if (target.getHorizontalDatum().getWgs84Parameters() != null && !target.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly())
		{
			ct.getCoordinateTransformationList().add(new CoordinateTransformation(((source.getHorizontalDatum().getWgs84Parameters() == null || source.getHorizontalDatum().getWgs84Parameters().getHasZeroValuesOnly()) ? source : GeocentricCoordinateSystem.getWGS84()), target, TransformType.Transformation, (new DatumTransform(target.getHorizontalDatum().getWgs84Parameters())).Inverse(), "", "", -1, "", ""));
		}

		//If we don't have a transformation in this list, return null
		if (ct.getCoordinateTransformationList().isEmpty())
		{
			return null;
		}
		//If we only have one shift, lets just return the datumshift from/to wgs84
		if (ct.getCoordinateTransformationList().size() == 1)
		{
			return new CoordinateTransformation(source, target, TransformType.ConversionAndTransformation, ct.getCoordinateTransformationList().get(0).getMathTransform(), "", "", -1, "", "");
		}

		return new CoordinateTransformation(source, target, TransformType.ConversionAndTransformation, ct, "", "", -1, "", "");
	}

	private static ICoordinateTransformation CreateGeog2Geog(IGeographicCoordinateSystem source, IGeographicCoordinateSystem target)
	{
		if (source.getHorizontalDatum().EqualParams(target.getHorizontalDatum()))
		{
			//No datum shift needed
			return new CoordinateTransformation(source, target, TransformType.Conversion, new GeographicTransform(source, target), "", "", -1, "", "");
		}
		else
		{
			//Create datum shift
			//Convert to geocentric, perform shift and return to geographic
			CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
			CoordinateSystemFactory cFac = new CoordinateSystemFactory();
			IGeocentricCoordinateSystem sourceCentric = cFac.CreateGeocentricCoordinateSystem(source.getHorizontalDatum().getName() + " Geocentric", source.getHorizontalDatum(), LinearUnit.getMetre(), source.getPrimeMeridian());
			IGeocentricCoordinateSystem targetCentric = cFac.CreateGeocentricCoordinateSystem(target.getHorizontalDatum().getName() + " Geocentric", target.getHorizontalDatum(), LinearUnit.getMetre(), source.getPrimeMeridian());
			ConcatenatedTransform ct = new ConcatenatedTransform();
			AddIfNotNull(ct, ctFac.CreateFromCoordinateSystems(source, sourceCentric));
			AddIfNotNull(ct, ctFac.CreateFromCoordinateSystems(sourceCentric, targetCentric));
			AddIfNotNull(ct, ctFac.CreateFromCoordinateSystems(targetCentric, target));


			return new CoordinateTransformation(source, target, TransformType.Transformation, ct, "", "", -1, "", "");
		}
	}

	private static void AddIfNotNull(ConcatenatedTransform concatTrans, ICoordinateTransformation trans)
	{
		if (trans != null)
		{
			concatTrans.getCoordinateTransformationList().add(trans);
		}
	}


	private static ICoordinateTransformation Fitt2Any(IFittedCoordinateSystem source, ICoordinateSystem target)
	{
		//transform from fitted to base system of fitted (which is equal to target)
		IMathTransform mt = CreateFittedTransform(source);

		//case when target system is equal to base system of the fitted
		if (source.getBaseCoordinateSystem().EqualParams(target))
		{
			//Transform form base system of fitted to target coordinate system
			return CreateTransform(source, target, TransformType.Transformation, mt);
		}

		//Transform form base system of fitted to target coordinate system
		ConcatenatedTransform ct = new ConcatenatedTransform();
		ct.getCoordinateTransformationList().add(CreateTransform(source, source.getBaseCoordinateSystem(), TransformType.Transformation, mt));

		//Transform form base system of fitted to target coordinate system
		CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
		ct.getCoordinateTransformationList().add(ctFac.CreateFromCoordinateSystems(source.getBaseCoordinateSystem(), target));

		return CreateTransform(source, target, TransformType.Transformation, ct);
	}

	private static IMathTransform CreateFittedTransform(IFittedCoordinateSystem fittedSystem)
	{
		//create transform From fitted to base and inverts it
		if (fittedSystem instanceof FittedCoordinateSystem)
		{
			return ((FittedCoordinateSystem)fittedSystem).getToBaseTransform();
		}

		//MathTransformFactory mtFac = new MathTransformFactory ();
		////create transform From fitted to base and inverts it
		//return mtFac.CreateFromWKT (fittedSystem.ToBase ());

		throw new UnsupportedOperationException();
	}

	private static CoordinateTransformation CreateTransform(ICoordinateSystem sourceCS, ICoordinateSystem targetCS, TransformType transformType, IMathTransform mathTransform)
	{
		return new CoordinateTransformation(sourceCS, targetCS, transformType, mathTransform, "", "", -1, "", "");
	}

	private static ICoordinateTransformation Any2Fitt(ICoordinateSystem source, IFittedCoordinateSystem target)
	{
		//Transform form base system of fitted to target coordinate system - use invered math transform
		IMathTransform invMt = CreateFittedTransform(target).Inverse();

		//case when source system is equal to base system of the fitted
		if (target.getBaseCoordinateSystem().EqualParams(source))
		{
			//Transform form base system of fitted to target coordinate system
			return CreateTransform(source, target, TransformType.Transformation, invMt);
		}

		ConcatenatedTransform ct = new ConcatenatedTransform();
		//First transform from source to base system of fitted
		CoordinateTransformationFactory ctFac = new CoordinateTransformationFactory();
		ct.getCoordinateTransformationList().add(ctFac.CreateFromCoordinateSystems(source, target.getBaseCoordinateSystem()));

		//Transform form base system of fitted to target coordinate system - use invered math transform
		ct.getCoordinateTransformationList().add(CreateTransform(target.getBaseCoordinateSystem(), target, TransformType.Transformation, invMt));

		return CreateTransform(source, target, TransformType.Transformation, ct);
	}
}