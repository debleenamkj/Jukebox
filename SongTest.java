import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SongTest {
	Song song;
	@BeforeEach
	void setUp() {
	song=new Song();
	}
	
	@AfterEach
	void tearDown() {
		song=null;
	}
	
	@Test
	void displayTestTrue() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Song> listOfOutput=(ArrayList<Song>) song.storeSongsInArrayList(conn);
		assertTrue(listOfOutput.size()>0);
	}
	
	@Test
	void displayTestFalse() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Song> listOfOutput=(ArrayList<Song>) song.storeSongsInArrayList(conn);
		assertFalse(listOfOutput.size()<0);
	}
	
	@Test
	void searchByNameTestFalse() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Song> listOfOutput=(ArrayList<Song>) song.storeSongsInArrayList(conn);
		assertTrue(song.searchByName(listOfOutput, "Raa").size()>0);
		assertTrue(song.searchByName(listOfOutput, "Ki").size()>0);
		assertTrue(song.searchByName(listOfOutput, "Mer").size()>0);
		assertTrue(song.searchByName(listOfOutput, "A").size()>0);
		assertFalse(song.searchByName(listOfOutput, "A").size()==0);
		assertFalse(song.searchByName(listOfOutput, "Raa").size()==3);
		assertFalse(song.searchByName(listOfOutput, "M").size()<0);
	}
	
	@Test
	void searchByArtistTestFalse() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Song> listOfOutput=(ArrayList<Song>) song.storeSongsInArrayList(conn);
		assertTrue(song.searchByArtist(listOfOutput, "Asha").size()>0);
		assertTrue(song.searchByArtist(listOfOutput, "Kishore Kumar").size()>0);
		assertTrue(song.searchByArtist(listOfOutput, "Hemant").size()>0);
		assertTrue(song.searchByArtist(listOfOutput, "A").size()>0);
		assertFalse(song.searchByArtist(listOfOutput, "He").size()==0);
		assertFalse(song.searchByArtist(listOfOutput, "Raa").size()==3);
		assertFalse(song.searchByArtist(listOfOutput, "Hamant").size()>0);
	}
	
	@Test
	void searchByGenreTestFalse() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Song> listOfOutput=(ArrayList<Song>) song.storeSongsInArrayList(conn);
		assertTrue(song.searchByGenre(listOfOutput, "Pop").size()>0);
		assertTrue(song.searchByGenre(listOfOutput, "Melody").size()>0);
		assertTrue(song.searchByGenre(listOfOutput, "Bollywood").size()>0);
		assertTrue(song.searchByGenre(listOfOutput, "Pizza").size()==0);
		assertFalse(song.searchByGenre(listOfOutput, "Sandwich").size()>0);
		assertFalse(song.searchByGenre(listOfOutput, "Romantic").size()<0);
		assertFalse(song.searchByGenre(listOfOutput, "Sad").size()<0);
	}
	
	@Test
	void searchByAlbumTestFalse() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Song> listOfOutput=(ArrayList<Song>) song.storeSongsInArrayList(conn);
		assertTrue(song.searchByAlbum(listOfOutput, "Sh").size()>0);
		assertTrue(song.searchByAlbum(listOfOutput, "Yaad").size()>0);
		assertTrue(song.searchByAlbum(listOfOutput, "Jab").size()>0);
		assertTrue(song.searchByAlbum(listOfOutput, "ZZZZZZZ").size()==0);
		assertFalse(song.searchByAlbum(listOfOutput, "Waqt").size()==0);
		assertFalse(song.searchByAlbum(listOfOutput, "Jewel").size()<0);
		assertFalse(song.searchByAlbum(listOfOutput, "Ro").size()<0);
	}
}
