package io.github.sanchert.namelessgf;

enum GameDifficulty {
    //    EASY(2),
//    MEDIUM(3),
    HARDCORE(4);

    private final int enemyCount;

    GameDifficulty(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int mode() {
        return enemyCount;
    }
}
