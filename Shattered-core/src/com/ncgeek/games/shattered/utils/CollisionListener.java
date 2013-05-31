package com.ncgeek.games.shattered.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ncgeek.games.shattered.entities.EntitySprite;

public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();
		
		if(a instanceof EntitySprite && b instanceof EntitySprite) {
			EntitySprite esA = (EntitySprite)a;
			EntitySprite esB = (EntitySprite)b;
			
			esA.setTarget(esB);
			esB.setTarget(esA);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();
		
		if(a instanceof EntitySprite && b instanceof EntitySprite) {
			EntitySprite esA = (EntitySprite)a;
			EntitySprite esB = (EntitySprite)b;
			
			esA.setTarget(null);
			esB.setTarget(null);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
