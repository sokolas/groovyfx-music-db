package groovyfx.music.db.services;

import groovyfx.music.db.models.Album;

interface AlbumDAO extends CrudDAO<Album, Integer> {
    List<Album> findAllByArtistId(Integer artistId);
}
