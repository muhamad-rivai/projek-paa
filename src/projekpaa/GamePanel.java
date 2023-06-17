package projekpaa;

import java.awt.Color;
import java.util.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Muhamad Rivai
 */
public class GamePanel extends JPanel implements ActionListener{
    
    private final int row = 10;
    private final int col = 10;
    private final int[][] maze = new int[row][col];
    private final int cellSize = 40;
    private final int offsetX = 20;
    private final int offsetY = 20;
    private JButton randomizeMap;
    private JButton randomizeRedDroid;
    private JButton randomizeGreenDroid;
    private JButton startMove;
    private JButton stopMove;
    private JButton addRedDroid;
    private JButton redDroidView;
    private JButton greenDroidView;
    private Droid redDroid;
    private Droid greenDroid;
    private ArrayList<Droid> redDroids = new ArrayList<>();
    private boolean resetRedDroids;
    private boolean showRedDroidView = true;
    private boolean showGreenDroidView = false;
    private JButton exitGame;
    private JSlider greenDroidSightSlider;
    private int greenDroidSightRadius = 1;
    
    private Timer timer;
    private boolean gameOver = false;
    
    
    public GamePanel(){
        init();
        startGame();
    }
    
    private void init(){
        setPreferredSize(new Dimension(800,800));
        setLayout(null);
        setBackground(Color.decode("#9EDBA2"));
        
        randomizeMap = new JButton("Acak Peta");
        randomizeMap.setBounds(600, 20, 250, 35);
        randomizeMap.addActionListener(this);
        
        randomizeRedDroid = new JButton("Acak Droid Merah");
        randomizeRedDroid.setBounds(600, 65, 250, 35);
        randomizeRedDroid.addActionListener(this);
        
        randomizeGreenDroid = new JButton("Acak Droid Hijau");
        randomizeGreenDroid.setBounds(600, 110, 250, 35);
        randomizeGreenDroid.addActionListener(this);
        
        startMove = new JButton("Mulai Pergerakan");
        startMove.setBounds(600, 155, 250, 35);
        startMove.addActionListener(this);
        
        stopMove = new JButton("Hentikan Pergerakan");
        stopMove.setBounds(600, 200, 250, 35);
        stopMove.addActionListener(this);

        addRedDroid = new JButton("Tambah Droid Merah");
        addRedDroid.setBounds(600, 245, 250, 35);
        addRedDroid.addActionListener(this);

        redDroidView = new JButton("Pandangan Droid Merah");
        redDroidView.setBounds(600, 335, 250, 35);
        redDroidView.addActionListener(this);

        greenDroidView = new JButton("Pandangan Droid Hijau");
        greenDroidView.setBounds(600, 380, 250, 35);
        greenDroidView.addActionListener(this);

        exitGame = new JButton("Keluar");
        exitGame.setBounds(600, 425, 250, 35);
        exitGame.addActionListener(this);

        greenDroidSightSlider = new JSlider(1, 5, greenDroidSightRadius);
        greenDroidSightSlider.setMajorTickSpacing(1);
        greenDroidSightSlider.setPaintTicks(true);
        greenDroidSightSlider.setPaintLabels(true);
        greenDroidSightSlider.setBounds(600, 290, 250, 35);

        greenDroidSightSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    // Mengambil nilai jarak pandang droid hijau
                    int newSightRadius = (int) source.getValue();

                    // Update jarak pandang droid hijau dengan nilai yang digeser pengguna
                    greenDroidSightRadius = newSightRadius;
                }
            }
        });
        
        redDroid = new Droid(Color.RED);
        greenDroid = new Droid(Color.GREEN);
        
        add(randomizeMap);
        add(randomizeRedDroid);
        add(randomizeGreenDroid);
        add(startMove);
        add(stopMove);
        add(greenDroidSightSlider);
        add(addRedDroid);
        add(redDroidView);
        add(greenDroidView);
        add(exitGame);
    }
    
    private void startGame(){
        randomizeMap();
        randomizeDroid(redDroid);
        randomizeDroid(greenDroid);
    }

    private void gameOver(){
        if(gameOver){
            JOptionPane.showMessageDialog(null, "Game Berakhir");
            timer.stop();
            randomizeDroid(redDroid);
            randomizeDroid(greenDroid);
            resetRedDroids = false;
            redDroids.clear();
        }
    }

    private void addRedDroid(){
        Droid newRedDroid = new Droid(Color.RED);

        randomizeDroid(newRedDroid);
        redDroids.add(newRedDroid);
    }

    private class Droid {
        private int x;
        private int y;
        private Color color;

        public Droid(Color color) {
            this.color = color;
        }
        
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(x * cellSize + offsetX, y * cellSize + offsetY, cellSize, cellSize);
        }
    }
    
    private void randomizeMap() {
        for (int i = 0; i < maze.length; i++) {
            // Mengatur seluruh elemen dalam maze menjadi 1, yang berarti mengatur seluruh sel sebagai tembok
            Arrays.fill(maze[i], 1);
        }

        // Penciptaan peta dimulai dari pojok kiri atas
        generateMazeRecursive(0, 0);

        maze[0][0] = 0;
        maze[maze.length - 1][maze[0].length - 1] = 1;
    }

    private void generateMazeRecursive(int row, int col) {
        // Tandai sel saat ini sebagai telah dikunjungi
        maze[row][col] = 0;
    
        // Tentukan arah (atas, bawah, kiri, kanan) dalam urutan acak
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        shuffleArray(directions);
    
        // Kunjungi sel tetangga secara acak
        for (int[] direction : directions) {
            int newRow = row + 2 * direction[0];
            int newCol = col + 2 * direction[1];
            
            // Pengecekan apakah sel tetangga adalah sel yang valid dan belum dikunjungi
            if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length && maze[newRow][newCol] == 1) {
                maze[row + direction[0]][col + direction[1]] = 0;
    
                // Kunjungi sel berikutnya secara rekursif
                generateMazeRecursive(newRow, newCol);
            }
        }
    }
    
    private void shuffleArray(int[][] array) {
        Random random = new Random();
        
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
    
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
    
    private void randomizeDroid(Droid droid) {
        int x, y;
        // Melakukan looping hingga ditemukan sel yang bernilai bukan 1 (dinding).
        do {
            x = (int) (Math.random() * 10);
            y = (int) (Math.random() * 10);
        } while (maze[x][y] == 1);
        // Mengatur posisi Droid sesuai dengan nilai x dan y yang telah ditemukan.
        droid.setPosition(x, y);
    }

    private boolean canSeeGreenDroid(int redX, int redY, int greenX, int greenY, int[][] maze) {
        int row = maze.length;
        int col = maze[0].length;

        // Cek droid merah secara horizontal
        for (int i = redX + 1; i < row; i++) {
            if (maze[i][redY] == 1) break;
            if (i == greenX && redY == greenY) {
                return true;
            }
        }
        for (int i = redX - 1; i >= 0; i--) {
            if (maze[i][redY] == 1) break;
            if (i == greenX && redY == greenY) {
                return true;
            }
        }

        // Cek droid merah secara vertikal
        for (int j = redY + 1; j < col; j++) {
            if (maze[redX][j] == 1) break;
            if (redX == greenX && j == greenY) {
                return true;
            }
        }
        for (int j = redY - 1; j >= 0; j--) {
            if (maze[redX][j] == 1) break;
            if (redX == greenX && j == greenY) {
                return true;
            }
        }

        return false;
    }
    
    private void moveRedDroid() {
        int greenX = greenDroid.getX();
        int greenY = greenDroid.getY();
        int redX = redDroid.getX();
        int redY = redDroid.getY();

        // Cek apakah posisi droid merah dan hijau sudah sama (menempel)
        if (redX == greenX && redY == greenY) {
            gameOver = true;
            resetRedDroids = true;
            return; 
        }

        boolean canSeeGreenDroid = canSeeGreenDroid(redX, redY, greenX, greenY, maze);

        // Jika droid merah tidak melihat droid hijau, maka droid merah bergerak random mengelilingi peta
        if (!canSeeGreenDroid) {
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            List<int[]> validMoves = new ArrayList<>();
            for (int[] direction : directions) {
                int newX = redX + direction[0];
                int newY = redY + direction[1];
                if (newX >= 0 && newX < row && newY >= 0 && newY < col && maze[newX][newY] != 1) {
                    validMoves.add(new int[]{newX, newY});
                }
            }
            if (!validMoves.isEmpty()) {
                int[] nextMove = validMoves.get((int)(Math.random() * validMoves.size()));
                redDroid.setPosition(nextMove[0], nextMove[1]);
            }
            return;
        }

        // Terapkan algoritma BFS (Breadth First Search) kepada droid merah untuk mengejar droid hijau
        int[][] distance = new int[row][col]; 
        boolean[][] visited = new boolean[row][col]; 
        int[][] parentX = new int[row][col];
        int[][] parentY = new int[row][col];
        Queue<int[]> queue = new LinkedList<>();

        for (int i = 0; i < row; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
            Arrays.fill(visited[i], false);
            Arrays.fill(parentX[i], -1);
            Arrays.fill(parentY[i], -1);
        }

        distance[redX][redY] = 0; 
        visited[redX][redY] = true; 
        queue.offer(new int[]{redX, redY}); 

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                if (newX >= 0 && newX < row && newY >= 0 && newY < col && maze[newX][newY] != 1 && !visited[newX][newY]) {
                    distance[newX][newY] = distance[x][y] + 1;
                    visited[newX][newY] = true;
                    parentX[newX][newY] = x;
                    parentY[newX][newY] = y;
                    queue.offer(new int[]{newX, newY});

                    if (newX == greenX && newY == greenY) {
                        queue.clear();
                        break;
                    }
                }
            }
        }

        int nextX = greenX;
        int nextY = greenY;

        while (parentX[nextX][nextY] != redX || parentY[nextX][nextY] != redY) {
            int tempX = nextX;
            int tempY = nextY;
            nextX = parentX[tempX][tempY];
            nextY = parentY[tempX][tempY];
        }

        redDroid.setPosition(nextX, nextY);
        repaint();
    }
    
    private void moveGreenDroid() {
        int redX = redDroid.getX();
        int redY = redDroid.getY();
        int greenX = greenDroid.getX();
        int greenY = greenDroid.getY();
    
        if (redX == greenX && redY == greenY) {
            return; 
        }
    
        boolean[][] visited = new boolean[row][col];
        
        dfs(greenX, greenY, visited, redX, redY, greenDroidSightRadius);
        repaint();
    }
    
    private boolean dfs(int x, int y, boolean[][] visited, int redX, int redY, int sightRadius) {
        visited[x][y] = true;
    
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; 
    
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
    
            if (newX >= 0 && newX < row && newY >= 0 && newY < col && maze[newX][newY] != 1 && !visited[newX][newY] && (newX != redX || newY != redY)) {
                int distanceToRedDroid = Math.abs(newX - redX) + Math.abs(newY - redY);
                if (distanceToRedDroid > sightRadius) { 
                    greenDroid.setPosition(newX, newY);
                    return true;
                } else if (distanceToRedDroid == sightRadius) { 
                    return false;
                }
            }
        }
    
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
    
            if (newX >= 0 && newX < row && newY >= 0 && newY < col && maze[newX][newY] != 1 && !visited[newX][newY]) {
                if (dfs(newX, newY, visited, redX, redY, sightRadius)) {
                    return true;
                }
            }
        }
    
        return false;
    }

    private void paintRedDroidView() {
        showRedDroidView = !showRedDroidView;
        repaint();
    }

    private void paintGreenDroidView(){
        showGreenDroidView = !showGreenDroidView;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
   
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (maze[i][j] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(i * cellSize + offsetX, j * cellSize + offsetY, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(i * cellSize + offsetX, j * cellSize + offsetY, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(i * cellSize + offsetX, j * cellSize + offsetY, cellSize, cellSize);
                }
            }
        }
        
        redDroid.draw(g);
        for (Droid redDroid : redDroids) {
            redDroid.draw(g);
        }
        
        if(showRedDroidView){
            greenDroid.draw(g);
        }

        if(showGreenDroidView){
            int greenX = greenDroid.getX(); 
            int greenY = greenDroid.getY(); 

            // Menggambar pandangan Droid hijau sesuai dengan greenDroidSight
            for (int i = -greenDroidSightRadius; i <= greenDroidSightRadius; i++) {
                for (int j = -greenDroidSightRadius; j <= greenDroidSightRadius; j++) {
                    int drawX = greenX + i;
                    int drawY = greenY + j;

                    if (drawX >= 0 && drawX < row && drawY >= 0 && drawY < col) {
                        g.setColor(new Color(0, 255, 0, 100)); 
                        g.fillRect(drawX * cellSize + offsetX, drawY * cellSize + offsetY, cellSize, cellSize);
                    }
                }
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == randomizeMap) {
            randomizeMap();
            repaint();
        }else if(e.getSource() == randomizeRedDroid) {
            randomizeDroid(redDroid);
            repaint();
        }else if(e.getSource() == randomizeGreenDroid){
            randomizeDroid(greenDroid);
            repaint();
        }else if(e.getSource() == startMove){
            timer = new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gameOver = false;
                    moveRedDroid();
                    moveGreenDroid();
                    gameOver();
                    repaint();
                }
            });
            timer.start();
        }else if(e.getSource() == stopMove){
            timer.stop();
        }else if(e.getSource() == addRedDroid){
            addRedDroid();
            repaint();
        }else if(e.getSource() == exitGame){
            System.exit(0);
        }else if(e.getSource() == redDroidView){
            paintRedDroidView();
        }else if(e.getSource() == greenDroidView){
            paintGreenDroidView();
        }
    }
}
