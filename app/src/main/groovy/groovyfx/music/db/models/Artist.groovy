package groovyfx.music.db.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

class Artist {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();

    Artist(int id, String name) {
        this.id.set(id);
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

    @Override
    String toString() { return "Artist{" + "\"ID\": " + id.get() + ", \"Name\": \"" + name.get() + '\"' + '}'; }
}
