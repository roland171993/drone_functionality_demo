package demoapp.mapping;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class ConcatenatedTransform extends MathTransform
{
	/** 
	 
	*/
	protected IMathTransform _inverse;

	/** 
	 
	*/
	public ConcatenatedTransform()
	{
		this(new CopyOnWriteArrayList<ICoordinateTransformation>());
	}

	/** 
	 
	 
	 @param transformlist
	*/
	public ConcatenatedTransform(CopyOnWriteArrayList<ICoordinateTransformation> transformlist)
	{
		_coordinateTransformationList = transformlist;
	}

	private CopyOnWriteArrayList<ICoordinateTransformation> _coordinateTransformationList;

	/** 
	 
	*/
	public final CopyOnWriteArrayList<ICoordinateTransformation> getCoordinateTransformationList()
	{
		return _coordinateTransformationList;
	}
	public final void setCoordinateTransformationList(CopyOnWriteArrayList<ICoordinateTransformation> value)
	{
		_coordinateTransformationList = value;
		_inverse = null;
	}

	@Override
	public int getDimSource()
	{
		return _coordinateTransformationList.get(0).getSourceCS().getDimension();
	}

	@Override
	public int getDimTarget()
	{
		return _coordinateTransformationList.get(_coordinateTransformationList.size() - 1).getTargetCS().getDimension();
	}

	/** 
	 Transforms a point
	 
	 @param point
	 @return 
	*/
	@Override
	public double[] Transform(double[] point)
	{
		for (ICoordinateTransformation ct : _coordinateTransformationList)
		{
			point = ct.getMathTransform().Transform(point);
		}
		return point;
	}

	/** 
	 Transforms a list point
	 
	 @param points
	 @return 
	*/
	@Override
	public List<double[]> TransformList(List<double[]> points)
	{
		List<double[]> pnts = new CopyOnWriteArrayList<double[]>(points);
		for (ICoordinateTransformation ct : _coordinateTransformationList)
		{
			pnts = ct.getMathTransform().TransformList(pnts);
		}
		return pnts;
	}

	@Override
	public List<Coordinate> TransformListCoordinateList(List<Coordinate> points)
	{
		List<Coordinate> pnts = new CopyOnWriteArrayList<Coordinate>(points);
		for (ICoordinateTransformation ct : _coordinateTransformationList)
		{
			pnts = ct.getMathTransform().TransformListCoordinateList(pnts);
		}
		return pnts;
	}

	@Override
	public ICoordinateSequence Transform(ICoordinateSequence coordinateSequence)
	{
		// ICoordinateSequence res = (ICoordinateSequence)coordinateSequence.Clone();
		ICoordinateSequence res = (ICoordinateSequence)coordinateSequence;
		for (ICoordinateTransformation ct : _coordinateTransformationList)
		{
			res = ct.getMathTransform().Transform(res);
		}
		return res;
	}

	/** 
	 Returns the inverse of this conversion.
	 
	 @return IMathTransform that is the reverse of the current conversion.
	*/
	@Override
	public IMathTransform Inverse()
	{
		if (_inverse == null)
		{
			_inverse = Clone();
			_inverse.Invert();
		}
		return _inverse;
	}

	/** 
	 Reverses the transformation
	*/
	@Override
	public void Invert()
	{
		Collections.reverse(_coordinateTransformationList);
		for (ICoordinateTransformation ic : _coordinateTransformationList)
		{
			ic.getMathTransform().Invert();
		}
	}

	public final ConcatenatedTransform Clone()
	{
		CopyOnWriteArrayList<ICoordinateTransformation> clonedList = new CopyOnWriteArrayList<ICoordinateTransformation>();
		for (ICoordinateTransformation ct : _coordinateTransformationList)
		{
			clonedList.add(ct);
		}
		return new ConcatenatedTransform(clonedList);
	}

	/** 
	 Gets a Well-Known text representation of this object.
	 
	 <value></value>
	*/
	@Override
	public String getWKT()
	{
		throw new UnsupportedOperationException();
	}

	/** 
	 Gets an XML representation of this object.
	 
	 <value></value>
	*/
	@Override
	public String getXML()
	{
		throw new UnsupportedOperationException();
	}
}