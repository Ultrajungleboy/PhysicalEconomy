package me.ultrajungleboy.physicaleconomy.betonquest;

import me.ultrajungleboy.physicaleconomy.players.Util;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.bukkit.Bukkit;

public class HasMoneyCondition extends Condition {
    private final int amount;

    public HasMoneyCondition(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        this.amount = instruction.getInt();
    }

    protected Boolean execute(Profile profile) throws QuestRuntimeException {
        return Util.cashOnPlayer(Bukkit.getPlayer(profile.getPlayerUUID())) < this.amount ? false : true;
    }
}
