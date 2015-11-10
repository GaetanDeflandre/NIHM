// GLOBALLY

// declare the min and max variables that you need in parseInfo
float minX, maxX;
float minY, maxY;
int totalCount;// total number of places 
float minPopulation, maxPopulation; 
float minSurface, maxSurface; 
float minAltitude, maxAltitude;

// declare the variables corresponding to the column ids for x and y
final int X = 1;
final int Y = 2;

// the table in which the city data will be stored
City[] cities;

void setup() { 
  size(800,800);
  readData();
}

void draw(){ 
  background(255);
  for (int i = 0 ; i < totalCount ; i++) {
    cities[i].drawCity();
  }
}

void readData() {
  String[] lines = loadStrings("./villes.tsv");
  parseInfo(lines[0]); // read the header line
  
  cities = new City[totalCount];
  for (int i = 2 ; i < totalCount+2; ++i) {
    String[] columns = split(lines[i], TAB);
    
    int postalcode = int (columns[0]);
    float x = float (columns[X]);
    float y = float (columns[Y]);
    String name = new String(columns[4]);
    float population = float (columns[5]);
    float surface = float (columns[6]);
    float altitude = float (columns[7]);
       
    cities[i-2] = new City(postalcode, name, x, y, population, surface, altitude);
  }
}

void parseInfo(String line) { 
  // remove the # 
  String infoString = line.substring(2);
  
  String[] infoPieces = split(infoString, ',');
  totalCount = int(infoPieces[0]);
  minX = float(infoPieces[1]);
  maxX = float(infoPieces[2]);
  minY = float(infoPieces[3]);
  maxY = float(infoPieces[4]);
  minPopulation = float(infoPieces[5]);
  maxPopulation = float(infoPieces[6]);
  minSurface = float(infoPieces[7]);
  maxSurface = float(infoPieces[8]);
  minAltitude = float(infoPieces[9]);
  maxAltitude = float(infoPieces[10]);
}