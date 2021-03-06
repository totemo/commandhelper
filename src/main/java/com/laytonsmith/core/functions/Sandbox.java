package com.laytonsmith.core.functions;

import com.laytonsmith.abstraction.*;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.*;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.events.BoundEvent;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;
import org.bukkit.event.Cancellable;

/**
 * @author Layton
 */
public class Sandbox {

    public static String docs() {
        return "This class is for functions that are experimental. They don't actually get added"
                + " to the documentation, and are subject to removal at any point in time, nor are they"
                + " likely to have good documentation.";
    }

    //This broke as of 1.1
//    @api
//    public static class plugin_cmd extends AbstractFunction {
//
//        public String getName() {
//            return "plugin_cmd";
//        }
//
//        public Integer[] numArgs() {
//            return new Integer[]{2};
//        }
//
//        public String docs() {
//            return "void {plugin, cmd} ";
//        }
//
//        public ExceptionType[] thrown() {
//            return null;
//        }
//
//        public boolean isRestricted() {
//            return true;
//        }
//
//        //
//        public boolean preResolveVariables() {
//            return true;
//        }
//
//        public CHVersion since() {
//            return "0.0.0";
//        }
//
//        public Boolean runAsync() {
//            return false;
//        }
//
//        public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException {
//            Object o = AliasCore.parent.getServer().getPluginManager();
//            if (o instanceof SimplePluginManager) {
//                SimplePluginManager spm = (SimplePluginManager) o;
//                try {
//                    Method m = spm.getClass().getDeclaredMethod("getEventListeners", Event.Type.class);
//                    m.setAccessible(true);
//                    SortedSet<RegisteredListener> sl = (SortedSet<RegisteredListener>) m.invoke(spm, Event.Type.SERVER_COMMAND);
//                    for(RegisteredListener l : sl){
//                        if (l.getPlugin().getDescription().getName().equalsIgnoreCase(args[0].val())) {
//                            if(env.GetCommandSender() instanceof ConsoleCommandSender){
//                                l.callEvent(new ServerCommandEvent((ConsoleCommandSender)env.GetCommandSender(), args[1].val()));
//                            }
//                        }
//                    }
//                    SortedSet<RegisteredListener> ss = (SortedSet<RegisteredListener>) m.invoke(spm, Event.Type.PLAYER_COMMAND_PREPROCESS);
//
//                    for (RegisteredListener l : ss) {
//                        if (l.getPlugin().getDescription().getName().equalsIgnoreCase(args[0].val())) {
//                            if(env.GetCommandSender() instanceof MCPlayer){
//                                l.callEvent(new PlayerCommandPreprocessEvent(((BukkitMCPlayer)env.GetPlayer())._Player(), args[1].val()));
//                            }
//                            PluginCommand.class.getDeclaredMethods();
//                            Constructor c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
//                            c.setAccessible(true);
//                            List<String> argList = Arrays.asList(args[1].val().split(" "));
//                            Command com = (Command) c.newInstance(argList.get(0).substring(1), l.getPlugin());
//                            l.getPlugin().onCommand(((BukkitMCCommandSender)env.GetCommandSender())._CommandSender(), com, argList.get(0).substring(1), argList.subList(1, argList.size()).toArray(new String[]{}));
//                        }
//                    }
//                } catch (InstantiationException ex) {
//                    Logger.getLogger(Sandbox.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IllegalAccessException ex) {
//                    Logger.getLogger(Sandbox.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IllegalArgumentException ex) {
//                    Logger.getLogger(Sandbox.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (InvocationTargetException ex) {
//                    Logger.getLogger(Sandbox.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (NoSuchMethodException ex) {
//                    Logger.getLogger(Sandbox.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (SecurityException ex) {
//                    Logger.getLogger(Sandbox.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//            return new CVoid(t);
//        }
//    }
    @api(environments={CommandHelperEnvironment.class})
    public static class drop_item extends AbstractFunction {

        public String getName() {
            return "drop_item";
        }

        public Integer[] numArgs() {
            return new Integer[]{1, 2, 3};
        }

        public String docs() {
            return "int {[player/LocationArray], item} Drops the specified item at the specified quantity at the specified player's feet (or "
                    + " at an arbitrary Location, if an array is given),"
                    + " like the vanilla /give command. player defaults to the current player, and qty defaults to 1. item follows the"
                    + " same type[:data] format used elsewhere. Returns the entity id of the item stack dropped.";
        }

        public ExceptionType[] thrown() {
            return new ExceptionType[]{ExceptionType.CastException, ExceptionType.FormatException, ExceptionType.PlayerOfflineException};
        }

        public boolean isRestricted() {
            return true;
        }
        public CHVersion since() {
            return CHVersion.V3_2_0;
        }

        public Boolean runAsync() {
            return false;
        }

