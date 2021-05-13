package com.biksapp;

public class Cameras {
    String description, imageName, imageType;
    double[] coords;

    public Cameras(String description, String imageName, String imageType,double[] coords){
        this.description= description;
        this.imageName= imageName;
        this.imageType= imageType;
        this.coords=coords;
    }
    //checking what type of image we have to use the right url base
    public String getImageUrl(){

        if (imageType.equals("sdot")){
            return  "https://www.seattle.gov/trafficcams/images/"+imageName;
        }else{
            return "https://images.wsdot.wa.gov/nw/"+imageName;
        }
    }

    public double[] getCoords() {
        return coords;
    }
}
