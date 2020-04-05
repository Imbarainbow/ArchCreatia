package studio.robotmonkey.archcreatia.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.HashSet;

public class DirectionHelper {
    public static BlockPos getRightPos(Direction direction, BlockPos blockPos) {
        BlockPos newPos = new BlockPos(blockPos);
        if(direction == Direction.NORTH) {
            newPos = newPos.offset(Direction.WEST);
            return newPos;
        } else if(direction == Direction.SOUTH) {
            newPos = newPos.offset(Direction.EAST);
            return newPos;
        } else if(direction == Direction.EAST) {
            newPos = newPos.offset(Direction.NORTH);
            return newPos;
        } else if(direction == Direction.WEST) {
            newPos = newPos.offset(Direction.SOUTH);
            return newPos;
        } else {
            return blockPos;
        }
    }

    public static BlockPos getUpPos(Direction direction, BlockPos blockPos) {
        BlockPos newPos = new BlockPos(blockPos);
        if(direction == Direction.NORTH) {
            newPos = newPos.offset(Direction.SOUTH);
            return newPos;
        } else if(direction == Direction.SOUTH) {
            newPos = newPos.offset(Direction.NORTH);
            return newPos;
        } else if(direction == Direction.EAST) {
            newPos = newPos.offset(Direction.WEST);
            return newPos;
        } else if(direction == Direction.WEST) {
            newPos = newPos.offset(Direction.EAST);
            return newPos;
        } else {
            return blockPos;
        }
    }

    public static BlockPos getLeftPos(Direction direction, BlockPos blockPos) {
        BlockPos newPos = new BlockPos(blockPos);
        if(direction == Direction.NORTH) {
            newPos = newPos.offset(Direction.EAST);
            return newPos;
        } else if(direction == Direction.SOUTH) {
            newPos = newPos.offset(Direction.WEST);
            return newPos;
        } else if(direction == Direction.EAST) {
            newPos = newPos.offset(Direction.SOUTH);
            return newPos;
        } else if(direction == Direction.WEST) {
            newPos = newPos.offset(Direction.NORTH);
            return newPos;
        } else {
            return blockPos;
        }
    }

    public static BlockPos getDownPos(Direction direction, BlockPos blockPos) {
        BlockPos newPos = new BlockPos(blockPos);
        if(direction == Direction.NORTH) {
            newPos = newPos.offset(Direction.NORTH);
            return newPos;
        } else if(direction == Direction.SOUTH) {
            newPos = newPos.offset(Direction.SOUTH);
            return newPos;
        } else if(direction == Direction.EAST) {
            newPos = newPos.offset(Direction.EAST);
            return newPos;
        } else if(direction == Direction.WEST) {
            newPos = newPos.offset(Direction.WEST);
            return newPos;
        } else {
            return blockPos;
        }
    }

    public static HashMap<String, BlockPos> getInputSides(Direction direction, BlockPos blockPos) {
        HashMap<String, BlockPos> inputs = new HashMap<>();
        inputs.put("up", getUpPos(direction, blockPos));
        inputs.put("left", getLeftPos(direction, blockPos));
        inputs.put("down", getDownPos(direction, blockPos));

        return inputs;
    }
}
