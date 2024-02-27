package vip.floatationdevice.msu.suicide;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static vip.floatationdevice.msu.suicide.SuicidePlugin.getInstance;

/**
 * Command executor for the "/suicide" command.
 */
public class SuicideCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // skip console
        if(!(sender instanceof Player))
        {
            sender.sendMessage(getInstance().i18n.translate("err-player-only"));
            return false;
        }
        // check usage
        if(args.length != 0)
        {
            sender.sendMessage(getInstance().i18n.translate("usage"));
            return false;
        }
        // check permissions
        if(!sender.hasPermission("suicide.suicide"))
        {
            sender.sendMessage(getInstance().i18n.translate("err-permission-denied"));
            return true;
        }

        Player p = (Player) sender;
        if(p.getHealth() <= 0.0 || getInstance().hasCooldown(p.getUniqueId()))
        {
            // disallow dead players and cooling down players
            p.sendMessage(getInstance().i18n.translate("err-generic"));
        }
        else
        {
            getInstance().addCooldown(p.getUniqueId());
            p.setHealth(0.0);
            p.sendMessage(getInstance().i18n.translate("suicide-success"));
        }

        return true;
    }
}
