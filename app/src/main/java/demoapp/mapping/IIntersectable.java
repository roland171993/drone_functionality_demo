package demoapp.mapping;

/**
	 Interface describing objects that can perform an intersects predicate with <typeparamref name="T"/> objects.
	 
	 <typeparam name="T">The type of the component that can intersect</typeparam>
	*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if FEATURE_INTERFACE_VARIANCE
//C# TO JAVA CONVERTER TODO TASK: Java does not allow specifying covariance or contravariance in a generic type list:
////ORIGINAL LINE: public interface IIntersectable<in T>
//	public interface IIntersectable<T>
////#else
//	public interface IIntersectable<T>
////#endif
//ORIGINAL LINE: public interface IIntersectable<in T>
	public interface IIntersectable<T>
	{
		/** 
		 Predicate function to test if <paramref name="other"/> intersects with this object.
		 
		 @param other The object to test
		 @return <value>true</value> if this objects intersects with <paramref name="other"/>
		*/
		boolean Intersects(T other);
	}