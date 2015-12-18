package in.newzbyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class Custome_YouTubePlayerActivity extends YouTubeBaseActivity implements OnInitializedListener {

	private YouTubePlayer player;
	private String api_key = "AIzaSyAbYhYtq67dzVmX65Muq7mMCSH2W5zU2dM";
	
	public static String videoKey = "2zNSgSzhBfM";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youtube_player);
		
		YouTubePlayerView playerview = (YouTubePlayerView)findViewById(R.id.yplayer);
		playerview.initialize(api_key, this);
	}

	@Override
	public void onInitializationFailure(Provider arg0,YouTubeInitializationResult error) {
		// TODO Auto-generated method stub
		Log.d("jaspal",error.toString());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1)
		{
			YouTubePlayerView playerview = (YouTubePlayerView)findViewById(R.id.yplayer);
			playerview.initialize(api_key, this);
		}
			
	}
	

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,boolean wasRestored) {
		// TODO Auto-generated method stub
		this.player= player;
		
		if (!wasRestored) {
			this.player.cueVideo(videoKey);
		}
	}
	
	
}
