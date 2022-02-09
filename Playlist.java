import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Playlist {
	
	public String generatePlayListId(Connection conn) throws SQLException{
		String playlistId="";
		Statement statement=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs=statement.executeQuery("select PlayListId from userplaylist order by PlayListId");
		while(rs.next()) {
			
				playlistId=rs.getString(1);
			
		}
	
		String newPlaylistId="PLS"+Integer.toString(Integer.parseInt(playlistId.substring(3,7))+1);			
		return newPlaylistId;
	}
	
	public void createUserPlaylist(Connection conn, String username) {
		Scanner scanObj=new Scanner(System.in);
		try {
			String playlistID=generatePlayListId(conn);
			PreparedStatement psmt=conn.prepareStatement("insert into userplaylist values(?,?,?)");
			psmt.setString(1, playlistID);
			System.out.print("\n\t\tEnter the name of your new playlist: ");
			String playlistName=scanObj.next();
			psmt.setString(2, playlistName);
			psmt.setString(3, username);
			int row_eff=psmt.executeUpdate();
			psmt.close();
			if(row_eff>0) {
				System.out.print("\n\t\tPlaylist successfully created. Your playlist ID is: "+playlistID);
				insertSongsIntoPlayList(playlistID,conn, username);
			}
			else {
				System.out.print("\n\t\tError in creating playlist");
			}
		}
		catch(Exception e) {
			System.out.print("\n\t\tCannot Create Playlist");
		}
		scanObj.close();
	}
	public void insertSongsIntoPlayList(String playlistID, Connection conn, String username) {
		Scanner scanObj=new Scanner(System.in);
		try {
			do {
				System.out.print("\n\n\t\tDo you want to add Songs in the playlist? (Y/N) ");
				String choice=scanObj.next();
				if(choice.equalsIgnoreCase("Y")) {
					PreparedStatement psmt=conn.prepareStatement("Insert into playlist (PlayListId,SongId,PodId) values(?,?,?)");
					psmt.setString(1, playlistID);
					System.out.print("\n\t\tPlease enter a SongID for the Playlist: ");
					String songID=scanObj.next();
					psmt.setString(2, songID);
					psmt.setString(3, null);
					int row=psmt.executeUpdate();
					if(row>0) {
						System.out.print("\n\n\t\tSong is successfully added.");
					}
					else {
						System.out.print("\n\n\t\tUnable to add songs at this moment");
					}
				}
				else if(choice.equalsIgnoreCase("N")) {
					addPodcastToPlaylist(conn,playlistID,username);
					break;
				}
				else {
					System.out.print("\n\t\tPlease enter either Y or N");
				}
			}while(true);
		}
		catch(Exception e) {
			System.out.print("\n\t\tInvalid song code");
		}
		scanObj.close();
	}
	
	public void addPodcastToPlaylist(Connection conn, String playlistID, String username) {
		Scanner scanObj=new Scanner(System.in);
		Podcast podcast=new Podcast();
		podcast.viewAllPodcast(conn, username);
		try {
			int row=0;
			do {
				System.out.print("\n\n\t\tDo you want to add a Podcast in the playlist? (Y/N) ");
				String choice=scanObj.next();
				if(choice.equalsIgnoreCase("Y")) {
					
					System.out.print("\n\t\tPlease enter a Podcast ID to add in the Playlist: ");
					String podcastID=scanObj.next();
				
					Statement statementObj=conn.createStatement();
					ResultSet rs=statementObj.executeQuery("select SongId from podcastsonglist where PodId='"+podcastID+"'");
					while(rs.next()) {
			
					
					PreparedStatement psmt=conn.prepareStatement("Insert into playlist (PlayListId,SongId,PodId) values(?,?,?)");
					psmt.setString(1, playlistID);
					psmt.setString(2, rs.getString(1));
					psmt.setString(3, podcastID);
					row=psmt.executeUpdate();
					}
					if(row>0) {
						System.out.print("\n\n\t\tPodcast is successfully added.");
					}
					else {
						System.out.print("\n\n\t\tUnable to add songs at this moment");
					}
				
				}
				else if(choice.equalsIgnoreCase("N")) {
					viewSongsInPlaylist(conn,username);
					break;
				}
				else {
					System.out.print("\n\t\tPlease enter either Y or N");
				}
			}while(true);
		}
		catch(Exception e) {
			System.out.print("\n\t\tInvalid song code");
		}
		scanObj.close();
	}
	
	public void viewSongsInPlaylist(Connection conn, String username) {
		Scanner scanObj=new Scanner(System.in);
		try {
			Statement statementObj=conn.createStatement();
			ResultSet rs=statementObj.executeQuery("select playlistid,playlistname from userplaylist where username='"+username+"'");
			System.out.print("\n\t\t Your: ");
			int count=0;
			while(rs.next()) {
				System.out.print("\n\t\t Playlist ID: "+rs.getString(1)+", Playlist Name: "+rs.getString(2));
				count++;
			}
			if(count!=0) {
				System.out.print("\n\n\t\tEnter your Playlist ID to view all the songs: ");
				String playlistId=scanObj.next();
				ArrayList<Song> songList=new ArrayList<Song>();
				songList=(ArrayList<Song>) gettingSongsFromPlaylistID(conn,username,playlistId);
				Song song=new Song();
				song.displaySongs(songList, username);
				
			}
			if(count==0) {
				System.out.print("No Playlist is present at this moment. Please create a playlist before trying again: ");
				createUserPlaylist(conn,username);
			}
		}
		catch(SQLException se) {
			System.out.print("\n\t\tUnable to fetch songs at this moment.");
		}
		scanObj.close();
	}
	
	public List<Song> gettingSongsFromPlaylistID(Connection conn, String username, String playlistId) throws SQLException {
		Statement statementObj=conn.createStatement();
		ResultSet rs2=statementObj.executeQuery("select * from song where songId in(select songId from PlayList  where PlaylistId  in (select playlistId from userplaylist where username='"+username+"' AND playlistId='"+playlistId+"'))");
		
		ArrayList<Song> songList=new ArrayList<Song>();
		while(rs2.next()) {
			songList.add(new Song(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4),rs2.getString(5),rs2.getString(6)));
		}
		return songList;
	}
	
	
}
