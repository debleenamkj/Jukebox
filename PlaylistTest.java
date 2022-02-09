import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaylistTest {

	Playlist playlist;
	
	@BeforeEach
	void setUp() {
		playlist=new Playlist();
	}
	
	@AfterEach
	void tearDown() {
		playlist=null;
	}
	
	@Test
	void testNumberOfSongs() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Song> listOfOutput=(ArrayList<Song>) playlist.gettingSongsFromPlaylistID(conn, "j", "PLS1001");
		assertTrue(listOfOutput.size()>0);
	}
	
	@Test
	void testNumberOfSongs2() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Song> listOfOutput=(ArrayList<Song>) playlist.gettingSongsFromPlaylistID(conn, "user1", "PLS1002");
		assertTrue(listOfOutput.size()>0);
	}
	
	@Test
	void testParticularSongIsAddedToThePlaylist() throws Exception {
		Connection conn = DBConn.getConnection();
		List<Song> listOfOutput=(ArrayList<Song>) playlist.gettingSongsFromPlaylistID(conn, "user4", "PLS1005");
		List<Song> list=new ArrayList<Song>();
		list=listOfOutput.stream().filter(s->s.getSongID().contains("SNG1011")).toList();
		assertTrue(list.size()>0);
	}
	
	@Test
	void testParticularSongIsAddedToThePlaylist2() throws Exception {
		Connection conn = DBConn.getConnection();
		List<Song> listOfOutput=(ArrayList<Song>) playlist.gettingSongsFromPlaylistID(conn, "user4", "PLS1005");
		List<Song> list=new ArrayList<Song>();
		list=listOfOutput.stream().filter(s->s.getSongID().contains("SNG1005")).toList();
		assertTrue(list.size()>0);
	}
	
	@Test
	void testParticularSongIsAddedToThePlaylist3() throws Exception {
		Connection conn = DBConn.getConnection();
		List<Song> listOfOutput=(ArrayList<Song>) playlist.gettingSongsFromPlaylistID(conn, "user4", "PLS1005");
		List<Song> list=new ArrayList<Song>();
		list=listOfOutput.stream().filter(s->s.getSongID().contains("SNG1009")).toList();
		assertTrue(list.size()>0);
	}
}
