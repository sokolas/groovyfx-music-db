package groovyfx.music.db.controllers

import groovy.util.logging.Slf4j
import groovyfx.music.db.models.Album
import groovyfx.music.db.models.Artist
import groovyfx.music.db.services.impl.AlbumDAOImpl
import groovyfx.music.db.services.impl.ArtistDAOImpl
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.scene.control.ProgressBar
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane

@Slf4j
class Controller {

    @FXML
    private BorderPane mainPane

    @FXML
    private TableView artistTable

    @FXML
    private ProgressBar progressBar

    void initialize() {
        artistTable.getSelectionModel().selectFirst()
    }

    @FXML
    void listArtists() {
        Task<ObservableList<Artist>> task = new GetAllArtistsTask()

        progressBar.progressProperty().bind(task.progressProperty())
        progressBar.setVisible(true)
        task.setOnSucceeded(e -> progressBar.setVisible(false))
        task.setOnFailed(e -> progressBar.setVisible(false))

        artistTable.itemsProperty().bind(task.valueProperty())
        new Thread(task).start()
    }

    @FXML
    void listAlbumsForArtist() {
        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem()
        if (artist != null) {
            Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
                @Override
                protected ObservableList<Album> call() throws Exception {
                    AlbumDAOImpl albumDAO = new AlbumDAOImpl()
                    return FXCollections.observableArrayList(albumDAO.findAllByArtistId(artist.getId()))
                }
            }
            artistTable.itemsProperty().bind(task.valueProperty())
            new Thread(task).start()
        } else {
            log.warn("No Artist selected!")
        }
    }

    //TODO: Open Dialog Box to add the values.
    @FXML
    void updateArtist() {
        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem()
        Task<Artist> task = new Task<Artist>() {
            @Override
            protected Artist call() throws Exception {
                ArtistDAOImpl artistDAO = new ArtistDAOImpl()
                return artistDAO.update(new Artist(artist.getId(), "Wizkid"))
            }
        }
        task.setOnSucceeded(e -> {
            if (task.valueProperty().get() != null) {
                artist.setName("Wizkid")
//                artistTable.refresh(); - may not be needed in recent versions of jfx
            }
        })
        new Thread(task).start()
    }
}

//We're creating a separate class because we may need to use this in 2 places.
class GetAllArtistsTask extends Task<ObservableList<Artist>> {
    @Override
    protected ObservableList<Artist> call() throws Exception {
        return FXCollections.observableArrayList(new ArtistDAOImpl().findAll())
    }
}
