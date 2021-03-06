

package com.laytonsmith.core.events;

/**
 * This class is an enum class that represents all the types of events that CH is aware of. The
 * reason an enum is required, is because events can more easily be sorted and found this way.
 * @author layton
 */
public enum Driver {
	PLAYER_PRELOGIN,
	PLAYER_LOGIN,
	PLAYER_TELEPORT,
    PLAYER_JOIN,    
    PLAYER_INTERACT, 
    PLAYER_INTERACT_ENTITY,
    PLAYER_SPAWN,     
    PLAYER_DEATH, 
    PLAYER_QUIT,
    PLAYER_CHAT, 
    PLAYER_COMMAND, 
    PLAYER_KICK,
	PLAYER_BED_EVENT,
    WORLD_CHANGED,
    SIGN_CHANGED,
	SERVER_COMMAND,
    BLOCK_BREAK,
    BLOCK_PLACE,
    TARGET_ENTITY, 
    ENTITY_DAMAGE_PLAYER, 
	ENTITY_DAMAGE,
	ENTITY_DEATH,
	ENTITY_EXPLODE,
	CREATURE_SPAWN,
	PLAYER_MOVE,  
	ITEM_PICKUP,
	ITEM_DROP,
	ITEM_SPAWN,
	INVENTORY_CLICK,
	INVENTORY_DRAG,
	INVENTORY_OPEN,
	INVENTORY_CLOSE,
	PLAYER_CONSUME,
	PROJECTILE_HIT,
	POTION_SPLASH,
	PLAYER_FISH,
	SERVER_PING,
	ENTITY_ENTER_PORTAL,
	PLUGIN_MESSAGE_RECEIVED,
	VEHICLE_ENTER,
	VEHICLE_LEAVE,
	VEHICLE_COLLIDE,
	PLAYER_PORTAL_TRAVEL,
	GAMEMODE_CHANGE,
	TAB_COMPLETE,
	/**
	 * Used by events fired from the extension system.
	 */
	EXTENSION
}
