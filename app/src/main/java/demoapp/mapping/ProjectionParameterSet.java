package demoapp.mapping;


import demoapp.mapping.yieldreturn.Generator;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 A set of projection parameters
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class ProjectionParameterSet extends HashMap<String, Double>
{
	private final HashMap<String, String> _originalNames = new HashMap<String, String>();
	private final HashMap<Integer, String> _originalIndex = new HashMap<Integer, String>();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if FEATURE_DESERIALIZATION_CONSTRUCTOR
	/** 
	 Needed for serialzation
	*/
//	public ProjectionParameterSet(system.Runtime.Serialization.SerializationInfo info, system.Runtime.Serialization.StreamingContext context)
//	{
//		super(info, context);
//	}
//#endif
	/** 
	 Creates an instance of this class
	 
	 @param parameters An enumeration of paramters
	*/
	public ProjectionParameterSet(Iterable<ProjectionParameter> parameters)
	{
		for (ProjectionParameter pp : parameters)
		{
			String key = pp.getName().toLowerCase(Locale.ROOT);
			_originalNames.put(key, pp.getName());
			_originalIndex.put(_originalIndex.size(), key);
			this.put(key, pp.getValue());
		}
	}

	/** 
	 Function to create an enumeration of <see cref="ProjectionParameter"/>s of the content of this projection parameter set.
	 
	 @return An enumeration of <see cref="ProjectionParameter"/>s
	*/
	static abstract class GeneratorTestBase<T> extends Generator<T> {

		// keep track of which items have been generated, for bookkeeping purposes.
		List<T> generatedItems = new CopyOnWriteArrayList<T>();
	}


	public final Iterable<ProjectionParameter> ToProjectionParameter()
	{
		return new GeneratorTestBase<ProjectionParameter>() {

			@Override
			protected void run() {
				for (Entry<Integer, String> oi : _originalIndex.entrySet())
				{
//C# TO JAVA CONVERTER TODO TASK: Java does not have an equivalent to the C# 'yield' keyword:
					//yield return new ProjectionParameter(_originalNames.get(oi.getValue()), this.get(oi.getValue()));
					ProjectionParameter item = new ProjectionParameter(_originalNames.get(oi.getValue()), ProjectionParameterSet.this.get(oi.getValue()));
					generatedItems.add(item);
					this.yield(item);
				}
			}
		};
	}


	public final double GetParameterValue(String parameterName, String... alternateNames)
	{
		String name = parameterName.toLowerCase(Locale.ROOT);
		if (!this.containsKey(name))
		{
			for (String alternateName : alternateNames)
			{
				double res;
				Double tempOut_res;
				tempOut_res = get(alternateName.toLowerCase(Locale.ROOT));
				if (tempOut_res != null)
				{
				res = tempOut_res;
					return res;
				}
			else
			{
				res = tempOut_res;
			}
			}

			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Missing projection parameter '%1$s'", parameterName));
			if (alternateNames.length > 0)
			{
				sb.append(String.format("\nIt is also not defined as '%1$s'", alternateNames[0]));
				for (int i = 1; i < alternateNames.length; i++)
				{
					sb.append(String.format(", '%1$s'", alternateNames[i]));
				}
				sb.append(".");
			}

			throw new IllegalArgumentException(sb.toString() + "parameterName");
		}
		return this.get(name);
	}

	/** 
	 Method to check if all mandatory projection parameters are passed
	*/
	public final double GetOptionalParameterValue(String name, double value, String... alternateNames)
	{
		name = name.toLowerCase(Locale.ROOT);
		if (!this.containsKey(name))
		{
			for (String alternateName : alternateNames)
			{
				double res;
				Double tempOut_res;
				tempOut_res = get(alternateName.toLowerCase(Locale.ROOT));
				if (tempOut_res != null)
				{
				res = tempOut_res;
					return res;
				}
			else
			{
				res = tempOut_res;
			}
			}
			//Add(name, value);
			return value;
		}
		return this.get(name);
	}

	/** 
	 Function to find a parameter based on its name
	 
	 @param name The name of the parameter
	 @return The parameter if present, otherwise null
	*/
	public final ProjectionParameter Find(String name)
	{
		name = name.toLowerCase(Locale.ROOT);
		return this.containsKey(name) ? new ProjectionParameter(_originalNames.get(name), this.get(name)) : null;
	}

	public final ProjectionParameter GetAtIndex(int index)
	{
		if (index < 0 || index >= this.size())
		{
			throw new IndexOutOfBoundsException("index");
		}
		String name = _originalIndex.get(index);
		return new ProjectionParameter(_originalNames.get(name), this.get(name));
	}



	public final boolean equals(ProjectionParameterSet other)
	{
		if (other == null)
		{
			return false;
		}

		if (other.size() != this.size())
		{
			return false;
		}

		for (Entry<String, Double> kvp : this.entrySet()) {

			if (!other.containsKey(kvp.getKey()))
			{
				return false;
			}

			double otherValue = other.GetParameterValue(kvp.getKey());
			if (otherValue != kvp.getValue())
			{
				return false;
			}
		}

		return true;
	}

	public final void SetParameterValue(String name, double value)
	{
		String key = name.toLowerCase(Locale.ROOT);
		if (!this.containsKey(key))
		{
			_originalIndex.put(_originalIndex.size(), key);
			_originalNames.put(key, name);
			this.put(key, value);
		}
		else
		{
			this.remove(key);
			this.put(key, value);
		}
	}
}