        public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException {

            MCLocation l = null;
            int qty = 1;
            MCItemStack is = null;
			MCItem item = null;
            boolean natural = false;
            if (env.getEnv(CommandHelperEnvironment.class).GetCommandSender() instanceof MCPlayer) {
                l = env.getEnv(CommandHelperEnvironment.class).GetPlayer().getEyeLocation();
            }
			if(args.length == 1){
				is = ObjectGenerator.GetGenerator().item(args[0], t);
				natural = true;
			} else if(args.length == 2){
				if(args[0] instanceof CString){
					l = Static.GetPlayer(args[0], t).getEyeLocation();
					natural = false;
				} else {
					l = ObjectGenerator.GetGenerator().location(args[0], l==null?null:l.getWorld(), t);
					natural = true;
				}
				is = ObjectGenerator.GetGenerator().item(args[1], t);
			}
//            if (args.length == 1) {
//                //It is just the item
//                is = Static.ParseItemNotation(this.getName(), args[0].val(), qty, t);
//                natural = true;
//            } else if (args.length == 2) {
//                //If args[0] starts with a number, it's the (item, qty) version, otherwise it's
//                //(player, item)
//                if (args[0].val().matches("\\d.*")) {
//                    qty = Static.getInt32(args[1], t);
//                    is = Static.ParseItemNotation(this.getName(), args[0].val(), qty, t);
//                    natural = true;
//                } else {
//                    if (args[0] instanceof CArray) {
//                        l = ObjectGenerator.GetGenerator().location(args[0], ( l != null ? l.getWorld() : null ), t);
//                        natural = false;
//                    } else {
//                        l = Static.GetPlayer(args[0].val(), t).getLocation();
//                        natural = true;
//                    }
//                    is = Static.ParseItemNotation(this.getName(), args[1].val(), qty, t);
//
//                }
//            } else if (args.length == 3) {
//                //We are specifying all 3
//                if (args[0] instanceof CArray) {
//                    l = ObjectGenerator.GetGenerator().location(args[0], ( l != null ? l.getWorld() : null ), t);
//                    natural = false;
//                } else {
//                    l = Static.GetPlayer(args[0].val(), t).getLocation();
//                    natural = true;
//                }
//                qty = Static.getInt32(args[2], t);
//                is = Static.ParseItemNotation(this.getName(), args[1].val(), qty, t);
//            }
            if (l.getWorld() != null) {
                if (natural) {
                    item = l.getWorld().dropItemNaturally(l, is);
                } else {
                    item = l.getWorld().dropItem(l, is);
                }
            } else {
                throw new ConfigRuntimeException("World was not specified", ExceptionType.InvalidWorldException, t);
            }

			if(item != null){
				return new CInt(item.getEntityId(), t);
			}
            return new CNull(t);
        }
    }    

    @api(environments={CommandHelperEnvironment.class})
    public static class super_cancel extends AbstractFunction {

        public String getName() {
            return "super_cancel";
        }

        public Integer[] numArgs() {
            return new Integer[]{0};
        }

        public String docs() {
            return "void {} \"Super Cancels\" an event. This only will work if play-dirty is set to true. If an event is"
                    + " super cancelled, not only is the cancelled flag set to true, the event stops propagating down, so"
                    + " no other plugins (as in other server plugins, not just CH scripts) will receive the event at all "
                    + " (other than monitor level plugins). This is useful for overridding"
                    + " event handlers for plugins that don't respect the cancelled flag. This function hooks into the play-dirty"
                    + " framework that injects custom event handlers into bukkit.";
        }

        public ExceptionType[] thrown() {
            return new ExceptionType[]{ExceptionType.BindException};
        }

        public boolean isRestricted() {
            return true;
        }
        public CHVersion since() {
            return CHVersion.V3_3_0;
        }

        public Boolean runAsync() {
            return null;
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            BoundEvent.ActiveEvent original = environment.getEnv(CommandHelperEnvironment.class).GetEvent();
            if (original == null) {
                throw new ConfigRuntimeException("is_cancelled cannot be called outside an event handler", ExceptionType.BindException, t);
            }
            if (original.getUnderlyingEvent() != null && original.getUnderlyingEvent() instanceof Cancellable
                    && original.getUnderlyingEvent() instanceof org.bukkit.event.Event) {
                ( (Cancellable) original.getUnderlyingEvent() ).setCancelled(true);
                BukkitDirtyRegisteredListener.setCancelled((org.bukkit.event.Event) original.getUnderlyingEvent());
            }
            environment.getEnv(CommandHelperEnvironment.class).GetEvent().setCancelled(true);
            return new CVoid(t);
        }
    }

    @api(environments={CommandHelperEnvironment.class})
    public static class enchant_inv_unsafe extends AbstractFunction {

        public String getName() {
            return "enchant_inv_unsafe";
        }

        public Integer[] numArgs() {
            return new Integer[]{3, 4};
        }

        public String docs() {
            return "void {[player], slot, type, level} Works the same as enchant_inv, except anything goes. "
                    + " You can enchant a fish with a level 5000 enchantment if you wish. Side effects"
                    + " may include nausia, dry mouth, insomnia, or server crashes. (Seriously, this might"
                    + " crash your server, be careful with it.)";
        }

