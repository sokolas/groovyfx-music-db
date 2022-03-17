package groovyfx.music.db.models;

public record Song(int id, String title, int track, int albumID) {
    @Override
    public String toString() {
        return "Song{" + "\"ID\": " + id + ", \"Title\": \"" + title + "\", \"Track\": " + track + ", \"AlbumID\": " + track + '}';
    }
}
