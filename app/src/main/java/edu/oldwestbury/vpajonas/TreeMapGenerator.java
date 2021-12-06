package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.BitmapFactory;

import org.w3c.dom.Node;

import java.util.Random;
import java.util.Vector;

public class TreeMapGenerator {

    private Context context;

    private int partitionSize;
    private int threadMax;
    private int threadCounter;
    private int threadMaxLength;
    private int threadLengthCounter;
    private int direction;
    private Random rand;

    private int roomWidth;
    private int roomHeight;
    private int xOffset;
    private int yOffset;


    private Node head;

    private Node startRoom;
    private Node endRoom;
    private Vector<Node> allRooms;

    private int lowestX;
    private int lowestY;
    private int highestX;
    private int highestY;

    private Vector<Actor> actors;
    private ScreenXYPositionFinder screenXYPosFin;
    private Player player;
    private String errorText;
    private FloorTheme floorTheme;

    private boolean impossibleRoomMade;

    private int startX;
    private int startY;

    public TreeMapGenerator(Context passedContext,Player passedPlayer, FloorTheme passedFloorTheme, int floorNum){
        rand = new Random();

        context = passedContext;
        threadMax = (floorNum / 3) + 1;
        threadCounter = 0;
        threadMaxLength = floorNum + (floorNum / 6) + 2;
        threadLengthCounter = 0;
        direction = 0;
        xOffset = 0;
        yOffset = 0;
        errorText = "";

        startX = 0;
        startY = 0;

        actors = new Vector<Actor>();

        BitmapFactory bitmapFactory = new BitmapFactory();
        floorTheme = passedFloorTheme;


        rand.setSeed(System.currentTimeMillis());
        partitionSize = 10;
        roomWidth = partitionSize;
        roomHeight = partitionSize;

        head = new Node(new EmptyRoom(context,floorTheme,roomWidth,roomHeight,xOffset,yOffset,-1));

        Node current = head;
        Node toAddNode;
        threadLengthCounter++;
        try {
            while (threadCounter < threadMax) {
                current = getAllRooms().elementAt(rand.nextInt(getAllRooms().size()));
                xOffset = current.getData().getXOffset();
                yOffset = current.getData().getYOffset();

                while (threadLengthCounter < threadMaxLength) {
                    findDirection(current);
                    int widthMultiplier = (rand.nextInt(2) + 1);
                    int heightMultiplier = (rand.nextInt(2) + 1);

                    roomWidth = (partitionSize * widthMultiplier);
                    roomHeight = (partitionSize * heightMultiplier);
                    if (direction == 1) {
                        yOffset = yOffset - roomHeight + partitionSize;
                    }
                    if (direction == 3) {
                        xOffset = xOffset - roomWidth + partitionSize;
                    }
                    while (!isFreeSpace(xOffset, yOffset, roomWidth, roomHeight)) {

                        if(roomWidth == partitionSize && roomHeight == partitionSize){
                            impossibleRoomMade = true;
                            break;
                        }


                        if (roomWidth > partitionSize) {
                            roomWidth -= partitionSize;
                            if (direction == 3) {
                                xOffset += partitionSize;
                            }
                        }
                        if (roomHeight > partitionSize) {
                            roomHeight -= partitionSize;
                            if (direction == 1) {
                                yOffset += partitionSize;
                            }
                        }


                    }
                    if(!impossibleRoomMade) {
                        if (roomHeight == roomWidth) {
                            if(roomWidth == partitionSize && roomHeight == partitionSize && rand.nextInt(2) == 0){

                                toAddNode = new Node(new BridgedCenterRoom(context, floorTheme, xOffset, yOffset, direction), current);
                            }
                            else {
                                toAddNode = new Node(new CrossRoom(context, floorTheme, roomWidth, roomHeight, xOffset, yOffset, direction), current);
                            }
                        } else {
                            toAddNode = new Node(new EmptyRoom(context, floorTheme, roomWidth, roomHeight, xOffset, yOffset, direction), current);
                        }
                        current.addAdjacent(toAddNode);
                        current = toAddNode;
                        threadLengthCounter++;

                    }
                    else{
                        impossibleRoomMade = false;
                        Node secondToLast = null;
                        for(int i = 0; i < threadLengthCounter;i++){
                            secondToLast = current;
                            current = current.getAdjacents().elementAt(0);
                        }
                        for(int i = 0; i < current.getAdjacents().size();i++){
                            if(current.getAdjacents().elementAt(i) == secondToLast){
                                current.getAdjacents().remove(i);
                                break;
                            }

                        }

                        current = getAllRooms().elementAt(rand.nextInt(getAllRooms().size()));
                        xOffset = current.getData().getXOffset();
                        yOffset = current.getData().getYOffset();
                        threadLengthCounter = 0;

                    }
                }

                threadLengthCounter = 0;
                threadCounter++;

            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.constructor() ERROR 1 -" + e.toString();
        }

        allRooms = getAllRooms();
        int distance = setStartEnd(floorNum);

        if(passedPlayer == null) {
            player = new Player(context, startRoom.getData().getTiles(), startX, startY);
        }
        else{
            player = passedPlayer;
            player.setTileSet(startRoom.getData().getTiles());
            player.setTileX(startX);
            player.setTileY(startY);
            startRoom.getData().getTiles()[player.getTileX()][player.getTileY()].setActor(player);
            if(player.getPartner() != null){
                player.getPartner().setTiles(startRoom.getData().getTiles());
                if ( !startRoom.getData().getTiles()[player.getTileX()+1][player.getTileY()].getIsSolid()  &&
                        startRoom.getData().getTiles()[player.getTileX()+1][player.getTileY()].getActor() == null) {
                    player.getPartner().setTileX(player.getTileX() + 1);
                    player.getPartner().setTileY(player.getTileY());
                }
                else if ( !startRoom.getData().getTiles()[player.getTileX()-1][player.getTileY()].getIsSolid()  &&
                        startRoom.getData().getTiles()[player.getTileX()-1][player.getTileY()].getActor() == null) {
                    player.getPartner().setTileX(player.getTileX() - 1);
                    player.getPartner().setTileY(player.getTileY());
                }
                else if ( !startRoom.getData().getTiles()[player.getTileX()][player.getTileY()+1].getIsSolid()  &&
                        startRoom.getData().getTiles()[player.getTileX()][player.getTileY()+1].getActor() == null) {
                    player.getPartner().setTileX(player.getTileX());
                    player.getPartner().setTileY(player.getTileY()+1);
                }
                else{
                    player.getPartner().setTileX(player.getTileX());
                    player.getPartner().setTileY(player.getTileY()-1);
                }

                startRoom.getData().getTiles()[player.getPartner().getTileX()][player.getPartner().getTileY()].setActor(player.getPartner());
            }
        }

        screenXYPosFin = player.getScreenXYPosFin();

        actors.add(player);
        if(player.getPartner()!= null){
            actors.add(player.getPartner());
        }




        Vector<Node> path = new Vector<Node>();

        getMinPath(startRoom,endRoom,null,path,0,distance);

       setKeysAndLockedDoors(path,distance/2);

       try {
          spawnEnemies(allRooms.size() - allRooms.size() / 2 + rand.nextInt(allRooms.size()));
       //    spawnItems(1 + rand.nextInt(allRooms.size()/2));
       }catch (Exception e){
           errorText += e.getLocalizedMessage();
       }

       HeldWeapon heldWeapon = new HeldWeapon(player.getPlayerStats(),new GunWeapon(context));
       FloorItem floorItem = new FloorItem(context,startRoom.getData().getTiles(),heldWeapon,player,player.getTileX(),player.getTileY() + 1);
       startRoom.getData().getTiles()[player.getTileX()][player.getTileY() + 1].setItem(floorItem);
       actors.add(floorItem);

       makeEntrances(path);

       orientXY();


    }

    private void spawnItems(int number){
        int count = 0;
        int roomIndex;
        int spawnPointIndex;
        boolean checkedRooms[] = new boolean[allRooms.size()];
        for (int i = 0; i < checkedRooms.length; i++){
            checkedRooms[i] = false;
        }
        boolean checkedPositions[];

        while(count < number){
            try{
                roomIndex = getRandomRoomIndex(checkedRooms);


                checkedPositions = new boolean[allRooms.elementAt(roomIndex).getData().getItemSpawnPoints().length];
                for (int i = 0; i < checkedPositions.length; i++){
                    checkedPositions[i] = false;
                }

                spawnPointIndex = rand.nextInt(allRooms.elementAt(roomIndex).getData().getItemSpawnPoints().length);
                checkedPositions[spawnPointIndex] = true;
            }catch (Exception e){
                return;
            }
            while(allRooms.elementAt(roomIndex).getData().getItemSpawnPoints()[spawnPointIndex].getItem() != null){

                for(int i = 0; i < checkedPositions.length;i++){

                    if(!checkedPositions[i]){
                        break;
                    }

                    if(i == checkedPositions.length-1){
                        checkedRooms[roomIndex] = true;
                        roomIndex = getRandomRoomIndex(checkedRooms);
                        checkedPositions = new boolean[allRooms.elementAt(roomIndex).getData().getItemSpawnPoints().length];
                        for (int j = 0; j < checkedPositions.length; j++){
                            checkedPositions[j] = false;
                        }

                        spawnPointIndex = rand.nextInt(allRooms.elementAt(roomIndex).getData().getItemSpawnPoints().length);
                        spawnPointIndex--;
                    }
                }

                spawnPointIndex++;
                if (spawnPointIndex >= allRooms.elementAt(roomIndex).getData().getItemSpawnPoints().length){
                    spawnPointIndex = 0;
                }
                checkedPositions[spawnPointIndex] = true;
            }



           FloorItem floorItem = new FloorItem(context,
                   allRooms.elementAt(roomIndex).getData().getTiles(),
                   new RecoveryHeartHeldItem(player.getPlayerStats()),
                   player,
                    allRooms.elementAt(roomIndex).getData().getItemSpawnPoints()[spawnPointIndex].getX(),
                    allRooms.elementAt(roomIndex).getData().getItemSpawnPoints()[spawnPointIndex].getY());

            actors.add(floorItem);
            count++;
        }


    }

    private void spawnEnemies(int number){
        int count = 0;
        int roomIndex;
        int spawnPointIndex;
        boolean checkedRooms[] = new boolean[allRooms.size()];
        for (int i = 0; i < checkedRooms.length; i++){
            checkedRooms[i] = false;
        }
        boolean checkedPositions[];

        while(count < number){
            try{
            roomIndex = getRandomRoomIndex(checkedRooms);


            checkedPositions = new boolean[allRooms.elementAt(roomIndex).getData().getEnemySpawnPoints().length];
            for (int i = 0; i < checkedPositions.length; i++){
                checkedPositions[i] = false;
            }

            spawnPointIndex = rand.nextInt(allRooms.elementAt(roomIndex).getData().getEnemySpawnPoints().length);
            checkedPositions[spawnPointIndex] = true;
            }catch (Exception e){
                return;
            }
            while(allRooms.elementAt(roomIndex).getData().getEnemySpawnPoints()[spawnPointIndex].getActor() != null){

                for(int i = 0; i < checkedPositions.length;i++){

                    if(!checkedPositions[i]){
                        break;
                    }

                    if(i == checkedPositions.length-1){
                        checkedRooms[roomIndex] = true;
                        roomIndex = getRandomRoomIndex(checkedRooms);
                        checkedPositions = new boolean[allRooms.elementAt(roomIndex).getData().getEnemySpawnPoints().length];
                        for (int j = 0; j < checkedPositions.length; j++){
                            checkedPositions[j] = false;
                        }

                        spawnPointIndex = rand.nextInt(allRooms.elementAt(roomIndex).getData().getEnemySpawnPoints().length);
                      spawnPointIndex--;
                    }
                }

                spawnPointIndex++;
                if (spawnPointIndex >= allRooms.elementAt(roomIndex).getData().getEnemySpawnPoints().length){
                    spawnPointIndex = 0;
                }
                checkedPositions[spawnPointIndex] = true;
            }

            TestEnemy testEnemy = new TestEnemy(
                    context,
                    allRooms.elementAt(roomIndex).getData().getTiles(),
                    player,
                    allRooms.elementAt(roomIndex).getData().getEnemySpawnPoints()[spawnPointIndex].getX(),
                    allRooms.elementAt(roomIndex).getData().getEnemySpawnPoints()[spawnPointIndex].getY());


            actors.add(testEnemy);
            count++;
        }

    }
    private int getRandomRoomIndex(boolean[] checkedRooms){
        int roomIndex = rand.nextInt(allRooms.size());
        while(checkedRooms[roomIndex]){
            roomIndex = rand.nextInt(allRooms.size());
        }

        return roomIndex;

    }

    private void setKeysAndLockedDoors(Vector<Node> path, int numberOfDoors){
        while(path.size() <= numberOfDoors +1){
            numberOfDoors--;
            if(numberOfDoors <= 0){
                return;
            }
        }

        int[] lockedRooms = new int[numberOfDoors];
        try {


            for (int i = 0; i < lockedRooms.length; i++) {

                lockedRooms[i] = rand.nextInt(path.size() - 2) + 1;

                for (int j = 0; j < i; j++) {
                    if (lockedRooms[i] == lockedRooms[j]) {

                        i--;
                        break;
                    }
                }

            }
        }catch(Exception e){
            errorText += "TreeMapGenerator.setKeysAndLockedDoors() ERROR 1 -" + e.toString();

        }
        try {
            for (int i = 0; i < lockedRooms.length; i++) {
                Node current = path.elementAt(lockedRooms[i]);
                current.getData().setLocked(true);

                Node initial = current;
                current = path.elementAt(lockedRooms[i] - 1);
                while (rand.nextInt(3) != 0 && current.getAdjacents().size() > 1) {
                    int randomNum = rand.nextInt(current.getAdjacents().size());
                    while (current.getAdjacents().elementAt(randomNum) == initial) {
                        randomNum = rand.nextInt(current.getAdjacents().size());
                    }
                    current = current.getAdjacents().elementAt(randomNum);
                }

                int j = 0;
                while(current.getData().getItemSpawnPoints()[j].getItem() !=null){
                    j++;
                }

                FloorItem key = new FloorItem(context, current.getData().getTiles(), 0 ,player, current.getData().getItemSpawnPoints()[j].getX(), current.getData().getItemSpawnPoints()[j].getY());
                current.getData().getItemSpawnPoints()[j].setItem(key);
                actors.add(key);

            }
        }catch(Exception e){
            errorText += "TreeMapGenerator.setKeysAndLockedDoors() ERROR 2 -" + e.toString();

        }
    }

    public DTile[][] getTiles(){

        DTile[][] toReturn = new DTile[highestX][highestY];

        try {

            for (int i = 0; i < allRooms.size(); i++) {
                try {
                    int tempXOffset = allRooms.elementAt(i).getData().getXOffset();
                    int tempYOffset = allRooms.elementAt(i).getData().getYOffset();
                    int tempWidth = allRooms.elementAt(i).getData().getWidth();
                    int tempHeight = allRooms.elementAt(i).getData().getHeight();

                    for (int j = 0; j < tempWidth; j++) {
                        for (int k = 0; k < tempHeight; k++) {
                            try {
                                toReturn[tempXOffset + j][tempYOffset + k] = allRooms.elementAt(i).getData().getTiles()[j][k];
                                toReturn[tempXOffset + j][tempYOffset + k].setXY(tempXOffset + j, tempYOffset + k);
                                if (toReturn[tempXOffset + j][tempYOffset + k].getActor() != null) {
                                    Actor temp = toReturn[tempXOffset + j][tempYOffset + k].getActor();
                                    temp.setTileX(tempXOffset + j);
                                    temp.setTileY(tempYOffset + k);
                                    temp.setTiles(toReturn);


                                }
                                if (toReturn[tempXOffset + j][tempYOffset + k].getItem() != null) {
                                    FloorItem temp = toReturn[tempXOffset + j][tempYOffset + k].getItem();
                                    temp.setTileX(tempXOffset + j);
                                    temp.setTileY(tempYOffset + k);
                                    temp.setTiles(toReturn);

                                }
                            } catch (Exception e) {
                                errorText += "TreeMapGenerator.getTiles() ERROR 3 -" + e.toString();
                            }
                        }
                    }
                }catch (Exception e){
                    errorText += "TreeMapGenerator.getTiles() ERROR 2 -" + e.toString();
                }
            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.getTiles() ERROR 1 -" + e.toString();
        }
        for(int i = 0; i < actors.size(); i++){
            actors.elementAt(i).setTiles(toReturn);
        }

        return toReturn;
    }

    private void orientXY(){

        lowestX = allRooms.elementAt(0).getData().getXOffset();
        lowestY = allRooms.elementAt(0).getData().getYOffset();
        try {
            for (int i = 1; i < allRooms.size(); i++) {
                if (allRooms.elementAt(i).getData().getXOffset() < lowestX) {
                    lowestX = allRooms.elementAt(i).getData().getXOffset();
                }
                if (allRooms.elementAt(i).getData().getYOffset() < lowestY) {
                    lowestY = allRooms.elementAt(i).getData().getYOffset();
                }
            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.orientXY() ERROR 1 -" + e.toString();
        }
        //finding room that is leftmost and topmost
        if (lowestX < 0) {

            lowestX *= -1;
        }
        if (lowestY < 0) {

            lowestY *= -1;
        }
        try {
            for (int i = 0; i < allRooms.size(); i++) { //moving all the rooms to the left so the minimum is at 0
                allRooms.elementAt(i).getData().setXOffset(allRooms.elementAt(i).getData().getXOffset() + lowestX);
                allRooms.elementAt(i).getData().setYOffset(allRooms.elementAt(i).getData().getYOffset() + lowestY);
            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.orientXY() ERROR 2 -" + e.toString();
        }


        highestX = allRooms.elementAt(0).getData().getXOffset() + allRooms.elementAt(0).getData().getWidth();
        highestY = allRooms.elementAt(0).getData().getYOffset() + allRooms.elementAt(0).getData().getHeight(); //finding max tiles to the right/down
        try {
            for (int i = 1; i < allRooms.size(); i++) {
                if (allRooms.elementAt(i).getData().getXOffset() + allRooms.elementAt(i).getData().getWidth() > highestX) {
                    highestX = allRooms.elementAt(i).getData().getXOffset() + allRooms.elementAt(i).getData().getWidth();
                }
                if (allRooms.elementAt(i).getData().getYOffset() + allRooms.elementAt(i).getData().getHeight() > highestY) {
                    highestY = allRooms.elementAt(i).getData().getYOffset() + allRooms.elementAt(i).getData().getHeight();
                }
            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.orientXY() ERROR 3 -" + e.toString();
        }



    }

    private boolean isFreeSpace(int x, int y, int w, int h){

        allRooms = getAllRooms();

        for(int i = 0; i < allRooms.size();i++){

            if (x < (allRooms.elementAt(i).getData().getXOffset() + allRooms.elementAt(i).getData().getWidth()) - 2 &&
                    x + w > allRooms.elementAt(i).getData().getXOffset() + 1 &&
                    y < (allRooms.elementAt(i).getData().getYOffset() + allRooms.elementAt(i).getData().getHeight()) - 2 &&
                    y + h > allRooms.elementAt(i).getData().getYOffset() + 1) {

                return false;
            }
        }
        return true;
    }
    private void findDirection(Node current){

        boolean upChecked = false;
        boolean downChecked = false;
        boolean leftChecked = false;
        boolean rightChecked = false;
        int originalX = xOffset;
        int originalY = yOffset;

        try {
            direction = rand.nextInt(4) + 1;
            while (!isFreeSpace(xOffset, yOffset, partitionSize, partitionSize)) {
                xOffset = originalX;
                yOffset = originalY;
                if (leftChecked && rightChecked && upChecked && downChecked) {
                    direction = 0;
                    return;
                }


                if (upChecked && direction == 1) {
                    direction++;
                }
                if (downChecked && direction == 2) {
                    direction++;
                }
                if (leftChecked && direction == 3) {
                    direction++;
                }
                if (rightChecked && direction == 4) {
                    direction = 1;
                }

                if (direction == 1) {
                    yOffset = (yOffset - partitionSize);
                    upChecked = true;
                } else if (direction == 2) {
                    yOffset = (yOffset + current.getData().getHeight());
                    downChecked = true;
                } else if (direction == 3) {
                    xOffset = (xOffset - partitionSize);
                    leftChecked = true;
                } else if (direction == 4) {
                    xOffset = (xOffset + current.getData().getWidth());
                    rightChecked = true;
                }
            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.findDirection() ERROR 1 -" + e.toString();
        }

    }

    public Vector<Actor> getActors(){
        return actors;
    }

    private void makeEntrances(Vector<Node> path){
        Node current;
        Node currentAdjacent;
        int tempX =  0;
        int tempY = 0;
        try {
            for (int i = 0; i < allRooms.size(); i++) {
                current = allRooms.elementAt(i);
                for (int j = 0; j < current.getAdjacents().size(); j++) {
                    currentAdjacent = current.getAdjacents().elementAt(j);
                    try {
                        if (current.getData().getXOffset() < currentAdjacent.getData().getXOffset()) { //adj is to right

                            if (current.getData().getHeight() > currentAdjacent.getData().getHeight()) {
                                tempX = current.getData().getWidth() - 1;
                                tempY = (currentAdjacent.getData().getHeight() / 2);
                            } else {
                                tempX = current.getData().getWidth() - 1;
                                tempY = (current.getData().getHeight() / 2);
                            }
                        } else if (current.getData().getXOffset() > currentAdjacent.getData().getXOffset()) { //adj is to left

                            if (current.getData().getHeight() > currentAdjacent.getData().getHeight()) {
                                tempX = 0;
                                tempY = (currentAdjacent.getData().getHeight() / 2);
                            } else {
                                tempX = 0;
                                tempY = (current.getData().getHeight() / 2);

                            }
                        } else if (current.getData().getYOffset() < currentAdjacent.getData().getYOffset()) { //adj is below

                            if (current.getData().getWidth() > currentAdjacent.getData().getWidth()) {
                                tempX = (currentAdjacent.getData().getWidth() / 2);
                                tempY = current.getData().getHeight() - 1;
                            } else {
                                tempX = (current.getData().getWidth() / 2);
                                tempY = current.getData().getHeight() - 1;

                            }
                        } else if (current.getData().getYOffset() > currentAdjacent.getData().getYOffset()) { //adj is above

                            if (current.getData().getWidth() > currentAdjacent.getData().getWidth()) {
                                tempX = (currentAdjacent.getData().getWidth() / 2);
                                tempY = 0;
                            } else {
                                tempX = (current.getData().getWidth() / 2);
                                tempY = 0;

                            }

                        }
                    }catch (Exception e){
                        errorText += "TreeMapGenerator.makeEntrances() ERROR 2 -" + e.toString();
                    }
                    current.getData().getTiles()[tempX][tempY] = new EmptyDTile(floorTheme.getFloorSprite(), tempX, tempY);

                }
            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.makeEntrances() ERROR 1 -" + e.toString();
        }
        try {
            for (int k = 0; k < path.size()-1; k++) {
                current = path.elementAt(k);
                currentAdjacent = path.elementAt(k+1);
                if(currentAdjacent.getData().isLocked()){

                    if (current.getData().getXOffset() < currentAdjacent.getData().getXOffset()) { //adj is to right

                        if (current.getData().getHeight() > currentAdjacent.getData().getHeight()) {
                            tempX = current.getData().getWidth() - 1;
                            tempY = (currentAdjacent.getData().getHeight() / 2);
                        } else {
                            tempX = current.getData().getWidth() - 1;
                            tempY = (current.getData().getHeight() / 2);
                        }
                    } else if (current.getData().getXOffset() > currentAdjacent.getData().getXOffset()) { //adj is to left

                        if (current.getData().getHeight() > currentAdjacent.getData().getHeight()) {
                            tempX = 0;
                            tempY = (currentAdjacent.getData().getHeight() / 2);
                        } else {
                            tempX = 0;
                            tempY = (current.getData().getHeight() / 2);

                        }
                    } else if (current.getData().getYOffset() < currentAdjacent.getData().getYOffset()) { //adj is below

                        if (current.getData().getWidth() > currentAdjacent.getData().getWidth()) {
                            tempX = (currentAdjacent.getData().getWidth() / 2);
                            tempY = current.getData().getHeight() - 1;
                        } else {
                            tempX = (current.getData().getWidth() / 2);
                            tempY = current.getData().getHeight() - 1;

                        }
                    } else if (current.getData().getYOffset() > currentAdjacent.getData().getYOffset()) { //adj is above

                        if (current.getData().getWidth() > currentAdjacent.getData().getWidth()) {
                            tempX = (currentAdjacent.getData().getWidth() / 2);
                            tempY = 0;
                        } else {
                            tempX = (current.getData().getWidth() / 2);
                            tempY = 0;

                        }

                    }
                    LockedDoor lockedDoor = new LockedDoor(context,current.getData().getTiles(),player,tempX,tempY);
                    actors.add(lockedDoor);
                }
            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.getTiles() ERROR 3 -" + e.toString();
        }

    }
    private int setStartEnd(int distance){
        int finalDistance;
        startRoom = allRooms.elementAt(rand.nextInt(allRooms.size()));
        endRoom = allRooms.elementAt(rand.nextInt(allRooms.size()));

            finalDistance = findMinDistance(startRoom, endRoom, null, 0);
        try {
            while (finalDistance < distance + 1) {

                endRoom = allRooms.elementAt(rand.nextInt(allRooms.size()));
                finalDistance = findMinDistance(startRoom, endRoom, null, 0);
            }

            int randomX = 0;
            int randomY = 0;
            boolean freespace = false;
            while(!freespace) {
                freespace = true;
                randomX = rand.nextInt(startRoom.getData().getWidth() - 2) + 1;
                randomY = rand.nextInt(startRoom.getData().getHeight() - 2) + 1;
                if(!(startRoom.getData().getTiles()[randomX][randomY] instanceof EmptyDTile)){
                    freespace = false;
                }
                if(freespace) {
                    for (int i = 0; i < startRoom.getData().getEnemySpawnPoints().length; i++) {
                        if (startRoom.getData().getEnemySpawnPoints()[i] == startRoom.getData().getTiles()[randomX][randomY]) {
                            freespace = false;
                            break;
                        }
                    }
                }
                if(freespace){
                    for(int i = 0; i < startRoom.getData().getItemSpawnPoints().length;i++){
                        if(startRoom.getData().getItemSpawnPoints()[i] == startRoom.getData().getTiles()[randomX][randomY]){
                            freespace = false;
                            break;
                        }
                    }
                }

            }

            startRoom.getData().getTiles()[randomX][randomY] = new UpstairsDTile(floorTheme.getUpstairsSprite(), randomX, randomY);
            startX = randomX;
            startY = randomY;

            freespace = false;
            while(!freespace) {
                freespace = true;
                randomX = rand.nextInt(endRoom.getData().getWidth() - 2) + 1;
                randomY = rand.nextInt(endRoom.getData().getHeight() - 2) + 1;
                if(!(endRoom.getData().getTiles()[randomX][randomY] instanceof EmptyDTile)){
                    freespace = false;
                }

                if(freespace){
                    for(int i = 0; i < endRoom.getData().getItemSpawnPoints().length;i++){
                        if(endRoom.getData().getItemSpawnPoints()[i] == endRoom.getData().getTiles()[randomX][randomY]){
                            freespace = false;
                            break;
                        }
                    }
                }

            }


            endRoom.getData().getTiles()[randomX][randomY] = new DownstairsDTile(floorTheme.getDownstairsSprite(), randomX, randomY);
        }catch (Exception e){
            errorText += "TreeMapGenerator.setStartEnd() ERROR 1 -" + e.toString();
        }
        return finalDistance;


    }

    private int findMinDistance(Node start, Node end, Node previous, int startDistance){

        int newDistance = startDistance+1;

        if(start == end){
            return newDistance;
        }

        int minDistance = 999999999;
        int tempDistance;
        try {
            for (int i = 0; i < start.getAdjacents().size(); i++) {
                if (start.getAdjacents().elementAt(i) != previous) {


                    tempDistance = findMinDistance(start.getAdjacents().elementAt(i), end, start, newDistance);

                    if (tempDistance < minDistance) {
                        minDistance = tempDistance;
                    }
                }
            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.findMinDistance() ERROR 1 -" + e.toString();
        }



        return minDistance;

    }
    private Node getMinPath(Node start, Node end,Node previous,Vector<Node> path, int distance ,int minDistance){
        int newDistance = distance+1;

        if(distance > minDistance){
            return null;
        }

        path.add(start);

        if(start == end){
            return start;
        }

        Node dummy;
        try {
            for (int i = 0; i < start.getAdjacents().size(); i++) {
                if (start.getAdjacents().elementAt(i) != previous) {

                    dummy = getMinPath(start.getAdjacents().elementAt(i), end, start, path, newDistance, minDistance);

                    if (dummy == end) {
                        return end;
                    } else if (dummy == null) {
                        if(path.size() != 0) {
                            path.remove(path.size() - 1);
                        }
                        else{
                            i++;
                            i--;
                        }
                    }

                }
            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.getMinPath() ERROR 1 -" + e.toString();
        }


        return null;
    }

    public short compareRoomXY(EmptyRoom a, EmptyRoom b){
        if(a.getXOffset() < b.getXOffset()){ //b is to the right of a
            return 4;
        }
        else if(a.getXOffset() > b.getXOffset()){ //b is to the left of a
            return 3;
        }
        else if(a.getYOffset() < b.getYOffset()){ //b is lower than a
            return 2;
        }
        else if(a.getYOffset() > b.getYOffset()){ //b is higher than a
            return 1;
        }

        return 0;

    }
    private Vector<Node> getAllRooms(){
        Vector<Node> toReturn = new Vector<Node>();
        Node previous;
        toReturn.add(head);
        boolean isPresent;
        try {
            for (int i = 0; i < toReturn.size(); i++) {

                for (int j = 0; j < toReturn.elementAt(i).getAdjacents().size(); j++) {
                    isPresent = false;
                    for (int k = 0; k < toReturn.size(); k++) {
                        if (toReturn.elementAt(i).getAdjacents().elementAt(j) == toReturn.elementAt(k)) {
                            isPresent = true;
                        }
                    }
                    if (!isPresent) {
                        toReturn.add(toReturn.elementAt(i).getAdjacents().elementAt(j));
                    }

                }

            }
        }catch (Exception e){
            errorText += "TreeMapGenerator.getAllRooms() ERROR 1 -" + e.toString();
        }

        return toReturn;

    }
    public Room getStartRoom(){
        return startRoom.getData();
    }

    private class Node{
        private Room data;
        private Vector<Node> adjacents;
        public Node(Room room){
            data = room;
            adjacents = new Vector<Node>();
        }
        public Node(Room room, Node Adjacent){
            data = room;
            adjacents = new Vector<Node>();
            addAdjacent(Adjacent);
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
        public Room getData(){
            return data;
        }
        @Override
        public String toString(){
            return "X=" + data.getXOffset() + " Y=" + data.getYOffset();
        }

    }
    public String allRoomsString(){
        Vector<Node> allRooms = getAllRooms();
        String toReturn = allRooms.size() + "||";
        for(int i = 0; i < allRooms.size(); i++){

            toReturn += i + ": " + allRooms.elementAt(i) + "|";
        }

        return toReturn;
    }

    public Player getPlayer(){
        return player;
    }

    public ScreenXYPositionFinder getScreenXYPositionFinder() {
        return screenXYPosFin;
    }
    public String getErrorText(){
        String temp = errorText;
        errorText = "";
        return temp;
    }
}
