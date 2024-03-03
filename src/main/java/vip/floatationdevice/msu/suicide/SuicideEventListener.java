package vip.floatationdevice.msu.suicide;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static vip.floatationdevice.msu.suicide.SuicidePlugin.i18n;
import static vip.floatationdevice.msu.suicide.SuicidePlugin.instance;

/**
 * If player's death was caused by using "/suicide", change the death message.
 */
public class SuicideEventListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent evt)
    {
        Player p = evt.getEntity();
        if(instance.suicideData.containsKey(p.getUniqueId()) && instance.suicideData.get(p.getUniqueId()))
        {
            evt.setDeathMessage(i18n.translate("suicide-broadcast").replace("{0}", p.getName()));
            instance.suicideData.put(p.getUniqueId(), false);
        }
    }
}
