package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.tanyelbariser.iceaspirations.entities.Player;
import com.tanyelbariser.iceaspirations.platforms.Platforms;

public class MyContactListener implements ContactListener {
	private Player player;
	private Body body;
	private float angle;
	private boolean touchLeftEdge;
	private boolean touchRightEdge;
	private boolean feetContact;
	private boolean headContact;
	private boolean playerContact;
	private boolean boulderContact;
	private boolean platformContact;
	private boolean groundContact;
	private boolean justBoulder;
	private String fixA;
	private String fixB;

	public MyContactListener(Player player) {
		this.player = player;
		this.body = player.getBody();
	}

	@Override
	public void beginContact(Contact contact) {
		fixA = (String) contact.getFixtureA().getUserData();
		fixB = (String) contact.getFixtureB().getUserData();
		boolean playerContact = fixA.equals("player") || fixB.equals("player");
		boolean clockContact = fixA.equals("clock") || fixB.equals("clock");
		if (playerContact && clockContact) {
			AudioManager.playPickUpSound();
			player.setClockTouched(true);
		}
		boolean carrotContact = fixA.equals("carrot") || fixB.equals("carrot");
		if (playerContact && carrotContact) {
			AudioManager.playPickUpSound();
			player.setCarrotTouched(true);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		touchLeftEdge = Platforms.LEFT_SCREEN_EDGE + 0.6f > body.getPosition().x;
		touchRightEdge = Platforms.RIGHT_SCREEN_EDGE - 0.6f < body
				.getPosition().x;
		feetContact = contact.getWorldManifold().getPoints()[0].y < body
				.getPosition().y;
		headContact = contact.getWorldManifold().getPoints()[0].y > body
				.getPosition().y;
		playerContact = fixA.equals("player") || fixB.equals("player");
		boulderContact = fixA.equals("boulder") || fixB.equals("boulder");
		platformContact = fixA.equals("platform") || fixB.equals("platform");
		groundContact = fixA.equals("ground") || fixB.equals("ground");
		justBoulder = boulderContact && !(platformContact || groundContact);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		if (playerContact && boulderContact && headContact
				&& !player.isCarrotTouched()) {
			player.setDown(-10);
			player.setDazed(true);
		} else if ((touchLeftEdge || touchRightEdge)
				&& !(playerContact && (platformContact || boulderContact))) {
			player.setCanJump(false);
			player.setDown(0);
		} else if (feetContact) {
			if (playerContact && (platformContact || justBoulder)) {
				if (platformContact) {
					// FixtureB is a platform
					angle = contact.getFixtureB().getBody().getAngle();
					player.setAngle(angle);
				} else {
					player.setAngle(0);
				}
				player.setSlippery(angle * 15);
				player.setDown(-1.5f);
				player.setCanJump(true);
				player.setStanding(true);
			} else if (playerContact && groundContact) {
				player.setDown(0);
				player.setAngle(0);
				player.setCanJump(true);
				player.setStanding(true);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		player.setDown(0);
		player.setStanding(false);
		// Delay in resetting slippery causes player to jump off platform at an
		// angular direction perpendicular to the platform & allows smoother
		// sliding on platforms & inertia after.
		Timer.schedule(new Task() {
			@Override
			public void run() {
				player.setSlippery(0);
				player.setCanJump(false);
			}
		}, 0.5f);
	}
}