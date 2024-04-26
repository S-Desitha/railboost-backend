package org.ucsc.railboostbackend.models;

public class ResponseType {
    private boolean isSuccessful;
    private String error;

    public ResponseType() {}

    public ResponseType(boolean isSuccessful, String error) {
        this.isSuccessful = isSuccessful;
        this.error = error;
    }

    public String getError(){
        return  error;
    }
    public boolean getIsSuccessful(boolean isSuccessful){
        return  isSuccessful;
    }
    public void setError(String error) {
        this.error = error;
    }
    public void setISSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

}
