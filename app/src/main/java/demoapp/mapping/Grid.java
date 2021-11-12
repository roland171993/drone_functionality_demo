package demoapp.mapping;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class Grid
{
	private static String TAG = Grid.class.getSimpleName();
	private static final double rad2deg = (180 / Math.PI);
	private static final double deg2rad = (1.0 / rad2deg);

//C# TO JAVA CONVERTER WARNING: Java does not allow user-defined value types. The behavior of this class may differ from the original:
//ORIGINAL LINE: public struct linelatlng
	public final static class linelatlng
	{
		// start of line
		public utmpos p1 = new utmpos();
		// end of line
		public utmpos p2 = new utmpos();
		// used as a base for grid along line (initial setout)
		public utmpos basepnt = new utmpos();

		public linelatlng clone()
		{
			linelatlng varCopy = new linelatlng();

			varCopy.p1 = this.p1;
			varCopy.p2 = this.p2;
			varCopy.basepnt = this.basepnt;

			return varCopy;
		}
	}

	public enum StartPosition
	{
		Home(0),
		BottomLeft(1),
		TopLeft(2),
		BottomRight(3),
		TopRight(4),
		Point(5);

		public static final int SIZE = Integer.SIZE;

		private int intValue;
		private static HashMap<Integer, StartPosition> mappings;
		private static HashMap<Integer, StartPosition> getMappings()
		{
			if (mappings == null)
			{
				synchronized (StartPosition.class)
				{
					if (mappings == null)
					{
						mappings = new HashMap<Integer, StartPosition>();
					}
				}
			}
			return mappings;
		}

		private StartPosition(int value)
		{
			intValue = value;
			getMappings().put(value, this);
		}

		public int getValue()
		{
			return intValue;
		}

		public static StartPosition forValue(int value)
		{
			return getMappings().get(value);
		}
	}

	public static PointLatLngAlt StartPointLatLngAlt = PointLatLngAlt.Zero.Clone();


//C# TO JAVA CONVERTER TODO TASK: There is no equivalent in Java to the 'async' keyword:
//ORIGINAL LINE: public static async Task<List<PointLatLngAlt>> CreateGridAsync(List<PointLatLngAlt> polygon, double altitude, double distance, double spacing, double angle, double overshoot1, double overshoot2, StartPosition startpos, bool shutter, float minLaneSeparation, float leadin1,float leadin2, PointLatLngAlt HomeLocation, bool useextendedendpoint = true)
//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
	public static CopyOnWriteArrayList<PointLatLngAlt> CreateGridAsync(CopyOnWriteArrayList<PointLatLngAlt> polygon, double altitude, double distance, double spacing, double angle, double overshoot1, double overshoot2, StartPosition startpos, boolean shutter, float minLaneSeparation, float leadin1, float leadin2, PointLatLngAlt HomeLocation, boolean useextendedendpoint, final Mapping.OnComputingComplete callBack)
	{
		return CreateGrid(polygon, altitude, distance, spacing, angle, overshoot1, overshoot2, startpos, shutter, minLaneSeparation, leadin1, leadin2, HomeLocation, useextendedendpoint,callBack);
	}


	public static CopyOnWriteArrayList<PointLatLngAlt> CreateGrid(CopyOnWriteArrayList<PointLatLngAlt> polygon, double altitude, double distance, double spacing, double angle, double overshoot1, double overshoot2, StartPosition startpos, boolean shutter, float minLaneSeparation, float leadin1, float leadin2, PointLatLngAlt HomeLocation, final Mapping.OnComputingComplete callBack)
	{
		return CreateGrid(polygon, altitude, distance, spacing, angle, overshoot1, overshoot2, startpos, shutter, minLaneSeparation, leadin1, leadin2, HomeLocation, true,callBack);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static List<PointLatLngAlt> CreateGrid(List<PointLatLngAlt> polygon, double altitude, double distance, double spacing, double angle, double overshoot1, double overshoot2, StartPosition startpos, bool shutter, float minLaneSeparation, float leadin1, float leadin2, PointLatLngAlt HomeLocation, bool useextendedendpoint = true)
	public static CopyOnWriteArrayList<PointLatLngAlt> CreateGrid(CopyOnWriteArrayList<PointLatLngAlt> polygon, double altitude, double distance, double spacing, double angle, double overshoot1, double overshoot2, StartPosition startpos, boolean shutter, float minLaneSeparation, float leadin1, float leadin2, PointLatLngAlt HomeLocation, boolean useextendedendpoint, final Mapping.OnComputingComplete callBack)
	{
		final int STEP_ONE = 10, STEP_2 = 20, STEP_3 =30,
				STEP_4 = 40, STEP_5 = 50, STEP_6= 60,
				STEP_7= 70, STEP_8 = 80, STEP_9 = 90,
				STEP_10= 100;
		try {
			// GOOD WORK GREATE N°1 START
			if (Utils.isNull(callBack)){
				return null;
			}else{
				if (spacing < 0.1 && spacing != 0)
				{
					spacing = 0.1;
				}

				if (distance < 0.1)
				{
					distance = 0.1;
				}

				if (polygon.isEmpty())
				{
					return new CopyOnWriteArrayList<PointLatLngAlt>();
				}
				callBack.OnNotifyState(STEP_ONE);


				// Make a non round number in case of corner cases
				if (minLaneSeparation != 0)
				{
					minLaneSeparation += 0.5F;
				}
				// Lane Separation in meters
				double minLaneSeparationINMeters = minLaneSeparation * distance;

				CopyOnWriteArrayList<PointLatLngAlt> ans = new CopyOnWriteArrayList<PointLatLngAlt>();

				// utm zone distance calcs will be done in
				int utmzone = polygon.get(0).GetUTMZone();

				// utm position list
				CopyOnWriteArrayList<utmpos> utmpositions = utmpos.ToList(PointLatLngAlt.ToUTM(utmzone, polygon), utmzone);

				// close the loop if its not already
				if (!utmpositions.get(0).equals(utmpositions.get(utmpositions.size() - 1)))
				{
					utmpositions.add(utmpositions.get(0)); // make a full loop
				}

				callBack.OnNotifyState(STEP_3);

				// get mins/maxs of coverage area
				Rect area = getPolyMinMax(utmpositions);

				// get initial grid

				// used to determine the size of the outer grid area
				double diagdist = area.DiagDistance();

				// somewhere to store out generated lines
				CopyOnWriteArrayList<linelatlng> grid = new CopyOnWriteArrayList<linelatlng>();
				// number of lines we need
				int lines = 0;

				// get start point middle
				double x = area.getMidWidth();
				double y = area.getMidHeight();

				// GOOD WORK GREATE N°1 END

				// GOOD WORK GREATE N°2 START

				addtomap(new utmpos(x, y, utmzone), "Base");

				// get left extent
				double xb1 = x;
				double yb1 = y;
				// to the left
				RefObject<Double> tempRef_xb1 = new RefObject<Double>(xb1);
				RefObject<Double> tempRef_yb1 = new RefObject<Double>(yb1);
				newpos(tempRef_xb1, tempRef_yb1, angle - 90, diagdist / 2 + distance);
				yb1 = tempRef_yb1.argValue;
				xb1 = tempRef_xb1.argValue;
				// backwards
				RefObject<Double> tempRef_xb12 = new RefObject<Double>(xb1);
				RefObject<Double> tempRef_yb12 = new RefObject<Double>(yb1);
				newpos(tempRef_xb12, tempRef_yb12, angle + 180, diagdist / 2 + distance);
				yb1 = tempRef_yb12.argValue;
				xb1 = tempRef_xb12.argValue;

				utmpos left = new utmpos(xb1, yb1, utmzone);

				addtomap(left, "left");

				callBack.OnNotifyState(STEP_4);

				// get right extent
				double xb2 = x;
				double yb2 = y;
				// to the right
				RefObject<Double> tempRef_xb2 = new RefObject<Double>(xb2);
				RefObject<Double> tempRef_yb2 = new RefObject<Double>(yb2);
				newpos(tempRef_xb2, tempRef_yb2, angle + 90, diagdist / 2 + distance);
				yb2 = tempRef_yb2.argValue;
				xb2 = tempRef_xb2.argValue;
				// backwards
				RefObject<Double> tempRef_xb22 = new RefObject<Double>(xb2);
				RefObject<Double> tempRef_yb22 = new RefObject<Double>(yb2);
				newpos(tempRef_xb22, tempRef_yb22, angle + 180, diagdist / 2 + distance);
				yb2 = tempRef_yb22.argValue;
				xb2 = tempRef_xb22.argValue;

				utmpos right = new utmpos(xb2, yb2, utmzone);

				addtomap(right, "right");

				// set start point to left hand side
				x = xb1;
				y = yb1;

				callBack.OnNotifyState(STEP_5);

				// draw the outergrid, this is a grid that cover the entire area of the rectangle plus more.
				while (lines < ((diagdist + distance * 2) / distance))
				{
					// copy the start point to generate the end point
					double nx = x;
					double ny = y;
					RefObject<Double> tempRef_nx = new RefObject<Double>(nx);
					RefObject<Double> tempRef_ny = new RefObject<Double>(ny);
					newpos(tempRef_nx, tempRef_ny, angle, diagdist + distance * 2);
					ny = tempRef_ny.argValue;
					nx = tempRef_nx.argValue;

					linelatlng line = new linelatlng();
					line.p1 = new utmpos(x, y, utmzone);
					line.p2 = new utmpos(nx, ny, utmzone);
					line.basepnt = new utmpos(x, y, utmzone);
					grid.add(line);

					// addtomap(line);

					RefObject<Double> tempRef_x = new RefObject<Double>(x);
					RefObject<Double> tempRef_y = new RefObject<Double>(y);
					newpos(tempRef_x, tempRef_y, angle + 90, distance);
					y = tempRef_y.argValue;
					x = tempRef_x.argValue;
					lines++;
				}
				callBack.OnNotifyState(STEP_6);

				// find intersections with our polygon

				// store lines that dont have any intersections
				CopyOnWriteArrayList<linelatlng> remove = new CopyOnWriteArrayList<linelatlng>();

				// GOOD WORK GREATE N°2 END

				int gridno = grid.size();

				// DEBUG END
				// GOOD WORK GREATE N°3 START
				// cycle through our grid
				for (int a = 0; a < gridno; a++)
				{
					double closestdistance = Double.MAX_VALUE;
					double farestdistance = -Double.MAX_VALUE;

					utmpos closestpoint = utmpos.Zero.clone();
					utmpos farestpoint = utmpos.Zero.clone();

					// somewhere to store our intersections
					CopyOnWriteArrayList<utmpos> matchs = new CopyOnWriteArrayList<utmpos>();

					int b = -1;
					int crosses = 0;
					utmpos newutmpos = utmpos.Zero.clone();

					// GOOD WORK GREATE N°3 END utmpositions match With cSharpContent
					for (utmpos pnt : utmpositions)
					{
						b++;
						if (b == 0)
						{
							continue;
						}
						newutmpos = FindLineIntersection(utmpositions.get(b - 1), utmpositions.get(b), grid.get(a).p1, grid.get(a).p2);
						// COUPALE 1
						if (!newutmpos.getIsZero())
						{
							crosses++;
							matchs.add(newutmpos);
							if (closestdistance > grid.get(a).p1.GetDistance(newutmpos))
							{
								closestpoint.y = newutmpos.y;
								closestpoint.x = newutmpos.x;
								closestpoint.zone = newutmpos.zone;
								closestdistance = grid.get(a).p1.GetDistance(newutmpos);
							}
							if (farestdistance < grid.get(a).p1.GetDistance(newutmpos))
							{
								farestpoint.y = newutmpos.y;
								farestpoint.x = newutmpos.x;
								farestpoint.zone = newutmpos.zone;
								farestdistance = grid.get(a).p1.GetDistance(newutmpos);
							}
						}
					}
					if (crosses == 0) // outside our polygon
					{
						if (!PointInPolygon(grid.get(a).p1, utmpositions) && !PointInPolygon(grid.get(a).p2, utmpositions))
						{
							remove.add(grid.get(a));
						}
					}
					else if (crosses == 1) // bad - shouldnt happen
					{

					}
					else if (crosses == 2) // simple start and finish
					{
						// FOUND START WHEN a=4
						linelatlng line = grid.get(a);
						line.p1 = closestpoint;
						line.p2 = farestpoint;
						grid.set(a, line);
						// FOUND START WHEN a=4 END
					}
					else // multiple intersections
					{
						linelatlng line = grid.get(a);
						remove.add(line);

						while (matchs.size() > 1)
						{
							linelatlng newline = new linelatlng();

							closestpoint = findClosestPoint(closestpoint, matchs);
							newline.p1 = closestpoint;
							matchs.remove(closestpoint);

							closestpoint = findClosestPoint(closestpoint, matchs);
							newline.p2 = closestpoint;
							matchs.remove(closestpoint);

							newline.basepnt = line.basepnt;

							grid.add(newline);
						}
					}
				}

				// GOOD WORK GREATE N°4 END
				// cleanup and keep only lines that pass though our polygon
				for (linelatlng line : remove)
				{
					grid.remove(line);
				}
				callBack.OnNotifyState(STEP_7);

				// debug
				for (linelatlng line : grid)
				{
					addtomap(line);
				}

				if (grid.isEmpty())
				{
					return ans;
				}

				// pick start positon based on initial point rectangle
				utmpos startposutm = new utmpos();

				switch (startpos)
				{
					default:
					case Home:
						startposutm = new utmpos(HomeLocation);
						break;
					case BottomLeft:
						startposutm = new utmpos(area.Left, area.Bottom, utmzone);
						break;
					case BottomRight:
						startposutm = new utmpos(area.Right, area.Bottom, utmzone);
						break;
					case TopLeft:
						startposutm = new utmpos(area.Left, area.Top, utmzone);
						break;
					case TopRight:
						startposutm = new utmpos(area.Right, area.Top, utmzone);
						break;
					case Point:
						startposutm = new utmpos(StartPointLatLngAlt);
						break;
				}

				// find the closes polygon point based from our startpos selection
				startposutm = findClosestPoint(startposutm, utmpositions);

				// find closest line point to startpos
				linelatlng closest = findClosestLine(startposutm, grid, 0, angle);

				utmpos lastpnt = new utmpos();

				// get the closes point from the line we picked
				if (closest.p1.GetDistance(startposutm) < closest.p2.GetDistance(startposutm))
				{
					lastpnt = closest.p1;
				}
				else
				{
					lastpnt = closest.p2;
				}

				// S =  start
				// E = end
				// ME = middle end
				// SM = start middle
				callBack.OnNotifyState(STEP_9);

				while (!grid.isEmpty())
				{
					// for each line, check which end of the line is the next closest
					if (closest.p1.GetDistance(lastpnt) < closest.p2.GetDistance(lastpnt))
					{
						utmpos newstart = newpos(closest.p1, angle, -leadin1);
						newstart.Tag = "S";
						addtomap(newstart, "S");
						ans.add(newstart.toPointLatLngAlt());

						if (leadin1 < 0)
						{
							utmpos p2 = new utmpos(newstart);
							p2.Tag = "SM";
							addtomap(p2, "SM");
							ans.add(p2.toPointLatLngAlt());
						}
						else
						{
							closest.p1.Tag = "SM";
							addtomap(closest.p1, "SM");
							ans.add(closest.p1.toPointLatLngAlt());
						}

						if (spacing > 0)
						{
							for (double d = (spacing - ((closest.basepnt.GetDistance(closest.p1)) % spacing)); d < (closest.p1.GetDistance(closest.p2)); d += spacing)
							{
								double ax = closest.p1.x;
								double ay = closest.p1.y;

								RefObject<Double> tempRef_ax = new RefObject<Double>(ax);
								RefObject<Double> tempRef_ay = new RefObject<Double>(ay);
								newpos(tempRef_ax, tempRef_ay, angle, d);
								ay = tempRef_ay.argValue;
								ax = tempRef_ax.argValue;
								utmpos utmpos1 = new utmpos(ax, ay, utmzone);
								utmpos1.Tag = "M";
								addtomap(utmpos1, "M");
								ans.add(utmpos1.toPointLatLngAlt());
							}
						}

						utmpos newend = newpos(closest.p2, angle, overshoot1);

						if (overshoot1 < 0)
						{
							utmpos p2 = new utmpos(newend);
							p2.Tag = "ME";
							addtomap(p2, "ME");
							ans.add(p2.toPointLatLngAlt());
						}
						else
						{
							closest.p2.Tag = "ME";
							addtomap(closest.p2, "ME");
							ans.add(closest.p2.toPointLatLngAlt());
						}

						newend.Tag = "E";
						addtomap(newend, "E");
						ans.add(newend.toPointLatLngAlt());

						lastpnt = closest.p2;

						grid.remove(closest);
						if (grid.isEmpty())
						{
							break;
						}

						if (useextendedendpoint)
						{
							closest = findClosestLine(newend, grid, minLaneSeparationINMeters, angle);
						}
						else
						{
							closest = findClosestLine(closest.p2, grid, minLaneSeparationINMeters, angle);
						}
					}
					else
					{
						utmpos newstart = newpos(closest.p2, angle, leadin2);
						newstart.Tag = "S";
						addtomap(newstart, "S");
						ans.add(newstart.toPointLatLngAlt());

						if (leadin2 < 0)
						{
							utmpos p2 = new utmpos(newstart);
							p2.Tag = "SM";
							addtomap(p2, "SM");
							ans.add(p2.toPointLatLngAlt());
						}
						else
						{
							closest.p2.Tag = "SM";
							addtomap(closest.p2, "SM");
							ans.add(closest.p2.toPointLatLngAlt());
						}

						if (spacing > 0)
						{
							for (double d = ((closest.basepnt.GetDistance(closest.p2)) % spacing); d < (closest.p1.GetDistance(closest.p2)); d += spacing)
							{
								double ax = closest.p2.x;
								double ay = closest.p2.y;

								RefObject<Double> tempRef_ax2 = new RefObject<Double>(ax);
								RefObject<Double> tempRef_ay2 = new RefObject<Double>(ay);
								newpos(tempRef_ax2, tempRef_ay2, angle, -d);
								ay = tempRef_ay2.argValue;
								ax = tempRef_ax2.argValue;
								utmpos utmpos2 = new utmpos(ax, ay, utmzone);
								utmpos2.Tag = "M";
								addtomap(utmpos2, "M");
								ans.add(utmpos2.toPointLatLngAlt());
							}
						}

						utmpos newend = newpos(closest.p1, angle, -overshoot2);

						if (overshoot2 < 0)
						{
							utmpos p2 = new utmpos(newend);
							p2.Tag = "ME";
							addtomap(p2, "ME");
							ans.add(p2.toPointLatLngAlt());
						}
						else
						{
							closest.p1.Tag = "ME";
							addtomap(closest.p1, "ME");
							ans.add(closest.p1.toPointLatLngAlt());
						}

						newend.Tag = "E";
						addtomap(newend, "E");
						ans.add(newend.toPointLatLngAlt());

						lastpnt = closest.p1;

						grid.remove(closest);
						if (grid.isEmpty())
						{
							break;
						}

						if (useextendedendpoint)
						{
							closest = findClosestLine(newend, grid, minLaneSeparationINMeters, angle);
						}
						else
						{
							closest = findClosestLine(closest.p1, grid, minLaneSeparationINMeters, angle);
						}
					}
				}
				callBack.OnNotifyState(STEP_10);

				// set the altitude on all points
				for (PointLatLngAlt plla: ans) {
					plla.setAlt(altitude);
				}

				return ans;
			}
		}catch (Exception e){
			Utils.logContent(TAG, "CreateGrid",e,null);
		}catch (Error er){
			Utils.logContent(TAG, "CreateGrid",null,er);
		}
		return null;
	}


	public static int LIMIT11 = 11;

	public static String getValue(JSONObject basepntJSON, String mX, String mY)throws Exception,Error {
		String valX = basepntJSON.getDouble(mX)+"";
		if (valX.length() > LIMIT11){
			valX = valX.substring(0, LIMIT11);
		}
		String valY = basepntJSON.getDouble(mY)+"";
		if (valY.length() > LIMIT11){
			valY = valY.substring(0, LIMIT11);
		}
		String res = valX+","+valY;
		return res;

	}

	public static utmpos FindLineIntersection(utmpos start1, utmpos end1, utmpos start2, utmpos end2)throws Exception,Error
	{
		double denom = ((end1.x - start1.x) * (end2.y - start2.y)) - ((end1.y - start1.y) * (end2.x - start2.x));
		//  AB & CD are parallel         
		if (denom == 0)
		{
			return utmpos.Zero.clone();
		}
		double numer = ((start1.y - start2.y) * (end2.x - start2.x)) - ((start1.x - start2.x) * (end2.y - start2.y));
		double r = numer / denom;
		double numer2 = ((start1.y - start2.y) * (end1.x - start1.x)) - ((start1.x - start2.x) * (end1.y - start1.y));
		double s = numer2 / denom;
		if ((r < 0 || r > 1) || (s < 0 || s > 1))
		{
			return utmpos.Zero.clone();
		}
		// Find intersection point      
		utmpos result = new utmpos();
		result.x = start1.x + (r * (end1.x - start1.x));
		result.y = start1.y + (r * (end1.y - start1.y));
		result.zone = start1.zone;
		return result;
	}

	private static void addtomap(linelatlng pos)
	{

	}

	private static void addtomap(utmpos pos, String tag)
	{

	}

	private static linelatlng findClosestLine(utmpos start, CopyOnWriteArrayList<linelatlng> list, double minDistance, double angle)throws Exception,Error
	{
		if (minDistance == 0)
		{
			linelatlng answer = list.get(0);
			double shortest = Double.MAX_VALUE;

			for (linelatlng line : list)
			{
				double ans1 = start.GetDistance(line.p1);
				double ans2 = start.GetDistance(line.p2);
				utmpos shorterpnt = ans1 < ans2 ? line.p1 : line.p2;

				if (shortest > start.GetDistance(shorterpnt))
				{
					answer = line;
					shortest = start.GetDistance(shorterpnt);
				}
			}

			return answer;
		}


		// By now, just add 5.000 km to our lines so they are long enough to allow intersection
		double METERS_TO_EXTEND = 5000;

		double perperndicularOrientation = AddAngle(angle, 90);

		// Calculation of a perpendicular line to the grid lines containing the "start" point
		/*
		 *  --------------------------------------|------------------------------------------
		 *  --------------------------------------|------------------------------------------
		 *  -------------------------------------start---------------------------------------
		 *  --------------------------------------|------------------------------------------
		 *  --------------------------------------|------------------------------------------
		 *  --------------------------------------|------------------------------------------
		 *  --------------------------------------|------------------------------------------
		 *  --------------------------------------|------------------------------------------
		 */
		utmpos start_perpendicular_line = newpos(start, perperndicularOrientation, -METERS_TO_EXTEND);
		utmpos stop_perpendicular_line = newpos(start, perperndicularOrientation, METERS_TO_EXTEND);

		// Store one intersection point per grid line
		HashMap<utmpos, linelatlng> intersectedPoints = new HashMap<utmpos, linelatlng>();
		// lets order distances from every intersected point per line with the "start" point
		HashMap<Double, utmpos> ordered_min_to_max = new HashMap<Double, utmpos>();

		for (linelatlng line : list)
		{
			// Calculate intersection point
			utmpos p = FindLineIntersectionExtension(line.p1, line.p2, start_perpendicular_line, stop_perpendicular_line);

			// Store it
			intersectedPoints.put(p, line);

			// Calculate distances between interesected point and "start" (i.e. line and start)
			double distance_p = start.GetDistance(p);

			if (!ordered_min_to_max.containsKey(distance_p))
			{
				ordered_min_to_max.put(distance_p, p);
			}
		}

		// Acquire keys and sort them.
		CopyOnWriteArrayList<Double> ordered_keys_base = new CopyOnWriteArrayList<>();
		// ArrayList<Double> ordered_keys = ordered_min_to_max.keySet().ToList();
		for(Map.Entry<Double, utmpos> entry : ordered_min_to_max.entrySet()) {
			Double key = entry.getKey();
			ordered_keys_base.add(key);
		}

		List<Double> ordered_keys = ordered_keys_base.stream().sorted().collect(Collectors.toList());


//C# TO JAVA CONVERTER TODO TASK: This version of the List.Sort method is not converted to Java:

		// Lets select a line that is the closest to "start" point but "mindistance" away at least.
		// If we have only one line, return that line whatever the minDistance says
		double key = Double.MAX_VALUE;
		int i = 0;
		while (key == Double.MAX_VALUE && i < ordered_keys.size())
		{
			if (ordered_keys.get(i).compareTo(minDistance) >= 0)
			{
				key = ordered_keys.get(i);
			}
			i++;
		}

		// If no line is selected (because all of them are closer than minDistance, then get the farest one
		if (key == Double.MAX_VALUE)
		{
			key = ordered_keys.get(ordered_keys.size() - 1);
		}

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		// var filteredlist = intersectedPoints.Where(a -> a.Key.GetDistance(start.clone()) >= key);
		CopyOnWriteArrayList<linelatlng> filteredlist = new CopyOnWriteArrayList<>();

		double finalKey = key;
		List<Map.Entry<utmpos,linelatlng>> mapEntry = intersectedPoints.entrySet()
				.stream()
				.filter(a -> a.getKey().GetDistance(start) >= finalKey)
				.collect(Collectors.toList());
		mapEntry.stream().forEach(e -> {
			filteredlist.add(e.getValue());
		});

		return findClosestLine(start, filteredlist, 0, angle);
	}

	private static Rect getPolyMinMax(CopyOnWriteArrayList<utmpos> utmpos)throws Exception,Error
	{
		if (utmpos.isEmpty())
		{
			return new Rect();
		}

		double minx, miny, maxx, maxy;

		minx = maxx = utmpos.get(0).x;
		miny = maxy = utmpos.get(0).y;

		for (utmpos pnt : utmpos)
		{
			minx = Math.min(minx, pnt.x);
			maxx = Math.max(maxx, pnt.x);

			miny = Math.min(miny, pnt.y);
			maxy = Math.max(maxy, pnt.y);
		}

		return new Rect(minx, maxy, maxx - minx, miny - maxy);
	}

	// polar to rectangular
	private static void newpos(RefObject<Double> x,RefObject<Double> y, double bearing, double distance)throws Exception,Error
	{
		double degN = 90 - bearing;
		if (degN < 0)
		{
			degN += 360;
		}
		x.argValue = x.argValue + distance * Math.cos(degN * deg2rad);
		y.argValue = y.argValue + distance * Math.sin(degN * deg2rad);
	}

	// polar to rectangular
	private static utmpos newpos(utmpos input, double bearing, double distance)throws Exception,Error
	{
		double degN = 90 - bearing;
		if (degN < 0)
		{
			degN += 360;
		}
		double x = input.x + distance * Math.cos(degN * deg2rad);
		double y = input.y + distance * Math.sin(degN * deg2rad);

		return new utmpos(x, y, input.zone);
	}

	private static boolean PointInPolygon(utmpos p, CopyOnWriteArrayList<utmpos> poly)throws Exception,Error
	{
		utmpos p1, p2 = new utmpos();
		boolean inside = false;

		if (poly.size() < 3)
		{
			return inside;
		}
		utmpos oldPoint = new utmpos(poly.get(poly.size() - 1));

		for (int i = 0; i < poly.size(); i++)
		{

			utmpos newPoint = new utmpos(poly.get(i));

			if (newPoint.y > oldPoint.y)
			{
				p1 = oldPoint;
				p2 = newPoint;
			}
			else
			{
				p1 = newPoint;
				p2 = oldPoint;
			}

			if ((newPoint.y < p.y) == (p.y <= oldPoint.y) && ((double)p.x - (double)p1.x) * (double)(p2.y - p1.y) < ((double)p2.x - (double)p1.x) * (double)(p.y - p1.y))
			{
				inside = !inside;
			}
			oldPoint = newPoint;
		}
		return inside;
	}

	public static utmpos FindLineIntersectionExtension(utmpos start1, utmpos end1, utmpos start2, utmpos end2)throws Exception,Error
	{
		double denom = ((end1.x - start1.x) * (end2.y - start2.y)) - ((end1.y - start1.y) * (end2.x - start2.x));
		//  AB & CD are parallel         
		if (denom == 0)
		{
			return utmpos.Zero.clone();
		}
		double numer = ((start1.y - start2.y) * (end2.x - start2.x)) - ((start1.x - start2.x) * (end2.y - start2.y));
		double r = numer / denom;
		double numer2 = ((start1.y - start2.y) * (end1.x - start1.x)) - ((start1.x - start2.x) * (end1.y - start1.y));
		double s = numer2 / denom;
		if ((r < 0 || r > 1) || (s < 0 || s > 1))
		{
			// line intersection is outside our lines.
		}
		// Find intersection point      
		utmpos result = new utmpos();
		result.x = start1.x + (r * (end1.x - start1.x));
		result.y = start1.y + (r * (end1.y - start1.y));
		result.zone = start1.zone;
		return result;
	}

	// Add an angle while normalizing output in the range 0...360
	private static double AddAngle(double angle, double degrees)throws Exception,Error
	{
		angle += degrees;

		angle = angle % 360;

		while (angle < 0)
		{
			angle += 360;
		}
		return angle;
	}

	private static utmpos findClosestPoint(utmpos start, CopyOnWriteArrayList<utmpos> list)throws Exception,Error
	{
		utmpos answer = utmpos.Zero.clone();
		double currentbest = Double.MAX_VALUE;

		for (utmpos pnt : list)
		{
			double dist1 = start.GetDistance(pnt);

			if (dist1 < currentbest)
			{
				answer = pnt;
				currentbest = dist1;
			}
		}

		return answer;
	}

}