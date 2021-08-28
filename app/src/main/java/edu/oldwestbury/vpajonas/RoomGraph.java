package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;
import java.util.Vector;

public class RoomGraph {
    private Node head;
    private DTile[][] tiles;
    private int maxWidth;
    private int maxHeight;
    private int minWidth;
    private int minHeight;
    private int tileOffsetX;
    private int tileOffsetY;
    public RoomGraph(Context context, int widthPassed, int heightPassed){
        maxWidth = 16;
        maxHeight = 16;
        minWidth = 5;
        minHeight = 5;
        tiles = new DTile[widthPassed][heightPassed];
        int numberOfRooms = 0;
        BitmapFactory bitmapfac = new BitmapFactory();
        Bitmap emptySprite = bitmapfac.decodeResource(context.getResources(),R.drawable.empty_tile);
        Bitmap wallSprite = bitmapfac.decodeResource(context.getResources(),R.drawable.wall_tile);

        int width, height; //of the current room
        Random rand = new Random();
        width = rand.nextInt(maxWidth-minWidth)+ minWidth; //random numb min to max size
        height = rand.nextInt(maxHeight-minHeight)+ minHeight; //random numb min to max size


        head = new Node(new EmptyRoom(context,null ,width,height,0,0,0));
        Node current = head; //CURRENT

        tileOffsetX = 50; //where on the actual map the room will be placed
        tileOffsetY = 50; //where on the actual map the room will be placed

        int originOffsetX = 0;
        int originOffsetY = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[tileOffsetX + i][tileOffsetY + j] = head.getData().getTiles()[i][j];
                tiles[tileOffsetX + i][tileOffsetY + j].setXY(tileOffsetX + i, tileOffsetY + j);
            }
        }

        numberOfRooms++;



        int direction = findDirection(current); //direction of the next room with respect to current
        while(direction != 0 && numberOfRooms < 425) { //if =0 then out of places to place new room


            width = rand.nextInt(maxWidth - minWidth) + minWidth;
            height = rand.nextInt(maxHeight - minHeight) + minHeight;

            if(direction == 1){
                tileOffsetY = tileOffsetY - height + minHeight ;
            }
            if(direction == 3){
                tileOffsetX = tileOffsetX - width + minWidth ;
            }

            while (!isValidSpace(tileOffsetX,tileOffsetY,width,height,direction)) {

                    if (width > minWidth) {
                        width--;
                        if (direction == 3) {
                            tileOffsetX++;
                        }
                    }

                    if(height > minHeight) {
                        height--;

                        if (direction == 1) {
                            tileOffsetY++;
                        }
                    }
            }



            current.addAdjacent(new Node(new EmptyRoom(context, null,width,height,0,0,0)));
            current = current.getAdjacents().elementAt(0);

            int roomTileOffsetX = 0;
            int roomTileOffsetY = 0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    tiles[tileOffsetX + i][tileOffsetY + j] = current.getData().getTiles()[i][j];
                    tiles[tileOffsetX + i][tileOffsetY + j].setXY(tileOffsetX + i, tileOffsetY + j);

                }
            }
            switch(direction){
                case 1:
                    tiles[tileOffsetX+1][tileOffsetY+height-1] = new EmptyDTile(emptySprite,tileOffsetX+1,tileOffsetY+height-1);
                    break;
                case 2:
                    tiles[tileOffsetX+1][tileOffsetY] = new EmptyDTile(emptySprite, tileOffsetX+1,tileOffsetY);
                    break;
                case 3:
                    tiles[tileOffsetX+width -1][tileOffsetY+1] = new EmptyDTile(emptySprite, tileOffsetX +width - 1,tileOffsetY+1);
                    break;
                case 4:
                    tiles[tileOffsetX][tileOffsetY+1] = new EmptyDTile(emptySprite, tileOffsetX,tileOffsetY+1);
                    break;
            }

            numberOfRooms++;
            direction = findDirection(current);
        }

    }

    private boolean isValidSpace(int x, int y, int w, int h, int d){

        if (x < 0 || x + w > tiles.length || y < 0|| y + h > tiles[x].length) {
            return false;
        }
        y++;
        h-=2;
        x++;
        w-=2;


        for (int i = x; i < x+w; i++){
            if(tiles[i][y] != null || tiles[i][y+h-1] != null){
                return false;
            }

        }
        for (int i = y; i < y+h; i++){
            if(tiles[x][i] != null || tiles[x+w-1][i] != null){
                return false;
            }

        }


        return true;

    }
    private boolean isInvalidXRow(int x, int y, int w){

        if  (y > tiles[x].length || y < 0 || x + w > tiles.length){
            return true;
        }

        y++;
        x++;
        w-=2;

        for (int i = x; i < x+w; i++){
            if(tiles[i][y] != null){
                return false;
            }

        }

        return false;
    }
    private boolean isInvalidYRow(int x, int y, int h){

        if  (x > tiles.length || x < 0 || y+ h > tiles[x].length){
            return true;
        }

        y++;
        h-=2;
        x++;

        for (int i = y; i < y+h; i++) {
            if (tiles[x][i] != null) {
                return false;
            }

        }

        return false;
    }

    private int findDirection(Node current){


        int originalX= tileOffsetX;
        int originalY = tileOffsetY;
        boolean upChecked = false;
        boolean downChecked = false;
        boolean leftChecked = false;
        boolean rightChecked = false;
        Random rand = new Random();
        int direction = rand.nextInt(4) + 1;

        if (direction == 1) {
            tileOffsetY= (tileOffsetY - minHeight)+1;
            upChecked = true;

        }
        else if (direction == 2) {
            tileOffsetY = (tileOffsetY+ current.getData().getTiles()[0].length)-1;
            downChecked = true;
        }
        else if (direction == 3) {
             tileOffsetX = (tileOffsetX - minWidth)+1;
            leftChecked = true;
        }
        else if (direction == 4) {

            tileOffsetX= (tileOffsetX +current.getData().getTiles().length)-1;
            rightChecked = true;
        }

        while(!isValidSpace(tileOffsetX,tileOffsetY,minWidth,minHeight,direction)) {
            if(leftChecked && rightChecked && upChecked && downChecked){
                return 0;
            }

            tileOffsetX = originalX;
            tileOffsetY = originalY;

            if(upChecked && direction == 1){ direction++;}
            if(downChecked && direction == 2){ direction++;}
            if(leftChecked && direction == 3){ direction++;}
            if(rightChecked && direction == 4){ direction=1;}


            if (direction == 1) {
                tileOffsetY= (tileOffsetY - minHeight)+1;
                upChecked = true;

            }
            else if (direction == 2) {
                tileOffsetY = (tileOffsetY+ current.getData().getTiles()[0].length)-1;
                downChecked = true;
            }
            else if (direction == 3) {
                tileOffsetX = (tileOffsetX - minWidth)+1;
                leftChecked = true;
            }
            else if (direction == 4) {

                tileOffsetX= (tileOffsetX +current.getData().getTiles().length)-1;
                rightChecked = true;
            }


        }

        return direction;
    }

    public DTile[][] getTiles(){
        return tiles;
    }
    private class Node{
        private EmptyRoom data;
        private Vector<Node> adjacents;
        public Node(EmptyRoom room){
            data = room;
            adjacents = new Vector<Node>();
        }
        public void addAdjacent(Node toAdd){
            for(int i = 0; i < adjacents.size(); i++){
                if (adjacents.elementAt(i) == toAdd){
                    return;
                }
            }
            adjacents.add(toAdd);

        }
        public Vector<Node> getAdjacents(){
            return adjacents;
        }
        public EmptyRoom getData(){
            return data;
        }


    }
}
