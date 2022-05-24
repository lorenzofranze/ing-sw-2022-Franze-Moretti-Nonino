package it.polimi.ingsw.client.View;

import com.sun.glass.ui.*;
import it.polimi.ingsw.client.Controller.ClientController;
import it.polimi.ingsw.common.gamePojo.GameStatePojo;
import it.polimi.ingsw.common.messages.*;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class GUIView extends Application implements View{

    

    void beginUsername(MouseEvent event){
        Stage startWindow= (Stage) tfTitle.getScene().getWindow();
        String title=
    }
    @Override
    public void beginReadUsername() {
        {
            ClientController clientController = ClientController.getInstance();
            System.out.print("INSERT NICKNAME (at least 4 characters): ");
            boolean valid;
            String result;
            do {
                valid = true;
                result = scanner.nextLine();
                if (result.length() < 4 ) {
                    System.out.println("You must insert a nickname of at least 4 characters.");
                    System.out.print("INSERT NICKNAME (at least 4 characters): ");
                    valid = false;
                }
            } while (!valid);
            clientController.setNickname(result);
        }
    }

    @Override
    public synchronized void chooseGameMode() {
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
}
