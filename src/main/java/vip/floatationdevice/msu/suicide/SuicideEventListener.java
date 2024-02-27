package vip.floatationdevice.msu.suicide;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static vip.floatationdevice.msu.I18nUtil.translate;

/**
 * If player's death was caused by using "/suicide", change the death message.
 */
public class SuicideEventListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent evt)
    {
        Player p = evt.getEntity();
        if(SuicidePlugin.getInstance().suicideData.containsKey(p.getUniqueId()) && SuicidePlugin.getInstance().suicideData.get(p.getUniqueId()))
        {
            evt.setDeathMessage(translate("suicide-broadcast").replace("{0}", p.getName()));
            SuicidePlugin.getInstance().suicideData.put(p.getUniqueId(), false);
        }
    }
}