        public ExceptionType[] thrown() {
            return new ExceptionType[]{ExceptionType.CastException, ExceptionType.EnchantmentException, ExceptionType.PlayerOfflineException};
        }

        public boolean isRestricted() {
            return true;
        }
        public CHVersion since() {
            return CHVersion.V0_0_0;
        }

        public Boolean runAsync() {
            return false;
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            MCPlayer m = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
            int offset = 1;
            if (args.length == 4) {
                m = Static.GetPlayer(args[0].val(), t);
                offset = 0;
            }
            MCItemStack is = null;
            if (args[1 - offset] instanceof CNull) {
                is = m.getItemInHand();
            } else {
                int slot = Static.getInt32(args[1 - offset], t);
                is = m.getInventory().getItem(slot);
            }
            CArray enchantArray = new CArray(t);
            if (!( args[2 - offset] instanceof CArray )) {
                enchantArray.push(args[2 - offset]);
            } else {
                enchantArray = (CArray) args[2 - offset];
            }

            CArray levelArray = new CArray(t);
            if (!( args[3 - offset] instanceof CArray )) {
                levelArray.push(args[3 - offset]);
            } else {
                levelArray = (CArray) args[3 - offset];
            }
            for (String key : enchantArray.keySet()) {
                MCEnchantment e = StaticLayer.GetEnchantmentByName(Enchantments.ConvertName(enchantArray.get(key, t).val()).toUpperCase());
                if (e == null) {
                    throw new ConfigRuntimeException(enchantArray.get(key, t).val().toUpperCase() + " is not a valid enchantment type", ExceptionType.EnchantmentException, t);
                }
                int level = Static.getInt32(new CString(Enchantments.ConvertLevel(levelArray.get(key, t).val()), t), t);

                is.addUnsafeEnchantment(e, level);
            }
            return new CVoid(t);
        }
    }

    @api(environments={CommandHelperEnvironment.class})
    public static class raw_set_pvanish extends AbstractFunction {

        public String getName() {
            return "raw_set_pvanish";
        }

        public Integer[] numArgs() {
            return new Integer[]{2, 3};
        }

        public String docs() {
            return "void {[player], isVanished, otherPlayer} Sets the visibility"
                    + " of the current player (or the one specified) to visible or invisible"
                    + " (based on the value of isVanished) from the view of the otherPlayer."
                    + " This is the raw access function, you probably shouldn't use this, as"
                    + " the CommandHelper vanish api functions will probably be easier to use.";
        }

        public ExceptionType[] thrown() {
            return new ExceptionType[]{ExceptionType.PlayerOfflineException};
        }

        public boolean isRestricted() {
            return true; //lol, very
        }
        public Boolean runAsync() {
            return false;
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            MCPlayer me;
            boolean isVanished;
            MCPlayer other;
            if (args.length == 2) {
                me = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
                isVanished = Static.getBoolean(args[0]);
                other = Static.GetPlayer(args[1], t);
            } else {
                me = Static.GetPlayer(args[0], t);
                isVanished = Static.getBoolean(args[1]);
                other = Static.GetPlayer(args[2], t);
            }

            other.setVanished(isVanished, me);

            return new CVoid(t);
        }

        public CHVersion since() {
            return CHVersion.V3_3_0;
        }
    }

    @api(environments={CommandHelperEnvironment.class})
    public static class raw_pcan_see extends AbstractFunction {

        public String getName() {
            return "raw_pcan_see";
        }

        public Integer[] numArgs() {
            return new Integer[]{1, 2};
        }

        public String docs() {
            return "boolean {[player], other} Returns a boolean stating if the other player can"
                    + " see this player or not. This is the raw access function, you probably shouldn't use this, as"
                    + " the CommandHelper vanish api functions will probably be easier to use.";
        }

        public ExceptionType[] thrown() {
            return new ExceptionType[]{ExceptionType.PlayerOfflineException};
        }

        public boolean isRestricted() {
            return true;
        }
        public Boolean runAsync() {
            return false;
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            MCPlayer me;
            MCPlayer other;
            if (args.length == 1) {
                me = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
                other = Static.GetPlayer(args[0], t);
            } else {
                me = Static.GetPlayer(args[0], t);
                other = Static.GetPlayer(args[1], t);
            }
            return new CBoolean(me.canSee(other), t);
        }

        public CHVersion since() {
            return CHVersion.V3_3_0;
        }
    }
	
	@api public static class moo extends DummyFunction{

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		
		public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
			String saying = args[0].val();
			String divider = "";
			for(int i = 0; i < saying.length() + 4; i++){
				divider += "-";
			}
			return new CString(divider + "\n"
				+ "| " + saying + " |\n" 
				+ divider + "\n"
				+ " \\   ^__^\n"
				+ "  \\  (oo)\\_______\n"
				+ "     (__)\\       )\\/\\\n"
				+ "         ||----w |\n"
				+ "         ||     ||\n", t);
		}
		
	}
}
