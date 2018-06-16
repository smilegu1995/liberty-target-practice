package io.openliberty.sentry.demo.model;

public class Target {
	
	int id;
	int photoValue;
	int piezoValue;
	
	public Target(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public int getPhotoValue(){
		return photoValue;
	}
	
	public int getpiezoValue(){
		return piezoValue;
	}
	
	public void setPhotoValue(int value){
		photoValue = value;
	}
	
	public void setPiezoValue(int value){
		piezoValue = value;
	}

}
