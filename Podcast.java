import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class Podcast {
	String podId;
	String celebrity;
	String genre;
	String dateOfPodcast;
	public String getPodId() {
		return podId;
	}
	public void setPodId(String podId) {
		this.podId = podId;
	}
	public String getCelebrity() {
		return celebrity;
	}
	public void setCelebrity(String celebrity) {
		this.celebrity = celebrity;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getDateOfPodcast() {
		return dateOfPodcast;
	}
	public void setDateOfPodcast(String dateOfPodcast) {
		this.dateOfPodcast = dateOfPodcast;
	}
	
	public Podcast(String podId, String celebrity, String genre, String date) {
		
		this.podId = podId;
		this.celebrity = celebrity;
		this.genre = genre;
		this.dateOfPodcast =  date;
	}
	public Podcast() {
		
	}
	
	public List<Podcast> storePodcastInArray(Connection conn, String username) throws SQLException, ParseException{
		List<Podcast> podcastList=new ArrayList<Podcast>();
		
		Statement statementObj=conn.createStatement();
		ResultSet rs=statementObj.executeQuery("select * from podcast");
		
		while(rs.next()) {
			podcastList.add(new Podcast(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
		}
		
		
		return podcastList;
	}
	
	public void viewAllPodcast(Connection conn, String username)  {
		List<Podcast> podcastList=new ArrayList<Podcast>();
		try {
			podcastList=storePodcastInArray(conn, username);
			System.out.println("\t\t-------------------------------------------------------------");
			System.out.format("\t\t%-10s %-21s %-14s %-15s\n","PodId","Celebrity","Genre","DateOfPodcast");
			System.out.println("\t\t-------------------------------------------------------------");
			Consumer<Podcast> printlist=p->{
			System.out.format("\t\t%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getCelebrity(),p.getGenre(),p.getDateOfPodcast());
			};
			podcastList.forEach(printlist);
			System.out.println("\t\t-------------------------------------------------------------");
			
		} catch (SQLException | ParseException e) {
		
			System.out.print("Jukebox stopped working...");
		}
	}
	
	public void searchPodcast(Connection conn, String username) throws SQLException, ParseException  {
		Scanner scanObj=new Scanner(System.in);
		List<Podcast> podcastList=new ArrayList<Podcast>();
		podcastList=storePodcastInArray(conn, username);
		List<Podcast> podcastListOutput=new ArrayList<Podcast>();
			try {
			System.out.println("\n\t\tEnter your choice to search from Podcast:\n\t\t1. Celebrity\n\t\t2. Genre\n\t\t3. Date of Podcast");
			int choice=scanObj.nextInt();
			switch(choice) {
			case 1:
				System.out.print("\n\t\tEnter Celebrity: ");
				String celebrity=scanObj.next();
				System.out.println("\t\t-------------------------------------------------------------");
				System.out.format("\t\t%-10s %-21s %-14s %-15s\n","PodId","Celebrity","Genre","DateOfPodcast");
				System.out.println("\t\t-------------------------------------------------------------");
				podcastListOutput=searchByCelebrity(podcastList,celebrity);
				podcastListOutput.forEach(p->{
					System.out.format("\t\t%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getCelebrity(),p.getGenre(),p.getDateOfPodcast());
				});
				System.out.println("\t\t-------------------------------------------------------------");
				showPodcastSongList(conn, username);
				break;
			case 2:
				System.out.print("\n\t\tEnter Genre: ");
				String genre=scanObj.next();
				System.out.println("\t\t-------------------------------------------------------------");
				System.out.format("\t\t%-10s %-21s %-14s %-15s\n","PodId","Celebrity","Genre","DateOfPodcast");
				System.out.println("\t\t-------------------------------------------------------------");
				podcastListOutput=searchByGenre(podcastList,genre);
				podcastListOutput.forEach(p->{
					System.out.format("\t\t%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getCelebrity(),p.getGenre(),p.getDateOfPodcast());
				});
				System.out.println("\t\t-------------------------------------------------------------");
				showPodcastSongList(conn, username);
				break;
			case 3:
				System.out.print("\n\t\tEnter Date:(YYYY-MM-DD) ");
				String date=scanObj.next();
				System.out.println("\t\t-------------------------------------------------------------");
				System.out.format("\t\t%-10s %-21s %-14s %-15s\n","PodId","Celebrity","Genre","DateOfPodcast");
				System.out.println("\t\t-------------------------------------------------------------");
				podcastListOutput=searchDateOfPodcast(podcastList,date);
				podcastListOutput.forEach(p->{
					System.out.format("\t\t%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getCelebrity(),p.getGenre(),p.getDateOfPodcast());
				});
				System.out.println("\t\t-------------------------------------------------------------");
				showPodcastSongList(conn, username);
				
			break; 
			default:
				System.out.print("\n\t\tInvalid choice");
			}
		} catch (Exception e) {
			System.out.print("\n\t\tJukebox stopped working...");
		}
		scanObj.close();
	}
	
	public List<Podcast> searchByCelebrity(List<Podcast> podcastList, String celebrity){
		List<Podcast> podcastListOutput=new ArrayList<Podcast>();
		podcastListOutput=podcastList.stream().filter(s->s.getCelebrity().contains(celebrity)).toList();
		return podcastListOutput;
	}
	
	public List<Podcast> searchByGenre(List<Podcast> podcastList, String genre){
		List<Podcast> podcastListOutput=new ArrayList<Podcast>();
		podcastListOutput=podcastList.stream().filter(s->s.getGenre().contains(genre)).toList();
		return podcastListOutput;
	}
	
	public List<Podcast> searchDateOfPodcast(List<Podcast> podcastList, String date){
		List<Podcast> podcastListOutput=new ArrayList<Podcast>();
		podcastListOutput=podcastList.stream().filter(s->s.getDateOfPodcast().contains(date)).toList();
		return podcastListOutput;
	}
	
	public void showPodcastSongList(Connection conn, String username) {
		try {
		Scanner scanObj=new Scanner(System.in);
		System.out.print("\n\t\tEnter Podcast ID to view the song list: ");
		String podcastID=scanObj.next();
		Statement statementObj;
		
		statementObj = conn.createStatement();
		
		ResultSet rs2=statementObj.executeQuery("select * from song where songId in(select songId from podcastsonglist where PodId='"+podcastID+"')");
		ArrayList<Song> songList=new ArrayList<Song>();
		while(rs2.next()) {
			songList.add(new Song(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4),rs2.getString(5),rs2.getString(6)));
		}
		Song song=new Song();
		song.displaySongs(songList, username);
		scanObj.close();
		} catch (SQLException e) {
			System.out.print("\n\t\tJukebox stopped working...");
		}
	}
}
