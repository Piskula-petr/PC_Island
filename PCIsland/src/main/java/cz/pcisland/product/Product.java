package cz.pcisland.product;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

/**
 *	Třída produktu:
 *
 *		bezparametrový konstruktor,
 * 		gettery + settery atributů (+ vytvoření Mapy parametrů, popisku produktu);
 */
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Parametry produktu
	private int id;
	private String type;
	private String name;
	private int price;
	private int amount;
	private int stock;
	private int sales;
	private int overallRating;
	private int numberOfPreview;
	private Map<String, String> parametrsMap;
	
// Bezparamatrový konstruktor ///////////////////////////////////////////////////////////////////////
	
	public Product() {
		
	}
	
// Konstruktor //////////////////////////////////////////////////////////////////////////////////////
	
	public Product(int id, String type, String name, int price, int amount, int stock, int sales, int overallRating, int numberOfReview, Map<String, String> parametrs) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.stock = stock;
		this.sales = sales;
		this.overallRating = overallRating;
		this.numberOfPreview = numberOfReview;
		this.parametrsMap = parametrs;
	}

// Gettery + Settery ////////////////////////////////////////////////////////////////////////////////
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public int getSales() {
		return sales;
	}
	
	public void setSales(int sales) {
		this.sales = sales;
	}

	public int getOverallRating() {
		return overallRating;
	}

	public void setOverallRating(int overallRating) {
		this.overallRating = overallRating;
	}

	public int getNumberOfPreview() {
		return numberOfPreview;
	}

	public void setNumberOfPreview(int numberOfPreview) {
		this.numberOfPreview = numberOfPreview;
	}

	public void setParametrs(String type, String parametrs) {
		
		String[] parametrsArray = parametrs.split(";");
		Map<String, String> parametrsMap = new HashedMap<>();
		
		// Procesor
		if (type.equals("processor")) {
			
			parametrsMap.put("company", parametrsArray[0]);
			parametrsMap.put("series", parametrsArray[1]);
			parametrsMap.put("codeName", parametrsArray[2]);
			parametrsMap.put("socket", parametrsArray[3]);
			parametrsMap.put("numberOfCores", parametrsArray[4]);
			parametrsMap.put("numberOfThreads", parametrsArray[5]);
			parametrsMap.put("workFrequency", parametrsArray[6]);
			parametrsMap.put("turboFrequency", parametrsArray[7]);
			parametrsMap.put("cache", parametrsArray[8]);
			parametrsMap.put("thermalDesignPower", parametrsArray[9]);
			parametrsMap.put("technology", parametrsArray[10]);
			parametrsMap.put("chipset", parametrsArray[11]);
			
		// Grafická karta 	
		} else if (type.equals("graphics_card")) {
			
			parametrsMap.put("company", parametrsArray[0]);
			parametrsMap.put("graphicsChip", parametrsArray[1]);
			parametrsMap.put("series", parametrsArray[2]);
			parametrsMap.put("coreFrequency", parametrsArray[3]);
			parametrsMap.put("streamProcess", parametrsArray[4]);
			parametrsMap.put("memoryType", parametrsArray[5]);
			parametrsMap.put("memorySize", parametrsArray[6]);
			parametrsMap.put("memoryFrequency", parametrsArray[7]);
			parametrsMap.put("memoryWidth", parametrsArray[8]);
			parametrsMap.put("connector", parametrsArray[9]);
			parametrsMap.put("thermalDesignPower", parametrsArray[10]);
			parametrsMap.put("outputs", parametrsArray[11]);
			
		// Operační paměť
		} else if (type.equals("memory")) {
			
			parametrsMap.put("company", parametrsArray[0]);
			parametrsMap.put("series", parametrsArray[1]);
			parametrsMap.put("memorySize", parametrsArray[2]);
			parametrsMap.put("memoryType", parametrsArray[3]);
			parametrsMap.put("memoryFrequency", parametrsArray[4]);
			parametrsMap.put("latency", parametrsArray[5]);
			parametrsMap.put("voltage", parametrsArray[6]);
			parametrsMap.put("XMP", parametrsArray[7]);
			
		// Základní deska
		} else if (type.equals("motherboard")) {
			
			parametrsMap.put("company", parametrsArray[0]);
			parametrsMap.put("chipset", parametrsArray[1]);
			parametrsMap.put("socket", parametrsArray[2]);
			parametrsMap.put("processorGeneration", parametrsArray[3]);
			parametrsMap.put("memoryType", parametrsArray[4]);
			parametrsMap.put("maxMemoryFrequency", parametrsArray[5]);
			parametrsMap.put("maxMmemory", parametrsArray[6]);
			parametrsMap.put("format", parametrsArray[7]);
			parametrsMap.put("connectors", parametrsArray[8]);
			
		// Pevný disk
		} else if (type.equals("hard_disk")) {
			
			parametrsMap.put("company", parametrsArray[0]);
			parametrsMap.put("type", parametrsArray[1]);
			parametrsMap.put("format", parametrsArray[2]);
			parametrsMap.put("size", parametrsArray[3]);
			parametrsMap.put("readingSpeed", parametrsArray[4]);
			parametrsMap.put("writeSpeed", parametrsArray[5]);
			parametrsMap.put("RMP", parametrsArray[6]);
			parametrsMap.put("connector", parametrsArray[7]);
			
		// Zdroj
		} else if (type.equals("power_supply_unit")) {
			
			parametrsMap.put("company", parametrsArray[0]);
			parametrsMap.put("type", parametrsArray[1]);
			parametrsMap.put("performance", parametrsArray[2]);
			parametrsMap.put("efficiency", parametrsArray[3]);
			parametrsMap.put("fanSize", parametrsArray[4]);
			parametrsMap.put("connectors", parametrsArray[5]);
		}
		
		this.parametrsMap = parametrsMap;
	}

	public Map<String, String> getParametrs() {
		return parametrsMap;
	}
	
	public String getDescription(String type, Map<String, String> parametrsMap) {
		
		String pattern = "###,###.###";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		
		String description = "";
		
// Procesor ///////////////////////////////////////////////////////////////////////////////////////////
		
		if (type.equals("processor")) {
			
			description = "Procesor " + parametrsMap.get("company") + " "+ parametrsMap.get("series") + ", " + parametrsMap.get("codeName") +
					 ", " + parametrsMap.get("numberOfCores") + " jader, " + parametrsMap.get("numberOfThreads") + 
					 " vláken, frekvence " + parametrsMap.get("workFrequency") + " GHz, turboboost " + parametrsMap.get("turboFrequency") + 
					 " GHz, " + parametrsMap.get("thermalDesignPower") + " W, socket " + parametrsMap.get("socket") + 
					 ", vyrovnávací paměť cache " + parametrsMap.get("cache") + " MB, výrobní technologie " + parametrsMap.get("technology") + " nm, chipset " + parametrsMap.get("chipset");
		
// Grafická karta /////////////////////////////////////////////////////////////////////////////////////
		
		} else if (type.equals("graphics_card")) {
			
			description = "Grafická karta " + parametrsMap.get("graphicsChip") + " " + parametrsMap.get("series") + ", " + Integer.parseInt(parametrsMap.get("memorySize")) / 1000 + 
					 " GB " + parametrsMap.get("memoryType") + ", sběrnice " + parametrsMap.get("memoryWidth") + " bit, frekvence " + decimalFormat.format(Integer.parseInt(parametrsMap.get("coreFrequency"))) +
					 " MHz / frekvence paměti " + parametrsMap.get("memoryFrequency") + " GHz, " + parametrsMap.get("thermalDesignPower") +
					 " W, stream procesory " + decimalFormat.format(Integer.parseInt(parametrsMap.get("streamProcess"))) + " x, " + parametrsMap.get("connector") + ", " + parametrsMap.get("outputs");
		
// Operační paměť /////////////////////////////////////////////////////////////////////////////////////
		
		} else if (type.equals("memory")) {
			
			description = "Operační paměť " + parametrsMap.get("company") + ", řady " + parametrsMap.get("series") + ", kapacita " + parametrsMap.get("memorySize") + 
					 " GB, typu " + parametrsMap.get("memoryType") + ", frekvence " + decimalFormat.format(Integer.parseInt(parametrsMap.get("memoryFrequency"))) + " MHz, časování " + parametrsMap.get("latency") + 
					 " při pracovním napětí " + parametrsMap.get("voltage") + " V, podpora " + parametrsMap.get("XMP");
	
// Základní deska /////////////////////////////////////////////////////////////////////////////////////
		
		} else if (type.equals("motherboard")) {
			
			description = "Základní deska " + parametrsMap.get("company") + ", chipset " + parametrsMap.get("chipset") + ", socket " + parametrsMap.get("socket") + 
					 " pro procesory " + parametrsMap.get("processorGeneration") + ", " + parametrsMap.get("memoryType") + ", frekvence paměti až " + decimalFormat.format(Integer.parseInt(parametrsMap.get("maxMemoryFrequency"))) + 
					 " MHz, maximálně velikost paměti " + parametrsMap.get("maxMmemory") + " GB, formát " + parametrsMap.get("format");
		
// Pevný disk /////////////////////////////////////////////////////////////////////////////////////////
		
		} else if (type.equals("hard_disk")) {
			
			String format = ""; 
			if (parametrsMap.get("format").equals("2.5") || parametrsMap.get("format").equals("3.5")) {
				format = parametrsMap.get("format") + "\"";
				
			} else format = parametrsMap.get("format");
			
			String size = "";
			if (Integer.parseInt(parametrsMap.get("size")) >= 1000) {
				size = Integer.parseInt(parametrsMap.get("size")) / 1000 + " TB";
				
			} else size = (parametrsMap.get("size")) + " GB";
			
			String RMP = "";
			if (Integer.parseInt(parametrsMap.get("RMP")) > 0) {
				RMP = ", " + decimalFormat.format(Integer.parseInt(parametrsMap.get("RMP"))) + " otáček/min";
			}
			
			
			description = parametrsMap.get("type") + " disk " + parametrsMap.get("company") + " ve formátu " + format + ", kapacita " + size + 
					 RMP + ", rychlost čtení " + decimalFormat.format(Integer.parseInt(parametrsMap.get("readingSpeed"))) + " MB/s, rychlost zápisu " 
					 + decimalFormat.format(Integer.parseInt(parametrsMap.get("writeSpeed"))) + " MB/s, rozhraní " + parametrsMap.get("connector");
		
// Zdroje /////////////////////////////////////////////////////////////////////////////////////////////
		
		} else if (type.equals("power_supply_unit")) {
			
			description = parametrsMap.get("type") + " zdroj " + parametrsMap.get("company") + ", " + decimalFormat.format(Integer.parseInt(parametrsMap.get("performance"))) + 
					 " W, účinost " + parametrsMap.get("efficiency") + ", velikost ventilátoru " + parametrsMap.get("fanSize") + 
					 " mm, konektory " + parametrsMap.get("connectors");
		}
		
		return description;
	}
	
}
