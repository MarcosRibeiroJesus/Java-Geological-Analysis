package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
//Parsing library
import parsing.ParseFeed;
//Processing library
import processing.core.PApplet;

/**
 * EarthquakeCityMap An application with an interactive map displaying
 * earthquake data. Author: UC San Diego Intermediate Software Development MOOC
 * team
 * 
 * @author Marcos Ribeiro: January 24, 2020
 */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this. It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	int red = color(255, 0, 0);
	int green = color(0, 255, 0);
	int blue = color(0, 0, 255);
	int white = color(255, 250, 150);
	int black = color(0, 0, 0);
	int yellow = color(255, 255, 0);
	int gray = color(150, 150, 150);

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;

	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// The map
	private UnfoldingMap map;

	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	// The List you will populate with new SimplePointMarkers
	List<Marker> markers = new ArrayList<Marker>();

	public void setup() {
		size(1366, 768, OPENGL);

		if (offline) {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom"; // Same feed, saved Aug 7, 2015, for working offline
		} else {
			map = new UnfoldingMap(this, 0, 0, 1366, 768, new Microsoft.HybridProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			// earthquakesURL = "2.5_week.atom";
		}

		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		// Use provided parser to collect properties for each earthquake
		// PointFeatures have a getLocation method
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

		// TODO (Step 3): Add a loop here that calls createMarker (see below)
		// to create a new SimplePointMarker for each PointFeature in
		// earthquakes. Then add each new SimplePointMarker to the
		// List markers (so that it will be added to the map in the line below)
		for (PointFeature earthquake : earthquakes) {
			createMarker(earthquake);
		}

		// Add the markers to the map so that they are displayed
		map.addMarkers(markers);
	}

	/*
	 * createMarker: A suggested helper method that takes in an earthquake feature
	 * and returns a SimplePointMarker for that earthquake
	 * 
	 * In step 3 You can use this method as-is. Call it from a loop in the setp
	 * method.
	 * 
	 * TODO (Step 4): Add code to this method so that it adds the proper styling to
	 * each marker based on the magnitude of the earthquake.
	 */
	private SimplePointMarker createMarker(PointFeature feature) {
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below. Note this will only print if you call createMarker
		// from setup
//		System.out.println(feature.getProperties());

		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());

		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());

		// TODO (Step 4): Add code below to style the marker's size and color
		// according to the magnitude of the earthquake.
		// Don't forget about the constants THRESHOLD_MODERATE and
		// THRESHOLD_LIGHT, which are declared above.
		// Rather than comparing the magnitude to a number directly, compare
		// the magnitude to these variables (and change their value in the code
		// above if you want to change what you mean by "moderate" and "light")
		if (mag <= THRESHOLD_LIGHT) {
			marker.setColor(blue);
			marker.setRadius(2 * mag * 1.75f);
		}
		if (mag > THRESHOLD_LIGHT && mag < THRESHOLD_MODERATE) {
			marker.setColor(yellow);
			marker.setRadius(2 * mag * 1.75f);
		}
		if (mag >= THRESHOLD_MODERATE) {
			marker.setColor(red);
			marker.setRadius(2 * mag * 1.95f);
		}

		this.markers.add(marker);

		// Finally return the marker
		return marker;
	}

	public void draw() {
		background(10);
		map.draw();
		addKey();
	}

	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() {
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		rect(25, 50, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, 75);
		
		fill(color(255, 0, 0));
		ellipse(50, 125, 15, 15);
		fill(color(255, 255, 0));
		ellipse(50, 175, 10, 10);
		fill(color(0, 0, 255));
		ellipse(50, 225, 5, 5);
		
		fill(0, 0, 0);
		text("5.0+ Magnitude", 75, 125);
		text("4.0+ Magnitude", 75, 175);
		text("Below 4.0", 75, 225);
		
//		fill(255, 250, 250);
//		// x,y 
//		rect(25, 50, 400, 100);
//		
//		fill(0);
//		textAlign(LEFT, CENTER);
//		textSize(12);
//		text("Earthquake Key", 170, 75);
//		
//		fill(color(0, 0, 255));
//		ellipse(50, 125, 5, 5);
//		fill(color(255, 255, 0));
//		ellipse(150, 125, 10, 10);
//		fill(color(255, 0, 0));
//		ellipse(290, 125, 15, 15);
//		
//		fill(0, 0, 255);
//		text("Below 4.0", 70, 120);
//		fill(0, 0, 0);
//		text("4.0+ Magnitude", 170, 120);
//		fill(color(255, 0, 0));
//		text("5.0+ Magnitude", 310, 120);

	}
}
