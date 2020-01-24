package lifeExpectancy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class LifeExpectancy extends PApplet {

	private static final long serialVersionUID = 1L;

	UnfoldingMap map;

	Map<String, Float> lifeExpectancyByCountry;
	List<Feature> countries = new ArrayList<Feature>();
	List<Marker> countryMarkers = new ArrayList<Marker>();
	
	private Map<String, Float> lifeExpectancyFromCSV(String fileName) {

		Map<String, Float> lifeExpectancyMap = new HashMap<String, Float>();
		String[] rows = loadStrings(fileName);

		for (String row : rows) {
			String[] columns = row.split(",");
			if (columns.length == 6 && !columns[5].equals("..")) {
				float value = Float.parseFloat(columns[5]);
				lifeExpectancyMap.put(columns[4], value);
			}
		} 

		return lifeExpectancyMap;
	}
	
	private void shadeCountries() {
		
		for (Marker marker : countryMarkers) {
			String countryId = marker.getId();
			
			if (lifeExpectancyByCountry.containsKey(countryId)) {
				float lifeExpectancy = lifeExpectancyByCountry.get(countryId);
				int colorLevel = (int) map(lifeExpectancy, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			} 
			else {
				marker.setColor(color(150,150,150));
			}
		}
	}

	public void setup() {
		size(1366, 768, OPENGL);
		
		lifeExpectancyByCountry = lifeExpectancyFromCSV("../data/LifeExpectancyWorldBankModule3.csv");

		map = new UnfoldingMap(this, 0, 0, 1366, 768, new Microsoft.HybridProvider());

		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		countries = GeoJSONReader.loadData(this, "../data/countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		map.addMarkers(countryMarkers);
		shadeCountries();

	};

	public void draw() {
		background(10);
		map.draw();
		addKey();
	}
	
	private void addKey() {
		// Remember you can use Processing's graphics methods here
	}

}
