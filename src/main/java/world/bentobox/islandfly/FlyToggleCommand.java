package world.bentobox.islandfly;

import org.bukkit.entity.Player;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.IslandsManager;

import java.util.List;
import java.util.Optional;

public class FlyToggleCommand extends CompositeCommand {

    public FlyToggleCommand(final CompositeCommand baseCmd) {
        super(baseCmd, "fly");
    }

    @Override
    public void setup() {
        this.setPermission("fly");
        this.setOnlyPlayer(true);
        this.setDescription("islandfly.command.description");
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        // Allow this command only on island
        final IslandsManager islands = this.getIslands();
        islands.userIsOnIsland(user.getWorld(), user);
        final Optional<Island> island = islands.getIslandAt(user.getLocation());
        if((!island.isPresent() || !island.get().getMembers().containsKey(user.getUniqueId())) && !user.hasPermission("islandfly.bypass")){
            user.sendMessage("islandfly.command.only-on-island");
            return true;
        }
        final Player player = user.getPlayer();
        if(user.getPlayer().getAllowFlight()){
            // Disable fly and notify player
            player.setFlying(false);
            player.setAllowFlight(false);
            user.sendMessage("islandfly.disable-fly");
        }else{
            // Enable fly and notify player
            player.setAllowFlight(true);
            user.sendMessage("islandfly.enable-fly");
        }
        return true;
    }
}
