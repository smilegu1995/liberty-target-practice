package io.openliberty.sentry.demo.model.game.stat;

public class GameStat implements Comparable<GameStat>{
	
	private int game_id;
	private int score;
	private String user_id;
	private int rank;
	
	
	public int getGame_id() {
		return game_id;
	}


	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	@Override
	public int compareTo(GameStat o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
