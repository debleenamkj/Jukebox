import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Song {
	//to store current position of song
	Long currentFrame;
	Clip clip;
	
	//current status of song
	String status;
	
	AudioInputStream inputStream;
	
	//Song variables and their getter and setter
	String songID;
	String songName;
	String songArtist;
	String songGenre;
	String songAlbum;
	String songDuration;
	
	public Song(String songID, String songName, String songArtist, String songGenre, String songAlbum,
			String songDuration) {
		super();
		this.songID = songID;
		this.songName = songName;
		this.songArtist = songArtist;
		this.songGenre = songGenre;
		this.songAlbum = songAlbum;
		this.songDuration = songDuration;
	}
	
	public Song() {
		
	}
	
	public String getSongID() {
		return songID;
	}

	public void setSongID(String songID) {
		this.songID = songID;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSongArtist() {
		return songArtist;
	}

	public void setSongArtist(String songArtist) {
		this.songArtist = songArtist;
	}

	public String getSongGenre() {
		return songGenre;
	}

	public void setSongGenre(String songGenre) {
		this.songGenre = songGenre;
	}

	public String getSongAlbum() {
		return songAlbum;
	}

	public void setSongAlbum(String songAlbum) {
		this.songAlbum = songAlbum;
	}

	public String getSongDuration() {
		return songDuration;
	}

	public void setSongDuration(String songDuration) {
		this.songDuration = songDuration;
	}

	
	
	public String generateSongID(Connection conn) throws SQLException{
		String songId="", newSongId="";
		Statement statement=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs=statement.executeQuery("select SongId from song");
		while(rs.next()) {
			if(rs.isLast()) {
				songId=rs.getString(1);
			}
		}
		newSongId="SNG"+Integer.toString(Integer.parseInt(songId.substring(3,7))+1);
		
		return newSongId;
	}
	
	public List<Song> storeSongsInArrayList(Connection conn) throws SQLException {
		Statement statementObj=conn.createStatement();
		ResultSet rs=statementObj.executeQuery("select * from song");
		
		ArrayList<Song> songList=new ArrayList<Song>();
		while(rs.next()) {
			songList.add(new Song(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
		}
		
		return songList;
	}
	
	
	
	public void songMenu(String username) {
		
		try {
			Connection conn = DBConn.getConnection();
			Scanner scanObj=new Scanner(System.in);
			User user=new User();
			List<Song> songList=new ArrayList<Song>();
			songList=storeSongsInArrayList(conn);
			Podcast podcast=new Podcast();
			System.out.println();
			System.out.println("\n\t\tEnter:\n\t\t1. Display All Songs\n\t\t2. Search\n\t\t3. View Podcast\n\t\t4. Search Podcast\n\t\t5. See More");
			int choice=scanObj.nextInt();
			switch(choice) {
				case 1:
					displaySongs(songList, username);
				break;
				case 2:
					searchSongs(songList, username);
				break;
				case 3:
					podcast.viewAllPodcast(conn, username);
					podcast.showPodcastSongList(conn, username);
				break;
				case 4:
					podcast.searchPodcast(conn, username);
				break;
				case 5:
					user.callPlaylist(conn, username);
				break;
				default:
					System.out.println("\n\t\tInvalid Choice");
			}
			scanObj.close();
		}
		catch(Exception e) {
			System.out.print("\n\t\tJukeBox abruptly stopped working");
		}
	}

	
	
	public void displaySongs(List<Song> songList, String username) throws SQLException {
		Scanner scanObj=new Scanner(System.in);
		System.out.println("\t\t----------------------------------------------------------------------------------------------------------------------------------");
		System.out.format("\t\t%-15s %-50s %-18s %-14s %-19s %-15s"," #SongID","Song Name","Artist","Genre","Album","Duration");
		System.out.println("\n\t\t----------------------------------------------------------------------------------------------------------------------------------");
		if(songList.size()>0) {
			Consumer<Song> printlist=t->{
				System.out.format("\t\t%-12s %-50s %-18s %-13s %-23s %-15s\n","|"+t.getSongID(),"|"+t.getSongName(),"|"+t.getSongArtist(),"|"+t.getSongGenre(), "|"+t.getSongAlbum(),"|"+t.getSongDuration()+"|");
			};
			songList.forEach(printlist);
			System.out.println("\t\t----------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("\n\t\tDo you want to play?\n\t\t1. Individual Song\n\t\t2. All the songs in the list");
			int choice=scanObj.nextInt();
			if(choice==1) {
			System.out.print("\n\t\tEnter the ID of Song you want to play: ");
			String songID=scanObj.next();
			playSongs(songID, username);
			}
			if(choice==2) {
				playSongsPlaylist(songList, username);
			}
		}
		else {
			System.out.format("\t\t%-12s %-50s %-18s %-13s %-23s %-15s\n","N/A","N/A","N/A","N/A", "N/A","N/A");
		
		}
		scanObj.close();
	}
	public void playSongs(String songID, String username) {
		Scanner scanObj=new Scanner(System.in);
		try {			
			System.out.print("\t\tSong is playing...");
			String url="C:\\niit\\Eclipse\\FinalJavaProjectJUKEBOX\\src\\Songs\\"+songID+".wav";
			clip=AudioSystem.getClip();
			File file=new File(url);
			
			inputStream=AudioSystem.getAudioInputStream(file.getAbsoluteFile());
			
			clip.open(inputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);//check
			clip.start();
			status = "play";
			
			while(true) {
				System.out.println("\n\t\t1. Pause");
				System.out.println("\t\t2. Resume");
				System.out.println("\t\t3. Restart");
				System.out.println("\t\t4. Stop");
			
				
				int choice=scanObj.nextInt();
				playPauseStopAudioChoice(choice, url);
				if(choice==4) {
					System.out.print("\n\t\tDo you want to continue listening?(Y/N) ");
					String listen=scanObj.next();
					if(listen.equalsIgnoreCase("Y")) {
						songMenu(username);
						break;
					}
					else if(listen.equalsIgnoreCase("N")) {
						System.out.print("\n\t\tWe hope you enjoyed your time here. Hope to see you soon.");
						System.exit(0);
					}
					else {
						System.out.print("\n\t\tEnter 3 to restart the current song...");
						continue;
					}
				}
			}
		}
		catch(Exception e) {
			System.out.print("\n\t\tHave a nice day");
		}
		scanObj.close();
	}

	public void playPauseStopAudioChoice(int choice, String url) {
		try {
			switch(choice) {
			case 1:
				this.currentFrame=this.clip.getMicrosecondPosition();
				clip.stop();
				status="paused";
				if(status.equals("paused")) {
					System.out.println("\t\tSong is paused");
					return;
				}
				break;
			case 2:
				if(status.equals("play")) {
					System.out.println("\t\tSong is already playing...");
					return;
				}
				clip.close();
				inputStream=AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
				clip.open(inputStream);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				clip.setMicrosecondPosition(currentFrame);
				clip.start();
				status="play";
				break;
			case 3:
				clip.stop();
				clip.close();
				inputStream=AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
				clip.open(inputStream);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				currentFrame=0L;
				clip.setMicrosecondPosition(0);
				clip.start();
				status="play";
				break;
			case 4:
				currentFrame=0L;
				clip.stop();
				clip.close();
				break;
			
			}
		}
		catch(Exception e) {
			System.out.print("\n\t\tJukeBox abruptly stopped working");
		}
	}
	
	public void searchSongs(List<Song> songList, String username) {
		try {
			Scanner scanObj=new Scanner(System.in);
			System.out.println("\n\t\tEnter your choice to Search:\n\t\t1. Song Name\n\t\t2. Artist\n\t\t3. Genre\n\t\t4. Album");
			int choice=scanObj.nextInt();
			List<Song> list=new ArrayList<Song>();
			switch(choice) {
				case 1:
					System.out.print("\n\t\tEnter Song Name to Search: ");
					String name=scanObj.next();
					list=searchByName(songList, name);
					displaySongs(list, username);
					break;
				case 2:
					System.out.print("\n\t\tEnter Artist to Search: ");
					String artistname=scanObj.next();
					list=searchByArtist(songList,artistname);
					displaySongs(list, username);
					break;
				case 3:
					System.out.print("\n\t\tEnter Genre to Search: ");
					String genrename=scanObj.next();
					list=searchByGenre(songList,genrename);
					displaySongs(list, username);
					break;
				case 4:
					System.out.print("\n\t\tEnter Album to Search: ");
					String albumname=scanObj.next();
					list=searchByAlbum(songList,albumname);
					displaySongs(list, username);
					break;
				default:
					System.out.print("\n\t\tInvalid choice");
			}
			scanObj.close();
		}
		catch(Exception e) {
			System.out.print("\n\t\tJukeBox abruptly stopped working");
		}
	}
	
	public List<Song> searchByName(List<Song> songList, String name) throws SQLException{
		List<Song> list=new ArrayList<Song>();
		
		list=songList.stream().filter(s->s.getSongName().contains(name)).toList();
	
		return list;
	}
	
	public List<Song> searchByArtist(List<Song> songList, String artistname) throws SQLException{
		List<Song> list=new ArrayList<Song>();
		
		list=songList.stream().filter(s->s.getSongArtist().contains(artistname)).toList();
	
		return list;
	}
	
	public List<Song> searchByGenre(List<Song> songList, String genrename) throws SQLException{
		List<Song> list=new ArrayList<Song>();
		
		list=songList.stream().filter(s->s.getSongGenre().contains(genrename)).toList();
	
		return list;
	}
	
	public List<Song> searchByAlbum(List<Song> songList, String albumname) throws SQLException{
		List<Song> list=new ArrayList<Song>();
		
		list=songList.stream().filter(s->s.songAlbum.contains(albumname)).toList();
	
		return list;
	}
	
	public void playSongsPlaylist(List<Song> songList, String username) {
		Scanner scanObj=new Scanner(System.in);
		try {
			for(int i=0;i<songList.size();i++) {
			
				String url="C:\\niit\\Eclipse\\FinalJavaProjectJUKEBOX\\src\\Songs\\"+songList.get(i).getSongID()+".wav";
				Clip clip=AudioSystem.getClip();
				File f=new File(url);
			
				AudioInputStream inputStream=AudioSystem.getAudioInputStream(f.getAbsoluteFile());
				
				clip.open(inputStream);
				clip.loop(0);
				clip.start();
				status="play";
				System.out.print("\n\t\t"+songList.get(i).getSongName()+" is playing...");
				System.out.println("\n\t\t1. Next");
				System.out.println("\t\t2. Prev");
				System.out.println("\t\t3. Restart");
				System.out.println("\t\t4. Stop");

				int choice=scanObj.nextInt();
				while(true) {
					if(clip.isActive()) {
						
				
						if(choice==1) {
							clip.close();
							clip.stop();
							break;
						} else if(choice==2) {
							clip.close();
							clip.stop();
							i=i-2;
							break;
						} else if(choice==3) {
							clip.close();
							clip.stop();
							i=i-1;
							break;
						} else if(choice==4) {
							break;
						}
						
					}
					else {
						clip.close();
						clip.stop();
						break;
					}
				}
				if(choice==4) {
					clip.close();
					clip.stop();
					System.out.print("\n\t\tDo you want to continue listening?(Y/N) ");
					String listen=scanObj.next();
					if(listen.equalsIgnoreCase("Y")) {
						songMenu(username);
						break;
					}
					else if(listen.equalsIgnoreCase("N")) {
						System.out.print("\n\t\tWe hope you enjoyed your time here. Hope to see you soon.");
						System.exit(0);
					}
					
				}
			}
		}
		catch(Exception e) {
			System.out.print("\n\t\tJukebox stopped working...");
		}
		scanObj.close();
	}
	
	
}
