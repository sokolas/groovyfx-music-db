package groovyfx.music.db.models;

public record MusicView(String artistName, String songTitle, int track, String albumName) {
    @Override
    public String toString() {
        return "{" + "\"Artist\": \"\"" + artistName + "\", \"Title\": \"" + songTitle + "\", \"Track\": " + track + ", \"Album\": \"" + albumName + "\"}";
    }
}
