package vip.floatationdevice.msu.suicide;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import vip.floatationdevice.msu.ConfigManager;
import vip.floatationdevice.msu.I18nManager;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class SuicidePlugin extends JavaPlugin
{
    private static SuicidePlugin instance;
    ConfigManager cm;
    I18nManager i18n;
    Logger log;
    HashMap<UUID, Boolean> suicideData = new HashMap<>();

    public static SuicidePlugin getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        instance = this;
        log = getLogger();

        // initialize config manager
        cm = new ConfigManager(this, 1).initialize();

        // initialize translation engine
        i18n = new I18nManager(this).setLanguage(cm.get(String.class, "language"));
        log.info("Language: " + i18n.getLanguage() + " by " + i18n.getLanguageFileContributor());

        // register command and event listener
        getCommand("suicide").setExecutor(new SuicideCommand());
        getServer().getPluginManager().registerEvents(new SuicideEventListener(), this);

        log.info("Suicide has been loaded");
    }

    @Override
    public void onDisable()
    {
        log.info("Suicide is being disabled");
        Bukkit.getScheduler().cancelTasks(this); // remove all remaining cooldowns
    }

    /**
     * Check if a player has suicide cooldown.
     * @param u The UUID of the player.
     * @return true if so, false otherwise.
     */
    public boolean hasCooldown(UUID u)
    {
        return suicideData.containsKey(u);
    }

    /**
     * Add a cooldown to the specified player.
     * The cooldown will be removed automatically after the specific time passed.
     * @param u The UUID of the player.
     */
    public void addCooldown(UUID u)
    {
        if(hasCooldown(u))
        {
            log.warning("Trying to add cooldown for player " + getServer().getPlayer(u) + getName() +
                    " but cooldown already exists. This should not happen, probably a bug in the plugin"
            );
            return;
        }
        suicideData.put(u, true);
        Bukkit.getScheduler().runTaskLater(this, new Runnable()
        {
            @Override
            public void run()
            {
                instance.removeCooldown(u);
            }
        }, 20L * cm.get(Integer.class, "cooldown"));
    }

    /**
     * Remove the cooldown from the specified player.
     * @param u The UUID of the player.
     */
    public void removeCooldown(UUID u)
    {
        if(!hasCooldown(u))
        {
            log.warning("Trying to remove cooldown for player " + getServer().getPlayer(u) + getName() +
                    " but cooldown doesn't exist. This should not happen, probably a bug in the plugin"
            );
            return;
        }
        suicideData.remove(u);
    }
}
