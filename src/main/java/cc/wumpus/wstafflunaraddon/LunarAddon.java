package cc.wumpus.wstafflunaraddon;

import cc.wumpus.wstaff.api.StaffmodeStateChangeEvent;
import cc.wumpus.wstaff.api.wStaffAPI;
import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.event.ApolloListener;
import com.lunarclient.apollo.module.staffmod.StaffModModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;

public class LunarAddon extends JavaPlugin implements Listener, ApolloListener {

    private static LunarAddon instance;
    private static wStaffAPI staffAPI;

    @Override
    public void onEnable() {
        instance = this;

        if (Bukkit.getPluginManager().getPlugin("wStaff") == null) {
            WumUtil.tellConsole(" &e&l|> &awStaff &7was &cnot found&7.");
            Bukkit.getPluginManager().disablePlugin(instance);
        }

        staffAPI = wStaffAPI.getInstance();
        WumUtil.tellConsole(" &e&l|> &awStaff &7was &afound &7and has been hooked.");

        Bukkit.getPluginManager().registerEvents(instance, instance);

        WumUtil.tellConsole(" &e&l|> &7" + getDescription().getName() + " has been enabled.");
    }

    @Override
    public void onDisable() {
        WumUtil.tellConsole(" &e&l|> &7" + getDescription().getName() + " has been disabled.");
    }

    @EventHandler
    public void onStaffmodeToggle(StaffmodeStateChangeEvent event) {
        if (!Apollo.getPlayerManager().hasSupport(event.getStaffPlayer().getUniqueId())) return;

        BukkitTask run = new BukkitRunnable() {
            @Override
            public void run() {
                if (getStaffAPI().isStaffmode(event.getStaffPlayer())) {
                    enableStaffMods(event.getStaffPlayer());
                    return;
                }
                disableStaffMods(event.getStaffPlayer());

            }
        }.runTaskLater(instance, 5L);
    }

    @EventHandler
    public void onStaffJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        BukkitTask run = new BukkitRunnable() {
            @Override
            public void run() {
                if (getStaffAPI().isStaffmode(player)) {
                    enableStaffMods(player);
                    return;
                }
                disableStaffMods(player);

            }
        }.runTaskLater(instance, 5L);
    }

    public void enableStaffMods(Player viewer) {
        Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(viewer.getUniqueId());
        apolloPlayerOpt.ifPresent(apolloPlayer -> Apollo.getModuleManager().getModule(StaffModModule.class).enableAllStaffMods(apolloPlayer));
    }

    public void disableStaffMods(Player viewer) {
        Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(viewer.getUniqueId());
        apolloPlayerOpt.ifPresent(apolloPlayer -> Apollo.getModuleManager().getModule(StaffModModule.class).disableAllStaffMods(apolloPlayer));
    }

    public static wStaffAPI getStaffAPI() {
        return staffAPI;
    }
    public static LunarAddon getInstance() {
        return instance;
    }

}
