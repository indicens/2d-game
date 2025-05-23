package utils;

public interface Constants {

	public static class Direction {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
		public static final int UP = 2;
		public static final int DOWN = 3;
	}

	public static class PlayerConstants {

		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;

		public static int GetSpriteAmount(int player_action) {

			switch (player_action) {
			case IDLE:
				return 0;
			case RUNNING:
				return 1;
			case JUMP:
				return 2;
			default:
				return 3;
			}

		}

	}
}
