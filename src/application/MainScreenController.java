package application;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainScreenController {
	@FXML
	private TextField t1;
	@FXML
	private Button btn1;
	@FXML
	private Label l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15, l16, l17, l18, l19;
	
	// Event Listener on TextField[#t1].onKeyPressed
	@FXML
	public void OnEnterKeyPressed(ActionEvent event) {
		ComputeValues(); 
	}
	// Event Listener on Button[#btn1].onAction
	@FXML
	public void SetValues(ActionEvent event) {
		 ComputeValues();
	}
	public void ComputeValues(){
		String city = t1.getText();
		t1.clear();
		try {
	      	String LocationKey = "";
	  		var cityApi = "http://dataservice.accuweather.com/locations/v1/search?apikey=5oW9IO9aAIxXo8aZGQiBGpxCILb35AE8&q=" + city;
	  		var request = HttpRequest.newBuilder().GET().uri(URI.create(cityApi)).build();
	  		var client = HttpClient.newBuilder().build();
	  		var response = client.send(request, HttpResponse.BodyHandlers.ofString());
	
	  		try {
	  	        JSONArray jsonArray = new JSONArray(response.body());
	          	JSONObject resObj = jsonArray.getJSONObject(0);
	          	LocationKey = resObj.getString("Key");
	          	
	        	String localName = resObj.getString("LocalizedName");
	          	JSONObject region = resObj.getJSONObject("Region");
	          	String regionName = region.getString("LocalizedName");
	          	l1.setText(regionName + "/ " + localName);
	          	
	          	JSONObject geoPos = resObj.getJSONObject("GeoPosition");
	          	BigDecimal lat = geoPos.getBigDecimal("Latitude");
	          	BigDecimal lon = geoPos.getBigDecimal("Longitude");
	          	l2.setText(lat + " Lat/ " + lon + " Lon");
	  		}catch (JSONException e) {
	  			System.out.println(e);
	  		}
  	
	  		var weatherApi = "http://dataservice.accuweather.com/currentconditions/v1/"+LocationKey+"?apikey=5oW9IO9aAIxXo8aZGQiBGpxCILb35AE8&details=true";
	  		var req = HttpRequest.newBuilder().GET().uri(URI.create(weatherApi)).build();
	  		var cli = HttpClient.newBuilder().build();
	  		var resp = cli.send(req, HttpResponse.BodyHandlers.ofString());

	  		try {
		  		  JSONArray jsonArr = new JSONArray(resp.body());
		          JSONObject resultsObj = jsonArr.getJSONObject(0);
		
		          String date = resultsObj.getString("LocalObservationDateTime");
		          String[] dt = date.split("T");
		          String[] d = dt[0].split("-");
		          String[] time = dt[1].split(":");
		          
		          LocalDate localDate = LocalDate.of(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
		          java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		          java.time.Month month = localDate.getMonth();
		          String day = dayOfWeek.toString().toLowerCase();
		          String mon = month.toString().toLowerCase();
		          
		          l3.setText(Integer.parseInt(time[0])%12 + " : " + time[1]);
		          l4.setText(((Integer.parseInt(time[0]) >= 12) ? "PM" : "AM"));
		          l5.setText(day.substring(0, 1).toUpperCase() + day.substring(1) + ", " + d[2] + " " + mon.substring(0, 1).toUpperCase() + mon.substring(1));
		          
		          JSONObject temperatureObj = resultsObj.getJSONObject("Temperature");
		          BigDecimal temp = temperatureObj.getJSONObject("Metric").getBigDecimal("Value");
		          l6.setText(temp + " °C");
		          
		          JSONObject temperatureSummObj = resultsObj.getJSONObject("TemperatureSummary");
	              BigDecimal minTemperature = temperatureSummObj.getJSONObject("Past24HourRange").getJSONObject("Minimum").getJSONObject("Metric").getBigDecimal("Value");
	              BigDecimal maxTemperature = temperatureSummObj.getJSONObject("Past24HourRange").getJSONObject("Maximum").getJSONObject("Metric").getBigDecimal("Value");
	              l7.setText(minTemperature + " ~ " + maxTemperature + " °C");
	              
		          JSONObject temperatureFeltObj = resultsObj.getJSONObject("RealFeelTemperature");
	              BigDecimal tempFelt = temperatureFeltObj.getJSONObject("Metric").getBigDecimal("Value");
	              l8.setOpacity(1);
	              l9.setText(tempFelt + " °C");
	              
	              BigDecimal humidity = resultsObj.getBigDecimal("RelativeHumidity");
	              l10.setOpacity(1);
	              l11.setText(humidity + " %");
	              
	              JSONObject visibilityObj = resultsObj.getJSONObject("Visibility");
	              BigDecimal vis = visibilityObj.getJSONObject("Metric").getBigDecimal("Value");
	              l12.setOpacity(1);
	              l13.setText(vis + " km");
	              
	              JSONObject windObj = resultsObj.getJSONObject("Wind");
	              BigDecimal windSpeed = windObj.getJSONObject("Speed").getJSONObject("Metric").getBigDecimal("Value");
	              l14.setOpacity(1);
	              l15.setText(windSpeed + " km/h");
	              
	              JSONObject windGustObj = resultsObj.getJSONObject("WindGust");
	              BigDecimal windgust = windGustObj.getJSONObject("Speed").getJSONObject("Metric").getBigDecimal("Value");
	              l16.setOpacity(1);
	              l17.setText(windgust + " km/h");

	              JSONObject pressureObj = resultsObj.getJSONObject("Pressure");
	              BigDecimal pressure = pressureObj.getJSONObject("Metric").getBigDecimal("Value");
	              l18.setOpacity(1);
	              l19.setText(pressure + " mb"); // millibar

	          } catch (JSONException e) {
	          	System.out.println(e);
	          }
	          
	  	 }
	      catch(Exception e) {
	      	System.out.println(e);
	      }
    }
}
