package com.example.hzm17.ichack18;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;


public class MainActivity extends AppCompatActivity implements SpotifyPlayer.NotificationCallback,
    ConnectionStateCallback{
  // TODO: Replace with your client ID
  private static final String CLIENT_ID = "ed470f7bea2845c2a3e5f23830dbba95";
  // TODO: Replace with your redirect URI
  private static final String REDIRECT_URI = "myAppICHACK://callback";

  private Player mPlayer;
  private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

      AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
          AuthenticationResponse.Type.TOKEN,
          REDIRECT_URI);
      builder.setScopes(new String[]{"user-read-private", "streaming"});
      AuthenticationRequest request = builder.build();

      AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

       /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

    // Check if result comes from the correct activity
    if (requestCode == REQUEST_CODE) {
      AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
      if (response.getType() == AuthenticationResponse.Type.TOKEN) {
        Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
        Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
          @Override
          public void onInitialized(SpotifyPlayer spotifyPlayer) {
            mPlayer = spotifyPlayer;
            mPlayer.addConnectionStateCallback(MainActivity.this);
            mPlayer.addNotificationCallback(MainActivity.this);
          }

          @Override
          public void onError(Throwable throwable) {
            Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
          }
        });
      }
    }
  }

  public void onPlaybackEvent(PlayerEvent playerEvent) {
    Log.d("MainActivity", "Playback event received: " + playerEvent.name());
    switch (playerEvent) {
      // Handle event type as necessary
      default:
        break;
    }
  }

  @Override
  public void onPlaybackError(Error error) {

  }

  @Override
  public void onLoggedIn() {
    Log.d("MainActivity", "User logged in");

    mPlayer.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
  }

  public void onLoggedOut() {
    Log.d("MainActivity", "User logged out");
  }

  @Override
  public void onLoginFailed(Error error) {

  }
  @Override
  protected void onDestroy() {
    Spotify.destroyPlayer(this);
    super.onDestroy();
  }

  public void onLoginFailed(int i) {
    Log.d("MainActivity", "Login failed");
  }

  public void onTemporaryError() {
    Log.d("MainActivity", "Temporary error occurred");
  }

  public void onConnectionMessage(String message) {
    Log.d("MainActivity", "Received connection message: " + message);
  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
