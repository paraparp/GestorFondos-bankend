package com.paraparp.gestorfondos.controller;

import java.io.IOException;
import java.math.BigDecimal;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paraparp.gestorfondos.service.imp.MorningStarService;

import net.minidev.json.parser.ParseException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/fundapp/test")
public class TestController {

	
	@Autowired
	MorningStarService msService;
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getJSONfromUrl() throws IOException, JSONException, ParseException {

		String date = "2019-11-11";
//		String json2 = msService.getPriceDate(LocalDate.now().plusDays(-1).toString(), "IE00B03HCZ61").toString();
		
		
		String json2 = msService.getSymbolInfo( "IE00B03HCZ61").toString();
	
//		ArrayList jsonObjList = googleJson.fromJson(json2, ArrayList.class);
//		System.out.println("List size is : " + jsonObjList.size());
//		System.out.println("List Elements are  : " + jsonObjList.toString());

//		for (int i = 0; i < array.length(); i++) {
//			System.out.println(array.getJSONObject(i).toString());
//		    JSONObject object = array.getJSONObject(i);
//		    String username = object.getString("EndDate");
//		    String status = object.getString("Value");
//		    
//		    System.out.println(username + " - " + status);
//		}
//		
//		

//		ObjectMapper objectMapper = new ObjectMapper();
//		List<HistoryDetail> listCar = objectMapper.readValue(json2, new TypeReference<List<HistoryDetail>>(){});

//		List<HistoryDetail> historyDetail = new Gson().fromJson(json2, HistoryDetail.class);

		return json2;
	}

	@RequestMapping(value = "/{isin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getMorningStarID(@PathVariable(value = "isin") String isin) throws IOException, JSONException, ParseException {

	
		return 	 msService.getSymbolInfo( "IE00B03HCZ61").toString();
	}
	
	
	@RequestMapping(value = "/{isin}/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BigDecimal getPriceByDate(@PathVariable(value = "isin") String isin,@PathVariable(value = "date") String date) throws IOException, JSONException, ParseException {

	
		return 	 msService.getPriceDate( date,isin);
	}
}