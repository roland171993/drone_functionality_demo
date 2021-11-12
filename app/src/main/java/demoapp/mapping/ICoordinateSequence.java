package demoapp.mapping;//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_ICLONEABLE
//#else
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter could not confirm whether this is a namespace alias or a type alias:
//	using ICloneable = GeoAPI.ICloneable;
//#endif

/**
	 The internal representation of a list of coordinates inside a Geometry.
	 <p>
	 This allows Geometries to store their
	 points using something other than the NTS <see cref="Coordinate"/> class. 
	 For example, a storage-efficient implementation
	 might store coordinate sequences as an array of x's
	 and an array of y's. 
	 Or a custom coordinate class might support extra attributes like M-values.
	 </p>
	 <p>
	 Implementing a custom coordinate storage structure
	 requires implementing the <see cref="ICoordinateSequence"/> and
	 <see cref="ICoordinateSequenceFactory"/> interfaces. 
	 To use the custom CoordinateSequence, create a
	 new <see cref="IGeometryFactory"/> parameterized by the CoordinateSequenceFactory
	 The <see cref="IGeometryFactory"/> can then be used to create new <see cref="IGeometry"/>s.
	 The new Geometries will use the custom CoordinateSequence implementation.
	 </p>
	 
	*/
	///// <remarks>
	///// For an example see <see cref="ExtendedCoordinateSample"/>
	///// </remarks>
	///// <seealso cref="NetTopologySuite.Geometries.Implementation.CoordinateArraySequenceFactory"/>
	///// <seealso cref="NetTopologySuite.Geometries.Implementation.ExtendedCoordinateExample"/>
	///// <seealso cref="NetTopologySuite.Geometries.Implementation.PackedCoordinateSequenceFactory"/>
	public interface ICoordinateSequence extends Cloneable
	{
		/** 
		 Returns the dimension (number of ordinates in each coordinate) for this sequence.
		*/

		/** 
		 Returns the kind of ordinates this sequence supplys. .
		*/
		Ordinates getOrdinates();

		/** 
		 Returns the number of coordinates in this sequence.
		*/
		int getCount();

		/** 
		 Returns (possibly a copy of) the ith Coordinate in this collection.
		 Whether or not the Coordinate returned is the actual underlying
		 Coordinate or merely a copy depends on the implementation.
		 Note that in the future the semantics of this method may change
		 to guarantee that the Coordinate returned is always a copy. Callers are
		 advised not to assume that they can modify a CoordinateSequence by
		 modifying the Coordinate returned by this method.
		 
		 @param i
		 @return 
		*/
		Coordinate GetCoordinate(int i);

		/** 
		 Returns a copy of the i'th coordinate in this sequence.
		 This method optimizes the situation where the caller is
		 going to make a copy anyway - if the implementation
		 has already created a new Coordinate object, no further copy is needed.
		 
		 @param i The index of the coordinate to retrieve.
		 @return A copy of the i'th coordinate in the sequence
		*/
		Coordinate GetCoordinateCopy(int i);

		/** 
		 Copies the i'th coordinate in the sequence to the supplied Coordinate.  
		 At least the first two dimensions <b>must</b> be copied.        
		 
		 @param index The index of the coordinate to copy.
		 @param coord A Coordinate to receive the value.
		*/
		void GetCoordinate(int index, Coordinate coord);

		/** 
		 Returns ordinate X (0) of the specified coordinate.
		 
		 @param index
		 @return The value of the X ordinate in the index'th coordinate.
		*/
		double GetX(int index);

		/** 
		 Returns ordinate Y (1) of the specified coordinate.
		 
		 @param index
		 @return The value of the Y ordinate in the index'th coordinate.
		*/
		double GetY(int index);

		/** 
		 Returns the ordinate of a coordinate in this sequence.
		 Ordinate indices 0 and 1 are assumed to be X and Y.
		 Ordinate indices greater than 1 have user-defined semantics
		 (for instance, they may contain other dimensions or measure values).         
		 
		 
		 If the sequence does not provide value for the required ordinate, the implementation <b>must not</b> throw an exception, it should return <see cref="Coordinate.NullOrdinate"/>.
		 
		 @param index The coordinate index in the sequence.
		 @param ordinate The ordinate index in the coordinate (in range [0, dimension-1]).
		 @return The ordinate value, or <see cref="Coordinate.NullOrdinate"/> if the sequence does not provide values for <paramref name="ordinate"/>"/></returns>       
		*/
		double GetOrdinate(int index, Ordinate ordinate);

		/** 
		 Sets the value for a given ordinate of a coordinate in this sequence.       
		 
		 
		 If the sequence can't store the ordinate value, the implementation <b>must not</b> throw an exception, it should simply ignore the call.
		 
		 @param index The coordinate index in the sequence.
		 @param ordinate The ordinate index in the coordinate (in range [0, dimension-1]).
		 @param value The new ordinate value.       
		*/
		void SetOrdinate(int index, Ordinate ordinate, double value);

		/** 
		 Returns (possibly copies of) the Coordinates in this collection.
		 Whether or not the Coordinates returned are the actual underlying
		 Coordinates or merely copies depends on the implementation. Note that
		 if this implementation does not store its data as an array of Coordinates,
		 this method will incur a performance penalty because the array needs to
		 be built from scratch.
		 
		 @return 
		*/
		Coordinate[] ToCoordinateArray();

		/** 
		 Expands the given Envelope to include the coordinates in the sequence.
		 Allows implementing classes to optimize access to coordinate values.      
		 
		 @param env The envelope to expand.
		 @return A reference to the expanded envelope.       
		*/
		Envelope ExpandEnvelope(Envelope env);

		/** 
		 Creates a reversed version of this coordinate sequence with cloned <see cref="Coordinate"/>s
		 
		 @return A reversed version of this sequence
		*/
		ICoordinateSequence Reversed();

		/** 
		 Returns a deep copy of this collection.
		 
		 @return A copy of the coordinate sequence containing copies of all points
		*/
		ICoordinateSequence Copy();
	}