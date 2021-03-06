package additional;

import model.Model;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class FGPlayer {
    private Socket fg;
    private PrintWriter out;
    private double playSpeed, prevPlaySpeed;
    private TimeSeries ts;
    private Model flightSimulatorModel;
    private boolean pause, connectionIsClose, moveForwardIsInProgress, moveRewindIsInProgress;

    public FGPlayer(TimeSeries ts, double speed, Model flightSimulatorModel) {
        connectionIsClose = true;
        this.ts = ts;
        playSpeed = speed;
        this.flightSimulatorModel = flightSimulatorModel;
        moveForwardIsInProgress = false;
        moveRewindIsInProgress = false;
    }

    public boolean isMoveForwardIsInProgress() {
        return moveForwardIsInProgress;
    }

    public void setMoveForwardIsInProgress(boolean val) {
        this.moveForwardIsInProgress = val;
    }

    public boolean isMoveRewindIsInProgress() {
        return moveRewindIsInProgress;
    }

    public void setMoveRewindIsInProgress(boolean val) {
        this.moveRewindIsInProgress = val;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    private boolean createConnection() {
        try {
            fg = new Socket("127.0.0.1", 5400);
            out = new PrintWriter(fg.getOutputStream());
            connectionIsClose = false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized void play(int start) {
        if (connectionIsClose) {
            createConnection();
        }
        try {
            for (int i = start; i < ts.getRowSize(); i++) {
                while (pause) {
                    wait();
                }
                try {
                    out.println(ts.getRowByRowNumber(i));
                    out.flush();
                } catch (Exception e) { }
                flightSimulatorModel.setNumOfRow(i);
                Thread.sleep((long) (flightSimulatorModel.getProperties().getRate()/ playSpeed));
            }
            out.close();
            fg.close();
            connectionIsClose = true;
        } catch (Exception e) { }
    }

    public synchronized void wakeUP() {
        setPause(false);
        notify();
    }

    public void setPlaySpeed(double speed) {
        playSpeed = speed;
    }

    public double getPlaySpeed() {
        return playSpeed;
    }

    public void forward() {
        setMoveForwardIsInProgress(true);
        prevPlaySpeed = getPlaySpeed();
        setPlaySpeed(3.5);
    }

    public void rewind(int start) {
        if (connectionIsClose) {
            createConnection();
        }
        try {
            for (int i = start; i >= 0; i--) {
                try {
                    out.println(ts.getRowByRowNumber(i));
                    out.flush();
                } catch (Exception e) {
                }
                flightSimulatorModel.setNumOfRow(i);
                Thread.sleep((long) (flightSimulatorModel.getProperties().getRate() / 3.5));
            }
            stop();
        }
        catch (Exception e) { }
    }

    public void unForward() {
        if (isMoveForwardIsInProgress()) {
            setPlaySpeed(prevPlaySpeed);
            setMoveForwardIsInProgress(false);
        }
    }

    private void stop() {
        try {
            out.close();
            fg.close();
            connectionIsClose = true;
        }
        catch (IOException e) { }
    }
}
