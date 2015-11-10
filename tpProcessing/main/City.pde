public class City {
  
  // ATTRIBUTS
  private int postalcode;
  private String name;
  private float x;
  private float y;
  private float population;
  private float surface;
  private float altitude;
  
  /**
   * Classe static, contenant les informations sur 
   * les minima et maxima des villes instanciées
   */
  private CityMinMax minMaxStat;
  

  // CONSTANTES
  public final static float MIN_DIAMETER = 2.0f;
  public final static float MAX_DIAMETER = 250.0f;
  
  
  // CONSTRUCTEUR
  
  /**
   * Allocation d'un objet ville.
   */
  public City (int postalcode, String name, float x, float y, float population, float surface, float altitude){
    this.postalcode = postalcode;
    this.name = name;
    this.x = x;
    this.y = y;
    this.population = population;
    this.surface = surface;
    this.altitude = altitude;
    
    // Mise à jour minima et maxima
    if (minMaxStat.MIN_POP == -1.0f || minMaxStat.MIN_POP > population){
      minMaxStat.MIN_POP = population;
    }
    
    if (minMaxStat.MAX_POP == -1.0f || minMaxStat.MAX_POP < population){
      minMaxStat.MAX_POP = population;
    }
    
  }
  
  
  // GETTER
  public float getX(){
    return this.x; 
  }
  
  public float getY(){
    return this.y;
  }
  
  public int getPostalcode(){
    return this.postalcode;
  }
  
  public String getName(){
    return this.name; 
  }
  
  public float getPopulation(){
    return this.population;
  }
  
  public float getSurface(){
    return this.surface; 
  }
  
  public float getAltitude(){
    return this.altitude;
  }
  
  public float getDensity(){
    if(this.getSurface() <= 0.0){
      return 0.0;
    }
    return  this.getPopulation() / this.getSurface();
  }
  
  
  // METHODES
  
  private float mapX(float x) {
    return map(x, minX, maxX, 0, 800);
  }
  
  private float mapY(float y) {
    return map(y, maxY, minY, 0, 800);
  }
  
  /**
   * @param valInI1 Valeur du nombre à interpoler. Est dans l'intervalle 1 (I1) 
   * @param minI1 Valeur minimun de l'intervalle 1 (I1)
   * @param maxI1 Valeur maximum de l'intervalle 1 (I1)
   * @param minI2 Valeur minimun de l'intervalle 2 (I2)
   * @param maxI2 Valeur maximum de l'intervalle 2 (I2)
   * @return une valeur entre minI2 et maxI2, correspondant à la valeur valInI1 interpolé.
   */
  private float interpolateFromI1ToI2(float valInI1, float minI1, float maxI1, float minI2, float maxI2){
    float lenI1 = maxI1 - minI1;
    float lenI2 = maxI2 - minI2;
    
    return minI2 + ( lenI2*(valInI1-minI1) / lenI1 );
  }
  
  public void drawCity(){
    
    if(getPopulation() == 0.0 || getDensity() == 0.0){
      // check limit cases
      return;
    }
    

    int posX = int(mapX(getX()));
    int posY = int(mapY(getY()));

    /*
    On recherche un diamètre correspondant à la population:
     - on divise alors une premier fois par PI, 
       pour que les aires des villes soit représentatives 
       de la population (et non le diamétre).
     - on interpole pour convertir un nombre d'habitant en diametre.
    */
    float popDiameter = interpolateFromI1ToI2(getPopulation(), minMaxStat.MIN_POP, minMaxStat.MAX_POP, MIN_DIAMETER, MAX_DIAMETER);
    
    //colorMode(HSB, 255);
    color densColor = color(0, 0, 255);
    float value = hue(densColor);
    
    fill(densColor);
    ellipse(posX, posY, int(popDiameter), int(popDiameter));
  }
}