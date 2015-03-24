class Game{
	private char[][] board = new char[8][8];
	private static final char RED = 'r';
	private static final char BLUE = 'b';
	private String TOKEN;
	/*public static void main(String[] args) {
		Game g = new Game();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean color = true;
		while(!g.isGameOver()){
			int i = (int)(Math.random() * 8);
			int j = (int)(Math.random() * 8);
			if(color){
				System.out.println(i+","+j+",RED");
				g.move(i, j, RED);
				color = false;
			}
			else{
				System.out.println(i+","+j+",BLUE");
				g.move(i, j, BLUE);
				color = true;
			}
			g.display();
			try{
				Thread.sleep(250);
			}
			catch(Exception e){
				//
			}
		}
	}*/
	public Game(String  token){
		TOKEN = token;

		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				board[i][j] = '.';
			}
		}
		board[3][3] = RED;
		board[3][4] = BLUE;
		board[4][3] = BLUE;
		board[4][4] = RED;
	}
	public String getDisplay(){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				sb.append(board[i][j]);
			}
		}
		return sb.toString();
	}
	public void display(){
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	public String getToken(){
		return TOKEN;
	}
	public boolean move(int row, int col, char color){
		// Returns true if the specified move is valid and move is made else returns false

		if(row < 0 || col < 0 || row > 7 || col > 7)
			return false;

		if(board[row][col] == '.' && isMovePossible(row, col, color)){
			board[row][col] = color;
			flip(row, col, color);
			//flip2(color);
			return true;
		}
		else{
			return false;
		}
	}
	private boolean isMovePossible(int row, int col, char color){
		// Returns true if a move is possible for a color at the position specified by row & col

		if(getIndices(row, col, color, 'L') != null || getIndices(row, col, color, 'R') != null || getIndices(row, col, color, 'T') != null || getIndices(row, col, color, 'B') != null
			|| getIndices(row, col, color, '1') != null || getIndices(row, col, color, '2') != null || getIndices(row, col, color, '3') != null || getIndices(row, col, color, '4') != null){
			return true;
		}
		else{
			return false;
		}
	}
	private void flip(int row, int col, char color){
		// Flip the colors of the coins accordingly.

		int left[] = getIndices(row, col, color, 'L');
		int right[] = getIndices(row, col, color, 'R');
		int top[] = getIndices(row, col, color, 'T');
		int bottom[] = getIndices(row, col, color, 'B');
		int t_left[] = getIndices(row, col, color, '1');
		int b_left[] = getIndices(row, col, color, '3');
		int t_right[] = getIndices(row, col, color, '2');
		int b_right[] = getIndices(row, col, color, '4');

		if(left != null){
			System.out.println("left");
			for(int i=left[0];i<=col;i++){
				board[row][i] = color;
			}
		}
		if(right != null){
			for(int i=right[1];i>=col;i--){
				board[row][i] = color;
			}
		}
		if(top != null){
			System.out.println("top");
			for(int i=top[0];i<=row;i++){
				System.out.println(i);
				board[i][col] = color;
			}
		}
		if(bottom != null){
			for(int i=bottom[1];i>=row;i--){
				board[i][col] = color;
			}
		}
		if(t_left != null){
			for(int i=t_left[0], j=t_left[1];i<=row && j<=col;i++, j++){
				board[i][j] = color;
			}
		}
		if(t_right != null){
			for(int i=t_right[0], j=t_right[1];i<=row && j>=col;i++, j--){
				board[i][j] = color;
			}
		}
		if(b_left != null){
			for(int i=b_left[0], j=b_left[1];i>=row && j<=col;i--, j++){
				board[i][j] = color;
			}
		}
		if(b_right != null){
			for(int i=b_right[0], j=b_right[1];i>=row && j>=col;i--, j--){
				board[i][j] = color;
			}
		}

		//flip2(color);
	}
	private void flip2(char color){
		char complementary;
		if(color == RED)
			complementary = BLUE;
		else
			complementary = RED;

		
		for(int i=0;i<8;i++){
			int start_ind = -1;
			int end_ind = -1;
			for(int j=0;j<8;j++){
				if(board[i][j] == color && start_ind == -1){
					start_ind = j;
					end_ind = j;
				}
				if(board[i][j] == complementary && start_ind != -1 && j == end_ind+1){
					end_ind = j;
				}
			}
			if(start_ind != -1 && board[i][end_ind+1] == color){
				System.out.println(i+","+start_ind+" to "+i+","+end_ind);
				for(int k = start_ind+1; k<=end_ind;k++){
					board[i][k] = color;
				}
			}
		}


		for(int i=0;i<8;i++){
			int start_ind = -1;
			int end_ind = -1;
			for(int j=0;j<8;j++){
				if(board[j][i] == color && start_ind == -1){
					start_ind = j;
					end_ind = j;
				}
				if(board[j][i] == complementary && start_ind != -1 && j == end_ind+1){
					end_ind = j;
				}
			}
			if(start_ind != -1 && board[end_ind+1][i] == color){
				System.out.println(start_ind+","+i+" to "+end_ind+","+i);
				for(int k = start_ind+1; k<=end_ind;k++){
					board[k][i] = color;
				}
			}
		}
	}
	private int[] getIndices(int row, int col, char color, char direction){
		// Returns index for jump starting position in the direction specified.
		int res[] = new int[2];

		if(color == RED)
			color = BLUE;
		else
			color = RED;

		int i, j;
		switch(direction){
			case 'T':	i = row-1;
						for(;i>=0;i--){
							if(board[i][col] != color)
								break;
						}
						if(i == row-1 || board[i][col] == '.')
							return null;
						else{
							res[0] = i;
							res[1] = col;
							return res;
						}
			case 'B':	i = row+1;
						for(;i<8;i++){
							if(board[i][col] != color)
								break;
						}
						if(i == row+1 || board[i][col] == '.')
							return null;
						else{
							res[0] = i;
							res[1] = col;
							return res;
						}
			case 'L':	i = col-1;
						for(;i>=0;i--){
							if(board[row][i] != color)
								break;
						}
						if(i == col-1 || board[row][i] == '.')
							return null;
						else{
							res[0] = row;
							res[1] = i;
							return res;
						}
			case 'R':	i = col+1;
						for(;i<8;i++){
							if(board[row][i] != color)
								break;
						}
						if(i == col+1 || board[row][i] == '.')
							return null;
						else{
							res[0] = row;
							res[1] = i;
							return res;
						}
			case '1':	i = row-1;
					j = col-1;
						for(;i>=0 && j>=0;i--,j--){
							if(board[i][j] != color)
								break;
						}
						if((i == row-1 && j == col-1) || board[i][j] == '.')
							return null;
						else{
							res[0] = i;
							res[1] = j;
							return res;
						}
			case '2':	i = row-1;
					j = col+1;
						for(;i>=0 && j<8;i--,j++){
							if(board[i][j] != color)
								break;
						}
						if((i == row-1 && j == col+1) || board[i][j] == '.')
							return null;
						else{
							res[0] = i;
							res[1] = j;
							return res;
						}
			case '3':	i = row+1;
					j = col-1;
					for(;i<8 && j>=0;i++,j--){
							if(board[i][j] != color)
								break;
						}
						if((i == row+1 && j == col-1) || board[i][j] == '.')
							return null;
						else{
							res[0] = i;
							res[1] = j;
							return res;
						}
			case '4':	i = row+1;
					j = col+1;
						for(;i<8 && j<8;i++,j++){
							if(board[i][j] != color)
								break;
						}
						if((i == row+1 && j == col+1) || board[i][j] == '.')
							return null;
						else{
							res[0] = i;
							res[1] = j;
							return res;
						}
                        default:    return null;
		}
	}
	private boolean isFilled(){
		// Returns true if the entire board is filled.

		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if(board[i][j] == '.')
					return false;
			}
		}
		return true;
	}
	public boolean isGameOver(){
		if(isFilled())
			return true;
		else{
			for(int i=0;i<8;i++){
				for(int j=0;j<8;j++){
					if(board[i][j] != '.')
						continue;
					else{
						if(isMovePossible(i, j, RED) || isMovePossible(i, j, BLUE))
							return false;
					}
				}
			}
			return true;
		}
	}
}