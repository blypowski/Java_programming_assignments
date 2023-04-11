package zd2;

/**
 * 
 * @author Jakub Marczyk
 *
 */

public class Drawing implements SimpleDrawing {
	int canvas[][]; //płótno
	int coordinate1;	//początkowy pierwszy koordynat
	int coordinate2;	//początkowy drugi koordynat
	int size;	//rozmiar płótna
	boolean initializedCanvas = false;
		
	public void setCanvasGeometry(Geometry input) {
		coordinate1 = input.getInitialFirstCoordinate();
		coordinate2 = input.getInitialSecondCoordinate();
		size = input.getSize();
		canvas = new int[size][size];
		initializedCanvas = true;
	}
	
	public void draw(Segment segment) {
		int direction = segment.getDirection();
		int length = segment.getLength();
		int color = segment.getColor();
		
		int i=1;
		if(direction==1) {
			while(i<=length && coordinate1<size) {	//odcinek nie może wyjść poza ostatni/przed pierwszy element tablicy
				canvas[coordinate1++][coordinate2] = color;
				i++;
			}
			coordinate1--;	//w każdym przypadku muszę zadbać o to, by nowy odcinek rozpoczął się w końcu poprzedniego
		}
		else if(direction==2) {
			while(i<=length && coordinate2<size) {
				canvas[coordinate1][coordinate2++] = color;
				i++;
			}
			coordinate2--;
		}
		else if(direction==-1) {
			while(i<=length && coordinate1>=0) {
				canvas[coordinate1--][coordinate2] = color;
				i++;
			}
			coordinate1++;
		}
		else if(direction==-2) {
			while(i<=length && coordinate2>=0) {
				canvas[coordinate1][coordinate2--] = color;
				i++;
			}
			coordinate2++;
		}
		
	}

	public int[][] getPainting(){
		if(initializedCanvas)	//nie mogę bezpośrednio sprawdzić niezainicjalizowanej zmiennej int[][], więc korzystam ze zmiennej pomocnicznej typu bool
			return canvas;
		else 
			return null;
	}

	public void clear() {
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				canvas[i][j]=0;
			}
		}
	}

}
