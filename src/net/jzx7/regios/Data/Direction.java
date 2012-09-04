package net.jzx7.regios.Data;

import net.jzx7.regiosapi.exceptions.InvalidDirectionException;

import org.bukkit.util.Vector;

public enum Direction {
	WEST(-1, 0, 0),
	NORTH(0, 0, -1),
	EAST(1, 0, 0),
	SOUTH(0, 0, 1),
	UP(0, 1, 0),
	DOWN(0, -1, 0);

	private final int modX;
	private final int modY;
	private final int modZ;

	private Direction(final int modX, final int modY, final int modZ) {
		this.modX = modX;
		this.modY = modY;
		this.modZ = modZ;
	}

	public int getModX() {
		return modX;
	}

	public int getModY() {
		return modY;
	}

	public int getModZ() {
		return modZ;
	}
	
	public Vector getVector() {
		return new Vector(this.getModX(), this.getModY(), this.getModZ());
	}

	public static Direction getDirection(String dirStr) throws InvalidDirectionException {
		final Direction dir;
		switch (dirStr.toLowerCase().charAt(0)) {
		case 'w':
			dir = Direction.WEST;
			break;

		case 'e':
			dir = Direction.EAST;
			break;

		case 's':
			dir = Direction.SOUTH;
			break;

		case 'n':
			dir = Direction.NORTH;
			break;

		case 'u':
			dir = Direction.UP;
			break;

		case 'd':
			dir = Direction.DOWN;
			break;
			
		default:
			throw new InvalidDirectionException(dirStr);
		}
		return dir;
	}
}