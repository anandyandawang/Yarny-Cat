package com.sodirea.yarnycat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sodirea.yarnycat.sprites.Cat;
import com.sodirea.yarnycat.states.GameStateManager;
import com.sodirea.yarnycat.states.MenuState;
import com.sodirea.yarnycat.states.PlayState;
import com.sodirea.yarnycat.states.State;

public class YarnyCat extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	public static final String TITLE = "Yarny Cat";

	private GameStateManager gsm;
	private SpriteBatch sb;
	private boolean outOfApp;
	private Music music;
	
	@Override
	public void create () {
		gsm = new GameStateManager(); // Make new gsm upon opening app
		sb = new SpriteBatch();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		outOfApp = false;
		music = Gdx.audio.newMusic(Gdx.files.internal("littleidea.mp3"));
		music.setLooping(true);
		music.setVolume(0.4f);
		music.play();
		gsm.push(new MenuState(gsm)); // Seeing main menu is first thing they should see when opening app
	}

	@Override
	public void render () { // After opening app, this method is constantly run. Having menustate in stack is not enough; also need to render it to the screen. so, constantly clear the screen and render the topmost state (i.e. the menu state in this case)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime(), outOfApp);
		gsm.render(sb);
	}
	
	@Override
	public void dispose () {
		sb.dispose();
		music.dispose();
	}

	@Override
	public void pause() {
		outOfApp = true;
		gsm.update(Gdx.graphics.getDeltaTime(), outOfApp); // call update manually, to let states do what they want with the outOfApp variable before exiting. (if not called here, then PlayState would not check if outOfApp is true (to set paused to true), before exiting the app.. then it would re-enter the app and resume() / set outOfApp to false, thus it never sees outOfApp as true)
		music.pause();
	}

	@Override
	public void resume() {
		outOfApp = false;
		music.play();
	}
}