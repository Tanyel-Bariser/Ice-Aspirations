package com.tanyelbariser.iceaspirations;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.tanyelbariser.iceaspirations.entities.Player;
import com.tanyelbariser.iceaspirations.platforms.Platforms;

@Getter
@Setter
public class CollisionDetection implements ContactListener {
	private Body body;
	private boolean clockTouched;
	private boolean carrotTouched;
	private float down;
	private boolean dazed;
	private boolean canJump;
	private float angle;
	private float slippery;
	private boolean standing;

	public CollisionDetection(Player player) {
		player.setCollisionDetection(this);
		this.body = player.getBody();
	}

	@Override
	public void beginContact(Contact contact) {
		String fixA = (String) contact.getFixtureA().getUserData();
		String fixB = (String) contact.getFixtureB().getUserData();
		boolean playerContact = fixA.equals("player") || fixB.equals("player");
		boolean clockContact = fixA.equals("clock") || fixB.equals("clock");
		if (playerContact && clockContact) {
			AudioManager.playPickUpSound();
			clockTouched = true;
		}
		boolean carrotContact = fixA.equals("carrot") || fixB.equals("carrot");
		if (playerContact && carrotContact) {
			AudioManager.playPickUpSound();
			carrotTouched = true;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		boolean touchLeftEdge = Platforms.LEFT_SCREEN_EDGE + 0.6f > body
				.getPosition().x;
		boolean touchRightEdge = Platforms.RIGHT_SCREEN_EDGE - 0.6f < body
				.getPosition().x;
		boolean feetContact = contact.getWorldManifold().getPoints()[0].y < body
				.getPosition().y;
		boolean headContact = contact.getWorldManifold().getPoints()[0].y > body
				.getPosition().y;
		String fixA = (String) contact.getFixtureA().getUserData();
		String fixB = (String) contact.getFixtureB().getUserData();
		boolean playerContact = fixA.equals("player") || fixB.equals("player");
		boolean boulderContact = fixA.equals("boulder")
				|| fixB.equals("boulder");
		boolean platformContact = fixA.equals("platform")
				|| fixB.equals("platform");
		boolean groundContact = fixA.equals("ground") || fixB.equals("ground");
		boolean justBoulder = boulderContact
				&& !(platformContact || groundContact);
		if (playerContact && boulderContact && headContact && !carrotTouched) {
			down = -10;
			dazed = true;
		} else if ((touchLeftEdge || touchRightEdge)
				&& !(playerContact && (platformContact || boulderContact))) {
			canJump = false;
			down = 0;
		} else if (feetContact) {
			if (playerContact && (platformContact || justBoulder)) {
				if (platformContact) {
					// FixtureB is a platform
					angle = contact.getFixtureB().getBody().getAngle();
				} else {
					angle = 0;
				}
				slippery = angle * 15;
				down = -1.5f;
				canJump = standing = true;
			} else if (playerContact && groundContact) {
				down = angle = slippery = 0;
				canJump = standing = true;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		down = 0;
		standing = false;
		// Delay in resetting slippery causes player to jump off platform at an
		// angle perpendicular to the platform & allows smoother sliding on
		// platforms & continued movement in the same direction after contact.
		Timer.schedule(new Task() {
			@Override
			public void run() {
				slippery = 0;
				canJump = false;
			}
		}, 0.5f);
	}
}