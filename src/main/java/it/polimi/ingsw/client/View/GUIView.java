package it.polimi.ingsw.client.View;

import com.sun.glass.ui.*;
import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.*;

import it.polimi.ingsw.server.controller.logic.GameMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class GUIView implements View{

    private Stage currentStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("startFrame"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    private void showScene(String nameFileFxml){
        Parent root=null;
        try {
            root= FXMLLoader.load(getClass().getResource("startFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentStage.setScene(new Scene(root,800,500));
        currentStage.show();
    }

    @Override
    public void startScreen(){
        showScene("startFrame.fxml");
    }


    @Override
    public synchronized void chooseGameMode() {
        ClientController clientController = ClientController.getInstance();
        showScene("chooseGameModeFrame.fxml");


        System.out.println("\nTHESE ARE THE POSSIBLE GAME MODES:");
        System.out.println("1. 2 players simple\n" + "2. 3 players simple\n" + "3. 2 players complex\n" + "4. 3 players complex");
        System.out.print("\nCHOOSE THE GAME MODE: ");
        boolean valid;
        int result = 0;
        do {
            valid = true;

        } while (!valid);
        clientController.setGameMode(GameMode.values()[result-1]);
    }


    @Override
    public void beginReadUsername() {

    }

    @Override
    public void chooseAssistantCard() {

    }

    @Override
    public void askForCharacter() {

    }

    @Override
    public void moveStudent() {

    }

    @Override
    public void placeMotherNature() {

    }

    @Override
    public void chooseCloud() {

    }

    @Override
    public void showMessage(Message message) {

    }

    @Override
    public void showConnection(ConnectionMessage connectionMessage) {

    }

    @Override
    public void showError(ErrorMessage errorMessage) {

    }

    @Override
    public void showAck(AckMessage ackMessage) {

    }

    @Override
    public void showUpdate(UpdateMessage updateMessage) {

    }

    @Override
    public void showAsync(AsyncMessage asyncMessage) {

    }

    @Override
    public void showPing(PingMessage pingMessage) {

    }

    @Override
    public void showMove(GameMessage gameMessage) {

    }

    @Override
    public void showGameState(GameStatePojo gameStatePojo) {

    }

    @Override
    public void moveStudentToIsland() {

    }

    @Override
    public void chooseColour() {

    }

    @Override
    public void chooseIsland() {

    }

    @Override
    public void chooseNumOfMove() {

    }

    @Override
    protected void runLoop(Runnable runnable) {

    }

    @Override
    protected void _invokeAndWait(Runnable runnable) {

    }

    @Override
    protected void _invokeLater(Runnable runnable) {

    }

    @Override
    protected Object _enterNestedEventLoop() {
        return null;
    }

    @Override
    protected void _leaveNestedEventLoop(Object o) {

    }

    @Override
    public Window createWindow(Window window, Screen screen, int i) {
        return null;
    }

    @Override
    public com.sun.glass.ui.View createView() {
        return null;
    }

    @Override
    public Cursor createCursor(int i) {
        return null;
    }

    @Override
    public Cursor createCursor(int i, int i1, Pixels pixels) {
        return null;
    }

    @Override
    protected void staticCursor_setVisible(boolean b) {

    }

    @Override
    protected Size staticCursor_getBestSize(int i, int i1) {
        return null;
    }

    @Override
    public Pixels createPixels(int i, int i1, ByteBuffer byteBuffer) {
        return null;
    }

    @Override
    public Pixels createPixels(int i, int i1, IntBuffer intBuffer) {
        return null;
    }

    @Override
    public Pixels createPixels(int i, int i1, IntBuffer intBuffer, float v, float v1) {
        return null;
    }

    @Override
    protected int staticPixels_getNativeFormat() {
        return 0;
    }

    @Override
    public GlassRobot createRobot() {
        return null;
    }

    @Override
    protected double staticScreen_getVideoRefreshPeriod() {
        return 0;
    }

    @Override
    protected Screen[] staticScreen_getScreens() {
        return new Screen[0];
    }

    @Override
    public Timer createTimer(Runnable runnable) {
        return null;
    }

    @Override
    protected int staticTimer_getMinPeriod() {
        return 0;
    }

    @Override
    protected int staticTimer_getMaxPeriod() {
        return 0;
    }

    @Override
    protected CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window window, String s, String s1, String s2, int i, boolean b, CommonDialogs.ExtensionFilter[] extensionFilters, int i1) {
        return null;
    }

    @Override
    protected File staticCommonDialogs_showFolderChooser(Window window, String s, String s1) {
        return null;
    }

    @Override
    protected long staticView_getMultiClickTime() {
        return 0;
    }

    @Override
    protected int staticView_getMultiClickMaxX() {
        return 0;
    }

    @Override
    protected int staticView_getMultiClickMaxY() {
        return 0;
    }

    @Override
    protected boolean _supportsTransparentWindows() {
        return false;
    }

    @Override
    protected boolean _supportsUnifiedWindows() {
        return false;
    }

    @Override
    protected int _getKeyCodeForChar(char c) {
        return 0;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}
