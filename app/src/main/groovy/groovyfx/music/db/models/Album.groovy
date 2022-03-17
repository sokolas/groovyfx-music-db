package groovyfx.music.db.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

class Album {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleIntegerProperty artistId = new SimpleIntegerProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();

    Album(int id, int artistId, String name) {
        this.id.set(id);
        this.artistId.set(artistId);
        this.name.set(name);
    }

    int getId() {
        return id.get();
    }

    SimpleIntegerProperty idProperty() {
        return id;
    }

    void setId(int id) {
        this.id.set(id);
    }

    String getName() {
        return name.get();
    }

    SimpleStringProperty nameProperty() {
        return name;
    }

    void setName(String name) {
        this.name.set(name);
    }

    int getArtistId() {
        return artistId.get();
    }

    SimpleIntegerProperty artistIdProperty() {
        return artistId;
    }

    void setArtistId(int artistId) {
        this.artistId.set(artistId);
    }

    @Override
    String toString() {
        return "Album{" + "\"ID\": " + id.get() + ", \"Name\": \"" + name.get() + "\", \"Artist ID\": " + artistId.get() + '}';
    }

}
