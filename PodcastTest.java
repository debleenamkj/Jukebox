import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PodcastTest {

	Podcast podcast;
	
	@BeforeEach
	void setUp() {
		podcast=new Podcast();
	}
	
	@AfterEach
	void tearDown() {
		podcast=null;
	}
	
	@Test
	void searchByCelebrity() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Podcast> listOfOutput=(ArrayList<Podcast>) podcast.storePodcastInArray(conn, "j");
		assertTrue(podcast.searchByCelebrity(listOfOutput, "Kish").size()>0);
		assertTrue(podcast.searchByCelebrity(listOfOutput, "F").size()>0);
		assertTrue(podcast.searchByCelebrity(listOfOutput, "123456").size()==0);
		assertFalse(podcast.searchByCelebrity(listOfOutput, "F").size()<0);
		assertFalse(podcast.searchByCelebrity(listOfOutput, "Galaktak").size()<0);
		assertFalse(podcast.searchByCelebrity(listOfOutput, "Kish").size()<0);
	}
	
	@Test
	void searchByGenre() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Podcast> listOfOutput=(ArrayList<Podcast>) podcast.storePodcastInArray(conn,"j");
		assertTrue(podcast.searchByGenre(listOfOutput, "Pop").size()>0);
		assertTrue(podcast.searchByGenre(listOfOutput, "Bollywood").size()>0);
		assertTrue(podcast.searchByGenre(listOfOutput, "Kishore").size()==0);
		assertFalse(podcast.searchByGenre(listOfOutput, "Pop").size()<0);
		assertFalse(podcast.searchByGenre(listOfOutput, "Kishore").size()>0);
		assertFalse(podcast.searchByGenre(listOfOutput, "Bollywood").size()==0);
	}
	
	@Test
	void searchByDateOfPodcast() throws Exception {
		Connection conn = DBConn.getConnection();
		ArrayList<Podcast> listOfOutput=(ArrayList<Podcast>) podcast.storePodcastInArray(conn,"j");
		assertTrue(podcast.searchDateOfPodcast(listOfOutput, "1971-01-29").size()>0);
		assertTrue(podcast.searchDateOfPodcast(listOfOutput, "1998-03-12").size()>0);
		assertFalse(podcast.searchDateOfPodcast(listOfOutput, "1998-03-12").size()<0);
		assertFalse(podcast.searchDateOfPodcast(listOfOutput, "1998-03-12").size()==0);
	}
	
	

}
