package com.biksapp;

public class DataModel {

    private String Id, PointCoordinate, Type, Description, ImageUrl;

    public String getImageUrl(){
        return ImageUrl;
    }

    public String getId(){
        return Id;
    }

    public void setId(String Id){
        this.Id = Id;
    }

    public void setImageUrl(String imageUrl){
        this.ImageUrl = imageUrl;
    }

    public String getPointCoordinate() {
        return PointCoordinate;
    }

    public void setPointCoordinate(String pointCoordinate) {
        this.PointCoordinate = pointCoordinate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }
}



