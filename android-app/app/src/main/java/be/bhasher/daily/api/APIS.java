package be.bhasher.daily.api;

public enum APIS {
    CONNECTION("connections.php?");

    final private String path;

    APIS(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}